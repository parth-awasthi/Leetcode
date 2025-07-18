import java.util.*;

class Solution {
    public int maxLen(int n, int[][] edges, String label) {
        // build adjacency masks
        int[] nbr = new int[n];
        for (int[] e : edges) {
            int u = e[0], v = e[1];
            nbr[u] |= 1 << v;
            nbr[v] |= 1 << u;
        }
        // build per‐character node masks
        char[] L = label.toCharArray();
        int[] charMask = new int[26];
        for (int i = 0; i < n; i++) {
            charMask[L[i] - 'a'] |= 1 << i;
        }
        int allMask = (1 << n) - 1;
        // dp[mask][u] = bit‐mask of all v such that
        // there's a palindrome of length popcount(mask)
        // starting at u and ending at v, using exactly 'mask'.
        int[][] dp = new int[1 << n][n];
        int ans = 1;
        // odd‐length seeds (single‐node centers)
        for (int u = 0; u < n; u++) {
            dp[1 << u][u] = 1 << u;
        }
        // even‐length seeds (edges whose endpoints match)
        for (int u = 0; u < n; u++) {
            for (int v = u + 1; v < n; v++) {
                if (L[u] == L[v] && ((nbr[u] >> v) & 1) != 0) {
                    int m = (1 << u) | (1 << v);
                    dp[m][u] |= 1 << v;
                    dp[m][v] |= 1 << u;
                    ans = 2;
                }
            }
        }
        // group masks by popcount so we expand in increasing size
        List<Integer>[] masksByCount = new List[n + 1];
        for (int i = 0; i <= n; i++) masksByCount[i] = new ArrayList<>();
        for (int m = 0; m <= allMask; m++) {
            int bc = Integer.bitCount(m);
            if (bc >= 1 && bc <= n) masksByCount[bc].add(m);
        }
        // expand outward by 2 characters at a time
        for (int bc = 1; bc <= n - 2; bc++) {
            for (int mask : masksByCount[bc]) {
                for (int u = 0; u < n; u++) {
                    int vMask = dp[mask][u];
                    if (vMask == 0) continue;
                    // union all neighbors of those v's
                    int unionNbr = 0, tmp = vMask;
                    while (tmp != 0) {
                        int v = Integer.numberOfTrailingZeros(tmp);
                        unionNbr |= nbr[v];
                        tmp &= tmp - 1;
                    }
                    int notUsed = (~mask) & allMask;
                    // try to extend u -> u' and v -> v'
                    int candU = nbr[u] & notUsed;
                    while (candU != 0) {
                        int u2 = Integer.numberOfTrailingZeros(candU);
                        candU &= candU - 1;
                        int c = L[u2] - 'a';
                        // only v' not used, matching char, and ≠ u2
                        int candV = unionNbr & notUsed & charMask[c] & ~(1 << u2);
                        int tv = candV;
                        while (tv != 0) {
                            int v2 = Integer.numberOfTrailingZeros(tv);
                            tv &= tv - 1;
                            int nm = mask | (1 << u2) | (1 << v2);
                            if ((dp[nm][u2] & (1 << v2)) == 0) {
                                dp[nm][u2] |= 1 << v2;
                                ans = Math.max(ans, bc + 2);
                            }
                        }
                    }
                }
            }
        }
        return ans;
    }
}
