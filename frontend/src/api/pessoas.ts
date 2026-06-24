import { http } from "./http";

export interface Pessoa {
  idPessoa: number;
  nome: string;
  dataNascimento: string;
  cpf: string;
  gerente: boolean;
  email: string;
}

export interface PessoaInput {
  nome: string;
  dataNascimento: string;
  cpf: string;
  gerente: boolean;
  email: string;
  senha: string;
}

export interface Telefone {
  telefone: string;
  idPessoa: number;
}

export const pessoasApi = {
  listar: () => http.get<Pessoa[]>("/pessoas"),
  buscar: (id: number) => http.get<Pessoa>(`/pessoas/${id}`),
  criar: (data: PessoaInput) => http.post<Pessoa>("/pessoas", data),
  atualizar: (id: number, data: PessoaInput) => http.put<Pessoa>(`/pessoas/${id}`, data),
  excluir: (id: number) => http.delete(`/pessoas/${id}`),
  listarTelefones: (id: number) => http.get<Telefone[]>(`/pessoas/${id}/telefones`),
  adicionarTelefone: (id: number, telefone: string) => http.post<void>(`/pessoas/${id}/telefones`, { telefone }),
  removerTelefone: (id: number, telefone: string) => http.delete(`/pessoas/${id}/telefones/${telefone}`),
};
