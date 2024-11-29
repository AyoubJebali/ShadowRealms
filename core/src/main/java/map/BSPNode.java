package map;

class BSPNode {
    int x, y, width, height; // The bounds of this node.
    BSPNode left, right;     // Children nodes (sub-regions).

    // Constructor
    public BSPNode(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    // Check if the node is a leaf (has no children).
    public boolean isLeaf() {
        return left == null && right == null;
    }
}

