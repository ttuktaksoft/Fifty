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
import com.google.android.gms.common.internal.service.Common;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.File;
import java.util.List;

import fifty.fiftyhouse.com.fifty.CommonData;
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
                CommonFunc.getInstance().GetPermissionForGalleryCamera(MyProfileEditActivity.this, CommonData.GET_PHOTO_FROM_CROP);
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
                    startActivityForResult(new Intent(getApplicationContext(), NickNameEditActivity.class), MyProfileEditMenuAdapter.MY_PROFILE_EDIT_MENU_NICKNAME_INDEX);
                }
                else if(position == MyProfileEditMenuAdapter.MY_PROFILE_EDIT_MENU_STORY_INDEX)
                {
                    startActivityForResult(new Intent(getApplicationContext(), MemoEditActivity.class), MyProfileEditMenuAdapter.MY_PROFILE_EDIT_MENU_STORY_INDEX);
                }
                else if(position == MyProfileEditMenuAdapter.MY_PROFILE_EDIT_MENU_LOC_INDEX)
                {
                    startActivityForResult(new Intent(getApplicationContext(), LocationEditActivity.class), MyProfileEditMenuAdapter.MY_PROFILE_EDIT_MENU_LOC_INDEX);
                }
                else if(position == MyProfileEditMenuAdapter.MY_PROFILE_EDIT_MENU_FAVORITE_INDEX)
                {
                    FirebaseManager.CheckFirebaseComplete listener = new FirebaseManager.CheckFirebaseComplete() {
                        @Override
                        public void CompleteListener() {
                            Intent intent = new Intent(getApplicationContext(), FavoriteSelectActivity.class);
                            intent.putExtra("Type",1);
                            startActivityForResult(intent, MyProfileEditMenuAdapter.MY_PROFILE_EDIT_MENU_FAVORITE_INDEX);
                        }

                        @Override
                        public void CompleteListener_Yes() {
                        }

                        @Override
                        public void CompleteListener_No() {
                        }
                    };

                    FirebaseManager.getInstance().GetPopFavoriteData(listener);
                }
            }

            @Override
            public void onLongItemClick(View view, int position) {
                //  Toast.makeText(getApplicationContext(),position+"번 째 아이템 롱 클릭",Toast.LENGTH_SHORT).show();
            }
        }));
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                DialogFunc.getInstance().ShowLoadingPage(MyProfileEditActivity.this);
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
                Uri resultUri = result.getUri();
                CommonFunc.getInstance().SetCropImage(mContext, resultUri, 0, iv_MyProfile_Edit_Profile, listener);
            }
        }
        else if(resultCode == MyProfileEditMenuAdapter.MY_PROFILE_EDIT_MENU_NICKNAME_INDEX ||
                resultCode == MyProfileEditMenuAdapter.MY_PROFILE_EDIT_MENU_FAVORITE_INDEX ||
                resultCode == MyProfileEditMenuAdapter.MY_PROFILE_EDIT_MENU_STORY_INDEX ||
                resultCode == MyProfileEditMenuAdapter.MY_PROFILE_EDIT_MENU_AGE_INDEX ||
                resultCode == MyProfileEditMenuAdapter.MY_PROFILE_EDIT_MENU_LOC_INDEX)
        {
            RefreshUI();
        }
    }

    private void RefreshUI()
    {
        mAdapter.notifyDataSetChanged();
    }
}
