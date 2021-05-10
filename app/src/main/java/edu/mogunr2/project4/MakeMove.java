package edu.mogunr2.project4;

import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.Callable;
/*
This callable is used by the two threads. X will use AI_MinMax,
O will use AI_Easy
 */
public class MakeMove implements Callable{
    String[] board;
    String turn;
    String difficulty;

    public MakeMove(String[] board, String turn, String difficulty) {
        this. board = board;
        this.turn = turn;
        this.difficulty = difficulty;
    }
    @Override
    public Object call() throws Exception {
        if(difficulty.equals("Normal")){
            AI_MinMax round = new AI_MinMax(board);
            ArrayList<Integer> bestMoves = round.getBestMoves(turn);
            Collections.shuffle(bestMoves);
            System.out.println("\n" + "player: " + turn + " chooses index: "+bestMoves.get(0));
            return bestMoves.get(0) -1;
        }else{
            AI_Easy round = new AI_Easy();
            return round.getMove(board);
        }
    }
}