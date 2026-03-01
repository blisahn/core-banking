package com.devblo.common;

import org.springframework.core.GenericTypeResolver;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class Mediator {

    private final Map<Class<?>, ICommandHandler<?, ?>> commandHandlers = new HashMap<>();
    private final Map<Class<?>, IQueryHandler<?, ?>> queryHandlers = new HashMap<>();

    public Mediator(List<ICommandHandler<?, ?>> allCommandHandlers,
                    List<IQueryHandler<?, ?>> allQueryHandlers) {

        allCommandHandlers.forEach(handler -> {
            Class<?> commandType = GenericTypeResolver.resolveTypeArguments(handler.getClass(), ICommandHandler.class)[0];
            commandHandlers.put(commandType, handler);
        });
        allQueryHandlers.forEach(handler -> {
            Class<?> queryType = GenericTypeResolver.resolveTypeArguments(handler.getClass(), IQueryHandler.class)[0];
            queryHandlers.put(queryType, handler);
        });
    }




    @SuppressWarnings("unchecked")
    public <R> R sendCommand(ICommand<R> command) {
        ICommandHandler<ICommand<R>, R> handler =
                (ICommandHandler<ICommand<R>, R>) commandHandlers.get(command.getClass());

        if (handler == null) {
            throw new IllegalStateException(
                    "No handler registered for command: " + command.getClass().getName()
            );
        }

        return handler.handle(command);
    }


    @SuppressWarnings("unchecked")
    public <R> R sendQuery(IQuery<R> query) {
        IQueryHandler<IQuery<R>, R> handler =
                (IQueryHandler<IQuery<R>, R>) queryHandlers.get(query.getClass());

        if (handler == null) {
            throw new IllegalStateException(
                    "No handler registered for query: " + query.getClass().getName()
            );
        }
        return handler.handle(query);
    }
}
