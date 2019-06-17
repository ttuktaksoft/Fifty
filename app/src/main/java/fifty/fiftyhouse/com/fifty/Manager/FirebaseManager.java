package fifty.fiftyhouse.com.fifty.Manager;

import android.app.Activity;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
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
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.firestore.Transaction;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import javax.annotation.Nullable;

import fifty.fiftyhouse.com.fifty.CommonData;
import fifty.fiftyhouse.com.fifty.CommonFunc;
import fifty.fiftyhouse.com.fifty.DataBase.ChatData;
import fifty.fiftyhouse.com.fifty.DataBase.UserData;
import fifty.fiftyhouse.com.fifty.MainActivity;

import static com.google.firebase.firestore.DocumentChange.Type.ADDED;
import static com.google.firebase.firestore.DocumentChange.Type.MODIFIED;
import static com.google.firebase.firestore.DocumentChange.Type.REMOVED;
import static fifty.fiftyhouse.com.fifty.CommonData.REFERENCE_DAY;
import static fifty.fiftyhouse.com.fifty.MainActivity.mFragmentMng;

public class FirebaseManager {

    private static final String TAG = "!!!Firebase";
    private static FirebaseManager _Instance;
    private static FirebaseAuth mAuth;
    private static FirebaseFirestore mDataBase;
    private static FirebaseStorage mStorage;
    private FirebaseUser currentUser;
    private static StorageReference mStorageRef;

    private static int UserLoading = 0;

    private ListenerRegistration ChatDataMonitor_registration;


    // 임시 데이터


