import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import { type Contrato, contratosApi } from "../api/contratos";
import { ApiError } from "../api/http";
import { Button } from "../components/ui/Button";
import { Card } from "../components/ui/Card";

export function ContratosListPage() {
  const [contratos, setContratos] = useState<Contrato[] | null>(null);
  const [error, setError] = useState<string | null>(null);
  const navigate = useNavigate();

  function carregar() {
    setError(null);
    contratosApi
      .listar()
      .then(setContratos)
      .catch((err: ApiError) => setError(err.message));
  }

  useEffect(() => {
    carregar();
  }, []);

  async function excluir(contrato: Contrato) {
    if (!confirm(`Excluir contrato de ${contrato.nomeColaborador}?`)) return;
    try {
      await contratosApi.excluir(contrato.idContrato);
      carregar();
    } catch (err) {
      setError((err as ApiError).message);
    }
  }

  return (
    <>
      <div className="page-header">
        <div className="page-header__title">
          <h1>Contratos</h1>
          <span className="page-header__subtitle">Vínculos de trabalho dos colaboradores</span>
        </div>
        <Button onClick={() => navigate("/contratos/novo")}>+ Novo contrato</Button>
      </div>

      {error && <div className="alert alert-danger">{error}</div>}

      <Card>
        {contratos === null ? (
          <p>Carregando...</p>
        ) : contratos.length === 0 ? (
          <div className="empty-state">Nenhum contrato cadastrado ainda.</div>
        ) : (
          <table className="table">
            <thead>
              <tr>
                <th>Colaborador</th>
                <th>Início</th>
                <th>Fim</th>
                <th>Salário</th>
                <th></th>
              </tr>
            </thead>
            <tbody>
              {contratos.map((contrato) => (
                <tr key={contrato.idContrato}>
                  <td>{contrato.nomeColaborador}</td>
                  <td>{contrato.dataInicio}</td>
                  <td>
                    {contrato.dataFim ? (
                      contrato.dataFim
                    ) : (
                      <span className="badge badge-success">Ativo</span>
                    )}
                  </td>
                  <td>
                    {contrato.salario.toLocaleString("pt-BR", { style: "currency", currency: "BRL" })}
                  </td>
                  <td>
                    <div className="table__actions">
                      <Button
                        variant="secondary"
                        size="sm"
                        onClick={() => navigate(`/contratos/${contrato.idContrato}/editar`)}
                      >
                        Editar
                      </Button>
                      <Button variant="danger" size="sm" onClick={() => excluir(contrato)}>
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
