# Defining Decision Graphs

## Syntax
### define
The `define` operation takes a minimum of four arguments to be syntactically correct. The format is:
```
define <node_id> <is_endpoint> <question...>
```
`node_id`: The desired ID of the node to define, must be unique
`is_endpoint`: boolean value to determine if the node should be an exit point
`question`: Question/End statement just placed in raw text

### assert
The `assert` operation takes exactly four arguments, `assert` is used to declare the relationships between already defined nodes.
```
assert <from_id> <yes|no> <to_id>
```
`from_id`: The ID of the node to map from (Must be pre-defined)
`<yes|no>`: The response that should be mapped
`to_id`: The ID of the node to map to (Must be pre-defined)

### mkroot
Every graph must have an entry point to start traversing, so `mkroot` is used to declare the root node.
```
mkroot <node_id>
```
`node_id`: The ID of the desired root (Must be pre-defined)

### Whitespace and comments
Whitespace is simply empty lines
Comments are used by prefixing a line with `#`
Any other line must be syntactically correct

### Example
As an example I'll write a script to define this decision graph:
![Decision Graph](https://i.imgur.com/l20SrZi.png)
Green -> Entry Point
Red -> Exit Point

```

# For this example script we're going to be writing
# The script that'll allow us to define the decision
# graph found here:
# https://drive.google.com/file/d/1mUPB5unm_WvMv1wtopP_bzn2YGHwYAli/view?usp=sharing

# Out root node (entry node) is defined first
# define syntax is:
# define <node_id> <is_endpoint> <question...>
define A false Did you have breakfast?

# We also want to make A our root
mkroot A

# Now we can define both of A's children
define B false Do you want breakfast?
define C true Leave for university!
# Notice C is an endpoint

# Now declare the relationships between A and its children
# assert syntax is:
# assert <node_id> <yes|no> <node_id>
# To (response) from
assert A yes C
assert A no B

# Also when we don't want breakfast we go to uni
assert B no C

define D false Can you afford to buy breakfast?
assert B yes D
# No afford means no breakfast -> Leave for uni

define E false Do you want to buy breakfast?
define F true Buy Breakfast
assert D yes E
assert E yes F

define G false Reconsider?
assert G yes B
assert G no C
assert E no G

define H false Can you make breakfast?
assert D no H

# Potential loop between H and I for checking again
define I false Check again?
assert I yes H
assert I no C
assert H no I

define J false Is there enough time to make breakfast?
assert H yes J
assert J no C

define K true Make breakfast
assert J yes K
```
