import { api } from '@/lib/api';
import type { UserSummary, CustomerSummary, TransactionSummary, PagedResult } from '@/types';

export const adminService = {
  createStaffUser: (data: { email: string; password: string; role: string }) =>
    api.post<void>('/admin/users', data),

  getAllUsers: () =>
    api.get<UserSummary[]>('/admin/users'),

  getAllCustomers: () =>
    api.get<CustomerSummary[]>('/admin/customers'),

  getAllTransactions: (page: number = 0, size: number = 20) =>
    api.get<PagedResult<TransactionSummary>>(`/admin/transactions?page=${page}&size=${size}`),
};
