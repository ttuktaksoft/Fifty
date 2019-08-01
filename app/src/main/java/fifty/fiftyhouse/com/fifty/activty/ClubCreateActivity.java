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
import fifty.fiftyhouse.com.fifty.DataBase.ChatData;
import fifty.fiftyhouse.com.fifty.DialogFunc;
import fifty.fiftyhouse.com.fifty.Manager.FirebaseManager;
import fifty.fiftyhouse.com.fifty.Manager.TKManager;
import fifty.fiftyhouse.com.fifty.R;
import fifty.fiftyhouse.com.fifty.fragment.SignUpFragment;
import fifty.fiftyhouse.com.fifty.util.OnSingleClickListener;

public class ClubCreateActivity extends AppCompatActivity {

    View ui_ClubCreate_TopBar;
    TextView tv_TopBar_Title;
    ImageView iv_TopBar_Back;

    EditText et_ClubCreate_Name;
    ImageView iv_ClubCreate_Profile;
    Switch sw_ClubCreate_VIP;
    TextView tv_ClubCreate_OK;

    Context mContext;
    boolean isProfileUpload = false;
    boolean isVIPType = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_club_create);

        ui_ClubCreate_TopBar = findViewById(R.id.ui_ClubCreate_TopBar);
        tv_TopBar_Title = ui_ClubCreate_TopBar.findViewById(R.id.tv_TopBar_Title);
        iv_TopBar_Back = ui_ClubCreate_TopBar.findViewById(R.id.iv_TopBar_Back);
        et_ClubCreate_Name = findViewById(R.id.et_ClubCreate_Name);
        iv_ClubCreate_Profile = findViewById(R.id.iv_ClubCreate_Profile);
        sw_ClubCreate_VIP = findViewById(R.id.sw_ClubCreate_VIP);
        tv_ClubCreate_OK = findViewById(R.id.tv_ClubCreate_OK);

        mContext = getApplicationContext();
        isProfileUpload = false;

        tv_TopBar_Title.setText(CommonFunc.getInstance().getStr(mContext.getResources(), R.string.TITLE_CREAT_CLUB));
        iv_TopBar_Back.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View view) {
                Intent intent = new Intent(mContext, FavoriteSelectActivity.class);
                intent.putExtra("Type",2);
                startActivity(intent);
            }
        });


        RefreshOkButton();

        et_ClubCreate_Name.addTextChangedListener(new TextWatcher() {
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

        tv_ClubCreate_OK.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View view) {
                // 클럽 생성 가능한지 체크
                if(CommonFunc.getInstance().CheckStringNull(et_ClubCreate_Name.getText().toString()))
                    DialogFunc.getInstance().ShowMsgPopup(ClubCreateActivity.this, CommonFunc.getInstance().getStr(getResources(), R.string.CREATE_CLUB_NAME_EMPTY));
                else if(isProfileUpload == false)
                    DialogFunc.getInstance().ShowMsgPopup(ClubCreateActivity.this, CommonFunc.getInstance().getStr(getResources(), R.string.CREATE_CLUB_NAME_PROFILE));
                else
                {
                    DialogFunc.getInstance().ShowLoadingPage(ClubCreateActivity.this);
                    TKManager.getInstance().CreateTempClubData.SetClubMasterIndex(TKManager.getInstance().MyData.GetUserIndex());
                    TKManager.getInstance().CreateTempClubData.SetClubName(et_ClubCreate_Name.getText().toString());
                    TKManager.getInstance().CreateTempClubData.SetClubThumb(TKManager.getInstance().CreateTempClubData.GetClubThumb());
                    TKManager.getInstance().CreateTempClubData.SetClubType(isVIPType);
                    TKManager.getInstance().CreateTempClubData.AddClubMember(TKManager.getInstance().MyData.GetUserIndex());
                    TKManager.getInstance().CreateTempClubData.ClubMemberCount = 1;
                    TKManager.getInstance().CreateTempClubData.SetClubCreateDate(Long.parseLong(CommonFunc.getInstance().GetCurrentTime()));
                    TKManager.getInstance().CreateTempClubData.SetClubComment(et_ClubCreate_Name.getText().toString() + "입니다");


                    FirebaseManager.CheckFirebaseComplete registClubListener = new FirebaseManager.CheckFirebaseComplete() {
                        @Override
                        public void CompleteListener() {
                            TKManager.getInstance().MyData.SetUserClubData(TKManager.getInstance().CreateTempClubData.ClubIndex, TKManager.getInstance().CreateTempClubData);
                            TKManager.getInstance().ClubData_Simple.put(TKManager.getInstance().CreateTempClubData.GetClubIndex(), TKManager.getInstance().CreateTempClubData);
                            TKManager.getInstance().mUpdateClubFragmentFunc.UpdateUI();
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

                    FirebaseManager.getInstance().RegistClubList(TKManager.getInstance().CreateTempClubData, registClubListener);

                    ChatData tempChatData = new ChatData();
                    tempChatData.SetRoomIndex(TKManager.getInstance().CreateTempClubData.GetClubIndex());

                    tempChatData.SetFromIndex(TKManager.getInstance().MyData.GetUserIndex());
                    tempChatData.SetFromNickName(TKManager.getInstance().MyData.GetUserNickName());
                    tempChatData.SetFromThumbNail(TKManager.getInstance().MyData.GetUserImgThumb());

                    tempChatData.SetToIndex("club");
                    tempChatData.SetToNickName(TKManager.getInstance().CreateTempClubData.GetClubName());
                    tempChatData.SetToThumbNail("club");

                    tempChatData.SetMsgIndex(0);
                    tempChatData.SetMsgReadCheck(false);
                    tempChatData.SetMsgDate(Long.parseLong(CommonFunc.getInstance().GetCurrentTime()));
                    tempChatData.SetMsgType(CommonData.MSGType.MSG);
                    tempChatData.SetMsgSender(TKManager.getInstance().MyData.GetUserIndex());
                    tempChatData.SetMsg(TKManager.getInstance().CreateTempClubData.GetClubName() + "클럽 채팅방입니다");
                    tempChatData.SetRoomType(CommonData.CHAT_ROOM_TYPE.DEFAULT);

                    FirebaseManager.getInstance().RegistClubChatList(TKManager.getInstance().CreateTempClubData.GetClubIndex(), tempChatData, true);
                }
            }
        });

        iv_ClubCreate_Profile.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View view) {
                CommonFunc.getInstance().GetPermissionForGalleryCamera(ClubCreateActivity.this, CommonData.GET_PHOTO_FROM_CROP);
            }
        });

        sw_ClubCreate_VIP.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if(TKManager.getInstance().MyData.GetUserVip())
                {
                    isVIPType = isChecked;
                }
                else
                {
                    // VIP 상태 확인하고 유료 상점으로 이동 시켜야함
                    isVIPType = false;
                }

            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == Activity.RESULT_OK) {
                isProfileUpload = true;
                RefreshOkButton();
                DialogFunc.getInstance().ShowLoadingPage(ClubCreateActivity.this);

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

                CommonFunc.getInstance().DrawImageByGlide(mContext, iv_ClubCreate_Profile, originalBm, false);

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
                FirebaseManager.getInstance().UploadClubThumbImg(TKManager.getInstance().CreateTempClubData.GetClubIndex(), originalBm, TKManager.getInstance().CreateTempClubData, listener);
            }
        }
    }

    public void RefreshOkButton() {

        boolean enable = CommonFunc.getInstance().CheckStringNull(et_ClubCreate_Name.getText().toString()) == false&& isProfileUpload;

        int selectBGColor = ContextCompat.getColor(mContext, R.color.button_enable);
        int selectSrtColor = ContextCompat.getColor(mContext, R.color.button_enable_str);
        int disableBGColor = ContextCompat.getColor(mContext, R.color.button_disable);
        int disableSrtColor = ContextCompat.getColor(mContext, R.color.button_disable_str);

        tv_ClubCreate_OK.setBackgroundColor(enable ? selectBGColor : disableBGColor);
        tv_ClubCreate_OK.setTextColor(enable ? selectSrtColor : disableSrtColor);
    }
}
