package fifty.fiftyhouse.com.fifty.activty.SignUp;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.beloo.widget.chipslayoutmanager.ChipsLayoutManager;
import com.beloo.widget.chipslayoutmanager.gravity.IChildGravityResolver;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import fifty.fiftyhouse.com.fifty.CommonData;
import fifty.fiftyhouse.com.fifty.CommonFunc;
import fifty.fiftyhouse.com.fifty.DialogFunc;
import fifty.fiftyhouse.com.fifty.Manager.FirebaseManager;
import fifty.fiftyhouse.com.fifty.Manager.TKManager;
import fifty.fiftyhouse.com.fifty.R;
import fifty.fiftyhouse.com.fifty.activty.FavoriteSelectActivity;
import fifty.fiftyhouse.com.fifty.adapter.FavoriteViewAdapter;
import fifty.fiftyhouse.com.fifty.util.ImageResize;
import fifty.fiftyhouse.com.fifty.util.RecyclerItemClickListener;

// 닉네임 입니다
public class SignUpActivity extends AppCompatActivity {

    View ui_SignUp_TopBar;
    TextView tv_TopBar_Title;
    ImageView iv_TopBar_Back;
    ImageView iv_SignUp_Profile, iv_SignUp_Gender;
    EditText et_SignUp_NickName;
    TextView tv_SignUp_Save, tv_SignUp_NickName_Check, tv_SignUp_NickName_Check_Result, tv_SignUp_Gender, tv_SignUp_Age;
    RecyclerView rv_SignUp_Favorite;

    Context mContext;
    static final int GET_FROM_GALLERY = 1;
    File tempFile;
    boolean isCamera = false;
    boolean isProfileUpload = false;
    boolean mIsCheckNickName = false;
    InputMethodManager imm;

    FavoriteViewAdapter mFavoriteAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        mContext = getApplicationContext();
        imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        CommonFunc.getInstance().mCurActivity = this;

        isProfileUpload = false;
        mIsCheckNickName = false;

        ui_SignUp_TopBar = findViewById(R.id.ui_SignUp_TopBar);
        tv_TopBar_Title = ui_SignUp_TopBar.findViewById(R.id.tv_TopBar_Title);
        iv_TopBar_Back = ui_SignUp_TopBar.findViewById(R.id.iv_TopBar_Back);
        tv_SignUp_Save = findViewById(R.id.tv_SignUp_Save);
        iv_SignUp_Profile = findViewById(R.id.iv_SignUp_Profile);
        iv_SignUp_Gender = findViewById(R.id.iv_SignUp_Gender);
        tv_SignUp_NickName_Check = findViewById(R.id.tv_SignUp_NickName_Check);
        et_SignUp_NickName = findViewById(R.id.et_SignUp_NickName);
        tv_SignUp_NickName_Check_Result = findViewById(R.id.tv_SignUp_NickName_Check_Result);
        tv_SignUp_Gender = findViewById(R.id.tv_SignUp_Gender);
        tv_SignUp_Age = findViewById(R.id.tv_SignUp_Age);
        rv_SignUp_Favorite = findViewById(R.id.rv_SignUp_Favorite);

        iv_TopBar_Back.setVisibility(View.GONE);
        tv_TopBar_Title.setText(CommonFunc.getInstance().getStr(mContext.getResources(), R.string.TITLE_SIGNUP));

