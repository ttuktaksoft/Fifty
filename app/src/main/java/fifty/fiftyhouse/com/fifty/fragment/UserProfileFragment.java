package fifty.fiftyhouse.com.fifty.fragment;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import fifty.fiftyhouse.com.fifty.CommonData;
import fifty.fiftyhouse.com.fifty.CommonFunc;
import fifty.fiftyhouse.com.fifty.DataBase.UserData;
import fifty.fiftyhouse.com.fifty.DialogFunc;
import fifty.fiftyhouse.com.fifty.MainActivity;
import fifty.fiftyhouse.com.fifty.Manager.FirebaseManager;
import fifty.fiftyhouse.com.fifty.Manager.TKManager;
import fifty.fiftyhouse.com.fifty.R;
import fifty.fiftyhouse.com.fifty.activty.ClubActivity;
import fifty.fiftyhouse.com.fifty.activty.ClubListActivity;
import fifty.fiftyhouse.com.fifty.activty.CustomPhotoView;
import fifty.fiftyhouse.com.fifty.activty.FriendListActivity;
import fifty.fiftyhouse.com.fifty.activty.MemoEditActivity;
import fifty.fiftyhouse.com.fifty.activty.MyProfileEditActivity;
import fifty.fiftyhouse.com.fifty.activty.SettingActivity;
import fifty.fiftyhouse.com.fifty.activty.StrContentListActivity;
import fifty.fiftyhouse.com.fifty.activty.UserListActivity;
import fifty.fiftyhouse.com.fifty.activty.UserProfileActivity;
import fifty.fiftyhouse.com.fifty.activty.UserProfileMemoActivity;
import fifty.fiftyhouse.com.fifty.activty.WebContentActivity;
import fifty.fiftyhouse.com.fifty.adapter.FavoriteSlotAdapter;
import fifty.fiftyhouse.com.fifty.adapter.MyProfileEditMenuAdapter;
import fifty.fiftyhouse.com.fifty.adapter.UserProfileClubAdapter;
import fifty.fiftyhouse.com.fifty.adapter.UserProfileMenuAdapter;
import fifty.fiftyhouse.com.fifty.adapter.UserProfilePhotoAdapter;
import fifty.fiftyhouse.com.fifty.util.OnRecyclerItemClickListener;
import fifty.fiftyhouse.com.fifty.util.OnSingleClickListener;
import fifty.fiftyhouse.com.fifty.util.RecyclerItemClickListener;

public class UserProfileFragment extends Fragment {

    ConstraintLayout v_UserProfile_Info_Detail, v_UserProfile_Info_Favorite, v_UserProfile_Info_Photo, v_UserProfile_Info_Club,
            v_UserProfile_Info_Menu, v_UserProfile_Info_Etc;
    ImageView iv_UserProfile_Profile, iv_UserProfile_Info_Gender, iv_UserProfile_Info_Memo_BG, iv_UserProfile_Info_Edit;
    TextView tv_UserProfile_Info_Name, tv_UserProfile_Info_Age, tv_UserProfile_Info_Location,
            tv_UserProfile_Info_Memo, tv_UserProfile_Info_Count_1, tv_UserProfile_Info_Count_2, tv_UserProfile_Info_Count_3,
            tv_UserProfile_Info_Club, tv_UserProfile_Info_Club_Plus;
    RecyclerView rv_UserProfile_Info_Favorite, rv_UserProfile_Info_Club, rv_UserProfile_Info_Photo, rv_UserProfile_Info_Menu;
    Context mContext;
    View mUserProfileFragView = null;

    FavoriteSlotAdapter mFavoriteAdapter;
    UserProfilePhotoAdapter mPhotoAdapter;
    UserProfileClubAdapter mClubAdapter;
    UserProfileMenuAdapter mMenuAdapter;

    boolean mMyProfile = true;

    int MY_PROFILE_EDIT = 1;
    int mSelectPhotoIndex = 0;

    ArrayList<String> mFavoriteList = new ArrayList<>();

    public UserProfileFragment() {
        // Required empty public constructor
    }
    public void setMyProfileView(boolean myProfile) {
        // Required empty public constructor
        mMyProfile = myProfile;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if(mUserProfileFragView != null)
        {
            RefreshUI();
            return mUserProfileFragView;
        }


        mContext = getActivity();

        mUserProfileFragView = inflater.inflate(R.layout.fragment_user_profile, container, false);
        /*FragmentManager mFragmentMng = getFragmentManager();
        mFragmentMng.beginTransaction().addToBackStack(null);*/

        v_UserProfile_Info_Detail = mUserProfileFragView.findViewById(R.id.v_UserProfile_Info_Detail);
        v_UserProfile_Info_Favorite = mUserProfileFragView.findViewById(R.id.v_UserProfile_Info_Favorite);
        v_UserProfile_Info_Photo = mUserProfileFragView.findViewById(R.id.v_UserProfile_Info_Photo);
        v_UserProfile_Info_Club = mUserProfileFragView.findViewById(R.id.v_UserProfile_Info_Club);
        v_UserProfile_Info_Menu = mUserProfileFragView.findViewById(R.id.v_UserProfile_Info_Menu);
        v_UserProfile_Info_Etc = mUserProfileFragView.findViewById(R.id.v_UserProfile_Info_Etc);
        iv_UserProfile_Profile = mUserProfileFragView.findViewById(R.id.iv_UserProfile_Profile);
        iv_UserProfile_Info_Gender = mUserProfileFragView.findViewById(R.id.iv_UserProfile_Info_Gender);
        iv_UserProfile_Info_Memo_BG  = mUserProfileFragView.findViewById(R.id.iv_UserProfile_Info_Memo_BG);
        iv_UserProfile_Info_Edit = mUserProfileFragView.findViewById(R.id.iv_UserProfile_Info_Edit);
        tv_UserProfile_Info_Name = mUserProfileFragView.findViewById(R.id.tv_UserProfile_Info_Name);
        tv_UserProfile_Info_Age = mUserProfileFragView.findViewById(R.id.tv_UserProfile_Info_Age);
        tv_UserProfile_Info_Location = mUserProfileFragView.findViewById(R.id.tv_UserProfile_Info_Location);
        tv_UserProfile_Info_Memo = mUserProfileFragView.findViewById(R.id.tv_UserProfile_Info_Memo);
        tv_UserProfile_Info_Count_1 = mUserProfileFragView.findViewById(R.id.tv_UserProfile_Info_Count_1);
        tv_UserProfile_Info_Count_2 = mUserProfileFragView.findViewById(R.id.tv_UserProfile_Info_Count_2);
        tv_UserProfile_Info_Count_3 = mUserProfileFragView.findViewById(R.id.tv_UserProfile_Info_Count_3);
        tv_UserProfile_Info_Club = mUserProfileFragView.findViewById(R.id.tv_UserProfile_Info_Club);
        tv_UserProfile_Info_Club_Plus = mUserProfileFragView.findViewById(R.id.tv_UserProfile_Info_Club_Plus);
        rv_UserProfile_Info_Favorite = mUserProfileFragView.findViewById(R.id.rv_UserProfile_Info_Favorite);
        rv_UserProfile_Info_Club = mUserProfileFragView.findViewById(R.id.rv_UserProfile_Info_Club);
        rv_UserProfile_Info_Photo = mUserProfileFragView.findViewById(R.id.rv_UserProfile_Info_Photo);
        rv_UserProfile_Info_Menu = mUserProfileFragView.findViewById(R.id.rv_UserProfile_Info_Menu);

        if(mMyProfile)
        {
            setMyProfileData();
        }
        else
        {
            setUserProfileData();
        }

        initFavoriteList();
        initPhotoList();
        initClubList();
        initMenuList();

        // Inflate the layout for this fragment
        return mUserProfileFragView;
    }

