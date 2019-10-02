package fifty.fiftyhouse.com.fifty.activty;

import android.content.Context;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import fifty.fiftyhouse.com.fifty.CommonFunc;
import fifty.fiftyhouse.com.fifty.DialogFunc;
import fifty.fiftyhouse.com.fifty.Manager.FirebaseManager;
import fifty.fiftyhouse.com.fifty.Manager.TKManager;
import fifty.fiftyhouse.com.fifty.R;
import fifty.fiftyhouse.com.fifty.util.OnSingleClickListener;

public class SettingAccountActivity extends AppCompatActivity {

    View ui_Setting_Account_TopBar;
    TextView tv_TopBar_Title;
    ImageView iv_TopBar_Back;
    TextView tv_Setting_Account_ID, tv_Setting_Account_Logout, tv_Setting_Account_Withdrawal;

    Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_account);

        mContext = getApplicationContext();
        ui_Setting_Account_TopBar = findViewById(R.id.ui_Setting_Account_TopBar);
        tv_TopBar_Title = ui_Setting_Account_TopBar.findViewById(R.id.tv_TopBar_Title);
        iv_TopBar_Back = ui_Setting_Account_TopBar.findViewById(R.id.iv_TopBar_Back);

        tv_Setting_Account_ID = findViewById(R.id.tv_Setting_Account_ID);
        tv_Setting_Account_Logout = findViewById(R.id.tv_Setting_Account_Logout);
        tv_Setting_Account_Withdrawal = findViewById(R.id.tv_Setting_Account_Withdrawal);

        tv_TopBar_Title.setText(CommonFunc.getInstance().getStr(mContext.getResources(), R.string.MSG_ACCOUNT));

        iv_TopBar_Back.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View view) {
                finish();
            }
        });

        tv_Setting_Account_ID.setText(TKManager.getInstance().MyData.GetUserNickName());
        tv_Setting_Account_Logout.setOnClickListener(new OnSingleClickListener(){
            @Override
            public void onSingleClick(View view) {
                final DialogFunc.MsgPopupListener listenerYes = new DialogFunc.MsgPopupListener() {
                    @Override
                    public void Listener() {

                    }
                };

                DialogFunc.getInstance().ShowMsgPopup(SettingAccountActivity.this, listenerYes, null, CommonFunc.getInstance().getStr(SettingAccountActivity.this.getResources(), R.string.MSG_LOGOUT_ASK),
                        CommonFunc.getInstance().getStr(SettingAccountActivity.this.getResources(), R.string.MSG_OK), CommonFunc.getInstance().getStr(SettingAccountActivity.this.getResources(), R.string.MSG_CANCEL));
            }
        });

        tv_Setting_Account_Withdrawal.setOnClickListener(new OnSingleClickListener(){
            @Override
            public void onSingleClick(View view) {
                final DialogFunc.MsgPopupListener listenerYes = new DialogFunc.MsgPopupListener() {
                    @Override
                    public void Listener() {

                    }
                };

                DialogFunc.getInstance().ShowMsgPopup(SettingAccountActivity.this, listenerYes, null, CommonFunc.getInstance().getStr(SettingAccountActivity.this.getResources(), R.string.MSG_WITHDRAWAL_ASK),
                        CommonFunc.getInstance().getStr(SettingAccountActivity.this.getResources(), R.string.MSG_OK), CommonFunc.getInstance().getStr(SettingAccountActivity.this.getResources(), R.string.MSG_CANCEL));
            }
        });
    }
}
