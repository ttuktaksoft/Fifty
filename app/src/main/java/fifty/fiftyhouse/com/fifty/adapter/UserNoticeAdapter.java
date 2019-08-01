package fifty.fiftyhouse.com.fifty.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import fifty.fiftyhouse.com.fifty.CommonFunc;
import fifty.fiftyhouse.com.fifty.DataBase.ClubData;
import fifty.fiftyhouse.com.fifty.Manager.TKManager;
import fifty.fiftyhouse.com.fifty.R;

public class UserNoticeAdapter extends RecyclerView.Adapter<UserNoticeListHolder> {

    Context mContext;
    public UserNoticeAdapter(Context context) {
        mContext = context;
    }

    @Override
    public UserNoticeListHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.adapter_user_notice, parent, false);

        return new UserNoticeListHolder(view, mContext);
    }



    @Override
    public void onBindViewHolder(UserNoticeListHolder holder, final int position) {
        int i = position;

        holder.setData(i);
    }

    @Override
    public int getItemCount() {
        //return 1;
        return TKManager.getInstance().MyData.GetAlarmListCount();
    }

}

class UserNoticeListHolder extends RecyclerView.ViewHolder {

    public ImageView iv_Notice_Profile;
    public TextView tv_Notice_Desc, tv_Notice_Date;
    Context mContext;

    public UserNoticeListHolder(View itemView, Context context) {
        super(itemView);

        mContext = context;
        iv_Notice_Profile = itemView.findViewById(R.id.iv_Notice_Profile);
        tv_Notice_Desc = itemView.findViewById(R.id.tv_Notice_Desc);
        tv_Notice_Date = itemView.findViewById(R.id.tv_Notice_Date);

    }

    public void setData(int position)
    {
        Set tempKey = TKManager.getInstance().MyData.GetAlarmListKeySet();
        final List array = new ArrayList(tempKey);

        CommonFunc.getInstance().DrawImageByGlide(mContext, iv_Notice_Profile, TKManager.getInstance().UserData_Simple.get(array.get(position).toString()).GetUserImgThumb(), true);

        switch (TKManager.getInstance().MyData.GetAlarmList(array.get(position).toString()).GetType())
        {
            case "VISIT":
                tv_Notice_Desc.setText(TKManager.getInstance().UserData_Simple.get(array.get(position).toString()).GetUserNickName() + "님이 방문하였습니다");
                break;
            case "LIKE":
                tv_Notice_Desc.setText(TKManager.getInstance().UserData_Simple.get(array.get(position).toString()).GetUserNickName() + "님이 좋아합니다");
                break;
            case "FRIEND":
                tv_Notice_Desc.setText(TKManager.getInstance().UserData_Simple.get(array.get(position).toString()).GetUserNickName() + "님이 친구 추가를 하였습니다");
                break;
        }

        tv_Notice_Date.setText(CommonFunc.getInstance().ConvertTimeSrt(Long.toString(TKManager.getInstance().MyData.GetAlarmList(array.get(position).toString()).Date), "MM.dd a hh:mm"));
    }
}
