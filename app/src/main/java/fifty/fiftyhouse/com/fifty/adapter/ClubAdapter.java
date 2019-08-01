package fifty.fiftyhouse.com.fifty.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import fifty.fiftyhouse.com.fifty.CommonFunc;
import fifty.fiftyhouse.com.fifty.DataBase.ChatData;
import fifty.fiftyhouse.com.fifty.DataBase.ClubData;
import fifty.fiftyhouse.com.fifty.Manager.TKManager;
import fifty.fiftyhouse.com.fifty.R;

public class ClubAdapter extends RecyclerView.Adapter<ClubListHolder> {

    Context mContext;
    int mItemCount = 0;
    ArrayList<String> mItemList = new ArrayList<>();

    public ClubAdapter(Context context) {
        mContext = context;
    }

    @Override
    public ClubListHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.adapter_club, parent, false);

        view.setLayoutParams(new RelativeLayout.LayoutParams(CommonFunc.getInstance().getWidthByDevice() / 2,CommonFunc.getInstance().getWidthByDevice()/2));
        return new ClubListHolder(view);
    }



    @Override
    public void onBindViewHolder(ClubListHolder holder, final int position) {
        int i = position;

        holder.setData(mItemList.get(position));
    }

    @Override
    public int getItemCount() {
        return mItemCount;
    }

    public void ResetItem()
    {
        mItemList.clear();
        mItemCount = 0;
    }

    public void setItemData(ArrayList<String> list)
    {
        mItemList.clear();
        mItemList.addAll(list);
    }
    public void setItemCount(int count)
    {
        mItemCount = count;
    }

}

class ClubListHolder extends RecyclerView.ViewHolder {

    public ImageView iv_Club_Profile, iv_Club_Tag, iv_Club_Master;
    public TextView tv_Club_Name, tv_Club_Count;
    public TextView tv_Club_Tag;
    Context mContext;

    public ClubListHolder(View itemView) {
        super(itemView);
        mContext = itemView.getContext();

        iv_Club_Profile = itemView.findViewById(R.id.iv_Club_Profile);
        tv_Club_Name = itemView.findViewById(R.id.tv_Club_Name);
        iv_Club_Tag = itemView.findViewById(R.id.iv_Club_Tag);
        tv_Club_Tag = itemView.findViewById(R.id.tv_Club_Tag);
        iv_Club_Master = itemView.findViewById(R.id.iv_Club_Master);
    }

    public void setData(String clubKey)
    {
        ClubData tempClub = TKManager.getInstance().ClubData_Simple.get(clubKey);

        iv_Club_Master.setVisibility(View.GONE);

        if(TKManager.getInstance().MyData.GetUserClubData(clubKey) != null)
        {
            // 내 클럽
            tv_Club_Tag.setText("");
            iv_Club_Tag.setVisibility(View.GONE);

            if(tempClub.GetClubMasterIndex().equals(TKManager.getInstance().MyData.GetUserIndex()))
                iv_Club_Master.setVisibility(View.VISIBLE);
        }
        else
        {
            // 추천 클럽
            tv_Club_Tag.setText(CommonFunc.getInstance().getStr(mContext.getResources(), R.string.MSG_RECOM_CLUB));
            iv_Club_Tag.setVisibility(View.VISIBLE);
        }

        tv_Club_Name.setText(tempClub.ClubName);
        tv_Club_Count.setText(tempClub.GetClubMemberCount() + "명");
        CommonFunc.getInstance().DrawImageByGlide(mContext, iv_Club_Profile, tempClub.ClubThumbNail, false);
    }
}