package com.example.microserviceone.controller;

import brave.Span;
import brave.Tracer;
import lombok.extern.slf4j.Slf4j;
import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.jms.*;

@RestController
@Slf4j
public class MicroserviceOne {
    @Autowired
    public Tracer tracer;

    @Autowired
    RestTemplate restTemplate;
    @GetMapping("/func1")
    public String func1() throws JMSException {
        String url= ActiveMQConnection.DEFAULT_BROKER_URL;

        String queueName="Queue_3";

        ConnectionFactory connectionFactory= new ActiveMQConnectionFactory(url);

        Connection connection=connectionFactory.createConnection();

        connection.start();

        Session session=connection.createSession(false,Session.AUTO_ACKNOWLEDGE);

        Destination destination=session.createQueue(queueName);

        MessageProducer producer=session.createProducer(destination);

        Span span=tracer.currentSpan();
        if (span != null) {
            log.info("Trace ID {}", span.context().traceIdString());
            log.info("Span ID {}", span.context().spanIdString());
            TextMessage message=session.createTextMessage(span.context().traceIdString());
            log.info("Message{}",message);
            producer.send(message);
        }
        log.info("Func1");
        return restTemplate.getForObject("http://localhost:8081/func2",String.class);
    }
    @GetMapping("/func2")
    public String func2() throws JMSException {
        String url= ActiveMQConnection.DEFAULT_BROKER_URL;

        String queueName="Queue_2";

        ConnectionFactory connectionFactory= new ActiveMQConnectionFactory(url);

        Connection connection=connectionFactory.createConnection();

        connection.start();

        Session session=connection.createSession(false,Session.AUTO_ACKNOWLEDGE);

        Destination destination=session.createQueue(queueName);

        MessageConsumer consumer=session.createConsumer(destination);

        Message message=consumer.receive();

        log.info("message in func2:{}",message);

        if(message instanceof TextMessage)
        {
            TextMessage message1=(TextMessage) message;
            log.info("Message from : {}",message1.getText());
        }

        log.info("Func2");
        return "Func2";
    }
    @Bean
    public RestTemplate restTemplate()
    {
        return new RestTemplate();
    }
}
