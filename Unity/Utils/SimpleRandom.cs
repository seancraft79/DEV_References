using System;
using System.Collections.Generic;
using System.Linq;
using UnityEngine;

namespace Knumbers
{
    /// <summary>
    /// Simple Random class which provide not repeated random values all the time
    /// </summary>
    public class SimpleRandom
    {
        // HOW TO USE (사용예시)
        public static void TestRandom()
        {
            int[] randomArray = SimpleRandom.GetRandomArray(0, 5, 5);
            List<int> randomList = SimpleRandom.GetRandomList(0, 5, 5);

            Debug.Log(" ==== Random Array ====");
            for (int i = 0; i < randomArray.Length; i++)
            {
                Debug.Log("i : " + i + "  array : " + randomArray[i] + "  list : " + randomList[i]);
            }
        }

        /// <summary>
        /// Returns array of integer
        /// </summary>
        public static int[] GetRandomArray(int min, int max, int howMany)
        {
            // check for impossible combinations
            if (howMany > max - min)
                throw new ArgumentException(String.Format("Range {0}-{1} is too small to have {2} unique random numbers.", min, max, howMany));

            int[] myNumbers = new int[howMany];

            // actual generation of random numbers
            System.Random randNum = new System.Random();

            // special case for range and howMany being equal
            if (howMany == max - min)
            {
                // Linq version
                // return Enumerable.Range(min, howMany).ToArray();

                // for loop version
                for (int i = 0; i < howMany; i++)
                    myNumbers[i] = i;

                // Shuffle data
                for (int i = myNumbers.Length - 1; i > 0; i--)
                {
                    int k = randNum.Next(i + 1);
                    int tmp = myNumbers[k];
                    myNumbers[k] = myNumbers[i];
                    myNumbers[i] = tmp;
                }
                return myNumbers;
            }


            for (int currIndex = 0; currIndex < howMany; currIndex++)
            {
                // generate a candidate
                int randCandidate = randNum.Next(min, max);

                // generate a new candidate as long as we don't get one that isn't in the array
                while (myNumbers.Contains(randCandidate))
                {
                    randCandidate = randNum.Next(min, max);
                }

                myNumbers[currIndex] = randCandidate;
            }

            return myNumbers;
        }

        /// <summary>
        /// Returns list of integer
        /// </summary>
        public static List<int> GetRandomList(int min, int max, int howMany)
        {
            if (max <= min || howMany < 0 ||
                    (howMany > max - min && max - min > 0))
            {
                throw new ArgumentOutOfRangeException("Range " + min + " to " + max +
                        " (" + ((Int64)max - (Int64)min) + " values), or count " + howMany + " is illegal");
            }

            HashSet<int> candidates = new HashSet<int>();
            System.Random random = new System.Random();

            for (int top = max - howMany; top < max; top++)
            {
                if (!candidates.Add(random.Next(min, top + 1)))
                {
                    candidates.Add(top);
                }
            }

            List<int> result = candidates.ToList();

            for (int i = result.Count - 1; i > 0; i--)
            {
                int k = random.Next(i + 1);
                int tmp = result[k];
                result[k] = result[i];
                result[i] = tmp;
            }
            return result;
        }

        public static NotRepeatRandom GetNotRepeatRandom()
        {
            return new NotRepeatRandom();
        }

        public static NotRepeatedRandomRange GetNotRepeatRandomRange(int start, int end)
        {
            return new NotRepeatedRandomRange(start, end);
        }
    }

    public class NotRepeatRandom
    {
        protected System.Random nrRandom;
        protected int pre = -1;

        public NotRepeatRandom()
        {
            var seed = Time.time.ToString();
            nrRandom = new System.Random(seed.GetHashCode());
        }

        public virtual int GetRandomValue()
        {
            return GetRandomValue(0, 100);
        }

        public virtual int GetRandomValue(int min, int max)
        {
            while(true)
            {
                int r = nrRandom.Next(min, max);
                if(r != pre)
                {
                    pre = r;
                    return pre;
                }
            }
        }
    }

    /// <summary>
    /// 전달된 시작에서 끝까지의 수 중에서 겹치지 않게 랜덤값을 리턴
    /// </summary>
    public class NotRepeatedRandomRange : NotRepeatRandom
    {
        int start = 0;
        int end = 10;
        int[] arr;
        int index = 0;

        public NotRepeatedRandomRange(int start, int end)
        {
            this.start = start;
            this.end = end;

            Init();
        }

        void Init()
        {
            var length = end - start;
            arr = new int[length];
            for (int i = 0; i < length; i++)
            {
                arr[i] = start + i;
            }

            // Shuffle array
            int n = arr.Length;
            System.Random rng = new System.Random();
            while (n > 1)
            {
                n--;
                int k = rng.Next(n + 1);
                var value = arr[k];
                arr[k] = arr[n];
                arr[n] = value;
            }
        }

        public int GetRangeRandomValue()
        {
            if (index < 0 || index > arr.Length - 1) index = 0;
            return arr[index++];
        }
    }
}
