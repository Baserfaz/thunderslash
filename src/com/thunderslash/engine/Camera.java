package com.thunderslash.engine;

import com.thunderslash.gameobjects.GameObject;
import com.thunderslash.utilities.Coordinate;
import com.thunderslash.utilities.Util;

import java.awt.Rectangle;

public class Camera {

    private Rectangle cameraBounds;
    private GameObject followTarget;
    private boolean following;

    private boolean firstPass = true;
    
    public Camera() {
        this.cameraBounds = new Rectangle(0, 0,
                Game.CAMERA_WIDTH, Game.CAMERA_HEIGHT);
        this.followTarget = null;
        
        // set this false if we want static camera
        this.following = true;
    }

    public void tick() {
        if(this.isFollowing()) {
            // current position
            int camX = this.getCameraBounds().x;
            int camY = this.getCameraBounds().y;
          
            // calculate new camera position
            Coordinate target = Util.calculateCameraPos();
          
            // apply smoothing
            camX += (target.x - camX) * Game.CAMERA_SMOOTH_MULT;
            camY += (target.y - camY) * Game.CAMERA_SMOOTH_MULT;
          
            // on first pass instantly 
            // focus on player, after that
            // use smoothing.
            if(this.firstPass) {
                firstPass = false;
                // no smoothing
                this.Update(target.x, target.y);
            } else {
                // smoothing
                this.Update((int)camX, (int)camY);
            }
        }
    }
    
    public void setFollowTarget(GameObject obj) { this.followTarget = obj; }
    public GameObject getFollowTarget() { return this.followTarget; }
    public void Update(Coordinate pos, int width, int height) { 
        cameraBounds.setBounds(pos.x, pos.y, width, height); 
    }
    public void Update(int x, int y) {
        cameraBounds.setBounds(x, y, Game.CAMERA_WIDTH, Game.CAMERA_HEIGHT);
    }
    public Rectangle getCameraBounds() { return cameraBounds; }
    public boolean isFollowing() { return following; }
    public void setFollowing(boolean allowFollowing) {
        this.following = allowFollowing;
    }
}
