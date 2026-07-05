import { useState, useRef, useEffect } from 'react';
import { Send, Sparkles, User, FileCode, Wrench, Brain, Loader2 } from 'lucide-react';
import { useAuthStore } from '@/store/authStore';
import api from '@/lib/api';
import type { ChatMessage, ChatEvent } from '@/types';

interface ChatPanelProps {
  projectId: number;
  onFileClick?: (path: string) => void;
}

export default function ChatPanel({ projectId, onFileClick }: ChatPanelProps) {
  const [messages, setMessages] = useState<ChatMessage[]>([]);
  const [streamingContent, setStreamingContent] = useState('');
  const [streamingStatus, setStreamingStatus] = useState('');
  const [input, setInput] = useState('');
  const [streaming, setStreaming] = useState(false);
  const [loadingHistory, setLoadingHistory] = useState(true);
  const messagesEndRef = useRef<HTMLDivElement>(null);
  const token = useAuthStore((s) => s.accessToken);

  useEffect(() => {
    const loadHistory = async () => {
      try {
        const res = await api.get(`/api/projects/${projectId}/chat`);
        setMessages(res.data);
      } catch {
        // No history yet
      } finally {
        setLoadingHistory(false);
      }
    };
    loadHistory();
  }, [projectId]);

  useEffect(() => {
    messagesEndRef.current?.scrollIntoView({ behavior: 'smooth' });
  }, [messages, streamingContent]);

  const handleSend = async () => {
    if (!input.trim() || streaming) return;

    const userMessage = input.trim();
    setInput('');

    const userMsg: ChatMessage = {
      id: Date.now(),
      role: 'USER',
      content: userMessage,
      tokensUsed: 0,
      createdAt: new Date().toISOString(),
      events: [],
    };
    setMessages((prev) => [...prev, userMsg]);
    setStreaming(true);
    setStreamingContent('');
    setStreamingStatus('Thinking...');

    try {
      const response = await fetch('/api/chat/stream', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
          Authorization: `Bearer ${token}`,
        },
        body: JSON.stringify({ message: userMessage, projectId }),
      });

      if (!response.ok) throw new Error('Stream failed');

      const reader = response.body?.getReader();
      const decoder = new TextDecoder();
      if (!reader) throw new Error('No reader');

      let buffer = '';
      let fullText = '';
      setStreamingStatus('Generating...');

      while (true) {
        const { done, value } = await reader.read();
        if (done) break;

        buffer += decoder.decode(value, { stream: true });
        const lines = buffer.split('\n');
        buffer = lines.pop() ?? '';

        for (const line of lines) {
          if (line.startsWith('data:')) {
            const jsonStr = line.slice(5).trim();
            if (!jsonStr) continue;
            try {
              const data = JSON.parse(jsonStr) as { text: string };
              if (data.text) {
                fullText += data.text;
                // Parse events incrementally so UI shows structured view
                setStreamingContent(fullText);
              }
            } catch {
              // skip
            }
          }
        }
      }

      const assistantMsg: ChatMessage = {
        id: Date.now() + 1,
        role: 'ASSISTANT',
        content: fullText,
        tokensUsed: 0,
        createdAt: new Date().toISOString(),
        events: parseEventsFromRawText(fullText),
      };
      setMessages((prev) => [...prev, assistantMsg]);
      setStreamingContent('');
    } catch (err) {
      console.error('Stream error:', err);
      setStreamingContent('');
      const errorMsg: ChatMessage = {
        id: Date.now() + 1,
        role: 'ASSISTANT',
        content: 'Something went wrong. Please try again.',
        tokensUsed: 0,
        createdAt: new Date().toISOString(),
        events: [],
      };
      setMessages((prev) => [...prev, errorMsg]);
    } finally {
      setStreaming(false);
      setStreamingStatus('');
    }
  };

  return (
    <div className="flex flex-col h-full bg-base-100">
      {/* Header */}
      <div className="px-4 py-3 border-b border-base-300 flex items-center gap-2">
        <Sparkles className="w-4 h-4 text-primary" />
        <span className="font-semibold text-sm">AI Chat</span>
      </div>

      {/* Messages Area */}
      <div className="flex-1 overflow-y-auto">
        {loadingHistory ? (
          <div className="flex flex-col items-center justify-center h-full gap-2 text-base-content/40">
            <Loader2 className="w-5 h-5 animate-spin" />
            <span className="text-xs">Loading conversation...</span>
          </div>
        ) : messages.length === 0 && !streaming ? (
          <div className="flex flex-col items-center justify-center h-full px-6 text-center">
            <div className="w-12 h-12 rounded-full bg-primary/10 flex items-center justify-center mb-3">
              <Sparkles className="w-6 h-6 text-primary" />
            </div>
            <p className="text-sm font-medium text-base-content/70">What would you like to build?</p>
            <p className="text-xs text-base-content/40 mt-1">Describe your app and I'll generate the code</p>
          </div>
        ) : (
          <div className="px-4 py-4 space-y-6">
            {messages.map((msg) => (
              <MessageBlock key={msg.id} message={msg} onFileClick={onFileClick} />
            ))}

            {/* Streaming indicator */}
            {streaming && (
              <div className="space-y-2">
                <div className="flex items-start gap-3">
                  <div className="w-7 h-7 rounded-full bg-primary/10 flex items-center justify-center shrink-0 mt-0.5">
                    <Sparkles className="w-3.5 h-3.5 text-primary" />
                  </div>
                  <div className="flex-1 min-w-0 space-y-2">
                    {/* Show completed events parsed so far */}
                    {parseEventsFromRawText(streamingContent).map((event) => (
                      <EventBlock key={event.id} event={event} onFileClick={onFileClick} />
                    ))}
                    {/* Working indicator */}
                    <div className="flex items-center gap-2 py-1">
                      <div className="flex gap-1">
                        <span className="w-1.5 h-1.5 rounded-full bg-primary animate-bounce [animation-delay:0ms]" />
                        <span className="w-1.5 h-1.5 rounded-full bg-primary animate-bounce [animation-delay:150ms]" />
                        <span className="w-1.5 h-1.5 rounded-full bg-primary animate-bounce [animation-delay:300ms]" />
                      </div>
                      <span className="text-xs text-base-content/50">{streamingStatus}</span>
                    </div>
                  </div>
                </div>
              </div>
            )}

            <div ref={messagesEndRef} />
          </div>
        )}
      </div>

      {/* Input Area */}
      <div className="border-t border-base-300 p-3">
        <form
          onSubmit={(e) => {
            e.preventDefault();
            handleSend();
          }}
          className="relative"
        >
          <input
            type="text"
            className="input input-bordered w-full pr-12 text-sm h-11 rounded-xl bg-base-200/50 focus:bg-base-100"
            placeholder="Describe what you want to build..."
            value={input}
            onChange={(e) => setInput(e.target.value)}
            disabled={streaming}
          />
          <button
            type="submit"
            className="absolute right-2 top-1/2 -translate-y-1/2 btn btn-primary btn-sm btn-circle"
            disabled={streaming || !input.trim()}
          >
            {streaming ? (
              <Loader2 className="w-4 h-4 animate-spin" />
            ) : (
              <Send className="w-4 h-4" />
            )}
          </button>
        </form>
      </div>
    </div>
  );
}

