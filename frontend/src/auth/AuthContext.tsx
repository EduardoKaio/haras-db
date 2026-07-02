import { createContext, useContext, useEffect, useMemo, useState, type ReactNode } from "react";
import { Navigate, useLocation } from "react-router-dom";
import { setAuthToken, setUnauthorizedHandler } from "../api/http";
import { authApi, type LoginInput } from "../api/auth";

interface SessionUser {
  idPessoa: number;
  email: string;
  roles: string[];
}

interface AuthState {
  user: SessionUser | null;
  login: (data: LoginInput) => Promise<void>;
  logout: () => void;
  hasRole: (role: string) => boolean;
}

const TOKEN_KEY = "haras.token";
const USER_KEY = "haras.user";

const AuthContext = createContext<AuthState | null>(null);

export function AuthProvider({ children }: { children: ReactNode }) {
  const [user, setUser] = useState<SessionUser | null>(() => {
    const raw = localStorage.getItem(USER_KEY);
    return raw ? (JSON.parse(raw) as SessionUser) : null;
  });

  function clearSession() {
    localStorage.removeItem(TOKEN_KEY);
    localStorage.removeItem(USER_KEY);
    setAuthToken(null);
    setUser(null);
  }

  // 401 vindo da API (token expirado) => encerra a sessão; RequireAuth redireciona para /login.
  useEffect(() => {
    setUnauthorizedHandler(clearSession);
    return () => setUnauthorizedHandler(null);
  }, []);

  async function login(data: LoginInput) {
    const res = await authApi.login(data);
    const sessionUser: SessionUser = { idPessoa: res.idPessoa, email: res.email, roles: res.roles };
    localStorage.setItem(TOKEN_KEY, res.token);
    localStorage.setItem(USER_KEY, JSON.stringify(sessionUser));
    setAuthToken(res.token);
    setUser(sessionUser);
  }

  const value = useMemo<AuthState>(
    () => ({
      user,
      login,
      logout: clearSession,
      hasRole: (role: string) => {
        if (!user) return false;
        const full = role.startsWith("ROLE_") ? role : `ROLE_${role}`;
        return user.roles.includes(full);
      },
    }),
    [user],
  );

  return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>;
}

// eslint-disable-next-line react-refresh/only-export-components
export function useAuth() {
  const ctx = useContext(AuthContext);
  if (!ctx) {
    throw new Error("useAuth deve ser usado dentro de AuthProvider");
  }
  return ctx;
}

export function RequireAuth({ children }: { children: ReactNode }) {
  const { user } = useAuth();
  const location = useLocation();
  if (!user) {
    return <Navigate to="/login" replace state={{ from: location }} />;
  }
  return <>{children}</>;
}

/** Bloqueia a rota se o usuário não tiver o papel exigido, redirecionando para `redirect`. */
export function RequireRole({
  role,
  children,
  redirect = "/",
}: {
  role: string;
  children: ReactNode;
  redirect?: string;
}) {
  const { hasRole } = useAuth();
  if (!hasRole(role)) {
    return <Navigate to={redirect} replace />;
  }
  return <>{children}</>;
}
