package fifty.fiftyhouse.com.fifty.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import fifty.fiftyhouse.com.fifty.CommonFunc;
import fifty.fiftyhouse.com.fifty.DataBase.ChatData;
import fifty.fiftyhouse.com.fifty.Manager.TKManager;
import fifty.fiftyhouse.com.fifty.R;
import fifty.fiftyhouse.com.fifty.viewHolder.ChatListHolder;

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
        holder.setData(i);
    }

    @Override
    public int getItemCount() {
        return TKManager.getInstance().MyData.GetUserChatListCount();
        //return mMyData.arrChatTargetData.size();
    }

}