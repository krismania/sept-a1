package main;
import java.util.Scanner;

import console.Console;

public class Main
{  
	
	public static void main(String[] args)
	{
		if (args.length == 1 && args[0].equals("-debugDB"))
		{
			Controller.debugDB = true;
		}
		Scanner sc = new Scanner(System.in);
		Console console = new Console(sc);
		
		console.start();
	}
}
