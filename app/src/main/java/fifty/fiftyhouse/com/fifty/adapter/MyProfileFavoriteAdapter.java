package fifty.fiftyhouse.com.fifty.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import fifty.fiftyhouse.com.fifty.R;
import fifty.fiftyhouse.com.fifty.viewHolder.MyPofileFavoriteListHolder;

public class MyProfileFavoriteAdapter extends RecyclerView.Adapter<MyPofileFavoriteListHolder> {

    Context mContext;
    public MyProfileFavoriteAdapter(Context context) {
        mContext = context;
    }

    @Override
    public MyPofileFavoriteListHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.adapter_myprofile_favorite, parent, false);

        return new MyPofileFavoriteListHolder(view);
    }



    @Override
    public void onBindViewHolder(MyPofileFavoriteListHolder holder, final int position) {
        int i = position;
        holder.setData(i);
    }

    @Override
    public int getItemCount() {
        return 2;
        //return mMyData.arrChatTargetData.size();
    }

}