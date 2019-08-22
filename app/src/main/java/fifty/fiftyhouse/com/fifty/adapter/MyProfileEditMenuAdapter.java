package fifty.fiftyhouse.com.fifty.adapter;

import android.content.Context;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.beloo.widget.chipslayoutmanager.ChipsLayoutManager;
import com.beloo.widget.chipslayoutmanager.gravity.IChildGravityResolver;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import fifty.fiftyhouse.com.fifty.CommonFunc;
import fifty.fiftyhouse.com.fifty.Manager.TKManager;
import fifty.fiftyhouse.com.fifty.R;

public class MyProfileEditMenuAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public static int MY_PROFILE_EDIT_MENU_TYPE_DEFAULT = 0;
    public static int MY_PROFILE_EDIT_MENU_TYPE_AGE = 1;
    public static int MY_PROFILE_EDIT_MENU_TYPE_FAVORITE = 2;

    public static int MY_PROFILE_EDIT_MENU_NICKNAME_INDEX = 0;
    public static int MY_PROFILE_EDIT_MENU_FAVORITE_INDEX = 1;
    public static int MY_PROFILE_EDIT_MENU_STORY_INDEX = 2;
    public static int MY_PROFILE_EDIT_MENU_AGE_INDEX = 3;
    public static int MY_PROFILE_EDIT_MENU_LOC_INDEX = 4;


    Context mContext;

    public MyProfileEditMenuAdapter(Context context) {
        mContext = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if(viewType == MY_PROFILE_EDIT_MENU_TYPE_AGE)
        {
            View view = LayoutInflater.from(mContext).inflate(R.layout.adapter_my_profile_edit_age, parent, false);

            return new MyProfileEditMenuAgeListHolder(view);
        }
        else if(viewType == MY_PROFILE_EDIT_MENU_TYPE_FAVORITE)
        {
            View view = LayoutInflater.from(mContext).inflate(R.layout.adapter_my_profile_edit_favorite, parent, false);

            return new MyProfileEditMenuFavoriteListHolder(view);
        }
        else
        {
            View view = LayoutInflater.from(mContext).inflate(R.layout.adapter_my_profile_edit_default, parent, false);

            return new MyProfileEditMenuListHolder(view);
        }
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        int i = position;
        if(holder instanceof MyProfileEditMenuAgeListHolder)
        {
            MyProfileEditMenuAgeListHolder AgeHolder = (MyProfileEditMenuAgeListHolder)holder;
            AgeHolder.setData();
        }
        else if(holder instanceof MyProfileEditMenuFavoriteListHolder)
        {
            MyProfileEditMenuFavoriteListHolder FavoriteHolder = (MyProfileEditMenuFavoriteListHolder)holder;
            FavoriteHolder.setData();
        }
        else
        {
            MyProfileEditMenuListHolder DefaultHolder = (MyProfileEditMenuListHolder)holder;
            DefaultHolder.setData(i);
        }
    }

    @Override
    public int getItemViewType(int position) {
        // Just as an example, return 0 or 2 depending on position
        // Note that unlike in ListView adapters, types don't have to be contiguous
        if(position == MY_PROFILE_EDIT_MENU_AGE_INDEX)
            return MY_PROFILE_EDIT_MENU_TYPE_AGE;
        else if(position == MY_PROFILE_EDIT_MENU_FAVORITE_INDEX)
            return MY_PROFILE_EDIT_MENU_TYPE_FAVORITE;

        return MY_PROFILE_EDIT_MENU_TYPE_DEFAULT;
    }

    @Override
    public int getItemCount() {
        return 5;
        //return mMyData.arrChatTargetData.size();
    }
}

class MyProfileEditMenuAgeListHolder extends RecyclerView.ViewHolder {
    ImageView tv_MyProfile_Edit_Age_Menu_Gender;
    TextView tv_MyProfile_Edit_Age_Menu_Age;
    Context mContext;

    public MyProfileEditMenuAgeListHolder(View itemView) {
        super(itemView);
        mContext = itemView.getContext();

        tv_MyProfile_Edit_Age_Menu_Gender = itemView.findViewById(R.id.tv_MyProfile_Edit_Age_Menu_Gender);
        tv_MyProfile_Edit_Age_Menu_Age = itemView.findViewById(R.id.tv_MyProfile_Edit_Age_Menu_Age);

    }

    public void setData()
    {
        if (true) {
            Glide.with(mContext).load(R.drawable.ic_man_simple)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(tv_MyProfile_Edit_Age_Menu_Gender);
        } else {
            Glide.with(mContext).load(R.drawable.ic_woman_simple)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(tv_MyProfile_Edit_Age_Menu_Gender);
        }
        tv_MyProfile_Edit_Age_Menu_Age.setText(TKManager.getInstance().MyData.GetUserAge() + "세");
    }
}

class MyProfileEditMenuFavoriteListHolder extends RecyclerView.ViewHolder {
    RecyclerView rv_MyProfile_Edit_Favorite_Menu;
    Context mContext;
    FavoriteSlotAdapter mFavoriteAdapter;

    public MyProfileEditMenuFavoriteListHolder(View itemView) {
        super(itemView);
        mContext = itemView.getContext();
    }

    public void setData()
    {
        rv_MyProfile_Edit_Favorite_Menu = itemView.findViewById(R.id.rv_MyProfile_Edit_Favorite_Menu);

        initFavoriteList();
    }

