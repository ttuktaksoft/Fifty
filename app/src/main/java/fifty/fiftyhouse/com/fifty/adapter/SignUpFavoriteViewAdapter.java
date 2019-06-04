package fifty.fiftyhouse.com.fifty.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import fifty.fiftyhouse.com.fifty.R;
import fifty.fiftyhouse.com.fifty.viewHolder.SignUpFavoriteViewListHolder;

public class SignUpFavoriteViewAdapter extends RecyclerView.Adapter<SignUpFavoriteViewListHolder> {

    Context mContext;
    public SignUpFavoriteViewAdapter(Context context) {
        mContext = context;
    }

    @Override
    public SignUpFavoriteViewListHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.adapter_sign_up_favorite_view, parent, false);

        return new SignUpFavoriteViewListHolder(view);
    }



    @Override
    public void onBindViewHolder(SignUpFavoriteViewListHolder holder, final int position) {
        int i = position;

        holder.setData(i);
    }

    @Override
    public int getItemCount() {
        return 6;
        //return mMyData.arrChatTargetData.size();
    }

}