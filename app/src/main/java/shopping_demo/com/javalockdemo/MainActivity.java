package shopping_demo.com.javalockdemo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.barraier:
                Intent barraier_intent = new Intent();
                barraier_intent.setClassName(this, ThreadStudyDemo.class.getName());
                startActivity(barraier_intent);
                break;
            case R.id.re_entry_lock:

                Intent intent = new Intent();
                intent.setClassName(this, LockStudyActivity.class.getName());
                startActivity(intent);
                break;
            default:
                break;
        }

    }
}
