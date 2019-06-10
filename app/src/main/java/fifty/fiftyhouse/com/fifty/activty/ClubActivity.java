package fifty.fiftyhouse.com.fifty.activty;

import android.content.Intent;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import fifty.fiftyhouse.com.fifty.CommonFunc;
import fifty.fiftyhouse.com.fifty.R;
import fifty.fiftyhouse.com.fifty.activty.Profile.UserProfileActivity;
import fifty.fiftyhouse.com.fifty.adapter.ClubContentAdapter;
import fifty.fiftyhouse.com.fifty.adapter.MainAdapter;
import fifty.fiftyhouse.com.fifty.util.RecyclerItemClickListener;

public class ClubActivity extends AppCompatActivity {

    Toolbar v_Club_ToolBar;
    NestedScrollView ns_Club_Scroll;

    ImageView iv_Club_Thumbnail;
    TextView tv_Club_Name;
    RecyclerView rv_Club_Content;
    ClubContentAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_club);

        v_Club_ToolBar = findViewById(R.id.v_Club_ToolBar);
        ns_Club_Scroll = findViewById(R.id.ns_Club_Scroll);
        iv_Club_Thumbnail = findViewById(R.id.iv_Club_Thumbnail);
        tv_Club_Name = findViewById(R.id.tv_Club_Name);
        rv_Club_Content = findViewById(R.id.rv_Club_Content);

        v_Club_ToolBar.setNavigationIcon(R.drawable.icon_backarrow);
        v_Club_ToolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        v_Club_ToolBar.setTitle("냥냥 클럽");
        setSupportActionBar(v_Club_ToolBar);

        v_Club_ToolBar.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.alpha));
        v_Club_ToolBar.setTitleTextColor(ContextCompat.getColor(getApplicationContext(), R.color.alpha));

        ns_Club_Scroll.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener(){
            @Override
            public void onScrollChange(NestedScrollView var1, int x, int y, int oldx, int oldy)
            {
                Log.d("test", "test : " + CommonFunc.getInstance().convertPXtoDP(getResources(), y));
                if(CommonFunc.getInstance().convertPXtoDP(getResources(), y) < 240)
                {
                    v_Club_ToolBar.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.alpha));
                    v_Club_ToolBar.setTitleTextColor(ContextCompat.getColor(getApplicationContext(), R.color.alpha));
                }
                else
                {
                    v_Club_ToolBar.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.topbar_bg));
                    v_Club_ToolBar.setTitleTextColor(ContextCompat.getColor(getApplicationContext(), R.color.black));
                }
            }
        });

        initRecyclerView();
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
        mAdapter.setHasStableIds(true);

        rv_Club_Content.setAdapter(mAdapter);
        rv_Club_Content.setLayoutManager(new GridLayoutManager(getApplicationContext(), 1));
        rv_Club_Content.addOnItemTouchListener(new RecyclerItemClickListener(getApplicationContext(), rv_Club_Content, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                startActivity(new Intent(getApplicationContext(), ClubBodyActivity.class));
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

        rv_Club_Content.addOnScrollListener(new RecyclerView.OnScrollListener() {
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
