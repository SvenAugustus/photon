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

package xyz.flysium.photon.c020_Exercises_monitor5;

import java.util.ArrayList;
import java.util.List;

/**
 * 错误示例
 *
 * @author Sven Augustus
 */
public class E01_volatile {

  static class MyContainer {

    volatile List list = new ArrayList<>();

    public void add(Object o) {
      list.add(o);
      System.out.println(o);
    }

    public int size() {
      return list.size();
    }
  }

  volatile static MyContainer container = new MyContainer();

  public static void main(String[] args) {
    new Thread(() -> {
      for (int i = 1; i <= 10; i++) {
        container.add(i);
      }
    }, "t1").start();

    new Thread(() -> {
      while (container.size() != 5) {
      }
      System.out.println("now is 5");
    }, "t2").start();
  }

}
