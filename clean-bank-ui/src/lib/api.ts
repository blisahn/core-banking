import axios, { AxiosError, type InternalAxiosRequestConfig } from 'axios';

const API_URL = 'http://localhost:8080/api';

const apiClient = axios.create({
  baseURL: API_URL,
  headers: { 'Content-Type': 'application/json' },
});

// Request interceptor: attach JWT token
apiClient.interceptors.request.use((config: InternalAxiosRequestConfig) => {
  if (typeof window !== 'undefined') {
    const token = localStorage.getItem('token');
    if (token) {
      config.headers.Authorization = `Bearer ${token}`;
    }
  }
  return config;
});

// Response interceptor: unwrap ApiResponse<T> and handle 401/403
apiClient.interceptors.response.use(
  (response) => response,
  (error: AxiosError<{ errorMessage?: string; errorCode?: string }>) => {
    if (error.response?.status === 401 || error.response?.status === 403) {
      if (typeof window !== 'undefined') {
        localStorage.removeItem('token');
        window.dispatchEvent(new Event('auth:unauthorized'));
      }
    }
    const message =
      error.response?.data?.errorMessage || error.message || 'An error occurred';
    return Promise.reject(new Error(message));
  },
);

// Unwrap { success, data, errorMessage } from backend
function unwrap<T>(response: { data: { success: boolean; data: T; errorMessage?: string } }): T {
  const body = response.data;
  if (!body.success) {
    throw new Error(body.errorMessage || 'Request failed');
  }
  return body.data;
}

export const api = {
  get: <T>(url: string) => apiClient.get<{ success: boolean; data: T; errorMessage?: string }>(url).then(unwrap),
  post: <T>(url: string, data?: unknown) => apiClient.post<{ success: boolean; data: T; errorMessage?: string }>(url, data).then(unwrap),
  put: <T>(url: string, data?: unknown) => apiClient.put<{ success: boolean; data: T; errorMessage?: string }>(url, data).then(unwrap),
  patch: <T>(url: string, data?: unknown) => apiClient.patch<{ success: boolean; data: T; errorMessage?: string }>(url, data).then(unwrap),
  delete: <T>(url: string) => apiClient.delete<{ success: boolean; data: T; errorMessage?: string }>(url).then(unwrap),
};
