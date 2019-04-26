package com.dummy.myerp.consumer;

import java.util.regex.Pattern;

public class App {
	
	public static void main( String[] args )
    {
//		boolean b = Pattern.matches("\\d{1,5}-\\d{4}/\\d{5}", "AC-2016/00001");
		boolean b = Pattern.matches("[A-Z]{2}-\\d{4}/\\d{5}", "AC-2016/00001");
		System.out.println(b);
    	
  
    }


}
