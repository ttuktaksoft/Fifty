package fifty.fiftyhouse.com.fifty.adapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import fifty.fiftyhouse.com.fifty.DataBase.NoticeData;
import fifty.fiftyhouse.com.fifty.Manager.TKManager;
import fifty.fiftyhouse.com.fifty.R;

public class StrContentListAdapter extends RecyclerView.Adapter<StrContentListListHolder> {

    Context mContext;
    int mItemCount = 0;
    ArrayList<NoticeData> mItemList = new ArrayList<>();

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

        holder.setData(mItemList.get(i));
    }

    @Override
    public int getItemCount() {
        return mItemCount;
        //return  TKManager.getInstance().TargetUserData.GetUserImgCount();
        //return mMyData.arrChatTargetData.size();
    }

    public void setItemCount(int count)
    {
        mItemCount = count;
    }

    public void setItemData(ArrayList<NoticeData> list)
    {
        mItemList.clear();
        mItemList.addAll(list);
        mItemCount = mItemList.size();
    }

}

class StrContentListListHolder extends RecyclerView.ViewHolder {

    TextView tv_StrContent_Desc;
    Context mContext;

    public StrContentListListHolder(View itemView) {
        super(itemView);
        mContext = itemView.getContext();

        tv_StrContent_Desc = itemView.findViewById(R.id.tv_StrContent_Desc);
    }

    public void setData(NoticeData data)
    {
        tv_StrContent_Desc.setText(data.GetTitle());
    }
}