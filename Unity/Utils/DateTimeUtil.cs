using System;
using UnityEngine;

public class DateTimeUtil : MonoBehaviour
{
    /// <summary>
    /// targetDate 이 startDate 에 dueMonth(사용기간)를 더한 날짜보다 지났다면 false
    /// 아직 안지났다면  true
    /// </summary>
    /// <param name="startDate">시작날짜</param>
    /// <param name="targetDate">비교할 날짜</param>
    /// <param name="dueDate">사용기간</param>
    /// <returns></returns>
    public static bool IsTargetDateValid(DateTime startDate, DateTime targetDate, int dueDate)
    {
        startDate = startDate.AddDays(dueDate);
        // 왼쪽날짜가 나중이면 1, 같으면 0, 오른쪽이 나중이면 -1
        int compare = DateTime.Compare(startDate, targetDate);
        Debug.Log("CheckUserValidation startDate : " + startDate + "     targetDate : " + targetDate + "  compare : " + compare);
        if (compare >= 0) return true;
        return false;
    }

    public static bool GetUserValidationOfCurrentime()
    {
        // Debug
        return true;

        SetUserStartDate();

        DateTime startDate = new DateTime();
        DateTime.TryParse(GetUserStartDate(), out startDate);
        if (IsTargetDateValid(startDate, DateTime.Now, 14)) return true;
        return false;            
    }

    public static string GetDayString()
    {
        return DateTime.Now.Day.ToString();
    }

    public static int GetDayInt()
    {
        return DateTime.Now.Day;
    }

    public static string GetMonthString()
    {
        return DateTime.Now.Month.ToString();
    }

    public static int GetMonthInt()
    {
        return DateTime.Now.Month;
    }

    public static string GetYearString()
    {
        return DateTime.Now.Year.ToString();
    }

    public static int GetYearInt()
    {
        return DateTime.Now.Year;
    }

    public static string GetYearMonthDayString()
    {
        return DateTime.Now.ToString("yyyy/MM/dd");
    }

    public static string GetDateAndTimeString()
    {
        return DateTime.Now.ToString("yyyy/MM/dd HH:mm:ss");
    }

    public static void SetUserStartDate()
    {
        if (string.IsNullOrEmpty(GetUserStartDate()))
        {
            Debug.Log("SetUserStartDate : " + DateTime.Now.ToString());
            PlayerPrefs.SetString("userstartdate", DateTime.Now.ToString());
        }
    }

    public static string GetUserStartDate()
    {
        return PlayerPrefs.GetString("userstartdate", "");
    }

    // Local Time
    public static string GetNowString()
    {
        return DateTime.Now.ToString("yyyy-MM-dd-HH-mm-ss");
    }

    public static string GetShortDate()
    {
        return DateTime.Now.ToShortDateString();
    }

    public static string GetDate()
    {
        return DateTime.Now.ToString("yyyy-MM-dd-HH-mm-ss");
    }

    public static string GetLongDate()
    {
        return DateTime.Now.ToLongDateString();
    }

    public static DateTime GetLocalNow()
    {
        return DateTime.Now;
    }
    
    // UTC Time
    public static DateTime GetUTCNow()
    {
        return DateTime.UtcNow;
    }

    public static string GetUTCShortDate()
    {
        return DateTime.UtcNow.ToShortDateString();
    }

    public static string GetUTCDate()
    {
        return DateTime.UtcNow.ToString("yyyy-MM-dd-HH-mm-ss");
    }

    public static string GetUTCLongDate()
    {
        return DateTime.UtcNow.ToLongDateString();
    }

    public static DateTime GetDateTimeFromString(string time)
    {
        return DateTime.Parse(time);
    }

    public static DateTime ConvertLocalTimeToUTC(DateTime time)
    {
        return TimeZoneInfo.ConvertTimeToUtc(time);
    }

    public static DateTime ConvertUTCTimeToLocalTime(DateTime utcTime)
    {
        TimeZoneInfo localZone = TimeZoneInfo.Local;

        return TimeZoneInfo.ConvertTimeFromUtc(utcTime, localZone);
    }

