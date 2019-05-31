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

    private Spinner spinner_birth;
    ArrayList<Integer> arrayList_birth;
    ArrayAdapter<String> arrayAdapter_birth;

    ImageView iv_Birth_Man, iv_Birth_Woman;

    // 이미지로 변경 예정
    Button bt_Birth_Back, bt_Birth_Next;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_birth);

        iv_Birth_Man = (ImageView)findViewById(R.id.iv_Birth_Man);
        iv_Birth_Man.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TKManager.getInstance().myData.SetUserGender(0);
            }
        });

        iv_Birth_Woman= (ImageView)findViewById(R.id.iv_Birth_Woman);
        iv_Birth_Woman.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TKManager.getInstance().myData.SetUserGender(1);
            }
        });

        bt_Birth_Back = (Button)findViewById(R.id.bt_Birth_BackButton);
        bt_Birth_Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), SignUpActivity.class);
                startActivity(intent);
                finish();
            }
        });

        bt_Birth_Next = (Button)findViewById(R.id.bt_Birth_NextButton);
        bt_Birth_Next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), FavoriteActivity.class);
                startActivity(intent);
                finish();
            }
        });


        arrayList_birth = new ArrayList<>();

        for(int i = 1950; i<1971 ; i++)
        {
            arrayList_birth.add(i);
        }

        spinner_birth = (Spinner)findViewById(R.id.spinner2);
        spinner_birth.setAdapter(arrayAdapter_birth);
        spinner_birth.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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
