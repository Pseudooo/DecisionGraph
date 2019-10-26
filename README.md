# Decision Graphs
## Creation
In order to create a DecisionGraph object within your program you're going to need to read it from a valid script file. The syntax of which can be viewed [here](#Syntax).

Once a file exists on the system we can instantiate it like so:
```java
DecisionGraph dg = null;
File f = new File("MyFile.dg");
try {
	dg = new DecisionGraph(f);
} catch (FileNotFoundException e) {
	// ...
} catch (InvalidArgumentsException e) {
	// ...
} catch (NodeAlreadyDefinedException e) {
	// ...
} catch (NodeNotDefinedException e) {
	// ...
} catch (InvalidOperationException e) {
	// ...
} catch (RootNodeMissingException e) {
	// ...
} catch (ChildlessNodeException e) {
	// ...
} catch (UnreadableFileException e) {
	// ...
} catch (IOException e) {
	// ...
}finally {
	// ...
}
```
This will catch all of the respective exceptions should you want to handle them individually, alternatively, should you want to handle all of the syntactical exceptions in one place we can throw their super class:
```java
try {
	dg = new DecisionGraph(f);
} catch (FileNotFoundException e) {
	// ...
} catch (InvalidSyntaxException e) {
	// ...
} catch (UnreadableFileException e) {
	// ...
} catch (IOException e) {
	// ...
}finally {
	// ...
}
```
## Traversal
The decision graph object will also handle traversal of the internal graph internally and we can check updates from various methods.
### Starting Traversal
Before starting traversal of the graph use:
```java
myDecisionGraph.startTraversing();
```
### Decisions
#### Getting text
```java
myDecisionGraph.text();
```
returns `String`
#### Getting possible responses
```java
myDecisionGraph.getResponses()
```
returns `ArrayList<String>`
#### Asserting response
```java
myDecisionGraph.assertResponse(String response);
```
Note: If the response isn't defined in `myDecisionGraph.getResponses()` then it will throw an exception
### End points
```java
myDecisionGraph.isEndpoint()
```
returns `boolean` of true if current node **is** an end-point
### Paths
#### Node text path
```java
myDecisionGraph.getTextPath()
```
returns `ArrayList<String>`
#### Responses given
```java
myDecisionGraph.getResponsePath()
```
returns `ArrayList<String>`
### Ending Traversal
Once a graph has finished being traversed call:
```java
myDecisionGraph.endTraversal();
```
Note this will clear both of the paths held by the structure.
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
