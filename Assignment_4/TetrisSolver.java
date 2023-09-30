import java.util.HashMap;

import java.util.Iterator;
import java.util.Map;

import java.util.Map.Entry;

public class TetrisSolver {
 // height and width of the tetris
 int height;
 int width;
 
 //Stores the possible rotations of each tetromino shape
 Map<String,Integer>possibleRotations = new HashMap<String,Integer>();
 
 //Stores the tetromino with its frequency
 Map<Integer,HashMap<String, Integer>> tetrominoShape = new HashMap<Integer,HashMap<String, Integer>>();
 public char[][] tetrisBoard ;
 
 //Keeps track of current position of the last tetromino
 int currentRowPointer = height;
 int placePointer;
 int tetrominoCounter = 0;
 
 //Sets the height and width of the tetris board
   public TetrisSolver(int height, int width) {
	   if(height > 0 && width > 0) {
		   if(height > width) {
			   setHeight(height);
			   setWidth(width);  
			   currentRowPointer = this.height;
			   tetrisBoard =  new char[height][width];
		   }
		   else {
			   System.out.println("Error: Height should be more than width");
		   }
	   }
	   else{
		   System.out.println("Error: Zero or negative value");
	   }
	 }
   
    // Getter and Setter functions
	public int getHeight() {
		return height;
	}
	
	public void setHeight(int height) {
		this.height = height;
	}
	
	public int getWidth() {
		return width;
	}
	
	public void setWidth(int width) {
		this.width = width;
	}
    
	/*
	 * To add the tetromino row in the puzzle
	 * 
	 * @param   String   tetromino row
	 * @return  void
	 */
	public void addPuzzleRow(String nextRow) {
		
		//Checks whether the row is empty or null
		if(nextRow.isBlank() || !notNull(nextRow)) {}
		else {
			//performs row validations
			if(rowValidations(nextRow)) {
					for(int i = 0; i < this.width ; i++) {
						tetrisBoard[currentRowPointer - 1][i] = nextRow.charAt(i);
					}
				}
				currentRowPointer--;
			}
	   }
	/*
	 * Displays the puzzle
	 * 
	 * @return   String
	 */
	public String showPuzzle() {
		String result = "";
		
		//Loops from the latest tetromino piece
		for(int i = currentRowPointer; i < this.height; i++) {
			for(int j = 0; j < this.width; j++) {
				char ch = tetrisBoard[i][j];
				
				// Add # for filled cell
				if(ch == '*') {
					result = result.concat("#");
				}
				
				//Add space for empty cell	
				else if((int)ch == 32) {
					 result = result.concat(" ");
					 }
				}
			  result = result.concat("\\n");
			}
			if(result.length() == 0) return "Error: Puzzle Error";
			else return  result;
		}
	
    /*
     * Adds the tetromino piece 
     * 
     * @param  String  tetromino shape
     * @param  int     frequqncy of the shape being repeated
     * @return int     id of the shape
     */
	public int addPuzzlePiece(String piece, int relativeFrequency) {
		//Input validations
		if(notNull(piece) && !piece.isBlank() && relativeFrequency > 0) {
			
			//checks for valid piece string
			if(piece.contains("*") && piece.contains(" ") || piece.contains("\\n") ) {
				   
			//Checks if the first tetromino piece
			if(tetrominoShape.size() == 0) {
				 HashMap<String, Integer> map = new HashMap<String, Integer>();
					map.put(piece,relativeFrequency);
					
					//Increments the tetromino shape count
					tetrominoCounter = tetrominoCounter + 1;
					tetrominoShape.put(tetrominoCounter,map);
					
					//performs the tetromino rotations
					performRotations(piece,tetrominoCounter);
					return tetrominoCounter;
			 }
			 else {
				    //
					HashMap<String, Integer> map = new HashMap<String, Integer>();
					map.put(piece,relativeFrequency);
					
					//determines whether the tetromino already exists or not
					if(!tetrominoShape.containsValue(map)) {
				      
					  //Checks the piece if present in the rotated shape
					  if(!possibleRotations.containsKey(piece)) {
					
					      tetrominoCounter = tetrominoCounter + 1;
						  performRotations(piece,tetrominoCounter);
						  tetrominoShape.put(tetrominoCounter,map);
						  return tetrominoCounter;
					   }
				  }
			     }
			   return 0;
			} 
		}
	   return 0;
	}
	
