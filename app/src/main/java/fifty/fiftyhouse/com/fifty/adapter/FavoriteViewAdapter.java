package fifty.fiftyhouse.com.fifty.adapter;

import android.content.Context;
import android.content.res.ColorStateList;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.ImageViewCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.common.internal.service.Common;

import java.util.ArrayList;

import fifty.fiftyhouse.com.fifty.CommonData;
import fifty.fiftyhouse.com.fifty.CommonFunc;
import fifty.fiftyhouse.com.fifty.Manager.TKManager;
import fifty.fiftyhouse.com.fifty.R;

public class FavoriteViewAdapter extends RecyclerView.Adapter<FavoriteViewHolder> {

    Context mContext;
    String mAddString;
    boolean mAdd = false;
    int mItemCount = 0;
    ArrayList<String> mItemList = new ArrayList<>();

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
        if(mAdd)
        {
            if(mItemCount - 1 <= position)
                holder.setData(mAddString, true);
            else
                holder.setData(mItemList.get(i), false);
        }
        else
            holder.setData(mItemList.get(i), false);

    }

    @Override
    public int getItemCount() {
        return mItemCount;
    }

    public void addSlot(String str)
    {
        mAdd = true;
        mAddString = str;
    }
    public void setItemCount(int count)
    {
        if(mAdd)
            mItemCount = count + 1;
        else
            mItemCount = count;
    }

    public void setItemData(ArrayList<String> list)
    {
        mItemList.clear();
        mItemList.addAll(list);
    }

}

class FavoriteViewHolder extends RecyclerView.ViewHolder {

    ImageView iv_Favorite_View_Bg;
    TextView tv_Favorite_View_Name;
    Context mContext;

    public FavoriteViewHolder(View itemView) {
        super(itemView);
        mContext = itemView.getContext();

        iv_Favorite_View_Bg  = itemView.findViewById(R.id.iv_Favorite_View_Bg);
        tv_Favorite_View_Name = itemView.findViewById(R.id.tv_Favorite_View_Name);
    }

    public void setData(String favorite, boolean add)
    {
        if(add == false)
        {
            ImageViewCompat.setImageTintList(iv_Favorite_View_Bg, ColorStateList.valueOf(ContextCompat.getColor(mContext, R.color.favorite_view_bg_tint_1)));
            tv_Favorite_View_Name.setText(favorite);
        }
        else
        {
            ImageViewCompat.setImageTintList(iv_Favorite_View_Bg, ColorStateList.valueOf(ContextCompat.getColor(mContext, R.color.favorite_view_bg_tint_2)));
            tv_Favorite_View_Name.setText(favorite);
        }

    }
}