package com.dummy.myerp.consumer;

public class App {
	
	public static void main( String[] args )
    {
    	System.out.println( "Hello DAO !" );
    	Integer derniereValeur = 12345;
    	String numSequence = "";
    	if (derniereValeur<10) {
    		numSequence = "0000"+Integer.toString(derniereValeur);
    	} else if (derniereValeur<100) {
    		numSequence = "000"+Integer.toString(derniereValeur);
    	} else if (derniereValeur<1000) {
    		numSequence = "00"+Integer.toString(derniereValeur);
    	} else if (derniereValeur<10000) {
    		numSequence = "0"+Integer.toString(derniereValeur);
    	} else {
    		numSequence = Integer.toString(derniereValeur);
    	}
    	System.out.println(numSequence);
  
    }


}
