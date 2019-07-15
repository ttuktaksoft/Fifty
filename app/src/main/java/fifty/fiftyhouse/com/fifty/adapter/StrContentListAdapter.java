package fifty.fiftyhouse.com.fifty.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import fifty.fiftyhouse.com.fifty.GlobalApplication;
import fifty.fiftyhouse.com.fifty.R;

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

class StrContentListListHolder extends RecyclerView.ViewHolder {

    TextView tv_StrContent_Desc;
    Context mContext;

    public StrContentListListHolder(View itemView) {
        super(itemView);
        mContext = itemView.getContext();

        tv_StrContent_Desc = itemView.findViewById(R.id.tv_StrContent_Desc);
        GlobalApplication.getGlobalApplicationContext().SetContentFont(tv_StrContent_Desc);
    }

    public void setData(int i)
    {
        tv_StrContent_Desc.setText("내용 입니다_" + i);
    }
}