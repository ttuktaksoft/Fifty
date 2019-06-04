package fifty.fiftyhouse.com.fifty.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import fifty.fiftyhouse.com.fifty.R;
import fifty.fiftyhouse.com.fifty.viewHolder.SignUpFavoriteFixListHolder;

public class SignUpFavoriteFixAdapter extends RecyclerView.Adapter<SignUpFavoriteFixListHolder> {

    Context mContext;
    public SignUpFavoriteFixAdapter(Context context) {
        mContext = context;
    }

    @Override
    public SignUpFavoriteFixListHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.adapter_sign_up_favorite_fix, parent, false);

        return new SignUpFavoriteFixListHolder(view);
    }



    @Override
    public void onBindViewHolder(SignUpFavoriteFixListHolder holder, final int position) {
        int i = position;

        holder.setData(i);
    }

    @Override
    public int getItemCount() {
        return 1;
        //return mMyData.arrChatTargetData.size();
    }

}