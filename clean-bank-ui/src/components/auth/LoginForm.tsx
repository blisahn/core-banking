"use client";

import { useState } from 'react';
import { useAuth } from '@/contexts/AuthContext';
import { api } from '@/lib/api';
import { Input, Button, Alert } from '@/components/ui';
import type { AuthToken } from '@/types';
import Link from 'next/link';

export default function LoginForm() {
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [error, setError] = useState('');
  const [isLoading, setIsLoading] = useState(false);
  const { login } = useAuth();

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setError('');
    setIsLoading(true);
    try {
      const data = await api.post<AuthToken>('/auth/login', { email, password });
      if (data?.accessToken) login(data.accessToken);
    } catch (err: unknown) {
      setError(err instanceof Error ? err.message : 'Login failed.');
    } finally {
      setIsLoading(false);
    }
  };

  return (
    <div className="w-full max-w-md p-8 glass-panel rounded-2xl">
      <div className="text-center mb-8">
        <h1 className="text-3xl font-bold tracking-tight mb-2 text-gradient">Welcome Back</h1>
        <p className="text-slate-400 text-sm">Sign in to your CleanBank account</p>
      </div>

      <form onSubmit={handleSubmit} className="space-y-6">
        {error && <Alert type="error" message={error} />}
        <Input id="email" label="Email" type="email" required value={email} onChange={(e) => setEmail(e.target.value)} placeholder="john@example.com" />
        <Input id="password" label="Password" type="password" required value={password} onChange={(e) => setPassword(e.target.value)} placeholder="••••••••" />
        <Button type="submit" fullWidth loading={isLoading} loadingText="Verifying...">Sign In</Button>
        <p className="text-center text-slate-400 text-sm mt-6">
          Don&apos;t have an account?{' '}
          <Link href="/register" className="text-emerald-400 hover:text-emerald-300 font-medium transition-colors">Create one</Link>
        </p>
      </form>
    </div>
  );
}
