package fifty.fiftyhouse.com.fifty.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import fifty.fiftyhouse.com.fifty.R;

public class MyProfileClubAdapter extends RecyclerView.Adapter<MyProfileClubListHolder> {

    Context mContext;
    public MyProfileClubAdapter(Context context) {
        mContext = context;
    }

    @Override
    public MyProfileClubListHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.adapter_myprofile_club, parent, false);

        return new MyProfileClubListHolder(view);
    }



    @Override
    public void onBindViewHolder(MyProfileClubListHolder holder, final int position) {
        int i = position;
        holder.setData(i);
    }

    @Override
    public int getItemCount() {
        return 3;
        //return mMyData.arrChatTargetData.size();
    }

}

class MyProfileClubListHolder extends RecyclerView.ViewHolder{

    ImageView iv_MyProfile_Club;
    TextView tv_MyProfile_Club;
    Context mContext;

    public MyProfileClubListHolder(View itemView) {
        super(itemView);
        mContext = itemView.getContext();

        iv_MyProfile_Club = itemView.findViewById(R.id.iv_MyProfile_Club);
        tv_MyProfile_Club = itemView.findViewById(R.id.tv_MyProfile_Club);
    }

    public void setData(int i)
    {
        if(i == 0)
        {
            tv_MyProfile_Club.setText("등산");
            Glide.with(mContext).load(R.drawable.dummy_1)
                    .centerCrop()
                    .into(iv_MyProfile_Club);
        }
        else if(i == 1)
        {
            tv_MyProfile_Club.setText("야구");
            Glide.with(mContext).load(R.drawable.dummy_2)
                    .centerCrop()
                    .into(iv_MyProfile_Club);
        }
        else
        {
            tv_MyProfile_Club.setText("축구");
            Glide.with(mContext).load(R.drawable.dummy_3)
                    .centerCrop()
                    .into(iv_MyProfile_Club);
        }
    }
}
