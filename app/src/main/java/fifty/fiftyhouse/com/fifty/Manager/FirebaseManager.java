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


    public void SetUserDataOnFireBase(CommonData.CollentionType collectType, String documentName, Object obj)
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

        mDataBase.collection("UserData").document(documentName)
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


        Map<String, Object> simpleUser = new HashMap<>();

        simpleUser.put("Index", ((UserData) obj).GetUserIndex());
        simpleUser.put("NickName", ((UserData) obj).GetUserNickName());
        simpleUser.put("Img_ThumbNail", ((UserData) obj).GetUserImgThumb());

        mDataBase.collection("UserData_Simple").document(documentName)
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
        nickName.put(((UserData) obj).GetUserNickName(), ((UserData) obj).GetUserIndex());

        mDataBase.collection("UserData_NickName").document(((UserData) obj).GetUserNickName())
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

    public void GetUserIndex()
    {
        final DocumentReference sfDocRef = mDataBase.collection("USERCOUNT").document("Count");

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

    public void GetUserData(String userIndex)
    {
        DocumentReference docRef = mDataBase.collection("UserData").document(userIndex);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d(TAG, "DocumentSnapshot data: " + document.getData());

                        if(document.getData().containsKey("NickName"))
                        {
                            String NickName = document.getData().get("NickName").toString();
                            TKManager.getInstance().TargetUserData.SetUserNickName(NickName);
                        }
                        else
                            TKManager.getInstance().TargetUserData.SetUserNickName(null);

                        if(document.getData().containsKey("Token"))
                        {
                            String Token = document.getData().get("Token").toString();
                            TKManager.getInstance().TargetUserData.SetUserToken(Token);
                        }
                        else
                            TKManager.getInstance().TargetUserData.SetUserToken(null);

                        if(document.getData().containsKey("Age"))
                        {
                            TKManager.getInstance().TargetUserData.SetUserAge(Integer.parseInt(document.getData().get("Age").toString()));
                        }
                        else
                            TKManager.getInstance().TargetUserData.SetUserAge(50);

                        if(document.getData().containsKey("FavoriteList"))
                        {
                            HashMap<String, String> tempFavorite = (HashMap<String, String>)document.getData().get("FavoriteList");

                            int FavoriteSize = tempFavorite.size();

                            String[] temp = new String[FavoriteSize];
                            for(int i=0; i<FavoriteSize; i++)
                            {
                                temp[i] = tempFavorite.get( Integer.toString(i)).toString();
                                TKManager.getInstance().TargetUserData.SetUserFavorite(temp[i]);
                            }
                        }
                        else
                            TKManager.getInstance().TargetUserData.SetUserFavorite("");


                        if(document.getData().containsKey("Gender"))
                        {
                            TKManager.getInstance().TargetUserData.SetUserGender(Integer.parseInt(document.getData().get("Gender").toString()));
                        }
                        else
                            TKManager.getInstance().TargetUserData.SetUserGender(0);

                        if(document.getData().containsKey("Img_ThumbNail"))
                        {
                            String ThumbNail = document.getData().get("Img_ThumbNail").toString();
                            TKManager.getInstance().TargetUserData.SetUserImgThumb(ThumbNail);
                        }
                        else
                            TKManager.getInstance().TargetUserData.SetUserImgThumb(null);

                        if(document.getData().containsKey("Img"))
                        {
                            HashMap<String, String> tempImg = (HashMap<String, String>)document.getData().get("Img");

                            int ImgSize = tempImg.size();

                            String[] temp = new String[ImgSize];
                            for(int i=0; i<ImgSize; i++)
                            {
                                temp[i] = tempImg.get( Integer.toString(i)).toString();
                                TKManager.getInstance().TargetUserData.SetUserImg(temp[i]);
                            }
                        }
                        else
                            TKManager.getInstance().TargetUserData.SetUserImg("");

                        if(document.getData().containsKey("Visit"))
                        {
                            TKManager.getInstance().TargetUserData.SetUserVisit(Integer.parseInt(document.getData().get("Visit").toString()));
                        }
                        else
                            TKManager.getInstance().TargetUserData.SetUserVisit(0);

                        if(document.getData().containsKey("Like"))
                        {
                            TKManager.getInstance().TargetUserData.SetUserLike(Integer.parseInt(document.getData().get("Like").toString()));
                        }
                        else
                            TKManager.getInstance().TargetUserData.SetUserLike(0);

                        if(document.getData().containsKey("Dist"))
                        {
                            TKManager.getInstance().TargetUserData.SetUserDist(Integer.parseInt(document.getData().get("Dist").toString()));
                        }
                        else
                            TKManager.getInstance().TargetUserData.SetUserDist(0);


                        CommonFunc.getInstance().MoveUserActivity(CommonFunc.getInstance().mCurActivity);

                    } else {
                        Log.d(TAG, "No such document");
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

        colRef.whereEqualTo("value", "86").limit(CommonData.UserList_Loding_Count).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
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

                            int ImgSize = tempImg.size();

                            String[] temp = new String[3];
                            for(int i=0; i<ImgSize; i++)
                            {
                                temp[i] = tempImg.get( Integer.toString(i)).toString();
                                tempUser.SetUserImg(temp[i]);
                            }
                        }
                        else
                            tempUser.SetUserImg("");

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

    public void GetPopFavoriteData()
    {
        CollectionReference colRef = mDataBase.collection("PopFavorite");
        colRef.orderBy("count", Query.Direction.DESCENDING).limit(CommonData.Favorite_Pop_Count).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
        @Override
        public void onComplete(@NonNull Task<QuerySnapshot> task) {
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot document : task.getResult()) {
                    Log.d(TAG, document.getId() + " => " + document.getData());
                }
            } else {
                Log.d(TAG, "Error getting documents: ", task.getException());
            }
          }
    });
    }

    public void CheckNickName(final String nickName)
    {
        DocumentReference docRef = mDataBase.collection("NickNameList").document(nickName);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                    } else {
                        Log.d(TAG, "No such document");
                        TKManager.getInstance().MyData.SetUserNickName(nickName);
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });
    }

    public void UploadImg(String userIndex, Bitmap bitmap)
    {
        final StorageReference tempThumbnailRef = mStorageRef.child("Image/" + TKManager.getInstance().MyData.GetUserIndex() + "/thumbnail.jpg");
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        UploadTask uploadTask = tempThumbnailRef.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                Uri downloadUrl = taskSnapshot.getUploadSessionUri();
                TKManager.getInstance().MyData.SetUserImgThumb(downloadUrl.toString());
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

}