    public static FirebaseManager getInstance() {
        if (_Instance == null) {
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

    public void SetFireBaseLoadingCount(int count) {
        FireBaseLoadingCount = count;
    }

    public void AddFireBaseLoadingCount() {
        FireBaseLoadingCount++;
    }

    public int GetFireBaseLoadingCount() {
        return FireBaseLoadingCount;
    }

    public void CompleteFireBaseLoadingCount(final FirebaseManager.CheckFirebaseComplete listener) {
        UserLoading++;
        if (UserLoading == GetFireBaseLoadingCount()) {
            UserLoading = 0;
            if (listener != null)
                listener.CompleteListener();
        }
    }

    public void Complete(final FirebaseManager.CheckFirebaseComplete listener) {
        UserLoading++;
        if (UserLoading == GetFireBaseLoadingCount()) {
            UserLoading = 0;
            if (listener != null)
                listener.CompleteListener();
        }
    }

    private static void Init() {
        GetFireBaseAuth();
        GetFireStore();
        GetFireStorage();
    }

    public static FirebaseAuth GetFireBaseAuth() {
        mAuth = FirebaseAuth.getInstance();
        return mAuth;
    }

    public static FirebaseFirestore GetFireStore() {
        mDataBase = FirebaseFirestore.getInstance();
        return mDataBase;
    }

    public static FirebaseStorage GetFireStorage() {
        mStorage = FirebaseStorage.getInstance();
        mStorageRef = mStorage.getReference();
        return mStorage;
    }


    public FirebaseUser GetCurUser() {
        if (mAuth == null)
            GetFireBaseAuth();

        // Check if user is signed in (non-null) and update UI accordingly.
        currentUser = mAuth.getCurrentUser();
        return currentUser;
    }

    public void SignInAnonymously(final Activity activity) {
        if (mAuth == null)
            GetFireBaseAuth();

        mAuth.signInAnonymously()
                .addOnCompleteListener(activity, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInAnonymously:success");
                 /*           Toast.makeText(activity, "Authentication success.",
                                    Toast.LENGTH_SHORT).show();*/
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


    public void SetMyDataOnFireBase(final FirebaseManager.CheckFirebaseComplete listener) {
        if (mDataBase == null)
            GetFireStore();


        FirebaseManager.getInstance().RegistUserDistInfo();

        Map<String, String> tempFavoriteData = TKManager.getInstance().MyData.GetUserFavoriteList();
        Set set = tempFavoriteData.entrySet();
        Iterator iterator = set.iterator();
        while (iterator.hasNext()) {
            Map.Entry entry = (Map.Entry) iterator.next();
            String key = (String) entry.getKey();
            String value = (String) entry.getValue();
            SetUserFavoriteOnFireBase(value, false);
        }

        Map<String, Object> user = new HashMap<>();

        user.put("UId", TKManager.getInstance().MyData.GetUserIndex());
        user.put("Index", TKManager.getInstance().MyData.GetUserIndex());

        user.put("Token", TKManager.getInstance().MyData.GetUserIndex());

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

        user.put("Dist_Lon", TKManager.getInstance().MyData.GetUserDist_Lon());
        user.put("Dist_Lat", TKManager.getInstance().MyData.GetUserDist_Lat());

        user.put("Dist_Region", TKManager.getInstance().MyData.GetUserDist_Region());
        user.put("Dist_Area", TKManager.getInstance().MyData.GetUserDist_Area());

        user.put("Location", "");

        mDataBase.collection("UserData").document(TKManager.getInstance().MyData.GetUserIndex())
                .set(user, SetOptions.merge())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot successfully written!");
                        if (listener != null)
                            listener.CompleteListener();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error writing document", e);
                        if (listener != null)
                            listener.CompleteListener_No();
                    }
                });


        Map<String, Object> simpleUser = new HashMap<>();

        simpleUser.put("Index", TKManager.getInstance().MyData.GetUserIndex());
        simpleUser.put("NickName", TKManager.getInstance().MyData.GetUserNickName());
        simpleUser.put("Img_ThumbNail", TKManager.getInstance().MyData.GetUserImgThumb());
        simpleUser.put("Img", TKManager.getInstance().MyData.GetUserImg());
        simpleUser.put("Age", TKManager.getInstance().MyData.GetUserAge());
        simpleUser.put("Dist_Lon", TKManager.getInstance().MyData.GetUserDist_Lon());
        simpleUser.put("Dist_Lat", TKManager.getInstance().MyData.GetUserDist_Lat());
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
        Dist.put("value", TKManager.getInstance().MyData.GetUserDist_Region());
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
        New.put("value", TodayDate);
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

    public void SetUserDataOnFireBase(CommonData.CollentionType collectType, String documentName, String key, Object obj) {
        if (mDataBase == null)
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

    public void GetUserIndex(final CheckFirebaseComplete listener) {
        final DocumentReference sfDocRef = mDataBase.collection("UserData_Count").document("Count");


        mDataBase.runTransaction(new Transaction.Function<Void>() {
            @Override
            public Void apply(Transaction transaction) throws FirebaseFirestoreException {
                DocumentSnapshot snapshot = transaction.get(sfDocRef);
                double newPopulation = snapshot.getDouble("count") + 1;
                transaction.update(sfDocRef, "count", newPopulation);
                int tempIndex = (int)newPopulation;
                TKManager.getInstance().MyData.SetUserIndex(Integer.toString(tempIndex));

                // Success
                return null;
            }
        }).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d(TAG, "Transaction success!");
                if (listener != null) {
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

    public void GetMyData(String userIndex) {
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

    public void GetUserFavoriteList(String userIndex, final UserData userData, final CheckFirebaseComplete listener) {
        CollectionReference colRef = mDataBase.collection("UserData").document(userIndex).collection("FavoriteList");
        final int TodayDate = Integer.parseInt(CommonFunc.getInstance().GetCurrentDate());

        colRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                int tempTotayLikeCount = 0;
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Log.d(TAG, document.getId() + " => " + document.getData());
                        userData.SetUserFavorite(document.getData().get("Name").toString(), document.getData().get("Name").toString());
                    }

                    if (listener != null)
                        listener.CompleteListener();

                } else {
                    Log.d(TAG, "Error getting documents: ", task.getException());
                }
            }
        });
    }

    public void RemoveMonitorUserChatData()
    {
        ChatDataMonitor_registration.remove();
    }

    public void MonitorUserChatData(final String chatRoomIndex, final UserData userData, final CheckFirebaseComplete listener) {

        //TKManager.getInstance().MyData.ClearUserChatData();
        CollectionReference colRef = mDataBase.collection("ChatRoomData").document(chatRoomIndex).collection(chatRoomIndex);
        final int TodayDate = Integer.parseInt(CommonFunc.getInstance().GetCurrentDate());

        Query query = mDataBase.collection("ChatRoomData").document(chatRoomIndex).collection(chatRoomIndex).orderBy("MsgIndex", Query.Direction.DESCENDING).limit(1);

        ChatDataMonitor_registration = query.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.w(TAG, "Listen failed.", e);
                    return;
                }

                for (DocumentChange document : queryDocumentSnapshots.getDocumentChanges()) {
                    switch (document.getType()) {
                        case ADDED:
                        case MODIFIED:
                            String tempRoomName = document.getDocument().getId().toString();

                            if(!document.getDocument().getId().equals("Index"))
                            {
                                ChatData tempData = new ChatData();
                                tempData.SetRoomIndex(document.getDocument().getData().get("RoomIndex").toString());

                                tempData.SetFromIndex(document.getDocument().getData().get("FromIndex").toString());
                                tempData.SetFromNickName(document.getDocument().getData().get("FromNickName").toString());
                                tempData.SetFromThumbNail(document.getDocument().getData().get("FromThumbNail").toString());

                                tempData.SetToIndex(document.getDocument().getData().get("ToIndex").toString());
                                tempData.SetToNickName(document.getDocument().getData().get("ToNickName").toString());
                                tempData.SetToThumbNail(document.getDocument().getData().get("ToThumbNail").toString());

                                tempData.SetMsg(document.getDocument().getData().get("Msg").toString());
                                if(tempData.GetToIndex().equals(TKManager.getInstance().MyData.GetUserIndex()))
                                {
                                    tempData.SetMsgReadCheck(true);

                                    Map<String, Object> ReadCheck = new HashMap<>();
                                    ReadCheck.put("MsgReadCheck", true);

                                    mDataBase.collection("ChatRoomData").document(chatRoomIndex).collection(chatRoomIndex).document(document.getDocument().getId())
                                            .set(ReadCheck, SetOptions.merge())
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
                                else
                                {
                                    tempData.SetMsgReadCheck(Boolean.valueOf((document.getDocument().getData().get("MsgReadCheck").toString())).booleanValue());
                                }

                                tempData.SetMsgIndex(Long.parseLong(document.getDocument().getData().get("MsgIndex").toString()));
                                tempData.SetMsgSender(document.getDocument().getData().get("MsgSender").toString());
                                tempData.SetMsgDate(Long.parseLong(document.getDocument().getData().get("MsgDate").toString()));

                                String tempType = document.getDocument().getData().get("MsgType").toString();

                                switch (tempType)
                                {
                                    case "MSG":
                                        tempData.SetMsgType(CommonData.MSGType.MSG);
                                        break;
                                    case "IMG":
                                        tempData.SetMsgType(CommonData.MSGType.IMG);
                                        break;
                                    case "VIDEO":
                                        tempData.SetMsgType(CommonData.MSGType.VIDEO);
                                        break;
                                }
                                userData.SetUserChatData(Long.toString(tempData.GetMsgIndex()), tempData);
                                userData.SetUserChatReadIndexList(tempData.GetRoomIndex(), tempData.GetMsgIndex());

                                mDataBase.collection("UserData").document(TKManager.getInstance().MyData.GetUserIndex()).collection("ChatRoomList").document(tempData.GetRoomIndex())
                                        .set(tempData, SetOptions.merge())
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


                            break;
                        case REMOVED:
                            tempRoomName = document.getDocument().getId().toString();
                            userData.DelUserChatDataList(tempRoomName);
                            break;
                    }
                }
                if (listener != null)
                    listener.CompleteListener();
            }
        });
    }

    public void GetUserChatData(final String chatRoomIndex, final UserData userData, final CheckFirebaseComplete listener) {

        TKManager.getInstance().MyData.ClearUserChatData();
        CollectionReference colRef = mDataBase.collection("ChatRoomData").document(chatRoomIndex).collection(chatRoomIndex);
        final int TodayDate = Integer.parseInt(CommonFunc.getInstance().GetCurrentDate());

            colRef.orderBy("MsgIndex", Query.Direction.ASCENDING).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Log.d(TAG, document.getId() + " => " + document.getData());
                            if(!document.getId().equals("Index"))
                            {
                                ChatData tempData = new ChatData();
                                tempData.SetRoomIndex(document.getData().get("RoomIndex").toString());

                                tempData.SetFromIndex(document.getData().get("FromIndex").toString());
                                tempData.SetFromNickName(document.getData().get("FromNickName").toString());
                                tempData.SetFromThumbNail(document.getData().get("FromThumbNail").toString());

                                tempData.SetToIndex(document.getData().get("ToIndex").toString());
                                tempData.SetToNickName(document.getData().get("ToNickName").toString());
                                tempData.SetToThumbNail(document.getData().get("ToThumbNail").toString());

                                tempData.SetMsg(document.getData().get("Msg").toString());
                                if(tempData.GetToIndex().equals(TKManager.getInstance().MyData.GetUserIndex()))
                                {
                                    tempData.SetMsgReadCheck(true);

                                    Map<String, Object> ReadCheck = new HashMap<>();
                                    ReadCheck.put("MsgReadCheck", true);

                                    mDataBase.collection("ChatRoomData").document(chatRoomIndex).collection(chatRoomIndex).document(document.getId())
                                            .set(ReadCheck, SetOptions.merge())
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
                                else
                                {
                                    tempData.SetMsgReadCheck(Boolean.valueOf((document.getData().get("MsgReadCheck").toString())).booleanValue());
                                }

                                tempData.SetMsgIndex(Long.parseLong(document.getData().get("MsgIndex").toString()));
                                tempData.SetMsgSender(document.getData().get("MsgSender").toString());
                                tempData.SetMsgDate(Long.parseLong(document.getData().get("MsgDate").toString()));

                                String tempType = document.getData().get("MsgType").toString();

                                switch (tempType)
                                {
                                    case "MSG":
                                        tempData.SetMsgType(CommonData.MSGType.MSG);
                                        break;
                                    case "IMG":
                                        tempData.SetMsgType(CommonData.MSGType.IMG);
                                        break;
                                    case "VIDEO":
                                        tempData.SetMsgType(CommonData.MSGType.VIDEO);
                                        break;
                                }
                                userData.SetUserChatData(Long.toString(tempData.GetMsgIndex()), tempData);
                                userData.SetUserChatReadIndexList(tempData.GetRoomIndex(), tempData.GetMsgIndex());

                 /*               mDataBase.collection("UserData").document(TKManager.getInstance().MyData.GetUserIndex()).collection("ChatRoomList").document(tempData.GetRoomIndex())
                                        .set(tempData, SetOptions.merge())
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
                                        });*/
                            }
                        }

                        if (listener != null)
                            listener.CompleteListener();

                    } else {
                        Log.d(TAG, "Error getting documents: ", task.getException());
                    }
                }
            });
    }

    public void MonitorChatData(final String chatRoomIndex, final UserData userData, final CheckFirebaseComplete listener) {

        CollectionReference colRef = mDataBase.collection("ChatRoomData").document(chatRoomIndex).collection(chatRoomIndex);
        final int TodayDate = Integer.parseInt(CommonFunc.getInstance().GetCurrentDate());
        {
            colRef.orderBy("MsgIndex", Query.Direction.DESCENDING).limit(1).addSnapshotListener(new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                    if (e != null) {
                        Log.w(TAG, "Listen failed.", e);
                        return;
                    }

                    for (DocumentChange document : queryDocumentSnapshots.getDocumentChanges()) {
                        switch (document.getType()) {
                            case ADDED:
                            case MODIFIED:
                                String tempRoomName = document.getDocument().getId().toString();

                                if(!document.getDocument().getId().equals("Index"))
                                {
                                    ChatData tempData = new ChatData();
                                    tempData.SetRoomIndex(document.getDocument().getData().get("RoomIndex").toString());

                                    tempData.SetFromIndex(document.getDocument().getData().get("FromIndex").toString());
                                    tempData.SetFromNickName(document.getDocument().getData().get("FromNickName").toString());
                                    tempData.SetFromThumbNail(document.getDocument().getData().get("FromThumbNail").toString());

                                    tempData.SetToIndex(document.getDocument().getData().get("ToIndex").toString());
                                    tempData.SetToNickName(document.getDocument().getData().get("ToNickName").toString());
                                    tempData.SetToThumbNail(document.getDocument().getData().get("ToThumbNail").toString());

                                    tempData.SetMsg(document.getDocument().getData().get("Msg").toString());
                                    tempData.SetMsgIndex(Long.parseLong(document.getDocument().getData().get("MsgIndex").toString()));
                                    tempData.SetMsgReadCheck(Boolean.valueOf((document.getDocument().getData().get("MsgReadCheck").toString())).booleanValue());
                                    tempData.SetMsgSender(document.getDocument().getData().get("MsgSender").toString());
                                    tempData.SetMsgDate(Long.parseLong(document.getDocument().getData().get("MsgDate").toString()));

                                    String tempType = document.getDocument().getData().get("MsgType").toString();

                                    switch (tempType)
                                    {
                                        case "MSG":
                                            tempData.SetMsgType(CommonData.MSGType.MSG);
                                            break;
                                        case "IMG":
                                            tempData.SetMsgType(CommonData.MSGType.IMG);
                                            break;
                                        case "VIDEO":
                                            tempData.SetMsgType(CommonData.MSGType.VIDEO);
                                            break;
                                    }

                                    userData.SetUserChatDataList(tempData.GetRoomIndex(), tempData);
                                }


                                break;
                            case REMOVED:
                                tempRoomName = document.getDocument().getId().toString();
                                userData.DelUserChatDataList(tempRoomName);
                                break;
                        }
                    }
                    if (listener != null)
                        listener.CompleteListener();
                }
            });


        }

    }

    public void GetUserChatList(String userIndex, final UserData userData, final CheckFirebaseComplete listener) {
        CollectionReference colRef = mDataBase.collection("UserData").document(userIndex).collection("ChatRoomList");
        final int TodayDate = Integer.parseInt(CommonFunc.getInstance().GetCurrentDate());

        colRef.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.w(TAG, "Listen failed.", e);
                    return;
                }

                for (DocumentChange document : queryDocumentSnapshots.getDocumentChanges()) {
                    switch (document.getType()) {
                        case ADDED:
                        case MODIFIED:
                            String tempRoomName = document.getDocument().getId().toString();

                            ChatData tempData = new ChatData();
                            tempData.SetRoomIndex(document.getDocument().getData().get("RoomIndex").toString());

                            tempData.SetFromIndex(document.getDocument().getData().get("FromIndex").toString());
                            tempData.SetFromNickName(document.getDocument().getData().get("FromNickName").toString());
                            tempData.SetFromThumbNail(document.getDocument().getData().get("FromThumbNail").toString());

                            tempData.SetToIndex(document.getDocument().getData().get("ToIndex").toString());
                            tempData.SetToNickName(document.getDocument().getData().get("ToNickName").toString());
                            tempData.SetToThumbNail(document.getDocument().getData().get("ToThumbNail").toString());

                            tempData.SetMsg(document.getDocument().getData().get("Msg").toString());
                            tempData.SetMsgReadCheck(Boolean.valueOf((document.getDocument().getData().get("MsgReadCheck").toString())).booleanValue());
                            tempData.SetMsgIndex(Long.parseLong(document.getDocument().getData().get("MsgIndex").toString()));
                            tempData.SetMsgSender(document.getDocument().getData().get("MsgSender").toString());
                            tempData.SetMsgDate(Long.parseLong(document.getDocument().getData().get("MsgDate").toString()));

                            String tempType = document.getDocument().getData().get("MsgType").toString();

                            switch (tempType)
                            {
                                case "MSG":
                                    tempData.SetMsgType(CommonData.MSGType.MSG);
                                    break;
                                case "IMG":
                                    tempData.SetMsgType(CommonData.MSGType.IMG);
                                    break;
                                case "VIDEO":
                                    tempData.SetMsgType(CommonData.MSGType.VIDEO);
                                    break;
                            }

                            userData.SetUserChatReadIndexList(tempRoomName, tempData.GetMsgIndex());

                            //userData.SetUserChatList(tempRoomName, tempRoomName);

                            break;
                        case REMOVED:
                            tempRoomName = document.getDocument().getId().toString();
                            userData.DelUserChatDataList(tempRoomName);
                            break;
                    }
                }


                if (listener != null)
                    listener.CompleteListener();

            }
        });
    }

    public void GetUserFriendList(String userIndex, final UserData userData, final CheckFirebaseComplete listener) {
        CollectionReference colRef = mDataBase.collection("UserData").document(userIndex).collection("FriendUsers");
        final int TodayDate = Integer.parseInt(CommonFunc.getInstance().GetCurrentDate());

        colRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Log.d(TAG, document.getId() + " => " + document.getData());
                        userData.SetUserFriend(document.getData().get("Index").toString(), document.getData().get("Index").toString());
                    }

                    if (listener != null)
                        listener.CompleteListener();

                } else {
                    Log.d(TAG, "Error getting documents: ", task.getException());
                }
            }
        });
    }

    public void GetUserLikeList(String userIndex, final UserData userData, final CheckFirebaseComplete listener) {
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

                        if (Integer.parseInt(document.getData().get("Date").toString()) == TodayDate) {
                            tempTotayLikeCount++;
                            userData.SetUserTodayLike(tempTotayLikeCount);
                        }
                    }
                    userData.SetUserTotalLike(userData.GetUserLikeListCount());

                    if (listener != null)
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

                        if (Integer.parseInt(document.getData().get("Date").toString()) == TodayDate) {
                            tempTotayVisitCount++;
                            userData.SetUserTodayVisit(tempTotayVisitCount);
                        }
                    }
                    userData.SetUserTotalVisit(userData.GetUserVisitListCount());

                    if (listener != null)
                        listener.CompleteListener();

                } else {
                    Log.d(TAG, "Error getting documents: ", task.getException());
                }
            }
        });
    }


    public void GetUserData(final String userIndex, final UserData userData, final CheckFirebaseComplete listener) {

        if(userIndex.equals(TKManager.getInstance().MyData.GetUserIndex()))
            SetFireBaseLoadingCount(5);
        else
            SetFireBaseLoadingCount(4);

        final DocumentReference docRef = mDataBase.collection("UserData").document(userIndex);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {

                        Log.d(TAG, "DocumentSnapshot data: " + document.getData());

                        if (document.getData().containsKey("Index")) {
                            String Index = document.getData().get("Index").toString();
                            //TKManager.getInstance().TargetUserData.SetUserNickName(NickName);
                            userData.SetUserIndex(Index);
                        } else
                            userData.SetUserIndex("1");

                        if (document.getData().containsKey("NickName")) {
                            String NickName = document.getData().get("NickName").toString();
                            //TKManager.getInstance().TargetUserData.SetUserNickName(NickName);
                            userData.SetUserNickName(NickName);
                        } else
                            userData.SetUserNickName(null);

                        if (document.getData().containsKey("Token")) {
                            String Token = document.getData().get("Token").toString();
                            userData.SetUserToken(Token);
                        } else
                            userData.SetUserToken(null);

                        if (document.getData().containsKey("Age")) {
                            userData.SetUserAge(Integer.parseInt(document.getData().get("Age").toString()));
                        } else
                            userData.SetUserAge(50);

                        if (document.getData().containsKey("Memo")) {
                            userData.SetUserMemo(document.getData().get("Memo").toString());
                        } else
                            userData.SetUserMemo(null);

                        if (document.getData().containsKey("Gender")) {
                            userData.SetUserGender(Integer.parseInt(document.getData().get("Gender").toString()));
                        } else
                            userData.SetUserGender(0);

                        if (document.getData().containsKey("Img_ThumbNail")) {
                            String ThumbNail = document.getData().get("Img_ThumbNail").toString();
                            userData.SetUserImgThumb(ThumbNail);
                        } else
                            userData.SetUserImgThumb(null);

                        if (document.getData().containsKey("Img")) {
                            userData.ClearUserImg();
                            HashMap<String, String> tempImg = (HashMap<String, String>) document.getData().get("Img");
                            Set set = tempImg.entrySet();
                            Iterator iterator = set.iterator();
                            while (iterator.hasNext()) {
                                Map.Entry entry = (Map.Entry) iterator.next();
                                String key = (String) entry.getKey();
                                String value = (String) entry.getValue();
                                userData.SetUserImg(key, value);
                            }
                        }

                        FirebaseManager.CheckFirebaseComplete ChatRoomListener = new FirebaseManager.CheckFirebaseComplete() {
                            @Override
                            public void CompleteListener() {
                                Complete(listener);
                            /*    FirebaseManager.CheckFirebaseComplete ChatListener = new FirebaseManager.CheckFirebaseComplete() {
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

                                Set KeySet = TKManager.getInstance().MyData.GetUserChatListKeySet();
                                Iterator iterator = KeySet.iterator();

                                while(iterator.hasNext()){
                                    String key = (String)iterator.next();
                                    MonitorChatData(key, userData, ChatListener);
                                }*/
                            }

                            @Override
                            public void CompleteListener_Yes() {
                            }

                            @Override
                            public void CompleteListener_No() {
                            }
                        };

                        if(userIndex.equals(TKManager.getInstance().MyData.GetUserIndex()))
                            GetUserChatList(userIndex, userData, ChatRoomListener);


                     /*   FirebaseManager.CheckFirebaseComplete FriendUserListener = new FirebaseManager.CheckFirebaseComplete() {
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
                        GetUserFriendList(userIndex, userData, FriendUserListener);*/

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



                        if (document.getData().containsKey("Dist")) {
                            userData.SetUserDist(Integer.parseInt(document.getData().get("Dist").toString()));
                        } else
                            userData.SetUserDist(0);

                        if (document.getData().containsKey("Dist_Lon")) {
                            userData.SetUserDist_Lon(Double.parseDouble(document.getData().get("Dist_Lon").toString()));
                        } else
                            userData.SetUserDist_Lon(126.978425);

                        if (document.getData().containsKey("Dist_Lat")) {
                            userData.SetUserDist_Lat(Double.parseDouble(document.getData().get("Dist_Lat").toString()));
                        } else
                            userData.SetUserDist_Lat(37.566659);

                        if (document.getData().containsKey("Dist_Region")) {
                            userData.SetUserDist_Region(Double.parseDouble(document.getData().get("Dist_Region").toString()));
                        } else
                            userData.SetUserDist_Region(0);

                        if (document.getData().containsKey("Dist_Area")) {
                            userData.SetUserDist_Area(document.getData().get("Dist_Area").toString());
                        } else
                            userData.SetUserDist_Area("대한민국");

                        double Distance = CommonFunc.getInstance().DistanceByDegree(TKManager.getInstance().MyData.GetUserDist_Lat(), TKManager.MyData.GetUserDist_Lon(), userData.GetUserDist_Lat(), userData.GetUserDist_Lon());
                        userData.SetUserDist((long)Distance);

                        if (document.getData().containsKey("Location")) {
                            String Location = document.getData().get("Location").toString();
                            userData.SetUserLocation(Location);
                        } else
                            userData.SetUserLocation("");


                        Complete(listener);

                    } else {
                        Log.d(TAG, "No such document");
                        if (listener != null)
                            listener.CompleteListener_No();
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });
    }


    public void GetUserList(final CheckFirebaseComplete listener) {
        SetFireBaseLoadingCount(0);
        GetUserListDist(listener);
        GetUserListHot(listener);
        GetUserListNew(listener);
        GetUserListFriend(listener);
    }

    public void GetUserListDist(final CheckFirebaseComplete listener) {
        CollectionReference colRef = mDataBase.collection("UserList_Dist");

        colRef.whereEqualTo("value", TKManager.getInstance().MyData.GetUserDist_Region()).limit(CommonData.UserList_Loding_Count).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {

                    for (QueryDocumentSnapshot document : task.getResult()) {

                        if(!document.getId().equals(TKManager.getInstance().MyData.GetUserIndex()))
                        {
                            TKManager.getInstance().UserList_Dist.add(document.getId().toString());

                            if(TKManager.getInstance().UserData_Simple.get(document.getId().toString()) == null)
                            {
                                AddFireBaseLoadingCount();
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                GetUserData_Simple(document.getId(), TKManager.getInstance().UserData_Simple, listener);
                            }
                        }

                    }
                } else {
                    Log.d(TAG, "Error getting documents: ", task.getException());
                }
            }
        });
    }

    public void GetUserListHot(final CheckFirebaseComplete listener) {
        //CollectionReference colRef = mDataBase.collection("UserList_Hot");
        CollectionReference colRef = mDataBase.collection("FavoriteList").document(REFERENCE_DAY[4]).collection("UserIndex");
        colRef.orderBy("Index", Query.Direction.DESCENDING).limit(CommonData.UserList_Loding_Count).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {

                        if(!document.getId().equals(TKManager.getInstance().MyData.GetUserIndex()))
                        {
                            TKManager.getInstance().UserList_Hot.add(document.getId().toString());

                            if(TKManager.getInstance().UserData_Simple.get(document.getId().toString()) == null)
                            {
                                AddFireBaseLoadingCount();
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                GetUserData_Simple(document.getId(), TKManager.getInstance().UserData_Simple, listener);
                            }
                        }
                    }
                } else {
                    Log.d(TAG, "Error getting documents: ", task.getException());
                }
            }
        });
    }

    public void GetUserListNew(final CheckFirebaseComplete listener) {
        CollectionReference colRef = mDataBase.collection("UserList_New");

        colRef.orderBy("value", Query.Direction.DESCENDING).limit(CommonData.UserList_Loding_Count).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {

                        if(!document.getId().equals(TKManager.getInstance().MyData.GetUserIndex()))
                        {
                            TKManager.getInstance().UserList_New.add(document.getId().toString());

                            if(TKManager.getInstance().UserData_Simple.get(document.getId().toString()) == null)
                            {
                                AddFireBaseLoadingCount();
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                GetUserData_Simple(document.getId(), TKManager.getInstance().UserData_Simple, listener);
                            }
                        }
                    }
                } else {
                    Log.d(TAG, "Error getting documents: ", task.getException());
                }
            }
        });
    }

    public void GetUserListFriend(final CheckFirebaseComplete listener) {
        //CollectionReference colRef = mDataBase.collection("UserList_Friend");
        CollectionReference colRef = mDataBase.collection("UserData").document(TKManager.getInstance().MyData.GetUserIndex()).collection("FriendUsers");
        colRef.orderBy("Date", Query.Direction.DESCENDING).limit(CommonData.UserList_Loding_Count).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {

                        if(!document.getId().equals(TKManager.getInstance().MyData.GetUserIndex()))
                        {
                            TKManager.getInstance().MyData.SetUserFriend(document.getId().toString(), document.getId().toString());
                            if(TKManager.getInstance().UserData_Simple.get(document.getId().toString()) == null)
                            {
                                AddFireBaseLoadingCount();
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                GetUserData_Simple(document.getId(), TKManager.getInstance().UserData_Simple, listener);
                            }
                        }


                    }
                } else {
                    Log.d(TAG, "Error getting documents: ", task.getException());
                }
            }
        });
    }

    public void GetUserData_Simple(final String userIndex, final Map<String, UserData> getData, final FirebaseManager.CheckFirebaseComplete listener) {
        final DocumentReference docRef = mDataBase.collection("UserData_Simple").document(userIndex);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d(TAG, "DocumentSnapshot data: " + document.getData());

                        UserData tempUser = new UserData();

                        if (document.getData().containsKey("NickName")) {
                            String NickName = document.getData().get("NickName").toString();
                            tempUser.SetUserNickName(NickName);
                        } else
                            tempUser.SetUserNickName(null);

                        if (document.getData().containsKey("Index")) {
                            String Index = document.getData().get("Index").toString();
                            tempUser.SetUserIndex(Index);
                        } else
                            tempUser.SetUserIndex("1");

                        if (document.getData().containsKey("Img_ThumbNail")) {
                            String tempData = document.getData().get("Img_ThumbNail").toString();
                            tempUser.SetUserImgThumb(tempData);
                        } else
                            tempUser.SetUserImgThumb(null);

                        if (document.getData().containsKey("Img")) {
                            tempUser.ClearUserImg();
                            HashMap<String, String> tempImg = (HashMap<String, String>) document.getData().get("Img");
                            Set set = tempImg.entrySet();
                            Iterator iterator = set.iterator();
                            while (iterator.hasNext()) {
                                Map.Entry entry = (Map.Entry) iterator.next();
                                String key = (String) entry.getKey();
                                String value = (String) entry.getValue();
                                tempUser.SetUserImg(key, value);
                            }
                        }

                        if (document.getData().containsKey("Favorite")) {
                            HashMap<String, String> tempImg = (HashMap<String, String>) document.getData().get("Favorite");
                            Set set = tempImg.entrySet();
                            Iterator iterator = set.iterator();
                            while (iterator.hasNext()) {
                                Map.Entry entry = (Map.Entry) iterator.next();
                                String key = (String) entry.getKey();
                                String value = (String) entry.getValue();
                                tempUser.SetUserFavorite(key, value);
                            }
                        }

                        if (document.getData().containsKey("Dist_Lon")) {
                            tempUser.SetUserDist_Lon(Double.parseDouble(document.getData().get("Dist_Lon").toString()));
                        } else
                            tempUser.SetUserDist_Lon(126.978425);

                        if (document.getData().containsKey("Dist_Lat")) {
                            tempUser.SetUserDist_Lat(Double.parseDouble(document.getData().get("Dist_Lat").toString()));
                        } else
                            tempUser.SetUserDist_Lat(37.566659);

                        double Distance = CommonFunc.getInstance().DistanceByDegree(TKManager.getInstance().MyData.GetUserDist_Lat(), TKManager.MyData.GetUserDist_Lon(), tempUser.GetUserDist_Lat(), tempUser.GetUserDist_Lon());
                        tempUser.SetUserDist((long)Distance);


                        if (document.getData().containsKey("Age")) {
                            int Age = Integer.parseInt(document.getData().get("Age").toString());
                            tempUser.SetUserAge(Age);
                        } else
                            tempUser.SetUserAge(50);

                        if (document.getData().containsKey("Gender")) {
                            int Gender = Integer.parseInt(document.getData().get("Gender").toString());
                            tempUser.SetUserGender(Gender);
                        } else
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

    public void SetUserFavoriteOnFireBase(final String favoriteName, boolean update) {
        if (mDataBase == null)
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

        RegistFavoriteListInUserData(favoriteName, update);
        RegistFavoriteList(favoriteName);
    }

    public void GetPopFavoriteData(final FirebaseManager.CheckFirebaseComplete listener) {
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

    public void CheckNickName(final String nickName, final CheckFirebaseComplete listener) {
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
                        //TKManager.getInstance().MyData.SetUserNickName(nickName);
                        if (listener != null)
                            listener.CompleteListener_Yes();
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });
    }

    public void UpdateUserName()
    {
        Map<String, Object> tempData = new HashMap<>();
        tempData.put("NickName", TKManager.getInstance().MyData.GetUserNickName());

        mDataBase.collection("UserData").document(TKManager.getInstance().MyData.GetUserIndex())
                .set(tempData, SetOptions.merge())
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

        mDataBase.collection("UserData_Simple").document(TKManager.getInstance().MyData.GetUserIndex())
                .set(tempData, SetOptions.merge())
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

        mDataBase.collection("UserData_NickName").document(TKManager.getInstance().MyData.GetUserNickName())
                .set(tempData, SetOptions.merge())
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

    public void RemoveUserNickName(final FirebaseManager.CheckFirebaseComplete listener)
    {
        mDataBase.collection("UserData_NickName").document(TKManager.getInstance().MyData.GetUserNickName()).delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot successfully deleted!");
                        if(listener != null)
                            listener.CompleteListener();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error deleting document", e);
                    }
                });
    }

    public void UploadThumbImg(String userIndex, Bitmap bitmap, final FirebaseManager.CheckFirebaseComplete listener) {
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
                    SetUserImgThumb();
                    if (listener != null)
                        listener.CompleteListener();
                } else {
                    // Handle failures
                    // ...
                }
            }
        });

    }

    public void UploadImgInChatRoom(String roomIndex, String chatIndex, Bitmap bitmap, final FirebaseManager.CheckFirebaseComplete listener) {
        final StorageReference tempThumbnailRef = mStorageRef.child("ChatData/" + roomIndex + "/" + chatIndex + "/Image.jpg");
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
                    TKManager.getInstance().MyData.SetUserImgChat(downloadUri.toString());
                    if (listener != null)
                        listener.CompleteListener();
                } else {
                    // Handle failures
                    // ...
                }
            }
        });


    }

    public void UploadImg(String userIndex, Bitmap bitmap, final int imageIndex, final FirebaseManager.CheckFirebaseComplete listener) {
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
                    SetUserImg();
                    if (listener != null)
                        listener.CompleteListener();
                } else {
                    // Handle failures
                    // ...
                }
            }
        });


    }

    public void SetUserImgThumb() {
        Map<String, Object> UserThumb = new HashMap<>();
        UserThumb.put("Img_ThumbNail", TKManager.getInstance().MyData.GetUserImgThumb());

        mDataBase.collection("UserData").document(TKManager.getInstance().MyData.GetUserIndex())
                .update(UserThumb)
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

        mDataBase.collection("UserData_Simple").document(TKManager.getInstance().MyData.GetUserIndex())
                .update(UserThumb)
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

    public void SetUserImg() {
        Map<String, Object> UserImg = new HashMap<>();
        UserImg.put("Img", TKManager.getInstance().MyData.GetUserImg());

        mDataBase.collection("UserData").document(TKManager.getInstance().MyData.GetUserIndex())
                .update(UserImg)
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

        mDataBase.collection("UserData_Simple").document(TKManager.getInstance().MyData.GetUserIndex())
                .update(UserImg)
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


    public void SetUserMemo() {
        Map<String, Object> UserMemo = new HashMap<>();
        UserMemo.put("Memo", TKManager.getInstance().MyData.GetUserMemo());

        mDataBase.collection("UserData").document(TKManager.getInstance().MyData.GetUserIndex())
                .update(UserMemo)
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

    public void SetUserLocation() {
        Map<String, Object> UserLocation = new HashMap<>();
        UserLocation.put("Location", TKManager.getInstance().MyData.GetUserLocation());

        mDataBase.collection("UserData").document(TKManager.getInstance().MyData.GetUserIndex())
                .update(UserLocation)
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

    public void randomList(String index) {

        Random rnd = new Random();
        int tempIndex = rnd.nextInt(4);
        Map<String, Object> simpleUser = new HashMap<>();
        simpleUser.put("value", index);
        switch (tempIndex) {
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


    public void AddChatData(final String roomIndex, final ChatData chatData) {

        final String userIndex = TKManager.getInstance().MyData.GetUserIndex();
        final Long[] newPopulation = new Long[1];

        final DocumentReference sfChatIndexRef = mDataBase.collection("ChatRoomData").document(roomIndex).collection(roomIndex).document("Index");
        mDataBase.runTransaction(new Transaction.Function<Void>() {
            @Override
            public Void apply(Transaction transaction) throws FirebaseFirestoreException {
                DocumentSnapshot snapshot = transaction.get(sfChatIndexRef);

                newPopulation[0] = snapshot.getLong("Index") + 1;
                transaction.update(sfChatIndexRef, "Index", newPopulation[0]);
                // Success
                return null;
            }
        }).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d(TAG, "Transaction success!");

                chatData.SetMsgIndex((newPopulation[0]));
                mDataBase.collection("ChatRoomData").document(roomIndex).collection(roomIndex).document( newPopulation[0].toString())
                        .set(chatData, SetOptions.merge())
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

                mDataBase.collection("UserData").document(TKManager.getInstance().MyData.GetUserIndex()).collection("ChatRoomList").document(roomIndex)
                        .set(chatData, SetOptions.merge())
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

                TKManager.getInstance().MyData.SetUserChatReadIndexList(chatData.GetRoomIndex(), chatData.GetMsgIndex());
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Transaction failure.", e);
                    }
                });
    }


    public void RegistChatList(String targetIndex, ChatData chatData) {
        String userIndex = TKManager.getInstance().MyData.GetUserIndex();
        String ChatRoomIndex = userIndex + "_" + targetIndex;

      /*  mDataBase.collection("ChatRoomList").document(targetIndex).collection("ChatRoomIndex").document(ChatRoomIndex)
                .set(chatData, SetOptions.merge())
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
                });*/

        //favoriteData.put("Index", Integer.parseInt(CommonFunc.getInstance().GetCurrentDate()));

        mDataBase.collection("UserData").document(userIndex).collection("ChatRoomList").document(ChatRoomIndex)
                .set(chatData, SetOptions.merge())
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


        mDataBase.collection("UserData").document(targetIndex).collection("ChatRoomList").document(ChatRoomIndex)
                .set(chatData, SetOptions.merge())
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

    public void ExistChatRoom(final String targetIndex, final FirebaseManager.CheckFirebaseComplete listener)
    {
        String userIndex = TKManager.getInstance().MyData.GetUserIndex();
        final String ChatRoomIndex = userIndex + "_" + targetIndex;
        final String AnotherChatRoomIndex = targetIndex+ "_" + userIndex ;

        DocumentReference docRef = mDataBase.collection("ChatRoomData").document(ChatRoomIndex).collection(ChatRoomIndex).document("0");
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                        if (listener != null)
                            listener.CompleteListener();

                    } else {
                        Log.d(TAG, "No such document");
                        DocumentReference  docRef = mDataBase.collection("ChatRoomData").document(AnotherChatRoomIndex).collection(AnotherChatRoomIndex).document("0");
                        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful()) {
                                    DocumentSnapshot document = task.getResult();
                                    if (document.exists()) {
                                        Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                                        if (listener != null)
                                            listener.CompleteListener_Yes();

                                    } else {
                                        Log.d(TAG, "No such document");
                                        if (listener != null)
                                            listener.CompleteListener_No();
                                    }
                                } else {
                                    Log.d(TAG, "get failed with ", task.getException());
                                }
                            }
                        });
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });



    }

    public void RegistChatData(String targetIndex, ChatData chatData)
    {

        String userIndex = TKManager.getInstance().MyData.GetUserIndex();
        String ChatRoomIndex = userIndex + "_" + targetIndex;
        //favoriteData.put("Index", Integer.parseInt(CommonFunc.getInstance().GetCurrentDate()));

        mDataBase.collection("ChatRoomData").document(ChatRoomIndex).collection(ChatRoomIndex).document("0")
                .set(chatData, SetOptions.merge())
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

        Map<String, Object> tempIndex = new HashMap<>();
        tempIndex.put("Index", 0);
        mDataBase.collection("ChatRoomData").document(ChatRoomIndex).collection(ChatRoomIndex).document("Index")
                .set(tempIndex, SetOptions.merge())
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

    public void UpdateFavoriteListInUserData()
    {
        String userIndex = TKManager.getInstance().MyData.GetUserIndex();

        Set EntrySet = TKManager.getInstance().MyData.GetUserFavoriteListKeySet();
        Iterator iterator = EntrySet.iterator();

        while(iterator.hasNext()){
            String key = (String)iterator.next();
            Map<String, Object> favoriteData = new HashMap<>();
            favoriteData.put("Name", key);
            mDataBase.collection("UserData").document(userIndex).collection("FavoriteList").document(key)
                    .set(favoriteData);

            RegistFavoriteList(key);
        }
    }

    public void RegistFavoriteList(String favoriteName)
    {
        String userIndex = TKManager.getInstance().MyData.GetUserIndex();
        Map<String, Object> favoriteData = new HashMap<>();
        favoriteData.put("Index", userIndex);
        //favoriteData.put("Index", Integer.parseInt(CommonFunc.getInstance().GetCurrentDate()));

        mDataBase.collection("FavoriteList").document(favoriteName).collection("UserIndex").document(userIndex)
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

    public void RegistFavoriteListInUserData(String favoriteName, boolean update)
    {
        Map<String, Object> favoriteData = new HashMap<>();
        favoriteData.put("Name", favoriteName);
        //favoriteData.put("Index", Integer.parseInt(CommonFunc.getInstance().GetCurrentDate()));
        String userIndex = TKManager.getInstance().MyData.GetUserIndex();


        if(update)
        {
            mDataBase.collection("UserData").document(userIndex).collection("FavoriteList").document(favoriteName)
                    .set(favoriteData)
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
        else
        {
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

    }


    public void RegistFriendInUserData(final String targetIndex, final CheckFirebaseComplete listener)
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
                        if(listener != null)
                        {
                            listener.CompleteListener();
                        }
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

    public void RemoveFavoriteUser(final String favorite)
    {
        String userIndex = TKManager.getInstance().MyData.GetUserIndex();

        DocumentReference sfDocRef = mDataBase.collection("UserData").document(userIndex).collection("FavoriteList").document(favorite);

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

        sfDocRef = mDataBase.collection("FavoriteList").document(favorite).collection("UserIndex").document(userIndex);

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

    public void RemoveFriendUser(final String targetIndex, final CheckFirebaseComplete listener)
    {
        String userIndex = TKManager.getInstance().MyData.GetUserIndex();

        final DocumentReference sfDocRef = mDataBase.collection("UserData").document(userIndex).collection("FriendUsers").document(targetIndex);

        sfDocRef.delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot successfully deleted!");
                        if(listener != null)
                        {
                            listener.CompleteListener();
                        }
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

    public void RegistUserDistInfo()
    {
        String userIndex = TKManager.getInstance().MyData.GetUserIndex();

        Map<String, Object> UserDistInfo = new HashMap<>();
        UserDistInfo.put("Dist_Lat", TKManager.getInstance().MyData.GetUserDist_Lat());
        UserDistInfo.put("Dist_Lon", TKManager.getInstance().MyData.GetUserDist_Lon());
        UserDistInfo.put("Dist_Region", TKManager.getInstance().MyData.GetUserDist_Region());
        UserDistInfo.put("Dist_Area", TKManager.getInstance().MyData.GetUserDist_Area());

        mDataBase.collection("UserData").document(userIndex)
                .set(UserDistInfo, SetOptions.merge())
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
