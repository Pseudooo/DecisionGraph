package me.Pseudo.DecisionGraph;

import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {

	public static void main(String[] args) {
		// Execution arguments from cmd line
		if(args.length == 2) {
			
			if(args[0].equalsIgnoreCase("traverse") || args[0].equalsIgnoreCase("tv")) {
				
				DecisionGraph dg = null;
				try {
					dg = new DecisionGraph(new File(args[1]));
				}catch(Exception e) {
					e.printStackTrace();
				}
				
				traverse(dg);
				
			}
			
		}
		// TODO Add system for interactive construction/traversal
	}
	
	// Traverse from console
	private static void traverse(DecisionGraph dg) {
		
		// Setup for console traversal
		Scanner sc = new Scanner(System.in);
		dg.startTraversing();
		
		// Start traversing graph
		while(!dg.isEndpoint()) {
			
			// Show text and available responses
			System.out.println("Text: " + dg.text());
			ArrayList<String> responses = dg.getResponses();
			System.out.println("Responses: " + responses.toString());
			
			// Request a choice
			System.out.print("Choice: ");
			String res = sc.nextLine();
			
			// Invalid response?
			if(!responses.contains(res)) {
				System.err.println("Invalid");
				continue;
			}
			
			dg.assertResponse(res);
			
		}
		
		// End reached
		System.out.println(dg.text());
		System.out.println("End reached");
		System.out.println("This has been your path: ");
		
		ArrayList<String> textPath = dg.getTextPath();
		ArrayList<String> responsePath = dg.getResponsePath();
		
		for(int i = 0; i < textPath.size(); i++) {
			
			System.out.println("Node: " + textPath.get(i));
			if(i != textPath.size() - 1) { // Text path always +1
				System.out.println("Response: " + responsePath.get(i));
			}
		}
		
	}
	
}
