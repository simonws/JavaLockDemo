package shopping_demo.com.javalockdemo;

/**
 * Created by ws on 18-2-24.
 */

import java.util.concurrent.CyclicBarrier;

/**
 * PROJECT_NAME:downLoad Author:lucaifang Date:2016/3/18
 */
public class CyclicBarrierStudy {
    public static void main(String[] args) throws InterruptedException {
        CyclicBarrier cyclicBarrier = new CyclicBarrier(5, new Runnable() {
            @Override
            public void run() {
                System.out.println("线程组执行结束");
            }
        });
        for (int i = 0; i < 5; i++) {
            new Thread(new readNum(i, cyclicBarrier)).start();
        }
        // CyclicBarrier 可以重复利用，
        // 这个是CountDownLatch做不到的
//        for (int i = 11; i < 16; i++) {
//            new Thread(new CyclicBarrierReadNum(i,cyclicBarrier)).start();
//        }
    }

    private void testBarrier() {
        CyclicBarrier cyclicBarrier = new CyclicBarrier(5, new Runnable() {
            @Override
            public void run() {
                System.out.println("线程组执行结束");
            }
        });
        for (int i = 0; i < 5; i++) {
            new Thread(new readNum(i, cyclicBarrier)).start();
        }
        // CyclicBarrier 可以重复利用，
        // 这个是CountDownLatch做不到的
        for (int i = 11; i < 16; i++) {
            new Thread(new readNum(i, cyclicBarrier)).start();
        }
    }

    static class readNum implements Runnable {
        private int id;
        private CyclicBarrier cyc;

        public readNum(int id, CyclicBarrier cyc) {
            this.id = id;
            this.cyc = cyc;
        }

        @Override
        public void run() {
            synchronized (this) {
                System.out.println("id:" + id);
                try {
                    cyc.await();
                    System.out.println("线程组任务" + id + "结束，其他任务继续");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
