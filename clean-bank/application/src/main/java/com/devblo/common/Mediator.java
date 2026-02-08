package com.devblo.common;

import org.springframework.stereotype.Component;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class Mediator {

    private final Map<Class<?>, ICommandHandler<?, ?>> commandHandlers = new HashMap<>();
    private final Map<Class<?>, IQueryHandler<?, ?>> queryHandlers = new HashMap<>();

    public Mediator(List<ICommandHandler<?, ?>> allCommandHandlers,
                    List<IQueryHandler<?, ?>> allQueryHandlers) {
        for (var handler : allCommandHandlers) {
            Class<?> commandType = extractGenericType(handler, ICommandHandler.class);
            commandHandlers.put(commandType, handler);
        }

        for (var handler : allQueryHandlers) {
            Class<?> queryType = extractGenericType(handler, IQueryHandler.class);
            queryHandlers.put(queryType, handler);
        }
    }

    private Class<?> extractGenericType(Object handler, Class<?> targetInterface) {
        for (Type genericInterface : handler.getClass().getGenericInterfaces()) {
            if (genericInterface instanceof ParameterizedType paramType) {
                if (paramType.getRawType().equals(targetInterface)) {
                    Type firstTypeArg = paramType.getActualTypeArguments()[0];
                    if (firstTypeArg instanceof Class<?> clazz) {
                        return clazz;
                    }
                }
            }
        }
        throw new IllegalStateException(
                "Could not extract generic type from handler: " + handler.getClass().getName()
        );
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
