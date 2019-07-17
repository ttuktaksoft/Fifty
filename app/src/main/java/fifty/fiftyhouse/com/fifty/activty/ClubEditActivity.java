package fifty.fiftyhouse.com.fifty.activty;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.theartofdev.edmodo.cropper.CropImage;

import java.io.FileNotFoundException;
import java.io.IOException;

import fifty.fiftyhouse.com.fifty.CommonData;
import fifty.fiftyhouse.com.fifty.CommonFunc;
import fifty.fiftyhouse.com.fifty.DialogFunc;
import fifty.fiftyhouse.com.fifty.Manager.FirebaseManager;
import fifty.fiftyhouse.com.fifty.Manager.TKManager;
import fifty.fiftyhouse.com.fifty.R;
import fifty.fiftyhouse.com.fifty.util.OnSingleClickListener;

public class ClubEditActivity extends AppCompatActivity {

    View ui_ClubEdit_TopBar;
    TextView tv_TopBar_Title;
    ImageView iv_TopBar_Back;

    EditText et_ClubEdit_Name;
    ImageView iv_ClubEdit_Profile;
    TextView tv_ClubEdit_OK;

    Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_club_edit);

        ui_ClubEdit_TopBar = findViewById(R.id.ui_ClubEdit_TopBar);
        tv_TopBar_Title = ui_ClubEdit_TopBar.findViewById(R.id.tv_TopBar_Title);
        iv_TopBar_Back = ui_ClubEdit_TopBar.findViewById(R.id.iv_TopBar_Back);
        et_ClubEdit_Name = findViewById(R.id.et_ClubEdit_Name);
        iv_ClubEdit_Profile = findViewById(R.id.iv_ClubEdit_Profile);
        tv_ClubEdit_OK = findViewById(R.id.tv_ClubEdit_OK);

        mContext = getApplicationContext();

        tv_TopBar_Title.setText(CommonFunc.getInstance().getStr(mContext.getResources(), R.string.TITLE_EDIT_CLUB));
        iv_TopBar_Back.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View view) {
                finish();
            }
        });
        RefreshOkButton();

        et_ClubEdit_Name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                RefreshOkButton();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        tv_ClubEdit_OK.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View view) {
                // 클럽 생성 가능한지 체크
                if (CommonFunc.getInstance().CheckStringNull(et_ClubEdit_Name.getText().toString()))
                    DialogFunc.getInstance().ShowMsgPopup(ClubEditActivity.this, CommonFunc.getInstance().getStr(getResources(), R.string.CREATE_CLUB_NAME_EMPTY));
                else {
                    // TODO  수정
                }
            }
        });

        iv_ClubEdit_Profile.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View view) {
                CommonFunc.getInstance().GetPermissionForGalleryCamera(ClubEditActivity.this, CommonData.GET_PHOTO_FROM_CROP);
            }
        });

        CommonFunc.getInstance().DrawImageByGlide(ClubEditActivity.this, iv_ClubEdit_Profile, TKManager.getInstance().TargetClubData.GetClubThumb(), false);
        et_ClubEdit_Name.setText(TKManager.getInstance().TargetClubData.GetClubName());
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == Activity.RESULT_OK) {
                RefreshOkButton();
                DialogFunc.getInstance().ShowLoadingPage(ClubEditActivity.this);
            }
        }
    }

    public void RefreshOkButton() {

        boolean enable = CommonFunc.getInstance().CheckStringNull(et_ClubEdit_Name.getText().toString()) == false;

        int selectBGColor = ContextCompat.getColor(mContext, R.color.button_enable);
        int selectSrtColor = ContextCompat.getColor(mContext, R.color.button_enable_str);
        int disableBGColor = ContextCompat.getColor(mContext, R.color.button_disable);
        int disableSrtColor = ContextCompat.getColor(mContext, R.color.button_disable_str);

        tv_ClubEdit_OK.setBackgroundColor(enable ? selectBGColor : disableBGColor);
        tv_ClubEdit_OK.setTextColor(enable ? selectSrtColor : disableSrtColor);
    }
}