package fifty.fiftyhouse.com.fifty.DataBase;

import fifty.fiftyhouse.com.fifty.DialogFunc;

public class FilterData {
    public int Distance = 100;
    public int MinAge = 10;
    public int MaxAge = 100;

    public int Gender = 0;
    public int Connect = 0;


    public void SetDistance(int value)
    {
        Distance = value;
    }
    public int GetDistance()
    {
        return Distance;
    }

    public void SetMinAge(int value)
    {
        MinAge = value;
    }
    public int GetMinAge()
    {
        return MinAge;
    }

    public void SetMaxAge(int value)
    {
        MaxAge = value;
    }
    public int GetMaxAge()
    {
        return MaxAge;
    }

    public void SetGender(int value)
    {
        Gender = value;
    }
    public int GetGender()
    {
        return Gender;
    }

    public void SetConnect(int value)
    {
        Connect = value;
    }
    public int GetConnect()
    {
        return Connect;
    }
}
