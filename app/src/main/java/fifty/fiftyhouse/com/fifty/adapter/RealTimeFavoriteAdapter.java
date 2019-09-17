package fifty.fiftyhouse.com.fifty.adapter;

import android.content.Context;
import android.content.res.ColorStateList;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import androidx.core.content.ContextCompat;
import androidx.core.widget.ImageViewCompat;
import androidx.recyclerview.widget.RecyclerView;
import fifty.fiftyhouse.com.fifty.CommonData;
import fifty.fiftyhouse.com.fifty.Manager.TKManager;
import fifty.fiftyhouse.com.fifty.R;

public class RealTimeFavoriteAdapter extends RecyclerView.Adapter<RealTimeFavoriteHolder> {

    Context mContext;
    int mItemCount = 0;
    ArrayList<String> mItemList = new ArrayList<>();

    public RealTimeFavoriteAdapter(Context context) {
        mContext = context;
    }

    @Override
    public RealTimeFavoriteHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.adapter_realtime_favorite, parent, false);

        return new RealTimeFavoriteHolder(view);
    }

    @Override
    public void onBindViewHolder(RealTimeFavoriteHolder holder, final int position) {
        int i = position;
        holder.setData(mItemList.get(i), i + 1);
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
}

class RealTimeFavoriteHolder extends RecyclerView.ViewHolder {

    TextView tv_RealTime_Favorite;
    Context mContext;

    public RealTimeFavoriteHolder(View itemView) {
        super(itemView);
        mContext = itemView.getContext();

        tv_RealTime_Favorite  = itemView.findViewById(R.id.tv_RealTime_Favorite);
    }

    public void setData(String favorite, int count)
    {
        SpannableStringBuilder str = new SpannableStringBuilder(count + ". #" + favorite);
        str.setSpan(new ForegroundColorSpan(mContext.getResources().getColor(R.color.str_color_7)), 0, Integer.toString(count).length() + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        str.setSpan(new android.text.style.StyleSpan(android.graphics.Typeface.BOLD), Integer.toString(count).length() + 1, str.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        tv_RealTime_Favorite.setText(str);
    }
}