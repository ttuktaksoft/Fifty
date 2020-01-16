package fifty.fiftyhouse.com.fifty;

import android.app.Activity;
import android.content.Context;

import androidx.annotation.NonNull;

import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import fifty.fiftyhouse.com.fifty.DataBase.ClubData;
import fifty.fiftyhouse.com.fifty.Manager.FirebaseManager;
import fifty.fiftyhouse.com.fifty.Manager.TKManager;
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

    private int MoveMainTabIndex = -1;
    private int MoveSubTabIndex = -1;

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
        TKManager.getInstance().mMainActivity = this;
        DialogFunc.getInstance().ShowMainImgNoticePopup(mActivity);

        bnv_Main_BottomMenu = (BottomNavigationView) findViewById(R.id.bnv_Main_BottomMenu);

        //  String[] temp = FirebaseManager.getInstance().AddFavorite(mContext);

        DialogFunc.getInstance().DismissLoadingPage();


        Intent intent = getIntent(); //getIntent()로 받을준비
        MoveSubTabIndex = getIntent().getIntExtra("subtabpostion", -1);
        MoveMainTabIndex = getIntent().getIntExtra("maintabpostion", -1);

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
                                if(MoveSubTabIndex >= 0)
                                {
                                    mFragmentMng.beginTransaction().replace(R.id.fl_Main_FrameLayout, mMainFragment, "MainFragment")
                                            .runOnCommit(new Runnable() // be carefull on this call. check documentation!!
                                            {
                                                @Override
                                                public void run()
                                                {
                                                    mMainFragment.MoveStartTab(MoveSubTabIndex);
                                                    MoveSubTabIndex = -1;
                                                }
                                            }).commit();
                                }
                                else
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

                                    FirebaseManager.getInstance().SetFireBaseLoadingCount("클럽 페이지" , tempClub.size());

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
                                            FirebaseManager.getInstance().Complete("클럽 페이지 ㅇㅇㅇ" ,listener);
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

                                    FirebaseManager.getInstance().SetFireBaseLoadingCount("클럽 페이지" ,TKManager.getInstance().MyData.GetUserClubDataCount());

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
                                            FirebaseManager.getInstance().Complete("클럽 페이지 ㅇㅇㅇ" ,listener);
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

        if(MoveMainTabIndex >= 0)
            MoveFragmentTab(MoveMainTabIndex, MoveSubTabIndex);

        mFragmentMng.beginTransaction().replace(R.id.fl_Main_FrameLayout, mMainFragment, "MainFragment").commit();

        SetChatBadgeView(TKManager.getInstance().mMainChatBadgeNumber);
    }

    @Override
    public void onBackPressed()
    {
        //super.onBackPressed();
        backPressCloseHandler.onBackPressed();
    }

    public void MoveFragmentTab(int postion, int etcPostion)
    {
        MoveSubTabIndex = -1;
        if(postion == 0)
        {
            MoveSubTabIndex = etcPostion;
            bnv_Main_BottomMenu.setSelectedItemId(R.id.i_main_bottom_main);
        }
        else if(postion == 1)
            bnv_Main_BottomMenu.setSelectedItemId(R.id.i_main_bottom_club);
        else if(postion == 2)
            bnv_Main_BottomMenu.setSelectedItemId(R.id.i_main_bottom_chat);
        else if(postion == 3)
            bnv_Main_BottomMenu.setSelectedItemId(R.id.i_main_bottom_profile);
    }

    public void SetChatBadgeView(int num)
    {
        TKManager.getInstance().mMainChatBadgeNumber = num;
        if(num > 0)
        {
            BadgeDrawable badge = bnv_Main_BottomMenu.getOrCreateBadge(R.id.i_main_bottom_chat);
            badge.setNumber(num);
            badge.setBackgroundColor(ContextCompat.getColor(mContext, R.color.red));
            badge.setBadgeTextColor(ContextCompat.getColor(mContext, R.color.str_color_2));
        }
        else
            bnv_Main_BottomMenu.removeBadge(R.id.i_main_bottom_chat);
    }
}
