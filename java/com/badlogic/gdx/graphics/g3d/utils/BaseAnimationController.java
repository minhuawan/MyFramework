/*     */ package com.badlogic.gdx.graphics.g3d.utils;
/*     */ 
/*     */ import com.badlogic.gdx.graphics.g3d.ModelInstance;
/*     */ import com.badlogic.gdx.graphics.g3d.model.Animation;
/*     */ import com.badlogic.gdx.graphics.g3d.model.Node;
/*     */ import com.badlogic.gdx.graphics.g3d.model.NodeAnimation;
/*     */ import com.badlogic.gdx.graphics.g3d.model.NodeKeyframe;
/*     */ import com.badlogic.gdx.math.Matrix4;
/*     */ import com.badlogic.gdx.math.Quaternion;
/*     */ import com.badlogic.gdx.math.Vector3;
/*     */ import com.badlogic.gdx.utils.Array;
/*     */ import com.badlogic.gdx.utils.GdxRuntimeException;
/*     */ import com.badlogic.gdx.utils.ObjectMap;
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
/*     */ public class BaseAnimationController
/*     */ {
/*     */   public static final class Transform
/*     */     implements Pool.Poolable
/*     */   {
/*  41 */     public final Vector3 translation = new Vector3();
/*  42 */     public final Quaternion rotation = new Quaternion();
/*  43 */     public final Vector3 scale = new Vector3(1.0F, 1.0F, 1.0F);
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Transform idt() {
/*  49 */       this.translation.set(0.0F, 0.0F, 0.0F);
/*  50 */       this.rotation.idt();
/*  51 */       this.scale.set(1.0F, 1.0F, 1.0F);
/*  52 */       return this;
/*     */     }
/*     */     
/*     */     public Transform set(Vector3 t, Quaternion r, Vector3 s) {
/*  56 */       this.translation.set(t);
/*  57 */       this.rotation.set(r);
/*  58 */       this.scale.set(s);
/*  59 */       return this;
/*     */     }
/*     */     
/*     */     public Transform set(Transform other) {
/*  63 */       return set(other.translation, other.rotation, other.scale);
/*     */     }
/*     */     
/*     */     public Transform lerp(Transform target, float alpha) {
/*  67 */       return lerp(target.translation, target.rotation, target.scale, alpha);
/*     */     }
/*     */     
/*     */     public Transform lerp(Vector3 targetT, Quaternion targetR, Vector3 targetS, float alpha) {
/*  71 */       this.translation.lerp(targetT, alpha);
/*  72 */       this.rotation.slerp(targetR, alpha);
/*  73 */       this.scale.lerp(targetS, alpha);
/*  74 */       return this;
/*     */     }
/*     */     
/*     */     public Matrix4 toMatrix4(Matrix4 out) {
/*  78 */       return out.set(this.translation, this.rotation, this.scale);
/*     */     }
/*     */ 
/*     */     
/*     */     public void reset() {
/*  83 */       idt();
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/*  88 */       return this.translation.toString() + " - " + this.rotation.toString() + " - " + this.scale.toString();
/*     */     }
/*     */   }
/*     */   
/*  92 */   private final Pool<Transform> transformPool = new Pool<Transform>()
/*     */     {
/*     */       protected BaseAnimationController.Transform newObject() {
/*  95 */         return new BaseAnimationController.Transform();
/*     */       }
/*     */     };
/*  98 */   private static final ObjectMap<Node, Transform> transforms = new ObjectMap();
/*     */   
/*     */   private boolean applying = false;
/*     */   
/*     */   public final ModelInstance target;
/*     */ 
/*     */   
/*     */   public BaseAnimationController(ModelInstance target) {
/* 106 */     this.target = target;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void begin() {
/* 112 */     if (this.applying) throw new GdxRuntimeException("You must call end() after each call to being()"); 
/* 113 */     this.applying = true;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void apply(Animation animation, float time, float weight) {
/* 119 */     if (!this.applying) throw new GdxRuntimeException("You must call begin() before adding an animation"); 
/* 120 */     applyAnimation(transforms, this.transformPool, weight, animation, time);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void end() {
/* 125 */     if (!this.applying) throw new GdxRuntimeException("You must call begin() first"); 
/* 126 */     for (ObjectMap.Entries<ObjectMap.Entry<Node, Transform>> entries = transforms.entries().iterator(); entries.hasNext(); ) { ObjectMap.Entry<Node, Transform> entry = entries.next();
/* 127 */       ((Transform)entry.value).toMatrix4(((Node)entry.key).localTransform);
/* 128 */       this.transformPool.free(entry.value); }
/*     */     
/* 130 */     transforms.clear();
/* 131 */     this.target.calculateTransforms();
/* 132 */     this.applying = false;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void applyAnimation(Animation animation, float time) {
/* 137 */     if (this.applying) throw new GdxRuntimeException("Call end() first"); 
/* 138 */     applyAnimation(null, null, 1.0F, animation, time);
/* 139 */     this.target.calculateTransforms();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void applyAnimations(Animation anim1, float time1, Animation anim2, float time2, float weight) {
/* 145 */     if (anim2 == null || weight == 0.0F)
/* 146 */     { applyAnimation(anim1, time1); }
/* 147 */     else if (anim1 == null || weight == 1.0F)
/* 148 */     { applyAnimation(anim2, time2); }
/* 149 */     else { if (this.applying) {
/* 150 */         throw new GdxRuntimeException("Call end() first");
/*     */       }
/* 152 */       begin();
/* 153 */       apply(anim1, time1, 1.0F);
/* 154 */       apply(anim2, time2, weight);
/* 155 */       end(); }
/*     */   
/*     */   }
/*     */   
/* 159 */   private static final Transform tmpT = new Transform();
/*     */   
/*     */   private static final <T> int getFirstKeyframeIndexAtTime(Array<NodeKeyframe<T>> arr, float time) {
/* 162 */     int n = arr.size - 1;
/* 163 */     for (int i = 0; i < n; i++) {
/* 164 */       if (time >= ((NodeKeyframe)arr.get(i)).keytime && time <= ((NodeKeyframe)arr.get(i + 1)).keytime) {
/* 165 */         return i;
/*     */       }
/*     */     } 
/* 168 */     return 0;
/*     */   }
/*     */   
/*     */   private static final Vector3 getTranslationAtTime(NodeAnimation nodeAnim, float time, Vector3 out) {
/* 172 */     if (nodeAnim.translation == null) return out.set(nodeAnim.node.translation); 
/* 173 */     if (nodeAnim.translation.size == 1) return out.set((Vector3)((NodeKeyframe)nodeAnim.translation.get(0)).value);
/*     */     
/* 175 */     int index = getFirstKeyframeIndexAtTime(nodeAnim.translation, time);
/* 176 */     NodeKeyframe firstKeyframe = (NodeKeyframe)nodeAnim.translation.get(index);
/* 177 */     out.set((Vector3)firstKeyframe.value);
/*     */     
/* 179 */     if (++index < nodeAnim.translation.size) {
/* 180 */       NodeKeyframe<Vector3> secondKeyframe = (NodeKeyframe<Vector3>)nodeAnim.translation.get(index);
/* 181 */       float t = (time - firstKeyframe.keytime) / (secondKeyframe.keytime - firstKeyframe.keytime);
/* 182 */       out.lerp((Vector3)secondKeyframe.value, t);
/*     */     } 
/* 184 */     return out;
/*     */   }
/*     */   
/*     */   private static final Quaternion getRotationAtTime(NodeAnimation nodeAnim, float time, Quaternion out) {
/* 188 */     if (nodeAnim.rotation == null) return out.set(nodeAnim.node.rotation); 
/* 189 */     if (nodeAnim.rotation.size == 1) return out.set((Quaternion)((NodeKeyframe)nodeAnim.rotation.get(0)).value);
/*     */     
/* 191 */     int index = getFirstKeyframeIndexAtTime(nodeAnim.rotation, time);
/* 192 */     NodeKeyframe firstKeyframe = (NodeKeyframe)nodeAnim.rotation.get(index);
/* 193 */     out.set((Quaternion)firstKeyframe.value);
/*     */     
/* 195 */     if (++index < nodeAnim.rotation.size) {
/* 196 */       NodeKeyframe<Quaternion> secondKeyframe = (NodeKeyframe<Quaternion>)nodeAnim.rotation.get(index);
/* 197 */       float t = (time - firstKeyframe.keytime) / (secondKeyframe.keytime - firstKeyframe.keytime);
/* 198 */       out.slerp((Quaternion)secondKeyframe.value, t);
/*     */     } 
/* 200 */     return out;
/*     */   }
/*     */   
/*     */   private static final Vector3 getScalingAtTime(NodeAnimation nodeAnim, float time, Vector3 out) {
/* 204 */     if (nodeAnim.scaling == null) return out.set(nodeAnim.node.scale); 
/* 205 */     if (nodeAnim.scaling.size == 1) return out.set((Vector3)((NodeKeyframe)nodeAnim.scaling.get(0)).value);
/*     */     
/* 207 */     int index = getFirstKeyframeIndexAtTime(nodeAnim.scaling, time);
/* 208 */     NodeKeyframe firstKeyframe = (NodeKeyframe)nodeAnim.scaling.get(index);
/* 209 */     out.set((Vector3)firstKeyframe.value);
/*     */     
/* 211 */     if (++index < nodeAnim.scaling.size) {
/* 212 */       NodeKeyframe<Vector3> secondKeyframe = (NodeKeyframe<Vector3>)nodeAnim.scaling.get(index);
/* 213 */       float t = (time - firstKeyframe.keytime) / (secondKeyframe.keytime - firstKeyframe.keytime);
/* 214 */       out.lerp((Vector3)secondKeyframe.value, t);
/*     */     } 
/* 216 */     return out;
/*     */   }
/*     */   
/*     */   private static final Transform getNodeAnimationTransform(NodeAnimation nodeAnim, float time) {
/* 220 */     Transform transform = tmpT;
/* 221 */     getTranslationAtTime(nodeAnim, time, transform.translation);
/* 222 */     getRotationAtTime(nodeAnim, time, transform.rotation);
/* 223 */     getScalingAtTime(nodeAnim, time, transform.scale);
/* 224 */     return transform;
/*     */   }
/*     */   
/*     */   private static final void applyNodeAnimationDirectly(NodeAnimation nodeAnim, float time) {
/* 228 */     Node node = nodeAnim.node;
/* 229 */     node.isAnimated = true;
/* 230 */     Transform transform = getNodeAnimationTransform(nodeAnim, time);
/* 231 */     transform.toMatrix4(node.localTransform);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static final void applyNodeAnimationBlending(NodeAnimation nodeAnim, ObjectMap<Node, Transform> out, Pool<Transform> pool, float alpha, float time) {
/* 237 */     Node node = nodeAnim.node;
/* 238 */     node.isAnimated = true;
/* 239 */     Transform transform = getNodeAnimationTransform(nodeAnim, time);
/*     */     
/* 241 */     Transform t = (Transform)out.get(node, null);
/* 242 */     if (t != null) {
/* 243 */       if (alpha > 0.999999F) {
/* 244 */         t.set(transform);
/*     */       } else {
/* 246 */         t.lerp(transform, alpha);
/*     */       } 
/* 248 */     } else if (alpha > 0.999999F) {
/* 249 */       out.put(node, ((Transform)pool.obtain()).set(transform));
/*     */     } else {
/* 251 */       out.put(node, ((Transform)pool.obtain()).set(node.translation, node.rotation, node.scale).lerp(transform, alpha));
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected static void applyAnimation(ObjectMap<Node, Transform> out, Pool<Transform> pool, float alpha, Animation animation, float time) {
/* 259 */     if (out == null) {
/* 260 */       for (NodeAnimation nodeAnim : animation.nodeAnimations)
/* 261 */         applyNodeAnimationDirectly(nodeAnim, time); 
/*     */     } else {
/* 263 */       for (ObjectMap.Keys<Node> keys = out.keys().iterator(); keys.hasNext(); ) { Node node = keys.next();
/* 264 */         node.isAnimated = false; }
/* 265 */        for (NodeAnimation nodeAnim : animation.nodeAnimations)
/* 266 */         applyNodeAnimationBlending(nodeAnim, out, pool, alpha, time); 
/* 267 */       for (ObjectMap.Entries<ObjectMap.Entry<Node, Transform>> entries = out.entries().iterator(); entries.hasNext(); ) { ObjectMap.Entry<Node, Transform> e = entries.next();
/* 268 */         if (!((Node)e.key).isAnimated) {
/* 269 */           ((Node)e.key).isAnimated = true;
/* 270 */           ((Transform)e.value).lerp(((Node)e.key).translation, ((Node)e.key).rotation, ((Node)e.key).scale, alpha);
/*     */         }  }
/*     */     
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void removeAnimation(Animation animation) {
/* 279 */     for (NodeAnimation nodeAnim : animation.nodeAnimations)
/* 280 */       nodeAnim.node.isAnimated = false; 
/*     */   }
/*     */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\graphics\g3\\utils\BaseAnimationController.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */