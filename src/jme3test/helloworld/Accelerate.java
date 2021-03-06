/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jme3test.helloworld;

import com.jme3.app.SimpleApplication;
import com.jme3.bounding.BoundingBox;
import com.jme3.font.BitmapText;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.light.DirectionalLight;
import com.jme3.light.PointLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.terrain.geomipmap.TerrainLodControl;
import com.jme3.terrain.geomipmap.TerrainQuad;
import com.jme3.terrain.geomipmap.lodcalc.DistanceLodCalculator;
import com.jme3.terrain.heightmap.AbstractHeightMap;
import com.jme3.terrain.heightmap.ImageBasedHeightMap;
import com.jme3.texture.Texture;
import com.jme3.texture.Texture.WrapMode;

public class Accelerate extends SimpleApplication implements ActionListener {

    private TerrainQuad terrain;
    Material matTerrain;
    Material matWire;
    boolean wireframe = false;
    boolean triPlanar = false;
    boolean wardiso = false;
    boolean minnaert = false;
    protected BitmapText hintText;
    PointLight pl;
    Geometry lightMdl;
    private float dirtScale = 16;

    private enum Actions {

        LEFT, RIGHT, UP, DOWN;
    }
    private final int CAMERA_HEIGHT = 250;
    private final int STARTING_CAM_SPEED = 1;
    private final Vector3f CAM_DIRECTION = new Vector3f(0, -0.3f, 1f);
    private final Vector3f CAM_LOCATION = new Vector3f(-1, 0, -CAMERA_HEIGHT);
    private final Vector3f walkDirection = new Vector3f();
    private boolean left = false;
    private boolean right = false;
    private boolean up = false;
    private boolean down = false;
    private float camSpeed = STARTING_CAM_SPEED;

    private void initCamera() {
        cam.setLocation(CAM_LOCATION);
        cam.lookAtDirection(CAM_DIRECTION.normalizeLocal(), Vector3f.UNIT_Y.clone().addLocal(0, 2, 0));
    }

    public static void main(String[] args) {
        Accelerate app = new Accelerate();
        app.start();
    }

    @Override
    public void simpleInitApp() {
        setupKeys();
        initTerrain();
        initCamera();
    }

    @Override
    public void simpleUpdate(float tpf) {
        updateMovement(tpf);
    }

    private void updateMovement(float tpf) {
        if (left || right || up || down) {
            // here we increment the speed using the tpf
            camSpeed += tpf * 2;
            System.out.print("Camera speed " + camSpeed + ";  Location:" + cam.getLocation().toString()
                    + ";  direction:"+cam.getDirection()+"walkDirection:" + walkDirection.toString());
        }
        Vector3f camDir = cam.getDirection().clone().multLocal(camSpeed);
        Vector3f camLeft = cam.getLeft().clone().multLocal(camSpeed);
        // adjust position of rigid body for collisions
        walkDirection.set(cam.getLocation().clone());
        if (left) {
            walkDirection.addLocal(camLeft);
        }
        if (right) {
            walkDirection.addLocal(camLeft.negate());
        }
        if (up) {
            walkDirection.addLocal(camDir);
        }
        if (down) {
            walkDirection.addLocal(camDir.negate());
        }
//        player.setWalkDirection(walkDirection);
        cam.setLocation(walkDirection);
        if (left || right || up || down) {
            System.out.println("; new walkDirection:" + walkDirection);
        }
    }

