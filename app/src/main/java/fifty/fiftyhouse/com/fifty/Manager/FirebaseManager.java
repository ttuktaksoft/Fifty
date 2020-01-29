package fifty.fiftyhouse.com.fifty.Manager;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import androidx.annotation.NonNull;
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
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.firestore.Transaction;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.imperiumlabs.geofirestore.GeoFirestore;
import org.imperiumlabs.geofirestore.GeoQuery;
import org.imperiumlabs.geofirestore.GeoQueryDataEventListener;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import javax.annotation.Nullable;

import fifty.fiftyhouse.com.fifty.CommonData;
import fifty.fiftyhouse.com.fifty.CommonFunc;
import fifty.fiftyhouse.com.fifty.DataBase.AlarmData;
import fifty.fiftyhouse.com.fifty.DataBase.ChatData;
import fifty.fiftyhouse.com.fifty.DataBase.ClubContextData;
import fifty.fiftyhouse.com.fifty.DataBase.ClubData;
import fifty.fiftyhouse.com.fifty.DataBase.NoticeData;
import fifty.fiftyhouse.com.fifty.DataBase.UserData;
import fifty.fiftyhouse.com.fifty.DialogFunc;
import fifty.fiftyhouse.com.fifty.MainActivity;
import fifty.fiftyhouse.com.fifty.R;

import static fifty.fiftyhouse.com.fifty.CommonData.FAVORITE_RANK_COUNT;

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

    private DocumentSnapshot lastNewUserVisible;
    private DocumentSnapshot lastFriendUserVisible;
    private DocumentSnapshot lastFavoriteUserVisible;

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

    public void SetFireBaseLoadingCount(final String Call, int count) {
        UserLoading = 0;
        FireBaseLoadingCount = count;
        if(CommonData.TEST_TEXT_VISIBLE)
            Log.d("!!!!!!!", Call + "로딩 설정: " + FireBaseLoadingCount);
    }

    public void AddFireBaseLoadingCount(final String Call) {
        FireBaseLoadingCount++;
        if(CommonData.TEST_TEXT_VISIBLE)
            Log.d("!!!!!!!", Call + "로딩 추가: " + FireBaseLoadingCount);
    }

    public void AddFireBaseLoadingCount(final String Call, int count) {
        FireBaseLoadingCount += count;
        if(CommonData.TEST_TEXT_VISIBLE)
            Log.d("!!!!!!!",  Call + "로딩 추가 덩어리 : " + FireBaseLoadingCount);
    }

    public int GetFireBaseLoadingCount() {
        return FireBaseLoadingCount;
    }

/*    public void CompleteFireBaseLoadingCount(final FirebaseManager.CheckFirebaseComplete listener) {
        UserLoading++;
        if (UserLoading == GetFireBaseLoadingCount()) {
            UserLoading = 0;
            SetFireBaseLoadingCount(0);
            if (listener != null)
                listener.CompleteListener();
        }
    }*/

    public void Complete(final String Call, final FirebaseManager.CheckFirebaseComplete listener) {

        UserLoading++;

        if(CommonData.TEST_TEXT_VISIBLE)
            Log.d("!!!!!!!", Call + " 로딩 완료 : " + UserLoading + "   " + GetFireBaseLoadingCount() );

        if (UserLoading == GetFireBaseLoadingCount()) {
            UserLoading = 0;
            SetFireBaseLoadingCount(Call, 0);

            if(CommonData.TEST_TEXT_VISIBLE)
                Log.d("!!!!!!!", "로딩 완료 끝: " + UserLoading);

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

    public void SignInAnonymously(final Activity activity, final FirebaseManager.CheckFirebaseComplete listener) {
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

                            FirebaseInstanceId.getInstance().getInstanceId()
                                    .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                                        @Override
                                        public void onComplete(@NonNull Task<InstanceIdResult> task) {
                                            if (!task.isSuccessful()) {
                                                Log.w(TAG, "getInstanceId failed", task.getException());
                                                return;
                                            }

                                            // Get new Instance ID token
                                            String token = task.getResult().getToken();
                                            TKManager.getInstance().MyData.SetUserToken(token);

                                            if(listener != null)
                                                listener.CompleteListener();
                                        }
                                    });



                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInAnonymously:failure", task.getException());
                            Toast.makeText(activity, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public void SignInNickName(String nickName, final String passWord, final FirebaseManager.CheckFirebaseComplete listener)
    {
        final DocumentReference docRef = mDataBase.collection("UserAuth").document(nickName);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                        Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                        if(document.getData().get("PassWord").toString().equals(passWord))
                        {
                            TKManager.getInstance().MyData.SetUserIndex(document.getData().get("Index").toString());
                            if(listener != null)
                                listener.CompleteListener();
                        }
                        else
                        {
                            if(listener != null)
                                listener.CompleteListener_No();
                        }

                    } else {
                        if(listener != null)
                            listener.CompleteListener_Yes();

                        Log.d(TAG, "No such document");
                    }
                } else {
                    if(listener != null)
                        listener.CompleteListener();

                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });
    }

    public void SetMyAuthData(final FirebaseManager.CheckFirebaseComplete listener) {
        if (mDataBase == null)
            GetFireStore();

        Map<String, Object> user = new HashMap<>();
        user.put("Index", TKManager.getInstance().MyData.GetUserIndex());
        user.put("PhoneNumber", TKManager.getInstance().MyData.GetUserPhone());
        user.put("Name", TKManager.getInstance().MyData.GetUserName());
        user.put("NickName", TKManager.getInstance().MyData.GetUserNickName());
        user.put("PassWord", TKManager.getInstance().MyData.GetUserPassWord());

        Map<String, Object> userAuth = new HashMap<>();
        userAuth.put(TKManager.getInstance().MyData.GetUserIndex(), user);

        mDataBase.collection("UserAuth").document(TKManager.getInstance().MyData.GetUserNickName())
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

    }

    public void SetMyDataOnFireBase(boolean boot, final FirebaseManager.CheckFirebaseComplete listener) {
        if(CommonData.TEST_TEXT_VISIBLE)
            Log.d("!!!!!!!", "내 정보 저장");

        SetFireBaseLoadingCount("내 정보 저장", 5);

        if (mDataBase == null)
            GetFireStore();


        FirebaseManager.getInstance().RegistUserDistInfo();
        FirebaseManager.getInstance().RegistUserGeoInfo();

        if(boot)
        {
            Map<String, String> tempFavoriteData = TKManager.getInstance().MyData.GetUserFavoriteList();
            Set set = tempFavoriteData.entrySet();
            Iterator iterator = set.iterator();
            while (iterator.hasNext()) {
                Map.Entry entry = (Map.Entry) iterator.next();
                String key = (String) entry.getKey();
                String value = (String) entry.getValue();
                SetUserFavoriteOnFireBase(value, false);
            }
        }

        Map<String, Object> user = new HashMap<>();

        user.put("UId", TKManager.getInstance().MyData.GetUserIndex());
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

        user.put("Dist_Lon", TKManager.getInstance().MyData.GetUserDist_Lon());
        user.put("Dist_Lat", TKManager.getInstance().MyData.GetUserDist_Lat());

        user.put("Dist_Region", TKManager.getInstance().MyData.GetUserDist_Region());
        user.put("Dist_Area", TKManager.getInstance().MyData.GetUserDist_Area());
        user.put("ConnectDate", Long.parseLong(CommonFunc.getInstance().GetCurrentDate()));

        //user.put("Location", "");

        mDataBase.collection("UserData").document(TKManager.getInstance().MyData.GetUserIndex())
                .set(user, SetOptions.merge())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot successfully written!");
                        Complete("내 정보 저장 완", listener);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error writing document", e);
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
        simpleUser.put("ConnectDate", Long.parseLong(CommonFunc.getInstance().GetCurrentTime()));

        mDataBase.collection("UserData_Simple").document(TKManager.getInstance().MyData.GetUserIndex())
                .set(simpleUser, SetOptions.merge())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot successfully written!");
                        Complete("내 심플 정보 저장 완", listener);
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
                        Complete("내 닉네임 저장 완", listener);
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
                        Complete("내 거리 저장 완", listener);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error writing document", e);
                    }
                });


        Map<String, Object> New = new HashMap<>();
        final long TodayDate = Long.parseLong(CommonFunc.getInstance().GetCurrentTime());
        New.put("value", TodayDate);
        mDataBase.collection("UserList_New").document(TKManager.getInstance().MyData.GetUserIndex()).set(New, SetOptions.merge())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot successfully written!");
                        Complete("신규 정보 저장 완", listener);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error writing document", e);
                    }
                });

        Complete("내 정보 모두 완",  listener);
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


    public void MonitorAlarm(final String index,  final CheckFirebaseComplete listener) {

        CollectionReference colRef = mDataBase.collection("UserData").document(TKManager.getInstance().MyData.GetUserIndex()).collection("AlarmList");
        //CollectionReference colRef = mDataBase.collection("ChatRoomData").document(chatRoomIndex).collection(chatRoomIndex);
            colRef.orderBy("Date", Query.Direction.DESCENDING).limit(10).addSnapshotListener(new EventListener<QuerySnapshot>() {
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
                                Log.d("@@@@@@", "알람 변경 " + document.getDocument().getData().get("Index").toString());
                                AlarmData tempData = new AlarmData();

                                tempData.SetType(document.getDocument().getData().get("Type").toString());
                                tempData.SetIndex(document.getDocument().getData().get("Index").toString());
                                tempData.SetDate(Long.parseLong(document.getDocument().getData().get("Date").toString()));
                                if(tempData.GetType().equals("CHAT")) {
                                    TKManager.getInstance().mMainChatBadgeNumber = 1;

                                    if(TKManager.getInstance().mMainActivity != null)
                                    {
                                        TKManager.getInstance().mMainActivity.SetChatBadgeView(1);
                                    }
                                    tempData.SetMsg(document.getDocument().getData().get("Msg").toString());
                                }
                                TKManager.getInstance().MyData.SetAlarmList(document.getDocument().getId(), tempData);

                                break;
                            case REMOVED:
                                break;
                        }
                    }


                    if (TKManager.getInstance().isLoadDataByBoot == true && listener != null)
                        listener.CompleteListener();

                }
            });
    }

    public void RemoveMonitorUserChatData()
    {
        if(ChatDataMonitor_registration != null)
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

                                if (document.getDocument().getData().containsKey("File") && document.getDocument().getData().get("File") != null) {
                                    tempData.SetFile((document.getDocument().getData().get("File").toString()));
                                }

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



                 /*               userData.SetUserChatReadIndexList(tempData.GetRoomIndex(), tempData.GetMsgIndex());


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
                                        });*/
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

              //  TKManager.getInstance().mUpdateChatFragmentFunc.UpdateUI();

              //  ChatDataMonitor_registration.remove();
            }
        });
    }


    public void GetUserClubChatData(final String chatRoomIndex, final UserData userData, final CheckFirebaseComplete listener) {

        TKManager.getInstance().MyData.ClearUserChatData();
        SetFireBaseLoadingCount("클럽 채팅 로드", 0);
        CollectionReference colRef = mDataBase.collection("ChatRoomData").document(chatRoomIndex).collection(chatRoomIndex);
        final int TodayDate = Integer.parseInt(CommonFunc.getInstance().GetCurrentDate());

        colRef.orderBy("MsgIndex", Query.Direction.ASCENDING).limit(50).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    SetFireBaseLoadingCount("클럽 채팅 로드", task.getResult().size());

                    if(task.getResult().size() == 0)
                        listener.CompleteListener_No();

                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Log.d(TAG, document.getId() + " => " + document.getData());
                        if(!document.getId().equals("Index"))
                        {
                            ChatData tempData = new ChatData();
                            tempData.SetRoomIndex(document.getData().get("RoomIndex").toString());

                            tempData.SetFromIndex(document.getData().get("FromIndex").toString());
                            tempData.SetFromNickName(document.getData().get("FromNickName").toString());
                            tempData.SetFromThumbNail(document.getData().get("FromThumbNail").toString());

                            tempData.SetMsg(document.getData().get("Msg").toString());


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

                            if(!tempData.GetFromIndex().equals(TKManager.getInstance().MyData.GetUserIndex()))
                            {
                                GetUserData_Simple(tempData.GetFromIndex(), TKManager.getInstance().UserData_Simple, listener);
                            }
                            else
                            {
                                Complete("클럽 채팅 로드 완", listener);
                            }

                        }

                    }

                } else {
                    Log.d(TAG, "Error getting documents: ", task.getException());
                }
            }
        });
    }

    public void GetUserListInChatRoom(final String chatRoomIndex, final UserData userData, final CheckFirebaseComplete listener) {

        TKManager.getInstance().MyData.ClearChatUserList();
        SetFireBaseLoadingCount("채팅 인원 로드", 0);

        DocumentReference docRef = mDataBase.collection("ChatRoomData").document(chatRoomIndex);

        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {

                        Log.d(TAG, "DocumentSnapshot data: " + document.getData());

                        if (document.getData().containsKey("UserList")) {
                            HashMap<String, String> tempImg = (HashMap<String, String>) document.getData().get("UserList");
                            SetFireBaseLoadingCount("채팅 인원 로드", tempImg.size());

                            Set set = tempImg.entrySet();
                            Iterator iterator = set.iterator();
                            while (iterator.hasNext()) {
                                Map.Entry entry = (Map.Entry) iterator.next();
                                String key = (String) entry.getKey();
                                String value = (String) entry.getValue();
                                userData.SetChatUserList(key, value);

                                if(!TKManager.getInstance().UserData_Simple.containsKey(key))
                                {
                                    GetUserData_Simple(key, TKManager.getInstance().UserData_Simple, listener);
                                }
                                else
                                {
                                    Complete( "채팅 인원 로드 완", listener);
                                }
                            }
                        }

                    } else {
                        Log.d(TAG, "No such document");
                        DialogFunc.getInstance().DismissLoadingPage();
                        //TKManager.getInstance().MyData.SetUserNickName(nickName);
                        if (listener != null)
                            listener.CompleteListener_No();
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });
    }

  /*  public void GetUserChatData(final String chatRoomIndex, final UserData userData, final CheckFirebaseComplete listener) {

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

                                tempData.SetMsgReadCheckNumber(Long.parseLong(document.getData().get("MsgReadCheckNum").toString()));

                                if(!tempData.GetFromIndex().equals(TKManager.getInstance().MyData.GetUserIndex()))
                                {
                                    tempData.SetMsgReadCheck(true);

                                    final DocumentReference sfDocRef = mDataBase.collection("ChatRoomData").document(chatRoomIndex).collection(chatRoomIndex).document(document.getId());

                                    mDataBase.runTransaction(new Transaction.Function<Void>() {
                                        @Override
                                        public Void apply(Transaction transaction) throws FirebaseFirestoreException {
                                            DocumentSnapshot snapshot = transaction.get(sfDocRef);
                                            double newPopulation = snapshot.getDouble("MsgReadCheckNum") - 1;
                                            transaction.update(sfDocRef, "MsgReadCheckNum", newPopulation);

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


                            }
                        }

                        if (listener != null)
                            listener.CompleteListener();

                    } else {
                        Log.d(TAG, "Error getting documents: ", task.getException());
                    }
                }
            });
    }*/


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

                            if (document.getData().containsKey("MsgReadCheckNum")) {
                                double num = Double.parseDouble(document.getData().get("MsgReadCheckNum").toString());
                                int inum = (int)num;
                                tempData.SetMsgReadCheckNumber(inum);
                            }

                            if (document.getData().containsKey("File") && document.getData().get("File") != null) {
                                tempData.SetFile((document.getData().get("File").toString()));
                            }


                            if (document.getData().containsKey("ReadUserList")) {
                                tempData.ClearReadUserList();
                                HashMap<String, String> tempImg = (HashMap<String, String>) document.getData().get("ReadUserList");
                                Set set = tempImg.entrySet();
                                Iterator iterator = set.iterator();
                                while (iterator.hasNext()) {
                                    Map.Entry entry = (Map.Entry) iterator.next();
                                    String key = (String) entry.getKey();
                                    String value = (String) entry.getValue();
                                    tempData.AddMsgReadCheckUser(key);
                                }
                            }

                            if(!tempData.GetFromIndex().equals(TKManager.getInstance().MyData.GetUserIndex()))
                            {
                                if(CommonFunc.getInstance().CheckStringNull(tempData.GetMsgReadCheckUser(TKManager.getInstance().MyData.GetUserIndex())))
                                {
                                    Map<String, Object> tempReadUser = new HashMap<>();
                                    tempReadUser.put(TKManager.getInstance().MyData.GetUserIndex(), TKManager.getInstance().MyData.GetUserIndex());

                                    Map<String, Object> ReadUser = new HashMap<>();
                                    ReadUser.put("ReadUserList", tempReadUser);

                                    final DocumentReference sfDocRef = mDataBase.collection("ChatRoomData").document(chatRoomIndex).collection(chatRoomIndex).document(document.getId());

                                    sfDocRef
                                            .set(ReadUser, SetOptions.merge() )
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

                                    tempData.SetMsgReadCheck(true);


                                    mDataBase.runTransaction(new Transaction.Function<Void>() {
                                        @Override
                                        public Void apply(Transaction transaction) throws FirebaseFirestoreException {
                                            DocumentSnapshot snapshot = transaction.get(sfDocRef);
                                            double newPopulation = snapshot.getDouble("MsgReadCheckNum") - 1;
                                            transaction.update(sfDocRef, "MsgReadCheckNum", newPopulation);

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
                                }
                                else
                                {

                                }
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
                            //userData.SetUserChatReadIndexList(tempData.GetRoomIndex(), tempData.GetMsgIndex());


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
        //CollectionReference colRef = mDataBase.collection("ChatRoomData").document(chatRoomIndex).collection(chatRoomIndex);
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
                                CommonData.CHAT_ROOM_TYPE tempRoomType = CommonData.CHAT_ROOM_TYPE.DEFAULT;

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

                                    if (document.getDocument().getData().containsKey("File") && document.getDocument().getData().get("File") != null) {
                                        tempData.SetFile((document.getDocument().getData().get("File").toString()));
                                    }


                                   /* if (document.getDocument().getData().containsKey("RoomType")) {
                                        tempRoomType = CommonData.CHAT_ROOM_TYPE.valueOf(document.getDocument().getData().get("RoomType").toString());
                                    }*/

                                   if(TKManager.getInstance().MyData.GetUserChatRoomTypeList(tempData.GetRoomIndex()) == CommonData.CHAT_ROOM_TYPE.BOOKMARK)
                                   //if(TKManager.getInstance().MyData.ExistUserBookMarkChatDataListKeySet(tempData.GetRoomIndex()))
                                       tempData.SetRoomType(CommonData.CHAT_ROOM_TYPE.BOOKMARK);
                                   else
                                       tempData.SetRoomType(CommonData.CHAT_ROOM_TYPE.DEFAULT);

                                    CommonData.MSGType tempType = CommonData.MSGType.valueOf(document.getDocument().getData().get("MsgType").toString());
                                    tempData.SetMsgType(tempType);


                                    try {
                                        TKManager.getInstance().copyData = (ChatData)tempData.clone();
                                    } catch (CloneNotSupportedException e1) {
                                        e1.printStackTrace();
                                    }

                                    if ( TKManager.getInstance().copyData.GetRoomType().equals(CommonData.CHAT_ROOM_TYPE.BOOKMARK)) {
                                        userData.SetUserBookMarkChatDataList(TKManager.getInstance().copyData.GetRoomIndex(), TKManager.getInstance().copyData);
                                    }
                                    else
                                    {
                                        userData.SetUserChatDataList(TKManager.getInstance().copyData.GetRoomIndex(), TKManager.getInstance().copyData);
                                    }

                                   userData.SetUserChatReadIndexList(tempRoomName, TKManager.getInstance().copyData.GetMsgIndex());


                                    if(!TKManager.getInstance().copyData.GetFromIndex().equals(TKManager.getInstance().MyData.GetUserIndex()))
                                    {
                                        Map<String, Object> AlarmData = new HashMap<>();
                                        AlarmData.put("Index", TKManager.getInstance().copyData.GetFromIndex());
                                        AlarmData.put("Date", TKManager.getInstance().copyData.GetMsgDate());
                                        AlarmData.put("Type", "CHAT");
                                        AlarmData.put("Msg", TKManager.getInstance().copyData.GetMsg());


                                        mDataBase.collection("UserData").document(TKManager.getInstance().MyData.GetUserIndex()).collection("AlarmList").document(TKManager.getInstance().copyData.GetFromIndex())
                                                .set(AlarmData, SetOptions.merge())
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

                                break;
                            case REMOVED:
                    /*            tempRoomName = document.getDocument().getData().get("RoomIndex").toString();
                                tempRoomType = CommonData.CHAT_ROOM_TYPE.DEFAULT;

                                if (document.getDocument().getData().containsKey("RoomType")) {
                                    tempRoomType = CommonData.CHAT_ROOM_TYPE.valueOf(document.getDocument().getData().get("RoomType").toString());
                                }

                                if (tempRoomType == CommonData.CHAT_ROOM_TYPE.BOOKMARK) {
                                    userData.DelUserBookMarkChatDataList(tempRoomName);
                                }
                                else
                                {
                                    userData.DelUserChatDataList(tempRoomName);
                                }*/
                                break;
                        }
                    }

                    if (TKManager.getInstance().isLoadDataByBoot == true && listener != null)
                        listener.CompleteListener();

                    if(TKManager.getInstance().mUpdateChatFragmentFunc != null)
                    {
                        TKManager.getInstance().mUpdateChatFragmentFunc.UpdateUI();
                    }


                }
            });
        }
    }

 /*   public void GetUserChatList(String userIndex, final UserData userData, final CheckFirebaseComplete listener) {
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
                            ChatData tempData = new ChatData();


                            if (document.getDocument().getData().containsKey("MsgType")) {
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
                            }

                            if (document.getDocument().getData().containsKey("RoomName")) {
                                tempData.SetRoomName(document.getDocument().getData().get("RoomName").toString());
                            }

                            if (document.getDocument().getData().containsKey("RoomThumb")) {
                                tempData.SetRoomThumb(document.getDocument().getData().get("RoomThumb").toString());
                            }


                            if (document.getDocument().getData().containsKey("FromIndex")) {
                                tempData.SetFromIndex(document.getDocument().getData().get("FromIndex").toString());
                            }
                            if (document.getDocument().getData().containsKey("ToIndex")) {
                                tempData.SetToIndex(document.getDocument().getData().get("ToIndex").toString());
                            }

                            if (document.getDocument().getData().containsKey("MsgIndex")) {
                                tempData.SetMsgIndex(Long.parseLong(document.getDocument().getData().get("MsgIndex").toString()));
                            }

                            if (document.getDocument().getData().containsKey("Msg")) {
                                tempData.SetMsg(document.getDocument().getData().get("Msg").toString());
                            }

                            if (document.getDocument().getData().containsKey("MsgDate")) {
                                tempData.SetMsgDate(Long.parseLong(document.getDocument().getData().get("MsgDate").toString()));
                            }

                            if (document.getDocument().getData().containsKey("MsgSender")) {
                                tempData.SetMsgSender(document.getDocument().getData().get("MsgSender").toString());
                            }

                            if (document.getDocument().getData().containsKey("RoomIndex")) {
                                tempData.SetRoomIndex(document.getDocument().getData().get("RoomIndex").toString());
                            }

                            CommonData.CHAT_ROOM_TYPE tempRoomType = CommonData.CHAT_ROOM_TYPE.DEFAULT;

                            if (document.getDocument().getData().containsKey("RoomType")) {
                                tempRoomType = CommonData.CHAT_ROOM_TYPE.valueOf(document.getDocument().getData().get("RoomType").toString());
                            }

                            tempData.SetRoomType(tempRoomType);

                            if (tempData.GetRoomType().equals(CommonData.CHAT_ROOM_TYPE.BOOKMARK)) {
                                userData.SetUserBookMarkChatDataList(tempData.GetRoomIndex(), tempData);
                            }
                            else
                            {
                                userData.SetUserChatDataList(tempData.GetRoomIndex(), tempData);
                            }



                     *//*       if(!TKManager.getInstance().MonitorChatList.contains(tempData.GetRoomIndex()))
                            {
                                TKManager.getInstance().MonitorChatList.add(tempData.GetRoomIndex());
                                MonitorChatData(tempData.GetRoomIndex(), TKManager.getInstance().MyData, null);
                            }*//*

                            break;


                        case REMOVED:
                            tempRoomType = CommonData.CHAT_ROOM_TYPE.DEFAULT;

                            if (document.getDocument().getData().containsKey("RoomType")) {
                                tempRoomType = CommonData.CHAT_ROOM_TYPE.valueOf(document.getDocument().getData().get("RoomType").toString());
                            }

                            if (tempRoomType == CommonData.CHAT_ROOM_TYPE.BOOKMARK) {
                                userData.DelUserBookMarkChatDataList(document.getDocument().getId());
                            }
                            else
                            {
                                userData.DelUserChatDataList(document.getDocument().getId());
                            }

                            break;
                    }
                }


                if (TKManager.getInstance().isLoadDataByBoot == true && listener != null)
                    listener.CompleteListener();

            }
        });
    }*/

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
                            ChatData tempData = new ChatData();

                            if (document.getDocument().getData().containsKey("RoomIndex")) {
                                tempData.SetRoomIndex(document.getDocument().getData().get("RoomIndex").toString());
                            }

                            CommonData.CHAT_ROOM_TYPE tempRoomType = CommonData.CHAT_ROOM_TYPE.DEFAULT;

                            if (document.getDocument().getData().containsKey("RoomType")) {
                                tempRoomType = CommonData.CHAT_ROOM_TYPE.valueOf(document.getDocument().getData().get("RoomType").toString());
                                userData.SetUserChatRoomTypeList(tempData.GetRoomIndex(), tempRoomType);
                            }

                     /*       tempData.SetRoomType(tempRoomType);

                            if (tempData.GetRoomType().equals(CommonData.CHAT_ROOM_TYPE.BOOKMARK)) {
                                userData.SetUserBookMarkChatDataList(tempData.GetRoomIndex(), tempData);
                            }
                            else
                            {
                                userData.SetUserChatDataList(tempData.GetRoomIndex(), tempData);
                            }*/

                            Log.d("@@@@@@", "TKManager.getInstance().MonitorChatList " + tempData.GetRoomIndex());

                            if(!TKManager.getInstance().MonitorChatList.contains(tempData.GetRoomIndex()))
                            {
                                AddFireBaseLoadingCount("채팅 방 로드");
                                TKManager.getInstance().MonitorChatList.add(tempData.GetRoomIndex());
                                MonitorChatData(tempData.GetRoomIndex(), TKManager.getInstance().MyData, listener);
                            }
                            else
                            {
                                if (TKManager.getInstance().isLoadDataByBoot == true && listener != null)
                                    listener.CompleteListener();
                            }

                            break;


                        case REMOVED:
                            tempRoomType = CommonData.CHAT_ROOM_TYPE.DEFAULT;

                            if (document.getDocument().getData().containsKey("RoomType")) {
                                tempRoomType = CommonData.CHAT_ROOM_TYPE.valueOf(document.getDocument().getData().get("RoomType").toString());
                            }

                            if (tempRoomType == CommonData.CHAT_ROOM_TYPE.BOOKMARK) {
                                userData.DelUserBookMarkChatDataList(document.getDocument().getId());
                            }
                            else
                            {
                                userData.DelUserChatDataList(document.getDocument().getId());
                            }

                            break;
                    }
                }

