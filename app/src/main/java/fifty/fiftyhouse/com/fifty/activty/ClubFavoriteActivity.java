package fifty.fiftyhouse.com.fifty.activty;

import android.content.Context;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import fifty.fiftyhouse.com.fifty.CommonData;
import fifty.fiftyhouse.com.fifty.CommonFunc;
import fifty.fiftyhouse.com.fifty.DialogFunc;
import fifty.fiftyhouse.com.fifty.Manager.FirebaseManager;
import fifty.fiftyhouse.com.fifty.Manager.TKManager;
import fifty.fiftyhouse.com.fifty.R;
import fifty.fiftyhouse.com.fifty.adapter.ClubFavoriteAdapter;
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
            public void onSingleClick(View view, final int position) {

                final String key = mFavoriteList.get(position);

                if(key.equals("plus"))
                {
                    // 관심사 변경
                }
                else
                {
                    DialogFunc.getInstance().ShowLoadingPage(ClubFavoriteActivity.this);

                    FirebaseManager.CheckFirebaseComplete FavoriteClubData = new FirebaseManager.CheckFirebaseComplete() {
                        @Override
                        public void CompleteListener() {
                            DialogFunc.getInstance().DismissLoadingPage();

                            Intent intent = new Intent(getApplicationContext(), ClubListActivity.class);
                            intent.putExtra("Type", CommonData.CLUB_LIST_FAVORITE_SEARCH);
                            intent.putExtra("FAVORITE",key);
                            startActivity(intent);
                        }

                        @Override
                        public void CompleteListener_Yes() {

                        }

                        @Override
                        public void CompleteListener_No() {

                        }
                    };

                    FirebaseManager.getInstance().SearchClubListOnFavorite(key, FavoriteClubData);


                    //FirebaseManager.getInstance().GetUserFavoriteClubData(key, FavoriteClubData);


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
        mFavoriteList.addAll(TKManager.getInstance().FavoriteLIst_ClubList);
        mFavoriteList.add("plus");
    }
    public void RefreshAdapter()
    {
        RefreshFavoriteList();
        mAdapter.setItemData(mFavoriteList);
        mAdapter.setItemCount(mFavoriteList.size());
    }
}