function MessageBlock({
  message,
  onFileClick,
}: {
  message: ChatMessage;
  onFileClick?: (path: string) => void;
}) {
  if (message.role === 'USER') {
    return (
      <div className="flex items-start gap-3 justify-end">
        <div className="max-w-[85%] bg-primary text-primary-content px-4 py-2.5 rounded-2xl rounded-tr-sm">
          <p className="text-sm leading-relaxed">{message.content}</p>
        </div>
        <div className="w-7 h-7 rounded-full bg-base-300 flex items-center justify-center shrink-0 mt-0.5">
          <User className="w-3.5 h-3.5" />
        </div>
      </div>
    );
  }

  // Assistant — use events from backend, or parse them from content as fallback
  let events = message.events;
  if (!events || events.length === 0) {
    events = parseEventsFromRawText(message.content);
  }

  return (
    <div className="flex items-start gap-3">
      <div className="w-7 h-7 rounded-full bg-primary/10 flex items-center justify-center shrink-0 mt-0.5">
        <Sparkles className="w-3.5 h-3.5 text-primary" />
      </div>
      <div className="flex-1 min-w-0 space-y-2">
        {events.length > 0 ? (
          events.map((event) => (
            <EventBlock key={event.id} event={event} onFileClick={onFileClick} />
          ))
        ) : (
          <div className="text-sm text-base-content/90 leading-relaxed">
            <pre className="whitespace-pre-wrap font-sans">{message.content}</pre>
          </div>
        )}
      </div>
    </div>
  );
}

