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

package com.github.flysium.io.photon.jvm.agent;

import java.lang.instrument.Instrumentation;

/**
 * 计算对象大小 Agent
 *
 * @author Sven Augustus
 */
public class ObjectSizeAgent {

  private static Instrumentation instrumentation;

  public static void premain(String agentArgs, Instrumentation inst) {
    instrumentation = inst;
  }

  /**
   * 返回对象的大小
   *
   * @param o 对象实例
   * @return 对象的大小
   */
  public static long sizeOf(Object o) {
    return instrumentation.getObjectSize(o);
  }

}
