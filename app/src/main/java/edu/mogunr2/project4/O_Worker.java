package edu.mogunr2.project4;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class O_Worker implements Runnable {
    private Handler handler;
    private String[] board;
    public O_Worker(Handler h, String[] b){
        handler = h;
        board = b;
    }
    @Override
    public void run() {
        ExecutorService executorService = Executors.newFixedThreadPool(1);
        Future<Integer> future = executorService.submit(new MakeMove(board, "O","Easy"));
        try {
            board[future.get()] = "O";
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        executorService.shutdown();
        Message msg = handler.obtainMessage(MainActivity.MADE_MOVE_O);
        Bundle b = new Bundle();
        b.putStringArray("board",board);
        msg.setData(b);
        handler.sendMessageDelayed(msg,1000);
    }
}
