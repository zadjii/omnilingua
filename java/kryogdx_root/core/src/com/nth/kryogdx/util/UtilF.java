package com.nth.kryogdx.util;

/**
 * Created with IntelliJ IDEA.
 * User: zadjii
 * Date: 12/7/13
 * Time: 11:20 AM
 */
public class UtilF {


	public static long getTimeMillis(){
		return System.nanoTime()/1000000L;
	}

	public static String serverMsg(){return ":-----\t\t\tSERVER:";}
	public static String clientMsg(){return "CLIENT:";}
}
