package com.test.bean;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.locks.ReentrantReadWriteLock;


public abstract class SuperBean {
	
	@SuppressWarnings("rawtypes")
	private transient static Map<Class, String[]> map = new HashMap<Class, String[]>();

	private transient static ReentrantReadWriteLock rwl = new ReentrantReadWriteLock();
	
	
	public String[] getAttributeNames(){
		rwl.readLock().lock();
		try {
			return getAttributeAry();
		} finally {
			rwl.readLock().unlock();
		}
	}
	
	
	private String[] getAttributeAry(){
		String[] arys = map.get(this.getClass());
		if(arys == null){
			rwl.readLock().unlock();
			rwl.writeLock().lock();
			try {
				arys = map.get(this.getClass());
				if(arys == null){
					Set<String> set = new HashSet<String>();
					String[] strAry = BeanHelper.getInstance().getPropertiesAry(this);
					for(String str : strAry){
						if(getPKFieldName() != null && str.equals("primarykey")) {
							set.add(getPKFieldName());
						} else if(!(str.equals("status") || str.equals("dirty"))) {
							set.add(str);
						}
						arys = set.toArray(new String[set.size()]);
						map.put(this.getClass(), arys);	
					}
				}
				
			} finally {
				rwl.readLock().lock();
				rwl.writeLock().unlock();
			}
		}
		return arys;
	}
	
	public abstract String getParentPKFieldName();
	
	public abstract String getPKFieldName();
	
	public abstract String getTableName();
	
	
	public String getPrimaryKey() {
		if(getPKFieldName() == null)
			return null;
		return (String) BeanHelper.getProperty(this, getPKFieldName().toLowerCase());
	}
	
	public Object getAttributeValue(String attributeName) {
		if(attributeName == null || attributeName.length() == 0)
			return null;
		if(getPKFieldName() != null && attributeName.equals(getPKFieldName()))
			attributeName = "primarykey";
		return BeanHelper.getProperty(this, attributeName);
	}

	
	public void setAttributeValue(String attributeName, Object value) {
		if(attributeName == null || attributeName.length() == 0)
			return;
		if(getPKFieldName() != null && attributeName.equals(getPKFieldName()))
			attributeName = "primarykey";
		BeanHelper.setProperty(this, attributeName, value);
	}
	

}
