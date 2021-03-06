package fifty.fiftyhouse.com.fifty.activty;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.beloo.widget.chipslayoutmanager.ChipsLayoutManager;
import com.beloo.widget.chipslayoutmanager.gravity.IChildGravityResolver;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import fifty.fiftyhouse.com.fifty.CommonData;
import fifty.fiftyhouse.com.fifty.CommonFunc;
import fifty.fiftyhouse.com.fifty.DataBase.ChatData;
import fifty.fiftyhouse.com.fifty.DialogFunc;
import fifty.fiftyhouse.com.fifty.Manager.FirebaseManager;
import fifty.fiftyhouse.com.fifty.Manager.TKManager;
import fifty.fiftyhouse.com.fifty.R;
import fifty.fiftyhouse.com.fifty.adapter.FavoriteViewAdapter;
import fifty.fiftyhouse.com.fifty.util.OnRecyclerItemClickListener;
import fifty.fiftyhouse.com.fifty.util.OnSingleClickListener;
import fifty.fiftyhouse.com.fifty.util.RecyclerItemClickListener;

public class ClubCreateActivity extends AppCompatActivity {

    View ui_ClubCreate_TopBar;
    TextView tv_TopBar_Title;
    ImageView iv_TopBar_Back;

    EditText et_ClubCreate_Name, et_ClubCreate_Introduce;
    ImageView iv_ClubCreate_Profile;
    RecyclerView rv_ClubCreate_Favorite;
    TextView tv_ClubCreate_Favorite_Title, tv_ClubCreate_JoinType_Title;
    TextView tv_ClubCreate_JoinType_Free, tv_ClubCreate_JoinType_Approval;
    Spinner s_ClubCreate_JoinCount;
    TextView tv_ClubCreate_OK;

    FavoriteViewAdapter mFavoriteAdapter;
    Context mContext;
    InputMethodManager imm;
    boolean isProfileUpload = false;
    boolean isJoinType = true;

    int GET_FROM_FAVORITE_SELECT = 1000;
    public int CLUB_CREATE_TYPE = 0;
    public int CLUB_EDIT_TYPE = 1;
    int mClubCreateType = CLUB_CREATE_TYPE;

