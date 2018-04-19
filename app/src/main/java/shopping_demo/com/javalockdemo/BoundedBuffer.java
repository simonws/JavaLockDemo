package shopping_demo.com.javalockdemo;

/**
 * Created by ws on 18-3-8.
 */

import android.util.Log;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * BoundedBuffer 是一个定长100的集合，当集合中没有元素时，take方法需要等待，直到有元素时才返回元素 当其中的元素数达到最大值时，要等待直到元素被take之后才执行put的操作
 */
class BoundedBuffer {
    private String TAG = "BoundedBuffer";
    final Lock lock = new ReentrantLock();
    final Condition fullCondition = lock.newCondition();
    final Condition emptyCondition = lock.newCondition();

    final Object[] items = new Object[5];
    int currentTop = -1;
    int count = 0;

    public void put(Object x) throws InterruptedException {
        Log.d(TAG, "put wait lock");
        lock.lock();
        Log.d(TAG, "put get lock");
        try {
            while (count == items.length) {
                Log.d(TAG, "put buffer full, please wait");
                fullCondition.await();
            }

            items[++currentTop] = x;
//            if (++putptr == items.length)
//                putptr = 0;
            ++count;
            Log.d(TAG, "put buffer value = " + x + " currentTop=" + currentTop + " count=" + count + " == " + Thread.currentThread());
            emptyCondition.signal();
        } finally {
            lock.unlock();
        }
    }

    public Object take() throws InterruptedException {

        Log.d(TAG, "take wait lock");
        lock.lock();
        Log.d(TAG, "take get lock");
        try {
            while (count == 0) {
                Log.d(TAG, "take no elements, please wait");
                emptyCondition.await();
            }

            Object x = items[currentTop--];

//            if (++takeptr == items.length)
//                takeptr = 0;
            --count;
            Log.d(TAG, "take buffer value = " + x + " currentTop=" + currentTop + " count=" + count + " == " + Thread.currentThread());
            fullCondition.signal();
            return x;
        } finally {
            lock.unlock();
        }
    }
}
