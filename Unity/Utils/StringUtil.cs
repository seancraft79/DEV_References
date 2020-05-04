using System;
using System.Collections;
using System.Collections.Generic;
using System.Linq;
using System.Text.RegularExpressions;
using UnityEngine;

public class StringUtil
{
    public static string[] SplitString(string str)
    {
        string[] splittedString = new string[5];
        if(!string.IsNullOrEmpty(str))
        {
            splittedString = str.Split('.');
        }

        return splittedString;
    }

    public static string[] SplitString(string str, char splitter)
    {
        string[] splittedString = new string[5];
        if (!string.IsNullOrEmpty(str))
        {
            splittedString = str.Split(splitter);
        }

        return splittedString;
    }

    public static int[] SplitStringToIntArray(string str)
    {
        if (!string.IsNullOrEmpty(str))
        {
            string[] splittedString = str.Split('_');
            if (splittedString.Length > 0)
            {
                int[] array = new int[splittedString.Length];
                for (int i = 0; i < splittedString.Length; i++)
                {
                    array[i] = ParseUtil.ParseStringToInt(splittedString[i]);
                }
                return array;
            }
        }

        return null;
    }

    public static int[] SplitStringToIntArray(string str, char splitter)
    {
        if (!string.IsNullOrEmpty(str))
        {
            string[] splittedString = str.Split(splitter);
            if(splittedString.Length > 0)
            {
                int[] array = new int[splittedString.Length];
                for (int i = 0; i < splittedString.Length; i++)
                {
                    array[i] = ParseUtil.ParseStringToInt(splittedString[i]);
                }
                return array;
            }
        }
        
        return null;
    }

    public static bool IsValidEmail(string email)
    {
        bool valid = Regex.IsMatch(email, @"[a-zA-Z0-9!#$%&'*+/=?^_`{|}~-]+(?:\.[a-zA-Z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-zA-Z0-9](?:[a-zA-Z0-9-]*[a-zA-Z0-9])?\.)+[a-zA-Z0-9](?:[a-zA-Z0-9-]*[a-zA-Z0-9])?");
        return valid;
    }

    public static string ReplaceAllWhiteSpaces(string str)
    {
        if (str == null) return null;
        return Regex.Replace(str, @"\s+", String.Empty);
    }

    public static string ReplaceAllWhiteSpacesToLower(string str)
    {
        if (str == null) return null;
        return Regex.Replace(str, @"\s+", String.Empty).ToLower();
    }

    public static string GetNormalizedString(string str)
    {
        if (str == null) return null;
        return ReplaceAllWhiteSpacesToLower(str);
    }

    public static string ReplaceString(string str, string pattern, string replacement)
    {
        return Regex.Replace(str, pattern, replacement);
    }

    /// <summary>
    /// 한글 초성 검색
    /// </summary>
    /// <param name="wordList">찾고자 하는 문자 리스트</param>
    /// <param name="find">찾고자 하는 문자(초성)</param>
    public static List<string> SearchKoWordByInitialSound(List<string> wordList, string find)
    {
        char[] chr = { 'ㄱ', 'ㄲ', 'ㄴ', 'ㄷ', 'ㄸ', 'ㄹ', 'ㅁ', 'ㅂ', 'ㅃ', 'ㅅ', 'ㅆ',
            'ㅇ', 'ㅈ', 'ㅉ', 'ㅊ','ㅋ','ㅌ', 'ㅍ', 'ㅎ' };
        string[] str = { "가", "까", "나", "다", "따", "라", "마", "바", "빠", "사", "싸",
            "아", "자", "짜", "차","카","타", "파", "하" };
        int[] chrint = {44032,44620,45208,45796,46384,46972,47560,48148,48736,49324,49912,
            50500,51088,51676,52264,52852,53440,54028,54616,55204};
        
        string pattern = "";
        for (int i = 0; i < find.Length; i++)
        {
            if (find[i] >= 'ㄱ' &&  find[i] < '가')
            {
                for (int j = 0; j < chr.Length; j++)
                {
                    if (find[i] == chr[j])
                    {
                        pattern += "[" + str[j] + "-" + (char)(chrint[j + 1] - 1) + "]";
                        //pattern += string.Format("[{0}-{1}]", str[j], (char)(chrint[j + 1] - 1));
                    }
                }
            }
            else
            {
                pattern += find[i];
            }               
        }
        return wordList.Where(e => Regex.IsMatch(e.ToString(),pattern)).ToList();
    }
    /*
     List<string> testList = new List<string>() {
        "가시나무", "소나무", "목련", "백일홍", "청단풍", "홍단풍","가이즈까향나무","감나무","개나리","금송", "고수"
        , "누나", "나인", "소인"
    };
     */
}
