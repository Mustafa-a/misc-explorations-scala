import scalaz.syntax.equal._ // for assert_===
import scalaz.std.anyVal._   // for assert_=== to work on basic values
import scalaz.std.list._     // for assert_=== to work on lists
import scalaz.std.option._   // for assert_=== to work on options
import scalaz.std.string._   // for assert_=== to work on strings

/*
 * Many functional languages, especially those in the ML family, include
 * algebraic data types (discriminated union types), which can be
 * recursive. These types provide an effective, light-weight mechanism
 * for creating tree-like structures of arbitrary depth. Behavior can be
 * added easily in the form of recursive functions that use pattern
 * matching for branching on the structure of the argument. These types
 * are closed in the sense that it is not possible to add new variants to
 * the type.

 * By contrast, in mainstream object-oriented languages, one would
 * typically create an interface to represent the type and a class for
 * each variant of the type, using the Composite and Decorator patterns
 * for structure and, sometimes, the Visitor pattern for
 * behavior. Without the visitor, it is very easy to add more variants;
 * with the visitor, this is still possible but harder.
 *
 * This discussion provides more background on this trade-off.
 *
 * Organizational charts (org charts) are a simple, well understood
 * example of a hierarchical structure. The following example explores
 * how we can represent and work with org charts in F#.
 *
 * In this example, an org chart is either a person (a leaf in the
 * resulting tree) or an organizational unit (an interior node), which
 * has zero or more org charts as children.
 *
 * F# version at http://laufer.cs.luc.edu/teaching/372/handouts/orgchart
 */

sealed trait Node
case class P(name: String) extends Node
case class OU(name: String, children: List[Node]) extends Node

val p = P("George")
p.name assert_=== "George"

val cs =   OU("CS",   List(P("Sekharan"), P("Rom"), P("Thiruvathukal")))
val math = OU("Math", List(P("Jensen"), P("Doty"), P("Giaquinto")))
val cas =  OU("CAS",  List(P("Andress"), P("Andrade"), cs, math ))
val luc =  OU("luc",  List(cas))

/*
 * Now we will define a size function on org charts. If the org chart is
 * a person, then the size is one. Else we compute the size recursively
 * from the list of children, orgs. (Each item in this list can be a
 * person or OU, but this distinction will be made in the recursive
 * application of the function to each child.) Now we apply the size
 * function recursively to each child of orgs to find out the size of
 * each child. Instead of explicit loops, we use List.map to apply a
 * function to each item in a list and List.fold for adding up the
 * results. (These functions are discussed in a separate handout in more
 * detail.)
 */

def size(o: Node): Int = o match {
  case P(_) => 1
  case OU(_, children) => children.map(size).sum
}

size(p) assert_=== 1
size(cs) assert_=== 3
size(luc) assert_=== 8

def depth(o: Node): Int = o match {
  case P(_) => 1
  case OU(_, children) => ??? // TODO
}

depth(p) assert_=== 1
depth(cs) assert_=== 2
depth(luc) assert_=== 4

// TODO convert these functions into methods