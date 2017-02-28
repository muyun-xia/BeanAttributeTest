package com.test.bean;

import java.lang.reflect.Field;

public class MainTest {
	
	private static final int MALE = 1;
	
	private static final int FEMALE = 2;
	
	public static void main(String[] args) {
		
		TestBean testBean  = new TestBean();
		
		String[] attributeNames = testBean.getAttributeNames();
		
		testBean.setPk_user("00001");
		testBean.setUser_name("ÕÅÈý");
		testBean.setAge(14);
		testBean.setSex(MALE);
		
		for(String attr : attributeNames){
			System.out.println(attr);
			System.out.println(testBean.getAttributeValue(attr));
		}
		try {
			String sql = buildInsertSql(testBean);
			System.out.println(sql);
		} catch (NoSuchFieldException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println("__________________________________________");
		
		
	}
	
	
	public static String buildInsertSql(TestBean testBean) throws NoSuchFieldException, SecurityException{
		StringBuilder colNames = new StringBuilder();
		StringBuilder colvalues = new StringBuilder();
		for(String key : testBean.getAttributeNames()){
			Field field = testBean.getClass().getDeclaredField(key);
			Object value = testBean.getAttributeValue(key);
			if(value != null){
				if(colNames.length() > 0){
					colNames.append(",");
					colvalues.append(",");
				}
				colNames.append(key);
				if(field.getType().equals(String.class)){
					colvalues.append("'");
					colvalues.append(value.toString().replace("'", "''"));
					colvalues.append("'");
				} else {
					colvalues.append(value);
				}
				
			}
		}
		StringBuilder sb = new StringBuilder("insert into ");
		sb.append(testBean.getTableName());
		sb.append(" (");
		sb.append(colNames);
		sb.append(") values (");
		sb.append(colvalues);
		sb.append(")");
		return sb.toString();
	}
	
	
}
