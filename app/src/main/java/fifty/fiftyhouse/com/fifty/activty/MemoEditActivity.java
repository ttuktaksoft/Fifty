package fifty.fiftyhouse.com.fifty.activty;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import fifty.fiftyhouse.com.fifty.CommonData;
import fifty.fiftyhouse.com.fifty.CommonFunc;
import fifty.fiftyhouse.com.fifty.Manager.FirebaseManager;
import fifty.fiftyhouse.com.fifty.Manager.TKManager;
import fifty.fiftyhouse.com.fifty.R;
import fifty.fiftyhouse.com.fifty.util.OnSingleClickListener;

public class MemoEditActivity extends AppCompatActivity {

    View ui_Memo_Edit_TopBar;
    TextView tv_TopBar_Title;
    ImageView iv_TopBar_Back;

    TextView tv_Story_Edit_Save, tv_Story_Edit_Count;
    EditText et_Story_Edit_Memo;

    Context mContext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memo_edit);
        mContext = getApplicationContext();

        ui_Memo_Edit_TopBar = findViewById(R.id.ui_Memo_Edit_TopBar);
        tv_TopBar_Title = ui_Memo_Edit_TopBar.findViewById(R.id.tv_TopBar_Title);
        iv_TopBar_Back = ui_Memo_Edit_TopBar.findViewById(R.id.iv_TopBar_Back);

        tv_Story_Edit_Save = findViewById(R.id.tv_Story_Edit_Save);
        tv_Story_Edit_Count = findViewById(R.id.tv_Story_Edit_Count);
        et_Story_Edit_Memo = findViewById(R.id.et_Story_Edit_Memo);
        tv_TopBar_Title.setText(CommonFunc.getInstance().getStr(mContext.getResources(), R.string.TITLE_MEMO_EDIT));
        iv_TopBar_Back.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View view) {
                finish();
            }
        });

        et_Story_Edit_Memo.setText(TKManager.getInstance().MyData.GetUserMemo());
        CommonFunc.getInstance().setEditTextMaxSize(et_Story_Edit_Memo, CommonData.MemoMaxSize);
        if(CommonFunc.getInstance().CheckStringNull(TKManager.getInstance().MyData.GetUserMemo()))
            tv_Story_Edit_Count.setText(Integer.toString(CommonData.MemoMaxSize));
        else
            tv_Story_Edit_Count.setText(Integer.toString(CommonData.MemoMaxSize - TKManager.getInstance().MyData.GetUserMemo().length()));

        et_Story_Edit_Memo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                tv_Story_Edit_Count.setText(Integer.toString(CommonData.MemoMaxSize - s.length()));
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        tv_Story_Edit_Save.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View view) {
            String tempMemo = et_Story_Edit_Memo.getText().toString();
            TKManager.getInstance().MyData.SetUserMemo(tempMemo);
            FirebaseManager.getInstance().SetUserMemo();
            finish();
            }
        });

    }
}
