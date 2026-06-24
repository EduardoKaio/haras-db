import { http } from "./http";
import type { Equino } from "./equinos";

export interface Proprietario {
  idPessoa: number;
  nomePessoa: string;
  emailPessoa: string;
}

export const proprietariosApi = {
  listar: () => http.get<Proprietario[]>("/proprietarios"),
  buscar: (idPessoa: number) => http.get<Proprietario>(`/proprietarios/${idPessoa}`),
  criar: (idPessoa: number) => http.post<Proprietario>("/proprietarios", { idPessoa }),
  excluir: (idPessoa: number) => http.delete(`/proprietarios/${idPessoa}`),
  listarEquinos: (idPessoa: number) => http.get<Equino[]>(`/proprietarios/${idPessoa}/equinos`),
  vincularEquino: (idPessoa: number, idEquino: number) =>
    http.post<void>(`/proprietarios/${idPessoa}/equinos`, { idEquino }),
  desvincularEquino: (idPessoa: number, idEquino: number) =>
    http.delete(`/proprietarios/${idPessoa}/equinos/${idEquino}`),
};
