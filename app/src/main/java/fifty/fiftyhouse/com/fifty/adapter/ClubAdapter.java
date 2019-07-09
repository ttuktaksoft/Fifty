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

        ClubData tempClub = new ClubData();
        Set tempKey = TKManager.getInstance().MyData.GetUserClubDataKeySet();
        List array = new ArrayList(tempKey);
        tempClub = TKManager.getInstance().ClubData_Simple.get(array.get(position).toString());

        holder.tv_Club_Name.setText(tempClub.ClubName);
        Glide.with(mContext)
                //.load(mMyData.arrSendDataList.get(position).strTargetImg)
                .load(tempClub.ClubThumbNail)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .thumbnail(0.1f)
                .into(holder.iv_Club_Profile);
    }

    @Override
    public int getItemCount() {
        return TKManager.getInstance().MyData.GetUserClubDataCount();
    }

}

class ClubListHolder extends RecyclerView.ViewHolder {

    public ImageView iv_Club_Profile;
    public TextView tv_Club_Name;

    public ClubListHolder(View itemView) {
        super(itemView);
        iv_Club_Profile = itemView.findViewById(R.id.iv_Club_Profile);
        tv_Club_Name = itemView.findViewById(R.id.tv_Club_Name);
    }
}