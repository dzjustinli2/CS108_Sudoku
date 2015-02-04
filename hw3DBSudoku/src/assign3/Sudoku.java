package assign3;

import java.util.*;

/*
 * Encapsulates a Sudoku grid to be solved.
 * CS108 Stanford.
 */
public class Sudoku {
	// Provided grid data for main/testing
	// The instance variable strategy is up to you.
	
	// Provided easy 1 6 grid
	// (can paste this text into the GUI too)
	public static final int[][] easyGrid = Sudoku.stringsToGrid(
	"1 6 4 0 0 0 0 0 2",
	"2 0 0 4 0 3 9 1 0",
	"0 0 5 0 8 0 4 0 7",
	"0 9 0 0 0 6 5 0 0",
	"5 0 0 1 0 2 0 0 8",
	"0 0 8 9 0 0 0 3 0",
	"8 0 9 0 4 0 2 0 0",
	"0 7 3 5 0 9 0 0 1",
	"4 0 0 0 0 0 6 7 9");
	
	
	// Provided medium 5 3 grid
	public static final int[][] mediumGrid = Sudoku.stringsToGrid(
	 "530070000",
	 "600195000",
	 "098000060",
	 "800060003",
	 "400803001",
	 "700020006",
	 "060000280",
	 "000419005",
	 "000080079");
	
	// Provided hard 3 7 grid
	// 1 solution this way, 6 solutions if the 7 is changed to 0
	public static final int[][] hardGrid = Sudoku.stringsToGrid(
	"3 7 0 0 0 0 0 8 0",
	"0 0 1 0 9 3 0 0 0",
	"0 4 0 7 8 0 0 0 3",
	"0 9 3 8 0 0 0 1 2",
	"0 0 0 0 4 0 0 0 0",
	"5 2 0 0 0 6 7 9 0",
	"6 0 0 0 2 1 0 4 0",
	"0 0 0 5 3 0 9 0 0",
	"0 3 0 0 0 0 0 5 1");
	
	
	public static final int SIZE = 9;  // size of the whole 9x9 puzzle
	public static final int PART = 3;  // size of each 3x3 part
	public static final int MAX_SOLUTIONS = 100;
	
	// Provided various static utility methods to
	// convert data formats to int[][] grid.
	
	/**
	 * Returns a 2-d grid parsed from strings, one string per row.
	 * The "..." is a Java 5 feature that essentially
	 * makes "rows" a String[] array.
	 * (provided utility)
	 * @param rows array of row strings
	 * @return grid
	 */
	public static int[][] stringsToGrid(String... rows) {
		int[][] result = new int[rows.length][];
		for (int row = 0; row<rows.length; row++) {
			result[row] = stringToInts(rows[row]);
		}
		return result;
	}
	
	
	/**
	 * Given a single string containing 81 numbers, returns a 9x9 grid.
	 * Skips all the non-numbers in the text.
	 * (provided utility)
	 * @param text string of 81 numbers
	 * @return grid
	 */
	public static int[][] textToGrid(String text) {
		int[] nums = stringToInts(text);
		if (nums.length != SIZE*SIZE) {
			throw new RuntimeException("Needed 81 numbers, but got:" + nums.length);
		}
		
		int[][] result = new int[SIZE][SIZE];
		int count = 0;
		for (int row = 0; row<SIZE; row++) {
			for (int col=0; col<SIZE; col++) {
				result[row][col] = nums[count];
				count++;
			}
		}
		return result;
	}
	
	
	/**
	 * Given a string containing digits, like "1 23 4",
	 * returns an int[] of those digits {1 2 3 4}.
	 * (provided utility)
	 * @param string string containing ints
	 * @return array of ints
	 */
	public static int[] stringToInts(String string) {
		int[] a = new int[string.length()];
		int found = 0;
		for (int i=0; i<string.length(); i++) {
			if (Character.isDigit(string.charAt(i))) {
				a[found] = Integer.parseInt(string.substring(i, i+1));
				found++;
			}
		}
		int[] result = new int[found];
		System.arraycopy(a, 0, result, 0, found);
		return result;
	}

	@Override
	public String toString(){
		StringBuilder str = new StringBuilder();
		for(int i = 0; i < SIZE; i++){
			for(int j = 0; j < SIZE; j++){
				int value = originalGrid[i][j];
				str.append(Integer.toString(value));
				str.append(" ");
			}
			str.append("\n");
		}
		return str.toString();
	}

	public String toString(int[][] grid){
		StringBuilder str = new StringBuilder();
		for(int i = 0; i < SIZE; i++){
			for(int j = 0; j < SIZE; j++){
				int value = grid[i][j];
				str.append(Integer.toString(value));
				str.append(" ");
			}
			str.append("\n");
		}
		return str.toString();
	}

	// Provided -- the deliverable main().
	// You can edit to do easier cases, but turn in
	// solving hardGrid.
	public static void main(String[] args) {
		Sudoku sudoku;
		sudoku = new Sudoku(hardGrid);
		
		System.out.println(sudoku); // print the raw problem
		int count = sudoku.solve();
		System.out.println("solutions:" + count);
		System.out.println("elapsed:" + sudoku.getElapsed() + "ms");
		System.out.println(sudoku.getSolutionText());
	}
	