    private void initTerrain() {
        // First, we load up our textures and the heightmap texture for the terrain

        // TERRAIN TEXTURE material
        matTerrain = new Material(assetManager, "Common/MatDefs/Terrain/TerrainLighting.j3md");
        matTerrain.setBoolean("useTriPlanarMapping", false);
        matTerrain.setFloat("Shininess", 0.0f);

        // ALPHA map (for splat textures)
        matTerrain.setTexture("AlphaMap", assetManager.loadTexture("Textures/Terrain/splat/alpha1.png"));
        matTerrain.setTexture("AlphaMap_1", assetManager.loadTexture("Textures/Terrain/splat/alpha2.png"));

        // HEIGHTMAP image (for the terrain heightmap)
        Texture heightMapImage = assetManager.loadTexture("Textures/Terrain/splat/mountains512.png");

        // GRASS texture
        Texture grass = assetManager.loadTexture("Textures/Terrain/splat/grass.jpg");
        grass.setWrap(WrapMode.Repeat);

        // DIRT texture
        Texture dirt = assetManager.loadTexture("Textures/Terrain/splat/dirt.jpg");
        dirt.setWrap(WrapMode.Repeat);
        matTerrain.setTexture("DiffuseMap", dirt);
        matTerrain.setFloat("DiffuseMap_0_scale", dirtScale);

        // ROCK texture
        Texture rock = assetManager.loadTexture("Textures/Terrain/splat/road.jpg");
        rock.setWrap(WrapMode.Repeat);

        // BRICK texture
        Texture brick = assetManager.loadTexture("Textures/Terrain/BrickWall/BrickWall.jpg");
        brick.setWrap(WrapMode.Repeat);

        // RIVER ROCK texture
        Texture riverRock = assetManager.loadTexture("Textures/Terrain/Pond/Pond.jpg");
        riverRock.setWrap(WrapMode.Repeat);

        // WIREFRAME material
        matWire = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        matWire.getAdditionalRenderState().setWireframe(true);
        matWire.setColor("Color", ColorRGBA.Green);


        // CREATE HEIGHTMAP
        AbstractHeightMap heightmap = null;
        try {
            heightmap = new ImageBasedHeightMap(heightMapImage.getImage(), 0.5f);
            heightmap.load();
            heightmap.smooth(0.9f, 1);

        } catch (Exception e) {
            e.printStackTrace();
        }

        /*
         * Here we create the actual terrain. The tiles will be 65x65, and the total size of the
         * terrain will be 513x513. It uses the heightmap we created to generate the height values.
         */
        /**
         * Optimal terrain patch size is 65 (64x64).
         * The total size is up to you. At 1025 it ran fine for me (200+FPS), however at
         * size=2049, it got really slow. But that is a jump from 2 million to 8 million triangles...
         */
        terrain = new TerrainQuad("terrain", 65, 513, heightmap.getHeightMap());//, new LodPerspectiveCalculatorFactory(getCamera(), 4)); // add this in to see it use entropy for LOD calculations
        TerrainLodControl control = new TerrainLodControl(terrain, getCamera());
        control.setLodCalculator(new DistanceLodCalculator(65, 2.7f)); // patch size, and a multiplier
        terrain.addControl(control);
        terrain.setMaterial(matTerrain);
        terrain.setModelBound(new BoundingBox());
        terrain.updateModelBound();
        terrain.setLocalTranslation(0, -100, 0);
        terrain.setLocalScale(1f, 1f, 1f);
        rootNode.attachChild(terrain);


        DirectionalLight light = new DirectionalLight();
        light.setDirection((new Vector3f(-0.5f, -0.5f, -0.5f)).normalize());
        rootNode.addLight(light);
    }

    private void setupKeys() {
        inputManager.addMapping(Actions.LEFT.name(), new KeyTrigger(KeyInput.KEY_A));
        inputManager.addMapping(Actions.RIGHT.name(), new KeyTrigger(KeyInput.KEY_D));
        inputManager.addMapping(Actions.UP.name(), new KeyTrigger(KeyInput.KEY_W));
        inputManager.addMapping(Actions.DOWN.name(), new KeyTrigger(KeyInput.KEY_S));
        inputManager.addListener(this, Actions.LEFT.name());
        inputManager.addListener(this, Actions.RIGHT.name());
        inputManager.addListener(this, Actions.UP.name());
        inputManager.addListener(this, Actions.DOWN.name());
    }

    /**
     * These are our custom actions triggered by key presses. We do not walk yet, we just keep track of the direction
     * the user pressed.
     */
    @Override
    public void onAction(String name, boolean keyPressed, float tpf) {
        System.out.println("name:" + name + "; keyPressed:" + keyPressed);
        if (name.equals(Actions.LEFT.name())) {
            left = keyPressed;
            if (!keyPressed) { // if the key isn't pressed anymore reset the speed to the initial value
                camSpeed = STARTING_CAM_SPEED;
            }
        } else if (name.equals(Actions.RIGHT.name())) {
            right = keyPressed;
            if (!keyPressed) {
                camSpeed = STARTING_CAM_SPEED;
            }
        } else if (name.equals(Actions.UP.name())) {
            up = keyPressed;
            if (!keyPressed) {
                camSpeed = STARTING_CAM_SPEED;
            }
        } else if (name.equals(Actions.DOWN.name())) {
            down = keyPressed;
            if (!keyPressed) {
                camSpeed = STARTING_CAM_SPEED;
            }
        }
    }
}