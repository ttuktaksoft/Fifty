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
import fifty.fiftyhouse.com.fifty.viewHolder.ClubContentReplyListHolder;

public class ClubContentReplyAdapter extends RecyclerView.Adapter<ClubContentReplyListHolder> {

    Context mContext;
    int mReplyCount = 0;

    public ClubContentReplyAdapter(Context context) {
        mContext = context;
    }

    @Override
    public ClubContentReplyListHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.adapter_club_content_reply, parent, false);

        return new ClubContentReplyListHolder(view);
    }

    @Override
    public void onBindViewHolder(ClubContentReplyListHolder holder, final int position) {
        int i = position;
        holder.setClubContentReply(i);
    }

    @Override
    public int getItemCount() {
        return mReplyCount;
    }

    public void setReplyCount(int count)
    {
        mReplyCount = count;
    }

}
