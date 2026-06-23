import { useEffect, useState } from "react";
import { fetchPing, type PingResponse } from "./api/ping";

function App() {
  const [data, setData] = useState<PingResponse | null>(null);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    fetchPing()
      .then(setData)
      .catch((err) => setError(err.message));
  }, []);

  return (
    <div style={{ fontFamily: "sans-serif", padding: "2rem" }}>
      <h1>Haras — Status da Integração</h1>
      {error && <p style={{ color: "red" }}>Erro: {error}</p>}
      {!error && !data && <p>Carregando...</p>}
      {data && (
        <ul>
          <li>Status: {data.status}</li>
          <li>Hora do banco: {data.dbTime}</li>
          <li>Total de pessoas cadastradas: {data.pessoaCount}</li>
        </ul>
      )}
    </div>
  );
}

export default App;
