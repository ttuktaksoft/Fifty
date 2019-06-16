package fifty.fiftyhouse.com.fifty.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import fifty.fiftyhouse.com.fifty.CommonData;
import fifty.fiftyhouse.com.fifty.CommonFunc;
import fifty.fiftyhouse.com.fifty.DataBase.ChatData;
import fifty.fiftyhouse.com.fifty.Manager.TKManager;
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
        holder.setData(i);
    }

    @Override
    public int getItemCount() {
        return TKManager.getInstance().MyData.GetUserChatDataListCount();
        //return mMyData.arrChatTargetData.size();
    }

    @Override
    public long getItemId(int position) {
        long rtValue = 0;

        Set tempKey = TKManager.getInstance().MyData.GetUserChatDataListKeySet();
        List array = new ArrayList(tempKey);
        ChatData tempChatData = TKManager.getInstance().MyData.GetUserChatDataList(array.get(position).toString());

        String tempID = null;

        int idx = tempChatData.GetRoomIndex().indexOf("_");
        String tempStr = tempChatData.GetRoomIndex().substring(0, idx);
        String tempStrBack = tempChatData.GetRoomIndex().substring(idx+1);
        if(tempStr.equals(TKManager.getInstance().MyData.GetUserIndex()))
        {
            tempID = tempStrBack;
        }
        else
        {
            tempID = tempStr;
        }
        rtValue = Long.parseLong(tempID);



        //rtValue = Long.valueOf(mMyData.arrUserAll_Hot_Age.get(position).Idx);

        return rtValue;
    }

}

class ChatListHolder extends RecyclerView.ViewHolder {

    public ImageView iv_Chat_Profile, iv_Chat_Check;
    public TextView tv_Chat_Nickname,tv_Chat_Msg, tv_Chat_Date, tv_Chat_Check;
    Context mContext;

    public ChatListHolder(View itemView) {
        super(itemView);
        mContext = itemView.getContext();

        iv_Chat_Profile = itemView.findViewById(R.id.iv_Chat_Profile);
        tv_Chat_Nickname = itemView.findViewById(R.id.tv_Chat_Nickname);
        tv_Chat_Msg = itemView.findViewById(R.id.tv_Chat_Msg);
        tv_Chat_Date = itemView.findViewById(R.id.tv_Chat_Date);
        tv_Chat_Check = itemView.findViewById(R.id.tv_Chat_Check);
        iv_Chat_Check = itemView.findViewById(R.id.iv_Chat_Check);
    }

    public void setData(int i)
    {
        Set tempKey = TKManager.getInstance().MyData.GetUserChatDataListKeySet();
        List array = new ArrayList(tempKey);
        ChatData tempChatData = TKManager.getInstance().MyData.GetUserChatDataList(array.get(i).toString());

        if(tempChatData.GetMsgType() == CommonData.MSGType.MSG)
        {
            tv_Chat_Msg.setText(tempChatData.GetMsg());
        }
        else
        {
            tv_Chat_Msg.setText("사진이 추가되었습니다");
        }

        String tempMsgDate = Long.toString(tempChatData.GetMsgDate());

        String tempDate = tempMsgDate.substring(0, 7);
        String tempTime = tempMsgDate.substring(8, 12);

        String tempHour = tempTime.substring(0, 2);
        int tempHourInteger = Integer.parseInt(tempHour);

        String tempMinute = tempTime.substring(2, 4);
        if(Integer.parseInt(tempHour) > 12) {
            tempHour = Integer.toString(tempHourInteger - 12);
            tv_Chat_Date.setText("오후 "+ tempHour + ":" + tempMinute);
        }
        else
        {
            tv_Chat_Date.setText("오전 "+ tempHour + ":" + tempMinute);
        }

        //tv_Chat_Date.setText(Long.toString(tempChatData.GetMsgDate()));

        if(tempChatData.GetFromIndex().equals(TKManager.getInstance().MyData.GetUserIndex()))
        {
            CommonFunc.getInstance().DrawImageByGlide(mContext, iv_Chat_Profile, tempChatData.GetToThumbNail(), true);
            tv_Chat_Nickname.setText(tempChatData.GetToNickName());

        }
        else
        {
            CommonFunc.getInstance().DrawImageByGlide(mContext, iv_Chat_Profile, tempChatData.GetFromThumbNail(), true);
            tv_Chat_Nickname.setText(tempChatData.GetFromNickName());
        }

        long tempReadCount = 0 ;
        if(TKManager.getInstance().MyData.GetUserChatReadIndexList(tempChatData.GetRoomIndex()) == null)
        {
            tempReadCount = tempChatData.GetMsgIndex();
        }
        else
         tempReadCount = tempChatData.GetMsgIndex() - TKManager.getInstance().MyData.GetUserChatReadIndexList(tempChatData.GetRoomIndex());

        if(tempReadCount > 0)
        {
            tv_Chat_Check.setVisibility(View.VISIBLE);
            iv_Chat_Check.setVisibility(View.VISIBLE);

            tv_Chat_Check.setText(Long.toString(tempReadCount));
        }

        else
        {
            tv_Chat_Check.setVisibility(View.INVISIBLE);
            iv_Chat_Check.setVisibility(View.INVISIBLE);
        }

    }
}
