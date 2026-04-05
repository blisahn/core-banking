"use client";

import { X } from 'lucide-react';
import { useEffect, type ReactNode } from 'react';

interface ModalProps {
  open: boolean;
  onClose: () => void;
  title: string;
  children: ReactNode;
}

export default function Modal({ open, onClose, title, children }: ModalProps) {
  useEffect(() => {
    if (!open) return;
    const handler = (e: KeyboardEvent) => { if (e.key === 'Escape') onClose(); };
    document.addEventListener('keydown', handler);
    return () => document.removeEventListener('keydown', handler);
  }, [open, onClose]);

  if (!open) return null;

  return (
    <div className="fixed inset-0 z-50 flex items-center justify-center p-4 bg-slate-950/80 backdrop-blur-sm animate-in fade-in" onClick={onClose}>
      <div className="glass-panel w-full max-w-md rounded-2xl overflow-hidden shadow-2xl relative animate-in slide-in-from-bottom-8" onClick={(e) => e.stopPropagation()}>
        <div className="flex justify-between items-center p-6 border-b border-slate-800/50">
          <h2 className="text-xl font-bold text-slate-50">{title}</h2>
          <button onClick={onClose} className="text-slate-400 hover:text-slate-200 transition-colors bg-slate-800/50 hover:bg-slate-700/50 rounded-full p-2">
            <X className="w-5 h-5" />
          </button>
        </div>
        <div className="p-6">
          {children}
        </div>
      </div>
    </div>
  );
}
