package com.thunderslash.engine;

import com.thunderslash.data.Room;
import com.thunderslash.gameobjects.Actor;
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
        if(this.isFollowing() && this.followTarget != null) {
            
            Actor actor = null;
            
            if(this.followTarget instanceof Actor) {
                actor = (Actor) this.followTarget;
            }
            
            // current position
            int camX = this.getCameraBounds().x;
            int camY = this.getCameraBounds().y;
          
            // calculate new camera position
            // centers cam pos to the target.
            Coordinate target = Util.calculateCameraPos();
            
            // add offset to the target pos
            // target.y -= 100f;
            
            // apply camera smoothing
            camX -= (target.x + camX) * Game.CAMERA_SMOOTH_MULT;
                
            //if(actor != null) {
            //    if(actor.isGrounded()) {
                    camY -= (target.y + camY) * Game.CAMERA_SMOOTH_MULT;
            //    }
            //}
          
            // on first pass instantly 
            // focus on player, after that
            // use smoothing.
            if(this.firstPass) {
                firstPass = false;
                this.Update(-target.x, -target.y);
            } else {
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
