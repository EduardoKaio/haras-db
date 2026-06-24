import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import { type Pessoa, pessoasApi } from "../api/pessoas";
import { ApiError } from "../api/http";
import { Button } from "../components/ui/Button";
import { Card } from "../components/ui/Card";

export function PessoasListPage() {
  const [pessoas, setPessoas] = useState<Pessoa[] | null>(null);
  const [error, setError] = useState<string | null>(null);
  const navigate = useNavigate();

  function carregar() {
    setError(null);
    pessoasApi
      .listar()
      .then(setPessoas)
      .catch((err: ApiError) => setError(err.message));
  }

  useEffect(() => {
    carregar();
  }, []);

  async function excluir(pessoa: Pessoa) {
    if (!confirm(`Excluir ${pessoa.nome}?`)) return;
    try {
      await pessoasApi.excluir(pessoa.idPessoa);
      carregar();
    } catch (err) {
      setError((err as ApiError).message);
    }
  }

  return (
    <>
      <div className="page-header">
        <div className="page-header__title">
          <h1>Pessoas</h1>
          <span className="page-header__subtitle">Cadastro base de pessoas do haras</span>
        </div>
        <Button onClick={() => navigate("/pessoas/novo")}>+ Nova pessoa</Button>
      </div>

      {error && <div className="alert alert-danger">{error}</div>}

      <Card>
        {pessoas === null ? (
          <p>Carregando...</p>
        ) : pessoas.length === 0 ? (
          <div className="empty-state">Nenhuma pessoa cadastrada ainda.</div>
        ) : (
          <table className="table">
            <thead>
              <tr>
                <th>Nome</th>
                <th>CPF</th>
                <th>Email</th>
                <th>Perfil</th>
                <th></th>
              </tr>
            </thead>
            <tbody>
              {pessoas.map((pessoa) => (
                <tr key={pessoa.idPessoa}>
                  <td>{pessoa.nome}</td>
                  <td>{pessoa.cpf}</td>
                  <td>{pessoa.email}</td>
                  <td>
                    <span className={`badge ${pessoa.gerente ? "badge-success" : "badge-muted"}`}>
                      {pessoa.gerente ? "Gerente" : "Padrão"}
                    </span>
                  </td>
                  <td>
                    <div className="table__actions">
                      <Button
                        variant="secondary"
                        size="sm"
                        onClick={() => navigate(`/pessoas/${pessoa.idPessoa}/editar`)}
                      >
                        Editar
                      </Button>
                      <Button variant="danger" size="sm" onClick={() => excluir(pessoa)}>
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
