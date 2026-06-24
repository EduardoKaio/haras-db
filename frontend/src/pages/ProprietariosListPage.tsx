import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import { type Proprietario, proprietariosApi } from "../api/proprietarios";
import { ApiError } from "../api/http";
import { Button } from "../components/ui/Button";
import { Card } from "../components/ui/Card";

export function ProprietariosListPage() {
  const [proprietarios, setProprietarios] = useState<Proprietario[] | null>(null);
  const [error, setError] = useState<string | null>(null);
  const navigate = useNavigate();

  function carregar() {
    setError(null);
    proprietariosApi
      .listar()
      .then(setProprietarios)
      .catch((err: ApiError) => setError(err.message));
  }

  useEffect(() => {
    carregar();
  }, []);

  async function excluir(proprietario: Proprietario) {
    if (!confirm(`Remover ${proprietario.nomePessoa} do quadro de proprietários?`)) return;
    try {
      await proprietariosApi.excluir(proprietario.idPessoa);
      carregar();
    } catch (err) {
      setError((err as ApiError).message);
    }
  }

  return (
    <>
      <div className="page-header">
        <div className="page-header__title">
          <h1>Proprietários</h1>
          <span className="page-header__subtitle">Pessoas que possuem equinos no haras</span>
        </div>
        <Button onClick={() => navigate("/proprietarios/novo")}>+ Novo proprietário</Button>
      </div>

      {error && <div className="alert alert-danger">{error}</div>}

      <Card>
        {proprietarios === null ? (
          <p>Carregando...</p>
        ) : proprietarios.length === 0 ? (
          <div className="empty-state">Nenhum proprietário cadastrado ainda.</div>
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
              {proprietarios.map((proprietario) => (
                <tr key={proprietario.idPessoa}>
                  <td>{proprietario.nomePessoa}</td>
                  <td>{proprietario.emailPessoa}</td>
                  <td>
                    <div className="table__actions">
                      <Button
                        variant="secondary"
                        size="sm"
                        onClick={() => navigate(`/proprietarios/${proprietario.idPessoa}`)}
                      >
                        Gerenciar equinos
                      </Button>
                      <Button variant="danger" size="sm" onClick={() => excluir(proprietario)}>
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
