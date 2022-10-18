/*      */ package com.badlogic.gdx.graphics.g3d.utils;
/*      */ 
/*      */ import com.badlogic.gdx.graphics.Color;
/*      */ import com.badlogic.gdx.graphics.Mesh;
/*      */ import com.badlogic.gdx.graphics.VertexAttribute;
/*      */ import com.badlogic.gdx.graphics.VertexAttributes;
/*      */ import com.badlogic.gdx.graphics.g2d.TextureRegion;
/*      */ import com.badlogic.gdx.graphics.g3d.model.MeshPart;
/*      */ import com.badlogic.gdx.graphics.g3d.utils.shapebuilders.ArrowShapeBuilder;
/*      */ import com.badlogic.gdx.graphics.g3d.utils.shapebuilders.BoxShapeBuilder;
/*      */ import com.badlogic.gdx.graphics.g3d.utils.shapebuilders.CapsuleShapeBuilder;
/*      */ import com.badlogic.gdx.graphics.g3d.utils.shapebuilders.ConeShapeBuilder;
/*      */ import com.badlogic.gdx.graphics.g3d.utils.shapebuilders.CylinderShapeBuilder;
/*      */ import com.badlogic.gdx.graphics.g3d.utils.shapebuilders.EllipseShapeBuilder;
/*      */ import com.badlogic.gdx.graphics.g3d.utils.shapebuilders.PatchShapeBuilder;
/*      */ import com.badlogic.gdx.graphics.g3d.utils.shapebuilders.SphereShapeBuilder;
/*      */ import com.badlogic.gdx.math.MathUtils;
/*      */ import com.badlogic.gdx.math.Matrix3;
/*      */ import com.badlogic.gdx.math.Matrix4;
/*      */ import com.badlogic.gdx.math.Vector2;
/*      */ import com.badlogic.gdx.math.Vector3;
/*      */ import com.badlogic.gdx.math.collision.BoundingBox;
/*      */ import com.badlogic.gdx.utils.Array;
/*      */ import com.badlogic.gdx.utils.FloatArray;
/*      */ import com.badlogic.gdx.utils.GdxRuntimeException;
/*      */ import com.badlogic.gdx.utils.IntIntMap;
/*      */ import com.badlogic.gdx.utils.NumberUtils;
/*      */ import com.badlogic.gdx.utils.ShortArray;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class MeshBuilder
/*      */   implements MeshPartBuilder
/*      */ {
/*   56 */   private static final ShortArray tmpIndices = new ShortArray();
/*   57 */   private static final FloatArray tmpVertices = new FloatArray();
/*      */   
/*   59 */   private final MeshPartBuilder.VertexInfo vertTmp1 = new MeshPartBuilder.VertexInfo();
/*   60 */   private final MeshPartBuilder.VertexInfo vertTmp2 = new MeshPartBuilder.VertexInfo();
/*   61 */   private final MeshPartBuilder.VertexInfo vertTmp3 = new MeshPartBuilder.VertexInfo();
/*   62 */   private final MeshPartBuilder.VertexInfo vertTmp4 = new MeshPartBuilder.VertexInfo();
/*      */   
/*   64 */   private final Color tempC1 = new Color();
/*      */ 
/*      */   
/*      */   private VertexAttributes attributes;
/*      */   
/*   69 */   private FloatArray vertices = new FloatArray();
/*      */   
/*   71 */   private ShortArray indices = new ShortArray();
/*      */   
/*      */   private int stride;
/*      */   
/*      */   private short vindex;
/*      */   
/*      */   private int istart;
/*      */   
/*      */   private int posOffset;
/*      */   
/*      */   private int posSize;
/*      */   
/*      */   private int norOffset;
/*      */   
/*      */   private int biNorOffset;
/*      */   
/*      */   private int tangentOffset;
/*      */   
/*      */   private int colOffset;
/*      */   
/*      */   private int colSize;
/*      */   
/*      */   private int cpOffset;
/*      */   
/*      */   private int uvOffset;
/*      */   
/*      */   private MeshPart part;
/*      */   
/*   99 */   private Array<MeshPart> parts = new Array();
/*      */   
/*  101 */   private final Color color = new Color(Color.WHITE);
/*      */   
/*      */   private boolean hasColor = false;
/*      */   
/*      */   private int primitiveType;
/*  106 */   private float uOffset = 0.0F; private float uScale = 1.0F; private float vOffset = 0.0F; private float vScale = 1.0F;
/*      */   
/*      */   private boolean hasUVTransform = false;
/*      */   private float[] vertex;
/*      */   private boolean vertexTransformationEnabled = false;
/*  111 */   private final Matrix4 positionTransform = new Matrix4();
/*  112 */   private final Matrix3 normalTransform = new Matrix3();
/*  113 */   private final BoundingBox bounds = new BoundingBox();
/*      */ 
/*      */ 
/*      */   
/*      */   public static VertexAttributes createAttributes(long usage) {
/*  118 */     Array<VertexAttribute> attrs = new Array();
/*  119 */     if ((usage & 0x1L) == 1L)
/*  120 */       attrs.add(new VertexAttribute(1, 3, "a_position")); 
/*  121 */     if ((usage & 0x2L) == 2L)
/*  122 */       attrs.add(new VertexAttribute(2, 4, "a_color")); 
/*  123 */     if ((usage & 0x4L) == 4L)
/*  124 */       attrs.add(new VertexAttribute(4, 4, "a_color")); 
/*  125 */     if ((usage & 0x8L) == 8L)
/*  126 */       attrs.add(new VertexAttribute(8, 3, "a_normal")); 
/*  127 */     if ((usage & 0x10L) == 16L)
/*  128 */       attrs.add(new VertexAttribute(16, 2, "a_texCoord0")); 
/*  129 */     VertexAttribute[] attributes = new VertexAttribute[attrs.size];
/*  130 */     for (int i = 0; i < attributes.length; i++)
/*  131 */       attributes[i] = (VertexAttribute)attrs.get(i); 
/*  132 */     return new VertexAttributes(attributes);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void begin(long attributes) {
/*  139 */     begin(createAttributes(attributes), -1);
/*      */   }
/*      */ 
/*      */   
/*      */   public void begin(VertexAttributes attributes) {
/*  144 */     begin(attributes, -1);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void begin(long attributes, int primitiveType) {
/*  151 */     begin(createAttributes(attributes), primitiveType);
/*      */   }
/*      */ 
/*      */   
/*      */   public void begin(VertexAttributes attributes, int primitiveType) {
/*  156 */     if (this.attributes != null) throw new RuntimeException("Call end() first"); 
/*  157 */     this.attributes = attributes;
/*  158 */     this.vertices.clear();
/*  159 */     this.indices.clear();
/*  160 */     this.parts.clear();
/*  161 */     this.vindex = 0;
/*  162 */     this.lastIndex = -1;
/*  163 */     this.istart = 0;
/*  164 */     this.part = null;
/*  165 */     this.stride = attributes.vertexSize / 4;
/*  166 */     if (this.vertex == null || this.vertex.length < this.stride) this.vertex = new float[this.stride]; 
/*  167 */     VertexAttribute a = attributes.findByUsage(1);
/*  168 */     if (a == null) throw new GdxRuntimeException("Cannot build mesh without position attribute"); 
/*  169 */     this.posOffset = a.offset / 4;
/*  170 */     this.posSize = a.numComponents;
/*  171 */     a = attributes.findByUsage(8);
/*  172 */     this.norOffset = (a == null) ? -1 : (a.offset / 4);
/*  173 */     a = attributes.findByUsage(256);
/*  174 */     this.biNorOffset = (a == null) ? -1 : (a.offset / 4);
/*  175 */     a = attributes.findByUsage(128);
/*  176 */     this.tangentOffset = (a == null) ? -1 : (a.offset / 4);
/*  177 */     a = attributes.findByUsage(2);
/*  178 */     this.colOffset = (a == null) ? -1 : (a.offset / 4);
/*  179 */     this.colSize = (a == null) ? 0 : a.numComponents;
/*  180 */     a = attributes.findByUsage(4);
/*  181 */     this.cpOffset = (a == null) ? -1 : (a.offset / 4);
/*  182 */     a = attributes.findByUsage(16);
/*  183 */     this.uvOffset = (a == null) ? -1 : (a.offset / 4);
/*  184 */     setColor(null);
/*  185 */     setVertexTransform(null);
/*  186 */     setUVRange(null);
/*  187 */     this.primitiveType = primitiveType;
/*  188 */     this.bounds.inf();
/*      */   }
/*      */   
/*      */   private void endpart() {
/*  192 */     if (this.part != null) {
/*  193 */       this.bounds.getCenter(this.part.center);
/*  194 */       this.bounds.getDimensions(this.part.halfExtents).scl(0.5F);
/*  195 */       this.part.radius = this.part.halfExtents.len();
/*  196 */       this.bounds.inf();
/*  197 */       this.part.offset = this.istart;
/*  198 */       this.part.size = this.indices.size - this.istart;
/*  199 */       this.istart = this.indices.size;
/*  200 */       this.part = null;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public MeshPart part(String id, int primitiveType) {
/*  208 */     return part(id, primitiveType, new MeshPart());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public MeshPart part(String id, int primitiveType, MeshPart meshPart) {
/*  217 */     if (this.attributes == null) throw new RuntimeException("Call begin() first"); 
/*  218 */     endpart();
/*      */     
/*  220 */     this.part = meshPart;
/*  221 */     this.part.id = id;
/*  222 */     this.primitiveType = this.part.primitiveType = primitiveType;
/*  223 */     this.parts.add(this.part);
/*      */     
/*  225 */     setColor(null);
/*  226 */     setVertexTransform(null);
/*  227 */     setUVRange(null);
/*      */     
/*  229 */     return this.part;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Mesh end(Mesh mesh) {
/*  236 */     endpart();
/*      */     
/*  238 */     if (this.attributes == null) throw new GdxRuntimeException("Call begin() first"); 
/*  239 */     if (!this.attributes.equals(mesh.getVertexAttributes())) throw new GdxRuntimeException("Mesh attributes don't match"); 
/*  240 */     if (mesh.getMaxVertices() * this.stride < this.vertices.size) {
/*  241 */       throw new GdxRuntimeException("Mesh can't hold enough vertices: " + mesh.getMaxVertices() + " * " + this.stride + " < " + this.vertices.size);
/*      */     }
/*  243 */     if (mesh.getMaxIndices() < this.indices.size) {
/*  244 */       throw new GdxRuntimeException("Mesh can't hold enough indices: " + mesh.getMaxIndices() + " < " + this.indices.size);
/*      */     }
/*  246 */     mesh.setVertices(this.vertices.items, 0, this.vertices.size);
/*  247 */     mesh.setIndices(this.indices.items, 0, this.indices.size);
/*      */     
/*  249 */     for (MeshPart p : this.parts)
/*  250 */       p.mesh = mesh; 
/*  251 */     this.parts.clear();
/*      */     
/*  253 */     this.attributes = null;
/*  254 */     this.vertices.clear();
/*  255 */     this.indices.clear();
/*      */     
/*  257 */     return mesh;
/*      */   }
/*      */ 
/*      */   
/*      */   public Mesh end() {
/*  262 */     return end(new Mesh(true, this.vertices.size / this.stride, this.indices.size, this.attributes));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void clear() {
/*  269 */     this.vertices.clear();
/*  270 */     this.indices.clear();
/*  271 */     this.parts.clear();
/*  272 */     this.vindex = 0;
/*  273 */     this.lastIndex = -1;
/*  274 */     this.istart = 0;
/*  275 */     this.part = null;
/*      */   }
/*      */ 
/*      */   
/*      */   public int getFloatsPerVertex() {
/*  280 */     return this.stride;
/*      */   }
/*      */ 
/*      */   
/*      */   public int getNumVertices() {
/*  285 */     return this.vertices.size / this.stride;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void getVertices(float[] out, int destOffset) {
/*  293 */     if (this.attributes == null) throw new GdxRuntimeException("Must be called in between #begin and #end"); 
/*  294 */     if (destOffset < 0 || destOffset > out.length - this.vertices.size)
/*  295 */       throw new GdxRuntimeException("Array to small or offset out of range"); 
/*  296 */     System.arraycopy(this.vertices.items, 0, out, destOffset, this.vertices.size);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected float[] getVertices() {
/*  303 */     return this.vertices.items;
/*      */   }
/*      */ 
/*      */   
/*      */   public int getNumIndices() {
/*  308 */     return this.indices.size;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void getIndices(short[] out, int destOffset) {
/*  316 */     if (this.attributes == null) throw new GdxRuntimeException("Must be called in between #begin and #end"); 
/*  317 */     if (destOffset < 0 || destOffset > out.length - this.indices.size)
/*  318 */       throw new GdxRuntimeException("Array to small or offset out of range"); 
/*  319 */     System.arraycopy(this.indices.items, 0, out, destOffset, this.indices.size);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected short[] getIndices() {
/*  326 */     return this.indices.items;
/*      */   }
/*      */ 
/*      */   
/*      */   public VertexAttributes getAttributes() {
/*  331 */     return this.attributes;
/*      */   }
/*      */ 
/*      */   
/*      */   public MeshPart getMeshPart() {
/*  336 */     return this.part;
/*      */   }
/*      */ 
/*      */   
/*      */   public int getPrimitiveType() {
/*  341 */     return this.primitiveType;
/*      */   }
/*      */ 
/*      */   
/*      */   public void setColor(float r, float g, float b, float a) {
/*  346 */     this.color.set(r, g, b, a);
/*  347 */     this.hasColor = !this.color.equals(Color.WHITE);
/*      */   }
/*      */ 
/*      */   
/*      */   public void setColor(Color color) {
/*  352 */     this.color.set(!(this.hasColor = (color != null)) ? Color.WHITE : color);
/*      */   }
/*      */ 
/*      */   
/*      */   public void setUVRange(float u1, float v1, float u2, float v2) {
/*  357 */     this.uOffset = u1;
/*  358 */     this.vOffset = v1;
/*  359 */     this.uScale = u2 - u1;
/*  360 */     this.vScale = v2 - v1;
/*  361 */     this.hasUVTransform = (!MathUtils.isZero(u1) || !MathUtils.isZero(v1) || !MathUtils.isEqual(u2, 1.0F) || !MathUtils.isEqual(v2, 1.0F));
/*      */   }
/*      */ 
/*      */   
/*      */   public void setUVRange(TextureRegion region) {
/*  366 */     if (!(this.hasUVTransform = (region != null))) {
/*  367 */       this.uOffset = this.vOffset = 0.0F;
/*  368 */       this.uScale = this.vScale = 1.0F;
/*      */     } else {
/*  370 */       setUVRange(region.getU(), region.getV(), region.getU2(), region.getV2());
/*      */     } 
/*      */   }
/*      */   
/*      */   public Matrix4 getVertexTransform(Matrix4 out) {
/*  375 */     return out.set(this.positionTransform);
/*      */   }
/*      */ 
/*      */   
/*      */   public void setVertexTransform(Matrix4 transform) {
/*  380 */     if ((this.vertexTransformationEnabled = (transform != null)) == true) {
/*  381 */       this.positionTransform.set(transform);
/*  382 */       this.normalTransform.set(transform).inv().transpose();
/*      */     } else {
/*  384 */       this.positionTransform.idt();
/*  385 */       this.normalTransform.idt();
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isVertexTransformationEnabled() {
/*  391 */     return this.vertexTransformationEnabled;
/*      */   }
/*      */ 
/*      */   
/*      */   public void setVertexTransformationEnabled(boolean enabled) {
/*  396 */     this.vertexTransformationEnabled = enabled;
/*      */   }
/*      */ 
/*      */   
/*      */   public void ensureVertices(int numVertices) {
/*  401 */     this.vertices.ensureCapacity(this.stride * numVertices);
/*      */   }
/*      */ 
/*      */   
/*      */   public void ensureIndices(int numIndices) {
/*  406 */     this.indices.ensureCapacity(numIndices);
/*      */   }
/*      */ 
/*      */   
/*      */   public void ensureCapacity(int numVertices, int numIndices) {
/*  411 */     ensureVertices(numVertices);
/*  412 */     ensureIndices(numIndices);
/*      */   }
/*      */ 
/*      */   
/*      */   public void ensureTriangleIndices(int numTriangles) {
/*  417 */     if (this.primitiveType == 1) {
/*  418 */       ensureIndices(6 * numTriangles);
/*  419 */     } else if (this.primitiveType == 4 || this.primitiveType == 0) {
/*  420 */       ensureIndices(3 * numTriangles);
/*      */     } else {
/*  422 */       throw new GdxRuntimeException("Incorrect primtive type");
/*      */     } 
/*      */   }
/*      */   
/*      */   @Deprecated
/*      */   public void ensureTriangles(int numVertices, int numTriangles) {
/*  428 */     ensureVertices(numVertices);
/*  429 */     ensureTriangleIndices(numTriangles);
/*      */   }
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public void ensureTriangles(int numTriangles) {
/*  435 */     ensureVertices(3 * numTriangles);
/*  436 */     ensureTriangleIndices(numTriangles);
/*      */   }
/*      */ 
/*      */   
/*      */   public void ensureRectangleIndices(int numRectangles) {
/*  441 */     if (this.primitiveType == 0) {
/*  442 */       ensureIndices(4 * numRectangles);
/*  443 */     } else if (this.primitiveType == 1) {
/*  444 */       ensureIndices(8 * numRectangles);
/*      */     } else {
/*      */       
/*  447 */       ensureIndices(6 * numRectangles);
/*      */     } 
/*      */   }
/*      */   
/*      */   @Deprecated
/*      */   public void ensureRectangles(int numVertices, int numRectangles) {
/*  453 */     ensureVertices(numVertices);
/*  454 */     ensureRectangleIndices(numRectangles);
/*      */   }
/*      */ 
/*      */   
/*      */   public void ensureRectangles(int numRectangles) {
/*  459 */     ensureVertices(4 * numRectangles);
/*  460 */     ensureRectangleIndices(numRectangles);
/*      */   }
/*      */   
/*  463 */   private short lastIndex = -1;
/*      */ 
/*      */   
/*      */   public short lastIndex() {
/*  467 */     return this.lastIndex;
/*      */   }
/*      */   
/*  470 */   private static final Vector3 vTmp = new Vector3();
/*      */   
/*      */   private static final void transformPosition(float[] values, int offset, int size, Matrix4 transform) {
/*  473 */     if (size > 2) {
/*  474 */       vTmp.set(values[offset], values[offset + 1], values[offset + 2]).mul(transform);
/*  475 */       values[offset] = vTmp.x;
/*  476 */       values[offset + 1] = vTmp.y;
/*  477 */       values[offset + 2] = vTmp.z;
/*  478 */     } else if (size > 1) {
/*  479 */       vTmp.set(values[offset], values[offset + 1], 0.0F).mul(transform);
/*  480 */       values[offset] = vTmp.x;
/*  481 */       values[offset + 1] = vTmp.y;
/*      */     } else {
/*  483 */       values[offset] = (vTmp.set(values[offset], 0.0F, 0.0F).mul(transform)).x;
/*      */     } 
/*      */   }
/*      */   private static final void transformNormal(float[] values, int offset, int size, Matrix3 transform) {
/*  487 */     if (size > 2) {
/*  488 */       vTmp.set(values[offset], values[offset + 1], values[offset + 2]).mul(transform).nor();
/*  489 */       values[offset] = vTmp.x;
/*  490 */       values[offset + 1] = vTmp.y;
/*  491 */       values[offset + 2] = vTmp.z;
/*  492 */     } else if (size > 1) {
/*  493 */       vTmp.set(values[offset], values[offset + 1], 0.0F).mul(transform).nor();
/*  494 */       values[offset] = vTmp.x;
/*  495 */       values[offset + 1] = vTmp.y;
/*      */     } else {
/*  497 */       values[offset] = (vTmp.set(values[offset], 0.0F, 0.0F).mul(transform).nor()).x;
/*      */     } 
/*      */   }
/*      */   private final void addVertex(float[] values, int offset) {
/*  501 */     int o = this.vertices.size;
/*  502 */     this.vertices.addAll(values, offset, this.stride);
/*  503 */     this.lastIndex = this.vindex = (short)(this.vindex + 1);
/*      */     
/*  505 */     if (this.vertexTransformationEnabled) {
/*  506 */       transformPosition(this.vertices.items, o + this.posOffset, this.posSize, this.positionTransform);
/*  507 */       if (this.norOffset >= 0) transformNormal(this.vertices.items, o + this.norOffset, 3, this.normalTransform); 
/*  508 */       if (this.biNorOffset >= 0) transformNormal(this.vertices.items, o + this.biNorOffset, 3, this.normalTransform); 
/*  509 */       if (this.tangentOffset >= 0) transformNormal(this.vertices.items, o + this.tangentOffset, 3, this.normalTransform);
/*      */     
/*      */     } 
/*  512 */     float x = this.vertices.items[o + this.posOffset];
/*  513 */     float y = (this.posSize > 1) ? this.vertices.items[o + this.posOffset + 1] : 0.0F;
/*  514 */     float z = (this.posSize > 2) ? this.vertices.items[o + this.posOffset + 2] : 0.0F;
/*  515 */     this.bounds.ext(x, y, z);
/*      */     
/*  517 */     if (this.hasColor) {
/*  518 */       if (this.colOffset >= 0) {
/*  519 */         this.vertices.items[o + this.colOffset] = this.vertices.items[o + this.colOffset] * this.color.r;
/*  520 */         this.vertices.items[o + this.colOffset + 1] = this.vertices.items[o + this.colOffset + 1] * this.color.g;
/*  521 */         this.vertices.items[o + this.colOffset + 2] = this.vertices.items[o + this.colOffset + 2] * this.color.b;
/*  522 */         if (this.colSize > 3) this.vertices.items[o + this.colOffset + 3] = this.vertices.items[o + this.colOffset + 3] * this.color.a; 
/*  523 */       } else if (this.cpOffset >= 0) {
/*  524 */         this.vertices.items[o + this.cpOffset] = this.tempC1.set(NumberUtils.floatToIntColor(this.vertices.items[o + this.cpOffset])).mul(this.color)
/*  525 */           .toFloatBits();
/*      */       } 
/*      */     }
/*      */     
/*  529 */     if (this.hasUVTransform && this.uvOffset >= 0) {
/*  530 */       this.vertices.items[o + this.uvOffset] = this.uOffset + this.uScale * this.vertices.items[o + this.uvOffset];
/*  531 */       this.vertices.items[o + this.uvOffset + 1] = this.vOffset + this.vScale * this.vertices.items[o + this.uvOffset + 1];
/*      */     } 
/*      */   }
/*      */   
/*  535 */   private final Vector3 tmpNormal = new Vector3();
/*      */ 
/*      */   
/*      */   public short vertex(Vector3 pos, Vector3 nor, Color col, Vector2 uv) {
/*  539 */     if (this.vindex >= Short.MAX_VALUE) throw new GdxRuntimeException("Too many vertices used");
/*      */     
/*  541 */     this.vertex[this.posOffset] = pos.x;
/*  542 */     if (this.posSize > 1) this.vertex[this.posOffset + 1] = pos.y; 
/*  543 */     if (this.posSize > 2) this.vertex[this.posOffset + 2] = pos.z;
/*      */     
/*  545 */     if (this.norOffset >= 0) {
/*  546 */       if (nor == null) nor = this.tmpNormal.set(pos).nor(); 
/*  547 */       this.vertex[this.norOffset] = nor.x;
/*  548 */       this.vertex[this.norOffset + 1] = nor.y;
/*  549 */       this.vertex[this.norOffset + 2] = nor.z;
/*      */     } 
/*      */     
/*  552 */     if (this.colOffset >= 0) {
/*  553 */       if (col == null) col = Color.WHITE; 
/*  554 */       this.vertex[this.colOffset] = col.r;
/*  555 */       this.vertex[this.colOffset + 1] = col.g;
/*  556 */       this.vertex[this.colOffset + 2] = col.b;
/*  557 */       if (this.colSize > 3) this.vertex[this.colOffset + 3] = col.a; 
/*  558 */     } else if (this.cpOffset > 0) {
/*  559 */       if (col == null) col = Color.WHITE; 
/*  560 */       this.vertex[this.cpOffset] = col.toFloatBits();
/*      */     } 
/*      */     
/*  563 */     if (uv != null && this.uvOffset >= 0) {
/*  564 */       this.vertex[this.uvOffset] = uv.x;
/*  565 */       this.vertex[this.uvOffset + 1] = uv.y;
/*      */     } 
/*      */     
/*  568 */     addVertex(this.vertex, 0);
/*  569 */     return this.lastIndex;
/*      */   }
/*      */ 
/*      */   
/*      */   public short vertex(float... values) {
/*  574 */     int n = values.length - this.stride;
/*  575 */     for (int i = 0; i <= n; i += this.stride)
/*  576 */       addVertex(values, i); 
/*  577 */     return this.lastIndex;
/*      */   }
/*      */ 
/*      */   
/*      */   public short vertex(MeshPartBuilder.VertexInfo info) {
/*  582 */     return vertex(info.hasPosition ? info.position : null, info.hasNormal ? info.normal : null, info.hasColor ? info.color : null, info.hasUV ? info.uv : null);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void index(short value) {
/*  588 */     this.indices.add(value);
/*      */   }
/*      */ 
/*      */   
/*      */   public void index(short value1, short value2) {
/*  593 */     ensureIndices(2);
/*  594 */     this.indices.add(value1);
/*  595 */     this.indices.add(value2);
/*      */   }
/*      */ 
/*      */   
/*      */   public void index(short value1, short value2, short value3) {
/*  600 */     ensureIndices(3);
/*  601 */     this.indices.add(value1);
/*  602 */     this.indices.add(value2);
/*  603 */     this.indices.add(value3);
/*      */   }
/*      */ 
/*      */   
/*      */   public void index(short value1, short value2, short value3, short value4) {
/*  608 */     ensureIndices(4);
/*  609 */     this.indices.add(value1);
/*  610 */     this.indices.add(value2);
/*  611 */     this.indices.add(value3);
/*  612 */     this.indices.add(value4);
/*      */   }
/*      */ 
/*      */   
/*      */   public void index(short value1, short value2, short value3, short value4, short value5, short value6) {
/*  617 */     ensureIndices(6);
/*  618 */     this.indices.add(value1);
/*  619 */     this.indices.add(value2);
/*  620 */     this.indices.add(value3);
/*  621 */     this.indices.add(value4);
/*  622 */     this.indices.add(value5);
/*  623 */     this.indices.add(value6);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void index(short value1, short value2, short value3, short value4, short value5, short value6, short value7, short value8) {
/*  629 */     ensureIndices(8);
/*  630 */     this.indices.add(value1);
/*  631 */     this.indices.add(value2);
/*  632 */     this.indices.add(value3);
/*  633 */     this.indices.add(value4);
/*  634 */     this.indices.add(value5);
/*  635 */     this.indices.add(value6);
/*  636 */     this.indices.add(value7);
/*  637 */     this.indices.add(value8);
/*      */   }
/*      */ 
/*      */   
/*      */   public void line(short index1, short index2) {
/*  642 */     if (this.primitiveType != 1) throw new GdxRuntimeException("Incorrect primitive type"); 
/*  643 */     index(index1, index2);
/*      */   }
/*      */ 
/*      */   
/*      */   public void line(MeshPartBuilder.VertexInfo p1, MeshPartBuilder.VertexInfo p2) {
/*  648 */     ensureVertices(2);
/*  649 */     line(vertex(p1), vertex(p2));
/*      */   }
/*      */ 
/*      */   
/*      */   public void line(Vector3 p1, Vector3 p2) {
/*  654 */     line(this.vertTmp1.set(p1, null, null, null), this.vertTmp2.set(p2, null, null, null));
/*      */   }
/*      */ 
/*      */   
/*      */   public void line(float x1, float y1, float z1, float x2, float y2, float z2) {
/*  659 */     line(this.vertTmp1.set(null, null, null, null).setPos(x1, y1, z1), this.vertTmp2.set(null, null, null, null).setPos(x2, y2, z2));
/*      */   }
/*      */ 
/*      */   
/*      */   public void line(Vector3 p1, Color c1, Vector3 p2, Color c2) {
/*  664 */     line(this.vertTmp1.set(p1, null, c1, null), this.vertTmp2.set(p2, null, c2, null));
/*      */   }
/*      */ 
/*      */   
/*      */   public void triangle(short index1, short index2, short index3) {
/*  669 */     if (this.primitiveType == 4 || this.primitiveType == 0) {
/*  670 */       index(index1, index2, index3);
/*  671 */     } else if (this.primitiveType == 1) {
/*  672 */       index(index1, index2, index2, index3, index3, index1);
/*      */     } else {
/*  674 */       throw new GdxRuntimeException("Incorrect primitive type");
/*      */     } 
/*      */   }
/*      */   
/*      */   public void triangle(MeshPartBuilder.VertexInfo p1, MeshPartBuilder.VertexInfo p2, MeshPartBuilder.VertexInfo p3) {
/*  679 */     ensureVertices(3);
/*  680 */     triangle(vertex(p1), vertex(p2), vertex(p3));
/*      */   }
/*      */ 
/*      */   
/*      */   public void triangle(Vector3 p1, Vector3 p2, Vector3 p3) {
/*  685 */     triangle(this.vertTmp1.set(p1, null, null, null), this.vertTmp2.set(p2, null, null, null), this.vertTmp3.set(p3, null, null, null));
/*      */   }
/*      */ 
/*      */   
/*      */   public void triangle(Vector3 p1, Color c1, Vector3 p2, Color c2, Vector3 p3, Color c3) {
/*  690 */     triangle(this.vertTmp1.set(p1, null, c1, null), this.vertTmp2.set(p2, null, c2, null), this.vertTmp3.set(p3, null, c3, null));
/*      */   }
/*      */ 
/*      */   
/*      */   public void rect(short corner00, short corner10, short corner11, short corner01) {
/*  695 */     if (this.primitiveType == 4) {
/*  696 */       index(corner00, corner10, corner11, corner11, corner01, corner00);
/*  697 */     } else if (this.primitiveType == 1) {
/*  698 */       index(corner00, corner10, corner10, corner11, corner11, corner01, corner01, corner00);
/*  699 */     } else if (this.primitiveType == 0) {
/*  700 */       index(corner00, corner10, corner11, corner01);
/*      */     } else {
/*  702 */       throw new GdxRuntimeException("Incorrect primitive type");
/*      */     } 
/*      */   }
/*      */   
/*      */   public void rect(MeshPartBuilder.VertexInfo corner00, MeshPartBuilder.VertexInfo corner10, MeshPartBuilder.VertexInfo corner11, MeshPartBuilder.VertexInfo corner01) {
/*  707 */     ensureVertices(4);
/*  708 */     rect(vertex(corner00), vertex(corner10), vertex(corner11), vertex(corner01));
/*      */   }
/*      */ 
/*      */   
/*      */   public void rect(Vector3 corner00, Vector3 corner10, Vector3 corner11, Vector3 corner01, Vector3 normal) {
/*  713 */     rect(this.vertTmp1.set(corner00, normal, null, null).setUV(0.0F, 1.0F), this.vertTmp2.set(corner10, normal, null, null).setUV(1.0F, 1.0F), this.vertTmp3
/*  714 */         .set(corner11, normal, null, null).setUV(1.0F, 0.0F), this.vertTmp4.set(corner01, normal, null, null).setUV(0.0F, 0.0F));
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void rect(float x00, float y00, float z00, float x10, float y10, float z10, float x11, float y11, float z11, float x01, float y01, float z01, float normalX, float normalY, float normalZ) {
/*  720 */     rect(this.vertTmp1.set(null, null, null, null).setPos(x00, y00, z00).setNor(normalX, normalY, normalZ).setUV(0.0F, 1.0F), this.vertTmp2
/*  721 */         .set(null, null, null, null).setPos(x10, y10, z10).setNor(normalX, normalY, normalZ).setUV(1.0F, 1.0F), this.vertTmp3
/*  722 */         .set(null, null, null, null).setPos(x11, y11, z11).setNor(normalX, normalY, normalZ).setUV(1.0F, 0.0F), this.vertTmp4
/*  723 */         .set(null, null, null, null).setPos(x01, y01, z01).setNor(normalX, normalY, normalZ).setUV(0.0F, 0.0F));
/*      */   }
/*      */ 
/*      */   
/*      */   public void addMesh(Mesh mesh) {
/*  728 */     addMesh(mesh, 0, mesh.getNumIndices());
/*      */   }
/*      */ 
/*      */   
/*      */   public void addMesh(MeshPart meshpart) {
/*  733 */     if (meshpart.primitiveType != this.primitiveType) throw new GdxRuntimeException("Primitive type doesn't match"); 
/*  734 */     addMesh(meshpart.mesh, meshpart.offset, meshpart.size);
/*      */   }
/*      */ 
/*      */   
/*      */   public void addMesh(Mesh mesh, int indexOffset, int numIndices) {
/*  739 */     if (!this.attributes.equals(mesh.getVertexAttributes())) throw new GdxRuntimeException("Vertex attributes do not match"); 
/*  740 */     if (numIndices <= 0) {
/*      */       return;
/*      */     }
/*  743 */     int numFloats = mesh.getNumVertices() * this.stride;
/*  744 */     tmpVertices.clear();
/*  745 */     tmpVertices.ensureCapacity(numFloats);
/*  746 */     tmpVertices.size = numFloats;
/*  747 */     mesh.getVertices(tmpVertices.items);
/*      */     
/*  749 */     tmpIndices.clear();
/*  750 */     tmpIndices.ensureCapacity(numIndices);
/*  751 */     tmpIndices.size = numIndices;
/*  752 */     mesh.getIndices(indexOffset, numIndices, tmpIndices.items, 0);
/*      */     
/*  754 */     addMesh(tmpVertices.items, tmpIndices.items, 0, numIndices);
/*      */   }
/*      */   
/*  757 */   private static IntIntMap indicesMap = null;
/*      */ 
/*      */   
/*      */   public void addMesh(float[] vertices, short[] indices, int indexOffset, int numIndices) {
/*  761 */     if (indicesMap == null) {
/*  762 */       indicesMap = new IntIntMap(numIndices);
/*      */     } else {
/*  764 */       indicesMap.clear();
/*  765 */       indicesMap.ensureCapacity(numIndices);
/*      */     } 
/*  767 */     ensureIndices(numIndices);
/*  768 */     int numVertices = vertices.length / this.stride;
/*  769 */     ensureVertices((numVertices < numIndices) ? numVertices : numIndices);
/*  770 */     for (int i = 0; i < numIndices; i++) {
/*  771 */       int sidx = indices[indexOffset + i];
/*  772 */       int didx = indicesMap.get(sidx, -1);
/*  773 */       if (didx < 0) {
/*  774 */         addVertex(vertices, sidx * this.stride);
/*  775 */         indicesMap.put(sidx, didx = this.lastIndex);
/*      */       } 
/*  777 */       index((short)didx);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public void addMesh(float[] vertices, short[] indices) {
/*  783 */     short offset = (short)(this.lastIndex + 1);
/*      */     
/*  785 */     int numVertices = vertices.length / this.stride;
/*  786 */     ensureVertices(numVertices); int v;
/*  787 */     for (v = 0; v < vertices.length; v += this.stride) {
/*  788 */       addVertex(vertices, v);
/*      */     }
/*  790 */     ensureIndices(indices.length);
/*  791 */     for (int i = 0; i < indices.length; i++) {
/*  792 */       index((short)(indices[i] + offset));
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public void patch(MeshPartBuilder.VertexInfo corner00, MeshPartBuilder.VertexInfo corner10, MeshPartBuilder.VertexInfo corner11, MeshPartBuilder.VertexInfo corner01, int divisionsU, int divisionsV) {
/*  802 */     PatchShapeBuilder.build(this, corner00, corner10, corner11, corner01, divisionsU, divisionsV);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public void patch(Vector3 corner00, Vector3 corner10, Vector3 corner11, Vector3 corner01, Vector3 normal, int divisionsU, int divisionsV) {
/*  809 */     PatchShapeBuilder.build(this, corner00, corner10, corner11, corner01, normal, divisionsU, divisionsV);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public void patch(float x00, float y00, float z00, float x10, float y10, float z10, float x11, float y11, float z11, float x01, float y01, float z01, float normalX, float normalY, float normalZ, int divisionsU, int divisionsV) {
/*  816 */     PatchShapeBuilder.build(this, x00, y00, z00, x10, y10, z10, x11, y11, z11, x01, y01, z01, normalX, normalY, normalZ, divisionsU, divisionsV);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public void box(MeshPartBuilder.VertexInfo corner000, MeshPartBuilder.VertexInfo corner010, MeshPartBuilder.VertexInfo corner100, MeshPartBuilder.VertexInfo corner110, MeshPartBuilder.VertexInfo corner001, MeshPartBuilder.VertexInfo corner011, MeshPartBuilder.VertexInfo corner101, MeshPartBuilder.VertexInfo corner111) {
/*  823 */     BoxShapeBuilder.build(this, corner000, corner010, corner100, corner110, corner001, corner011, corner101, corner111);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public void box(Vector3 corner000, Vector3 corner010, Vector3 corner100, Vector3 corner110, Vector3 corner001, Vector3 corner011, Vector3 corner101, Vector3 corner111) {
/*  830 */     BoxShapeBuilder.build(this, corner000, corner010, corner100, corner110, corner001, corner011, corner101, corner111);
/*      */   }
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public void box(Matrix4 transform) {
/*  836 */     BoxShapeBuilder.build(this, transform);
/*      */   }
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public void box(float width, float height, float depth) {
/*  842 */     BoxShapeBuilder.build(this, width, height, depth);
/*      */   }
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public void box(float x, float y, float z, float width, float height, float depth) {
/*  848 */     BoxShapeBuilder.build(this, x, y, z, width, height, depth);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public void circle(float radius, int divisions, float centerX, float centerY, float centerZ, float normalX, float normalY, float normalZ) {
/*  855 */     EllipseShapeBuilder.build(this, radius, divisions, centerX, centerY, centerZ, normalX, normalY, normalZ);
/*      */   }
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public void circle(float radius, int divisions, Vector3 center, Vector3 normal) {
/*  861 */     EllipseShapeBuilder.build(this, radius, divisions, center, normal);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public void circle(float radius, int divisions, Vector3 center, Vector3 normal, Vector3 tangent, Vector3 binormal) {
/*  868 */     EllipseShapeBuilder.build(this, radius, divisions, center, normal, tangent, binormal);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public void circle(float radius, int divisions, float centerX, float centerY, float centerZ, float normalX, float normalY, float normalZ, float tangentX, float tangentY, float tangentZ, float binormalX, float binormalY, float binormalZ) {
/*  875 */     EllipseShapeBuilder.build(this, radius, divisions, centerX, centerY, centerZ, normalX, normalY, normalZ, tangentX, tangentY, tangentZ, binormalX, binormalY, binormalZ);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public void circle(float radius, int divisions, float centerX, float centerY, float centerZ, float normalX, float normalY, float normalZ, float angleFrom, float angleTo) {
/*  884 */     EllipseShapeBuilder.build(this, radius, divisions, centerX, centerY, centerZ, normalX, normalY, normalZ, angleFrom, angleTo);
/*      */   }
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public void circle(float radius, int divisions, Vector3 center, Vector3 normal, float angleFrom, float angleTo) {
/*  890 */     EllipseShapeBuilder.build(this, radius, divisions, center, normal, angleFrom, angleTo);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public void circle(float radius, int divisions, Vector3 center, Vector3 normal, Vector3 tangent, Vector3 binormal, float angleFrom, float angleTo) {
/*  897 */     circle(radius, divisions, center.x, center.y, center.z, normal.x, normal.y, normal.z, tangent.x, tangent.y, tangent.z, binormal.x, binormal.y, binormal.z, angleFrom, angleTo);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public void circle(float radius, int divisions, float centerX, float centerY, float centerZ, float normalX, float normalY, float normalZ, float tangentX, float tangentY, float tangentZ, float binormalX, float binormalY, float binormalZ, float angleFrom, float angleTo) {
/*  906 */     EllipseShapeBuilder.build(this, radius, divisions, centerX, centerY, centerZ, normalX, normalY, normalZ, tangentX, tangentY, tangentZ, binormalX, binormalY, binormalZ, angleFrom, angleTo);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public void ellipse(float width, float height, int divisions, float centerX, float centerY, float centerZ, float normalX, float normalY, float normalZ) {
/*  914 */     EllipseShapeBuilder.build(this, width, height, divisions, centerX, centerY, centerZ, normalX, normalY, normalZ);
/*      */   }
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public void ellipse(float width, float height, int divisions, Vector3 center, Vector3 normal) {
/*  920 */     EllipseShapeBuilder.build(this, width, height, divisions, center, normal);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public void ellipse(float width, float height, int divisions, Vector3 center, Vector3 normal, Vector3 tangent, Vector3 binormal) {
/*  927 */     EllipseShapeBuilder.build(this, width, height, divisions, center, normal, tangent, binormal);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public void ellipse(float width, float height, int divisions, float centerX, float centerY, float centerZ, float normalX, float normalY, float normalZ, float tangentX, float tangentY, float tangentZ, float binormalX, float binormalY, float binormalZ) {
/*  935 */     EllipseShapeBuilder.build(this, width, height, divisions, centerX, centerY, centerZ, normalX, normalY, normalZ, tangentX, tangentY, tangentZ, binormalX, binormalY, binormalZ);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public void ellipse(float width, float height, int divisions, float centerX, float centerY, float centerZ, float normalX, float normalY, float normalZ, float angleFrom, float angleTo) {
/*  943 */     EllipseShapeBuilder.build(this, width, height, divisions, centerX, centerY, centerZ, normalX, normalY, normalZ, angleFrom, angleTo);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public void ellipse(float width, float height, int divisions, Vector3 center, Vector3 normal, float angleFrom, float angleTo) {
/*  951 */     EllipseShapeBuilder.build(this, width, height, divisions, center, normal, angleFrom, angleTo);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public void ellipse(float width, float height, int divisions, Vector3 center, Vector3 normal, Vector3 tangent, Vector3 binormal, float angleFrom, float angleTo) {
/*  958 */     EllipseShapeBuilder.build(this, width, height, divisions, center, normal, tangent, binormal, angleFrom, angleTo);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public void ellipse(float width, float height, int divisions, float centerX, float centerY, float centerZ, float normalX, float normalY, float normalZ, float tangentX, float tangentY, float tangentZ, float binormalX, float binormalY, float binormalZ, float angleFrom, float angleTo) {
/*  966 */     EllipseShapeBuilder.build(this, width, height, divisions, centerX, centerY, centerZ, normalX, normalY, normalZ, tangentX, tangentY, tangentZ, binormalX, binormalY, binormalZ, angleFrom, angleTo);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public void ellipse(float width, float height, float innerWidth, float innerHeight, int divisions, Vector3 center, Vector3 normal) {
/*  974 */     EllipseShapeBuilder.build(this, width, height, innerWidth, innerHeight, divisions, center, normal);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public void ellipse(float width, float height, float innerWidth, float innerHeight, int divisions, float centerX, float centerY, float centerZ, float normalX, float normalY, float normalZ) {
/*  981 */     EllipseShapeBuilder.build(this, width, height, innerWidth, innerHeight, divisions, centerX, centerY, centerZ, normalX, normalY, normalZ);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public void ellipse(float width, float height, float innerWidth, float innerHeight, int divisions, float centerX, float centerY, float centerZ, float normalX, float normalY, float normalZ, float angleFrom, float angleTo) {
/*  989 */     EllipseShapeBuilder.build(this, width, height, innerWidth, innerHeight, divisions, centerX, centerY, centerZ, normalX, normalY, normalZ, angleFrom, angleTo);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public void ellipse(float width, float height, float innerWidth, float innerHeight, int divisions, float centerX, float centerY, float centerZ, float normalX, float normalY, float normalZ, float tangentX, float tangentY, float tangentZ, float binormalX, float binormalY, float binormalZ, float angleFrom, float angleTo) {
/*  998 */     EllipseShapeBuilder.build(this, width, height, innerWidth, innerHeight, divisions, centerX, centerY, centerZ, normalX, normalY, normalZ, tangentX, tangentY, tangentZ, binormalX, binormalY, binormalZ, angleFrom, angleTo);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public void cylinder(float width, float height, float depth, int divisions) {
/* 1005 */     CylinderShapeBuilder.build(this, width, height, depth, divisions);
/*      */   }
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public void cylinder(float width, float height, float depth, int divisions, float angleFrom, float angleTo) {
/* 1011 */     CylinderShapeBuilder.build(this, width, height, depth, divisions, angleFrom, angleTo);
/*      */   }
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public void cylinder(float width, float height, float depth, int divisions, float angleFrom, float angleTo, boolean close) {
/* 1017 */     CylinderShapeBuilder.build(this, width, height, depth, divisions, angleFrom, angleTo, close);
/*      */   }
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public void cone(float width, float height, float depth, int divisions) {
/* 1023 */     cone(width, height, depth, divisions, 0.0F, 360.0F);
/*      */   }
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public void cone(float width, float height, float depth, int divisions, float angleFrom, float angleTo) {
/* 1029 */     ConeShapeBuilder.build(this, width, height, depth, divisions, angleFrom, angleTo);
/*      */   }
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public void sphere(float width, float height, float depth, int divisionsU, int divisionsV) {
/* 1035 */     SphereShapeBuilder.build(this, width, height, depth, divisionsU, divisionsV);
/*      */   }
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public void sphere(Matrix4 transform, float width, float height, float depth, int divisionsU, int divisionsV) {
/* 1041 */     SphereShapeBuilder.build(this, transform, width, height, depth, divisionsU, divisionsV);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public void sphere(float width, float height, float depth, int divisionsU, int divisionsV, float angleUFrom, float angleUTo, float angleVFrom, float angleVTo) {
/* 1048 */     SphereShapeBuilder.build(this, width, height, depth, divisionsU, divisionsV, angleUFrom, angleUTo, angleVFrom, angleVTo);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public void sphere(Matrix4 transform, float width, float height, float depth, int divisionsU, int divisionsV, float angleUFrom, float angleUTo, float angleVFrom, float angleVTo) {
/* 1055 */     SphereShapeBuilder.build(this, transform, width, height, depth, divisionsU, divisionsV, angleUFrom, angleUTo, angleVFrom, angleVTo);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public void capsule(float radius, float height, int divisions) {
/* 1062 */     CapsuleShapeBuilder.build(this, radius, height, divisions);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public void arrow(float x1, float y1, float z1, float x2, float y2, float z2, float capLength, float stemThickness, int divisions) {
/* 1069 */     ArrowShapeBuilder.build(this, x1, y1, z1, x2, y2, z2, capLength, stemThickness, divisions);
/*      */   }
/*      */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\graphics\g3\\utils\MeshBuilder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */