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
import fifty.fiftyhouse.com.fifty.viewHolder.ClubContentListHolder;

public class ClubContentAdapter extends RecyclerView.Adapter<ClubContentListHolder> {

    Context mContext;
    public ClubContentAdapter(Context context) {
        mContext = context;
    }

    @Override
    public ClubContentListHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.adapter_club_content, parent, false);

        return new ClubContentListHolder(view);
    }



    @Override
    public void onBindViewHolder(ClubContentListHolder holder, final int position) {
        int i = position;
        holder.setClubContent(i);
    }

    @Override
    public int getItemCount() {
        return 10;
        //return mMyData.arrChatTargetData.size();
    }

}
