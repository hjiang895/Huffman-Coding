import java.util.TreeMap;

public class HuffTree implements Comparable<HuffTree> {

  // instance variables
  Node top;
  int size;
  TreeMap<Integer, String> codes = new TreeMap<Integer, String>();

  // Node class with various useful constructors.
  public class Node {
    Node rchild;
    Node lchild;
    int weight;
    int character;

    public Node (Node lchild, Node rchild, int weight) {
      this.lchild = lchild;
      this.rchild = rchild;
      this.weight = weight;
    }

    public Node (int weight, int character) {
      this.weight = weight;
      this.character = character;
    }

    public Node() {
      rchild = null;
      lchild = null;
      weight = 0;
      character = -1;
    }
  }

  // Constructors for HuffTree
  public HuffTree() {
    this.top = new Node();
  }

  public HuffTree(int character, int weight) {
    Node newnode = new Node(weight, character);
    this.top = newnode;
  }

  // compareTo() method
  public int compareTo(HuffTree other) {
    if (this.top.weight > other.top.weight) {
      return 1;
    } else if (this.top.weight == other.top.weight) {
      return 1;
    } else {
      return -1;
    }
  }

  private int totalWeight(Node n){
    if(n == null){
      return 0;
    }
    else{
      return n.weight + totalWeight(n.lchild) + totalWeight(n.rchild);
    }
  }

  //helper method that recursively adds the weights of individual nodes in the
  //HuffTree to find the total weight of the HuffTree

  // mergeTrees() method
  public void mergeTrees(HuffTree left, HuffTree right) {

    ///////////////////////////////
    // YOUR CODE HERE FOR TASK 2 //
    ///////////////////////////////
    top.rchild = left.top;
    top.lchild = right.top;
    top.weight = totalWeight(top);

    //we assign the top node of the first and second argument to the
    //right child and left child of the calling HuffTrees
    //then we call the totalWeight method to find the overall weight
    //of the calling HuffTree and assign it



    // This takes two HuffTree objects as arguments and merges those two trees
    // into the calling HuffTree object.
    // Make the right child of the calling HuffTree point at the top node
    // of the first argument HuffTree.
    // Make the left child of the calling HuffTree point at the top node
    // of the second argument HuffTree.
    // Make the weight variable of the calling HuffTree object be equal
    // to the sum of the weights of the two argument HuffTrees.
    // The character variable will remain unchanged (i.e., -1).
    // THIS WILL REQUIRED FEWER THAN 10 LINES OF CODE!


  }

  // getCodes() method
  public String getCodes(Node n, String s) {

    ///////////////////////////////
    // YOUR CODE HERE FOR TASK 3 //
    ///////////////////////////////

    // This method reads off the path to each leaf (character)
    //in the calling HuffTree and saves out the character-path pair
    // to its codes TreeMap instance variable.
    // Here is some pseudocode you might find useful.

    // if n is null
      //return s
      if(n == null){
        return s;
      }
      if(n.lchild == null && n.rchild == null){
        codes.put(n.character, s);
        System.out.println(s);
        return s;
      }
    // if the node's leftchild and rightchild are both null
      // you are at a leaf!
      // enter character of n and its path as a key-value pair in the codes TreeMap
      // return s
      if(n.lchild != null){
        String str = s + "0";
        return getCodes(n.lchild, str);
      }
   // if the left child is not null
      // create a new String by appending "0" to path
      // call the method with the left child and the new string as arguments
      if(n.rchild != null){
        String str1 = s + "1";
        return getCodes(n.rchild, str1);

      }
   // if the right child is not null
      // create a new string by appending "1" to path
      // call the method with the left child and the new string as arguments
      return "";
  }

}