    int mClubMaxMember = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_club_create);

        ui_ClubCreate_TopBar = findViewById(R.id.ui_ClubCreate_TopBar);
        tv_TopBar_Title = ui_ClubCreate_TopBar.findViewById(R.id.tv_TopBar_Title);
        iv_TopBar_Back = ui_ClubCreate_TopBar.findViewById(R.id.iv_TopBar_Back);
        et_ClubCreate_Name = findViewById(R.id.et_ClubCreate_Name);
        et_ClubCreate_Introduce = findViewById(R.id.et_ClubCreate_Introduce);
        iv_ClubCreate_Profile = findViewById(R.id.iv_ClubCreate_Profile);
        rv_ClubCreate_Favorite = findViewById(R.id.rv_ClubCreate_Favorite);

        tv_ClubCreate_Favorite_Title = findViewById(R.id.tv_ClubCreate_Favorite_Title);
        tv_ClubCreate_JoinType_Title = findViewById(R.id.tv_ClubCreate_JoinType_Title);
        tv_ClubCreate_JoinType_Free = findViewById(R.id.tv_ClubCreate_JoinType_Free);
        tv_ClubCreate_JoinType_Approval = findViewById(R.id.tv_ClubCreate_JoinType_Approval);
        s_ClubCreate_JoinCount = findViewById(R.id.s_ClubCreate_JoinCount);
        tv_ClubCreate_OK = findViewById(R.id.tv_ClubCreate_OK);

        imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        mContext = getApplicationContext();

        iv_TopBar_Back.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View view) {
                finish();
            }
        });

        int nType = getIntent().getIntExtra("Type", 0);

        switch (nType) {
            case 0:
                mClubCreateType = CLUB_CREATE_TYPE;
                break;
            case 1:
                mClubCreateType = CLUB_EDIT_TYPE;
                break;
        }

        if(mClubCreateType == CLUB_CREATE_TYPE)
        {
            tv_ClubCreate_Favorite_Title.setVisibility(View.VISIBLE);
            rv_ClubCreate_Favorite.setVisibility(View.VISIBLE);
            tv_ClubCreate_JoinType_Title.setVisibility(View.VISIBLE);
            tv_ClubCreate_JoinType_Free.setVisibility(View.VISIBLE);
            tv_ClubCreate_JoinType_Approval.setVisibility(View.VISIBLE);

            tv_ClubCreate_OK.setText(CommonFunc.getInstance().getStr(getResources(), R.string.MSG_CLUB_CREATE_OK));

            mClubMaxMember = CommonData.ClubUserCountMaxSize;
        }
        else
        {
            tv_ClubCreate_Favorite_Title.setVisibility(View.GONE);
            rv_ClubCreate_Favorite.setVisibility(View.GONE);
            tv_ClubCreate_JoinType_Title.setVisibility(View.GONE);
            tv_ClubCreate_JoinType_Free.setVisibility(View.GONE);
            tv_ClubCreate_JoinType_Approval.setVisibility(View.GONE);

            tv_ClubCreate_OK.setText(CommonFunc.getInstance().getStr(getResources(), R.string.MSG_CLUB_CREATE_EDIT));
            mClubMaxMember = TKManager.getInstance().TargetClubData.GetClubMaxMember();
        }


        CommonFunc.getInstance().setEditTextMaxSize(et_ClubCreate_Name, CommonData.ClubNameMaxSize);
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

        et_ClubCreate_Name.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                switch (actionId) {
                    case EditorInfo.IME_ACTION_DONE:
                        // 검색 동작
                        imm.hideSoftInputFromWindow(et_ClubCreate_Name.getWindowToken(), 0);
                        break;
                    default:
                        // 기본 엔터키 동작
                        imm.hideSoftInputFromWindow(et_ClubCreate_Name.getWindowToken(), 0);
                        return false;
                }
                return true;
            }
        });

        tv_ClubCreate_OK.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View view) {
                imm.hideSoftInputFromWindow(et_ClubCreate_Name.getWindowToken(), 0);
                imm.hideSoftInputFromWindow(et_ClubCreate_Introduce.getWindowToken(), 0);

                // 클럽 생성 가능한지 체크
                if(CommonFunc.getInstance().CheckStringNull(et_ClubCreate_Name.getText().toString()))
                    DialogFunc.getInstance().ShowMsgPopup(ClubCreateActivity.this, CommonFunc.getInstance().getStr(getResources(), R.string.MSG_CLUB_CREATE_NAME_EMPTY));
                else if(et_ClubCreate_Name.getText().length() < CommonData.ClubNameMinSize)
                {
                    DialogFunc.getInstance().ShowMsgPopup(ClubCreateActivity.this, CommonFunc.getInstance().getStr(getResources(), R.string.MSG_CLUB_NAME_LEAK));
                }
                else if(isProfileUpload == false)
                    DialogFunc.getInstance().ShowMsgPopup(ClubCreateActivity.this, CommonFunc.getInstance().getStr(getResources(), R.string.MSG_CLUB_CREATE_NAME_PROFILE));
                else if(mClubCreateType == CLUB_CREATE_TYPE && TKManager.getInstance().CreateTempClubData.ClubFavorite.size() < CommonData.ClubFavoriteSelectMinCount)
                    DialogFunc.getInstance().ShowMsgPopup(ClubCreateActivity.this, CommonFunc.getInstance().getStr(getResources(), R.string.MSG_FAVORITE_SELECT_LACK_1));
                else
                {
                    if(mClubCreateType == CLUB_CREATE_TYPE)
                    {
                        DialogFunc.getInstance().ShowLoadingPage(ClubCreateActivity.this);
                        TKManager.getInstance().CreateTempClubData.SetClubMasterIndex(TKManager.getInstance().MyData.GetUserIndex());
                        TKManager.getInstance().CreateTempClubData.SetClubName(et_ClubCreate_Name.getText().toString());
                        TKManager.getInstance().CreateTempClubData.SetClubThumb(TKManager.getInstance().CreateTempClubData.GetClubThumb());
                        TKManager.getInstance().CreateTempClubData.SetClubType(isJoinType);
                        TKManager.getInstance().CreateTempClubData.AddClubMember(TKManager.getInstance().MyData.GetUserIndex());
                        TKManager.getInstance().CreateTempClubData.ClubMemberCount = 1;
                        TKManager.getInstance().CreateTempClubData.SetClubCreateDate(Long.parseLong(CommonFunc.getInstance().GetCurrentTime()));
                        TKManager.getInstance().CreateTempClubData.SetClubComment(et_ClubCreate_Name.getText().toString() + "입니다");
                        TKManager.getInstance().CreateTempClubData.SetClubMaxMember(mClubMaxMember);

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
                        tempChatData.SetMsgDate(Long.parseLong(CommonFunc.getInstance().GetCurrentTime()));
                        tempChatData.SetMsgType(CommonData.MSGType.MSG);
                        tempChatData.SetMsgSender(TKManager.getInstance().MyData.GetUserIndex());
                        tempChatData.SetMsg(TKManager.getInstance().CreateTempClubData.GetClubName() + "클럽 채팅방입니다");
                        tempChatData.SetRoomType(CommonData.CHAT_ROOM_TYPE.DEFAULT);

                        FirebaseManager.getInstance().RegistClubChatList(TKManager.getInstance().CreateTempClubData.GetClubIndex(), tempChatData, true);

                    }
                    else
                    {
                        if(TKManager.getInstance().TargetClubData.GetClubMemberCount() > mClubMaxMember)
                        {
                            DialogFunc.getInstance().ShowMsgPopup(ClubCreateActivity.this, CommonFunc.getInstance().getStr(getResources(), R.string.MSG_CLUB_EDIT_JOIN_OVER_DESC));
                        }
                        else
                        {
                            DialogFunc.getInstance().ShowLoadingPage(ClubCreateActivity.this);
                            TKManager.getInstance().TargetClubData.SetClubName(et_ClubCreate_Name.getText().toString());
                            TKManager.getInstance().TargetClubData.SetClubThumb(TKManager.getInstance().TargetClubData.GetClubThumb());
                            TKManager.getInstance().TargetClubData.SetClubComment(et_ClubCreate_Introduce.getText().toString());
                            TKManager.getInstance().TargetClubData.SetClubMaxMember(mClubMaxMember);

                            FirebaseManager.CheckFirebaseComplete registClubListener = new FirebaseManager.CheckFirebaseComplete() {
                                @Override
                                public void CompleteListener() {
                                    TKManager.getInstance().MyData.SetUserClubData(TKManager.getInstance().TargetClubData.ClubIndex, TKManager.getInstance().TargetClubData);
                                    TKManager.getInstance().ClubData_Simple.put(TKManager.getInstance().TargetClubData.GetClubIndex(), TKManager.getInstance().TargetClubData);
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

                            FirebaseManager.getInstance().EditClubList(TKManager.getInstance().TargetClubData, registClubListener);
                        }
                    }
                }
            }
        });

        iv_ClubCreate_Profile.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View view) {

                CommonFunc.PhotoSelectListener selectListener = new CommonFunc.PhotoSelectListener()
                {
                    @Override
                    public void Listener(List<Uri> list)
                    {
                        if(list.size() <= 0)
                            return;

                        Bitmap[] originalBm = new Bitmap[list.size()];

                        CropImage.activity(list.get(0))
                                .setActivityTitle(CommonFunc.getInstance().getStr(mContext.getResources(), R.string.MSG_PHOTO_SELECT))
                                .setGuidelines(CropImageView.Guidelines.ON)
                                .setInitialCropWindowPaddingRatio(0)
                                .start(ClubCreateActivity.this);
                    }
                };


                CommonFunc.getInstance().GetPermissionForGalleryCamera(ClubCreateActivity.this, selectListener, false);
            }
        });

        tv_ClubCreate_JoinType_Free.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View view) {

                if(mClubCreateType == CLUB_EDIT_TYPE)
                    return;

                isJoinType = true;
                RefreshJoinType();
            }
        });

        tv_ClubCreate_JoinType_Approval.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View view) {

                if(mClubCreateType == CLUB_EDIT_TYPE)
                    return;

                isJoinType = false;
                RefreshJoinType();
            }
        });

        ArrayList<String> arrayList = new ArrayList<>();
        for (int i = CommonData.ClubUserCountMinSize ; i <= CommonData.ClubUserCountMaxSize ; i++)
        {
            arrayList.add(i + " 명");
        }

        ArrayAdapter arrayAdapter = new ArrayAdapter<>(getApplicationContext(),
                R.layout.ui_club_count_spinner,
                arrayList);

        s_ClubCreate_JoinCount.setAdapter(arrayAdapter);
        s_ClubCreate_JoinCount.setSelection(mClubMaxMember - CommonData.ClubUserCountMinSize);
        s_ClubCreate_JoinCount.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                mClubMaxMember = CommonData.ClubUserCountMinSize + i;
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        initFavoriteList();
        RefreshClubInfo();

    }

    public void initFavoriteList()
    {
        mFavoriteAdapter = new FavoriteViewAdapter(mContext);
        RefreshFavoriteViewListSlot();
        mFavoriteAdapter.setHasStableIds(true);

        rv_ClubCreate_Favorite.setAdapter(mFavoriteAdapter);
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
        rv_ClubCreate_Favorite.setLayoutManager(chipsLayoutManager);
        rv_ClubCreate_Favorite.addOnItemTouchListener(new RecyclerItemClickListener(mContext, rv_ClubCreate_Favorite, new OnRecyclerItemClickListener() {
            @Override
            public void onSingleClick(View view, int position) {

                if(mClubCreateType == CLUB_EDIT_TYPE)
                    return;

                DialogFunc.getInstance().ShowLoadingPage(ClubCreateActivity.this);
                FirebaseManager.CheckFirebaseComplete listener = new FirebaseManager.CheckFirebaseComplete() {
                    @Override
                    public void CompleteListener() {
                        DialogFunc.getInstance().DismissLoadingPage();
                        if(mClubCreateType == CLUB_CREATE_TYPE)
                        {
                            Intent intent = new Intent(mContext, FavoriteSelectActivity.class);
                            intent.putExtra("Type",2);
                            startActivityForResult(intent, GET_FROM_FAVORITE_SELECT);
                        }
                        else if(mClubCreateType == CLUB_EDIT_TYPE)
                        {
                            Intent intent = new Intent(mContext, FavoriteSelectActivity.class);
                            intent.putExtra("Type",3);
                            startActivityForResult(intent, GET_FROM_FAVORITE_SELECT);
                        }
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

            @Override
            public void onLongItemClick(View view, int position) {
            }
        }));
    }

    private void RefreshFavoriteViewListSlot()
    {
        ArrayList<String> list = new ArrayList<>();
        Map<String, String> tempMapFavorite = new LinkedHashMap<String, String>();

        if(mClubCreateType == CLUB_CREATE_TYPE)
        {
            tempMapFavorite = TKManager.getInstance().CreateTempClubData.GetClubFavoriteList();
        }
        else if(mClubCreateType == CLUB_EDIT_TYPE)
        {
            tempMapFavorite = TKManager.getInstance().TargetClubData.GetClubFavoriteList();
        }

        Set EntrySet = tempMapFavorite.entrySet();
        Iterator iterator = EntrySet.iterator();

        while(iterator.hasNext()){
            Map.Entry entry = (Map.Entry)iterator.next();
            String key = (String)entry.getKey();
            String value = (String)entry.getValue();
            list.add(value);
        }
        if(mClubCreateType == CLUB_CREATE_TYPE)
        {
            mFavoriteAdapter.addSlot(CommonFunc.getInstance().getStr(mContext.getResources(), R.string.MSG_FAVORITE_PLUS));
        }

        mFavoriteAdapter.setItemCount(list.size());
        mFavoriteAdapter.setItemData(list);
        mFavoriteAdapter.notifyDataSetChanged();
    }

    public void RefreshClubInfo()
    {
        if(mClubCreateType == CLUB_CREATE_TYPE)
        {
            isProfileUpload = false;
            isJoinType = true;

            tv_TopBar_Title.setText(CommonFunc.getInstance().getStr(mContext.getResources(), R.string.TITLE_CREAT_CLUB));
        }
        else if(mClubCreateType == CLUB_EDIT_TYPE)
        {
            isProfileUpload = true;
            isJoinType = TKManager.getInstance().TargetClubData.GetClubType();

            tv_TopBar_Title.setText(CommonFunc.getInstance().getStr(mContext.getResources(), R.string.TITLE_EDIT_CLUB));
            et_ClubCreate_Name.setText(TKManager.getInstance().TargetClubData.GetClubName());
            et_ClubCreate_Introduce.setText(TKManager.getInstance().TargetClubData.GetClubComment());
            CommonFunc.getInstance().DrawImageByGlide(mContext, iv_ClubCreate_Profile, TKManager.getInstance().TargetClubData.GetClubThumb(), false);
        }

        RefreshOkButton();
        RefreshJoinType();
    }


    public void RefreshOkButton() {

        boolean enable = CommonFunc.getInstance().CheckStringNull(et_ClubCreate_Name.getText().toString()) == false &&
                isProfileUpload &&
                ((mClubCreateType == CLUB_CREATE_TYPE && TKManager.getInstance().CreateTempClubData.GetClubFavoriteList().size() >= CommonData.ClubFavoriteSelectMinCount) ||
                        (mClubCreateType == CLUB_EDIT_TYPE && TKManager.getInstance().TargetClubData.GetClubFavoriteList().size() >= CommonData.ClubFavoriteSelectMinCount));

        int selectBGColor = ContextCompat.getColor(mContext, R.color.button_enable);
        int selectSrtColor = ContextCompat.getColor(mContext, R.color.button_enable_str);
        int disableBGColor = ContextCompat.getColor(mContext, R.color.button_disable);
        int disableSrtColor = ContextCompat.getColor(mContext, R.color.button_disable_str);

        tv_ClubCreate_OK.setBackgroundColor(enable ? selectBGColor : disableBGColor);
        tv_ClubCreate_OK.setTextColor(enable ? selectSrtColor : disableSrtColor);
    }

    public void RefreshJoinType()
    {
        int selectBGColor = ContextCompat.getColor(mContext, R.color.button_enable);
        int selectSrtColor = ContextCompat.getColor(mContext, R.color.button_enable_str);
        int disableBGColor = ContextCompat.getColor(mContext, R.color.white);
        int disableSrtColor = ContextCompat.getColor(mContext, R.color.str_color_1);

        tv_ClubCreate_JoinType_Free.setBackgroundColor(isJoinType ? selectBGColor : disableBGColor);
        tv_ClubCreate_JoinType_Free.setTextColor(isJoinType ? selectSrtColor : disableSrtColor);
        tv_ClubCreate_JoinType_Approval.setBackgroundColor(isJoinType == false? selectBGColor : disableBGColor);
        tv_ClubCreate_JoinType_Approval.setTextColor(isJoinType == false ? selectSrtColor : disableSrtColor);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == Activity.RESULT_OK) {
                isProfileUpload = true;
                RefreshOkButton();


                // TODO 클럽 이미지 올려야함
                Uri resultUri = result.getUri();

                Bitmap originalBm = null;
                originalBm = CommonFunc.getInstance().resize(mContext, resultUri, 512);

                CommonFunc.getInstance().DrawImageByGlide(mContext, iv_ClubCreate_Profile, originalBm, false);


                if(mClubCreateType == CLUB_CREATE_TYPE)
                {
                    DialogFunc.getInstance().ShowLoadingPage(ClubCreateActivity.this);

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
                else
                {
                    DialogFunc.getInstance().ShowLoadingPage(ClubCreateActivity.this);

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
                    FirebaseManager.getInstance().UploadClubThumbImg(TKManager.getInstance().TargetClubData.GetClubIndex(), originalBm, TKManager.getInstance().TargetClubData, listener);
                }

            }
        }
        else if (requestCode == GET_FROM_FAVORITE_SELECT)
        {
            RefreshFavoriteViewListSlot();
            RefreshOkButton();
        }
    }
}
