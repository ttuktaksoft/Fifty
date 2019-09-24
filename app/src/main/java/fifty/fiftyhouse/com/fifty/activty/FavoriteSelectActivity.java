package fifty.fiftyhouse.com.fifty.activty;

import android.content.Context;
import android.content.Intent;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.RecyclerView;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.beloo.widget.chipslayoutmanager.ChipsLayoutManager;
import com.beloo.widget.chipslayoutmanager.gravity.IChildGravityResolver;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import fifty.fiftyhouse.com.fifty.CommonData;
import fifty.fiftyhouse.com.fifty.CommonFunc;
import fifty.fiftyhouse.com.fifty.DialogFunc;
import fifty.fiftyhouse.com.fifty.Manager.FirebaseManager;
import fifty.fiftyhouse.com.fifty.Manager.TKManager;
import fifty.fiftyhouse.com.fifty.R;
import fifty.fiftyhouse.com.fifty.adapter.FavoriteSelectViewAdapter;
import fifty.fiftyhouse.com.fifty.adapter.FavoriteViewAdapter;
import fifty.fiftyhouse.com.fifty.util.OnRecyclerItemClickListener;
import fifty.fiftyhouse.com.fifty.util.OnSingleClickListener;
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

    int nType = 0;

    int mFavoriteRecommend = 0;
    ArrayList<String> mFavoriteViewList = new ArrayList<>();
    Map<String, String> mFavoriteSelectList = new LinkedHashMap<String, String>() {
        @Override
        protected boolean removeEldestEntry(Entry<String, String> arg0) {
            return size() == CommonData.FavoriteSelectMaxCountCheck ? true : false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite_select);
        mContext = getApplicationContext();
        imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);

        ui_FavoriteSelect_TopBar = findViewById(R.id.ui_FavoriteSelect_TopBar);
        tv_TopBar_Title = ui_FavoriteSelect_TopBar.findViewById(R.id.tv_TopBar_Title);
        iv_TopBar_Back = ui_FavoriteSelect_TopBar.findViewById(R.id.iv_TopBar_Back);
        iv_FavoriteSelect_Search = findViewById(R.id.iv_FavoriteSelect_Search);
        rv_FavoriteSelect_Select = findViewById(R.id.rv_FavoriteSelect_Select);
        rv_FavoriteSelect_View = findViewById(R.id.rv_FavoriteSelect_View);
        et_FavoriteSelect_Search = findViewById(R.id.et_FavoriteSelect_Search);
        tv_FavoriteSelect_Ok = findViewById(R.id.tv_FavoriteSelect_Ok);
        tv_FavoriteSelect_Empty = findViewById(R.id.tv_FavoriteSelect_Empty);

        Intent intent = getIntent(); //getIntent()로 받을준비
        nType = getIntent().getIntExtra("Type", 0);

        switch (nType) {
            case 0:
                tv_TopBar_Title.setText(CommonFunc.getInstance().getStr(mContext.getResources(), R.string.TITLE_FAVORITE_SELECT));
                tv_FavoriteSelect_Ok.setText(CommonFunc.getInstance().getStr(mContext.getResources(), R.string.MSG_SAVE));
                mFavoriteSelectList.putAll(TKManager.getInstance().MyData.GetUserFavoriteList());
                break;
            case 1:
                tv_TopBar_Title.setText(CommonFunc.getInstance().getStr(mContext.getResources(), R.string.TITLE_FAVORITE_EDIT));
                tv_FavoriteSelect_Ok.setText(CommonFunc.getInstance().getStr(mContext.getResources(), R.string.MSG_SAVE));
                mFavoriteSelectList.putAll(TKManager.getInstance().MyData.GetUserFavoriteList());
                break;
            case 2:
                tv_TopBar_Title.setText(CommonFunc.getInstance().getStr(mContext.getResources(), R.string.TITLE_CLUB_FAVORITE_EDIT));
                tv_FavoriteSelect_Ok.setText(CommonFunc.getInstance().getStr(mContext.getResources(), R.string.MSG_NEXT));
                mFavoriteSelectList.putAll(TKManager.getInstance().CreateTempClubData.GetClubFavoriteList());
                break;
            case 3:
                tv_TopBar_Title.setText(CommonFunc.getInstance().getStr(mContext.getResources(), R.string.TITLE_CLUB_FAVORITE_EDIT));
                tv_FavoriteSelect_Ok.setText(CommonFunc.getInstance().getStr(mContext.getResources(), R.string.MSG_NEXT));
                mFavoriteSelectList.putAll(TKManager.getInstance().TargetClubData.GetClubFavoriteList());
                break;
        }

        RefreshOkButton();

        iv_TopBar_Back.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View view) {
                imm.hideSoftInputFromWindow(et_FavoriteSelect_Search.getWindowToken(), 0);
                et_FavoriteSelect_Search.setText("");
                finish();
            }
        });

        tv_FavoriteSelect_Ok.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View view) {
                if(nType == 2 || nType == 3)
                {
                    if(mFavoriteSelectList.size() < CommonData.ClubFavoriteSelectMinCount)
                    {
                        DialogFunc.getInstance().ShowMsgPopup(FavoriteSelectActivity.this, CommonFunc.getInstance().getStr(getResources(), R.string.MSG_FAVORITE_SELECT_LACK_1));
                        return;
                    }
                }
                else
                {
                    if(mFavoriteSelectList.size() < CommonData.FavoriteSelectMinCount)
                    {
                        DialogFunc.getInstance().ShowMsgPopup(FavoriteSelectActivity.this, CommonFunc.getInstance().getStr(getResources(), R.string.MSG_FAVORITE_SELECT_LACK_3));
                        return;
                    }
                }

                if (nType == 2) {

                    Set EntrySet = TKManager.getInstance().CreateTempClubData.ClubFavorite.keySet();
                    Iterator iterator = EntrySet.iterator();

                    EntrySet = mFavoriteSelectList.entrySet();
                    iterator = EntrySet.iterator();

                    ArrayList<String> tempFavorite = new ArrayList<>();

                    int FavoriteIndex = 0;
                    while (iterator.hasNext()) {

                        Map.Entry entry = (Map.Entry) iterator.next();
                        String key = (String) entry.getKey();
                        String value = (String) entry.getValue();
                        TKManager.getInstance().CreateTempClubData.AddClubFavorite(key, value);
                        FavoriteIndex++;
                    }
                    finish();

                }
                else if (nType == 3)
                {
                    Set EntrySet = TKManager.getInstance().TargetClubData.ClubFavorite.keySet();
                    Iterator iterator = EntrySet.iterator();

                    EntrySet = mFavoriteSelectList.entrySet();
                    iterator = EntrySet.iterator();

                    ArrayList<String> tempFavorite = new ArrayList<>();

                    int FavoriteIndex = 0;
                    while (iterator.hasNext()) {

                        Map.Entry entry = (Map.Entry) iterator.next();
                        String key = (String) entry.getKey();
                        String value = (String) entry.getValue();
                        TKManager.getInstance().TargetClubData.AddClubFavorite(key, value);
                        FavoriteIndex++;
                    }
                    finish();
                }
                else {
                    Set EntrySet = TKManager.getInstance().MyData.GetUserFavoriteListKeySet();
                    Iterator iterator = EntrySet.iterator();

                    while (iterator.hasNext()) {
                        String key = (String) iterator.next();
                        FirebaseManager.getInstance().RemoveFavoriteUser(key);
                    }

                    TKManager.getInstance().MyData.ClearUserFavorite();

                    EntrySet = mFavoriteSelectList.entrySet();
                    iterator = EntrySet.iterator();

                    ArrayList<String> tempFavorite = new ArrayList<>();

                    while (iterator.hasNext()) {
                        Map.Entry entry = (Map.Entry) iterator.next();
                        String key = (String) entry.getKey();
                        String value = (String) entry.getValue();
                        TKManager.getInstance().MyData.SetUserFavorite(key, value);
                        FirebaseManager.getInstance().SetUserFavoriteOnFireBase(TKManager.getInstance().MyData.GetUserFavoriteList(key), true);
                    }

                    finish();
                }
            }
        });

        iv_FavoriteSelect_Search.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View view) {
                imm.hideSoftInputFromWindow(et_FavoriteSelect_Search.getWindowToken(), 0);
                FavoriteSearch();
            }
        });

        CommonFunc.getInstance().setEditTextMaxSize(et_FavoriteSelect_Search, CommonData.FavoriteNameMaxSize);
        et_FavoriteSelect_Search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                switch (actionId) {
                    case EditorInfo.IME_ACTION_SEARCH:
                        // 검색 동작
                        imm.hideSoftInputFromWindow(et_FavoriteSelect_Search.getWindowToken(), 0);
                        FavoriteSearch();
                        break;
                    default:
                        // 기본 엔터키 동작
                        imm.hideSoftInputFromWindow(et_FavoriteSelect_Search.getWindowToken(), 0);
                        et_FavoriteSelect_Search.setText("");
                        return false;
                }
                return true;
            }
        });

        RefreshFavoriteSelectList();
        RefreshFavoriteViewList();
        RefreshFavoriteSelectViewListDesc();
    }

    public void FavoriteSearch()
    {
        if (CommonFunc.getInstance().CheckStringNull(et_FavoriteSelect_Search.getText().toString())) {
            DialogFunc.getInstance().ShowMsgPopup(FavoriteSelectActivity.this, CommonFunc.getInstance().getStr(getResources(), R.string.MSG_FAVORITE_SEARCH_EMPTY));
        }
        else if(et_FavoriteSelect_Search.getText().length() < CommonData.FavoriteNameMinSize)
        {
            DialogFunc.getInstance().ShowMsgPopup(FavoriteSelectActivity.this, CommonFunc.getInstance().getStr(getResources(), R.string.MSG_FAVORITE_NAME_LEAK));
        }else {
            String favorite = et_FavoriteSelect_Search.getText().toString();
            mFavoriteSelectList.put(favorite, favorite);
            RefreshFavoriteSelectViewListDesc();
            RefreshFavoriteSelectViewListSlot();
            RefreshOkButton();
            mSelectViewAdapter.notifyDataSetChanged();
            et_FavoriteSelect_Search.setText("");
        }
    }

    public void RefreshFavoriteSelectList() {
        mSelectViewAdapter = new FavoriteSelectViewAdapter(mContext);
        RefreshFavoriteSelectViewListSlot();
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

        OnRecyclerItemClickListener clickListener = new OnRecyclerItemClickListener() {
            @Override
            public void onSingleClick(View view, int position) {
                Set tempKey = mFavoriteSelectList.keySet();
                List array = new ArrayList(tempKey);

                String tempFavoriteSelect = mFavoriteSelectList.get(array.get(position).toString());
                mFavoriteSelectList.remove(tempFavoriteSelect);
                RefreshFavoriteSelectViewListDesc();
                RefreshFavoriteSelectViewListSlot();
                RefreshFavoriteViewListSlot();
                RefreshOkButton();
                mSelectViewAdapter.notifyDataSetChanged();
                mViewAdapter.notifyDataSetChanged();
            }

            @Override
            public void onLongItemClick(View view, int position) {
            }
        };
        clickListener.setClickInterval(10);

        rv_FavoriteSelect_Select.addOnItemTouchListener(new RecyclerItemClickListener(mContext, rv_FavoriteSelect_Select, clickListener));
    }

    public void RefreshFavoriteViewList() {
        mViewAdapter = new FavoriteViewAdapter(mContext);
        mViewAdapter.setSelectView(true);
        RefreshFavoriteViewListSlot();
        mViewAdapter.setHasStableIds(true);

        rv_FavoriteSelect_View.setAdapter(mViewAdapter);
        ChipsLayoutManager chipsLayoutManager = ChipsLayoutManager.newBuilder(mContext)
                .setChildGravity(Gravity.CENTER)
                .setMaxViewsInRow(4)
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

        OnRecyclerItemClickListener clickListener = new OnRecyclerItemClickListener() {
            @Override
            public void onSingleClick(View view, int position) {
                int itemCount = mViewAdapter.getItemCount();

                if (itemCount - 1 > position) {
                    String tempFavoriteSelect = mFavoriteViewList.get(position);
                    mFavoriteSelectList.put(tempFavoriteSelect, tempFavoriteSelect);
                    RefreshFavoriteSelectViewListDesc();
                    RefreshFavoriteSelectViewListSlot();
                    RefreshFavoriteViewListSlot();
                    RefreshOkButton();
                    mSelectViewAdapter.notifyDataSetChanged();
                    mViewAdapter.notifyDataSetChanged();
                } else {
                    // TODO 관심사 추천
                    mFavoriteRecommend++;
                    RefreshFavoriteViewListSlot();
                    mViewAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onLongItemClick(View view, int position) {
            }
        };

        clickListener.setClickInterval(10);

        rv_FavoriteSelect_View.addOnItemTouchListener(new RecyclerItemClickListener(mContext, rv_FavoriteSelect_View, clickListener));
    }

    private void RefreshFavoriteSelectViewListDesc() {
        if (mFavoriteSelectList.size() <= 0)
            tv_FavoriteSelect_Empty.setVisibility(View.VISIBLE);
        else
            tv_FavoriteSelect_Empty.setVisibility(View.GONE);
    }

    private void RefreshFavoriteViewListSlot() {
        mFavoriteViewList.clear();
        int viewCount = CommonData.Favorite_Pop_Count;
        while (viewCount > 0) {
            int min = mFavoriteRecommend * CommonData.Favorite_Pop_Count;
            int max = (mFavoriteRecommend + 1) * CommonData.Favorite_Pop_Count;
            int index = min;
            for (int i = min; i < max; i++) {
                if (i >= TKManager.getInstance().FavoriteLIst_Pop.size()) {
                    mFavoriteRecommend = 0;
                    break;
                }
                mFavoriteViewList.add(TKManager.getInstance().FavoriteLIst_Pop.get(i));
                viewCount--;

                if (viewCount <= 0)
                    break;
            }
        }

        mViewAdapter.addSlot(CommonFunc.getInstance().getStr(mContext.getResources(), R.string.MSG_FAVORITE_RECOMMEND));
        mViewAdapter.setItemCount(mFavoriteViewList.size());
        mViewAdapter.setItemData(mFavoriteViewList);


        ArrayList<String> list = new ArrayList<>();
        Set EntrySet = mFavoriteSelectList.entrySet();
        Iterator iterator = EntrySet.iterator();

        ArrayList<String> tempFavorite = new ArrayList<>();

        while (iterator.hasNext()) {
            Map.Entry entry = (Map.Entry) iterator.next();
            String key = (String) entry.getKey();
            String value = (String) entry.getValue();
            list.add(value);
        }

        mViewAdapter.setSelectItemData(list);

    }

    private void RefreshFavoriteSelectViewListSlot() {
        ArrayList<String> list = new ArrayList<>();
        Set EntrySet = mFavoriteSelectList.entrySet();
        Iterator iterator = EntrySet.iterator();

        ArrayList<String> tempFavorite = new ArrayList<>();

        while (iterator.hasNext()) {
            Map.Entry entry = (Map.Entry) iterator.next();
            String key = (String) entry.getKey();
            String value = (String) entry.getValue();
            list.add(value);
        }

        mSelectViewAdapter.setItemCount(list.size());
        mSelectViewAdapter.setItemData(list);
    }

    public void RefreshOkButton() {
        int selectBGColor = ContextCompat.getColor(mContext, R.color.button_enable);
        int selectSrtColor = ContextCompat.getColor(mContext, R.color.button_enable_str);
        int disableBGColor = ContextCompat.getColor(mContext, R.color.button_disable);
        int disableSrtColor = ContextCompat.getColor(mContext, R.color.button_disable_str);

        if(nType == 2 || nType == 3)
        {
            tv_FavoriteSelect_Ok.setBackgroundColor(mFavoriteSelectList.size() >= CommonData.ClubFavoriteSelectMinCount ? selectBGColor : disableBGColor);
            tv_FavoriteSelect_Ok.setTextColor(mFavoriteSelectList.size() >= CommonData.ClubFavoriteSelectMinCount ? selectSrtColor : disableSrtColor);
        }
        else
        {
            tv_FavoriteSelect_Ok.setBackgroundColor(mFavoriteSelectList.size() >= CommonData.FavoriteSelectMinCount ? selectBGColor : disableBGColor);
            tv_FavoriteSelect_Ok.setTextColor(mFavoriteSelectList.size() >= CommonData.FavoriteSelectMinCount ? selectSrtColor : disableSrtColor);
        }
    }
}
