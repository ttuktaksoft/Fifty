package fifty.fiftyhouse.com.fifty.adapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

import fifty.fiftyhouse.com.fifty.CommonFunc;
import fifty.fiftyhouse.com.fifty.R;

public class ClubWriteImgAdapter extends RecyclerView.Adapter<ClubWriteImgHolder> {

    Context mContext;
    int mItemCount = 0;
    List<String> mItemList = new ArrayList<>();

    public ClubWriteImgAdapter(Context context) {
        mContext = context;
    }

    @Override
    public ClubWriteImgHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.adapter_club_write_img, parent, false);

        return new ClubWriteImgHolder(view);
    }

    @Override
    public void onBindViewHolder(ClubWriteImgHolder holder, final int position) {
        int i = position;
        holder.setClubBodyImg(mItemList.get(i));
    }

    @Override
    public int getItemCount() {
        return mItemCount;
    }

    public void setItemCount(int count)
    {
        mItemCount = count;
    }

    public void setItemData(ArrayList<String> list)
    {
        mItemList.clear();
        mItemList.addAll(list);
    }
}

class ClubWriteImgHolder extends RecyclerView.ViewHolder {

    public ImageView iv_Club_Write_Img;
    Context mContext;

    public ClubWriteImgHolder(View itemView) {
        super(itemView);
        mContext = itemView.getContext();

        iv_Club_Write_Img = itemView.findViewById(R.id.iv_Club_Write_Img);
    }

    public void setClubBodyImg(String uri)
    {
        CommonFunc.getInstance().DrawImageByGlide(mContext, iv_Club_Write_Img, uri, false);
    }
}