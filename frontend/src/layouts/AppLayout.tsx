import { NavLink, Outlet } from "react-router-dom";

export function AppLayout() {
  return (
    <div className="app-shell">
      <header className="app-header">
        <div className="app-header__brand">🐴 Haras</div>
        <nav className="app-header__nav">
          <NavLink to="/pessoas" className={({ isActive }) => (isActive ? "active" : "")}>
            Pessoas
          </NavLink>
          <NavLink to="/medicos-veterinarios" className={({ isActive }) => (isActive ? "active" : "")}>
            Médicos Veterinários
          </NavLink>
          <NavLink to="/colaboradores" className={({ isActive }) => (isActive ? "active" : "")}>
            Colaboradores
          </NavLink>
          <NavLink to="/contratos" className={({ isActive }) => (isActive ? "active" : "")}>
            Contratos
          </NavLink>
          <NavLink to="/equinos" className={({ isActive }) => (isActive ? "active" : "")}>
            Equinos
          </NavLink>
          <NavLink to="/proprietarios" className={({ isActive }) => (isActive ? "active" : "")}>
            Proprietários
          </NavLink>
        </nav>
      </header>
      <main className="app-main">
        <Outlet />
      </main>
    </div>
  );
}
