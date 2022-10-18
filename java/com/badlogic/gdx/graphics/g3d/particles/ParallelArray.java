/*     */ package com.badlogic.gdx.graphics.g3d.particles;
/*     */ 
/*     */ import com.badlogic.gdx.utils.Array;
/*     */ import com.badlogic.gdx.utils.GdxRuntimeException;
/*     */ import com.badlogic.gdx.utils.reflect.ArrayReflection;
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
/*     */ public class ParallelArray
/*     */ {
/*     */   Array<Channel> arrays;
/*     */   public int capacity;
/*     */   public int size;
/*     */   
/*     */   public static class ChannelDescriptor
/*     */   {
/*     */     public int id;
/*     */     public Class<?> type;
/*     */     public int count;
/*     */     
/*     */     public ChannelDescriptor(int id, Class<?> type, int count) {
/*  39 */       this.id = id;
/*  40 */       this.type = type;
/*  41 */       this.count = count;
/*     */     }
/*     */   }
/*     */   
/*     */   public abstract class Channel
/*     */   {
/*     */     public int id;
/*     */     public Object data;
/*     */     public int strideSize;
/*     */     
/*     */     public Channel(int id, Object data, int strideSize) {
/*  52 */       this.id = id;
/*  53 */       this.strideSize = strideSize;
/*  54 */       this.data = data;
/*     */     }
/*     */     
/*     */     public abstract void add(int param1Int, Object... param1VarArgs);
/*     */     
/*     */     public abstract void swap(int param1Int1, int param1Int2);
/*     */     
/*     */     protected abstract void setCapacity(int param1Int);
/*     */   }
/*     */   
/*     */   public static interface ChannelInitializer<T extends Channel> {
/*     */     void init(T param1T);
/*     */   }
/*     */   
/*     */   public class FloatChannel
/*     */     extends Channel {
/*     */     public float[] data;
/*     */     
/*     */     public FloatChannel(int id, int strideSize, int size) {
/*  73 */       super(id, new float[size * strideSize], strideSize);
/*  74 */       this.data = (float[])super.data;
/*     */     }
/*     */ 
/*     */     
/*     */     public void add(int index, Object... objects) {
/*  79 */       for (int i = this.strideSize * ParallelArray.this.size, c = i + this.strideSize, k = 0; i < c; i++, k++) {
/*  80 */         this.data[i] = ((Float)objects[k]).floatValue();
/*     */       }
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public void swap(int i, int k) {
/*  87 */       i = this.strideSize * i;
/*  88 */       k = this.strideSize * k;
/*  89 */       for (int c = i + this.strideSize; i < c; i++, k++) {
/*  90 */         float t = this.data[i];
/*  91 */         this.data[i] = this.data[k];
/*  92 */         this.data[k] = t;
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void setCapacity(int requiredCapacity) {
/*  98 */       float[] newData = new float[this.strideSize * requiredCapacity];
/*  99 */       System.arraycopy(this.data, 0, newData, 0, Math.min(this.data.length, newData.length));
/* 100 */       this.data = newData;
/*     */     }
/*     */   }
/*     */   
/*     */   public class IntChannel extends Channel {
/*     */     public int[] data;
/*     */     
/*     */     public IntChannel(int id, int strideSize, int size) {
/* 108 */       super(id, new int[size * strideSize], strideSize);
/* 109 */       this.data = (int[])super.data;
/*     */     }
/*     */ 
/*     */     
/*     */     public void add(int index, Object... objects) {
/* 114 */       for (int i = this.strideSize * ParallelArray.this.size, c = i + this.strideSize, k = 0; i < c; i++, k++) {
/* 115 */         this.data[i] = ((Integer)objects[k]).intValue();
/*     */       }
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public void swap(int i, int k) {
/* 122 */       i = this.strideSize * i;
/* 123 */       k = this.strideSize * k;
/* 124 */       for (int c = i + this.strideSize; i < c; i++, k++) {
/* 125 */         int t = this.data[i];
/* 126 */         this.data[i] = this.data[k];
/* 127 */         this.data[k] = t;
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void setCapacity(int requiredCapacity) {
/* 133 */       int[] newData = new int[this.strideSize * requiredCapacity];
/* 134 */       System.arraycopy(this.data, 0, newData, 0, Math.min(this.data.length, newData.length));
/* 135 */       this.data = newData;
/*     */     }
/*     */   }
/*     */   
/*     */   public class ObjectChannel<T>
/*     */     extends Channel {
/*     */     Class<T> componentType;
/*     */     public T[] data;
/*     */     
/*     */     public ObjectChannel(int id, int strideSize, int size, Class<T> type) {
/* 145 */       super(id, ArrayReflection.newInstance(type, size * strideSize), strideSize);
/* 146 */       this.componentType = type;
/* 147 */       this.data = (T[])super.data;
/*     */     }
/*     */ 
/*     */     
/*     */     public void add(int index, Object... objects) {
/* 152 */       for (int i = this.strideSize * ParallelArray.this.size, c = i + this.strideSize, k = 0; i < c; i++, k++) {
/* 153 */         this.data[i] = (T)objects[k];
/*     */       }
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public void swap(int i, int k) {
/* 160 */       i = this.strideSize * i;
/* 161 */       k = this.strideSize * k;
/* 162 */       for (int c = i + this.strideSize; i < c; i++, k++) {
/* 163 */         T t = this.data[i];
/* 164 */         this.data[i] = this.data[k];
/* 165 */         this.data[k] = t;
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void setCapacity(int requiredCapacity) {
/* 171 */       T[] newData = (T[])ArrayReflection.newInstance(this.componentType, this.strideSize * requiredCapacity);
/* 172 */       System.arraycopy(this.data, 0, newData, 0, Math.min(this.data.length, newData.length));
/* 173 */       this.data = newData;
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
/*     */   public ParallelArray(int capacity) {
/* 185 */     this.arrays = new Array(false, 2, Channel.class);
/* 186 */     this.capacity = capacity;
/* 187 */     this.size = 0;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public <T extends Channel> T addChannel(ChannelDescriptor channelDescriptor) {
/* 193 */     return addChannel(channelDescriptor, null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public <T extends Channel> T addChannel(ChannelDescriptor channelDescriptor, ChannelInitializer<T> initializer) {
/* 200 */     T channel = getChannel(channelDescriptor);
/* 201 */     if (channel == null) {
/* 202 */       channel = allocateChannel(channelDescriptor);
/* 203 */       if (initializer != null) initializer.init(channel); 
/* 204 */       this.arrays.add(channel);
/*     */     } 
/* 206 */     return channel;
/*     */   }
/*     */ 
/*     */   
/*     */   private <T extends Channel> T allocateChannel(ChannelDescriptor channelDescriptor) {
/* 211 */     if (channelDescriptor.type == float.class)
/* 212 */       return (T)new FloatChannel(channelDescriptor.id, channelDescriptor.count, this.capacity); 
/* 213 */     if (channelDescriptor.type == int.class) {
/* 214 */       return (T)new IntChannel(channelDescriptor.id, channelDescriptor.count, this.capacity);
/*     */     }
/* 216 */     return (T)new ObjectChannel(channelDescriptor.id, channelDescriptor.count, this.capacity, channelDescriptor.type);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public <T> void removeArray(int id) {
/* 222 */     this.arrays.removeIndex(findIndex(id));
/*     */   }
/*     */   
/*     */   private int findIndex(int id) {
/* 226 */     for (int i = 0; i < this.arrays.size; i++) {
/* 227 */       Channel array = ((Channel[])this.arrays.items)[i];
/* 228 */       if (array.id == id) return i; 
/*     */     } 
/* 230 */     return -1;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addElement(Object... values) {
/* 237 */     if (this.size == this.capacity) throw new GdxRuntimeException("Capacity reached, cannot add other elements");
/*     */     
/* 239 */     int k = 0;
/* 240 */     for (Channel strideArray : this.arrays) {
/* 241 */       strideArray.add(k, values);
/* 242 */       k += strideArray.strideSize;
/*     */     } 
/* 244 */     this.size++;
/*     */   }
/*     */ 
/*     */   
/*     */   public void removeElement(int index) {
/* 249 */     int last = this.size - 1;
/*     */     
/* 251 */     for (Channel strideArray : this.arrays) {
/* 252 */       strideArray.swap(index, last);
/*     */     }
/* 254 */     this.size = last;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public <T extends Channel> T getChannel(ChannelDescriptor descriptor) {
/* 260 */     for (Channel array : this.arrays) {
/* 261 */       if (array.id == descriptor.id) return (T)array; 
/*     */     } 
/* 263 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public void clear() {
/* 268 */     this.arrays.clear();
/* 269 */     this.size = 0;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setCapacity(int requiredCapacity) {
/* 275 */     if (this.capacity != requiredCapacity) {
/* 276 */       for (Channel channel : this.arrays) {
/* 277 */         channel.setCapacity(requiredCapacity);
/*     */       }
/* 279 */       this.capacity = requiredCapacity;
/*     */     } 
/*     */   }
/*     */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\graphics\g3d\particles\ParallelArray.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */