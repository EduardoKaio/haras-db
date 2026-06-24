import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import { type Equino, equinosApi } from "../api/equinos";
import { ApiError } from "../api/http";
import { Button } from "../components/ui/Button";
import { Card } from "../components/ui/Card";

export function EquinosListPage() {
  const [equinos, setEquinos] = useState<Equino[] | null>(null);
  const [error, setError] = useState<string | null>(null);
  const navigate = useNavigate();

  function carregar() {
    setError(null);
    equinosApi
      .listar()
      .then(setEquinos)
      .catch((err: ApiError) => setError(err.message));
  }

  useEffect(() => {
    carregar();
  }, []);

  async function excluir(equino: Equino) {
    if (!confirm(`Excluir ${equino.nome}?`)) return;
    try {
      await equinosApi.excluir(equino.idEquino);
      carregar();
    } catch (err) {
      setError((err as ApiError).message);
    }
  }

  return (
    <>
      <div className="page-header">
        <div className="page-header__title">
          <h1>Equinos</h1>
          <span className="page-header__subtitle">Cavalos cadastrados no haras</span>
        </div>
        <Button onClick={() => navigate("/equinos/novo")}>+ Novo equino</Button>
      </div>

      {error && <div className="alert alert-danger">{error}</div>}

      <Card>
        {equinos === null ? (
          <p>Carregando...</p>
        ) : equinos.length === 0 ? (
          <div className="empty-state">Nenhum equino cadastrado ainda.</div>
        ) : (
          <table className="table">
            <thead>
              <tr>
                <th>Nome</th>
                <th>Raça</th>
                <th>Registro</th>
                <th>Status</th>
                <th></th>
              </tr>
            </thead>
            <tbody>
              {equinos.map((equino) => (
                <tr key={equino.idEquino}>
                  <td>{equino.nome}</td>
                  <td>{equino.raca}</td>
                  <td>{equino.registro}</td>
                  <td>
                    <span className="badge badge-muted">{equino.status}</span>
                  </td>
                  <td>
                    <div className="table__actions">
                      <Button
                        variant="secondary"
                        size="sm"
                        onClick={() => navigate(`/equinos/${equino.idEquino}/editar`)}
                      >
                        Editar
                      </Button>
                      <Button variant="danger" size="sm" onClick={() => excluir(equino)}>
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
