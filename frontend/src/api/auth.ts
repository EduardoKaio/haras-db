import { http } from "./http";

export interface LoginInput {
  email: string;
  senha: string;
}

export interface LoginResponse {
  token: string;
  idPessoa: number;
  email: string;
  roles: string[];
}

export const authApi = {
  login: (data: LoginInput) => http.post<LoginResponse>("/auth/login", data),
};
