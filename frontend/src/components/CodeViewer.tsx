import { useQuery } from '@tanstack/react-query';
import { FileCode } from 'lucide-react';
import Editor from '@monaco-editor/react';
import api from '@/lib/api';
import type { FileContent } from '@/types';

interface CodeViewerProps {
  projectId: number;
  filePath: string | null;
}

function getLanguageFromPath(path: string): string {
  const ext = path.split('.').pop()?.toLowerCase() ?? '';
  const languageMap: Record<string, string> = {
    ts: 'typescript',
    tsx: 'typescript',
    js: 'javascript',
    jsx: 'javascript',
    json: 'json',
    html: 'html',
    css: 'css',
    scss: 'scss',
    md: 'markdown',
    yaml: 'yaml',
    yml: 'yaml',
    xml: 'xml',
    java: 'java',
    py: 'python',
    rb: 'ruby',
    go: 'go',
    rs: 'rust',
    sql: 'sql',
    sh: 'shell',
    bash: 'shell',
    dockerfile: 'dockerfile',
    toml: 'ini',
    env: 'ini',
    gitignore: 'plaintext',
  };
  return languageMap[ext] ?? 'plaintext';
}

export default function CodeViewer({ projectId, filePath }: CodeViewerProps) {
  const { data, isLoading } = useQuery<FileContent>({
    queryKey: ['fileContent', projectId, filePath],
    queryFn: () =>
      api.get(`/api/projects/${projectId}/files/${filePath}`).then((r) => r.data),
    enabled: !!filePath,
  });

  if (!filePath) {
    return (
      <div className="h-full flex items-center justify-center text-base-content/40 bg-base-200/30">
        <div className="text-center">
          <FileCode className="w-12 h-12 mx-auto mb-2 opacity-30" />
          <p className="text-sm">Select a file to view its contents</p>
        </div>
      </div>
    );
  }

  if (isLoading) {
    return (
      <div className="h-full flex items-center justify-center bg-[#1e1e1e]">
        <span className="loading loading-spinner loading-md text-base-content/50" />
      </div>
    );
  }

  const language = getLanguageFromPath(filePath);

  return (
    <div className="h-full flex flex-col">
      <div className="px-4 py-2 border-b border-base-300 bg-[#252526] text-xs font-mono text-base-content/70 flex items-center gap-2">
        <FileCode className="w-3.5 h-3.5" />
        {filePath}
      </div>
      <div className="flex-1">
        <Editor
          height="100%"
          language={language}
          value={data?.content ?? ''}
          theme="vs-dark"
          options={{
            readOnly: true,
            minimap: { enabled: true },
            fontSize: 13,
            lineNumbers: 'on',
            scrollBeyondLastLine: false,
            wordWrap: 'on',
            automaticLayout: true,
            padding: { top: 12 },
            renderLineHighlight: 'gutter',
            scrollbar: {
              verticalScrollbarSize: 8,
              horizontalScrollbarSize: 8,
            },
          }}
        />
      </div>
    </div>
  );
}
