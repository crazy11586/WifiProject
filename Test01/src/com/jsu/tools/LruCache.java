package com.jsu.tools;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

public class LruCache<K, V> {

	private static final float hashTableLoadFactor = 0.75f;

	private LinkedHashMap<K, V> map;
	private int cacheSize;

	public LruCache(int cacheSize) {
		this.cacheSize = cacheSize;
		int hashTableCapacity = (int) Math.ceil(cacheSize / hashTableLoadFactor) + 1;
		map = new LinkedHashMap<K, V>(hashTableCapacity, hashTableLoadFactor, true) {
			// (an anonymous inner class)
			private static final long serialVersionUID = 1;

			@Override
			protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
				return size() > LruCache.this.cacheSize;
			}
		};
	}

	public synchronized V get(K key) {
		return map.get(key);
	}

	public synchronized void put(K key, V value) {
		map.put(key, value);
	}


	public synchronized void clear() {
		map.clear();
	}
	
	public synchronized boolean contains(String str){
		Iterator keys = map.keySet().iterator();
		while(keys.hasNext()){
			String key = (String)keys.next();
			if(str.equals(key)){
				return true;
			}
		}
		return false;
	}


	public synchronized int usedEntries() {
		return map.size();
	}


	public synchronized Collection<Map.Entry<K, V>> getAll() {
		return new ArrayList<Map.Entry<K, V>>(map.entrySet());
	}

}