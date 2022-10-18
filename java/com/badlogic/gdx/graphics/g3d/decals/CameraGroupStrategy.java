/*     */ package com.badlogic.gdx.graphics.g3d.decals;
/*     */ 
/*     */ import com.badlogic.gdx.Gdx;
/*     */ import com.badlogic.gdx.graphics.Camera;
/*     */ import com.badlogic.gdx.graphics.glutils.ShaderProgram;
/*     */ import com.badlogic.gdx.utils.Array;
/*     */ import com.badlogic.gdx.utils.Disposable;
/*     */ import com.badlogic.gdx.utils.ObjectMap;
/*     */ import com.badlogic.gdx.utils.Pool;
/*     */ import java.util.Comparator;
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
/*     */ public class CameraGroupStrategy
/*     */   implements GroupStrategy, Disposable
/*     */ {
/*     */   private static final int GROUP_OPAQUE = 0;
/*     */   private static final int GROUP_BLEND = 1;
/*     */   
/*  85 */   Pool<Array<Decal>> arrayPool = new Pool<Array<Decal>>(16)
/*     */     {
/*     */       protected Array<Decal> newObject() {
/*  88 */         return new Array();
/*     */       }
/*     */     };
/*  91 */   Array<Array<Decal>> usedArrays = new Array();
/*  92 */   ObjectMap<DecalMaterial, Array<Decal>> materialGroups = new ObjectMap();
/*     */   
/*     */   Camera camera;
/*     */   ShaderProgram shader;
/*     */   private final Comparator<Decal> cameraSorter;
/*     */   
/*     */   public CameraGroupStrategy(Camera camera) {
/*  99 */     this(camera, new Comparator<Decal>(camera)
/*     */         {
/*     */           public int compare(Decal o1, Decal o2) {
/* 102 */             float dist1 = camera.position.dst(o1.position);
/* 103 */             float dist2 = camera.position.dst(o2.position);
/* 104 */             return (int)Math.signum(dist2 - dist1);
/*     */           }
/*     */         });
/*     */   }
/*     */   
/*     */   public CameraGroupStrategy(Camera camera, Comparator<Decal> sorter) {
/* 110 */     this.camera = camera;
/* 111 */     this.cameraSorter = sorter;
/* 112 */     createDefaultShader();
/*     */   }
/*     */ 
/*     */   
/*     */   public void setCamera(Camera camera) {
/* 117 */     this.camera = camera;
/*     */   }
/*     */   
/*     */   public Camera getCamera() {
/* 121 */     return this.camera;
/*     */   }
/*     */ 
/*     */   
/*     */   public int decideGroup(Decal decal) {
/* 126 */     return decal.getMaterial().isOpaque() ? 0 : 1;
/*     */   }
/*     */ 
/*     */   
/*     */   public void beforeGroup(int group, Array<Decal> contents) {
/* 131 */     if (group == 1) {
/* 132 */       Gdx.gl.glEnable(3042);
/* 133 */       contents.sort(this.cameraSorter);
/*     */     } else {
/* 135 */       for (int i = 0, n = contents.size; i < n; i++) {
/* 136 */         Decal decal = (Decal)contents.get(i);
/* 137 */         Array<Decal> materialGroup = (Array<Decal>)this.materialGroups.get(decal.material);
/* 138 */         if (materialGroup == null) {
/* 139 */           materialGroup = (Array<Decal>)this.arrayPool.obtain();
/* 140 */           materialGroup.clear();
/* 141 */           this.usedArrays.add(materialGroup);
/* 142 */           this.materialGroups.put(decal.material, materialGroup);
/*     */         } 
/* 144 */         materialGroup.add(decal);
/*     */       } 
/*     */       
/* 147 */       contents.clear();
/* 148 */       for (ObjectMap.Values<Array<Decal>> values = this.materialGroups.values().iterator(); values.hasNext(); ) { Array<Decal> materialGroup = values.next();
/* 149 */         contents.addAll(materialGroup); }
/*     */ 
/*     */       
/* 152 */       this.materialGroups.clear();
/* 153 */       this.arrayPool.freeAll(this.usedArrays);
/* 154 */       this.usedArrays.clear();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void afterGroup(int group) {
/* 160 */     if (group == 1) {
/* 161 */       Gdx.gl.glDisable(3042);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void beforeGroups() {
/* 167 */     Gdx.gl.glEnable(2929);
/* 168 */     this.shader.begin();
/* 169 */     this.shader.setUniformMatrix("u_projectionViewMatrix", this.camera.combined);
/* 170 */     this.shader.setUniformi("u_texture", 0);
/*     */   }
/*     */ 
/*     */   
/*     */   public void afterGroups() {
/* 175 */     this.shader.end();
/* 176 */     Gdx.gl.glDisable(2929);
/*     */   }
/*     */   
/*     */   private void createDefaultShader() {
/* 180 */     String vertexShader = "attribute vec4 a_position;\nattribute vec4 a_color;\nattribute vec2 a_texCoord0;\nuniform mat4 u_projectionViewMatrix;\nvarying vec4 v_color;\nvarying vec2 v_texCoords;\n\nvoid main()\n{\n   v_color = a_color;\n   v_texCoords = a_texCoord0;\n   gl_Position =  u_projectionViewMatrix * a_position;\n}\n";
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
/* 193 */     String fragmentShader = "#ifdef GL_ES\nprecision mediump float;\n#endif\nvarying vec4 v_color;\nvarying vec2 v_texCoords;\nuniform sampler2D u_texture;\nvoid main()\n{\n  gl_FragColor = v_color * texture2D(u_texture, v_texCoords);\n}";
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
/* 204 */     this.shader = new ShaderProgram(vertexShader, fragmentShader);
/* 205 */     if (!this.shader.isCompiled()) throw new IllegalArgumentException("couldn't compile shader: " + this.shader.getLog());
/*     */   
/*     */   }
/*     */   
/*     */   public ShaderProgram getGroupShader(int group) {
/* 210 */     return this.shader;
/*     */   }
/*     */ 
/*     */   
/*     */   public void dispose() {
/* 215 */     if (this.shader != null) this.shader.dispose(); 
/*     */   }
/*     */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\graphics\g3d\decals\CameraGroupStrategy.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */