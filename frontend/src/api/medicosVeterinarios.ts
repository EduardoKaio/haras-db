import { http } from "./http";

export interface MedicoVeterinario {
  idPessoa: number;
  nomePessoa: string;
  emailPessoa: string;
  numCrmv: string;
  ufCrmv: string;
}

export interface MedicoVeterinarioInput {
  idPessoa: number;
  numCrmv: string;
  ufCrmv: string;
}

export const medicosVeterinariosApi = {
  listar: () => http.get<MedicoVeterinario[]>("/medicos-veterinarios"),
  buscar: (idPessoa: number) => http.get<MedicoVeterinario>(`/medicos-veterinarios/${idPessoa}`),
  criar: (data: MedicoVeterinarioInput) => http.post<MedicoVeterinario>("/medicos-veterinarios", data),
  atualizar: (idPessoa: number, data: MedicoVeterinarioInput) =>
    http.put<MedicoVeterinario>(`/medicos-veterinarios/${idPessoa}`, data),
  excluir: (idPessoa: number) => http.delete(`/medicos-veterinarios/${idPessoa}`),
};
