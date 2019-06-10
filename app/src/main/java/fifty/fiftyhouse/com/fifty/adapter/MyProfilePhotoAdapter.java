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

public class MyProfilePhotoAdapter extends RecyclerView.Adapter<MyProfilePhotoListHolder> {

    Context mContext;

    public MyProfilePhotoAdapter(Context context) {
        mContext = context;
    }

    @Override
    public MyProfilePhotoListHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.adapter_myprofile_photo, parent, false);

        return new MyProfilePhotoListHolder(view);
    }


    @Override
    public void onBindViewHolder(MyProfilePhotoListHolder holder, final int position) {
        int i = position;
        holder.setData(i);
    }

    @Override
    public int getItemCount() {
        return 8;
        //return mMyData.arrChatTargetData.size();
    }
}

class MyProfilePhotoListHolder extends RecyclerView.ViewHolder {

    ImageView iv_MyProfile_Photo;
    Context mContext;

    public MyProfilePhotoListHolder(View itemView) {
        super(itemView);

        mContext = itemView.getContext();
        iv_MyProfile_Photo = itemView.findViewById(R.id.iv_MyProfile_Photo);

        int viewCount = 4;
        int thumbnailSize = CommonFunc.getInstance().getWidthByDevice() / viewCount - CommonFunc.getInstance().convertPXtoDP(mContext.getResources(), 2);
        ConstraintLayout.LayoutParams lp_Photo = new ConstraintLayout.LayoutParams(thumbnailSize, thumbnailSize);
        iv_MyProfile_Photo.setLayoutParams(lp_Photo);
    }

    public void setData(int i)
    {

        if(TKManager.getInstance().MyData.GetUserImg(Integer.toString(i)) != null)
        {
            // TODO 비어 있을때
            Glide.with(mContext).load(TKManager.getInstance().MyData.GetUserImg(Integer.toString(i)) )
                    .centerCrop()
                    .into(iv_MyProfile_Photo);
        }
        else
        {
            // TODO 비어 있을때
            Glide.with(mContext).load(R.drawable.bg_empty_square)
                    .centerCrop()
                    .into(iv_MyProfile_Photo);
        }

    }
}