package fifty.fiftyhouse.com.fifty.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import fifty.fiftyhouse.com.fifty.R;
import fifty.fiftyhouse.com.fifty.viewHolder.MyProfileClubListHolder;

public class MyProfileClubAdapter extends RecyclerView.Adapter<MyProfileClubListHolder> {

    Context mContext;
    public MyProfileClubAdapter(Context context) {
        mContext = context;
    }

    @Override
    public MyProfileClubListHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.adapter_myprofile_club, parent, false);

        return new MyProfileClubListHolder(view);
    }



    @Override
    public void onBindViewHolder(MyProfileClubListHolder holder, final int position) {
        int i = position;
        holder.setData(i);
    }

    @Override
    public int getItemCount() {
        return 3;
        //return mMyData.arrChatTargetData.size();
    }

}
