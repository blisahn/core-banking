package com.devblo.common;

public interface ICommandHandler<C extends ICommand<R>, R> {
    R handle(C command);
}
