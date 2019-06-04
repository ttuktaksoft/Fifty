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
import android.widget.Toast;

import com.google.firestore.v1beta1.FirestoreGrpc;

import java.util.ArrayList;

import fifty.fiftyhouse.com.fifty.CommonData;
import fifty.fiftyhouse.com.fifty.Manager.FirebaseManager;
import fifty.fiftyhouse.com.fifty.Manager.TKManager;
import fifty.fiftyhouse.com.fifty.R;

public class BirthActivity extends AppCompatActivity {

    private Spinner sp_SignUp_Birth_Birth;
    ArrayList<Integer> arrayList_birth;
    ArrayAdapter<String> arrayAdapter_birth;

    ImageView iv_SignUp_Birth_Man, iv_SignUp_Birth_Woman, iv_SignUp_Birth_Next;

    boolean mIsGenderSelect = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_birth);

        sp_SignUp_Birth_Birth = findViewById(R.id.sp_SignUp_Birth_Birth);
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

        // TODO 카톡에서 연령을 받아오게 되면서 필요성이 있나?
        arrayList_birth = new ArrayList<>();

        for(int i = 1950; i<1971 ; i++)
        {
            arrayList_birth.add(i);
        }

        sp_SignUp_Birth_Birth.setAdapter(arrayAdapter_birth);
        sp_SignUp_Birth_Birth.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(getApplicationContext(),arrayList_birth.get(i)+"가 선택되었습니다.",
                        Toast.LENGTH_SHORT).show();

                TKManager.getInstance().myData.SetUserAge(arrayList_birth.get(i));
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                TKManager.getInstance().myData.SetUserAge(50);
            }
        });
    }
}
