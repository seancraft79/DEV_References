using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class ImageUtil : MonoBehaviour
{
    public static Sprite GetSpriteFromByteData(byte[] data)
    {
        var tex = new Texture2D(0, 0);

        tex.LoadImage(data);

        return GetSpriteFromTexture(tex);
    }

    public static Sprite GetSpriteFromTexture(Texture2D tex)
    {
        Rect rec = new Rect(0, 0, tex.width, tex.height);

        return Sprite.Create(tex, rec, new Vector2(0.5f, 0.5f), 100f);
    }

    public static byte[] GetByteDataFromSprite(Sprite sprite)
    {
        return sprite.texture.EncodeToPNG();
    }
}
