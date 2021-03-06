package fifty.fiftyhouse.com.fifty.adapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import fifty.fiftyhouse.com.fifty.R;

public class FavoriteSelectViewAdapter extends RecyclerView.Adapter<FavoriteSelectViewHolder> {

    Context mContext;
    int mItemCount = 0;
    ArrayList<String> mItemList = new ArrayList<>();
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
        holder.setData(mItemList.get(i));
    }

    @Override
    public int getItemCount() {
        return mItemCount;
    }

    public void setItemCount(int count)
    {
        mItemCount = count;
    }

    public void setItemData(ArrayList<String> list)
    {
        mItemList.clear();
        mItemList.addAll(list);
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

    public void setData(String str)
    {
        tv_Favorite_Select_View_Name.setText(str);
    }
}