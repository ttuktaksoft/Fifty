package fifty.fiftyhouse.com.fifty.viewHolder;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import fifty.fiftyhouse.com.fifty.R;

public class ClubContentReplyListHolder extends RecyclerView.ViewHolder {

    public ImageView iv_Club_Con_Reply_Profile;
    public TextView tv_Club_Con_Reply_Nickname;
    public TextView tv_Club_Con_Reply_Date;
    public TextView tv_Club_Con_Reply_Desc;
    Context mContext;

    public ClubContentReplyListHolder(View itemView) {
        super(itemView);
        mContext = itemView.getContext();

        iv_Club_Con_Reply_Profile = itemView.findViewById(R.id.iv_Club_Con_Reply_Profile);
        tv_Club_Con_Reply_Nickname = itemView.findViewById(R.id.tv_Club_Con_Reply_Nickname);
        tv_Club_Con_Reply_Date = itemView.findViewById(R.id.tv_Club_Con_Reply_Date);
        tv_Club_Con_Reply_Desc = itemView.findViewById(R.id.tv_Club_Con_Reply_Desc);
    }

    public void setClubContentReply(int pos)
    {
        // TODO 클럽 내용 추가
        Glide.with(mContext)
                //.load(mMyData.arrSendDataList.get(position).strTargetImg)
                .load(R.drawable.dummy_13)
                .circleCrop()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .thumbnail(0.1f)
                .into(iv_Club_Con_Reply_Profile);
        tv_Club_Con_Reply_Nickname.setText("피프티하우스");
        tv_Club_Con_Reply_Date.setText("19-04-15");
        tv_Club_Con_Reply_Desc.setText("댓글 내용");
    }
}