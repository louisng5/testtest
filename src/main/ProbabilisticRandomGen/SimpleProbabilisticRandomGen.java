package main.ProbabilisticRandomGen;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class SimpleProbabilisticRandomGen implements ProbabilisticRandomGen{

    private BST_class bst;
    private float cumSum;
    private HashMap<Float, Integer> map;
    private Random r = new Random();

    @Override
    public int nextFromSample() {
        return  map.get(bst.search(random()));
    }

    private float random()
    {
        return cumSum * r.nextFloat();
    }

    public void loadSample(List<NumAndProbability> lst){
        bst = new BST_class();
        map = new HashMap<Float, Integer>();
        cumSum = 0;
        for (NumAndProbability n:lst){
            cumSum += n.getProbabilityOfSample();
            bst.insert(cumSum);
            map.put(cumSum,n.getNumber());
        }
    }

    public void xxx(List<Float> lst){
        BST_class bst = new BST_class();
        for (Float f: lst){
            bst.insert(f);
        }
        System.out.println(bst.search(20));
    }

    class BST_class {
        //node class that defines BST node
        class Node {
            float key;
            Node left, right;

            public Node(float data){
                key = data;
                left = right = null;
            }
        }
        // BST root node
        Node root;

        // Constructor for BST =>initial empty tree
        BST_class(){
            root = null;
        }
        //delete a node from BST
        void deleteKey(int key) {
            root = delete_Recursive(root, key);
        }

        //recursive delete function
        Node delete_Recursive(Node root, float key)  {
            //tree is empty
            if (root == null)  return root;

            //traverse the tree
            if (key < root.key)     //traverse left subtree
                root.left = delete_Recursive(root.left, key);
            else if (key > root.key)  //traverse right subtree
                root.right = delete_Recursive(root.right, key);
            else  {
                // node contains only one child
                if (root.left == null)
                    return root.right;
                else if (root.right == null)
                    return root.left;

                // node has two children;
                //get inorder successor (min value in the right subtree)
                root.key = minValue(root.right);

                // Delete the inorder successor
                root.right = delete_Recursive(root.right, root.key);
            }
            return root;
        }

        float minValue(Node root)  {
            //initially minval = root
            float minval = root.key;
            //find minval
            while (root.left != null)  {
                minval = root.left.key;
                root = root.left;
            }
            return minval;
        }

        // insert a node in BST
        void insert(float key)  {
            root = insert_Recursive(root, key);
        }

        //recursive insert function
        Node insert_Recursive(Node root, float key) {
            //tree is empty
            if (root == null) {
                root = new Node(key);
                return root;
            }
            //traverse the tree
            if (key < root.key)     //insert in the left subtree
                root.left = insert_Recursive(root.left, key);
            else if (key > root.key)    //insert in the right subtree
                root.right = insert_Recursive(root.right, key);
            // return pointer
            return root;
        }

        // method for inorder traversal of BST
        void inorder() {
            inorder_Recursive(root);
        }

        // recursively traverse the BST
        void inorder_Recursive(Node root) {
            if (root != null) {
                inorder_Recursive(root.left);
                System.out.print(root.key + " ");
                inorder_Recursive(root.right);
            }
        }

        float search(float key)  {
            return search_Recursive(root, key, (float) Double.POSITIVE_INFINITY);
        }

        //recursive insert function
        float search_Recursive(Node root, float key,float min_val)  {
            // Base Cases: root is null or key is present at root

            if (root==null) {
                return min_val;
            }
            if (root.key == key){
                return key;
            }
            if (root.key > key && root.key < min_val){
                min_val =  root.key;
            }
            // val is greater than root's key
            if (root.key > key)
                return search_Recursive(root.left, key,min_val);
            // val is less than root's key
            return search_Recursive(root.right, key,min_val);
        }
    }
}
