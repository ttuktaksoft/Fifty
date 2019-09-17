package fifty.fiftyhouse.com.fifty.adapter;

import android.content.Context;

import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import fifty.fiftyhouse.com.fifty.CommonFunc;
import fifty.fiftyhouse.com.fifty.Manager.TKManager;
import fifty.fiftyhouse.com.fifty.R;

public class ClubFavoriteAdapter extends RecyclerView.Adapter<ClubFavoriteHolder> {

    Context mContext;
    int mItemCount = 0;
    ArrayList<String> mItemList = new ArrayList<>();

    public ClubFavoriteAdapter(Context context) {
        mContext = context;
    }

    @Override
    public ClubFavoriteHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.adapter_club_favorite, parent, false);

        view.setLayoutParams(new RelativeLayout.LayoutParams(CommonFunc.getInstance().getWidthByDevice() / 3,CommonFunc.getInstance().getWidthByDevice()/3));
        return new ClubFavoriteHolder(view);
    }



    @Override
    public void onBindViewHolder(ClubFavoriteHolder holder, final int position) {
        int i = position;

        holder.setData(mItemList.get(position));
    }

    @Override
    public int getItemCount() {
        return mItemCount;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void ResetItem()
    {
        mItemList.clear();
        mItemCount = 0;
    }

    public void setItemData(ArrayList<String> list)
    {
        mItemList.clear();
        mItemList.addAll(list);
    }
    public void setItemCount(int count)
    {
        mItemCount = count;
    }

}

class ClubFavoriteHolder extends RecyclerView.ViewHolder {

    ImageView iv_ClubFavorite_BG;
    TextView tv_ClubFavorite;
    Context mContext;

    public ClubFavoriteHolder(View itemView) {
        super(itemView);
        mContext = itemView.getContext();

        tv_ClubFavorite = itemView.findViewById(R.id.tv_ClubFavorite);
        iv_ClubFavorite_BG = itemView.findViewById(R.id.iv_ClubFavorite_BG);
    }

    public void setData(String key)
    {
        if(key.equals("plus"))
        {
            CommonFunc.getInstance().DrawImageByGlide(mContext, iv_ClubFavorite_BG, R.drawable.bg_empty_square_plus, false);
            tv_ClubFavorite.setText(CommonFunc.getInstance().getStr(mContext.getResources(), R.string.MSG_CLUB_FAVORITE_PLUS));
        }
        else
        {
            tv_ClubFavorite.setText(key);
            CommonFunc.getInstance().DrawImageByGlide(mContext, iv_ClubFavorite_BG, TKManager.getInstance().FavoriteLIst_ClubThumbList.get(key), false);
        }
    }
}