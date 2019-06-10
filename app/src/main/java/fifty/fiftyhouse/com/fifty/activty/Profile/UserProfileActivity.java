package fifty.fiftyhouse.com.fifty.activty.Profile;

import android.content.Context;
import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import fifty.fiftyhouse.com.fifty.CommonFunc;
import fifty.fiftyhouse.com.fifty.DialogFunc;
import fifty.fiftyhouse.com.fifty.MainActivity;
import fifty.fiftyhouse.com.fifty.Manager.FirebaseManager;
import fifty.fiftyhouse.com.fifty.Manager.TKManager;
import fifty.fiftyhouse.com.fifty.R;
import fifty.fiftyhouse.com.fifty.activty.CustomPhotoView;
import fifty.fiftyhouse.com.fifty.adapter.UserProfilePhotoAdapter;
import fifty.fiftyhouse.com.fifty.util.RecyclerItemClickListener;

public class UserProfileActivity extends AppCompatActivity {

    NestedScrollView ns_UserProfile_Scroll;
    TextView tv_UserProfile_Name, tv_UserProfile_Info_Name, tv_UserProfile_Info_Age, tv_UserProfile_Info_Memo, tv_UserProfile_Info_Count_Visit, tv_UserProfile_Info_Count_Like, tv_UserProfile_Info_Count_Near, tv_UserProfile_Info_Favorite;

    ImageView iv_UserProfile_Info_Profile;
    ImageView iv_UserProfile_Info_Gender, iv_UserProfile_BottomBar_Like;
    ConstraintLayout v_UserProfile_TopBar, v_UserProfile_BottomBar_Like, v_UserProfile_BottomBar_Chat, v_UserProfile_BottomBar_Friend;
    RecyclerView rv_UserProfile_Info_Photo;
    Context mContext;

    UserProfilePhotoAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        mContext = getApplicationContext();

        CommonFunc.getInstance().mCurActivity = this;
        FirebaseManager.getInstance().RegistVisitUser(TKManager.getInstance().TargetUserData.GetUserIndex());

        ns_UserProfile_Scroll = findViewById(R.id.ns_UserProfile_Scroll);

        iv_UserProfile_Info_Profile = findViewById(R.id.iv_UserProfile_Profile);
        tv_UserProfile_Name = findViewById(R.id.tv_UserProfile_Name);
        tv_UserProfile_Info_Name = findViewById(R.id.tv_UserProfile_Info_Name);
        tv_UserProfile_Info_Age = findViewById(R.id.tv_UserProfile_Info_Age);
        tv_UserProfile_Info_Memo = findViewById(R.id.tv_UserProfile_Info_Memo);
        tv_UserProfile_Info_Count_Visit = findViewById(R.id.tv_UserProfile_Info_Count_Visit);
        tv_UserProfile_Info_Count_Like = findViewById(R.id.tv_UserProfile_Info_Count_Like);
        tv_UserProfile_Info_Count_Near = findViewById(R.id.tv_UserProfile_Info_Count_Near);
        tv_UserProfile_Info_Favorite = findViewById(R.id.tv_UserProfile_Info_Favorite);
        iv_UserProfile_Info_Gender = findViewById(R.id.iv_UserProfile_Info_Gender);
        v_UserProfile_TopBar = findViewById(R.id.v_UserProfile_TopBar);
        rv_UserProfile_Info_Photo = findViewById(R.id.rv_UserProfile_Info_Photo);

        iv_UserProfile_BottomBar_Like = findViewById(R.id.iv_UserProfile_BottomBar_Like);
        v_UserProfile_BottomBar_Like = findViewById(R.id.v_UserProfile_BottomBar_Like);
        v_UserProfile_BottomBar_Chat = findViewById(R.id.v_UserProfile_BottomBar_Chat);
        v_UserProfile_BottomBar_Friend  = findViewById(R.id.v_UserProfile_BottomBar_Friend);

