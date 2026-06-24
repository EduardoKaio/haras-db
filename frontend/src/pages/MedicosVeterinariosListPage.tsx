import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import { type MedicoVeterinario, medicosVeterinariosApi } from "../api/medicosVeterinarios";
import { ApiError } from "../api/http";
import { Button } from "../components/ui/Button";
import { Card } from "../components/ui/Card";

export function MedicosVeterinariosListPage() {
  const [medicos, setMedicos] = useState<MedicoVeterinario[] | null>(null);
  const [error, setError] = useState<string | null>(null);
  const navigate = useNavigate();

  function carregar() {
    setError(null);
    medicosVeterinariosApi
      .listar()
      .then(setMedicos)
      .catch((err: ApiError) => setError(err.message));
  }

  useEffect(() => {
    carregar();
  }, []);

  async function excluir(medico: MedicoVeterinario) {
    if (!confirm(`Remover ${medico.nomePessoa} do quadro de médicos veterinários?`)) return;
    try {
      await medicosVeterinariosApi.excluir(medico.idPessoa);
      carregar();
    } catch (err) {
      setError((err as ApiError).message);
    }
  }

  return (
    <>
      <div className="page-header">
        <div className="page-header__title">
          <h1>Médicos Veterinários</h1>
          <span className="page-header__subtitle">Profissionais de saúde habilitados a atender o haras</span>
        </div>
        <Button onClick={() => navigate("/medicos-veterinarios/novo")}>+ Novo médico veterinário</Button>
      </div>

      {error && <div className="alert alert-danger">{error}</div>}

      <Card>
        {medicos === null ? (
          <p>Carregando...</p>
        ) : medicos.length === 0 ? (
          <div className="empty-state">Nenhum médico veterinário cadastrado ainda.</div>
        ) : (
          <table className="table">
            <thead>
              <tr>
                <th>Nome</th>
                <th>Email</th>
                <th>CRMV</th>
                <th></th>
              </tr>
            </thead>
            <tbody>
              {medicos.map((medico) => (
                <tr key={medico.idPessoa}>
                  <td>{medico.nomePessoa}</td>
                  <td>{medico.emailPessoa}</td>
                  <td>{medico.numCrmv}/{medico.ufCrmv}</td>
                  <td>
                    <div className="table__actions">
                      <Button
                        variant="secondary"
                        size="sm"
                        onClick={() => navigate(`/medicos-veterinarios/${medico.idPessoa}/editar`)}
                      >
                        Editar
                      </Button>
                      <Button variant="danger" size="sm" onClick={() => excluir(medico)}>
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
