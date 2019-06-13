package fifty.fiftyhouse.com.fifty.activty;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
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
import fifty.fiftyhouse.com.fifty.adapter.FavoriteSelectViewAdapter;
import fifty.fiftyhouse.com.fifty.adapter.FavoriteViewAdapter;
import fifty.fiftyhouse.com.fifty.util.RecyclerItemClickListener;

public class FavoriteSelectActivity extends AppCompatActivity {

    View ui_FavoriteSelect_TopBar;
    TextView tv_TopBar_Title;
    ImageView iv_TopBar_Back, iv_FavoriteSelect_Search;
    RecyclerView rv_FavoriteSelect_Select, rv_FavoriteSelect_View;
    EditText et_FavoriteSelect_Search;
    TextView tv_FavoriteSelect_Ok, tv_FavoriteSelect_Empty;

    FavoriteViewAdapter mViewAdapter;
    FavoriteSelectViewAdapter mSelectViewAdapter;
    Context mContext;
    InputMethodManager imm;
    CommonData.FavoriteSelectType mType = CommonData.FavoriteSelectType.SIGNUP;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite_select);
        mContext = getApplicationContext();
        imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);

        ui_FavoriteSelect_TopBar = findViewById(R.id.ui_FavoriteSelect_TopBar);
        tv_TopBar_Title = ui_FavoriteSelect_TopBar.findViewById(R.id.tv_TopBar_Title);
        iv_TopBar_Back = ui_FavoriteSelect_TopBar.findViewById(R.id.iv_TopBar_Back);
        iv_FavoriteSelect_Search  = findViewById(R.id.iv_FavoriteSelect_Search);
        rv_FavoriteSelect_Select = findViewById(R.id.rv_FavoriteSelect_Select);
        rv_FavoriteSelect_View = findViewById(R.id.rv_FavoriteSelect_View);
        et_FavoriteSelect_Search = findViewById(R.id.et_FavoriteSelect_Search);
        tv_FavoriteSelect_Ok = findViewById(R.id.tv_FavoriteSelect_Ok);
        tv_FavoriteSelect_Empty = findViewById(R.id.tv_FavoriteSelect_Empty);

        Intent intent = getIntent(); //getIntent()로 받을준비
        int ntype = getIntent().getIntExtra("Type", 0);

        switch (ntype)
        {
            case 0:
                mType = CommonData.FavoriteSelectType.SIGNUP;
                tv_TopBar_Title.setText(CommonFunc.getInstance().getStr(mContext.getResources(), R.string.TITLE_FAVORITE_SELECT));
                break;
            case 1:
                mType = CommonData.FavoriteSelectType.EDIT;
                tv_TopBar_Title.setText(CommonFunc.getInstance().getStr(mContext.getResources(), R.string.TITLE_FAVORITE_EDIT));
                break;
        }

        iv_TopBar_Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        tv_FavoriteSelect_Ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mType == CommonData.FavoriteSelectType.SIGNUP)
                {
                    if(TKManager.MyData.GetUserFavoriteListCount() < 2)
                    {
                        DialogFunc.getInstance().ShowMsgPopup(FavoriteSelectActivity.this, CommonFunc.getInstance().getStr(getResources(), R.string.FAVORITE_SELECT_LACK));
                    }
                    else
                    {
                        finish();
                    }
                }

            }
        });

        iv_FavoriteSelect_Search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imm.hideSoftInputFromWindow(et_FavoriteSelect_Search.getWindowToken(), 0);
                if(CommonFunc.getInstance().CheckStringNull(et_FavoriteSelect_Search.getText().toString()))
                {
                    DialogFunc.getInstance().ShowMsgPopup(FavoriteSelectActivity.this, CommonFunc.getInstance().getStr(getResources(), R.string.FAVORITE_SELECT_SEARCH_EMPTY));
                }
                else
                {
                    String favorite = et_FavoriteSelect_Search.getText().toString();
                    TKManager.getInstance().MyData.SetUserFavorite(favorite, favorite);
                    RefreshFavoriteSelectViewListDesc();
                    mSelectViewAdapter.notifyDataSetChanged();
                }
            }
        });

        RefreshFavoriteSelectList();
        RefreshFavoriteViewList();
        RefreshFavoriteSelectViewListDesc();
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
                if(mType == CommonData.FavoriteSelectType.SIGNUP)
                {
                    Set tempKey = TKManager.getInstance().MyData.GetUserFavoriteListKeySet();
                    List array = new ArrayList(tempKey);

                    String tempFavoriteSelect = TKManager.getInstance().MyData.GetUserFavoriteList(array.get(position).toString());
                    TKManager.getInstance().MyData.DelUserFavoriteList(tempFavoriteSelect);

                    RefreshFavoriteSelectViewListDesc();

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
        RefreshFavoriteViewListSlot();
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
                if(mType == CommonData.FavoriteSelectType.SIGNUP)
                {
                    int itemCount = mViewAdapter.getItemCount();

                    if(itemCount - 1 > position)
                    {
                        String tempFavoriteSelect = TKManager.getInstance().FavoriteLIst_Pop.get(position);
                        TKManager.getInstance().MyData.SetUserFavorite(tempFavoriteSelect, tempFavoriteSelect);

                        RefreshFavoriteSelectViewListDesc();

                        mSelectViewAdapter.notifyDataSetChanged();
                    }
                    else
                    {
                        // TODO 관심사 추천
                        RefreshFavoriteViewListSlot();
                        mViewAdapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onLongItemClick(View view, int position) {
            }
        }));
    }

    private void RefreshFavoriteSelectViewListDesc()
    {
        if(TKManager.getInstance().MyData.GetUserFavoriteList().size() <= 0)
            tv_FavoriteSelect_Empty.setVisibility(View.VISIBLE);
        else
            tv_FavoriteSelect_Empty.setVisibility(View.GONE);
    }

    private void RefreshFavoriteViewListSlot()
    {
        if(mType == CommonData.FavoriteSelectType.SIGNUP)
            mViewAdapter.addSlot(CommonFunc.getInstance().getStr(mContext.getResources(), R.string.MSG_FAVORITE_RECOMMEND));

        mViewAdapter.setItemCount(CommonData.Favorite_Pop_Count);
        mViewAdapter.setItemData(TKManager.getInstance().FavoriteLIst_Pop);
    }
}
