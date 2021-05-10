package edu.mogunr2.project4;

import java.sql.Struct;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;

public class X_Worker implements Runnable {
    private Handler handler;
    private String[] board;
    public X_Worker(Handler h, String[] b){
        handler = h;
        board = b;
    }
    @Override
    public void run() {
        ExecutorService executorService = Executors.newFixedThreadPool(1);
        Future<Integer> future = executorService.submit(new MakeMove(board, "X","Normal"));
        try {
            board[future.get()] = "X";
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        executorService.shutdown();
        Message msg = handler.obtainMessage(MainActivity.MADE_MOVE_X);
        Bundle b = new Bundle();
        b.putStringArray("board",board);
        msg.setData(b);
        handler.sendMessageDelayed(msg,1000);
    }
}
