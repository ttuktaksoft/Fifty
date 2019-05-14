package fifty.fiftyhouse.com.fifty.activty;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import fifty.fiftyhouse.com.fifty.MainActivity;
import fifty.fiftyhouse.com.fifty.R;

public class LoadingActivity extends AppCompatActivity {

    private Activity mActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);

        mActivity = this;

        // 임의로 2초 슬립
        try {
            Thread.sleep(2000);
        }
        catch (InterruptedException e)
        {
            System.out.print((e.getMessage()));
        }

        startActivity(new Intent(getApplicationContext(),LoginActivity.class));
        finish();
    }
}