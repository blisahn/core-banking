"use client";

import { useEffect, useState, useCallback } from 'react';
import { adminService } from '@/lib/services/admin';
import { useEventStream } from '@/lib/hooks/useEventStream';
import type { AuditEvent, PagedResult } from '@/types';
import RoleGuard from '@/components/auth/RoleGuard';
import { Spinner, Select, Pagination } from '@/components/ui';

const severityStyles = {
  INFO: 'bg-slate-500/10 text-slate-400 border-slate-500/20',
  WARNING: 'bg-amber-500/10 text-amber-400 border-amber-500/20',
  CRITICAL: 'bg-rose-500/10 text-rose-400 border-rose-500/20',
};

function SeverityBadge({ severity }: { severity: string }) {
  const style = severityStyles[severity as keyof typeof severityStyles] || severityStyles.INFO;
  return (
    <span className={`text-xs font-semibold px-2.5 py-1 rounded-full border ${style}`}>
      {severity}
    </span>
  );
}

function formatTime(dateStr: string) {
  const d = new Date(dateStr);
  return d.toLocaleString('tr-TR', { day: '2-digit', month: '2-digit', year: 'numeric', hour: '2-digit', minute: '2-digit', second: '2-digit' });
}

function truncateId(id: string | null) {
  if (!id) return '-';
  return id.length > 8 ? `${id.slice(0, 8)}...` : id;
}

const aggregateOptions = [
  { value: '', label: 'All' },
  { value: 'account', label: 'Account' },
  { value: 'customer', label: 'Customer' },
  { value: 'transaction', label: 'Transaction' },
];

const severityOptions = [
  { value: '', label: 'All' },
  { value: 'INFO', label: 'INFO' },
  { value: 'WARNING', label: 'WARNING' },
  { value: 'CRITICAL', label: 'CRITICAL' },
];

