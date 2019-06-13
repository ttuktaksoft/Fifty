package fifty.fiftyhouse.com.fifty.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

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

    public static int MY_PROFILE_EDIT_MENU_NICKNAME_INDEX = 0;
    public static int MY_PROFILE_EDIT_MENU_STORY_INDEX = 2;
    public static int MY_PROFILE_EDIT_MENU_NICKNAME_AGE = 3;
    public static int MY_PROFILE_EDIT_MENU_LOC_INDEX = 4;
    public static int MY_PROFILE_EDIT_MENU_FAVORITE_INDEX = 1;

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
        if(position == MY_PROFILE_EDIT_MENU_NICKNAME_AGE)
            return MY_PROFILE_EDIT_MENU_TYPE_AGE;

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
            if(true)
            {
                // 있음
                tv_MyProfile_Edit_Menu_Title.setText(CommonFunc.getInstance().getStr(mContext.getResources(), R.string.MY_PROFILE_STORY));
                tv_MyProfile_Edit_Menu_Desc.setTextColor(ContextCompat.getColor(mContext, R.color.black));

                if(CommonFunc.getInstance().CheckStringNull(TKManager.getInstance().MyData.GetUserMemo()) == false) {

                    if (TKManager.getInstance().MyData.GetUserMemo().length() >= 20)
                        tv_MyProfile_Edit_Menu_Desc.setText(TKManager.getInstance().MyData.GetUserMemo().substring(0, 20));
                    else
                        tv_MyProfile_Edit_Menu_Desc.setText(TKManager.getInstance().MyData.GetUserMemo());
                }
            }
            else
            {
                //없음
                tv_MyProfile_Edit_Menu_Title.setText(CommonFunc.getInstance().getStr(mContext.getResources(), R.string.MY_PROFILE_STORY));
                tv_MyProfile_Edit_Menu_Desc.setTextColor(ContextCompat.getColor(mContext, R.color.gray_light));
                tv_MyProfile_Edit_Menu_Desc.setText(CommonFunc.getInstance().getStr(mContext.getResources(), R.string.MY_PROFILE_STORY_HINT));
            }
        }
        else if(i == MyProfileEditMenuAdapter.MY_PROFILE_EDIT_MENU_LOC_INDEX)
        {
            // 지역
            if(true)
            {
                // 있음
                tv_MyProfile_Edit_Menu_Title.setText(CommonFunc.getInstance().getStr(mContext.getResources(), R.string.MY_PROFILE_LOCATION));
                tv_MyProfile_Edit_Menu_Desc.setTextColor(ContextCompat.getColor(mContext, R.color.black));
                tv_MyProfile_Edit_Menu_Desc.setText("지역설정");
            }
            else
            {
                //없음
                tv_MyProfile_Edit_Menu_Title.setText(CommonFunc.getInstance().getStr(mContext.getResources(), R.string.MY_PROFILE_LOCATION));
                tv_MyProfile_Edit_Menu_Desc.setTextColor(ContextCompat.getColor(mContext, R.color.gray_light));
                tv_MyProfile_Edit_Menu_Desc.setText(CommonFunc.getInstance().getStr(mContext.getResources(), R.string.MY_PROFILE_LOCATION_HINT));
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