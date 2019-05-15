package fifty.fiftyhouse.com.fifty.activty.SignUp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;

import fifty.fiftyhouse.com.fifty.R;

public class BirthActivity extends AppCompatActivity {

    private Spinner spinner_birth;
    ArrayList<Integer> arrayList_birth;
    ArrayAdapter<String> arrayAdapter_birth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_birth);

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
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
    }
}
