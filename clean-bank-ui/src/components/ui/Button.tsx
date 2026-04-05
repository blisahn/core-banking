"use client";

import Spinner from './Spinner';
import type { ReactNode, ButtonHTMLAttributes } from 'react';

type ButtonVariant = 'primary' | 'danger' | 'success' | 'ghost' | 'outline';

const variantStyles: Record<ButtonVariant, string> = {
  primary: 'bg-gradient-to-r from-primary-600 to-emerald-600 hover:from-primary-500 hover:to-emerald-500 text-white shadow-lg shadow-primary-900/40',
  danger: 'bg-rose-500/10 hover:bg-rose-500/20 text-rose-400',
  success: 'bg-emerald-500/10 hover:bg-emerald-500/20 text-emerald-400',
  ghost: 'hover:bg-slate-800 text-slate-300 hover:text-slate-100',
  outline: 'border border-slate-700 hover:border-slate-600 hover:bg-slate-800/50 text-slate-300',
};

interface ButtonProps extends ButtonHTMLAttributes<HTMLButtonElement> {
  variant?: ButtonVariant;
  loading?: boolean;
  loadingText?: string;
  icon?: ReactNode;
  fullWidth?: boolean;
}

export default function Button({
  variant = 'primary',
  loading = false,
  loadingText,
  icon,
  fullWidth = false,
  children,
  className = '',
  disabled,
  ...props
}: ButtonProps) {
  return (
    <button
      disabled={disabled || loading}
      className={`
        flex items-center justify-center font-medium py-2.5 px-4 rounded-lg transition-all duration-200
        disabled:opacity-50 disabled:cursor-not-allowed
        ${variantStyles[variant]}
        ${fullWidth ? 'w-full' : ''}
        ${className}
      `}
      {...props}
    >
      {loading ? (
        <div className="flex items-center space-x-2">
          <Spinner size="sm" />
          {loadingText && <span>{loadingText}</span>}
        </div>
      ) : (
        <>
          {icon && <span className="mr-2">{icon}</span>}
          {children}
        </>
      )}
    </button>
  );
}
