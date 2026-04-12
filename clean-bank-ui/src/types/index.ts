export interface AuthToken {
  accessToken: string;
  expiresIn: number;
}

export type UserRole = "ADMIN" | "EMPLOYEE" | "CUSTOMER";

export interface UserSummary {
  id: ValueId;
  email: string;
  role: UserRole;
  customerId: ValueId | null;
}

export type AccountType = "CHECKING" | "SAVINGS" | "INVESTMENT";
export type AccountStatus = "PENDING" | "ACTIVE" | "FROZEN" | "CLOSED";
export type TransactionType = "DEPOSIT" | "WITHDRAWAL" | "TRANSFER";
export type TransactionStatus = "PENDING" | "COMPLETED" | "FAILED";
export type CustomerStatus = "ACTIVE" | "SUSPENDED" | "CLOSED";

export interface Money {
  amount: number;
  currency: string;
}

export interface ValueId {
  value: string;
}

export interface AccountSummary {
  id: ValueId;
  accountNumber: string;
  balance: Money;
  customerName: string;
  type: AccountType;
  status: AccountStatus;
  createdAt: string;
}

export interface TransactionSummary {
  id: ValueId;
  sourceAccountId: ValueId | null;
  targetAccountId: ValueId | null;
  amount: number;
  currency: string;
  type: TransactionType;
  status: TransactionStatus;
  description: string;
  timestamp: string;
}

export interface CustomerSummary {
  id: ValueId;
  firstName: string;
  lastName: string;
  status: CustomerStatus;
}

export interface PersonalInfo {
  firstName: string;
  lastName: string;
  email: string;
  dateOfBirth: string;
}

export interface Address {
  street: string;
  district: string;
}

export interface PagedResult<T> {
  content: T[];
  page: number;
  size: number;
  totalElements: number;
  totalPages: number;
  hasNext: boolean;
  hasPrevious: boolean;
}

export interface TransactionStatsSummary {
  totalDeposits: number;
  totalWithdrawals: number;
  totalTransfersIn: number;
  totalTransfersOut: number;
  transactionCount: number;
  netFlow: number;
}

export interface ApiResponse<T> {
  success: boolean;
  data: T;
  errorMessage?: string;
  errorCode?: string;
}

export type AuditSeverity = "INFO" | "WARNING" | "CRITICAL";

export interface AuditEvent {
  id: string;
  eventType: string;
  aggregateType: string;
  aggregateId: string;
  actorId: string | null;
  actorRole: string | null;
  summary: string;
  severity: AuditSeverity;
  occurredOn: string;
}
