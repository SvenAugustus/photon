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

package xyz.flysium.photon.sort;

import xyz.flysium.photon.CommonSort;
import xyz.flysium.photon.SortSupport;

/**
 * 插入排序（Insertion Sort）
 *
 * @author zeno (Sven Augustus)
 * @version 1.0
 */
public class T03_InsertionSort {

    public static void main(String[] args) {
        SortSupport.testToEnd(100000, 10, 100, 0, 1000, new T03_InsertionSort()::sort);
        System.out.println("Finish !");
    }

    public void sort(int[] array) {
        if (array == null || array.length < 2) {
            return;
        }
        // (0,1)
        // (0,1,2)
        // (0,1,2,3)
        // ...
        // (0, N)
        final int length = array.length;
        for (int k = 1; k < length; k++) {
            for (int i = k; i >= 1; i--) {
                if (array[i] >= array[i - 1]) {
                    break;
                }
                CommonSort.swap(array, i, i - 1);
            }
        }
    }

}
