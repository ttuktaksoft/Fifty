package fifty.fiftyhouse.com.fifty.activty;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import fifty.fiftyhouse.com.fifty.CommonFunc;
import fifty.fiftyhouse.com.fifty.MainActivity;
import fifty.fiftyhouse.com.fifty.Manager.TKManager;
import fifty.fiftyhouse.com.fifty.R;
import fifty.fiftyhouse.com.fifty.adapter.ClubFavoriteAdapter;
import fifty.fiftyhouse.com.fifty.adapter.UserListAdapter;
import fifty.fiftyhouse.com.fifty.util.OnRecyclerItemClickListener;
import fifty.fiftyhouse.com.fifty.util.OnSingleClickListener;
import fifty.fiftyhouse.com.fifty.util.RecyclerItemClickListener;

public class ClubFavoriteActivity extends AppCompatActivity {

    View ui_ClubFavorite_TopBar;
    TextView tv_TopBar_Title;
    ImageView iv_TopBar_Back;

    RecyclerView rv_ClubFavorite_List;
    ClubFavoriteAdapter mAdapter;
    ArrayList<String> mFavoriteList = new ArrayList<>();

    Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_club_favorite);
        mContext = getApplicationContext();

        ui_ClubFavorite_TopBar = findViewById(R.id.ui_ClubFavorite_TopBar);
        tv_TopBar_Title = ui_ClubFavorite_TopBar.findViewById(R.id.tv_TopBar_Title);
        iv_TopBar_Back = ui_ClubFavorite_TopBar.findViewById(R.id.iv_TopBar_Back);

        tv_TopBar_Title.setText(CommonFunc.getInstance().getStr(mContext.getResources(), R.string.TITLE_CLUB_FAVORITE));
        iv_TopBar_Back.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View view) {
                finish();
            }
        });

        rv_ClubFavorite_List = findViewById(R.id.rv_ClubFavorite_List);

        initRecyclerView();
    }

    private void initRecyclerView()
    {
        mAdapter = new ClubFavoriteAdapter(getApplicationContext());
        RefreshAdapter();
        mAdapter.setHasStableIds(true);

        rv_ClubFavorite_List.setAdapter(mAdapter);
        rv_ClubFavorite_List.setLayoutManager(new GridLayoutManager(getApplicationContext(), 3));
        rv_ClubFavorite_List.addOnItemTouchListener(new RecyclerItemClickListener(getApplicationContext(), rv_ClubFavorite_List, new OnRecyclerItemClickListener() {
            @Override
            public void onSingleClick(View view, int position) {

                String key = mFavoriteList.get(position);

                if(key.equals("plus"))
                {
                    // 관심사 변경
                }
                else
                {
                    startActivity(new Intent(getApplicationContext(), ClubListActivity.class));
                }
            }

            @Override
            public void onLongItemClick(View view, int position) {
                //  Toast.makeText(getApplicationContext(),position+"번 째 아이템 롱 클릭",Toast.LENGTH_SHORT).show();
            }
        }));
    }

    public void RefreshFavoriteList() {
        mFavoriteList.clear();
        //mFavoriteList.addAll(TKManager.getInstance().MyData.GetUserFriendListKeySet());


        // 마지막엔 관심사 추가
        mFavoriteList.add("관심사1");
        mFavoriteList.add("관심사2");
        mFavoriteList.add("관심사3");
        mFavoriteList.add("plus");
    }
    public void RefreshAdapter()
    {
        RefreshFavoriteList();
        mAdapter.setItemData(mFavoriteList);
        mAdapter.setItemCount(mFavoriteList.size());
    }
}
