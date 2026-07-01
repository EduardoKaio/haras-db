import { NavLink, Outlet, useNavigate } from "react-router-dom";
import { useAuth } from "../auth/AuthContext";

export function AppLayout() {
  const { user, logout } = useAuth();
  const navigate = useNavigate();

  function handleLogout() {
    logout();
    navigate("/login", { replace: true });
  }

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
        <div className="app-header__user">
          {user && <span className="app-header__email">{user.email}</span>}
          <button type="button" className="app-header__logout" onClick={handleLogout}>
            Sair
          </button>
        </div>
      </header>
      <main className="app-main">
        <Outlet />
      </main>
    </div>
  );
}
