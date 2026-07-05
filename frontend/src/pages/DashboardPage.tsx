import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query';
import { Plus, FolderOpen, LogOut, Trash2 } from 'lucide-react';
import api from '@/lib/api';
import { useAuthStore } from '@/store/authStore';
import type { ProjectSummary } from '@/types';

export default function DashboardPage() {
  const navigate = useNavigate();
  const logout = useAuthStore((s) => s.logout);
  const queryClient = useQueryClient();
  const [showCreate, setShowCreate] = useState(false);
  const [newName, setNewName] = useState('');

  const { data: projects, isLoading } = useQuery<ProjectSummary[]>({
    queryKey: ['projects'],
    queryFn: () => api.get('/api/projects').then((r) => r.data),
  });

  const createMutation = useMutation({
    mutationFn: (data: { name: string }) =>
      api.post('/api/projects', data).then((r) => r.data),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['projects'] });
      setShowCreate(false);
      setNewName('');
    },
  });

  const deleteMutation = useMutation({
    mutationFn: (id: number) => api.delete(`/api/projects/${id}`),
    onSuccess: () => queryClient.invalidateQueries({ queryKey: ['projects'] }),
  });

  const handleLogout = () => {
    queryClient.clear();
    logout();
    navigate('/login');
  };

  return (
    <div className="min-h-screen bg-base-200">
      <div className="navbar bg-base-100 shadow-sm px-6">
        <div className="flex-1">
          <span className="text-xl font-bold">AI App Builder</span>
        </div>
        <div className="flex-none">
          <button className="btn btn-ghost btn-sm" onClick={handleLogout}>
            <LogOut className="w-4 h-4" />
            Logout
          </button>
        </div>
      </div>

      <div className="container mx-auto p-6 max-w-5xl">
        <div className="flex justify-between items-center mb-6">
          <h1 className="text-2xl font-bold">My Projects</h1>
          <button className="btn btn-primary btn-sm" onClick={() => setShowCreate(true)}>
            <Plus className="w-4 h-4" />
            New Project
          </button>
        </div>

        {showCreate && (
          <div className="card bg-base-100 shadow mb-6">
            <div className="card-body">
              <h3 className="font-semibold mb-2">Create Project</h3>
              <form
                onSubmit={(e) => {
                  e.preventDefault();
                  createMutation.mutate({ name: newName });
                }}
                className="space-y-3"
              >
                <input
                  type="text"
                  className="input input-bordered w-full"
                  placeholder="Project name"
                  value={newName}
                  onChange={(e) => setNewName(e.target.value)}
                  required
                />
                <div className="flex gap-2">
                  <button type="submit" className="btn btn-primary btn-sm" disabled={createMutation.isPending}>
                    {createMutation.isPending ? <span className="loading loading-spinner loading-xs" /> : 'Create'}
                  </button>
                  <button type="button" className="btn btn-ghost btn-sm" onClick={() => setShowCreate(false)}>
                    Cancel
                  </button>
                </div>
              </form>
            </div>
          </div>
        )}

        {isLoading ? (
          <div className="flex justify-center p-12">
            <span className="loading loading-spinner loading-lg" />
          </div>
        ) : projects && projects.length > 0 ? (
          <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4">
            {projects.map((project) => (
              <div key={project.id} className="card bg-base-100 shadow hover:shadow-md transition-shadow">
                <div className="card-body">
                  <h3 className="card-title text-base">{project.name}</h3>
                  <p className="text-sm text-base-content/60">
                    Created {new Date(project.createdAt).toLocaleDateString()}
                  </p>
                  <div className="card-actions justify-end mt-2">
                    <button
                      className="btn btn-ghost btn-xs text-error"
                      onClick={() => deleteMutation.mutate(project.id)}
                    >
                      <Trash2 className="w-3 h-3" />
                    </button>
                    <button
                      className="btn btn-primary btn-xs"
                      onClick={() => navigate(`/project/${project.id}`)}
                    >
                      <FolderOpen className="w-3 h-3" />
                      Open
                    </button>
                  </div>
                </div>
              </div>
            ))}
          </div>
        ) : (
          <div className="text-center py-12 text-base-content/50">
            <p>No projects yet. Create one to get started.</p>
          </div>
        )}
      </div>
    </div>
  );
}
