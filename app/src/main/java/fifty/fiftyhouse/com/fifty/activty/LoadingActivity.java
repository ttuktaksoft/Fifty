package fifty.fiftyhouse.com.fifty.activty;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Display;

import fifty.fiftyhouse.com.fifty.CommonFunc;
import fifty.fiftyhouse.com.fifty.DialogFunc;
import fifty.fiftyhouse.com.fifty.MainActivity;
import fifty.fiftyhouse.com.fifty.Manager.FirebaseManager;
import fifty.fiftyhouse.com.fifty.Manager.TKManager;
import fifty.fiftyhouse.com.fifty.R;
import fifty.fiftyhouse.com.fifty.activty.SignUp.BirthActivity;
import fifty.fiftyhouse.com.fifty.activty.SignUp.FavoriteActivity;
import fifty.fiftyhouse.com.fifty.activty.SignUp.FavoriteDetailActivity;
import fifty.fiftyhouse.com.fifty.activty.SignUp.ProfileImgActivity;
import fifty.fiftyhouse.com.fifty.activty.SignUp.SignUpActivity;

public class LoadingActivity extends AppCompatActivity {

    private Activity mActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);

        mActivity = this;

        final Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        CommonFunc.getInstance().setWidthByDevice(size.x);
        CommonFunc.getInstance().setHeightByDevice(size.y);

        //FirebaseManager.getInstance().GetMyData("1");

        //CommonFunc.getInstance().AddDummy(100);


        TKManager.getInstance().MyData.SetUserIndex("1");
        CommonFunc.getInstance().mCurActivity = this;
        //startActivity(new Intent(getApplicationContext(), LoginActivity.class));
        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
        /*DialogFunc.getInstance().ShowLoadingPage(LoadingActivity.this);
        FirebaseManager.getInstance().GetUserList();

        DialogFunc.getInstance().DismissLoadingPage();*/
    }
}