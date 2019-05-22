package fifty.fiftyhouse.com.fifty.activty.Profile;

import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import fifty.fiftyhouse.com.fifty.R;

public class UserProfileActivity extends AppCompatActivity {

    private ImageView iv_ThumbNail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        iv_ThumbNail = findViewById(R.id.iv_UserProfile_ThumbNail);

        Glide.with(getApplicationContext())
                //.load(mMyData.arrSendDataList.get(position).strTargetImg)
                .load(R.drawable.dummy_8)
                .circleCrop()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(iv_ThumbNail);

        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bnv_UserProfile_main_menu);
        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                        switch (menuItem.getItemId()) {

                            case R.id.menuitem_bottombar_Like:
                                return true;

                            case R.id.menuitem_bottombar_Chat:
                                return true;

                            case R.id.menuitem_bottombar_Friend:
                                return true;
                        }
                        return false;
                    }

                });

    }
}
