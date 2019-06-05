package fifty.fiftyhouse.com.fifty.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import fifty.fiftyhouse.com.fifty.R;
import fifty.fiftyhouse.com.fifty.viewHolder.StrContentListListHolder;

public class StrContentListAdapter extends RecyclerView.Adapter<StrContentListListHolder> {

    Context mContext;
    public StrContentListAdapter(Context context) {
        mContext = context;
    }

    @Override
    public StrContentListListHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.adapter_str_content_list, parent, false);

        return new StrContentListListHolder(view);
    }



    @Override
    public void onBindViewHolder(StrContentListListHolder holder, final int position) {
        int i = position;

        holder.setData(i);
    }

    @Override
    public int getItemCount() {
        return 20;
        //return  TKManager.getInstance().TargetUserData.GetUserImgCount();
        //return mMyData.arrChatTargetData.size();
    }

}