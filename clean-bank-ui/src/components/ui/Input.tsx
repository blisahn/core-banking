"use client";

import type { InputHTMLAttributes, ReactNode } from "react";

interface InputProps extends InputHTMLAttributes<HTMLInputElement> {
  label?: string;
  icon?: ReactNode;
}

export default function Input({
  label,
  icon,
  id,
  className = "",
  ...props
}: InputProps) {
  return (
    <div className="space-y-2">
      {label && (
        <label
          htmlFor={id}
          className="text-sm font-medium text-slate-300 flex items-center"
        >
          {icon && <span className="mr-1.5 opacity-70">{icon}</span>}
          {label}
        </label>
      )}
      <input
        id={id}
        className={`w-full bg-slate-950/50 border border-slate-700/50 rounded-lg px-4 py-3 text-slate-100 placeholder:text-slate-500 focus:outline-none focus:ring-2 focus:ring-primary-500/50 focus:border-primary-500 transition-all ${className}`}
        {...props}
      />
    </div>
  );
}
