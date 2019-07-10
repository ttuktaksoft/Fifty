package fifty.fiftyhouse.com.fifty.DataBase;

import android.widget.LinearLayout;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import fifty.fiftyhouse.com.fifty.CommonData;
import fifty.fiftyhouse.com.fifty.Manager.FirebaseManager;

public class UserData {

/*    private static UserData _Instance;
    public static UserData getInstance() {
        if (_Instance == null)
        {
            _Instance = new UserData();
            Init();
        }
        return _Instance;
    }*/

    private  String UId;
    private  String Index;
    private  String Token;

    private String NickName ;
    private String Memo;
    private  String Location;

    private boolean Vip;

    private Map<String, String> FavoriteList = new LinkedHashMap<String, String>(){
        @Override
        protected boolean removeEldestEntry(Entry<String, String> arg0)
        {
            return size() == CommonData.FavoriteSelectMaxCountCheck? true : false;
        }
    };

    private  String Img_Chat;
    private  String Img_ThumbNail;
    private Map<String, String> ImgList = new LinkedHashMap<>();

    private  int Age = 50;
    private  int Gender = 0;

    private  double Visit_Today = 0;
    private  double Visit_Total = 0;

    private  double Like_Today = 0;
    private  double Like_Total = 0;

    private  long Dist = 0;
    private  double Dist_Lat = 0;
    private  double Dist_Lon = 0;
    private  double Dist_Region = 0;
    private  String Dist_Area = null;

    private Map<String, String> FriendList = new LinkedHashMap<>();
    private Map<String, String> RequestFriendList = new LinkedHashMap<>();

    private Map<String, String> AlarmList = new LinkedHashMap<>();

    private Map<String, String> LikeList = new LinkedHashMap<>();
    private Map<String, String> VisitList = new LinkedHashMap<>();

    private Map<String, ChatData> ChatRoomDataList = new LinkedHashMap<>();

    private Map<String, ChatData> ChatRoomData = new LinkedHashMap<>();

    private Map<String, Long> ChatReadIndex = new LinkedHashMap<>();
    public ArrayList<String> UserList_Chat = new ArrayList<>();
    private String PassWord;
    private String PhoneNumber;
    private String Name;

    public Map<String, ClubData> ClubData = new LinkedHashMap<>();

  /*  public void SetUserData(String index, String token, String nickname, String[] favorite, String thumb, int age,  int gender)
    {
        Index = index;
        Token = token;
        NickName = nickname;

        for(int length = 0; length < favorite.length; length++)
            FavoriteList.put(favorite[length]);

        Img_ThumbNail = thumb;
        ImgList.put(0, thumb);

        Age = age;
        Gender = gender;
    }*/


    public void SetUserUId(String uid)
    {
        UId = uid;
    }
    public String GetUserUId()
    {
        return UId;
    }

    public void SetUserToken(String token)
    {
        Token = token;
    }
    public String GetUserToken()
    {
        return Token;
    }

    public void SetUserNickName(String nickname)
    {
        NickName = nickname;
    }
    public String GetUserNickName()
    {
        return NickName;
    }

    public void SetUserIndex(String index)
    {
        Index = index;
    }
    public String GetUserIndex()
    {
        return Index;
    }

    public void SetUserVip(Boolean vip)
    {
        Vip = vip;
    }
    public Boolean GetUserVip()
    {
        return Vip;
    }

    public void SetUserMemo(String memo)
    {
        Memo = memo;
    }
    public String GetUserMemo()
    {
        return Memo;
    }
    public void SetUserLocation(String location)
    {
        Location = location;
    }
    public String GetUserLocation()
    {
        return Location;
    }

    public void SetUserImgChat(String img)
    {
        Img_Chat = img;
    }
    public String GetUserImgChat()
    {
        return Img_Chat;
    }

    public void SetUserImgThumb(String thumb)
    {
        Img_ThumbNail = thumb;
    }
    public String GetUserImgThumb()
    {
        return Img_ThumbNail;
    }

    public void SetUserAge(int age)
    {
        Age = age;
    }
    public int GetUserAge()
    {
        return Age;
    }

    public void SetUserGender(int gender)
    {
        Gender = gender;
    }
    public  int GetUserGender()
    {
        return Gender;
    }

    public void SetUserFavorite(String key, String favorite)
    {
        FavoriteList.put(key, favorite);
    }
    public String  GetUserFavoriteList(String key)
    {
        return FavoriteList.get(key);
    }

