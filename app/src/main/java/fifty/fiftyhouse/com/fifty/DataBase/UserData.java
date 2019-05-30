package fifty.fiftyhouse.com.fifty.DataBase;

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

    private static String UId;
    private static String Index;
    private static String Token;

    private static String NickName;
    private static String Memo;
    private ArrayList<String> FavoriteList = new ArrayList<>();

    private static String Img_ThumbNail;
    private ArrayList<String> ImgList = new ArrayList<>();

    private static int Age;
    private static int Gender;

    private static int Visit;
    private static int Like;
    private static long Dist;

    private static void Init() {
        UId = null;
        Index = null;
        Token = null;

        NickName = null;
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
    public ArrayList<String>  GetUserImgListList()
    {
        return ImgList;
    }
}
