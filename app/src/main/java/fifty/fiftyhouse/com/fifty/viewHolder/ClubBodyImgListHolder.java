package fifty.fiftyhouse.com.fifty.viewHolder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import fifty.fiftyhouse.com.fifty.R;

public class ClubBodyImgListHolder extends RecyclerView.ViewHolder {

    public ImageView iv_Club_Body_Img;
    Context mContext;

    public ClubBodyImgListHolder(View itemView) {
        super(itemView);
        mContext = itemView.getContext();

        iv_Club_Body_Img = itemView.findViewById(R.id.iv_Club_Body_Img);
    }

    public void setClubBodyImg(int pos)
    {
        // TODO 클럽 내용 추가
        if(pos == 0)
        {
            Glide.with(mContext)
                    //.load(mMyData.arrSendDataList.get(position).strTargetImg)
                    .load(R.drawable.login_icon)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .thumbnail(0.1f)
                    .into(iv_Club_Body_Img);
        }
        else if(pos == 1)
        {
            Glide.with(mContext)
                    //.load(mMyData.arrSendDataList.get(position).strTargetImg)
                    .load(R.drawable.woman)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .thumbnail(0.1f)
                    .into(iv_Club_Body_Img);
        }
        else
        {
            Glide.with(mContext)
                    //.load(mMyData.arrSendDataList.get(position).strTargetImg)
                    .load(R.drawable.man)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .thumbnail(0.1f)
                    .into(iv_Club_Body_Img);
        }
    }
}
