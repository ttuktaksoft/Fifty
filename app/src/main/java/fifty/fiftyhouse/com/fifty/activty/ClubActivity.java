package fifty.fiftyhouse.com.fifty.activty;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.beloo.widget.chipslayoutmanager.ChipsLayoutManager;
import com.beloo.widget.chipslayoutmanager.gravity.IChildGravityResolver;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import fifty.fiftyhouse.com.fifty.CommonData;
import fifty.fiftyhouse.com.fifty.CommonFunc;
import fifty.fiftyhouse.com.fifty.DataBase.ClubContextData;
import fifty.fiftyhouse.com.fifty.DialogFunc;
import fifty.fiftyhouse.com.fifty.Manager.FirebaseManager;
import fifty.fiftyhouse.com.fifty.Manager.TKManager;
import fifty.fiftyhouse.com.fifty.R;
import fifty.fiftyhouse.com.fifty.adapter.ClubContentAdapter;
import fifty.fiftyhouse.com.fifty.adapter.FavoriteViewAdapter;
import fifty.fiftyhouse.com.fifty.util.OnRecyclerItemClickListener;
import fifty.fiftyhouse.com.fifty.util.OnSingleClickListener;
import fifty.fiftyhouse.com.fifty.util.RecyclerItemClickListener;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

public class ClubActivity extends AppCompatActivity {

    View ui_Club_TopBar;
    TextView tv_TopBar_Title;
    ImageView iv_TopBar_Back;

    ImageView iv_Club_Thumbnail, iv_Club_Write, iv_Club_UserCount, iv_Club_Setting;
    TextView tv_Club_Name, tv_Club_UserCount, tv_Club_Join;
    RecyclerView rv_Club_Content;
    RecyclerView rv_Club_Favorite;
    ClubContentAdapter mAdapter;
    FavoriteViewAdapter mFavoriteViewAdapter;

