package fifty.fiftyhouse.com.fifty.activty;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import fifty.fiftyhouse.com.fifty.CommonData;
import fifty.fiftyhouse.com.fifty.CommonFunc;
import fifty.fiftyhouse.com.fifty.DialogFunc;
import fifty.fiftyhouse.com.fifty.Manager.FirebaseManager;
import fifty.fiftyhouse.com.fifty.Manager.TKManager;
import fifty.fiftyhouse.com.fifty.R;

public class UserReportActivity extends AppCompatActivity {

    View ui_UserReport_TopBar;
    TextView tv_TopBar_Title;
    ImageView iv_TopBar_Back;
    Context mContext;

    TextView tv_UserReport_Count, tv_UserReport_Report;
    EditText et_UserReport_Memo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_report);
        mContext = getApplicationContext();

        ui_UserReport_TopBar = findViewById(R.id.ui_UserReport_TopBar);
        tv_TopBar_Title = ui_UserReport_TopBar.findViewById(R.id.tv_TopBar_Title);
        iv_TopBar_Back = ui_UserReport_TopBar.findViewById(R.id.iv_TopBar_Back);
        tv_UserReport_Count = findViewById(R.id.tv_UserReport_Count);
        tv_UserReport_Report = findViewById(R.id.tv_UserReport_Report);
        et_UserReport_Memo = findViewById(R.id.et_UserReport_Memo);

        iv_TopBar_Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        tv_TopBar_Title.setText(CommonFunc.getInstance().getStr(mContext.getResources(), R.string.TITLE_USER_REPORT));

        CommonFunc.getInstance().setEditTextMaxSize(et_UserReport_Memo, CommonData.UserReportMaxSize);
        tv_UserReport_Count.setText(Integer.toString(CommonData.UserReportMaxSize));

        et_UserReport_Memo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                tv_UserReport_Count.setText(Integer.toString(CommonData.UserReportMaxSize - s.length()));
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        tv_UserReport_Report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFunc.getInstance().ShowToast(UserReportActivity.this, CommonFunc.getInstance().getStr(getResources(), R.string.MSG_USER_REPORT_RESULT), true);
                finish();
            }
        });

    }
}
