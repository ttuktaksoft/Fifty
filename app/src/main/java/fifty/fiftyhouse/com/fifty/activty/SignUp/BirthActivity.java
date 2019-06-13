package fifty.fiftyhouse.com.fifty.activty.SignUp;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.PorterDuff;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.ImageViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firestore.v1beta1.FirestoreGrpc;

import java.util.ArrayList;

import fifty.fiftyhouse.com.fifty.CommonData;
import fifty.fiftyhouse.com.fifty.CommonFunc;
import fifty.fiftyhouse.com.fifty.DialogFunc;
import fifty.fiftyhouse.com.fifty.Manager.FirebaseManager;
import fifty.fiftyhouse.com.fifty.Manager.TKManager;
import fifty.fiftyhouse.com.fifty.R;

public class BirthActivity extends AppCompatActivity {

    TextView tv_SignUp_Birth_Birth_Data;
    ImageView iv_SignUp_Birth_Man, iv_SignUp_Birth_Woman, iv_SignUp_Birth_Next;

    boolean mIsGenderSelect = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_birth);

        tv_SignUp_Birth_Birth_Data = findViewById(R.id.tv_SignUp_Birth_Birth_Data);
        iv_SignUp_Birth_Man = findViewById(R.id.iv_SignUp_Birth_Man);
        iv_SignUp_Birth_Woman = findViewById(R.id.iv_SignUp_Birth_Woman);
        iv_SignUp_Birth_Next = findViewById(R.id.iv_SignUp_Birth_Next);

        ImageViewCompat.setImageTintList(iv_SignUp_Birth_Man, ColorStateList.valueOf(ContextCompat.getColor(getApplicationContext(), R.color.iv_SignUp_Birth_Man_Dis)));
        ImageViewCompat.setImageTintMode(iv_SignUp_Birth_Man, PorterDuff.Mode.SRC_ATOP);
        ImageViewCompat.setImageTintList(iv_SignUp_Birth_Woman, ColorStateList.valueOf(ContextCompat.getColor(getApplicationContext(), R.color.iv_SignUp_Birth_Woman_Dis)));
        ImageViewCompat.setImageTintMode(iv_SignUp_Birth_Woman, PorterDuff.Mode.SRC_ATOP);
        ImageViewCompat.setImageTintList(iv_SignUp_Birth_Next, ColorStateList.valueOf(ContextCompat.getColor(getApplicationContext(), R.color.gray_light)));

        iv_SignUp_Birth_Man.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mIsGenderSelect = true;
                ImageViewCompat.setImageTintList(iv_SignUp_Birth_Man, ColorStateList.valueOf(ContextCompat.getColor(getApplicationContext(), R.color.iv_SignUp_Birth_Man_Ena)));
                ImageViewCompat.setImageTintMode(iv_SignUp_Birth_Man, PorterDuff.Mode.SRC_ATOP);
                ImageViewCompat.setImageTintList(iv_SignUp_Birth_Woman, ColorStateList.valueOf(ContextCompat.getColor(getApplicationContext(), R.color.iv_SignUp_Birth_Woman_Dis)));
                ImageViewCompat.setImageTintMode(iv_SignUp_Birth_Woman, PorterDuff.Mode.SRC_ATOP);
                ImageViewCompat.setImageTintList(iv_SignUp_Birth_Next, ColorStateList.valueOf(ContextCompat.getColor(getApplicationContext(), R.color.button_enable)));
                TKManager.getInstance().MyData.SetUserGender(0);
            }
        });

        iv_SignUp_Birth_Woman.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mIsGenderSelect = true;
                ImageViewCompat.setImageTintList(iv_SignUp_Birth_Man, ColorStateList.valueOf(ContextCompat.getColor(getApplicationContext(), R.color.iv_SignUp_Birth_Man_Dis)));
                ImageViewCompat.setImageTintMode(iv_SignUp_Birth_Man, PorterDuff.Mode.SRC_ATOP);
                ImageViewCompat.setImageTintList(iv_SignUp_Birth_Woman, ColorStateList.valueOf(ContextCompat.getColor(getApplicationContext(), R.color.iv_SignUp_Birth_Woman_Ena)));
                ImageViewCompat.setImageTintMode(iv_SignUp_Birth_Woman, PorterDuff.Mode.SRC_ATOP);
                ImageViewCompat.setImageTintList(iv_SignUp_Birth_Next, ColorStateList.valueOf(ContextCompat.getColor(getApplicationContext(), R.color.button_enable)));

                TKManager.getInstance().MyData.SetUserGender(1);
            }
        });

        iv_SignUp_Birth_Next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mIsGenderSelect == false)
                {
                    // TODO 성별을 선택 유도 팝업
                    DialogFunc.getInstance().ShowMsgPopup(BirthActivity.this, CommonFunc.getInstance().getStr(getResources(), R.string.SIGN_UP_GENDER_CHECK));
                }
                else
                {
                    Intent intent = new Intent(getApplicationContext(), FavoriteActivity.class);
                    startActivity(intent);

                }
            }
        });
    }
}
