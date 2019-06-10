package fifty.fiftyhouse.com.fifty.activty;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import fifty.fiftyhouse.com.fifty.Manager.FirebaseManager;
import fifty.fiftyhouse.com.fifty.Manager.TKManager;
import fifty.fiftyhouse.com.fifty.R;
public class LocationEditActivity extends AppCompatActivity {

    ImageView iv_Location_Edit_Back, iv_Location_Edit_Save;
    TextView tv_Location_Edit_Count;
    EditText et_Location_Edit_Location;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_edit);

        iv_Location_Edit_Back = findViewById(R.id.iv_Location_Edit_Back);
        iv_Location_Edit_Save = findViewById(R.id.iv_Location_Edit_Save);
        tv_Location_Edit_Count = findViewById(R.id.tv_Location_Edit_Count);
        et_Location_Edit_Location = findViewById(R.id.et_Location_Edit_Location);

        iv_Location_Edit_Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        iv_Location_Edit_Save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String tempLocation  = et_Location_Edit_Location.getText().toString();
                if(tempLocation.getBytes().length > 0)
                {
                    TKManager.getInstance().MyData.SetUserLocation(tempLocation);
                    FirebaseManager.getInstance().SetUserLocation();
                }

                startActivity(new Intent(LocationEditActivity.this, MyProfileEditActivity.class));
                finish();
            }
        });
    }
}
