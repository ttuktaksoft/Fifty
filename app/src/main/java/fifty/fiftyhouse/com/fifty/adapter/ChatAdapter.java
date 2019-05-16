package fifty.fiftyhouse.com.fifty.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import fifty.fiftyhouse.com.fifty.R;
import fifty.fiftyhouse.com.fifty.viewHolder.ChatListHolder;

public class ChatAdapter extends RecyclerView.Adapter<ChatListHolder> {
    Context mContext;
    public ChatAdapter(Context context) {
        mContext = context;
    }

    @Override
    public ChatListHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.fragment_chat_adapter, parent, false);

        //view.setLayoutParams(new_img RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,mUIData.getHeight()/7));
        return new ChatListHolder(view);
    }



    @Override
    public void onBindViewHolder(ChatListHolder holder, final int position) {
        int i = position;

    }

    @Override
    public int getItemCount() {
        return 0;
        //return mMyData.arrChatTargetData.size();
    }

}