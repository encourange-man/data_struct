package com.machen.datastruct.heap;

/**
 * 最大堆
 * 堆的存储最常用的是使用数组，因为堆是在形式上是一个完全二叉树（非空结点的左结点不为空）
 *
 * @author machen
 * @date 2019-04-04 17:20
 */
public class MaxHeap<T> {

   private  T[] data;
    /**
     * 堆的容量
     */
   private int capacity;
    /**
     * 堆的实际结点个数
     */
    private int count;

    /**
     * 数组从下标1开始存储
     * @param capacity
     */
    public MaxHeap(int capacity) {
        data = (T[])new Object[capacity+1];
        this.capacity = capacity;
        count = 0;
    }

    /**
     * 判断堆是否为空
     */
    public boolean isEmpty(){
        return count==0;
    }
    /**
     * 堆中元素个数
     */
    public int size(){
        return count;
    }

    /**
     *  插入操作
     */
    public void insert(T item){

    }

    private void shiftUp(){

    }

    private void shiftDown(){

    }

    private void swap(){

    }

}
