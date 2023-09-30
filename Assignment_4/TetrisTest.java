
public class TetrisTest {
 public static void main(String[] args) {
	 TetrisSolver tetris = new TetrisSolver(8,6);
	 tetris.addPuzzleRow(" **** ");
	 tetris.addPuzzleRow(" **** ");

	 System.out.println(tetris.showPuzzle());
	 System.out.println("Id :-"+tetris.addPuzzlePiece(" **\\n** \\n", 3));
	 
	 //Adding same piece again
	 System.out.println("Id :-"+tetris.addPuzzlePiece(" **\\n** \\n", 3));
	 
	 System.out.println("Id :-"+tetris.addPuzzlePiece("**\\n*\\n*\\n", 4));

	 tetris.placePiece(2, 0);
	 
	 System.out.println(tetris.showPuzzle());
 }
}
