package fifty.fiftyhouse.com.fifty.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import fifty.fiftyhouse.com.fifty.R;
import fifty.fiftyhouse.com.fifty.viewHolder.ChatBodyListHolder;

public class ChatBodyAdapter extends RecyclerView.Adapter<ChatBodyListHolder> {

    Context mContext;
    public ChatBodyAdapter(Context context) {
        mContext = context;
    }

    @Override
    public ChatBodyListHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.adapter_chat_body, parent, false);
        return new ChatBodyListHolder(view);
    }



    @Override
    public void onBindViewHolder(ChatBodyListHolder holder, final int position) {
        int i = position;

        holder.setChatData();
    }

    @Override
    public int getItemCount() {
        return 10;
    }
}