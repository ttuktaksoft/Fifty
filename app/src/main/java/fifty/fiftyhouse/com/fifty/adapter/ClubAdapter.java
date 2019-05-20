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

        view.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,CommonFunc.getInstance().getHeightByDevice()/7));
        return new ClubListHolder(view);
    }



    @Override
    public void onBindViewHolder(ClubListHolder holder, final int position) {
        int i = position;

        Glide.with(mContext)
                //.load(mMyData.arrSendDataList.get(position).strTargetImg)
                .load(R.drawable.login_icon)
                .circleCrop()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .thumbnail(0.1f)
                .into(holder.iv_Club_Profile);
    }

    @Override
    public int getItemCount() {
        return 10;
        //return mMyData.arrChatTargetData.size();
    }

}