package fifty.fiftyhouse.com.fifty.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.beloo.widget.chipslayoutmanager.ChipsLayoutManager;
import com.beloo.widget.chipslayoutmanager.gravity.IChildGravityResolver;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import fifty.fiftyhouse.com.fifty.CommonData;
import fifty.fiftyhouse.com.fifty.CommonFunc;
import fifty.fiftyhouse.com.fifty.DialogFunc;
import fifty.fiftyhouse.com.fifty.Manager.FirebaseManager;
import fifty.fiftyhouse.com.fifty.Manager.TKManager;
import fifty.fiftyhouse.com.fifty.R;
import fifty.fiftyhouse.com.fifty.activty.CustomPhotoView;
import fifty.fiftyhouse.com.fifty.activty.MyProfileEditActivity;
import fifty.fiftyhouse.com.fifty.activty.SettingActivity;
import fifty.fiftyhouse.com.fifty.activty.StrContentListActivity;
import fifty.fiftyhouse.com.fifty.activty.UserListActivity;
import fifty.fiftyhouse.com.fifty.activty.UserProfileMemoActivity;
import fifty.fiftyhouse.com.fifty.adapter.FavoriteViewAdapter;
import fifty.fiftyhouse.com.fifty.adapter.UserProfileClubAdapter;
import fifty.fiftyhouse.com.fifty.adapter.UserProfileMenuAdapter;
import fifty.fiftyhouse.com.fifty.adapter.UserProfilePhotoAdapter;
import fifty.fiftyhouse.com.fifty.util.RecyclerItemClickListener;

public class UserProfileFragment extends Fragment {

    ConstraintLayout v_UserProfile_Info_Detail, v_UserProfile_Info_Favorite, v_UserProfile_Info_Photo, v_UserProfile_Info_Club,
            v_UserProfile_Info_Menu;
    ImageView iv_UserProfile_Profile, iv_UserProfile_Info_Gender, iv_UserProfile_Info_Memo_BG;
    TextView tv_UserProfile_Info_Name, tv_UserProfile_Info_Age, tv_UserProfile_Info_Location,
            tv_UserProfile_Info_Memo, tv_UserProfile_Info_Count_1, tv_UserProfile_Info_Count_2, tv_UserProfile_Info_Count_3;
    RecyclerView rv_UserProfile_Info_Favorite, rv_UserProfile_Info_Club, rv_UserProfile_Info_Photo, rv_UserProfile_Info_Menu;
    Context mContext;
    View mUserProfileFragView;

    FavoriteViewAdapter mFavoriteAdapter;
    UserProfilePhotoAdapter mPhotoAdapter;
    UserProfileClubAdapter mClubAdapter;
    UserProfileMenuAdapter mMenuAdapter;

    boolean mMyProfile = true;

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

        mContext = getActivity();

        mUserProfileFragView = inflater.inflate(R.layout.fragment_user_profile, container, false);
        /*FragmentManager mFragmentMng = getFragmentManager();
        mFragmentMng.beginTransaction().addToBackStack(null);*/

