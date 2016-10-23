/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jme3test.helloworld.environment;

/**
 *
 * @author Administrator
 */
public final class Environment_Constant {
    private int data;
    private Environment_Constant(int data ){
        this.data = data;
    }
    
    public static final int MAP_SIZE = 128;
    public static final int MAP_HEIGHT = 10;
    public static final int MAP_DEPTH = 10;
    
}
