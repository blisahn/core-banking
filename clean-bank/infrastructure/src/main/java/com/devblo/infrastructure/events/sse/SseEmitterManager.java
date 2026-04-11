package com.devblo.infrastructure.events.sse;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Component
public class SseEmitterManager {
    // TODO: instead of SSE can use web-socket for filtering requests?
    private final List<SseEmitter> emitters = new CopyOnWriteArrayList<>();}
