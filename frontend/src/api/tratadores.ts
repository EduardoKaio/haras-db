import { http } from "./http";

export interface Tratador {
  idPessoa: number;
  nomePessoa: string;
  emailPessoa: string;
}

export const tratadoresApi = {
  listar: () => http.get<Tratador[]>("/tratadores"),
  buscar: (idPessoa: number) => http.get<Tratador>(`/tratadores/${idPessoa}`),
  criar: (idPessoa: number) => http.post<Tratador>("/tratadores", { idPessoa }),
  excluir: (idPessoa: number) => http.delete(`/tratadores/${idPessoa}`),
};