        v_UserProfile_Info_Detail = mUserProfileFragView.findViewById(R.id.v_UserProfile_Info_Detail);
        v_UserProfile_Info_Favorite = mUserProfileFragView.findViewById(R.id.v_UserProfile_Info_Favorite);
        v_UserProfile_Info_Photo = mUserProfileFragView.findViewById(R.id.v_UserProfile_Info_Photo);
        v_UserProfile_Info_Club = mUserProfileFragView.findViewById(R.id.v_UserProfile_Info_Club);
        v_UserProfile_Info_Menu = mUserProfileFragView.findViewById(R.id.v_UserProfile_Info_Menu);
        iv_UserProfile_Profile = mUserProfileFragView.findViewById(R.id.iv_UserProfile_Profile);
        iv_UserProfile_Info_Gender = mUserProfileFragView.findViewById(R.id.iv_UserProfile_Info_Gender);
        iv_UserProfile_Info_Memo_BG  = mUserProfileFragView.findViewById(R.id.iv_UserProfile_Info_Memo_BG);
        tv_UserProfile_Info_Name = mUserProfileFragView.findViewById(R.id.tv_UserProfile_Info_Name);
        tv_UserProfile_Info_Age = mUserProfileFragView.findViewById(R.id.tv_UserProfile_Info_Age);
        tv_UserProfile_Info_Location = mUserProfileFragView.findViewById(R.id.tv_UserProfile_Info_Location);
        tv_UserProfile_Info_Memo = mUserProfileFragView.findViewById(R.id.tv_UserProfile_Info_Memo);
        tv_UserProfile_Info_Count_1 = mUserProfileFragView.findViewById(R.id.tv_UserProfile_Info_Count_1);
        tv_UserProfile_Info_Count_2 = mUserProfileFragView.findViewById(R.id.tv_UserProfile_Info_Count_2);
        tv_UserProfile_Info_Count_3 = mUserProfileFragView.findViewById(R.id.tv_UserProfile_Info_Count_3);
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
        v_UserProfile_Info_Detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(mContext, MyProfileEditActivity.class));
            }
        });

        tv_UserProfile_Info_Count_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 방문자

                DialogFunc.getInstance().ShowLoadingPage(mContext);

                Set KeySet = TKManager.getInstance().MyData.GetUserVisitKeySet();
                Iterator iterator = KeySet.iterator();

                FirebaseManager.getInstance().SetFireBaseLoadingCount(TKManager.getInstance().MyData.GetUserVisitListCount());

                FirebaseManager.CheckFirebaseComplete listener = new FirebaseManager.CheckFirebaseComplete() {
                    @Override
                    public void CompleteListener() {
                        DialogFunc.getInstance().DismissLoadingPage();
                        Intent intent = new Intent(mContext, UserListActivity.class);
                        intent.putExtra("Type",0);
                        intent.putExtra("Count",TKManager.getInstance().MyData.GetUserVisitListCount());
                        startActivity(intent);
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
                        FirebaseManager.getInstance().Complete(listener);
                    }
                    else
                        FirebaseManager.getInstance().GetUserData_Simple(key, TKManager.getInstance().UserData_Simple, listener);
                }
            }
        });

        tv_UserProfile_Info_Count_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 좋아요

                DialogFunc.getInstance().ShowLoadingPage(mContext);


                Set KeySet = TKManager.getInstance().MyData.GetUserLikeKeySet();
                Iterator iterator = KeySet.iterator();

                FirebaseManager.getInstance().SetFireBaseLoadingCount(TKManager.getInstance().MyData.GetUserLikeListCount());

                FirebaseManager.CheckFirebaseComplete listener = new FirebaseManager.CheckFirebaseComplete() {
                    @Override
                    public void CompleteListener() {
                        DialogFunc.getInstance().DismissLoadingPage();
                        Intent intent = new Intent(mContext, UserListActivity.class);
                        intent.putExtra("Type",1);
                        intent.putExtra("Count",TKManager.getInstance().MyData.GetUserLikeListCount());
                        startActivity(intent);
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
                        FirebaseManager.getInstance().Complete(listener);
                    }
                    else
                        FirebaseManager.getInstance().GetUserData_Simple(key, TKManager.getInstance().UserData_Simple, listener);
                }
            }
        });

        tv_UserProfile_Info_Count_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 친구

                DialogFunc.getInstance().ShowLoadingPage(mContext);

                Set KeySet = TKManager.getInstance().MyData.GetUserFriendListKeySet();
                Iterator iterator = KeySet.iterator();

                FirebaseManager.getInstance().SetFireBaseLoadingCount(TKManager.getInstance().MyData.GetUserFriendListCount());

                FirebaseManager.CheckFirebaseComplete listener = new FirebaseManager.CheckFirebaseComplete() {
                    @Override
                    public void CompleteListener() {
                        DialogFunc.getInstance().DismissLoadingPage();

                        Intent intent = new Intent(mContext, UserListActivity.class);
                        intent.putExtra("Type",2);
                        intent.putExtra("Count",TKManager.getInstance().MyData.GetUserFriendListCount());
                        startActivity(intent);
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
                        FirebaseManager.getInstance().Complete(listener);
                    }
                    else
                        FirebaseManager.getInstance().GetUserData_Simple(key, TKManager.getInstance().UserData_Simple, listener);
                }
            }
        });

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
        tv_UserProfile_Info_Location.setText(TKManager.getInstance().MyData.GetUserLocation());

        if(CommonFunc.getInstance().CheckStringNull(TKManager.getInstance().MyData.GetUserMemo()))
            tv_UserProfile_Info_Memo.setText(CommonFunc.getInstance().getStr(mContext.getResources(), R.string.DEFAULT_USERPROFILE_MEMO));
        else
            tv_UserProfile_Info_Memo.setText(TKManager.getInstance().MyData.GetUserMemo());

        tv_UserProfile_Info_Count_1.setText("방문자 " + TKManager.getInstance().MyData.GetUserTodayVisit() + " / " + TKManager.getInstance().MyData.GetUserTotalVisit());
        tv_UserProfile_Info_Count_2.setText("좋아요 " + TKManager.getInstance().MyData.GetUserTodayLike() + " / " + TKManager.getInstance().MyData.GetUserTotalLike());
        tv_UserProfile_Info_Count_3.setText("친구 " + TKManager.getInstance().MyData.GetUserFriendListCount());

        // TODO 클럽이 없거나 그러면 뷰를 아예 꺼줘야함
        setRecyclerViewEnable(true, true, true, true);
    }

    public void setUserProfileData()
    {
        Glide.with(mContext).load(TKManager.getInstance().TargetUserData.GetUserImgThumb())
                .circleCrop()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(iv_UserProfile_Profile);

        iv_UserProfile_Profile.setOnClickListener(new ImageView.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, CustomPhotoView.class);
                intent.putExtra("ImgSrc",TKManager.getInstance().TargetUserData.GetUserImg("0"));
                startActivity(intent);
            }
        });

        iv_UserProfile_Info_Memo_BG.setOnClickListener(new ImageView.OnClickListener() {
            @Override
            public void onClick(View v) {
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

        tv_UserProfile_Info_Name.setText(TKManager.getInstance().TargetUserData.GetUserNickName());
        tv_UserProfile_Info_Age.setText(TKManager.getInstance().TargetUserData.GetUserAge() + "세");
        tv_UserProfile_Info_Location.setText(TKManager.getInstance().TargetUserData.GetUserLocation());

        tv_UserProfile_Info_Count_1.setText("방문자 " + TKManager.getInstance().TargetUserData.GetUserTodayVisit() + " / " + TKManager.getInstance().TargetUserData.GetUserTotalVisit());
        tv_UserProfile_Info_Count_2.setText("좋아요 " + TKManager.getInstance().TargetUserData.GetUserTodayLike() + " / " + TKManager.getInstance().TargetUserData.GetUserTotalLike());
        tv_UserProfile_Info_Count_3.setText("거리 " + TKManager.getInstance().TargetUserData.GetUserDist() + " Km");

        if(CommonFunc.getInstance().CheckStringNull(TKManager.getInstance().TargetUserData.GetUserMemo()))
            tv_UserProfile_Info_Memo.setText(CommonFunc.getInstance().getStr(mContext.getResources(), R.string.DEFAULT_USERPROFILE_MEMO));
        else
            tv_UserProfile_Info_Memo.setText(TKManager.getInstance().TargetUserData.GetUserMemo());


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
        ArrayList<String> list = new ArrayList<>();
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
            list.add(value);
        }

        mFavoriteAdapter = new FavoriteViewAdapter(mContext);
        mFavoriteAdapter.setItemCount(list.size());
        mFavoriteAdapter.setItemData(list);
        mFavoriteAdapter.setHasStableIds(true);

        rv_UserProfile_Info_Favorite.setAdapter(mFavoriteAdapter);
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
        rv_UserProfile_Info_Favorite.setLayoutManager(chipsLayoutManager);
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
        mPhotoAdapter.setItemCount(list.size());
        mPhotoAdapter.setItemData(list);
        mPhotoAdapter.setHasStableIds(true);

        rv_UserProfile_Info_Photo.setAdapter(mPhotoAdapter);
        rv_UserProfile_Info_Photo.setLayoutManager(new GridLayoutManager(mContext, 4));
        rv_UserProfile_Info_Photo.addOnItemTouchListener(new RecyclerItemClickListener(mContext, rv_UserProfile_Info_Photo, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                startActivity(new Intent(mContext, StrContentListActivity.class));
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
        mClubAdapter.setHasStableIds(true);

        rv_UserProfile_Info_Club.setAdapter(mClubAdapter);
        rv_UserProfile_Info_Club.setLayoutManager(new GridLayoutManager(mContext, 1));
        rv_UserProfile_Info_Club.addOnItemTouchListener(new RecyclerItemClickListener(mContext, rv_UserProfile_Info_Club, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                startActivity(new Intent(mContext, StrContentListActivity.class));
            }

            @Override
            public void onLongItemClick(View view, int position) {
                //  Toast.makeText(getApplicationContext(),position+"번 째 아이템 롱 클릭",Toast.LENGTH_SHORT).show();
            }
        }));
    }

    public void initMenuList()
    {
        mMenuAdapter = new UserProfileMenuAdapter(mContext);
        mMenuAdapter.setHasStableIds(true);

        rv_UserProfile_Info_Menu.setAdapter(mMenuAdapter);
        rv_UserProfile_Info_Menu.setLayoutManager(new GridLayoutManager(mContext, 1));
        rv_UserProfile_Info_Menu.addOnItemTouchListener(new RecyclerItemClickListener(mContext, rv_UserProfile_Info_Menu, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if(position == 3)
                {
                    Intent intent = new Intent(mContext, SettingActivity.class);
                }
                else
                {
                    Intent intent = new Intent(mContext, StrContentListActivity.class);
                    intent.putExtra("Type",position);
                    startActivity(intent);
                }
            }

            @Override
            public void onLongItemClick(View view, int position) {
                //  Toast.makeText(getApplicationContext(),position+"번 째 아이템 롱 클릭",Toast.LENGTH_SHORT).show();
            }
        }));
    }

    public void setCountInfoStr_1(String str)
    {
        tv_UserProfile_Info_Count_1.setText(str);
    }

    public void setCountInfoStr_2(String str)
    {
        tv_UserProfile_Info_Count_2.setText(str);
    }

    public void setCountInfoStr_3(String str)
    {
        tv_UserProfile_Info_Count_3.setText(str);
    }
}