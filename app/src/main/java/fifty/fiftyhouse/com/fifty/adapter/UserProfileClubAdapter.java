package fifty.fiftyhouse.com.fifty.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import fifty.fiftyhouse.com.fifty.R;

public class UserProfileClubAdapter extends RecyclerView.Adapter<UserProfileClubListHolder> {

    Context mContext;
    int mItemCount = 0;
    ArrayList<String> mItemList = new ArrayList<>();

    public UserProfileClubAdapter(Context context) {
        mContext = context;
    }

    @Override
    public UserProfileClubListHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.adapter_user_profile_club, parent, false);

        return new UserProfileClubListHolder(view);
    }



    @Override
    public void onBindViewHolder(UserProfileClubListHolder holder, final int position) {
        int i = position;
        holder.setData(i);
    }

    @Override
    public int getItemCount() {
        return 3;
        //return mMyData.arrChatTargetData.size();
    }

    public void setItemCount(int count)
    {
        mItemCount = count;
    }

    public void setItemData(ArrayList<String> list)
    {
        mItemList.clear();
        mItemList.addAll(list);
    }

}

class UserProfileClubListHolder extends RecyclerView.ViewHolder{

    ImageView iv_UserProfile_Club;
    TextView tv_UserProfile_Club;
    Context mContext;

    public UserProfileClubListHolder(View itemView) {
        super(itemView);
        mContext = itemView.getContext();

        iv_UserProfile_Club = itemView.findViewById(R.id.iv_UserProfile_Club);
        tv_UserProfile_Club = itemView.findViewById(R.id.tv_UserProfile_Club);
    }

    public void setData(int i)
    {
        if(i == 0)
        {
            tv_UserProfile_Club.setText("등산");
            Glide.with(mContext).load(R.drawable.dummy_1)
                    .centerCrop()
                    .into(iv_UserProfile_Club);
        }
        else if(i == 1)
        {
            tv_UserProfile_Club.setText("야구");
            Glide.with(mContext).load(R.drawable.dummy_2)
                    .centerCrop()
                    .into(iv_UserProfile_Club);
        }
        else
        {
            tv_UserProfile_Club.setText("축구");
            Glide.with(mContext).load(R.drawable.dummy_3)
                    .centerCrop()
                    .into(iv_UserProfile_Club);
        }
    }
}
