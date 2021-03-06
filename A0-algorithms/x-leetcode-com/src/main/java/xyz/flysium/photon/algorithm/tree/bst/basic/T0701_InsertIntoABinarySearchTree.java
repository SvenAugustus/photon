package xyz.flysium.photon.algorithm.tree.bst.basic;

import xyz.flysium.photon.tree.TreeNode;

/**
 * 701. 二叉搜索树中的插入操作
 * <p>
 * https://leetcode-cn.com/problems/insert-into-a-binary-search-tree/
 *
 * @author zeno
 */
public interface T0701_InsertIntoABinarySearchTree {

  // 给定二叉搜索树（BST）的根节点和要插入树中的值，将值插入二叉搜索树。
  // 返回插入后二叉搜索树的根节点。
  //
  // 输入数据保证，新值和原始二叉搜索树中的任意节点值都不同。
  // 注意，可能存在多种有效的插入方式，只要树在插入后仍保持为二叉搜索树即可。 你可以返回任意有效的结果。

  class Solution {

    public TreeNode insertIntoBST(TreeNode root, int val) {
      if (root == null) {
        return new TreeNode(val);
      }
      TreeNode parent = null;
      parent = inorderTraversal(root, parent, val);

      if (val < parent.val) {
        parent.left = new TreeNode(val);
      } else {
        parent.right = new TreeNode(val);

      }
      return root;
    }

    private TreeNode inorderTraversal(TreeNode root, TreeNode parent, int val) {
      if (root == null) {
        return parent;
      }
      if (val < root.val) {
        return this.inorderTraversal(root.left, root, val);
      }
      return this.inorderTraversal(root.right, root, val);
    }

  }

}
