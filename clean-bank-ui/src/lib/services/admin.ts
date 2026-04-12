import { api } from '@/lib/api';
import type { UserSummary, CustomerSummary, TransactionSummary, AuditEvent, PagedResult } from '@/types';

export const adminService = {
  createStaffUser: (data: { email: string; password: string; role: string }) =>
    api.post<void>('/admin/users', data),

  getAllUsers: () =>
    api.get<UserSummary[]>('/admin/users'),

  getAllCustomers: () =>
    api.get<CustomerSummary[]>('/admin/customers'),

  getAllTransactions: (page: number = 0, size: number = 20) =>
    api.get<PagedResult<TransactionSummary>>(`/admin/transactions?page=${page}&size=${size}`),

  getAuditEvents: (params: {
    page?: number;
    size?: number;
    aggregateType?: string;
    severity?: string;
    from?: string;
    to?: string;
  } = {}) => {
    const query = new URLSearchParams();
    if (params.page !== undefined) query.set('page', String(params.page));
    if (params.size !== undefined) query.set('size', String(params.size));
    if (params.aggregateType) query.set('aggregateType', params.aggregateType);
    if (params.severity) query.set('severity', params.severity);
    if (params.from) query.set('from', params.from);
    if (params.to) query.set('to', params.to);
    return api.get<PagedResult<AuditEvent>>(`/admin/events?${query.toString()}`);
  },
};
