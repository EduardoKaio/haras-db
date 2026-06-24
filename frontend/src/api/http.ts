const BASE_URL = "http://localhost:8080/api";

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
  const response = await fetch(`${BASE_URL}${path}`, {
    headers: { "Content-Type": "application/json" },
    ...options,
  });

  if (response.status === 204) {
    return undefined as T;
  }

  const body = await response.json().catch(() => undefined);

  if (!response.ok) {
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
