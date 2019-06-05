package fifty.fiftyhouse.com.fifty.viewHolder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import fifty.fiftyhouse.com.fifty.R;

public class MyProfileEtcListHolder extends RecyclerView.ViewHolder {

    ImageView iv_MyProfile_Etc;
    TextView tv_MyProfile_Etc;
    Context mContext;
    public MyProfileEtcListHolder(View itemView) {
        super(itemView);
        mContext = itemView.getContext();

        iv_MyProfile_Etc = itemView.findViewById(R.id.iv_MyProfile_Etc);
        tv_MyProfile_Etc = itemView.findViewById(R.id.tv_MyProfile_Etc);
    }

    public void setData(int i)
    {
        if(i == 0)
        {
            tv_MyProfile_Etc.setText("이벤트");
            Glide.with(mContext).load(R.drawable.dummy_1)
                    .centerCrop()
                    .into(iv_MyProfile_Etc);
        }
        else if(i == 1)
        {
            tv_MyProfile_Etc.setText("공지사항");
            Glide.with(mContext).load(R.drawable.dummy_2)
                    .centerCrop()
                    .into(iv_MyProfile_Etc);
        }
        else
        {
            tv_MyProfile_Etc.setText("FAQ");
            Glide.with(mContext).load(R.drawable.dummy_3)
                    .centerCrop()
                    .into(iv_MyProfile_Etc);
        }
    }
}