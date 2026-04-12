package com.devblo.infrastructure.events.sse;

import com.devblo.audit.AuditEventSummary;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Component
public class SseEmitterManager {
    private final List<SseEmitter> emitters = new CopyOnWriteArrayList<>();

    public SseEmitter create(long timeout) {
        SseEmitter emitter = new SseEmitter(timeout);
        this.emitters.add(emitter);

        emitter.onCompletion(() -> {
            this.emitters.remove(emitter);
        });

        emitter.onTimeout(() -> {
            emitter.complete();
            this.emitters.remove(emitter);
        });

        emitter.onError((ex) -> {
            this.emitters.remove(emitter);
        });

        return emitter;
    }

    public void broadcast(AuditEventSummary summary) {
        List<SseEmitter> deadEmitters = new ArrayList<>();

        for (SseEmitter emitter : emitters) {
            try {
                emitter.send(SseEmitter.event()
                        .name("audit-event")
                        .data(summary));
            } catch (Exception e) {
                deadEmitters.add(emitter);
            }
        }

        emitters.removeAll(deadEmitters);
    }
}
