package fifty.fiftyhouse.com.fifty.activty;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import java.io.File;
import java.util.List;

import fifty.fiftyhouse.com.fifty.CommonFunc;
import fifty.fiftyhouse.com.fifty.DialogFunc;
import fifty.fiftyhouse.com.fifty.Manager.FirebaseManager;
import fifty.fiftyhouse.com.fifty.Manager.TKManager;
import fifty.fiftyhouse.com.fifty.R;
import fifty.fiftyhouse.com.fifty.adapter.MyProfileEditMenuAdapter;
import fifty.fiftyhouse.com.fifty.util.ImageResize;
import fifty.fiftyhouse.com.fifty.util.RecyclerItemClickListener;

public class MyProfileEditActivity extends AppCompatActivity {

    View ui_MyProfile_Edit_TopBar;
    TextView tv_TopBar_Title;
    ImageView iv_TopBar_Back;
    ImageView iv_MyProfile_Edit_Profile;
    RecyclerView rv_MyProfile_Edit_Menu;

    MyProfileEditMenuAdapter mAdapter;
    private Context mContext;

    private static final int GET_FROM_GALLERY = 1;
    private File tempFile;
    private boolean isCamera = false;

    private boolean isUpload = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile_edit);
        mContext = getApplicationContext();

        ui_MyProfile_Edit_TopBar = findViewById(R.id.ui_MyProfile_Edit_TopBar);
        tv_TopBar_Title = ui_MyProfile_Edit_TopBar.findViewById(R.id.tv_TopBar_Title);
        iv_TopBar_Back = ui_MyProfile_Edit_TopBar.findViewById(R.id.iv_TopBar_Back);
        iv_MyProfile_Edit_Profile = findViewById(R.id.iv_MyProfile_Edit_Profile);
        rv_MyProfile_Edit_Menu = findViewById(R.id.rv_MyProfile_Edit_Menu);
        tv_TopBar_Title.setText(CommonFunc.getInstance().getStr(mContext.getResources(), R.string.TITLE_PROFILE_EDIT));
        iv_TopBar_Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        CommonFunc.getInstance().DrawImageByGlide(mContext, iv_MyProfile_Edit_Profile, TKManager.getInstance().MyData.GetUserImgThumb(), true);

        iv_MyProfile_Edit_Profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GetPermission();
            }
        });

        initRecyclerView();
    }


    private void initRecyclerView()
    {
        mAdapter = new MyProfileEditMenuAdapter(getApplicationContext());
        mAdapter.setHasStableIds(true);

        rv_MyProfile_Edit_Menu.setAdapter(mAdapter);
        rv_MyProfile_Edit_Menu.setLayoutManager(new GridLayoutManager(getApplicationContext(), 1));
        rv_MyProfile_Edit_Menu.addOnItemTouchListener(new RecyclerItemClickListener(getApplicationContext(), rv_MyProfile_Edit_Menu, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if(position == MyProfileEditMenuAdapter.MY_PROFILE_EDIT_MENU_NICKNAME_INDEX)
                {
                    startActivity(new Intent(getApplicationContext(), NickNameEditActivity.class));
                    finish();
                }
                else if(position == MyProfileEditMenuAdapter.MY_PROFILE_EDIT_MENU_STORY_INDEX)
                {
                    startActivity(new Intent(getApplicationContext(), MemoEditActivity.class));
                    finish();
                }
                else if(position == MyProfileEditMenuAdapter.MY_PROFILE_EDIT_MENU_LOC_INDEX)
                {
                    startActivity(new Intent(getApplicationContext(), LocationEditActivity.class));
                    finish();
                }
                else if(position == MyProfileEditMenuAdapter.MY_PROFILE_EDIT_MENU_FAVORITE_INDEX)
                {
                    startActivity(new Intent(getApplicationContext(), FavoriteSelectActivity.class));
                }
            }

            @Override
            public void onLongItemClick(View view, int position) {
                //  Toast.makeText(getApplicationContext(),position+"번 째 아이템 롱 클릭",Toast.LENGTH_SHORT).show();
            }
        }));
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

        DialogFunc.getInstance().ShowLoadingPage(MyProfileEditActivity.this);

        ImageResize.resizeFile(tempFile, tempFile, 1280, isCamera);

        BitmapFactory.Options options = new BitmapFactory.Options();
        Bitmap originalBm = BitmapFactory.decodeFile(tempFile.getAbsolutePath(), options);
        iv_MyProfile_Edit_Profile.setImageBitmap(originalBm);
        Glide.with(this).load(originalBm)
                .centerCrop()
                .circleCrop()
                .into(iv_MyProfile_Edit_Profile);

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


        FirebaseManager.getInstance().UploadImg(TKManager.getInstance().MyData.GetUserIndex(), originalBm, 0, listener);
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
