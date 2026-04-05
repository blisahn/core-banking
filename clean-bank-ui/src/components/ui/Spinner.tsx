"use client";

interface SpinnerProps {
  className?: string;
  size?: 'sm' | 'md' | 'lg';
}

const sizeMap = { sm: 'w-5 h-5', md: 'w-8 h-8', lg: 'w-12 h-12' };

export default function Spinner({ className = '', size = 'md' }: SpinnerProps) {
  return (
    <div className={`${sizeMap[size]} border-4 border-primary-500/30 border-t-primary-500 rounded-full animate-spin ${className}`} />
  );
}
