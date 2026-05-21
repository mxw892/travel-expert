// Matthew Wang

// binary tree node for the decision tree, stores destinations or questions
public class BinaryTreeNode {

    String data;
    BinaryTreeNode left;
    BinaryTreeNode right;

    // constructor to make a node with inputted data
    public BinaryTreeNode(String data) {
        this.data = data;
        this.left = null;
        this.right = null;
    }

    // checks if node is a leaf
    public boolean isLeaf() {
        return left == null && right == null;
    }
}
