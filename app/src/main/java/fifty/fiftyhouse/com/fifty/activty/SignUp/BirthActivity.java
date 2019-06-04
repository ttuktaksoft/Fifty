package fifty.fiftyhouse.com.fifty.activty.SignUp;

import android.content.Intent;
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

        iv_SignUp_Birth_Man.setColorFilter(R.color.gray_light);
        iv_SignUp_Birth_Woman.setColorFilter(R.color.gray_light);
        iv_SignUp_Birth_Next.setColorFilter(R.color.gray_light);

        iv_SignUp_Birth_Man.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mIsGenderSelect = true;
                iv_SignUp_Birth_Man.setColorFilter(R.color.white);
                iv_SignUp_Birth_Woman.setColorFilter(R.color.gray_light);
                iv_SignUp_Birth_Next.setColorFilter(R.color.white);
                TKManager.getInstance().myData.SetUserGender(0);
            }
        });

        iv_SignUp_Birth_Woman.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mIsGenderSelect = true;
                iv_SignUp_Birth_Man.setColorFilter(R.color.gray_light);
                iv_SignUp_Birth_Woman.setColorFilter(R.color.white);
                iv_SignUp_Birth_Next.setColorFilter(R.color.white);
                TKManager.getInstance().myData.SetUserGender(1);
            }
        });

        iv_SignUp_Birth_Next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mIsGenderSelect == false)
                {
                    // TODO 성별을 선택 유도 팝업
                }
                else
                {
                    Intent intent = new Intent(getApplicationContext(), FavoriteActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }
}
