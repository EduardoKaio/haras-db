import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import { type Colaborador, colaboradoresApi } from "../api/colaboradores";
import { ApiError } from "../api/http";
import { Button } from "../components/ui/Button";
import { Card } from "../components/ui/Card";

export function ColaboradoresListPage() {
  const [colaboradores, setColaboradores] = useState<Colaborador[] | null>(null);
  const [error, setError] = useState<string | null>(null);
  const navigate = useNavigate();

  function carregar() {
    setError(null);
    colaboradoresApi
      .listar()
      .then(setColaboradores)
      .catch((err: ApiError) => setError(err.message));
  }

  useEffect(() => {
    carregar();
  }, []);

  async function excluir(colaborador: Colaborador) {
    if (!confirm(`Remover ${colaborador.nomePessoa} do quadro de colaboradores?`)) return;
    try {
      await colaboradoresApi.excluir(colaborador.idPessoa);
      carregar();
    } catch (err) {
      setError((err as ApiError).message);
    }
  }

  return (
    <>
      <div className="page-header">
        <div className="page-header__title">
          <h1>Colaboradores</h1>
          <span className="page-header__subtitle">Pessoas contratadas pelo haras</span>
        </div>
        <Button onClick={() => navigate("/colaboradores/novo")}>+ Novo colaborador</Button>
      </div>

      {error && <div className="alert alert-danger">{error}</div>}

      <Card>
        {colaboradores === null ? (
          <p>Carregando...</p>
        ) : colaboradores.length === 0 ? (
          <div className="empty-state">Nenhum colaborador cadastrado ainda.</div>
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
              {colaboradores.map((colaborador) => (
                <tr key={colaborador.idPessoa}>
                  <td>{colaborador.nomePessoa}</td>
                  <td>{colaborador.emailPessoa}</td>
                  <td>
                    <div className="table__actions">
                      <Button variant="danger" size="sm" onClick={() => excluir(colaborador)}>
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
