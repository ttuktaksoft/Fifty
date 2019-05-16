package fifty.fiftyhouse.com.fifty.adapter;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;

import fifty.fiftyhouse.com.fifty.CommonFunc;
import fifty.fiftyhouse.com.fifty.R;

import fifty.fiftyhouse.com.fifty.viewHolder.MainUserListHolder;

public class MainAdapter extends RecyclerView.Adapter<MainUserListHolder> {

    Context mContext;
    int viewCount = 0;
    public MainAdapter(Context context) {
        mContext = context;

    }

    @Override
    public MainUserListHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.adapter_main, parent, false);
        return new MainUserListHolder(view);
    }

    @Override
    public void onBindViewHolder(MainUserListHolder holder, final int position) {
        holder.tv_Main_Thumbnail_Info_NickName.setText("닉네임");
        holder.tv_Main_Thumbnail_Info_State.setText("접속중");

        viewCount = CommonFunc.getInstance().MainUserListSlotCount;
        int thumbnailMargin = CommonFunc.getInstance().convertPXtoDP(mContext.getResources(), 5);
        int thumbnailSize = CommonFunc.getInstance().GetWidthByDevice() / viewCount - CommonFunc.getInstance().convertPXtoDP(mContext.getResources(), 10);

        ConstraintLayout.LayoutParams lp_Main_Thumbnail_1 = new ConstraintLayout.LayoutParams(thumbnailSize, thumbnailSize);
        lp_Main_Thumbnail_1.leftToLeft = ConstraintLayout.LayoutParams.PARENT_ID;
        lp_Main_Thumbnail_1.topToTop = ConstraintLayout.LayoutParams.PARENT_ID;
        lp_Main_Thumbnail_1.setMargins(thumbnailMargin + CommonFunc.getInstance().convertPXtoDP(mContext.getResources(), 5),thumbnailMargin,thumbnailMargin,thumbnailMargin);
        holder.iv_Main_Thumbnail_1.setLayoutParams(lp_Main_Thumbnail_1);
        Glide.with(mContext).load(R.drawable.dummy_3)
                .centerCrop()
                .into(holder.iv_Main_Thumbnail_1);

        ConstraintLayout.LayoutParams lp_Main_Thumbnail_2 = new ConstraintLayout.LayoutParams(thumbnailSize, thumbnailSize);
        lp_Main_Thumbnail_2.leftToRight = holder.iv_Main_Thumbnail_1.getId();
        lp_Main_Thumbnail_2.topToTop = ConstraintLayout.LayoutParams.PARENT_ID;
        lp_Main_Thumbnail_2.setMargins(thumbnailMargin,thumbnailMargin,thumbnailMargin,thumbnailMargin);
        holder.iv_Main_Thumbnail_2.setLayoutParams(lp_Main_Thumbnail_2);
        Glide.with(mContext).load(R.drawable.dummy_10)
                .centerCrop()
                .into(holder.iv_Main_Thumbnail_2);

        ConstraintLayout.LayoutParams lp_Main_Thumbnail_3 = new ConstraintLayout.LayoutParams(thumbnailSize, thumbnailSize);
        lp_Main_Thumbnail_3.leftToRight = holder.iv_Main_Thumbnail_2.getId();
        lp_Main_Thumbnail_3.topToTop = ConstraintLayout.LayoutParams.PARENT_ID;
        lp_Main_Thumbnail_3.setMargins(thumbnailMargin,thumbnailMargin,thumbnailMargin,thumbnailMargin);
        holder.iv_Main_Thumbnail_3.setLayoutParams(lp_Main_Thumbnail_3);
        Glide.with(mContext).load(R.drawable.dummy_13)
                .centerCrop()
                .into(holder.iv_Main_Thumbnail_3);

        ConstraintLayout.LayoutParams lp_Main_Thumbnail_4 = new ConstraintLayout.LayoutParams(thumbnailSize, thumbnailSize);
        lp_Main_Thumbnail_4.leftToRight = holder.iv_Main_Thumbnail_3.getId();
        lp_Main_Thumbnail_4.topToTop = ConstraintLayout.LayoutParams.PARENT_ID;
        lp_Main_Thumbnail_4.setMargins(thumbnailMargin,thumbnailMargin,thumbnailMargin,thumbnailMargin);
        holder.iv_Main_Thumbnail_4.setLayoutParams(lp_Main_Thumbnail_4);
        Glide.with(mContext).load(R.drawable.dummy_15)
                .centerCrop()
                .into(holder.iv_Main_Thumbnail_4);
    }

    @Override
    public long getItemId(int position) {
        long rtValue = 0;
        //rtValue = Long.valueOf(mMyData.arrUserAll_Hot_Age.get(position).Idx);

        return rtValue;
    }


    @Override
    public int getItemCount() {
        int rtValue = 10;
        return rtValue;
    }

    @Override
    public int getItemViewType(int position) {
        return position % 2 * 2;
    }
}
