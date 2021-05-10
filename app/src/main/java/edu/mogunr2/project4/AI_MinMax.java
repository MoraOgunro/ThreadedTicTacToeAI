package edu.mogunr2.project4;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * This class is used to read in a state of a tic tac toe board. It creates a MinMax object and passes the state to it. What returns is a list 
 * of possible moves for the player X that have been given min/max values by the method findMoves. The moves that can result in a win or a 
 * tie for X are printed out with the method printBestMoves()
 * 
 * @author Mark Hallenbeck
 *
 * Copyright© 2014, Mark Hallenbeck, All Rights Reservered.
 *
 */
public class  AI_MinMax {
	
	private String[] init_board;
	
	private ArrayList<edu.mogunr2.project4.Node> movesList;
	
	AI_MinMax(String[] board)
	{
		init_board = board;
		if(init_board.length != 9)
		{
			System.out.println("You have entered an invalid state for tic tac toe, exiting......");
			System.exit(-1);
		}
		
		MinMax sendIn_InitState = new MinMax(init_board);
		
		movesList = sendIn_InitState.findMoves();
		
		//printBestMoves();
	}
	
	/**
	 * reads in a string from user and parses the individual letters into a string array
	 * @return String[]
	 */
	private String[] getBoard()
	{
			String puzzle;
			String[] puzzleParsed;
			String delim = "[,]+";
			
			//give input message
			System.out.println("Enter a string to represent the board state:");
			
			Scanner userInput = new Scanner("");		//open scanner
			
			puzzle = userInput.nextLine();					//scan in string
			
			puzzleParsed = puzzle.split(delim);
			userInput.close();   	  						//close scanner
			
			return puzzleParsed;
			
	}
	
	/**
	 * goes through a node list and prints out the moves with the best result for player X
	 * checks the min/max function of each state and only recomends a path that leads to a win or tie
	 */
	public ArrayList<Integer> printBestMoves()
	{
		System.out.print("\n\nThe moves list is: < ");
		ArrayList<Integer> bestMovesList = new ArrayList<>();
		for(int x = 0; x < movesList.size(); x++)
		{
			Node temp = movesList.get(x);
			
			if(temp.getMinMax() == 10 || temp.getMinMax() == 0)
			{
				bestMovesList.add(temp.getMovedTo()-1);
				System.out.print(temp.getMovedTo() + " ");
			}
		}
		
		System.out.print(">");
		return bestMovesList;
	}

	ArrayList getBestMoves(String turn){
		ArrayList moves = new ArrayList();
		if(turn.equals("X")){
			for(int x = 0; x < movesList.size(); x++)
			{
				Node temp = movesList.get(x);

				if(temp.getMinMax() == 10 || temp.getMinMax() == 0)
				{
					moves.add(temp.getMovedTo());
				}
			}
		}else{
			for(int x = 0; x < movesList.size(); x++)
			{
				Node temp = movesList.get(x);

				if(temp.getMinMax() == 10 || temp.getMinMax() == 0)
				{
					moves.add(temp.getMovedTo());
				}
			}
			if(moves.size() == 0){
				for(int x = 0; x < movesList.size(); x++)
				{
					Node temp = movesList.get(x);

					if(temp.getMinMax() == -10)
					{
						moves.add(temp.getMovedTo());
						return moves;
					}
				}
			}
		}
		return moves;
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		//edu.mogunr2.project4.AI_MinMax startThis = new edu.mogunr2.project4.AI_MinMax();
	}

}
