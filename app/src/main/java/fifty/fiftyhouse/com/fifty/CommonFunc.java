package fifty.fiftyhouse.com.fifty;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.text.InputFilter;
import android.util.TypedValue;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Random;

import fifty.fiftyhouse.com.fifty.DataBase.UserData;
import fifty.fiftyhouse.com.fifty.Manager.FirebaseManager;
import fifty.fiftyhouse.com.fifty.activty.UserProfileActivity;

public class CommonFunc {

    private static CommonFunc _Instance;
    public static CommonFunc getInstance() {
        if (_Instance == null)
            _Instance = new CommonFunc();

        return _Instance;
    }
    private CommonFunc() {
    }

    private int mWidth;
    private  int mHeight;
    public int MainUserListSlotCount = 3;

    public Activity mCurActivity;

    public void GoMainActivity(Activity activity) {
        Intent intent = new Intent(activity, MainActivity.class);
        intent.setFlags(Intent. FLAG_ACTIVITY_SINGLE_TOP);
        activity.startActivity(intent);
        activity.finish();
    }


    public int getWidthByDevice()
    {
        return mWidth;
    }

    public void setWidthByDevice(int width)
    {
        mWidth = width;
    }

    public int getHeightByDevice()
    {
        return mHeight;
    }

    public void setHeightByDevice(int height)
    {
        mHeight = height;
    }

    public int convertPXtoDP(Resources res, int pixel)
    {
        float density = res.getDisplayMetrics().density;
        if(density == 1.0)
            density *= 4.0;
        else if(density == 1.5)
            density *= (8/3);
        else if(density == 2.0)
            density *= 2.0;
        return Math.round(pixel / density);
    }

