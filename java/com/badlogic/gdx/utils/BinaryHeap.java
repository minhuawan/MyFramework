/*     */ package com.badlogic.gdx.utils;
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
/*     */ public class BinaryHeap<T extends BinaryHeap.Node>
/*     */ {
/*     */   public int size;
/*     */   private Node[] nodes;
/*     */   private final boolean isMaxHeap;
/*     */   
/*     */   public BinaryHeap() {
/*  27 */     this(16, false);
/*     */   }
/*     */   
/*     */   public BinaryHeap(int capacity, boolean isMaxHeap) {
/*  31 */     this.isMaxHeap = isMaxHeap;
/*  32 */     this.nodes = new Node[capacity];
/*     */   }
/*     */ 
/*     */   
/*     */   public T add(T node) {
/*  37 */     if (this.size == this.nodes.length) {
/*  38 */       Node[] newNodes = new Node[this.size << 1];
/*  39 */       System.arraycopy(this.nodes, 0, newNodes, 0, this.size);
/*  40 */       this.nodes = newNodes;
/*     */     } 
/*     */     
/*  43 */     ((Node)node).index = this.size;
/*  44 */     this.nodes[this.size] = (Node)node;
/*  45 */     up(this.size++);
/*  46 */     return node;
/*     */   }
/*     */   
/*     */   public T add(T node, float value) {
/*  50 */     ((Node)node).value = value;
/*  51 */     return add(node);
/*     */   }
/*     */   
/*     */   public T peek() {
/*  55 */     if (this.size == 0) throw new IllegalStateException("The heap is empty."); 
/*  56 */     return (T)this.nodes[0];
/*     */   }
/*     */   
/*     */   public T pop() {
/*  60 */     return remove(0);
/*     */   }
/*     */   
/*     */   public T remove(T node) {
/*  64 */     return remove(((Node)node).index);
/*     */   }
/*     */   
/*     */   private T remove(int index) {
/*  68 */     Node[] nodes = this.nodes;
/*  69 */     Node removed = nodes[index];
/*  70 */     nodes[index] = nodes[--this.size];
/*  71 */     nodes[this.size] = null;
/*  72 */     if (this.size > 0 && index < this.size) down(index); 
/*  73 */     return (T)removed;
/*     */   }
/*     */   
/*     */   public void clear() {
/*  77 */     Node[] nodes = this.nodes;
/*  78 */     for (int i = 0, n = this.size; i < n; i++)
/*  79 */       nodes[i] = null; 
/*  80 */     this.size = 0;
/*     */   }
/*     */   
/*     */   public void setValue(T node, float value) {
/*  84 */     float oldValue = ((Node)node).value;
/*  85 */     ((Node)node).value = value;
/*  86 */     if ((((value < oldValue) ? 1 : 0) ^ this.isMaxHeap) != 0) {
/*  87 */       up(((Node)node).index);
/*     */     } else {
/*  89 */       down(((Node)node).index);
/*     */     } 
/*     */   }
/*     */   private void up(int index) {
/*  93 */     Node[] nodes = this.nodes;
/*  94 */     Node node = nodes[index];
/*  95 */     float value = node.value;
/*  96 */     while (index > 0) {
/*  97 */       int parentIndex = index - 1 >> 1;
/*  98 */       Node parent = nodes[parentIndex];
/*  99 */       if ((((value < parent.value) ? 1 : 0) ^ this.isMaxHeap) != 0) {
/* 100 */         nodes[index] = parent;
/* 101 */         parent.index = index;
/* 102 */         index = parentIndex;
/*     */       } 
/*     */     } 
/*     */     
/* 106 */     nodes[index] = node;
/* 107 */     node.index = index;
/*     */   }
/*     */   
/*     */   private void down(int index) {
/* 111 */     Node[] nodes = this.nodes;
/* 112 */     int size = this.size;
/*     */     
/* 114 */     Node node = nodes[index];
/* 115 */     float value = node.value; while (true) {
/*     */       Node rightNode;
/*     */       float rightValue;
/* 118 */       int leftIndex = 1 + (index << 1);
/* 119 */       if (leftIndex >= size)
/* 120 */         break;  int rightIndex = leftIndex + 1;
/*     */ 
/*     */       
/* 123 */       Node leftNode = nodes[leftIndex];
/* 124 */       float leftValue = leftNode.value;
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 129 */       if (rightIndex >= size) {
/* 130 */         rightNode = null;
/* 131 */         rightValue = this.isMaxHeap ? Float.MIN_VALUE : Float.MAX_VALUE;
/*     */       } else {
/* 133 */         rightNode = nodes[rightIndex];
/* 134 */         rightValue = rightNode.value;
/*     */       } 
/*     */ 
/*     */       
/* 138 */       if ((((leftValue < rightValue) ? 1 : 0) ^ this.isMaxHeap) != 0) {
/* 139 */         if (leftValue == value || (((leftValue > value) ? 1 : 0) ^ this.isMaxHeap) != 0)
/* 140 */           break;  nodes[index] = leftNode;
/* 141 */         leftNode.index = index;
/* 142 */         index = leftIndex; continue;
/*     */       } 
/* 144 */       if (rightValue == value || (((rightValue > value) ? 1 : 0) ^ this.isMaxHeap) != 0)
/* 145 */         break;  nodes[index] = rightNode;
/* 146 */       rightNode.index = index;
/* 147 */       index = rightIndex;
/*     */     } 
/*     */ 
/*     */     
/* 151 */     nodes[index] = node;
/* 152 */     node.index = index;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object obj) {
/* 157 */     if (!(obj instanceof BinaryHeap)) return false; 
/* 158 */     BinaryHeap other = (BinaryHeap)obj;
/* 159 */     if (other.size != this.size) return false; 
/* 160 */     for (int i = 0, n = this.size; i < n; i++) {
/* 161 */       if ((other.nodes[i]).value != (this.nodes[i]).value) return false; 
/* 162 */     }  return true;
/*     */   }
/*     */   
/*     */   public int hashCode() {
/* 166 */     int h = 1;
/* 167 */     for (int i = 0, n = this.size; i < n; i++)
/* 168 */       h = h * 31 + Float.floatToIntBits((this.nodes[i]).value); 
/* 169 */     return h;
/*     */   }
/*     */   
/*     */   public String toString() {
/* 173 */     if (this.size == 0) return "[]"; 
/* 174 */     Node[] nodes = this.nodes;
/* 175 */     StringBuilder buffer = new StringBuilder(32);
/* 176 */     buffer.append('[');
/* 177 */     buffer.append((nodes[0]).value);
/* 178 */     for (int i = 1; i < this.size; i++) {
/* 179 */       buffer.append(", ");
/* 180 */       buffer.append((nodes[i]).value);
/*     */     } 
/* 182 */     buffer.append(']');
/* 183 */     return buffer.toString();
/*     */   }
/*     */   
/*     */   public static class Node
/*     */   {
/*     */     float value;
/*     */     int index;
/*     */     
/*     */     public Node(float value) {
/* 192 */       this.value = value;
/*     */     }
/*     */     
/*     */     public float getValue() {
/* 196 */       return this.value;
/*     */     }
/*     */     
/*     */     public String toString() {
/* 200 */       return Float.toString(this.value);
/*     */     }
/*     */   }
/*     */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gd\\utils\BinaryHeap.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */