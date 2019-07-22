package fifty.fiftyhouse.com.fifty.adapter;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import fifty.fiftyhouse.com.fifty.CommonData;
import fifty.fiftyhouse.com.fifty.CommonFunc;
import fifty.fiftyhouse.com.fifty.DataBase.ChatData;
import fifty.fiftyhouse.com.fifty.DataBase.ClubContextData;
import fifty.fiftyhouse.com.fifty.Manager.TKManager;
import fifty.fiftyhouse.com.fifty.R;

public class ClubBodyReportAdapter extends RecyclerView.Adapter<ClubBodyReportHolder> {
    Context mContext;
    int mItemCount = 0;
    ArrayList<String> mItemData = new ArrayList<>();

    public ClubBodyReportAdapter(Context context) {
        mContext = context;
    }

    @Override
    public ClubBodyReportHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.adapter_club_body_report, parent, false);
        return new ClubBodyReportHolder(view);
    }

    @Override
    public void onBindViewHolder(ClubBodyReportHolder holder, final int position) {
        int i = position;
        holder.setData(mItemData.get(i));
    }

    @Override
    public int getItemCount() { return mItemCount;}

    public void setItemCount(int count)
    {
        mItemCount = count;
    }

    public void setItemData(ArrayList<String> list)
    {
        mItemData.clear();
        mItemData.addAll(list);
    }
}

class ClubBodyReportHolder extends RecyclerView.ViewHolder {

    ConstraintLayout v_Club_Reply;
    ImageView iv_Club_Body_Report_Profile;
    TextView tv_Club_Body_Report_Nickname,tv_Club_Body_Report_Msg, tv_Club_Body_Report_Date;
    Context mContext;

    public ClubBodyReportHolder(View itemView) {
        super(itemView);
        mContext = itemView.getContext();

        iv_Club_Body_Report_Profile = itemView.findViewById(R.id.iv_Club_Body_Report_Profile);
        tv_Club_Body_Report_Nickname = itemView.findViewById(R.id.tv_Club_Body_Report_Nickname);
        tv_Club_Body_Report_Msg = itemView.findViewById(R.id.tv_Club_Body_Report_Msg);
        tv_Club_Body_Report_Date = itemView.findViewById(R.id.tv_Club_Body_Report_Date);
    }

    public void setData(String key)
    {
        // TODO 신고당한 클럽 내용
        ClubContextData tempData = TKManager.getInstance().TargetReportContextData.get(key);
        CommonFunc.getInstance().DrawImageByGlide(mContext, iv_Club_Body_Report_Profile, TKManager.getInstance().UserData_Simple.get(tempData.GetWriterIndex()).GetUserImgThumb(), true);
        tv_Club_Body_Report_Nickname.setText(TKManager.getInstance().UserData_Simple.get(tempData.GetWriterIndex()).GetUserNickName());
        tv_Club_Body_Report_Date.setText(tempData.GetDate());
        tv_Club_Body_Report_Msg.setText(tempData.GetContext());
    }
}
