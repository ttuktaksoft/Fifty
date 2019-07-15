package fifty.fiftyhouse.com.fifty.activty;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import fifty.fiftyhouse.com.fifty.CommonFunc;
import fifty.fiftyhouse.com.fifty.R;
import fifty.fiftyhouse.com.fifty.fragment.SignUpFragment;
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
                finish();
            }
        });

        tv_SortSetting_Save.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View view) {
                finish();
            }
        });

        mSortSettingFragment = new SortSettingFragment();

        mFragmentMgr.beginTransaction().replace(R.id.fl_SortSetting_FrameLayout, mSortSettingFragment, "SortSettingFragment").commit();
    }
}
