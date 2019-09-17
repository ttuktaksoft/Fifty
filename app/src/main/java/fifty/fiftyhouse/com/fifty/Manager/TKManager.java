package fifty.fiftyhouse.com.fifty.Manager;

import android.app.Activity;
import android.graphics.Bitmap;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.auth.User;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import fifty.fiftyhouse.com.fifty.DataBase.ChatData;
import fifty.fiftyhouse.com.fifty.DataBase.ClubContextData;
import fifty.fiftyhouse.com.fifty.DataBase.ClubData;
import fifty.fiftyhouse.com.fifty.DataBase.FilterData;
import fifty.fiftyhouse.com.fifty.DataBase.NoticeData;
import fifty.fiftyhouse.com.fifty.DataBase.UserData;
import fifty.fiftyhouse.com.fifty.MainActivity;

public class TKManager {
    private static TKManager _Instance;
    public static TKManager getInstance() {
        if (_Instance == null)
        {
            _Instance = new TKManager();
        }
        return _Instance;
    }

    private static final String TAG = "!!!TKManager";
    public static UserData MyData = new UserData();

    public UserData TargetUserData = new UserData();
    public ClubData TargetClubData = new ClubData();

    public Map<String, ClubContextData> TargetReportContextData =  new HashMap<>();
    public Map<String, UserData> UserData_RequestJoin =  new HashMap<>();

    public Map<String, Object> UserData = new HashMap<>();

    public Map<String, UserData> UserData_Simple = new HashMap<>();
    public Map<String, ClubData> ClubData_Simple = new HashMap<>();

    public ArrayList<String> UserList_Dist = new ArrayList<>();
    public ArrayList<String> UserList_New= new ArrayList<>();
    public ArrayList<String> UserList_Hot = new ArrayList<>();
   // public Map<String, String> UserList_Hot = new HashMap<>();

    public ArrayList<String> UserList_Search_Hot = new ArrayList<>();
    public ArrayList<String> SearchList_Favorite = new ArrayList<>();

    public ArrayList<String> UserList_Friend = new ArrayList<>();

    public ArrayList<String> View_UserList_Dist = new ArrayList<>();
    public ArrayList<String> View_UserList_New= new ArrayList<>();
    public ArrayList<String> View_UserList_Hot = new ArrayList<>();

    public ArrayList<String> FavoriteLIst_Pop = new ArrayList<>();
    public ArrayList<String> DailyFavorite = new ArrayList<>();

    public Map<String, String> FavoriteLIst_ClubThumbList =  new HashMap<>();
    public ArrayList<String> FavoriteLIst_ClubList = new ArrayList<>();
    public ArrayList<String> FavoriteLIst_Club = new ArrayList<>();

    public boolean isLoadDataByBoot = true;

    //public Map<String, ClubData> SearchClubList = new LinkedHashMap<>();
    public ArrayList<String> SearchClubList = new ArrayList<>();

    public FilterData FilterData = new FilterData();
    public ClubData CreateTempClubData = new ClubData();
    public ClubContextData CreateTempClubContextData = new ClubContextData();
    public Map<String, Bitmap> TempClubContextImg = new HashMap<>();
    public ClubContextData TargetContextData = new ClubContextData();
    public ChatData copyData = new ChatData();

    public Map<String, Boolean> AppSettingData = new HashMap<>();

    public ArrayList<NoticeData> NoticeData = new ArrayList<>();

    public interface UpdateUIFunc {
        void UpdateUI();
    }

    public UpdateUIFunc mUpdateClubFragmentFunc = null;
    public UpdateUIFunc mUpdateClubActivityFunc = null;
    public UpdateUIFunc mUpdateClubFragmentkeybordDownFunc = null;
    public UpdateUIFunc mUpdateChatFragmentFunc = null;

    public ArrayList<String> MonitorChatList = new ArrayList<>();

    // 메인 추천탭에서 보여줘야 할 관심사
    public String SelectFavorite = "";
}
