package fifty.fiftyhouse.com.fifty.activty;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Display;

import fifty.fiftyhouse.com.fifty.CommonFunc;
import fifty.fiftyhouse.com.fifty.MainActivity;
import fifty.fiftyhouse.com.fifty.Manager.FirebaseManager;
import fifty.fiftyhouse.com.fifty.R;
import fifty.fiftyhouse.com.fifty.activty.SignUp.ProfileImgActivity;

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

        // 임의로 2초 슬립
        try {
            Thread.sleep(100);
        }
        catch (InterruptedException e)
        {
            System.out.print((e.getMessage()));
        }

        FirebaseManager.getInstance().GetUserData("1", true);

        startActivity(new Intent(getApplicationContext(), MainActivity.class));
        finish();
    }
}