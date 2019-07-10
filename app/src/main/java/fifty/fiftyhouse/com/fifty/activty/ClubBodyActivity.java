package fifty.fiftyhouse.com.fifty.activty;

import android.content.Context;
import android.content.Intent;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import fifty.fiftyhouse.com.fifty.CommonFunc;
import fifty.fiftyhouse.com.fifty.DataBase.ClubContextData;
import fifty.fiftyhouse.com.fifty.Manager.TKManager;
import fifty.fiftyhouse.com.fifty.R;
import fifty.fiftyhouse.com.fifty.adapter.ClubBodyImgAdapter;
import fifty.fiftyhouse.com.fifty.adapter.ClubBodyReplyAdapter;
import fifty.fiftyhouse.com.fifty.adapter.ClubContentAdapter;
import fifty.fiftyhouse.com.fifty.util.OnRecyclerItemClickListener;
import fifty.fiftyhouse.com.fifty.util.RecyclerItemClickListener;

public class ClubBodyActivity extends AppCompatActivity {

    Toolbar tb_Club_Body_Toolbar;

    ImageView iv_Club_Body_Profile;
    TextView tv_Club_Body_Nickname, tv_Club_Body_Date, tv_Club_Body_Desc;
    RecyclerView rv_Club_Body_Img_List, rv_Club_Body_Reply_List;
    ClubBodyImgAdapter mImgAdapter;
    ClubBodyReplyAdapter mReplyAdapter;
    Context mContext;
    ClubContextData tempData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_club_body);
        mContext = getApplicationContext();

        Intent intent = getIntent(); //getIntent()로 받을준비
        int nPosition = getIntent().getIntExtra("position", 0);

        tempData = new ClubContextData();
        tempData = TKManager.getInstance().TargetClubData.GetClubContext(Integer.toString(nPosition));

        tb_Club_Body_Toolbar = findViewById(R.id.tb_Club_Body_Toolbar);
        iv_Club_Body_Profile = findViewById(R.id.iv_Club_Body_Profile);
        tv_Club_Body_Nickname = findViewById(R.id.tv_Club_Body_Nickname);
        tv_Club_Body_Date = findViewById(R.id.tv_Club_Body_Date);
        tv_Club_Body_Desc = findViewById(R.id.tv_Club_Body_Desc);
        rv_Club_Body_Img_List = findViewById(R.id.rv_Club_Body_Img_List);
        rv_Club_Body_Reply_List = findViewById(R.id.rv_Club_Body_Reply_List);

        tb_Club_Body_Toolbar.setNavigationIcon(R.drawable.icon_backarrow);
        tb_Club_Body_Toolbar.setTitle(TKManager.getInstance().TargetClubData.GetClubName());
        setSupportActionBar(tb_Club_Body_Toolbar);

        /*Glide.with(mContext)
                //.load(mMyData.arrSendDataList.get(position).strTargetImg)
                .load(R.drawable.login_icon)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .thumbnail(0.1f)
                .into(iv_Club_Body_Profile);
*/
        CommonFunc.getInstance().DrawImageByGlide(ClubBodyActivity.this, iv_Club_Body_Profile, TKManager.getInstance().UserData_Simple.get(tempData.GetWriterIndex()).GetUserImgThumb(), true);
        tv_Club_Body_Nickname.setText(TKManager.getInstance().UserData_Simple.get(tempData.GetWriterIndex()).GetUserNickName());
        tv_Club_Body_Date.setText(tempData.GetDate());
        tv_Club_Body_Desc.setText(tempData.GetContext());

        initRecyclerImgView();
        initRecyclerReplyView();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }

        return super.onOptionsItemSelected(item);
    }

    private void initRecyclerImgView()
    {
        mImgAdapter = new ClubBodyImgAdapter(getApplicationContext(), tempData);
        mImgAdapter.setImgCount(tempData.GetImgCount());
        mImgAdapter.setHasStableIds(true);

        rv_Club_Body_Img_List.setAdapter(mImgAdapter);
        rv_Club_Body_Img_List.setLayoutManager(new GridLayoutManager(getApplicationContext(), 1));
        rv_Club_Body_Img_List.addOnItemTouchListener(new RecyclerItemClickListener(getApplicationContext(), rv_Club_Body_Img_List, new OnRecyclerItemClickListener() {
            @Override
            public void onSingleClick(View view, int position) {
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

        rv_Club_Body_Img_List.addOnScrollListener(new RecyclerView.OnScrollListener() {
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

    private void initRecyclerReplyView()
    {
        mReplyAdapter = new ClubBodyReplyAdapter(getApplicationContext());
        mReplyAdapter.setReplyCount(tempData.GetReplyCount());
        mReplyAdapter.setHasStableIds(true);

        rv_Club_Body_Reply_List.setAdapter(mReplyAdapter);
        rv_Club_Body_Reply_List.setLayoutManager(new GridLayoutManager(getApplicationContext(), 1));
        rv_Club_Body_Reply_List.addOnItemTouchListener(new RecyclerItemClickListener(getApplicationContext(), rv_Club_Body_Reply_List, new OnRecyclerItemClickListener() {
            @Override
            public void onSingleClick(View view, int position) {
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

        rv_Club_Body_Reply_List.addOnScrollListener(new RecyclerView.OnScrollListener() {
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
