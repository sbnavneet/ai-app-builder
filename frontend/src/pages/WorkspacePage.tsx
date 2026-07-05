import { useState } from 'react';
import { useParams } from 'react-router-dom';
import ChatPanel from '@/components/ChatPanel';
import FileTree from '@/components/FileTree';
import CodeViewer from '@/components/CodeViewer';

export default function WorkspacePage() {
  const { projectId } = useParams<{ projectId: string }>();
  const [selectedFile, setSelectedFile] = useState<string | null>(null);

  if (!projectId) return null;

  return (
    <div className="h-screen flex flex-col">
      <div className="navbar bg-base-100 border-b border-base-300 px-4 min-h-0 h-12">
        <span className="text-sm font-semibold">Project #{projectId}</span>
      </div>

      <div className="flex flex-1 overflow-hidden">
        {/* Chat Panel - Left */}
        <div className="w-[400px] border-r border-base-300 flex flex-col">
          <ChatPanel projectId={Number(projectId)} onFileClick={setSelectedFile} />
        </div>

        {/* File Tree - Middle */}
        <div className="w-56 border-r border-base-300 overflow-y-auto bg-base-200">
          <FileTree projectId={Number(projectId)} onSelect={setSelectedFile} />
        </div>

        {/* Code Viewer - Right */}
        <div className="flex-1 overflow-hidden">
          <CodeViewer projectId={Number(projectId)} filePath={selectedFile} />
        </div>
      </div>
    </div>
  );
}