	/*
	 * Places the piece in the tetris board
	 * 
	 * @param  int  Id of the piece
	 * @param  int  possible pieces to be considered
	 * @return int  final points of the adding piece
	 */
	public int placePiece(int pieceId, int lookahead) {
		//Input validations
		if(pieceId > 0 && lookahead >= 0 ) {
		  
			//fetch the piece on basis of id
		  String piece = fetchPiece(pieceId);
		  
		  //checks for null piec
		  if(piece != null) {
			  
			  //calculates the position
			  calculatePlacePosition(piece);
			  
			  //calculates the penalty points
			  calculatePenaltyPoints();
		  } 
	    }
		return 0;
	 }
	
	
	/************* Helper Functions *************/
	
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
	  
	  //Tests the row validations
	  private boolean rowValidations(String nextRow) {
		   if(nextRow.length() != this.width) {
			   System.out.println("Error: Invalid Row length");
			   return false;
		    }
		   else {
			   if(currentRowPointer == 0) {
				   System.out.println("Error: Max height reached");
				   return false;
			   }

			   int charCounter = 0;
			   for (int i = 0 ; i < nextRow.length() ;i++) {				   
				   if ((int)nextRow.charAt(i) == 42  ) {
					   charCounter++;
				   }
				   else if((int)nextRow.charAt(i) != 32  ) {
					   System.out.println("Error: Invalid row charcater");
					   return false;
				   }
			   }
			   
			   //puzzle should be same of the width
			   if(charCounter == this.width) {
				   System.out.println("Error: Cannot have entire filled row");
					return false;
			   }
			   return true;
		    }
		  }
	  
	  public String fetchPiece(Integer id){

		if(tetrominoShape.containsKey(id))  {
			 HashMap<String, Integer> map = tetrominoShape.get(id);
		 
			 for (Map.Entry<String,Integer> entry : map.entrySet()) 
		           return entry.getKey();
		 }
		return null;
	}
	
	//Perform shape rotations
	public void  performRotations(String piece,Integer pieceId) { 
		 int[][] matrix = new int[3][3];
		 int flag = 0;
		 int flag1 = 0;
		 int[][] matrixMirror = new int[3][3];
		 int[][] transpose = new int[3][3];
		 int[][] transposeMirror = new int[3][3];
		 
		 
		 // used for only square and line shape
		 
		 //line shape
		 if(piece == "****\\n") {
			 String str = "*\\n*\\n*\\n*\\n";
			 possibleRotations.put(piece, pieceId);
			 possibleRotations.put(str, pieceId);
		 }
		 else if (piece == "*\\n*\\n*\\n*\\n") {
			 String str = "****\\n";
			 possibleRotations.put(piece, pieceId);
			 possibleRotations.put(str, pieceId);
		 }
		 
		 //square shape
		 else if(piece == "**\\n**\\n") {
			 possibleRotations.put(piece, pieceId);
		 }
		 
		 //performs rotations on the rest of the shapes
		 else{
			 for(int i = 0; i< piece.length(); i++) {
				 if(piece.charAt(i) == '*') {
					 matrix[flag][flag1] = 1;
					 flag1++;
				 } 
				 else if((int)piece.charAt(i) == 32 ){
					 matrix[flag][flag1] = 0;
					 flag1++;
					 
				 }
				 else if((int)piece.charAt(i) == 92 ){
					 flag++;
					 flag1 = 0;
					 
				 }
		    }
			 //Adding of rotations in the map
			 possibleRotations.put(matrixToString(matrix),pieceId);
			 transpose = getTranspose(matrix);
			 possibleRotations.put(matrixToString(transpose),pieceId);
			 transposeMirror = rotate(transpose);
			 possibleRotations.put(matrixToString(transposeMirror),pieceId);
			 matrixMirror = rotate(matrix);
			 possibleRotations.put(matrixToString(matrixMirror),pieceId);
		     
		 }
			
	 }
	 //Calculates the position to add the piece
	  private void calculatePlacePosition(String piece) {
		  int lineCount = 0;
		  int currentBlockHeight = 0;
		  
		  //Counts the height space required for the piece to fit
		  for(int i = 0; i < piece.length() ; i++) {
			   if((int)piece.charAt(i) == 110) {
				   lineCount++;
			      }
		  }
		  for(int i = currentRowPointer; i < height ; i++) {
			  currentBlockHeight++;
		  }
		  //if the space is same
		 if(lineCount == currentBlockHeight) {
			 
			 //checks for the width space
			 while(!checkWidthSpace(piece)) {
				  currentRowPointer--;
			  }
			 
			 //add the piece if space is enough
			  addPiece(piece);
			  
		  }
		 
		  //if space is large enough
		  else if(lineCount < currentBlockHeight)
		  {
			  while(!checkWidthSpace(piece)) {
				  currentRowPointer--;
			  }
			  addPiece(piece);
		  }
		 
		 //if the space is less it moves the pointer and calls the method
		  else {
			       currentRowPointer--;
				  calculatePlacePosition(piece);
		  }
	  }
	  