	private int numSolutions;
	private int[][] originalGrid;
	private ArrayList<int[][]> completedList;
	private ArrayList<Spot> spotsList;
	private long msTime;
	
	public Sudoku(String board){
		this(textToGrid(board));
	}

	/**
	 * Sets up based on the given ints.
	 */
	public Sudoku(int[][] ints) {
		numSolutions = 0;
		originalGrid = new int[SIZE][SIZE];
		completedList = new ArrayList<int[][]>();
		spotsList = new ArrayList<Spot>();
		msTime = 0;
		originalGrid = copyGrid(ints);
		for(int i = 0; i < SIZE; i++){
			for(int j = 0; j < SIZE; j++){
				if(originalGrid[i][j] == 0){
					Spot sp =  new Spot(originalGrid,i,j);
					spotsList.add(sp);
				}
			}
		}
		Collections.sort(spotsList);
	}
	
	/**
	 * Solves the puzzle, invoking the underlying recursive search.
	 */
	public int solve() {
		long ms1 = System.currentTimeMillis();
		recursion(originalGrid,0);
		long ms2 = System.currentTimeMillis();
		msTime = ms2 - ms1;
		return numSolutions;
	}
	
	private void recursion(int[][] grid, int index){
		if(numSolutions >= MAX_SOLUTIONS) return;
		if(isBoardComplete(grid)){
			numSolutions = numSolutions + 1;
			completedList.add(grid);
			return;
		}
		Spot sp = spotsList.get(index);
		ArrayList<Integer> possibleValues = sp.computePossibleValues(grid);
		int numPos = possibleValues.size();
		for(int j = 0; j < numPos; j++){
			int value = possibleValues.get(j);
			recursion(sp.setValue(grid,value),index+1);
		}
	}
	
	private boolean isBoardComplete(int[][] grid){
		for(int i = 0; i < SIZE; i++){
			for(int j = 0; j < SIZE; j++){
				int value = grid[i][j];
				if(value == 0) return false;
			}
		}
		return true;
	}
	
	public String getSolutionText() {
		if(numSolutions > 0){
			return toString(completedList.get(0));
		}
		return "";
	}
	
	public long getElapsed() {
		return msTime;
	}
	
	private int[][] copyGrid(int[][] oldGrid){
		//Makes a new grid
		int[][] newGrid = new int[SIZE][SIZE];
		for (int i = 0; i < SIZE; i++) {
			System.arraycopy(oldGrid[i], 0, newGrid[i], 0, SIZE);
		}
		return newGrid;
	}
	
	private class Spot implements Comparator<Spot>, Comparable<Spot>{
		private int row;
		private int col;
		private int startRow;
		private int startCol;
		private int size;
		
		public Spot(int[][] grid, int i, int j){
			row = i;
			col = j;
			startRow = startIndex(row);
			startCol = startIndex(col);
			ArrayList<Integer> possibleValues = computePossibleValues(grid);
			//The most constrained spots have a low number of possible values
			size = possibleValues.size();
		}
		
		//Finds all of the possible values for this spot
		public ArrayList<Integer> computePossibleValues(int[][] grid){
			HashSet<Integer> allowedValues = fillSet();
			for(int i = 0; i < SIZE; i++){
				Integer valueA = new Integer(grid[row][i]);
				Integer valueB = new Integer(grid[i][col]);
				if(allowedValues.contains(valueA)){
					allowedValues.remove(valueA);
				}
				if(allowedValues.contains(valueB)){
					allowedValues.remove(valueB);
				}
			}
			for(int i = 0; i < PART; i++){
				for(int j = 0; j < PART; j++){
					Integer value = new Integer(grid[startRow+i][startCol+j]);
					if(allowedValues.contains(value)){
						allowedValues.remove(value);
					}
				}
			}
			return new ArrayList<Integer>(allowedValues);
		}
		
		private HashSet<Integer> fillSet(){
			HashSet<Integer> hs = new HashSet<Integer>();
			for(int i = 1; i < 10; i++){
				hs.add(new Integer(i));
			}
			return hs;
		}
		
		//Returns a new grid that is altered
		public int[][] setValue(int[][] grid, int value){
			int[][] newGrid = copyGrid(grid);
			newGrid[row][col] = value;
			return newGrid;
		}
		
		private int startIndex(int value){
			int reduced = value/3;
			if(reduced == 0) return 0;
			if(reduced == 1) return 3;
			if(reduced == 2) return 6;
			return -1;
		}
		
	   // Overriding the compareTo method
	   public int compareTo(Spot sp){
		   Integer thisSize = new Integer(this.size);
		   Integer spSize = new Integer(sp.size);
	      return thisSize.compareTo(spSize);
	   }

	   // Overriding the compare method to sort by size
	   public int compare(Spot sp1, Spot sp2){
	      return sp1.size - sp2.size;
	   }
	   
	}

}
