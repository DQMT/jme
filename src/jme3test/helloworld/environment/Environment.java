/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jme3test.helloworld.environment;

import com.jme3.asset.AssetManager;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.collision.shapes.CollisionShape;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.bullet.util.CollisionShapeFactory;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import jme3test.helloworld.blocks.DirtBlockFactory;

/**
 *
 * @author Administrator
 */
public class Environment {

    private AssetManager assetManager;
    private Spatial block;
    private CollisionShape sceneShape;
    private RigidBodyControl rigidBlock;

    public Environment(AssetManager assetManager) {
        this.assetManager = assetManager;
    }

    public Node initialGround(Node node, BulletAppState bulletAppState) {
        for (int i = 0; i < Environment_Constant.MAP_SIZE; i++) {
            for (int j = 0; j < Environment_Constant.MAP_SIZE; j++) {
                node = addBlock(node,bulletAppState,new Vector3f(2 * (i - Environment_Constant.MAP_SIZE / 2), 0f, 2 * (j - Environment_Constant.MAP_SIZE / 2)),i+j);
//                sceneShape = CollisionShapeFactory.createMeshShape(block);
//                rigidBlock = new RigidBodyControl(sceneShape, 0);
//                block.addControl(rigidBlock);
//                node.attachChild(block);
//                bulletAppState.getPhysicsSpace().add(rigidBlock);
            }
        }
        return node;

    }

    public Node addBlock(Node node, BulletAppState bulletAppState, Vector3f position) {
        block = DirtBlockFactory.getDirtBlock(assetManager, position);
        sceneShape = CollisionShapeFactory.createMeshShape(block);
        rigidBlock = new RigidBodyControl(sceneShape, 0);
        block.addControl(rigidBlock);
        node.attachChild(block);
        bulletAppState.getPhysicsSpace().add(rigidBlock);
        return node;
    }
    
     public Node addBlock(Node node, BulletAppState bulletAppState, Vector3f position,int x) {
        block = DirtBlockFactory.getDirtBlock(assetManager, position,x);
        sceneShape = CollisionShapeFactory.createMeshShape(block);
        rigidBlock = new RigidBodyControl(sceneShape, 0);
        block.addControl(rigidBlock);
        node.attachChild(block);
        bulletAppState.getPhysicsSpace().add(rigidBlock);
        return node;
    }
     
}
