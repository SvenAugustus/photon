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

package xyz.flysium.photon.c006_util;

import java.util.Random;
import java.util.concurrent.Phaser;
import java.util.concurrent.TimeUnit;

/**
 * 采用 Phaser 简单模拟几个游客一起约旅游几个景点, 中间可能发生一些随机事件 （有人可能临时离开）
 *
 * @author Sven Augustus
 */
public class T05_Phaser {

  public static void main(String[] args) {
    // 定义游客
    final String[] tourists = new String[]{"小红", "小陈", "小黄", "小白", "小青", "小蓝", "小紫"};
    final String[] points = new String[]{"出发点", "景点A", "景点B", "景点C", "酒店", "飞机场，回家去"};

    Phaser phaser = new Phaser() {

      @Override
      protected boolean onAdvance(int phase, int registeredParties) {
        System.out.print(Thread.currentThread().getName()
            + ": 所有人" + getArrivedParties() + "都到" + points[phase] + "了");
        if (phase < points.length - 1) {
          System.out.println(",现在是第" + (phase + 1) + "次集合准备去下一个地方..................\n");
        } else {
          System.out.println();
        }
        return super.onAdvance(phase, registeredParties);
      }
    };
    // 指定第一阶段参与的parties个数
    phaser.bulkRegister(tourists.length);

    for (String tourist : tourists) {
      new Thread(() -> {
        for (int i = 0; i < points.length; i++) {
          if (!goToPoint(phaser, points[i])) {
            break;
          }
        }
      }).start();
    }
  }

  // 如果返回 false,表示不继续了
  private static boolean goToPoint(Phaser phaser, String point) {
    try {
      // 制作随机事件
      Random random = new Random();
      int f = random.nextInt(100);
      if (f < 5) {
        System.out.println(Thread.currentThread().getName() + ":突然有事要离开一下,不和你们继续旅游了....");
        // 有事情离开了
        phaser.arriveAndDeregister();
        return false;
      }

      randomSleep(point);
      // 开始等待其他人到齐
      phaser.arriveAndAwaitAdvance();
    } catch (Exception e) {
      e.printStackTrace();
    }
    return true;
  }

  private static void randomSleep(String point) {
    Random random = new Random();
    try {
      final long time = 100 + random.nextInt(900);
      TimeUnit.MILLISECONDS.sleep(time);
      System.out.println(Thread.currentThread().getName() + " 花了 " + time + " 时间才到了" + point);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

}
