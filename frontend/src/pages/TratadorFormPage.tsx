import { useEffect, useState, type FormEvent } from "react";
import { useNavigate } from "react-router-dom";
import { tratadoresApi } from "../api/tratadores";
import { type Colaborador, colaboradoresApi } from "../api/colaboradores";
import { ApiError } from "../api/http";
import { Button } from "../components/ui/Button";
import { Card } from "../components/ui/Card";

export function TratadorFormPage() {
  const navigate = useNavigate();
  const [colaboradoresElegiveis, setColaboradoresElegiveis] = useState<Colaborador[]>([]);
  const [selectedId, setSelectedId] = useState("");
  const [generalError, setGeneralError] = useState<string | null>(null);
  const [loading, setLoading] = useState(true);
  const [saving, setSaving] = useState(false);

  useEffect(() => {
    // Tratador é especialização de Colaborador: só colaboradores que ainda não são tratadores.
    Promise.all([colaboradoresApi.listar(), tratadoresApi.listar()])
      .then(([colaboradores, tratadores]) => {
        const jaTratadores = new Set(tratadores.map((t) => t.idPessoa));
        setColaboradoresElegiveis(colaboradores.filter((c) => !jaTratadores.has(c.idPessoa)));
      })
      .catch((err: ApiError) => setGeneralError(err.message))
      .finally(() => setLoading(false));
  }, []);

  async function handleSubmit(event: FormEvent) {
    event.preventDefault();
    setSaving(true);
    setGeneralError(null);
    try {
      await tratadoresApi.criar(Number(selectedId));
      navigate("/tratadores");
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
          <h1>Novo tratador</h1>
          <span className="page-header__subtitle">Escolha um colaborador para torná-lo tratador</span>
        </div>
      </div>

      {generalError && <div className="alert alert-danger">{generalError}</div>}

      <Card>
        <form onSubmit={handleSubmit}>
          <div className="field">
            <label htmlFor="colaborador">Colaborador</label>
            <select
              id="colaborador"
              value={selectedId}
              onChange={(e) => setSelectedId(e.target.value)}
              required
            >
              <option value="" disabled>
                Selecione um colaborador...
              </option>
              {colaboradoresElegiveis.map((c) => (
                <option key={c.idPessoa} value={c.idPessoa}>
                  {c.nomePessoa} ({c.emailPessoa})
                </option>
              ))}
            </select>
            {colaboradoresElegiveis.length === 0 && (
              <span className="field__error">
                Nenhum colaborador elegível. Cadastre a pessoa como colaborador primeiro.
              </span>
            )}
          </div>

          <div className="form-actions">
            <Button type="submit" disabled={saving || !selectedId}>
              {saving ? "Salvando..." : "Salvar"}
            </Button>
            <Button type="button" variant="secondary" onClick={() => navigate("/tratadores")}>
              Cancelar
            </Button>
          </div>
        </form>
      </Card>
    </>
  );
}
