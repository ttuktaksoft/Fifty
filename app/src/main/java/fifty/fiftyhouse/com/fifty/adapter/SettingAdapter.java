package fifty.fiftyhouse.com.fifty.adapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import fifty.fiftyhouse.com.fifty.Manager.TKManager;
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
        return TKManager.getInstance().AppSettingData.size();
        //return mMyData.arrChatTargetData.size();
    }

}

class SettingListHolder extends RecyclerView.ViewHolder {

    public TextView tv_Setting_Desc;
    public CheckBox cb_Setting_Check;

    public SettingListHolder(View itemView) {
        super(itemView);

        tv_Setting_Desc = itemView.findViewById(R.id.tv_Setting_Desc);
        cb_Setting_Check = itemView.findViewById(R.id.cb_Setting_Check);

    }

    public void setData(int i)
    {

        Set tempKey = TKManager.getInstance().AppSettingData.keySet();
        final List array = new ArrayList(tempKey);


        tv_Setting_Desc.setText(array.get(i).toString());
        cb_Setting_Check.setChecked(TKManager.getInstance().AppSettingData.get(array.get(i).toString()));
    }
}
