package com.dummy.myerp.consumer;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class App {
	
	public static void main( String[] args )
    {
//		boolean b = Pattern.matches("\\d{1,5}-\\d{4}/\\d{5}", "AC-2016/00001");
//		boolean b = Pattern.matches("[A-Z]{2}-\\d{4}/\\d{5}", "AC-2019/00245");
//		System.out.println(b);
		Logger logger = LogManager.getLogger();
		logger.warn("ceci est un avertissement");

    }


}
