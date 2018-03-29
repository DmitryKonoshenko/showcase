package com.divanxan.shovcase.servlet;

import com.divanxan.shovcase.dto.Product;
import com.divanxan.shovcase.dto.ProductList;
import com.divanxan.shovcase.json.JsonReader;
import com.rabbitmq.client.*;

import javax.faces.bean.ManagedBean;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeoutException;

@ManagedBean(name = "servlet", eager = true)
public class Servlet {

    private static ProductList productList;


    private static String message;

    private static boolean isResiveStart = false;

    public Servlet() {

        if (!isResiveStart) Resive();

        if (productList == null || message!=null) {
            message = null;
            productList = new ProductList();
            JsonReader jsonReader = new JsonReader();
            try {
                productList.setProductList(jsonReader.getTop());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public String getList() {
        return productList.getProductList().toString();
    }

    public List<Product> getListProduct() {
        return productList.getProductList();
    }


    private final static String QUEUE_NAME = "hello";

    private static void Resive() {
        isResiveStart = true;
        ConnectionFactory factory = new ConnectionFactory();
        factory.setUsername("admin");
        factory.setPassword("admin");
//        factory.setPort(5672);
        factory.setHost("192.168.99.100");
        Connection connection = null;
        try {
            connection = factory.newConnection();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }
        Channel channel = null;
        try {
            channel = connection.createChannel();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            channel.queueDeclare(QUEUE_NAME, false, false, false, null);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(" [*] Waiting for messages. To exit press CTRL+C");

        Consumer consumer = new DefaultConsumer(channel) {

            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body)
                    throws IOException {
                message = new String(body, "UTF-8");
                System.out.println(" [x] Received '" + message + "'");
            }
        };
        try {
            channel.basicConsume(QUEUE_NAME, true, consumer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

