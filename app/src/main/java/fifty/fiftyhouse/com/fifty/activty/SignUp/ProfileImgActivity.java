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

import fifty.fiftyhouse.com.fifty.Manager.FirebaseManager;
import fifty.fiftyhouse.com.fifty.Manager.TKManager;
import fifty.fiftyhouse.com.fifty.R;
import fifty.fiftyhouse.com.fifty.util.ImageResize;

public class ProfileImgActivity extends AppCompatActivity {

    private static final int GET_FROM_GALLERY = 1;
    private File tempFile;
    private boolean isCamera = false;
    ImageView iv_userThumbnail;
    Button bt_ProfileImg_Back, bt_ProfileImg_Next;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_img);
        iv_userThumbnail = findViewById(R.id.iv_ProfileImg_Select);

        iv_userThumbnail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GetPermission();
            }
        });


        bt_ProfileImg_Back = (Button)findViewById(R.id.bt_ProfileImg_BackButton);
        bt_ProfileImg_Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), FavoriteActivity.class);
                startActivity(intent);
                finish();
            }
        });

        bt_ProfileImg_Next = (Button)findViewById(R.id.bt_ProfileImg_NextButton);
        bt_ProfileImg_Next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), SignUpCompleteActivity.class);
                startActivity(intent);
                finish();
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

        ImageResize.resizeFile(tempFile, tempFile, 1280, isCamera);

        BitmapFactory.Options options = new BitmapFactory.Options();
        Bitmap originalBm = BitmapFactory.decodeFile(tempFile.getAbsolutePath(), options);
        iv_userThumbnail.setImageBitmap(originalBm);
        Glide.with(this).load(originalBm)
                .centerCrop()
                .circleCrop()
                .into(iv_userThumbnail);

        FirebaseManager.getInstance().UploadImg(TKManager.getInstance().MyData.GetUserIndex(), originalBm);
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
