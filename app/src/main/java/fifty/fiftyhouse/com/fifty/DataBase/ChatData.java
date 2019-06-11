package fifty.fiftyhouse.com.fifty.DataBase;

import fifty.fiftyhouse.com.fifty.CommonData;

/**
 * Created by boram on 2019-06-11.
 */

public class ChatData {
    private  String RoomIndex;

    private String FromIndex ;
    private String FromNickName;
    private String FromThumbNail;

    private String ToIndex ;
    private String ToNickName;
    private String ToThumbNail;

    private String Msg;
    private String MSGSender;
    private CommonData.MSGType MSGType;
    private long MsgDate;

    public void SetRoomIndex(String roomIndex)
    {
        RoomIndex = roomIndex;
    }
    public String GetRoomIndex()
    {
        return RoomIndex;
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

    public void SetMSG(String msg) {Msg = msg;}
    public String GetMSG()
    {
        return Msg;
    }
    public void SetMSGSender(String msgSender) {MSGSender = msgSender;}
    public String GetMSGSender()
    {
        return MSGSender;
    }

    public void SetMSGType(CommonData.MSGType type) {MSGType = type;}
    public CommonData.MSGType GetMSGType()
    {
        return MSGType;
    }
    public void SetMsgDate(long msgDate) {MsgDate = msgDate;}
    public long GetMsgDate()
    {
        return MsgDate;
    }

}
