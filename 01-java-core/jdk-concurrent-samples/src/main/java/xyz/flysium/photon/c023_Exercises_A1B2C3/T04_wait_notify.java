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

package xyz.flysium.photon.c023_Exercises_A1B2C3;

import java.util.concurrent.CountDownLatch;

/**
 * synchronized + notify / wait
 *
 * @author Sven Augustus
 */
public class T04_wait_notify {

  static final CountDownLatch latch = new CountDownLatch(1);
  static final Object LOCK_OBJECT = new Object();

  public static void main(String[] args) {
    new Thread(() -> {
      try {
        latch.await();
        synchronized (LOCK_OBJECT) {
          for (int i = 0; i < 26; i++) {
            System.out.print((i + 1));
            LOCK_OBJECT.notify();
            LOCK_OBJECT.wait();
          }
          LOCK_OBJECT.notify();
        }
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }, "INTS").start();

    new Thread(() -> {
      try {
        latch.countDown();
        synchronized (LOCK_OBJECT) {
          for (int i = 0; i < 26; i++) {
            System.out.print((char) ('A' + i));
            LOCK_OBJECT.notify();
            LOCK_OBJECT.wait();
          }
          LOCK_OBJECT.notify();
        }
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }, "CHARS").start();
  }

}