    public void setMyProfileData()
    {
        v_UserProfile_Info_Detail.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View view) {
                // TODO 메인화면 갱신이 필요함
                startActivityForResult(new Intent(mContext, MyProfileEditActivity.class), MY_PROFILE_EDIT);
            }
        });

        OnSingleClickListener Listener_1 = new OnSingleClickListener() {
            @Override
            public void onSingleClick(View view) {

                if(TKManager.getInstance().MyData.GetUserVip().equals("nVip") == true)
                {
                    if(TKManager.getInstance().MyData.GetUserVisitListCount() == 0)
                    {
                        Intent intent = new Intent(mContext, UserListActivity.class);
                        intent.putExtra("Type",CommonData.USER_LIST_MY_VISIT);
                        startActivityForResult(intent, 1000);
                    }
                    else{
                        DialogFunc.getInstance().ShowLoadingPage(mContext);

                        Set KeySet = TKManager.getInstance().MyData.GetUserVisitKeySet();
                        Iterator iterator = KeySet.iterator();

                        FirebaseManager.getInstance().SetFireBaseLoadingCount("프로필변경", TKManager.getInstance().MyData.GetUserVisitListCount());

                        FirebaseManager.CheckFirebaseComplete listener = new FirebaseManager.CheckFirebaseComplete() {
                            @Override
                            public void CompleteListener() {
                                DialogFunc.getInstance().DismissLoadingPage();
                                Intent intent = new Intent(mContext, UserListActivity.class);
                                intent.putExtra("Type",CommonData.USER_LIST_MY_VISIT);
                                startActivityForResult(intent,1000);
                            }

                            @Override
                            public void CompleteListener_Yes() {
                            }

                            @Override
                            public void CompleteListener_No() {
                            }
                        };

                        while(iterator.hasNext()){
                            String key = (String)iterator.next();
                            if(TKManager.getInstance().UserData_Simple.get(key) != null)
                            {
                                FirebaseManager.getInstance().Complete("프로필변경", listener);
                            }
                            else
                                FirebaseManager.getInstance().GetUserData_Simple(key, TKManager.getInstance().UserData_Simple, listener);
                        }
                    }
                }
                else
                {
                    Intent intent = new Intent(mContext, UserListActivity.class);
                    intent.putExtra("Type",CommonData.USER_LIST_MY_VISIT);
                    startActivityForResult(intent, 1000);
                }

            }
        };

        setCountInfoListener_1(Listener_1);

        OnSingleClickListener Listener_2 = new OnSingleClickListener() {
            @Override
            public void onSingleClick(View view) {

                if(TKManager.getInstance().MyData.GetUserVip().equals("nVip") == true)
                {
                    // 좋아요
                    if(TKManager.getInstance().MyData.GetUserLikeListCount() == 0)
                    {
                        Intent intent = new Intent(mContext, UserListActivity.class);
                        intent.putExtra("Type",CommonData.USER_LIST_MY_LIKE);
                        startActivityForResult(intent, 1000);
                    }
                    else {
                        DialogFunc.getInstance().ShowLoadingPage(mContext);


                        Set KeySet = TKManager.getInstance().MyData.GetUserLikeKeySet();
                        Iterator iterator = KeySet.iterator();

                        FirebaseManager.getInstance().SetFireBaseLoadingCount("프로필변경", TKManager.getInstance().MyData.GetUserLikeListCount());

                        FirebaseManager.CheckFirebaseComplete listener = new FirebaseManager.CheckFirebaseComplete() {
                            @Override
                            public void CompleteListener() {
                                DialogFunc.getInstance().DismissLoadingPage();
                                Intent intent = new Intent(mContext, UserListActivity.class);
                                intent.putExtra("Type", CommonData.USER_LIST_MY_LIKE);
                                startActivityForResult(intent, 1000);
                            }

                            @Override
                            public void CompleteListener_Yes() {
                            }

                            @Override
                            public void CompleteListener_No() {
                            }
                        };

                        while (iterator.hasNext()) {
                            String key = (String) iterator.next();
                            if (TKManager.getInstance().UserData_Simple.get(key) != null) {
                                FirebaseManager.getInstance().Complete("프로필변경", listener);
                            } else
                                FirebaseManager.getInstance().GetUserData_Simple(key, TKManager.getInstance().UserData_Simple, listener);
                        }
                    }
                }

                else
                {
                    Intent intent = new Intent(mContext, UserListActivity.class);
                    intent.putExtra("Type",CommonData.USER_LIST_MY_LIKE);
                    startActivityForResult(intent, 1000);
                }

            }
        };

        setCountInfoListener_2(Listener_2);

        OnSingleClickListener Listener_3 = new OnSingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                if(TKManager.getInstance().MyData.GetUserFriendListCount() == 0 &&
                        TKManager.getInstance().MyData.GetRequestFriendListCount() == 0)
                {
                    Intent intent = new Intent(mContext, FriendListActivity.class);
                    startActivityForResult(intent, 1000);
                }
                else
                {
                    DialogFunc.getInstance().ShowLoadingPage(mContext);

                    Set KeySet = TKManager.getInstance().MyData.GetUserFriendListKeySet();

                   // KeySet.addAll(TKManager.getInstance().MyData.GetRequestFriendListKeySet());
                    
                    Map<String, String> tempFriend = new LinkedHashMap<>();
                    tempFriend.putAll(TKManager.getInstance().MyData.GetUserFirendList());
                    tempFriend.putAll(TKManager.getInstance().MyData.GetUserRequestFirendList());

                    Set tempKeySet = tempFriend.keySet();

                    Iterator iterator = tempKeySet.iterator();

                    //FirebaseManager.getInstance().SetFireBaseLoadingCount(TKManager.getInstance().MyData.GetUserFriendListCount() + TKManager.getInstance().MyData.GetRequestFriendListCount());
                    FirebaseManager.getInstance().SetFireBaseLoadingCount("프로필변경", tempFriend.size());

                    FirebaseManager.CheckFirebaseComplete listener = new FirebaseManager.CheckFirebaseComplete() {
                        @Override
                        public void CompleteListener() {
                            DialogFunc.getInstance().DismissLoadingPage();

                            Intent intent = new Intent(mContext, FriendListActivity.class);
                            startActivityForResult(intent, 1000);
                        }

                        @Override
                        public void CompleteListener_Yes() {
                        }

                        @Override
                        public void CompleteListener_No() {
                        }
                    };

                    while(iterator.hasNext()){
                        String key = (String)iterator.next();
                        if(TKManager.getInstance().UserData_Simple.get(key) != null)
                        {
                            FirebaseManager.getInstance().Complete("프로필변경", listener);
                        }
                        else
                            FirebaseManager.getInstance().GetUserData_Simple(key, TKManager.getInstance().UserData_Simple, listener);
                    }
                }
            }
        };

        setCountInfoListener_3(Listener_3);

        tv_UserProfile_Info_Name.setText(TKManager.getInstance().MyData.GetUserNickName());

        Glide.with(mContext).load(TKManager.getInstance().MyData.GetUserImgThumb())
                .centerCrop()
                .circleCrop()
                .into(iv_UserProfile_Profile);


        if (TKManager.getInstance().MyData.GetUserGender() == 0) {
            Glide.with(mContext).load(R.drawable.ic_man_simple)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(iv_UserProfile_Info_Gender);
        } else {
            Glide.with(mContext).load(R.drawable.ic_woman_simple)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(iv_UserProfile_Info_Gender);
        }

        tv_UserProfile_Info_Age.setText(TKManager.getInstance().MyData.GetUserAge() + "세");

        tv_UserProfile_Info_Memo.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View view) {
                startActivityForResult(new Intent(mContext, MemoEditActivity.class), 1000);
            }
        });

        RefreshLocationText();
        RefreshMemoText();
        RefreshCountText();

        v_UserProfile_Info_Etc.setVisibility(View.VISIBLE);
        /*tv_UserProfile_Info_Terms_1.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View view) {
                Intent intent = new Intent(mContext, WebContentActivity.class);
                intent.putExtra("title", CommonFunc.getInstance().getStr(mContext.getResources(), R.string.MSG_TERMS_1));
                intent.putExtra("content", CommonFunc.getInstance().getStr(mContext.getResources(), R.string.MSG_TERMS_SERVICE));
                startActivity(intent);
            }
        });

        tv_UserProfile_Info_Terms_2.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View view) {
                Intent intent = new Intent(mContext, WebContentActivity.class);
                intent.putExtra("title", CommonFunc.getInstance().getStr(mContext.getResources(), R.string.MSG_TERMS_2));
                intent.putExtra("content", CommonFunc.getInstance().getStr(mContext.getResources(), R.string.MSG_TERMS_PRIVACY));
                startActivity(intent);
            }
        });

        tv_UserProfile_Info_Terms_3.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View view) {
                Intent intent = new Intent(mContext, WebContentActivity.class);
                intent.putExtra("title", CommonFunc.getInstance().getStr(mContext.getResources(), R.string.MSG_TERMS_3));
                intent.putExtra("content", CommonFunc.getInstance().getStr(mContext.getResources(), R.string.MSG_TERMS_REFUND));
                startActivity(intent);
            }
        });*/

        iv_UserProfile_Info_Edit.setVisibility(View.VISIBLE);

        tv_UserProfile_Info_Club_Plus.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View view) {
                Intent intent = new Intent(mContext, ClubListActivity.class);
                intent.putExtra("Type", CommonData.CLUB_LIST_MY);
                startActivityForResult(intent, 1000);
            }
        });

        // TODO 클럽이 없거나 그러면 뷰를 아예 꺼줘야함
        setRecyclerViewEnable(true, true, true, true);
    }

    public void setUserProfileData()
    {
        Glide.with(mContext).load(TKManager.getInstance().TargetUserData.GetUserImgThumb())
                .circleCrop()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(iv_UserProfile_Profile);

        iv_UserProfile_Profile.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                Intent intent = new Intent(mContext, CustomPhotoView.class);
                intent.putExtra("Type", CustomPhotoView.PHOTO_VIEW_TYPE_USER_PROFILE);
                startActivityForResult(intent, 1000);
            }
        });

        iv_UserProfile_Info_Memo_BG.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                startActivity(new Intent(mContext, UserProfileMemoActivity.class));
            }
        });

        if (TKManager.getInstance().TargetUserData.GetUserGender() == 0) {
            Glide.with(mContext).load(R.drawable.ic_man_simple)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(iv_UserProfile_Info_Gender);
        } else {
            Glide.with(mContext).load(R.drawable.ic_woman_simple)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(iv_UserProfile_Info_Gender);
        }

        setCountInfoListener_1(null);
        setCountInfoListener_2(null);
        setCountInfoListener_3(null);

