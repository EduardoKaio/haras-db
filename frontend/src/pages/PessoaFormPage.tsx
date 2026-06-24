import { useEffect, useState, type FormEvent } from "react";
import { useNavigate, useParams } from "react-router-dom";
import { pessoasApi, type PessoaInput } from "../api/pessoas";
import { ApiError } from "../api/http";
import { Button } from "../components/ui/Button";
import { TextField } from "../components/ui/TextField";
import { Card } from "../components/ui/Card";

const emptyForm: PessoaInput = {
  nome: "",
  dataNascimento: "",
  cpf: "",
  gerente: false,
  email: "",
  senha: "",
};

export function PessoaFormPage() {
  const { id } = useParams();
  const isEdit = Boolean(id);
  const navigate = useNavigate();

  const [form, setForm] = useState<PessoaInput>(emptyForm);
  const [telefones, setTelefones] = useState<string[]>([]);
  const [telefonesOriginais, setTelefonesOriginais] = useState<string[]>([]);
  const [fieldErrors, setFieldErrors] = useState<Record<string, string>>({});
  const [generalError, setGeneralError] = useState<string | null>(null);
  const [loading, setLoading] = useState(isEdit);
  const [saving, setSaving] = useState(false);

  useEffect(() => {
    if (!isEdit) return;
    Promise.all([pessoasApi.buscar(Number(id)), pessoasApi.listarTelefones(Number(id))])
      .then(([pessoa, tels]) => {
        setForm({
          nome: pessoa.nome,
          dataNascimento: pessoa.dataNascimento,
          cpf: pessoa.cpf,
          gerente: pessoa.gerente,
          email: pessoa.email,
          senha: "",
        });
        const numeros = tels.map((t) => t.telefone);
        setTelefones(numeros);
        setTelefonesOriginais(numeros);
      })
      .catch((err: ApiError) => setGeneralError(err.message))
      .finally(() => setLoading(false));
  }, [id, isEdit]);

  function handleChange<K extends keyof PessoaInput>(key: K, value: PessoaInput[K]) {
    setForm((prev) => ({ ...prev, [key]: value }));
  }

  async function sincronizarTelefones(idPessoa: number) {
    const novos = telefones.filter((t) => !telefonesOriginais.includes(t));
    const removidos = telefonesOriginais.filter((t) => !telefones.includes(t));
    await Promise.all([
      ...novos.map((t) => pessoasApi.adicionarTelefone(idPessoa, t)),
      ...removidos.map((t) => pessoasApi.removerTelefone(idPessoa, t)),
    ]);
  }

  async function handleSubmit(event: FormEvent) {
    event.preventDefault();
    setSaving(true);
    setGeneralError(null);
    setFieldErrors({});
    try {
      const idPessoa = isEdit ? Number(id) : (await pessoasApi.criar(form)).idPessoa;
      if (isEdit) {
        await pessoasApi.atualizar(idPessoa, form);
      }
      await sincronizarTelefones(idPessoa);
      navigate("/pessoas");
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
          <h1>{isEdit ? "Editar pessoa" : "Nova pessoa"}</h1>
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
            name="cpf"
            label="CPF"
            value={form.cpf}
            onChange={(e) => handleChange("cpf", e.target.value.replace(/\D/g, ""))}
            error={fieldErrors.cpf}
            maxLength={11}
            required
          />
          <TextField
            name="email"
            label="Email"
            type="email"
            value={form.email}
            onChange={(e) => handleChange("email", e.target.value)}
            error={fieldErrors.email}
            required
          />
          <TextField
            name="senha"
            label={isEdit ? "Nova senha (deixe em branco para manter)" : "Senha"}
            type="password"
            value={form.senha}
            onChange={(e) => handleChange("senha", e.target.value)}
            error={fieldErrors.senha}
            required={!isEdit}
          />
          <div className="field field--checkbox">
            <input
              id="gerente"
              type="checkbox"
              checked={form.gerente}
              onChange={(e) => handleChange("gerente", e.target.checked)}
            />
            <label htmlFor="gerente">É gerente</label>
          </div>

          <TelefonesField telefones={telefones} onChange={setTelefones} />

          <div className="form-actions">
            <Button type="submit" disabled={saving}>
              {saving ? "Salvando..." : "Salvar"}
            </Button>
            <Button type="button" variant="secondary" onClick={() => navigate("/pessoas")}>
              Cancelar
            </Button>
          </div>
        </form>
      </Card>
    </>
  );
}

function TelefonesField({
  telefones,
  onChange,
}: {
  telefones: string[];
  onChange: (telefones: string[]) => void;
}) {
  const [novoTelefone, setNovoTelefone] = useState("");

  function adicionar() {
    if (!novoTelefone || telefones.includes(novoTelefone)) return;
    onChange([...telefones, novoTelefone]);
    setNovoTelefone("");
  }

  function remover(telefone: string) {
    onChange(telefones.filter((t) => t !== telefone));
  }

  return (
    <div className="field">
      <label>Telefones</label>
      <div className="tag-list" style={{ marginBottom: "8px" }}>
        {telefones.length === 0 && (
          <span style={{ color: "var(--color-text-muted)" }}>Nenhum telefone adicionado.</span>
        )}
        {telefones.map((tel) => (
          <span className="tag" key={tel}>
            {tel}
            <button type="button" onClick={() => remover(tel)} aria-label="Remover telefone">
              ×
            </button>
          </span>
        ))}
      </div>
      <div style={{ display: "flex", gap: "8px" }}>
        <input
          placeholder="Somente números (10 ou 11 dígitos)"
          value={novoTelefone}
          onChange={(e) => setNovoTelefone(e.target.value.replace(/\D/g, ""))}
          maxLength={11}
          style={{
            flex: 1,
            padding: "8px 12px",
            border: "1px solid var(--color-border)",
            borderRadius: "6px",
            fontSize: "14px",
          }}
        />
        <Button type="button" size="sm" variant="secondary" onClick={adicionar}>
          Adicionar
        </Button>
      </div>
    </div>
  );
}
