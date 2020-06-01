/*
 * Apache License 2.0
 *
 * Copyright 2018-2025 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.github.flysium.io.photon.juc.c004_cas;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.LongAdder;

/**
 * AtomicLong 、synchronized、LongAdder
 */
public class T02_Atomic_Vs_Sync_LongAdder {

  static final int nThreads = 1000;
  static AtomicLong count1 = new AtomicLong(0L);
  static long count2 = 0L;
  static LongAdder count3 = new LongAdder();

  public static void main(String[] args) throws Exception {
    long total = timeTasks(nThreads, () -> {
      for (int k = 0; k < 100000; k++) {
        count1.incrementAndGet();
      }
    });
    System.out.println("Atomic: " + count1.get() + " time " + total);
    //-----------------------------------------------------------
    final Object lock = new Object();
    total = timeTasks(nThreads, () -> {
      for (int k = 0; k < 100000; k++) {
        synchronized (lock) {
          count2++;
        }
      }
    });
    System.out.println("Sync: " + count2 + " time " + total);
    //-----------------------------------------------------------
    total = timeTasks(nThreads, () -> {
      for (int k = 0; k < 100000; k++) {
        count3.increment();
      }
    });
    System.out.println("LongAdder: " + count2 + " time " + total);
  }

  private static long timeTasks(final int nThreads, Runnable runnable) {
    CountDownLatch startSignal = new CountDownLatch(1);
    CountDownLatch doneSignal = new CountDownLatch(nThreads);
    for (int i = 0; i < nThreads; i++) {
      new Thread(() -> {
        try {
          startSignal.await();
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
        try {
          runnable.run();
        } finally {
          doneSignal.countDown();
        }
      }).start();
    }
    startSignal.countDown();
    long start = System.currentTimeMillis();
    try {
      doneSignal.await();
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    return System.currentTimeMillis() - start;
  }

  static void microSleep(int m) {
    try {
      TimeUnit.MICROSECONDS.sleep(m);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

}