package fifty.fiftyhouse.com.fifty.activty;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import androidx.fragment.app.FragmentManager;
import androidx.core.widget.NestedScrollView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;

import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import fifty.fiftyhouse.com.fifty.CommonFunc;
import fifty.fiftyhouse.com.fifty.DataBase.ClubContextData;
import fifty.fiftyhouse.com.fifty.DialogFunc;
import fifty.fiftyhouse.com.fifty.Manager.FirebaseManager;
import fifty.fiftyhouse.com.fifty.Manager.TKManager;
import fifty.fiftyhouse.com.fifty.R;
import fifty.fiftyhouse.com.fifty.fragment.ClubBodyFragment;
import fifty.fiftyhouse.com.fifty.util.OnSingleClickListener;

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
    public static Activity mClubBodyActivity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_club_body);
        imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        mContext = getApplicationContext();
        mActivity = this;
        mFragmentMgr = getSupportFragmentManager();
        mClubBodyActivity = this;

        Intent intent = getIntent(); //getIntent()로 받을준비
        int nType = getIntent().getIntExtra("Type", 0);
        final String nKey = getIntent().getStringExtra("key");


        tempData = new ClubContextData();
        if(nType == 1)
        {
            tempData = TKManager.getInstance().TargetReportContextData.get(nKey);
            TKManager.getInstance().TargetContextData = tempData;
        }
        else
        {
            tempData = TKManager.getInstance().TargetClubData.GetClubContext(nKey);
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
                        DialogFunc.getInstance().ShowLoadingPage(ClubBodyActivity.this);
                        // 게시글 삭제
                        FirebaseManager.CheckFirebaseComplete listener = new FirebaseManager.CheckFirebaseComplete() {
                            @Override
                            public void CompleteListener() {
                                // 데이터 추가 하고 아래 함수 콜
                                FirebaseManager.CheckFirebaseComplete ReportListener = new FirebaseManager.CheckFirebaseComplete() {
                                    @Override
                                    public void CompleteListener() {
                                        // 데이터 추가 하고 아래 함수 콜
                                        DialogFunc.getInstance().DismissLoadingPage();
                                        TKManager.getInstance().TargetReportContextData.remove(nKey);
                                        TKManager.getInstance().TargetRemoveContextData.put(nKey, tempData);
                                        finish();
                                    }

                                    @Override
                                    public void CompleteListener_Yes() {

                                    }

                                    @Override
                                    public void CompleteListener_No() {

                                    }
                                };
                                FirebaseManager.getInstance().RemoveReportContext(TKManager.getInstance().TargetClubData.GetClubIndex(), tempData.GetContextIndex(), ReportListener);
                            }

                            @Override
                            public void CompleteListener_Yes() {

                            }

                            @Override
                            public void CompleteListener_No() {

                            }
                        };
                        FirebaseManager.getInstance().RemoveClubContext(TKManager.getInstance().TargetClubData.GetClubIndex(), tempData.GetContextIndex(), listener);
                    }
                };

                DialogFunc.getInstance().ShowMsgPopup(ClubBodyActivity.this, listenerYes, null, CommonFunc.getInstance().getStr(ClubBodyActivity.this.getResources(), R.string.MSG_CLUB_BODY_REPORT_DEL_DESC),
                        CommonFunc.getInstance().getStr(ClubBodyActivity.this.getResources(), R.string.MSG_TRY_DEL), CommonFunc.getInstance().getStr(ClubBodyActivity.this.getResources(), R.string.MSG_CANCEL));
            }
        });

        tv_ClubBody_Send.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View view) {
                imm.hideSoftInputFromWindow(et_ClubBody_Reply.getWindowToken(), 0);

                if(CommonFunc.getInstance().CheckStringNull(et_ClubBody_Reply.getText().toString()))
                {
                    DialogFunc.getInstance().ShowMsgPopup(ClubBodyActivity.this, CommonFunc.getInstance().getStr(getResources(), R.string.MSG_DESC_EMPTY));
                    return;
                }

                int tempCount = tempData.GetReplyDataCount();

                String tempReplyData = TKManager.getInstance().MyData.GetUserIndex() + "_" + CommonFunc.getInstance().GetCurrentTime() + "_"  + et_ClubBody_Reply.getText().toString();
                tempData.SetReply(Integer.toString(tempData.GetReplyDataCount()), tempReplyData);

                ClubContextData tempRepData =new ClubContextData();

                tempRepData.SetContext(et_ClubBody_Reply.getText().toString());
                tempRepData.SetDate(CommonFunc.getInstance().GetCurrentTime());
                tempRepData.SetWriterIndex(TKManager.getInstance().MyData.GetUserIndex());
                tempData.SetReplyData(Integer.toString(tempData.GetReplyDataCount()), tempRepData);

                et_ClubBody_Reply.setText(null);

                FirebaseManager.CheckFirebaseComplete ReplyListener = new FirebaseManager.CheckFirebaseComplete() {
                    @Override
                    public void CompleteListener() {
                        // 데이터 추가 하고 아래 함수 콜
                        mClubBodyFragment.RefreshReply();
                        mClubBodyFragment.MoveScrollEnd();
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
