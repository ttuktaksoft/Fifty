package fifty.fiftyhouse.com.fifty.adapter;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;

import java.util.Iterator;
import java.util.Objects;
import java.util.Set;

import fifty.fiftyhouse.com.fifty.CommonData;
import fifty.fiftyhouse.com.fifty.CommonFunc;
import fifty.fiftyhouse.com.fifty.DataBase.UserData;
import fifty.fiftyhouse.com.fifty.Manager.TKManager;
import fifty.fiftyhouse.com.fifty.R;
import fifty.fiftyhouse.com.fifty.viewHolder.MainUserListHolder;

public class MainAdapter extends RecyclerView.Adapter<MainUserListHolder> {

    Context mContext;
    int viewCount = 0;

    int UserCount = 0;
    CommonData.MainViewType UserType = CommonData.MainViewType.DIST;
    String mUserName = null;
    String mUserDist = null;
    String[] mUserImg = new String[3];

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

        viewCount = CommonFunc.getInstance().MainUserListSlotCount;
        int thumbnailMargin = CommonFunc.getInstance().convertPXtoDP(mContext.getResources(), 2);
        int thumbnailSize = CommonFunc.getInstance().getWidthByDevice() / viewCount - CommonFunc.getInstance().convertPXtoDP(mContext.getResources(), 2);

        ConstraintLayout.LayoutParams lp_Main_Thumbnail_1 = new ConstraintLayout.LayoutParams(thumbnailSize, thumbnailSize);
        lp_Main_Thumbnail_1.leftToLeft = ConstraintLayout.LayoutParams.PARENT_ID;
        lp_Main_Thumbnail_1.topToTop = ConstraintLayout.LayoutParams.PARENT_ID;
        lp_Main_Thumbnail_1.setMargins(thumbnailMargin + CommonFunc.getInstance().convertPXtoDP(mContext.getResources(), 5),thumbnailMargin,thumbnailMargin,thumbnailMargin);
        holder.iv_Main_Thumbnail_1.setLayoutParams(lp_Main_Thumbnail_1);
        Glide.with(mContext).load(mUserImg[0])
                .centerCrop()
                .into(holder.iv_Main_Thumbnail_1);

        ConstraintLayout.LayoutParams lp_Main_Thumbnail_2 = new ConstraintLayout.LayoutParams(thumbnailSize, thumbnailSize);
        lp_Main_Thumbnail_2.leftToRight = holder.iv_Main_Thumbnail_1.getId();
        lp_Main_Thumbnail_2.topToTop = ConstraintLayout.LayoutParams.PARENT_ID;
        lp_Main_Thumbnail_2.setMargins(thumbnailMargin,thumbnailMargin,thumbnailMargin,thumbnailMargin);
        holder.iv_Main_Thumbnail_2.setLayoutParams(lp_Main_Thumbnail_2);
        Glide.with(mContext).load(mUserImg[1])
                .centerCrop()
                .into(holder.iv_Main_Thumbnail_2);

        ConstraintLayout.LayoutParams lp_Main_Thumbnail_3 = new ConstraintLayout.LayoutParams(thumbnailSize, thumbnailSize);
        lp_Main_Thumbnail_3.leftToRight = holder.iv_Main_Thumbnail_2.getId();
        lp_Main_Thumbnail_3.topToTop = ConstraintLayout.LayoutParams.PARENT_ID;
        lp_Main_Thumbnail_3.setMargins(thumbnailMargin,thumbnailMargin,thumbnailMargin,thumbnailMargin);
        holder.iv_Main_Thumbnail_3.setLayoutParams(lp_Main_Thumbnail_3);
        Glide.with(mContext).load(mUserImg[2])
                .centerCrop()
                .into(holder.iv_Main_Thumbnail_3);

    }

    @Override
    public long getItemId(int position) {
        long rtValue = 0;
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
        switch (type)
        {
            case DIST:
                tempData = (UserData)(TKManager.getInstance().UserData_Simple.get(TKManager.getInstance().UserList_Dist.get(position)));
                mUserName = TKManager.getInstance().UserData_Simple.get(TKManager.getInstance().UserList_Dist.get(position)).GetUserNickName();
                mUserDist = "400m";

                for(int i = 0; i < TKManager.getInstance().UserData_Simple.get(TKManager.getInstance().UserList_Dist.get(position)).GetUserImgCount(); i++)
                {
                    mUserImg[i] = TKManager.getInstance().UserData_Simple.get(TKManager.getInstance().UserList_Dist.get(position)).GetUserImg(i);
                }
                break;

            case NEW:
                tempData = (UserData)(TKManager.getInstance().UserData_Simple.get(TKManager.getInstance().UserList_New.get(position)));
                mUserName = tempData.GetUserNickName();
                mUserDist = "400m";
                for(int i = 0; i < TKManager.getInstance().UserData_Simple.get(TKManager.getInstance().UserList_New.get(position)).GetUserImgCount(); i++)
                {
                    mUserImg[i] = TKManager.getInstance().UserData_Simple.get(TKManager.getInstance().UserList_New.get(position)).GetUserImg(i);
                }
                break;

            case HOT:
                tempData = (UserData)(TKManager.getInstance().UserData_Simple.get(TKManager.getInstance().UserList_Hot.get(position)));
                mUserName = tempData.GetUserNickName();
                mUserDist = "400m";
                for(int i = 0; i < TKManager.getInstance().UserData_Simple.get(TKManager.getInstance().UserList_Hot.get(position)).GetUserImgCount(); i++)
                {
                    mUserImg[i] = TKManager.getInstance().UserData_Simple.get(TKManager.getInstance().UserList_Hot.get(position)).GetUserImg(i);
                }
                break;

            case FRIEND:
                tempData = (UserData)(TKManager.getInstance().UserData_Simple.get(TKManager.getInstance().UserList_Friend.get(position)));
                mUserName = tempData.GetUserNickName();
                mUserDist = "400m";
                for(int i = 0; i < TKManager.getInstance().UserData_Simple.get(TKManager.getInstance().UserList_Friend.get(position)).GetUserImgCount(); i++)
                {
                    mUserImg[i] = TKManager.getInstance().UserData_Simple.get(TKManager.getInstance().UserList_Friend.get(position)).GetUserImg(i);
                }
                break;

        }

    }
}