/*                if (TKManager.getInstance().isLoadDataByBoot == true && listener != null)
                    listener.CompleteListener();*/

               /* if (TKManager.getInstance().isLoadDataByBoot == true && listener != null)
                    listener.CompleteListener();*/

            }
        });
    }


    public void GetRequestFriendList(final CheckFirebaseComplete listener) {
        CollectionReference colRef = mDataBase.collection("UserData").document(TKManager.getInstance().MyData.GetUserIndex()).collection("RequestFriendList");
        final int TodayDate = Integer.parseInt(CommonFunc.getInstance().GetCurrentDate());

        colRef.orderBy("Date", Query.Direction.DESCENDING).limit(CommonData.UserList_Loding_Count).addSnapshotListener(new EventListener<QuerySnapshot>() {
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
                            if(!document.getDocument().getId().equals(TKManager.getInstance().MyData.GetUserIndex()))
                            {
                                TKManager.getInstance().MyData.SetRequestFriend(document.getDocument().getId().toString(), document.getDocument().getData().get("Date").toString());
                                if(TKManager.getInstance().UserData_Simple.get(document.getDocument().getId().toString()) == null)
                                {
                                    AddFireBaseLoadingCount("친구 목록 로드");
                                    Log.d(TAG, document.getDocument().getId() + " => " + document.getDocument().getData());
                                    GetUserData_Simple(document.getDocument().getId(), TKManager.getInstance().UserData_Simple, listener);
                                }

                            }
                            break;
                        case REMOVED:
                            TKManager.getInstance().MyData.DelRequestFriendList(document.getDocument().getId().toString());
                            break;
                    }
                }
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

    public void GetUserLikeList(String userIndex, final UserData userData, final  boolean mydata, final CheckFirebaseComplete listener) {
        CollectionReference colRef = mDataBase.collection("UserData").document(userIndex).collection("LikeUsers");
        final int TodayDate = Integer.parseInt(CommonFunc.getInstance().GetCurrentDate());

        if(mydata)
        {
            colRef.whereLessThanOrEqualTo("Date", TodayDate).limit(20).addSnapshotListener(new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                    if (e != null) {
                        Log.w(TAG, "Listen failed.", e);
                        return;
                    }

                    int tempTotayVisitCount = 0;

                    for (DocumentChange document : queryDocumentSnapshots.getDocumentChanges()) {
                        switch (document.getType()) {
                            case ADDED:
                            case MODIFIED:

                                TKManager.getInstance().MyData.SetUserLikeList(document.getDocument().getData().get("Index").toString(), document.getDocument().getData().get("Date").toString());

                                if (Integer.parseInt(document.getDocument().getData().get("Date").toString()) == TodayDate) {
                                    tempTotayVisitCount++;
                                    TKManager.getInstance().MyData.SetUserTodayLike(tempTotayVisitCount);
                                }

                                TKManager.getInstance().MyData.SetUserTotalLike(userData.GetUserVisitListCount());

                                break;
                            case REMOVED:
                                break;
                        }
                    }

                    if (TKManager.getInstance().isLoadDataByBoot == true && listener != null)
                        listener.CompleteListener();
                }
            });
        }
        else
        {
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
    }

    private void GetUserVisitList(String userIndex, final UserData userData, final  boolean mydata, final CheckFirebaseComplete listener) {
        CollectionReference colRef = mDataBase.collection("UserData").document(userIndex).collection("VisitUsers");
        final int TodayDate = Integer.parseInt(CommonFunc.getInstance().GetCurrentDate());

        if(mydata)
        {
            colRef.whereLessThanOrEqualTo("Date", TodayDate).limit(20).addSnapshotListener(new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                    if (e != null) {
                        Log.w(TAG, "Listen failed.", e);
                        return;
                    }

                    int tempTotayVisitCount = 0;

                    for (DocumentChange document : queryDocumentSnapshots.getDocumentChanges()) {
                        switch (document.getType()) {
                            case ADDED:
                            case MODIFIED:

                                TKManager.getInstance().MyData.SetUserVisitList(document.getDocument().getData().get("Index").toString(), document.getDocument().getData().get("Date").toString());

                                if (Integer.parseInt(document.getDocument().getData().get("Date").toString()) == TodayDate) {
                                    tempTotayVisitCount++;
                                    TKManager.getInstance().MyData.SetUserTodayVisit(tempTotayVisitCount);
                                }

                                TKManager.getInstance().MyData.SetUserTotalVisit(userData.GetUserVisitListCount());

                                break;
                            case REMOVED:
                                break;
                        }
                    }

                    if (TKManager.getInstance().isLoadDataByBoot == true && listener != null)
                        listener.CompleteListener();
                }
            });
        }
        else
        {
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
    }

