package fifty.fiftyhouse.com.fifty.DataBase;

import fifty.fiftyhouse.com.fifty.CommonData;

public class NoticeData {

    public String Title = null;
    public String Content = null;
    public long Date = 100;
    public int NoticeType = CommonData.NOTICE_TYPE_STR;
    public boolean AppOpenShow = false;

    public void SetTitle(String title)
    {
        Title = title;
    }
    public String GetTitle()
    {
        return Title;
    }

    public void SetContent(String content)
    {
        Content = content;
    }
    public String GetContent()
    {
        return Content;
    }

    public void SetDate(long date)
    {
        Date = date;
    }
    public long GetDate()
    {
        return Date;
    }

    public void SetNoticeType(int type)
    {
        NoticeType = type;
    }
    public int GetNoticeType()
    {
        return NoticeType;
    }

    public void SetAppOpenShow(boolean show)
    {
        AppOpenShow = show;
    }
    public boolean GetAppOpenShow()
    {
        return AppOpenShow;
    }
}
