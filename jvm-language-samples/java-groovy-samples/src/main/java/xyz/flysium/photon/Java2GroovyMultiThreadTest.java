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

package xyz.flysium.photon;

import groovy.util.ResourceException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import javax.script.Bindings;
import javax.script.ScriptException;
import javax.script.SimpleBindings;
import xyz.flysium.photon.util.GroovyScriptEngineJsr223Util;
import xyz.flysium.photon.util.GroovyScriptEngineUtil;

/**
 * Test Java To Groovy In multiThread environment.
 *
 * @author Sven Augustus
 */
public class Java2GroovyMultiThreadTest {

  private static final String S1 = "1";
  private static final String S2 = "2";
  private static final ThreadPoolExecutor THREAD_POOL_EXECUTOR = new ThreadPoolExecutor(8, 32,
      60, TimeUnit.SECONDS,
      new LinkedBlockingDeque<>(1024), Executors.defaultThreadFactory(),
      new ThreadPoolExecutor.CallerRunsPolicy());
  public static final int INT = 3;

  private static String scriptFile = "cls4.groovy";

  public static void main(String[] args)
      throws IllegalAccessException, InterruptedException, ResourceException, IOException, groovy.util.ScriptException, InstantiationException, ScriptException, NoSuchMethodException {
    if (args.length == 0) {
      return;
    }
    if (S1.equals(args[0])) {
      testGroovyScriptEngineUtil();
    }
    if (S2.equals(args[0])) {
      testGroovyScriptEngineJsr223Util();
    }
  }

  public static void testGroovyScriptEngineUtil()
      throws groovy.util.ScriptException, IOException, ResourceException,
      InterruptedException, IllegalAccessException, InstantiationException {
    long start = System.currentTimeMillis();

    int times = 100;
    List<Future<Boolean>> futures = new ArrayList<>(times);
    for (int i = 0; i < times; i++) {
      if (i % INT == 0) {
        futures.add(THREAD_POOL_EXECUTOR.submit(new Callable<Boolean>() {

          @Override
          public Boolean call() throws Exception {
            Bindings binding = new SimpleBindings();
            binding.put("x", 3);
            binding.put("y", 5);
            return (Integer) GroovyScriptEngineUtil.invokeFile(scriptFile, binding, "mul") == 15;
          }
        }));
      }
      if (i % INT == 1) {
        futures.add(THREAD_POOL_EXECUTOR.submit(new Callable<Boolean>() {

          @Override
          public Boolean call() throws Exception {
            Bindings binding = new SimpleBindings();
            binding.put("x", 2);
            binding.put("y", 6);
            return (Integer) GroovyScriptEngineUtil.invokeFile(scriptFile, binding, "mul") == 12;
          }
        }));
      }
    }
    THREAD_POOL_EXECUTOR.shutdown();
    for (Future<Boolean> future : futures) {
      Boolean result = null;
      try {
        result = future.get();
        if (!result) {
          System.out.println("Error testGroovyScriptEngineUtil !");
        }
      } catch (ExecutionException e) {
        e.printStackTrace();
      }
    }
    System.gc();
    System.out.println((System.currentTimeMillis() - start) + "ms");
  }

  public static void testGroovyScriptEngineJsr223Util()
      throws InstantiationException, IllegalAccessException, InterruptedException, ScriptException, NoSuchMethodException, FileNotFoundException {
    long start = System.currentTimeMillis();

    int times = 100;
    List<Future<Boolean>> futures = new ArrayList<>(times);
    for (int i = 0; i < times; i++) {
      if (i % INT == 0) {
        futures.add(THREAD_POOL_EXECUTOR.submit(new Callable<Boolean>() {

          @Override
          public Boolean call() throws Exception {
            Bindings binding = new SimpleBindings();
            binding.put("x", 3);
            binding.put("y", 5);
            return (Integer) GroovyScriptEngineJsr223Util.invokeFile(scriptFile, binding, "mul")
                == 15;
          }
        }));
      }
      if (i % INT == 1) {
        futures.add(THREAD_POOL_EXECUTOR.submit(new Callable<Boolean>() {

          @Override
          public Boolean call() throws Exception {
            Bindings binding = new SimpleBindings();
            binding.put("x", 2);
            binding.put("y", 6);
            return (Integer) GroovyScriptEngineJsr223Util.invokeFile(scriptFile, binding, "mul")
                == 12;
          }
        }));
      }
    }
    THREAD_POOL_EXECUTOR.shutdown();
    for (Future<Boolean> future : futures) {
      Boolean result = null;
      try {
        result = future.get();
        if (!result) {
          System.out.println("Error GroovyScriptEngineJsr223Util !");
        }
      } catch (ExecutionException e) {
        e.printStackTrace();
      }
    }
    System.gc();
    System.out.println((System.currentTimeMillis() - start) + "ms");
  }

}
