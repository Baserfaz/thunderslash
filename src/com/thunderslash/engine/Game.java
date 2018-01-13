package com.thunderslash.engine;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.image.BufferStrategy;

import com.thunderslash.data.Session;
import com.thunderslash.data.World;
import com.thunderslash.engine.Camera;
import com.thunderslash.engine.Renderer;
import com.thunderslash.engine.Window;
import com.thunderslash.enumerations.GameState;
import com.thunderslash.enumerations.SpriteType;
import com.thunderslash.particles.EmitterManager;
import com.thunderslash.ui.GuiElementManager;
import com.thunderslash.utilities.ActorManager;
import com.thunderslash.utilities.Animator;
import com.thunderslash.utilities.SpriteCreator;
import com.thunderslash.utilities.Util;

public class Game extends Canvas implements Runnable {
    
    private static final long serialVersionUID = -8921773408778554392L;

    public static Game instance;

    public static final int WIDTH                  = 1280;			                   
    public static final int HEIGHT                 = 720;

    public static final String TITLE               = "Project Thunderslash";
    public static final String VERSION             = "v. 0.1a";

    public static final int CAMERA_WIDTH           = Game.WIDTH;
    public static final int CAMERA_HEIGHT          = Game.HEIGHT;
    public static final float CAMERA_SMOOTH_MULT   = 0.05f;
                             
    public static final double FRAME_CAP           = 60.0;                             

    public static final String SPRITESHEETNAME     = "/images/spritesheet.png";           
    public static final String FRAMICONPATH        = "/images/icon.png";      
    public static final String LEVELFOLDER         = "/levels/";
    
    public static final int SPRITEGRIDSIZE         = 32;
    public static final int SPRITESIZEMULT         = 5;

    public static final String CUSTOMFONTNAME      = "coders_crux";		               
    public static final String CUSTOMFONTEXTENSION = ".ttf";			               
    public static final String CUSTOMFONTFOLDER    = "coders_crux";		               
    
    public static final int TEXT_LINEHEIGHT		   = 2;						           

    public static final int WORLD_ROOM_COUNT       = 1;
    
    public static final float GRAVITY              = 0.015f * Game.SPRITESIZEMULT;
    
    // ------------------------------
    // DEBUG

    public static boolean drawDebugInfo                  = false;
    public static final Color debugInfoColor             = Color.white;
    
    public static boolean drawCameraRect                 = false;
    public static final Color cameraRectColor            = Color.red;
    
    public static boolean drawCurrentBlock               = false;
    public static final Color currentBlockColor          = Color.green;
    
    public static boolean drawGameObjectRects            = false;
    public static final Color gameObjectRectColor        = Color.red;
    
    public static boolean drawActorCollisionPoints       = false;
    public static final Color actorCollisionPointColor   = Color.green;
    
    public static boolean drawAttackBoxes                = false;
    public static final Color attackBoxDrawColor         = Color.red;
    
    // -----------------------------
    
    public static int FPS = 0;
    
    private boolean isRunning = false;
    public static boolean isPaused = false;
    
    private int currentRoomIndex = 0;
    private double timeBetweenFrames = 0.0;
    
    private Thread thread;
    private Window window;
    
    private Font customFont;
    private Camera camera;
    private SpriteCreator spriteCreator;
    private Handler handler;
    private GuiRenderer guiRenderer;
    private GuiElementManager guiElementManager;
    private EmitterManager emitterManager;
    private Renderer renderer;
    private GameState gamestate;
    
    private Animator animator;
    private World world;
    private ActorManager actorManager;
    private Point mousePos;

    private Session session;

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

        this.window = new Window(Game.WIDTH, Game.HEIGHT, Game.TITLE, this);
        this.spriteCreator = new SpriteCreator(Game.SPRITESHEETNAME);
        this.guiElementManager = new GuiElementManager();
        this.emitterManager = new EmitterManager();
        
        this.guiRenderer = new GuiRenderer();
        this.actorManager = new ActorManager();
        this.camera = new Camera();
        this.animator = new Animator();
        this.renderer = new Renderer();
        
