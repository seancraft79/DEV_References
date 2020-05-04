using System;
using System.IO;
using System.Threading;
using System.Threading.Tasks;
using UnityEngine;
using UnityEngine.UI;

public class FileUtil : MonoBehaviour
{
    /// <summary>
    /// 폴더에 찾는 파일이 있는지 확인할수 있는 함수
    /// </summary>
    /// <param name="folderPath">찾는 폴더 경로</param>
    /// <param name="filePath">찾는 파일의 경로이름</param>
    /// <returns></returns>
    public static bool IsFileExist(string folderPath, string filePath)
    {
        DirectoryInfo dir = new DirectoryInfo(folderPath);
        FileInfo[] fileInfo = dir.GetFiles("*.*", SearchOption.AllDirectories);
        Debug.Log("FileInfo : " + fileInfo.Length);
        foreach (FileInfo file in fileInfo)
        {
            if (File.Exists(filePath)) return true;
        }
        return false;
    }
    
    /// <summary>
    /// Get possible android external data directory path
    /// </summary>
    /// <returns></returns>
    public static string GetAndroidInternalFilesDir()
    {
        string[] potentialDirectories = {
            "/mnt/sdcard",
            "/sdcard",
            "/storage/sdcard0",
            "/storage/sdcard1"
        };

        if(Application.platform == RuntimePlatform.Android)
        {
            for(int i = 0; i < potentialDirectories.Length; i++)
            {
                if(Directory.Exists(potentialDirectories[i]))
                {
                    return potentialDirectories[i];
                }
            }
        }
        return "";
    }

    public static void CreateDirectory(string path)
    {
        if (!Directory.Exists(path)) Directory.CreateDirectory(path);
    }

    /// <summary>
    /// Create text file with message
    /// </summary>
    /// <param name="path"></param>
    public static void CreateTextFile(string path)
    {
        if (!File.Exists(path))
        {
            using (var sw = File.CreateText(path))
            {
                sw.WriteLine("File created");
            }
        }
    }
    
    /// <summary>
    /// Write message to existing text file
    /// </summary>
    /// <param name="path"></param>
    /// <param name="msg"></param>
    /// <returns></returns>
    async Task WriteMsgToFile(string path, string msg)
    {
        using (StreamWriter file =
            new StreamWriter(path, true, System.Text.Encoding.UTF8))
        {
            await file.WriteLineAsync(msg);
        }
    }
    
    /// <summary>
    /// Save Image byte data
    /// </summary>
    /// <param name="data"></param>
    /// <param name="path"></param>
    public void WriteImageData(byte[] data, string path)
    {
        File.WriteAllBytes(path, data);
    }

    /// <summary>
    /// Get Iamge byte data
    /// </summary>
    /// <param name="path"></param>
    /// <returns></returns>
    public byte[] ReadImageData(string path)
    {
        return File.ReadAllBytes(path);
    }
    
    /// <summary>
    /// Load texture from file
    /// </summary>
    /// <param name="filePath"></param>
    /// <param name="tex"></param>
    // public static void LoadTextureFromFileAsync(string filePath, Texture2D tex)
    // {
    //     //Use ThreadPool to avoid freezing
    //     ThreadPool.QueueUserWorkItem(delegate
    //     {
    //         bool success = false;
    //
    //         byte[] imageBytes;
    //         FileStream fileStream = new FileStream(filePath, FileMode.Open, FileAccess.Read, FileShare.Read, 4096, true);
    //
    //         try
    //         {
    //             int length = (int)fileStream.Length;
    //             imageBytes = new byte[length];
    //             int count;
    //             int sum = 0;
    //
    //             // read until Read method returns 0
    //             while ((count = fileStream.Read(imageBytes, sum, length - sum)) > 0)
    //                 sum += count;
    //
    //             success = true;
    //         }
    //         finally
    //         {
    //             fileStream.Close();
    //         }
    //
    //         //Create Texture2D from the imageBytes in the main Thread if file was read successfully
    //         if (success)
    //         {
    //             UnityMainThreadDispatcher.Instance().Enqueue(() =>
    //             {
    //                 tex.LoadImage(imageBytes);
    //             });
    //         }
    //     });
    // }
    public static void GetTextureFromFileAsync(string filePath, Action<Texture2D> cb)
    {
        //Use ThreadPool to avoid freezing
        ThreadPool.QueueUserWorkItem(delegate
        {
            bool success = false;

            byte[] imageBytes;
            FileStream fileStream = new FileStream(filePath, FileMode.Open, FileAccess.Read, FileShare.Read, 4096, true);

            try
            {
                int length = (int)fileStream.Length;
                imageBytes = new byte[length];
                int count;
                int sum = 0;

                // read until Read method returns 0
                while ((count = fileStream.Read(imageBytes, sum, length - sum)) > 0)
                    sum += count;

                Texture2D tex = new Texture2D(0, 0);
                tex.LoadImage(imageBytes);
                cb(tex);
            }
            finally
            {
                fileStream.Close();
            }
        });
    }
}
