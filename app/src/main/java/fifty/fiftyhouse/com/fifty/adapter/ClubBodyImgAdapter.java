package fifty.fiftyhouse.com.fifty.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import fifty.fiftyhouse.com.fifty.R;

public class ClubBodyImgAdapter extends RecyclerView.Adapter<ClubBodyImgListHolder> {

    Context mContext;
    int mImgCount = 0;

    public ClubBodyImgAdapter(Context context) {
        mContext = context;
    }

    @Override
    public ClubBodyImgListHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.adapter_club_body_img, parent, false);

        return new ClubBodyImgListHolder(view);
    }

    @Override
    public void onBindViewHolder(ClubBodyImgListHolder holder, final int position) {
        int i = position;
        holder.setClubBodyImg(i);
    }

    @Override
    public int getItemCount() {
        return mImgCount;
    }

    public void setImgCount(int count)
    {
        mImgCount = count;
    }

}

class ClubBodyImgListHolder extends RecyclerView.ViewHolder {

    public ImageView iv_Club_Body_Img;
    Context mContext;

    public ClubBodyImgListHolder(View itemView) {
        super(itemView);
        mContext = itemView.getContext();

        iv_Club_Body_Img = itemView.findViewById(R.id.iv_Club_Body_Img);
    }

    public void setClubBodyImg(int pos)
    {
        // TODO 클럽 내용 추가
        if(pos == 0)
        {
            Glide.with(mContext)
                    //.load(mMyData.arrSendDataList.get(position).strTargetImg)
                    .load(R.drawable.login_icon)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .thumbnail(0.1f)
                    .into(iv_Club_Body_Img);
        }
        else if(pos == 1)
        {
            Glide.with(mContext)
                    //.load(mMyData.arrSendDataList.get(position).strTargetImg)
                    .load(R.drawable.dummy_10)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .thumbnail(0.1f)
                    .into(iv_Club_Body_Img);
        }
        else
        {
            Glide.with(mContext)
                    //.load(mMyData.arrSendDataList.get(position).strTargetImg)
                    .load(R.drawable.dummy_11)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .thumbnail(0.1f)
                    .into(iv_Club_Body_Img);
        }
    }
}
