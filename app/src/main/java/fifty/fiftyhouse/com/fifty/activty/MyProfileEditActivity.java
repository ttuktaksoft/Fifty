package fifty.fiftyhouse.com.fifty.activty;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import fifty.fiftyhouse.com.fifty.R;
import fifty.fiftyhouse.com.fifty.adapter.MyProfileEditMenuAdapter;
import fifty.fiftyhouse.com.fifty.util.RecyclerItemClickListener;

public class MyProfileEditActivity extends AppCompatActivity {

    ImageView iv_MyProfile_Edit_Back, iv_MyProfile_Edit_Profile;
    RecyclerView rv_MyProfile_Edit_Menu;

    MyProfileEditMenuAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile_edit);

        iv_MyProfile_Edit_Back = findViewById(R.id.iv_MyProfile_Edit_Back);
        iv_MyProfile_Edit_Profile = findViewById(R.id.iv_MyProfile_Edit_Profile);;
        rv_MyProfile_Edit_Menu = findViewById(R.id.rv_MyProfile_Edit_Menu);
        iv_MyProfile_Edit_Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        initRecyclerView();
    }


    private void initRecyclerView()
    {
        mAdapter = new MyProfileEditMenuAdapter(getApplicationContext());
        mAdapter.setHasStableIds(true);

        rv_MyProfile_Edit_Menu.setAdapter(mAdapter);
        rv_MyProfile_Edit_Menu.setLayoutManager(new GridLayoutManager(getApplicationContext(), 1));
        rv_MyProfile_Edit_Menu.addOnItemTouchListener(new RecyclerItemClickListener(getApplicationContext(), rv_MyProfile_Edit_Menu, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if(position == MyProfileEditMenuAdapter.MY_PROFILE_EDIT_MENU_NICKNAME_INDEX)
                {
                    startActivity(new Intent(getApplicationContext(), NickNameEditActivity.class));
                }
                else if(position == MyProfileEditMenuAdapter.MY_PROFILE_EDIT_MENU_STORY_INDEX)
                {
                    startActivity(new Intent(getApplicationContext(), StoryEditActivity.class));
                }
                else if(position == MyProfileEditMenuAdapter.MY_PROFILE_EDIT_MENU_LOC_INDEX)
                {
                    startActivity(new Intent(getApplicationContext(), LocationEditActivity.class));
                }
                else if(position == MyProfileEditMenuAdapter.MY_PROFILE_EDIT_MENU_FAVORITE_INDEX)
                {
                    startActivity(new Intent(getApplicationContext(), FavoriteEditActivity.class));
                }
            }

            @Override
            public void onLongItemClick(View view, int position) {
                //  Toast.makeText(getApplicationContext(),position+"번 째 아이템 롱 클릭",Toast.LENGTH_SHORT).show();
            }
        }));
    }
}
