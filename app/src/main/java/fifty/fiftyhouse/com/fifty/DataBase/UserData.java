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
    private Map<String, String> FavoriteList = new LinkedHashMap<String, String>(){
        @Override
        protected boolean removeEldestEntry(Entry<String, String> arg0)
        {
            return size() == 3? true : false;
        }
    };

    private  String Img_ThumbNail;
    private Map<String, String> ImgList = new LinkedHashMap<>();

    private  int Age = 50;
    private  int Gender = 0;

    private  int Visit_Today = 0;
    private  int Visit_Total = 0;

    private  int Like_Today = 0;
    private  int Like_Total = 0;

    private  long Dist = 0;

    private Map<String, String> FriendList = new LinkedHashMap<>();


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

    public void SetUserFriend(String index, String friendIdx)
    {
        FriendList.put(index, friendIdx);
    }
    public String  GetUserFriendList(int index)
    {
        return FriendList.get(index);
    }
    public int  GetUserFriendListCount()
    {
        return FriendList.size();
    }

    public void  SetUserLike(int like)
    {
        Like_Total += like;
    }
    public int  GetUserTodayLike()
    {
        return Like_Today;
    }
    public int  GetUserTotalLike()
    {
        return Like_Total;
    }

    public void  SetUserVisit(int visit)
    {
        Visit_Total += visit;
    }
    public int  GetUserTodayVisit()
    {
        return Visit_Today;
    }
    public int  GetUserTotalVisit()
    {
        return Visit_Total;
    }

    public void  SetUserDist(long dist)
    {
        Dist = dist;
    }
    public long  GetUserDist()
    {
        return Dist;
    }
}
