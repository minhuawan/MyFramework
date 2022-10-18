/*     */ package com.badlogic.gdx.graphics.g3d;
/*     */ 
/*     */ import com.badlogic.gdx.graphics.GLTexture;
/*     */ import com.badlogic.gdx.graphics.Mesh;
/*     */ import com.badlogic.gdx.graphics.Texture;
/*     */ import com.badlogic.gdx.graphics.VertexAttributes;
/*     */ import com.badlogic.gdx.graphics.g3d.attributes.BlendingAttribute;
/*     */ import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
/*     */ import com.badlogic.gdx.graphics.g3d.attributes.FloatAttribute;
/*     */ import com.badlogic.gdx.graphics.g3d.attributes.TextureAttribute;
/*     */ import com.badlogic.gdx.graphics.g3d.model.Animation;
/*     */ import com.badlogic.gdx.graphics.g3d.model.MeshPart;
/*     */ import com.badlogic.gdx.graphics.g3d.model.Node;
/*     */ import com.badlogic.gdx.graphics.g3d.model.NodeAnimation;
/*     */ import com.badlogic.gdx.graphics.g3d.model.NodeKeyframe;
/*     */ import com.badlogic.gdx.graphics.g3d.model.NodePart;
/*     */ import com.badlogic.gdx.graphics.g3d.model.data.ModelAnimation;
/*     */ import com.badlogic.gdx.graphics.g3d.model.data.ModelData;
/*     */ import com.badlogic.gdx.graphics.g3d.model.data.ModelMaterial;
/*     */ import com.badlogic.gdx.graphics.g3d.model.data.ModelMesh;
/*     */ import com.badlogic.gdx.graphics.g3d.model.data.ModelMeshPart;
/*     */ import com.badlogic.gdx.graphics.g3d.model.data.ModelNode;
/*     */ import com.badlogic.gdx.graphics.g3d.model.data.ModelNodeAnimation;
/*     */ import com.badlogic.gdx.graphics.g3d.model.data.ModelNodeKeyframe;
/*     */ import com.badlogic.gdx.graphics.g3d.model.data.ModelNodePart;
/*     */ import com.badlogic.gdx.graphics.g3d.model.data.ModelTexture;
/*     */ import com.badlogic.gdx.graphics.g3d.utils.TextureDescriptor;
/*     */ import com.badlogic.gdx.graphics.g3d.utils.TextureProvider;
/*     */ import com.badlogic.gdx.math.Matrix4;
/*     */ import com.badlogic.gdx.math.Quaternion;
/*     */ import com.badlogic.gdx.math.Vector3;
/*     */ import com.badlogic.gdx.math.collision.BoundingBox;
/*     */ import com.badlogic.gdx.utils.Array;
/*     */ import com.badlogic.gdx.utils.ArrayMap;
/*     */ import com.badlogic.gdx.utils.BufferUtils;
/*     */ import com.badlogic.gdx.utils.Disposable;
/*     */ import com.badlogic.gdx.utils.GdxRuntimeException;
/*     */ import com.badlogic.gdx.utils.ObjectMap;
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
/*     */ public class Model
/*     */   implements Disposable
/*     */ {
/*  73 */   public final Array<Material> materials = new Array();
/*     */   
/*  75 */   public final Array<Node> nodes = new Array();
/*     */   
/*  77 */   public final Array<Animation> animations = new Array();
/*     */   
/*  79 */   public final Array<Mesh> meshes = new Array();
/*     */ 
/*     */   
/*  82 */   public final Array<MeshPart> meshParts = new Array();
/*     */   
/*  84 */   protected final Array<Disposable> disposables = new Array();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private ObjectMap<NodePart, ArrayMap<String, Matrix4>> nodePartBones;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Model(ModelData modelData) {
/*  95 */     this(modelData, (TextureProvider)new TextureProvider.FileTextureProvider());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void load(ModelData modelData, TextureProvider textureProvider) {
/* 106 */     loadMeshes((Iterable<ModelMesh>)modelData.meshes);
/* 107 */     loadMaterials((Iterable<ModelMaterial>)modelData.materials, textureProvider);
/* 108 */     loadNodes((Iterable<ModelNode>)modelData.nodes);
/* 109 */     loadAnimations((Iterable<ModelAnimation>)modelData.animations);
/* 110 */     calculateTransforms();
/*     */   }
/*     */   
/*     */   protected void loadAnimations(Iterable<ModelAnimation> modelAnimations) {
/* 114 */     for (ModelAnimation anim : modelAnimations) {
/* 115 */       Animation animation = new Animation();
/* 116 */       animation.id = anim.id;
/* 117 */       for (ModelNodeAnimation nanim : anim.nodeAnimations) {
/* 118 */         Node node = getNode(nanim.nodeId);
/* 119 */         if (node == null)
/* 120 */           continue;  NodeAnimation nodeAnim = new NodeAnimation();
/* 121 */         nodeAnim.node = node;
/*     */         
/* 123 */         if (nanim.translation != null) {
/* 124 */           nodeAnim.translation = new Array();
/* 125 */           nodeAnim.translation.ensureCapacity(nanim.translation.size);
/* 126 */           for (ModelNodeKeyframe<Vector3> kf : (Iterable<ModelNodeKeyframe<Vector3>>)nanim.translation) {
/* 127 */             if (kf.keytime > animation.duration) animation.duration = kf.keytime; 
/* 128 */             nodeAnim.translation.add(new NodeKeyframe(kf.keytime, new Vector3((kf.value == null) ? node.translation : (Vector3)kf.value)));
/*     */           } 
/*     */         } 
/*     */ 
/*     */         
/* 133 */         if (nanim.rotation != null) {
/* 134 */           nodeAnim.rotation = new Array();
/* 135 */           nodeAnim.rotation.ensureCapacity(nanim.rotation.size);
/* 136 */           for (ModelNodeKeyframe<Quaternion> kf : (Iterable<ModelNodeKeyframe<Quaternion>>)nanim.rotation) {
/* 137 */             if (kf.keytime > animation.duration) animation.duration = kf.keytime; 
/* 138 */             nodeAnim.rotation.add(new NodeKeyframe(kf.keytime, new Quaternion((kf.value == null) ? node.rotation : (Quaternion)kf.value)));
/*     */           } 
/*     */         } 
/*     */ 
/*     */         
/* 143 */         if (nanim.scaling != null) {
/* 144 */           nodeAnim.scaling = new Array();
/* 145 */           nodeAnim.scaling.ensureCapacity(nanim.scaling.size);
/* 146 */           for (ModelNodeKeyframe<Vector3> kf : (Iterable<ModelNodeKeyframe<Vector3>>)nanim.scaling) {
/* 147 */             if (kf.keytime > animation.duration) animation.duration = kf.keytime; 
/* 148 */             nodeAnim.scaling.add(new NodeKeyframe(kf.keytime, new Vector3((kf.value == null) ? node.scale : (Vector3)kf.value)));
/*     */           } 
/*     */         } 
/*     */ 
/*     */         
/* 153 */         if ((nodeAnim.translation != null && nodeAnim.translation.size > 0) || (nodeAnim.rotation != null && nodeAnim.rotation.size > 0) || (nodeAnim.scaling != null && nodeAnim.scaling.size > 0))
/*     */         {
/* 155 */           animation.nodeAnimations.add(nodeAnim); } 
/*     */       } 
/* 157 */       if (animation.nodeAnimations.size > 0) this.animations.add(animation); 
/*     */     } 
/*     */   }
/*     */   
/* 161 */   public Model() { this.nodePartBones = new ObjectMap(); } public Model(ModelData modelData, TextureProvider textureProvider) { this.nodePartBones = new ObjectMap();
/*     */     load(modelData, textureProvider); }
/*     */    protected void loadNodes(Iterable<ModelNode> modelNodes) {
/* 164 */     this.nodePartBones.clear();
/* 165 */     for (ModelNode node : modelNodes) {
/* 166 */       this.nodes.add(loadNode(node));
/*     */     }
/* 168 */     for (ObjectMap.Entries<ObjectMap.Entry<NodePart, ArrayMap<String, Matrix4>>> entries = this.nodePartBones.entries().iterator(); entries.hasNext(); ) { ObjectMap.Entry<NodePart, ArrayMap<String, Matrix4>> e = entries.next();
/* 169 */       if (((NodePart)e.key).invBoneBindTransforms == null)
/* 170 */         ((NodePart)e.key).invBoneBindTransforms = new ArrayMap(Node.class, Matrix4.class); 
/* 171 */       ((NodePart)e.key).invBoneBindTransforms.clear();
/* 172 */       for (ObjectMap.Entry<String, Matrix4> b : (Iterable<ObjectMap.Entry<String, Matrix4>>)((ArrayMap)e.value).entries())
/* 173 */         ((NodePart)e.key).invBoneBindTransforms.put(getNode((String)b.key), (new Matrix4((Matrix4)b.value)).inv());  }
/*     */   
/*     */   }
/*     */   
/*     */   protected Node loadNode(ModelNode modelNode) {
/* 178 */     Node node = new Node();
/* 179 */     node.id = modelNode.id;
/*     */     
/* 181 */     if (modelNode.translation != null) node.translation.set(modelNode.translation); 
/* 182 */     if (modelNode.rotation != null) node.rotation.set(modelNode.rotation); 
/* 183 */     if (modelNode.scale != null) node.scale.set(modelNode.scale);
/*     */     
/* 185 */     if (modelNode.parts != null) {
/* 186 */       for (ModelNodePart modelNodePart : modelNode.parts) {
/* 187 */         MeshPart meshPart = null;
/* 188 */         Material meshMaterial = null;
/*     */         
/* 190 */         if (modelNodePart.meshPartId != null) {
/* 191 */           for (MeshPart part : this.meshParts) {
/* 192 */             if (modelNodePart.meshPartId.equals(part.id)) {
/* 193 */               meshPart = part;
/*     */               
/*     */               break;
/*     */             } 
/*     */           } 
/*     */         }
/* 199 */         if (modelNodePart.materialId != null) {
/* 200 */           for (Material material : this.materials) {
/* 201 */             if (modelNodePart.materialId.equals(material.id)) {
/* 202 */               meshMaterial = material;
/*     */               
/*     */               break;
/*     */             } 
/*     */           } 
/*     */         }
/* 208 */         if (meshPart == null || meshMaterial == null) throw new GdxRuntimeException("Invalid node: " + node.id);
/*     */         
/* 210 */         if (meshPart != null && meshMaterial != null) {
/* 211 */           NodePart nodePart = new NodePart();
/* 212 */           nodePart.meshPart = meshPart;
/* 213 */           nodePart.material = meshMaterial;
/* 214 */           node.parts.add(nodePart);
/* 215 */           if (modelNodePart.bones != null) this.nodePartBones.put(nodePart, modelNodePart.bones);
/*     */         
/*     */         } 
/*     */       } 
/*     */     }
/* 220 */     if (modelNode.children != null) {
/* 221 */       for (ModelNode child : modelNode.children) {
/* 222 */         node.addChild(loadNode(child));
/*     */       }
/*     */     }
/*     */     
/* 226 */     return node;
/*     */   }
/*     */   
/*     */   protected void loadMeshes(Iterable<ModelMesh> meshes) {
/* 230 */     for (ModelMesh mesh : meshes) {
/* 231 */       convertMesh(mesh);
/*     */     }
/*     */   }
/*     */   
/*     */   protected void convertMesh(ModelMesh modelMesh) {
/* 236 */     int numIndices = 0;
/* 237 */     for (ModelMeshPart part : modelMesh.parts) {
/* 238 */       numIndices += part.indices.length;
/*     */     }
/* 240 */     VertexAttributes attributes = new VertexAttributes(modelMesh.attributes);
/* 241 */     int numVertices = modelMesh.vertices.length / attributes.vertexSize / 4;
/*     */     
/* 243 */     Mesh mesh = new Mesh(true, numVertices, numIndices, attributes);
/* 244 */     this.meshes.add(mesh);
/* 245 */     this.disposables.add(mesh);
/*     */     
/* 247 */     BufferUtils.copy(modelMesh.vertices, mesh.getVerticesBuffer(), modelMesh.vertices.length, 0);
/* 248 */     int offset = 0;
/* 249 */     mesh.getIndicesBuffer().clear();
/* 250 */     for (ModelMeshPart part : modelMesh.parts) {
/* 251 */       MeshPart meshPart = new MeshPart();
/* 252 */       meshPart.id = part.id;
/* 253 */       meshPart.primitiveType = part.primitiveType;
/* 254 */       meshPart.offset = offset;
/* 255 */       meshPart.size = part.indices.length;
/* 256 */       meshPart.mesh = mesh;
/* 257 */       mesh.getIndicesBuffer().put(part.indices);
/* 258 */       offset += meshPart.size;
/* 259 */       this.meshParts.add(meshPart);
/*     */     } 
/* 261 */     mesh.getIndicesBuffer().position(0);
/* 262 */     for (MeshPart part : this.meshParts)
/* 263 */       part.update(); 
/*     */   }
/*     */   
/*     */   protected void loadMaterials(Iterable<ModelMaterial> modelMaterials, TextureProvider textureProvider) {
/* 267 */     for (ModelMaterial mtl : modelMaterials) {
/* 268 */       this.materials.add(convertMaterial(mtl, textureProvider));
/*     */     }
/*     */   }
/*     */   
/*     */   protected Material convertMaterial(ModelMaterial mtl, TextureProvider textureProvider) {
/* 273 */     Material result = new Material();
/* 274 */     result.id = mtl.id;
/* 275 */     if (mtl.ambient != null) result.set((Attribute)new ColorAttribute(ColorAttribute.Ambient, mtl.ambient)); 
/* 276 */     if (mtl.diffuse != null) result.set((Attribute)new ColorAttribute(ColorAttribute.Diffuse, mtl.diffuse)); 
/* 277 */     if (mtl.specular != null) result.set((Attribute)new ColorAttribute(ColorAttribute.Specular, mtl.specular)); 
/* 278 */     if (mtl.emissive != null) result.set((Attribute)new ColorAttribute(ColorAttribute.Emissive, mtl.emissive)); 
/* 279 */     if (mtl.reflection != null) result.set((Attribute)new ColorAttribute(ColorAttribute.Reflection, mtl.reflection)); 
/* 280 */     if (mtl.shininess > 0.0F) result.set((Attribute)new FloatAttribute(FloatAttribute.Shininess, mtl.shininess)); 
/* 281 */     if (mtl.opacity != 1.0F) result.set((Attribute)new BlendingAttribute(770, 771, mtl.opacity));
/*     */     
/* 283 */     ObjectMap<String, Texture> textures = new ObjectMap();
/*     */ 
/*     */     
/* 286 */     if (mtl.textures != null) {
/* 287 */       for (ModelTexture tex : mtl.textures) {
/*     */         Texture texture;
/* 289 */         if (textures.containsKey(tex.fileName)) {
/* 290 */           texture = (Texture)textures.get(tex.fileName);
/*     */         } else {
/* 292 */           texture = textureProvider.load(tex.fileName);
/* 293 */           textures.put(tex.fileName, texture);
/* 294 */           this.disposables.add(texture);
/*     */         } 
/*     */         
/* 297 */         TextureDescriptor descriptor = new TextureDescriptor((GLTexture)texture);
/* 298 */         descriptor.minFilter = texture.getMinFilter();
/* 299 */         descriptor.magFilter = texture.getMagFilter();
/* 300 */         descriptor.uWrap = texture.getUWrap();
/* 301 */         descriptor.vWrap = texture.getVWrap();
/*     */         
/* 303 */         float offsetU = (tex.uvTranslation == null) ? 0.0F : tex.uvTranslation.x;
/* 304 */         float offsetV = (tex.uvTranslation == null) ? 0.0F : tex.uvTranslation.y;
/* 305 */         float scaleU = (tex.uvScaling == null) ? 1.0F : tex.uvScaling.x;
/* 306 */         float scaleV = (tex.uvScaling == null) ? 1.0F : tex.uvScaling.y;
/*     */         
/* 308 */         switch (tex.usage) {
/*     */           case 2:
/* 310 */             result.set((Attribute)new TextureAttribute(TextureAttribute.Diffuse, descriptor, offsetU, offsetV, scaleU, scaleV));
/*     */           
/*     */           case 5:
/* 313 */             result.set((Attribute)new TextureAttribute(TextureAttribute.Specular, descriptor, offsetU, offsetV, scaleU, scaleV));
/*     */           
/*     */           case 8:
/* 316 */             result.set((Attribute)new TextureAttribute(TextureAttribute.Bump, descriptor, offsetU, offsetV, scaleU, scaleV));
/*     */           
/*     */           case 7:
/* 319 */             result.set((Attribute)new TextureAttribute(TextureAttribute.Normal, descriptor, offsetU, offsetV, scaleU, scaleV));
/*     */           
/*     */           case 4:
/* 322 */             result.set((Attribute)new TextureAttribute(TextureAttribute.Ambient, descriptor, offsetU, offsetV, scaleU, scaleV));
/*     */           
/*     */           case 3:
/* 325 */             result.set((Attribute)new TextureAttribute(TextureAttribute.Emissive, descriptor, offsetU, offsetV, scaleU, scaleV));
/*     */           
/*     */           case 10:
/* 328 */             result.set((Attribute)new TextureAttribute(TextureAttribute.Reflection, descriptor, offsetU, offsetV, scaleU, scaleV));
/*     */         } 
/*     */ 
/*     */       
/*     */       } 
/*     */     }
/* 334 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void manageDisposable(Disposable disposable) {
/* 341 */     if (!this.disposables.contains(disposable, true)) this.disposables.add(disposable);
/*     */   
/*     */   }
/*     */   
/*     */   public Iterable<Disposable> getManagedDisposables() {
/* 346 */     return (Iterable<Disposable>)this.disposables;
/*     */   }
/*     */ 
/*     */   
/*     */   public void dispose() {
/* 351 */     for (Disposable disposable : this.disposables) {
/* 352 */       disposable.dispose();
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
/* 364 */     int n = this.nodes.size; int i;
/* 365 */     for (i = 0; i < n; i++) {
/* 366 */       ((Node)this.nodes.get(i)).calculateTransforms(true);
/*     */     }
/* 368 */     for (i = 0; i < n; i++) {
/* 369 */       ((Node)this.nodes.get(i)).calculateBoneTransforms(true);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public BoundingBox calculateBoundingBox(BoundingBox out) {
/* 377 */     out.inf();
/* 378 */     return extendBoundingBox(out);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public BoundingBox extendBoundingBox(BoundingBox out) {
/* 386 */     int n = this.nodes.size;
/* 387 */     for (int i = 0; i < n; i++)
/* 388 */       ((Node)this.nodes.get(i)).extendBoundingBox(out); 
/* 389 */     return out;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Animation getAnimation(String id) {
/* 395 */     return getAnimation(id, true);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Animation getAnimation(String id, boolean ignoreCase) {
/* 402 */     int n = this.animations.size;
/*     */     
/* 404 */     if (ignoreCase)
/* 405 */     { for (int i = 0; i < n; i++) {
/* 406 */         Animation animation; if ((animation = (Animation)this.animations.get(i)).id.equalsIgnoreCase(id)) return animation; 
/*     */       }  }
/* 408 */     else { for (int i = 0; i < n; i++) {
/* 409 */         Animation animation; if ((animation = (Animation)this.animations.get(i)).id.equals(id)) return animation; 
/*     */       }  }
/* 411 */      return null;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Material getMaterial(String id) {
/* 417 */     return getMaterial(id, true);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Material getMaterial(String id, boolean ignoreCase) {
/* 424 */     int n = this.materials.size;
/*     */     
/* 426 */     if (ignoreCase)
/* 427 */     { for (int i = 0; i < n; i++) {
/* 428 */         Material material; if ((material = (Material)this.materials.get(i)).id.equalsIgnoreCase(id)) return material; 
/*     */       }  }
/* 430 */     else { for (int i = 0; i < n; i++) {
/* 431 */         Material material; if ((material = (Material)this.materials.get(i)).id.equals(id)) return material; 
/*     */       }  }
/* 433 */      return null;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Node getNode(String id) {
/* 439 */     return getNode(id, true);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Node getNode(String id, boolean recursive) {
/* 446 */     return getNode(id, recursive, false);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Node getNode(String id, boolean recursive, boolean ignoreCase) {
/* 454 */     return Node.getNode(this.nodes, id, recursive, ignoreCase);
/*     */   }
/*     */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\graphics\g3d\Model.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */