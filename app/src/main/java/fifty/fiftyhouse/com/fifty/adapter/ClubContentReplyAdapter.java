package fifty.fiftyhouse.com.fifty.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import fifty.fiftyhouse.com.fifty.CommonFunc;
import fifty.fiftyhouse.com.fifty.DataBase.ClubContextData;
import fifty.fiftyhouse.com.fifty.Manager.TKManager;
import fifty.fiftyhouse.com.fifty.R;

public class ClubContentReplyAdapter extends RecyclerView.Adapter<ClubContentReplyListHolder> {

    Context mContext;
    int mReplyCount = 0;

    public ClubContentReplyAdapter(Context context) {
        mContext = context;
    }

    @Override
    public ClubContentReplyListHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.adapter_club_content_reply, parent, false);

        return new ClubContentReplyListHolder(view);
    }

    @Override
    public void onBindViewHolder(ClubContentReplyListHolder holder, final int position) {
        int i = position;
        holder.setClubContentReply(i);
    }

    @Override
    public int getItemCount() {
        return mReplyCount;
    }

    public void setReplyCount(int count)
    {
        mReplyCount = count;
    }

}

class ClubContentReplyListHolder extends RecyclerView.ViewHolder {

    public ImageView iv_Club_Con_Reply_Profile;
    public TextView tv_Club_Con_Reply_Nickname;
    public TextView tv_Club_Con_Reply_Date;
    public TextView tv_Club_Con_Reply_Desc;
    Context mContext;

    public ClubContentReplyListHolder(View itemView) {
        super(itemView);
        mContext = itemView.getContext();

        iv_Club_Con_Reply_Profile = itemView.findViewById(R.id.iv_Club_Con_Reply_Profile);
        tv_Club_Con_Reply_Nickname = itemView.findViewById(R.id.tv_Club_Con_Reply_Nickname);
        tv_Club_Con_Reply_Date = itemView.findViewById(R.id.tv_Club_Con_Reply_Date);
        tv_Club_Con_Reply_Desc = itemView.findViewById(R.id.tv_Club_Con_Reply_Desc);
    }

    public void setClubContentReply(int pos)
    {
        ClubContextData tempData = TKManager.getInstance().TargetContextData;

        Set tempKey = tempData.GetReplyDataKeySet();
        List array = new ArrayList();
        array = new ArrayList(tempKey);

        //tempData = TKManager.getInstance().TargetClubData.GetClubContext(Integer.toString(pos));
        tv_Club_Con_Reply_Nickname.setText(TKManager.getInstance().UserData_Simple.get(tempData.GetReplyData(array.get(pos).toString()).GetWriterIndex()).GetUserNickName());
        tv_Club_Con_Reply_Date.setText(CommonFunc.getInstance().ConvertTimeSrt(tempData.GetReplyData(array.get(pos).toString()).GetDate(), "yyyy/MM/dd HH시mm분"));
        tv_Club_Con_Reply_Desc.setText(tempData.GetReplyData(array.get(pos).toString()).GetContext());
        CommonFunc.getInstance().DrawImageByGlide(mContext, iv_Club_Con_Reply_Profile, TKManager.getInstance().UserData_Simple.get(tempData.GetReplyData(array.get(pos).toString()).GetWriterIndex()).GetUserImgThumb(), true);


    }
}
