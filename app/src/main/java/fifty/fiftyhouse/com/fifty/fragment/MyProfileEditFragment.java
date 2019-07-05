package fifty.fiftyhouse.com.fifty.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.theartofdev.edmodo.cropper.CropImage;

import fifty.fiftyhouse.com.fifty.CommonData;
import fifty.fiftyhouse.com.fifty.CommonFunc;
import fifty.fiftyhouse.com.fifty.DialogFunc;
import fifty.fiftyhouse.com.fifty.Manager.FirebaseManager;
import fifty.fiftyhouse.com.fifty.Manager.TKManager;
import fifty.fiftyhouse.com.fifty.R;
import fifty.fiftyhouse.com.fifty.activty.FavoriteSelectActivity;
import fifty.fiftyhouse.com.fifty.activty.LocationEditActivity;
import fifty.fiftyhouse.com.fifty.activty.MemoEditActivity;
import fifty.fiftyhouse.com.fifty.activty.MyProfileEditActivity;
import fifty.fiftyhouse.com.fifty.activty.NickNameEditActivity;
import fifty.fiftyhouse.com.fifty.adapter.MyProfileEditMenuAdapter;
import fifty.fiftyhouse.com.fifty.util.OnRecyclerItemClickListener;
import fifty.fiftyhouse.com.fifty.util.OnSingleClickListener;
import fifty.fiftyhouse.com.fifty.util.RecyclerItemClickListener;

public class MyProfileEditFragment extends Fragment {

    ImageView iv_MyProfile_Edit_Profile;
    RecyclerView rv_MyProfile_Edit_Menu;

    MyProfileEditMenuAdapter mAdapter;

    Context mContext;
    View mMyProfileEditFragView;

    public MyProfileEditFragment() {
        // Required empty public constructor
    }
    public static MyProfileEditFragment newInstance(String param1, String param2) {
        MyProfileEditFragment fragment = new MyProfileEditFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        mContext = getActivity();
        mMyProfileEditFragView = inflater.inflate(R.layout.fragment_my_profile_edit, container, false);

        iv_MyProfile_Edit_Profile = mMyProfileEditFragView.findViewById(R.id.iv_MyProfile_Edit_Profile);
        rv_MyProfile_Edit_Menu = mMyProfileEditFragView.findViewById(R.id.rv_MyProfile_Edit_Menu);

        CommonFunc.getInstance().DrawImageByGlide(mContext, iv_MyProfile_Edit_Profile, TKManager.getInstance().MyData.GetUserImgThumb(), true);

        iv_MyProfile_Edit_Profile.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                CommonFunc.getInstance().GetPermissionForGalleryCamera(mContext, MyProfileEditFragment.this, CommonData.GET_PHOTO_FROM_CROP);
            }
        });

        initRecyclerView();

        return mMyProfileEditFragView;
    }

    private void initRecyclerView()
    {
        mAdapter = new MyProfileEditMenuAdapter(mContext);
        mAdapter.setHasStableIds(true);

        rv_MyProfile_Edit_Menu.setAdapter(mAdapter);
        rv_MyProfile_Edit_Menu.setLayoutManager(new GridLayoutManager(mContext, 1));
        rv_MyProfile_Edit_Menu.addOnItemTouchListener(new RecyclerItemClickListener(mContext, rv_MyProfile_Edit_Menu, new OnRecyclerItemClickListener() {
            @Override
            public void onSingleClick(View view, int position) {
                if(position == MyProfileEditMenuAdapter.MY_PROFILE_EDIT_MENU_NICKNAME_INDEX)
                {
                    startActivityForResult(new Intent(mContext, NickNameEditActivity.class), MyProfileEditMenuAdapter.MY_PROFILE_EDIT_MENU_NICKNAME_INDEX);
                }
                else if(position == MyProfileEditMenuAdapter.MY_PROFILE_EDIT_MENU_STORY_INDEX)
                {
                    startActivityForResult(new Intent(mContext, MemoEditActivity.class), MyProfileEditMenuAdapter.MY_PROFILE_EDIT_MENU_STORY_INDEX);
                }
                else if(position == MyProfileEditMenuAdapter.MY_PROFILE_EDIT_MENU_LOC_INDEX)
                {
                    startActivityForResult(new Intent(mContext, LocationEditActivity.class), MyProfileEditMenuAdapter.MY_PROFILE_EDIT_MENU_LOC_INDEX);
                }
                else if(position == MyProfileEditMenuAdapter.MY_PROFILE_EDIT_MENU_FAVORITE_INDEX)
                {
                    FirebaseManager.CheckFirebaseComplete listener = new FirebaseManager.CheckFirebaseComplete() {
                        @Override
                        public void CompleteListener() {
                            Intent intent = new Intent(mContext, FavoriteSelectActivity.class);
                            intent.putExtra("Type",1);
                            MyProfileEditFragment.this.startActivityForResult(intent, MyProfileEditMenuAdapter.MY_PROFILE_EDIT_MENU_FAVORITE_INDEX);
                        }

                        @Override
                        public void CompleteListener_Yes() {
                        }

                        @Override
                        public void CompleteListener_No() {
                        }
                    };

                    FirebaseManager.getInstance().GetPopFavoriteData(listener);
                }
            }

            @Override
            public void onLongItemClick(View view, int position) {
                //  Toast.makeText(getApplicationContext(),position+"번 째 아이템 롱 클릭",Toast.LENGTH_SHORT).show();
            }
        }));
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == Activity.RESULT_OK) {
                DialogFunc.getInstance().ShowLoadingPage(getActivity());
                FirebaseManager.CheckFirebaseComplete listener = new FirebaseManager.CheckFirebaseComplete() {
                    @Override
                    public void CompleteListener() {
                        DialogFunc.getInstance().DismissLoadingPage();
                    }

                    @Override
                    public void CompleteListener_Yes() {
                    }

                    @Override
                    public void CompleteListener_No() {
                    }
                };
                Uri resultUri = result.getUri();
                CommonFunc.getInstance().SetCropImage(mContext, resultUri, 0, iv_MyProfile_Edit_Profile, listener);
            }
        }
        else if(resultCode == MyProfileEditMenuAdapter.MY_PROFILE_EDIT_MENU_NICKNAME_INDEX ||
                resultCode == MyProfileEditMenuAdapter.MY_PROFILE_EDIT_MENU_FAVORITE_INDEX ||
                resultCode == MyProfileEditMenuAdapter.MY_PROFILE_EDIT_MENU_STORY_INDEX ||
                resultCode == MyProfileEditMenuAdapter.MY_PROFILE_EDIT_MENU_AGE_INDEX ||
                resultCode == MyProfileEditMenuAdapter.MY_PROFILE_EDIT_MENU_LOC_INDEX)
        {
            RefreshUI();
        }
    }

    private void RefreshUI()
    {
        mAdapter.notifyDataSetChanged();
    }
}
