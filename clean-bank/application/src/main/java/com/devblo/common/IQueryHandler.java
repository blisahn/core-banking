package com.devblo.common;

public interface IQueryHandler<Q extends IQuery<R>, R> {
    R handle(Q query);
}
