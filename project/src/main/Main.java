package main;
import java.util.Scanner;

import console.Console;

public class Main
{  
	
	public static void main(String[] args)
	{
		Scanner sc = new Scanner(System.in);
		Console console = new Console(sc);
		
		console.start();
	}
}
