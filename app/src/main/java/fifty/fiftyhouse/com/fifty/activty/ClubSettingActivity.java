package fifty.fiftyhouse.com.fifty.activty;

import android.content.Context;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import fifty.fiftyhouse.com.fifty.CommonData;
import fifty.fiftyhouse.com.fifty.CommonFunc;
import fifty.fiftyhouse.com.fifty.DialogFunc;
import fifty.fiftyhouse.com.fifty.Manager.FirebaseManager;
import fifty.fiftyhouse.com.fifty.Manager.TKManager;
import fifty.fiftyhouse.com.fifty.R;
import fifty.fiftyhouse.com.fifty.util.OnSingleClickListener;

public class ClubSettingActivity extends AppCompatActivity {

    View ui_ClubSetting_TopBar;
    TextView tv_TopBar_Title;
    ImageView iv_TopBar_Back;

    TextView tv_ClubSetting_Join_Wait, tv_ClubSetting_Report, tv_ClubSetting_Edit, tv_ClubSetting_Withdrawal, iv_ClubSetting_Invite;
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
        iv_ClubSetting_Invite = findViewById(R.id.iv_ClubSetting_Invite);

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

                DialogFunc.getInstance().ShowLoadingPage(ClubSettingActivity.this);
                FirebaseManager.CheckFirebaseComplete listener = new FirebaseManager.CheckFirebaseComplete() {
                    @Override
                    public void CompleteListener() {
                        Intent intent = new Intent(mContext, UserListActivity.class);
                        intent.putExtra("Type",CommonData.USER_LIST_CLUB_JOIN_WAIT);
                        startActivity(intent);
                        DialogFunc.getInstance().DismissLoadingPage();
                    }

                    @Override
                    public void CompleteListener_Yes() {

                    }

                    @Override
                    public void CompleteListener_No() {

                    }
                };

                FirebaseManager.getInstance().GetRequestJoinUserInMyClub(TKManager.getInstance().TargetClubData.GetClubIndex(), listener );



            }
        });

        tv_ClubSetting_Report.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View view) {

                DialogFunc.getInstance().ShowLoadingPage(ClubSettingActivity.this);
                FirebaseManager.CheckFirebaseComplete listener = new FirebaseManager.CheckFirebaseComplete() {
                    @Override
                    public void CompleteListener() {
                        DialogFunc.getInstance().DismissLoadingPage();
                        Log.d("test","@@@@@@ 111 " + TKManager.getInstance().TargetReportContextData.size());
                        Intent intent = new Intent(mContext, ClubBodyReportListActivity.class);
                        startActivity(intent);
                    }

                    @Override
                    public void CompleteListener_Yes() {

                    }

                    @Override
                    public void CompleteListener_No() {

                    }
                };

                FirebaseManager.getInstance().GetClubReportData(TKManager.getInstance().TargetClubData.GetClubIndex(), listener );

            }
        });

        tv_ClubSetting_Edit.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View view) {
                Intent intent = new Intent(mContext, ClubCreateActivity.class);
                intent.putExtra("Type",1);
                startActivity(intent);
            }
        });

        tv_ClubSetting_Withdrawal.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View view) {
                final DialogFunc.MsgPopupListener listenerYes = new DialogFunc.MsgPopupListener() {
                    @Override
                    public void Listener() {
                        DialogFunc.getInstance().ShowLoadingPage(ClubSettingActivity.this);

                        // 클럽 탈퇴
                        FirebaseManager.CheckFirebaseComplete removeListener = new FirebaseManager.CheckFirebaseComplete() {
                            @Override
                            public void CompleteListener() {
                                TKManager.getInstance().MyData.DelUserClubData(TKManager.getInstance().TargetClubData.GetClubIndex());
                                TKManager.getInstance().MyData.DelRequestJoinClubList(TKManager.getInstance().TargetClubData.GetClubIndex());
                                //TKManager.getInstance().ClubData_Simple.get(TKManager.getInstance().TargetClubData.GetClubIndex()).DelClubMember(TKManager.getInstance().MyData.GetUserIndex());
                                TKManager.getInstance().ClubData_Simple.get(TKManager.getInstance().TargetClubData.GetClubIndex()).ClubMemberCount = TKManager.getInstance().TargetClubData.GetClubMemberCount();
                                DialogFunc.getInstance().DismissLoadingPage();
                                DialogFunc.getInstance().ShowToast(ClubSettingActivity.this, "탈퇴 하였습니다", true);
                                finish();
                            }

                            @Override
                            public void CompleteListener_Yes() {

                            }

                            @Override
                            public void CompleteListener_No() {

                            }
                        };

                        FirebaseManager.getInstance().RemoveClubMember(TKManager.getInstance().TargetClubData, TKManager.getInstance().MyData.GetUserIndex(), removeListener);
                    }
                };

                DialogFunc.getInstance().ShowMsgPopup(ClubSettingActivity.this, listenerYes, null, CommonFunc.getInstance().getStr(ClubSettingActivity.this.getResources(), R.string.MSG_CLUB_WITHDRAWAL_DESC),
                        CommonFunc.getInstance().getStr(ClubSettingActivity.this.getResources(), R.string.MSG_CLUB_WITHDRAWAL), CommonFunc.getInstance().getStr(ClubSettingActivity.this.getResources(), R.string.MSG_CANCEL));
            }
        });

        iv_ClubSetting_Invite.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View view) {

                DialogFunc.getInstance().ShowLoadingPage(ClubSettingActivity.this);
                Set tempKey = TKManager.getInstance().TargetClubData.ClubFavorite.keySet();
                List array = new ArrayList();

                array = new ArrayList(tempKey);

                // 클럽 탈퇴
                FirebaseManager.CheckFirebaseComplete InviteListener = new FirebaseManager.CheckFirebaseComplete() {
                    @Override
                    public void CompleteListener() {
                        DialogFunc.getInstance().DismissLoadingPage();
                        Intent intent = new Intent(mContext, UserListActivity.class);
                        intent.putExtra("Type",CommonData.USER_LIST_CLUB_INVITE);
                        startActivity(intent);
                    }

                    @Override
                    public void CompleteListener_Yes() {

                    }

                    @Override
                    public void CompleteListener_No() {
                        DialogFunc.getInstance().DismissLoadingPage();
                        DialogFunc.getInstance().ShowMsgPopup(ClubSettingActivity.this, CommonFunc.getInstance().getStr(ClubSettingActivity.this.getResources(), R.string.MSG_CLUB_INVITE_EMPTY));
                    }
                };

                FirebaseManager.getInstance().InviteUsersInClub(array.get(0).toString(), InviteListener);


                //DialogFunc.getInstance().ShowUserInvitePopup(ClubSettingActivity.this, ClubSettingActivity.this);
            }
        });
    }
}
