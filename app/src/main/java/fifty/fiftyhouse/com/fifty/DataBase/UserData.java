package fifty.fiftyhouse.com.fifty.DataBase;

import android.widget.LinearLayout;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.Arrays;

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
    private ArrayList<String> FavoriteList = new ArrayList<>();

    private  String Img_ThumbNail;
    private ArrayList<String> ImgList = new ArrayList<>();

    private  int Age;
    private  int Gender;

    private  int Visit;
    private  int Like;
    private  long Dist;

    private ArrayList<String> FriendList = new ArrayList<>();

    private  void Init() {
        UId = null;
        Index = null;
        Token = null;

        NickName = "";
        Memo = null;

        Img_ThumbNail = null;

        Age = 50;
        Gender = 0;

        Visit = 0;
        Like = 0;
        Dist = 0;
    }


    public void SetUserData(String index, String token, String nickname, String[] favorite, String thumb, String img, int age,  int gender)
    {
        Index = index;
        Token = token;
        NickName = nickname;

        for(int length = 0; length < favorite.length; length++)
            FavoriteList.add(favorite[length]);

        Img_ThumbNail = thumb;
        ImgList.add(img);

        Age = age;
        Gender = gender;
    }


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

    public void SetUserFavorite(String favorite)
    {
        FavoriteList.add(favorite);
    }
    public void SetUserFavorite(String[] favorite)
    {
        FavoriteList.addAll(Arrays.asList(favorite));
    }
    public ArrayList<String>  GetUserFavoriteList()
    {
        return FavoriteList;
    }

    public void SetUserImg(String Img)
    {
        ImgList.add(Img);
    }
    public void SetUserImg(String[] Img)
    {
        ImgList.addAll(Arrays.asList(Img));
    }
    public String  GetUserImg(int Index)
    {
        if(ImgList.size() <= Index)
        {
            return null;
        }

        return ImgList.get(Index);
    }

    public ArrayList<String>  GetUserImgList()
    {
        return ImgList;
    }
    public int  GetUserImgCount()
    {
        return ImgList.size();
    }

    public void SetUserFriend(String friendIdx)
    {
        FriendList.add(friendIdx);
    }
    public void SetUserFriend(String[] friendIdx)
    {
        FriendList.addAll(Arrays.asList(friendIdx));
    }
    public ArrayList<String>  GetUserFriendList()
    {
        return FriendList;
    }

    public void  SetUserLike(int like)
    {
        Like = like;
    }
    public int  GetUserLike()
    {
        return Like;
    }

    public void  SetUserVisit(int visit)
    {
        Visit = visit;
    }
    public int  GetUserVisit()
    {
        return Visit;
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
