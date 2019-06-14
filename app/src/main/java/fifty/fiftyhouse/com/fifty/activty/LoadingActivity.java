package fifty.fiftyhouse.com.fifty.activty;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.graphics.Point;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Display;

import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import java.io.IOException;
import java.util.List;

import fifty.fiftyhouse.com.fifty.CommonFunc;
import fifty.fiftyhouse.com.fifty.DialogFunc;
import fifty.fiftyhouse.com.fifty.Manager.FirebaseManager;
import fifty.fiftyhouse.com.fifty.Manager.TKManager;
import fifty.fiftyhouse.com.fifty.R;

public class LoadingActivity extends AppCompatActivity {

    private Activity mActivity;
    private static final int REQUEST_LOCATION = 1;

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

        int permissionCamera = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION);
        if(permissionCamera == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(LoadingActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);


        } else {
            CommonFunc.getInstance().GetUserLocation(this);
            GetUserList();
        }

        final LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        //  lm = (LocationManager) activity.getSystemService(Context.LOCATION_SERVICE);



        CommonFunc.getInstance().mCurActivity = this;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_LOCATION:
                for (int i = 0; i < permissions.length; i++) {
                    String permission = permissions[i];
                    int grantResult = grantResults[i];
                    if (permission.equals(Manifest.permission.ACCESS_FINE_LOCATION)) {
                        if(grantResult == PackageManager.PERMISSION_GRANTED) {
                            CommonFunc.getInstance().GetUserLocation(this);
                            GetUserList();
                        } else {
                            android.os.Process.killProcess(android.os.Process.myPid());
                        }
                    }
                }
                break;
        }
    }

    public void GetUserList()
    {
        TKManager.getInstance().MyData.SetUserIndex("14");

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
    }




}