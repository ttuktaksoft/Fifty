package fifty.fiftyhouse.com.fifty.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import fifty.fiftyhouse.com.fifty.Manager.TKManager;
import fifty.fiftyhouse.com.fifty.R;
import fifty.fiftyhouse.com.fifty.viewHolder.FavoriteEditSelectListHolder;

public class FavoriteEditSelectAdapter extends RecyclerView.Adapter<FavoriteEditSelectListHolder> {

    Context mContext;
    public FavoriteEditSelectAdapter(Context context) {
        mContext = context;
    }

    @Override
    public FavoriteEditSelectListHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.adapter_favorite_edit_select, parent, false);

        return new FavoriteEditSelectListHolder(view);
    }



    @Override
    public void onBindViewHolder(FavoriteEditSelectListHolder holder, final int position) {
        int i = position;

        holder.setData(i);
    }

    @Override
    public int getItemCount() {
        return 2;
        //return mMyData.arrChatTargetData.size();
    }

}