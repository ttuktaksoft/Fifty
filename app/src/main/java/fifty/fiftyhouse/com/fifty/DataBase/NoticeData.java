package fifty.fiftyhouse.com.fifty.DataBase;

public class NoticeData {
    public String Title = null;
    public String Content = null;
    public long Date = 100;

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
}