        this.gamestate = GameState.MAINMENU;
        
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

    public void run() { this.gameloop(); }

    private void gameloop() {

        long lastTime = System.nanoTime();
        double unprocessedTime = 0;

        int frames = 0;
        long frameCounter = 0;

        final double frameTime = 1 / FRAME_CAP;
        final long SECOND = 1000000000L;

        boolean render = false;
        long now = 0l, passedTime = 0l;
        
        double lastRender = 0.0;
        
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
                this.tick();
                
                if(frameCounter >= SECOND) {
                    Game.FPS = frames;
                    frames = 0;
                    frameCounter = 0;
                }
            }

            // render the scene
            if(isRunning && render) {
                this.timeBetweenFrames = System.nanoTime() - lastRender;
                this.render();
                frames++;        
                lastRender = System.nanoTime();
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

        // draws all graphics
        this.renderer.preRender(g);

        g.dispose();
        bs.show();
    }

    private void tick() { 
        if(this.gamestate == GameState.INGAME) {
            if(Game.isPaused == false) {
                handler.tickGameObjects(); 
                handler.tickAnimations();
                handler.tickEmitters();
                this.camera.tick();
                this.guiElementManager.tick(GameState.INGAME);
            }
        } else if(this.gamestate == GameState.MAINMENU) {
            this.guiElementManager.tick(GameState.MAINMENU);
        } else if(this.gamestate == GameState.LOADING) {
            this.guiElementManager.tick(GameState.LOADING);
        }
    }
    
    public void startNewGame() {
        
        System.out.println("-------- New Game --------");
        
        this.gamestate = GameState.LOADING;
        
        // create world
        this.world = new World();
        
        // initiate first level
        this.world.initializeRoom(Game.instance.currentRoomIndex);
        
        // create mock up player actor
        actorManager.createPlayerInstance("Player", SpriteType.PLAYER, 4);
        
        // TODO: load session
        // create session
        this.session = new Session();
        
        Game.instance.setGamestate(GameState.INGAME);
        
    }
    
    public static void main(String args[]) { new Game(); }
    
    // ----- GETTERS & SETTERS ------
    public Window getWindow() { return this.window; }
    public Font getCustomFont() { return customFont; }
    public void setCustomFont(Font customFont) { this.customFont = customFont; }
    public Camera getCamera() { return camera; }
    public void setCamera(Camera camera) { this.camera = camera; }
    public SpriteCreator getSpriteCreator() { return spriteCreator;}
    public void setSpriteCreator(SpriteCreator spriteCreator) { this.spriteCreator = spriteCreator; }
    public Handler getHandler() { return handler; }
    public void setHandler(Handler handler) { this.handler = handler; }
    public ActorManager getActorManager() { return this.actorManager; }
    public void setActorManager(ActorManager actorManager) { this.actorManager = actorManager; }
    public World getWorld() { return this.world; }
    public void setWorld(World world) { this.world = world; }
    public GameState getGamestate() { return this.gamestate; }
    public void setGamestate(GameState gamestate) { this.gamestate = gamestate; }
    public GuiRenderer getGuiRenderer() { return this.guiRenderer; }
    public void setGuiRenderer(GuiRenderer guiRenderer) { this.guiRenderer = guiRenderer; }
    public Point getMousePos() { return this.mousePos; }
    public void setMousePos(Point mousePos) { this.mousePos = mousePos; }
    public int getCurrentRoomIndex() { return this.currentRoomIndex; }
    public void setCurrentRoomIndex(int currentRoomIndex) { this.currentRoomIndex = currentRoomIndex; }
    public double getTimeBetweenFrames() { return this.timeBetweenFrames * 0.000001; }
    public Animator getAnimator() { return this.animator; }
    public GuiElementManager getGuiElementManager() { return guiElementManager;  }
    public EmitterManager getEmitterManager() { return emitterManager; }
    public void setEmitterManager(EmitterManager emitterManager) { this.emitterManager = emitterManager; }
    public Session getSession() { return session; }
    public void setSession(Session session) { this.session = session; }
}