    public static DateTime ConvertUTCToKoreaTime(DateTime time)
    {
        TimeZoneInfo tzi = TimeZoneInfo.FindSystemTimeZoneById("Korea Standard Time");
        return TimeZoneInfo.ConvertTimeFromUtc(time, tzi);
    }

    public static string ConvertUtcToKoreaTime(string time)
    {
        DateTime t = GetDateTimeFromString(time);
        return ConvertUTCToKoreaTime(t).ToString("yyyy-MM-dd T HH-mm-ss");
    }
}

/*
 
Dateline Standard Time
UTC-11
Aleutian Standard Time
Hawaiian Standard Time
Marquesas Standard Time
Alaskan Standard Time
UTC-09
Pacific Standard Time (Mexico)
UTC-08
Pacific Standard Time
US Mountain Standard Time
Mountain Standard Time (Mexico)
Mountain Standard Time
Central America Standard Time
Central Standard Time
Easter Island Standard Time
Central Standard Time (Mexico)
Canada Central Standard Time
SA Pacific Standard Time
Eastern Standard Time (Mexico)
Eastern Standard Time
Haiti Standard Time
Cuba Standard Time
US Eastern Standard Time
Paraguay Standard Time
Atlantic Standard Time
Venezuela Standard Time
Central Brazilian Standard Time
SA Western Standard Time
Pacific SA Standard Time
Turks And Caicos Standard Time
Newfoundland Standard Time
Tocantins Standard Time
E. South America Standard Time
SA Eastern Standard Time
Argentina Standard Time
Greenland Standard Time
Montevideo Standard Time
Saint Pierre Standard Time
Bahia Standard Time
UTC-02
Mid-Atlantic Standard Time
Azores Standard Time
Cape Verde Standard Time
UTC
Morocco Standard Time
GMT Standard Time
Greenwich Standard Time
W. Europe Standard Time
Central Europe Standard Time
Romance Standard Time
Central European Standard Time
W. Central Africa Standard Time
Namibia Standard Time
Jordan Standard Time
GTB Standard Time
Middle East Standard Time
Egypt Standard Time
E. Europe Standard Time
Syria Standard Time
West Bank Standard Time
South Africa Standard Time
FLE Standard Time
Israel Standard Time
Kaliningrad Standard Time
Libya Standard Time
Arabic Standard Time
Turkey Standard Time
Arab Standard Time
Belarus Standard Time
Russian Standard Time
E. Africa Standard Time
Iran Standard Time
Arabian Standard Time
Astrakhan Standard Time
Azerbaijan Standard Time
Russia Time Zone 3
Mauritius Standard Time
Georgian Standard Time
Caucasus Standard Time
Afghanistan Standard Time
West Asia Standard Time
Ekaterinburg Standard Time
Pakistan Standard Time
India Standard Time
Sri Lanka Standard Time
Nepal Standard Time
Central Asia Standard Time
Bangladesh Standard Time
Omsk Standard Time
Myanmar Standard Time
SE Asia Standard Time
Altai Standard Time
W. Mongolia Standard Time
North Asia Standard Time
N. Central Asia Standard Time
Tomsk Standard Time
China Standard Time
North Asia East Standard Time
Singapore Standard Time
W. Australia Standard Time
Taipei Standard Time
Ulaanbaatar Standard Time
North Korea Standard Time
Aus Central W. Standard Time
Transbaikal Standard Time
Tokyo Standard Time
Korea Standard Time
Yakutsk Standard Time
Cen. Australia Standard Time
AUS Central Standard Time
E. Australia Standard Time
AUS Eastern Standard Time
West Pacific Standard Time
Tasmania Standard Time
Vladivostok Standard Time
Lord Howe Standard Time
Bougainville Standard Time
Russia Time Zone 10
Magadan Standard Time
Norfolk Standard Time
Sakhalin Standard Time
Central Pacific Standard Time
Russia Time Zone 11
New Zealand Standard Time
UTC+12
Fiji Standard Time
Kamchatka Standard Time
Chatham Islands Standard Time
Tonga Standard Time
Samoa Standard Time
Line Islands Standard Time

 */
