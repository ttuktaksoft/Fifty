package fifty.fiftyhouse.com.fifty.activty;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import androidx.fragment.app.FragmentManager;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Collections;
import java.util.Random;

import fifty.fiftyhouse.com.fifty.CommonFunc;
import fifty.fiftyhouse.com.fifty.DialogFunc;
import fifty.fiftyhouse.com.fifty.MainActivity;
import fifty.fiftyhouse.com.fifty.Manager.FirebaseManager;
import fifty.fiftyhouse.com.fifty.Manager.TKManager;
import fifty.fiftyhouse.com.fifty.R;
import fifty.fiftyhouse.com.fifty.fragment.SortSettingFragment;
import fifty.fiftyhouse.com.fifty.util.OnSingleClickListener;

public class SortSettingActivity extends AppCompatActivity {

    View ui_SortSetting_TopBar;
    TextView tv_TopBar_Title;
    ImageView iv_TopBar_Back;
    TextView tv_SortSetting_Save;

    Context mContext;
    Activity mActivity;
    FragmentManager mFragmentMgr;
    SortSettingFragment mSortSettingFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sort_setting);

        mActivity = this;
        mContext = getApplicationContext();
        mFragmentMgr = getSupportFragmentManager();

        ui_SortSetting_TopBar = findViewById(R.id.ui_SortSetting_TopBar);
        tv_TopBar_Title = ui_SortSetting_TopBar.findViewById(R.id.tv_TopBar_Title);
        iv_TopBar_Back = ui_SortSetting_TopBar.findViewById(R.id.iv_TopBar_Back);
        tv_SortSetting_Save = findViewById(R.id.tv_SortSetting_Save);

        tv_TopBar_Title.setText(CommonFunc.getInstance().getStr(mContext.getResources(), R.string.TITLE_SORT_SETTING));
        iv_TopBar_Back.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View view) {

                DialogFunc.getInstance().ShowLoadingPage(SortSettingActivity.this);

                FirebaseManager.CheckFirebaseComplete listener = new FirebaseManager.CheckFirebaseComplete() {
                    @Override
                    public void CompleteListener() {


                        CommonFunc.getInstance().SortByDistance(TKManager.getInstance().UserList_Dist, TKManager.getInstance().View_UserList_Dist, true);
                        CommonFunc.getInstance().SortByDistance(TKManager.getInstance().UserList_New, TKManager.getInstance().View_UserList_New, true);
                        //CommonFunc.getInstance().SortByDistance(TKManager.getInstance().UserList_Hot, TKManager.getInstance().View_UserList_Hot, true);

                        TKManager.getInstance().View_UserList_Hot = TKManager.getInstance().UserList_Hot;

                        long seed = System.nanoTime();
                        Collections.shuffle(TKManager.getInstance().View_UserList_Hot, new Random(seed));

                        Intent intent = new Intent(MainActivity.mActivity, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }

                    @Override
                    public void CompleteListener_Yes() {

                    }

                    @Override
                    public void CompleteListener_No() {

                    }
                };
                FirebaseManager.getInstance().RegistSortData(listener);
            }
        });

        tv_SortSetting_Save.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View view) {
                DialogFunc.getInstance().ShowLoadingPage(SortSettingActivity.this);

                FirebaseManager.CheckFirebaseComplete listener = new FirebaseManager.CheckFirebaseComplete() {
                    @Override
                    public void CompleteListener() {

                        CommonFunc.getInstance().SortByDistance(TKManager.getInstance().UserList_Dist, TKManager.getInstance().View_UserList_Dist, true);
                        CommonFunc.getInstance().SortByDistance(TKManager.getInstance().UserList_New, TKManager.getInstance().View_UserList_New, true);
                        TKManager.getInstance().View_UserList_Hot = TKManager.getInstance().UserList_Hot;

                        long seed = System.nanoTime();
                        Collections.shuffle(TKManager.getInstance().View_UserList_Hot, new Random(seed));


                        CommonFunc.getInstance().MoveMainActivity(SortSettingActivity.this, false);
                    }

                    @Override
                    public void CompleteListener_Yes() {

                    }

                    @Override
                    public void CompleteListener_No() {

                    }
                };
                FirebaseManager.getInstance().RegistSortData(listener);
            }
        });

        mSortSettingFragment = new SortSettingFragment();

        mFragmentMgr.beginTransaction().replace(R.id.fl_SortSetting_FrameLayout, mSortSettingFragment, "SortSettingFragment").commit();
    }

    @Override public void onBackPressed() {
        //super.onBackPressed();
        DialogFunc.getInstance().ShowLoadingPage(SortSettingActivity.this);

        FirebaseManager.CheckFirebaseComplete listener = new FirebaseManager.CheckFirebaseComplete() {
            @Override
            public void CompleteListener() {
                CommonFunc.getInstance().MoveMainActivity(SortSettingActivity.this, false);
            }

            @Override
            public void CompleteListener_Yes() {

            }

            @Override
            public void CompleteListener_No() {

            }
        };
        FirebaseManager.getInstance().RegistSortData(listener);
    }

}
