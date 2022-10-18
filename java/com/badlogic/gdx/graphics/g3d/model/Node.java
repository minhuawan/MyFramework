/*     */ package com.badlogic.gdx.graphics.g3d.model;
/*     */ 
/*     */ import com.badlogic.gdx.math.Matrix4;
/*     */ import com.badlogic.gdx.math.Quaternion;
/*     */ import com.badlogic.gdx.math.Vector3;
/*     */ import com.badlogic.gdx.math.collision.BoundingBox;
/*     */ import com.badlogic.gdx.utils.Array;
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
/*     */ public class Node
/*     */ {
/*     */   public String id;
/*     */   public boolean inheritTransform = true;
/*     */   public boolean isAnimated;
/*  41 */   public final Vector3 translation = new Vector3();
/*     */   
/*  43 */   public final Quaternion rotation = new Quaternion(0.0F, 0.0F, 0.0F, 1.0F);
/*     */   
/*  45 */   public final Vector3 scale = new Vector3(1.0F, 1.0F, 1.0F);
/*     */   
/*  47 */   public final Matrix4 localTransform = new Matrix4();
/*     */ 
/*     */   
/*  50 */   public final Matrix4 globalTransform = new Matrix4();
/*     */   
/*  52 */   public Array<NodePart> parts = new Array(2);
/*     */   
/*     */   protected Node parent;
/*  55 */   private final Array<Node> children = new Array(2);
/*     */ 
/*     */ 
/*     */   
/*     */   public Matrix4 calculateLocalTransform() {
/*  60 */     if (!this.isAnimated) this.localTransform.set(this.translation, this.rotation, this.scale); 
/*  61 */     return this.localTransform;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Matrix4 calculateWorldTransform() {
/*  67 */     if (this.inheritTransform && this.parent != null) {
/*  68 */       this.globalTransform.set(this.parent.globalTransform).mul(this.localTransform);
/*     */     } else {
/*  70 */       this.globalTransform.set(this.localTransform);
/*  71 */     }  return this.globalTransform;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void calculateTransforms(boolean recursive) {
/*  78 */     calculateLocalTransform();
/*  79 */     calculateWorldTransform();
/*     */     
/*  81 */     if (recursive) {
/*  82 */       for (Node child : this.children) {
/*  83 */         child.calculateTransforms(true);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public void calculateBoneTransforms(boolean recursive) {
/*  89 */     for (NodePart part : this.parts) {
/*  90 */       if (part.invBoneBindTransforms == null || part.bones == null || part.invBoneBindTransforms.size != part.bones.length)
/*     */         continue; 
/*  92 */       int n = part.invBoneBindTransforms.size;
/*  93 */       for (int i = 0; i < n; i++)
/*  94 */         part.bones[i].set((((Node[])part.invBoneBindTransforms.keys)[i]).globalTransform).mul(((Matrix4[])part.invBoneBindTransforms.values)[i]); 
/*     */     } 
/*  96 */     if (recursive) {
/*  97 */       for (Node child : this.children) {
/*  98 */         child.calculateBoneTransforms(true);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public BoundingBox calculateBoundingBox(BoundingBox out) {
/* 105 */     out.inf();
/* 106 */     return extendBoundingBox(out);
/*     */   }
/*     */ 
/*     */   
/*     */   public BoundingBox calculateBoundingBox(BoundingBox out, boolean transform) {
/* 111 */     out.inf();
/* 112 */     return extendBoundingBox(out, transform);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public BoundingBox extendBoundingBox(BoundingBox out) {
/* 118 */     return extendBoundingBox(out, true);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public BoundingBox extendBoundingBox(BoundingBox out, boolean transform) {
/* 124 */     int partCount = this.parts.size;
/* 125 */     for (int i = 0; i < partCount; i++) {
/* 126 */       NodePart part = (NodePart)this.parts.get(i);
/* 127 */       if (part.enabled) {
/* 128 */         MeshPart meshPart = part.meshPart;
/* 129 */         if (transform) {
/* 130 */           meshPart.mesh.extendBoundingBox(out, meshPart.offset, meshPart.size, this.globalTransform);
/*     */         } else {
/* 132 */           meshPart.mesh.extendBoundingBox(out, meshPart.offset, meshPart.size);
/*     */         } 
/*     */       } 
/* 135 */     }  int childCount = this.children.size;
/* 136 */     for (int j = 0; j < childCount; j++)
/* 137 */       ((Node)this.children.get(j)).extendBoundingBox(out); 
/* 138 */     return out;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public <T extends Node> void attachTo(T parent) {
/* 144 */     parent.addChild(this);
/*     */   }
/*     */ 
/*     */   
/*     */   public void detach() {
/* 149 */     if (this.parent != null) {
/* 150 */       this.parent.removeChild(this);
/* 151 */       this.parent = null;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean hasChildren() {
/* 157 */     return (this.children != null && this.children.size > 0);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int getChildCount() {
/* 163 */     return this.children.size;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Node getChild(int index) {
/* 169 */     return (Node)this.children.get(index);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Node getChild(String id, boolean recursive, boolean ignoreCase) {
/* 175 */     return getNode(this.children, id, recursive, ignoreCase);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public <T extends Node> int addChild(T child) {
/* 183 */     return insertChild(-1, child);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public <T extends Node> int addChildren(Iterable<T> nodes) {
/* 191 */     return insertChildren(-1, nodes);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public <T extends Node> int insertChild(int index, T child) {
/*     */     Node p;
/* 201 */     for (p = this; p != null; p = p.getParent()) {
/* 202 */       if (p == child) throw new GdxRuntimeException("Cannot add a parent as a child"); 
/*     */     } 
/* 204 */     p = child.getParent();
/* 205 */     if (p != null && !p.removeChild(child)) throw new GdxRuntimeException("Could not remove child from its current parent"); 
/* 206 */     if (index < 0 || index >= this.children.size) {
/* 207 */       index = this.children.size;
/* 208 */       this.children.add(child);
/*     */     } else {
/* 210 */       this.children.insert(index, child);
/* 211 */     }  ((Node)child).parent = this;
/* 212 */     return index;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public <T extends Node> int insertChildren(int index, Iterable<T> nodes) {
/* 222 */     if (index < 0 || index > this.children.size) index = this.children.size; 
/* 223 */     int i = index;
/* 224 */     for (Node node : nodes)
/* 225 */       insertChild(i++, node); 
/* 226 */     return index;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public <T extends Node> boolean removeChild(T child) {
/* 235 */     if (!this.children.removeValue(child, true)) return false; 
/* 236 */     ((Node)child).parent = null;
/* 237 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public Iterable<Node> getChildren() {
/* 242 */     return (Iterable<Node>)this.children;
/*     */   }
/*     */ 
/*     */   
/*     */   public Node getParent() {
/* 247 */     return this.parent;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean hasParent() {
/* 252 */     return (this.parent != null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Node copy() {
/* 263 */     return (new Node()).set(this);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Node set(Node other) {
/* 275 */     detach();
/* 276 */     this.id = other.id;
/* 277 */     this.isAnimated = other.isAnimated;
/* 278 */     this.inheritTransform = other.inheritTransform;
/* 279 */     this.translation.set(other.translation);
/* 280 */     this.rotation.set(other.rotation);
/* 281 */     this.scale.set(other.scale);
/* 282 */     this.localTransform.set(other.localTransform);
/* 283 */     this.globalTransform.set(other.globalTransform);
/* 284 */     this.parts.clear();
/* 285 */     for (NodePart nodePart : other.parts) {
/* 286 */       this.parts.add(nodePart.copy());
/*     */     }
/* 288 */     this.children.clear();
/* 289 */     for (Node child : other.getChildren()) {
/* 290 */       addChild(child.copy());
/*     */     }
/* 292 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Node getNode(Array<Node> nodes, String id, boolean recursive, boolean ignoreCase) {
/* 299 */     int n = nodes.size;
/*     */     
/* 301 */     if (ignoreCase)
/* 302 */     { for (int i = 0; i < n; i++) {
/* 303 */         Node node; if ((node = (Node)nodes.get(i)).id.equalsIgnoreCase(id)) return node; 
/*     */       }  }
/* 305 */     else { for (int i = 0; i < n; i++) {
/* 306 */         Node node; if ((node = (Node)nodes.get(i)).id.equals(id)) return node; 
/*     */       }  }
/* 308 */      if (recursive)
/* 309 */       for (int i = 0; i < n; i++) {
/* 310 */         Node node; if ((node = getNode(((Node)nodes.get(i)).children, id, true, ignoreCase)) != null) return node; 
/*     */       }  
/* 312 */     return null;
/*     */   }
/*     */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\graphics\g3d\model\Node.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */