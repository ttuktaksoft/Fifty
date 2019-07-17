package fifty.fiftyhouse.com.fifty.activty;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.theartofdev.edmodo.cropper.CropImage;

import java.io.FileNotFoundException;
import java.io.IOException;

import fifty.fiftyhouse.com.fifty.CommonData;
import fifty.fiftyhouse.com.fifty.CommonFunc;
import fifty.fiftyhouse.com.fifty.DataBase.ClubContextData;
import fifty.fiftyhouse.com.fifty.DialogFunc;
import fifty.fiftyhouse.com.fifty.Manager.FirebaseManager;
import fifty.fiftyhouse.com.fifty.Manager.TKManager;
import fifty.fiftyhouse.com.fifty.R;
import fifty.fiftyhouse.com.fifty.fragment.ClubWriteFragment;
import fifty.fiftyhouse.com.fifty.fragment.SignUpFragment;
import fifty.fiftyhouse.com.fifty.util.OnSingleClickListener;

public class ClubWriteActivity extends AppCompatActivity {

    View ui_ClubWrite_TopBar;
    TextView tv_TopBar_Title;
    ImageView iv_TopBar_Back;
    TextView tv_ClubWrite_OK;
    ImageView iv_ClubWrite_Img;

    Context mContext;
    Activity mActivity;
    FragmentManager mFragmentMgr;
    ClubWriteFragment mClubWriteFragment;

