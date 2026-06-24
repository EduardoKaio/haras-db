import { useEffect, useState, type FormEvent } from "react";
import { useNavigate, useParams } from "react-router-dom";
import { type ContratoInput, contratosApi } from "../api/contratos";
import { type Colaborador, colaboradoresApi } from "../api/colaboradores";
import { ApiError } from "../api/http";
import { Button } from "../components/ui/Button";
import { TextField } from "../components/ui/TextField";
import { Card } from "../components/ui/Card";

const emptyForm: ContratoInput = {
  idPessoa: 0,
  dataInicio: "",
  dataFim: null,
  salario: 0,
};

export function ContratoFormPage() {
  const { id } = useParams();
  const isEdit = Boolean(id);
  const navigate = useNavigate();

  const [colaboradores, setColaboradores] = useState<Colaborador[]>([]);
  const [form, setForm] = useState<ContratoInput>(emptyForm);
  const [fieldErrors, setFieldErrors] = useState<Record<string, string>>({});
  const [generalError, setGeneralError] = useState<string | null>(null);
  const [loading, setLoading] = useState(true);
  const [saving, setSaving] = useState(false);

  useEffect(() => {
    const carregarColaboradores = colaboradoresApi.listar();
    const carregarContrato = isEdit ? contratosApi.buscar(Number(id)) : Promise.resolve(null);

    Promise.all([carregarColaboradores, carregarContrato])
      .then(([colaboradoresList, contrato]) => {
        setColaboradores(colaboradoresList);
        if (contrato) {
          setForm({
            idPessoa: contrato.idPessoa,
            dataInicio: contrato.dataInicio,
            dataFim: contrato.dataFim,
            salario: contrato.salario,
          });
        }
      })
      .catch((err: ApiError) => setGeneralError(err.message))
      .finally(() => setLoading(false));
  }, [id, isEdit]);

  function handleChange<K extends keyof ContratoInput>(key: K, value: ContratoInput[K]) {
    setForm((prev) => ({ ...prev, [key]: value }));
  }

  async function handleSubmit(event: FormEvent) {
    event.preventDefault();
    setSaving(true);
    setGeneralError(null);
    setFieldErrors({});
    try {
      if (isEdit) {
        await contratosApi.atualizar(Number(id), form);
      } else {
        await contratosApi.criar(form);
      }
      navigate("/contratos");
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
          <h1>{isEdit ? "Editar contrato" : "Novo contrato"}</h1>
        </div>
      </div>

      {generalError && <div className="alert alert-danger">{generalError}</div>}

      <Card>
        <form onSubmit={handleSubmit}>
          <div className="field">
            <label htmlFor="colaborador">Colaborador</label>
            <select
              id="colaborador"
              value={form.idPessoa || ""}
              onChange={(e) => handleChange("idPessoa", Number(e.target.value))}
              required
            >
              <option value="" disabled>
                Selecione um colaborador...
              </option>
              {colaboradores.map((c) => (
                <option key={c.idPessoa} value={c.idPessoa}>
                  {c.nomePessoa} ({c.emailPessoa})
                </option>
              ))}
            </select>
            {fieldErrors.idPessoa && <span className="field__error">{fieldErrors.idPessoa}</span>}
          </div>

          <TextField
            name="dataInicio"
            label="Data de início"
            type="date"
            value={form.dataInicio}
            onChange={(e) => handleChange("dataInicio", e.target.value)}
            error={fieldErrors.dataInicio}
            required
          />
          <TextField
            name="dataFim"
            label="Data de fim (deixe em branco se ativo)"
            type="date"
            value={form.dataFim ?? ""}
            onChange={(e) => handleChange("dataFim", e.target.value || null)}
            error={fieldErrors.dataFim}
          />
          <TextField
            name="salario"
            label="Salário"
            type="number"
            step="0.01"
            min="0.01"
            value={form.salario || ""}
            onChange={(e) => handleChange("salario", Number(e.target.value))}
            error={fieldErrors.salario}
            required
          />

          <div className="form-actions">
            <Button type="submit" disabled={saving}>
              {saving ? "Salvando..." : "Salvar"}
            </Button>
            <Button type="button" variant="secondary" onClick={() => navigate("/contratos")}>
              Cancelar
            </Button>
          </div>
        </form>
      </Card>
    </>
  );
}
