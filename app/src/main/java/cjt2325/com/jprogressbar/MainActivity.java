package cjt2325.com.jprogressbar;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import cjt2325.com.jprogressbarlib.JProgressBar;

public class MainActivity extends AppCompatActivity {
    JProgressBar jpbar;
    Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        jpbar = (JProgressBar) findViewById(R.id.jpbar);
        btn = (Button) findViewById(R.id.btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                jpbar.setProgressWithAnimation(700);
            }
        });
    }
}
