export interface PingResponse {
  status: string;
  dbTime: string;
  pessoaCount: number;
}

export async function fetchPing(): Promise<PingResponse> {
  const response = await fetch("http://localhost:8080/api/ping");
  if (!response.ok) {
    throw new Error(`Request failed with status ${response.status}`);
  }
  return response.json();
}
