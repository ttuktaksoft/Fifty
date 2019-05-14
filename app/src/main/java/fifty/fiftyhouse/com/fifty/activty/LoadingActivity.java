package fifty.fiftyhouse.com.fifty.activty;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import fifty.fiftyhouse.com.fifty.R;

public class LoadingActivity extends AppCompatActivity {

    private Activity mActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);

        mActivity = this;
    }
}