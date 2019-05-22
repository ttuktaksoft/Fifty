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
import fifty.fiftyhouse.com.fifty.viewHolder.ClubListHolder;

public class ClubAdapter extends RecyclerView.Adapter<ClubListHolder> {

    Context mContext;
    public ClubAdapter(Context context) {
        mContext = context;
    }

    @Override
    public ClubListHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.adapter_club, parent, false);

        view.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,CommonFunc.getInstance().getHeightByDevice()/4));
        return new ClubListHolder(view);
    }



    @Override
    public void onBindViewHolder(ClubListHolder holder, final int position) {
        int i = position;

        if(position == 0)
        {
            holder.tv_Club_Name.setText("축구밴드");
            Glide.with(mContext)
                    //.load(mMyData.arrSendDataList.get(position).strTargetImg)
                    .load(R.drawable.dummy_10)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .thumbnail(0.1f)
                    .into(holder.iv_Club_Profile);
        }
        else if(position == 1)
        {
            holder.tv_Club_Name.setText("야구밴드");
            Glide.with(mContext)
                    //.load(mMyData.arrSendDataList.get(position).strTargetImg)
                    .load(R.drawable.dummy_10)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .thumbnail(0.1f)
                    .into(holder.iv_Club_Profile);
        }
        else if(position == 2)
        {
            holder.tv_Club_Name.setText("줌마들의 나들이");
            Glide.with(mContext)
                    //.load(mMyData.arrSendDataList.get(position).strTargetImg)
                    .load(R.drawable.dummy_12)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .thumbnail(0.1f)
                    .into(holder.iv_Club_Profile);
        }
        else if(position == 3)
        {
            holder.tv_Club_Name.setText("먹거리 수비대");
            Glide.with(mContext)
                    //.load(mMyData.arrSendDataList.get(position).strTargetImg)
                    .load(R.drawable.dummy_2)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .thumbnail(0.1f)
                    .into(holder.iv_Club_Profile);
        }
        else if(position == 4)
        {
            holder.tv_Club_Name.setText("여행을 떠나요");
            Glide.with(mContext)
                    //.load(mMyData.arrSendDataList.get(position).strTargetImg)
                    .load(R.drawable.dummy_6)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .thumbnail(0.1f)
                    .into(holder.iv_Club_Profile);
        }
        else if(position == 5)
        {
            holder.tv_Club_Name.setText("골프 칠사람");
            Glide.with(mContext)
                    //.load(mMyData.arrSendDataList.get(position).strTargetImg)
                    .load(R.drawable.dummy_8)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .thumbnail(0.1f)
                    .into(holder.iv_Club_Profile);
        }
        else
        {
            Glide.with(mContext)
                    //.load(mMyData.arrSendDataList.get(position).strTargetImg)
                    .load(R.drawable.dummy_13)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .thumbnail(0.1f)
                    .into(holder.iv_Club_Profile);
        }
    }

    @Override
    public int getItemCount() {
        return 5;
        //return mMyData.arrChatTargetData.size();
    }

}