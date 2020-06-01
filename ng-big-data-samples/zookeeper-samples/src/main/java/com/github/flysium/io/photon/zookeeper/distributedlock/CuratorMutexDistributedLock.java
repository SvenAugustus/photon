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

package com.github.flysium.io.photon.zookeeper.distributedlock;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;

/**
 * Curator 分布式可重入排它锁
 *
 * @author Sven Augustus
 * @version 1.0
 */
public class CuratorMutexDistributedLock extends AbstractCuratorDistributedLock implements
    DistributedLock {

  public CuratorMutexDistributedLock(CuratorFramework client, String path) {
    super(client, new InterProcessMutex(client, path));
  }

}