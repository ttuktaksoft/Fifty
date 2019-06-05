package fifty.fiftyhouse.com.fifty.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import fifty.fiftyhouse.com.fifty.R;
import fifty.fiftyhouse.com.fifty.viewHolder.MyProfileEtcListHolder;

public class MyProfileEtcAdapter extends RecyclerView.Adapter<MyProfileEtcListHolder> {

    Context mContext;

    public MyProfileEtcAdapter(Context context) {
        mContext = context;
    }

    @Override
    public MyProfileEtcListHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.adapter_myprofile_etc, parent, false);

        return new MyProfileEtcListHolder(view);
    }


    @Override
    public void onBindViewHolder(MyProfileEtcListHolder holder, final int position) {
        int i = position;
        holder.setData(i);;
    }

    @Override
    public int getItemCount() {
        return 4;
        //return mMyData.arrChatTargetData.size();
    }
}