    ClubContextData tempData = new ClubContextData();
    int mClubWriteType;
    String key;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_club_write);
        mActivity = this;
        mContext = getApplicationContext();
        mFragmentMgr = getSupportFragmentManager();

        ui_ClubWrite_TopBar = findViewById(R.id.ui_ClubWrite_TopBar);
        tv_TopBar_Title = ui_ClubWrite_TopBar.findViewById(R.id.tv_TopBar_Title);
        iv_TopBar_Back = ui_ClubWrite_TopBar.findViewById(R.id.iv_TopBar_Back);
        tv_ClubWrite_OK = findViewById(R.id.tv_ClubWrite_OK);
        iv_ClubWrite_Img = findViewById(R.id.iv_ClubWrite_Img);

        Intent intent = getIntent(); //getIntent()로 받을준비
        mClubWriteType = getIntent().getIntExtra("Type", 0);
        key = getIntent().getStringExtra("key");

        iv_TopBar_Back.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View view) {
                finish();
            }
        });

        if(mClubWriteType == 0)
        {
            tv_TopBar_Title.setText(CommonFunc.getInstance().getStr(mContext.getResources(), R.string.TITLE_WRITE_CLUB));
            tv_ClubWrite_OK.setText(CommonFunc.getInstance().getStr(mContext.getResources(), R.string.MSG_CLUB_WRITE));
        }
        else
        {
            tv_TopBar_Title.setText(CommonFunc.getInstance().getStr(mContext.getResources(), R.string.TITLE_WRITE_CLUB_CHANGE));
            tv_ClubWrite_OK.setText(CommonFunc.getInstance().getStr(mContext.getResources(), R.string.MSG_CLUB_WRITE_EDIT));
        }

        mClubWriteFragment = new ClubWriteFragment();

        if(mClubWriteType == 1)
        {
            tempData = TKManager.getInstance().TargetClubData.GetClubContext(key);
            mClubWriteFragment.tempData = tempData;
        }
        else
        {
            mClubWriteFragment.tempData = null;
        }

        mFragmentMgr.beginTransaction().replace(R.id.fl_ClubWrite_FrameLayout, mClubWriteFragment, "ClubWriteFragment").commit();

        iv_ClubWrite_Img.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View view) {
                CommonFunc.getInstance().GetPermissionForGalleryCamera(ClubWriteActivity.this, CommonData.GET_PHOTO_FROM_CROP);
            }
        });

        tv_ClubWrite_OK.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View view) {

                if(mClubWriteType == 0)
                {
                    if(mClubWriteFragment.IsWrite() == false)
                        return;

                    DialogFunc.getInstance().ShowLoadingPage(ClubWriteActivity.this);
                    TKManager.getInstance().CreateTempClubContextData.SetWriterIndex(TKManager.getInstance().MyData.GetUserIndex());
                    TKManager.getInstance().CreateTempClubContextData.SetDate(CommonFunc.getInstance().GetCurrentTime());

                    if(TKManager.getInstance().TempClubContextImg.size()  == 0)
                    {
                        TKManager.getInstance().CreateTempClubContextData.SetContextType(0);
                        SetClubContext();
                    }

                    else if(TKManager.getInstance().TempClubContextImg.size() == 1)
                    {
                        TKManager.getInstance().CreateTempClubContextData.SetContextType(1);
                        FirebaseManager.getInstance().SetFireBaseLoadingCount(TKManager.getInstance().TempClubContextImg.size());
                        FirebaseManager.CheckFirebaseComplete uploadListener = new FirebaseManager.CheckFirebaseComplete() {
                            @Override
                            public void CompleteListener() {
                                SetClubContext();
                            }

                            @Override
                            public void CompleteListener_Yes() {

                            }

                            @Override
                            public void CompleteListener_No() {

                            }
                        };

                        FirebaseManager.getInstance().UploadClubContextImg(TKManager.getInstance().TargetClubData, TKManager.getInstance().TempClubContextImg.get("0"),
                                "0", TKManager.getInstance().CreateTempClubContextData, uploadListener);
                    }
                    else
                    {
                        TKManager.getInstance().CreateTempClubContextData.SetContextType(2);

                        for(int i=0 ;i< TKManager.getInstance().TempClubContextImg.size(); i++)
                        {
                            FirebaseManager.getInstance().SetFireBaseLoadingCount(TKManager.getInstance().TempClubContextImg.size());
                            FirebaseManager.CheckFirebaseComplete uploadListener = new FirebaseManager.CheckFirebaseComplete() {
                                @Override
                                public void CompleteListener() {
                                    SetClubContext();
                                }

                                @Override
                                public void CompleteListener_Yes() {

                                }

                                @Override
                                public void CompleteListener_No() {

                                }
                            };

                            FirebaseManager.getInstance().UploadClubContextImg(TKManager.getInstance().TargetClubData, TKManager.getInstance().TempClubContextImg.get(Integer.toString(i)),
                                    Integer.toString(i), TKManager.getInstance().CreateTempClubContextData, uploadListener);
                        }
                    }
                }
                else
                {
                    // 글 수정
                    EditClubContext();
                }

            }
        });
    }

    public void EditClubContext()
    {
        TKManager.getInstance().TargetClubData.GetClubContext(key).SetContext(TKManager.getInstance().CreateTempClubContextData.Context);

        FirebaseManager.CheckFirebaseComplete ContextListener = new FirebaseManager.CheckFirebaseComplete() {
            @Override
            public void CompleteListener() {

                ClubContextData tempData = new ClubContextData();

                try {
                    tempData =  (ClubContextData) TKManager.getInstance().TargetClubData.GetClubContext(key).clone();
                } catch (CloneNotSupportedException e) {
                    e.printStackTrace();
                }


                TKManager.getInstance().TargetClubData.AddClubContext(tempData.GetContextIndex(), tempData);
                CommonFunc.getInstance().SortByClubContentDate(TKManager.getInstance().TargetClubData.ClubContext, false);

                TKManager.getInstance().CreateTempClubContextData.Clear();

                DialogFunc.getInstance().DismissLoadingPage();
                finish();
            }

            @Override
            public void CompleteListener_Yes() {

            }

            @Override
            public void CompleteListener_No() {

            }
        };
        FirebaseManager.getInstance().EditClubContext(TKManager.getInstance().TargetClubData.GetClubIndex(), TKManager.getInstance().TargetClubData.GetClubContext(key), ContextListener);
    }



    public void SetClubContext()
    {
        FirebaseManager.CheckFirebaseComplete ContextListener = new FirebaseManager.CheckFirebaseComplete() {
            @Override
            public void CompleteListener() {

                ClubContextData tempData = new ClubContextData();

                try {
                    tempData =  (ClubContextData) TKManager.getInstance().CreateTempClubContextData.clone();
                } catch (CloneNotSupportedException e) {
                    e.printStackTrace();
                }


                TKManager.getInstance().TargetClubData.AddClubContext(tempData.GetContextIndex(), tempData);
                CommonFunc.getInstance().SortByClubContentDate(TKManager.getInstance().TargetClubData.ClubContext, false);

                TKManager.getInstance().CreateTempClubContextData.Clear();

                DialogFunc.getInstance().DismissLoadingPage();
                finish();
            }

            @Override
            public void CompleteListener_Yes() {

            }

            @Override
            public void CompleteListener_No() {

            }
        };
        FirebaseManager.getInstance().RegistClubContext(TKManager.getInstance().TargetClubData.GetClubIndex(), TKManager.getInstance().CreateTempClubContextData, ContextListener);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == Activity.RESULT_OK) {
                DialogFunc.getInstance().ShowLoadingPage(ClubWriteActivity.this);

                // TODO 클럽 이미지 올려야함
                Uri resultUri = result.getUri();

                Bitmap originalBm = null;
                try {
                    originalBm = MediaStore.Images.Media.getBitmap(mContext.getContentResolver(), resultUri);
                } catch (FileNotFoundException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

                TKManager.getInstance().TempClubContextImg.put(Integer.toString(TKManager.getInstance().TempClubContextImg.size()), originalBm);
                mClubWriteFragment.AddImg(resultUri.toString());
                //CommonFunc.getInstance().DrawImageByGlide(mContext, iv_ClubCreate_Profile, originalBm, false);
                DialogFunc.getInstance().DismissLoadingPage();

                FirebaseManager.CheckFirebaseComplete listener = new FirebaseManager.CheckFirebaseComplete() {
                    @Override
                    public void CompleteListener() {
                        DialogFunc.getInstance().DismissLoadingPage();

                    }

                    @Override
                    public void CompleteListener_Yes() {
                    }

                    @Override
                    public void CompleteListener_No() {
                    }
                };
                // TODO 클럽 이미지 올려야함
              //  Uri resultUri = result.getUri();

            }
        }
    }
}