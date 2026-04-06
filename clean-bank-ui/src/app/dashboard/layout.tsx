"use client";

import { useAuth } from '@/contexts/AuthContext';
import Link from 'next/link';
import { usePathname } from 'next/navigation';
import { LayoutDashboard, Wallet, ArrowLeftRight, User, Users, UserCheck, LogOut, Menu, X } from 'lucide-react';
import { useState, useMemo } from 'react';
import { Badge } from '@/components/ui';

export default function DashboardLayout({ children }: { children: React.ReactNode }) {
  const { logout, user } = useAuth();
  const pathname = usePathname();
  const [mobileOpen, setMobileOpen] = useState(false);

  const navigation = useMemo(() => {
    const role = user?.role;

    if (role === 'ADMIN') {
      return [
        { name: 'Overview', href: '/dashboard', icon: LayoutDashboard },
        { name: 'Transactions', href: '/dashboard/transactions', icon: ArrowLeftRight },
        { name: 'Users', href: '/dashboard/users', icon: Users },
        { name: 'Customers', href: '/dashboard/customers', icon: UserCheck },
      ];
    }

    if (role === 'EMPLOYEE') {
      return [
        { name: 'Overview', href: '/dashboard', icon: LayoutDashboard },
        { name: 'Customers', href: '/dashboard/customers', icon: UserCheck },
      ];
    }

    return [
      { name: 'Overview', href: '/dashboard', icon: LayoutDashboard },
      { name: 'Accounts', href: '/dashboard/accounts', icon: Wallet },
      { name: 'Profile', href: '/dashboard/profile', icon: User },
    ];
  }, [user?.role]);

  const roleBadge = user?.role === 'ADMIN' || user?.role === 'EMPLOYEE' ? user.role : null;

  return (
    <div className="min-h-screen bg-slate-950">
      {/* Mobile nav header */}
      <div className="lg:hidden flex items-center justify-between p-4 bg-slate-900 border-b border-slate-800">
        <div className="flex items-center space-x-2">
          <div className="w-8 h-8 rounded-lg bg-gradient-to-br from-primary-500 to-emerald-500 flex items-center justify-center">
            <Wallet className="w-5 h-5 text-white" />
          </div>
          <span className="text-xl font-bold bg-clip-text text-transparent bg-gradient-to-r from-primary-400 to-emerald-400">
            CleanBank
          </span>
        </div>
        <button onClick={() => setMobileOpen(!mobileOpen)} className="text-slate-300">
          {mobileOpen ? <X /> : <Menu />}
        </button>
      </div>

      <div className="flex">
        {/* Sidebar */}
        <div className={`${mobileOpen ? 'block' : 'hidden'} lg:block fixed lg:static inset-0 z-50 lg:z-auto bg-slate-950 lg:bg-transparent`}>
          <div className="w-64 h-full border-r border-slate-800/50 bg-slate-900/40 backdrop-blur-xl flex flex-col p-4">
            <div className="hidden lg:flex items-center space-x-3 px-2 py-4 mb-6">
              <div className="w-10 h-10 rounded-xl bg-gradient-to-br from-primary-500 to-emerald-500 flex items-center justify-center shadow-lg shadow-primary-500/20">
                <Wallet className="w-6 h-6 text-white" />
              </div>
              <div>
                <span className="text-2xl font-bold tracking-tight bg-clip-text text-transparent bg-gradient-to-r from-primary-400 to-emerald-400">
                  CleanBank
                </span>
                {roleBadge && (
                  <div className="mt-1">
                    <Badge status={roleBadge} />
                  </div>
                )}
              </div>
            </div>

            <nav className="flex-1 space-y-1">
              {navigation.map((item) => {
                const isActive = pathname === item.href || (item.href !== '/dashboard' && pathname.startsWith(item.href));
                return (
                  <Link
                    key={item.name}
                    href={item.href}
                    onClick={() => setMobileOpen(false)}
                    className={`flex items-center space-x-3 px-3 py-3 rounded-lg transition-all duration-200 group ${
                      isActive
                        ? 'bg-primary-500/10 text-primary-400 relative'
                        : 'text-slate-400 hover:bg-slate-800/50 hover:text-slate-200'
                    }`}
                  >
                    {isActive && (
                      <div className="absolute left-0 top-1/2 -translate-y-1/2 w-1 h-6 bg-primary-500 rounded-r-full" />
                    )}
                    <item.icon className={`w-5 h-5 ${isActive ? 'text-primary-500' : 'text-slate-500 group-hover:text-slate-400'}`} />
                    <span className="font-medium">{item.name}</span>
                  </Link>
                );
              })}
            </nav>

            <div className="mt-auto border-t border-slate-800/50 pt-4">
              <button
                onClick={logout}
                className="flex items-center space-x-3 px-3 py-3 rounded-lg text-rose-400 hover:bg-rose-500/10 w-full transition-colors"
              >
                <LogOut className="w-5 h-5" />
                <span className="font-medium">Sign Out</span>
              </button>
            </div>
          </div>
        </div>

        {/* Main Content */}
        <div className="flex-1 min-w-0" onClick={() => mobileOpen && setMobileOpen(false)}>
          <main className="p-4 lg:p-8 max-w-7xl mx-auto w-full animate-in">
            {children}
          </main>
        </div>
      </div>
    </div>
  );
}
