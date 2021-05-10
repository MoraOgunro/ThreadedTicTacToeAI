package edu.mogunr2.project4;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.provider.Contacts;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.InputStream;
import java.net.URL;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public class MainActivity extends Activity {
    TextView textViewWinner;
    public static final int MADE_MOVE_X = 0;
    public static final int MADE_MOVE_O = 1 ;

    /*
    * This is the handler that the x thread and o thread communicate with
    * it is attached to the main thread.
    * each switch case will increment the round counter, update the board,
    * and check if someone has won yet.
    */
    public final Handler xHandler = new Handler(Looper.getMainLooper()) {
        public void handleMessage(Message msg) {
            int what = msg.what ;
            switch (what) {
                /*
                * triggered when x finishes, calls the function to update
                * the board, then starts o's thread
                * */
                case MADE_MOVE_X:
                    Log.i("IN X HANDLER","x moved.");
                    round++;
                    board = msg.getData().getStringArray("board");
                    setUpdateBoard();
                    if(!checkWhoWon(board).equals("")){
                        textViewWinner.setText(checkWhoWon(board));
                        threadRunning = false;
                        break;
                    }
                    oThread.run();
                    break;
                /*
                 * triggered when o finishes, calls the function to update
                 * the board, then starts x's thread
                 * */
                case MADE_MOVE_O:
                    Log.i("IN Y HANDLER","o moved.");
                    round++;
                    board = msg.getData().getStringArray("board");
                    setUpdateBoard();
                    if(!checkWhoWon(board).equals("")){
                        textViewWinner.setText(checkWhoWon(board));
                        threadRunning = false;
                        break;
                    }
                    xThread.run();
                    break;
            }
        }
    };

    String[] board;
    // Values to be used by handleMessage()
    Thread xThread;
    Thread oThread;
    int round;
    static boolean threadRunning = false;

    //A helper function to replace elements on the board with another
    //Useful for formatting the board for display
    void replace(String[] a, String current, String replacer){
        for (int i = 0; i < a.length; i++) {
            if(a[i].equals(current))
                    a[i] = replacer;
        }
    }
    //Updates the GUI
    void setUpdateBoard(){
        replace(board,"b","-");
        TextView t0 =  findViewById(R.id.boardBox0);
        t0.setText(board[0]);
        TextView t1 =  findViewById(R.id.boardBox1);
        t1.setText(board[1]);
        TextView t2 =  findViewById(R.id.boardBox2);
        t2.setText(board[2]);
        TextView t3 =  findViewById(R.id.boardBox3);
        t3.setText(board[3]);
        TextView t4 =  findViewById(R.id.boardBox4);
        t4.setText(board[4]);
        TextView t5 =  findViewById(R.id.boardBox5);
        t5.setText(board[5]);
        TextView t6 =  findViewById(R.id.boardBox6);
        t6.setText(board[6]);
        TextView t7 =  findViewById(R.id.boardBox7);
        t7.setText(board[7]);
        TextView t8 =  findViewById(R.id.boardBox8);
        t8.setText(board[8]);
        replace(board,"-","b");
    }
    //Returns the winner or an empty string
    String checkWhoWon(String[] board){
        if(round >= 9){
            return "Draw!";
        }

        if(checkWhoWonHelper(board,"X")){
            return "X won!";
        }
        if(checkWhoWonHelper(board,"O")){
            return "O won!";
        }

        return "";
    }
    boolean checkWhoWonHelper(String[] board, String comp) {
        //rows
        if (board[0].equals(comp) && board[1].equals(comp) && board[2].equals(comp)) {
            return true;
        }
        if (board[3].equals(comp) && board[4].equals(comp) && board[5].equals(comp)) {
            return true;
        }
        if (board[6].equals(comp) && board[7].equals(comp) && board[8].equals(comp)) {
            return true;
        }
        //columns
        if (board[0].equals(comp) && board[3].equals(comp) && board[6].equals(comp)) {
            return true;
        }
        if (board[1].equals(comp) && board[4].equals(comp) && board[7].equals(comp)) {
            return true;
        }
        if (board[2].equals(comp) && board[5].equals(comp) && board[8].equals(comp)) {
            return true;
        }
        //diagonal
        if (board[0].equals(comp) && board[4].equals(comp) && board[8].equals(comp)) {
            return true;
        }
        if (board[2].equals(comp) && board[4].equals(comp) && board[6].equals(comp)) {
            return true;
        }

        return false;
    }
    //Clears the board
    public void resetGame(){
        Arrays.fill(board,"b");
        textViewWinner.setText("");
        threadRunning = true;
        round = 0;
        setUpdateBoard();
    }

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        round = 0;
        board = new String[]{
                "b", "b", "b",
                "b", "b", "b",
                "b", "b", "b"
        };

        xThread = new Thread(new X_Worker(xHandler,board));
        oThread = new Thread(new O_Worker(xHandler,board));

        textViewWinner = findViewById(R.id.textViewWinner);
        Button mButton1 =  findViewById(R.id.button1);

        mButton1.setOnClickListener(v -> {
            /*
            If the game is currently running, then just reset all of
            the game's data (GUI, round counter)
            Otherwise, start a new game
             */
            if(!threadRunning){
                playGame();
            }else{
                Arrays.fill(board,"b");
                textViewWinner.setText("");
                round = 0;
            }

        });

    }

    public void playGame() {
        Log.i("Play Game", "********Starting new game********");
        resetGame();
        xThread.run();
    }
}