/*     */ package com.badlogic.gdx.graphics.g3d.decals;
/*     */ 
/*     */ import com.badlogic.gdx.Gdx;
/*     */ import com.badlogic.gdx.graphics.Mesh;
/*     */ import com.badlogic.gdx.graphics.VertexAttribute;
/*     */ import com.badlogic.gdx.graphics.glutils.ShaderProgram;
/*     */ import com.badlogic.gdx.utils.Array;
/*     */ import com.badlogic.gdx.utils.Disposable;
/*     */ import com.badlogic.gdx.utils.Pool;
/*     */ import com.badlogic.gdx.utils.SortedIntList;
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
/*     */ public class DecalBatch
/*     */   implements Disposable
/*     */ {
/*     */   private static final int DEFAULT_SIZE = 1000;
/*     */   private float[] vertices;
/*     */   private Mesh mesh;
/*     */   private GroupStrategy groupStrategy;
/*  55 */   private final SortedIntList<Array<Decal>> groupList = new SortedIntList();
/*     */   
/*  57 */   private final Pool<Array<Decal>> groupPool = new Pool<Array<Decal>>(16)
/*     */     {
/*     */       protected Array<Decal> newObject() {
/*  60 */         return new Array(false, 100);
/*     */       }
/*     */     };
/*  63 */   private final Array<Array<Decal>> usedGroups = new Array(16);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public DecalBatch(GroupStrategy groupStrategy) {
/*  71 */     this(1000, groupStrategy);
/*     */   }
/*     */   
/*     */   public DecalBatch(int size, GroupStrategy groupStrategy) {
/*  75 */     initialize(size);
/*  76 */     setGroupStrategy(groupStrategy);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setGroupStrategy(GroupStrategy groupStrategy) {
/*  82 */     this.groupStrategy = groupStrategy;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void initialize(int size) {
/*  89 */     this.vertices = new float[size * 24];
/*     */     
/*  91 */     Mesh.VertexDataType vertexDataType = Mesh.VertexDataType.VertexArray;
/*  92 */     if (Gdx.gl30 != null) {
/*  93 */       vertexDataType = Mesh.VertexDataType.VertexBufferObjectWithVAO;
/*     */     }
/*  95 */     this.mesh = new Mesh(vertexDataType, false, size * 4, size * 6, new VertexAttribute[] { new VertexAttribute(1, 3, "a_position"), new VertexAttribute(4, 4, "a_color"), new VertexAttribute(16, 2, "a_texCoord0") });
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 100 */     short[] indices = new short[size * 6];
/* 101 */     int v = 0;
/* 102 */     for (int i = 0; i < indices.length; i += 6, v += 4) {
/* 103 */       indices[i] = (short)v;
/* 104 */       indices[i + 1] = (short)(v + 2);
/* 105 */       indices[i + 2] = (short)(v + 1);
/* 106 */       indices[i + 3] = (short)(v + 1);
/* 107 */       indices[i + 4] = (short)(v + 2);
/* 108 */       indices[i + 5] = (short)(v + 3);
/*     */     } 
/* 110 */     this.mesh.setIndices(indices);
/*     */   }
/*     */ 
/*     */   
/*     */   public int getSize() {
/* 115 */     return this.vertices.length / 24;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void add(Decal decal) {
/* 122 */     int groupIndex = this.groupStrategy.decideGroup(decal);
/* 123 */     Array<Decal> targetGroup = (Array<Decal>)this.groupList.get(groupIndex);
/* 124 */     if (targetGroup == null) {
/* 125 */       targetGroup = (Array<Decal>)this.groupPool.obtain();
/* 126 */       targetGroup.clear();
/* 127 */       this.usedGroups.add(targetGroup);
/* 128 */       this.groupList.insert(groupIndex, targetGroup);
/*     */     } 
/* 130 */     targetGroup.add(decal);
/*     */   }
/*     */ 
/*     */   
/*     */   public void flush() {
/* 135 */     render();
/* 136 */     clear();
/*     */   }
/*     */ 
/*     */   
/*     */   protected void render() {
/* 141 */     this.groupStrategy.beforeGroups();
/* 142 */     for (SortedIntList.Node<Array<Decal>> group : this.groupList) {
/* 143 */       this.groupStrategy.beforeGroup(group.index, (Array<Decal>)group.value);
/* 144 */       ShaderProgram shader = this.groupStrategy.getGroupShader(group.index);
/* 145 */       render(shader, (Array<Decal>)group.value);
/* 146 */       this.groupStrategy.afterGroup(group.index);
/*     */     } 
/* 148 */     this.groupStrategy.afterGroups();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void render(ShaderProgram shader, Array<Decal> decals) {
/* 156 */     DecalMaterial lastMaterial = null;
/* 157 */     int idx = 0;
/* 158 */     for (Decal decal : decals) {
/* 159 */       if (lastMaterial == null || !lastMaterial.equals(decal.getMaterial())) {
/* 160 */         if (idx > 0) {
/* 161 */           flush(shader, idx);
/* 162 */           idx = 0;
/*     */         } 
/* 164 */         decal.material.set();
/* 165 */         lastMaterial = decal.material;
/*     */       } 
/* 167 */       decal.update();
/* 168 */       System.arraycopy(decal.vertices, 0, this.vertices, idx, decal.vertices.length);
/* 169 */       idx += decal.vertices.length;
/*     */       
/* 171 */       if (idx == this.vertices.length) {
/* 172 */         flush(shader, idx);
/* 173 */         idx = 0;
/*     */       } 
/*     */     } 
/*     */     
/* 177 */     if (idx > 0) {
/* 178 */       flush(shader, idx);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void flush(ShaderProgram shader, int verticesPosition) {
/* 186 */     this.mesh.setVertices(this.vertices, 0, verticesPosition);
/* 187 */     this.mesh.render(shader, 4, 0, verticesPosition / 4);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void clear() {
/* 192 */     this.groupList.clear();
/* 193 */     this.groupPool.freeAll(this.usedGroups);
/* 194 */     this.usedGroups.clear();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void dispose() {
/* 200 */     clear();
/* 201 */     this.vertices = null;
/* 202 */     this.mesh.dispose();
/*     */   }
/*     */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\graphics\g3d\decals\DecalBatch.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */