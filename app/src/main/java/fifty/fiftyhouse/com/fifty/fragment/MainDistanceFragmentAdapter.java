package fifty.fiftyhouse.com.fifty.fragment;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.Constraints;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;

import fifty.fiftyhouse.com.fifty.CommonFunc;
import fifty.fiftyhouse.com.fifty.R;

import fifty.fiftyhouse.com.fifty.viewHolder.MainUserListHolder;

public class MainDistanceFragmentAdapter extends RecyclerView.Adapter<MainUserListHolder> {

    Context mContext;

    public MainDistanceFragmentAdapter(Context context) {
        mContext = context;
    }

    @Override
    public MainUserListHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.fragment_main_distance_adapter, parent, false);
        return new MainUserListHolder(view);
    }

    @Override
    public void onBindViewHolder(MainUserListHolder holder, final int position) {
        holder.tv_Main_Thumbnail_Info_NickName.setText("닉네임");
        holder.tv_Main_Thumbnail_Info_State.setText("온라인");

        //holder.v_Main_Thumbnail.setLayoutParams(new Constraints.LayoutParams(CommonFunc.getInstance().GetWidthByDevice(), CommonFunc.getInstance().GetWidthByDevice() / 3));
        ConstraintLayout tt = new ConstraintLayout(mContext, new AttributeSet());
        holder.iv_Main_Thumbnail_1.setLayoutParams(tt.getLayoutParams());
        Constraints.LayoutParams data =  new Constraints.LayoutParams(CommonFunc.getInstance().GetWidthByDevice() / 10, CommonFunc.getInstance().GetWidthByDevice() / 10);
        data.set
        holder.iv_Main_Thumbnail_1.setLayoutParams(new Constraints.LayoutParams(CommonFunc.getInstance().GetWidthByDevice() / 10, CommonFunc.getInstance().GetWidthByDevice() / 10));
        Glide.with(mContext).load(R.drawable.login_icon)
                .centerCrop()
                .into(holder.iv_Main_Thumbnail_1);
        holder.iv_Main_Thumbnail_2.setLayoutParams(new Constraints.LayoutParams(CommonFunc.getInstance().GetWidthByDevice() / 10, CommonFunc.getInstance().GetWidthByDevice() /10));
        Glide.with(mContext).load(R.drawable.man)
                .centerCrop()
                .into(holder.iv_Main_Thumbnail_2);
        holder.iv_Main_Thumbnail_3.setLayoutParams(new Constraints.LayoutParams(CommonFunc.getInstance().GetWidthByDevice() / 10, CommonFunc.getInstance().GetWidthByDevice() / 10));
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
