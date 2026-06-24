import { useEffect, useState, type FormEvent } from "react";
import { useNavigate, useParams } from "react-router-dom";
import { medicosVeterinariosApi } from "../api/medicosVeterinarios";
import { type Pessoa, pessoasApi } from "../api/pessoas";
import { ApiError } from "../api/http";
import { Button } from "../components/ui/Button";
import { TextField } from "../components/ui/TextField";
import { Card } from "../components/ui/Card";

export function MedicoVeterinarioFormPage() {
  const { idPessoa } = useParams();
  const isEdit = Boolean(idPessoa);
  const navigate = useNavigate();

  const [pessoasElegiveis, setPessoasElegiveis] = useState<Pessoa[]>([]);
  const [nomePessoaAtual, setNomePessoaAtual] = useState("");
  const [selectedPessoaId, setSelectedPessoaId] = useState("");
  const [numCrmv, setNumCrmv] = useState("");
  const [ufCrmv, setUfCrmv] = useState("");
  const [fieldErrors, setFieldErrors] = useState<Record<string, string>>({});
  const [generalError, setGeneralError] = useState<string | null>(null);
  const [loading, setLoading] = useState(true);
  const [saving, setSaving] = useState(false);

  useEffect(() => {
    if (isEdit) {
      medicosVeterinariosApi
        .buscar(Number(idPessoa))
        .then((medico) => {
          setNomePessoaAtual(medico.nomePessoa);
          setNumCrmv(medico.numCrmv);
          setUfCrmv(medico.ufCrmv);
        })
        .catch((err: ApiError) => setGeneralError(err.message))
        .finally(() => setLoading(false));
    } else {
      Promise.all([pessoasApi.listar(), medicosVeterinariosApi.listar()])
        .then(([pessoas, medicos]) => {
          const idsComCadastro = new Set(medicos.map((m) => m.idPessoa));
          setPessoasElegiveis(pessoas.filter((p) => !idsComCadastro.has(p.idPessoa)));
        })
        .catch((err: ApiError) => setGeneralError(err.message))
        .finally(() => setLoading(false));
    }
  }, [idPessoa, isEdit]);

  async function handleSubmit(event: FormEvent) {
    event.preventDefault();
    setSaving(true);
    setGeneralError(null);
    setFieldErrors({});
    try {
      if (isEdit) {
        await medicosVeterinariosApi.atualizar(Number(idPessoa), {
          idPessoa: Number(idPessoa),
          numCrmv,
          ufCrmv,
        });
      } else {
        await medicosVeterinariosApi.criar({ idPessoa: Number(selectedPessoaId), numCrmv, ufCrmv });
      }
      navigate("/medicos-veterinarios");
    } catch (err) {
      const apiError = err as ApiError;
      if (apiError.fieldErrors) {
        setFieldErrors(apiError.fieldErrors);
      } else {
        setGeneralError(apiError.message);
      }
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
          <h1>{isEdit ? "Editar médico veterinário" : "Novo médico veterinário"}</h1>
        </div>
      </div>

      {generalError && <div className="alert alert-danger">{generalError}</div>}

      <Card>
        <form onSubmit={handleSubmit}>
          {isEdit ? (
            <div className="field">
              <label>Pessoa</label>
              <input value={nomePessoaAtual} disabled />
            </div>
          ) : (
            <div className="field">
              <label htmlFor="pessoa">Pessoa</label>
              <select
                id="pessoa"
                value={selectedPessoaId}
                onChange={(e) => setSelectedPessoaId(e.target.value)}
                required
              >
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
                <span className="field__error">Nenhuma pessoa elegível (todas já são médicas veterinárias).</span>
              )}
            </div>
          )}

          <TextField
            name="numCrmv"
            label="Número do CRMV"
            value={numCrmv}
            onChange={(e) => setNumCrmv(e.target.value)}
            error={fieldErrors.numCrmv}
            maxLength={5}
            required
          />
          <TextField
            name="ufCrmv"
            label="UF do CRMV"
            value={ufCrmv}
            onChange={(e) => setUfCrmv(e.target.value.toUpperCase())}
            error={fieldErrors.ufCrmv}
            maxLength={2}
            required
          />

          <div className="form-actions">
            <Button type="submit" disabled={saving}>
              {saving ? "Salvando..." : "Salvar"}
            </Button>
            <Button type="button" variant="secondary" onClick={() => navigate("/medicos-veterinarios")}>
              Cancelar
            </Button>
          </div>
        </form>
      </Card>
    </>
  );
}