/*        tv_UserProfile_Info_Terms_1.setOnClickListener(null);
        tv_UserProfile_Info_Terms_2.setOnClickListener(null);
        tv_UserProfile_Info_Terms_3.setOnClickListener(null);*/

        tv_UserProfile_Info_Name.setText(TKManager.getInstance().TargetUserData.GetUserNickName());
        tv_UserProfile_Info_Age.setText(TKManager.getInstance().TargetUserData.GetUserAge() + "세");
        tv_UserProfile_Info_Memo.setOnClickListener(null);

        RefreshLocationText();
        RefreshMemoText();
        RefreshCountText();

        v_UserProfile_Info_Etc.setVisibility(View.GONE);
        iv_UserProfile_Info_Edit.setVisibility(View.GONE);

        tv_UserProfile_Info_Club_Plus.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View view) {
                Intent intent = new Intent(mContext, ClubListActivity.class);
                intent.putExtra("Type", CommonData.CLUB_LIST_USER);
                startActivityForResult(intent, 1000);
            }
        });

        // TODO 클럽이 없거나 그러면 뷰를 아예 꺼줘야함
        setRecyclerViewEnable(true, true, true, false);
    }

    public void setRecyclerViewEnable(boolean favorite, boolean photo, boolean club, boolean menu)
    {
        int topbottomId = R.id.iv_UserProfile_Info_Count_BG;

        v_UserProfile_Info_Favorite.setVisibility(View.GONE);
        v_UserProfile_Info_Photo.setVisibility(View.GONE);
        v_UserProfile_Info_Club.setVisibility(View.GONE);
        v_UserProfile_Info_Menu.setVisibility(View.GONE);

        if(favorite)
        {
            v_UserProfile_Info_Favorite.setVisibility(View.VISIBLE);

            ConstraintLayout.LayoutParams Params = new ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            Params.topToBottom = topbottomId;
            v_UserProfile_Info_Favorite.setLayoutParams(Params);

            topbottomId = v_UserProfile_Info_Favorite.getId();
        }

        if(photo)
        {
            v_UserProfile_Info_Photo.setVisibility(View.VISIBLE);

            ConstraintLayout.LayoutParams Params = new ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            Params.topToBottom = topbottomId;
            v_UserProfile_Info_Photo.setLayoutParams(Params);

            topbottomId = v_UserProfile_Info_Photo.getId();
        }

        if(club)
        {
            v_UserProfile_Info_Club.setVisibility(View.VISIBLE);

            ConstraintLayout.LayoutParams Params = new ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            Params.topToBottom = topbottomId;
            v_UserProfile_Info_Club.setLayoutParams(Params);

            topbottomId = v_UserProfile_Info_Club.getId();
        }

        if(menu)
        {
            v_UserProfile_Info_Menu.setVisibility(View.VISIBLE);

            ConstraintLayout.LayoutParams Params = new ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            Params.topToBottom = topbottomId;
            v_UserProfile_Info_Menu.setLayoutParams(Params);

            topbottomId = v_UserProfile_Info_Menu.getId();
        }
    }

    public void initFavoriteList()
    {
        mFavoriteList.clear();
        Map<String, String> mapList = new HashMap<>();
        if(mMyProfile)
        {
            mapList = TKManager.getInstance().MyData.GetUserFavoriteList();
        }
        else
        {
            mapList = TKManager.getInstance().TargetUserData.GetUserFavoriteList();
        }

        Set EntrySet = mapList.entrySet();
        Iterator iterator = EntrySet.iterator();
        while(iterator.hasNext()){
            Map.Entry entry = (Map.Entry)iterator.next();
            String key = (String)entry.getKey();
            String value = (String)entry.getValue();
            mFavoriteList.add(value);
        }

        mFavoriteAdapter = new FavoriteSlotAdapter(mContext);
        mFavoriteAdapter.setItemData(mFavoriteList);
        mFavoriteAdapter.setHasStableIds(true);

        rv_UserProfile_Info_Favorite.setAdapter(mFavoriteAdapter);
/*        ChipsLayoutManager chipsLayoutManager = ChipsLayoutManager.newBuilder(mContext)
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
        rv_UserProfile_Info_Favorite.setLayoutManager(chipsLayoutManager);*/
        rv_UserProfile_Info_Favorite.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
        rv_UserProfile_Info_Favorite.addOnItemTouchListener(new RecyclerItemClickListener(mContext, rv_UserProfile_Info_Favorite, new OnRecyclerItemClickListener() {
            @Override
            public void onSingleClick(View view, final int position) {

                ActivityManager manager = (ActivityManager)mContext.getSystemService(Activity.ACTIVITY_SERVICE);
                List<ActivityManager.RunningTaskInfo>list = manager.getRunningTasks(1);
                ActivityManager.RunningTaskInfo info = list.get(0);

                if (info.topActivity.getClassName().equals("fifty.fiftyhouse.com.fifty.activty.UserProfileActivity")) {
                    TKManager.getInstance().SelectFavorite = mFavoriteList.get(position);
                    CommonFunc.getInstance().MoveMainActivity(UserProfileActivity.mActivity, 0, 1, false);
                }
                else if(info.topActivity.getClassName().equals("fifty.fiftyhouse.com.fifty.MainActivity")){
                    if((MainActivity)getActivity() != null)
                    {
                        TKManager.getInstance().SelectFavorite = mFavoriteList.get(position);
                        ((MainActivity)getActivity()).MoveFragmentTab(0, 1);
                    }
                }
            }
        }));
    }

    public void initPhotoList()
    {
        ArrayList<String> list = new ArrayList<>();
        Map<String, String> mapList = new HashMap<>();
        if(mMyProfile)
        {
            mapList = TKManager.getInstance().MyData.GetUserImg();
        }
        else
        {
            mapList = TKManager.getInstance().TargetUserData.GetUserImg();
        }

        Set EntrySet = mapList.entrySet();
        Iterator iterator = EntrySet.iterator();
        while(iterator.hasNext()){
            Map.Entry entry = (Map.Entry)iterator.next();
            String key = (String)entry.getKey();
            String value = (String)entry.getValue();
            list.add(value);
        }

        mPhotoAdapter = new UserProfilePhotoAdapter(mContext);
        if(mMyProfile)
            mPhotoAdapter.setProfilePhotoType(UserProfilePhotoAdapter.PROFILE_PHOTO_TYPE.MY_PROFILE);
        else
            mPhotoAdapter.setProfilePhotoType(UserProfilePhotoAdapter.PROFILE_PHOTO_TYPE.USER_PROFILE);
        mPhotoAdapter.setItemCount(list.size());
        mPhotoAdapter.setItemData(list);
        mPhotoAdapter.setHasStableIds(true);

        rv_UserProfile_Info_Photo.setAdapter(mPhotoAdapter);
        rv_UserProfile_Info_Photo.setLayoutManager(new GridLayoutManager(mContext, 4));
        rv_UserProfile_Info_Photo.addOnItemTouchListener(new RecyclerItemClickListener(mContext, rv_UserProfile_Info_Photo, new OnRecyclerItemClickListener() {
            @Override
            public void onSingleClick(View view, int position) {
                if(mMyProfile)
                {
                    if(position < TKManager.getInstance().MyData.GetUserImgCount())
                    {
                        mSelectPhotoIndex = position;
                        // TODO 사진은 바로 리스트로 본다고 했었나??
                        ArrayList<String> menuList = new ArrayList<>();
                        menuList.add(CommonFunc.getInstance().getStr(mContext.getResources(), R.string.MSG_VIEW));
                        menuList.add(CommonFunc.getInstance().getStr(mContext.getResources(), R.string.MSG_TRY_PHOTO_DEL));
                        menuList.add(CommonFunc.getInstance().getStr(mContext.getResources(), R.string.MSG_TRY_PHOTO_ADD));
                        menuList.add(CommonFunc.getInstance().getStr(mContext.getResources(), R.string.MSG_CANCEL));

                        ArrayList<DialogFunc.MsgPopupListener> menuListenerList = getPhotoViewFunc();

                        DialogFunc.getInstance().ShowMenuListPopup(mContext, menuList, menuListenerList, null);
                    }
                    else if(position >= TKManager.getInstance().MyData.GetUserImgCount())
                    {
                        mSelectPhotoIndex = TKManager.getInstance().MyData.GetUserImgCount();

                        ArrayList<String> menuList = new ArrayList<>();
                        menuList.add(CommonFunc.getInstance().getStr(mContext.getResources(), R.string.MSG_TRY_PHOTO_ADD));
                        menuList.add(CommonFunc.getInstance().getStr(mContext.getResources(), R.string.MSG_CANCEL));

                        ArrayList<DialogFunc.MsgPopupListener> menuListenerList = getPhotoAddFunc();

                        DialogFunc.getInstance().ShowMenuListPopup(mContext, menuList, menuListenerList, null);
                    }
                }
                else {
                    if (position < TKManager.getInstance().TargetUserData.GetUserImgCount())
                    {
                        Intent intent = new Intent(mContext, CustomPhotoView.class);
                        intent.putExtra("Type", CustomPhotoView.PHOTO_VIEW_TYPE_USER_PROFILE_LIST);
                        startActivityForResult(intent, 1000);
                    }
                }

            }

            @Override
            public void onLongItemClick(View view, int position) {
                //  Toast.makeText(getApplicationContext(),position+"번 째 아이템 롱 클릭",Toast.LENGTH_SHORT).show();
            }
        }));
    }

    public void initClubList()
    {
        mClubAdapter = new UserProfileClubAdapter(mContext);
        RefreshClubDataList();
        mClubAdapter.setHasStableIds(true);

        rv_UserProfile_Info_Club.setAdapter(mClubAdapter);
        rv_UserProfile_Info_Club.setLayoutManager(new GridLayoutManager(mContext, 1));
        rv_UserProfile_Info_Club.addOnItemTouchListener(new RecyclerItemClickListener(mContext, rv_UserProfile_Info_Club, new OnRecyclerItemClickListener() {
            @Override
            public void onSingleClick(View view, final int position) {

                UserData tempuser =new UserData();
                Set tempKey;
                if(mMyProfile)
                {
                    tempKey = TKManager.getInstance().MyData.GetUserClubDataKeySet();
                    tempuser = TKManager.getInstance().MyData;
                }
                else
                {
                    tempKey = TKManager.getInstance().TargetUserData.GetUserClubDataKeySet();
                    tempuser = TKManager.getInstance().TargetUserData;
                }



                final List array = new ArrayList(tempKey);

                //DialogFunc.getInstance().ShowLoadingPage(MainActivity.mActivity);

               // if(CommonFunc.getInstance().CheckStringNull(tempuser.GetUserClubData(TKManager.getInstance().ClubData_Simple.get(array.get(position).toString()).GetClubIndex()).GetClubMasterIndex()))
                {
                    FirebaseManager.CheckFirebaseComplete GetClubDataListener = new FirebaseManager.CheckFirebaseComplete() {
                        @Override
                        public void CompleteListener() {

                            FirebaseManager.CheckFirebaseComplete GetClubContextListener = new FirebaseManager.CheckFirebaseComplete() {
                                @Override
                                public void CompleteListener() {
                                    startActivity(new Intent(getContext(), ClubActivity.class));
                                }

                                @Override
                                public void CompleteListener_Yes() {}
                                @Override
                                public void CompleteListener_No() {}
                            };

                            FirebaseManager.getInstance().GetClubContextData(TKManager.getInstance().ClubData_Simple.get(array.get(position).toString()).GetClubIndex(), GetClubContextListener);
                        }

                        @Override
                        public void CompleteListener_Yes() {}
                        @Override
                        public void CompleteListener_No() {}
                    };

                    FirebaseManager.getInstance().GetClubData(tempuser, TKManager.getInstance().ClubData_Simple.get(array.get(position).toString()).GetClubIndex(),
                            GetClubDataListener);
                }



            }

            @Override
            public void onLongItemClick(View view, int position) {
                //  Toast.makeText(getApplicationContext(),position+"번 째 아이템 롱 클릭",Toast.LENGTH_SHORT).show();
            }
        }));
    }

    public void RefreshClubDataList()
    {
        ArrayList<String> list = new ArrayList<>();
        ArrayList<String> templist = new ArrayList<>();
        if(mMyProfile)
        {
            templist.addAll(TKManager.getInstance().MyData.GetUserClubDataKeySet());
        }
        else
        {
            templist.addAll(TKManager.getInstance().TargetUserData.GetUserClubDataKeySet());
        }

        if(templist.size() >= CommonData.PROFILE_CLUB_VIEW_MAX)
            list.addAll(templist.subList(0, CommonData.PROFILE_CLUB_VIEW_MAX));
        else
            list.addAll(templist);

        if(list.size() == 0)
            tv_UserProfile_Info_Club.setVisibility(View.GONE);
        else
            tv_UserProfile_Info_Club.setVisibility(View.VISIBLE);

        if(templist.size() > CommonData.PROFILE_CLUB_VIEW_MAX)
            tv_UserProfile_Info_Club_Plus.setVisibility(View.VISIBLE);
        else
            tv_UserProfile_Info_Club_Plus.setVisibility(View.GONE);

        mClubAdapter.setMyProfile(mMyProfile);
        mClubAdapter.setItemCount(list.size());
        mClubAdapter.setItemData(list);

        mClubAdapter.notifyDataSetChanged();
    }

    public void initMenuList()
    {
        mMenuAdapter = new UserProfileMenuAdapter(mContext);
        mMenuAdapter.setHasStableIds(true);

        rv_UserProfile_Info_Menu.setAdapter(mMenuAdapter);
        rv_UserProfile_Info_Menu.setLayoutManager(new GridLayoutManager(mContext, 1));
        rv_UserProfile_Info_Menu.addOnItemTouchListener(new RecyclerItemClickListener(mContext, rv_UserProfile_Info_Menu, new OnRecyclerItemClickListener() {
            @Override
            public void onSingleClick(View view, int position) {
                if(position == 3)
                {
                    Intent intent = new Intent(mContext, SettingActivity.class);
                    startActivity(intent);
                }
                else
                {
                    Intent intent = new Intent(mContext, StrContentListActivity.class);
                    intent.putExtra("Type",position);
                    startActivity(intent);

                    /*
                    switch (position)
                    {
                        case 0:

                            FirebaseManager.CheckFirebaseComplete EventListener = new FirebaseManager.CheckFirebaseComplete() {
                                @Override
                                public void CompleteListener() {
                                    Intent intent = new Intent(mContext, StrContentListActivity.class);
                                    intent.putExtra("Type",position);
                                    startActivity(intent);
                                }

                                @Override
                                public void CompleteListener_Yes() {

                                }

                                @Override
                                public void CompleteListener_No() {

                                }
                            };

                            FirebaseManager.getInstance().GetManagerEvent(EventListener);

                            break;
                        case 1:
                            FirebaseManager.CheckFirebaseComplete NoticeListener = new FirebaseManager.CheckFirebaseComplete() {
                                @Override
                                public void CompleteListener() {
                                    Intent intent = new Intent(mContext, StrContentListActivity.class);
                                    intent.putExtra("Type",position);
                                    startActivity(intent);
                                }

                                @Override
                                public void CompleteListener_Yes() {

                                }

                                @Override
                                public void CompleteListener_No() {

                                }
                            };

                            FirebaseManager.getInstance().GetManagerNotice(NoticeListener);
                            break;
                        case 2:
                            FirebaseManager.CheckFirebaseComplete FAQListener = new FirebaseManager.CheckFirebaseComplete() {
                                @Override
                                public void CompleteListener() {
                                    Intent intent = new Intent(mContext, StrContentListActivity.class);
                                    intent.putExtra("Type",position);
                                    startActivity(intent);
                                }

                                @Override
                                public void CompleteListener_Yes() {

                                }

                                @Override
                                public void CompleteListener_No() {

                                }
                            };

                            FirebaseManager.getInstance().GetManagerFAQ(FAQListener);
                            break;
                    }
*/

                }
            }

            @Override
            public void onLongItemClick(View view, int position) {
                //  Toast.makeText(getApplicationContext(),position+"번 째 아이템 롱 클릭",Toast.LENGTH_SHORT).show();
            }
        }));
    }




    private ArrayList<DialogFunc.MsgPopupListener> getPhotoAddFunc()
    {
        ArrayList<DialogFunc.MsgPopupListener> list = new ArrayList<>();

        list.add(new DialogFunc.MsgPopupListener()
        {
            @Override
            public void Listener()
            {
                CommonFunc.PhotoSelectListener selectListener = new CommonFunc.PhotoSelectListener()
                {
                    @Override
                    public void Listener(List<Uri> list)
                    {
                        if(list.size() <= 0)
                            return;

                        CropImage.activity(list.get(0))
                                .setActivityTitle(CommonFunc.getInstance().getStr(mContext.getResources(), R.string.MSG_PHOTO_SELECT))
                                .setGuidelines(CropImageView.Guidelines.ON)
                                .setInitialCropWindowPaddingRatio(0)
                                .start(mContext, UserProfileFragment.this);

                    }
                };
                CommonFunc.getInstance().GetPermissionForGalleryCamera(UserProfileFragment.this.getActivity(), selectListener, false);
            }
        });

        return list;
    }

    private ArrayList<DialogFunc.MsgPopupListener> getPhotoViewFunc()
    {
        ArrayList<DialogFunc.MsgPopupListener> list = new ArrayList<>();
        list.add(new DialogFunc.MsgPopupListener()
        {
            @Override
            public void Listener()
            {
                Intent intent = new Intent(mContext, CustomPhotoView.class);
                intent.putExtra("Type", CustomPhotoView.PHOTO_VIEW_TYPE_MY_PROFILE_LIST);
                startActivity(intent);
            }
        });
        list.add(new DialogFunc.MsgPopupListener()
        {
            @Override
            public void Listener()
            {
                // #837951 사진 삭제 기능 추가
            }
        });
        list.add(new DialogFunc.MsgPopupListener()
        {
            @Override
            public void Listener()
            {
                CommonFunc.PhotoSelectListener selectListener = new CommonFunc.PhotoSelectListener()
                {
                    @Override
                    public void Listener(List<Uri> list)
                    {
                        if(list.size() <= 0)
                            return;

                        CropImage.activity(list.get(0))
                                .setActivityTitle(CommonFunc.getInstance().getStr(mContext.getResources(), R.string.MSG_PHOTO_SELECT))
                                .setGuidelines(CropImageView.Guidelines.ON)
                                .setInitialCropWindowPaddingRatio(0)
                                .start(mContext, UserProfileFragment.this);

                    }
                };
                CommonFunc.getInstance().GetPermissionForGalleryCamera(UserProfileFragment.this.getActivity(), selectListener, false);
            }
        });


        return list;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == MY_PROFILE_EDIT) {
            if(mMyProfile)
            {
                tv_UserProfile_Info_Name.setText(TKManager.getInstance().MyData.GetUserNickName());
                RefreshLocationText();
                RefreshMemoText();

                ArrayList<String> list = new ArrayList<>();
                Map<String, String> mapList = new HashMap<>();
                mapList = TKManager.getInstance().MyData.GetUserFavoriteList();
                Set EntrySet = mapList.entrySet();
                Iterator iterator = EntrySet.iterator();
                while(iterator.hasNext()){
                    Map.Entry entry = (Map.Entry)iterator.next();
                    String key = (String)entry.getKey();
                    String value = (String)entry.getValue();
                    list.add(value);
                }

                mFavoriteAdapter.setItemData(list);
                mFavoriteAdapter.notifyDataSetChanged();

                Glide.with(mContext).load(TKManager.getInstance().MyData.GetUserImgThumb())
                        .centerCrop()
                        .circleCrop()
                        .into(iv_UserProfile_Profile);

                RefreshMyPhotoList();
            }
        }
        else if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == Activity.RESULT_OK) {
                DialogFunc.getInstance().ShowLoadingPage(mContext);
                FirebaseManager.CheckFirebaseComplete listener = new FirebaseManager.CheckFirebaseComplete() {
                    @Override
                    public void CompleteListener() {
                        DialogFunc.getInstance().DismissLoadingPage();
                        if(mMyProfile)
                        {
                            Glide.with(mContext).load(TKManager.getInstance().MyData.GetUserImgThumb())
                                    .centerCrop()
                                    .circleCrop()
                                    .into(iv_UserProfile_Profile);

                            RefreshMyPhotoList();
                        }
                    }

                    @Override
                    public void CompleteListener_Yes() {
                    }

                    @Override
                    public void CompleteListener_No() {
                    }
                };
                Uri resultUri = result.getUri();
                CommonFunc.getInstance().SetCropImage(mContext, resultUri, mSelectPhotoIndex, null, listener);
            }
        }

        // TODO 무조건 갱신
        RefreshCountText();
        RefreshMemoText();
        RefreshClubDataList();
    }

    private void RefreshMyPhotoList()
    {
        ArrayList<String> list = new ArrayList<>();
        Map<String, String> mapList = TKManager.getInstance().MyData.GetUserImg();
        Set EntrySet = mapList.entrySet();

        Iterator iterator = EntrySet.iterator();
        while(iterator.hasNext()){
            Map.Entry entry = (Map.Entry)iterator.next();
            String key = (String)entry.getKey();
            String value = (String)entry.getValue();
            list.add(value);
        }

        mPhotoAdapter.setProfilePhotoType(UserProfilePhotoAdapter.PROFILE_PHOTO_TYPE.MY_PROFILE);
        mPhotoAdapter.setItemCount(list.size());
        mPhotoAdapter.setItemData(list);

        mPhotoAdapter.notifyDataSetChanged();
    }

    private void RefreshMemoText()
    {
        if(mMyProfile)
        {
            if(CommonFunc.getInstance().CheckStringNull(TKManager.getInstance().MyData.GetUserMemo()))
                tv_UserProfile_Info_Memo.setText(CommonFunc.getInstance().getStr(mContext.getResources(), R.string.MSG_DEFAULT_LOCATION_MEMO_1) + " " + TKManager.getInstance().MyData.GetUserNickName() + CommonFunc.getInstance().getStr(mContext.getResources(), R.string.MSG_DEFAULT_LOCATION_MEMO_2));
            else
                tv_UserProfile_Info_Memo.setText(TKManager.getInstance().MyData.GetUserMemo());
        }
        else
        {
            if(CommonFunc.getInstance().CheckStringNull(TKManager.getInstance().TargetUserData.GetUserMemo()))
                tv_UserProfile_Info_Memo.setText(CommonFunc.getInstance().getStr(mContext.getResources(), R.string.MSG_DEFAULT_LOCATION_MEMO_1) + " " + TKManager.getInstance().TargetUserData.GetUserNickName() + CommonFunc.getInstance().getStr(mContext.getResources(), R.string.MSG_DEFAULT_LOCATION_MEMO_2));
            else
                tv_UserProfile_Info_Memo.setText(TKManager.getInstance().TargetUserData.GetUserMemo());
        }



    }

    private void RefreshLocationText()
    {
        if(mMyProfile)
        {
            if(CommonFunc.getInstance().CheckStringNull(TKManager.getInstance().MyData.GetUserLocation()) == false)
                tv_UserProfile_Info_Location.setText(TKManager.getInstance().MyData.GetUserLocation());
            else if(CommonFunc.getInstance().CheckStringNull(TKManager.getInstance().MyData.GetUserDist_Area()) == false)
                tv_UserProfile_Info_Location.setText(TKManager.getInstance().MyData.GetUserDist_Area());
            else
                tv_UserProfile_Info_Location.setText("활동 지역이 없습니다");
        }
        else
        {
            if(CommonFunc.getInstance().CheckStringNull(TKManager.getInstance().TargetUserData.GetUserLocation()) == false)
                tv_UserProfile_Info_Location.setText(TKManager.getInstance().TargetUserData.GetUserLocation());
            else if(CommonFunc.getInstance().CheckStringNull(TKManager.getInstance().TargetUserData.GetUserDist_Area()) == false)
                tv_UserProfile_Info_Location.setText(TKManager.getInstance().TargetUserData.GetUserDist_Area());
            else
                tv_UserProfile_Info_Location.setText("활동 지역이 없습니다");
        }

    }

    public void RefreshCountText()
    {
        String MSG_VISITER = CommonFunc.getInstance().getStr(mContext.getResources(), R.string.MSG_VISITER);
        String MSG_FRIEND = CommonFunc.getInstance().getStr(mContext.getResources(), R.string.MSG_FRIEND);
        String MSG_DISTANCE = CommonFunc.getInstance().getStr(mContext.getResources(), R.string.MSG_DISTANCE);
        String MSG_LIKE = CommonFunc.getInstance().getStr(mContext.getResources(), R.string.MSG_LIKE);
        if(mMyProfile)
        {
            SpannableStringBuilder str_1 = new SpannableStringBuilder( TKManager.getInstance().MyData.GetUserTodayVisit() + " / " + TKManager.getInstance().MyData.GetUserTotalVisit() + "\n" + MSG_VISITER);
            str_1.setSpan(new android.text.style.StyleSpan(android.graphics.Typeface.BOLD), 0, str_1.length() - MSG_VISITER.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            str_1.setSpan(new ForegroundColorSpan(mContext.getResources().getColor(R.color.str_color_7)), str_1.length() - MSG_VISITER.length(), str_1.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            tv_UserProfile_Info_Count_1.setText(str_1);

            SpannableStringBuilder str_2 = new SpannableStringBuilder(TKManager.getInstance().MyData.GetUserTodayLike() + " / " + TKManager.getInstance().MyData.GetUserTotalLike() + "\n" + MSG_LIKE);
            str_2.setSpan(new android.text.style.StyleSpan(android.graphics.Typeface.BOLD), 0, str_2.length() - MSG_LIKE.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            str_2.setSpan(new ForegroundColorSpan(mContext.getResources().getColor(R.color.str_color_7)), str_2.length() - MSG_LIKE.length(), str_2.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            tv_UserProfile_Info_Count_2.setText(str_2);

            SpannableStringBuilder str_3 = new SpannableStringBuilder(TKManager.getInstance().MyData.GetUserFriendListCount()+ " / " + TKManager.getInstance().MyData.GetUserFriendListCount() + "\n" + MSG_FRIEND);
            str_3.setSpan(new android.text.style.StyleSpan(android.graphics.Typeface.BOLD), 0, str_3.length() - MSG_FRIEND.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            str_3.setSpan(new ForegroundColorSpan(mContext.getResources().getColor(R.color.str_color_7)), str_3.length() - MSG_FRIEND.length(), str_3.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            tv_UserProfile_Info_Count_3.setText(str_3);
        }
        else
        {
            SpannableStringBuilder str_1 = new SpannableStringBuilder(TKManager.getInstance().TargetUserData.GetUserTodayVisit() + " / " + TKManager.getInstance().TargetUserData.GetUserTotalVisit() + "\n" + MSG_VISITER );
            str_1.setSpan(new android.text.style.StyleSpan(android.graphics.Typeface.BOLD), 0, str_1.length() - MSG_VISITER.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            str_1.setSpan(new ForegroundColorSpan(mContext.getResources().getColor(R.color.str_color_7)), str_1.length() - MSG_VISITER.length(), str_1.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            tv_UserProfile_Info_Count_1.setText(str_1);

            SpannableStringBuilder str_2 = new SpannableStringBuilder(TKManager.getInstance().TargetUserData.GetUserTodayLike() + " / " + TKManager.getInstance().TargetUserData.GetUserTotalLike() + "\n" + MSG_LIKE);
            str_2.setSpan(new android.text.style.StyleSpan(android.graphics.Typeface.BOLD), 0, str_2.length() - MSG_LIKE.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            str_2.setSpan(new ForegroundColorSpan(mContext.getResources().getColor(R.color.str_color_7)), str_2.length() - MSG_LIKE.length(), str_2.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            tv_UserProfile_Info_Count_2.setText(str_2);

            String mUserDist = null;
            if(TKManager.getInstance().TargetUserData.GetUserDist()  < 1000)
            {
                mUserDist = "1km 이내";
            }
            else
            {
                mUserDist = (int)(TKManager.getInstance().TargetUserData.GetUserDist()  / 1000) + "km";
            }

            SpannableStringBuilder str_3 = new SpannableStringBuilder(mUserDist + "\n" + MSG_DISTANCE);
            str_3.setSpan(new android.text.style.StyleSpan(android.graphics.Typeface.BOLD), 0, str_3.length() - MSG_DISTANCE.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            str_3.setSpan(new ForegroundColorSpan(mContext.getResources().getColor(R.color.str_color_7)), str_3.length() - MSG_DISTANCE.length(), str_3.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            tv_UserProfile_Info_Count_3.setText(str_3);
        }

    }

    public void setCountInfoListener_1(OnSingleClickListener listener)
    {
        tv_UserProfile_Info_Count_1.setOnClickListener(listener);
    }

    public void setCountInfoListener_2(OnSingleClickListener listener)
    {
        tv_UserProfile_Info_Count_2.setOnClickListener(listener);
    }

    public void setCountInfoListener_3(OnSingleClickListener listener)
    {
        tv_UserProfile_Info_Count_3.setOnClickListener(listener);
    }

    public void RefreshUI()
    {
        RefreshClubDataList();
    }
}
