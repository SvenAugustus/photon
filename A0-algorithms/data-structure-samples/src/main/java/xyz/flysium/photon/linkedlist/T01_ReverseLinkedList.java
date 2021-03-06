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

package xyz.flysium.photon.linkedlist;

import java.util.LinkedList;
import java.util.List;
import java.util.function.Function;
import xyz.flysium.photon.LinkedListSupport;

/**
 * 单链表和双链表如何反转
 *
 * @author zeno (Sven Augustus)
 * @version 1.0
 */
public class T01_ReverseLinkedList {

  public static void main(String[] args) {
    testReverseLinkedListToEnd(10000, 5, 100, new T01_ReverseLinkedList()::reverse);
    testReverseDoubleLinkedListToEnd(10000, 5, 100, new T01_ReverseLinkedList()::reverse);
    System.out.println("Finish !");
  }

  public ListNode reverse(ListNode head) {
    ListNode curr = head;
    ListNode prev = null;
    ListNode next = null;

    while (curr != null) {
      next = curr.next;

      curr.next = prev;

      prev = curr;
      curr = next;
    }
    return prev;
  }

  public DoubleNode reverse(DoubleNode head) {
    DoubleNode curr = head;
    DoubleNode prev = null;
    DoubleNode next = null;

    while (curr != null) {
      next = curr.next;
      prev = curr.prev;

      curr.next = prev;
      curr.prev = next;

      prev = curr;
      curr = next;
    }
    return prev;
  }

  public static void testReverseLinkedListToEnd(int times, int length, int maxValue,
    Function<ListNode, ListNode> reverseConsumer) {
    for (int i = 0; i < times; i++) {
      ListNode head1 = LinkedListSupport.generateRandomLinkedList(5, 100);
      ListNode reverseHead = reverseConsumer.apply(head1);
      ListNode head2 = internalReverse(reverseHead);

      if (!LinkedListSupport.equals(head1, head2)) {
        System.out.println("-> Wrong algorithm !!!");
        break;
      }
    }
  }

  public static void testReverseDoubleLinkedListToEnd(int times, int length, int maxValue,
    Function<DoubleNode, DoubleNode> reverseConsumer) {
    for (int i = 0; i < times; i++) {
      DoubleNode head1 = LinkedListSupport.generateRandomDoubleLinkedList(5, 100);
      DoubleNode reverseHead = reverseConsumer.apply(head1);
      DoubleNode head2 = internalReverse(reverseHead);

      if (!LinkedListSupport.equals(head1, head2)) {
        System.out.println("-> Wrong algorithm !!!");
        break;
      }
    }
  }

  private static ListNode internalReverse(ListNode head) {
    if (head == null) {
      return null;
    }
    if (head.next == null) {
      return head;
    }
    List<ListNode> list = new LinkedList<>();
    ListNode curr = head;
    while (curr != null) {
      list.add(curr);
      curr = curr.next;
    }
    int size = list.size();
    list.get(0).next = null;
    for (int i = 1; i < size; i++) {
      list.get(i).next = list.get(i - 1);
    }
    return list.get(size - 1);
  }

  private static DoubleNode internalReverse(DoubleNode head) {
    if (head == null) {
      return null;
    }
    if (head.next == null) {
      return head;
    }
    List<DoubleNode> list = new LinkedList<>();
    DoubleNode curr = head;
    while (curr != null) {
      list.add(curr);
      curr = curr.next;
    }
    int size = list.size();
    list.get(0).next = null;
    for (int i = 1; i < size - 1; i++) {
      list.get(i).prev = list.get(i + 1);
      list.get(i).next = list.get(i - 1);
    }
    list.get(size - 1).prev = null;
    return list.get(size - 1);
  }
}
