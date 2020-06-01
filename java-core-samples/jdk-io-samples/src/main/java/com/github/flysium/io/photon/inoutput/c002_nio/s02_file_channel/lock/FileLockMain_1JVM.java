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

package com.github.flysium.io.photon.inoutput.c002_nio.s02_file_channel.lock;

/**
 * 同一进程测试
 *
 * @author Sven Augustus
 */
public class FileLockMain_1JVM extends FileChannelTest2 {

	public static void main(String[] args) throws InterruptedException {
		final String path = "file.txt";
		// 同一进程，即使是共享锁，同时读并且重叠，一样 文件重叠锁异常【OverlappingFileLockException】
		new Thread(new Runnable() {
			@Override
			public void run() {
				read(path, "线程A");
			}
		}).start();
		Thread.sleep(2000);
		new Thread(new Runnable() {
			@Override
			public void run() {
				read(path, "线程B");
			}
		}).start();
	}

}