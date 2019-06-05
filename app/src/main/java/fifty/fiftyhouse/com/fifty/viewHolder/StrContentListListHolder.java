package fifty.fiftyhouse.com.fifty.viewHolder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import fifty.fiftyhouse.com.fifty.R;

public class StrContentListListHolder extends RecyclerView.ViewHolder {

    TextView tv_StrContent_Desc;
    Context mContext;

    public StrContentListListHolder(View itemView) {
        super(itemView);
        mContext = itemView.getContext();

        tv_StrContent_Desc = itemView.findViewById(R.id.tv_StrContent_Desc);
    }

    public void setData(int i)
    {
        tv_StrContent_Desc.setText("내용 입니다_" + i);
    }
}
