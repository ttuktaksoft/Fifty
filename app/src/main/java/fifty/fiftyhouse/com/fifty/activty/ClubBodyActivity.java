package fifty.fiftyhouse.com.fifty.activty;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import fifty.fiftyhouse.com.fifty.CommonFunc;
import fifty.fiftyhouse.com.fifty.DataBase.ClubContextData;
import fifty.fiftyhouse.com.fifty.DialogFunc;
import fifty.fiftyhouse.com.fifty.Manager.FirebaseManager;
import fifty.fiftyhouse.com.fifty.Manager.TKManager;
import fifty.fiftyhouse.com.fifty.R;
import fifty.fiftyhouse.com.fifty.adapter.ClubBodyImgAdapter;
import fifty.fiftyhouse.com.fifty.adapter.ClubBodyReplyAdapter;
import fifty.fiftyhouse.com.fifty.adapter.ClubContentAdapter;
import fifty.fiftyhouse.com.fifty.fragment.ClubBodyFragment;
import fifty.fiftyhouse.com.fifty.fragment.SignUpFragment;
import fifty.fiftyhouse.com.fifty.util.OnRecyclerItemClickListener;
import fifty.fiftyhouse.com.fifty.util.OnSingleClickListener;
import fifty.fiftyhouse.com.fifty.util.RecyclerItemClickListener;

public class ClubBodyActivity extends AppCompatActivity {

    View ui_ClubBody_TopBar;
    TextView tv_TopBar_Title;
    ImageView iv_TopBar_Back;

    EditText et_ClubBody_Reply;
    TextView tv_ClubBody_Send;

    Context mContext;
    Activity mActivity;
    FragmentManager mFragmentMgr;
    ClubBodyFragment mClubBodyFragment;
    ClubContextData tempData;

    InputMethodManager imm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_club_body);
        imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        mContext = getApplicationContext();
        mActivity = this;
        mFragmentMgr = getSupportFragmentManager();

        Intent intent = getIntent(); //getIntent()로 받을준비
        int nPosition = getIntent().getIntExtra("position", 0);

        tempData = new ClubContextData();
        tempData = TKManager.getInstance().TargetClubData.GetClubContext(Integer.toString(nPosition));

        ui_ClubBody_TopBar = findViewById(R.id.ui_SignUp_TopBar);
        tv_TopBar_Title = ui_ClubBody_TopBar.findViewById(R.id.tv_TopBar_Title);
        iv_TopBar_Back = ui_ClubBody_TopBar.findViewById(R.id.iv_TopBar_Back);
        et_ClubBody_Reply = findViewById(R.id.et_ClubBody_Reply);
        tv_ClubBody_Send = findViewById(R.id.tv_ClubBody_Send);


        iv_TopBar_Back.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View view) {
                finish();
            }
        });
        tv_TopBar_Title.setText(CommonFunc.getInstance().getStr(mContext.getResources(), R.string.TITLE_CLUB_BODY));

        mClubBodyFragment = new ClubBodyFragment();
        mClubBodyFragment.tempData = tempData;
        mFragmentMgr.beginTransaction().replace(R.id.fl_ClubBody_FrameLayout, mClubBodyFragment, "ClubBodyFragment").commit();

        tv_ClubBody_Send.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View view) {
                imm.hideSoftInputFromWindow(et_ClubBody_Reply.getWindowToken(), 0);

                // 데이터 추가 하고 아래 함수 콜
                mClubBodyFragment.RefreshReply();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }

        return super.onOptionsItemSelected(item);
    }

}
