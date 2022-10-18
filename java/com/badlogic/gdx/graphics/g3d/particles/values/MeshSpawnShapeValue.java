/*     */ package com.badlogic.gdx.graphics.g3d.particles.values;
/*     */ 
/*     */ import com.badlogic.gdx.assets.AssetDescriptor;
/*     */ import com.badlogic.gdx.assets.AssetManager;
/*     */ import com.badlogic.gdx.graphics.Mesh;
/*     */ import com.badlogic.gdx.graphics.g3d.Model;
/*     */ import com.badlogic.gdx.graphics.g3d.particles.ResourceData;
/*     */ import com.badlogic.gdx.math.MathUtils;
/*     */ import com.badlogic.gdx.math.Vector3;
/*     */ import com.badlogic.gdx.utils.GdxRuntimeException;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class MeshSpawnShapeValue
/*     */   extends SpawnShapeValue
/*     */ {
/*     */   protected Mesh mesh;
/*     */   protected Model model;
/*     */   
/*     */   public static class Triangle
/*     */   {
/*     */     float x1;
/*     */     float y1;
/*     */     float z1;
/*     */     float x2;
/*     */     float y2;
/*     */     float z2;
/*     */     float x3;
/*     */     float y3;
/*     */     float z3;
/*     */     
/*     */     public Triangle(float x1, float y1, float z1, float x2, float y2, float z2, float x3, float y3, float z3) {
/*  37 */       this.x1 = x1;
/*  38 */       this.y1 = y1;
/*  39 */       this.z1 = z1;
/*  40 */       this.x2 = x2;
/*  41 */       this.y2 = y2;
/*  42 */       this.z2 = z2;
/*  43 */       this.x3 = x3;
/*  44 */       this.y3 = y3;
/*  45 */       this.z3 = z3;
/*     */     }
/*     */ 
/*     */     
/*     */     public static Vector3 pick(float x1, float y1, float z1, float x2, float y2, float z2, float x3, float y3, float z3, Vector3 vector) {
/*  50 */       float a = MathUtils.random(), b = MathUtils.random();
/*  51 */       return vector.set(x1 + a * (x2 - x1) + b * (x3 - x1), y1 + a * (y2 - y1) + b * (y3 - y1), z1 + a * (z2 - z1) + b * (z3 - z1));
/*     */     }
/*     */ 
/*     */     
/*     */     public Vector3 pick(Vector3 vector) {
/*  56 */       float a = MathUtils.random(), b = MathUtils.random();
/*  57 */       return vector.set(this.x1 + a * (this.x2 - this.x1) + b * (this.x3 - this.x1), this.y1 + a * (this.y2 - this.y1) + b * (this.y3 - this.y1), this.z1 + a * (this.z2 - this.z1) + b * (this.z3 - this.z1));
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public MeshSpawnShapeValue(MeshSpawnShapeValue value) {
/*  67 */     super(value);
/*     */   }
/*     */ 
/*     */   
/*     */   public MeshSpawnShapeValue() {}
/*     */ 
/*     */   
/*     */   public void load(ParticleValue value) {
/*  75 */     super.load(value);
/*  76 */     MeshSpawnShapeValue spawnShapeValue = (MeshSpawnShapeValue)value;
/*  77 */     setMesh(spawnShapeValue.mesh, spawnShapeValue.model);
/*     */   }
/*     */   
/*     */   public void setMesh(Mesh mesh, Model model) {
/*  81 */     if (mesh.getVertexAttribute(1) == null)
/*  82 */       throw new GdxRuntimeException("Mesh vertices must have Usage.Position"); 
/*  83 */     this.model = model;
/*  84 */     this.mesh = mesh;
/*     */   }
/*     */   
/*     */   public void setMesh(Mesh mesh) {
/*  88 */     setMesh(mesh, null);
/*     */   }
/*     */ 
/*     */   
/*     */   public void save(AssetManager manager, ResourceData data) {
/*  93 */     if (this.model != null) {
/*  94 */       ResourceData.SaveData saveData = data.createSaveData();
/*  95 */       saveData.saveAsset(manager.getAssetFileName(this.model), Model.class);
/*  96 */       saveData.save("index", Integer.valueOf(this.model.meshes.indexOf(this.mesh, true)));
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void load(AssetManager manager, ResourceData data) {
/* 102 */     ResourceData.SaveData saveData = data.getSaveData();
/* 103 */     AssetDescriptor descriptor = saveData.loadAsset();
/* 104 */     if (descriptor != null) {
/* 105 */       Model model = (Model)manager.get(descriptor);
/* 106 */       setMesh((Mesh)model.meshes.get(((Integer)saveData.load("index")).intValue()), model);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\graphics\g3d\particles\values\MeshSpawnShapeValue.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */