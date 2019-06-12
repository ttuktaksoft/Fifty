package fifty.fiftyhouse.com.fifty.activty;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import fifty.fiftyhouse.com.fifty.CommonFunc;
import fifty.fiftyhouse.com.fifty.Manager.TKManager;
import fifty.fiftyhouse.com.fifty.R;
public class UserProfileMemoActivity extends AppCompatActivity {

    View ui_UserProfile_Memo_TopBar;
    TextView tv_TopBar_Title, tv_UserProfile_Memo;
    ImageView iv_TopBar_Back;

    Context mContext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile_memo);
        mContext = getApplicationContext();

        ui_UserProfile_Memo_TopBar = findViewById(R.id.ui_UserProfile_Memo_TopBar);
        tv_TopBar_Title = ui_UserProfile_Memo_TopBar.findViewById(R.id.tv_TopBar_Title);
        iv_TopBar_Back = ui_UserProfile_Memo_TopBar.findViewById(R.id.iv_TopBar_Back);
        tv_UserProfile_Memo = findViewById(R.id.tv_UserProfile_Memo);
        tv_TopBar_Title.setText(CommonFunc.getInstance().getStr(mContext.getResources(), R.string.TITLE_MEMO));
        iv_TopBar_Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        tv_UserProfile_Memo.setText(TKManager.getInstance().TargetUserData.GetUserMemo());
    }
}
