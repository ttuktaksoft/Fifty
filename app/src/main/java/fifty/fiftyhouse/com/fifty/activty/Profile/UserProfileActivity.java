package fifty.fiftyhouse.com.fifty.activty.Profile;

import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import fifty.fiftyhouse.com.fifty.R;

public class UserProfileActivity extends AppCompatActivity {

    private ImageView iv_ThumbNail;
    private ImageView iv_Back;

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

        iv_Back = findViewById(R.id.iv_UserProfile_Back);
        iv_Back.setOnClickListener(new ImageView.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(v.getId() ==R.id.iv_UserProfile_Back){
                    finish();
                }

            }
        });

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