    public  Map<String, String> GetUserFavoriteList()
    {
        return FavoriteList;
    }
    public int  GetUserFavoriteListCount()
    {
        return FavoriteList.size();
    }
    public Set GetUserFavoriteListKeySet()
    {
        return FavoriteList.keySet();
    }
    public void  DelUserFavoriteList(String key)
    {
        FavoriteList.remove(key);
    }
    public void ClearUserFavorite()
    {
        FavoriteList.clear();
    }

    public void SetUserImg(String Index, String Img)
    {
        ImgList.put(Index, Img);
    }
    public Map<String, String>  GetUserImg()
    {
        return ImgList;
    }
    public String  GetUserImg(String Index)
    {
        return ImgList.get(Index);
    }
    public int  GetUserImgCount()
    {
        return ImgList.size();
    }
    public void ClearUserImg()
    {
        ImgList.clear();
    }
/*
    public void SetUserChatList(String chatRoomIdx, String index)
    {
        ChatRoomList.put(chatRoomIdx, index);
    }
    public String  GetUserChatList(String chatRoomIdx)
    {
        return ChatRoomList.get(chatRoomIdx);
    }
    public int  GetUserChatListCount()
    {
        return ChatRoomList.size();
    }
    public Set  GetUserChatListKeySet()
    {
        return ChatRoomList.keySet();
    }
    public void  DelUserChatList(String chatRoomIdx)
    {
        ChatRoomList.remove(chatRoomIdx);
    }*/

    public void SetUserChatDataList(Map<String, ChatData> map)
    {
        ChatRoomDataList = map;
    }
    public void SetUserChatDataList(String chatRoomIdx, ChatData data)
    {
        ChatRoomDataList.put(chatRoomIdx, data);
    }
    public ChatData  GetUserChatDataList(String chatRoomIdx)
    {
        return ChatRoomDataList.get(chatRoomIdx);
    }
    public Map<String, ChatData>  GetUserChatDataList()
    {
        return ChatRoomDataList;
    }
    public int  GetUserChatDataListCount()
    {
        return ChatRoomDataList.size();
    }
    public Set  GetUserChatDataListKeySet()
    {
        return ChatRoomDataList.keySet();
    }
    public void  DelUserChatDataList(String chatRoomIdx)
    {
        ChatRoomDataList.remove(chatRoomIdx);
    }
    public void  ClearUserChatDataList()
    {
        ChatRoomDataList.clear();
    }

    public void SetUserChatReadIndexList(String chatRoomIdx, Long index)
    {
        ChatReadIndex.put(chatRoomIdx, index);
    }
    public Long  GetUserChatReadIndexList(String chatRoomIdx)
    {
      return ChatReadIndex.get(chatRoomIdx);
    }
    public int  GetUserChatReadIndexListCount()
    {
        return ChatReadIndex.size();
    }
    public Set  GetUserChatReadIndexListKeySet()
    {
        return ChatReadIndex.keySet();
    }
    public void  DelUserChatReadIndexList(String chatRoomIdx)
    {
        ChatReadIndex.remove(chatRoomIdx);
    }

    public void SetUserChatData(String chatIdx, ChatData data)
    {
        ChatRoomData.put(chatIdx, data);
    }
    public ChatData  GetUserChatData(String chatIdx)
    {
        return ChatRoomData.get(chatIdx);
    }
    public int  GetUserChatDataCount()
    {
        return ChatRoomData.size();
    }
    public Set  GetUserChatDataKeySet()
    {
        return ChatRoomData.keySet();
    }
    public void  DelUserChatData(String chatIdx)
    {
        ChatRoomData.remove(chatIdx);
    }
    public void  ClearUserChatData()
    {
        ChatRoomData.clear();
    }

    public void SetUserFriend(String index, String friendIdx)
    {
        FriendList.put(index, friendIdx);
    }
    public String  GetUserFriendList(String index)
    {
        return FriendList.get(index);
    }
    public int  GetUserFriendListCount()
    {
        return FriendList.size();
    }
    public Set  GetUserFriendListKeySet()
    {
        return FriendList.keySet();
    }
    public void  DelUserFriendList(String key)
    {
        FriendList.remove(key);
    }


