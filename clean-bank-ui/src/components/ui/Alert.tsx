"use client";

import { ShieldAlert, CheckCircle2 } from 'lucide-react';

interface AlertProps {
  type: 'error' | 'success';
  message: string;
}

export default function Alert({ type, message }: AlertProps) {
  if (!message) return null;

  const isError = type === 'error';
  return (
    <div className={`${isError ? 'bg-rose-500/10 border-rose-500/50 text-rose-400' : 'bg-emerald-500/10 border-emerald-500/50 text-emerald-400'} border px-4 py-3 rounded-lg text-sm flex items-start`}>
      {isError ? <ShieldAlert className="w-5 h-5 mr-3 shrink-0" /> : <CheckCircle2 className="w-5 h-5 mr-3 shrink-0" />}
      <p>{message}</p>
    </div>
  );
}
