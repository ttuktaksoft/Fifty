package fifty.fiftyhouse.com.fifty.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import fifty.fiftyhouse.com.fifty.R;
import fifty.fiftyhouse.com.fifty.viewHolder.MyProfilePhotoListHolder;

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