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
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

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
    TextView tv_ClubBody_Del;

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
        int nType = getIntent().getIntExtra("Type", 0);
        int nPosition = getIntent().getIntExtra("position", 0);


        tempData = new ClubContextData();
        if(nType == 1)
        {
            // TODO 신고 당한 게시물 선택

        }
        else
        {
            //GetClubMemberKeySet1
            Set tempKey = TKManager.getInstance().TargetClubData.GetClubContextKeySet();
            List array = new ArrayList(tempKey);
            tempData = TKManager.getInstance().TargetClubData.GetClubContext(array.get(nPosition).toString());

            TKManager.getInstance().TargetContextData = tempData;
        }


        ui_ClubBody_TopBar = findViewById(R.id.ui_ClubBody_TopBar);
        tv_TopBar_Title = ui_ClubBody_TopBar.findViewById(R.id.tv_TopBar_Title);
        iv_TopBar_Back = ui_ClubBody_TopBar.findViewById(R.id.iv_TopBar_Back);
        et_ClubBody_Reply = findViewById(R.id.et_ClubBody_Reply);
        tv_ClubBody_Send = findViewById(R.id.tv_ClubBody_Send);
        tv_ClubBody_Del = findViewById(R.id.tv_ClubBody_Del);

        iv_TopBar_Back.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View view) {
                finish();
            }
        });
        tv_TopBar_Title.setText(CommonFunc.getInstance().getStr(mContext.getResources(), R.string.TITLE_CLUB_BODY));


        if(nType == 1)
        {
            et_ClubBody_Reply.setVisibility(View.GONE);
            tv_ClubBody_Send.setVisibility(View.GONE);
            tv_ClubBody_Del.setVisibility(View.VISIBLE);
        }
        else
        {
            et_ClubBody_Reply.setVisibility(View.VISIBLE);
            tv_ClubBody_Send.setVisibility(View.VISIBLE);
            tv_ClubBody_Del.setVisibility(View.GONE);
        }

        mClubBodyFragment = new ClubBodyFragment();
        mClubBodyFragment.tempData = tempData;

        mFragmentMgr.beginTransaction().replace(R.id.fl_ClubBody_FrameLayout, mClubBodyFragment, "ClubBodyFragment").commit();

        tv_ClubBody_Del.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View view) {
                final DialogFunc.MsgPopupListener listenerYes = new DialogFunc.MsgPopupListener() {
                    @Override
                    public void Listener() {
                        // 게시글 삭제
                    }
                };

                DialogFunc.getInstance().ShowMsgPopup(ClubBodyActivity.this, listenerYes, null, CommonFunc.getInstance().getStr(ClubBodyActivity.this.getResources(), R.string.MSG_CLUB_BODY_REPORT_DEL_DESC),
                        CommonFunc.getInstance().getStr(ClubBodyActivity.this.getResources(), R.string.MSG_CLUB_BODY_REPORT_DEL), CommonFunc.getInstance().getStr(ClubBodyActivity.this.getResources(), R.string.MSG_CANCEL));
            }
        });

        tv_ClubBody_Send.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View view) {
                imm.hideSoftInputFromWindow(et_ClubBody_Reply.getWindowToken(), 0);

                int tempCount = tempData.GetReplyDataCount();

                String tempReplyData = TKManager.getInstance().MyData.GetUserIndex() + "_" + CommonFunc.getInstance().GetCurrentTime() + "_"  + et_ClubBody_Reply.getText().toString();
                tempData.SetReply(Integer.toString(tempData.GetReplyDataCount()), tempReplyData);

                ClubContextData tempRepData =new ClubContextData();

                tempRepData.SetContext(et_ClubBody_Reply.getText().toString());
                tempRepData.SetDate(CommonFunc.getInstance().GetCurrentTime());
                tempRepData.SetWriterIndex(TKManager.getInstance().MyData.GetUserIndex());
                tempData.SetReplyData(Integer.toString(tempData.GetReplyDataCount()), tempRepData);


                FirebaseManager.CheckFirebaseComplete ReplyListener = new FirebaseManager.CheckFirebaseComplete() {
                    @Override
                    public void CompleteListener() {
                        // 데이터 추가 하고 아래 함수 콜
                        mClubBodyFragment.RefreshReply();
                    }

                    @Override
                    public void CompleteListener_Yes() {

                    }

                    @Override
                    public void CompleteListener_No() {

                    }
                };
                FirebaseManager.getInstance().RegistClubReply(TKManager.getInstance().TargetClubData, tempData, tempCount, ReplyListener);


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