        v_UserProfile_TopBar.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.alpha));
        tv_UserProfile_Name.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.alpha));

        ns_UserProfile_Scroll.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView var1, int x, int y, int oldx, int oldy) {
                Log.d("test", "test : " + CommonFunc.getInstance().convertPXtoDP(getResources(), y));
                if (CommonFunc.getInstance().convertPXtoDP(getResources(), y) < 50) {
                    v_UserProfile_TopBar.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.alpha));
                } else {
                    v_UserProfile_TopBar.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.topbar_bg));
                    tv_UserProfile_Name.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.black));
                }
            }
        });

        Glide.with(mContext).load(TKManager.getInstance().TargetUserData.GetUserImgThumb())
                .circleCrop()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(iv_UserProfile_Info_Profile);

        tv_UserProfile_Name.setText(TKManager.getInstance().TargetUserData.GetUserNickName());
        tv_UserProfile_Info_Name.setText(TKManager.getInstance().TargetUserData.GetUserNickName());
        tv_UserProfile_Info_Age.setText(TKManager.getInstance().TargetUserData.GetUserAge() + "세");
        //tv_UserProfile_Info_Memo.setText(TKManager.getInstance().TargetUserData.GetUserMemo());

        tv_UserProfile_Info_Count_Visit.setText("방문자 " + TKManager.getInstance().TargetUserData.GetUserTodayVisit() + " / " + TKManager.getInstance().TargetUserData.GetUserTotalVisit());
        tv_UserProfile_Info_Count_Like.setText("좋아요 " + TKManager.getInstance().TargetUserData.GetUserTodayLike() + " / " + TKManager.getInstance().TargetUserData.GetUserTotalLike());
        tv_UserProfile_Info_Count_Near.setText("거리 " + TKManager.getInstance().TargetUserData.GetUserDist() + " Km");

        tv_UserProfile_Info_Memo.setText(TKManager.getInstance().TargetUserData.GetUserMemo());

        iv_UserProfile_Info_Profile.setOnClickListener(new ImageView.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getId() == R.id.iv_UserProfile_Profile) {

                    Intent intent = new Intent(UserProfileActivity.this, CustomPhotoView.class);
                    intent.putExtra("ImgSrc",TKManager.getInstance().TargetUserData.GetUserImgThumb());
                    startActivity(intent);
                }
            }
        });

        v_UserProfile_BottomBar_Like.setOnClickListener(new ImageView.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getId() == R.id.v_UserProfile_BottomBar_Friend) {

                    DialogFunc.getInstance().SetShowLoadingPageMsg(UserProfileActivity.this);

                    if(TKManager.getInstance().TargetUserData.GetUserLikeList(TKManager.getInstance().MyData.GetUserIndex()) == null){

                        Glide.with(mContext).load(R.drawable.ic_like)
                                .into(iv_UserProfile_BottomBar_Like);
                    } else {


                        Glide.with(mContext).load(R.drawable.ic_like_empty)
                                .into(iv_UserProfile_BottomBar_Like);
                    }

                    tv_UserProfile_Info_Count_Like.setText("좋아요 " + TKManager.getInstance().TargetUserData.GetUserTodayLike() + " / " + TKManager.getInstance().TargetUserData.GetUserTotalLike());
                }
            }
        });



        v_UserProfile_BottomBar_Friend.setOnClickListener(new ImageView.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getId() == R.id.v_UserProfile_BottomBar_Like) {

                    DialogFunc.getInstance().SetShowLoadingPageMsg(UserProfileActivity.this);

                    if(TKManager.getInstance().TargetUserData.GetUserLikeList(TKManager.getInstance().MyData.GetUserIndex()) == null){
                        RefreshLikeCount(true);

                        Glide.with(mContext).load(R.drawable.ic_like)
                                .into(iv_UserProfile_BottomBar_Like);
                    } else {
                        TKManager.getInstance().MyData.DelUserLikeList(TKManager.getInstance().TargetUserData.GetUserIndex());

                        RefreshLikeCount(false);

                        Glide.with(mContext).load(R.drawable.ic_like_empty)
                                .into(iv_UserProfile_BottomBar_Like);
                    }

                    tv_UserProfile_Info_Count_Like.setText("좋아요 " + TKManager.getInstance().TargetUserData.GetUserTodayLike() + " / " + TKManager.getInstance().TargetUserData.GetUserTotalLike());
                }
            }
        });

        v_UserProfile_BottomBar_Chat.setOnClickListener(new ImageView.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getId() == R.id.v_UserProfile_BottomBar_Chat) {
                    int index = 11;
                    TKManager.getInstance().MyData.SetUserIndex(Integer.toString(index));
                    index ++;
                }

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


        if(TKManager.getInstance().TargetUserData.GetUserLikeList(TKManager.getInstance().MyData.GetUserIndex()) == null)
        {
            Glide.with(mContext).load(R.drawable.ic_like_empty)
                    .into(iv_UserProfile_BottomBar_Like);
        } else {
            Glide.with(mContext).load(R.drawable.ic_like)
                    .into(iv_UserProfile_BottomBar_Like);
        }

        initRecyclerView();

        /*








        iv_ThumbNail = findViewById(R.id.iv_UserProfile_ThumbNail);

        Glide.with(getApplicationContext())
                //.load(mMyData.arrSendDataList.get(position).strTargetImg)
                .load(TKManager.getInstance().TargetUserData.GetUserImgThumb())
                .circleCrop()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(iv_ThumbNail);

        tv_NickName = findViewById(R.id.tv_UserProfile_NickName);
        tv_NickName.setText(TKManager.getInstance().TargetUserData.GetUserNickName());

        iv_Back = findViewById(R.id.iv_UserProfile_Back);
        iv_Back.setOnClickListener(new ImageView.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(v.getId() ==R.id.iv_UserProfile_Back){
                    finish();
                }

            }
        });

        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bnv_UserProfile_main_menu);
        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                        switch (menuItem.getItemId()) {

                            case R.id.menuitem_bottombar_Like:
                                return true;

                            case R.id.menuitem_bottombar_Chat:
                                return true;

                            case R.id.menuitem_bottombar_Friend:
                                return true;
                        }
                        return false;
                    }

                });
*/
    }

    private void initRecyclerView() {
        mAdapter = new UserProfilePhotoAdapter(mContext);
        mAdapter.setHasStableIds(true);

        rv_UserProfile_Info_Photo.setAdapter(mAdapter);
        rv_UserProfile_Info_Photo.setLayoutManager(new GridLayoutManager(getApplicationContext(), 4));
        rv_UserProfile_Info_Photo.addOnItemTouchListener(new RecyclerItemClickListener(getApplicationContext(), rv_UserProfile_Info_Photo, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                //startActivity(new Intent(getApplicationContext(), ClubBodyActivity.class));
                //startActivity(new Intent(getApplicationContext(), UserProfileActivity.class));
                /*//CommonFunc.getInstance().ShowToast(view.getContext(), position+"번 째 아이템 클릭", true);
                if (mAppStatus.bCheckMultiSend == false) {
                    stTargetData = mMyData.arrUserAll_Hot_Age.get(position);

                    if (mCommon.getClickStatus() == false)
                        mCommon.MoveUserPage(getActivity(), stTargetData);
                }*/
            }

            @Override
            public void onLongItemClick(View view, int position) {
                //  Toast.makeText(getApplicationContext(),position+"번 째 아이템 롱 클릭",Toast.LENGTH_SHORT).show();
            }
        }));

        rv_UserProfile_Info_Photo.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                /*int lastVisibleItemPosition = ((LinearLayoutManager) recyclerView.getLayoutManager()).findLastCompletelyVisibleItemPosition();
                int nSize = 0;
                nSize = recyclerView.getAdapter().getItemCount() - 1;

                if (lastVisibleItemPosition == nSize) {
                    // Toast.makeText(getContext(), "Last Position", Toast.LENGTH_SHORT).show();
                    //    CommonFunc.getInstance().ShowLoadingPage(getContext(), "로딩중");
                    //  FirebaseData.getInstance().GetHotData(RecvAdapter, false);
                }*/
            }
        });
    }

    private void RefreshLikeCount(boolean like)
    {
        if(like)
        {
            TKManager.getInstance().TargetUserData.AddUserTotalLike(1);
            TKManager.getInstance().TargetUserData.AddUserTodayLike(1);
            TKManager.getInstance().TargetUserData.SetUserLikeList(TKManager.getInstance().MyData.GetUserIndex(), CommonFunc.getInstance().GetCurrentDate());

            FirebaseManager.getInstance().RegistLikeUser( TKManager.getInstance().TargetUserData.GetUserIndex());
        }
        else
        {
            TKManager.getInstance().TargetUserData.AddUserTotalLike(-1);
            TKManager.getInstance().TargetUserData.AddUserTodayLike(-1);
            TKManager.getInstance().TargetUserData.DelUserLikeList(TKManager.getInstance().MyData.GetUserIndex());

            FirebaseManager.getInstance().RemoveLikeUser( TKManager.getInstance().TargetUserData.GetUserIndex());
        }
    }
}
