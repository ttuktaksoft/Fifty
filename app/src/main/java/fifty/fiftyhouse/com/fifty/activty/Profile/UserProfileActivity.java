package fifty.fiftyhouse.com.fifty.activty.Profile;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import fifty.fiftyhouse.com.fifty.CommonData;
import fifty.fiftyhouse.com.fifty.CommonFunc;
import fifty.fiftyhouse.com.fifty.Manager.TKManager;
import fifty.fiftyhouse.com.fifty.R;
import fifty.fiftyhouse.com.fifty.activty.ClubBodyActivity;
import fifty.fiftyhouse.com.fifty.adapter.ClubContentAdapter;
import fifty.fiftyhouse.com.fifty.adapter.UserProfilePhotoAdapter;
import fifty.fiftyhouse.com.fifty.util.RecyclerItemClickListener;

public class UserProfileActivity extends AppCompatActivity {

    NestedScrollView ns_UserProfile_Scroll;
    TextView iv_UserProfile_Name, tv_UserProfile_Info_Name, tv_UserProfile_Info_Age, tv_UserProfile_Info_Memo, tv_UserProfile_Info_Count_Visit, tv_UserProfile_Info_Count_Like, tv_UserProfile_Info_Count_Near;
    ImageView iv_UserProfile_Info_Gender;
    ConstraintLayout v_UserProfile_TopBar;
    RecyclerView rv_UserProfile_Info_Photo;
    Context mContext;

    UserProfilePhotoAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        mContext = getApplicationContext();

        CommonFunc.getInstance().mCurActivity = this;

        ns_UserProfile_Scroll = findViewById(R.id.ns_UserProfile_Scroll);
        iv_UserProfile_Name = findViewById(R.id.iv_UserProfile_Name);
        tv_UserProfile_Info_Name = findViewById(R.id.tv_UserProfile_Info_Name);
        tv_UserProfile_Info_Age = findViewById(R.id.tv_UserProfile_Info_Age);
        tv_UserProfile_Info_Memo = findViewById(R.id.tv_UserProfile_Info_Memo);
        tv_UserProfile_Info_Count_Visit = findViewById(R.id.tv_UserProfile_Info_Count_Visit);
        tv_UserProfile_Info_Count_Like = findViewById(R.id.tv_UserProfile_Info_Count_Like);
        tv_UserProfile_Info_Count_Near = findViewById(R.id.tv_UserProfile_Info_Count_Near);
        iv_UserProfile_Info_Gender = findViewById(R.id.iv_UserProfile_Info_Gender);
        v_UserProfile_TopBar = findViewById(R.id.v_UserProfile_TopBar);
        rv_UserProfile_Info_Photo = findViewById(R.id.rv_UserProfile_Info_Photo);

        v_UserProfile_TopBar.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.alpha));
        iv_UserProfile_Name.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.alpha));

        ns_UserProfile_Scroll.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener(){
            @Override
            public void onScrollChange(NestedScrollView var1, int x, int y, int oldx, int oldy)
            {
                Log.d("test", "test : " + CommonFunc.getInstance().convertPXtoDP(getResources(), y));
                if(CommonFunc.getInstance().convertPXtoDP(getResources(), y) < 50)
                {
                    v_UserProfile_TopBar.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.alpha));
                    iv_UserProfile_Name.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.alpha));
                }
                else
                {
                    v_UserProfile_TopBar.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.baseColor));
                    iv_UserProfile_Name.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.black));
                }
            }
        });

        if(true)
        {
            Glide.with(mContext).load(R.drawable.ic_man_simple)
                    .into(iv_UserProfile_Info_Gender);
        }
        else
        {
            Glide.with(mContext).load(R.drawable.ic_woman_simple)
                    .into(iv_UserProfile_Info_Gender);
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

    private void initRecyclerView()
    {
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
}