/*
    public void GetUserFavoriteClubThumb(final String clubIndex, final CheckFirebaseComplete listener)
    {
        CollectionReference colRef = mDataBase.collection("ClubData_Favorite").document(favorite).collection("ClubIndex");
        colRef.orderBy("MemberCount", Query.Direction.DESCENDING).limit(1).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {

                        TKManager.getInstance().Fa.add(document.getId().toString());
                    }

                    if(listener != null)
                    {
                        long seed = System.nanoTime();
                        Collections.shuffle( TKManager.getInstance().FavoriteLIst_ClubList, new Random(seed));

                        listener.CompleteListener();
                    }


                } else {
                    Log.d(TAG, "Error getting documents: ", task.getException());
                }
            }
        });
    }*/

    public void GetUserFavoriteClubThumbList(final String favorite, final CheckFirebaseComplete listener)
    {
        CollectionReference colRef = mDataBase.collection("ClubData_Favorite").document(favorite).collection("ClubIndex");
        colRef.orderBy("MemberCount", Query.Direction.DESCENDING).limit(1).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {

                        if (document.getData().containsKey("Thumb")) {
                            TKManager.getInstance().FavoriteLIst_ClubThumbList.put(favorite, document.getData().get("Thumb").toString());
                        }
                    }

                  Complete("즐겨찾기 클럽 썸데일 로드 와ㅓㄴ", listener);

                } else {
                    Log.d(TAG, "Error getting documents: ", task.getException());
                }
            }
        });
    }

    public void GetUserFavoriteClubList(final CheckFirebaseComplete listener)
    {
        SetFireBaseLoadingCount("관심사 클럽 로드", 0);
        CollectionReference colRef = mDataBase.collection("ClubData_Favorite");
        colRef.limit(50).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {

                    if(task.getResult().size() == 0)
                    {
                        if(listener != null)
                            listener.CompleteListener_No();
                    }

                    for (QueryDocumentSnapshot document : task.getResult()) {
                        SetFireBaseLoadingCount("관심사 클럽 로드", task.getResult().size());

                        TKManager.getInstance().FavoriteLIst_ClubList.add(document.getId().toString());
                        GetUserFavoriteClubThumbList(document.getId().toString(), listener);
                    }
/*
                    if(listener != null)
                    {
                        long seed = System.nanoTime();
                        Collections.shuffle( TKManager.getInstance().FavoriteLIst_ClubList, new Random(seed));

                        listener.CompleteListener();
                    }*/


                } else {
                    Log.d(TAG, "Error getting documents: ", task.getException());
                }
            }
        });
    }

    public void GetUserFavoriteClubData(final  String clubName, final CheckFirebaseComplete listener)
    {
        CollectionReference colRef = mDataBase.collection("ClubData_Favorite").document(clubName).collection("ClubIndex");
        colRef.orderBy("MemberCount", Query.Direction.DESCENDING).limit(10).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        TKManager.getInstance().FavoriteLIst_Club.add(document.getId().toString());
                    }

                } else {
                    Log.d(TAG, "Error getting documents: ", task.getException());
                }
            }
        });
    }

    private void GetUserRequestClubList(String userIndex,final  boolean mydata, final CheckFirebaseComplete listener) {
        CollectionReference colRef = mDataBase.collection("UserData").document(userIndex).collection("RequestClubList");
        //final int TodayDate = Integer.parseInt(CommonFunc.getInstance().GetCurrentDate());

        if(mydata)
        {
            colRef.orderBy("Date", Query.Direction.DESCENDING).limit(20).addSnapshotListener(new EventListener<QuerySnapshot>() {
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

                                ClubData tempData = new ClubData();
                                tempData.SetClubIndex(document.getDocument().getData().get("index").toString());
                                TKManager.getInstance().MyData.SetRequestJoinClubList(document.getDocument().getData().get("index").toString(), tempData);

                                break;
                            case REMOVED:
                                break;
                        }
                    }

                    if (TKManager.getInstance().isLoadDataByBoot == true && listener != null)
                        listener.CompleteListener();
                }
            });
        }
    }



    public void GetUserData(final String userIndex, final UserData userData, final CheckFirebaseComplete listener) {

        boolean bMyData = false;

        if(userIndex.equals(TKManager.getInstance().MyData.GetUserIndex()))
        {
            Log.d("!!!!!!!", "본인");
            bMyData = true;
            SetFireBaseLoadingCount("본인 로드", 9);
            //SetFireBaseLoadingCount(7);
        }
        else
        {
            Log.d("!!!!!!!", "타인");
            bMyData = false;
            userData.Clear();
            SetFireBaseLoadingCount("타인 로드", 5);
        }


        final DocumentReference docRef = mDataBase.collection("UserData").document(userIndex);
        final boolean finalMyData = bMyData;
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

                        if (document.getData().containsKey("Vip")) {
                            userData.SetUserVip(document.getData().get("Vip").toString());
                        } else
                            userData.SetUserVip("nVip");

                        if (document.getData().containsKey("Age")) {
                            userData.SetUserAge(Integer.parseInt(document.getData().get("Age").toString()));
                        } else
                            userData.SetUserAge(70);

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
                                Log.d("%%%%", "ChatRoom");
                                Complete("채팅데이터 로드 완", listener);
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

                        if(finalMyData)
                            GetUserChatList(userIndex, userData, ChatRoomListener);

                        /*FirebaseManager.CheckFirebaseComplete FavoriteRankListener = new FirebaseManager.CheckFirebaseComplete() {
                            @Override
                            public void CompleteListener() {
                                Log.d("%%%%", "FavoriteRankListener");
                                  Complete("관심사랭킹 로드 완",  listener);
                            }

                            @Override
                            public void CompleteListener_Yes() {
                            }

                            @Override
                            public void CompleteListener_No() {
                            }
                        };

                        if(finalMyData)
                            FindFavoriteRank(FavoriteRankListener);*/



                        FirebaseManager.CheckFirebaseComplete AlarmListener = new FirebaseManager.CheckFirebaseComplete() {
                            @Override
                            public void CompleteListener() {
                                Log.d("%%%%", "AlarmListener");
                                Complete("알람 로드 완", listener);
                            }

                            @Override
                            public void CompleteListener_Yes() {
                            }

                            @Override
                            public void CompleteListener_No() {
                            }
                        };

                        if(finalMyData)
                            MonitorAlarm(userIndex, AlarmListener);

                        FirebaseManager.CheckFirebaseComplete FilterListener = new FirebaseManager.CheckFirebaseComplete() {
                            @Override
                            public void CompleteListener() {
                                Log.d("%%%%", "FilterListener");
                                Complete("정렬 로드 완", listener);
                            }

                            @Override
                            public void CompleteListener_Yes() {
                            }

                            @Override
                            public void CompleteListener_No() {
                            }
                        };

                        if(finalMyData)
                            GetSortData(FilterListener);

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
                                Log.d("%%%%", "FavoriteUserListener");
                                Complete("관심사 로드 완", listener);
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
                                Log.d("%%%%", "LikeUserListener");
                                  Complete("좋아요 로드 완", listener);
                            }

                            @Override
                            public void CompleteListener_Yes() {
                            }

                            @Override
                            public void CompleteListener_No() {
                            }
                        };
                        GetUserLikeList(userIndex, userData, finalMyData, LikeUserListener);

                        FirebaseManager.CheckFirebaseComplete VisitUserListener = new FirebaseManager.CheckFirebaseComplete() {
                            @Override
                            public void CompleteListener() {
                                Log.d("%%%%", "VisitUserListener");
                                 Complete("방문자 로드 완", listener);
                            }

                            @Override
                            public void CompleteListener_Yes() {
                            }

                            @Override
                            public void CompleteListener_No() {
                            }
                        };
                        GetUserVisitList(userIndex, userData, finalMyData, VisitUserListener);

                        FirebaseManager.CheckFirebaseComplete ClubRequestUserListener = new FirebaseManager.CheckFirebaseComplete() {
                            @Override
                            public void CompleteListener() {
                                Log.d("%%%%", "ClubRequestUserListener");
                                  Complete("클럽가입신청 로드 완", listener);
                            }

                            @Override
                            public void CompleteListener_Yes() {
                            }

                            @Override
                            public void CompleteListener_No() {
                            }
                        };
                        if(finalMyData)
                            GetUserRequestClubList(userIndex, finalMyData,ClubRequestUserListener );




                        FirebaseManager.CheckFirebaseComplete ClubUserListener = new FirebaseManager.CheckFirebaseComplete() {
                            @Override
                            public void CompleteListener() {
                                Log.d("%%%%", "ClubUserListener");
                              Complete("클럽목록 로드 완", listener);
                            }

                            @Override
                            public void CompleteListener_Yes() {
                            }

                            @Override
                            public void CompleteListener_No() {
                            }
                        };
                        GetUserClubList(userIndex, userData, ClubUserListener);

                        FirebaseManager.CheckFirebaseComplete RecommendClubListener = new FirebaseManager.CheckFirebaseComplete() {
                            @Override
                            public void CompleteListener() {
                                Log.d("%%%%", "RecommendClubListener");
                                 Complete("추천클럽 로드 완", listener);
                            }

                            @Override
                            public void CompleteListener_Yes() {
                            }

                            @Override
                            public void CompleteListener_No() {
                            }
                        };
                        if(finalMyData)
                            RecommendClubList(RecommendClubListener);


                        if (document.getData().containsKey("Dist")) {
                            userData.SetUserDist(Integer.parseInt(document.getData().get("Dist").toString()));
                        } else
                            userData.SetUserDist(0);

                        if (document.getData().containsKey("Dist_Lon")) {
                            if(Double.parseDouble(document.getData().get("Dist_Lon").toString()) == 0)
                            {
                                userData.SetUserDist_Lon(126.978425);
                            }
                            else
                            {
                                userData.SetUserDist_Lon(Double.parseDouble(document.getData().get("Dist_Lon").toString()));
                            }
                        } else
                            userData.SetUserDist_Lon(126.978425);

                        if (document.getData().containsKey("Dist_Lat")) {
                            if(Double.parseDouble(document.getData().get("Dist_Lat").toString()) == 0)
                            {
                                userData.SetUserDist_Lat(37.566659);
                            }
                            else
                                userData.SetUserDist_Lat(Double.parseDouble(document.getData().get("Dist_Lat").toString()));

                        } else
                            userData.SetUserDist_Lat(37.566659);


                        if(finalMyData)
                            RegistUserGeoInfo();

                        if (document.getData().containsKey("Dist_Region")) {
                            userData.SetUserDist_Region(Double.parseDouble(document.getData().get("Dist_Region").toString()));
                        } else
                            userData.SetUserDist_Region(0);

                        if(finalMyData)
                        {
                            if (document.getData().containsKey("Dist_Area")) {
                                if( document.getData().get("Dist_Area") == null)
                                {
                                    userData.SetUserDist_Area("대한민국");
                                }
                                else
                                    userData.SetUserDist_Area(document.getData().get("Dist_Area").toString());
                            } else
                                userData.SetUserDist_Area("대한민국");
                        }


                        double Distance = CommonFunc.getInstance().DistanceByDegree(TKManager.getInstance().MyData.GetUserDist_Lat(), TKManager.MyData.GetUserDist_Lon(), userData.GetUserDist_Lat(), userData.GetUserDist_Lon());
                        userData.SetUserDist((long)Distance);

                        if (document.getData().containsKey("Location")) {
                            userData.SetUserLocation(document.getData().get("Location").toString());
                        } else
                        {
                            userData.SetUserLocation("");
                        }



                        Complete("마이데아터 로드 완", listener);

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

    public void GetDailyFavorite(final  FirebaseManager.CheckFirebaseComplete listener)
    {
        CollectionReference colRef = mDataBase.collection("DailyFavorite");
        colRef.orderBy("Index", Query.Direction.ASCENDING).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                          TKManager.getInstance().DailyFavorite.add(document.getId().toString());
                   }

                   if(listener != null)
                       listener.CompleteListener();

                } else {
                    Log.d(TAG, "Error getting documents: ", task.getException());
                }
            }
        });
    }

    public void UpdateUserArea()
    {
        Map<String, Object> tempData = new HashMap<>();
        tempData.put("Dist_Area", TKManager.getInstance().MyData.GetUserDist_Area());
        tempData.put("Dist_Lon", TKManager.getInstance().MyData.GetUserDist_Lon());
        tempData.put("Dist_Lat", TKManager.getInstance().MyData.GetUserDist_Lat());


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
    }

    public void GetUserList(final CheckFirebaseComplete listener) {
        SetFireBaseLoadingCount("유저목록 로드", 9);
        GetUserListDist( listener);

        GetReportUser(listener);
        Set EntrySet = TKManager.getInstance().MyData.GetUserFavoriteListKeySet();
        List array = new ArrayList(EntrySet);
        GetFavoriteList(true, array.get(0).toString(), listener);

        FindFavoriteRank(listener);

        GetUserListNew(true, listener);
        GetUserListFriend(true,listener);

        GetManagerNotice(listener);
        GetManagerEvent(listener);
        GetManagerFAQ(listener);
    }

    public void AddUserList(final  CheckFirebaseComplete listener) {
        GetUserListDist( listener);

        Set EntrySet = TKManager.getInstance().MyData.GetUserFavoriteListKeySet();
        List array = new ArrayList(EntrySet);
        GetFavoriteList(false, array.get(0).toString(), listener);

        GetUserListNew(false, listener);
        GetUserListFriend(false, listener);
    }

    public void GetUserListDist(final CheckFirebaseComplete listener) {

        CollectionReference collectionRef = FirebaseFirestore.getInstance().collection("UserData_Geo");
        Query limitQuery = collectionRef.limit(1);

        GeoFirestore geoFirestore = new GeoFirestore(collectionRef);
        GeoQuery geoQuery = geoFirestore.queryAtLocation(new GeoPoint(TKManager.getInstance().MyData.GetUserDist_Lat(), TKManager.getInstance().MyData.GetUserDist_Lon()), 500);

        geoQuery.addGeoQueryDataEventListener(new GeoQueryDataEventListener() {
            @Override
            public void onDocumentEntered(DocumentSnapshot documentSnapshot, GeoPoint location) {
                Log.d("DIST!!!", documentSnapshot.getId());


                if(!documentSnapshot.getId().equals(TKManager.getInstance().MyData.GetUserIndex()))
                {
                    AddFireBaseLoadingCount("유저목록 거리 로드");
                    TKManager.getInstance().UserList_Dist.add(documentSnapshot.getId().toString());


                    Log.d("DIST!!!", "GEO : " +documentSnapshot.getId() + " => " + documentSnapshot.getData());
//                    GetUserData_Simple(documentSnapshot.getId(), TKManager.getInstance().UserData_Simple, listener);
                }

            }

            @Override
            public void onDocumentExited(DocumentSnapshot documentSnapshot) {
                Log.d("DIST!!!", documentSnapshot.getId());
            }

            @Override
            public void onDocumentMoved(DocumentSnapshot documentSnapshot, GeoPoint location) {
                Log.d("##%%%%", documentSnapshot.getId());
            }

            @Override
            public void onDocumentChanged(DocumentSnapshot documentSnapshot, GeoPoint location) {
                Log.d("##%%%%", documentSnapshot.getId());
            }

            @Override
            public void onGeoQueryReady() {
                Log.d("DIST!!!", "All initial data has been loaded and events have been fired!");

                for(int i=0; i<TKManager.getInstance().UserList_Dist.size(); i++)
                {
                    GetUserData_Simple(TKManager.getInstance().UserList_Dist.get(i), TKManager.getInstance().UserData_Simple, listener);
                }
            }

            @Override
            public void onGeoQueryError(Exception exception) {
            }
        });

        Complete("거리유져 ㅐㅐ", listener);
       /* CollectionReference colRef = mDataBase.collection("UserList_Dist");

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
        });*/
    }


    public void GetFiftyInfo(final CheckFirebaseComplete listener) {

        final DocumentReference sfDocRef = mDataBase.collection("Info").document("context");
        sfDocRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d(TAG, "DocumentSnapshot data: " + document.getData());

                        if(CommonFunc.getInstance().CheckStringNull(document.getData().get("privacy").toString()) == false)
                        {

                        }
                        if(CommonFunc.getInstance().CheckStringNull(document.getData().get("refund").toString()) == false)
                        {

                        }
                        if(CommonFunc.getInstance().CheckStringNull(document.getData().get("service").toString()) == false)
                        {

                        }
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });
    }

    public void GetUserListHot(final CheckFirebaseComplete listener) {

            CollectionReference colRef = mDataBase.collection("UserData");
            colRef./*orderBy("Index", Query.Direction.DESCENDING).*/limit(CommonData.UserList_Loding_Count).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {

                            if(!document.getId().equals(TKManager.getInstance().MyData.GetUserIndex()))
                            {
                                TKManager.getInstance().UserList_Hot.add(document.getId().toString());

                                if(TKManager.getInstance().UserData_Simple.get(document.getId().toString()) == null)
                                {
                                    AddFireBaseLoadingCount("유저목록 추천 로드");
                                    Log.d(TAG, "HOT : " +document.getId() + " => " + document.getData());
                                    GetUserData_Simple(document.getId(), TKManager.getInstance().UserData_Simple, listener);
                                }
                            }
                        }


                    } else {
                        Log.d(TAG, "Error getting documents: ", task.getException());
                    }
                }
            });


