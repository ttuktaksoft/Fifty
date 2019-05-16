package fifty.fiftyhouse.com.fifty;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import fifty.fiftyhouse.com.fifty.fragment.MainFragment;

public class MainActivity extends AppCompatActivity {

    public static Context mContext;
    public static Activity mActivity;
    public static FragmentManager mFragmentMng;
    private MainFragment mMainFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mActivity = this;
        mContext = getApplicationContext();
        mFragmentMng = getSupportFragmentManager();

        mMainFragment = new MainFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.fl_Main_FrameLayout, mMainFragment, "MainFragment").commit();
    }
}
