package com.thunderslash.engine;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.image.BufferStrategy;

import com.thunderslash.data.World;
import com.thunderslash.engine.Camera;
import com.thunderslash.engine.Renderer;
import com.thunderslash.engine.Window;
import com.thunderslash.enumerations.ActorType;
import com.thunderslash.enumerations.GameState;
import com.thunderslash.enumerations.SpriteType;
import com.thunderslash.utilities.ActorManager;
import com.thunderslash.utilities.SpriteCreator;
import com.thunderslash.utilities.Util;

public class Game extends Canvas implements Runnable {
    
    private static final long serialVersionUID = -8921773408778554392L;

    public static Game instance;

    public static final int WIDTH                  = 1280;			                   
    public static final int HEIGHT                 = 720;

    public static final String TITLE               = "Project Thunderslash";

    public static final int CAMERA_WIDTH           = Game.WIDTH;
    public static final int CAMERA_HEIGHT          = Game.HEIGHT;
    public static final float CAMERA_SMOOTH_MULT   = 0.05f;
                             
    public static final double FRAME_CAP           = 60.0;                             

    public static final String SPRITESHEETNAME     = "images/spritesheet.png";           
    public static final String FRAMICONPATH        = "/images/icon.png";      
    public static final String LEVELFOLDER         = "/levels/";
    
    public static final int SPRITEGRIDSIZE         = 32;
    public static final int SPRITESIZEMULT         = 5;

    public static final String CUSTOMFONTNAME      = "coders_crux";		               
    public static final String CUSTOMFONTEXTENSION = ".ttf";			               
    public static final String CUSTOMFONTFOLDER    = "coders_crux";		               
    
    public static final int LINEHEIGHT			   = 2;						           

    public static final int WORLD_ROOM_COUNT       = 1;
    
    public static final float GRAVITY              = 0.015f * Game.SPRITESIZEMULT;
    
    // ------------------------------
    // DEBUG

    public static boolean drawDebugInfo                  = true;
    public static final Color debugInfoColor             = Color.red;
    
    public static boolean drawCameraRect                 = false;
    public static final Color cameraRectColor            = Color.red;
    
    public static boolean drawCurrentBlock               = false;
    public static final Color currentBlockColor          = Color.green;
    
    public static boolean drawGameObjectRects            = false;
    public static final Color gameObjectRectColor        = Color.red;
    
    public static boolean drawActorCollisionPoints       = false;
    public static final Color actorCollisionPointColor   = Color.green;
    
    // -----------------------------

    public static boolean isPaused = false;
    
    private boolean isRunning = false;
    private int currentRoomIndex = 0;

    private Thread thread;
    private Window window;

    private Font customFont;
    private Camera camera;
    private SpriteCreator spriteCreator;
    private Handler handler;
    private GuiRenderer guiRenderer;
    private GameState gamestate;
    
    private World world;
    private ActorManager actorManager;
    private Point mousePos;

    public Game() {

        if(instance != null) return;
        Game.instance = this;

        // create object handler
        this.handler = new Handler();

        // create input listeners
        this.addKeyListener(new KeyInput());
        MouseInput mouseInput = new MouseInput();
        this.addMouseMotionListener(mouseInput);
        this.addMouseListener(mouseInput);

        // load custom font
        Util.loadCustomFont();

        // create window 
        this.window = new Window(Game.WIDTH,
                Game.HEIGHT, Game.TITLE, this);

        // create sprite managers and creators
        this.spriteCreator = new SpriteCreator(Game.SPRITESHEETNAME);
        this.guiRenderer = new GuiRenderer();       
        
        // create actor manager
        setActorManager(new ActorManager());

        // set gamestate
        this.gamestate = GameState.INGAME;
        
        // create camera
        this.camera = new Camera();
        
        // create world
        this.world = new World();
        
        // initiate first level
        this.world.initializeRoom(Game.instance.currentRoomIndex);
        
        // create mock up player actor
        actorManager.createPlayerInstance("Player", SpriteType.PLAYER, 3);
        
        // start game thread
        start();

        System.out.println("Game started succesfully!");
    }

    public synchronized void start() {
        thread = new Thread(this);
        thread.start();
        isRunning = true;
    }

    public synchronized void stop() {
        try {
            thread.join();
            isRunning = false;
        } catch (Exception e) { e.printStackTrace(); }
    }

    public void run() { 
        gameLoop();
    }

    private void gameLoop() {

        long lastTime = System.nanoTime();
        double unprocessedTime = 0;

        int frames = 0;
        long frameCounter = 0;

        final double frameTime = 1 / FRAME_CAP;
        final long SECOND = 1000000000L;

        boolean render = false;
        long now = 0l, passedTime = 0l;
        
        while(isRunning) {

            render = false;

            now = System.nanoTime();
            passedTime = now - lastTime;
            lastTime = now;
            
            // calculate tick in seconds
            unprocessedTime += passedTime / (double) SECOND;
            frameCounter += passedTime;
            
            while(unprocessedTime > frameTime) {
                
                render = true;
                unprocessedTime -= frameTime;
                
                if(this.gamestate == GameState.INGAME) {
                    if(Game.isPaused == false) {
                        tick();
                        this.camera.tick();
                    }
                }
                
                if(frameCounter >= SECOND) {
                    window.SetCustomTitle("FPS: " + frames);
                    frames = 0;
                    frameCounter = 0;
                }
            }

            // render the scene
            if(isRunning && render) {
                render();
                frames++;         
            }
        }
    }
    
    private void render() {
        BufferStrategy bs = this.getBufferStrategy();

        if(bs == null) {
            this.createBufferStrategy(3);
            return;
        }

        Graphics g = bs.getDrawGraphics();

        // DRAW GRAPHICS HERE ---------------------------------

        Renderer.preRender(g);

        // END DRAW -------------------------------------------

        g.dispose();
        bs.show();
    }

    private void tick() { handler.tick(); }
    public static void main(String args[]) { new Game(); }
    public Window getWindow() { return this.window; }
    public Font getCustomFont() { return customFont; }
    public void setCustomFont(Font customFont) { this.customFont = customFont; }
    public Camera getCamera() { return camera; }
    public void setCamera(Camera camera) { this.camera = camera; }
    public SpriteCreator getSpriteCreator() { return spriteCreator;}
    public void setSpriteCreator(SpriteCreator spriteCreator) { this.spriteCreator = spriteCreator; }
    public Handler getHandler() { return handler; }
    public void setHandler(Handler handler) { this.handler = handler; }

    public ActorManager getActorManager() {
        return actorManager;
    }

    public void setActorManager(ActorManager actorManager) {
        this.actorManager = actorManager;
    }

    public World getWorld() {
        return world;
    }

    public void setWorld(World world) {
        this.world = world;
    }

    public GameState getGamestate() {
        return gamestate;
    }

    public void setGamestate(GameState gamestate) {
        this.gamestate = gamestate;
    }

    public GuiRenderer getGuiRenderer() {
        return guiRenderer;
    }

    public void setGuiRenderer(GuiRenderer guiRenderer) {
        this.guiRenderer = guiRenderer;
    }

    public Point getMousePos() {
        return mousePos;
    }

    public void setMousePos(Point mousePos) {
        this.mousePos = mousePos;
    }

    public int getCurrentRoomIndex() {
        return currentRoomIndex;
    }

    public void setCurrentRoomIndex(int currentRoomIndex) {
        this.currentRoomIndex = currentRoomIndex;
    }

}
