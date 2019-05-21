package fifty.fiftyhouse.com.fifty.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import fifty.fiftyhouse.com.fifty.R;
import fifty.fiftyhouse.com.fifty.viewHolder.ClubBodyReplyListHolder;

public class ClubBodyReplyAdapter extends RecyclerView.Adapter<ClubBodyReplyListHolder> {

    Context mContext;
    int mReplyCount = 0;

    public ClubBodyReplyAdapter(Context context) {
        mContext = context;
    }

    @Override
    public ClubBodyReplyListHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.adapter_club_body_reply, parent, false);

        return new ClubBodyReplyListHolder(view);
    }

    @Override
    public void onBindViewHolder(ClubBodyReplyListHolder holder, final int position) {
        int i = position;
        holder.setClubBodyReply(i);
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
