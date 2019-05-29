package fifty.fiftyhouse.com.fifty.Manager;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import fifty.fiftyhouse.com.fifty.CommonData;
import fifty.fiftyhouse.com.fifty.DataBase.UserData;

public class FirebaseManager {

    private static final String TAG = "!!!Firebase";
    private static FirebaseManager _Instance;
    private static FirebaseAuth mAuth;
    private static FirebaseFirestore mDataBase;

    private FirebaseUser currentUser;


    public static FirebaseManager getInstance() {
        if (_Instance == null)
        {
            _Instance = new FirebaseManager();
            Init();
        }
        return _Instance;
    }

    private static void Init()
    {
        GetFireBaseAuth();
        GetFireStore();
    }

    public static FirebaseAuth GetFireBaseAuth()
    {
        mAuth = FirebaseAuth.getInstance();
        return mAuth;
    }

    public static FirebaseFirestore GetFireStore()
    {
        mDataBase = FirebaseFirestore.getInstance();
        return mDataBase;
    }


    public FirebaseUser GetCurUser()
    {
        if(mAuth == null)
            GetFireBaseAuth();

        // Check if user is signed in (non-null) and update UI accordingly.
        currentUser = mAuth.getCurrentUser();
        return  currentUser;
    }

    public void SignInAnonymously(final Activity mActivity)
    {
        if(mAuth == null)
            GetFireBaseAuth();

        mAuth.signInAnonymously()
                .addOnCompleteListener(mActivity, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInAnonymously:success");
                            Toast.makeText(mActivity, "Authentication success.",
                                    Toast.LENGTH_SHORT).show();
                            FirebaseUser user = mAuth.getCurrentUser();

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInAnonymously:failure", task.getException());
                            Toast.makeText(mActivity, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }


    private String UId;
    private String Index;
    private String Token;

    private String NickName;
    private String Memo;
    private ArrayList<String> FavoriteList = new ArrayList<>();

    private String Img_ThumbNail;
    private ArrayList<String> ImgList = new ArrayList<>();

    private int Age;
    private int Gender;

    private int Visit;
    private int Like;
    private long Dist;

    public void SetUserDataOnFireBase(CommonData.CollentionType CollectType, String DocumentName, Object obj)
    {
        if(mDataBase == null)
            GetFireStore();

        Map<String, Object> user = new HashMap<>();

        user.put("UId", ((UserData) obj).GetUserUId());
        user.put("Index", ((UserData) obj).GetUserIndex());
        user.put("Token", ((UserData) obj).GetUserToken());
        user.put("NickName", ((UserData) obj).GetUserNickName());
        user.put("Img_ThumbNail", ((UserData) obj).GetUserImgThumb());
        user.put("FavoriteList", ((UserData) obj).GetUserFavoriteList());
        user.put("Age", ((UserData) obj).GetUserAge());
        user.put("Gender", ((UserData) obj).GetUserGender());

        mDataBase.collection(CollectType.toString()).document(DocumentName)
                .set(user, SetOptions.merge())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot successfully written!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error writing document", e);
                    }
                });
    }


    public void SetUserDataOnFireBase(CommonData.CollentionType CollectType, String DocumentName, String Key, Object obj)
    {
        if(mDataBase == null)
            GetFireStore();

        Map<String, Object> user = new HashMap<>();
        user.put(Key, obj);

        mDataBase.collection(CollectType.toString()).document(DocumentName)
                .set(user, SetOptions.merge())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot successfully written!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error writing document", e);
                    }
                });
    }




}
