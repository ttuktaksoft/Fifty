package fifty.fiftyhouse.com.fifty.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import fifty.fiftyhouse.com.fifty.CommonData;
import fifty.fiftyhouse.com.fifty.Manager.TKManager;
import fifty.fiftyhouse.com.fifty.R;

public class FavoriteViewAdapter extends RecyclerView.Adapter<FavoriteViewHolder> {

    Context mContext;
    public FavoriteViewAdapter(Context context) {
        mContext = context;
    }

    @Override
    public FavoriteViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.adapter_favorite_view, parent, false);

        return new FavoriteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(FavoriteViewHolder holder, final int position) {
        int i = position;
        holder.setData(i, true);
    }

    @Override
    public int getItemCount() {
        boolean signup = true;
        if(signup)
            return CommonData.Favorite_Pop_Count;
        else
            return 40;

    }

}

class FavoriteViewHolder extends RecyclerView.ViewHolder {

    TextView tv_Favorite_View_Name;
    Context mContext;

    public FavoriteViewHolder(View itemView) {
        super(itemView);
        mContext = itemView.getContext();

        tv_Favorite_View_Name = itemView.findViewById(R.id.tv_Favorite_View_Name);
    }

    public void setData(int i, boolean signup)
    {
        if(signup)
        {
            tv_Favorite_View_Name.setText(TKManager.getInstance().FavoriteLIst_Pop.get(i));
        }
    }
}