    public int convertDPtoPX(Resources res, int dp)
    {
        int px = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, res.getDisplayMetrics());
        return px;
    }


    public String getStr(Resources res, int id)
    {
        if(res == null)
            return "ERROR";

        return res.getString(id);
    }

    public void ShowToast(Context context, String msg, boolean shortView)
    {
        if(shortView)
            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
    }

    public String GetCurrentDate()
    {
        long now = System.currentTimeMillis();
        Date date = new Date(now);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String getTime = sdf.format(date);
        return getTime;
    }

    int i = 0;
    public void MoveActivity(Activity from, Class  to)
    {
        i++;

        if(i == 22)
        {
            CommonFunc.getInstance().mCurActivity = from;
            from.startActivity(new Intent(from, to));
            from.finish();
        }
    }


    public void MoveUserActivity(Activity from)
    {
            from.startActivity(new Intent(from, UserProfileActivity.class));
            from.finish();
    }

    public void DrawImageByGlide(Context context, ImageView view, String src, boolean circle)
    {
        if(circle)
        {
            Glide.with(context).load(src)
                    .circleCrop()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(view);
        }
        else
            Glide.with(context).load(src)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(view);


    }

    public void setEditTextMaxSize(EditText et, int size)
    {
        InputFilter[] FilterArray = new InputFilter[1];
        FilterArray[0] = new InputFilter.LengthFilter(size);
        et.setFilters(FilterArray);
    }




    //////////////////////////////////////////////////////////////////////////////////////////////////
    /// 테스트 함수 //
    //////////////////////////////////////////////////////////////////////////////////////////////////

    // 더미 집어넣기
    public void AddDummy(int count)
    {

        String[] tempThumb = new String[15];

        tempThumb[0] = "https://firebasestorage.googleapis.com/v0/b/fifty-e2d17.appspot.com/o/dummy_0.jpg?alt=media&token=ca161fa5-6cb3-44a2-906f-c0dc6af3fe91";
        tempThumb[1] = "https://firebasestorage.googleapis.com/v0/b/fifty-e2d17.appspot.com/o/dummy_1.jpg?alt=media&token=419af5d2-3252-4d87-a06f-a5fd8c34bfea";
        tempThumb[2] = "https://firebasestorage.googleapis.com/v0/b/fifty-e2d17.appspot.com/o/dummy_10.jpg?alt=media&token=0518202d-94b8-490c-b8fe-4ef9c5d9d7b9";
        tempThumb[3] = "https://firebasestorage.googleapis.com/v0/b/fifty-e2d17.appspot.com/o/dummy_11.jpg?alt=media&token=ead97f7f-ed07-45c6-8dd9-5375dbea7c4d";
        tempThumb[4] = "https://firebasestorage.googleapis.com/v0/b/fifty-e2d17.appspot.com/o/dummy_12.jpg?alt=media&token=181cefd5-79f9-4d19-b76b-4c1eb696f72c";

        tempThumb[5] = "https://firebasestorage.googleapis.com/v0/b/fifty-e2d17.appspot.com/o/dummy_13.jpg?alt=media&token=d106be73-a6aa-45d3-b43e-22290dcb29d6";
        tempThumb[6] = "https://firebasestorage.googleapis.com/v0/b/fifty-e2d17.appspot.com/o/dummy_14.jpg?alt=media&token=b846e5ba-b14d-4605-b4cf-28e273012686";
        tempThumb[7] = "https://firebasestorage.googleapis.com/v0/b/fifty-e2d17.appspot.com/o/dummy_15.jpg?alt=media&token=678f4493-3290-4cc9-ba32-633258b9198e";
        tempThumb[8] = "https://firebasestorage.googleapis.com/v0/b/fifty-e2d17.appspot.com/o/dummy_2.jpg?alt=media&token=b161ad73-7c32-4f9b-884b-933e1f45ce4b";
        tempThumb[9] = "https://firebasestorage.googleapis.com/v0/b/fifty-e2d17.appspot.com/o/dummy_3.jpg?alt=media&token=1885ea33-2007-4aaa-a5ee-7c469f605c36";

        tempThumb[10] = "https://firebasestorage.googleapis.com/v0/b/fifty-e2d17.appspot.com/o/dummy_4.jpg?alt=media&token=0b6a12a9-b07e-4b7b-ab19-f17be557a01b";
        tempThumb[11] = "https://firebasestorage.googleapis.com/v0/b/fifty-e2d17.appspot.com/o/dummy_5.jpg?alt=media&token=3541331d-1524-46b8-8d6c-174bcc75bb42";
        tempThumb[12] = "https://firebasestorage.googleapis.com/v0/b/fifty-e2d17.appspot.com/o/dummy_6.jpg?alt=media&token=d9a22103-c78b-460a-98a9-9559051501c1";
        tempThumb[13] = "https://firebasestorage.googleapis.com/v0/b/fifty-e2d17.appspot.com/o/dummy_7.jpg?alt=media&token=a3baf274-b2eb-4b16-a537-33d770a94341";
        tempThumb[14] = "https://firebasestorage.googleapis.com/v0/b/fifty-e2d17.appspot.com/o/dummy_8.jpg?alt=media&token=1fbada1a-ec61-4d53-a952-1622c21f1423";

        Random rnd = new Random();

            for(int i=0; i< count; i++)
            {
                UserData tempData = new UserData();
                String[] fav = new String[3];
                fav[0] = "Book";
                fav[1] = "Cook";
                fav[2] = "Golf";

                String[] img = new String[3];
                img[0] = tempThumb[rnd.nextInt(15)];
                img[1] = tempThumb[rnd.nextInt(15)];
                img[2] = tempThumb[rnd.nextInt(15)];


               // tempData.SetUserData(Integer.toString(i), Integer.toString(i), randomHangulName(), fav, tempThumb[rnd.nextInt(15)], tempThumb[rnd.nextInt(15)], rnd.nextInt(20) + 50, rnd.nextInt(2));
                //FirebaseManager.getInstance().SetUserDataOnFireBase(CommonData.CollentionType.USERS, Integer.toString(i), tempData);
                for(int j = 0; j<fav.length; j++)
                    FirebaseManager.getInstance().SetUserFavoriteOnFireBase(fav[j], Integer.toString(i), Integer.toString(i));


                FirebaseManager.getInstance().randomList(Integer.toString(i));
            }
    }

    public static String randomHangulName() {
        List<String> 성 = Arrays.asList("김", "이", "박", "최", "정", "강", "조", "윤", "장", "임", "한", "오", "서", "신", "권", "황", "안",
                "송", "류", "전", "홍", "고", "문", "양", "손", "배", "조", "백", "허", "유", "남", "심", "노", "정", "하", "곽", "성", "차", "주",
                "우", "구", "신", "임", "나", "전", "민", "유", "진", "지", "엄", "채", "원", "천", "방", "공", "강", "현", "함", "변", "염", "양",
                "변", "여", "추", "노", "도", "소", "신", "석", "선", "설", "마", "길", "주", "연", "방", "위", "표", "명", "기", "반", "왕", "금",
                "옥", "육", "인", "맹", "제", "모", "장", "남", "탁", "국", "여", "진", "어", "은", "편", "구", "용");
        List<String> 이름 = Arrays.asList("가", "강", "건", "경", "고", "관", "광", "구", "규", "근", "기", "길", "나", "남", "노", "누", "다",
                "단", "달", "담", "대", "덕", "도", "동", "두", "라", "래", "로", "루", "리", "마", "만", "명", "무", "문", "미", "민", "바", "박",
                "백", "범", "별", "병", "보", "빛", "사", "산", "상", "새", "서", "석", "선", "설", "섭", "성", "세", "소", "솔", "수", "숙", "순",
                "숭", "슬", "승", "시", "신", "아", "안", "애", "엄", "여", "연", "영", "예", "오", "옥", "완", "요", "용", "우", "원", "월", "위",
                "유", "윤", "율", "으", "은", "의", "이", "익", "인", "일", "잎", "자", "잔", "장", "재", "전", "정", "제", "조", "종", "주", "준",
                "중", "지", "진", "찬", "창", "채", "천", "철", "초", "춘", "충", "치", "탐", "태", "택", "판", "하", "한", "해", "혁", "현", "형",
                "혜", "호", "홍", "화", "환", "회", "효", "훈", "휘", "희", "운", "모", "배", "부", "림", "봉", "혼", "황", "량", "린", "을", "비",
                "솜", "공", "면", "탁", "온", "디", "항", "후", "려", "균", "묵", "송", "욱", "휴", "언", "령", "섬", "들", "견", "추", "걸", "삼",
                "열", "웅", "분", "변", "양", "출", "타", "흥", "겸", "곤", "번", "식", "란", "더", "손", "술", "훔", "반", "빈", "실", "직", "흠",
                "흔", "악", "람", "뜸", "권", "복", "심", "헌", "엽", "학", "개", "롱", "평", "늘", "늬", "랑", "얀", "향", "울", "련");
        Collections.shuffle(성);
        Collections.shuffle(이름);
        return 성.get(0) + 이름.get(0) + 이름.get(1);
    }

}
