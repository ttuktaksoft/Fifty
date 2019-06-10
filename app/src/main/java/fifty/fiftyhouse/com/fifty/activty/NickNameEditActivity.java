package fifty.fiftyhouse.com.fifty.activty;

import android.content.res.ColorStateList;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.ImageViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import fifty.fiftyhouse.com.fifty.CommonFunc;
import fifty.fiftyhouse.com.fifty.DialogFunc;
import fifty.fiftyhouse.com.fifty.Manager.FirebaseManager;
import fifty.fiftyhouse.com.fifty.R;
import fifty.fiftyhouse.com.fifty.activty.SignUp.SignUpActivity;

public class NickNameEditActivity extends AppCompatActivity {

    ImageView iv_NickName_Edit_Back, iv_NickName_Edit_Check, iv_NickName_Edit_Save;
    TextView tv_NickName_Edit_Count;
    EditText et_NickName_Edit_NickName;

    boolean mIsNickNameCheck = false;
    InputMethodManager imm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nick_name_edit);
        imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);

        iv_NickName_Edit_Back = findViewById(R.id.iv_NickName_Edit_Back);
        iv_NickName_Edit_Check = findViewById(R.id.iv_NickName_Edit_Check);
        iv_NickName_Edit_Save = findViewById(R.id.iv_NickName_Edit_Save);
        tv_NickName_Edit_Count = findViewById(R.id.tv_NickName_Edit_Count);
        et_NickName_Edit_NickName = findViewById(R.id.et_NickName_Edit_NickName);

        iv_NickName_Edit_Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        ImageViewCompat.setImageTintList(iv_NickName_Edit_Save, ColorStateList.valueOf(ContextCompat.getColor(getApplicationContext(), R.color.gray_light)));
        et_NickName_Edit_NickName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 입력을 시도 했을경우 중복체크 안함으로 수정
                mIsNickNameCheck = false;
                ImageViewCompat.setImageTintList(iv_NickName_Edit_Save, ColorStateList.valueOf(ContextCompat.getColor(getApplicationContext(), R.color.gray_light)));
            }
        });

        iv_NickName_Edit_Check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String strNickName = et_NickName_Edit_NickName.getText().toString();
                if(TextUtils.isEmpty(strNickName))
                {
                    // TODO 닉네임 입력 해달라는 팝업 표시
                    DialogFunc.getInstance().ShowMsgPopup(NickNameEditActivity.this, CommonFunc.getInstance().getStr(getResources(), R.string.MSG_NOTICE), CommonFunc.getInstance().getStr(getResources(), R.string.NICKNAME_EDIT_EMPTY));
                }
                else
                {
                    imm.hideSoftInputFromWindow(et_NickName_Edit_NickName.getWindowToken(), 0);
                    FirebaseManager.CheckFirebaseComplete listener = new FirebaseManager.CheckFirebaseComplete() {
                        @Override
                        public void CompleteListener() {

                        }

                        @Override
                        public void CompleteListener_Yes() {
                            DialogFunc.getInstance().ShowMsgPopup(NickNameEditActivity.this, CommonFunc.getInstance().getStr(getResources(), R.string.MSG_NOTICE), CommonFunc.getInstance().getStr(getResources(), R.string.NICKNAME_EDIT_CHECK_SUCCESS));
                            mIsNickNameCheck = true;
                            ImageViewCompat.setImageTintList(iv_NickName_Edit_Save, ColorStateList.valueOf(ContextCompat.getColor(getApplicationContext(), R.color.button_enable)));
                        }

                        @Override
                        public void CompleteListener_No() {
                            DialogFunc.getInstance().ShowMsgPopup(NickNameEditActivity.this, CommonFunc.getInstance().getStr(getResources(), R.string.MSG_NOTICE), CommonFunc.getInstance().getStr(getResources(), R.string.NICKNAME_EDIT_CHECK_FAIL));
                        }
                    };

                    FirebaseManager.getInstance().CheckNickName(strNickName, listener);
                }
            }
        });
    }
}
