import { useEffect, useState, type FormEvent } from "react";
import { useNavigate } from "react-router-dom";
import { limpezasApi } from "../api/limpezas";
import { type Equino, equinosApi } from "../api/equinos";
import { type Tratador, tratadoresApi } from "../api/tratadores";
import { ApiError } from "../api/http";
import { Button } from "../components/ui/Button";
import { Card } from "../components/ui/Card";

export function LimpezaFormPage() {
  const navigate = useNavigate();

  const [equinos, setEquinos] = useState<Equino[]>([]);
  const [tratadores, setTratadores] = useState<Tratador[]>([]);
  const [dataHora, setDataHora] = useState("");
  const [equinosSelecionados, setEquinosSelecionados] = useState<Set<number>>(new Set());
  const [tratadoresSelecionados, setTratadoresSelecionados] = useState<Set<number>>(new Set());
  const [fieldErrors, setFieldErrors] = useState<Record<string, string>>({});
  const [generalError, setGeneralError] = useState<string | null>(null);
  const [loading, setLoading] = useState(true);
  const [saving, setSaving] = useState(false);

  useEffect(() => {
    Promise.all([equinosApi.listar(), tratadoresApi.listar()])
      .then(([todosEquinos, todosTratadores]) => {
        setEquinos(todosEquinos);
        setTratadores(todosTratadores);
      })
      .catch((err: ApiError) => setGeneralError(err.message))
      .finally(() => setLoading(false));
  }, []);

  function toggle(set: Set<number>, updater: (s: Set<number>) => void, id: number) {
    const novo = new Set(set);
    if (novo.has(id)) {
      novo.delete(id);
    } else {
      novo.add(id);
    }
    updater(novo);
  }

  async function handleSubmit(event: FormEvent) {
    event.preventDefault();
    setGeneralError(null);
    setFieldErrors({});

    const erros: Record<string, string> = {};
    if (!dataHora) erros.dataHora = "Data/hora é obrigatória";
    if (equinosSelecionados.size === 0) erros.idsEquinos = "Selecione ao menos um equino";
    if (tratadoresSelecionados.size === 0) erros.idsTratadores = "Selecione ao menos um tratador";
    if (Object.keys(erros).length > 0) {
      setFieldErrors(erros);
      return;
    }

    setSaving(true);
    try {
      await limpezasApi.criar({
        dataHora,
        idsEquinos: [...equinosSelecionados],
        idsTratadores: [...tratadoresSelecionados],
      });
      navigate("/limpezas");
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
          <h1>Nova limpeza</h1>
          <span className="page-header__subtitle">Registre a limpeza associando equinos e tratadores</span>
        </div>
      </div>

      {generalError && <div className="alert alert-danger">{generalError}</div>}

      <Card>
        <form onSubmit={handleSubmit}>
          <div className="field">
            <label htmlFor="dataHora">Data/hora</label>
            <input
              id="dataHora"
              type="datetime-local"
              value={dataHora}
              onChange={(e) => setDataHora(e.target.value)}
              className={fieldErrors.dataHora ? "has-error" : ""}
              required
            />
            {fieldErrors.dataHora && <span className="field__error">{fieldErrors.dataHora}</span>}
          </div>

          <div className="field">
            <label>Equinos limpos</label>
            {equinos.length === 0 ? (
              <span className="field__error">Nenhum equino cadastrado.</span>
            ) : (
              <div className="checkbox-group">
                {equinos.map((equino) => (
                  <label key={equino.idEquino} className="checkbox-group__item">
                    <input
                      type="checkbox"
                      checked={equinosSelecionados.has(equino.idEquino)}
                      onChange={() => toggle(equinosSelecionados, setEquinosSelecionados, equino.idEquino)}
                    />
                    {equino.nome} <span className="checkbox-group__meta">({equino.registro})</span>
                  </label>
                ))}
              </div>
            )}
            {fieldErrors.idsEquinos && <span className="field__error">{fieldErrors.idsEquinos}</span>}
          </div>

          <div className="field">
            <label>Tratadores participantes</label>
            {tratadores.length === 0 ? (
              <span className="field__error">Nenhum tratador cadastrado.</span>
            ) : (
              <div className="checkbox-group">
                {tratadores.map((tratador) => (
                  <label key={tratador.idPessoa} className="checkbox-group__item">
                    <input
                      type="checkbox"
                      checked={tratadoresSelecionados.has(tratador.idPessoa)}
                      onChange={() => toggle(tratadoresSelecionados, setTratadoresSelecionados, tratador.idPessoa)}
                    />
                    {tratador.nomePessoa}
                  </label>
                ))}
              </div>
            )}
            {fieldErrors.idsTratadores && <span className="field__error">{fieldErrors.idsTratadores}</span>}
          </div>

          <div className="form-actions">
            <Button type="submit" disabled={saving}>
              {saving ? "Salvando..." : "Registrar limpeza"}
            </Button>
            <Button type="button" variant="secondary" onClick={() => navigate("/limpezas")}>
              Cancelar
            </Button>
          </div>
        </form>
      </Card>
    </>
  );
}
