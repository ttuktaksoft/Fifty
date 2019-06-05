package fifty.fiftyhouse.com.fifty.Manager;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.firestore.Transaction;
import com.google.firebase.firestore.auth.User;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import fifty.fiftyhouse.com.fifty.CommonData;
import fifty.fiftyhouse.com.fifty.CommonFunc;
import fifty.fiftyhouse.com.fifty.DataBase.UserData;
import fifty.fiftyhouse.com.fifty.MainActivity;
import fifty.fiftyhouse.com.fifty.activty.LoadingActivity;

public class FirebaseManager {

    private static final String TAG = "!!!Firebase";
    private static FirebaseManager _Instance;
    private static FirebaseAuth mAuth;
    private static FirebaseFirestore mDataBase;
    private static FirebaseStorage mStorage;
    private FirebaseUser currentUser;
    private static StorageReference mStorageRef;

    // 임시 데이터


    public static FirebaseManager getInstance() {
        if (_Instance == null)
        {
            _Instance = new FirebaseManager();
            Init();
        }
        return _Instance;
    }

    public interface CheckFirebaseComplete {
        void CompleteListener();
        void CompleteListener_Yes();
        void CompleteListener_No();
    }

    public interface CheckFirebaseComplete_Yes {
        void CompleteListener_Yes();
    }
    public interface CheckFirebaseComplete_No {
        void CompleteListener_No();
    }

