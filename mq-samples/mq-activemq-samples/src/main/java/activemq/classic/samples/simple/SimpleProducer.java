/*
 * Copyright 2018-2025 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package activemq.classic.samples.simple;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.DeliveryMode;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;
import org.apache.activemq.ActiveMQConnectionFactory;

/**
 * 生产者：点对点 ( P2P )
 *
 * @author Sven Augustus
 */
public class SimpleProducer {

  public static void main(String[] args) throws JMSException {
    // 1、创建工厂连接对象，需要制定ip和端口号
    ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("mq", "mq123",
        "tcp://127.0.0.1:61616");
    // 2、使用连接工厂创建一个连接对象
    Connection connection = connectionFactory.createConnection();
    // 3、开启连接
    connection.start();
    // 4、使用连接对象创建会话（session）对象
    Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
    // 5、使用会话对象创建目标对象，包含queue和topic（一对一和一对多）
    Queue queue = session.createQueue("test-queue");
    // 6、使用会话对象创建生产者对象
    MessageProducer producer = session.createProducer(queue);
    // 7、使用会话对象创建一个消息对象
    TextMessage textMessage = session.createTextMessage("hello, test-queue");
    // TODO 设置消息持久化, 默认是非持久化的
    textMessage.setJMSDeliveryMode(DeliveryMode.PERSISTENT);
    // 8、发送消息
    // producer 默认是持久化模式
    producer.setDeliveryMode(DeliveryMode.PERSISTENT);
    producer.send(textMessage);
    System.out.println("Send maybe OK. msg=" + textMessage.getText());
    // 9、关闭资源
    producer.close();
    session.close();
    connection.close();
  }

}