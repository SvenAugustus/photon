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

package rabbitmq.classic.samples.priority;

import com.rabbitmq.client.AMQP.BasicProperties;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import java.io.IOException;
import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;

/**
 * 生产者：优先级队列
 *
 * @author Sven Augustus
 */
public class PriorityQueue {

  public static final String AMQP_URL = "amqp://mq:mq123@127.0.0.1:5672";

  private static final String QUEUE_NAME = "samples.priority.queue";
  private static final String EXCHANGE_NAME = "samples.priority.exchange.direct";
  // 如果 direct 模式交换器， ROUTING_KEY 与 BINDING_KEY 必须完全匹配
  private static final String BINDING_KEY = "samples.priority.key";
  private static final String ROUTING_KEY = "samples.priority.key";

  public static void main(String[] args)
      throws IOException, TimeoutException, NoSuchAlgorithmException, KeyManagementException, URISyntaxException {
    ConnectionFactory factory = new ConnectionFactory();
    factory.setUri(AMQP_URL);
    Connection conn = factory.newConnection();
    Channel channel = conn.createChannel();

    // 创建了一个持久化的、非自动删除的、绑定类型为 direct 的交换器
    channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.DIRECT, true, false,
        new HashMap<>(8));
    // 队列被声明为持久化的 非排他的 非自动删除的，而且也被分配另一个确定的己知的名称(由客户端分配而非 RabbitMQ 自动生成)。
    Map<String, Object> arguments = new HashMap<String, Object>(8);
    // FIXME 设置队列的优先级，即队列中消息的最大优先级，这是一个参考值。
    arguments.put("x-max-priority", 10);
    channel.queueDeclare(QUEUE_NAME, true, false, false, arguments);
    // 使用路由键将队列和交换器绑定起来
    channel.queueBind(QUEUE_NAME, EXCHANGE_NAME, BINDING_KEY);

    // 发送消息
    String msg = "Hello message priority 5";
    channel.basicPublish(EXCHANGE_NAME, ROUTING_KEY,
        getBasicProperties(5), "Hello message priority 5".getBytes());
    System.out.println("Send maybe OK.msg=" + msg);

    msg = "Hello message priority 2";
    channel.basicPublish(EXCHANGE_NAME, ROUTING_KEY,
        getBasicProperties(2), "Hello message priority 2".getBytes());
    System.out.println("Send maybe OK.msg=" + msg);

    msg = "Hello message priority 8";
    channel.basicPublish(EXCHANGE_NAME, ROUTING_KEY,
        getBasicProperties(8), msg.getBytes());
    System.out.println("Send maybe OK.msg=" + msg);

    channel.close();
    conn.close();
  }

  private static BasicProperties getBasicProperties(int priority) {
    return new BasicProperties.Builder()
        // 消息持久化
        .deliveryMode(2)
        .contentEncoding("UTF-8")
        // FIXME 设置 消息的优先级
        .priority(priority).build();
  }

}
