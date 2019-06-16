package fifty.fiftyhouse.com.fifty.activty;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.app.FragmentManager;
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
import fifty.fiftyhouse.com.fifty.fragment.MyProfileEditFragment;
import fifty.fiftyhouse.com.fifty.fragment.UserProfileFragment;
import fifty.fiftyhouse.com.fifty.util.ImageResize;
import fifty.fiftyhouse.com.fifty.util.RecyclerItemClickListener;

public class MyProfileEditActivity extends AppCompatActivity {

    View ui_MyProfile_Edit_TopBar;
    TextView tv_TopBar_Title;
    ImageView iv_TopBar_Back;

    Context mContext;
    Activity mActivity;
    FragmentManager mFragmentMgr;

    MyProfileEditFragment mMyProfileEditFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile_edit);

        mActivity = this;
        mContext = getApplicationContext();
        mFragmentMgr = getSupportFragmentManager();
        // mFragmentMgr.beginTransaction().addToBackStack(null);

        mMyProfileEditFragment = new MyProfileEditFragment();
        CommonFunc.getInstance().mCurActivity = this;
        mFragmentMgr.beginTransaction().replace(R.id.fl_MyProfile_Edit_FrameLayout, mMyProfileEditFragment, "MyProfileEditFragmentFragment").commit();


        ui_MyProfile_Edit_TopBar = findViewById(R.id.ui_MyProfile_Edit_TopBar);
        tv_TopBar_Title = ui_MyProfile_Edit_TopBar.findViewById(R.id.tv_TopBar_Title);
        iv_TopBar_Back = ui_MyProfile_Edit_TopBar.findViewById(R.id.iv_TopBar_Back);
        tv_TopBar_Title.setText(CommonFunc.getInstance().getStr(mContext.getResources(), R.string.TITLE_PROFILE_EDIT));
        iv_TopBar_Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}