    public void SetRequestFriend(String index, String friendIdx)
    {
        RequestFriendList.put(index, friendIdx);
    }
    public String  GetRequestFriendList(String index)
    {
        return RequestFriendList.get(index);
    }
    public int  GetRequestFriendListCount()
    {
        return RequestFriendList.size();
    }
    public Set  GetRequestFriendListKeySet()
    {
        return RequestFriendList.keySet();
    }
    public void  DelRequestFriendList(String key)
    {
        RequestFriendList.remove(key);
    }

    public void SetAlarm(String index, String friendIdx)
    {
        AlarmList.put(index, friendIdx);
    }
    public String  GetAlarmList(String index)
    {
        return AlarmList.get(index);
    }
    public int  GetAlarmListCount()
    {
        return AlarmList.size();
    }
    public Set  GetAlarmListKeySet()
    {
        return AlarmList.keySet();
    }
    public void  DelAlarmList(String key)
    {
        AlarmList.remove(key);
    }


    public void  SetUserTotalLike(double like){ Like_Total = like; }
    public void  AddUserTotalLike(double like){ Like_Total += like; }

    public void  SetUserTodayLike(double like)
    {
        Like_Today = like;
    }
    public void  AddUserTodayLike(double like)
    {
        Like_Today += like;
    }

    public int  GetUserTodayLike()
    {
        return (int)Like_Today;
    }
    public int  GetUserTotalLike()
    {
        return (int)Like_Total;
    }

    public void SetUserLikeList(String userIndex, String date)
    {
        LikeList.put(userIndex, date);
    }
    public String  GetUserLikeList(String index)
    {
        return LikeList.get(index);
    }
    public Map<String, String>  GetUserLikeList()
    {
        return LikeList;
    }
    public Set GetUserLikeKeySet() { return  LikeList.keySet(); }
    public int  GetUserLikeListCount()
    {
        return LikeList.size();
    }
    public void DelUserLikeList(String index)
    {
        LikeList.remove(index);
    }


    public void  SetUserTotalVisit(int visit)
    {
        Visit_Total = visit;
    }
    public void  AddUserTotalVisit(int visit)
    {
        Visit_Total += visit;
    }

    public void  SetUserTodayVisit(int visit)
    {
        Visit_Today = visit;
    }
    public void  AddUserTodayVisit(int visit)
    {
        Visit_Today += visit;
    }

    public int  GetUserTodayVisit()
    {
        return (int)Visit_Today;
    }
    public int  GetUserTotalVisit()
    {
        return (int)Visit_Total;
    }

    public void SetUserVisitList(String userIdx, String date){ VisitList.put(userIdx, date); }
    public String  GetUserVisitList(String index){ return VisitList.get(index); }
    public int  GetUserVisitListCount()
    {
        return VisitList.size();
    }
    public Map<String, String>  GetUserVisitList()
    {
        return VisitList;
    }
    public Set GetUserVisitKeySet() { return  VisitList.keySet(); }

    public void  SetUserDist(long dist)
    {
        Dist = dist;
    }
    public long  GetUserDist()
    {
        return Dist;
    }

    public void  SetUserDist_Lat(double dist)
    {
        Dist_Lat = dist;
    }
    public double  GetUserDist_Lat()
    {
        return Dist_Lat;
    }

    public void  SetUserDist_Lon(double dist)
    {
        Dist_Lon = dist;
    }
    public double  GetUserDist_Lon()
    {
        return Dist_Lon;
    }

    public void  SetUserDist_Region(double dist)
    {
        Dist_Region = dist;
    }
    public double  GetUserDist_Region()
    {
        return Dist_Region;
    }

    public void  SetUserDist_Area(String dist)
    {
        Dist_Area = dist;
    }
    public String  GetUserDist_Area()
    {
        return Dist_Area;
    }

    public void SetUserPassWord(String strPassword) {
        PassWord = strPassword;
    }
    public String GetUserPassWord() {
        return  PassWord;
    }

    public void SetUserPhone(String strPhoneNumber) {
        PhoneNumber = strPhoneNumber;
    }
    public String GetUserPhone()
    {
        return  PhoneNumber;
    }

    public void SetUserName(String name) {
        Name = name;
    }
    public String GetUserName()
    {
        return  Name;
    }

    public void SetUserClubData(String Idx, ClubData data){ ClubData.put(Idx, data); }
    public ClubData  GetUserClubData(String index){ return ClubData.get(index); }
    public int  GetUserClubDataCount()
    {
        return ClubData.size();
    }
    public Map<String, ClubData>  GetUserClubData()
    {
        return ClubData;
    }
    public Set GetUserClubDataKeySet() { return  ClubData.keySet(); }
}
