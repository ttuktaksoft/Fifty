package fifty.fiftyhouse.com.fifty.activty.SignUp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import fifty.fiftyhouse.com.fifty.R;

public class FavoriteActivity extends AppCompatActivity {

    Button bt_Favorite_Select;
    Button bt_Favorite_Back, bt_Favorite_Next;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite);

        bt_Favorite_Back = (Button)findViewById(R.id.bt_Favorite_BackButton);
        bt_Favorite_Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), BirthActivity.class);
                startActivity(intent);
                finish();
            }
        });

        bt_Favorite_Next = (Button)findViewById(R.id.bt_Favorite_NextButton);
        bt_Favorite_Next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ProfileImgActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
