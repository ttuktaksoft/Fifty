package fifty.fiftyhouse.com.fifty.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import fifty.fiftyhouse.com.fifty.R;
import fifty.fiftyhouse.com.fifty.viewHolder.MyProfileEditMenuListHolder;
import fifty.fiftyhouse.com.fifty.viewHolder.MyProfileEditMenuAgeListHolder;

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