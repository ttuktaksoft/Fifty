package fifty.fiftyhouse.com.fifty.activty;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Display;

import fifty.fiftyhouse.com.fifty.CommonFunc;
import fifty.fiftyhouse.com.fifty.DialogFunc;
import fifty.fiftyhouse.com.fifty.Manager.FirebaseManager;
import fifty.fiftyhouse.com.fifty.Manager.TKManager;
import fifty.fiftyhouse.com.fifty.R;

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

        FirebaseManager.CheckFirebaseComplete listener = new FirebaseManager.CheckFirebaseComplete() {
            @Override
            public void CompleteListener() {

                FirebaseManager.CheckFirebaseComplete Innerlistener = new FirebaseManager.CheckFirebaseComplete() {
                    @Override
                    public void CompleteListener() {
                        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                        finish();
                    }

                    @Override
                    public void CompleteListener_Yes() {
                    }

                    @Override
                    public void CompleteListener_No() {
                        DialogFunc.getInstance().DismissLoadingPage();
                    }
                };

                FirebaseManager.getInstance().GetUserList(Innerlistener);
            }

            @Override
            public void CompleteListener_Yes() {
            }

            @Override
            public void CompleteListener_No() {
                DialogFunc.getInstance().DismissLoadingPage();
            }
        };

        FirebaseManager.getInstance().GetUserData(TKManager.getInstance().MyData.GetUserIndex(), TKManager.getInstance().MyData, listener );
        CommonFunc.getInstance().mCurActivity = this;
        //startActivity(new Intent(getApplicationContext(), LoginActivity.class));


        /*DialogFunc.getInstance().ShowLoadingPage(LoadingActivity.this);


        DialogFunc.getInstance().DismissLoadingPage();*/
    }
}