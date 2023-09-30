/**
 * The SearchTreeTest class initiates the object of the Search Tree and performs valid tests 
 * 
 */
public class SearchTreeTest {
	 
	 static public class InputValidation{
		  SearchTree tree = new SearchTree();
		  
		  public void addNullString() {
	          System.out.println("-------Add Null Input -------");
	          assert(false == tree.add(null));
	          System.out.println("tree.add(null): " + tree.add(null));
	      }
		  
		  public void addEmptyString() {
	          System.out.println("------- Add Empty Input -------");
	          assert(false == tree.add(" "));
	          System.out.println("tree.add(\"\"): " + tree.add(""));
	      }
		  
		  public void findNullString() {
	          System.out.println("------- Find Null Node -------");
	          assert(-1 == tree.find(null));
	          System.out.println("tree.find("+"): " + tree.find(null));
	      }
		  
		  public void findEmptyString() {
	          System.out.println("------- Find Empty Node -------");
	          assert(-1 == tree.find(""));
	          System.out.println("tree.find(\"\"): " + tree.find(""));
	      }
	 }

      static public class BoundaryCases{
        SearchTree tree;
		  
		  public void add1Character() {
			  tree = new SearchTree();
	          System.out.println("-------Add One Character String -------");
	          assert(true == tree.add("X"));
	          assert(true == tree.add("J"));
	          assert(true == tree.add("Z"));
	          assert(true == tree.add("A"));
	          tree.add("X");
	          tree.add("J");
	          tree.add("Z");
	          tree.add("A");
	          System.out.println(tree.printTree());
	       }
		  
		  public void addLongString() {
			  tree = new SearchTree();
	          System.out.println("------- Add Long String -------");
	          assert(true == tree.add("The Sun rises in east"));
	          assert(true == tree.add("and sets in west."));
	          assert(true == tree.add("Venus is the last planet"));
	     
	          tree.add("The Sun rises in east");
	          tree.add("and sets in west.");
	          tree.add("Venus is the last planet");
	         
	          System.out.println(tree.printTree());
	       }
		  
		  public void find1Character() {
			  tree = new SearchTree();
	          System.out.println("------- Find One Character -------");
	         
	          tree.add("D");
	          tree.add("A");
	          tree.add("T");
	          tree.add("E");
              assert(0 == tree.find("T"));
	          System.out.println("tree.find(T): " + tree.find("T"));
	       }
		  
		  public void findLongString() {
			  tree = new SearchTree();
	          System.out.println("------- Find Long String -------");
	         
	          tree.add("Start");
	          tree.add("Find the longest string so far");
	          tree.add("End");
              assert(0 == tree.find("Find the longest string so far"));
	          System.out.println("tree.find(Find the longest string so far): " +
              tree.find("Find the longest string so far"));
	       }
		  
		  public void resetEmptyTree() {
			  tree = new SearchTree();
	          System.out.println("-------Reset Empty Tree-------");
	          tree.reset();
	       }
		  
		  public void resetOneNodeTree() {
			  tree = new SearchTree();
	          System.out.println("-------Reset One Node Tree-------");
	          tree.add("Venus");
	          tree.find("Venus");
	          System.out.println("Number of nodes :"+tree.size);
		         
	          System.out.println("Search count before reset  :"+tree.currentSearchNode.searchCount);
	          tree.reset();
	          System.out.println("Search count after reset :"+tree.currentSearchNode.searchCount);   
	       }
		  
		  public void resetAndPrintLongTree() {
			  tree = new SearchTree();
	          System.out.println("-------Reset And Print Big Tree-------");
	          tree.add("Jupiter");
	          tree.add("Earth");
	          tree.add("Saturn");
	          tree.add("Venus");
	          tree.add("Mars");
	          tree.add("Mercury");
	          tree.add("Uranus");
	          
	          System.out.println(tree.printTree());
	          tree.find("Earth");
	          tree.find("Earth");
	          
	          System.out.println("Before reset search count of Earth :"+tree.currentSearchNode.searchCount);
	          tree.reset();
	          System.out.println("After reset search count of Earth :"+tree.currentSearchNode.searchCount);   
	       }
      }
		  
		  
	static public class ControlFlow{
		SearchTree tree;
		
		 public void addStringAlphabetically() {
			  tree = new SearchTree();
	          System.out.println("-------Adding Keys in alphabetical order-------");
	          tree.add("Apple");
	          tree.add("Carrot");
	          tree.add("Cheese");
	          tree.add("Date");
	          tree.add("Egg");
	          tree.add("Fig");
	          tree.add("Lentils");
	          tree.add("Yam");
	          
	          System.out.println(tree.printTree());
	        }
		 