export default function EventsPage() {
  const { events: liveEvents, isConnected } = useEventStream(true);
  const [data, setData] = useState<PagedResult<AuditEvent> | null>(null);
  const [page, setPage] = useState(0);
  const [isLoading, setIsLoading] = useState(true);
  const [aggregateType, setAggregateType] = useState('');
  const [severity, setSeverity] = useState('');

  const fetchEvents = useCallback(async () => {
    setIsLoading(true);
    try {
      const result = await adminService.getAuditEvents({
        page,
        size: 20,
        aggregateType: aggregateType || undefined,
        severity: severity || undefined,
      });
      setData(result);
    } catch (error) {
      console.error('Failed to fetch audit events:', error);
    } finally {
      setIsLoading(false);
    }
  }, [page, aggregateType, severity]);

  useEffect(() => {
    fetchEvents();
  }, [fetchEvents]);

  const handleFilterChange = (setter: (v: string) => void) => (e: React.ChangeEvent<HTMLSelectElement>) => {
    setter(e.target.value);
    setPage(0);
  };

  return (
    <RoleGuard allowedRoles={["ADMIN"]}>
      <div className="space-y-6">
        <div>
          <h1 className="text-3xl font-bold tracking-tight text-slate-50">
            Audit <span className="text-gradient">Events</span>
          </h1>
          <p className="text-slate-400 mt-1">Real-time event stream and history.</p>
        </div>

        {/* Live Stream Section */}
        <div className="glass-card rounded-xl overflow-hidden">
          <div className="px-6 py-4 border-b border-slate-800/50 flex items-center justify-between">
            <h2 className="text-lg font-semibold text-slate-200">Live Stream</h2>
            <div className="flex items-center space-x-2">
              <div className={`w-2 h-2 rounded-full ${isConnected ? 'bg-emerald-400 animate-pulse' : 'bg-rose-400'}`} />
              <span className={`text-xs font-medium ${isConnected ? 'text-emerald-400' : 'text-rose-400'}`}>
                {isConnected ? 'Connected' : 'Disconnected'}
              </span>
            </div>
          </div>
          <div className="max-h-64 overflow-y-auto">
            {liveEvents.length === 0 ? (
              <p className="text-sm text-slate-500 p-6 text-center">Waiting for events...</p>
            ) : (
              <table className="w-full">
                <tbody>
                  {liveEvents.map((event, i) => (
                    <tr key={`${event.id}-${i}`} className="border-b border-slate-800/30 hover:bg-slate-800/20 transition-colors">
                      <td className="px-4 py-2 text-xs text-slate-500 w-40">{formatTime(event.occurredOn)}</td>
                      <td className="px-4 py-2 w-24"><SeverityBadge severity={event.severity} /></td>
                      <td className="px-4 py-2 text-sm text-slate-300">{event.summary}</td>
                    </tr>
                  ))}
                </tbody>
              </table>
            )}
          </div>
        </div>

        {/* Filters */}
        <div className="glass-card rounded-xl p-6">
          <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
            <Select
              label="Aggregate Type"
              options={aggregateOptions}
              value={aggregateType}
              onChange={handleFilterChange(setAggregateType)}
            />
            <Select
              label="Severity"
              options={severityOptions}
              value={severity}
              onChange={handleFilterChange(setSeverity)}
            />
          </div>
        </div>

        {/* History Table */}
        <div className="glass-card rounded-xl overflow-hidden">
          <div className="px-6 py-4 border-b border-slate-800/50">
            <h2 className="text-lg font-semibold text-slate-200">History</h2>
          </div>

          {isLoading ? (
            <div className="flex h-48 items-center justify-center"><Spinner /></div>
          ) : data && data.content.length > 0 ? (
            <>
              <div className="overflow-x-auto">
                <table className="w-full">
                  <thead>
                    <tr className="border-b border-slate-800/50 text-left">
                      <th className="px-6 py-3 text-xs font-medium text-slate-400 uppercase">Time</th>
                      <th className="px-6 py-3 text-xs font-medium text-slate-400 uppercase">Severity</th>
                      <th className="px-6 py-3 text-xs font-medium text-slate-400 uppercase">Event Type</th>
                      <th className="px-6 py-3 text-xs font-medium text-slate-400 uppercase">Summary</th>
                      <th className="px-6 py-3 text-xs font-medium text-slate-400 uppercase">Aggregate</th>
                      <th className="px-6 py-3 text-xs font-medium text-slate-400 uppercase">Actor</th>
                    </tr>
                  </thead>
                  <tbody>
                    {data.content.map((event) => (
                      <tr key={event.id} className="border-b border-slate-800/30 hover:bg-slate-800/20 transition-colors">
                        <td className="px-6 py-3 text-sm text-slate-400 whitespace-nowrap">{formatTime(event.occurredOn)}</td>
                        <td className="px-6 py-3"><SeverityBadge severity={event.severity} /></td>
                        <td className="px-6 py-3 text-sm text-slate-300 font-mono text-xs">{event.eventType}</td>
                        <td className="px-6 py-3 text-sm text-slate-200">{event.summary}</td>
                        <td className="px-6 py-3 text-sm text-slate-400">
                          <span className="capitalize">{event.aggregateType}</span>
                          <span className="text-slate-600 ml-1 font-mono text-xs">{truncateId(event.aggregateId)}</span>
                        </td>
                        <td className="px-6 py-3 text-sm text-slate-400">
                          {event.actorRole ? (
                            <span className="text-xs font-medium">{event.actorRole}</span>
                          ) : (
                            <span className="text-slate-600">System</span>
                          )}
                        </td>
                      </tr>
                    ))}
                  </tbody>
                </table>
              </div>
              <Pagination
                page={data.page}
                totalPages={data.totalPages}
                totalElements={data.totalElements}
                onPageChange={setPage}
              />
            </>
          ) : (
            <p className="text-sm text-slate-500 p-6 text-center">No audit events found.</p>
          )}
        </div>
      </div>
    </RoleGuard>
  );
}
