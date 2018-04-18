package shopping_demo.com.javalockdemo;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;

/**
 * Created by ws on 18-2-24.
 */

public class ThreadStudyDemo extends Activity {
    private static final String TAG = "ThreadStudy_Demo";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setContentView(android.R.layout.list_content);
        super.onCreate(savedInstanceState);
//        new Thread1().start();
//        new Thread2().start();
//        testBarrier();
        Thread yieldThread1 = new YieldThread("yield1");
        Thread yieldThread2 = new YieldThread("yield2");
        yieldThread1.start();
        yieldThread2.start();
    }

    public void testCountDownLatch(String[] args) throws InterruptedException {
        CountDownLatch countDownLatch = new CountDownLatch(5);
        for (int i = 0; i < 5; i++) {
            new Thread(new CountDownReadNum(i, countDownLatch)).start();
        }
        countDownLatch.await();
        System.out.println("线程执行结束。。。。");
    }

    static class CountDownReadNum implements Runnable {
        private int id;
        private CountDownLatch latch;

        public CountDownReadNum(int id, CountDownLatch latch) {
            this.id = id;
            this.latch = latch;
        }

        @Override
        public void run() {
            synchronized (this) {
                System.out.println("id:" + id);
                latch.countDown();
                System.out.println("线程组任务" + id + "结束，其他任务继续");
            }
        }
    }

    private void testBarrier() {
        CyclicBarrier cyclicBarrier = new CyclicBarrier(5, new Runnable() {
            @Override
            public void run() {
                Log.d(TAG, "线程组执行结束");
            }
        });
        for (int i = 0; i < 10; i++) {
            new Thread(new CyclicBarrierReadNum(i, cyclicBarrier)).start();
        }
        // CyclicBarrier 可以重复利用，
        // 这个是CountDownLatch做不到的
//        for (int i = 11; i < 16; i++) {
//            new Thread(new CyclicBarrierReadNum(i, cyclicBarrier)).start();
//        }
    }

    static class CyclicBarrierReadNum implements Runnable {
        private int id;
        private CyclicBarrier cyc;

        public CyclicBarrierReadNum(int id, CyclicBarrier cyc) {
            this.id = id;
            this.cyc = cyc;
        }

        @Override
        public void run() {
            synchronized (this) {
                Log.d(TAG, "id:" + id);
                try {
                    cyc.await();
                    Log.d(TAG, "线程组任务" + id + "结束，其他任务继续");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    class Thread1 extends Thread {
        @Override
        public void run() {
            while (true) {
                synchronized (ThreadStudyDemo.this) {
                    Log.d(TAG, "Thread1 run");
                    try {
                        ThreadStudyDemo.this.notify();
                        ThreadStudyDemo.this.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    class Thread2 extends Thread {
        @Override
        public void run() {
            while (true) {
                while (true) {
                    synchronized (ThreadStudyDemo.this) {
                        Log.d(TAG, "Thread2 run");
                        try {
                            ThreadStudyDemo.this.notify();
                            ThreadStudyDemo.this.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
    }

    class YieldThread extends Thread {
        YieldThread(String s) {
            super(s);
        }

        public void run() {
            for (int i = 0; i <= 30; i++) {
                Log.d(TAG, getName() + ":" + i);
                if (("t1").equals(getName())) {
                    if (i == 0) {
                        yield();
                    }
                }
            }
        }

    }

}
