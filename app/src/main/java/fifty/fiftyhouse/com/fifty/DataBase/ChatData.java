package fifty.fiftyhouse.com.fifty.DataBase;

import fifty.fiftyhouse.com.fifty.CommonData;

/**
 * Created by boram on 2019-06-11.
 */

public class ChatData implements Cloneable{

    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    public   String RoomIndex;
    public  CommonData.CHAT_ROOM_TYPE RoomType;

    public  String FromIndex ;
    public  String FromNickName;
    public  String FromThumbNail;

    public  String ToIndex ;
    public  String ToNickName;
    public  String ToThumbNail;

    public  String Msg;
    public  long MsgIndex;
    public  String MsgSender;
    public  CommonData.MSGType MsgType;
    public  long MsgDate;
    public  boolean MsgReadCheck;

    public void SetRoomIndex(String roomIndex)
    {
        RoomIndex = roomIndex;
    }
    public String GetRoomIndex()
    {
        return RoomIndex;
    }

    public void SetRoomType(CommonData.CHAT_ROOM_TYPE type)
    {
        RoomType = type;
    }
    public CommonData.CHAT_ROOM_TYPE GetRoomType()
    {
        return RoomType;
    }

    public void SetFromIndex(String fromIndex)
    {
        FromIndex = fromIndex;
    }
    public String GetFromIndex()
    {
        return FromIndex;
    }
    public void SetFromNickName(String fromNickName)
    {
        FromNickName = fromNickName;
    }
    public String GetFromNickName()
    {
        return FromNickName;
    }
    public void SetFromThumbNail(String thumbNail)
    {
        FromThumbNail = thumbNail;
    }
    public String GetFromThumbNail()
    {
        return FromThumbNail;
    }

    public void SetToIndex(String Index)
    {
        ToIndex = Index;
    }
    public String GetToIndex()
    {
        return ToIndex;
    }
    public void SetToNickName(String nickName){ ToNickName = nickName;}
    public String GetToNickName()
    {
        return ToNickName;
    }
    public void SetToThumbNail(String thumbNail) { ToThumbNail = thumbNail;   }
    public String GetToThumbNail()
    {
        return ToThumbNail;
    }

    public void SetMsgIndex(long msgindex) {MsgIndex = msgindex;}
    public long GetMsgIndex()
    {
        return MsgIndex;
    }

    public void SetMsg(String msg) {Msg = msg;}
    public String GetMsg()
    {
        return Msg;
    }
    public void SetMsgSender(String msgSender) {MsgSender = msgSender;}
    public String GetMsgSender()
    {
        return MsgSender;
    }

    public void SetMsgType(CommonData.MSGType type) {MsgType = type;}
    public CommonData.MSGType GetMsgType()
    {
        return MsgType;
    }
    public void SetMsgDate(long msgDate) {MsgDate = msgDate;}
    public long GetMsgDate()
    {
        return MsgDate;
    }

    public void SetMsgReadCheck(boolean msgReadCheck) {MsgReadCheck = msgReadCheck;}
    public boolean GetMsgReadCheck()
    {
        return MsgReadCheck;
    }
}
