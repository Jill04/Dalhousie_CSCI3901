/**
 * The TreeNode class stores the information about each node in the tree 
 * 
 */
public class TreeNode {
  
   TreeNode left;
   TreeNode right;
   TreeNode parent;
   String item;
   int searchCount;
   
   //A default constructor that creates an empty node.
   public TreeNode() {
	   this.item = null;
	   this.searchCount = 0;
	   this.left = null;
	   this.right = null;
	   this.parent = null;
   }
   //Parameterized constructor to store the parent and item of the node. 
   public TreeNode(String item, TreeNode parent) {
	   this.item = item;
	   this.searchCount = 0;
	   this.left = null;
	   this.right = null;
	   this.parent = parent;
   }
   
   /**
	  * Determines whether the node is leaf node or not 
	  *
	  * @return           String
	*/
   public boolean isLeafNode() {
	   boolean isLeaf = false;
	   
	   //Checks if both left node and right node is null or not
	   if(this.left == null && this.right == null) {
		   isLeaf = true;
	   }
	   return isLeaf;
   }
   
   /**
	  * Returns the item of the node 
	  *
	  * @return           String
	*/
    public String getItem() {
	  return this.item;
   }
 
   /**
	  * Returns the left node 
	  *
	  * @return           TreeNode
	*/
   public TreeNode getLeftNode() {
	   return this.left;
   }
   
   /**
	  * Returns the right node 
	  *
	  * @return           TreeNode
	*/
   public TreeNode getRightNode() {
	   return this.right;
   }
   
   /**
	  * Gets the search count 
	  *
	  * @return           int
	*/
   public int getSearchCount() {
	  return this.searchCount;
   }
   

   /**
	  * Returns the parent node 
	  *
	  * @return           TreeNode
	*/
   public TreeNode getParent() {
	  return this.parent;
   }
   
   /**
	  * Sets the left node 
	  *
	  * @param   left	  Left Tree Node
	  * @return           TreeNode
	*/
   public void setLeftNode(TreeNode left) {
	   this.left = left;
   }
   
   /**
	  * Sets the right node 
	  *
	  * @param   right	  Right Tree Node
	  * @return           TreeNode
	*/
   public void setRightNode(TreeNode right) {
	   this.right = right;
   } 
}
