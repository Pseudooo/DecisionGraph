package me.Pseudo.DecisionGraph;

import java.io.File;
import java.util.Scanner;

public class Main {

	public static void main(String[] args) {
		
		DecisionGraph dg = null;
		try {
			dg = new DecisionGraph(new File("Breakfast.dg"));
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		Scanner sc = new Scanner(System.in);
		
		dg.startTraversing();
		
		while(!dg.isEndpoint()) {
			
			System.out.println(dg.text());
			System.out.println(dg.getResponses().toString());
			String res = sc.nextLine();
			dg.assertResponse(res);
			
		}
		
		System.out.println(dg.text());
		dg.endTraversing();
		
	}
	
}
