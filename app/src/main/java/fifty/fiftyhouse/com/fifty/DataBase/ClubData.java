package fifty.fiftyhouse.com.fifty.DataBase;

import android.util.Log;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

public class ClubData  implements Cloneable{

    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    public void Clear()
    {
        ClubContext.clear();
        ClubMemberList.clear();
        ClubFavorite.clear();

        ClubMemberCount = 0;
        ClubContextCount = 0;

        ClubType = true;
        ClubMasterIndex = null;
        ClubThumbNail = null;

        ClubIndex = null;
        ClubName = null;
        ClubCreateDate = 0;
        ClubComment = null;

    }

    public String ClubIndex;

    public boolean ClubType;



    public String ClubName;
    public String ClubThumbNail;

    public String ClubMasterIndex;
    public Map<String, String> ClubMemberList = new LinkedHashMap<String, String>();
    public Map<String, ClubContextData> ClubContext = new LinkedHashMap<String, ClubContextData>();
    public Map<String, String> ClubFavorite = new LinkedHashMap<String, String>();

    public int ClubContextCount;
    public int ClubMemberCount;
    public long ClubCreateDate;
    public String ClubComment;

    public void SetClubIndex(String index) {
        ClubIndex = index;
    }

    public String GetClubIndex() {
        return ClubIndex;
    }

    public void SetClubComment(String vip)
    {
        ClubComment = vip;
    }
    public String GetClubComment()
    {
        return ClubComment;
    }

    public void SetClubCreateDate(long vip)
    {
        ClubCreateDate = vip;
    }
    public long GetClubCreateDate()
    {
        return ClubCreateDate;
    }

    public void SetClubType(boolean vip)
    {
        ClubType = vip;
    }
    public boolean GetClubType()
    {
        return ClubType;
    }

    public void SetClubName(String name) {
        ClubName = name;
    }
    public String GetClubName() {
        return ClubName;
    }

    public void SetClubThumb(String thumb) {
        ClubThumbNail = thumb;
    }
    public String GetClubThumb() {
        return ClubThumbNail;
    }

    public void SetClubMasterIndex(String index) {
        ClubMasterIndex = index;
    }
    public String GetClubMasterIndex() {
        return ClubMasterIndex;
    }

    public void AddClubMember(String index)
    {
        ClubMemberList.put(index, index);
    }
    public String GetClubMember(String index)
    {
        return ClubMemberList.get(index);
    }
    public Map<String, String>  GetClubMember()
    {
        return ClubMemberList;
    }
    public void  DelClubMember(String index)
    {
        ClubMemberList.remove(index);
    }

    public int GetClubMemberCount()
    {
        return ClubMemberList.size();
    }
    public Set GetClubMemberKeySet()
    {
        return ClubMemberList.keySet();
    }

    public void AddClubContext(String index, ClubContextData context)
    {
        ClubContext.put(index, context);
    }
    public ClubContextData GetClubContext(String index)
    {
        return ClubContext.get(index);
    }
    public void DelClubContext(String index)
    {
        ClubContext.remove(index);
    }
    public int GetClubContextCount()
    {
        return ClubContext.size();
    }
    public Set GetClubContextKeySet()
    {
        return ClubContext.keySet();
    }

    public void AddClubFavorite(String index, String name)
    {
        ClubFavorite.put(index, name);
    }
    public String GetClubFavorite(String index)
    {
        return ClubFavorite.get(index);
    }
    public int GetClubFavoriteCount()
    {
        return ClubFavorite.size();
    }
    public Set GetClubFavoriteKeySet()
    {
        return ClubFavorite.keySet();
    }

}
