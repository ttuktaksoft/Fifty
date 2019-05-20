package fifty.fiftyhouse.com.fifty.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import fifty.fiftyhouse.com.fifty.CommonFunc;
import fifty.fiftyhouse.com.fifty.R;
import fifty.fiftyhouse.com.fifty.viewHolder.ClubContentListHolder;

public class ClubContentAdapter extends RecyclerView.Adapter<ClubContentListHolder> {

    Context mContext;
    public ClubContentAdapter(Context context) {
        mContext = context;
    }

    @Override
    public ClubContentListHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.adapter_club_content, parent, false);

        return new ClubContentListHolder(view);
    }



    @Override
    public void onBindViewHolder(ClubContentListHolder holder, final int position) {
        int i = position;

        Glide.with(mContext)
                //.load(mMyData.arrSendDataList.get(position).strTargetImg)
                .load(R.drawable.login_icon)
                .circleCrop()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .thumbnail(0.1f)
                .into(holder.iv_Club_Con_Profile);

        if(i == 0)
        {
            holder.tv_Club_Con_Desc.setText("aslkdjhfalksjdhflkasdjhflkasjdhflkadsjhflkasjdhflkajsdhflkajsdhfklajsdhflkajsdhf");
            holder.v_Club_Con_Reply_1.setVisibility(View.GONE);
            holder.iv_Club_Con_Reply_1_Profile.setVisibility(View.GONE);
            holder.tv_Club_Con_Reply_1_Nickname.setVisibility(View.GONE);
            holder.tv_Club_Con_Reply_1_Date.setVisibility(View.GONE);
            holder.tv_Club_Con_Reply_1_Desc.setVisibility(View.GONE);

            holder.v_Club_Con_Reply_2.setVisibility(View.GONE);
            holder.iv_Club_Con_Reply_2_Profile.setVisibility(View.GONE);
            holder.tv_Club_Con_Reply_2_Nickname.setVisibility(View.GONE);
            holder.tv_Club_Con_Reply_2_Date.setVisibility(View.GONE);
            holder.tv_Club_Con_Reply_2_Desc.setVisibility(View.GONE);
        }
        else if(i == 1)
        {
            holder.tv_Club_Con_Desc.setText("aslkdjhfalksjdhflkasdjhflkasjdh!!!f");
            holder.v_Club_Con_Reply_1.setVisibility(View.GONE);
            holder.iv_Club_Con_Reply_1_Profile.setVisibility(View.GONE);
            holder.tv_Club_Con_Reply_1_Nickname.setVisibility(View.GONE);
            holder.tv_Club_Con_Reply_1_Date.setVisibility(View.GONE);
            holder.tv_Club_Con_Reply_1_Desc.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return 10;
        //return mMyData.arrChatTargetData.size();
    }

}
