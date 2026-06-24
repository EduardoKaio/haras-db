import { http } from "./http";

export interface Colaborador {
  idPessoa: number;
  nomePessoa: string;
  emailPessoa: string;
}

export const colaboradoresApi = {
  listar: () => http.get<Colaborador[]>("/colaboradores"),
  buscar: (idPessoa: number) => http.get<Colaborador>(`/colaboradores/${idPessoa}`),
  criar: (idPessoa: number) => http.post<Colaborador>("/colaboradores", { idPessoa }),
  excluir: (idPessoa: number) => http.delete(`/colaboradores/${idPessoa}`),
};
