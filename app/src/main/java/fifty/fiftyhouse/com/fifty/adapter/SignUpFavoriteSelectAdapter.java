package fifty.fiftyhouse.com.fifty.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import fifty.fiftyhouse.com.fifty.Manager.TKManager;
import fifty.fiftyhouse.com.fifty.R;
import fifty.fiftyhouse.com.fifty.viewHolder.SignUpFavoriteSelectListHolder;

public class SignUpFavoriteSelectAdapter extends RecyclerView.Adapter<SignUpFavoriteSelectListHolder> {

    Context mContext;
    public SignUpFavoriteSelectAdapter(Context context) {
        mContext = context;
    }

    @Override
    public SignUpFavoriteSelectListHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.adapter_sign_up_favorite_select, parent, false);

        return new SignUpFavoriteSelectListHolder(view);
    }



    @Override
    public void onBindViewHolder(SignUpFavoriteSelectListHolder holder, final int position) {
        int i = position;

        holder.setData(i);
    }

    @Override
    public int getItemCount() {
        return TKManager.getInstance().MyData.GetUserFavoriteListCount();
        //return mMyData.arrChatTargetData.size();
    }

}