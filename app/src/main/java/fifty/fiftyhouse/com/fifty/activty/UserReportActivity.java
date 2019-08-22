package fifty.fiftyhouse.com.fifty.activty;

import android.content.Context;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import fifty.fiftyhouse.com.fifty.CommonData;
import fifty.fiftyhouse.com.fifty.CommonFunc;
import fifty.fiftyhouse.com.fifty.DialogFunc;
import fifty.fiftyhouse.com.fifty.R;
import fifty.fiftyhouse.com.fifty.util.OnSingleClickListener;

public class UserReportActivity extends AppCompatActivity {

    View ui_UserReport_TopBar;
    TextView tv_TopBar_Title;
    ImageView iv_TopBar_Back;
    Context mContext;

    TextView tv_UserReport_Count, tv_UserReport_Report;
    EditText et_UserReport_Memo;
    InputMethodManager imm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_report);
        mContext = getApplicationContext();
        imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);

        ui_UserReport_TopBar = findViewById(R.id.ui_UserReport_TopBar);
        tv_TopBar_Title = ui_UserReport_TopBar.findViewById(R.id.tv_TopBar_Title);
        iv_TopBar_Back = ui_UserReport_TopBar.findViewById(R.id.iv_TopBar_Back);
        tv_UserReport_Count = findViewById(R.id.tv_UserReport_Count);
        tv_UserReport_Report = findViewById(R.id.tv_UserReport_Report);
        et_UserReport_Memo = findViewById(R.id.et_UserReport_Memo);

        iv_TopBar_Back.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View view) {
                imm.hideSoftInputFromWindow(et_UserReport_Memo.getWindowToken(), 0);
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

        tv_UserReport_Report.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View view) {
                imm.hideSoftInputFromWindow(et_UserReport_Memo.getWindowToken(), 0);

                if(CommonFunc.getInstance().CheckStringNull(et_UserReport_Memo.getText().toString()))
                {
                    DialogFunc.getInstance().ShowMsgPopup(UserReportActivity.this, CommonFunc.getInstance().getStr(getResources(), R.string.MSG_DESC_EMPTY));
                    return;
                }

                DialogFunc.getInstance().ShowToast(UserReportActivity.this, CommonFunc.getInstance().getStr(getResources(), R.string.MSG_USER_REPORT_RESULT), true);
                finish();
            }
        });

    }
}
