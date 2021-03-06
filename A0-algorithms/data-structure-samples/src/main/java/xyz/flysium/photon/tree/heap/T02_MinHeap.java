package xyz.flysium.photon.tree.heap;

import java.util.concurrent.ThreadLocalRandom;

/**
 * 完全二叉树中如果每棵子树的最小值都在顶部就是小根堆（最小堆）
 *
 * @author zeno
 */
public class T02_MinHeap {

  public static void main(String[] args) {
    int times = 10000;
    int operationTimes = 100;
    final ThreadLocalRandom random = ThreadLocalRandom.current();
    out:
    for (int i = 0; i < times; i++) {
      NativeButCorrectMinMap nativeButCorrectMinMap = new NativeButCorrectMinMap(100);
      MyMinHeap myMinHeap = new MyMinHeap(100);
      for (int j = 0; j < operationTimes; j++) {
        if (nativeButCorrectMinMap.isEmpty()) {
          int e = random.nextInt(0, 100);
          nativeButCorrectMinMap.push(e);
          myMinHeap.push(e);
        } else if (nativeButCorrectMinMap.isFull()) {
          int p1 = nativeButCorrectMinMap.pop();
          int p2 = myMinHeap.pop();
          if (p1 != p2) {
            System.out.println("-> Wrong algorithm !!!");
            break out;
          }
        } else {
          int f = random.nextInt(0, 100);
          if (f < 20) {
            int p1 = nativeButCorrectMinMap.pop();
            int p2 = myMinHeap.pop();
            if (p1 != p2) {
              System.out.println("-> Wrong algorithm !!!");
              break out;
            }
          } else {
            int e = random.nextInt(0, 100);
            nativeButCorrectMinMap.push(e);
            myMinHeap.push(e);
          }
        }
      }
    }
    System.out.println("Finish !");
  }

  //  采用数组的下标1开始，记录堆数据，那么index节点中
  //	左孩子的位置： index * 2  ->  index <<1
  //	右孩子的位置： index * 2 + 1  -> index << 1 | 1
  //	父亲的位置：   index / 2  -> index >>1
  //  可以充分里面位运算提高性能
  public static class MyMinHeap {

    private final int[] elements;
    private final int capacity;
    private int size = 0;

    public MyMinHeap(int capacity) {
      this.capacity = capacity;
      this.elements = new int[capacity];
    }

    public void push(int e) {
      if (isFull()) {
        throw new IllegalStateException("Heap is Full !");
      }
      // 数组的下标1开始
      ++size;
      elements[size] = e;
      heapInsert(elements, size);
    }

    public int pop() {
      if (isEmpty()) {
        throw new IllegalStateException("Heap is Empty !");
      }
      int ans = elements[1];
      swap(elements, 1, size);
      size--;
      heapify(elements, 1, size);
      return ans;
    }

    public boolean isEmpty() {
      return size <= 0;
    }

    public boolean isFull() {
      return size >= capacity;
    }

    private void heapInsert(int[] heap, int index) {
      int p = parentIndexOf(index);
      while (heap[index] < heap[p]) {
        swap(heap, p, index);
        index = p;
        p = parentIndexOf(index);
      }
    }

    private void heapify(int[] heap, int index, int heapSize) {
      int l = leftChildIndexOf(index);
      while (l <= heapSize) {
        int r = l + 1;
        int smaller = (r <= heapSize && heap[r] < heap[l]) ? r : l;
        smaller = heap[smaller] < heap[index] ? smaller : index;
        if (smaller == index) {
          break;
        }
        swap(heap, index, smaller);
        index = smaller;
        l = leftChildIndexOf(index);
      }
    }

    @Override
    public String toString() {
      if (isEmpty()) {
        return "[]";
      }

      StringBuilder b = new StringBuilder();
      b.append('[');
      for (int i = 1; i <= size; i++) {
        b.append(elements[i]);
        if (i == size) {
          return b.append(']').toString();
        }
        b.append(", ");
      }
      return b.toString();
    }

    private void swap(int[] arr, int x, int y) {
      if (x == y) {
        return;
      }
      arr[x] = arr[x] ^ arr[y];
      arr[y] = arr[x] ^ arr[y];
      arr[x] = arr[x] ^ arr[y];
    }

    private int leftChildIndexOf(int index) {
      return index << 1;
    }

    private int rightChildIndexOf(int index) {
      return (index << 1) | 1;
    }

    private int parentIndexOf(int index) {
      if (index == 1) {
        return 1;
      }
      return index >> 1;
    }

  }

  static class NativeButCorrectMinMap {

    private final int[] elements;
    private final int capacity;
    private int size = 0;

    public NativeButCorrectMinMap(int capacity) {
      this.capacity = capacity;
      this.elements = new int[capacity];
    }

    public void push(int e) {
      if (isFull()) {
        throw new IllegalStateException("Heap is Full !");
      }
      elements[size++] = e;
    }

    public int pop() {
      if (isEmpty()) {
        throw new IllegalStateException("Heap is Empty !");
      }
      int minIndex = 0;
      for (int i = 1; i < size; i++) {
        if (elements[i] < elements[minIndex]) {
          minIndex = i;
        }
      }
      int ans = elements[minIndex];
      elements[minIndex] = elements[--size];
      return ans;
    }

    public boolean isEmpty() {
      return size <= 0;
    }

    public boolean isFull() {
      return size >= capacity;
    }
  }

}
