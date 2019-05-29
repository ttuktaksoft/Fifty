package fifty.fiftyhouse.com.fifty;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fifty.fiftyhouse.com.fifty.DataBase.UserData;
import fifty.fiftyhouse.com.fifty.Manager.FirebaseManager;
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


        FirebaseManager.getInstance().SignInAnonymously(mActivity);

        UserData myData = new UserData();
        String[] fav = new String[2];
        fav[0] = "asd";
        fav[1] = "asd2";

        myData.SetUserData("1","1234123","ffff",fav,"22","22",22,1);

        //FirebaseManager.getInstance().SetUserDataOnFireBase(CommonData.CollentionType.USERS, myData.GetUserIndex(), myData);
        FirebaseManager.getInstance().SetUserDataOnFireBase(CommonData.CollentionType.USERS, myData.GetUserIndex(), "Token", myData.GetUserToken());



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
                                mFragmentMng.beginTransaction().replace(R.id.fl_Main_FrameLayout, mMainFragment, "MainFragment").commit();
                                return true;
                            case R.id.i_main_bottom_club:
                                mFragmentMng.beginTransaction().replace(R.id.fl_Main_FrameLayout, mClubFragment, "ClubFragment").commit();
                                return true;
                            case R.id.i_main_bottom_chat:
                                mFragmentMng.beginTransaction().replace(R.id.fl_Main_FrameLayout, mChatFragment, "ChatFragment").commit();
                                return true;
                            case R.id.i_main_bottom_profile:
                                mFragmentMng.beginTransaction().replace(R.id.fl_Main_FrameLayout, mMyProfileFragment, "MyProfileFragment").commit();
                                return true;
                        }
                        return false;
                    }

                });

        mFragmentMng.beginTransaction().replace(R.id.fl_Main_FrameLayout, mMainFragment, "MainFragment").commit();
    }

    public interface onKeyBackPressedListener{
        void onBackKey();
    }
    private onKeyBackPressedListener mOnKeyBackPressedListener;
    public void setOnKeyBackPressedListener(onKeyBackPressedListener listener)
    {
        mOnKeyBackPressedListener = listener;
    }

    @Override
    public void onBackPressed()
    {
        if(mOnKeyBackPressedListener != null)
            mOnKeyBackPressedListener.onBackKey();

        else
        {
            if(getSupportFragmentManager().getBackStackEntryCount() == 0)
            {
              /*  Toast toast = new Toast(getApplicationContext());
                toast.makeText(getApplicationContext(),"뒤로가기", Toast.LENGTH_SHORT);
                toast.show();*/
            }
            else
            {
                super.onBackPressed();
            }

        }
    }

}
