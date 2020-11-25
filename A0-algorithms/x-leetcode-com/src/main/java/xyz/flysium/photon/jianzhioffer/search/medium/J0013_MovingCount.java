package xyz.flysium.photon.jianzhioffer.search.medium;

/**
 * 剑指 Offer 13. 机器人的运动范围
 * <p>
 * https://leetcode-cn.com/problems/ji-qi-ren-de-yun-dong-fan-wei-lcof/
 *
 * @author zeno
 */
public class J0013_MovingCount {

//地上有一个m行n列的方格，从坐标 [0,0] 到坐标 [m-1,n-1] 。一个机器人从坐标 [0, 0] 的格子开始移动，它每次可以向左、右、上、下移动一
//格（不能移动到方格外），也不能进入行坐标和列坐标的数位之和大于k的格子。例如，当k为18时，机器人能够进入方格 [35, 37] ，因为3+5+3+7=18。但
//它不能进入方格 [35, 38]，因为3+5+3+8=19。请问该机器人能够到达多少个格子？
//
//
//
// 示例 1：
//
// 输入：m = 2, n = 3, k = 1
//输出：3
//
//
// 示例 2：
//
// 输入：m = 3, n = 1, k = 0
//输出：1
//
//
// 提示：
//
//
// 1 <= n,m <= 100
// 0 <= k <= 20
//
// 👍 169 👎 0


  public static void main(String[] args) {
    Solution solution = new J0013_MovingCount().new Solution();
//    solution.movingCount(38, 15, 9); // 135
    solution.movingCount(36, 11, 15); // 362
  }

  // 	执行用时：0 ms, 在所有 Java 提交中击败了100.00% 的用户
  // https://leetcode-cn.com/leetbook/read/illustration-of-algorithm/9hka9c/
  //leetcode submit region begin(Prohibit modification and deletion)
  class Solution {

    int m, n, k;
    boolean[][] visited;

    public int movingCount(int m, int n, int k) {
      this.m = m;
      this.n = n;
      this.k = k;
      this.visited = new boolean[m][n];
      return dfs(0, 0, 0, 0);
    }

    public int dfs(int i, int j, int si, int sj) {
      if (i >= m || j >= n || k < si + sj || visited[i][j]) {
        return 0;
      }
      visited[i][j] = true;
      return 1
        + dfs(i + 1, j, (i + 1) % 10 != 0 ? si + 1 : si - 8, sj)
        + dfs(i, j + 1, si, (j + 1) % 10 != 0 ? sj + 1 : sj - 8);
    }
  }

//leetcode submit region end(Prohibit modification and deletion)


}