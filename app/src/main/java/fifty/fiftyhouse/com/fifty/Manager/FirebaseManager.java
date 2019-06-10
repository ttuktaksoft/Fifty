package fifty.fiftyhouse.com.fifty.Manager;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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
import com.google.firebase.firestore.FieldPath;
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
import com.squareup.okhttp.internal.Internal;

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
import fifty.fiftyhouse.com.fifty.DialogFunc;
import fifty.fiftyhouse.com.fifty.MainActivity;
import fifty.fiftyhouse.com.fifty.activty.LoadingActivity;
import fifty.fiftyhouse.com.fifty.activty.SignUp.FavoriteDetailActivity;
import fifty.fiftyhouse.com.fifty.activty.SignUp.SignUpCompleteActivity;

import static fifty.fiftyhouse.com.fifty.CommonData.UserData_Loding_Count;

public class FirebaseManager {

    private static final String TAG = "!!!Firebase";
    private static FirebaseManager _Instance;
    private static FirebaseAuth mAuth;
    private static FirebaseFirestore mDataBase;
    private static FirebaseStorage mStorage;
    private FirebaseUser currentUser;
    private static StorageReference mStorageRef;

    private static int UserLoading = 0;

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

    public int FireBaseLoadingCount = 0;

    public void SetFireBaseLoadingCount(int count)
    {
        FireBaseLoadingCount = count;
    }
    public void AddFireBaseLoadingCount()
    {
        FireBaseLoadingCount++;
    }

    public int GetFireBaseLoadingCount()
    {
        return  FireBaseLoadingCount ;
    }

