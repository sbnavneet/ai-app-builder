export interface UserProfile {
  id: number;
  name: string;
  email: string;
}

export interface AuthResponse {
  token: string;
  refreshToken: string;
  user: UserProfile;
}

export interface ProjectSummary {
  id: number;
  name: string;
  createdAt: string;
  updatedAt: string;
}

export interface Project {
  id: number;
  name: string;
  createdAt: string;
  updatedAt: string;
}

export interface FileNode {
  path: string;
}

export interface FileContent {
  path: string;
  content: string;
}

export interface ChatMessage {
  id: number;
  role: 'USER' | 'ASSISTANT';
  content: string;
  tokensUsed: number;
  createdAt: string;
  events: ChatEvent[];
}

export interface ChatEvent {
  id: number;
  type: 'MESSAGE' | 'FILE_EDIT' | 'TOOL_LOG' | 'THOUGHT';
  sequenceOrder: number;
  content: string;
  filePath: string | null;
  metadata: string | null;
}

export interface StreamChunk {
  text: string;
}
