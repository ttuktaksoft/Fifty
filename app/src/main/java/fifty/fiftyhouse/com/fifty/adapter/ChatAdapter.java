package fifty.fiftyhouse.com.fifty.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import fifty.fiftyhouse.com.fifty.R;

public class ChatAdapter extends RecyclerView.Adapter<ChatListHolder> {
    Context mContext;
    public ChatAdapter(Context context) {
        mContext = context;
    }

    @Override
    public ChatListHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.adapter_chat, parent, false);
        return new ChatListHolder(view);
    }



    @Override
    public void onBindViewHolder(ChatListHolder holder, final int position) {
        int i = position;

    }

    @Override
    public int getItemCount() {
        return 10;
        //return mMyData.arrChatTargetData.size();
    }

}

class ChatListHolder extends RecyclerView.ViewHolder {

    public ImageView iv_Chat_Profile;
    public TextView tv_Chat_Nickname,tv_Chat_Msg, tv_Chat_Date, tv_Chat_Check;

    public ChatListHolder(View itemView) {
        super(itemView);

        iv_Chat_Profile = itemView.findViewById(R.id.iv_Chat_Profile);
        tv_Chat_Nickname = itemView.findViewById(R.id.tv_Chat_Nickname);
        tv_Chat_Msg = itemView.findViewById(R.id.tv_Chat_Msg);
        tv_Chat_Date = itemView.findViewById(R.id.tv_Chat_Date);
        tv_Chat_Check = itemView.findViewById(R.id.tv_Chat_Check);
    }
}
