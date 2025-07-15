class Solution {
    /**
     * Determines if an input string (s) and a pattern (p) match.
     * The pattern supports '.' (matches any single character) and 
     * '*' (matches zero or more of the preceding element).
     *
     * @param s The input string.
     * @param p The pattern string.
     * @return true if the pattern matches the entire input string, false otherwise.
     */
    public boolean isMatch(String s, String p) {
        int m = s.length();
        int n = p.length();

        // dp[i][j] is true if the first i characters of s match the first j characters of p.
        boolean[][] dp = new boolean[m + 1][n + 1];

        // Base Case 1: An empty string matches an empty pattern.
        dp[0][0] = true;

        // Base Case 2: An empty string with a non-empty pattern.
        // The pattern can only match if it consists of characters followed by '*', like "a*b*".
        for (int j = 1; j <= n; j++) {
            if (p.charAt(j - 1) == '*') {
                // If we see a '*', it can match zero preceding elements.
                // So, the result depends on the pattern two steps back (skipping over 'x*').
                dp[0][j] = dp[0][j - 2];
            }
        }

        // Fill the DP table for all other cases.
        for (int i = 1; i <= m; i++) {
            for (int j = 1; j <= n; j++) {
                char sChar = s.charAt(i - 1);
                char pChar = p.charAt(j - 1);

                if (pChar == '.' || pChar == sChar) {
                    // Case 1: The current characters match (or pattern is '.').
                    // The result depends on the match of the preceding substrings.
                    dp[i][j] = dp[i - 1][j - 1];
                } else if (pChar == '*') {
                    // Case 2: The pattern character is '*'.
                    // This gives two sub-possibilities.

                    // Sub-case 2a: The '*' matches zero occurrences of the preceding element.
                    // We effectively ignore the 'x*' part of the pattern and check the state two steps back.
                    dp[i][j] = dp[i][j - 2];
                    
                    char prevPChar = p.charAt(j - 2);
                    if (prevPChar == '.' || prevPChar == sChar) {
                        // Sub-case 2b: The '*' matches one or more occurrences.
                        // This is only possible if the current string character matches the character
                        // preceding the '*'. If so, we OR this result with the result from the previous
                        // string character and the same pattern (dp[i-1][j]).
                        dp[i][j] = dp[i][j] || dp[i - 1][j];
                    }
                }
                // else, the characters don't match, so dp[i][j] remains false.
            }
        }

        return dp[m][n];
    }
}