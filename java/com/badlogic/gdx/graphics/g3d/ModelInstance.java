/*     */ package com.badlogic.gdx.graphics.g3d;
/*     */ 
/*     */ import com.badlogic.gdx.graphics.g3d.model.Animation;
/*     */ import com.badlogic.gdx.graphics.g3d.model.Node;
/*     */ import com.badlogic.gdx.graphics.g3d.model.NodeAnimation;
/*     */ import com.badlogic.gdx.graphics.g3d.model.NodeKeyframe;
/*     */ import com.badlogic.gdx.graphics.g3d.model.NodePart;
/*     */ import com.badlogic.gdx.math.Matrix4;
/*     */ import com.badlogic.gdx.math.Quaternion;
/*     */ import com.badlogic.gdx.math.Vector3;
/*     */ import com.badlogic.gdx.math.collision.BoundingBox;
/*     */ import com.badlogic.gdx.utils.Array;
/*     */ import com.badlogic.gdx.utils.ArrayMap;
/*     */ import com.badlogic.gdx.utils.Pool;
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
/*     */ public class ModelInstance
/*     */   implements RenderableProvider
/*     */ {
/*     */   public static boolean defaultShareKeyframes = true;
/*  47 */   public final Array<Material> materials = new Array();
/*     */   
/*  49 */   public final Array<Node> nodes = new Array();
/*     */   
/*  51 */   public final Array<Animation> animations = new Array();
/*     */ 
/*     */   
/*     */   public final Model model;
/*     */   
/*     */   public Matrix4 transform;
/*     */   
/*     */   public Object userData;
/*     */ 
/*     */   
/*     */   public ModelInstance(Model model) {
/*  62 */     this(model, (String[])null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ModelInstance(Model model, String nodeId, boolean mergeTransform) {
/*  69 */     this(model, null, nodeId, false, false, mergeTransform);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ModelInstance(Model model, Matrix4 transform, String nodeId, boolean mergeTransform) {
/*  77 */     this(model, transform, nodeId, false, false, mergeTransform);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ModelInstance(Model model, String nodeId, boolean parentTransform, boolean mergeTransform) {
/*  86 */     this(model, null, nodeId, true, parentTransform, mergeTransform);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ModelInstance(Model model, Matrix4 transform, String nodeId, boolean parentTransform, boolean mergeTransform) {
/*  97 */     this(model, transform, nodeId, true, parentTransform, mergeTransform);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ModelInstance(Model model, String nodeId, boolean recursive, boolean parentTransform, boolean mergeTransform) {
/* 107 */     this(model, null, nodeId, recursive, parentTransform, mergeTransform);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ModelInstance(Model model, Matrix4 transform, String nodeId, boolean recursive, boolean parentTransform, boolean mergeTransform) {
/* 118 */     this(model, transform, nodeId, recursive, parentTransform, mergeTransform, defaultShareKeyframes);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ModelInstance(Model model, Matrix4 transform, String nodeId, boolean recursive, boolean parentTransform, boolean mergeTransform, boolean shareKeyframes) {
/* 129 */     this.model = model;
/* 130 */     this.transform = (transform == null) ? new Matrix4() : transform;
/* 131 */     Node node = model.getNode(nodeId, recursive); Node copy;
/* 132 */     this.nodes.add(copy = node.copy());
/* 133 */     if (mergeTransform)
/* 134 */     { this.transform.mul(parentTransform ? node.globalTransform : node.localTransform);
/* 135 */       copy.translation.set(0.0F, 0.0F, 0.0F);
/* 136 */       copy.rotation.idt();
/* 137 */       copy.scale.set(1.0F, 1.0F, 1.0F); }
/* 138 */     else if (parentTransform && copy.hasParent()) { this.transform.mul((node.getParent()).globalTransform); }
/* 139 */      invalidate();
/* 140 */     copyAnimations((Iterable<Animation>)model.animations, shareKeyframes);
/* 141 */     calculateTransforms();
/*     */   }
/*     */ 
/*     */   
/*     */   public ModelInstance(Model model, String... rootNodeIds) {
/* 146 */     this(model, (Matrix4)null, rootNodeIds);
/*     */   }
/*     */ 
/*     */   
/*     */   public ModelInstance(Model model, Matrix4 transform, String... rootNodeIds) {
/* 151 */     this.model = model;
/* 152 */     this.transform = (transform == null) ? new Matrix4() : transform;
/* 153 */     if (rootNodeIds == null) {
/* 154 */       copyNodes(model.nodes);
/*     */     } else {
/* 156 */       copyNodes(model.nodes, rootNodeIds);
/* 157 */     }  copyAnimations((Iterable<Animation>)model.animations, defaultShareKeyframes);
/* 158 */     calculateTransforms();
/*     */   }
/*     */ 
/*     */   
/*     */   public ModelInstance(Model model, Array<String> rootNodeIds) {
/* 163 */     this(model, (Matrix4)null, rootNodeIds);
/*     */   }
/*     */ 
/*     */   
/*     */   public ModelInstance(Model model, Matrix4 transform, Array<String> rootNodeIds) {
/* 168 */     this(model, transform, rootNodeIds, defaultShareKeyframes);
/*     */   }
/*     */ 
/*     */   
/*     */   public ModelInstance(Model model, Matrix4 transform, Array<String> rootNodeIds, boolean shareKeyframes) {
/* 173 */     this.model = model;
/* 174 */     this.transform = (transform == null) ? new Matrix4() : transform;
/* 175 */     copyNodes(model.nodes, rootNodeIds);
/* 176 */     copyAnimations((Iterable<Animation>)model.animations, shareKeyframes);
/* 177 */     calculateTransforms();
/*     */   }
/*     */ 
/*     */   
/*     */   public ModelInstance(Model model, Vector3 position) {
/* 182 */     this(model);
/* 183 */     this.transform.setToTranslation(position);
/*     */   }
/*     */ 
/*     */   
/*     */   public ModelInstance(Model model, float x, float y, float z) {
/* 188 */     this(model);
/* 189 */     this.transform.setToTranslation(x, y, z);
/*     */   }
/*     */ 
/*     */   
/*     */   public ModelInstance(Model model, Matrix4 transform) {
/* 194 */     this(model, transform, (String[])null);
/*     */   }
/*     */ 
/*     */   
/*     */   public ModelInstance(ModelInstance copyFrom) {
/* 199 */     this(copyFrom, copyFrom.transform.cpy());
/*     */   }
/*     */ 
/*     */   
/*     */   public ModelInstance(ModelInstance copyFrom, Matrix4 transform) {
/* 204 */     this(copyFrom, transform, defaultShareKeyframes);
/*     */   }
/*     */ 
/*     */   
/*     */   public ModelInstance(ModelInstance copyFrom, Matrix4 transform, boolean shareKeyframes) {
/* 209 */     this.model = copyFrom.model;
/* 210 */     this.transform = (transform == null) ? new Matrix4() : transform;
/* 211 */     copyNodes(copyFrom.nodes);
/* 212 */     copyAnimations((Iterable<Animation>)copyFrom.animations, shareKeyframes);
/* 213 */     calculateTransforms();
/*     */   }
/*     */ 
/*     */   
/*     */   public ModelInstance copy() {
/* 218 */     return new ModelInstance(this);
/*     */   }
/*     */   
/*     */   private void copyNodes(Array<Node> nodes) {
/* 222 */     for (int i = 0, n = nodes.size; i < n; i++) {
/* 223 */       Node node = (Node)nodes.get(i);
/* 224 */       this.nodes.add(node.copy());
/*     */     } 
/* 226 */     invalidate();
/*     */   }
/*     */   
/*     */   private void copyNodes(Array<Node> nodes, String... nodeIds) {
/* 230 */     for (int i = 0, n = nodes.size; i < n; i++) {
/* 231 */       Node node = (Node)nodes.get(i);
/* 232 */       for (String nodeId : nodeIds) {
/* 233 */         if (nodeId.equals(node.id)) {
/* 234 */           this.nodes.add(node.copy());
/*     */           break;
/*     */         } 
/*     */       } 
/*     */     } 
/* 239 */     invalidate();
/*     */   }
/*     */   
/*     */   private void copyNodes(Array<Node> nodes, Array<String> nodeIds) {
/* 243 */     for (int i = 0, n = nodes.size; i < n; i++) {
/* 244 */       Node node = (Node)nodes.get(i);
/* 245 */       for (String nodeId : nodeIds) {
/* 246 */         if (nodeId.equals(node.id)) {
/* 247 */           this.nodes.add(node.copy());
/*     */           break;
/*     */         } 
/*     */       } 
/*     */     } 
/* 252 */     invalidate();
/*     */   }
/*     */   
/*     */   private void invalidate(Node node) {
/*     */     int i;
/*     */     int n;
/* 258 */     for (i = 0, n = node.parts.size; i < n; i++) {
/* 259 */       NodePart part = (NodePart)node.parts.get(i);
/* 260 */       ArrayMap<Node, Matrix4> bindPose = part.invBoneBindTransforms;
/* 261 */       if (bindPose != null) {
/* 262 */         for (int j = 0; j < bindPose.size; j++) {
/* 263 */           ((Node[])bindPose.keys)[j] = getNode((((Node[])bindPose.keys)[j]).id);
/*     */         }
/*     */       }
/* 266 */       if (!this.materials.contains(part.material, true)) {
/* 267 */         int midx = this.materials.indexOf(part.material, false);
/* 268 */         if (midx < 0) {
/* 269 */           this.materials.add(part.material = part.material.copy());
/*     */         } else {
/* 271 */           part.material = (Material)this.materials.get(midx);
/*     */         } 
/*     */       } 
/* 274 */     }  for (i = 0, n = node.getChildCount(); i < n; i++) {
/* 275 */       invalidate(node.getChild(i));
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void invalidate() {
/* 282 */     for (int i = 0, n = this.nodes.size; i < n; i++) {
/* 283 */       invalidate((Node)this.nodes.get(i));
/*     */     }
/*     */   }
/*     */   
/*     */   private void copyAnimations(Iterable<Animation> source, boolean shareKeyframes) {
/* 288 */     for (Animation anim : source) {
/* 289 */       Animation animation = new Animation();
/* 290 */       animation.id = anim.id;
/* 291 */       animation.duration = anim.duration;
/* 292 */       for (NodeAnimation nanim : anim.nodeAnimations) {
/* 293 */         Node node = getNode(nanim.node.id);
/* 294 */         if (node == null)
/* 295 */           continue;  NodeAnimation nodeAnim = new NodeAnimation();
/* 296 */         nodeAnim.node = node;
/* 297 */         if (shareKeyframes) {
/* 298 */           nodeAnim.translation = nanim.translation;
/* 299 */           nodeAnim.rotation = nanim.rotation;
/* 300 */           nodeAnim.scaling = nanim.scaling;
/*     */         } else {
/* 302 */           if (nanim.translation != null) {
/* 303 */             nodeAnim.translation = new Array();
/* 304 */             for (NodeKeyframe<Vector3> kf : (Iterable<NodeKeyframe<Vector3>>)nanim.translation)
/* 305 */               nodeAnim.translation.add(new NodeKeyframe(kf.keytime, kf.value)); 
/*     */           } 
/* 307 */           if (nanim.rotation != null) {
/* 308 */             nodeAnim.rotation = new Array();
/* 309 */             for (NodeKeyframe<Quaternion> kf : (Iterable<NodeKeyframe<Quaternion>>)nanim.rotation)
/* 310 */               nodeAnim.rotation.add(new NodeKeyframe(kf.keytime, kf.value)); 
/*     */           } 
/* 312 */           if (nanim.scaling != null) {
/* 313 */             nodeAnim.scaling = new Array();
/* 314 */             for (NodeKeyframe<Vector3> kf : (Iterable<NodeKeyframe<Vector3>>)nanim.scaling)
/* 315 */               nodeAnim.scaling.add(new NodeKeyframe(kf.keytime, kf.value)); 
/*     */           } 
/*     */         } 
/* 318 */         if (nodeAnim.translation != null || nodeAnim.rotation != null || nodeAnim.scaling != null)
/* 319 */           animation.nodeAnimations.add(nodeAnim); 
/*     */       } 
/* 321 */       if (animation.nodeAnimations.size > 0) this.animations.add(animation);
/*     */     
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void getRenderables(Array<Renderable> renderables, Pool<Renderable> pool) {
/* 331 */     for (Node node : this.nodes) {
/* 332 */       getRenderables(node, renderables, pool);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public Renderable getRenderable(Renderable out) {
/* 338 */     return getRenderable(out, (Node)this.nodes.get(0));
/*     */   }
/*     */ 
/*     */   
/*     */   public Renderable getRenderable(Renderable out, Node node) {
/* 343 */     return getRenderable(out, node, (NodePart)node.parts.get(0));
/*     */   }
/*     */   
/*     */   public Renderable getRenderable(Renderable out, Node node, NodePart nodePart) {
/* 347 */     nodePart.setRenderable(out);
/* 348 */     if (nodePart.bones == null && this.transform != null) {
/* 349 */       out.worldTransform.set(this.transform).mul(node.globalTransform);
/* 350 */     } else if (this.transform != null) {
/* 351 */       out.worldTransform.set(this.transform);
/*     */     } else {
/* 353 */       out.worldTransform.idt();
/* 354 */     }  out.userData = this.userData;
/* 355 */     return out;
/*     */   }
/*     */   
/*     */   protected void getRenderables(Node node, Array<Renderable> renderables, Pool<Renderable> pool) {
/* 359 */     if (node.parts.size > 0) {
/* 360 */       for (NodePart nodePart : node.parts) {
/* 361 */         if (nodePart.enabled) renderables.add(getRenderable((Renderable)pool.obtain(), node, nodePart));
/*     */       
/*     */       } 
/*     */     }
/* 365 */     for (Node child : node.getChildren()) {
/* 366 */       getRenderables(child, renderables, pool);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void calculateTransforms() {
/* 378 */     int n = this.nodes.size; int i;
/* 379 */     for (i = 0; i < n; i++) {
/* 380 */       ((Node)this.nodes.get(i)).calculateTransforms(true);
/*     */     }
/* 382 */     for (i = 0; i < n; i++) {
/* 383 */       ((Node)this.nodes.get(i)).calculateBoneTransforms(true);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public BoundingBox calculateBoundingBox(BoundingBox out) {
/* 391 */     out.inf();
/* 392 */     return extendBoundingBox(out);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public BoundingBox extendBoundingBox(BoundingBox out) {
/* 400 */     int n = this.nodes.size;
/* 401 */     for (int i = 0; i < n; i++)
/* 402 */       ((Node)this.nodes.get(i)).extendBoundingBox(out); 
/* 403 */     return out;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Animation getAnimation(String id) {
/* 409 */     return getAnimation(id, false);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Animation getAnimation(String id, boolean ignoreCase) {
/* 416 */     int n = this.animations.size;
/*     */     
/* 418 */     if (ignoreCase)
/* 419 */     { for (int i = 0; i < n; i++) {
/* 420 */         Animation animation; if ((animation = (Animation)this.animations.get(i)).id.equalsIgnoreCase(id)) return animation; 
/*     */       }  }
/* 422 */     else { for (int i = 0; i < n; i++) {
/* 423 */         Animation animation; if ((animation = (Animation)this.animations.get(i)).id.equals(id)) return animation; 
/*     */       }  }
/* 425 */      return null;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Material getMaterial(String id) {
/* 431 */     return getMaterial(id, true);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Material getMaterial(String id, boolean ignoreCase) {
/* 438 */     int n = this.materials.size;
/*     */     
/* 440 */     if (ignoreCase)
/* 441 */     { for (int i = 0; i < n; i++) {
/* 442 */         Material material; if ((material = (Material)this.materials.get(i)).id.equalsIgnoreCase(id)) return material; 
/*     */       }  }
/* 444 */     else { for (int i = 0; i < n; i++) {
/* 445 */         Material material; if ((material = (Material)this.materials.get(i)).id.equals(id)) return material; 
/*     */       }  }
/* 447 */      return null;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Node getNode(String id) {
/* 453 */     return getNode(id, true);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Node getNode(String id, boolean recursive) {
/* 460 */     return getNode(id, recursive, false);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Node getNode(String id, boolean recursive, boolean ignoreCase) {
/* 468 */     return Node.getNode(this.nodes, id, recursive, ignoreCase);
/*     */   }
/*     */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\graphics\g3d\ModelInstance.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */