/*     */ package com.badlogic.gdx.graphics.g3d.utils;
/*     */ 
/*     */ import com.badlogic.gdx.graphics.Color;
/*     */ import com.badlogic.gdx.graphics.Mesh;
/*     */ import com.badlogic.gdx.graphics.VertexAttributes;
/*     */ import com.badlogic.gdx.graphics.g3d.Material;
/*     */ import com.badlogic.gdx.graphics.g3d.Model;
/*     */ import com.badlogic.gdx.graphics.g3d.model.MeshPart;
/*     */ import com.badlogic.gdx.graphics.g3d.model.Node;
/*     */ import com.badlogic.gdx.graphics.g3d.model.NodePart;
/*     */ import com.badlogic.gdx.math.Matrix4;
/*     */ import com.badlogic.gdx.math.Vector3;
/*     */ import com.badlogic.gdx.utils.Array;
/*     */ import com.badlogic.gdx.utils.Disposable;
/*     */ import com.badlogic.gdx.utils.GdxRuntimeException;
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
/*     */ public class ModelBuilder
/*     */ {
/*     */   private Model model;
/*     */   private Node node;
/*  47 */   private Array<MeshBuilder> builders = new Array();
/*     */   
/*  49 */   private Matrix4 tmpTransform = new Matrix4();
/*     */   
/*     */   private MeshBuilder getBuilder(VertexAttributes attributes) {
/*  52 */     for (MeshBuilder mb : this.builders) {
/*  53 */       if (mb.getAttributes().equals(attributes) && mb.lastIndex() < 16383) return mb; 
/*  54 */     }  MeshBuilder result = new MeshBuilder();
/*  55 */     result.begin(attributes);
/*  56 */     this.builders.add(result);
/*  57 */     return result;
/*     */   }
/*     */ 
/*     */   
/*     */   public void begin() {
/*  62 */     if (this.model != null) throw new GdxRuntimeException("Call end() first"); 
/*  63 */     this.node = null;
/*  64 */     this.model = new Model();
/*  65 */     this.builders.clear();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Model end() {
/*  71 */     if (this.model == null) throw new GdxRuntimeException("Call begin() first"); 
/*  72 */     Model result = this.model;
/*  73 */     endnode();
/*  74 */     this.model = null;
/*     */     
/*  76 */     for (MeshBuilder mb : this.builders)
/*  77 */       mb.end(); 
/*  78 */     this.builders.clear();
/*     */     
/*  80 */     rebuildReferences(result);
/*  81 */     return result;
/*     */   }
/*     */   
/*     */   private void endnode() {
/*  85 */     if (this.node != null) {
/*  86 */       this.node = null;
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   protected Node node(Node node) {
/*  92 */     if (this.model == null) throw new GdxRuntimeException("Call begin() first");
/*     */     
/*  94 */     endnode();
/*     */     
/*  96 */     this.model.nodes.add(node);
/*  97 */     this.node = node;
/*     */     
/*  99 */     return node;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Node node() {
/* 105 */     Node node = new Node();
/* 106 */     node(node);
/* 107 */     node.id = "node" + this.model.nodes.size;
/* 108 */     return node;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Node node(String id, Model model) {
/* 115 */     Node node = new Node();
/* 116 */     node.id = id;
/* 117 */     node.addChildren((Iterable)model.nodes);
/* 118 */     node(node);
/* 119 */     for (Disposable disposable : model.getManagedDisposables())
/* 120 */       manage(disposable); 
/* 121 */     return node;
/*     */   }
/*     */ 
/*     */   
/*     */   public void manage(Disposable disposable) {
/* 126 */     if (this.model == null) throw new GdxRuntimeException("Call begin() first"); 
/* 127 */     this.model.manageDisposable(disposable);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void part(MeshPart meshpart, Material material) {
/* 134 */     if (this.node == null) node(); 
/* 135 */     this.node.parts.add(new NodePart(meshpart, material));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public MeshPart part(String id, Mesh mesh, int primitiveType, int offset, int size, Material material) {
/* 143 */     MeshPart meshPart = new MeshPart();
/* 144 */     meshPart.id = id;
/* 145 */     meshPart.primitiveType = primitiveType;
/* 146 */     meshPart.mesh = mesh;
/* 147 */     meshPart.offset = offset;
/* 148 */     meshPart.size = size;
/* 149 */     part(meshPart, material);
/* 150 */     return meshPart;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public MeshPart part(String id, Mesh mesh, int primitiveType, Material material) {
/* 158 */     return part(id, mesh, primitiveType, 0, mesh.getNumIndices(), material);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public MeshPartBuilder part(String id, int primitiveType, VertexAttributes attributes, Material material) {
/* 167 */     MeshBuilder builder = getBuilder(attributes);
/* 168 */     part(builder.part(id, primitiveType), material);
/* 169 */     return builder;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public MeshPartBuilder part(String id, int primitiveType, long attributes, Material material) {
/* 180 */     return part(id, primitiveType, MeshBuilder.createAttributes(attributes), material);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Model createBox(float width, float height, float depth, Material material, long attributes) {
/* 188 */     return createBox(width, height, depth, 4, material, attributes);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Model createBox(float width, float height, float depth, int primitiveType, Material material, long attributes) {
/* 197 */     begin();
/* 198 */     part("box", primitiveType, attributes, material).box(width, height, depth);
/* 199 */     return end();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Model createRect(float x00, float y00, float z00, float x10, float y10, float z10, float x11, float y11, float z11, float x01, float y01, float z01, float normalX, float normalY, float normalZ, Material material, long attributes) {
/* 208 */     return createRect(x00, y00, z00, x10, y10, z10, x11, y11, z11, x01, y01, z01, normalX, normalY, normalZ, 4, material, attributes);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Model createRect(float x00, float y00, float z00, float x10, float y10, float z10, float x11, float y11, float z11, float x01, float y01, float z01, float normalX, float normalY, float normalZ, int primitiveType, Material material, long attributes) {
/* 219 */     begin();
/* 220 */     part("rect", primitiveType, attributes, material).rect(x00, y00, z00, x10, y10, z10, x11, y11, z11, x01, y01, z01, normalX, normalY, normalZ);
/*     */     
/* 222 */     return end();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Model createCylinder(float width, float height, float depth, int divisions, Material material, long attributes) {
/* 231 */     return createCylinder(width, height, depth, divisions, 4, material, attributes);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Model createCylinder(float width, float height, float depth, int divisions, int primitiveType, Material material, long attributes) {
/* 240 */     return createCylinder(width, height, depth, divisions, primitiveType, material, attributes, 0.0F, 360.0F);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Model createCylinder(float width, float height, float depth, int divisions, Material material, long attributes, float angleFrom, float angleTo) {
/* 249 */     return createCylinder(width, height, depth, divisions, 4, material, attributes, angleFrom, angleTo);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Model createCylinder(float width, float height, float depth, int divisions, int primitiveType, Material material, long attributes, float angleFrom, float angleTo) {
/* 258 */     begin();
/* 259 */     part("cylinder", primitiveType, attributes, material).cylinder(width, height, depth, divisions, angleFrom, angleTo);
/* 260 */     return end();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Model createCone(float width, float height, float depth, int divisions, Material material, long attributes) {
/* 268 */     return createCone(width, height, depth, divisions, 4, material, attributes);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Model createCone(float width, float height, float depth, int divisions, int primitiveType, Material material, long attributes) {
/* 277 */     return createCone(width, height, depth, divisions, primitiveType, material, attributes, 0.0F, 360.0F);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Model createCone(float width, float height, float depth, int divisions, Material material, long attributes, float angleFrom, float angleTo) {
/* 286 */     return createCone(width, height, depth, divisions, 4, material, attributes, angleFrom, angleTo);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Model createCone(float width, float height, float depth, int divisions, int primitiveType, Material material, long attributes, float angleFrom, float angleTo) {
/* 295 */     begin();
/* 296 */     part("cone", primitiveType, attributes, material).cone(width, height, depth, divisions, angleFrom, angleTo);
/* 297 */     return end();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Model createSphere(float width, float height, float depth, int divisionsU, int divisionsV, Material material, long attributes) {
/* 306 */     return createSphere(width, height, depth, divisionsU, divisionsV, 4, material, attributes);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Model createSphere(float width, float height, float depth, int divisionsU, int divisionsV, int primitiveType, Material material, long attributes) {
/* 315 */     return createSphere(width, height, depth, divisionsU, divisionsV, primitiveType, material, attributes, 0.0F, 360.0F, 0.0F, 180.0F);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Model createSphere(float width, float height, float depth, int divisionsU, int divisionsV, Material material, long attributes, float angleUFrom, float angleUTo, float angleVFrom, float angleVTo) {
/* 324 */     return createSphere(width, height, depth, divisionsU, divisionsV, 4, material, attributes, angleUFrom, angleUTo, angleVFrom, angleVTo);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Model createSphere(float width, float height, float depth, int divisionsU, int divisionsV, int primitiveType, Material material, long attributes, float angleUFrom, float angleUTo, float angleVFrom, float angleVTo) {
/* 334 */     begin();
/* 335 */     part("cylinder", primitiveType, attributes, material).sphere(width, height, depth, divisionsU, divisionsV, angleUFrom, angleUTo, angleVFrom, angleVTo);
/*     */     
/* 337 */     return end();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Model createCapsule(float radius, float height, int divisions, Material material, long attributes) {
/* 345 */     return createCapsule(radius, height, divisions, 4, material, attributes);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Model createCapsule(float radius, float height, int divisions, int primitiveType, Material material, long attributes) {
/* 354 */     begin();
/* 355 */     part("capsule", primitiveType, attributes, material).capsule(radius, height, divisions);
/* 356 */     return end();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static void rebuildReferences(Model model) {
/* 362 */     model.materials.clear();
/* 363 */     model.meshes.clear();
/* 364 */     model.meshParts.clear();
/* 365 */     for (Node node : model.nodes)
/* 366 */       rebuildReferences(model, node); 
/*     */   }
/*     */   
/*     */   private static void rebuildReferences(Model model, Node node) {
/* 370 */     for (NodePart mpm : node.parts) {
/* 371 */       if (!model.materials.contains(mpm.material, true)) model.materials.add(mpm.material); 
/* 372 */       if (!model.meshParts.contains(mpm.meshPart, true)) {
/* 373 */         model.meshParts.add(mpm.meshPart);
/* 374 */         if (!model.meshes.contains(mpm.meshPart.mesh, true)) model.meshes.add(mpm.meshPart.mesh); 
/* 375 */         model.manageDisposable((Disposable)mpm.meshPart.mesh);
/*     */       } 
/*     */     } 
/* 378 */     for (Node child : node.getChildren()) {
/* 379 */       rebuildReferences(model, child);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Model createXYZCoordinates(float axisLength, float capLength, float stemThickness, int divisions, int primitiveType, Material material, long attributes) {
/* 390 */     begin();
/*     */     
/* 392 */     Node node = node();
/*     */     
/* 394 */     MeshPartBuilder partBuilder = part("xyz", primitiveType, attributes, material);
/* 395 */     partBuilder.setColor(Color.RED);
/* 396 */     partBuilder.arrow(0.0F, 0.0F, 0.0F, axisLength, 0.0F, 0.0F, capLength, stemThickness, divisions);
/* 397 */     partBuilder.setColor(Color.GREEN);
/* 398 */     partBuilder.arrow(0.0F, 0.0F, 0.0F, 0.0F, axisLength, 0.0F, capLength, stemThickness, divisions);
/* 399 */     partBuilder.setColor(Color.BLUE);
/* 400 */     partBuilder.arrow(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, axisLength, capLength, stemThickness, divisions);
/*     */     
/* 402 */     return end();
/*     */   }
/*     */   
/*     */   public Model createXYZCoordinates(float axisLength, Material material, long attributes) {
/* 406 */     return createXYZCoordinates(axisLength, 0.1F, 0.1F, 5, 4, material, attributes);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Model createArrow(float x1, float y1, float z1, float x2, float y2, float z2, float capLength, float stemThickness, int divisions, int primitiveType, Material material, long attributes) {
/* 417 */     begin();
/* 418 */     part("arrow", primitiveType, attributes, material).arrow(x1, y1, z1, x2, y2, z2, capLength, stemThickness, divisions);
/* 419 */     return end();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Model createArrow(Vector3 from, Vector3 to, Material material, long attributes) {
/* 425 */     return createArrow(from.x, from.y, from.z, to.x, to.y, to.z, 0.1F, 0.1F, 5, 4, material, attributes);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Model createLineGrid(int xDivisions, int zDivisions, float xSize, float zSize, Material material, long attributes) {
/* 435 */     begin();
/* 436 */     MeshPartBuilder partBuilder = part("lines", 1, attributes, material);
/* 437 */     float xlength = xDivisions * xSize, zlength = zDivisions * zSize, hxlength = xlength / 2.0F, hzlength = zlength / 2.0F;
/* 438 */     float x1 = -hxlength, y1 = 0.0F, z1 = hzlength, x2 = -hxlength, y2 = 0.0F, z2 = -hzlength;
/* 439 */     for (int i = 0; i <= xDivisions; i++) {
/* 440 */       partBuilder.line(x1, y1, z1, x2, y2, z2);
/* 441 */       x1 += xSize;
/* 442 */       x2 += xSize;
/*     */     } 
/*     */     
/* 445 */     x1 = -hxlength;
/* 446 */     y1 = 0.0F;
/* 447 */     z1 = -hzlength;
/* 448 */     x2 = hxlength;
/* 449 */     y2 = 0.0F;
/* 450 */     z2 = -hzlength;
/* 451 */     for (int j = 0; j <= zDivisions; j++) {
/* 452 */       partBuilder.line(x1, y1, z1, x2, y2, z2);
/* 453 */       z1 += zSize;
/* 454 */       z2 += zSize;
/*     */     } 
/*     */     
/* 457 */     return end();
/*     */   }
/*     */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\graphics\g3\\utils\ModelBuilder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */