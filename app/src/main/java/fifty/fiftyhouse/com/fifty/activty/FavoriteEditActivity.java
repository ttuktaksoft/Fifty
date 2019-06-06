package fifty.fiftyhouse.com.fifty.activty;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.ImageViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;

import com.beloo.widget.chipslayoutmanager.ChipsLayoutManager;
import com.beloo.widget.chipslayoutmanager.gravity.IChildGravityResolver;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import fifty.fiftyhouse.com.fifty.CommonFunc;
import fifty.fiftyhouse.com.fifty.DialogFunc;
import fifty.fiftyhouse.com.fifty.Manager.TKManager;
import fifty.fiftyhouse.com.fifty.R;
import fifty.fiftyhouse.com.fifty.activty.SignUp.FavoriteDetailActivity;
import fifty.fiftyhouse.com.fifty.activty.SignUp.ProfileImgActivity;
import fifty.fiftyhouse.com.fifty.adapter.FavoriteEditSelectAdapter;
import fifty.fiftyhouse.com.fifty.adapter.FavoriteEditViewAdapter;
import fifty.fiftyhouse.com.fifty.adapter.SignUpFavoriteSelectAdapter;
import fifty.fiftyhouse.com.fifty.adapter.SignUpFavoriteViewAdapter;
import fifty.fiftyhouse.com.fifty.util.RecyclerItemClickListener;

public class FavoriteEditActivity extends AppCompatActivity{

    ImageView iv_Favorite_Edit_Back, iv_Favorite_Edit_Recom, iv_Favorite_Edit_Save;
    RecyclerView rv_Favorite_Edit_View_List, rv_Favorite_Edit_Select;
    FavoriteEditSelectAdapter mSelectAdapter;
    FavoriteEditViewAdapter mViewAdapter;
    Context mContext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite_edit);
        mContext = getApplicationContext();

        iv_Favorite_Edit_Back = findViewById(R.id.iv_Favorite_Edit_Back);
        iv_Favorite_Edit_Recom = findViewById(R.id.iv_Favorite_Edit_Recom);
        iv_Favorite_Edit_Save = findViewById(R.id.iv_Favorite_Edit_Save);
        rv_Favorite_Edit_View_List = findViewById(R.id.rv_Favorite_Edit_View_List);
        rv_Favorite_Edit_Select = findViewById(R.id.rv_Favorite_Edit_Select);

        iv_Favorite_Edit_Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        ImageViewCompat.setImageTintList(iv_Favorite_Edit_Save, ColorStateList.valueOf(ContextCompat.getColor(getApplicationContext(), R.color.gray_light)));
        iv_Favorite_Edit_Save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


            }
        });

        iv_Favorite_Edit_Recom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO 다른 관심사 추천
            }
        });

        RefreshFavoriteSelectList();
        RefreshFavoriteViewList();

    }

    public void RefreshFavoriteSelectList()
    {
        mSelectAdapter = new FavoriteEditSelectAdapter(mContext);
        mSelectAdapter.setHasStableIds(true);

        rv_Favorite_Edit_Select.setAdapter(mSelectAdapter);
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
        rv_Favorite_Edit_Select.setLayoutManager(chipsLayoutManager);

        rv_Favorite_Edit_Select.addOnItemTouchListener(new RecyclerItemClickListener(mContext, rv_Favorite_Edit_Select, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                //startActivity(new Intent(getContext(), UserProfileActivity.class));
/*                Set tempKey = TKManager.getInstance().MyData.GetUserFavoriteListKeySet();
                List array = new ArrayList(tempKey);

                String tempFavoriteSelect = TKManager.getInstance().MyData.GetUserFavoriteList(array.get(position).toString());
                TKManager.getInstance().MyData.DelUserFavoriteList(tempFavoriteSelect);
                mSelectAdapter.notifyDataSetChanged();*/
            }

            @Override
            public void onLongItemClick(View view, int position) {
                //  Toast.makeText(getApplicationContext(),position+"번 째 아이템 롱 클릭",Toast.LENGTH_SHORT).show();
            }
        }));
    }

    public void RefreshFavoriteViewList()
    {
        mViewAdapter = new FavoriteEditViewAdapter(mContext);
        mViewAdapter.setHasStableIds(true);

        rv_Favorite_Edit_View_List.setAdapter(mViewAdapter);
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
        rv_Favorite_Edit_View_List.setLayoutManager(chipsLayoutManager);

        rv_Favorite_Edit_View_List.addOnItemTouchListener(new RecyclerItemClickListener(mContext, rv_Favorite_Edit_View_List, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                //startActivity(new Intent(getContext(), UserProfileActivity.class));
/*                String tempFavoriteSelect = TKManager.getInstance().FavoriteLIst_Pop.get(position);

                TKManager.getInstance().MyData.SetUserFavorite(tempFavoriteSelect, tempFavoriteSelect);

              *//*  if(TKManager.getInstance().MyData.GetUserFavoriteListCount() >= 2)
                {
                    TKManager.getInstance().MyData.SetUserFavorite(tempFavoriteSelect, tempFavoriteSelect);
                }
                else
                {
                    TKManager.getInstance().MyData.SetUserFavorite(tempFavoriteSelect, tempFavoriteSelect);
                }*//*
                mSelectAdapter.notifyDataSetChanged();*/
            }

            @Override
            public void onLongItemClick(View view, int position) {
                //  Toast.makeText(getApplicationContext(),position+"번 째 아이템 롱 클릭",Toast.LENGTH_SHORT).show();
            }
        }));
    }
}
