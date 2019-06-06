package fifty.fiftyhouse.com.fifty.activty.SignUp;

import android.Manifest;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import java.io.File;
import java.util.List;

import fifty.fiftyhouse.com.fifty.CommonFunc;
import fifty.fiftyhouse.com.fifty.DialogFunc;
import fifty.fiftyhouse.com.fifty.MainActivity;
import fifty.fiftyhouse.com.fifty.Manager.FirebaseManager;
import fifty.fiftyhouse.com.fifty.Manager.TKManager;
import fifty.fiftyhouse.com.fifty.R;
import fifty.fiftyhouse.com.fifty.util.ImageResize;

public class ProfileImgActivity extends AppCompatActivity {

    ImageView tv_SignUp_Profile_Thumbnail, iv_SignUp_Profile_Next;

    private static final int GET_FROM_GALLERY = 1;
    private File tempFile;
    private boolean isCamera = false;

    private boolean isUpload = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_img);
        CommonFunc.getInstance().mCurActivity = this;

        tv_SignUp_Profile_Thumbnail = findViewById(R.id.tv_SignUp_Profile_Thumbnail);
        iv_SignUp_Profile_Next = findViewById(R.id.iv_SignUp_Profile_Next);

        tv_SignUp_Profile_Thumbnail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GetPermission();
            }
        });

        iv_SignUp_Profile_Next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(isUpload == true)
                {
                    Intent intent = new Intent(getApplicationContext(), SignUpCompleteActivity.class);
                    startActivity(intent);
                    finish();
                }
                else
                    DialogFunc.getInstance().ShowMsgPopup(ProfileImgActivity.this, CommonFunc.getInstance().getStr(getResources(), R.string.MSG_NOTICE), CommonFunc.getInstance().getStr(getResources(), R.string.tv_SignUp_Profile_Upload_Fail));
            }
        });

    }

    private void GetPermission() {
        PermissionListener permissionListener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
                GetPhotoInGallery();
            }

            @Override
            public void onPermissionDenied(List<String> deniedPermissions) {
            }
        };

        TedPermission.with(this)
                .setPermissionListener(permissionListener)
                .setRationaleMessage(getResources().getString(R.string.permission_cammera))
                .setDeniedMessage(getResources().getString(R.string.permission_request))
                .setPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA)
                .check();

    }

    // 앨범에서 가져오기
    private void GetPhotoInGallery() {

        isCamera = true;
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
        startActivityForResult(intent, GET_FROM_GALLERY);
    }

    private void setImage() {

        DialogFunc.getInstance().ShowLoadingPage(ProfileImgActivity.this);

        ImageResize.resizeFile(tempFile, tempFile, 1280, isCamera);

        BitmapFactory.Options options = new BitmapFactory.Options();
        Bitmap originalBm = BitmapFactory.decodeFile(tempFile.getAbsolutePath(), options);
        tv_SignUp_Profile_Thumbnail.setImageBitmap(originalBm);
        Glide.with(this).load(originalBm)
                .centerCrop()
                .circleCrop()
                .into(tv_SignUp_Profile_Thumbnail);

        FirebaseManager.CheckFirebaseComplete listener = new FirebaseManager.CheckFirebaseComplete() {
            @Override
            public void CompleteListener() {
                isUpload = true;
                DialogFunc.getInstance().DismissLoadingPage();
            }

            @Override
            public void CompleteListener_Yes() {
            }

            @Override
            public void CompleteListener_No() {
            }
        };


        FirebaseManager.getInstance().UploadImg(TKManager.getInstance().MyData.GetUserIndex(), originalBm, TKManager.getInstance().MyData.GetUserImgCount(), listener);
        FirebaseManager.getInstance().UploadThumbImg(TKManager.getInstance().MyData.GetUserIndex(), originalBm, listener);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == GET_FROM_GALLERY) {
            Uri photoUri = data.getData();
            Cursor cursor = null;

            try {
                String[] proj = { MediaStore.Images.Media.DATA };
                assert photoUri != null;
                cursor = getContentResolver().query(photoUri, proj, null, null, null);

                assert cursor != null;
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);

                cursor.moveToFirst();
                tempFile = new File(cursor.getString(column_index));

            } finally {
                if (cursor != null) {
                    cursor.close();
                }
            }

            setImage();

        }
    }
}