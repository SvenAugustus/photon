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

package xyz.flysium.photon.c000_bio.s06_parse;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.SequenceInputStream;
import java.util.Vector;

/**
 * SequenceInputStream 合并流
 *
 * @author Sven Augustus
 * @version 2017年1月28日
 */
public class SequenceInputStreamTest {

	public static void main(String[] args) throws IOException {
		String filepath1 = "file1.txt";
		String filepath2 = "file2.txt";

		java.io.Writer w = null;
		try {
			w = new FileWriter(filepath1);

			w.write("百世山河任凋换，一生意气未改迁。愿从劫火投身去，重自寒灰飞赤鸾。\r\n");
			w.write("沧海桑田新几度，月明还照旧容颜。琴心剑魄今何在，留见星虹贯九天。 \n");
			w.write("冰轮腾转下西楼，永夜初晗凝碧天。长路寻仙三山外，道心自在红尘间。 \n");
			w.write("何来慧剑破心茧，再把貂裘换酒钱。回望天涯携手处，踏歌重访白云间。\n");
			w.flush();// 把缓冲区内的数据刷新到磁盘

		} finally {
			if (w != null) {
				w.close();// 关闭流
			}
		}
		try {
			w = new FileWriter(filepath2);

			w.write("何以飘零去，何以少团栾，何以别离久，何以不得安？ ");
			w.flush();// 把缓冲区内的数据刷新到磁盘

		} finally {
			if (w != null) {
				w.close();// 关闭流
			}
		}
		java.io.Reader r = null;
		try {
			Vector<InputStream> v = new Vector<InputStream>(2);
			InputStream s1 = new FileInputStream(filepath1);
			InputStream s2 = new FileInputStream(filepath2);
			v.addElement(s1);
			v.addElement(s2);

			r = new BufferedReader(new InputStreamReader(new SequenceInputStream(v.elements())));

			char[] data = new char[256];
			int len = -1;
			while ((len = r.read(data)) != -1) {// -1 表示读取到达文件结尾
				// 操作数据
				for (int i = 0; i < len; i++) {
					System.out.print(data[i]);
				}
			}
		} finally {
			if (r != null) {
				r.close();// 关闭流
			}
		}
	}

}
