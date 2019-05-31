package fifty.fiftyhouse.com.fifty.Manager;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

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
    public static UserData myData = new UserData();

}
