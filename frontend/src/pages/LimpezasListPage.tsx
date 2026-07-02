import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import { type Limpeza, limpezasApi } from "../api/limpezas";
import { ApiError } from "../api/http";
import { useAuth } from "../auth/AuthContext";
import { Button } from "../components/ui/Button";
import { Card } from "../components/ui/Card";

function formatarDataHora(iso: string): string {
  const data = new Date(iso);
  return Number.isNaN(data.getTime()) ? iso : data.toLocaleString("pt-BR");
}

export function LimpezasListPage() {
  const [limpezas, setLimpezas] = useState<Limpeza[] | null>(null);
  const [error, setError] = useState<string | null>(null);
  const navigate = useNavigate();
  const { hasRole } = useAuth();
  const podeRegistrar = hasRole("GERENTE");

  function carregar() {
    setError(null);
    limpezasApi
      .listar()
      .then(setLimpezas)
      .catch((err: ApiError) => setError(err.message));
  }

  useEffect(() => {
    carregar();
  }, []);

  async function excluir(limpeza: Limpeza) {
    if (!confirm(`Excluir a limpeza de ${formatarDataHora(limpeza.dataHora)}?`)) return;
    try {
      await limpezasApi.excluir(limpeza.idLimpeza);
      carregar();
    } catch (err) {
      setError((err as ApiError).message);
    }
  }

  return (
    <>
      <div className="page-header">
        <div className="page-header__title">
          <h1>Limpezas</h1>
          <span className="page-header__subtitle">Registros de limpeza de baias por equinos e tratadores</span>
        </div>
        {podeRegistrar && (
          <Button onClick={() => navigate("/limpezas/nova")}>+ Nova limpeza</Button>
        )}
      </div>

      {error && <div className="alert alert-danger">{error}</div>}

      <Card>
        {limpezas === null ? (
          <p>Carregando...</p>
        ) : limpezas.length === 0 ? (
          <div className="empty-state">Nenhuma limpeza registrada ainda.</div>
        ) : (
          <table className="table">
            <thead>
              <tr>
                <th>Data/hora</th>
                <th>Equinos</th>
                <th>Tratadores</th>
                <th></th>
              </tr>
            </thead>
            <tbody>
              {limpezas.map((limpeza) => (
                <tr key={limpeza.idLimpeza}>
                  <td>{formatarDataHora(limpeza.dataHora)}</td>
                  <td>{limpeza.equinos.map((e) => e.nome).join(", ") || "—"}</td>
                  <td>{limpeza.tratadores.map((t) => t.nomePessoa).join(", ") || "—"}</td>
                  <td>
                    <div className="table__actions">
                      <Button variant="danger" size="sm" onClick={() => excluir(limpeza)}>
                        Excluir
                      </Button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        )}
      </Card>
    </>
  );
}
