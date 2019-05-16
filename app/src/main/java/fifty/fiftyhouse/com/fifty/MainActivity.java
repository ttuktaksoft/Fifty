package fifty.fiftyhouse.com.fifty;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

import fifty.fiftyhouse.com.fifty.fragment.ChatFragment;
import fifty.fiftyhouse.com.fifty.fragment.ClubFragment;
import fifty.fiftyhouse.com.fifty.fragment.MainFragment;
import fifty.fiftyhouse.com.fifty.fragment.MyProfileFragment;

public class MainActivity extends AppCompatActivity {

    public static Context mContext;
    public static Activity mActivity;
    public static FragmentManager mFragmentMng;
    private MainFragment mMainFragment;
    private ChatFragment mChatFragment;
    private ClubFragment mClubFragment;
    private MyProfileFragment mMyProfileFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mActivity = this;
        mContext = getApplicationContext();
        mFragmentMng = getSupportFragmentManager();

        mMainFragment = new MainFragment();
        mChatFragment = new ChatFragment();
        mClubFragment = new ClubFragment();
        mMyProfileFragment = new MyProfileFragment();

        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bnv_Main_BottomMenu);
        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                        switch (menuItem.getItemId()) {

                            case R.id.i_main_bottom_main:
                                getSupportFragmentManager().beginTransaction().replace(R.id.fl_Main_FrameLayout, mMainFragment, "MainFragment").commit();
                                return true;
                            case R.id.i_main_bottom_club:
                                //getSupportFragmentManager().beginTransaction().replace(R.id.fl_Main_FrameLayout, mClubFragment, "ClubFragment").commit();
                              //  return true;
                            case R.id.i_main_bottom_chat:
                                getSupportFragmentManager().beginTransaction().replace(R.id.fl_Main_FrameLayout, mChatFragment, "ChatFragment").commit();
                                return true;
                            case R.id.i_main_bottom_profile:
                                getSupportFragmentManager().beginTransaction().replace(R.id.fl_Main_FrameLayout, mMyProfileFragment, "MyProfileFragment").commit();
                                return true;
                        }
                        return false;
                    }

                });

        getSupportFragmentManager().beginTransaction().replace(R.id.fl_Main_FrameLayout, mMainFragment, "MainFragment").commit();
    }
}
