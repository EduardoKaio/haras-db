import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import { type Tratador, tratadoresApi } from "../api/tratadores";
import { ApiError } from "../api/http";
import { Button } from "../components/ui/Button";
import { Card } from "../components/ui/Card";

export function TratadoresListPage() {
  const [tratadores, setTratadores] = useState<Tratador[] | null>(null);
  const [error, setError] = useState<string | null>(null);
  const navigate = useNavigate();

  function carregar() {
    setError(null);
    tratadoresApi
      .listar()
      .then(setTratadores)
      .catch((err: ApiError) => setError(err.message));
  }

  useEffect(() => {
    carregar();
  }, []);

  async function excluir(tratador: Tratador) {
    if (!confirm(`Remover ${tratador.nomePessoa} do quadro de tratadores?`)) return;
    try {
      await tratadoresApi.excluir(tratador.idPessoa);
      carregar();
    } catch (err) {
      setError((err as ApiError).message);
    }
  }

  return (
    <>
      <div className="page-header">
        <div className="page-header__title">
          <h1>Tratadores</h1>
          <span className="page-header__subtitle">Colaboradores responsáveis pela rotina de manejo</span>
        </div>
        <Button onClick={() => navigate("/tratadores/novo")}>+ Novo tratador</Button>
      </div>

      {error && <div className="alert alert-danger">{error}</div>}

      <Card>
        {tratadores === null ? (
          <p>Carregando...</p>
        ) : tratadores.length === 0 ? (
          <div className="empty-state">Nenhum tratador cadastrado ainda.</div>
        ) : (
          <table className="table">
            <thead>
              <tr>
                <th>Nome</th>
                <th>Email</th>
                <th></th>
              </tr>
            </thead>
            <tbody>
              {tratadores.map((tratador) => (
                <tr key={tratador.idPessoa}>
                  <td>{tratador.nomePessoa}</td>
                  <td>{tratador.emailPessoa}</td>
                  <td>
                    <div className="table__actions">
                      <Button variant="danger" size="sm" onClick={() => excluir(tratador)}>
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
