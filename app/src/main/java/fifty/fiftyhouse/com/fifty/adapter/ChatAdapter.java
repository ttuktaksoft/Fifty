package fifty.fiftyhouse.com.fifty.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.chauthai.swipereveallayout.SwipeRevealLayout;
import com.chauthai.swipereveallayout.ViewBinderHelper;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import fifty.fiftyhouse.com.fifty.CommonData;
import fifty.fiftyhouse.com.fifty.CommonFunc;
import fifty.fiftyhouse.com.fifty.DataBase.ChatData;
import fifty.fiftyhouse.com.fifty.DialogFunc;
import fifty.fiftyhouse.com.fifty.GlobalApplication;
import fifty.fiftyhouse.com.fifty.Manager.FirebaseManager;
import fifty.fiftyhouse.com.fifty.Manager.TKManager;
import fifty.fiftyhouse.com.fifty.R;
import fifty.fiftyhouse.com.fifty.activty.ChatBodyActivity;
import fifty.fiftyhouse.com.fifty.viewPager.ChatViewPager;

public class ChatAdapter extends RecyclerView.Adapter<ChatListHolder> {
    Context mContext;
    int mItemCount;
    ArrayList<String> mItemData = new ArrayList<>();

    private final ViewBinderHelper binderHelper = new ViewBinderHelper();

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
        binderHelper.bind(holder.swipeLayout,mItemData.get(i));
        holder.bind(mItemData.get(i));
        holder.setData(mItemData.get(i));
    }

    @Override
    public int getItemCount() {
        return mItemCount;
    }

    @Override
    public long getItemId(int position) {
        long rtValue = 0;

        ChatData tempChatData = TKManager.getInstance().MyData.GetUserChatDataList(mItemData.get(position));

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

    public void setItemData(ArrayList<String> list)
    {
        mItemData.clear();
        mItemData.addAll(list);
        mItemCount = mItemData.size();
    }

}

class ChatListHolder extends RecyclerView.ViewHolder {
    public SwipeRevealLayout swipeLayout;

    private ConstraintLayout frontLayout;
    private View deleteLayout;

    public ImageView iv_Chat_Profile, iv_Chat_Check;
    public TextView tv_Chat_Nickname,tv_Chat_Msg, tv_Chat_Date, tv_Chat_Check;
    Context mContext;

    public ChatListHolder(View itemView) {
        super(itemView);
        mContext = itemView.getContext();

        swipeLayout = (SwipeRevealLayout) itemView.findViewById(R.id.swipe_layout);
        frontLayout = itemView.findViewById(R.id.front_layout);
        deleteLayout = itemView.findViewById(R.id.delete_layout);

        iv_Chat_Profile = itemView.findViewById(R.id.iv_Chat_Profile);
        tv_Chat_Nickname = itemView.findViewById(R.id.tv_Chat_Nickname);
        tv_Chat_Msg = itemView.findViewById(R.id.tv_Chat_Msg);
        tv_Chat_Date = itemView.findViewById(R.id.tv_Chat_Date);
        tv_Chat_Check = itemView.findViewById(R.id.tv_Chat_Check);
        iv_Chat_Check = itemView.findViewById(R.id.iv_Chat_Check);
    }

    public void setData(String key)
    {
        ChatData tempChatData = TKManager.getInstance().MyData.GetUserChatDataList(key);

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

    public void bind(final String data) {

        deleteLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // TODO 삭제
                TKManager.getInstance().MyData.DelUserChatDataList(data);
                TKManager.getInstance().mUpdateChatViewPagerFunc.UpdateUI();
            }
        });

        frontLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final String strTargetIndex;
                final ChatData tempChatData = TKManager.getInstance().MyData.GetUserChatDataList(data);

                int idx = tempChatData.GetRoomIndex().indexOf("_");
                String tempStr = tempChatData.GetRoomIndex().substring(0, idx);
                String tempStrBack = tempChatData.GetRoomIndex().substring(idx+1);
                if(tempStr.equals(TKManager.getInstance().MyData.GetUserIndex()))
                {
                    strTargetIndex = tempStrBack;
                }
                else
                {
                    strTargetIndex = tempStr;
                }


                FirebaseManager.CheckFirebaseComplete listener = new FirebaseManager.CheckFirebaseComplete() {
                    @Override
                    public void CompleteListener() {

                        FirebaseManager.CheckFirebaseComplete listener = new FirebaseManager.CheckFirebaseComplete() {
                            @Override
                            public void CompleteListener() {
                                Intent intent = new Intent(ChatViewPager.mChatViewPager.getContext(), ChatBodyActivity.class);
                                intent.putExtra("RoomIndex",tempChatData.GetRoomIndex());
                                //startActivity(intent);
                                ChatViewPager.mChatViewPager.startActivityForResult(intent, ChatViewPager.REFRESH_CHATFRAGMENT);
                            }

                            @Override
                            public void CompleteListener_Yes() {
                            }

                            @Override
                            public void CompleteListener_No() {
                            }
                        };

                        if(TKManager.getInstance().UserData_Simple.get(strTargetIndex) != null)
                        {
                            Intent intent = new Intent(ChatViewPager.mChatViewPager.getContext(), ChatBodyActivity.class);
                            intent.putExtra("RoomIndex",tempChatData.GetRoomIndex());
                            ChatViewPager.mChatViewPager.startActivityForResult(intent, ChatViewPager.REFRESH_CHATFRAGMENT);
                        }
                        else
                        {
                            FirebaseManager.getInstance().SetFireBaseLoadingCount(2);
                            FirebaseManager.getInstance().GetUserData_Simple(strTargetIndex, TKManager.getInstance().UserData_Simple, listener);
                        }

                    }

                    @Override
                    public void CompleteListener_Yes() {
                    }

                    @Override
                    public void CompleteListener_No() {
                    }
                };

                FirebaseManager.getInstance().GetUserChatData(tempChatData.GetRoomIndex(), TKManager.getInstance().MyData, listener);
            }
        });
    }
}
