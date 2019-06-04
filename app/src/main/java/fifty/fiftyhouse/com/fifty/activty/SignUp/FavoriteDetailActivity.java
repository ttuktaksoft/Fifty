package fifty.fiftyhouse.com.fifty.activty.SignUp;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.beloo.widget.chipslayoutmanager.ChipsLayoutManager;
import com.beloo.widget.chipslayoutmanager.gravity.IChildGravityResolver;

import java.util.ArrayList;

import fifty.fiftyhouse.com.fifty.Manager.FirebaseManager;
import fifty.fiftyhouse.com.fifty.R;
import fifty.fiftyhouse.com.fifty.adapter.SignUpFavoriteFixAdapter;
import fifty.fiftyhouse.com.fifty.adapter.SignUpFavoriteSelectAdapter;
import fifty.fiftyhouse.com.fifty.adapter.SignUpFavoriteViewAdapter;
import fifty.fiftyhouse.com.fifty.util.RecyclerItemClickListener;

public class FavoriteDetailActivity extends AppCompatActivity {

    RecyclerView rv_SignUp_Favorite_Detail_Select, rv_SignUp_Favorite_Detail_List;
    EditText et_SignUp_Favorite_Detail_Search;
    ImageView iv_SignUp_Favorite_Detail_Recom, iv_SignUp_Favorite_Detail_Next;

    SignUpFavoriteSelectAdapter mSelectAdapter;
    SignUpFavoriteViewAdapter mViewAdapter;
    Context mContext;

    ArrayList<String> mSelectFavoriteList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite_detail);
        mContext = getApplicationContext();

        rv_SignUp_Favorite_Detail_Select = findViewById(R.id.rv_SignUp_Favorite_Detail_Select);
        rv_SignUp_Favorite_Detail_List = findViewById(R.id.rv_SignUp_Favorite_Detail_List);
        et_SignUp_Favorite_Detail_Search = findViewById(R.id.et_SignUp_Favorite_Detail_Search);
        iv_SignUp_Favorite_Detail_Recom = findViewById(R.id.iv_SignUp_Favorite_Detail_Recom);
        iv_SignUp_Favorite_Detail_Next = findViewById(R.id.iv_SignUp_Favorite_Detail_Next);

        iv_SignUp_Favorite_Detail_Next.setColorFilter(R.color.gray_light);

        iv_SignUp_Favorite_Detail_Next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mSelectFavoriteList.size() == 0)
                {
                    // TODO 관심사 선택 유도 팝업
                }
                else
                {
                    Intent intent = new Intent(getApplicationContext(), ProfileImgActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });

        iv_SignUp_Favorite_Detail_Recom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO 다른 관심사 추천
            }
        });

        RefreshFavoriteSelectList();
        RefreshFavoriteViewList();


        FirebaseManager.getInstance().GetPopFavoriteData();
    }

    public void RefreshFavoriteSelectList()
    {
        mSelectAdapter = new SignUpFavoriteSelectAdapter(mContext);
        mSelectAdapter.setHasStableIds(true);

        rv_SignUp_Favorite_Detail_Select.setAdapter(mSelectAdapter);
        ChipsLayoutManager chipsLayoutManager = ChipsLayoutManager.newBuilder(mContext)
                .setChildGravity(Gravity.CENTER)
                .setMaxViewsInRow(2)
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
        rv_SignUp_Favorite_Detail_Select.setLayoutManager(chipsLayoutManager);

        rv_SignUp_Favorite_Detail_Select.addOnItemTouchListener(new RecyclerItemClickListener(mContext, rv_SignUp_Favorite_Detail_Select, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                //startActivity(new Intent(getContext(), UserProfileActivity.class));

            }

            @Override
            public void onLongItemClick(View view, int position) {
                //  Toast.makeText(getApplicationContext(),position+"번 째 아이템 롱 클릭",Toast.LENGTH_SHORT).show();
            }
        }));
    }

    public void RefreshFavoriteViewList()
    {
        mViewAdapter = new SignUpFavoriteViewAdapter(mContext);
        mViewAdapter.setHasStableIds(true);

        rv_SignUp_Favorite_Detail_List.setAdapter(mViewAdapter);
        ChipsLayoutManager chipsLayoutManager = ChipsLayoutManager.newBuilder(mContext)
                .setChildGravity(Gravity.CENTER)
                .setMaxViewsInRow(2)
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
        rv_SignUp_Favorite_Detail_List.setLayoutManager(chipsLayoutManager);

        rv_SignUp_Favorite_Detail_List.addOnItemTouchListener(new RecyclerItemClickListener(mContext, rv_SignUp_Favorite_Detail_List, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                //startActivity(new Intent(getContext(), UserProfileActivity.class));

            }

            @Override
            public void onLongItemClick(View view, int position) {
                //  Toast.makeText(getApplicationContext(),position+"번 째 아이템 롱 클릭",Toast.LENGTH_SHORT).show();
            }
        }));
    }
}
