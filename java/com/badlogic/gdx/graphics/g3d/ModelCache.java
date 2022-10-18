/*     */ package com.badlogic.gdx.graphics.g3d;
/*     */ 
/*     */ import com.badlogic.gdx.graphics.Camera;
/*     */ import com.badlogic.gdx.graphics.Mesh;
/*     */ import com.badlogic.gdx.graphics.VertexAttributes;
/*     */ import com.badlogic.gdx.graphics.g3d.model.MeshPart;
/*     */ import com.badlogic.gdx.graphics.g3d.utils.MeshBuilder;
/*     */ import com.badlogic.gdx.graphics.g3d.utils.RenderableSorter;
/*     */ import com.badlogic.gdx.utils.Array;
/*     */ import com.badlogic.gdx.utils.Disposable;
/*     */ import com.badlogic.gdx.utils.FlushablePool;
/*     */ import com.badlogic.gdx.utils.GdxRuntimeException;
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
/*     */ public class ModelCache
/*     */   implements Disposable, RenderableProvider
/*     */ {
/*     */   public static class SimpleMeshPool
/*     */     implements MeshPool
/*     */   {
/*  63 */     private Array<Mesh> freeMeshes = new Array();
/*  64 */     private Array<Mesh> usedMeshes = new Array();
/*     */ 
/*     */     
/*     */     public void flush() {
/*  68 */       this.freeMeshes.addAll(this.usedMeshes);
/*  69 */       this.usedMeshes.clear();
/*     */     }
/*     */ 
/*     */     
/*     */     public Mesh obtain(VertexAttributes vertexAttributes, int vertexCount, int indexCount) {
/*  74 */       for (int i = 0, n = this.freeMeshes.size; i < n; i++) {
/*  75 */         Mesh mesh = (Mesh)this.freeMeshes.get(i);
/*  76 */         if (mesh.getVertexAttributes().equals(vertexAttributes) && mesh.getMaxVertices() >= vertexCount && mesh
/*  77 */           .getMaxIndices() >= indexCount) {
/*  78 */           this.freeMeshes.removeIndex(i);
/*  79 */           this.usedMeshes.add(mesh);
/*  80 */           return mesh;
/*     */         } 
/*     */       } 
/*  83 */       vertexCount = 32768;
/*  84 */       indexCount = Math.max(32768, 1 << 32 - Integer.numberOfLeadingZeros(indexCount - 1));
/*  85 */       Mesh result = new Mesh(false, vertexCount, indexCount, vertexAttributes);
/*  86 */       this.usedMeshes.add(result);
/*  87 */       return result;
/*     */     }
/*     */ 
/*     */     
/*     */     public void dispose() {
/*  92 */       for (Mesh m : this.usedMeshes)
/*  93 */         m.dispose(); 
/*  94 */       this.usedMeshes.clear();
/*  95 */       for (Mesh m : this.freeMeshes)
/*  96 */         m.dispose(); 
/*  97 */       this.freeMeshes.clear();
/*     */     }
/*     */   }
/*     */   
/*     */   public static class TightMeshPool
/*     */     implements MeshPool
/*     */   {
/* 104 */     private Array<Mesh> freeMeshes = new Array();
/* 105 */     private Array<Mesh> usedMeshes = new Array();
/*     */ 
/*     */     
/*     */     public void flush() {
/* 109 */       this.freeMeshes.addAll(this.usedMeshes);
/* 110 */       this.usedMeshes.clear();
/*     */     }
/*     */ 
/*     */     
/*     */     public Mesh obtain(VertexAttributes vertexAttributes, int vertexCount, int indexCount) {
/* 115 */       for (int i = 0, n = this.freeMeshes.size; i < n; i++) {
/* 116 */         Mesh mesh = (Mesh)this.freeMeshes.get(i);
/* 117 */         if (mesh.getVertexAttributes().equals(vertexAttributes) && mesh.getMaxVertices() == vertexCount && mesh
/* 118 */           .getMaxIndices() == indexCount) {
/* 119 */           this.freeMeshes.removeIndex(i);
/* 120 */           this.usedMeshes.add(mesh);
/* 121 */           return mesh;
/*     */         } 
/*     */       } 
/* 124 */       Mesh result = new Mesh(true, vertexCount, indexCount, vertexAttributes);
/* 125 */       this.usedMeshes.add(result);
/* 126 */       return result;
/*     */     }
/*     */ 
/*     */     
/*     */     public void dispose() {
/* 131 */       for (Mesh m : this.usedMeshes)
/* 132 */         m.dispose(); 
/* 133 */       this.usedMeshes.clear();
/* 134 */       for (Mesh m : this.freeMeshes)
/* 135 */         m.dispose(); 
/* 136 */       this.freeMeshes.clear();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static class Sorter
/*     */     implements RenderableSorter, Comparator<Renderable>
/*     */   {
/*     */     public void sort(Camera camera, Array<Renderable> renderables) {
/* 146 */       renderables.sort(this);
/*     */     }
/*     */ 
/*     */     
/*     */     public int compare(Renderable arg0, Renderable arg1) {
/* 151 */       VertexAttributes va0 = arg0.meshPart.mesh.getVertexAttributes();
/* 152 */       VertexAttributes va1 = arg1.meshPart.mesh.getVertexAttributes();
/* 153 */       int vc = va0.compareTo(va1);
/* 154 */       if (vc == 0) {
/* 155 */         int mc = arg0.material.compareTo(arg1.material);
/* 156 */         if (mc == 0) {
/* 157 */           return arg0.meshPart.primitiveType - arg1.meshPart.primitiveType;
/*     */         }
/* 159 */         return mc;
/*     */       } 
/* 161 */       return vc;
/*     */     }
/*     */   }
/*     */   
/* 165 */   private Array<Renderable> renderables = new Array();
/* 166 */   private FlushablePool<Renderable> renderablesPool = new FlushablePool<Renderable>()
/*     */     {
/*     */       protected Renderable newObject() {
/* 169 */         return new Renderable();
/*     */       }
/*     */     };
/* 172 */   private FlushablePool<MeshPart> meshPartPool = new FlushablePool<MeshPart>()
/*     */     {
/*     */       protected MeshPart newObject() {
/* 175 */         return new MeshPart();
/*     */       }
/*     */     };
/*     */   
/* 179 */   private Array<Renderable> items = new Array();
/* 180 */   private Array<Renderable> tmp = new Array();
/*     */   
/*     */   private MeshBuilder meshBuilder;
/*     */   
/*     */   private boolean building;
/*     */   
/*     */   private RenderableSorter sorter;
/*     */   private MeshPool meshPool;
/*     */   private Camera camera;
/*     */   
/*     */   public ModelCache() {
/* 191 */     this(new Sorter(), new SimpleMeshPool());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ModelCache(RenderableSorter sorter, MeshPool meshPool) {
/* 199 */     this.sorter = sorter;
/* 200 */     this.meshPool = meshPool;
/* 201 */     this.meshBuilder = new MeshBuilder();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void begin() {
/* 209 */     begin(null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void begin(Camera camera) {
/* 218 */     if (this.building) throw new GdxRuntimeException("Call end() after calling begin()"); 
/* 219 */     this.building = true;
/*     */     
/* 221 */     this.camera = camera;
/* 222 */     this.renderablesPool.flush();
/* 223 */     this.renderables.clear();
/* 224 */     this.items.clear();
/* 225 */     this.meshPartPool.flush();
/* 226 */     this.meshPool.flush();
/*     */   }
/*     */   
/*     */   private Renderable obtainRenderable(Material material, int primitiveType) {
/* 230 */     Renderable result = (Renderable)this.renderablesPool.obtain();
/* 231 */     result.bones = null;
/* 232 */     result.environment = null;
/* 233 */     result.material = material;
/* 234 */     result.meshPart.mesh = null;
/* 235 */     result.meshPart.offset = 0;
/* 236 */     result.meshPart.size = 0;
/* 237 */     result.meshPart.primitiveType = primitiveType;
/* 238 */     result.meshPart.center.set(0.0F, 0.0F, 0.0F);
/* 239 */     result.meshPart.halfExtents.set(0.0F, 0.0F, 0.0F);
/* 240 */     result.meshPart.radius = -1.0F;
/* 241 */     result.shader = null;
/* 242 */     result.userData = null;
/* 243 */     result.worldTransform.idt();
/* 244 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void end() {
/* 251 */     if (!this.building) throw new GdxRuntimeException("Call begin() prior to calling end()"); 
/* 252 */     this.building = false;
/*     */     
/* 254 */     if (this.items.size == 0)
/* 255 */       return;  this.sorter.sort(this.camera, this.items);
/*     */     
/* 257 */     int itemCount = this.items.size;
/* 258 */     int initCount = this.renderables.size;
/*     */     
/* 260 */     Renderable first = (Renderable)this.items.get(0);
/* 261 */     VertexAttributes vertexAttributes = first.meshPart.mesh.getVertexAttributes();
/* 262 */     Material material = first.material;
/* 263 */     int primitiveType = first.meshPart.primitiveType;
/* 264 */     int offset = this.renderables.size;
/*     */     
/* 266 */     this.meshBuilder.begin(vertexAttributes);
/* 267 */     MeshPart part = this.meshBuilder.part("", primitiveType, (MeshPart)this.meshPartPool.obtain());
/* 268 */     this.renderables.add(obtainRenderable(material, primitiveType));
/*     */     
/* 270 */     for (int i = 0, n = this.items.size; i < n; i++) {
/* 271 */       Renderable renderable = (Renderable)this.items.get(i);
/* 272 */       VertexAttributes va = renderable.meshPart.mesh.getVertexAttributes();
/* 273 */       Material mat = renderable.material;
/* 274 */       int pt = renderable.meshPart.primitiveType;
/*     */ 
/*     */       
/* 277 */       boolean sameMesh = (va.equals(vertexAttributes) && renderable.meshPart.size + this.meshBuilder.getNumVertices() < 32767);
/* 278 */       boolean samePart = (sameMesh && pt == primitiveType && mat.same(material, true));
/*     */       
/* 280 */       if (!samePart) {
/* 281 */         if (!sameMesh) {
/* 282 */           Mesh mesh1 = this.meshBuilder.end(this.meshPool.obtain(vertexAttributes, this.meshBuilder.getNumVertices(), this.meshBuilder
/* 283 */                 .getNumIndices()));
/* 284 */           while (offset < this.renderables.size)
/* 285 */             ((Renderable)this.renderables.get(offset++)).meshPart.mesh = mesh1; 
/* 286 */           this.meshBuilder.begin(vertexAttributes = va);
/*     */         } 
/*     */         
/* 289 */         MeshPart newPart = this.meshBuilder.part("", pt, (MeshPart)this.meshPartPool.obtain());
/* 290 */         Renderable renderable1 = (Renderable)this.renderables.get(this.renderables.size - 1);
/* 291 */         renderable1.meshPart.offset = part.offset;
/* 292 */         renderable1.meshPart.size = part.size;
/* 293 */         part = newPart;
/*     */         
/* 295 */         this.renderables.add(obtainRenderable(material = mat, primitiveType = pt));
/*     */       } 
/*     */       
/* 298 */       this.meshBuilder.setVertexTransform(renderable.worldTransform);
/* 299 */       this.meshBuilder.addMesh(renderable.meshPart.mesh, renderable.meshPart.offset, renderable.meshPart.size);
/*     */     } 
/*     */     
/* 302 */     Mesh mesh = this.meshBuilder.end(this.meshPool.obtain(vertexAttributes, this.meshBuilder.getNumVertices(), this.meshBuilder
/* 303 */           .getNumIndices()));
/* 304 */     while (offset < this.renderables.size) {
/* 305 */       ((Renderable)this.renderables.get(offset++)).meshPart.mesh = mesh;
/*     */     }
/* 307 */     Renderable previous = (Renderable)this.renderables.get(this.renderables.size - 1);
/* 308 */     previous.meshPart.offset = part.offset;
/* 309 */     previous.meshPart.size = part.size;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void add(Renderable renderable) {
/* 320 */     if (!this.building) throw new GdxRuntimeException("Can only add items to the ModelCache in between .begin() and .end()"); 
/* 321 */     if (renderable.bones == null) {
/* 322 */       this.items.add(renderable);
/*     */     } else {
/* 324 */       this.renderables.add(renderable);
/*     */     } 
/*     */   }
/*     */   
/*     */   public void add(RenderableProvider renderableProvider) {
/* 329 */     renderableProvider.getRenderables(this.tmp, (Pool<Renderable>)this.renderablesPool);
/* 330 */     for (int i = 0, n = this.tmp.size; i < n; i++)
/* 331 */       add((Renderable)this.tmp.get(i)); 
/* 332 */     this.tmp.clear();
/*     */   }
/*     */ 
/*     */   
/*     */   public <T extends RenderableProvider> void add(Iterable<T> renderableProviders) {
/* 337 */     for (RenderableProvider renderableProvider : renderableProviders) {
/* 338 */       add(renderableProvider);
/*     */     }
/*     */   }
/*     */   
/*     */   public void getRenderables(Array<Renderable> renderables, Pool<Renderable> pool) {
/* 343 */     if (this.building) throw new GdxRuntimeException("Cannot render a ModelCache in between .begin() and .end()"); 
/* 344 */     for (Renderable r : this.renderables) {
/* 345 */       r.shader = null;
/* 346 */       r.environment = null;
/*     */     } 
/* 348 */     renderables.addAll(this.renderables);
/*     */   }
/*     */ 
/*     */   
/*     */   public void dispose() {
/* 353 */     if (this.building) throw new GdxRuntimeException("Cannot dispose a ModelCache in between .begin() and .end()"); 
/* 354 */     this.meshPool.dispose();
/*     */   }
/*     */   
/*     */   public static interface MeshPool extends Disposable {
/*     */     Mesh obtain(VertexAttributes param1VertexAttributes, int param1Int1, int param1Int2);
/*     */     
/*     */     void flush();
/*     */   }
/*     */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\graphics\g3d\ModelCache.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */