# Decision Graphs
## Contents

- [Using the Jar](#Using-The-Jar)
- [The API](#The-API)
  - [Getting a DecisionGraph Instance](#Getting-a-DeicisionGraph-Instance)
  - [Traversing an Instance](#Traversing-an-Instance)
  - [Giving a Response](#Giving-a-Response)
- [Terminating-Traversal](#Terminating-Traversal)
- [Traversal](#Traversal)
- [Syntax](#Syntax)
- [Example](#Example)

## Using The Jar
### Traversing DecisionGraph Script
The built Jar-file can be used to traverse readily made DecisionGraph scripts (.dg) the argument `traverse` or its shorter alias, `tv`, can be used like so:
```
java -jar DecisionGraph.jar traverse <YourScript.dg>
```

This will start traversing your graph at its defined root node, if the script is not valid then it will notify you of any errors. Should you wish you start somewhere else in the `DecisionGraph` you've defined you can specificy the start node like so:
```
java -jar DecisionGraph.jar traverse <YourScript.dg> <start_node>
```

If we make use of the [example](#Example) script covered below we can view the following:

![Example of root traversal](https://i.imgur.com/hxShwTL.png)

We can also specify our starting node:

![Example of specifying a start point](https://i.imgur.com/7nzNrkT.png)

## The API

### Getting a DecisionGraph Instance
In order to obtain an instance of the `DecisionGraph` class we're going to need access to a valid script file, such as the one described down in the [example](#Example). There's a `static` method that we can use to load a script from a file like so:
```java
DecisionGraph dg = null;
try {
    dg = DecisionGraph.fromFile(new File("MyFile.dg"));
}catch(FileNotFoundException e) {
    // ...
}catch(IOException e) {
    // ...
}catch(InvalidSyntaxException e) {
    // ...
}
```
### Traversing an Instance
The `DecisionGraph` instance is also responsible for handling its own traversal, if we imagine we have an instance of the class, `dg`, that we can use.

#### Initiating Traversal
Before actually traversing the graph we must declare that we're going to be doing so:
```java
dg.initTraversal();
```

Or, alternatively if we want to start at a specific node, `String myNode`, we can use:
```java
boolean success = dg.initTraversal(myNode);
```

If the provided value, `myNode` is a valid node within `dg` then it will return `true` otherwise `false`.

#### Endpoints
Now during traversal you must keep check of if the current node is an end point or not, as some methods will return `null` values when an end-point is reached. We can check if the current node is an endpoint with:
```java
boolean isEndpoint = dg.isCurrentEndpoint();
```

#### Decisions
Each node will have a _"decision"_ associated with it _(Note: endpoints do not have decisions they're simply outputs)_ which means there will be a `label` and a list of `responses`. 

##### Getting Current Label
We can fetch the `String` value of the current node's label by doing:
```java
String label = dg.getCurrentLabel();
```
Note: Will return a `null` value if the graph is not currently traversing.

##### Getting Possible Responses
We can fetch a `List<String>` value containing all possible responses for our current node by doing:
```java
List<String> responses = dg.getCurrentResponses();
```
Note: Will return a `null` value if the graph is not currently traversing or the current node is an endpoint.

##### Giving a Response
If you have a value, `String myResponse`, then we can give this response to `dg` like so:
```java
boolean success = dg.giveResponse(myResponse);
```
Note: Will return a `boolean` value of `true` if `myResponse` is a valid response, `false` otherwise.

#### Terminating Traversal
Once you're done traversing `dg` you need to end its traversal:
```java
dg.terminateTraversal();
```

### Example
A simple program that'll allow the user to traverse their graph is:
```java
public static void traverse(DecisionGraph dg) {
	
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
			continue; // Skip assertion
		}
		
		dg.assertResponse(res);
		
	}
	
	// End reached
	System.out.println(dg.text());
	System.out.println("End reached");
	System.out.println("This has been your path: ");
	
	// Notify user of their path through the graph
	ArrayList<String> textPath = dg.getTextPath();
	ArrayList<String> responsePath = dg.getResponsePath();
	
	// Output paths
	for(int i = 0; i < textPath.size(); i++) {
		
		System.out.println("Node: " + textPath.get(i));
		if(i != textPath.size() - 1) { // Text path always +1
			System.out.println("Response: " + responsePath.get(i));
		}
		
	}
	
}
```
Which will allow text-based traversal within the console window
## Syntax
### define
The `define` keyword (`def` can be used as a shorter alternative) is used to define the nodes of your graph like so:
```
define <node_id> <is_endpoint> <text...>
```
* `node_id` -> The desired ID of the node, this must be unique
* `is_endpoint` -> boolean to determine if this node is an endpoint
* `text` -> The text associated with the node

Alternatively, we can use def:
```
def <node_id> <is_endpoint> <text...>
```
### assert
The `assert` keyword (`asrt` can be used as a shorted alternative) is used to define the relationships between various nodes within the graph.
```
assert <from_id> <to_id> <response...>
```
* `from_id` -> The ID of the node that the mapping will run **FROM**
* `to_id` -> The ID of the node that the mapping will run **TOO**
* `response` -> The response required to run this mapping

Alternatively:
```
asrt <from_id> <to_id> <response...>
```
### mkroot
The `mkroot` keyword (`mkrt` can be used as a shoter alternative) is used to delcare the root (entry point of the graph)
```
mkroot <node_id>
```
* `node_id` -> The ID of the node to declare as root

Alternatively:
```
mkrt <node_id>
```
## Example
As an example to show the script we're going to implement the following, very simple, decision graph using this syntax!
![Decision Graph](https://i.imgur.com/mYhRzT7.png)
```
# Making an example decision graph!

# These are called comments, if you need to store a note of sort in the script itself then
# start the line with a # and it won't be interpretted by the program.

# Defining the root node (entry point)
def root false Do you want anything?
mkrt root

# Now we have two children, one of which is an endpoint
def result false I don't have anything to give you!
def end true Great!

# Now we can assign our relationships between these nodes
# There's a loop between root and result
asrt root result Yes
asrt result root Ok

# Now we also need a link to the end node
asrt root end No
```
