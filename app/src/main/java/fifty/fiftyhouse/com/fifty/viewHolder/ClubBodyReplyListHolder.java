package fifty.fiftyhouse.com.fifty.viewHolder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import fifty.fiftyhouse.com.fifty.R;

public class ClubBodyReplyListHolder extends RecyclerView.ViewHolder {

    public ImageView iv_Club_Con_Reply_Profile;
    public TextView tv_Club_Con_Reply_Nickname;
    public TextView tv_Club_Con_Reply_Date;
    public TextView tv_Club_Con_Reply_Desc;
    Context mContext;

    public ClubBodyReplyListHolder(View itemView) {
        super(itemView);
        mContext = itemView.getContext();

        iv_Club_Con_Reply_Profile = itemView.findViewById(R.id.iv_Club_Con_Reply_Profile);
        tv_Club_Con_Reply_Nickname = itemView.findViewById(R.id.tv_Club_Con_Reply_Nickname);
        tv_Club_Con_Reply_Date = itemView.findViewById(R.id.tv_Club_Con_Reply_Date);
        tv_Club_Con_Reply_Desc = itemView.findViewById(R.id.tv_Club_Con_Reply_Desc);
    }

    public void setClubBodyReply(int pos)
    {
        // TODO 클럽 내용 추가
        if(pos == 0)
        {
            Glide.with(mContext)
                    //.load(mMyData.arrSendDataList.get(position).strTargetImg)
                    .load(R.drawable.login_icon)
                    .circleCrop()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .thumbnail(0.1f)
                    .into(iv_Club_Con_Reply_Profile);
            tv_Club_Con_Reply_Nickname.setText("닉네임입니다_1");
            tv_Club_Con_Reply_Date.setText("지금막");
            tv_Club_Con_Reply_Desc.setText("내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용");
        }
        else
        {
            Glide.with(mContext)
                    //.load(mMyData.arrSendDataList.get(position).strTargetImg)
                    .load(R.drawable.login_icon)
                    .circleCrop()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .thumbnail(0.1f)
                    .into(iv_Club_Con_Reply_Profile);
            tv_Club_Con_Reply_Nickname.setText("닉네임입니다_2");
            tv_Club_Con_Reply_Date.setText("지금막");
            tv_Club_Con_Reply_Desc.setText("내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용");
        }
    }
}
