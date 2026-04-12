"use client";

import { useEffect, useRef, useState, useCallback } from 'react';
import type { AuditEvent } from '@/types';

const SSE_URL = 'http://localhost:8080/api/admin/events/stream';
const MAX_LIVE_EVENTS = 50;
const RECONNECT_DELAY = 3000;

export function useEventStream(enabled: boolean) {
  const [events, setEvents] = useState<AuditEvent[]>([]);
  const [isConnected, setIsConnected] = useState(false);
  const abortRef = useRef<AbortController | null>(null);

  const connect = useCallback(() => {
    const token = localStorage.getItem('token');
    if (!token) return;

    const controller = new AbortController();
    abortRef.current = controller;

    fetch(SSE_URL, {
      headers: { 'Authorization': `Bearer ${token}` },
      signal: controller.signal,
    })
      .then((response) => {
        if (!response.ok || !response.body) {
          throw new Error(`SSE connection failed: ${response.status}`);
        }

        setIsConnected(true);
        const reader = response.body.getReader();
        const decoder = new TextDecoder();
        let buffer = '';

        function read(): Promise<void> {
          return reader.read().then(({ done, value }) => {
            if (done) {
              setIsConnected(false);
              setTimeout(connect, RECONNECT_DELAY);
              return;
            }

            buffer += decoder.decode(value, { stream: true });
            const lines = buffer.split('\n');
            buffer = lines.pop() || '';

            for (const line of lines) {
              if (line.startsWith('data:')) {
                const jsonStr = line.slice(5).trim();
                if (jsonStr) {
                  try {
                    const event: AuditEvent = JSON.parse(jsonStr);
                    setEvents((prev) => [event, ...prev].slice(0, MAX_LIVE_EVENTS));
                  } catch {
                    // skip malformed JSON
                  }
                }
              }
            }

            return read();
          });
        }

        return read();
      })
      .catch((err) => {
        if (err.name === 'AbortError') return;
        setIsConnected(false);
        setTimeout(connect, RECONNECT_DELAY);
      });
  }, []);

  useEffect(() => {
    if (!enabled) return;

    connect();

    return () => {
      abortRef.current?.abort();
      setIsConnected(false);
    };
  }, [enabled, connect]);

  return { events, isConnected };
}
