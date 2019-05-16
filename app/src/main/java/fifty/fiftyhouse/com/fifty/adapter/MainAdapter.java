package fifty.fiftyhouse.com.fifty.adapter;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;

import fifty.fiftyhouse.com.fifty.CommonFunc;
import fifty.fiftyhouse.com.fifty.R;

import fifty.fiftyhouse.com.fifty.viewHolder.MainUserListHolder;

public class MainAdapter extends RecyclerView.Adapter<MainUserListHolder> {

    Context mContext;

    public MainAdapter(Context context) {
        mContext = context;
    }

    @Override
    public MainUserListHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.main_adapter, parent, false);
        return new MainUserListHolder(view);
    }

    @Override
    public void onBindViewHolder(MainUserListHolder holder, final int position) {
        holder.tv_Main_Thumbnail_Info_NickName.setText("닉네임");
        holder.tv_Main_Thumbnail_Info_State.setText("온라인");

        int viewCount = 3;
        int thumbnailMargin = CommonFunc.getInstance().convertPXtoDP(mContext.getResources(), 5);
        int thumbnailSize = CommonFunc.getInstance().GetWidthByDevice() / viewCount - CommonFunc.getInstance().convertPXtoDP(mContext.getResources(), 10);
        ConstraintLayout.LayoutParams lp_Main_Thumbnail_1 = new ConstraintLayout.LayoutParams(thumbnailSize, thumbnailSize);
        lp_Main_Thumbnail_1.leftToLeft = ConstraintLayout.LayoutParams.PARENT_ID;
        lp_Main_Thumbnail_1.topToTop = ConstraintLayout.LayoutParams.PARENT_ID;
        lp_Main_Thumbnail_1.setMargins(thumbnailMargin + CommonFunc.getInstance().convertPXtoDP(mContext.getResources(), 15),thumbnailMargin,thumbnailMargin,thumbnailMargin);
        holder.iv_Main_Thumbnail_1.setLayoutParams(lp_Main_Thumbnail_1);
        Glide.with(mContext).load(R.drawable.login_icon)
                .centerCrop()
                .into(holder.iv_Main_Thumbnail_1);

        ConstraintLayout.LayoutParams lp_Main_Thumbnail_2 = new ConstraintLayout.LayoutParams(thumbnailSize, thumbnailSize);
        lp_Main_Thumbnail_2.leftToRight = holder.iv_Main_Thumbnail_1.getId();
        lp_Main_Thumbnail_2.topToTop = ConstraintLayout.LayoutParams.PARENT_ID;
        lp_Main_Thumbnail_2.setMargins(thumbnailMargin,thumbnailMargin,thumbnailMargin,thumbnailMargin);
        holder.iv_Main_Thumbnail_2.setLayoutParams(lp_Main_Thumbnail_2);
        Glide.with(mContext).load(R.drawable.man)
                .centerCrop()
                .into(holder.iv_Main_Thumbnail_2);

        ConstraintLayout.LayoutParams lp_Main_Thumbnail_3 = new ConstraintLayout.LayoutParams(thumbnailSize, thumbnailSize);
        lp_Main_Thumbnail_3.leftToRight = holder.iv_Main_Thumbnail_2.getId();
        lp_Main_Thumbnail_3.topToTop = ConstraintLayout.LayoutParams.PARENT_ID;
        lp_Main_Thumbnail_3.setMargins(thumbnailMargin,thumbnailMargin,thumbnailMargin,thumbnailMargin);
        holder.iv_Main_Thumbnail_3.setLayoutParams(lp_Main_Thumbnail_3);
        Glide.with(mContext).load(R.drawable.woman)
                .centerCrop()
                .into(holder.iv_Main_Thumbnail_3);
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

        //rtValue = mMyData.arrUserAll_Hot_Age.size();
        return rtValue;
    }
}
