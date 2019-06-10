package fifty.fiftyhouse.com.fifty.adapter;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import fifty.fiftyhouse.com.fifty.CommonFunc;
import fifty.fiftyhouse.com.fifty.Manager.TKManager;
import fifty.fiftyhouse.com.fifty.R;

public class UserProfilePhotoAdapter extends RecyclerView.Adapter<UserProfilePhotoListHolder> {

    Context mContext;
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

        holder.setData(i);

    }

    @Override
    public int getItemCount() {
        return 8;
        //return  TKManager.getInstance().TargetUserData.GetUserImgCount();
        //return mMyData.arrChatTargetData.size();
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
        int thumbnailSize = CommonFunc.getInstance().getWidthByDevice() / viewCount - CommonFunc.getInstance().convertPXtoDP(mContext.getResources(), 2);
        ConstraintLayout.LayoutParams lp_Photo = new ConstraintLayout.LayoutParams(thumbnailSize, thumbnailSize);
        iv_UserProfile_Photo.setLayoutParams(lp_Photo);
    }

    public void setData(int i)
    {
        if(TKManager.getInstance().TargetUserData.GetUserImg(Integer.toString(i)) != null)
        {
            // TODO 비어 있을때
            Glide.with(mContext).load(TKManager.getInstance().TargetUserData.GetUserImg(Integer.toString(i)) )
                    .centerCrop()
                    .into(iv_UserProfile_Photo);
        }
        else
        {
            // TODO 비어 있을때
            Glide.with(mContext).load(R.drawable.bg_empty_square)
                    .centerCrop()
                    .into(iv_UserProfile_Photo);
        }

    }
}
