"use client";

import { useState } from 'react';
import { Modal, Input, Select, Button } from '@/components/ui';
import { api } from '@/lib/api';
import { useAuth } from '@/contexts/AuthContext';
import toast from 'react-hot-toast';

type ActionType = 'OPEN' | 'DEPOSIT' | 'WITHDRAW' | 'TRANSFER';

const CURRENCY_OPTIONS = [
  { value: 'TRY', label: 'TRY' },
  { value: 'USD', label: 'USD' },
  { value: 'EUR', label: 'EUR' },
];

const ACCOUNT_TYPE_OPTIONS = [
  { value: 'CHECKING', label: 'Checking' },
  { value: 'SAVINGS', label: 'Savings' },
  { value: 'INVESTMENT', label: 'Investment' },
];

const TITLES: Record<ActionType, string> = {
  OPEN: 'Open New Account',
  DEPOSIT: 'Deposit Funds',
  WITHDRAW: 'Withdraw Funds',
  TRANSFER: 'Transfer Funds',
};

interface AccountActionModalProps {
  type: ActionType;
  accountId?: string;
  open: boolean;
  onClose: () => void;
  onSuccess: () => void;
}

export default function AccountActionModal({ type, accountId, open, onClose, onSuccess }: AccountActionModalProps) {
  const { user } = useAuth();
  const [amount, setAmount] = useState('');
  const [currency, setCurrency] = useState('TRY');
  const [description, setDescription] = useState('');
  const [targetAccountNumber, setTargetAccountNumber] = useState('');
  const [accountType, setAccountType] = useState('CHECKING');
  const [isSubmitting, setIsSubmitting] = useState(false);

  const reset = () => {
    setAmount('');
    setCurrency('TRY');
    setDescription('');
    setTargetAccountNumber('');
    setAccountType('CHECKING');
  };

  const handleClose = () => {
    reset();
    onClose();
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setIsSubmitting(true);
    try {
      if (type === 'OPEN') {
        await api.post('/accounts', { customerId: user?.id, accountType, currency });
        toast.success('Account opened successfully');
      } else {
        const payload: Record<string, unknown> = { amount: parseFloat(amount), currency, description };
        if (type === 'TRANSFER') {
          payload.targetAccountNumber = targetAccountNumber;
          await api.post(`/accounts/${accountId}/transfer`, payload);
          toast.success('Transfer completed');
        } else if (type === 'DEPOSIT') {
          await api.post(`/accounts/${accountId}/deposit`, payload);
          toast.success('Deposit successful');
        } else {
          await api.post(`/accounts/${accountId}/withdraw`, payload);
          toast.success('Withdrawal successful');
        }
      }
      handleClose();
      onSuccess();
    } catch (err: unknown) {
      const message = err instanceof Error ? err.message : 'Operation failed';
      toast.error(message);
    } finally {
      setIsSubmitting(false);
    }
  };

  return (
    <Modal open={open} onClose={handleClose} title={TITLES[type]}>
      <form onSubmit={handleSubmit} className="space-y-5">
        {type === 'OPEN' ? (
          <>
            <Select id="accountType" label="Account Type" options={ACCOUNT_TYPE_OPTIONS} value={accountType} onChange={(e) => setAccountType(e.target.value)} />
            <Select id="currency" label="Currency" options={CURRENCY_OPTIONS} value={currency} onChange={(e) => setCurrency(e.target.value)} />
          </>
        ) : (
          <>
            <Input id="amount" label="Amount" type="number" step="0.01" min="0.01" required value={amount} onChange={(e) => setAmount(e.target.value)} placeholder="0.00" />
            <Select id="currency" label="Currency" options={CURRENCY_OPTIONS} value={currency} onChange={(e) => setCurrency(e.target.value)} />
            <Input id="description" label="Description" type="text" required value={description} onChange={(e) => setDescription(e.target.value)} placeholder="What is this for?" />
            {type === 'TRANSFER' && (
              <Input id="iban" label="Target IBAN" type="text" required value={targetAccountNumber} onChange={(e) => setTargetAccountNumber(e.target.value.toUpperCase())} placeholder="TR123456789012345678901234" maxLength={26} className="font-mono text-sm" />
            )}
          </>
        )}
        <Button type="submit" fullWidth loading={isSubmitting} loadingText="Processing...">
          Confirm
        </Button>
      </form>
    </Modal>
  );
}
