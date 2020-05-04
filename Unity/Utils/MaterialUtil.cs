using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class MaterialUtil : MonoBehaviour {

    public const string MATERIAL_TYPE_SPRITE_DEFAULT = "Sprites/Default";
    public const string MATERIAL_TYPE_PARTICLE_ADDITIVE = "Legacy Shaders/Particles/Additive";


    public static Material GetNewMaterial(string type)
    {
        return new Material(Shader.Find(type));
    }

    public static Material GetNewSpriteDefaultMaterial()
    {
        return new Material(Shader.Find(MATERIAL_TYPE_SPRITE_DEFAULT));
    }
}
