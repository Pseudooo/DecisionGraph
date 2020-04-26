package me.Pseudo.DecisionGraph;

import java.io.File;
import java.util.Scanner;

import me.Pseudo.DecisionGraph.Exceptions.InvalidSyntaxException;

public class Main {

	public static void main(String[] args) {
		
		// traverse <file>
		if(args.length == 2 || args.length == 3) {
			
			// Check arguments
			if(!(args[0].equals("tv") || args[0].equals("traverse"))) {
				System.out.println("Invalid Arguments!");
				return;
			}
			
			// Attempt to load graph
			DecisionGraph dg = null;
			try {
				dg = DecisionGraph.fromFile(new File(args[1]));
			}catch(Exception e) {
				e.printStackTrace();
				return;
			}
			
			// Assign entry point to be root or user-defined node
			String entry = args.length == 3 ? args[2] : dg.getRoot();
			
			// Attempt to init at start point
			try {
				traverse(dg, entry);
			}catch(Exception e) {
				System.err.println("Invalid Entry Point!");
				return;
			}
			
		}else if(args.length == 0) {
			
			repl();
			
		}else {
			System.out.println("Invalid Arguments!");
		}
		
	}
	
	// Function to handle console-traversal of DecisionGraph
	private static void traverse(DecisionGraph dg, String entrypoint)
			throws Exception {
		
		// Attempt to start trav
		if(!dg.initTraversal(entrypoint))
			throw new Exception("Invalid Enty Point!");
		
		int step = 0;
		@SuppressWarnings("resource") // Don't want to close stdin
		Scanner sc = new Scanner(System.in);
		while(!dg.isCurrentEndpoint()) {
			step++;
			
			// Display current nodes information
			System.out.printf(" * * * [%03d]%n", step);
			System.out.printf("Label: %s%n", dg.getCurrentLabel());
			System.out.printf("Responses:%n");
			for(String response : dg.getCurrentResponses())
				System.out.printf("  - %s%n", response);
			
			System.out.print("Choice: ");
			String option = sc.nextLine();
			
			// giveResponse returns false for any invalid response
			if(!dg.giveResponse(option)) {
				step--; // Counter the incoming increase at beginning of loop
				System.out.println("Invalid Option!");
				continue; // reset loop
			}
			System.out.println(""); // Place a break at the end of this block
			
		}
		
		System.out.printf(" * * * [%03d] (end)%n", ++step);
		System.out.printf("Label: %s%n", dg.getCurrentLabel());
		
	}
	
	private static void repl() {
		
		System.out.println("DecisionGraph REPL");
		System.out.println("Use :help for help");
		
		DecisionGraph dg = new DecisionGraph();
		
		Scanner sc = new Scanner(System.in);
		loop: while(true) {
			
			// Ask for user-input
			System.out.print("- ");
			String[] cmd = sc.nextLine().split(" ");
			
			// Determine how to handle
			switch(cmd[0]) {
			
			case ":help":
			case ":h":
				System.out.println(":quit -> Quit");
				System.out.println(":load -> Load");
				break;
			
			case "quit":
			case ":q":
				System.out.println("Bye!");
				break loop; // Break while-loop
			
			case ":load":
			case ":l":
				
				// :l on its own will unload files 
				if(cmd.length == 1) {
					dg = new DecisionGraph();
				}else {
					try {
						dg = DecisionGraph.fromFile(new File(cmd[1]));
					}catch(Exception e) {
						e.printStackTrace();
					}
				}
				
				break;
			
			case ":enter":
			case ":e":
				
				// TODO: Write utility to handle "entering" a DecisionGraph
				
				break;
				
			default:
				try {
					dg.exec(cmd, -1);
				} catch (InvalidSyntaxException e) {
					e.printStackTrace();
				}
			}
			
		}
		
	}
	
}
	
