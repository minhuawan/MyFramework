/*     */ package com.esotericsoftware.spine;
/*     */ 
/*     */ import com.badlogic.gdx.graphics.Color;
/*     */ import com.badlogic.gdx.math.Vector2;
/*     */ import com.badlogic.gdx.utils.Array;
/*     */ import com.badlogic.gdx.utils.ObjectMap;
/*     */ import com.esotericsoftware.spine.attachments.Attachment;
/*     */ import com.esotericsoftware.spine.attachments.MeshAttachment;
/*     */ import com.esotericsoftware.spine.attachments.PathAttachment;
/*     */ import com.esotericsoftware.spine.attachments.RegionAttachment;
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
/*     */ public class Skeleton
/*     */ {
/*     */   final SkeletonData data;
/*     */   final Array<Bone> bones;
/*     */   final Array<Slot> slots;
/*     */   Array<Slot> drawOrder;
/*     */   final Array<IkConstraint> ikConstraints;
/*     */   final Array<IkConstraint> ikConstraintsSorted;
/*     */   final Array<TransformConstraint> transformConstraints;
/*     */   final Array<PathConstraint> pathConstraints;
/*  52 */   final Array<Updatable> updateCache = new Array();
/*     */   
/*     */   Skin skin;
/*     */   
/*     */   final Color color;
/*     */   float time;
/*     */   
/*     */   public Skeleton(SkeletonData data) {
/*  60 */     if (data == null) throw new IllegalArgumentException("data cannot be null."); 
/*  61 */     this.data = data;
/*     */     
/*  63 */     this.bones = new Array(data.bones.size);
/*  64 */     for (BoneData boneData : data.bones) {
/*     */       Bone bone;
/*  66 */       if (boneData.parent == null) {
/*  67 */         bone = new Bone(boneData, this, null);
/*     */       } else {
/*  69 */         Bone parent = (Bone)this.bones.get(boneData.parent.index);
/*  70 */         bone = new Bone(boneData, this, parent);
/*  71 */         parent.children.add(bone);
/*     */       } 
/*  73 */       this.bones.add(bone);
/*     */     } 
/*     */     
/*  76 */     this.slots = new Array(data.slots.size);
/*  77 */     this.drawOrder = new Array(data.slots.size);
/*  78 */     for (SlotData slotData : data.slots) {
/*  79 */       Bone bone = (Bone)this.bones.get(slotData.boneData.index);
/*  80 */       Slot slot = new Slot(slotData, bone);
/*  81 */       this.slots.add(slot);
/*  82 */       this.drawOrder.add(slot);
/*     */     } 
/*     */     
/*  85 */     this.ikConstraints = new Array(data.ikConstraints.size);
/*  86 */     this.ikConstraintsSorted = new Array(this.ikConstraints.size);
/*  87 */     for (IkConstraintData ikConstraintData : data.ikConstraints) {
/*  88 */       this.ikConstraints.add(new IkConstraint(ikConstraintData, this));
/*     */     }
/*  90 */     this.transformConstraints = new Array(data.transformConstraints.size);
/*  91 */     for (TransformConstraintData transformConstraintData : data.transformConstraints) {
/*  92 */       this.transformConstraints.add(new TransformConstraint(transformConstraintData, this));
/*     */     }
/*  94 */     this.pathConstraints = new Array(data.pathConstraints.size);
/*  95 */     for (PathConstraintData pathConstraintData : data.pathConstraints) {
/*  96 */       this.pathConstraints.add(new PathConstraint(pathConstraintData, this));
/*     */     }
/*  98 */     this.color = new Color(1.0F, 1.0F, 1.0F, 1.0F);
/*     */     
/* 100 */     updateCache();
/*     */   }
/*     */   boolean flipX; boolean flipY; float x; float y;
/*     */   
/*     */   public Skeleton(Skeleton skeleton) {
/* 105 */     if (skeleton == null) throw new IllegalArgumentException("skeleton cannot be null."); 
/* 106 */     this.data = skeleton.data;
/*     */     
/* 108 */     this.bones = new Array(skeleton.bones.size);
/* 109 */     for (Bone bone : skeleton.bones) {
/*     */       Bone copy;
/* 111 */       if (bone.parent == null) {
/* 112 */         copy = new Bone(bone, this, null);
/*     */       } else {
/* 114 */         Bone parent = (Bone)this.bones.get(bone.parent.data.index);
/* 115 */         copy = new Bone(bone, this, parent);
/* 116 */         parent.children.add(copy);
/*     */       } 
/* 118 */       this.bones.add(copy);
/*     */     } 
/*     */     
/* 121 */     this.slots = new Array(skeleton.slots.size);
/* 122 */     for (Slot slot : skeleton.slots) {
/* 123 */       Bone bone = (Bone)this.bones.get(slot.bone.data.index);
/* 124 */       this.slots.add(new Slot(slot, bone));
/*     */     } 
/*     */     
/* 127 */     this.drawOrder = new Array(this.slots.size);
/* 128 */     for (Slot slot : skeleton.drawOrder) {
/* 129 */       this.drawOrder.add(this.slots.get(slot.data.index));
/*     */     }
/* 131 */     this.ikConstraints = new Array(skeleton.ikConstraints.size);
/* 132 */     this.ikConstraintsSorted = new Array(this.ikConstraints.size);
/* 133 */     for (IkConstraint ikConstraint : skeleton.ikConstraints) {
/* 134 */       this.ikConstraints.add(new IkConstraint(ikConstraint, this));
/*     */     }
/* 136 */     this.transformConstraints = new Array(skeleton.transformConstraints.size);
/* 137 */     for (TransformConstraint transformConstraint : skeleton.transformConstraints) {
/* 138 */       this.transformConstraints.add(new TransformConstraint(transformConstraint, this));
/*     */     }
/* 140 */     this.pathConstraints = new Array(skeleton.pathConstraints.size);
/* 141 */     for (PathConstraint pathConstraint : skeleton.pathConstraints) {
/* 142 */       this.pathConstraints.add(new PathConstraint(pathConstraint, this));
/*     */     }
/* 144 */     this.skin = skeleton.skin;
/* 145 */     this.color = new Color(skeleton.color);
/* 146 */     this.time = skeleton.time;
/* 147 */     this.flipX = skeleton.flipX;
/* 148 */     this.flipY = skeleton.flipY;
/*     */     
/* 150 */     updateCache();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void updateCache() {
/* 156 */     Array<Updatable> updateCache = this.updateCache;
/* 157 */     updateCache.clear();
/*     */     
/* 159 */     Array<Bone> bones = this.bones;
/* 160 */     for (int i = 0, n = bones.size; i < n; i++) {
/* 161 */       ((Bone)bones.get(i)).sorted = false;
/*     */     }
/*     */     
/* 164 */     Array<IkConstraint> ikConstraints = this.ikConstraintsSorted;
/* 165 */     ikConstraints.clear();
/* 166 */     ikConstraints.addAll(this.ikConstraints);
/* 167 */     int ikCount = ikConstraints.size; int j, i2;
/* 168 */     for (j = 0, i2 = ikCount; j < i2; j++) {
/* 169 */       IkConstraint ik = (IkConstraint)ikConstraints.get(j);
/* 170 */       Bone bone = ((Bone)ik.bones.first()).parent; int level;
/* 171 */       for (level = 0; bone != null; level++)
/* 172 */         bone = bone.parent; 
/* 173 */       ik.level = level;
/*     */     } 
/* 175 */     for (j = 1; j < ikCount; j++) {
/* 176 */       IkConstraint ik = (IkConstraint)ikConstraints.get(j);
/* 177 */       int level = ik.level; int ii;
/* 178 */       for (ii = j - 1; ii >= 0; ii--) {
/* 179 */         IkConstraint other = (IkConstraint)ikConstraints.get(ii);
/* 180 */         if (other.level < level)
/* 181 */           break;  ikConstraints.set(ii + 1, other);
/*     */       } 
/* 183 */       ikConstraints.set(ii + 1, ik);
/*     */     }  int m;
/* 185 */     for (j = 0, m = ikConstraints.size; j < m; j++) {
/* 186 */       IkConstraint constraint = (IkConstraint)ikConstraints.get(j);
/* 187 */       Bone target = constraint.target;
/* 188 */       sortBone(target);
/*     */       
/* 190 */       Array<Bone> constrained = constraint.bones;
/* 191 */       Bone parent = (Bone)constrained.first();
/* 192 */       sortBone(parent);
/*     */       
/* 194 */       updateCache.add(constraint);
/*     */       
/* 196 */       sortReset(parent.children);
/* 197 */       ((Bone)constrained.peek()).sorted = true;
/*     */     } 
/*     */     
/* 200 */     Array<PathConstraint> pathConstraints = this.pathConstraints;
/* 201 */     for (int k = 0; k < i2; k++) {
/* 202 */       PathConstraint constraint = (PathConstraint)pathConstraints.get(k);
/*     */       
/* 204 */       Slot slot = constraint.target;
/* 205 */       int slotIndex = (slot.getData()).index;
/* 206 */       Bone slotBone = slot.bone;
/* 207 */       if (this.skin != null) sortPathConstraintAttachment(this.skin, slotIndex, slotBone); 
/* 208 */       if (this.data.defaultSkin != null && this.data.defaultSkin != this.skin)
/* 209 */         sortPathConstraintAttachment(this.data.defaultSkin, slotIndex, slotBone); 
/* 210 */       for (int ii = 0, nn = this.data.skins.size; ii < nn; ii++) {
/* 211 */         sortPathConstraintAttachment((Skin)this.data.skins.get(ii), slotIndex, slotBone);
/*     */       }
/* 213 */       Attachment attachment = slot.attachment;
/* 214 */       if (attachment instanceof PathAttachment) sortPathConstraintAttachment(attachment, slotBone);
/*     */       
/* 216 */       Array<Bone> constrained = constraint.bones;
/* 217 */       int boneCount = constrained.size; int i4;
/* 218 */       for (i4 = 0; i4 < boneCount; i4++) {
/* 219 */         sortBone((Bone)constrained.get(i4));
/*     */       }
/* 221 */       updateCache.add(constraint);
/*     */       
/* 223 */       for (i4 = 0; i4 < boneCount; i4++)
/* 224 */         sortReset(((Bone)constrained.get(i4)).children); 
/* 225 */       for (i4 = 0; i4 < boneCount; i4++) {
/* 226 */         ((Bone)constrained.get(i4)).sorted = true;
/*     */       }
/*     */     } 
/* 229 */     Array<TransformConstraint> transformConstraints = this.transformConstraints; int i1, i3;
/* 230 */     for (i1 = 0, i3 = transformConstraints.size; i1 < i3; i1++) {
/* 231 */       TransformConstraint constraint = (TransformConstraint)transformConstraints.get(i1);
/*     */       
/* 233 */       sortBone(constraint.target);
/*     */       
/* 235 */       Array<Bone> constrained = constraint.bones;
/* 236 */       int boneCount = constrained.size; int ii;
/* 237 */       for (ii = 0; ii < boneCount; ii++) {
/* 238 */         sortBone((Bone)constrained.get(ii));
/*     */       }
/* 240 */       updateCache.add(constraint);
/*     */       
/* 242 */       for (ii = 0; ii < boneCount; ii++)
/* 243 */         sortReset(((Bone)constrained.get(ii)).children); 
/* 244 */       for (ii = 0; ii < boneCount; ii++) {
/* 245 */         ((Bone)constrained.get(ii)).sorted = true;
/*     */       }
/*     */     } 
/* 248 */     for (i1 = 0, i3 = bones.size; i1 < i3; i1++)
/* 249 */       sortBone((Bone)bones.get(i1)); 
/*     */   }
/*     */   
/*     */   private void sortPathConstraintAttachment(Skin skin, int slotIndex, Bone slotBone) {
/* 253 */     for (ObjectMap.Entries<ObjectMap.Entry<Skin.Key, Attachment>> entries = skin.attachments.entries().iterator(); entries.hasNext(); ) { ObjectMap.Entry<Skin.Key, Attachment> entry = entries.next();
/* 254 */       if (((Skin.Key)entry.key).slotIndex == slotIndex) sortPathConstraintAttachment((Attachment)entry.value, slotBone);  }
/*     */   
/*     */   }
/*     */   private void sortPathConstraintAttachment(Attachment attachment, Bone slotBone) {
/* 258 */     if (!(attachment instanceof PathAttachment))
/* 259 */       return;  int[] pathBones = ((PathAttachment)attachment).getBones();
/* 260 */     if (pathBones == null) {
/* 261 */       sortBone(slotBone);
/*     */     } else {
/* 263 */       Array<Bone> bones = this.bones;
/* 264 */       for (int boneIndex : pathBones)
/* 265 */         sortBone((Bone)bones.get(boneIndex)); 
/*     */     } 
/*     */   }
/*     */   
/*     */   private void sortBone(Bone bone) {
/* 270 */     if (bone.sorted)
/* 271 */       return;  Bone parent = bone.parent;
/* 272 */     if (parent != null) sortBone(parent); 
/* 273 */     bone.sorted = true;
/* 274 */     this.updateCache.add(bone);
/*     */   }
/*     */   
/*     */   private void sortReset(Array<Bone> bones) {
/* 278 */     for (int i = 0, n = bones.size; i < n; i++) {
/* 279 */       Bone bone = (Bone)bones.get(i);
/* 280 */       if (bone.sorted) sortReset(bone.children); 
/* 281 */       bone.sorted = false;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void updateWorldTransform() {
/* 287 */     Array<Updatable> updateCache = this.updateCache;
/* 288 */     for (int i = 0, n = updateCache.size; i < n; i++) {
/* 289 */       ((Updatable)updateCache.get(i)).update();
/*     */     }
/*     */   }
/*     */   
/*     */   public void setToSetupPose() {
/* 294 */     setBonesToSetupPose();
/* 295 */     setSlotsToSetupPose();
/*     */   }
/*     */ 
/*     */   
/*     */   public void setBonesToSetupPose() {
/* 300 */     Array<Bone> bones = this.bones;
/* 301 */     for (int i = 0, n = bones.size; i < n; i++) {
/* 302 */       ((Bone)bones.get(i)).setToSetupPose();
/*     */     }
/* 304 */     Array<IkConstraint> ikConstraints = this.ikConstraints;
/* 305 */     for (int j = 0, m = ikConstraints.size; j < m; j++) {
/* 306 */       IkConstraint constraint = (IkConstraint)ikConstraints.get(j);
/* 307 */       constraint.bendDirection = constraint.data.bendDirection;
/* 308 */       constraint.mix = constraint.data.mix;
/*     */     } 
/*     */     
/* 311 */     Array<TransformConstraint> transformConstraints = this.transformConstraints;
/* 312 */     for (int k = 0, i2 = transformConstraints.size; k < i2; k++) {
/* 313 */       TransformConstraint constraint = (TransformConstraint)transformConstraints.get(k);
/* 314 */       TransformConstraintData data = constraint.data;
/* 315 */       constraint.rotateMix = data.rotateMix;
/* 316 */       constraint.translateMix = data.translateMix;
/* 317 */       constraint.scaleMix = data.scaleMix;
/* 318 */       constraint.shearMix = data.shearMix;
/*     */     } 
/*     */     
/* 321 */     Array<PathConstraint> pathConstraints = this.pathConstraints;
/* 322 */     for (int i1 = 0, i3 = pathConstraints.size; i1 < i3; i1++) {
/* 323 */       PathConstraint constraint = (PathConstraint)pathConstraints.get(i1);
/* 324 */       PathConstraintData data = constraint.data;
/* 325 */       constraint.position = data.position;
/* 326 */       constraint.spacing = data.spacing;
/* 327 */       constraint.rotateMix = data.rotateMix;
/* 328 */       constraint.translateMix = data.translateMix;
/*     */     } 
/*     */   }
/*     */   
/*     */   public void setSlotsToSetupPose() {
/* 333 */     Array<Slot> slots = this.slots;
/* 334 */     System.arraycopy(slots.items, 0, this.drawOrder.items, 0, slots.size);
/* 335 */     for (int i = 0, n = slots.size; i < n; i++)
/* 336 */       ((Slot)slots.get(i)).setToSetupPose(); 
/*     */   }
/*     */   
/*     */   public SkeletonData getData() {
/* 340 */     return this.data;
/*     */   }
/*     */   
/*     */   public Array<Bone> getBones() {
/* 344 */     return this.bones;
/*     */   }
/*     */   
/*     */   public Array<Updatable> getUpdateCache() {
/* 348 */     return this.updateCache;
/*     */   }
/*     */ 
/*     */   
/*     */   public Bone getRootBone() {
/* 353 */     if (this.bones.size == 0) return null; 
/* 354 */     return (Bone)this.bones.first();
/*     */   }
/*     */ 
/*     */   
/*     */   public Bone findBone(String boneName) {
/* 359 */     if (boneName == null) throw new IllegalArgumentException("boneName cannot be null."); 
/* 360 */     Array<Bone> bones = this.bones;
/* 361 */     for (int i = 0, n = bones.size; i < n; i++) {
/* 362 */       Bone bone = (Bone)bones.get(i);
/* 363 */       if (bone.data.name.equals(boneName)) return bone; 
/*     */     } 
/* 365 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public int findBoneIndex(String boneName) {
/* 370 */     if (boneName == null) throw new IllegalArgumentException("boneName cannot be null."); 
/* 371 */     Array<Bone> bones = this.bones;
/* 372 */     for (int i = 0, n = bones.size; i < n; i++) {
/* 373 */       if (((Bone)bones.get(i)).data.name.equals(boneName)) return i; 
/* 374 */     }  return -1;
/*     */   }
/*     */   
/*     */   public Array<Slot> getSlots() {
/* 378 */     return this.slots;
/*     */   }
/*     */ 
/*     */   
/*     */   public Slot findSlot(String slotName) {
/* 383 */     if (slotName == null) throw new IllegalArgumentException("slotName cannot be null."); 
/* 384 */     Array<Slot> slots = this.slots;
/* 385 */     for (int i = 0, n = slots.size; i < n; i++) {
/* 386 */       Slot slot = (Slot)slots.get(i);
/* 387 */       if (slot.data.name.equals(slotName)) return slot; 
/*     */     } 
/* 389 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public int findSlotIndex(String slotName) {
/* 394 */     if (slotName == null) throw new IllegalArgumentException("slotName cannot be null."); 
/* 395 */     Array<Slot> slots = this.slots;
/* 396 */     for (int i = 0, n = slots.size; i < n; i++) {
/* 397 */       if (((Slot)slots.get(i)).data.name.equals(slotName)) return i; 
/* 398 */     }  return -1;
/*     */   }
/*     */ 
/*     */   
/*     */   public Array<Slot> getDrawOrder() {
/* 403 */     return this.drawOrder;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setDrawOrder(Array<Slot> drawOrder) {
/* 408 */     if (drawOrder == null) throw new IllegalArgumentException("drawOrder cannot be null."); 
/* 409 */     this.drawOrder = drawOrder;
/*     */   }
/*     */ 
/*     */   
/*     */   public Skin getSkin() {
/* 414 */     return this.skin;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setSkin(String skinName) {
/* 420 */     Skin skin = this.data.findSkin(skinName);
/* 421 */     if (skin == null) throw new IllegalArgumentException("Skin not found: " + skinName); 
/* 422 */     setSkin(skin);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setSkin(Skin newSkin) {
/* 430 */     if (newSkin != null)
/* 431 */       if (this.skin != null) {
/* 432 */         newSkin.attachAll(this, this.skin);
/*     */       } else {
/* 434 */         Array<Slot> slots = this.slots;
/* 435 */         for (int i = 0, n = slots.size; i < n; i++) {
/* 436 */           Slot slot = (Slot)slots.get(i);
/* 437 */           String name = slot.data.attachmentName;
/* 438 */           if (name != null) {
/* 439 */             Attachment attachment = newSkin.getAttachment(i, name);
/* 440 */             if (attachment != null) slot.setAttachment(attachment);
/*     */           
/*     */           } 
/*     */         } 
/*     */       }  
/* 445 */     this.skin = newSkin;
/*     */   }
/*     */ 
/*     */   
/*     */   public Attachment getAttachment(String slotName, String attachmentName) {
/* 450 */     return getAttachment(this.data.findSlotIndex(slotName), attachmentName);
/*     */   }
/*     */ 
/*     */   
/*     */   public Attachment getAttachment(int slotIndex, String attachmentName) {
/* 455 */     if (attachmentName == null) throw new IllegalArgumentException("attachmentName cannot be null."); 
/* 456 */     if (this.skin != null) {
/* 457 */       Attachment attachment = this.skin.getAttachment(slotIndex, attachmentName);
/* 458 */       if (attachment != null) return attachment; 
/*     */     } 
/* 460 */     if (this.data.defaultSkin != null) return this.data.defaultSkin.getAttachment(slotIndex, attachmentName); 
/* 461 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setAttachment(String slotName, String attachmentName) {
/* 466 */     if (slotName == null) throw new IllegalArgumentException("slotName cannot be null."); 
/* 467 */     Array<Slot> slots = this.slots;
/* 468 */     for (int i = 0, n = slots.size; i < n; i++) {
/* 469 */       Slot slot = (Slot)slots.get(i);
/* 470 */       if (slot.data.name.equals(slotName)) {
/* 471 */         Attachment attachment = null;
/* 472 */         if (attachmentName != null) {
/* 473 */           attachment = getAttachment(i, attachmentName);
/* 474 */           if (attachment == null)
/* 475 */             throw new IllegalArgumentException("Attachment not found: " + attachmentName + ", for slot: " + slotName); 
/*     */         } 
/* 477 */         slot.setAttachment(attachment);
/*     */         return;
/*     */       } 
/*     */     } 
/* 481 */     throw new IllegalArgumentException("Slot not found: " + slotName);
/*     */   }
/*     */   
/*     */   public Array<IkConstraint> getIkConstraints() {
/* 485 */     return this.ikConstraints;
/*     */   }
/*     */ 
/*     */   
/*     */   public IkConstraint findIkConstraint(String constraintName) {
/* 490 */     if (constraintName == null) throw new IllegalArgumentException("constraintName cannot be null."); 
/* 491 */     Array<IkConstraint> ikConstraints = this.ikConstraints;
/* 492 */     for (int i = 0, n = ikConstraints.size; i < n; i++) {
/* 493 */       IkConstraint ikConstraint = (IkConstraint)ikConstraints.get(i);
/* 494 */       if (ikConstraint.data.name.equals(constraintName)) return ikConstraint; 
/*     */     } 
/* 496 */     return null;
/*     */   }
/*     */   
/*     */   public Array<TransformConstraint> getTransformConstraints() {
/* 500 */     return this.transformConstraints;
/*     */   }
/*     */ 
/*     */   
/*     */   public TransformConstraint findTransformConstraint(String constraintName) {
/* 505 */     if (constraintName == null) throw new IllegalArgumentException("constraintName cannot be null."); 
/* 506 */     Array<TransformConstraint> transformConstraints = this.transformConstraints;
/* 507 */     for (int i = 0, n = transformConstraints.size; i < n; i++) {
/* 508 */       TransformConstraint constraint = (TransformConstraint)transformConstraints.get(i);
/* 509 */       if (constraint.data.name.equals(constraintName)) return constraint; 
/*     */     } 
/* 511 */     return null;
/*     */   }
/*     */   
/*     */   public Array<PathConstraint> getPathConstraints() {
/* 515 */     return this.pathConstraints;
/*     */   }
/*     */ 
/*     */   
/*     */   public PathConstraint findPathConstraint(String constraintName) {
/* 520 */     if (constraintName == null) throw new IllegalArgumentException("constraintName cannot be null."); 
/* 521 */     Array<PathConstraint> pathConstraints = this.pathConstraints;
/* 522 */     for (int i = 0, n = pathConstraints.size; i < n; i++) {
/* 523 */       PathConstraint constraint = (PathConstraint)pathConstraints.get(i);
/* 524 */       if (constraint.data.name.equals(constraintName)) return constraint; 
/*     */     } 
/* 526 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void getBounds(Vector2 offset, Vector2 size) {
/* 533 */     if (offset == null) throw new IllegalArgumentException("offset cannot be null."); 
/* 534 */     if (size == null) throw new IllegalArgumentException("size cannot be null."); 
/* 535 */     Array<Slot> drawOrder = this.drawOrder;
/* 536 */     float minX = 2.14748365E9F, minY = 2.14748365E9F, maxX = -2.14748365E9F, maxY = -2.14748365E9F;
/* 537 */     for (int i = 0, n = drawOrder.size; i < n; i++) {
/* 538 */       Slot slot = (Slot)drawOrder.get(i);
/* 539 */       float[] vertices = null;
/* 540 */       Attachment attachment = slot.attachment;
/* 541 */       if (attachment instanceof RegionAttachment) {
/* 542 */         vertices = ((RegionAttachment)attachment).updateWorldVertices(slot, false);
/* 543 */       } else if (attachment instanceof MeshAttachment) {
/* 544 */         vertices = ((MeshAttachment)attachment).updateWorldVertices(slot, true);
/* 545 */       }  if (vertices != null) {
/* 546 */         for (int ii = 0, nn = vertices.length; ii < nn; ii += 5) {
/* 547 */           float x = vertices[ii], y = vertices[ii + 1];
/* 548 */           minX = Math.min(minX, x);
/* 549 */           minY = Math.min(minY, y);
/* 550 */           maxX = Math.max(maxX, x);
/* 551 */           maxY = Math.max(maxY, y);
/*     */         } 
/*     */       }
/*     */     } 
/* 555 */     offset.set(minX, minY);
/* 556 */     size.set(maxX - minX, maxY - minY);
/*     */   }
/*     */   
/*     */   public Color getColor() {
/* 560 */     return this.color;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setColor(Color color) {
/* 565 */     if (color == null) throw new IllegalArgumentException("color cannot be null."); 
/* 566 */     this.color.set(color);
/*     */   }
/*     */   
/*     */   public boolean getFlipX() {
/* 570 */     return this.flipX;
/*     */   }
/*     */   
/*     */   public void setFlipX(boolean flipX) {
/* 574 */     this.flipX = flipX;
/*     */   }
/*     */   
/*     */   public boolean getFlipY() {
/* 578 */     return this.flipY;
/*     */   }
/*     */   
/*     */   public void setFlipY(boolean flipY) {
/* 582 */     this.flipY = flipY;
/*     */   }
/*     */   
/*     */   public void setFlip(boolean flipX, boolean flipY) {
/* 586 */     this.flipX = flipX;
/* 587 */     this.flipY = flipY;
/*     */   }
/*     */   
/*     */   public float getX() {
/* 591 */     return this.x;
/*     */   }
/*     */   
/*     */   public void setX(float x) {
/* 595 */     this.x = x;
/*     */   }
/*     */   
/*     */   public float getY() {
/* 599 */     return this.y;
/*     */   }
/*     */   
/*     */   public void setY(float y) {
/* 603 */     this.y = y;
/*     */   }
/*     */   
/*     */   public void setPosition(float x, float y) {
/* 607 */     this.x = x;
/* 608 */     this.y = y;
/*     */   }
/*     */   
/*     */   public float getTime() {
/* 612 */     return this.time;
/*     */   }
/*     */   
/*     */   public void setTime(float time) {
/* 616 */     this.time = time;
/*     */   }
/*     */   
/*     */   public void update(float delta) {
/* 620 */     this.time += delta;
/*     */   }
/*     */   
/*     */   public String toString() {
/* 624 */     return (this.data.name != null) ? this.data.name : super.toString();
/*     */   }
/*     */ }


/* Location:              D:\files\slayTheSpire-export\!\com\esotericsoftware\spine\Skeleton.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */