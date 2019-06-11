package fifty.fiftyhouse.com.fifty.activty;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.beloo.widget.chipslayoutmanager.ChipsLayoutManager;
import com.beloo.widget.chipslayoutmanager.gravity.IChildGravityResolver;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import fifty.fiftyhouse.com.fifty.CommonData;
import fifty.fiftyhouse.com.fifty.CommonFunc;
import fifty.fiftyhouse.com.fifty.DialogFunc;
import fifty.fiftyhouse.com.fifty.Manager.TKManager;
import fifty.fiftyhouse.com.fifty.R;
import fifty.fiftyhouse.com.fifty.activty.SignUp.ProfileImgActivity;
import fifty.fiftyhouse.com.fifty.adapter.FavoriteSelectViewAdapter;
import fifty.fiftyhouse.com.fifty.adapter.FavoriteViewAdapter;
import fifty.fiftyhouse.com.fifty.util.RecyclerItemClickListener;

public class FavoriteSelectActivity extends AppCompatActivity {

    View ui_FavoriteSelect_TopBar;
    TextView tv_TopBar_Title;
    ImageView iv_TopBar_Back;
    RecyclerView rv_FavoriteSelect_Select, rv_FavoriteSelect_View;
    EditText et_FavoriteSelect_Search;
    TextView tv_FavoriteSelect_Ok;

    FavoriteViewAdapter mViewAdapter;
    FavoriteSelectViewAdapter mSelectViewAdapter;
    Context mContext;

    boolean mIsSignUp = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite_select);
        mContext = getApplicationContext();

        ui_FavoriteSelect_TopBar = findViewById(R.id.ui_FavoriteSelect_TopBar);
        tv_TopBar_Title = ui_FavoriteSelect_TopBar.findViewById(R.id.tv_TopBar_Title);
        iv_TopBar_Back = ui_FavoriteSelect_TopBar.findViewById(R.id.iv_TopBar_Back);
        rv_FavoriteSelect_Select = findViewById(R.id.rv_FavoriteSelect_Select);
        rv_FavoriteSelect_View = findViewById(R.id.rv_FavoriteSelect_View);
        et_FavoriteSelect_Search = findViewById(R.id.et_FavoriteSelect_Search);
        tv_FavoriteSelect_Ok = findViewById(R.id.tv_FavoriteSelect_Ok);

        iv_TopBar_Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        tv_FavoriteSelect_Ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mIsSignUp)
                {
                    if(TKManager.MyData.GetUserFavoriteListCount() < 2)
                    {
                        DialogFunc.getInstance().ShowMsgPopup(mContext, CommonFunc.getInstance().getStr(getResources(), R.string.MSG_NOTICE), CommonFunc.getInstance().getStr(getResources(), R.string.FAVORITE_SELECT_LACK));
                    }
                    else
                    {
                        Intent intent = new Intent(getApplicationContext(), ProfileImgActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }

            }
        });

        RefreshFavoriteSelectList();
        RefreshFavoriteViewList();
    }

    public void RefreshFavoriteSelectList()
    {
        mSelectViewAdapter = new FavoriteSelectViewAdapter(mContext);
        mSelectViewAdapter.setHasStableIds(true);

        rv_FavoriteSelect_Select.setAdapter(mSelectViewAdapter);
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
        rv_FavoriteSelect_Select.setLayoutManager(chipsLayoutManager);

        rv_FavoriteSelect_Select.addOnItemTouchListener(new RecyclerItemClickListener(mContext, rv_FavoriteSelect_Select, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if(mIsSignUp)
                {
                    Set tempKey = TKManager.getInstance().MyData.GetUserFavoriteListKeySet();
                    List array = new ArrayList(tempKey);

                    String tempFavoriteSelect = TKManager.getInstance().MyData.GetUserFavoriteList(array.get(position).toString());
                    TKManager.getInstance().MyData.DelUserFavoriteList(tempFavoriteSelect);
                    mSelectViewAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onLongItemClick(View view, int position) {
            }
        }));
    }

    public void RefreshFavoriteViewList()
    {
        mViewAdapter = new FavoriteViewAdapter(mContext);
        mViewAdapter.setItemCount(CommonData.Favorite_Pop_Count);
        mViewAdapter.setItemData(TKManager.getInstance().FavoriteLIst_Pop);
        mViewAdapter.setHasStableIds(true);

        rv_FavoriteSelect_View.setAdapter(mViewAdapter);
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
        rv_FavoriteSelect_View.setLayoutManager(chipsLayoutManager);

        rv_FavoriteSelect_View.addOnItemTouchListener(new RecyclerItemClickListener(mContext, rv_FavoriteSelect_View, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if(mIsSignUp)
                {
                    String tempFavoriteSelect = TKManager.getInstance().FavoriteLIst_Pop.get(position);
                    TKManager.getInstance().MyData.SetUserFavorite(tempFavoriteSelect, tempFavoriteSelect);
                    mSelectViewAdapter.notifyDataSetChanged();


                    // TODO 다른 관심사 추천
                    // 관심사를 추천할때 마지막에 관심사 갱신 버튼 추가
                    /*final String strNickName = et_SignUp_Favorite_Detail_Search.getText().toString();
                    if(TextUtils.isEmpty(strNickName))
                    {

                    }
                    else
                    {
                        TKManager.getInstance().MyData.SetUserFavorite(strNickName, strNickName);
                        mSelectAdapter.notifyDataSetChanged();
                    }*/
                }
            }

            @Override
            public void onLongItemClick(View view, int position) {
            }
        }));
    }
}
