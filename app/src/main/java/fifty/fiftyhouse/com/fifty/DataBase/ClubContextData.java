package fifty.fiftyhouse.com.fifty.DataBase;

import android.util.Log;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

public class ClubContextData implements Cloneable{

    public Object clone() throws CloneNotSupportedException {

        return super.clone();

    }

    public String writerIndex;
    public String ContextIndex;
    public String Context;
    public String Date;
    public int ContextType;

    public Map<String, String> ImgList = new LinkedHashMap<String, String>();
    public Map<String, String> ReplyList = new LinkedHashMap<String, String>();

    public Map<String, ClubContextData> ReplyDataList = new LinkedHashMap<String, ClubContextData>();

    public void SetContextIndex(String index){
        ContextIndex = index;
    }
    public String GetContextIndex(){
        return ContextIndex;
    }

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

    public void SetReply(String Index, String context)
    {
        ReplyList.put(Index, context);
    }
    public Map<String, String>  GetReply() { return ReplyList; }
    public String  GetReply(String Index)
    {
        return ReplyList.get(Index);
    }
    public Set GetReplyKeySet()
    {
        return ReplyList.keySet();
    }
    public int  GetReplyCount()
    {
        return ReplyList.size();
    }
    public void ClearReply()
    {
        ReplyList.clear();
    }

    public void SetReplyData(String Index, ClubContextData context)
    {
        ReplyDataList.put(Index, context);
    }
    public Map<String, ClubContextData>  GetReplyData() { return ReplyDataList; }
    public ClubContextData  GetReplyData(String Index)
    {
        return ReplyDataList.get(Index);
    }
    public Set GetReplyDataKeySet()
    {
        return ReplyDataList.keySet();
    }
    public int  GetReplyDataCount()
    {
        return ReplyDataList.size();
    }
    public void ClearReplyData()
    {
        ReplyDataList.clear();
    }

}
