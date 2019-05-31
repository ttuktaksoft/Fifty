package fifty.fiftyhouse.com.fifty.activty.SignUp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import fifty.fiftyhouse.com.fifty.Manager.FirebaseManager;
import fifty.fiftyhouse.com.fifty.R;

public class FavoriteDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite_detail);

        FirebaseManager.getInstance().GetPopFavoriteData();
    }
}
