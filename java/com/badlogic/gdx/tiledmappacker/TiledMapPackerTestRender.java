/*     */ package com.badlogic.gdx.tiledmappacker;
/*     */ 
/*     */ import com.badlogic.gdx.ApplicationAdapter;
/*     */ import com.badlogic.gdx.ApplicationListener;
/*     */ import com.badlogic.gdx.Gdx;
/*     */ import com.badlogic.gdx.assets.loaders.FileHandleResolver;
/*     */ import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
/*     */ import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
/*     */ import com.badlogic.gdx.graphics.OrthographicCamera;
/*     */ import com.badlogic.gdx.maps.tiled.AtlasTmxMapLoader;
/*     */ import com.badlogic.gdx.maps.tiled.TiledMap;
/*     */ import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
/*     */ import com.badlogic.gdx.utils.viewport.FitViewport;
/*     */ import com.badlogic.gdx.utils.viewport.Viewport;
/*     */ import java.io.File;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class TiledMapPackerTestRender
/*     */   extends ApplicationAdapter
/*     */ {
/*     */   private final boolean DELETE_DELETEME_FOLDER_ON_EXIT = false;
/*     */   private static final String MAP_PATH = "../../tests/gdx-tests-android/assets/data/maps/tiled-atlas-processed/deleteMe/";
/*  45 */   private final String MAP_NAME = "test.tmx";
/*  46 */   private final String TMX_LOC = "../../tests/gdx-tests-android/assets/data/maps/tiled-atlas-processed/deleteMe/test.tmx";
/*     */   private final boolean CENTER_CAM = true;
/*  48 */   private final float WORLD_WIDTH = 32.0F;
/*  49 */   private final float WORLD_HEIGHT = 18.0F;
/*  50 */   private final float PIXELS_PER_METER = 32.0F;
/*  51 */   private final float UNIT_SCALE = 0.03125F;
/*     */   
/*     */   private AtlasTmxMapLoader.AtlasTiledMapLoaderParameters params;
/*     */   private AtlasTmxMapLoader atlasTmxMapLoader;
/*     */   private TiledMap map;
/*     */   private Viewport viewport;
/*     */   private OrthogonalTiledMapRenderer mapRenderer;
/*     */   private OrthographicCamera cam;
/*     */   
/*     */   public void create() {
/*  61 */     this.atlasTmxMapLoader = new AtlasTmxMapLoader((FileHandleResolver)new InternalFileHandleResolver());
/*  62 */     this.params = new AtlasTmxMapLoader.AtlasTiledMapLoaderParameters();
/*     */     
/*  64 */     this.params.generateMipMaps = false;
/*  65 */     this.params.convertObjectToTileSpace = false;
/*  66 */     this.params.flipY = true;
/*     */     
/*  68 */     this.viewport = (Viewport)new FitViewport(32.0F, 18.0F);
/*  69 */     this.cam = (OrthographicCamera)this.viewport.getCamera();
/*     */     
/*  71 */     this.map = this.atlasTmxMapLoader.load("../../tests/gdx-tests-android/assets/data/maps/tiled-atlas-processed/deleteMe/test.tmx", this.params);
/*  72 */     this.mapRenderer = new OrthogonalTiledMapRenderer(this.map, 0.03125F);
/*     */   }
/*     */ 
/*     */   
/*     */   public void render() {
/*  77 */     Gdx.gl.glClearColor(0.0F, 0.0F, 0.0F, 1.0F);
/*  78 */     Gdx.gl.glClear(16384);
/*     */     
/*  80 */     this.viewport.apply();
/*  81 */     this.mapRenderer.setView(this.cam);
/*  82 */     this.mapRenderer.render();
/*     */     
/*  84 */     if (Gdx.input.isKeyPressed(131)) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*  90 */       dispose();
/*  91 */       Gdx.app.exit();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void resize(int width, int height) {
/*  97 */     this.viewport.update(width, height, true);
/*     */   }
/*     */ 
/*     */   
/*     */   public void dispose() {
/* 102 */     this.map.dispose();
/*     */   }
/*     */   
/*     */   public static void main(String[] args) throws Exception {
/* 106 */     File file = new File("../../tests/gdx-tests-android/assets/data/maps/tiled-atlas-processed/deleteMe/");
/* 107 */     if (!file.exists()) {
/* 108 */       System.out.println("Please run TiledMapPackerTest.");
/*     */       return;
/*     */     } 
/* 111 */     new LwjglApplication((ApplicationListener)new TiledMapPackerTestRender(), "", 640, 480);
/*     */   }
/*     */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\tiledmappacker\TiledMapPackerTestRender.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */