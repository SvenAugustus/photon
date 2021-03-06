package xyz.flysium.photon.algorithm.tree.binary.basic;

import java.util.Deque;
import java.util.LinkedList;
import xyz.flysium.photon.tree.TreeNode;

/**
 * 297. 二叉树的序列化与反序列化
 * <p>
 * https://leetcode-cn.com/problems/serialize-and-deserialize-binary-tree/
 *
 * @author zeno
 */
public interface W0297_SerializeAndDeserializeBinaryTree_2 {

  //序列化是将一个数据结构或者对象转换为连续的比特位的操作，进而可以将转换后的数据存储在一个文件或者内存中，
  //  同时也可以通过网络传输到另一个计算机环境，采取相反方式重构得到原数据。
  //
  //请设计一个算法来实现二叉树的序列化与反序列化。这里不限定你的序列 / 反序列化算法执行逻辑，
  //  你只需要保证一个二叉树可以被序列化为一个字符串并且将这个字符串反序列化为原始的树结构。

  // 后序遍历 29ms
  public class Codec {

    static final String NULL_STR = "#";
    static final String SPLIT_STR = ",";

    // Encodes a tree to a single string.
    public String serialize(TreeNode root) {
      if (root == null) {
        return NULL_STR;
      }
      Deque<Integer> ans = new LinkedList<>();
      postorderTraversal(root, ans);

      StringBuilder buf = new StringBuilder();
      String pre = "";
      for (Integer e : ans) {
        buf.append(pre).append((e == null) ? NULL_STR : e);
        pre = SPLIT_STR;
      }

      return buf.toString();
    }

    // Decodes your encoded data to tree.
    public TreeNode deserialize(String data) {
      if (NULL_STR.equals(data)) {
        return null;
      }
      Deque<Integer> list = new LinkedList<>();
      String[] ss = data.split(SPLIT_STR);
      for (String s : ss) {
        if (NULL_STR.equals(s)) {
          list.offerLast(null);
        } else {
          list.offerLast(Integer.parseInt(s));
        }
      }
      // 后序遍历 左右中  ->  stack (中右左)
      Deque<Integer> stack = new LinkedList<>();
      while (!list.isEmpty()) {
        stack.push(list.pollFirst());
      }
      return postorderTraversalRedo(stack);
    }

    private void postorderTraversal(TreeNode node, Deque<Integer> ans) {
      if (node == null) {
        ans.add(null);
        return;
      }
      postorderTraversal(node.right, ans);
      postorderTraversal(node.left, ans);
      ans.add(node.val);
    }

    private TreeNode postorderTraversalRedo(Deque<Integer> stack) {
//      Integer e = list.pollFirst();
      Integer e = stack.pop();
      if (e == null) {
        return null;
      }
      TreeNode root = new TreeNode(e);
      root.left = postorderTraversalRedo(stack);
      root.right = postorderTraversalRedo(stack);
      return root;
    }

  }

// Your Codec object will be instantiated and called as such:
// Codec ser = new Codec();
// Codec deser = new Codec();
// TreeNode ans = deser.deserialize(ser.serialize(root));
}