/*
        Calendar cal = Calendar.getInstance();
        int nWeek = cal.get(Calendar.DAY_OF_WEEK);
      //  DAILY_FAVORITE = TKManager.getInstance().DailyFavorite.get(nWeek -1);

        Iterator<String> it = TKManager.getInstance().MyData.GetUserFavoriteListKeySet().iterator();

        while(it.hasNext()) {
            final String key = it.next();
            CollectionReference colRef = mDataBase.collection("FavoriteList").document(key).collection("UserIndex");
            colRef.orderBy("Index", Query.Direction.DESCENDING).limit(CommonData.UserList_Loding_Count).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {

                            if(!document.getId().equals(TKManager.getInstance().MyData.GetUserIndex()))
                            {
                                TKManager.getInstance().UserList_Hot.put(document.getId().toString(), key);

                                if(TKManager.getInstance().UserData_Simple.get(document.getId().toString()) == null)
                                {
                                    AddFireBaseLoadingCount();
                                    Log.d(TAG, "HOT : " +document.getId() + " => " + document.getData());
                                    GetUserData_Simple(document.getId(), TKManager.getInstance().UserData_Simple, listener);
                                }
                            }
                        }


                    } else {
                        Log.d(TAG, "Error getting documents: ", task.getException());
                    }
                }
            });
        }*/



    }

    public void GetUserListNew(boolean firstLoad, final CheckFirebaseComplete listener) {

        final DocumentSnapshot[] lastVisible = {null};
        Query first = null;
        if(firstLoad)
        {
            first = mDataBase.collection("UserList_New")
                    .orderBy("value", Query.Direction.DESCENDING)
                    .limit(CommonData.UserList_First_Loding_Count);
        }
        else
        {
            if(lastNewUserVisible == null)
            {
                first = mDataBase.collection("UserList_New")
                        .orderBy("value", Query.Direction.DESCENDING)
                        .limit(CommonData.UserList_Loding_Count);
            }
            else
            {
                first = mDataBase.collection("UserList_New")
                        .orderBy("value", Query.Direction.DESCENDING)
                        .startAfter(lastNewUserVisible)
                        .limit(CommonData.UserList_Loding_Count);
            }
        }

        first.get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot documentSnapshots) {
                        // ...
                        AddFireBaseLoadingCount("유저목록 신규 로드" , documentSnapshots.size());

                        for (DocumentSnapshot document : documentSnapshots.getDocuments()) {

                            if(!document.getId().equals(TKManager.getInstance().MyData.GetUserIndex()))
                            {
                                TKManager.getInstance().UserList_New.add(document.getId().toString());


                                if(TKManager.getInstance().UserData_Simple.get(document.getId().toString()) == null)
                                {

                                    Log.d(TAG, "NEW : " +document.getId() + " => " + document.getData());
                                    GetUserData_Simple(document.getId(), TKManager.getInstance().UserData_Simple, listener);
                                }
                                else
                                    Complete("유저목록 신규 완" , listener);
                            }
                            else
                                Complete("유저목록 신규 완", listener);

                        }

                        if(documentSnapshots.size() > 0)
                        {
                            // Get the last visible document
                            lastNewUserVisible = documentSnapshots.getDocuments()
                                    .get(documentSnapshots.size() -1);
                        }
                        Complete("신규 ㅐㅐ", listener);

                        Log.d("##@@!", "NEW : " +lastNewUserVisible);
                        // Use the query for pagination
                        // ...
                    }
                });


       /*
       CollectionReference colRef = mDataBase.collection("UserList_New");
        first.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    AddFireBaseLoadingCount(task.getResult().size());
                    for (QueryDocumentSnapshot document : task.getResult()) {

                        if(!document.getId().equals(TKManager.getInstance().MyData.GetUserIndex()))
                        {
                            TKManager.getInstance().UserList_New.add(document.getId().toString());


                            if(TKManager.getInstance().UserData_Simple.get(document.getId().toString()) == null)
                            {

                                Log.d(TAG, "NEW : " +document.getId() + " => " + document.getData());
                                GetUserData_Simple(document.getId(), TKManager.getInstance().UserData_Simple, listener);
                            }
                            else
                                Complete(listener);
                        }
                        else
                            Complete(listener);


                        lastVisible = document.getDo
                                .get(task.getResult().size() -1);
                    }




                } else {
                    Log.d(TAG, "Error getting documents: ", task.getException());
                }
            }
        });*/
    }

    public void GetUserListFriend(boolean firstLoad, final CheckFirebaseComplete listener) {
        //CollectionReference colRef = mDataBase.collection("UserList_Friend");

        final DocumentSnapshot[] lastVisible = {null};
        Query first = null;
        if(firstLoad)
        {
            first =  mDataBase.collection("UserData").document(TKManager.getInstance().MyData.GetUserIndex()).collection("FriendUsers")
                    .orderBy("Date", Query.Direction.DESCENDING)
                    .limit(CommonData.UserList_First_Loding_Count);
        }
        else
        {
            if(lastFriendUserVisible == null)
            {
                first =  mDataBase.collection("UserData").document(TKManager.getInstance().MyData.GetUserIndex()).collection("FriendUsers")
                        .orderBy("Date", Query.Direction.DESCENDING)
                        .limit(CommonData.UserList_Loding_Count);
            }
            else
            {
                first = mDataBase.collection("UserData").document(TKManager.getInstance().MyData.GetUserIndex()).collection("FriendUsers")
                        .orderBy("Date", Query.Direction.DESCENDING)
                        .startAfter(lastFriendUserVisible)
                        .limit(CommonData.UserList_Loding_Count);
            }

    }

        first.get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot documentSnapshots) {
                        // ...
                        AddFireBaseLoadingCount("유저목록 친구",  documentSnapshots.size());

                        for (DocumentSnapshot document : documentSnapshots.getDocuments()) {

                            if(!document.getId().equals(TKManager.getInstance().MyData.GetUserIndex()))
                            {
                                TKManager.getInstance().MyData.SetUserFriend(document.getId().toString(), document.getId().toString());

                                if(TKManager.getInstance().UserData_Simple.get(document.getId().toString()) == null)
                                {
                                    Log.d(TAG, "FR : " +document.getId() + " => " + document.getData());
                                    GetUserData_Simple(document.getId(), TKManager.getInstance().UserData_Simple, listener);
                                }
                                else
                                    Complete("유저목록 친구 ㅇㅇ ",listener);
                            }
                            else
                                Complete("유저목록 친구 ㅇㅇ", listener);

                        }

                        if(documentSnapshots.size() > 0)
                        {
                            lastFriendUserVisible = documentSnapshots.getDocuments()
                                    .get(documentSnapshots.size() -1);
                        }

                        Complete("친구 dd ", listener);

                        // Get the last visible document


                        Log.d("##@@!", "NEW : " +lastFriendUserVisible);
                        // Use the query for pagination
                        // ...
                    }
                });


       /* CollectionReference colRef = mDataBase.collection("UserData").document(TKManager.getInstance().MyData.GetUserIndex()).collection("FriendUsers");
        colRef.orderBy("Date", Query.Direction.DESCENDING).limit(CommonData.UserList_Loding_Count).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {

                    AddFireBaseLoadingCount(task.getResult().size());
                    for (QueryDocumentSnapshot document : task.getResult()) {

                        if(!document.getId().equals(TKManager.getInstance().MyData.GetUserIndex()))
                        {
                            TKManager.getInstance().MyData.SetUserFriend(document.getId().toString(), document.getId().toString());

                            if(TKManager.getInstance().UserData_Simple.get(document.getId().toString()) == null)
                            {

                                Log.d(TAG, "FR : " +document.getId() + " => " + document.getData());
                                GetUserData_Simple(document.getId(), TKManager.getInstance().UserData_Simple, listener);
                            }
                            else
                                Complete(listener);
                        }
                        else
                            Complete(listener);
                    }

                } else {
                    Log.d(TAG, "Error getting documents: ", task.getException());
                }
            }
        });*/
    }

    public void RegistSearchResult(String searchName)
    {

        DocumentReference docRef = mDataBase.collection("SearchList").document(searchName);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d(TAG, "DocumentSnapshot data: " + document.getData());

                        final double[] newPopulation = new double[1];
                        mDataBase.runTransaction(new Transaction.Function<Void>() {
                            @Override
                            public Void apply(Transaction transaction) throws FirebaseFirestoreException {
                                DocumentSnapshot snapshot = transaction.get(docRef);
                                newPopulation[0] = snapshot.getDouble("count") + 1;
                                transaction.update(docRef, "count", newPopulation[0]);
                                int tempIndex = (int)newPopulation[0];

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
                        Log.d(TAG, "No such document");

                        Map<String, Object> popFavorite = new HashMap<>();
                        popFavorite.put("count", 1);

                        docRef
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

    }

    public void FindUserByNickName(final String userNick, final  Activity activity, final FirebaseManager.CheckFirebaseComplete listener) {
        DialogFunc.getInstance().ShowLoadingPage(activity);
        DocumentReference docRef = mDataBase.collection("UserData_NickName").document(userNick);

        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d(TAG, "DocumentSnapshot data: " + document.getData());


                        String Index = document.getData().get(userNick).toString();
                        //GetUserData(Index, TKManager.getInstance().TargetUserData, listener);
                        CommonFunc.getInstance().GetUserDataInFireBase(Index, activity, true);

                    } else {
                        Log.d(TAG, "No such document");
                        DialogFunc.getInstance().DismissLoadingPage();
                        //TKManager.getInstance().MyData.SetUserNickName(nickName);
                        if (listener != null)
                            listener.CompleteListener_No();
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });
    }

    public void GetUserData_Simple(final String userIndex, final Map<String, UserData> getData, final FirebaseManager.CheckFirebaseComplete listener) {

        if(TKManager.getInstance().UserData_Simple.get(userIndex) != null)
        {
            getData.put(userIndex, TKManager.getInstance().UserData_Simple.get(userIndex));

            CommonFunc.getInstance().tLog("!!!!!!!", "심플데이터 있음 " + userIndex);

            Complete("심플데이터 있음", listener);
        }

        else
        {
            final DocumentReference docRef = mDataBase.collection("UserData_Simple").document(userIndex);
            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            Log.d(TAG, "GetUserData_Simple DocumentSnapshot data: " + document.getData());

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

                            if (document.getData().containsKey("ConnectDate")) {
                                tempUser.SetUserConnectDate(Long.parseLong(document.getData().get("ConnectDate").toString()));
                            } else
                                tempUser.SetUserConnectDate(Long.parseLong(CommonFunc.getInstance().GetCurrentTime()));

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
                                tempUser.SetUserAge(70);

                            if (document.getData().containsKey("Gender")) {
                                int Gender = Integer.parseInt(document.getData().get("Gender").toString());
                                tempUser.SetUserGender(Gender);
                            } else
                                tempUser.SetUserGender(0);

                            getData.put(userIndex, tempUser);

                            //AddFireBaseLoadingCount();
                            Log.d("!!!!!!!", "심플데이터  " + userIndex);
                            Complete("심플데이터 ㄹㄷ ㅇ",listener);

                        } else {
                            Log.d(TAG, "No such document");
                            Log.d("!!!!!!!", "심플데이터 없 " + userIndex);
                            Complete("심플데이터 업ㅋ음",listener);
                        }
                    } else {
                        Log.d(TAG, "get failed with ", task.getException());
                    }
                }
            });
        }

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
                                Log.d("!!!!!!!" ," 팝 고고");
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

    public void RecommendClubList(final FirebaseManager.CheckFirebaseComplete listener) {
        CollectionReference colRef = mDataBase.collection("ClubData");
        colRef.orderBy("ClubMemberCount", Query.Direction.DESCENDING).limit(CommonData.ClubList_First_Loding_Count).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Log.d(TAG, document.getId() + " => " + document.getData());
                        //TKManager.getInstance().FavoriteLIst_Pop.add(document.getId().toString());

               //        AddFireBaseLoadingCount();
                        ClubData tempClubData = new ClubData();
                        tempClubData.SetClubIndex(document.getId());
                        TKManager.getInstance().MyData.SetUserRecommendClubData(document.getId(), tempClubData);
                        //GetClubData_Simple(document.getId(), TKManager.getInstance().MyData.RecommendClubData, listener);
                    }

                    if(listener != null)
                        listener.CompleteListener();

                } else {
                    Log.d(TAG, "Error getting documents: ", task.getException());
                }
            }
        });
    }


    public void SearchClubList(String name, final FirebaseManager.CheckFirebaseComplete listener) {
        TKManager.getInstance().SearchClubList.clear();
        SetFireBaseLoadingCount("심플데이터 있음", 0);
        CollectionReference colRef = mDataBase.collection("ClubData");
        colRef.whereEqualTo("ClubName", name).limit(20).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                if (task.isSuccessful()) {

                    if(task.getResult().size() == 0)
                    {
                        if (listener != null)
                            listener.CompleteListener_No();
                    }

                    for (QueryDocumentSnapshot document : task.getResult()) {
                        if (document.exists()) {
                            Log.d(TAG, document.getId() + " => " + document.getData());
                            //TKManager.getInstance().FavoriteLIst_Pop.add(document.getId().toString());

                            AddFireBaseLoadingCount("클럽찾기 ㅇ");
                            TKManager.getInstance().SearchClubList.add(document.getId());

                            GetClubData_Simple(document.getId(), TKManager.getInstance().ClubData_Simple, listener);
                        }

                    }

                    /*if (listener != null)
                        listener.CompleteListener();*/

                } else {
                    Log.d(TAG, "Error getting documents: ", task.getException());
                }
            }
        });
    }

    public void SearchClubListOnFavorite(String name, final FirebaseManager.CheckFirebaseComplete listener) {
        TKManager.getInstance().SearchClubList.clear();
        SetFireBaseLoadingCount("관심사클럽리스트 로드", 0);
        CollectionReference colRef = mDataBase.collection("ClubData_Favorite").document(name).collection("ClubIndex");
        colRef./*orderBy("Count", Query.Direction.DESCENDING).limit(CommonData.Favorite_Search_Pop_Count).*/get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {

                    if(task.getResult().size() == 0)
                    {
                        if(listener != null)
                            listener.CompleteListener_No();
                    }
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Log.d(TAG, document.getId() + " => " + document.getData());
                        //TKManager.getInstance().FavoriteLIst_Pop.add(document.getId().toString());
                        AddFireBaseLoadingCount("관심사클럽리스트 로드");
                        TKManager.getInstance().SearchClubList.add(document.getId());

                        GetClubData_Simple(document.getId(), TKManager.getInstance().ClubData_Simple, listener);
                    }

                    /*if (listener != null)
                        listener.CompleteListener();*/

                } else {
                    Log.d(TAG, "Error getting documents: ", task.getException());
                }
            }
        });
    }

    public void InviteUsersInClub(String favorite, final FirebaseManager.CheckFirebaseComplete listener) {
        TKManager.getInstance().UserList_Invite_Club.clear();
        SetFireBaseLoadingCount("클럽 초대유져 로드", 0);
        CollectionReference colRef = mDataBase.collection("FavoriteList").document(favorite).collection("UserIndex");
        colRef.limit(CommonData.UserList_First_Loding_Count)./*orderBy("Count", Query.Direction.DESCENDING).limit(CommonData.Favorite_Search_Pop_Count).*/get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {


                    if(task.getResult().size() == 0)
                    {
                        if(listener != null)
                            listener.CompleteListener_No();
                    }

                    AddFireBaseLoadingCount("클럽 초대유져 로드 시작", task.getResult().size());

                    for (QueryDocumentSnapshot document : task.getResult()) {

                        //TKManager.getInstance().FavoriteLIst_Pop.add(document.getId().toString());

                        Log.d(TAG, document.getId() + " => " + task.getResult().size());
                        if(document.getId().equals(TKManager.getInstance().MyData.GetUserIndex()))
                        {
                            Complete("클럽 초대유져 로드 본인", listener);
                        }
                        else
                        {
                            TKManager.getInstance().UserList_Invite_Club.put(document.getId(),document.getData().toString());
                            GetUserData_Simple(document.getId(), TKManager.getInstance().UserData_Simple, listener);
                        }
                    }

                    /*if (listener != null)
                        listener.CompleteListener();*/

                } else {
                    Log.d(TAG, "Error getting documents: ", task.getException());
                }
            }
        });
    }

    public void GetPopFavoriteData(final FirebaseManager.CheckFirebaseComplete listener) {
        CollectionReference colRef = mDataBase.collection("PopFavorite");
        colRef/*.orderBy("count", Query.Direction.DESCENDING).limit(CommonData.Favorite_Search_Pop_Count)*/.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Log.d(TAG, document.getId() + " => " + document.getData());
                        TKManager.getInstance().FavoriteLIst_Pop.add(document.getId().toString());
                    }

                    long seed = System.nanoTime();
                    Collections.shuffle(TKManager.getInstance().FavoriteLIst_Pop, new Random(seed));

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
        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, baos);
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

    public void UploadImgInChatRoom(String roomIndex, Bitmap bitmap, final FirebaseManager.CheckFirebaseComplete listener) {
        long seed = System.currentTimeMillis();
        Random rand = new Random(seed);

        String chatIndex = Integer.toString(rand.nextInt());

        final StorageReference tempThumbnailRef = mStorageRef.child("ChatData/" + roomIndex + "Images/" + chatIndex);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, baos);
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

    public void UploadFileInChatRoom(String roomIndex, Uri file, final FirebaseManager.CheckFirebaseComplete listener) {
        long seed = System.currentTimeMillis();
        Random rand = new Random(seed);

        String chatIndex = Integer.toString(rand.nextInt());

       // final StorageReference tempThumbnailRef = mStorageRef.child("ChatData/" + roomIndex + "/" + chatIndex + "/file");

        Uri UploadFile = file;
        StorageReference FileRef = mStorageRef.child("ChatData/" + roomIndex + "Files/"+UploadFile.getLastPathSegment());
        UploadTask uploadTask = FileRef.putFile(UploadFile);

        Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()) {
                    throw task.getException();
                }

                // Continue with the task to get the download URL
                return FileRef.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    Uri downloadUri = task.getResult();
                    TKManager.getInstance().MyData.SetUserFileChat(downloadUri.toString());
                    if (listener != null)
                        listener.CompleteListener();

                } else {
                    // Handle failures
                    // ...
                }
            }
        });

    }

  /*  public void UploadImgInChatRoom(String roomIndex, String chatIndex, Bitmap bitmap, final FirebaseManager.CheckFirebaseComplete listener) {
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


    }*/


    public void UploadClubThumbImg(String clubIndex, Bitmap bitmap, final ClubData clubData, final FirebaseManager.CheckFirebaseComplete listener) {
        final StorageReference tempThumbnailRef = mStorageRef.child("ClubImage/" + clubIndex + "/Thumb.jpg");
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
                    clubData.SetClubThumb(downloadUri.toString());
                    if (listener != null)
                        listener.CompleteListener();
                } else {
                    // Handle failures
                    // ...
                }
            }
        });
    }

    public void UploadClubContextImg(ClubData clubIndex, Bitmap bitmap, final String ImgIndex, final ClubContextData clubContextData, final FirebaseManager.CheckFirebaseComplete listener) {
        final StorageReference tempThumbnailRef = mStorageRef.child("ClubImage/" + clubIndex.GetClubIndex() + "/" + clubIndex.GetClubContextCount() + "/" + ImgIndex + "/Img.jpg");
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
                    clubContextData.SetImg(ImgIndex, downloadUri.toString());

                    Complete("클럽 본문 이미지 업로드 ㅇㅇ", listener);

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


    public void AddChatData(final String roomIndex, final String targetIndex, final CommonData.CHAT_ROOM_TYPE type, final  Context context, final ChatData chatData) {

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
                                CommonFunc.getInstance().SendMSGToFCM(TKManager.getInstance().UserData_Simple.get(targetIndex).GetUserToken(), chatData.Msg);
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w(TAG, "Error writing document", e);
                            }
                        });

              /*  Map<String, Object> tempTargetChatData = new HashMap<>();
                tempTargetChatData.put("FromIndex", TKManager.getInstance().MyData.GetUserIndex());
                tempTargetChatData.put("FromNickName", TKManager.getInstance().MyData.GetUserNickName());
                tempTargetChatData.put("Msg", chatData.GetMsg());
                tempTargetChatData.put("MsgDate", chatData.GetMsgDate());
                tempTargetChatData.put("MsgReadCheck", chatData.GetMsgReadCheck());
                tempTargetChatData.put("MsgSender", chatData.GetMsgSender());
                tempTargetChatData.put("MsgType", chatData.GetMsgType());
                tempTargetChatData.put("MsgIndex", chatData.GetMsgIndex());
                tempTargetChatData.put("RoomIndex", chatData.GetRoomIndex());
                tempTargetChatData.put("ToIndex", targetIndex);
                tempTargetChatData.put("ToNickName", chatData.GetToNickName());

                mDataBase.collection("UserData").document(targetIndex).collection("ChatRoomList").document(roomIndex)
                        .set(tempTargetChatData, SetOptions.merge())
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

                Map<String, Object> tempMyChatData = new HashMap<>();
                tempMyChatData.put("FromIndex", TKManager.getInstance().MyData.GetUserIndex());
                tempMyChatData.put("FromNickName", TKManager.getInstance().MyData.GetUserNickName());
                tempMyChatData.put("Msg", chatData.GetMsg());
                tempMyChatData.put("MsgDate", chatData.GetMsgDate());
                tempMyChatData.put("MsgReadCheck", chatData.GetMsgReadCheck());
                tempMyChatData.put("MsgSender", chatData.GetMsgSender());
                tempMyChatData.put("MsgType", chatData.GetMsgType());
                tempMyChatData.put("File", chatData.GetFile());
                tempMyChatData.put("MsgIndex", chatData.GetMsgIndex());
                tempMyChatData.put("RoomIndex", chatData.GetRoomIndex());
                tempMyChatData.put("ToIndex", targetIndex);
                tempMyChatData.put("ToNickName", chatData.GetToNickName());
                tempMyChatData.put("RoomType", type);

 /*
                HashMap<String, Long> ReadIndex = new HashMap<String, Long>();
                ReadIndex.put(chatData.GetRoomIndex(), chatData.GetMsgIndex());

                //convert to string using gson
               Gson gson = new Gson();
                String hashMapString = gson.toJson(ReadIndex);

                SharedPreferences prefs = context.getSharedPreferences("userFile", context.MODE_PRIVATE);
                prefs.edit().putString("SaveReadChatIndex", hashMapString).apply();*/

                mDataBase.collection("UserData").document(TKManager.getInstance().MyData.GetUserIndex()).collection("ChatRoomList").document(roomIndex)
                        .set(tempMyChatData, SetOptions.merge())
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

    public void AddClubChatData(final String roomIndex, final ChatData chatData) {

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
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Transaction failure.", e);
                    }
                });
    }

    public void RegistClubChatList(String clubIndex, ChatData chatData, boolean master) {
        String userIndex = TKManager.getInstance().MyData.GetUserIndex();
        String ChatRoomIndex = clubIndex;

        Map<String, Object> tempData = new HashMap<>();
        tempData.put("RoomIndex", ChatRoomIndex);
        tempData.put("RoomType", "DEFAULT");

/*        mDataBase.collection("UserData").document(userIndex).collection("ChatRoomList").document(ChatRoomIndex)
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

        if(master)
        {
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
    }

    public void RegistChatList(String targetIndex, ChatData data) {
        String userIndex = TKManager.getInstance().MyData.GetUserIndex();
        String ChatRoomIndex = userIndex + "_" + targetIndex;

        Map<String, Object> tempData = new HashMap<>();
        tempData.put("RoomIndex", data.GetRoomIndex());
        tempData.put("RoomType", "DEFAULT");
        tempData.put("MsgIndex", 0);

        CommonFunc.getInstance().SendMSGToFCM(TKManager.getInstance().UserData_Simple.get(targetIndex).GetUserToken(), data.Msg);

        mDataBase.collection("UserData").document(userIndex).collection("ChatRoomList").document(ChatRoomIndex)
                .set(data, SetOptions.merge())
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
                .set(data, SetOptions.merge())
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

        Map<String, Object> tempUserList= new HashMap<>();
        tempUserList.put(TKManager.getInstance().MyData.GetUserIndex(), TKManager.getInstance().MyData.GetUserIndex());
        tempUserList.put(targetIndex, targetIndex);

        Map<String, Object> tempUser= new HashMap<>();
        tempUser.put("UserList", tempUserList);

        mDataBase.collection("ChatRoomData").document(ChatRoomIndex)
                .set(tempUser, SetOptions.merge())
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

    public void RegistClubReply(final ClubData clubData, final ClubContextData data, final int Index, final FirebaseManager.CheckFirebaseComplete listener) {
        final DocumentReference sfDocRef = mDataBase.collection("ClubData").document(clubData.GetClubIndex()).collection("ClubContext")
                .document(data.GetContextIndex());

        Map<String, Object> Reply = new HashMap<>();
        Reply.put(Integer.toString(Index), data.GetReply(Integer.toString(Index)));

        Map<String, Object> ReplyData = new HashMap<>();
        ReplyData.put("ReplyList", Reply);

        sfDocRef.set(ReplyData, SetOptions.merge())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        listener.CompleteListener();
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

    public void RemoveReportContext(final String clubIndex, final String dataIndex, final FirebaseManager.CheckFirebaseComplete listener) {
        mDataBase.collection("ClubData").document(clubIndex).collection("ReportContextList").document(dataIndex).delete()
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
                        if(listener != null)
                            listener.CompleteListener_No();
                    }
                });
    }


    public void ChangeChatRoomType(final String chatIndex, final  CommonData.CHAT_ROOM_TYPE type, final FirebaseManager.CheckFirebaseComplete listener) {
        Map<String, Object> RoomType = new HashMap<>();
        RoomType.put("RoomType", type);

        mDataBase.collection("UserData").document(TKManager.getInstance().MyData.GetUserIndex()).collection("ChatRoomList").document(chatIndex)
                .set(RoomType, SetOptions.merge())
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
                        if(listener != null)
                            listener.CompleteListener_No();
                    }
                });
    }

    public void RemoveChatList(final String chatIndex, final FirebaseManager.CheckFirebaseComplete listener) {
        mDataBase.collection("UserData").document(TKManager.getInstance().MyData.GetUserIndex()).collection("ChatRoomList").document(chatIndex).delete()
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
                        if(listener != null)
                            listener.CompleteListener_No();
                    }
                });
    }


    public void RemoveClubContext(final String clubIndex, final String dataIndex, final FirebaseManager.CheckFirebaseComplete listener) {
         mDataBase.collection("ClubData").document(clubIndex).collection("ClubContext").document(dataIndex).delete()
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
                         if(listener != null)
                             listener.CompleteListener_No();
                     }
                 });
    }

    public void RegistClubReport(final String clubIndex, final ClubContextData dataIndex, final FirebaseManager.CheckFirebaseComplete listener) {
        final DocumentReference sfDocRef = mDataBase.collection("ClubData").document(clubIndex).collection("ReportContextList").document(dataIndex.GetContextIndex());

        Map<String, Object> ReportContext = new HashMap<>();
        ReportContext.put(TKManager.getInstance().MyData.GetUserIndex(), TKManager.getInstance().MyData.GetUserIndex());
        ReportContext.put("Date", dataIndex.GetDate());

        sfDocRef.set(ReportContext, SetOptions.merge())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                        final DocumentReference sfDocReportRef = mDataBase.collection("ClubData").document(clubIndex).collection("ClubContext").document(dataIndex.GetContextIndex());

                        Map<String, Object> ReportInfo = new HashMap<>();
                        ReportInfo.put(TKManager.getInstance().MyData.GetUserIndex(), TKManager.getInstance().MyData.GetUserIndex());

                        Map<String, Object> ReportInfoList = new HashMap<>();
                        ReportInfoList.put("ReportList", ReportInfo);

                        sfDocReportRef.set(ReportInfoList, SetOptions.merge())
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        if(listener != null)
                                            listener.CompleteListener();

                                        Log.d(TAG, "DocumentSnapshot successfully written!");
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.w(TAG, "Error writing document", e);
                                    }
                                });

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


    public void RemoveReportUser(final String userIndex, final FirebaseManager.CheckFirebaseComplete listener) {

        mDataBase.collection("UserData").document(TKManager.getInstance().MyData.GetUserIndex())
                .collection("ReportUsers").document(userIndex).delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot successfully deleted!");

                        TKManager.getInstance().MyData.DelReportUserList(userIndex.toString());

                        if(listener != null)
                            listener.CompleteListener();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error deleting document", e);
                        if(listener != null)
                            listener.CompleteListener_No();
                    }
                });
    }


    public  void GetReportUser( final FirebaseManager.CheckFirebaseComplete listener) {
        TKManager.getInstance().MyData.ReportUserList.clear();

        final CollectionReference cocRef = mDataBase.collection("UserData").document(TKManager.getInstance().MyData.GetUserIndex())
                .collection("ReportUsers");

        cocRef.limit(20).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                int tempTotayLikeCount = 0;
                if (task.isSuccessful()) {
                    if(task.getResult().size() == 0)
                    {
                        Complete("신고유져  ㅇㅇ", listener);
                      /*  if(listener != null)
                            listener.CompleteListener();*/
                    }
                    else
                    {

//                            SetFireBaseLoadingCount(task.getResult().size());

                            for (QueryDocumentSnapshot document : task.getResult()) {
                                AddFireBaseLoadingCount("사용자 신고" );
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                TKManager.getInstance().MyData.SetReportUserList(document.getId().toString(), TKManager.getInstance().UserData_Simple.get(document.getId().toString()) );
                                GetUserData_Simple(document.getId().toString(), TKManager.getInstance().UserData_Simple, listener);
                            }

                    }

                    Complete("신고유져 ㅐㅐ", listener);

                } else {
                    Log.d(TAG, "Error getting documents: ", task.getException());
                }
            }
        });
    }

    public void RegistReportUser(final String userIndex,  final FirebaseManager.CheckFirebaseComplete listener) {
        final DocumentReference sfDocRef = mDataBase.collection("UserData").document(TKManager.getInstance().MyData.GetUserIndex())
                .collection("ReportUsers").document(userIndex);

        Map<String, Object> ReportContext = new HashMap<>();
        ReportContext.put("Index", userIndex);

        sfDocRef.set(ReportContext, SetOptions.merge())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                        TKManager.getInstance().MyData.SetReportUserList(userIndex, TKManager.getInstance().UserData_Simple.get(userIndex));

                        if(listener != null)
                            listener.CompleteListener();
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

    public void EditClubContext(final String clubIndex, final ClubContextData data, final FirebaseManager.CheckFirebaseComplete listener) {
        final DocumentReference sfDocRef = mDataBase.collection("ClubData").document(clubIndex);

        mDataBase.collection("ClubData").document(clubIndex).collection("ClubContext").document(data.GetContextIndex())
                .set(data, SetOptions.merge())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        listener.CompleteListener();
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
    public void RegistClubContext(final String clubIndex, final ClubContextData data, final FirebaseManager.CheckFirebaseComplete listener) {
        final DocumentReference sfDocRef = mDataBase.collection("ClubData").document(clubIndex);
        final double[] newPopulation = new double[1];
        mDataBase.runTransaction(new Transaction.Function<Void>() {
            @Override
            public Void apply(Transaction transaction) throws FirebaseFirestoreException {
                DocumentSnapshot snapshot = transaction.get(sfDocRef);
                newPopulation[0] = snapshot.getDouble("ClubContextCount") + 1;
                int tempIndex = (int)newPopulation[0];
                transaction.update(sfDocRef, "ClubContextCount", tempIndex);

                // Success
                return null;
            }
        }).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d(TAG, "Transaction success!");
                if (listener != null) {

                    Map<String, Object> Context = new HashMap<>();
                    Context.put(Integer.toString((int)newPopulation[0]), data);
                    data.SetContextIndex(Integer.toString((int)newPopulation[0]));

                    mDataBase.collection("ClubData").document(clubIndex).collection("ClubContext").document(Integer.toString((int)newPopulation[0]))
                            .set(data, SetOptions.merge())
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    listener.CompleteListener();
                                    Log.d(TAG, "DocumentSnapshot successfully written!");
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.w(TAG, "Error writing document", e);
                                }
                            });

                    Map<String, Object> ContextData = new HashMap<>();
                    ContextData.put("ContextCount", (int)newPopulation[0]);

                    mDataBase.collection("ClubData_Simple").document(clubIndex)
                            .set(ContextData, SetOptions.merge())
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
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Transaction failure.", e);
                    }
                });
    }



    public void GetClubContextData(String clubIndex, final String contextIndex, final CheckFirebaseComplete listener) {
        final DocumentReference docRef = mDataBase.collection("ClubData").document(clubIndex).collection("ClubContext").document(contextIndex);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d(TAG, "DocumentSnapshot data: " + document.getData());

                        ClubContextData tempData = new ClubContextData();
                        tempData.ContextType = Integer.parseInt(document.getData().get("ContextType").toString());

                        if (document.getData().containsKey("Context")) {
                            if(CommonFunc.getInstance().isEmpty(document.getData().get("Context")))
                            {
                                tempData.Context = "";
                            }
                            else
                                tempData.Context = document.getData().get("Context").toString();
                        } else
                            tempData.Context = "";

                        if (document.getData().containsKey("ContextIndex")) {
                            if(CommonFunc.getInstance().isEmpty(document.getData().get("ContextIndex")))
                            {
                                tempData.SetContextIndex(document.getId());
                            }
                            else
                                tempData.SetContextIndex(document.getData().get("ContextIndex").toString());
                        } else
                            tempData.SetContextIndex(document.getId());


                        tempData.Date = document.getData().get("Date").toString();
                        tempData.writerIndex = document.getData().get("writerIndex").toString();

                        if (document.getData().containsKey("ImgList")) {
                            tempData.ClearImg();
                            HashMap<String, String> tempImg = (HashMap<String, String>) document.getData().get("ImgList");
                            Set set = tempImg.entrySet();
                            Iterator iterator = set.iterator();
                            while (iterator.hasNext()) {
                                Map.Entry entry = (Map.Entry) iterator.next();
                                String key = (String) entry.getKey();
                                String value = (String) entry.getValue();
                                tempData.SetImg(key, value);
                            }
                        }

                        if (document.getData().containsKey("ReportList")) {
                            tempData.ClearReportList();
                            HashMap<String, String> tempImg = (HashMap<String, String>) document.getData().get("ReportList");
                            Set set = tempImg.entrySet();
                            Iterator iterator = set.iterator();
                            while (iterator.hasNext()) {
                                Map.Entry entry = (Map.Entry) iterator.next();
                                String key = (String) entry.getKey();
                                String value = (String) entry.getValue();
                                tempData.SetReportList(key, value);
                            }
                        }

                        if (document.getData().containsKey("ReplyList")) {
                            tempData.ClearReplyData();
                            HashMap<String, String> tempImg = (HashMap<String, String>) document.getData().get("ReplyList");
                            Set set = tempImg.entrySet();
                            Iterator iterator = set.iterator();
                            while (iterator.hasNext()) {
                                Map.Entry entry = (Map.Entry) iterator.next();
                                String key = (String) entry.getKey();
                                String value = (String) entry.getValue();
                                tempData.SetReply(key, value);



                                String[] array = value.split("_");
                                ClubContextData tempReplyData= new ClubContextData();
                                tempReplyData.SetWriterIndex(array[0]);
                                tempReplyData.SetDate(array[1]);
                                tempReplyData.SetContext(array[2]);
                                tempData.SetReplyData(key, tempReplyData);

                                AddFireBaseLoadingCount("클럽 본문 로드");
                                GetUserData_Simple(tempReplyData.GetWriterIndex(), TKManager.getInstance().UserData_Simple, listener);

                            }
                        }

                        TKManager.getInstance().TargetReportContextData.put(contextIndex, tempData);

                        // if(TKManager.getInstance().UserData_Simple.get(tempData.writerIndex) == null)
                        {
                            AddFireBaseLoadingCount("클럽 신고 본문 로드");
                            // Log.d(TAG, tempData.writerIndex + " => " + document.getDocument().getData());
                            GetUserData_Simple(tempData.writerIndex, TKManager.getInstance().UserData_Simple, listener);
                        }

                        Complete("클럽 로드 ㅇㅇ" , listener);

                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });
    }

    public void GetClubContextData(final String clubIndex, final FirebaseManager.CheckFirebaseComplete listener) {
        final CollectionReference colRef = mDataBase.collection("ClubData").document(clubIndex).collection("ClubContext");

        SetFireBaseLoadingCount("클럽 본문 로드" ,0);

        colRef.orderBy("Date", Query.Direction.DESCENDING).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {

            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                int tempTotayVisitCount = 0;
                if (task.isSuccessful()) {

                    if(task.getResult().size() == 0)
                    {
                        if (listener != null)
                            listener.CompleteListener();
                    }
                    else
                    {
                        SetFireBaseLoadingCount("클럽 본문 로드" ,task.getResult().size());

                        for (QueryDocumentSnapshot document : task.getResult()) {
                            if(document.exists())
                            {
                                Log.d(TAG, document.getId() + " => " + document.getData());

                                ClubContextData tempData = new ClubContextData();
                                tempData.ContextType = Integer.parseInt(document.getData().get("ContextType").toString());

                                if (document.getData().containsKey("Context")) {
                                    if(CommonFunc.getInstance().isEmpty(document.getData().get("Context")))
                                    {
                                        tempData.Context = "";
                                    }
                                    else
                                        tempData.Context = document.getData().get("Context").toString();
                                } else
                                    tempData.Context = "";

                                if (document.getData().containsKey("ContextIndex")) {
                                    if(CommonFunc.getInstance().isEmpty(document.getData().get("ContextIndex")))
                                    {
                                        tempData.SetContextIndex(document.getId());
                                    }
                                    else
                                        tempData.SetContextIndex(document.getData().get("ContextIndex").toString());
                                } else
                                    tempData.SetContextIndex(document.getId());


                                tempData.Date = document.getData().get("Date").toString();
                                tempData.writerIndex = document.getData().get("writerIndex").toString();

                                if (document.getData().containsKey("ImgList")) {
                                    tempData.ClearImg();
                                    HashMap<String, String> tempImg = (HashMap<String, String>) document.getData().get("ImgList");
                                    Set set = tempImg.entrySet();
                                    Iterator iterator = set.iterator();
                                    while (iterator.hasNext()) {
                                        Map.Entry entry = (Map.Entry) iterator.next();
                                        String key = (String) entry.getKey();
                                        String value = (String) entry.getValue();
                                        tempData.SetImg(key, value);
                                    }
                                }

                                if (document.getData().containsKey("ReportList")) {
                                    tempData.ClearReportList();
                                    HashMap<String, String> tempImg = (HashMap<String, String>) document.getData().get("ReportList");
                                    Set set = tempImg.entrySet();
                                    Iterator iterator = set.iterator();
                                    while (iterator.hasNext()) {
                                        Map.Entry entry = (Map.Entry) iterator.next();
                                        String key = (String) entry.getKey();
                                        String value = (String) entry.getValue();
                                        tempData.SetReportList(key, value);
                                    }
                                }

                                if (document.getData().containsKey("ReplyList")) {
                                    tempData.ClearReplyData();
                                    HashMap<String, String> tempImg = (HashMap<String, String>) document.getData().get("ReplyList");
                                    Set set = tempImg.entrySet();
                                    Iterator iterator = set.iterator();
                                    while (iterator.hasNext()) {
                                        Map.Entry entry = (Map.Entry) iterator.next();
                                        String key = (String) entry.getKey();
                                        String value = (String) entry.getValue();
                                        tempData.SetReply(key, value);



                                      String[] array = value.split("_");
                                        ClubContextData tempReplyData= new ClubContextData();
                                        tempReplyData.SetWriterIndex(array[0]);
                                        tempReplyData.SetDate(array[1]);
                                        tempReplyData.SetContext(array[2]);
                                        tempData.SetReplyData(key, tempReplyData);


                                        if(!TKManager.getInstance().UserData_Simple.containsKey(tempReplyData.GetWriterIndex()))
                                        {
                                            AddFireBaseLoadingCount("클럽 본문 로드");
                                            GetUserData_Simple(tempReplyData.GetWriterIndex(), TKManager.getInstance().UserData_Simple, listener);
                                        }


                                    }
                                }

                                TKManager.getInstance().TargetClubData.AddClubContext(tempData.ContextIndex, tempData);

                                // if(TKManager.getInstance().UserData_Simple.get(tempData.writerIndex) == null)
                                {

                                    // Log.d(TAG, tempData.writerIndex + " => " + document.getDocument().getData());
                                    if(!TKManager.getInstance().UserData_Simple.containsKey(tempData.writerIndex))
                                    {
                                        AddFireBaseLoadingCount("클럽 본문 로드");
                                        GetUserData_Simple(tempData.writerIndex, TKManager.getInstance().UserData_Simple, listener);
                                    }

                                }
                            }
                            else
                            {

                            }
                            Complete("클럽 본문 로드 ㅇㅇ " ,listener);
                        }

                    }



              /*      if (listener != null)
                        listener.CompleteListener();*/

                } else {
                    Log.d(TAG, "Error getting documents: ", task.getException());
                }
            }
        });
    }

    public void GetClubReportData(final String clubIndex, final FirebaseManager.CheckFirebaseComplete listener)
    {
        TKManager.getInstance().TargetReportContextData.clear();
        TKManager.getInstance().TargetRemoveContextData.clear();
        SetFireBaseLoadingCount("클럽 신고 로드" ,0);
        final CollectionReference cocRef = mDataBase.collection("ClubData").document(clubIndex).collection("ReportContextList");
        cocRef.orderBy("Date", Query.Direction.DESCENDING).limit(10).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                int tempTotayLikeCount = 0;
                if (task.isSuccessful()) {
                    if(task.getResult().size() == 0)
                    {
                        if(listener != null)
                            listener.CompleteListener();
                    }
                    else
                    {
                        SetFireBaseLoadingCount("클럽 신고 로드" ,task.getResult().size());

                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Log.d(TAG, document.getId() + " => " + document.getData());

                            Log.d("test","@@@@@@ 111 " + document.getId());
                            GetClubContextData(clubIndex, document.getId(), listener);

                        }
                    }

                } else {
                    Log.d(TAG, "Error getting documents: ", task.getException());
                }
            }


        });
    }

    public void GetClubData(final UserData userData, final String clubIndex, final FirebaseManager.CheckFirebaseComplete listener) {

        final DocumentReference docRef = mDataBase.collection("ClubData").document(clubIndex);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d(TAG, "DocumentSnapshot data: " + document.getData());

                        ClubData getClubData = new ClubData();

                        if (document.getData().containsKey("ClubContextCount")) {
                            int contextCount = Integer.parseInt(document.getData().get("ClubContextCount").toString());
                            getClubData.ClubContextCount = contextCount;
                        } else
                            getClubData.ClubContextCount = 0;

                        if (document.getData().containsKey("ClubMemberCount")) {
                            int MemberCount = (int)Double.parseDouble(document.getData().get("ClubMemberCount").toString());
                            getClubData.ClubMemberCount = MemberCount;
                        } else
                            getClubData.ClubMemberCount = 0;

                        if (document.getData().containsKey("ClubThumbNail")) {
                            String tempData = document.getData().get("ClubThumbNail").toString();
                            getClubData.SetClubThumb(tempData);
                        } else
                            getClubData.SetClubThumb(null);

                        if (document.getData().containsKey("ClubName")) {
                            String tempData = document.getData().get("ClubName").toString();
                            getClubData.SetClubName(tempData);
                        } else
                            getClubData.SetClubName(null);

                        if (document.getData().containsKey("ClubIndex")) {
                            String tempData = document.getData().get("ClubIndex").toString();
                            getClubData.SetClubIndex(tempData);
                        } else
                            getClubData.SetClubIndex(null);


                        if (document.getData().containsKey("ClubMasterIndex")) {
                            String tempData = document.getData().get("ClubMasterIndex").toString();
                            getClubData.SetClubMasterIndex(tempData);
                        } else
                            getClubData.SetClubMasterIndex(null);

                        if (document.getData().containsKey("ClubType")) {
                            boolean tempData = (boolean)document.getData().get("ClubType");
                            getClubData.SetClubType(tempData);
                        } else
                            getClubData.SetClubType(false);

                        if (document.getData().containsKey("ClubCreateDate")) {
                            long tempData = Long.parseLong(document.getData().get("ClubCreateDate").toString());
                            getClubData.SetClubCreateDate(tempData);
                        } else
                            getClubData.SetClubCreateDate(Long.parseLong(CommonFunc.getInstance().GetCurrentTime()));

                        if (document.getData().containsKey("ClubComment")) {
                            String tempData = document.getData().get("ClubComment").toString();
                            getClubData.SetClubComment(tempData);
                        } else
                            getClubData.SetClubComment(getClubData.GetClubName() + "입니다");


                        if (document.getData().containsKey("ClubFavorite")) {
                            getClubData.ClubFavorite.clear();
                            HashMap<String, String> tempImg = (HashMap<String, String>) document.getData().get("ClubFavorite");
                            Set set = tempImg.entrySet();
                            Iterator iterator = set.iterator();
                            while (iterator.hasNext()) {
                                Map.Entry entry = (Map.Entry) iterator.next();
                                String key = (String) entry.getKey();
                                String value = (String) entry.getValue();
                                getClubData.AddClubFavorite(key, value);
                            }
                        }

                        if (document.getData().containsKey("ClubMemberList")) {
                            getClubData.ClubMemberList.clear();
                            HashMap<String, String> tempImg = (HashMap<String, String>) document.getData().get("ClubMemberList");
                            Set set = tempImg.entrySet();
                            Iterator iterator = set.iterator();
                            while (iterator.hasNext()) {
                                Map.Entry entry = (Map.Entry) iterator.next();
                                String key = (String) entry.getKey();
                                String value = (String) entry.getValue();
                                getClubData.AddClubMember(key);
                            }
                        }

                        if (document.getData().containsKey("ClubMaxMemberCount")) {
                            int MaxMemberCount = (int)Double.parseDouble(document.getData().get("ClubMaxMemberCount").toString());
                            getClubData.SetClubMaxMember(MaxMemberCount);
                        } else
                            getClubData.SetClubMaxMember(CommonData.ClubUserCountMinSize);

                    try {
                            TKManager.getInstance().TargetClubData =  (ClubData) getClubData.clone();
                            TKManager.getInstance().ClubData_Simple.put(TKManager.getInstance().TargetClubData.GetClubIndex(), TKManager.getInstance().TargetClubData);
                        } catch (CloneNotSupportedException e) {
                            e.printStackTrace();
                        }


                     //   getClubData.Clear();
                        //AddFireBaseLoadingCount();
                          if(listener != null);
                            listener.CompleteListener();

                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });
    }


    public void GetClubData_Simple(final String userIndex, final Map<String, ClubData> getData, final FirebaseManager.CheckFirebaseComplete listener) {
        final DocumentReference docRef = mDataBase.collection("ClubData_Simple").document(userIndex);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d(TAG, "DocumentSnapshot data: " + document.getData());

                        ClubData tempUser = new ClubData();

                        if (document.getData().containsKey("ContextCount")) {
                            int contextCount = Integer.parseInt(document.getData().get("ContextCount").toString());
                            tempUser.ClubContextCount = contextCount;
                        } else
                            tempUser.ClubContextCount = 0;

                        if (document.getData().containsKey("MemberCount")) {
                            int MemberCount =Integer.parseInt(document.getData().get("MemberCount").toString());
                            tempUser.ClubMemberCount = MemberCount;
                        } else
                            tempUser.ClubMemberCount = 0;

                        if (document.getData().containsKey("Thumb")) {
                            String tempData = document.getData().get("Thumb").toString();
                            tempUser.SetClubThumb(tempData);
                        } else
                            tempUser.SetClubThumb(null);

                        if (document.getData().containsKey("Name")) {
                            String tempData = document.getData().get("Name").toString();
                            tempUser.SetClubName(tempData);
                        } else
                            tempUser.SetClubName(null);

                        if (document.getData().containsKey("index")) {
                            String tempData = document.getData().get("index").toString();
                            tempUser.SetClubIndex(tempData);
                        } else
                            tempUser.SetClubIndex(null);

                        if (document.getData().containsKey("MasterIndex")) {
                            String tempData = document.getData().get("MasterIndex").toString();
                            tempUser.SetClubMasterIndex(tempData);
                        } else
                            tempUser.SetClubMasterIndex(null);

                        if (document.getData().containsKey("Date")) {
                            long tempData = Long.parseLong(document.getData().get("Date").toString());
                            tempUser.SetClubCreateDate(tempData);
                        } else
                            tempUser.SetClubCreateDate(Long.parseLong(CommonFunc.getInstance().GetCurrentTime()));

                        if (document.getData().containsKey("Comment")) {
                            String tempData = document.getData().get("Comment").toString();
                            tempUser.SetClubComment(tempData);
                        } else
                            tempUser.SetClubComment(tempUser.GetClubName() + "입니다");

                        if (document.getData().containsKey("MaxMemberCount")) {
                            int MaxMemberCount =Integer.parseInt(document.getData().get("MaxMemberCount").toString());
                            tempUser.SetClubMaxMember(MaxMemberCount);
                        } else
                            tempUser.SetClubMaxMember(CommonData.ClubUserCountMinSize);

                        getData.put(tempUser.GetClubIndex(), tempUser);

                        //AddFireBaseLoadingCount();

                        Complete("클럽 심플 로드 ㅇㅇ" ,listener);

                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });
    }

    private void GetUserClubList(String userIndex, final UserData userData, final CheckFirebaseComplete listener) {
        CollectionReference colRef = mDataBase.collection("UserData").document(userIndex).collection("ClubList");
        final int TodayDate = Integer.parseInt(CommonFunc.getInstance().GetCurrentDate());

        colRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                int tempTotayVisitCount = 0;
                ClubData tempData = new ClubData();
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Log.d(TAG, document.getId() + " => " + document.getData());

                        tempData.SetClubIndex(document.getData().get("index").toString());

                        userData.SetUserClubData(document.getData().get("index").toString(), tempData);


                    }

                    if (listener != null) {
                        listener.CompleteListener();
                    }

                } else {
                    Log.d(TAG, "Error getting documents: ", task.getException());
                }
            }
        });
    }

    public void EditClubList(final ClubData club, final CheckFirebaseComplete listener)
    {
        final Map<String, Object> clubSimpleData = new HashMap<>();
        clubSimpleData.put("Thumb", club.ClubThumbNail);
        clubSimpleData.put("Name", club.ClubName);
        clubSimpleData.put("Comment", club.GetClubComment());
        clubSimpleData.put("MaxMemberCount", club.GetClubMaxMember());

        mDataBase.collection("ClubData").document(club.GetClubIndex())
                .set(club, SetOptions.merge())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        mDataBase.collection("ClubData_Simple").document(club.GetClubIndex())
                                .set(clubSimpleData, SetOptions.merge())
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        if (listener != null) {
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
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error writing document", e);
                    }
                });
    }

    public void RegistClubList(final ClubData club, final CheckFirebaseComplete listener)
    {
        String userIndex = TKManager.getInstance().MyData.GetUserIndex();

        final Map<String, Object> clubSimpleData = new HashMap<>();
        clubSimpleData.put("index", club.ClubIndex);
        clubSimpleData.put("Thumb", club.ClubThumbNail);
        clubSimpleData.put("ContextCount", club.GetClubContextCount());
        clubSimpleData.put("Name", club.ClubName);
        clubSimpleData.put("MemberCount", club.GetClubMemberCount());
        clubSimpleData.put("Comment", club.GetClubComment());
        clubSimpleData.put("Date", club.GetClubCreateDate());
        clubSimpleData.put("MasterIndex", club.GetClubMasterIndex());
        clubSimpleData.put("MaxMemberCount", club.GetClubMaxMember());

        final Map<String, Object> clubData = new HashMap<>();
        clubData.put("index", club.ClubIndex);

        //favoriteData.put("Index", Integer.parseInt(CommonFunc.getInstance().GetCurrentDate()));

        mDataBase.collection("ClubData").document(club.GetClubIndex())
                .set(club, SetOptions.merge())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot successfully written!");
                        mDataBase.collection("UserData").document(TKManager.getInstance().MyData.GetUserIndex()).collection("ClubList").document(club.ClubIndex)
                                .set(clubData, SetOptions.merge())
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {

                                        mDataBase.collection("ClubData_Simple").document(club.GetClubIndex())
                                                .set(clubSimpleData, SetOptions.merge())
                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {
                                                        if (listener != null) {
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
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error writing document", e);
                    }
                });


        Set tempKey = club.ClubFavorite.keySet();
        List array = new ArrayList();

        array = new ArrayList(tempKey);

        for(int i=0; i<club.ClubFavorite.size(); i++)
        {
            final DocumentReference sfDocRef = mDataBase.collection("ClubData_Favorite").document(array.get(i).toString());
            final double[] newPopulation = new double[1];
            final int finalI = i;
            final List finalArray = array;
            mDataBase.runTransaction(new Transaction.Function<Void>() {
                @Override
                public Void apply(Transaction transaction) throws FirebaseFirestoreException {
                    DocumentSnapshot snapshot = transaction.get(sfDocRef);

                    if(snapshot.getData() == null)
                    {
                        final Map<String, Object> clubFavoriteData = new HashMap<>();
                        clubFavoriteData.put("Count", 0);
                        mDataBase.collection("ClubData_Favorite").document(finalArray.get(finalI).toString()).set(clubFavoriteData, SetOptions.merge());
                        //transaction.update(sfDocRef, "Count", 0);
                    }
                    else
                    {
                        newPopulation[0] = snapshot.getDouble("Count") + 1;
                        transaction.update(sfDocRef, "Count", newPopulation[0]);
                    }

                    int tempIndex = (int)newPopulation[0];

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


            mDataBase.collection("ClubData_Favorite").document(array.get(i).toString()).collection("ClubIndex").document(club.ClubIndex)
                    .set(clubSimpleData, SetOptions.merge())
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

    public void RegistClubIndex(final ClubData club, final CheckFirebaseComplete listener)
    {
        final DocumentReference sfDocRef = mDataBase.collection("ClubData").document("ClubCount");
        final double[] newPopulation = new double[1];
        mDataBase.runTransaction(new Transaction.Function<Void>() {
            @Override
            public Void apply(Transaction transaction) throws FirebaseFirestoreException {
                DocumentSnapshot snapshot = transaction.get(sfDocRef);
                newPopulation[0] = snapshot.getDouble("count") + 1;
                transaction.update(sfDocRef, "count", newPopulation[0]);
                int tempIndex = (int)newPopulation[0];

                // Success
                return null;
            }
        }).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d(TAG, "Transaction success!");
                if (listener != null) {
                    club.SetClubIndex(Integer.toString((int)newPopulation[0]));
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

    public void SetFavoriteRank(String favoriteName)
    {
        final DocumentReference sfDocRef = mDataBase.collection("Favorite_Rank").document(favoriteName);
        final double[] newPopulation = new double[1];
        mDataBase.runTransaction(new Transaction.Function<Void>() {
            @Override
            public Void apply(Transaction transaction) throws FirebaseFirestoreException {
                DocumentSnapshot snapshot = transaction.get(sfDocRef);

                if(snapshot.getData() == null)
                {
                    final Map<String, Object> clubFavoriteData = new HashMap<>();
                    clubFavoriteData.put("count", 0);
                    mDataBase.collection("Favorite_Rank").document(favoriteName).set(clubFavoriteData, SetOptions.merge());
                }
                else
                {
                    newPopulation[0] = snapshot.getDouble("count") + 1;
                    transaction.update(sfDocRef, "count", newPopulation[0]);
                }

                int tempIndex = (int)newPopulation[0];

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
    }


    public void FindFavoriteRank(final CheckFirebaseComplete listener)
    {
        TKManager.getInstance().SearchList_Favorite.clear();

        final CollectionReference sfColRef = mDataBase.collection("Favorite_Rank");
        sfColRef.orderBy("count", Query.Direction.DESCENDING).limit(FAVORITE_RANK_COUNT).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                int tempTotayLikeCount = 0;
                if (task.isSuccessful()) {

                    //AddFireBaseLoadingCount(task.getResult().size());
                    {

                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Log.d("UserList_Search_Hot", document.getId() + " => " + document.getData());

                            TKManager.getInstance().SearchList_Favorite.add(document.getId());
                        }

                        Complete("관심사 랭킹 로드 ㅇㅇ" ,listener);
                      /*  if(listener != null)
                            listener.CompleteListener();*/



                    }


                } else {
                    Log.d(TAG, "Error getting documents: ", task.getException());
                }
            }
        });
    }

    public void GetFavoriteList(boolean firstLoad, String favoriteName, final CheckFirebaseComplete listener)
    {

        final DocumentSnapshot[] lastVisible = {null};
        Query first = null;


        if(firstLoad)
        {
            TKManager.getInstance().UserList_Hot.clear();
            TKManager.getInstance().View_UserList_Hot.clear();

            first =  mDataBase.collection("FavoriteList").document(favoriteName).collection("UserIndex")
                    .limit(CommonData.UserList_First_Loding_Count);
        }
        else
        {
            first = mDataBase.collection("FavoriteList").document(favoriteName).collection("UserIndex")
                    .startAfter(lastFavoriteUserVisible)
                    .limit(CommonData.UserList_Loding_Count);
        }

        first.get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot documentSnapshots) {
                        // ...
                        AddFireBaseLoadingCount("관심사 유져 로드" ,documentSnapshots.size());

                        for (DocumentSnapshot document : documentSnapshots.getDocuments()) {

                            if(!document.getId().equals(TKManager.getInstance().MyData.GetUserIndex()))
                            {
                                TKManager.getInstance().UserList_Hot.add(document.getId());
                                TKManager.getInstance().View_UserList_Hot = TKManager.getInstance().UserList_Hot;
                                GetUserData_Simple(document.getId().toString(), TKManager.getInstance().UserData_Simple, listener);
                            }
                            else
                                Complete("관심사 유져 로드" ,listener);

                        }

                        if(documentSnapshots.size() > 0)
                        {
                            // Get the last visible document
                            lastFavoriteUserVisible = documentSnapshots.getDocuments()
                                    .get(documentSnapshots.size() -1);
                        }


                        Log.d("##@@!", "Favorite  : " +lastFavoriteUserVisible);
                        // Use the query for pagination
                        // ...

                        Complete("관심사 ㅐㅐ", listener);
                    }
                });


    /*    final CollectionReference sfColRef = mDataBase.collection("FavoriteList").document(favoriteName).collection("UserIndex");
        sfColRef.limit(20).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                int tempTotayLikeCount = 0;
                if (task.isSuccessful()) {
                    {
                        AddFireBaseLoadingCount(task.getResult().size());
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Log.d("UserList_Search_Hot", document.getId() + " => " + document.getData());


                            if(!document.getId().equals(TKManager.getInstance().MyData.GetUserIndex()))
                            {

                                TKManager.getInstance().UserList_Hot.add(document.getId());
                                TKManager.getInstance().View_UserList_Hot = TKManager.getInstance().UserList_Hot;
                                GetUserData_Simple(document.getId().toString(), TKManager.getInstance().UserData_Simple, listener);
                            }
                            else
                                Complete(listener);
                        }
*//*
                        long seed = System.nanoTime();
                        Collections.shuffle(TKManager.getInstance().View_UserList_Hot, new Random(seed));*//*
                    }

                } else {
                    Log.d(TAG, "Error getting documents: ", task.getException());
                }
            }
        });*/
    }

    public void FindFavoriteClubList(String favoriteName, final CheckFirebaseComplete listener)
    {
        SetFireBaseLoadingCount("관심사 유져 로드" , 0);

        final CollectionReference sfColRef = mDataBase.collection("ClubData_Favorite").document(favoriteName).collection("ClubIndex");
        sfColRef.limit(20).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                int tempTotayLikeCount = 0;
                if (task.isSuccessful()) {
                    {
                        if(task.getResult().size() == 0)
                        {
                            if(listener != null)
                                listener.CompleteListener_No();
                        }
                        else
                        {
                            SetFireBaseLoadingCount("관심사 유져 로드" , task.getResult().size());

                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d("UserList_Search_Hot", document.getId() + " => " + document.getData());


                            }


                        }

                    }


                } else {
                    Log.d(TAG, "Error getting documents: ", task.getException());
                }
            }
        });
    }

    public void FindFavoriteList(String favoriteName, Boolean rankList, final CheckFirebaseComplete listener)
    {

        SetFireBaseLoadingCount("관심사 유져 로드" ,0);

        final CollectionReference sfColRef = mDataBase.collection("FavoriteList").document(favoriteName).collection("UserIndex");
        sfColRef.limit(20).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                int tempTotayLikeCount = 0;
                if (task.isSuccessful()) {
                    {
                        if(task.getResult().size() == 0)
                        {
                            if(listener != null)
                                listener.CompleteListener_No();
                        }
                        else
                        {
                            TKManager.getInstance().UserList_Hot.clear();
                            TKManager.getInstance().View_UserList_Hot.clear();

                            if(rankList == false)
                                SetFavoriteRank(favoriteName);

                            SetFireBaseLoadingCount("관심사 유져 로드" , task.getResult().size());

                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d("UserList_Search_Hot", document.getId() + " => " + document.getData());

                                if(!document.getId().equals(TKManager.getInstance().MyData.GetUserIndex()))
                                {
                                    TKManager.getInstance().UserList_Hot.add(document.getId());
                                    TKManager.getInstance().View_UserList_Hot = TKManager.getInstance().UserList_Hot;
                                    CommonFunc.getInstance().FilterHotUserByReport(TKManager.getInstance().View_UserList_Hot);
                                    GetUserData_Simple(document.getId().toString(), TKManager.getInstance().UserData_Simple, listener);
                                }
                                else
                                    Complete("관심사 유져 로드ㅇㅇ" , listener);
                            }


                        }

                    }


                } else {
                    Log.d(TAG, "Error getting documents: ", task.getException());
                }
            }
        });
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

    public void RemoveClubMember(final ClubData club, final String userIndex, final  CheckFirebaseComplete listener)
    {
        final DocumentReference sfDocRef = mDataBase.collection("ClubData").document(club.GetClubIndex());
        final double[] newPopulation = new double[1];
        mDataBase.runTransaction(new Transaction.Function<Void>() {
            @Override
            public Void apply(Transaction transaction) throws FirebaseFirestoreException {
                DocumentSnapshot snapshot = transaction.get(sfDocRef);
                newPopulation[0] = snapshot.getDouble("ClubMemberCount") - 1;
                transaction.update(sfDocRef, "ClubMemberCount", newPopulation[0]);
                int tempIndex = (int)newPopulation[0];

                // Success
                return null;
            }
        }).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d(TAG, "Transaction success!");

                Map<String, String> tempClub = new HashMap<>();
                tempClub.put("index", club.GetClubIndex());

                mDataBase.collection("UserData").document(userIndex).collection("ClubList").document(club.GetClubIndex()).delete()
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

                club.DelClubMember(userIndex);

                mDataBase.collection("ClubData_Simple").document(club.GetClubIndex()).update("MemberCount", club.GetClubMember().size())
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.d(TAG, "DocumentSnapshot successfully written!");
                                if (listener != null) {
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


                mDataBase.collection("ClubData").document(club.GetClubIndex()).update("ClubMemberList", club.GetClubMember())
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.d(TAG, "DocumentSnapshot successfully written!");
                                if (listener != null) {
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
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Transaction failure.", e);
                    }
                });
    }

    public void RegistClubMember(final ClubData club, final String userIndex, final  boolean refuse, final  CheckFirebaseComplete listener)
    {
        final DocumentReference sfDocRef = mDataBase.collection("ClubData").document(club.GetClubIndex());
        final double[] newPopulation = new double[1];

        mDataBase.collection("UserData").document(userIndex).collection("RequestClubList").document(club.GetClubIndex()).delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot successfully written!");
                        if (listener != null) {
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

        if(refuse)
        {
            mDataBase.collection("ClubData").document(club.GetClubIndex()).collection("RequestJoin").document(userIndex).delete()
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.d(TAG, "DocumentSnapshot successfully written!");
                            if (listener != null) {
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
        else
        {

            mDataBase.runTransaction(new Transaction.Function<Void>() {
                @Override
                public Void apply(Transaction transaction) throws FirebaseFirestoreException {
                    DocumentSnapshot snapshot = transaction.get(sfDocRef);
                    newPopulation[0] = snapshot.getDouble("ClubMemberCount") + 1;
                    transaction.update(sfDocRef, "ClubMemberCount", newPopulation[0]);
                    int tempIndex = (int)newPopulation[0];

                    // Success
                    return null;
                }
            }).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Log.d(TAG, "Transaction success!");

                    mDataBase.collection("ClubData").document(club.GetClubIndex()).collection("RequestJoin").document(userIndex).delete()
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

                    Map<String, String> tempClub = new HashMap<>();
                    tempClub.put("index", club.GetClubIndex());

                    mDataBase.collection("UserData").document(userIndex).collection("ClubList").document(club.GetClubIndex()).set(tempClub, SetOptions.merge())
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

                    club.AddClubMember(userIndex);
                    mDataBase.collection("ClubData").document(club.GetClubIndex()).update("ClubMemberList", club.GetClubMember())
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d(TAG, "DocumentSnapshot successfully written!");
                                    if (listener != null) {
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

                    mDataBase.collection("ClubData_Simple").document(club.GetClubIndex()).update("MemberCount", club.GetClubMemberCount())
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

                    Map<String, Object> tempUserList= new HashMap<>();
                    tempUserList.put(userIndex, userIndex);

                    Map<String, Object> tempUser= new HashMap<>();
                    tempUser.put("UserList", tempUserList);

                    mDataBase.collection("ChatRoomData").document(club.GetClubIndex())
                            .set(tempUser, SetOptions.merge())
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
            })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.w(TAG, "Transaction failure.", e);
                        }
                    });
        }

    }

    public void GetRequestJoinUserInMyClub(final String clubIndex, final CheckFirebaseComplete listener)
    {
        TKManager.getInstance().UserData_RequestJoin.clear();
        SetFireBaseLoadingCount("내 클럽 가입 신청" ,0);

        final CollectionReference sfColRef = mDataBase.collection("ClubData").document(clubIndex).collection("RequestJoin");
        sfColRef.orderBy("Date", Query.Direction.DESCENDING).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                int tempTotayLikeCount = 0;
                if (task.isSuccessful()) {
                    if(task.getResult().size() == 0)
                    {
                        if(listener != null)
                            listener.CompleteListener();
                    }
                    else
                    {
                        SetFireBaseLoadingCount("내 클럽 가입 신청" ,task.getResult().size());

                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Log.d(TAG, document.getId() + " => " + document.getData());

                         //   AddFireBaseLoadingCount();
                            GetUserData_Simple(document.getData().get(document.getId()).toString(), TKManager.getInstance().UserData_RequestJoin, listener);
                        }
                    }


                } else {
                    Log.d(TAG, "Error getting documents: ", task.getException());
                }
            }
        });
    }

    public void RequestJoinClub(final String clubIndex, final CheckFirebaseComplete listener)
    {
        final DocumentReference sfDocRef = mDataBase.collection("ClubData").document(clubIndex).collection("RequestJoin").document(TKManager.getInstance().MyData.GetUserIndex());

        Map<String, Object> Request = new HashMap<>();
        Request.put(TKManager.getInstance().MyData.GetUserIndex(), TKManager.getInstance().MyData.GetUserIndex());
        Request.put("Date", Long.parseLong(CommonFunc.getInstance().GetCurrentTime()));

        sfDocRef.set(Request, SetOptions.merge())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                        final DocumentReference sfDocRef = mDataBase.collection("UserData").document(TKManager.getInstance().MyData.GetUserIndex()).collection("RequestClubList").document(clubIndex);

                        Map<String, Object> Request = new HashMap<>();
                        Request.put("index", clubIndex);
                        Request.put("Date", Long.parseLong(CommonFunc.getInstance().GetCurrentTime()));

                        sfDocRef.set(Request, SetOptions.merge())
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {

                                        if(listener != null)
                                            listener.CompleteListener();

                                        Log.d(TAG, "DocumentSnapshot successfully written!");
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.w(TAG, "Error writing document", e);
                                    }
                                });

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

    public void CancelJoinClub(final String clubIndex, final CheckFirebaseComplete listener)
    {
        final DocumentReference sfDocRef = mDataBase.collection("ClubData").document(clubIndex).collection("RequestJoin").document(TKManager.getInstance().MyData.GetUserIndex());

        sfDocRef.delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                        final DocumentReference sfDocRef = mDataBase.collection("UserData").document(TKManager.getInstance().MyData.GetUserIndex()).collection("RequestClubList").document(clubIndex);

                        sfDocRef.delete()
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {

                                        if(listener != null)
                                            listener.CompleteListener();

                                        Log.d(TAG, "DocumentSnapshot successfully written!");
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.w(TAG, "Error writing document", e);
                                    }
                                });

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

        Map<String, Object> RequestFriendUserData = new HashMap<>();
        RequestFriendUserData.put("Index", userIndex);
        RequestFriendUserData.put("Date", Integer.parseInt(CommonFunc.getInstance().GetCurrentDate()));

        mDataBase.collection("UserData").document(targetIndex).collection("RequestFriendList").document(userIndex)
                .set(RequestFriendUserData, SetOptions.merge())
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

        Map<String, Object> AlarmData = new HashMap<>();
        AlarmData.put("Index", userIndex);
        AlarmData.put("Date", Long.parseLong(CommonFunc.getInstance().GetCurrentTime()));
        AlarmData.put("Type", "FRIEND");

        mDataBase.collection("UserData").document(targetIndex).collection("AlarmList").document(userIndex)
                .set(AlarmData, SetOptions.merge())
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

        Map<String, Object> AlarmData = new HashMap<>();
        AlarmData.put("Index", userIndex);
        AlarmData.put("Date", Long.parseLong(CommonFunc.getInstance().GetCurrentTime()));
        AlarmData.put("Type", "VISIT");

        mDataBase.collection("UserData").document(targetIndex).collection("AlarmList").document(userIndex)
                .set(AlarmData, SetOptions.merge())
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


        Map<String, Object> AlarmData = new HashMap<>();
        AlarmData.put("Index", userIndex);
        AlarmData.put("Date", Long.parseLong(CommonFunc.getInstance().GetCurrentTime()));
        AlarmData.put("Type", "LIKE");

        mDataBase.collection("UserData").document(targetIndex).collection("AlarmList").document(userIndex)
                .set(AlarmData, SetOptions.merge())
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

    public void RegistUserGeoInfo()
    {
        Log.d("%%%%", "RegistUserGeoInfo");

        CollectionReference collectionRef = FirebaseFirestore.getInstance().collection("UserData_Geo");
        GeoFirestore geoFirestore = new GeoFirestore(collectionRef);

      //  String[] aasd ={"18", "25", "27", "31", "39"};


      //  for(int i =0; i< 5; i++)
      //      geoFirestore.setLocation(aasd[i], new GeoPoint(TKManager.getInstance().MyData.GetUserDist_Lat(), TKManager.getInstance().MyData.GetUserDist_Lon()));

           geoFirestore.setLocation(TKManager.getInstance().MyData.GetUserIndex().toString(), new GeoPoint(TKManager.getInstance().MyData.GetUserDist_Lat(), TKManager.getInstance().MyData.GetUserDist_Lon()));
        Log.d("%%%%", "RegistUserGeoInfo end");
    }

    public void GetSortData(final FirebaseManager.CheckFirebaseComplete listener) {

        mDataBase.collection("UserData").document(TKManager.getInstance().MyData.GetUserIndex()).collection("FilterData").document(TKManager.getInstance().MyData.GetUserIndex())
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d(TAG, "DocumentSnapshot data: " + document.getData());

                        if(CommonFunc.getInstance().CheckStringNull(document.getData().get("Distance").toString()))
                        {
                           TKManager.getInstance().FilterData.SetDistance(100);
                        }
                        else
                            TKManager.getInstance().FilterData.SetDistance(Integer.parseInt(document.getData().get("Distance").toString()));

                        if(CommonFunc.getInstance().CheckStringNull(document.getData().get("MinAge").toString()))
                        {
                            TKManager.getInstance().FilterData.SetMinAge(50);
                        }
                        else
                            TKManager.getInstance().FilterData.SetMinAge(Integer.parseInt(document.getData().get("MinAge").toString()));

                        if(CommonFunc.getInstance().CheckStringNull(document.getData().get("MaxAge").toString()))
                        {
                            TKManager.getInstance().FilterData.SetMaxAge(100);
                        }
                        else
                            TKManager.getInstance().FilterData.SetMaxAge(Integer.parseInt(document.getData().get("MaxAge").toString()));

                        if(CommonFunc.getInstance().CheckStringNull(document.getData().get("Gender").toString()))
                        {
                            TKManager.getInstance().FilterData.SetGender(0);
                        }
                        else
                            TKManager.getInstance().FilterData.SetGender(Integer.parseInt(document.getData().get("Gender").toString()));


                        if(CommonFunc.getInstance().CheckStringNull(document.getData().get("Connect").toString()))
                        {
                            TKManager.getInstance().FilterData.SetConnect(0);
                        }
                        else
                            TKManager.getInstance().FilterData.SetConnect(Integer.parseInt(document.getData().get("Connect").toString()));

                        if(listener != null)
                            listener.CompleteListener();

                    } else {
                        Log.d(TAG, "No such document");

                        if(listener != null)
                            listener.CompleteListener();

                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });
    }

    public void RegistSortData(final FirebaseManager.CheckFirebaseComplete listener)
    {
        Map<String, Object> UserSortData = new HashMap<>();
        UserSortData.put("Distance", TKManager.getInstance().FilterData.GetDistance());
        UserSortData.put("MinAge", TKManager.getInstance().FilterData.GetMinAge());
        UserSortData.put("MaxAge", TKManager.getInstance().FilterData.GetMaxAge());
        UserSortData.put("Gender", TKManager.getInstance().FilterData.GetGender());
        UserSortData.put("Connect", TKManager.getInstance().FilterData.GetConnect());

        mDataBase.collection("UserData").document(TKManager.getInstance().MyData.GetUserIndex()).collection("FilterData").document(TKManager.getInstance().MyData.GetUserIndex())
                .set(UserSortData, SetOptions.merge())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        if(listener != null)
                            listener.CompleteListener();
                        Log.d(TAG, "DocumentSnapshot successfully written!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        if(listener != null)
                            listener.CompleteListener();
                        Log.w(TAG, "Error writing document", e);
                    }
                });
    }

    public void GetManagerNotice(final FirebaseManager.CheckFirebaseComplete listener)
    {
        TKManager.getInstance().NoticeData_Notice.clear();
        CollectionReference colRef = mDataBase.collection("Manager_Notice");

        colRef.orderBy("Date", Query.Direction.DESCENDING).limit(10).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {

                    for (QueryDocumentSnapshot document : task.getResult()) {

                         Log.d(TAG, document.getId() + " => " + document.getData());

                            NoticeData tempData = new NoticeData();
                            tempData.SetTitle(document.getData().get("Title").toString());
                            tempData.SetContent(document.getData().get("Context").toString());
                            tempData.SetDate(Long.parseLong(document.getData().get("Date").toString()));

                            tempData.SetNoticeType(Integer.parseInt(document.getData().get("NoticeType").toString()));
                            tempData.SetAppOpenShow(Boolean.parseBoolean(document.getData().get("AppOpenShow").toString()));

                            TKManager.getInstance().NoticeData_Notice.add(tempData);


                    }



                    Complete("공지사항 ㅇㅇ" ,listener);

                } else {
                    Log.d(TAG, "Error getting documents: ", task.getException());
                }
            }
        });
    }

    public void GetManagerEvent(final FirebaseManager.CheckFirebaseComplete listener)
    {
        TKManager.getInstance().NoticeData_Event.clear();
        CollectionReference colRef = mDataBase.collection("Manager_Event");

        colRef.orderBy("Date", Query.Direction.DESCENDING).limit(10).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {

                    for (QueryDocumentSnapshot document : task.getResult()) {

                        Log.d(TAG, document.getId() + " => " + document.getData());

                        NoticeData tempData = new NoticeData();
                        tempData.SetTitle(document.getData().get("Title").toString());
                        tempData.SetContent(document.getData().get("Context").toString());
                        tempData.SetDate(Long.parseLong(document.getData().get("Date").toString()));

                        tempData.SetNoticeType(Integer.parseInt(document.getData().get("NoticeType").toString()));
                        tempData.SetAppOpenShow(Boolean.parseBoolean(document.getData().get("AppOpenShow").toString()));

                        TKManager.getInstance().NoticeData_Event.add(tempData);


                    }

                    Complete("이벤트 ㅇㅇ" , listener);

                } else {
                    Log.d(TAG, "Error getting documents: ", task.getException());
                }
            }
        });
    }

    public void GetManagerFAQ(final FirebaseManager.CheckFirebaseComplete listener)
    {
        TKManager.getInstance().NoticeData_Faq.clear();
        CollectionReference colRef = mDataBase.collection("Manager_FAQ");

        colRef.orderBy("Date", Query.Direction.DESCENDING).limit(10).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {

                    for (QueryDocumentSnapshot document : task.getResult()) {

                        Log.d(TAG, document.getId() + " => " + document.getData());

                        NoticeData tempData = new NoticeData();
                        tempData.SetTitle(document.getData().get("Title").toString());
                        tempData.SetContent(document.getData().get("Context").toString());
                        tempData.SetDate(Long.parseLong(document.getData().get("Date").toString()));

                        tempData.SetNoticeType(Integer.parseInt(document.getData().get("NoticeType").toString()));
                        tempData.SetAppOpenShow(Boolean.parseBoolean(document.getData().get("AppOpenShow").toString()));

                        TKManager.getInstance().NoticeData_Faq.add(tempData);


                    }
                    Complete("faq ㅇㅇ" , listener);

                } else {
                    Log.d(TAG, "Error getting documents: ", task.getException());
                }
            }
        });
    }

    public String[] AddFavorite(Context context) {

        StringBuffer strBuffer = new StringBuffer();
        try{
            InputStream inputStream = context.getResources().openRawResource(R.raw.favorite);
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            String line="";
            while((line=reader.readLine())!=null){
                strBuffer.append(line+"\n");
            }

            reader.close();
            inputStream.close();
        }catch (IOException e){
            e.printStackTrace();
        }

        String[] change_target = strBuffer.toString().split("\\n");

        Map<String, Object> UserDistInfo = new HashMap<>();

     //   for(int i= 0; i<change_target.length; i++)
            UserDistInfo.put("count", 0);

            for(int i=0; i<change_target.length; i++)
            {
                mDataBase.collection("PopFavorite").document(change_target[i])
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



        return change_target;

   /*     Map<String, Object> UserDistInfo = new HashMap<>();
        UserDistInfo.put("Dist_Lat", TKManager.getInstance().MyData.GetUserDist_Lat());

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
                });*/
    }

    public void PurchaseVip()
    {
        String userIndex = TKManager.getInstance().MyData.GetUserIndex();
        Map<String, Object> VipData = new HashMap<>();
        VipData.put("Vip", TKManager.getInstance().MyData.GetUserVip());
        //favoriteData.put("Index", Integer.parseInt(CommonFunc.getInstance().GetCurrentDate()));

        mDataBase.collection("UserData").document(userIndex)
                .set(VipData, SetOptions.merge())
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
