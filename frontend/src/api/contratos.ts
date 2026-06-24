import { http } from "./http";

export interface Contrato {
  idContrato: number;
  idPessoa: number;
  nomeColaborador: string;
  dataInicio: string;
  dataFim: string | null;
  salario: number;
}

export interface ContratoInput {
  idPessoa: number;
  dataInicio: string;
  dataFim: string | null;
  salario: number;
}

export const contratosApi = {
  listar: () => http.get<Contrato[]>("/contratos"),
  buscar: (id: number) => http.get<Contrato>(`/contratos/${id}`),
  criar: (data: ContratoInput) => http.post<Contrato>("/contratos", data),
  atualizar: (id: number, data: ContratoInput) => http.put<Contrato>(`/contratos/${id}`, data),
  excluir: (id: number) => http.delete(`/contratos/${id}`),
};
