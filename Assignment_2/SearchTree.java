/*
 * The Search Tree focuses on the implementation of SearchTreeInterface
 */
public class SearchTree implements SearchTreeInterface {
	
  //Root node of the tree
  TreeNode root;
  //Keeps track of the current searched node
  TreeNode currentSearchNode;
  //Number of nodes in the tree
  public int size = 0;
  
  /*
   * Default Constructor
   */
  public SearchTree() {
	  this.root = null;
  }
  
  @Override
  public int find(String key) {
	  //Determines whether key is empty or null
	  if(!notNull(key) || key.isEmpty()) {
		  return -1;
	  }
	  //Returns zero if there is no node in the tree
	  if(this.root == null)return 0;
	  
	  TreeNode current = this.root;
	  //Returns the depth of the node
	  int depth = findDepth(current,key,true);
	  
	  //If the node is found, its search is determined 
	  if(depth > 0)
	  {
		  //Checks whether the search count of the current search node is greater
		  // than parent or not
		 checkSearchCount(this.currentSearchNode.getParent(),this.currentSearchNode);
	  }
	  return depth;
	}
  
  @Override	  
  public boolean add(String key) {
	//Determines whether the key is null or empty
	  if(!notNull(key) || key.isEmpty()) {
		  return false;
	  }
	  boolean isAdded = false;
	  int depth = 0;
	  
	  //Add the first node in an empty tree
	  if (this.root == null) {
		  this.root = new TreeNode(key,null);
		  size++;
		  return true;
	  }
	  else {
		  //find the depth of the node
		  depth = findDepth(this.root,key,false);
		 //if the key already exists the node is not added
		  if(depth >= 0) {
			  System.out.println("Key already exists");
			  return isAdded;
		  }
	  }
	  //Finds the location where the node can be added 
	  TreeNode location = findLast(key);
	 // Returns true if the child node is added
	  return addChild(location, new TreeNode(key,null));
  }
  
  @Override
  public void reset() {
	  if(this.root == null) {
		  System.out.println("Error: Cannot reset an empty tree");
	  }
	  else if(size == 1) {
		  this.root.searchCount = 0;
	  }
	  else{
		  //Traverses through each node in the tree and resets the search node 
		  traverseTree(this.root);
	  }
  }
  
  @Override
  public String printTree() {
	  if(root == null) {
		 return("Error: Tree is empty");
	  }
	  else if(size == 1) {
		  return(root.getItem()+" "+"0");
	  }
	  StringBuilder tree = new StringBuilder();
		TreeNode location;
		  location = root;
		  return inOrder(tree,location).toString();
  }  
    
  /***** Helper Functions *****/
  
  /*
   * Finds the depth of node
   * @param  root   Root node of the tree
   * @param  item   String item 
   * @return        int(depth)
   */
 private int findDepth(TreeNode root, String item, boolean flag)
{
      
    // Returns -1 if root is null
    if (root == null)
        return -1;
  
    // Initialize distance as -1
    int dist = -1;
    
    //Increments the search count each time an element is searched
    if(flag) {
    if (root.item.equalsIgnoreCase(item)){
		root.searchCount++;
		this.currentSearchNode = root;
     }
	} 
    
    // Check if item is current node
    if ((root.item.equalsIgnoreCase(item))|| 
      
        // Otherwise, check if item is
        // present in the left subtree
        (dist = findDepth(root.getLeftNode(), item, flag)) >= 0 || 
          
        // Otherwise, check if x is
        // present in the right subtree
        (dist = findDepth(root.getRightNode(), item,flag)) >= 0)
         
        // Return depth of the node
        return dist + 1;
     
    return dist;
}
 
 /*
  * Finds the last node of the tree
  * 
  * @param  key  String key
  * @return      Last tree node
  */
 private TreeNode findLast(String key) {
	 TreeNode current = this.root;
	 TreeNode previous = null;
	
	 while(current != null) {
		 previous = current;
		 
		 //Checks if key exists or not by sorting lexicographical order
		 int comp = sortItems(key, current.getItem());
		 
		 if(comp < 0) {
			 current = current.getLeftNode();	
		 }
		 else if(comp > 0){
			 current = current.getRightNode();
		 }
		 else {
			 return current;
		 }
	 }
	 return previous;
 }
 /*
  * Adds the node
  * 
  * @param  location   Location where child node needs to be added
  * @param  newNode    New node to be added
  * @return 		   boolean
  */
  private boolean addChild(TreeNode location, TreeNode newNode) {
	 
	  int comp = sortItems(newNode.getItem(),location.getItem());
	  
	  //if new node is less than its parent, add it to left
	  if(comp < 0) {
			 location.setLeftNode(newNode);	
		 }
	  //if new node is greater than its parent, add it to right
		 else if(comp > 0){
			 location.setRightNode(newNode);
		 }
	   newNode.parent = location;
	
	   size++;
	   return true;
  }
  
