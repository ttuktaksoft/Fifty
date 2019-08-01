package fifty.fiftyhouse.com.fifty.adapter;

import android.content.Context;
import android.location.Location;
import android.support.constraint.ConstraintLayout;
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

import fifty.fiftyhouse.com.fifty.CommonData;
import fifty.fiftyhouse.com.fifty.CommonFunc;
import fifty.fiftyhouse.com.fifty.DataBase.ChatData;
import fifty.fiftyhouse.com.fifty.DataBase.UserData;
import fifty.fiftyhouse.com.fifty.GlobalApplication;
import fifty.fiftyhouse.com.fifty.Manager.TKManager;
import fifty.fiftyhouse.com.fifty.R;

public class MainAdapter extends RecyclerView.Adapter<MainUserListHolder> {

    Context mContext;
    int viewCount = 0;

    int UserCount = 0;
    CommonData.MainViewType UserType = CommonData.MainViewType.DIST;
    String mUserName = null;
    String mUserDist = null;
    int mUserGender = 0;
    String mUserState = null;
    String[] mUserImg = new String[8];

    public MainAdapter(Context context) {
        mContext = context;
    }

    public MainAdapter(Context context, CommonData.MainViewType type, int count) {
        mContext = context;
        SetItemCountByType(type, count);
    }

    @Override
    public MainUserListHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.adapter_main, parent, false);
        return new MainUserListHolder(view);
    }

    @Override
    public void onBindViewHolder(MainUserListHolder holder, final int position) {

        SetItemByType(UserType, position);

        holder.tv_Main_Thumbnail_Info_NickName.setText(mUserName);
        holder.tv_Main_Thumbnail_Info_State.setText(mUserDist);
        holder.tv_Main_Thumbnail_Online_State.setText(mUserState);


        if (mUserGender == 0) {
            Glide.with(mContext).load(R.drawable.ic_man_simple)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(holder.iv_Main_Gender);
        } else {
            Glide.with(mContext).load(R.drawable.ic_woman_simple)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(holder.iv_Main_Gender);
        }

        viewCount = CommonFunc.getInstance().MainUserListSlotCount;
        int thumbnailMargin = CommonFunc.getInstance().convertPXtoDP(mContext.getResources(), 2);
        int thumbnailSize = CommonFunc.getInstance().getWidthByDevice() / viewCount - CommonFunc.getInstance().convertPXtoDP(mContext.getResources(), 2);

        ConstraintLayout.LayoutParams lp_Main_Thumbnail_1 = new ConstraintLayout.LayoutParams(thumbnailSize, thumbnailSize);
        lp_Main_Thumbnail_1.leftToLeft = ConstraintLayout.LayoutParams.PARENT_ID;
        lp_Main_Thumbnail_1.topToTop = ConstraintLayout.LayoutParams.PARENT_ID;
        lp_Main_Thumbnail_1.setMargins(thumbnailMargin + CommonFunc.getInstance().convertPXtoDP(mContext.getResources(), 5),thumbnailMargin,thumbnailMargin,thumbnailMargin);
        holder.iv_Main_Thumbnail_1.setLayoutParams(lp_Main_Thumbnail_1);

        if(CommonFunc.getInstance().CheckStringNull(mUserImg[0]) == false)
        {
            Glide.with(mContext).load(mUserImg[0])
                    .centerCrop()
                    .into(holder.iv_Main_Thumbnail_1);
        }
        else
            CommonFunc.getInstance().DrawImageByGlide(mContext, holder.iv_Main_Thumbnail_1, R.drawable.bg_empty_square, false);

        ConstraintLayout.LayoutParams lp_Main_Thumbnail_2 = new ConstraintLayout.LayoutParams(thumbnailSize, thumbnailSize);
        lp_Main_Thumbnail_2.leftToRight = holder.iv_Main_Thumbnail_1.getId();
        lp_Main_Thumbnail_2.topToTop = ConstraintLayout.LayoutParams.PARENT_ID;
        lp_Main_Thumbnail_2.setMargins(thumbnailMargin,thumbnailMargin,thumbnailMargin,thumbnailMargin);
        holder.iv_Main_Thumbnail_2.setLayoutParams(lp_Main_Thumbnail_2);

        if(CommonFunc.getInstance().CheckStringNull(mUserImg[1]) == false)
        {
            Glide.with(mContext).load(mUserImg[1])
                    .centerCrop()
                    .into(holder.iv_Main_Thumbnail_2);
        }
        else
            CommonFunc.getInstance().DrawImageByGlide(mContext, holder.iv_Main_Thumbnail_2, R.drawable.bg_empty_square, false);

        ConstraintLayout.LayoutParams lp_Main_Thumbnail_3 = new ConstraintLayout.LayoutParams(thumbnailSize, thumbnailSize);
        lp_Main_Thumbnail_3.leftToRight = holder.iv_Main_Thumbnail_2.getId();
        lp_Main_Thumbnail_3.topToTop = ConstraintLayout.LayoutParams.PARENT_ID;
        lp_Main_Thumbnail_3.setMargins(thumbnailMargin,thumbnailMargin,thumbnailMargin,thumbnailMargin);
        holder.iv_Main_Thumbnail_3.setLayoutParams(lp_Main_Thumbnail_3);
        if(CommonFunc.getInstance().CheckStringNull(mUserImg[2]) == false)
        {
            CommonFunc.getInstance().DrawImageByGlide(mContext, holder.iv_Main_Thumbnail_3, mUserImg[2], false);
        }
        else
            CommonFunc.getInstance().DrawImageByGlide(mContext, holder.iv_Main_Thumbnail_3, R.drawable.bg_empty_square, false);
    }

    @Override
    public long getItemId(int position) {
        long rtValue = 0;

        switch (UserType)
        {
            case DIST:
                rtValue = Long.valueOf(TKManager.getInstance().UserData_Simple.get(TKManager.getInstance().View_UserList_Dist.get(position)).GetUserIndex());
                break;
            case NEW:
                rtValue = Long.valueOf(TKManager.getInstance().UserData_Simple.get(TKManager.getInstance().View_UserList_New.get(position)).GetUserIndex());
                break;
            case HOT:
                rtValue = Long.valueOf(TKManager.getInstance().UserData_Simple.get(TKManager.getInstance().View_UserList_Hot.get(position)).GetUserIndex());
                break;
            case FRIEND:
                Set tempKey = TKManager.getInstance().MyData.GetUserFriendListKeySet();
                List array = new ArrayList(tempKey);
                rtValue = Long.valueOf(TKManager.getInstance().UserData_Simple.get(array.get(position).toString()).GetUserIndex());
                break;
        }
        //rtValue = Long.valueOf(mMyData.arrUserAll_Hot_Age.get(position).Idx);

        return rtValue;
    }


    @Override
    public int getItemCount() {
        int rtValue = UserCount;
        return rtValue;
    }

    @Override
    public int getItemViewType(int position) {
        return position % 2 * 2;
    }

    public void SetItemCountByType(CommonData.MainViewType type, int count)
    {
        UserType = type;
        UserCount = count;
    }

    public void SetItemByType(CommonData.MainViewType type, int position)
    {
        UserData tempData = new UserData();
        double Distance = 0;
        switch (type)
        {
            case DIST:
                tempData = (UserData)(TKManager.getInstance().UserData_Simple.get(TKManager.getInstance().View_UserList_Dist.get(position)));
                break;

            case NEW:
                tempData = (UserData)(TKManager.getInstance().UserData_Simple.get(TKManager.getInstance().View_UserList_New.get(position)));
                break;

            case HOT:
                tempData = (UserData)(TKManager.getInstance().UserData_Simple.get(TKManager.getInstance().View_UserList_Hot.get(position)));
                break;

            case FRIEND:

                Set tempKey = TKManager.getInstance().MyData.GetUserFriendListKeySet();
                List array = new ArrayList(tempKey);

                tempData = (UserData)(TKManager.getInstance().UserData_Simple.get(array.get(position).toString()));
                break;

        }

        mUserName = tempData.GetUserNickName();

        /*if(tempData.GetUserDist_Lat() == 0)
        {
            tempData.SetUserDist_Lat(37.566659);
        }
        if(tempData.GetUserDist_Lon() == 0)
        {
            tempData.SetUserDist_Lon(126.978425);
        }
*/
  //      Distance = CommonFunc.getInstance().DistanceByDegree(TKManager.getInstance().MyData.GetUserDist_Lat(), TKManager.MyData.GetUserDist_Lon(), tempData.GetUserDist_Lat(), tempData.GetUserDist_Lon());


        if(tempData.GetUserDist() < 1000)
        {
            mUserDist = "1km 이내";
        }
        else
        {
            mUserDist = (int)(tempData.GetUserDist()  / 1000) + "km";
        }

       // mUserDist = Long.toString(tempData.GetUserDist());

        for(int i = 0 ; i< 8 ; i++)
            mUserImg[i] = null;

        for(int i = 0; i < tempData.GetUserImgCount(); i++)
        {
            mUserImg[i] = tempData.GetUserImg(Integer.toString(i));
        }

        mUserGender = tempData.GetUserGender();
        mUserState = CommonFunc.getInstance().GetConnectGap(CommonFunc.getInstance().GetCurrentTime(), Long.toString(tempData.GetUserConnectDate()));
    }
}

