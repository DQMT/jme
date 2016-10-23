/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jme3test.helloworld.blocks;

import com.jme3.asset.AssetManager;
import com.jme3.scene.*;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Box;
import com.jme3.texture.Texture;

/**
 *
 * @author Administrator
 */
public class DirtBlockFactory {

    public static Geometry  getDirtBlock(AssetManager assetManager , Vector3f position) {
      
        Box cube1Mesh = new Box(1f, 1f, 1f);
        Geometry cube1Geo = new Geometry("My Textured Box", cube1Mesh);
        cube1Geo.setLocalTranslation(position);
        Material cube1Mat = new Material(assetManager,
                "Common/MatDefs/Misc/Unshaded.j3md");
//        Texture cube1Tex = assetManager.loadTexture(
//                "Interface/Logo/Monkey.jpg");
         Texture cube1Tex = assetManager.loadTexture(
                "minecraft/textures/blocks/dirt.png");
         
        cube1Mat.setTexture("ColorMap", cube1Tex);
        cube1Geo.setMaterial(cube1Mat);
        return cube1Geo;
    }
    
       public static Geometry  getDirtBlock(AssetManager assetManager , Vector3f position,int i) {
      
        Box cube1Mesh = new Box(1f, 1f, 1f);
        Geometry cube1Geo = new Geometry("My Textured Box", cube1Mesh);
        cube1Geo.setLocalTranslation(position);
        Material cube1Mat = new Material(assetManager,
                "Common/MatDefs/Misc/Unshaded.j3md");
//        Texture cube1Tex = assetManager.loadTexture(
//                "Interface/Logo/Monkey.jpg");
//        cube1Mat.setTexture("ColorMap", cube1Tex);
        if(i % 2 == 0 )
        cube1Mat.setColor("Color", ColorRGBA.Black);
        cube1Geo.setMaterial(cube1Mat);
        return cube1Geo;
    }
       
}
