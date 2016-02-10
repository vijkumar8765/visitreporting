/*
 * 
 * package com.vw.visitreporting.auditing;

import java.io.FileInputStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Properties;
import java.util.Set;

public class Test {//

	private int a = 10;
	private static String abc = null;
	public static void main(String[] args){
		Test obj = new Test();
		try {
			obj.abc();
			obj.def();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("added ====");
		obj.m1();
		obj.m2();
		obj.m3();
		obj.m4();
		double myNumber = 98765.4321;
		System.out.println(new DecimalFormat("0.0").format(myNumber));
		System.out.println(myNumber);
//		System.out.println(DecimalFormat.getInstance("00.0").format(myNumber));
		System.out.println(new DecimalFormat().format(myNumber));
		System.out.println(myNumber%98765.00);
		
		Date aDate = null;
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(1450000000000L);
		aDate = cal.getTime();
		System.out.println(new SimpleDateFormat("dd-MMM-yyyy").format(aDate));
		cal.add(Calendar.DAY_OF_MONTH, 60);
		aDate = cal.getTime();
		System.out.println(new SimpleDateFormat("dd-MMM-yyyy").format(aDate));
		
		double d = -27.2345;
		System.out.println(Math.ceil(d));
		System.out.println(Math.round(d));
		System.out.println(Math.abs(d));
		System.out.println(Math.floor(d));
		
		if(obj.abc == null || obj.abc.length()>10){
			
		}
		addToString();
	}

	public static void addToString (){
		System.out.println(new String("abc") == "abc");
		System.out.println("abc".equals("abc"));
		System.out.println(new String("abc") == new String("abc"));
		System.out.println("abc" == "abc");
		System.out.println(new String() == new String());
		
		int[] array1 = {};
		System.out.println(array1.length);
		Object array5[] = new Integer[5];
		System.out.println(array5.length);
		
		
	}
	
	public void def(){
		int x = 5,y =0;
		try {
			try {
				System.out.println(x);
				System.out.println(x/y);
				System.out.println(y);
			} catch (ArithmeticException e) {
				System.out.println("Inner Catch1");
			} catch (RuntimeException e) {
				System.out.println("Inner Catch1");
			}finally{
				System.out.println("Inner Finally");
			}
		} catch (Exception e) {
			System.out.println("Outer Catch");
		}
	}
	public void abc() throws Exception {
		Set<String> d = new LinkedHashSet<String>();
		String s = "abc";
		HashMap<String, Integer> aMap = new HashMap<String, Integer>();
		String param0 = null, param1 = null;
		aMap.put("Key1", 123);
		d.add("3");
		d.add("1");
		d.add("3");
		d.add("2");
		d.add("3");
		d.add("1");
		for (String string : d) {
			System.out.print((String) string + "-");
		}
		System.out.println("$$$$$$$$");
		ForEachTest obj1 = new ForEachTest(new String[]{"1","2","3"});
		for (String text: obj1) {
			System.out.println(text);
		}
		
		boolean b = false;
		int n = 5;
		System.out.println(b || n ==5);
		System.out.println(b = true && n == 5);
		System.out.println(b);
		Properties p = new Properties();
		p.load(new FileInputStream("e:\\props.properties"));
		String a = p.getProperty("a");
		Integer c = new Integer(p.getProperty("b"));
		System.out.println("@@@@@@@ " + a + c);
		
	}
	
	class A{
		private int b = 20;
		public void method() throws Exception {
			System.out.println(a);
			
		}

			
	}
	
	public void m1(){
		System.out.println("A.m1, ");
	}
	protected void m2(){
		System.out.println("A.m2, ");
	}
	private void m3(){
		System.out.println("A.m3, ");
	}
	void m4(){
		System.out.println("A.m4, ");
	}
	
	class ForEachTest implements Iterable<String>{
		
		private String[] array;
		
		public ForEachTest(String[] newArray){
			this.array = newArray;
		}
		
		public Iterator<String> iterator(){
			return Arrays.asList(array).iterator();
		}
	}
	
}


 */