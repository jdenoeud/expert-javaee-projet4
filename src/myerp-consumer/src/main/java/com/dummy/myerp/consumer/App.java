package com.dummy.myerp.consumer;

import java.util.Calendar;

public class App {
	
	public static void main( String[] args )
    {
    	Calendar calendar = Calendar.getInstance();
    	calendar.set(2016, 11, 03);
    	System.out.println(calendar.toString());
    	Integer annee = calendar.get(Calendar.YEAR);
    	System.out.println(annee);
  
    }


}
