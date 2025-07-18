import java.util.Stack;

class Solution {
    public int longestValidParentheses(String s) {
        int maxLength = 0;
        Stack<Integer> stack = new Stack<>();
        stack.push(-1); // Initialize the stack with a base value

        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (c == '(') {
                stack.push(i);
            } else {
                stack.pop();
                if (stack.isEmpty()) {
                    stack.push(i);
                } else {
                    int length = i - stack.peek();
                    if (length > maxLength) {
                        maxLength = length;
                    }
                }
            }
        }

        return maxLength;
    }
}
//assistant A