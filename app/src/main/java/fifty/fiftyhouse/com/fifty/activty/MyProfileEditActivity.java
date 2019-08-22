package fifty.fiftyhouse.com.fifty.activty;

import android.app.Activity;
import android.content.Context;

import androidx.fragment.app.FragmentManager;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import fifty.fiftyhouse.com.fifty.CommonFunc;
import fifty.fiftyhouse.com.fifty.R;
import fifty.fiftyhouse.com.fifty.fragment.MyProfileEditFragment;
import fifty.fiftyhouse.com.fifty.util.OnSingleClickListener;

public class MyProfileEditActivity extends AppCompatActivity {

    View ui_MyProfile_Edit_TopBar;
    TextView tv_TopBar_Title;
    ImageView iv_TopBar_Back;

    Context mContext;
    Activity mActivity;
    FragmentManager mFragmentMgr;

    MyProfileEditFragment mMyProfileEditFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile_edit);

        mActivity = this;
        mContext = getApplicationContext();
        mFragmentMgr = getSupportFragmentManager();
        // mFragmentMgr.beginTransaction().addToBackStack(null);

        mMyProfileEditFragment = new MyProfileEditFragment();
        CommonFunc.getInstance().mCurActivity = this;
        mFragmentMgr.beginTransaction().replace(R.id.fl_MyProfile_Edit_FrameLayout, mMyProfileEditFragment, "MyProfileEditFragmentFragment").commit();


        ui_MyProfile_Edit_TopBar = findViewById(R.id.ui_MyProfile_Edit_TopBar);
        tv_TopBar_Title = ui_MyProfile_Edit_TopBar.findViewById(R.id.tv_TopBar_Title);
        iv_TopBar_Back = ui_MyProfile_Edit_TopBar.findViewById(R.id.iv_TopBar_Back);
        tv_TopBar_Title.setText(CommonFunc.getInstance().getStr(mContext.getResources(), R.string.TITLE_PROFILE_EDIT));
        iv_TopBar_Back.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                finish();
            }
        });
    }
}
