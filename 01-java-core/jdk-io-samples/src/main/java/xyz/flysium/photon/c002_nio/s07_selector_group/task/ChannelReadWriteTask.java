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

package xyz.flysium.photon.c002_nio.s07_selector_group.task;

import java.io.ByteArrayOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.util.concurrent.TimeUnit;
import xyz.flysium.photon.c002_nio.s07_selector_group.NIOChannelUtil;

/**
 * NIO SocketChannel Task
 *
 * @author Sven Augustus
 * @version 1.0
 */
public class ChannelReadWriteTask implements NIOTask {

  private final SocketChannel client;
  private SelectionKey selectionKey;

  public ChannelReadWriteTask(SocketChannel client) {
    this.client = client;
  }

  public SelectionKey getSelectionKey() {
    assert selectionKey != null;
    return selectionKey;
  }

  public void setSelectionKey(SelectionKey selectionKey) {
    assert selectionKey != null;
    assert selectionKey.channel() == client;
    this.selectionKey = selectionKey;
  }

  @Override
  public void run() {
    ByteBuffer buffer = ByteBuffer.allocate(8092);
    if (logger.isDebugEnabled()) {
      logger.debug("ready read from client: " + NIOChannelUtil.getRemoteAddress(client));
    }

    buffer.clear();
    try {
      for (; ; ) {
        int readCount = client.read(buffer);
        if (readCount > 0) {
          logger.debug("readied from client: " + NIOChannelUtil.getRemoteAddress(client)
              + ", count: " + readCount);
          String request = null;
          try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            baos.write(buffer.array(), 0, readCount);
            request = baos.toString();
          }
          logger.debug("readied from client: " + NIOChannelUtil.getRemoteAddress(client)
              + ", data: " + request);

          // FIXME 模拟业务逻辑处理时间耗时
          try {
            TimeUnit.SECONDS.sleep(1);
          } catch (InterruptedException e) {
            logger.error(e.getMessage(), e);
          }

          // 写入响应内容
          buffer.clear();
          buffer.put(("recv->" + request).getBytes());

          doWrite(buffer);

          break;
        } else if (readCount == 0) {
          logger.debug("readied nothing from client: " + NIOChannelUtil.getRemoteAddress(client)
              + " ! ");
          break;
        } else {
          logger.warn("readied -1 from client: " + NIOChannelUtil.getRemoteAddress(client)
              + " ! ");
          NIOChannelUtil.closeClient(client);
          break;
        }
      }
    } catch (ClosedChannelException e) {
      NIOChannelUtil.closeClient(client);
    } catch (IOException e) {
      logger.error(e.getMessage(), e);
    }
  }

  private void doWrite(ByteBuffer buffer) throws IOException {
    int writePos = doWriteInternal(buffer);

    incompleteWrite(writePos > 0);
  }

  private int doWriteInternal(ByteBuffer buffer) throws IOException {
    if (logger.isDebugEnabled()) {
      logger.debug("ready write to client: " + NIOChannelUtil.getRemoteAddress(client));
    }
    buffer.flip();

    if (!buffer.hasRemaining()) {
      return -1;
    }
    while (buffer.hasRemaining()) {
      int writeCount = client.write(buffer);
      if (writeCount < 0) {
        throw new EOFException();
      }
      // 处理 Send-Q 满了，暂时无法写出的情况
      if (writeCount == 0) {
        // 解决处理 Send-Q 满了，暂时无法写出的情况，避免导致 CPU 100%
        doWriteEnd(buffer);
        return writeCount;
      }
    }
    doWriteEnd(buffer);
    return 0;
  }

  private void doWriteEnd(ByteBuffer buffer) {
    buffer.clear();
  }

  protected final void incompleteWrite(boolean setOpWrite) {
    // Did not write completely.
    if (setOpWrite) {
      setOpWrite();
    } else {
      clearOpWrite();
      // TODO flush ?

      // TODO 这里读写一次就关闭连接了, 注释掉则不由服务器主动关闭
      NIOChannelUtil.closeClient(client);
    }
  }

  protected final void setOpWrite() {
    final SelectionKey key = getSelectionKey();
    if (!key.isValid()) {
      return;
    }
    final int interestOps = key.interestOps();
    if ((interestOps & SelectionKey.OP_WRITE) == 0) {
      key.interestOps(interestOps | SelectionKey.OP_WRITE);
    }
  }

  protected final void clearOpWrite() {
    final SelectionKey key = getSelectionKey();
    if (!key.isValid()) {
      return;
    }
    final int interestOps = key.interestOps();
    if ((interestOps & SelectionKey.OP_WRITE) != 0) {
      key.interestOps(interestOps & ~SelectionKey.OP_WRITE);
    }
  }

}
