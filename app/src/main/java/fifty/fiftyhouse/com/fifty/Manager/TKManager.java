package fifty.fiftyhouse.com.fifty.Manager;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.auth.User;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import fifty.fiftyhouse.com.fifty.DataBase.UserData;

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

    public static UserData TargetUserData = new UserData();

    public Map<String, Object> UserData = new HashMap<>();

    public Map<String, UserData> UserData_Simple = new HashMap<>();

    public ArrayList<String> UserList_Dist = new ArrayList<>();
    public ArrayList<String> UserList_New= new ArrayList<>();
    public ArrayList<String> UserList_Hot = new ArrayList<>();
    public ArrayList<String> UserList_Friend = new ArrayList<>();

    public ArrayList<String> FavoriteLIst_Pop = new ArrayList<>();

    public boolean isWorkFireBase = true;


}
