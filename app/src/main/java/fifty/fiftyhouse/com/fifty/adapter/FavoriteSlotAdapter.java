package fifty.fiftyhouse.com.fifty.adapter;

import android.content.Context;
import android.content.res.ColorStateList;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.ImageViewCompat;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import fifty.fiftyhouse.com.fifty.CommonData;
import fifty.fiftyhouse.com.fifty.R;

public class FavoriteSlotAdapter extends RecyclerView.Adapter<FavoriteSlotHolder> {

    Context mContext;
    String mAddString;
    int mItemCount = 0;
    ArrayList<String> mItemList = new ArrayList<>();
    int mFontSize = CommonData.FAVORITE_FONT_SIZE_DEFAULT;

    public FavoriteSlotAdapter(Context context) {
        mContext = context;
    }

    @Override
    public FavoriteSlotHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.adapter_favorite_slot, parent, false);

        return new FavoriteSlotHolder(view);
    }

    @Override
    public void onBindViewHolder(FavoriteSlotHolder holder, final int position) {
        int i = position;
        holder.setData(mItemList.get(i), mFontSize);
    }
    @Override
    public long getItemId(int position) {
        return position;
    }
    @Override
    public int getItemCount() {
        return mItemCount;
    }

    public void setItemData(ArrayList<String> list)
    {
        mItemList.clear();
        mItemList.addAll(list);
        mItemCount = mItemList.size();
    }

    public void setFontSize(int size)
    {
        mFontSize = size;
    }

}

class FavoriteSlotHolder extends RecyclerView.ViewHolder {

    ImageView iv_Favorite_View_Bg;
    TextView tv_Favorite_View_Name;
    Context mContext;

    public FavoriteSlotHolder(View itemView) {
        super(itemView);
        mContext = itemView.getContext();

        iv_Favorite_View_Bg  = itemView.findViewById(R.id.iv_Favorite_View_Bg);
        tv_Favorite_View_Name = itemView.findViewById(R.id.tv_Favorite_View_Name);
    }

    public void setData(String favorite, int fontsize)
    {
        tv_Favorite_View_Name.setText("#" + favorite);
        tv_Favorite_View_Name.setTextSize(TypedValue.COMPLEX_UNIT_DIP, fontsize);
    }
}
