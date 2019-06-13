package fifty.fiftyhouse.com.fifty.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import fifty.fiftyhouse.com.fifty.Manager.TKManager;
import fifty.fiftyhouse.com.fifty.R;

public class FavoriteSelectViewAdapter extends RecyclerView.Adapter<FavoriteSelectViewHolder> {

    Context mContext;
    public FavoriteSelectViewAdapter(Context context) {
        mContext = context;
    }

    @Override
    public FavoriteSelectViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.adapter_favorite_select_view, parent, false);

        return new FavoriteSelectViewHolder(view);
    }

    @Override
    public void onBindViewHolder(FavoriteSelectViewHolder holder, final int position) {
        int i = position;
        holder.setData(i);
    }

    @Override
    public int getItemCount() {
        return TKManager.getInstance().MyData.GetUserFavoriteListCount();
    }

}

class FavoriteSelectViewHolder extends RecyclerView.ViewHolder {

    TextView tv_Favorite_Select_View_Name;
    Context mContext;

    public FavoriteSelectViewHolder(View itemView) {
        super(itemView);
        mContext = itemView.getContext();

        tv_Favorite_Select_View_Name = itemView.findViewById(R.id.tv_Favorite_Select_View_Name);
    }

    public void setData(int i)
    {
        Set tempKey = TKManager.getInstance().MyData.GetUserFavoriteListKeySet();
        List array = new ArrayList(tempKey);

        tv_Favorite_Select_View_Name.setText(TKManager.getInstance().MyData.GetUserFavoriteList(array.get(i).toString()));
    }
}