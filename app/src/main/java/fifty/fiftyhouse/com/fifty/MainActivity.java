package fifty.fiftyhouse.com.fifty;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import fifty.fiftyhouse.com.fifty.DataBase.ClubData;
import fifty.fiftyhouse.com.fifty.DataBase.UserData;
import fifty.fiftyhouse.com.fifty.Manager.FirebaseManager;
import fifty.fiftyhouse.com.fifty.Manager.TKManager;
import fifty.fiftyhouse.com.fifty.activty.SignUpActivity;
import fifty.fiftyhouse.com.fifty.activty.UserListActivity;
import fifty.fiftyhouse.com.fifty.fragment.ChatFragment;
import fifty.fiftyhouse.com.fifty.fragment.ClubFragment;
import fifty.fiftyhouse.com.fifty.fragment.MainFragment;
import fifty.fiftyhouse.com.fifty.fragment.MyProfileFragment;
import fifty.fiftyhouse.com.fifty.util.BackPressCloseHandler;

public class MainActivity extends AppCompatActivity {

    public static Context mContext;
    public static Activity mActivity;
    public static FragmentManager mFragmentMng;
    private MainFragment mMainFragment;
    private ChatFragment mChatFragment;
    private ClubFragment mClubFragment;
    private MyProfileFragment mMyProfileFragment;
    private BackPressCloseHandler backPressCloseHandler;

    BottomNavigationView bnv_Main_BottomMenu;

    public interface MoveFragmentListener {
        void MoveFragment();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mActivity = this;
        mContext = getApplicationContext();
        mFragmentMng = getSupportFragmentManager();
        bnv_Main_BottomMenu = (BottomNavigationView) findViewById(R.id.bnv_Main_BottomMenu);

      //  String[] temp = FirebaseManager.getInstance().AddFavorite(mContext);

        DialogFunc.getInstance().DismissLoadingPage();

        backPressCloseHandler = new BackPressCloseHandler(mActivity);

        CommonFunc.getInstance().mCurActivity = this;

        mMainFragment = new MainFragment();
        mChatFragment = new ChatFragment();
        mClubFragment = new ClubFragment();
        mMyProfileFragment = new MyProfileFragment();


        bnv_Main_BottomMenu.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                        switch (menuItem.getItemId()) {

                            case R.id.i_main_bottom_main:
                                mFragmentMng.beginTransaction().replace(R.id.fl_Main_FrameLayout, mMainFragment, "MainFragment").commit();
                                return true;
                            case R.id.i_main_bottom_club:

                                DialogFunc.getInstance().ShowLoadingPage(MainActivity.this);

                                Map<String, ClubData> tempClub = new LinkedHashMap<>();
                                tempClub.putAll(TKManager.getInstance().MyData.GetUserClubData());
                                tempClub.putAll(TKManager.getInstance().MyData.GetUserRecommendClubData());
                                tempClub.putAll(TKManager.getInstance().MyData.GetRequestJoinClubList());

                                Set KeySet = tempClub.keySet();

                                if(KeySet.size() > 0)
                                {
                                    Iterator iterator = KeySet.iterator();

                                    FirebaseManager.getInstance().SetFireBaseLoadingCount(tempClub.size());

                                    FirebaseManager.CheckFirebaseComplete listener = new FirebaseManager.CheckFirebaseComplete() {
                                        @Override
                                        public void CompleteListener() {
                                            DialogFunc.getInstance().DismissLoadingPage();
                                            mFragmentMng.beginTransaction().replace(R.id.fl_Main_FrameLayout, mClubFragment, "ClubFragment").commit();
                                        }

                                        @Override
                                        public void CompleteListener_Yes() {
                                        }

                                        @Override
                                        public void CompleteListener_No() {
                                        }
                                    };

                                    while(iterator.hasNext()){
                                        String key = (String)iterator.next();
                                        if(TKManager.getInstance().ClubData_Simple.get(key) != null)
                                        {
                                            FirebaseManager.getInstance().Complete(listener);
                                        }
                                        else
                                            FirebaseManager.getInstance().GetClubData_Simple(key, TKManager.getInstance().ClubData_Simple, listener);
                                    }
                                }
                                else
                                {
                                    DialogFunc.getInstance().DismissLoadingPage();
                                    mFragmentMng.beginTransaction().replace(R.id.fl_Main_FrameLayout, mClubFragment, "ClubFragment").commit();
                                }


                                //
                                //DialogFunc.getInstance().ShowToast(mContext, "클럽서비스 준비 중입니다", true);
                                return true;
                            case R.id.i_main_bottom_chat:
                                mFragmentMng.beginTransaction().replace(R.id.fl_Main_FrameLayout, mChatFragment, "ChatFragment").commit();
                                return true;
                            case R.id.i_main_bottom_profile:

                                DialogFunc.getInstance().ShowLoadingPage(MainActivity.this);
                                KeySet = TKManager.getInstance().MyData.GetUserClubDataKeySet();

                                if(KeySet.size() > 0)
                                {
                                    Iterator iterator = KeySet.iterator();

                                    FirebaseManager.getInstance().SetFireBaseLoadingCount(TKManager.getInstance().MyData.GetUserClubDataCount());

                                    FirebaseManager.CheckFirebaseComplete listener = new FirebaseManager.CheckFirebaseComplete() {
                                        @Override
                                        public void CompleteListener() {
                                            DialogFunc.getInstance().DismissLoadingPage();
                                            mFragmentMng.beginTransaction().replace(R.id.fl_Main_FrameLayout, mMyProfileFragment, "MyProfileFragment").commit();
                                        }

                                        @Override
                                        public void CompleteListener_Yes() {
                                        }

                                        @Override
                                        public void CompleteListener_No() {
                                        }
                                    };

                                    while(iterator.hasNext()){
                                        String key = (String)iterator.next();
                                        if(TKManager.getInstance().ClubData_Simple.get(key) != null)
                                        {
                                            FirebaseManager.getInstance().Complete(listener);
                                        }
                                        else
                                            FirebaseManager.getInstance().GetClubData_Simple(key, TKManager.getInstance().ClubData_Simple, listener);
                                    }
                                }
                                else
                                {
                                    DialogFunc.getInstance().DismissLoadingPage();
                                    mFragmentMng.beginTransaction().replace(R.id.fl_Main_FrameLayout, mMyProfileFragment, "MyProfileFragment").commit();
                                }

                                return true;
                        }
                        return false;
                    }

                });

        mFragmentMng.beginTransaction().replace(R.id.fl_Main_FrameLayout, mMainFragment, "MainFragment").commit();
    }

    @Override public void onBackPressed() {
        //super.onBackPressed();
         backPressCloseHandler.onBackPressed();
         }

}
