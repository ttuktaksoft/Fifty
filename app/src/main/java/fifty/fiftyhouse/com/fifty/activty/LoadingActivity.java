package fifty.fiftyhouse.com.fifty.activty;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Point;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Display;

import fifty.fiftyhouse.com.fifty.CommonFunc;
import fifty.fiftyhouse.com.fifty.DialogFunc;
import fifty.fiftyhouse.com.fifty.MainActivity;
import fifty.fiftyhouse.com.fifty.Manager.FirebaseManager;
import fifty.fiftyhouse.com.fifty.Manager.TKManager;
import fifty.fiftyhouse.com.fifty.R;

public class LoadingActivity extends AppCompatActivity {

    private Activity mActivity;
    private static final int REQUEST_LOCATION = 1;
    String userIndex;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);

        mActivity = this;


      /*  try {
            PackageInfo info = getPackageManager().getPackageInfo("fifty.fiftyhouse.com.fifty", PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.e("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }*/

        final Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        CommonFunc.getInstance().setWidthByDevice(size.x);
        CommonFunc.getInstance().setHeightByDevice(size.y);

        final LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        SharedPreferences sf = getSharedPreferences("userFile",MODE_PRIVATE);
        //text라는 key에 저장된 값이 있는지 확인. 아무값도 들어있지 않으면 ""를 반환
        userIndex = sf.getString("Index","");

        //userIndex = null;

       // userIndex = "39";
        if(CommonFunc.getInstance().CheckStringNull(userIndex))
        {

            int permissionCamera = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION);
            if(permissionCamera == PackageManager.PERMISSION_DENIED) {
                ActivityCompat.requestPermissions(LoadingActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);

            } else {
                CommonFunc.CheckLocationComplete listener = new CommonFunc.CheckLocationComplete() {
                    @Override
                    public void CompleteListener() {
                        CommonFunc.getInstance().MoveLoginActivity(LoadingActivity.this);
                    }

                    @Override
                    public void CompleteListener_Yes() {

                    }

                    @Override
                    public void CompleteListener_No() {
                        CommonFunc.getInstance().MoveLoginActivity(LoadingActivity.this);
                    }
                };

                CommonFunc.getInstance().GetUserLocation(LoadingActivity.this, listener);
                // GetUserList();
            }

            // 회ㅏ우너가입

          /*  FirebaseManager.CheckFirebaseComplete listen = new FirebaseManager.CheckFirebaseComplete() {
                @Override
                public void CompleteListener() {

                    int permissionCamera = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION);
                    if(permissionCamera == PackageManager.PERMISSION_DENIED) {
                        ActivityCompat.requestPermissions(LoadingActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);

                    } else {
                        CommonFunc.CheckLocationComplete listener = new CommonFunc.CheckLocationComplete() {
                            @Override
                            public void CompleteListener() {
                                CommonFunc.getInstance().MoveLoginActivity(LoadingActivity.this);
                            }

                            @Override
                            public void CompleteListener_Yes() {

                            }

                            @Override
                            public void CompleteListener_No() {
                                CommonFunc.getInstance().MoveLoginActivity(LoadingActivity.this);
                            }
                        };

                        CommonFunc.getInstance().GetUserLocation(LoadingActivity.this, listener);
                       // GetUserList();
                    }
                }

                @Override
                public void CompleteListener_Yes() {
                }

                @Override
                public void CompleteListener_No() {
                    DialogFunc.getInstance().DismissLoadingPage();
                }
            };

            FirebaseManager.getInstance().GetUserIndex(listen);*/
        }
        else
        {
            TKManager.getInstance().MyData.SetUserIndex(userIndex);

            int permissionCamera = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION);
            if(permissionCamera == PackageManager.PERMISSION_DENIED) {
                ActivityCompat.requestPermissions(LoadingActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);

            } else {
                CommonFunc.CheckLocationComplete listener = new CommonFunc.CheckLocationComplete() {
                    @Override
                    public void CompleteListener() {


                        FirebaseManager.CheckFirebaseComplete FavoriteListener = new   FirebaseManager.CheckFirebaseComplete() {
                            @Override
                            public void CompleteListener() {
                                CommonFunc.getInstance().GetUserList(LoadingActivity.this);
                            }

                            @Override
                            public void CompleteListener_Yes() {

                            }

                            @Override
                            public void CompleteListener_No() {
                                CommonFunc.getInstance().GetUserList(LoadingActivity.this);
                            }
                        };

                        FirebaseManager.getInstance().GetDailyFavorite(FavoriteListener);
                    }

                    @Override
                    public void CompleteListener_Yes() {

                    }

                    @Override
                    public void CompleteListener_No() {
                        CommonFunc.getInstance().GetUserList(LoadingActivity.this);
                    }
                };

                CommonFunc.getInstance().GetUserLocation(this, listener);
            }
        }

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



                            CommonFunc.CheckLocationComplete listener = new CommonFunc.CheckLocationComplete() {
                                @Override
                                public void CompleteListener() {
                                    if(CommonFunc.getInstance().CheckStringNull(userIndex))
                                    {
                                        CommonFunc.getInstance().MoveLoginActivity(LoadingActivity.this);
                                    }
                                    else
                                    {
                                        CommonFunc.getInstance().GetUserList(LoadingActivity.this);
                                    }


                                }

                                @Override
                                public void CompleteListener_Yes() {

                                }

                                @Override
                                public void CompleteListener_No() {
                                    if(CommonFunc.getInstance().CheckStringNull(userIndex))
                                    {
                                        CommonFunc.getInstance().MoveLoginActivity(LoadingActivity.this);
                                    }
                                    else
                                    {
                                        CommonFunc.getInstance().GetUserList(LoadingActivity.this);
                                    }

                                }
                            };

                            CommonFunc.getInstance().GetUserLocation(this, listener);

                        //    GetUserList();
                        } else {
                            android.os.Process.killProcess(android.os.Process.myPid());
                        }
                    }
                }
                break;
        }
    }





}