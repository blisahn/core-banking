package com.devblo.infrastructure.messaging;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMqConfig {

    public static final String BANKING_EXCHANGE = "banking.events";

    public static final String ACCOUNT_ROUTING_KEY = "account.#";
    public static final String CUSTOMER_ROUTING_KEY = "customer.#";
    public static final String TRANSACTION_ROUTING_KEY = "transaction.#";

    public static final String ACCOUNT_EVENTS_QUEUE = "account.events.queue";
    public static final String CUSTOMER_EVENTS_QUEUE = "customer.events.queue";
    public static final String TRANSACTION_EVENTS_QUEUE = "transaction.events.queue";

    @Bean
    public TopicExchange bankingExchange() {
        return new TopicExchange(BANKING_EXCHANGE, true, false);
    }
    @Bean
    public Queue accountEventsQueue() {
        return QueueBuilder.durable(ACCOUNT_EVENTS_QUEUE).build();
    }
    @Bean
    public Queue customerEventsQueue() {
        return QueueBuilder.durable(CUSTOMER_EVENTS_QUEUE).build();
    }
    @Bean
    public Queue transactionEventsQueue() {
        return QueueBuilder.durable(TRANSACTION_EVENTS_QUEUE).build();
    }

    @Bean
    public Binding accountBinding(){
        return BindingBuilder.bind(accountEventsQueue())
                .to(bankingExchange())
                .with(ACCOUNT_ROUTING_KEY);
    }

    @Bean
    public Binding transactionBinding(){
        return BindingBuilder.bind(customerEventsQueue())
                .to(bankingExchange())
                .with(CUSTOMER_ROUTING_KEY);
    }

    @Bean
    public Binding transdactionBinding(){
        return BindingBuilder.bind(transactionEventsQueue())
                .to(bankingExchange())
                .with(TRANSACTION_ROUTING_KEY);
    }

}
