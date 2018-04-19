package shopping_demo.com.javalockdemo;

import android.app.Activity;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by ws on 18-3-8.
 */

public class ReentrantLockStudyActivity extends Activity {
    private String TAG = "LockStudy_Activity";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        setContentView(R.layout.lock_activity_layout);
        super.onCreate(savedInstanceState, persistentState);
    }

    @Override
    protected void onResume() {
        super.onResume();

        final BoundedBuffer boundedBuffer = new BoundedBuffer();

        Thread t1 = new Thread(new Runnable() {
            @Override
            public void run() {
                Log.d(TAG, "t1 run");
                for (int i = 0; i < 10; i++) {
                    try {
                        Log.d(TAG, "putting..");
                        boundedBuffer.put(Integer.valueOf(i));
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }

        });

        Thread t2 = new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 10; i++) {
                    try {
                        Object val = boundedBuffer.take();
                        Log.d(TAG, val.toString());
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }

        });

        t1.start();
        t2.start();
    }
}
