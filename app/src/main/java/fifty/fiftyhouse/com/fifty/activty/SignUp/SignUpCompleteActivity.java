package fifty.fiftyhouse.com.fifty.activty.SignUp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import fifty.fiftyhouse.com.fifty.MainActivity;
import fifty.fiftyhouse.com.fifty.Manager.FirebaseManager;
import fifty.fiftyhouse.com.fifty.R;

public class SignUpCompleteActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_complete);

        FirebaseManager.getInstance().SignInAnonymously(SignUpCompleteActivity.this);

        startActivity(new Intent(getApplicationContext(), MainActivity.class));
        finish();
    }


}
