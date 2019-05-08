package com.dummy.myerp.consumer;


import java.math.BigDecimal;

import com.dummy.myerp.model.bean.comptabilite.CompteComptable;
import com.dummy.myerp.model.bean.comptabilite.LigneEcritureComptable;

public class App {
	
	public static void main( String[] args )
    {
//		boolean b = Pattern.matches("\\d{1,5}-\\d{4}/\\d{5}", "AC-2016/00001");
//		boolean b = Pattern.matches("[A-Z]{2}-\\d{4}/\\d{5}", "AC-2019/00245");
//		System.out.println(b);
//		Logger logger = LogManager.getLogger();
//		logger.warn("ceci est un avertissement");
//		
//		BigDecimal valeur1 = new BigDecimal("2.84555");
//		BigDecimal arrondi = valeur1.setScale(1,BigDecimal.ROUND_CEILING);
//		System.out.println("valeur1= "+ arrondi);
//		
//		BigDecimal val1 = new BigDecimal(123.435);
//		System.out.println(val1);
//		BigDecimal val2 = new BigDecimal(123.435).setScale(2, BigDecimal.ROUND_HALF_UP);
//		System.out.println(val2);
//		BigDecimal val3 = new BigDecimal(123);
//		System.out.println(val3);
//		
		LigneEcritureComptable ligne2 = new LigneEcritureComptable(new CompteComptable(512), null, null, new BigDecimal("52.74"));
		System.out.println(ligne2.getCredit());
    }


}
