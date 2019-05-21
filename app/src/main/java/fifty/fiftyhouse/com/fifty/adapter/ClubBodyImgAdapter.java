package fifty.fiftyhouse.com.fifty.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import fifty.fiftyhouse.com.fifty.R;
import fifty.fiftyhouse.com.fifty.viewHolder.ClubBodyImgListHolder;

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

    public void setReplyCount(int count)
    {
        mImgCount = count;
    }

}