    Context mContext;
    boolean mIsJoinClub = false;
    boolean mIsMasterClub = false;
    public static Activity mClubActivity;
    ArrayList<String> mContentList = new ArrayList<>();
    ArrayList<String> mFavoriteList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_club);
        mClubActivity = this;

        mContext = getApplicationContext();

        ui_Club_TopBar = findViewById(R.id.ui_Club_TopBar);
        tv_TopBar_Title = ui_Club_TopBar.findViewById(R.id.tv_TopBar_Title);
        iv_TopBar_Back = ui_Club_TopBar.findViewById(R.id.iv_TopBar_Back);
        iv_Club_Thumbnail = findViewById(R.id.iv_Club_Thumbnail);
        iv_Club_Write = findViewById(R.id.iv_Club_Write);
        tv_Club_Name = findViewById(R.id.tv_Club_Name);
        tv_Club_UserCount = findViewById(R.id.tv_Club_UserCount);
        rv_Club_Content = findViewById(R.id.rv_Club_Content);
        rv_Club_Favorite = findViewById(R.id.rv_Club_Favorite);
        iv_Club_UserCount = findViewById(R.id.iv_Club_UserCount);
        tv_Club_Join = findViewById(R.id.tv_Club_Join);
        iv_Club_Setting = findViewById(R.id.iv_Club_Setting);

        iv_TopBar_Back.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                finish();
            }
        });

        iv_Club_Write.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View view) {

                // 글쓰는 엑티비티로 이동
                Intent intent = new Intent(mContext, ClubWriteActivity.class);
                intent.putExtra("Type", 0);
                intent.putExtra("position", 0);
                startActivityForResult(intent, 1000);
/*

                Random random = new Random();
                int tempRange = random.nextInt(3);



                ClubContextData tempData = new ClubContextData();
                tempData.SetContext("asdasd");
                tempData.SetContextType(tempRange);
                if(tempRange == 1)
                {
                    tempData.SetImg("0", TKManager.getInstance().MyData.GetUserImgThumb());
                }
                if( tempRange == 2)
                {
                    tempData.SetImg("0", TKManager.getInstance().MyData.GetUserImgThumb());
                    tempData.SetImg("1", TKManager.getInstance().MyData.GetUserImgThumb());

                }

                tempData.SetDate(CommonFunc.getInstance().GetCurrentTime());
                tempData.SetWriterIndex(TKManager.getInstance().MyData.GetUserIndex());

                FirebaseManager.CheckFirebaseComplete listener = new FirebaseManager.CheckFirebaseComplete() {
                    @Override
                    public void CompleteListener() {
                        mAdapter.notifyDataSetChanged();

                    }

                    @Override
                    public void CompleteListener_Yes() {
                    }

                    @Override
                    public void CompleteListener_No() {
                        DialogFunc.getInstance().DismissLoadingPage();
                    }
                };

                FirebaseManager.getInstance().RegistClubContext(TKManager.getInstance().TargetClubData.GetClubIndex(), tempData,  listener);
*/


            }
        });

        iv_Club_UserCount.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View view) {
                Intent intent = new Intent(mContext, UserListActivity.class);
                intent.putExtra("Type",CommonData.USER_LIST_CLUB);
                startActivity(intent);
            }
        });



        tv_Club_Join.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View view) {

                if(CommonFunc.getInstance().CheckStringNull(TKManager.getInstance().TargetClubData.GetClubMember(TKManager.getInstance().MyData.GetUserIndex())))
                {
                    // 가입 대기가 아닌경우
                    final DialogFunc.MsgPopupListener listenerYes = new DialogFunc.MsgPopupListener() {
                        @Override
                        public void Listener() {
                            // 가입 신청
                            RefreshJoinStr();
                        }
                    };

                    DialogFunc.getInstance().ShowMsgPopup(ClubActivity.this, listenerYes, null, CommonFunc.getInstance().getStr(ClubActivity.this.getResources(), R.string.MSG_CLUB_JOIN_REQUEST_DESC),
                            CommonFunc.getInstance().getStr(ClubActivity.this.getResources(), R.string.MSG_CLUB_JOIN_REQUEST), CommonFunc.getInstance().getStr(ClubActivity.this.getResources(), R.string.MSG_CANCEL));
                }
                else
                {
                    final DialogFunc.MsgPopupListener listenerYes = new DialogFunc.MsgPopupListener() {
                        @Override
                        public void Listener() {
                            // 가입 신청 취소
                            RefreshJoinStr();
                        }
                    };

                    DialogFunc.getInstance().ShowMsgPopup(ClubActivity.this, listenerYes, null, CommonFunc.getInstance().getStr(ClubActivity.this.getResources(), R.string.MSG_CLUB_JOIN_REQUEST_CANCEL_DESC),
                            CommonFunc.getInstance().getStr(ClubActivity.this.getResources(), R.string.MSG_CLUB_JOIN_REQUEST), CommonFunc.getInstance().getStr(ClubActivity.this.getResources(), R.string.MSG_CANCEL));
                }
            }
        });

        iv_Club_Setting.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View view) {
                Intent intent = new Intent(mContext, ClubSettingActivity.class);
                startActivityForResult(intent, 1001);
            }
        });

        iv_Club_Thumbnail.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View view) {
                Intent intent = new Intent(mContext, CustomPhotoView.class);
                intent.putExtra("ImgSrc",TKManager.getInstance().TargetClubData.GetClubThumb());
                intent.putExtra("Type", CustomPhotoView.PHOTO_VIEW_TYPE_ONE);
                startActivity(intent);
            }
        });

        TKManager.getInstance().mUpdateClubActivityFunc = new TKManager.UpdateUIFunc(){
            @Override
            public void UpdateUI() {
                RefreshAdapter();
                mAdapter.notifyDataSetChanged();
            }
        };

        mIsJoinClub = TKManager.getInstance().TargetClubData.GetClubMember(TKManager.getInstance().MyData.GetUserIndex()) != null;
        mIsMasterClub = TKManager.getInstance().TargetClubData.GetClubMasterIndex().equals(TKManager.getInstance().MyData.GetUserIndex());

        if(mIsJoinClub)
        {
            // 가입한 클럽
            tv_Club_Join.setVisibility(View.GONE);
            iv_Club_Write.setVisibility(View.VISIBLE);
            iv_Club_Setting.setVisibility(View.VISIBLE);
        }
        else
        {
            tv_Club_Join.setVisibility(View.VISIBLE);
            iv_Club_Write.setVisibility(View.GONE);
            iv_Club_Setting.setVisibility(View.GONE);
        }

        RefreshJoinStr();
        RefreshClubInfo();
        initRecyclerView();
        initFavoriteList();
    }

    public void RefreshJoinStr()
    {
        if(CommonFunc.getInstance().CheckStringNull(TKManager.getInstance().TargetClubData.GetClubMember(TKManager.getInstance().MyData.GetUserIndex())))
        {
            // 가입 대기가 아닌경우
            tv_Club_Join.setText(CommonFunc.getInstance().getStr(ClubActivity.this.getResources(), R.string.MSG_CLUB_JOIN_REQUEST));
        }
        else
        {
            tv_Club_Join.setText(CommonFunc.getInstance().getStr(ClubActivity.this.getResources(), R.string.MSG_CLUB_JOIN_REQUEST_CANCEL));
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.club_top_menu, menu);
        return true;
    }

    private void initRecyclerView()
    {
        mAdapter = new ClubContentAdapter(getApplicationContext());
        RefreshAdapter();
        mAdapter.setHasStableIds(true);

        rv_Club_Content.setAdapter(mAdapter);
        rv_Club_Content.setLayoutManager(new LinearLayoutManager(this));
        /*rv_Club_Content.addOnItemTouchListener(new RecyclerItemClickListener(getApplicationContext(), rv_Club_Content, new OnRecyclerItemClickListener() {
            @Override
            public void onSingleClick(View view, int position) {

            }

            @Override
            public void onLongItemClick(View view, int position) {
                //  Toast.makeText(getApplicationContext(),position+"번 째 아이템 롱 클릭",Toast.LENGTH_SHORT).show();
            }
        }));*/
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1000)
        {
            RefreshAdapter();
            mAdapter.notifyDataSetChanged();
        }
        else if (requestCode == 1001)
        {
            RefreshClubInfo();
            mFavoriteViewAdapter.notifyDataSetChanged();
        }
    }

    public void initFavoriteList()
    {
        mFavoriteViewAdapter = new FavoriteViewAdapter(mContext);
        RefreshFavoriteAdapter();
        mFavoriteViewAdapter.setHasStableIds(true);

        rv_Club_Favorite.setAdapter(mFavoriteViewAdapter);
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
        rv_Club_Favorite.setLayoutManager(chipsLayoutManager);
    }

    public void RefreshAdapter()
    {
        mContentList.clear();
        mContentList.addAll(TKManager.getInstance().TargetClubData.GetClubContextKeySet());
        mAdapter.setItemData(mContentList);
    }

    public void RefreshFavoriteAdapter()
    {
        mFavoriteList.clear();
        Iterator<String> iterator = TKManager.getInstance().TargetClubData.GetClubFavoriteKeySet().iterator();
        while(iterator.hasNext()){
            String key = iterator.next();
            mFavoriteList.add(TKManager.getInstance().TargetClubData.GetClubFavorite(key));
        }

        mFavoriteViewAdapter.setItemCount(mFavoriteList.size());
        mFavoriteViewAdapter.setItemData(mFavoriteList);
    }

    public void RefreshClubInfo()
    {
        if(TKManager.getInstance().TargetClubData.GetClubType())
        {
            tv_Club_UserCount.setText("공개 "+TKManager.getInstance().TargetClubData.GetClubMemberCount() + "명" );
        }
        else
        {
            tv_Club_UserCount.setText("비공개 "+TKManager.getInstance().TargetClubData.GetClubMemberCount() + "명" );
        }

        CommonFunc.getInstance().DrawImageByGlide(ClubActivity.this, iv_Club_Thumbnail, TKManager.getInstance().TargetClubData.GetClubThumb(), false);

        tv_Club_Name.setText(TKManager.getInstance().TargetClubData.GetClubName());
        tv_TopBar_Title.setText(TKManager.getInstance().TargetClubData.GetClubName());
    }
}
