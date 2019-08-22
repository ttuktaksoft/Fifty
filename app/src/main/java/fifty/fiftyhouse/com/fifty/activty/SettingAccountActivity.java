package fifty.fiftyhouse.com.fifty.activty;

import android.content.Context;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import fifty.fiftyhouse.com.fifty.CommonFunc;
import fifty.fiftyhouse.com.fifty.R;
import fifty.fiftyhouse.com.fifty.util.OnSingleClickListener;

public class SettingAccountActivity extends AppCompatActivity {

    View ui_Setting_Account_TopBar;
    TextView tv_TopBar_Title, tv_Setting_Account_Save;
    ImageView iv_TopBar_Back;
    EditText et_Setting_Account_Email, et_Setting_Account_Password;
    Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_account);

        mContext = getApplicationContext();
        ui_Setting_Account_TopBar = findViewById(R.id.ui_Setting_TopBar);
        tv_TopBar_Title = ui_Setting_Account_TopBar.findViewById(R.id.tv_TopBar_Title);
        iv_TopBar_Back = ui_Setting_Account_TopBar.findViewById(R.id.iv_TopBar_Back);

        tv_Setting_Account_Save = findViewById(R.id.tv_Setting_Account_Save);
        et_Setting_Account_Email = findViewById(R.id.et_Setting_Account_Email);
        et_Setting_Account_Password = findViewById(R.id.et_Setting_Account_Password);

        tv_TopBar_Title.setText(CommonFunc.getInstance().getStr(mContext.getResources(), R.string.MSG_ACCOUNT));


        iv_TopBar_Back.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View view) {
                finish();
            }
        });
    }
}