    public void Complete(final FirebaseManager.CheckFirebaseComplete listener)
    {
        UserLoading++;
        if(UserLoading == GetFireBaseLoadingCount())
        {
            UserLoading = 0;
            if(listener != null)
                listener.CompleteListener();
        }
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
        simpleUser.put("Age", TKManager.getInstance().MyData.GetUserAge());
        simpleUser.put("Gender", TKManager.getInstance().MyData.GetUserGender());

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


        Map<String, Object> Dist = new HashMap<>();
        Dist.put("value", "1");
        mDataBase.collection("UserList_Dist").document(TKManager.getInstance().MyData.GetUserIndex()).set(Dist, SetOptions.merge())
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

        Map<String, Object> New = new HashMap<>();
        final int TodayDate = Integer.parseInt(CommonFunc.getInstance().GetCurrentDate());
        New.put("value",TodayDate);
        mDataBase.collection("UserList_New").document(TKManager.getInstance().MyData.GetUserIndex()).set(Dist, SetOptions.merge())
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

    public void GetUserFavoriteList(String userIndex, final UserData userData, final CheckFirebaseComplete listener)
    {
        CollectionReference colRef = mDataBase.collection("UserData").document(userIndex).collection("FavoriteList");
        final int TodayDate = Integer.parseInt(CommonFunc.getInstance().GetCurrentDate());

        colRef.limit(2).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                int tempTotayLikeCount = 0;
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Log.d(TAG, document.getId() + " => " + document.getData());
                        userData.SetUserFavorite(document.getData().get("Name").toString(), document.getData().get("Name").toString());
                    }

                    if(listener != null)
                        listener.CompleteListener();

                } else {
                    Log.d(TAG, "Error getting documents: ", task.getException());
                }
            }
        });
    }

    public void GetUserFriendList(String userIndex, final UserData userData, final CheckFirebaseComplete listener)
    {
        CollectionReference colRef = mDataBase.collection("UserData").document(userIndex).collection("FriendUsers");
        final int TodayDate = Integer.parseInt(CommonFunc.getInstance().GetCurrentDate());

        colRef.limit(5).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Log.d(TAG, document.getId() + " => " + document.getData());
                        userData.SetUserFriend(document.getData().get("Index").toString(), document.getData().get("Index").toString());
                    }

                    if(listener != null)
                        listener.CompleteListener();

                } else {
                    Log.d(TAG, "Error getting documents: ", task.getException());
                }
            }
        });
    }

    public void GetUserLikeList(String userIndex, final UserData userData, final CheckFirebaseComplete listener)
    {
        CollectionReference colRef = mDataBase.collection("UserData").document(userIndex).collection("LikeUsers");
        final int TodayDate = Integer.parseInt(CommonFunc.getInstance().GetCurrentDate());

        colRef.whereLessThanOrEqualTo("Date", TodayDate).limit(5).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                int tempTotayLikeCount = 0;
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Log.d(TAG, document.getId() + " => " + document.getData());


                        userData.SetUserLikeList(document.getData().get("Index").toString(), document.getData().get("Date").toString());

                        if(Integer.parseInt(document.getData().get("Date").toString())== TodayDate)
                        {
                            tempTotayLikeCount++ ;
                            userData.SetUserTodayLike(tempTotayLikeCount);
                        }
                    }
                    userData.SetUserTotalLike(userData.GetUserLikeListCount());

                    if(listener != null)
                        listener.CompleteListener();

                } else {
                    Log.d(TAG, "Error getting documents: ", task.getException());
                }
            }
        });
    }



    private void GetUserVisitList(String userIndex, final UserData userData, final CheckFirebaseComplete listener) {
        CollectionReference colRef = mDataBase.collection("UserData").document(userIndex).collection("VisitUsers");
        final int TodayDate = Integer.parseInt(CommonFunc.getInstance().GetCurrentDate());

        colRef.whereLessThanOrEqualTo("Date", TodayDate).limit(5).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                int tempTotayVisitCount = 0;
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Log.d(TAG, document.getId() + " => " + document.getData());


                        userData.SetUserVisitList(document.getData().get("Index").toString(), document.getData().get("Date").toString());

                        if(Integer.parseInt(document.getData().get("Date").toString())== TodayDate)
                        {
                            tempTotayVisitCount++ ;
                            userData.SetUserTodayVisit(tempTotayVisitCount);
                        }
                    }
                    userData.SetUserTotalVisit(userData.GetUserVisitListCount());

                    if(listener != null)
                        listener.CompleteListener();

                } else {
                    Log.d(TAG, "Error getting documents: ", task.getException());
                }
            }
        });
    }


    public void GetUserData(final String userIndex, final UserData userData, final CheckFirebaseComplete listener)
    {
        SetFireBaseLoadingCount(5);

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

                        FirebaseManager.CheckFirebaseComplete FriendUserListener = new FirebaseManager.CheckFirebaseComplete() {
                            @Override
                            public void CompleteListener() {
                                Complete(listener);
                            }

                            @Override
                            public void CompleteListener_Yes() {
                            }

                            @Override
                            public void CompleteListener_No() {
                            }
                        };
                        GetUserFriendList(userIndex, userData, FriendUserListener);


                        FirebaseManager.CheckFirebaseComplete FavoriteUserListener = new FirebaseManager.CheckFirebaseComplete() {
                            @Override
                            public void CompleteListener() {
                                Complete(listener);
                            }

                            @Override
                            public void CompleteListener_Yes() {
                            }

                            @Override
                            public void CompleteListener_No() {
                            }
                        };
                        GetUserFavoriteList(userIndex, userData, FavoriteUserListener);

                        FirebaseManager.CheckFirebaseComplete LikeUserListener = new FirebaseManager.CheckFirebaseComplete() {
                            @Override
                            public void CompleteListener() {
                                Complete(listener);
                            }

                            @Override
                            public void CompleteListener_Yes() {
                            }

                            @Override
                            public void CompleteListener_No() {
                            }
                        };
                        GetUserLikeList(userIndex, userData, LikeUserListener);

                        FirebaseManager.CheckFirebaseComplete VisitUserListener = new FirebaseManager.CheckFirebaseComplete() {
                            @Override
                            public void CompleteListener() {
                                Complete(listener);
                            }

                            @Override
                            public void CompleteListener_Yes() {
                            }

                            @Override
                            public void CompleteListener_No() {
                            }
                        };
                        GetUserVisitList(userIndex, userData, VisitUserListener);

                        if(document.getData().containsKey("Dist"))
                        {
                            userData.SetUserDist(Integer.parseInt(document.getData().get("Dist").toString()));
                        }
                        else
                            userData.SetUserDist(0);

                        Complete(listener);

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


    public void GetUserList(final CheckFirebaseComplete listener)
    {
        SetFireBaseLoadingCount(0);
        GetUserListDist(listener);
        GetUserListHot(listener);
        GetUserListNew(listener);
    }
    public void GetUserListDist(final CheckFirebaseComplete listener)
    {
        CollectionReference colRef = mDataBase.collection("UserList_Dist");

        colRef.whereEqualTo("value", "1").limit(CommonData.UserList_Loding_Count).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {

                    for (QueryDocumentSnapshot document : task.getResult()) {

                        AddFireBaseLoadingCount();
                        Log.d(TAG, document.getId() + " => " + document.getData());
                        TKManager.getInstance().UserList_Dist.add(document.getId().toString());
                        GetUserData_Simple(document.getId(), TKManager.getInstance().UserData_Simple, listener);
                    }
                } else {
                    Log.d(TAG, "Error getting documents: ", task.getException());
                }
            }
        });
    }

    public void GetUserListHot(final CheckFirebaseComplete listener)
    {
        CollectionReference colRef = mDataBase.collection("UserList_Hot");

        colRef.orderBy("value", Query.Direction.DESCENDING).limit(CommonData.UserList_Loding_Count).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {

                        AddFireBaseLoadingCount();
                        Log.d(TAG, document.getId() + " => " + document.getData());
                        TKManager.getInstance().UserList_Hot.add(document.getId().toString());
                        GetUserData_Simple(document.getId(), TKManager.getInstance().UserData_Simple, listener);
                    }
                } else {
                    Log.d(TAG, "Error getting documents: ", task.getException());
                }
            }
        });
    }

    public void GetUserListNew(final CheckFirebaseComplete listener)
    {
        CollectionReference colRef = mDataBase.collection("UserList_New");

        colRef.orderBy("value", Query.Direction.DESCENDING).limit(CommonData.UserList_Loding_Count).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {

                        AddFireBaseLoadingCount();
                        Log.d(TAG, document.getId() + " => " + document.getData());
                        TKManager.getInstance().UserList_New.add(document.getId().toString());
                        GetUserData_Simple(document.getId(), TKManager.getInstance().UserData_Simple, listener);
                    }
                } else {
                    Log.d(TAG, "Error getting documents: ", task.getException());
                }
            }
        });
    }

    public void GetUserData_Simple(final String userIndex, final Map<String, UserData> getData, final FirebaseManager.CheckFirebaseComplete listener)
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

                        if(document.getData().containsKey("Age"))
                        {
                            int Age = Integer.parseInt(document.getData().get("Age").toString());
                            tempUser.SetUserAge(Age);
                        }
                        else
                            tempUser.SetUserAge(50);

                        if(document.getData().containsKey("Gender"))
                        {
                            int Gender = Integer.parseInt(document.getData().get("Gender").toString());
                            tempUser.SetUserGender(Gender);
                        }
                        else
                            tempUser.SetUserGender(0);

                        getData.put(userIndex, tempUser);

                        //AddFireBaseLoadingCount();
                        Complete(listener);

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

        RegistFavoriteListInUserData(favoriteName);

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


    public void RegistFavoriteListInUserData(String favoriteName)
    {
        Map<String, Object> favoriteData = new HashMap<>();
        favoriteData.put("Name", favoriteName);
        //favoriteData.put("Index", Integer.parseInt(CommonFunc.getInstance().GetCurrentDate()));
        String userIndex = TKManager.getInstance().MyData.GetUserIndex();


        mDataBase.collection("UserData").document(userIndex).collection("FavoriteList").document(favoriteName)
                .set(favoriteData, SetOptions.merge())
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


    public void RegistFriendInUserData(final String targetIndex)
    {
        String userIndex = TKManager.getInstance().MyData.GetUserIndex();

        Map<String, Object> FriendUserData = new HashMap<>();
        FriendUserData.put("Index", targetIndex);
        FriendUserData.put("Date", Integer.parseInt(CommonFunc.getInstance().GetCurrentDate()));

        mDataBase.collection("UserData").document(userIndex).collection("FriendUsers").document(targetIndex)
                .set(FriendUserData, SetOptions.merge())
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


    public void RegistVisitUser(final String targetIndex)
    {
        String userIndex = TKManager.getInstance().MyData.GetUserIndex();

        Map<String, Object> VisitUserData = new HashMap<>();
        VisitUserData.put("Index", userIndex);
        VisitUserData.put("Date", Integer.parseInt(CommonFunc.getInstance().GetCurrentDate()));

        Map<String, Object> VisitUser = new HashMap<>();
        VisitUser.put(userIndex, VisitUserData);

        mDataBase.collection("UserData").document(targetIndex).collection("VisitUsers").document(userIndex)
                .set(VisitUserData, SetOptions.merge())
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

    public void RemoveVisitUser(final String targetIndex)
    {

    }



    public void RegistLikeUser(final String targetIndex)
    {
       String userIndex = TKManager.getInstance().MyData.GetUserIndex();

        Map<String, Object> LikeUserData = new HashMap<>();
        LikeUserData.put("Index", userIndex);
        LikeUserData.put("Date", Integer.parseInt(CommonFunc.getInstance().GetCurrentDate()));

        mDataBase.collection("UserData").document(targetIndex).collection("LikeUsers").document(userIndex)
                .set(LikeUserData, SetOptions.merge())
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

        final DocumentReference sfDocRef = mDataBase.collection("UserData").document(targetIndex).collection("LikeUsers").document(userIndex);

        sfDocRef.delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot successfully deleted!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error deleting document", e);
                    }
                });
    }

    public void RemoveFriendUser(final String targetIndex)
    {
        String userIndex = TKManager.getInstance().MyData.GetUserIndex();

        final DocumentReference sfDocRef = mDataBase.collection("UserData").document(userIndex).collection("FriendUsers").document(targetIndex);

        sfDocRef.delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot successfully deleted!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error deleting document", e);
                    }
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

}
