package fifty.fiftyhouse.com.fifty.viewHolder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import fifty.fiftyhouse.com.fifty.R;

public class MyProfileClubListHolder extends RecyclerView.ViewHolder{

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
