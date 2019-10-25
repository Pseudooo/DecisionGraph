# Decision Graphs
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
