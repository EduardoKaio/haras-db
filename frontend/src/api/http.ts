const BASE_URL = "http://localhost:8080/api";

const TOKEN_KEY = "haras.token";

// Token mantido em memória (inicializado do localStorage para sobreviver a refresh).
let authToken: string | null = localStorage.getItem(TOKEN_KEY);
// Callback chamado quando a API responde 401 fora do fluxo de login (token expirado/inválido).
let unauthorizedHandler: (() => void) | null = null;

export function setAuthToken(token: string | null) {
  authToken = token;
}

export function setUnauthorizedHandler(handler: (() => void) | null) {
  unauthorizedHandler = handler;
}

export class ApiError extends Error {
  status: number;
  fieldErrors?: Record<string, string>;

  constructor(status: number, message: string, fieldErrors?: Record<string, string>) {
    super(message);
    this.status = status;
    this.fieldErrors = fieldErrors;
  }
}

async function request<T>(path: string, options?: RequestInit): Promise<T> {
  const headers: Record<string, string> = {
    "Content-Type": "application/json",
    ...(options?.headers as Record<string, string> | undefined),
  };
  if (authToken) {
    headers.Authorization = `Bearer ${authToken}`;
  }

  const response = await fetch(`${BASE_URL}${path}`, { ...options, headers });

  if (response.status === 204) {
    return undefined as T;
  }

  const body = await response.json().catch(() => undefined);

  if (!response.ok) {
    // 401 em rota autenticada => sessão perdida: dispara o handler (logout + redirect).
    // O próprio login (/auth/*) trata seu 401 exibindo "Credenciais inválidas".
    if (response.status === 401 && !path.startsWith("/auth/")) {
      unauthorizedHandler?.();
    }
    if (response.status === 400 && body && typeof body === "object") {
      throw new ApiError(400, "Dados inválidos", body as Record<string, string>);
    }
    const message = (body && body.message) || `Erro ${response.status}`;
    throw new ApiError(response.status, message);
  }

  return body as T;
}

export const http = {
  get: <T,>(path: string) => request<T>(path),
  post: <T,>(path: string, data: unknown) => request<T>(path, { method: "POST", body: JSON.stringify(data) }),
  put: <T,>(path: string, data: unknown) => request<T>(path, { method: "PUT", body: JSON.stringify(data) }),
  delete: (path: string) => request<void>(path, { method: "DELETE" }),
};
