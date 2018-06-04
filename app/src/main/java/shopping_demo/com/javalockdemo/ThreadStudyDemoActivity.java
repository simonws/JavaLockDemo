package shopping_demo.com.javalockdemo;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Semaphore;

/**
 * Created by ws on 18-2-24.
 */

public class ThreadStudyDemoActivity extends Activity implements View.OnClickListener {
    private static final String TAG = "ThreadStudy_Demo";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.common_lock_layout);
        super.onCreate(savedInstanceState);
    }

    public void testCountDownLatch() throws InterruptedException {
        CountDownLatch countDownLatch = new CountDownLatch(5);
        for (int i = 0; i < 5; i++) {
            new Thread(new CountDownReadNum(i, countDownLatch)).start();
        }
        countDownLatch.await();
        System.out.println("线程执行结束。。。。");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_barrier:
                testBarrier();
                break;
            case R.id.btn_countdown_latcher:
                try {
                    testCountDownLatch();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.btn_yield:
                Thread yieldThread1 = new YieldThread("yield1");
                Thread yieldThread2 = new YieldThread("yield2");
                yieldThread1.start();
                yieldThread2.start();
                break;

            case R.id.Semaphore:
                int N = 8;            //工人数
                Semaphore semaphore = new Semaphore(5); //机器数目
                for (int i = 0; i < N; i++)
                    new Worker(i, semaphore).start();
                break;
            default:
                break;


        }
    }

    static class Worker extends Thread {
        private int num;
        private Semaphore semaphore;

        public Worker(int num, Semaphore semaphore) {
            this.num = num;
            this.semaphore = semaphore;
        }

        @Override
        public void run() {
            try {
                semaphore.acquire();
                Log.d(TAG, "工人" + this.num + "占用一个机器在生产...");
                Thread.sleep(2000);
                Log.d(TAG, "工人" + this.num + "释放出机器");
                semaphore.release();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
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
                synchronized (ThreadStudyDemoActivity.this) {
                    Log.d(TAG, "Thread1 run");
                    try {
                        ThreadStudyDemoActivity.this.notify();
                        ThreadStudyDemoActivity.this.wait();
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
                    synchronized (ThreadStudyDemoActivity.this) {
                        Log.d(TAG, "Thread2 run");
                        try {
                            ThreadStudyDemoActivity.this.notify();
                            ThreadStudyDemoActivity.this.wait();
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
