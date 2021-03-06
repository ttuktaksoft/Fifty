package fifty.fiftyhouse.com.fifty.adapter;

import android.content.Context;
import android.content.res.ColorStateList;
import androidx.core.content.ContextCompat;
import androidx.core.widget.ImageViewCompat;
import androidx.recyclerview.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import fifty.fiftyhouse.com.fifty.CommonData;
import fifty.fiftyhouse.com.fifty.R;

public class FavoriteViewAdapter extends RecyclerView.Adapter<FavoriteViewHolder> {

    Context mContext;
    String mAddString;
    boolean mAdd = false;
    boolean mSelectView = false;
    int mItemCount = 0;
    ArrayList<String> mItemList = new ArrayList<>();
    ArrayList<String> mSelectItemList = new ArrayList<>();
    int mFontSize = CommonData.FAVORITE_FONT_SIZE_DEFAULT;

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
                holder.setData(mAddString, true, mSelectView, false, mFontSize);
            else
                holder.setData(mItemList.get(i), false, mSelectView, mSelectItemList.contains(mItemList.get(i)), mFontSize);
        }
        else if(mSelectItemList.size() > 0)
        {
            holder.setData(mItemList.get(i), false, mSelectView, mSelectItemList.contains(mItemList.get(i)), mFontSize);
        }
        else
            holder.setData(mItemList.get(i), false, mSelectView, mSelectItemList.contains(mItemList.get(i)), mFontSize);

    }
    @Override
    public long getItemId(int position) {
        return position;
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

    public void setSelectView(boolean enable)
    {
        mSelectView = enable;
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

    public void setSelectItemData(ArrayList<String> list)
    {
        mSelectItemList.clear();
        mSelectItemList.addAll(list);
    }

    public void setFontSize(int size)
    {
        mFontSize = size;
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

    public void setData(String favorite, boolean add, boolean selectView, boolean selectBG, int fontsize)
    {
        if(add == false)
        {
            if(selectView)
            {
                if(selectBG)
                {
                    ImageViewCompat.setImageTintList(iv_Favorite_View_Bg, ColorStateList.valueOf(ContextCompat.getColor(mContext, R.color.favorite_view_bg_tint_1)));
                    tv_Favorite_View_Name.setTextColor(ContextCompat.getColor(mContext, R.color.favorite_view_bg_tint_1_str));
                }
                else
                {
                    ImageViewCompat.setImageTintList(iv_Favorite_View_Bg, ColorStateList.valueOf(ContextCompat.getColor(mContext, R.color.favorite_view_bg_tint_2)));
                    tv_Favorite_View_Name.setTextColor(ContextCompat.getColor(mContext, R.color.favorite_view_bg_tint_2_str));
                }

                tv_Favorite_View_Name.setText(favorite);

            }
            else
            {
                ImageViewCompat.setImageTintList(iv_Favorite_View_Bg, ColorStateList.valueOf(ContextCompat.getColor(mContext, R.color.favorite_view_bg_tint_1)));
                tv_Favorite_View_Name.setTextColor(ContextCompat.getColor(mContext, R.color.favorite_view_bg_tint_1_str));
                tv_Favorite_View_Name.setText(favorite);
            }
        }
        else
        {
            ImageViewCompat.setImageTintList(iv_Favorite_View_Bg, ColorStateList.valueOf(ContextCompat.getColor(mContext, R.color.favorite_view_bg_tint_3)));
            tv_Favorite_View_Name.setTextColor(ContextCompat.getColor(mContext, R.color.favorite_view_bg_tint_3_str));
            tv_Favorite_View_Name.setText(favorite);
        }

        tv_Favorite_View_Name.setTextSize(TypedValue.COMPLEX_UNIT_DIP, fontsize);

    }
}