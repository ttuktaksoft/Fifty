package fifty.fiftyhouse.com.fifty.adapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import fifty.fiftyhouse.com.fifty.CommonFunc;
import fifty.fiftyhouse.com.fifty.DataBase.ClubContextData;
import fifty.fiftyhouse.com.fifty.R;

public class ClubBodyImgAdapter extends RecyclerView.Adapter<ClubBodyImgListHolder> {

    Context mContext;
    int mImgCount = 0;
    ClubContextData tempData;

    public ClubBodyImgAdapter(Context context, ClubContextData data) {
        tempData = new ClubContextData();
        tempData = data;
        mContext = context;
    }

    @Override
    public ClubBodyImgListHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.adapter_club_body_img, parent, false);

        return new ClubBodyImgListHolder(view, tempData);
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
    ClubContextData tempData;

    public ClubBodyImgListHolder(View itemView,  ClubContextData data) {
        super(itemView);
        mContext = itemView.getContext();
        tempData = data;

        iv_Club_Body_Img = itemView.findViewById(R.id.iv_Club_Body_Img);
    }

    public void setClubBodyImg(int pos)
    {

        CommonFunc.getInstance().DrawImageByGlide(mContext, iv_Club_Body_Img, tempData.GetImg(Integer.toString(pos)), false);
        /*
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
        }*/
    }
}
