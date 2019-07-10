package fifty.fiftyhouse.com.fifty.DataBase;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

public class ClubContextData {
    public String writerIndex;
    public String Context;
    public String Date;
    public int ContextType;

    public Map<String, String> ImgList = new LinkedHashMap<String, String>();
    public Map<String, ClubContextData> ReplyList = new LinkedHashMap<String, ClubContextData>();


    public void SetWriterIndex(String index){
        writerIndex = index;
    }
    public String GetWriterIndex(){
        return writerIndex;
    }

    public void SetContext(String context){
        Context = context;
    }
    public String GetContext(){
        return Context;
    }

    public void SetDate(String date){
        Date = date;
    }
    public String GetDate(){
        return Date;
    }

    public void SetContextType(int type){
        ContextType = type;
    }
    public int GetContextType(){
        return ContextType;
    }

    public void SetImg(String Index, String Img)
    {
        ImgList.put(Index, Img);
    }
    public Map<String, String>  GetImg() { return ImgList; }
    public String  GetImg(String Index)
    {
        return ImgList.get(Index);
    }
    public int  GetImgCount()
    {
        return ImgList.size();
    }
    public void ClearImg()
    {
        ImgList.clear();
    }

    public void SetReply(String Index, ClubContextData context)
    {
        ReplyList.put(Index, context);
    }
    public Map<String, ClubContextData>  GetReply() { return ReplyList; }
    public ClubContextData  GetReply(String Index)
    {
        return ReplyList.get(Index);
    }
    public int  GetReplyCount()
    {
        return ReplyList.size();
    }
    public void ClearReply()
    {
        ReplyList.clear();
    }
}
