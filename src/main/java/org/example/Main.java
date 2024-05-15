package org.example;

//public class Main {
//    public static void main(String[] args) {
//        System.out.println("Hello world!");
//    }
//}

import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.ProducerTemplate;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Main {
    public static void main(String[] args) throws Exception {
        int count = 0;
//        ClassPathXmlApplicationContext appContext = new ClassPathXmlApplicationContext("applicationContext.xml");
        ClassPathXmlApplicationContext appContext = new ClassPathXmlApplicationContext("camel-context.xml");
        CamelContext context = appContext.getBean(CamelContext.class);
        while (count < 25) {
            callAPI(context);
            count++;
        }
        context.stop();
        appContext.close();
    }

    private static void callAPI(CamelContext context) {
        context.start();
        context.getComponentNames();
        ProducerTemplate producerTemplate = context.createProducerTemplate();
        Exchange exchange = producerTemplate.request("http://localhost:8080/hello", new MyProcessor());
        System.out.println("Response from REST endpoint: " + exchange.getMessage().getBody(String.class));
    }

    static class MyProcessor implements Processor {
        @Override
        public void process(Exchange exchange) throws Exception {
            exchange.getIn().setHeader(Exchange.HTTP_METHOD, "GET");
            exchange.getIn().setHeader(Exchange.CONTENT_TYPE, "application/json");
        }
    }
}