    public void initFavoriteList()
    {
        if(mFavoriteAdapter == null)
        {
            mFavoriteAdapter = new FavoriteSlotAdapter(mContext);
            RefreshFavoriteViewListSlot();
            mFavoriteAdapter.setHasStableIds(true);

            rv_MyProfile_Edit_Favorite_Menu.setAdapter(mFavoriteAdapter);
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
            rv_MyProfile_Edit_Favorite_Menu.setLayoutManager(chipsLayoutManager);
        }
        else
            RefreshFavoriteViewListSlot();
    }

    private void RefreshFavoriteViewListSlot()
    {
        ArrayList<String> list = new ArrayList<>();

        Map<String, String> tempMapFavorite = TKManager.getInstance().MyData.GetUserFavoriteList();
        Set EntrySet = tempMapFavorite.entrySet();
        Iterator iterator = EntrySet.iterator();

        while(iterator.hasNext()){
            Map.Entry entry = (Map.Entry)iterator.next();
            String key = (String)entry.getKey();
            String value = (String)entry.getValue();
            list.add(value);
        }

        //mFavoriteAdapter.setItemCount(list.size());
        mFavoriteAdapter.setItemData(list);
        mFavoriteAdapter.notifyDataSetChanged();
    }
}

class MyProfileEditMenuListHolder extends RecyclerView.ViewHolder {

    TextView tv_MyProfile_Edit_Menu_Title;
    TextView tv_MyProfile_Edit_Menu_Desc;

    Context mContext;
    public MyProfileEditMenuListHolder(View itemView) {
        super(itemView);
        mContext = itemView.getContext();

        tv_MyProfile_Edit_Menu_Title = itemView.findViewById(R.id.tv_MyProfile_Edit_Menu_Title);
        tv_MyProfile_Edit_Menu_Desc = itemView.findViewById(R.id.tv_MyProfile_Edit_Menu_Desc);

    }

    public void setData(int i)
    {
        if(i == MyProfileEditMenuAdapter.MY_PROFILE_EDIT_MENU_NICKNAME_INDEX)
        {
            // 닉네임
            tv_MyProfile_Edit_Menu_Title.setText(CommonFunc.getInstance().getStr(mContext.getResources(), R.string.MY_PROFILE_NIKCNMAE));
            tv_MyProfile_Edit_Menu_Desc.setText(TKManager.getInstance().MyData.GetUserNickName());
        }
        else if(i == MyProfileEditMenuAdapter.MY_PROFILE_EDIT_MENU_STORY_INDEX)
        {
            // 자기소개
            if(CommonFunc.getInstance().CheckStringNull(TKManager.getInstance().MyData.GetUserMemo()) == false)
            {
                // 있음
                tv_MyProfile_Edit_Menu_Title.setText(CommonFunc.getInstance().getStr(mContext.getResources(), R.string.TITLE_MEMO));
                tv_MyProfile_Edit_Menu_Desc.setText(TKManager.getInstance().MyData.GetUserMemo());
            }
            else
            {
                //없음
                tv_MyProfile_Edit_Menu_Title.setText(CommonFunc.getInstance().getStr(mContext.getResources(), R.string.TITLE_MEMO));
                tv_MyProfile_Edit_Menu_Desc.setText(CommonFunc.getInstance().getStr(mContext.getResources(), R.string.MSG_DEFAULT_LOCATION_MEMO_1) + " " +TKManager.getInstance().MyData.GetUserNickName() + CommonFunc.getInstance().getStr(mContext.getResources(), R.string.MSG_DEFAULT_LOCATION_MEMO_2));
            }
        }
        else if(i == MyProfileEditMenuAdapter.MY_PROFILE_EDIT_MENU_LOC_INDEX)
        {
            // 지역
            if(CommonFunc.getInstance().CheckStringNull(TKManager.getInstance().MyData.GetUserLocation()) == false)
            {
                // 있음
                tv_MyProfile_Edit_Menu_Title.setText(CommonFunc.getInstance().getStr(mContext.getResources(), R.string.MY_PROFILE_LOCATION));
                tv_MyProfile_Edit_Menu_Desc.setText(TKManager.getInstance().MyData.GetUserLocation());
            }
            else
            {
                //없음
                tv_MyProfile_Edit_Menu_Title.setText(CommonFunc.getInstance().getStr(mContext.getResources(), R.string.MY_PROFILE_LOCATION));
//                tv_MyProfile_Edit_Menu_Desc.setText(CommonFunc.getInstance().getStr(mContext.getResources(), R.string.MSG_DEFAULT_LOCATION));
                tv_MyProfile_Edit_Menu_Desc.setText(TKManager.getInstance().MyData.GetUserDist_Area());

            }
        }
        else if(i == MyProfileEditMenuAdapter.MY_PROFILE_EDIT_MENU_FAVORITE_INDEX)
        {
            tv_MyProfile_Edit_Menu_Title.setText(CommonFunc.getInstance().getStr(mContext.getResources(), R.string.MY_PROFILE_FAVORITE));

            Map<String, String> tempMapFavorite = TKManager.getInstance().MyData.GetUserFavoriteList();
            Set EntrySet = tempMapFavorite.entrySet();
            Iterator iterator = EntrySet.iterator();

            ArrayList<String> tempFavorite = new ArrayList<>();

            while(iterator.hasNext()){
                Map.Entry entry = (Map.Entry)iterator.next();
                String key = (String)entry.getKey();
                String value = (String)entry.getValue();
                tempFavorite.add(value);
            }
            if(tempFavorite.size() >= 2)
            {
                tv_MyProfile_Edit_Menu_Desc.setText(tempFavorite.get(0) + ", " + tempFavorite.get(1));
            }

        }
    }
}