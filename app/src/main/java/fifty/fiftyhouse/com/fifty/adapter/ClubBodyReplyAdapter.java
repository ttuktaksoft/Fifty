package fifty.fiftyhouse.com.fifty.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;

import fifty.fiftyhouse.com.fifty.R;

public class ClubBodyReplyAdapter extends RecyclerView.Adapter<ClubBodyReplyListHolder> {

    Context mContext;
    int mItemCount = 0;
    ArrayList<String> mItemList = new ArrayList<>();

    public ClubBodyReplyAdapter(Context context) {
        mContext = context;
    }

    @Override
    public ClubBodyReplyListHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.adapter_club_body_reply, parent, false);

        return new ClubBodyReplyListHolder(view);
    }

    @Override
    public void onBindViewHolder(ClubBodyReplyListHolder holder, final int position) {
        int i = position;
        holder.setClubBodyReply(i);
    }

    @Override
    public int getItemCount() {
        return mItemCount;
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

class ClubBodyReplyListHolder extends RecyclerView.ViewHolder {

    public ImageView iv_Club_Body_Reply_Profile;
    public TextView tv_Club_Body_Reply_Nickname;
    public TextView tv_Club_Body_Reply_Date;
    public TextView tv_Club_Body_Reply_Desc;
    Context mContext;

    public ClubBodyReplyListHolder(View itemView) {
        super(itemView);
        mContext = itemView.getContext();

        iv_Club_Body_Reply_Profile = itemView.findViewById(R.id.iv_Club_Body_Reply_Profile);
        tv_Club_Body_Reply_Nickname = itemView.findViewById(R.id.tv_Club_Body_Reply_Nickname);
        tv_Club_Body_Reply_Date = itemView.findViewById(R.id.tv_Club_Body_Reply_Date);
        tv_Club_Body_Reply_Desc = itemView.findViewById(R.id.tv_Club_Body_Reply_Desc);
    }

    public void setClubBodyReply(int pos)
    {
        // TODO 클럽 내용 추가
        if(pos == 0)
        {
            Glide.with(mContext)
                    //.load(mMyData.arrSendDataList.get(position).strTargetImg)
                    .load(R.drawable.dummy_4)
                    .circleCrop()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .thumbnail(0.1f)
                    .into(iv_Club_Body_Reply_Profile);
            tv_Club_Body_Reply_Nickname.setText("피프티하우스");
            tv_Club_Body_Reply_Date.setText("19-04-21");
            tv_Club_Body_Reply_Desc.setText("좋아요");
        }
        else
        {
            Glide.with(mContext)
                    //.load(mMyData.arrSendDataList.get(position).strTargetImg)
                    .load(R.drawable.man)
                    .circleCrop()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .thumbnail(0.1f)
                    .into(iv_Club_Body_Reply_Profile);
            tv_Club_Body_Reply_Nickname.setText("닉네임");
            tv_Club_Body_Reply_Date.setText("19-04-20");
            tv_Club_Body_Reply_Desc.setText("예뻐요");
        }
    }
}
