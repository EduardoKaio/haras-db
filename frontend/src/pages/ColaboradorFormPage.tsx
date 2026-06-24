import { useEffect, useState, type FormEvent } from "react";
import { useNavigate } from "react-router-dom";
import { colaboradoresApi } from "../api/colaboradores";
import { type Pessoa, pessoasApi } from "../api/pessoas";
import { ApiError } from "../api/http";
import { Button } from "../components/ui/Button";
import { Card } from "../components/ui/Card";

export function ColaboradorFormPage() {
  const navigate = useNavigate();
  const [pessoasElegiveis, setPessoasElegiveis] = useState<Pessoa[]>([]);
  const [selectedPessoaId, setSelectedPessoaId] = useState("");
  const [generalError, setGeneralError] = useState<string | null>(null);
  const [loading, setLoading] = useState(true);
  const [saving, setSaving] = useState(false);

  useEffect(() => {
    Promise.all([pessoasApi.listar(), colaboradoresApi.listar()])
      .then(([pessoas, colaboradores]) => {
        const idsComCadastro = new Set(colaboradores.map((c) => c.idPessoa));
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
      await colaboradoresApi.criar(Number(selectedPessoaId));
      navigate("/colaboradores");
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
          <h1>Novo colaborador</h1>
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
              <span className="field__error">Nenhuma pessoa elegível (todas já são colaboradoras).</span>
            )}
          </div>

          <div className="form-actions">
            <Button type="submit" disabled={saving || !selectedPessoaId}>
              {saving ? "Salvando..." : "Salvar"}
            </Button>
            <Button type="button" variant="secondary" onClick={() => navigate("/colaboradores")}>
              Cancelar
            </Button>
          </div>
        </form>
      </Card>
    </>
  );
}