	  //Determine the width space
	  private boolean checkWidthSpace(String piece) {
		  int spaceNeeded = 0;
		  int spaceAvailable = 0;
		  for(int i = 0; i < piece.length() ; i++) {
			  if((int)piece.charAt(i) == 92) {
				   break;
			      }
			   else {
				   spaceNeeded++;
			   }
			  }
		   for(int j = 0; j < width ; j++) {
				  if((int)tetrisBoard[currentRowPointer][j] == 32 || (int)tetrisBoard[currentRowPointer][j] == 0 ) {
					  spaceAvailable++;
				  }
				   else {
					   break;
				   }
				  } 
		
		  if(spaceAvailable >= spaceNeeded) {
			  return true;
		  }
		  else {
			  return false;
		  }
	  }
	  
	  //Add the piece in the tetris
	  private boolean addPiece(String piece) {
		  
		  String str = piece.replace("\\n", "~");
		  int flag = 0;	
		 
		  for(int i = currentRowPointer; i< height; i++) {
			  for(int j = 0 ; j < width; j++) {
				 
				    if(checkCharcater(str.charAt(flag),'*')) {
				    	 tetrisBoard[i][j]=str.charAt(flag);
							flag++;
				    	
				    }
					 else {
						 flag++;
					    	break;
					 }  
				 }
			 }
		  return false;
	  }
	  
	  //Checks for the character is same or not
	  private boolean checkCharcater(char ch, char pattern) {
		  if(ch == pattern) {
			  return true;
		  }
		  else return false;
	  }
	  
	  //returns the transpose of the matrix 
	  private int[][] getTranspose(int[][]matrix){
		  int[][] transpose = new int[3][3];
		  for(int i = 0; i< matrix.length; i++) {
			  for(int j = 0; j< matrix.length; j++) {
				  transpose[i][j] = matrix[j][i];
			  } 
		  }
		  return transpose;
	  }
	  
	  //Rotates teh matrix
	  private int[][] rotate(int[][]matrix){
		  int[][] mirror = new int[3][3];
		  int[][] inverse = {{0,0,1},{0,1,0},{1,0,0}};
				  
		  
		  for(int i = 0; i< matrix.length; i++) {
			  for(int j = 0; j< matrix.length; j++) {
				  for(int k = 0; k< matrix.length; k++) {
					  mirror[i][j] += matrix[i][k] * inverse[k][j];
				  }
				  
			  } 
		  }
		  return mirror;
	  }
	  
	  //Converts the matrix to string
	  private String matrixToString(int[][] matrix) {
		  
		  String str = "";
		  for(int i = 0; i< matrix.length; i++) {
			  for(int j = 0; j< matrix.length; j++) {
				  if(matrix[i][j] == 0) {
					  str = str.concat(" "); 
				  }
				  else if(matrix[i][j] == 1){
					  str = str.concat("*");
				  }
			  }
			  str = str.concat("\\n"); 
		  }
		  if(str.contains("   ")) {
			  str = str.replace("   \\n","");
			
		  }
		  return str;
	  }
	 
	  //Used for testing
//	  public void printRotations() {
//		  Iterator<Map.Entry<String, Integer>> itr = possibleRotations.entrySet().iterator();
//          
//	        while(itr.hasNext())
//	        {
//	             Map.Entry<String, Integer> entry = itr.next();
//	             System.out.println("Key = " + entry.getKey() + 
//	                                 ", Value = " + entry.getValue());
//	        }
//	  }
	  
	  //Calculates the penalty points
	  private int calculatePenaltyPoints() {
		  int emptyCell = 0;
		  int filledCell = 0;
		  int mid = 0;
		  if(width%2 == 0) {
				 mid = width/2;
			 }
		  else {
			  mid = Math.round(width/2);
		  }
		  
		 
		  for(int i = currentRowPointer; i<height ; i++) {
			  for(int j = 0;j < width; j++) {
				  
				 //penalty point calculation for empty cell
				 if((int)tetrisBoard[i][j] == 32) {
					 emptyCell += 7*(i - currentRowPointer);
				 }
				 
				 //penalty point calculation for filled cell
				 else {
					 filledCell += 10*((height-1)-i) + 1 *(mid -j);
				 }
			  }
		  }
		  return emptyCell+filledCell;
	  }
	
	 
}
	   
	  