function EventBlock({
  event,
  onFileClick,
}: {
  event: ChatEvent;
  onFileClick?: (path: string) => void;
}) {
  switch (event.type) {
    case 'MESSAGE':
      return (
        <div className="text-sm text-base-content/90 leading-relaxed">
          <pre className="whitespace-pre-wrap font-sans">{event.content}</pre>
        </div>
      );

    case 'FILE_EDIT':
      return (
        <button
          onClick={() => event.filePath && onFileClick?.(event.filePath)}
          className="flex items-center gap-2.5 w-full px-3 py-2 rounded-lg border border-success/20 bg-success/5 hover:bg-success/10 transition-colors text-left group"
        >
          <div className="w-6 h-6 rounded bg-success/15 flex items-center justify-center shrink-0">
            <FileCode className="w-3.5 h-3.5 text-success" />
          </div>
          <div className="flex-1 min-w-0">
            <p className="text-xs font-mono text-success truncate">{event.filePath}</p>
            <p className="text-[10px] text-base-content/40">File created/updated</p>
          </div>
          <span className="text-[10px] text-base-content/30 group-hover:text-base-content/50 transition-colors">
            click to view →
          </span>
        </button>
      );

    case 'TOOL_LOG':
      return (
        <div className="flex items-center gap-2 px-3 py-1.5 rounded-md bg-base-200/60">
          <Wrench className="w-3 h-3 text-warning shrink-0" />
          <span className="text-[11px] font-mono text-base-content/50 truncate">
            {event.content}
          </span>
          {event.metadata && (
            <span className="text-[10px] text-base-content/30 ml-auto font-mono truncate">
              {event.metadata}
            </span>
          )}
        </div>
      );

    case 'THOUGHT':
      return (
        <div className="flex items-center gap-1.5 py-0.5">
          <Brain className="w-3 h-3 text-info/50" />
          <span className="text-[11px] text-base-content/40 italic">{event.content}</span>
        </div>
      );

    default:
      return null;
  }
}

function parseEventsFromRawText(text: string): ChatEvent[] {
  const events: ChatEvent[] = [];
  const tagPattern = /<(message|file|tool)([^>]*)>([\s\S]*?)<\/\1>/gi;
  let match: RegExpExecArray | null;
  let order = 1;

  while ((match = tagPattern.exec(text)) !== null) {
    const tagName = match[1]!.toLowerCase();
    const attrs = match[2] ?? '';
    const content = match[3]!.trim();

    const pathMatch = attrs.match(/path="([^"]+)"/);
    const argsMatch = attrs.match(/args="([^"]+)"/);

    let type: ChatEvent['type'] = 'MESSAGE';
    let filePath: string | null = null;
    let metadata: string | null = null;

    if (tagName === 'file') {
      type = 'FILE_EDIT';
      filePath = pathMatch?.[1] ?? null;
    } else if (tagName === 'tool') {
      type = 'TOOL_LOG';
      metadata = argsMatch?.[1] ?? null;
    }

    events.push({
      id: order,
      type,
      sequenceOrder: order,
      content,
      filePath,
      metadata,
    });
    order++;
  }

  return events;
}
