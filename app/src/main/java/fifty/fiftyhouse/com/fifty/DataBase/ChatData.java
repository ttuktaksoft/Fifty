package fifty.fiftyhouse.com.fifty.DataBase;

/**
 * Created by boram on 2019-06-11.
 */

public class ChatData {
    private  String RoomIndex;

    private  String FromIndex ;
    private  String FromNickName;
    private  String FromThumbNail;

    private  String ToIndex ;
    private  String ToNickName;
    private  String ToThumbNail;

    private  String LastMsg;
    private  String LastMsgDate;

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
    public String GettOIndex()
    {
        return ToIndex;
    }
    public void SettONickName(String nickName){ ToNickName = nickName;}
    public String GetToNickName()
    {
        return ToNickName;
    }
    public void SetToThumbNail(String thumbNail) { ToThumbNail = thumbNail;   }
    public String GetToThumbNail()
    {
        return ToThumbNail;
    }

    public void SetLastMsg(String Msg) {LastMsg = Msg;}
    public String GetLastMsg()
    {
        return LastMsg;
    }
    public void SetLastMsgDate(String MsgDate) {LastMsgDate = MsgDate;}
    public String GetLastMsgDate()
    {
        return LastMsgDate;
    }
}
