import { useQuery } from '@tanstack/react-query';
import { File, Folder, FolderOpen, RefreshCw } from 'lucide-react';
import { useState } from 'react';
import api from '@/lib/api';
import type { FileNode as BackendFileNode } from '@/types';

interface TreeNode {
  name: string;
  path: string;
  isDirectory: boolean;
  children: TreeNode[];
}

interface FileTreeProps {
  projectId: number;
  onSelect: (path: string) => void;
}

function buildTree(paths: string[]): TreeNode[] {
  const root: TreeNode[] = [];

  for (const filePath of paths) {
    const parts = filePath.split('/');
    let current = root;

    for (let i = 0; i < parts.length; i++) {
      const part = parts[i]!;
      const isLast = i === parts.length - 1;
      const existing = current.find((n) => n.name === part);

      if (existing) {
        current = existing.children;
      } else {
        const node: TreeNode = {
          name: part,
          path: parts.slice(0, i + 1).join('/'),
          isDirectory: !isLast,
          children: [],
        };
        current.push(node);
        current = node.children;
      }
    }
  }

  return root;
}

export default function FileTree({ projectId, onSelect }: FileTreeProps) {
  const { data: files, isLoading, refetch } = useQuery<BackendFileNode[]>({
    queryKey: ['fileTree', projectId],
    queryFn: () => api.get(`/api/projects/${projectId}/files`).then((r) => r.data),
  });

  const tree = files ? buildTree(files.map((f) => f.path)) : [];

  return (
    <div className="p-2">
      <div className="flex items-center justify-between mb-2 px-1">
        <span className="text-xs font-semibold uppercase text-base-content/50">Files</span>
        <button className="btn btn-ghost btn-xs" onClick={() => refetch()}>
          <RefreshCw className="w-3 h-3" />
        </button>
      </div>

      {isLoading ? (
        <div className="flex justify-center p-4">
          <span className="loading loading-spinner loading-sm" />
        </div>
      ) : tree.length > 0 ? (
        <ul className="menu menu-xs w-full">
          {tree.map((node) => (
            <TreeNodeItem key={node.path} node={node} onSelect={onSelect} />
          ))}
        </ul>
      ) : (
        <p className="text-xs text-base-content/40 px-1">No files yet</p>
      )}
    </div>
  );
}

function TreeNodeItem({ node, onSelect }: { node: TreeNode; onSelect: (path: string) => void }) {
  const [open, setOpen] = useState(false);

  if (node.isDirectory) {
    return (
      <li>
        <details open={open} onToggle={(e) => setOpen((e.target as HTMLDetailsElement).open)}>
          <summary className="flex items-center gap-1">
            {open ? <FolderOpen className="w-3 h-3" /> : <Folder className="w-3 h-3" />}
            <span className="truncate">{node.name}</span>
          </summary>
          <ul>
            {node.children.map((child) => (
              <TreeNodeItem key={child.path} node={child} onSelect={onSelect} />
            ))}
          </ul>
        </details>
      </li>
    );
  }

  return (
    <li>
      <button className="flex items-center gap-1" onClick={() => onSelect(node.path)}>
        <File className="w-3 h-3" />
        <span className="truncate">{node.name}</span>
      </button>
    </li>
  );
}
