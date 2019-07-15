package fifty.fiftyhouse.com.fifty.DataBase;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import fifty.fiftyhouse.com.fifty.activty.LoginActivity;

public class AlarmData {

    public long Date;
    public String Index;
    public String Type;
    public String Msg;

    public Map<String, String> VisitMember = new LinkedHashMap<>();
    public Map<String, String> LikeMember = new LinkedHashMap<>();
    public Map<String, String> FriendMember = new LinkedHashMap<>();
    public Map<String, String> ChatMember = new LinkedHashMap<>();

    public void SetDate(long date)
    {
        Date = date;
    }
    public long GetDate()
    {
        return Date;
    }
    public void SetIndex(String index)
    {
        Index = index;
    }
    public String GetIndex()
    {
        return Index;
    }
    public void SetType(String type)
    {
        Type = type;
    }
    public String GetType()
    {
        return Type;
    }

    public void SetMsg(String msg)
    {
        Msg = msg;
    }
    public String GetMsg()
    {
        return Msg;
    }

    public  Map<String, String> GetVisitMemberList()
    {
        return VisitMember;
    }
    public int  GetVisitMemberListCount()
    {
        return VisitMember.size();
    }
    public Set GetVisitMemberListKeySet()
    {
        return VisitMember.keySet();
    }
    public void  DelVisitMemberList(String key)
    {
        VisitMember.remove(key);
    }
    public void ClearVisitMember()
    {
        VisitMember.clear();
    }

    public  Map<String, String> GetLikeMemberList()
    {
        return LikeMember;
    }
    public int  GetLikeMemberListCount()
    {
        return LikeMember.size();
    }
    public Set GetLikeMemberListKeySet()
    {
        return LikeMember.keySet();
    }
    public void  DelLikeMemberList(String key)
    {
        LikeMember.remove(key);
    }
    public void ClearLikeMember()
    {
        LikeMember.clear();
    }

    public  Map<String, String> GetFriendMemberList()
    {
        return FriendMember;
    }
    public int  GetFriendMemberListCount()
    {
        return FriendMember.size();
    }
    public Set GetFriendMemberListKeySet()
    {
        return FriendMember.keySet();
    }
    public void  DelFriendMemberList(String key)
    {
        FriendMember.remove(key);
    }
    public void ClearFriendMember()
    {
        FriendMember.clear();
    }

    public  Map<String, String> GetChatMemberList()
    {
        return ChatMember;
    }
    public int  GetChatMemberListCount()
    {
        return ChatMember.size();
    }
    public Set GetChatMemberListKeySet()
    {
        return ChatMember.keySet();
    }
    public void  DelChatMemberList(String key)
    {
        ChatMember.remove(key);
    }
    public void ClearChatMember()
    {
        ChatMember.clear();
    }
}


