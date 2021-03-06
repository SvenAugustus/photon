/*
 * MIT License
 *
 * Copyright (c) 2020 SvenAugustus
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package rabbitmq.classic.samples.broadcast;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.AMQP.BasicProperties;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Envelope;
import com.rabbitmq.client.GetResponse;
import java.io.IOException;
import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.concurrent.TimeoutException;

/**
 * 生产者: fanout 模式交换器
 *
 * @author Sven Augustus
 */
public class FanoutProducerTest {

  public static final String AMQP_URL = "amqp://mq:mq123@127.0.0.1:5672";

  private static final String QUEUE_NAME_1 = "samples.queue.1";
  private static final String QUEUE_NAME_2 = "samples.queue.2";
  private static final String QUEUE_NAME_3 = "samples.queue.3";

  private static final String EXCHANGE_NAME = "samples.exchange.fanout";
  // 如果 fanout 模式交换器， 与key无关，会发送到所有和exchange绑定的队列，广播行为
  private static final String BINDING_KEY = "samples.key.xxxxxxxx";
  private static final String ROUTING_KEY = "samples.what.are.you?";

  public static void main(String[] args)
      throws IOException, TimeoutException, NoSuchAlgorithmException, KeyManagementException, URISyntaxException {
    ConnectionFactory factory = new ConnectionFactory();
    factory.setUri(AMQP_URL);
    Connection conn = factory.newConnection();
    Channel channel = conn.createChannel();

    // 创建了一个持久化的、非自动删除的、绑定类型为 topic 的交换器
    channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.FANOUT, true, false,
        new HashMap<>(8));
    // 队列被声明为持久化的 非排他的 非自动删除的，而且也被分配另一个确定的己知的名称(由客户端分配而非 RabbitMQ 自动生成)。
    channel.queueDeclare(QUEUE_NAME_1, true, false, false, null);
    // 队列被声明为持久化的 非排他的 非自动删除的，而且也被分配另一个确定的己知的名称(由客户端分配而非 RabbitMQ 自动生成)。
    channel.queueDeclare(QUEUE_NAME_2, true, false, false, null);
    // 队列被声明为持久化的 非排他的 非自动删除的，而且也被分配另一个确定的己知的名称(由客户端分配而非 RabbitMQ 自动生成)。
    channel.queueDeclare(QUEUE_NAME_3, true, false, false, null);

    // 使用路由键将队列和交换器绑定起来
    channel.queueBind(QUEUE_NAME_1, EXCHANGE_NAME, BINDING_KEY);
    channel.queueBind(QUEUE_NAME_2, EXCHANGE_NAME, BINDING_KEY);
    channel.queueBind(QUEUE_NAME_3, EXCHANGE_NAME, BINDING_KEY);

    byte[] messageBodyBytes = "Hello Fanout Exchange ! ".getBytes();
    // 发送消息
    AMQP.BasicProperties properties = new AMQP.BasicProperties.Builder()
        // 消息持久化
        .deliveryMode(2).build();
    channel.basicPublish(EXCHANGE_NAME, ROUTING_KEY, properties, messageBodyBytes);

    pullMessage(channel, QUEUE_NAME_1);
    pullMessage(channel, QUEUE_NAME_2);
    pullMessage(channel, QUEUE_NAME_3);

    channel.close();
    conn.close();
  }

  private static void pullMessage(Channel channel, String queue) throws IOException {
    // 拉消费（Pull）, 基于 AMQP 协议的 basic.get 命令， 可以单条地获取消息
    GetResponse response = channel.basicGet(queue, false);
    if (response != null) {
      byte[] body = response.getBody();
      Envelope envelope = response.getEnvelope();
      BasicProperties properties = response.getProps();
      long deliveryTag = envelope.getDeliveryTag();
      String exchange = envelope.getExchange();
      String routingKey = envelope.getRoutingKey();
      String contentType = properties.getContentType();
      // Decode message.
      String content = new String(body);

      System.out.printf("%s Receive New Messages, exchange= %s, routingKey= %s , body= %s %n",
          Thread.currentThread().getName(), exchange, routingKey, content);

      // Acknowledgement
      channel.basicAck(deliveryTag, false);
    } else {
      System.out.println("Receive No Messages.");
    }
  }

}
