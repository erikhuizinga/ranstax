data class State(
    val stacks: List<Stack>,
)

data class Stack(
    val name: String,
    val size: Int,
)