    private static void Init()
    {
        GetFireBaseAuth();
        GetFireStore();
        GetFireStorage();
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

    public static FirebaseStorage GetFireStorage()
    {
        mStorage = FirebaseStorage.getInstance();
        mStorageRef = mStorage.getReference();
        return mStorage;
    }



    public FirebaseUser GetCurUser()
    {
        if(mAuth == null)
            GetFireBaseAuth();

        // Check if user is signed in (non-null) and update UI accordingly.
        currentUser = mAuth.getCurrentUser();
        return  currentUser;
    }

    public void SignInAnonymously(final Activity activity)
    {
        if(mAuth == null)
            GetFireBaseAuth();

        mAuth.signInAnonymously()
                .addOnCompleteListener(activity, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInAnonymously:success");
                            Toast.makeText(activity, "Authentication success.",
                                    Toast.LENGTH_SHORT).show();
                            FirebaseUser user = mAuth.getCurrentUser();

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInAnonymously:failure", task.getException());
                            Toast.makeText(activity, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }


    public void SetMyDataOnFireBase(final FirebaseManager.CheckFirebaseComplete listener)
    {
        if(mDataBase == null)
            GetFireStore();


        Map<String, String> tempFavoriteData = TKManager.getInstance().MyData.GetUserFavoriteList();
        Set set = tempFavoriteData.entrySet();
        Iterator iterator = set.iterator();
        while(iterator.hasNext()){
            Map.Entry entry = (Map.Entry)iterator.next();
            String key = (String)entry.getKey();
            String value = (String)entry.getValue();
            SetUserFavoriteOnFireBase(value,TKManager.getInstance().MyData.GetUserIndex(), TKManager.getInstance().MyData.GetUserNickName() );
        }

        Map<String, Object> user = new HashMap<>();

        user.put("UId", TKManager.getInstance().MyData.GetUserUId());
        user.put("Index", TKManager.getInstance().MyData.GetUserIndex());
        user.put("Token", TKManager.getInstance().MyData.GetUserToken());

        user.put("NickName", TKManager.getInstance().MyData.GetUserNickName());
        user.put("Img_ThumbNail", TKManager.getInstance().MyData.GetUserImgThumb());
        user.put("Img", TKManager.getInstance().MyData.GetUserImg());
        user.put("FavoriteList", TKManager.getInstance().MyData.GetUserFavoriteList());
        user.put("Age", TKManager.getInstance().MyData.GetUserAge());
        user.put("Gender", TKManager.getInstance().MyData.GetUserGender());

        user.put("TotalVisit", TKManager.getInstance().MyData.GetUserTotalVisit());
        user.put("TodayVisit", TKManager.getInstance().MyData.GetUserTodayVisit());

        user.put("TotalLike", TKManager.getInstance().MyData.GetUserTotalLike());
        user.put("TodayLike", TKManager.getInstance().MyData.GetUserTodayLike());

        user.put("Dist", TKManager.getInstance().MyData.GetUserDist());

        mDataBase.collection("UserData").document(TKManager.getInstance().MyData.GetUserIndex())
                .set(user, SetOptions.merge())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot successfully written!");
                        if(listener != null)
                            listener.CompleteListener();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error writing document", e);
                        if(listener != null)
                            listener.CompleteListener_No();
                    }
                });


        Map<String, Object> simpleUser = new HashMap<>();

        simpleUser.put("Index", TKManager.getInstance().MyData.GetUserIndex());
        simpleUser.put("NickName", TKManager.getInstance().MyData.GetUserNickName());
        simpleUser.put("Img_ThumbNail", TKManager.getInstance().MyData.GetUserImgThumb());
        simpleUser.put("Img", TKManager.getInstance().MyData.GetUserImg());

        mDataBase.collection("UserData_Simple").document(TKManager.getInstance().MyData.GetUserIndex())
                .set(simpleUser, SetOptions.merge())
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

        Map<String, Object> nickName = new HashMap<>();
        nickName.put(TKManager.getInstance().MyData.GetUserNickName(), TKManager.getInstance().MyData.GetUserIndex());

        mDataBase.collection("UserData_NickName").document(TKManager.getInstance().MyData.GetUserNickName())
                .set(nickName)
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

    public void SetUserDataOnFireBase(CommonData.CollentionType collectType, String documentName, String key, Object obj)
    {
        if(mDataBase == null)
            GetFireStore();

        Map<String, Object> user = new HashMap<>();
        user.put(key, obj);

        mDataBase.collection(collectType.toString()).document(documentName)
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

    public void GetUserIndex(final CheckFirebaseComplete listener)
    {
        final DocumentReference sfDocRef = mDataBase.collection("UserData_Count").document("Count");


        mDataBase.runTransaction(new Transaction.Function<Void>() {
            @Override
            public Void apply(Transaction transaction) throws FirebaseFirestoreException {
                DocumentSnapshot snapshot = transaction.get(sfDocRef);
                double newPopulation = snapshot.getDouble("count") + 1;
                transaction.update(sfDocRef, "count", newPopulation);
                TKManager.getInstance().MyData.SetUserIndex(Double.toString(newPopulation));

                // Success
                return null;
            }
        }).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d(TAG, "Transaction success!");
                if(listener != null)
                {
                    listener.CompleteListener();
                }

            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Transaction failure.", e);
                    }
                });
    }

    public void GetMyData(String userIndex)
    {
        DocumentReference docRef = mDataBase.collection("UserData").document(userIndex);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d(TAG, "DocumentSnapshot data: " + document.getData());


                            TKManager.getInstance().MyData.SetUserNickName(document.getData().get("NickName").toString());
                            Log.d(TAG, "DocumentSnapshot NickName: " + document.getData().get("NickName").toString());


                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });
    }

    public void GetUserData(String userIndex, final UserData userData, final CheckFirebaseComplete listener)
    {
        DocumentReference docRef = mDataBase.collection("UserData").document(userIndex);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d(TAG, "DocumentSnapshot data: " + document.getData());

                        if(document.getData().containsKey("Index"))
                        {
                            String Index = document.getData().get("Index").toString();
                            //TKManager.getInstance().TargetUserData.SetUserNickName(NickName);
                            userData.SetUserIndex(Index);
                        }
                        else
                            userData.SetUserIndex("1");

                        if(document.getData().containsKey("NickName"))
                        {
                            String NickName = document.getData().get("NickName").toString();
                            //TKManager.getInstance().TargetUserData.SetUserNickName(NickName);
                            userData.SetUserNickName(NickName);
                        }
                        else
                            userData.SetUserNickName(null);

                        if(document.getData().containsKey("Token"))
                        {
                            String Token = document.getData().get("Token").toString();
                            userData.SetUserToken(Token);
                        }
                        else
                            userData.SetUserToken(null);

                        if(document.getData().containsKey("Age"))
                        {
                            userData.SetUserAge(Integer.parseInt(document.getData().get("Age").toString()));
                        }
                        else
                            userData.SetUserAge(50);

                        if(document.getData().containsKey("Memo"))
                        {
                            userData.SetUserMemo(document.getData().get("Memo").toString());
                        }
                        else
                            userData.SetUserMemo(null);

                        if(document.getData().containsKey("FavoriteList"))
                        {
                            HashMap<String, String> tempFavorite = (HashMap<String, String>)document.getData().get("FavoriteList");
                            Set set = tempFavorite.entrySet();
                            Iterator iterator = set.iterator();
                            while(iterator.hasNext()){
                                Map.Entry entry = (Map.Entry)iterator.next();
                                String key = (String)entry.getKey();
                                String value = (String)entry.getValue();
                                userData.SetUserFavorite(key, value);
                            }
                        }

                        if(document.getData().containsKey("FriendList"))
                        {
                            HashMap<String, String> tempFavorite = (HashMap<String, String>)document.getData().get("FriendList");
                            Set set = tempFavorite.entrySet();
                            Iterator iterator = set.iterator();
                            while(iterator.hasNext()){
                                Map.Entry entry = (Map.Entry)iterator.next();
                                String key = (String)entry.getKey();
                                String value = (String)entry.getValue();
                                userData.SetUserFriend(key, value);
                            }
                        }

                        if(document.getData().containsKey("Gender"))
                        {
                            userData.SetUserGender(Integer.parseInt(document.getData().get("Gender").toString()));
                        }
                        else
                            userData.SetUserGender(0);

                        if(document.getData().containsKey("Img_ThumbNail"))
                        {
                            String ThumbNail = document.getData().get("Img_ThumbNail").toString();
                            userData.SetUserImgThumb(ThumbNail);
                        }
                        else
                            userData.SetUserImgThumb(null);

                        if(document.getData().containsKey("Img"))
                        {
                            HashMap<String, String> tempImg = (HashMap<String, String>)document.getData().get("Img");
                            Set set = tempImg.entrySet();
                            Iterator iterator = set.iterator();
                            while(iterator.hasNext()){
                                Map.Entry entry = (Map.Entry)iterator.next();
                                String key = (String)entry.getKey();
                                String value = (String)entry.getValue();
                                userData.SetUserImg(key, value);
                            }
                        }

                        if(document.getData().containsKey("LikeUserList"))
                        {
                            int tempTotayLikeCount = 0;

                            HashMap<String, String> tempLikeUserList= (HashMap<String, String>)document.getData().get("LikeUserList");
                            Set set = tempLikeUserList.entrySet();
                            Iterator iterator = set.iterator();
                            while(iterator.hasNext()){
                                Map.Entry entry = (Map.Entry)iterator.next();
                                String key = (String)entry.getKey();
                                HashMap<String, Object> tempLikeUser = (HashMap<String, Object>)entry.getValue();
                                userData.SetUserLikeList(tempLikeUser.get("Index").toString(), tempLikeUser.get("Date").toString());

                                if(tempLikeUser.get("Date").toString().equals(CommonFunc.getInstance().GetCurrentDate()))
                                {
                                    tempTotayLikeCount++ ;
                                    userData.SetUserTodayLike(tempTotayLikeCount);
                                }
                            }

                            userData.SetUserTotalLike(userData.GetUserLikeListCount());
                        }

                        if(document.getData().containsKey("VisitUserList"))
                        {
                            int tempTotayVisitCount = 0;
                            HashMap<String, String> tempVisitUserList= (HashMap<String, String>)document.getData().get("VisitUserList");
                            Set set = tempVisitUserList.entrySet();
                            Iterator iterator = set.iterator();
                            while(iterator.hasNext()){
                                Map.Entry entry = (Map.Entry)iterator.next();
                                String key = (String)entry.getKey();
                                HashMap<String, Object> tempVisitUser = (HashMap<String, Object>)entry.getValue();
                                userData.SetUserVisitList(tempVisitUser.get("Index").toString(), tempVisitUser.get("Date").toString());

                                if(tempVisitUser.get("Date").toString().equals(CommonFunc.getInstance().GetCurrentDate()))
                                {
                                    tempTotayVisitCount++ ;
                                    userData.SetUserTodayVisit(tempTotayVisitCount);
                                }
                                int aa = 0;
                            }

                            userData.SetUserTotalVisit(userData.GetUseVisitListCount());


                        }


                        /*
                        if(document.getData().containsKey("TotalVisit"))
                        {
                            userData.SetUserTotalVisit(Integer.parseInt(document.getData().get("TotalVisit").toString()));
                        }
                        else
                            userData.SetUserTotalVisit(0);

                        if(document.getData().containsKey("TotalLike"))
                        {
                            userData.SetUserTotalLike(Double.parseDouble(document.getData().get("TotalLike").toString()));
                        }
                        else
                            userData.SetUserTotalLike(0);
                        */


                        if(document.getData().containsKey("Dist"))
                        {
                            userData.SetUserDist(Integer.parseInt(document.getData().get("Dist").toString()));
                        }
                        else
                            userData.SetUserDist(0);

                        if(listener != null)
                            listener.CompleteListener();

                    } else {
                        Log.d(TAG, "No such document");
                        if(listener != null)
                            listener.CompleteListener_No();
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });
    }

    public void GetUserList()
    {
        GetUserListDist();
        GetUserListHot();
        GetUserListNew();
    }
    public void GetUserListDist()
    {
        CollectionReference colRef = mDataBase.collection("UserList_Dist");

        colRef.whereEqualTo("value", "1").limit(CommonData.UserList_Loding_Count).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Log.d(TAG, document.getId() + " => " + document.getData());

                        TKManager.getInstance().UserList_Dist.add(document.getId().toString());
                        GetUserData_Simple(document.getId());
                    }
                } else {
                    Log.d(TAG, "Error getting documents: ", task.getException());
                }
            }
        });
    }

    public void GetUserListHot()
    {
        CollectionReference colRef = mDataBase.collection("UserList_Hot");

        colRef.orderBy("value", Query.Direction.DESCENDING).limit(CommonData.UserList_Loding_Count).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Log.d(TAG, document.getId() + " => " + document.getData());

                        TKManager.getInstance().UserList_Hot.add(document.getId().toString());
                        GetUserData_Simple(document.getId());
                    }
                } else {
                    Log.d(TAG, "Error getting documents: ", task.getException());
                }
            }
        });
    }

    public void GetUserListNew()
    {
        CollectionReference colRef = mDataBase.collection("UserList_New");

        colRef.orderBy("value", Query.Direction.DESCENDING).limit(CommonData.UserList_Loding_Count).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Log.d(TAG, document.getId() + " => " + document.getData());

                        TKManager.getInstance().UserList_New.add(document.getId().toString());
                        GetUserData_Simple(document.getId());
                    }
                } else {
                    Log.d(TAG, "Error getting documents: ", task.getException());
                }
            }
        });
    }


    public void GetUserData_Simple(final String userIndex)
    {
        final DocumentReference docRef = mDataBase.collection("UserData_Simple").document(userIndex);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d(TAG, "DocumentSnapshot data: " + document.getData());

                        UserData tempUser = new UserData();

                        if(document.getData().containsKey("NickName"))
                        {
                            String NickName = document.getData().get("NickName").toString();
                            tempUser.SetUserNickName(NickName);
                        }
                        else
                            tempUser.SetUserNickName(null);

                        if(document.getData().containsKey("Index"))
                        {
                            String Index = document.getData().get("Index").toString();
                            tempUser.SetUserIndex(Index);
                        }
                        else
                            tempUser.SetUserIndex("1");

                        if(document.getData().containsKey("Img_ThumbNail"))
                        {
                            String tempData = document.getData().get("Img_ThumbNail").toString();
                            tempUser.SetUserImgThumb(tempData);
                        }
                        else
                            tempUser.SetUserImgThumb(null);

                        if(document.getData().containsKey("Img"))
                        {
                            HashMap<String, String> tempImg = (HashMap<String, String>)document.getData().get("Img");
                            Set set = tempImg.entrySet();
                            Iterator iterator = set.iterator();
                            while(iterator.hasNext()){
                                Map.Entry entry = (Map.Entry)iterator.next();
                                String key = (String)entry.getKey();
                                String value = (String)entry.getValue();
                                tempUser.SetUserImg(key, value);
                            }

                        }

                        TKManager.getInstance().UserData_Simple.put(userIndex, tempUser);

                        CommonFunc.getInstance().MoveActivity(CommonFunc.getInstance().mCurActivity, MainActivity.class);
                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });
    }

    public void SetUserFavoriteOnFireBase(final String favoriteName, final String index, final String nickName)
    {
        if(mDataBase == null)
            GetFireStore();

        final DocumentReference sfDocRef = mDataBase.collection("PopFavorite").document(favoriteName);
        sfDocRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d(TAG, "DocumentSnapshot data: " + document.getData());

                        mDataBase.runTransaction(new Transaction.Function<Void>() {
                            @Override
                            public Void apply(Transaction transaction) throws FirebaseFirestoreException {
                                DocumentSnapshot snapshot = transaction.get(sfDocRef);
                                double newPopulation = snapshot.getDouble("count") + 1;
                                transaction.update(sfDocRef, "count", newPopulation);

                                // Success
                                return null;
                            }
                        }).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.d(TAG, "Transaction success!");
                            }
                        })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.w(TAG, "Transaction failure.", e);
                                    }
                                });

                    } else {

                        Map<String, Object> popFavorite = new HashMap<>();
                        popFavorite.put("count", 1);

                        mDataBase.collection("PopFavorite").document(favoriteName)
                                .set(popFavorite, SetOptions.merge())
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
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });

        Map<String, Object> favorite = new HashMap<>();
        favorite.put(index, nickName);


        mDataBase.collection("FavoriteList").document(favoriteName)
                .set(favorite, SetOptions.merge())
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

    public void GetPopFavoriteData(final FirebaseManager.CheckFirebaseComplete listener)
    {
        CollectionReference colRef = mDataBase.collection("PopFavorite");
        colRef.orderBy("count", Query.Direction.DESCENDING).limit(CommonData.Favorite_Search_Pop_Count).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
        @Override
        public void onComplete(@NonNull Task<QuerySnapshot> task) {
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot document : task.getResult()) {
                    Log.d(TAG, document.getId() + " => " + document.getData());
                    TKManager.getInstance().FavoriteLIst_Pop.add(document.getId().toString());
                }

                if (listener != null)
                    listener.CompleteListener();

            } else {
                Log.d(TAG, "Error getting documents: ", task.getException());
            }
          }
    });
    }

    public void CheckNickName(final String nickName, final CheckFirebaseComplete listener)
    {
        DocumentReference docRef = mDataBase.collection("UserData_NickName").document(nickName);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                        if (listener != null)
                            listener.CompleteListener_No();

                    } else {
                        Log.d(TAG, "No such document");
                        TKManager.getInstance().MyData.SetUserNickName(nickName);
                        if (listener != null)
                            listener.CompleteListener_Yes();
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });
    }

    public void UploadThumbImg(String userIndex, Bitmap bitmap, final FirebaseManager.CheckFirebaseComplete listener)
    {
        final StorageReference tempThumbnailRef = mStorageRef.child("Image/" + TKManager.getInstance().MyData.GetUserIndex() + "/thumbnail.jpg");
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 40, baos);
        byte[] data = baos.toByteArray();

        UploadTask uploadTask = tempThumbnailRef.putBytes(data);
        Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()) {
                    throw task.getException();
                }

                // Continue with the task to get the download URL
                return tempThumbnailRef.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    Uri downloadUri = task.getResult();
                    TKManager.getInstance().MyData.SetUserImgThumb(downloadUri.toString());
                    if(listener != null)
                        listener.CompleteListener();
                } else {
                    // Handle failures
                    // ...
                }
            }
        });

    }

    public void UploadImg(String userIndex, Bitmap bitmap, final int imageIndex, final FirebaseManager.CheckFirebaseComplete listener)
    {
        final StorageReference tempThumbnailRef = mStorageRef.child("Image/" + TKManager.getInstance().MyData.GetUserIndex() + "/" + imageIndex + "/Image.jpg");
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        UploadTask uploadTask = tempThumbnailRef.putBytes(data);
        Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()) {
                    throw task.getException();
                }

                // Continue with the task to get the download URL
                return tempThumbnailRef.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    Uri downloadUri = task.getResult();
                    TKManager.getInstance().MyData.SetUserImg(Integer.toString(imageIndex), downloadUri.toString());
                    if(listener != null)
                        listener.CompleteListener();
                } else {
                    // Handle failures
                    // ...
                }
            }
        });


    }


    public void randomList(String index)
    {

        Random rnd = new Random();
        int tempIndex = rnd.nextInt(4);
        Map<String, Object> simpleUser = new HashMap<>();
        simpleUser.put("value", index);
        switch (tempIndex)
        {
            case 0:
            mDataBase.collection("UserList_Hot").document(String.valueOf(index)).set(simpleUser, SetOptions.merge())
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
            break;

            case 1:
                mDataBase.collection("UserList_Dist").document(String.valueOf(index)).set(simpleUser, SetOptions.merge())
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
                break;

            case 2:
                mDataBase.collection("UserList_New").document(String.valueOf(index)).set(simpleUser, SetOptions.merge())
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
                break;
        }

    }

    public void RegistLikeUser(final String targetIndex)
    {
       String userIndex = TKManager.getInstance().MyData.GetUserIndex();

       /*

        // 내가 좋아하는 사람들 리스트
        Map<String, Object> tempDetailTarget = new HashMap<>();
        tempDetailTarget.put("Index", targetIndex);
        tempDetailTarget.put("Date", CommonFunc.getInstance().GetCurrentDate());

        Map<String, Object> FavoriteUserData = new HashMap<>();
        FavoriteUserData.put(targetIndex, tempDetailTarget);

        Map<String, Object> FavoriteUser = new HashMap<>();
        FavoriteUser.put("FavoriteUserList", FavoriteUserData);

        mDataBase.collection("UserData").document(userIndex)
                .set(FavoriteUser, SetOptions.merge())
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
        */

        Map<String, Object> LikeUserDetailInTargetData = new HashMap<>();
        LikeUserDetailInTargetData.put("Index", userIndex);
        LikeUserDetailInTargetData.put("Date", CommonFunc.getInstance().GetCurrentDate());

        Map<String, Object> LikeUserInTargetData = new HashMap<>();
        LikeUserInTargetData.put(userIndex, LikeUserDetailInTargetData);

        Map<String, Object> LikeTarget = new HashMap<>();
        LikeTarget.put("LikeUserList", LikeUserInTargetData);

        mDataBase.collection("UserData").document(targetIndex)
                .set(LikeTarget, SetOptions.merge())
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

    public void RemoveLikeUser(final String targetIndex)
    {


        String userIndex = TKManager.getInstance().MyData.GetUserIndex();



        Map<String, Object> removeMap = new HashMap<>();
        removeMap.put("LikeUserList",  FieldValue.delete());

        final DocumentReference sfDocRef = mDataBase.collection("UserData").document(targetIndex);

        sfDocRef.update(removeMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            // [START_EXCLUDE]
            @Override
            public void onComplete(@NonNull Task<Void> task) {}
            // [START_EXCLUDE]
        });
    }

    public void AddUserLikeCount(final String userIndex, final boolean like, final CheckFirebaseComplete listener)
    {
        final DocumentReference sfDocRef = mDataBase.collection("UserData").document(userIndex);

        mDataBase.runTransaction(new Transaction.Function<Void>() {
            @Override
            public Void apply(Transaction transaction) throws FirebaseFirestoreException {
                DocumentSnapshot snapshot = transaction.get(sfDocRef);
                double newPopulation;
                if(like)
                {
                    newPopulation = snapshot.getDouble("TotalLike") + 1;
                }
                else
                    newPopulation = snapshot.getDouble("TotalLike") - 1;

                transaction.update(sfDocRef, "TotalLike", newPopulation);
                // Success
                return null;
            }
        }).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d(TAG, "Transaction success!");
                if(listener != null)
                {
                    listener.CompleteListener();
                }

            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Transaction failure.", e);
                    }
                });
    }


    public void RegistVisitUser(final String targetIndex)
    {
        String userIndex = TKManager.getInstance().MyData.GetUserIndex();

        Map<String, Object> tempDetailTarget = new HashMap<>();
        tempDetailTarget.put("Index", userIndex);
        tempDetailTarget.put("Date", CommonFunc.getInstance().GetCurrentDate());

        Map<String, Object> VisitUserData = new HashMap<>();
        VisitUserData.put(userIndex, tempDetailTarget);

        Map<String, Object> VisitUser = new HashMap<>();
        VisitUser.put("VisitUserList", VisitUserData);


        mDataBase.collection("UserData").document(targetIndex)
                .set(VisitUser, SetOptions.merge())
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
