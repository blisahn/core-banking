import { api } from '@/lib/api';

export const employeeService = {
  // Account state operations
  freezeAccount: (id: string) => api.patch<void>(`/employees/accounts/${id}/freeze`),
  activateAccount: (id: string) => api.patch<void>(`/employees/accounts/${id}/activate`),
  closeAccount: (id: string) => api.patch<void>(`/employees/accounts/${id}/close`),

  // Customer state operations
  suspendCustomer: (id: string) => api.patch<void>(`/employees/customers/${id}/suspend`),
  activateCustomer: (id: string) => api.patch<void>(`/employees/customers/${id}/activate`),
  closeCustomer: (id: string) => api.patch<void>(`/employees/customers/${id}/close`),

  // Register customer on behalf
  registerCustomer: (data: {
    firstName: string; lastName: string; email: string;
    dateOfBirth: string; street: string; district: string; password: string;
  }) => api.post<void>('/employees/customers', data),
};