		 public void addStringReverseAlphabetically() {
			  tree = new SearchTree();
	          System.out.println("-------Adding Keys in reverse alphabetical order-------");
	          tree.add("Zimbabwe");
	          tree.add("Venice");
	          tree.add("Phuket");
	          tree.add("Madagascar");
	          tree.add("Cosrica");
	          tree.add("China");
	          tree.add("Amsterdam");
	          System.out.println(tree.printTree());
	        }
		 
	     public void stringSearchLeft() {
			  tree = new SearchTree();
	          System.out.println("-------Search Sequences-------");
	          tree.add("Egg");
	          tree.add("Carrot");
	          tree.add("Lentil");
	          tree.add("Date");
	          tree.add("Beans");
	          tree.add("Apple");
	          tree.add("Caramel");
	          tree.add("Fig");
	          tree.add("Yam");
	          
	          System.out.println("Before rotations:"+"\n"+tree.printTree());
	          
	          tree.find("Carrot");
	          tree.find("Egg");
	          tree.find("Egg");
	          System.out.println("Left child then right child: "+"\n"+tree.printTree());
	          
	          tree.reset();
	          
	          tree.find("Carrot");
	          tree.find("Beans");
	          tree.find("Beans");
	          System.out.println("Left child then left child: "+"\n"+tree.printTree());   	          
		    }
	     
		 public void stringSearchRight() {
			  SearchTree tree1 = new SearchTree();
	          System.out.println("-------Search Sequences-------");
	          tree1.add("Egg");
	          tree1.add("Carrot");
	          tree1.add("Lentil");
	          tree1.add("Date");
	          tree1.add("Apple");
	          tree1.add("Fig");
	          tree1.add("Yam");
	          
	          System.out.println("Before rotations"+"\n"+tree.printTree());
	          tree1.find("Yam");
			  tree1.find("Lentil");
			  tree1.find("Lentil");
	          System.out.println("Right child then right child: "+"\n"+tree.printTree());
	         
	          tree1.reset();
	         
	          tree1.find("Lentil");
	          tree1.find("Fig");
	          tree1.find("Fig");
	          System.out.println("Right child then left child: "+"\n"+tree.printTree());
		    }
		 
		 public void addItemAlreadyAdded() {
			  tree = new SearchTree();
	          System.out.println("------- Repeated Items-------");
	          tree.add("Egg");
	          assert(false == tree.add("Egg"));
	          System.out.println("add(Egg):"+ tree.add("Egg"));
	        }
		 
		 public void findItemExists() {
			  tree = new SearchTree();
	          System.out.println("------- Find Items -------");
	          tree.add("Egg");
	          tree.add("Apple");
	          assert(0 == tree.find("Egg"));
	          System.out.println("find(Egg):"+ tree.find("Egg"));
	          
	          assert(-1 == tree.find("Beans"));
	          System.out.println("find(Beans):"+ tree.find("Beans"));
	       }
	}
		 
	 static public class DataFlow{
		 
		 public void dataFlow() {
			  SearchTree tree = new SearchTree();
			  
	          System.out.println("------- Find Before Add -------");
	          System.out.println("find(Egg):"+ tree.find("Egg"));
	          
	          System.out.println("------- Reset Before Add -------");
	          tree.reset();
	          
	          System.out.println("------- Print Tree Before Add -------");
	          System.out.println(tree.printTree());
	          
	          System.out.println("------- Reset Tree Twice -------");
	          tree.add("Date");
	          tree.add("Apple");
	          tree.add("Fig");
	          
	          tree.find("Date");
	          tree.find("Date");
	          
	          System.out.println("Before reset search count: "+tree.currentSearchNode.getSearchCount());
	          tree.reset();
	          tree.reset();
	          System.out.println("After reset search count: "+tree.currentSearchNode.getSearchCount());
	       }
	  }
	 
	public static void main(String[] args) {
         
		System.out.println("Input Validations"+"\n");
		InputValidation input  = new InputValidation();
		input.addNullString();
		input.addEmptyString();
		input.findNullString();
		input.findEmptyString();
		
		System.out.println("\n "+"Boundary Cases"+"\n");
		BoundaryCases boundary = new BoundaryCases();
		boundary.add1Character();
		boundary.addLongString();
		boundary.find1Character();
		boundary.findLongString();
		boundary.resetEmptyTree();
		boundary.resetOneNodeTree();
		boundary.resetAndPrintLongTree();
		
		System.out.println("\n "+"ControlFlow Cases"+"\n");
		ControlFlow control = new ControlFlow();
		control.addStringAlphabetically();
		control.addStringReverseAlphabetically();
		control.stringSearchLeft();
		control.stringSearchRight();
		control.addItemAlreadyAdded();
		control.findItemExists();
		
		System.out.println("\n "+"DataFlow Cases"+"\n");
		DataFlow data = new DataFlow();
		data.dataFlow();
		
	}
 
}
