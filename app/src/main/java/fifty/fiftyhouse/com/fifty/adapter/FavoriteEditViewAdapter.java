package fifty.fiftyhouse.com.fifty.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import fifty.fiftyhouse.com.fifty.Manager.TKManager;
import fifty.fiftyhouse.com.fifty.R;
import fifty.fiftyhouse.com.fifty.viewHolder.FavoriteEditViewListHolder;

public class FavoriteEditViewAdapter extends RecyclerView.Adapter<FavoriteEditViewListHolder> {

    Context mContext;
    public FavoriteEditViewAdapter(Context context) {
        mContext = context;
    }

    @Override
    public FavoriteEditViewListHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.adapter_favorite_edit_view, parent, false);

        return new FavoriteEditViewListHolder(view);
    }



    @Override
    public void onBindViewHolder(FavoriteEditViewListHolder holder, final int position) {
        int i = position;

        holder.setData(i);
    }

    @Override
    public int getItemCount() {
        return 4;
        //return mMyData.arrChatTargetData.size();
    }

}