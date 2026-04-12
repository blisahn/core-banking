"use client";

import type { ReactNode } from "react";

interface StatsCardProps {
  title: string;
  icon: ReactNode;
  color: "primary" | "emerald" | "amber" | "cyan";
  children: ReactNode;
}

const glowColors = {
  primary: "bg-primary-500/10 group-hover:bg-primary-500/20",
  emerald: "bg-emerald-500/10 group-hover:bg-emerald-500/20",
  amber: "bg-amber-500/10 group-hover:bg-amber-500/20",
  cyan: "bg-cyan-500/10 group-hover:bg-cyan-500/20",
};

const iconBgColors = {
  primary: "bg-primary-500/20 text-primary-400",
  emerald: "bg-emerald-500/20 text-emerald-400",
  amber: "bg-amber-500/20 text-amber-400",
  cyan: "bg-cyan-500/20 text-cyan-400",
};

export default function StatsCard({
  title,
  icon,
  color,
  children,
}: StatsCardProps) {
  return (
    <div className="glass-card rounded-xl p-6 relative overflow-hidden group hover:bg-slate-800/80">
      <div
        className={`absolute top-0 right-0 w-32 h-32 rounded-full blur-[40px] -mr-16 -mt-16 transition-all duration-500 ${glowColors[color]}`}
      />
      <div className="flex items-center justify-between mb-4 relative z-10">
        <h3 className="text-slate-400 font-medium">{title}</h3>
        <div className={`p-2 rounded-lg ${iconBgColors[color]}`}>{icon}</div>
      </div>
      <div className="relative z-10">{children}</div>
    </div>
  );
}
