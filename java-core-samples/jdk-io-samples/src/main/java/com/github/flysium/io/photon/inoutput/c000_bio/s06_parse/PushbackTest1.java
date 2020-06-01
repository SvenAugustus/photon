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

package com.github.flysium.io.photon.inoutput.c000_bio.s06_parse;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PushbackInputStream;

/**
 * PushbackInputStream
 *
 * @author Sven Augustus
 * @version 2017年1月27日
 */
public class PushbackTest1 {

	public static void main(String[] args) throws IOException {
		String filepath = "file.bin";

		java.io.OutputStream os = null;
		try {
			os = new FileOutputStream(filepath);

			os.write('#');
			os.write(new byte[]{'a', 'b', 'c', 'd'});
			os.flush();// 把缓冲区内的数据刷新到磁盘

		} finally {
			if (os != null) {
				os.close();// 关闭流
			}
		}
		/**
		 * 回推（pushback）
		 */
		PushbackInputStream pis = null;
		try {
			// pis = new PushbackInputStream(new FileInputStream(filepath));
			pis = new PushbackInputStream(new FileInputStream(filepath), 3);
			int len = -1;
			byte[] bytes = new byte[2];
			while ((len = pis.read(bytes)) != -1) {

				if ('b' == bytes[0]) {
					// pis.unread('U');
					// pis.unread(bytes);
					pis.unread(new byte[]{'1', '2', '3'});
				}

				for (int i = 0; i < len; i++) {
					System.out.print(((char) bytes[i]));
				}
			}
			System.out.println();
		} finally {
			if (pis != null) {
				pis.close();
			}
		}
		/**
		 * 会发现PushbackInputStream并没有改变目标介质的数据，不破坏流
		 */
		try {
			pis = new PushbackInputStream(new FileInputStream(filepath));
			int len = -1;
			byte[] bytes = new byte[2];
			while ((len = pis.read(bytes)) != -1) {
				for (int i = 0; i < len; i++) {
					System.out.print(((char) bytes[i]));
				}
			}
		} finally {
			if (pis != null) {
				pis.close();
			}
		}
	}

}