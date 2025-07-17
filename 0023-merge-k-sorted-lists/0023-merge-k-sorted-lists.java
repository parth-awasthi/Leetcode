class Solution {
    public ListNode mergeKLists(ListNode[] lists) {
        if (lists == null || lists.length == 0) return null;
        return divide(lists, 0, lists.length - 1);
    }

    // Recursively split [l, r] into halves and merge
    private ListNode divide(ListNode[] lists, int l, int r) {
        if (l == r) return lists[l];           // single list
        int mid = l + (r - l) / 2;
        ListNode left  = divide(lists, l, mid);
        ListNode right = divide(lists, mid + 1, r);
        return mergeTwo(left, right);
    }

    // Merge two sorted linked lists
    private ListNode mergeTwo(ListNode a, ListNode b) {
        ListNode dummy = new ListNode(0);
        ListNode cur = dummy;
        while (a != null && b != null) {
            if (a.val < b.val) {
                cur.next = a;
                a = a.next;
            } else {
                cur.next = b;
                b = b.next;
            }
            cur = cur.next;
        }
        cur.next = (a != null) ? a : b;
        return dummy.next;
    }
}