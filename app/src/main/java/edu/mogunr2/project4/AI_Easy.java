package edu.mogunr2.project4;

// A simple AI that just picks any open spot on the board
public class AI_Easy {
    int getMove(String[] board){
        for (int i = 0; i < board.length; i++) {
            if(board[i].equals("b")){
                return i;
            }
        }
        return 0;
    }
}