class MainUserListHolder extends RecyclerView.ViewHolder{

    public ImageView iv_Main_Thumbnail_1,iv_Main_Thumbnail_2,iv_Main_Thumbnail_3, iv_Main_Gender;
    public TextView tv_Main_Thumbnail_Info_NickName, tv_Main_Thumbnail_Info_State, tv_Main_Thumbnail_Online_State;

    public MainUserListHolder(View itemView) {
        super(itemView);
        //v_Main_Thumbnail = itemView.findViewById(R.id.v_Main_Thumbnail);
        iv_Main_Thumbnail_1 = itemView.findViewById(R.id.iv_Main_Thumbnail_1);
        iv_Main_Thumbnail_2 = itemView.findViewById(R.id.iv_Main_Thumbnail_2);
        iv_Main_Thumbnail_3 = itemView.findViewById(R.id.iv_Main_Thumbnail_3);
        tv_Main_Thumbnail_Info_NickName = itemView.findViewById(R.id.tv_Main_Thumbnail_Info_NickName);
        iv_Main_Gender = itemView.findViewById(R.id.iv_Main_Gender);
        tv_Main_Thumbnail_Info_State = itemView.findViewById(R.id.tv_Main_Thumbnail_Info_State);
        tv_Main_Thumbnail_Online_State = itemView.findViewById(R.id.tv_Main_Thumbnail_Online_State);
    }
}

