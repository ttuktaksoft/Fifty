package fifty.fiftyhouse.com.fifty.adapter;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import fifty.fiftyhouse.com.fifty.CommonData;
import fifty.fiftyhouse.com.fifty.CommonFunc;
import fifty.fiftyhouse.com.fifty.Manager.TKManager;
import fifty.fiftyhouse.com.fifty.R;

public class UserProfilePhotoAdapter extends RecyclerView.Adapter<UserProfilePhotoListHolder> {

    public enum PROFILE_PHOTO_TYPE
    {
        MY_PROFILE,
        USER_PROFILE
    }
    Context mContext;
    int mItemCount = 0;
    ArrayList<String> mItemList = new ArrayList<>();
    PROFILE_PHOTO_TYPE mProfilePhotoType = PROFILE_PHOTO_TYPE.USER_PROFILE;

    public UserProfilePhotoAdapter(Context context) {
        mContext = context;
    }

    @Override
    public UserProfilePhotoListHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.adapter_user_profile_photo, parent, false);

        return new UserProfilePhotoListHolder(view);
    }



    @Override
    public void onBindViewHolder(UserProfilePhotoListHolder holder, final int position) {
        int i = position;

        if(mItemList.size() <= i)
        {
            if(mProfilePhotoType == PROFILE_PHOTO_TYPE.MY_PROFILE)
            {
                if(i == mItemList.size())
                    holder.setData("", true);
                else
                    holder.setData("", false);
            }
            else
                holder.setData("", false);
        }
        else
        {
            holder.setData(mItemList.get(i), false);
        }


    }

    @Override
    public int getItemCount() {
        return CommonData.MAX_PROFILE_IMG_COUNT;
        //return  TKManager.getInstance().TargetUserData.GetUserImgCount();
        //return mMyData.arrChatTargetData.size();
    }

    public void setProfilePhotoType(PROFILE_PHOTO_TYPE type)
    {
        mProfilePhotoType = type;
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

class UserProfilePhotoListHolder extends RecyclerView.ViewHolder {

    public ImageView iv_UserProfile_Photo;
    Context mContext;

    public UserProfilePhotoListHolder(View itemView) {
        super(itemView);
        mContext = itemView.getContext();

        iv_UserProfile_Photo = itemView.findViewById(R.id.iv_UserProfile_Photo);

        int viewCount = 4;
        int thumbnailSize = CommonFunc.getInstance().getWidthByDevice() / viewCount;
        ConstraintLayout.LayoutParams lp_Photo = new ConstraintLayout.LayoutParams(thumbnailSize, thumbnailSize);
        int padding = CommonFunc.getInstance().convertDPtoPX(mContext.getResources(),2);
        iv_UserProfile_Photo.setPadding(padding,padding,padding,padding);
        iv_UserProfile_Photo.setLayoutParams(lp_Photo);
    }

    public void setData(String str, boolean plus)
    {
        if(CommonFunc.getInstance().CheckStringNull(str))
        {
            if(plus)
            {
                Glide.with(mContext).load(R.drawable.bg_empty_square_plus)
                        .centerCrop()
                        .into(iv_UserProfile_Photo);
            }
            else
            {
                Glide.with(mContext).load(R.drawable.bg_empty_square)
                        .centerCrop()
                        .into(iv_UserProfile_Photo);
            }

        }
        else
        {
            Glide.with(mContext).load(str)
                    .centerCrop()
                    .into(iv_UserProfile_Photo);
        }
    }
}