        iv_SignUp_Profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GetPermission();
            }
        });

        if (true) {
            Glide.with(mContext).load(R.drawable.ic_man_simple)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(iv_SignUp_Gender);
            tv_SignUp_Gender.setText(CommonFunc.getInstance().getStr(mContext.getResources(), R.string.MSG_MAN));
            TKManager.getInstance().MyData.SetUserGender(0);
        } else {
            Glide.with(mContext).load(R.drawable.ic_woman_simple)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(iv_SignUp_Gender);
            tv_SignUp_Gender.setText(CommonFunc.getInstance().getStr(mContext.getResources(), R.string.MSG_WOMAN));
            TKManager.getInstance().MyData.SetUserGender(1);
        }

        tv_SignUp_Age.setText("입력해주세요");

        CommonFunc.getInstance().setEditTextMaxSize(et_SignUp_NickName, CommonData.NickNameMaxSize);

        tv_SignUp_NickName_Check_Result.setText(CommonFunc.getInstance().getStr(mContext.getResources(), R.string.NICKNAME_CHECK_NO));
        tv_SignUp_NickName_Check_Result.setTextColor(ContextCompat.getColor(mContext, R.color.red));

        et_SignUp_NickName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 입력을 시도 했을경우 중복체크 안함으로 수정
                mIsCheckNickName = false;
                tv_SignUp_NickName_Check_Result.setText(CommonFunc.getInstance().getStr(mContext.getResources(), R.string.NICKNAME_CHECK_NO));
                tv_SignUp_NickName_Check_Result.setTextColor(ContextCompat.getColor(mContext, R.color.red));
            }
        });

        tv_SignUp_NickName_Check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String strNickName = et_SignUp_NickName.getText().toString();

                if(CommonFunc.getInstance().CheckStringNull(strNickName))
                {
                    DialogFunc.getInstance().ShowMsgPopup(SignUpActivity.this, CommonFunc.getInstance().getStr(getResources(), R.string.MSG_NOTICE), CommonFunc.getInstance().getStr(getResources(), R.string.NICKNAME_EMPTY));
                }
                else if(strNickName.length() < CommonData.NickNameMinSize)
                {
                    DialogFunc.getInstance().ShowMsgPopup(SignUpActivity.this, CommonFunc.getInstance().getStr(getResources(), R.string.MSG_NOTICE), CommonFunc.getInstance().getStr(getResources(), R.string.NICKNAME_LEAK));
                }
                else
                {
                    imm.hideSoftInputFromWindow(et_SignUp_NickName.getWindowToken(), 0);
                    FirebaseManager.CheckFirebaseComplete listener = new FirebaseManager.CheckFirebaseComplete() {
                        @Override
                        public void CompleteListener() {

                        }

                        @Override
                        public void CompleteListener_Yes() {
                            DialogFunc.getInstance().ShowMsgPopup(SignUpActivity.this, CommonFunc.getInstance().getStr(getResources(), R.string.MSG_NOTICE), CommonFunc.getInstance().getStr(getResources(), R.string.NICKNAME_CHECK_SUCCESS));
                            mIsCheckNickName = true;
                            tv_SignUp_NickName_Check_Result.setText(CommonFunc.getInstance().getStr(mContext.getResources(), R.string.NICKNAME_CHECK_YES));
                            tv_SignUp_NickName_Check_Result.setTextColor(ContextCompat.getColor(mContext, R.color.blue));
                        }

                        @Override
                        public void CompleteListener_No() {
                            DialogFunc.getInstance().ShowMsgPopup(SignUpActivity.this, CommonFunc.getInstance().getStr(getResources(), R.string.MSG_NOTICE), CommonFunc.getInstance().getStr(getResources(), R.string.NICKNAME_CHECK_FAIL));
                            mIsCheckNickName = false;
                            tv_SignUp_NickName_Check_Result.setText(CommonFunc.getInstance().getStr(mContext.getResources(), R.string.NICKNAME_CHECK_NO));
                            tv_SignUp_NickName_Check_Result.setTextColor(ContextCompat.getColor(mContext, R.color.red));
                        }
                    };

                    FirebaseManager.getInstance().CheckNickName(strNickName, listener);
                }
            }
        });

        tv_SignUp_Save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mIsCheckNickName == false)
                {
                    // TODO 중복체크 요청 팝업
                    DialogFunc.getInstance().ShowMsgPopup(SignUpActivity.this, CommonFunc.getInstance().getStr(getResources(), R.string.MSG_NOTICE), CommonFunc.getInstance().getStr(getResources(), R.string.NICKNAME_CHECK_ASK));
                }
                else if(isProfileUpload == false)
                {
                    DialogFunc.getInstance().ShowMsgPopup(SignUpActivity.this, CommonFunc.getInstance().getStr(getResources(), R.string.MSG_NOTICE), CommonFunc.getInstance().getStr(getResources(), R.string.PROFILE_EMPTY));
                }
                else
                {
                    Intent intent = new Intent(getApplicationContext(), SignUpCompleteActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });

        initFavoriteList();
    }

    public void initFavoriteList()
    {
        ArrayList<String> list = new ArrayList<>();
        list.add("+");

        mFavoriteAdapter = new FavoriteViewAdapter(mContext);
        mFavoriteAdapter.setItemCount(list.size());
        mFavoriteAdapter.setItemData(list);
        mFavoriteAdapter.setHasStableIds(true);

        rv_SignUp_Favorite.setAdapter(mFavoriteAdapter);
        ChipsLayoutManager chipsLayoutManager = ChipsLayoutManager.newBuilder(mContext)
                .setChildGravity(Gravity.CENTER)
                .setMaxViewsInRow(3)
                .setGravityResolver(new IChildGravityResolver() {
                    @Override
                    public int getItemGravity(int i) {
                        return Gravity.CENTER;
                    }
                })
                .setOrientation(ChipsLayoutManager.HORIZONTAL)
                .setRowStrategy(ChipsLayoutManager.STRATEGY_CENTER_DENSE)
                .withLastRow(true)
                .build();
        rv_SignUp_Favorite.setLayoutManager(chipsLayoutManager);
        rv_SignUp_Favorite.addOnItemTouchListener(new RecyclerItemClickListener(mContext, rv_SignUp_Favorite, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

                if(position == 0) // 추가 해야 하는 놈
                {
                    FirebaseManager.CheckFirebaseComplete listener = new FirebaseManager.CheckFirebaseComplete() {
                        @Override
                        public void CompleteListener() {
                            Intent intent = new Intent(getApplicationContext(), FavoriteSelectActivity.class);
                            startActivity(intent);
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

    private void GetPhotoInGallery() {

        isCamera = true;
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
        startActivityForResult(intent, GET_FROM_GALLERY);
    }

    private void setImage() {

        DialogFunc.getInstance().ShowLoadingPage(SignUpActivity.this);

        ImageResize.resizeFile(tempFile, tempFile, 1280, isCamera);

        BitmapFactory.Options options = new BitmapFactory.Options();
        Bitmap originalBm = BitmapFactory.decodeFile(tempFile.getAbsolutePath(), options);
        iv_SignUp_Profile.setImageBitmap(originalBm);
        Glide.with(this).load(originalBm)
                .centerCrop()
                .circleCrop()
                .into(iv_SignUp_Profile);

        FirebaseManager.CheckFirebaseComplete listener = new FirebaseManager.CheckFirebaseComplete() {
            @Override
            public void CompleteListener() {
                isProfileUpload = true;
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
