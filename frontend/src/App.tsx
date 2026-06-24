import { Navigate, Route, Routes } from "react-router-dom";
import { AppLayout } from "./layouts/AppLayout";
import { PessoasListPage } from "./pages/PessoasListPage";
import { PessoaFormPage } from "./pages/PessoaFormPage";
import { MedicosVeterinariosListPage } from "./pages/MedicosVeterinariosListPage";
import { MedicoVeterinarioFormPage } from "./pages/MedicoVeterinarioFormPage";
import { ColaboradoresListPage } from "./pages/ColaboradoresListPage";
import { ColaboradorFormPage } from "./pages/ColaboradorFormPage";
import { ContratosListPage } from "./pages/ContratosListPage";
import { ContratoFormPage } from "./pages/ContratoFormPage";
import { EquinosListPage } from "./pages/EquinosListPage";
import { EquinoFormPage } from "./pages/EquinoFormPage";
import { ProprietariosListPage } from "./pages/ProprietariosListPage";
import { ProprietarioFormPage } from "./pages/ProprietarioFormPage";

function App() {
  return (
    <Routes>
      <Route element={<AppLayout />}>
        <Route path="/" element={<Navigate to="/pessoas" replace />} />
        <Route path="/pessoas" element={<PessoasListPage />} />
        <Route path="/pessoas/novo" element={<PessoaFormPage />} />
        <Route path="/pessoas/:id/editar" element={<PessoaFormPage />} />

        <Route path="/medicos-veterinarios" element={<MedicosVeterinariosListPage />} />
        <Route path="/medicos-veterinarios/novo" element={<MedicoVeterinarioFormPage />} />
        <Route path="/medicos-veterinarios/:idPessoa/editar" element={<MedicoVeterinarioFormPage />} />

        <Route path="/colaboradores" element={<ColaboradoresListPage />} />
        <Route path="/colaboradores/novo" element={<ColaboradorFormPage />} />

        <Route path="/contratos" element={<ContratosListPage />} />
        <Route path="/contratos/novo" element={<ContratoFormPage />} />
        <Route path="/contratos/:id/editar" element={<ContratoFormPage />} />

        <Route path="/equinos" element={<EquinosListPage />} />
        <Route path="/equinos/novo" element={<EquinoFormPage />} />
        <Route path="/equinos/:id/editar" element={<EquinoFormPage />} />

        <Route path="/proprietarios" element={<ProprietariosListPage />} />
        <Route path="/proprietarios/novo" element={<ProprietarioFormPage />} />
        <Route path="/proprietarios/:idPessoa" element={<ProprietarioFormPage />} />
      </Route>
    </Routes>
  );
}

export default App;
