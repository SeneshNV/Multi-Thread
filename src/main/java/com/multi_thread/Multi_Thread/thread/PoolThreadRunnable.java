package com.multi_thread.Multi_Thread.thread;

import java.util.concurrent.BlockingQueue;

//PoolThread class which implements the threads that execute the tasks.
public class PoolThreadRunnable implements Runnable {

    private Thread        thread    = null;
    private BlockingQueue taskQueue = null;
    private boolean       isStopped = false;

    public PoolThreadRunnable(BlockingQueue queue){
        taskQueue = queue;
    }

    public void run(){
        this.thread = Thread.currentThread();
        while(!isStopped()){
            try{
                Runnable runnable = (Runnable) taskQueue.take();
                runnable.run();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    //thread pool stop
    public synchronized void doStop(){
        isStopped = true;
        //break pool thread out of dequeue() call.
        this.thread.interrupt();
    }

    public synchronized boolean isStopped(){
        return isStopped;
    }}
