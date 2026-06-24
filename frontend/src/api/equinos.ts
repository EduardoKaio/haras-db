import { http } from "./http";

export interface Equino {
  idEquino: number;
  nome: string;
  raca: string;
  peso: number;
  funcao: string;
  dataNascimento: string;
  status: string;
  registro: string;
  registroPai: string | null;
  registroMae: string | null;
  pelagem: string;
}

export interface EquinoInput {
  nome: string;
  raca: string;
  peso: number;
  funcao: string;
  dataNascimento: string;
  status: string;
  registro: string;
  registroPai: string | null;
  registroMae: string | null;
  pelagem: string;
}

export const equinosApi = {
  listar: () => http.get<Equino[]>("/equinos"),
  buscar: (id: number) => http.get<Equino>(`/equinos/${id}`),
  criar: (data: EquinoInput) => http.post<Equino>("/equinos", data),
  atualizar: (id: number, data: EquinoInput) => http.put<Equino>(`/equinos/${id}`, data),
  excluir: (id: number) => http.delete(`/equinos/${id}`),
};
