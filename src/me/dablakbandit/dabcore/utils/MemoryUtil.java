package me.dablakbandit.dabcore.utils;

import java.util.concurrent.atomic.AtomicBoolean;

public class MemoryUtil {
    
    private static AtomicBoolean memory = new AtomicBoolean(false);
    
    public static boolean isMemoryFree(){
        return !memory.get();
    }
    
    public static boolean isMemoryLimited(){
        return memory.get();
    }
    
    public static int calculateMemory(){
        long heapSize = Runtime.getRuntime().totalMemory();
        long heapMaxSize = Runtime.getRuntime().maxMemory();
        if(heapSize < heapMaxSize){
            return Integer.MAX_VALUE;
        }
        long heapFreeSize = Runtime.getRuntime().freeMemory();
        int size = (int) ((heapFreeSize * 100) / heapMaxSize);
        if(size > (100 - 95)){
            memoryPlentifulTask();
            return Integer.MAX_VALUE;
        }
        return size;
    }
    
    public static void memoryLimitedTask() {
        memory.set(true);
    }
    
    public static void memoryPlentifulTask() {
        memory.set(false);
    }
}
