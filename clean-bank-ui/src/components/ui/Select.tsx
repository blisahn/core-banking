"use client";

import type { SelectHTMLAttributes } from 'react';

interface SelectOption {
  value: string;
  label: string;
}

interface SelectProps extends SelectHTMLAttributes<HTMLSelectElement> {
  label?: string;
  options: SelectOption[];
}

export default function Select({ label, options, id, className = '', ...props }: SelectProps) {
  return (
    <div className="space-y-2">
      {label && <label htmlFor={id} className="text-sm font-medium text-slate-300">{label}</label>}
      <select
        id={id}
        className={`w-full bg-slate-950/50 border border-slate-700/50 rounded-lg px-4 py-3 text-slate-100 focus:outline-none focus:ring-2 focus:ring-primary-500/50 ${className}`}
        {...props}
      >
        {options.map((opt) => (
          <option key={opt.value} value={opt.value}>{opt.label}</option>
        ))}
      </select>
    </div>
  );
}
