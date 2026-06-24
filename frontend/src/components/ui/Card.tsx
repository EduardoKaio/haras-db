import type { ReactNode } from "react";

export function Card({ children }: { children: ReactNode }) {
  return (
    <div className="card">
      <div className="card__body">{children}</div>
    </div>
  );
}
