import { http } from "./http";

export interface EquinoResumo {
  idEquino: number;
  nome: string;
}

export interface TratadorResumo {
  idPessoa: number;
  nomePessoa: string;
}

export interface Limpeza {
  idLimpeza: number;
  dataHora: string;
  equinos: EquinoResumo[];
  tratadores: TratadorResumo[];
}

export interface LimpezaInput {
  dataHora: string;
  idsEquinos: number[];
  idsTratadores: number[];
}

export const limpezasApi = {
  listar: () => http.get<Limpeza[]>("/limpezas"),
  buscar: (id: number) => http.get<Limpeza>(`/limpezas/${id}`),
  criar: (data: LimpezaInput) => http.post<Limpeza>("/limpezas", data),
  excluir: (id: number) => http.delete(`/limpezas/${id}`),
};
