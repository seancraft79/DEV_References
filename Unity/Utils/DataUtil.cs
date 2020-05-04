using System.Collections;
using System.Collections.Generic;
//using UnityEngine;
using System;

public static class DataUtil
{
    public static void ShuffleList<T>(this List<T> list)
    {
        int n = list.Count;

        Random rng = new Random();

        while (n > 1)
        {
            n--;
            int k = rng.Next(n + 1);
            T value = list[k];
            list[k] = list[n];
            list[n] = value;
        }
    }

    public static void ShuffleArray(this int[] array)
    {
        int n = array.Length;

        Random rng = new Random();

        while (n > 1)
        {
            n--;
            int k = rng.Next(n + 1);
            int value = array[k];
            array[k] = array[n];
            array[n] = value;
        }
    }

    public static void ShuffleArray<T>(this T[] array)
    {
        int n = array.Length;

        Random rng = new Random();

        while (n > 1)
        {
            n--;
            int k = rng.Next(n + 1);
            var value = array[k];
            array[k] = array[n];
            array[n] = value;
        }
    }

    public static T[] ReverseArray<T>(this T[] array)
    {
        var newArray = new T[array.Length];
        int idx = array.Length - 1;
        for (int i = 0; i < array.Length; i++)
        {
            newArray[idx--] = array[i];
        }
        return newArray;
    }

    public static int GetMinValueFromArray(int[] arr)
    {
        int max = arr[0];
        int min = arr[0];
        int minIndex = 0;

        for (int i = 0; i < arr.Length; i++)
        {
            if (arr[i] > max)
            {
                max = arr[i];
            }
            if (arr[i] < min)
            {
                min = arr[i];
                minIndex = i;
            }
        }

        return min;
    }
    
    public static int[] BubbleSort(int[] arr)
    {
        for (int i = 0; i < arr.Length; i++)
        {
            for (int k = 0; k < arr.Length - 1; k++)
            {
                if (arr[k] > arr[k + 1])
                {
                    int temp = arr[k];
                    arr[k] = arr[k + 1];
                    arr[k + 1] = temp;
                }
            }
        }
        return arr;
    }

	/// <summary>
	/// list에서 count개를 랜덤하게 뽑는다(중복없이)
	/// </summary>
	public static List<T> SampleList<T>(List<T> list, int count)
	{
		List<T> result = new List<T>(list);
		ShuffleList(result);
		result.RemoveRange(count, result.Count - count);
		return result;
	}

	public static void Swap<T>(ref T a, ref T b)
	{
		T temp = a;
		a = b;
		b = temp;
	}
}
