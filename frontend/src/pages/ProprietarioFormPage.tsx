import { useEffect, useState, type FormEvent } from "react";
import { useNavigate, useParams } from "react-router-dom";
import { proprietariosApi } from "../api/proprietarios";
import { type Equino, equinosApi } from "../api/equinos";
import { type Pessoa, pessoasApi } from "../api/pessoas";
import { ApiError } from "../api/http";
import { Button } from "../components/ui/Button";
import { Card } from "../components/ui/Card";

export function ProprietarioFormPage() {
  const { idPessoa } = useParams();
  const isManage = Boolean(idPessoa);
  const navigate = useNavigate();

  if (isManage) {
    return <GerenciarEquinosPage idPessoa={Number(idPessoa)} />;
  }
  return <NovoProprietarioForm onCancel={() => navigate("/proprietarios")} />;
}

function NovoProprietarioForm({ onCancel }: { onCancel: () => void }) {
  const navigate = useNavigate();
  const [pessoasElegiveis, setPessoasElegiveis] = useState<Pessoa[]>([]);
  const [selectedPessoaId, setSelectedPessoaId] = useState("");
  const [generalError, setGeneralError] = useState<string | null>(null);
  const [loading, setLoading] = useState(true);
  const [saving, setSaving] = useState(false);

  useEffect(() => {
    Promise.all([pessoasApi.listar(), proprietariosApi.listar()])
      .then(([pessoas, proprietarios]) => {
        const idsComCadastro = new Set(proprietarios.map((p) => p.idPessoa));
        setPessoasElegiveis(pessoas.filter((p) => !idsComCadastro.has(p.idPessoa)));
      })
      .catch((err: ApiError) => setGeneralError(err.message))
      .finally(() => setLoading(false));
  }, []);

  async function handleSubmit(event: FormEvent) {
    event.preventDefault();
    setSaving(true);
    setGeneralError(null);
    try {
      const criado = await proprietariosApi.criar(Number(selectedPessoaId));
      navigate(`/proprietarios/${criado.idPessoa}`);
    } catch (err) {
      setGeneralError((err as ApiError).message);
    } finally {
      setSaving(false);
    }
  }

  if (loading) {
    return <p>Carregando...</p>;
  }

  return (
    <>
      <div className="page-header">
        <div className="page-header__title">
          <h1>Novo proprietário</h1>
        </div>
      </div>

      {generalError && <div className="alert alert-danger">{generalError}</div>}

      <Card>
        <form onSubmit={handleSubmit}>
          <div className="field">
            <label htmlFor="pessoa">Pessoa</label>
            <select id="pessoa" value={selectedPessoaId} onChange={(e) => setSelectedPessoaId(e.target.value)} required>
              <option value="" disabled>
                Selecione uma pessoa...
              </option>
              {pessoasElegiveis.map((p) => (
                <option key={p.idPessoa} value={p.idPessoa}>
                  {p.nome} ({p.email})
                </option>
              ))}
            </select>
            {pessoasElegiveis.length === 0 && (
              <span className="field__error">Nenhuma pessoa elegível (todas já são proprietárias).</span>
            )}
          </div>

          <div className="form-actions">
            <Button type="submit" disabled={saving || !selectedPessoaId}>
              {saving ? "Salvando..." : "Salvar"}
            </Button>
            <Button type="button" variant="secondary" onClick={onCancel}>
              Cancelar
            </Button>
          </div>
        </form>
      </Card>
    </>
  );
}

function GerenciarEquinosPage({ idPessoa }: { idPessoa: number }) {
  const navigate = useNavigate();
  const [nomePessoa, setNomePessoa] = useState("");
  const [vinculados, setVinculados] = useState<Equino[]>([]);
  const [disponiveis, setDisponiveis] = useState<Equino[]>([]);
  const [selectedEquinoId, setSelectedEquinoId] = useState("");
  const [error, setError] = useState<string | null>(null);
  const [loading, setLoading] = useState(true);

  function carregar() {
    Promise.all([proprietariosApi.buscar(idPessoa), proprietariosApi.listarEquinos(idPessoa), equinosApi.listar()])
      .then(([proprietario, equinosVinculados, todosEquinos]) => {
        setNomePessoa(proprietario.nomePessoa);
        setVinculados(equinosVinculados);
        const idsVinculados = new Set(equinosVinculados.map((e) => e.idEquino));
        setDisponiveis(todosEquinos.filter((e) => !idsVinculados.has(e.idEquino)));
      })
      .catch((err: ApiError) => setError(err.message))
      .finally(() => setLoading(false));
  }

  useEffect(() => {
    carregar();
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [idPessoa]);

  async function vincular() {
    if (!selectedEquinoId) return;
    setError(null);
    try {
      await proprietariosApi.vincularEquino(idPessoa, Number(selectedEquinoId));
      setSelectedEquinoId("");
      carregar();
    } catch (err) {
      setError((err as ApiError).message);
    }
  }

  async function desvincular(idEquino: number) {
    setError(null);
    try {
      await proprietariosApi.desvincularEquino(idPessoa, idEquino);
      carregar();
    } catch (err) {
      setError((err as ApiError).message);
    }
  }

  if (loading) {
    return <p>Carregando...</p>;
  }

  return (
    <>
      <div className="page-header">
        <div className="page-header__title">
          <h1>Equinos de {nomePessoa}</h1>
          <span className="page-header__subtitle">Gerencie os equinos vinculados a esta proprietária</span>
        </div>
      </div>

      {error && <div className="alert alert-danger">{error}</div>}

      <Card>
        {vinculados.length === 0 ? (
          <div className="empty-state">Nenhum equino vinculado ainda.</div>
        ) : (
          <table className="table">
            <thead>
              <tr>
                <th>Nome</th>
                <th>Raça</th>
                <th>Registro</th>
                <th></th>
              </tr>
            </thead>
            <tbody>
              {vinculados.map((equino) => (
                <tr key={equino.idEquino}>
                  <td>{equino.nome}</td>
                  <td>{equino.raca}</td>
                  <td>{equino.registro}</td>
                  <td>
                    <div className="table__actions">
                      <Button variant="danger" size="sm" onClick={() => desvincular(equino.idEquino)}>
                        Desvincular
                      </Button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        )}

        <div style={{ display: "flex", gap: "8px", marginTop: "16px" }}>
          <select
            value={selectedEquinoId}
            onChange={(e) => setSelectedEquinoId(e.target.value)}
            style={{ flex: 1 }}
          >
            <option value="" disabled>
              {disponiveis.length === 0 ? "Nenhum equino disponível" : "Selecione um equino pra vincular..."}
            </option>
            {disponiveis.map((e) => (
              <option key={e.idEquino} value={e.idEquino}>
                {e.nome} ({e.registro})
              </option>
            ))}
          </select>
          <Button type="button" size="sm" variant="secondary" onClick={vincular} disabled={!selectedEquinoId}>
            Vincular
          </Button>
        </div>
      </Card>

      <div className="form-actions">
        <Button type="button" variant="secondary" onClick={() => navigate("/proprietarios")}>
          Voltar
        </Button>
      </div>
    </>
  );
}
