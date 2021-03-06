package fifty.fiftyhouse.com.fifty.activty;

import android.content.Context;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import fifty.fiftyhouse.com.fifty.CommonData;
import fifty.fiftyhouse.com.fifty.CommonFunc;
import fifty.fiftyhouse.com.fifty.Manager.FirebaseManager;
import fifty.fiftyhouse.com.fifty.Manager.TKManager;
import fifty.fiftyhouse.com.fifty.R;
import fifty.fiftyhouse.com.fifty.util.OnSingleClickListener;

public class LocationEditActivity extends AppCompatActivity {

    View ui_Location_Edit_TopBar;
    TextView tv_TopBar_Title;
    ImageView iv_TopBar_Back;

    TextView tv_Location_Edit_Save, tv_Location_Edit_Count;
    EditText et_Location_Edit_Location;
    InputMethodManager imm;
    Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_edit);
        mContext = getApplicationContext();
        imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);

        ui_Location_Edit_TopBar = findViewById(R.id.ui_Location_Edit_TopBar);
        tv_TopBar_Title = ui_Location_Edit_TopBar.findViewById(R.id.tv_TopBar_Title);
        iv_TopBar_Back = ui_Location_Edit_TopBar.findViewById(R.id.iv_TopBar_Back);

        tv_Location_Edit_Save = findViewById(R.id.tv_Location_Edit_Save);
        tv_Location_Edit_Count = findViewById(R.id.tv_Location_Edit_Count);
        et_Location_Edit_Location = findViewById(R.id.et_Location_Edit_Location);

        tv_TopBar_Title.setText(CommonFunc.getInstance().getStr(mContext.getResources(), R.string.TITLE_LOCATION_EDIT));
        iv_TopBar_Back.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View view) {
                imm.hideSoftInputFromWindow(et_Location_Edit_Location.getWindowToken(), 0);
                finish();
            }
        });

        et_Location_Edit_Location.setText(TKManager.getInstance().MyData.GetUserLocation());
        CommonFunc.getInstance().setEditTextMaxSize(et_Location_Edit_Location, CommonData.LocationMaxSize);
        if(CommonFunc.getInstance().CheckStringNull(TKManager.getInstance().MyData.GetUserLocation()))
            tv_Location_Edit_Count.setText(Integer.toString(CommonData.LocationMaxSize));
        else
            tv_Location_Edit_Count.setText(Integer.toString(CommonData.LocationMaxSize - TKManager.getInstance().MyData.GetUserLocation().length()));

        et_Location_Edit_Location.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                tv_Location_Edit_Count.setText(Integer.toString(CommonData.LocationMaxSize - s.length()));
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        tv_Location_Edit_Save.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View view) {
                String tempLocation  = et_Location_Edit_Location.getText().toString();

                if(CommonFunc.getInstance().CheckStringNull(tempLocation) == false)
                {
                    TKManager.getInstance().MyData.SetUserLocation(tempLocation);
                    FirebaseManager.getInstance().SetUserLocation();
                }
                finish();
            }
        });

        et_Location_Edit_Location.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                switch (actionId) {
                    case EditorInfo.IME_ACTION_DONE:
                        // 검색 동작
                        imm.hideSoftInputFromWindow(et_Location_Edit_Location.getWindowToken(), 0);
                        break;
                    default:
                        // 기본 엔터키 동작
                        imm.hideSoftInputFromWindow(et_Location_Edit_Location.getWindowToken(), 0);
                        return false;
                }
                return true;
            }
        });
    }
}