  /*
   * In order traversal of tree for printing the tree
   */
  private StringBuilder inOrder(StringBuilder tree,TreeNode node) {
	    
	    if (node == null) {
	      return null;
	    }   
	    
	    inOrder(tree,node.getLeftNode());
	    tree.append(node.getItem()).append(" ").append(findDepth(root,node.getItem(),false)).append("\n");
	    inOrder(tree,node.getRightNode());
	    
	    return tree;
	  }
  /*
   * Traverse tree
   */
  private void traverseTree(TreeNode node) {
	    if (node != null) {
	    	 node.searchCount=0;
			 traverseTree(node.getLeftNode());
			 traverseTree(node.getRightNode());
	    }
	}
  
  /*
   * Checks for empty input
   */
  private boolean notNull(Object obj) {
	if(obj != null) {
		return true;
	}
	System.out.println(" Error: Empty or Null Input");
	return false;
  }
  
 /*
  * Checks for the search count
  * @param    parent     Parent node
  * @param    child      Child node
  * @return              boolean
  */
  private boolean checkSearchCount(TreeNode parent, TreeNode child) {
	if(child.getSearchCount() > parent.getSearchCount()) {
		//Rotate left if left node
		if (isLeftNode(parent))
			{
			rotateLeftNode(child);
			}
		else {
			rotateRightNode(child);
		}
		return true;
	}
	return false;
  }
  
  /*
   * Performs Left Rotation
   * 
   * @param  node  TreeNode
   */
  private void rotateLeftNode(TreeNode node) {
    
	//Get the parent node
	TreeNode parentNode = node.parent;
	
	//Used when handling leaf node or node of depth 2 or greater
	if(parentNode.parent != null)  {
	    
		//Gets the parent node of the parent
		TreeNode PparentNode = parentNode.parent;
		
		if (PparentNode.getLeftNode() == parentNode) {
		    PparentNode.setLeftNode(node);;	    
		} else {
		    PparentNode.setRightNode(node);
		}
	
		parentNode.setLeftNode(node.getRightNode()); 
		node.setRightNode(parentNode);
	   	node.parent = PparentNode;
	    parentNode.parent = node;
		
	} else {
	    parentNode.setLeftNode(node.getRightNode());
	    node.setRightNode(parentNode);
	    this.root = node;
	    node.parent = null;
	    parentNode.parent = node;
	    
	}
  }

  /*
   * Performs Right Rotation
   * 
   * @param  node  TreeNode
   */
  private void rotateRightNode(TreeNode node) {
	//Get the parent node
	TreeNode parentNode = node.parent;
	
	//Used when handling leaf node or node of depth 2 or greater
	if(parentNode.parent != null) {
		
		TreeNode PparentNode = parentNode.parent;
		
		if (PparentNode.getLeftNode() == parentNode) {
		    PparentNode.setLeftNode(node);
		} else {
		    PparentNode.setRightNode(node);
		}
		
		parentNode.setRightNode(node.getLeftNode());
		node.setLeftNode(parentNode);
		node.parent = PparentNode;
		parentNode.parent = node;
	}
	else {
		parentNode.setRightNode(node.getLeftNode());
		node.setLeftNode(parentNode);
		this.root = node;
		node.parent = null;
		parentNode.parent = node;
	}
   }
 /*
  * Determines whether current node is left or not
  * 
  * @param  parent   Parent node
  * @return          boolean
  */
  private boolean isLeftNode(TreeNode parent) {
	if(parent.getLeftNode()== currentSearchNode) {
		return true;
	}
	else return false;
  }
 
  //Sorts keys in lexicographical order
  private int sortItems(String item1, String item2) {
	return(item1.compareToIgnoreCase(item2));	
 }
}
