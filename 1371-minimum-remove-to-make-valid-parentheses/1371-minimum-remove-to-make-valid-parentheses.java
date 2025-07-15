class Solution {
    public String minRemoveToMakeValid(String s) {
        // 1) First pass: remove invalid ')'
        StringBuilder sb = new StringBuilder();
        int balance = 0;
        for (char c : s.toCharArray()) {
            if (c == '(') {
                balance++;
                sb.append(c);
            } else if (c == ')') {
                if (balance > 0) {
                    balance--;
                    sb.append(c);
                }
                // else skip this unmatched ')'
            } else {
                sb.append(c);
            }
        }
        
        // 2) Second pass: remove the extra '(' from the end
        StringBuilder res = new StringBuilder();
        int openToRemove = balance;
        for (int i = sb.length() - 1; i >= 0; i--) {
            char c = sb.charAt(i);
            if (c == '(' && openToRemove > 0) {
                openToRemove--;
                continue;  // skip this unmatched '('
            }
            res.append(c);
        }
        
        return res.reverse().toString();
    }
}
