package fifty.fiftyhouse.com.fifty.activty.SignUp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import fifty.fiftyhouse.com.fifty.CommonFunc;
import fifty.fiftyhouse.com.fifty.DialogFunc;
import fifty.fiftyhouse.com.fifty.MainActivity;
import fifty.fiftyhouse.com.fifty.Manager.FirebaseManager;
import fifty.fiftyhouse.com.fifty.R;

public class SignUpCompleteActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_complete);
        CommonFunc.getInstance().mCurActivity = this;

        FirebaseManager.CheckFirebaseComplete firebaseListener = new FirebaseManager.CheckFirebaseComplete() {
            @Override
            public void CompleteListener() {
                DialogFunc.MsgPopupListener listener = new DialogFunc.MsgPopupListener()
                {
                    @Override
                    public void Listener()
                    {
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        finish();
                    }
                };
                DialogFunc.getInstance().ShowSignUpCompletePopup(SignUpCompleteActivity.this, listener);

            }

            @Override
            public void CompleteListener_Yes() {
            }

            @Override
            public void CompleteListener_No() {
            }
        };

        FirebaseManager.getInstance().SetMyDataOnFireBase(firebaseListener);

    }


}
