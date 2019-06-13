package fifty.fiftyhouse.com.fifty.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import fifty.fiftyhouse.com.fifty.R;

public class UserNoticeAdapter extends RecyclerView.Adapter<UserNoticeListHolder> {

    Context mContext;
    public UserNoticeAdapter(Context context) {
        mContext = context;
    }

    @Override
    public UserNoticeListHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.adapter_user_notice, parent, false);

        return new UserNoticeListHolder(view);
    }



    @Override
    public void onBindViewHolder(UserNoticeListHolder holder, final int position) {
        int i = position;

        holder.setData(i);
    }

    @Override
    public int getItemCount() {
        return 1;
        //return mMyData.arrChatTargetData.size();
    }

}

class UserNoticeListHolder extends RecyclerView.ViewHolder {

    public ImageView iv_Notice_Profile;
    public TextView tv_Notice_Desc;

    public UserNoticeListHolder(View itemView) {
        super(itemView);

        iv_Notice_Profile = itemView.findViewById(R.id.iv_Notice_Profile);
        tv_Notice_Desc = itemView.findViewById(R.id.tv_Notice_Desc);

    }

    public void setData(int i)
    {
        tv_Notice_Desc.setText("알람을 넣어주세요");
    }
}
