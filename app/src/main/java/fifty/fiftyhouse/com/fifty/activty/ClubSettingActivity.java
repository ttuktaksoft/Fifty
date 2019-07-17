package fifty.fiftyhouse.com.fifty.activty;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import fifty.fiftyhouse.com.fifty.CommonData;
import fifty.fiftyhouse.com.fifty.CommonFunc;
import fifty.fiftyhouse.com.fifty.DialogFunc;
import fifty.fiftyhouse.com.fifty.Manager.TKManager;
import fifty.fiftyhouse.com.fifty.R;
import fifty.fiftyhouse.com.fifty.util.OnSingleClickListener;

public class ClubSettingActivity extends AppCompatActivity {

    View ui_ClubSetting_TopBar;
    TextView tv_TopBar_Title;
    ImageView iv_TopBar_Back;

    TextView tv_ClubSetting_Join_Wait, tv_ClubSetting_Report, tv_ClubSetting_Edit, tv_ClubSetting_Withdrawal;
    Context mContext;

    boolean mIsMasterClub = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_club_setting);

        mContext = getApplicationContext();
        ui_ClubSetting_TopBar = findViewById(R.id.ui_ClubSetting_TopBar);
        tv_TopBar_Title = ui_ClubSetting_TopBar.findViewById(R.id.tv_TopBar_Title);
        iv_TopBar_Back = ui_ClubSetting_TopBar.findViewById(R.id.iv_TopBar_Back);
        tv_ClubSetting_Join_Wait = findViewById(R.id.tv_ClubSetting_Join_Wait);
        tv_ClubSetting_Report = findViewById(R.id.tv_ClubSetting_Report);
        tv_ClubSetting_Edit = findViewById(R.id.tv_ClubSetting_Edit);
        tv_ClubSetting_Withdrawal = findViewById(R.id.tv_ClubSetting_Withdrawal);

        tv_TopBar_Title.setText(CommonFunc.getInstance().getStr(mContext.getResources(), R.string.MSG_CLUB_SETTING));
        iv_TopBar_Back.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View view) {
                finish();
            }
        });

        mIsMasterClub = TKManager.getInstance().TargetClubData.GetClubMasterIndex().equals(TKManager.getInstance().MyData.GetUserIndex());

        if(mIsMasterClub)
        {
            if(TKManager.getInstance().TargetClubData.GetClubType() == false)
                tv_ClubSetting_Join_Wait.setVisibility(View.VISIBLE);
            else
                tv_ClubSetting_Join_Wait.setVisibility(View.GONE);

            tv_ClubSetting_Report.setVisibility(View.VISIBLE);
            tv_ClubSetting_Edit.setVisibility(View.VISIBLE);
            tv_ClubSetting_Withdrawal.setVisibility(View.GONE);
        }
        else
        {
            tv_ClubSetting_Join_Wait.setVisibility(View.GONE);
            tv_ClubSetting_Report.setVisibility(View.GONE);
            tv_ClubSetting_Edit.setVisibility(View.GONE);
            tv_ClubSetting_Withdrawal.setVisibility(View.VISIBLE);
        }

        tv_ClubSetting_Join_Wait.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View view) {
                Intent intent = new Intent(mContext, UserListActivity.class);
                intent.putExtra("Type",CommonData.USER_LIST_CLUB_JOIN_WAIT);
                startActivity(intent);
            }
        });

        tv_ClubSetting_Report.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View view) {
                Intent intent = new Intent(mContext, ClubBodyReportListActivity.class);
                startActivity(intent);
            }
        });

        tv_ClubSetting_Edit.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View view) {
                Intent intent = new Intent(mContext, ClubEditActivity.class);
                startActivity(intent);
            }
        });

        tv_ClubSetting_Withdrawal.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View view) {
                final DialogFunc.MsgPopupListener listenerYes = new DialogFunc.MsgPopupListener() {
                    @Override
                    public void Listener() {
                        // 클럽 탈퇴
                    }
                };

                DialogFunc.getInstance().ShowMsgPopup(ClubSettingActivity.this, listenerYes, null, CommonFunc.getInstance().getStr(ClubSettingActivity.this.getResources(), R.string.MSG_CLUB_WITHDRAWAL_DESC),
                        CommonFunc.getInstance().getStr(ClubSettingActivity.this.getResources(), R.string.MSG_CLUB_WITHDRAWAL), CommonFunc.getInstance().getStr(ClubSettingActivity.this.getResources(), R.string.MSG_CANCEL));
            }
        });


    }
}
