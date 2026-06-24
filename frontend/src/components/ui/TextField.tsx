import type { InputHTMLAttributes } from "react";

interface TextFieldProps extends InputHTMLAttributes<HTMLInputElement> {
  label: string;
  error?: string;
}

export function TextField({ label, error, id, name, ...props }: TextFieldProps) {
  const inputId = id ?? name;
  return (
    <div className="field">
      <label htmlFor={inputId}>{label}</label>
      <input id={inputId} name={name} className={error ? "has-error" : ""} {...props} />
      {error && <span className="field__error">{error}</span>}
    </div>
  );
}
