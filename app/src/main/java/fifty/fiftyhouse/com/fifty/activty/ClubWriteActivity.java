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
import fifty.fiftyhouse.com.fifty.DialogFunc;
import fifty.fiftyhouse.com.fifty.Manager.FirebaseManager;
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

        iv_TopBar_Back.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View view) {
                finish();
            }
        });
        tv_TopBar_Title.setText(CommonFunc.getInstance().getStr(mContext.getResources(), R.string.TITLE_WRITE_CLUB));

        mClubWriteFragment = new ClubWriteFragment();

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

            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == Activity.RESULT_OK) {
                DialogFunc.getInstance().ShowLoadingPage(mContext);
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
                Uri resultUri = result.getUri();
                //mClubWriteFragment.AddImg(resultUri.toString());
            }
        }
    }
}