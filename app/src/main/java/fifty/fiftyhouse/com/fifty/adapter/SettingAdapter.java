package fifty.fiftyhouse.com.fifty.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import fifty.fiftyhouse.com.fifty.R;

public class SettingAdapter extends RecyclerView.Adapter<SettingListHolder> {

    Context mContext;
    public SettingAdapter(Context context) {
        mContext = context;
    }

    @Override
    public SettingListHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.adapter_setting, parent, false);

        return new SettingListHolder(view);
    }

    @Override
    public void onBindViewHolder(SettingListHolder holder, final int position) {
        int i = position;

        holder.setData(i);
    }

    @Override
    public int getItemCount() {
        return 1;
        //return mMyData.arrChatTargetData.size();
    }

}

class SettingListHolder extends RecyclerView.ViewHolder {

    public TextView tv_Setting_Desc;

    public SettingListHolder(View itemView) {
        super(itemView);

        tv_Setting_Desc = itemView.findViewById(R.id.tv_Setting_Desc);

    }

    public void setData(int i)
    {
        tv_Setting_Desc.setText("계정");
    }
}
