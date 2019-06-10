package fifty.fiftyhouse.com.fifty.viewHolder;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import fifty.fiftyhouse.com.fifty.CommonFunc;
import fifty.fiftyhouse.com.fifty.DataBase.ChatData;
import fifty.fiftyhouse.com.fifty.Manager.TKManager;
import fifty.fiftyhouse.com.fifty.R;

public class ChatListHolder extends RecyclerView.ViewHolder {

    public  ImageView iv_Chat_Profile;
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
    }

    public void setData(int i)
    {
        Set tempKey = TKManager.getInstance().MyData.GetUserChatListKeySet();
        List array = new ArrayList(tempKey);
        ChatData tempChatData = TKManager.getInstance().MyData.GetUserChatDataList(array.get(i).toString());

        tv_Chat_Msg.setText(tempChatData.GetLastMsg());
        tv_Chat_Date.setText(tempChatData.GetLastMsgDate());

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
    }
}
