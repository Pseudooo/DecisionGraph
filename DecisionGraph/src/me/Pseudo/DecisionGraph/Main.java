package me.Pseudo.DecisionGraph;

import java.io.File;
import java.util.Scanner;

public class Main {

	public static void main(String[] args) {
		
		File f = new File("Breakfast.dg");
		DecisionGraph dg;
		try {
			dg = new DecisionGraph(f);
		}catch (Exception e) {
			e.printStackTrace();
			return;
		}
		
		dg.startTraversing();
		Scanner sc = new Scanner(System.in);
		
		do {
			System.out.println(dg.question());
			String ln = sc.nextLine();
			dg.respond(Boolean.parseBoolean(ln));
		}while(!dg.endReached());
		
		System.out.println(dg.question());
		dg.endTraversal();
		System.out.println("End reached!");
	}
	
}
