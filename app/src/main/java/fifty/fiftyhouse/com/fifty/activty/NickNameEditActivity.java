package fifty.fiftyhouse.com.fifty.activty;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
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
import fifty.fiftyhouse.com.fifty.MainActivity;
import fifty.fiftyhouse.com.fifty.Manager.FirebaseManager;
import fifty.fiftyhouse.com.fifty.Manager.TKManager;
import fifty.fiftyhouse.com.fifty.R;

public class NickNameEditActivity extends AppCompatActivity {

    View ui_NickName_Edit_TopBar;
    TextView tv_TopBar_Title;
    ImageView iv_TopBar_Back;

    TextView tv_NickName_Edit_Save, tv_NickName_Edit_Count, tv_NickName_Edit_Check, tv_NickName_Edit_Check_Result;
    EditText et_NickName_Edit_NickName;

    boolean mIsNickNameCheck = false;
    InputMethodManager imm;
    Context mContext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nick_name_edit);
        mContext = getApplicationContext();
        imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);

        ui_NickName_Edit_TopBar = findViewById(R.id.ui_NickName_Edit_TopBar);
        tv_TopBar_Title = ui_NickName_Edit_TopBar.findViewById(R.id.tv_TopBar_Title);
        iv_TopBar_Back = ui_NickName_Edit_TopBar.findViewById(R.id.iv_TopBar_Back);
        tv_NickName_Edit_Save = findViewById(R.id.tv_NickName_Edit_Save);
        tv_NickName_Edit_Count = findViewById(R.id.tv_NickName_Edit_Count);
        tv_NickName_Edit_Check = findViewById(R.id.tv_NickName_Edit_Check);
        tv_NickName_Edit_Check_Result = findViewById(R.id.tv_NickName_Edit_Check_Result);
        et_NickName_Edit_NickName = findViewById(R.id.et_NickName_Edit_NickName);

        tv_TopBar_Title.setText(CommonFunc.getInstance().getStr(mContext.getResources(), R.string.TITLE_NICKNAME_EDIT));
        iv_TopBar_Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        et_NickName_Edit_NickName.setText(TKManager.getInstance().MyData.GetUserNickName());
        CommonFunc.getInstance().setEditTextMaxSize(et_NickName_Edit_NickName, CommonData.NickNameMaxSize);
        if(CommonFunc.getInstance().CheckStringNull(TKManager.getInstance().MyData.GetUserNickName()))
            tv_NickName_Edit_Count.setText(Integer.toString(CommonData.LocationMaxSize));
        else
            tv_NickName_Edit_Count.setText(Integer.toString(CommonData.NickNameMaxSize - TKManager.getInstance().MyData.GetUserNickName().length()));


        mIsNickNameCheck = false;
        tv_NickName_Edit_Check_Result.setText(CommonFunc.getInstance().getStr(mContext.getResources(), R.string.NICKNAME_CHECK_NO));
        tv_NickName_Edit_Check_Result.setTextColor(ContextCompat.getColor(mContext, R.color.red));

        et_NickName_Edit_NickName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 입력을 시도 했을경우 중복체크 안함으로 수정
                mIsNickNameCheck = false;
                tv_NickName_Edit_Check_Result.setText(CommonFunc.getInstance().getStr(mContext.getResources(), R.string.NICKNAME_CHECK_NO));
                tv_NickName_Edit_Check_Result.setTextColor(ContextCompat.getColor(mContext, R.color.red));
            }
        });

        et_NickName_Edit_NickName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                tv_NickName_Edit_Count.setText(Integer.toString(CommonData.NickNameMaxSize- s.length()));
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        tv_NickName_Edit_Check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imm.hideSoftInputFromWindow(et_NickName_Edit_NickName.getWindowToken(), 0);
                final String strNickName = et_NickName_Edit_NickName.getText().toString();

                if(CommonFunc.getInstance().CheckStringNull(strNickName))
                {
                    DialogFunc.getInstance().ShowMsgPopup(NickNameEditActivity.this, CommonFunc.getInstance().getStr(getResources(), R.string.NICKNAME_EMPTY));
                }
                else if(strNickName.length() < CommonData.NickNameMinSize)
                {
                    DialogFunc.getInstance().ShowMsgPopup(NickNameEditActivity.this, CommonFunc.getInstance().getStr(getResources(), R.string.NICKNAME_LEAK));
                }
                else
                {
                    FirebaseManager.CheckFirebaseComplete listener = new FirebaseManager.CheckFirebaseComplete() {
                        @Override
                        public void CompleteListener() {

                        }

                        @Override
                        public void CompleteListener_Yes() {
                            DialogFunc.getInstance().ShowToast(NickNameEditActivity.this, CommonFunc.getInstance().getStr(getResources(), R.string.NICKNAME_CHECK_SUCCESS), true);
                            mIsNickNameCheck = true;
                            tv_NickName_Edit_Check_Result.setText(CommonFunc.getInstance().getStr(mContext.getResources(), R.string.NICKNAME_CHECK_YES));
                            tv_NickName_Edit_Check_Result.setTextColor(ContextCompat.getColor(mContext, R.color.blue));
                        }

                        @Override
                        public void CompleteListener_No() {
                            DialogFunc.getInstance().ShowMsgPopup(NickNameEditActivity.this, CommonFunc.getInstance().getStr(getResources(), R.string.NICKNAME_CHECK_FAIL));
                            mIsNickNameCheck = false;
                            tv_NickName_Edit_Check_Result.setText(CommonFunc.getInstance().getStr(mContext.getResources(), R.string.NICKNAME_CHECK_NO));
                            tv_NickName_Edit_Check_Result.setTextColor(ContextCompat.getColor(mContext, R.color.red));
                        }
                    };

                    FirebaseManager.getInstance().CheckNickName(strNickName, listener);
                }
            }
        });

        tv_NickName_Edit_Save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imm.hideSoftInputFromWindow(et_NickName_Edit_NickName.getWindowToken(), 0);

                final String tempNickName = et_NickName_Edit_NickName.getText().toString();

                if(CommonFunc.getInstance().CheckStringNull(tempNickName))
                {
                    DialogFunc.getInstance().ShowMsgPopup(NickNameEditActivity.this, CommonFunc.getInstance().getStr(getResources(), R.string.NICKNAME_EMPTY));
                    return;
                }
                else
                {
                    if(tempNickName.equals(TKManager.getInstance().MyData.GetUserNickName()))
                        finish();
                }

                if(mIsNickNameCheck == false)
                {
                    DialogFunc.getInstance().ShowMsgPopup(NickNameEditActivity.this, CommonFunc.getInstance().getStr(getResources(), R.string.NICKNAME_CHECK_ASK));
                }
                else
                {

                  //  DialogFunc.getInstance().ShowLoadingPage(NickNameEditActivity.this);
                    FirebaseManager.CheckFirebaseComplete listener = new FirebaseManager.CheckFirebaseComplete() {
                        @Override
                        public void CompleteListener() {
                   //         DialogFunc.getInstance().DismissLoadingPage();
                            TKManager.getInstance().MyData.SetUserNickName(tempNickName);
                            FirebaseManager.getInstance().UpdateUserName();
                            finish();
                        }

                        @Override
                        public void CompleteListener_Yes() {
                        }

                        @Override
                        public void CompleteListener_No() {
                        }
                    };

                    FirebaseManager.getInstance().RemoveUserNickName(listener);
                    // TODO 닉네임 변경
                }
            }
        });
    }
}
