import { useEffect, useState, type FormEvent } from "react";
import { useNavigate, useParams } from "react-router-dom";
import { type EquinoInput, equinosApi } from "../api/equinos";
import { ApiError } from "../api/http";
import { Button } from "../components/ui/Button";
import { TextField } from "../components/ui/TextField";
import { Card } from "../components/ui/Card";

const emptyForm: EquinoInput = {
  nome: "",
  raca: "",
  peso: 0,
  funcao: "",
  dataNascimento: "",
  status: "",
  registro: "",
  registroPai: "",
  registroMae: "",
  pelagem: "",
};

export function EquinoFormPage() {
  const { id } = useParams();
  const isEdit = Boolean(id);
  const navigate = useNavigate();

  const [form, setForm] = useState<EquinoInput>(emptyForm);
  const [fieldErrors, setFieldErrors] = useState<Record<string, string>>({});
  const [generalError, setGeneralError] = useState<string | null>(null);
  const [loading, setLoading] = useState(isEdit);
  const [saving, setSaving] = useState(false);

  useEffect(() => {
    if (!isEdit) return;
    equinosApi
      .buscar(Number(id))
      .then((equino) =>
        setForm({
          nome: equino.nome,
          raca: equino.raca,
          peso: equino.peso,
          funcao: equino.funcao,
          dataNascimento: equino.dataNascimento,
          status: equino.status,
          registro: equino.registro,
          registroPai: equino.registroPai ?? "",
          registroMae: equino.registroMae ?? "",
          pelagem: equino.pelagem,
        }),
      )
      .catch((err: ApiError) => setGeneralError(err.message))
      .finally(() => setLoading(false));
  }, [id, isEdit]);

  function handleChange<K extends keyof EquinoInput>(key: K, value: EquinoInput[K]) {
    setForm((prev) => ({ ...prev, [key]: value }));
  }

  async function handleSubmit(event: FormEvent) {
    event.preventDefault();
    setSaving(true);
    setGeneralError(null);
    setFieldErrors({});
    try {
      const payload: EquinoInput = {
        ...form,
        registroPai: form.registroPai || null,
        registroMae: form.registroMae || null,
      };
      if (isEdit) {
        await equinosApi.atualizar(Number(id), payload);
      } else {
        await equinosApi.criar(payload);
      }
      navigate("/equinos");
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
          <h1>{isEdit ? "Editar equino" : "Novo equino"}</h1>
        </div>
      </div>

      {generalError && <div className="alert alert-danger">{generalError}</div>}

      <Card>
        <form onSubmit={handleSubmit}>
          <TextField
            name="nome"
            label="Nome"
            value={form.nome}
            onChange={(e) => handleChange("nome", e.target.value)}
            error={fieldErrors.nome}
            maxLength={45}
            required
          />
          <TextField
            name="raca"
            label="Raça"
            value={form.raca}
            onChange={(e) => handleChange("raca", e.target.value)}
            error={fieldErrors.raca}
            maxLength={45}
            required
          />
          <TextField
            name="peso"
            label="Peso (kg)"
            type="number"
            step="0.1"
            min="0.1"
            value={form.peso || ""}
            onChange={(e) => handleChange("peso", Number(e.target.value))}
            error={fieldErrors.peso}
            required
          />
          <TextField
            name="funcao"
            label="Função"
            value={form.funcao}
            onChange={(e) => handleChange("funcao", e.target.value)}
            error={fieldErrors.funcao}
            maxLength={25}
            placeholder="Ex: Trabalho, Lazer, Competição"
            required
          />
          <TextField
            name="dataNascimento"
            label="Data de nascimento"
            type="date"
            value={form.dataNascimento}
            onChange={(e) => handleChange("dataNascimento", e.target.value)}
            error={fieldErrors.dataNascimento}
            required
          />
          <TextField
            name="status"
            label="Status"
            value={form.status}
            onChange={(e) => handleChange("status", e.target.value)}
            error={fieldErrors.status}
            maxLength={25}
            placeholder="Ex: Ativo, Vendido, Em tratamento"
            required
          />
          <TextField
            name="registro"
            label="Registro"
            value={form.registro}
            onChange={(e) => handleChange("registro", e.target.value)}
            error={fieldErrors.registro}
            maxLength={25}
            required
          />
          <TextField
            name="registroPai"
            label="Registro do pai (opcional)"
            value={form.registroPai ?? ""}
            onChange={(e) => handleChange("registroPai", e.target.value)}
            error={fieldErrors.registroPai}
            maxLength={25}
          />
          <TextField
            name="registroMae"
            label="Registro da mãe (opcional)"
            value={form.registroMae ?? ""}
            onChange={(e) => handleChange("registroMae", e.target.value)}
            error={fieldErrors.registroMae}
            maxLength={25}
          />
          <TextField
            name="pelagem"
            label="Pelagem"
            value={form.pelagem}
            onChange={(e) => handleChange("pelagem", e.target.value)}
            error={fieldErrors.pelagem}
            maxLength={25}
            required
          />

          <div className="form-actions">
            <Button type="submit" disabled={saving}>
              {saving ? "Salvando..." : "Salvar"}
            </Button>
            <Button type="button" variant="secondary" onClick={() => navigate("/equinos")}>
              Cancelar
            </Button>
          </div>
        </form>
      </Card>
    </>
  );
}
