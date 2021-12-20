import java.io.File

abstract class Sfnumber(
    var parent: Sfnumber?
) {

    abstract fun magnitude(): Int

}

class Literal(
    var value: Int,
    parent: Sfnumber?
) : Sfnumber(parent) {

    override fun magnitude(): Int {
        return value
    }

    override fun toString(): String {
        return value.toString()
    }

}

class Container(
    var left: Sfnumber, 
    var right: Sfnumber,
    parent: Sfnumber?
): Sfnumber(parent) {

    override fun magnitude(): Int {
        return 3 * left.magnitude() + 2 * right.magnitude()
    }

    override fun toString(): String {
        return "[${this.left.toString()},${this.right.toString()}]"
    }

    fun rightMost(): Literal? {
        val r = right

        return when (r) {
            is Literal -> r
            is Container -> {
                var c: Sfnumber = r
                while (c is Container) { c = c.right }

                c as? Literal
            }
            else -> null
        }
    }

    fun leftMost(): Literal? {
        val l = left

        return when (l) {
            is Literal -> l
            is Container -> {
                var c: Sfnumber = l
                while (c is Container) { c = c.left }

                c as? Literal
            }
            else -> null
        }
    }

    fun setAsZero(number: Sfnumber) {
        when (number) {
            left -> left = Literal(0, this)
            right -> right = Literal(0, this)
        }
    }

    fun addLeft(number: Sfnumber, value: Int) {
        if (number == right) {
            if (left is Literal) (left as? Literal)?.let { it.value += value }
            else (left as? Container)?.rightMost()?.let { it.value += value }
        } else {
            val p = (parent as? Container) ?: return

            if (p.right == this) {
                val l = p.left

                if (l is Literal) l.value += value
                else if (l is Container) l.rightMost()?.let { it.value += value }
            } else if (p.left == this) {
                p.addLeft(this, value)
            }
        }
    }

    fun addRight(number: Sfnumber, value: Int) {
        if (number == left) {
            if (right is Literal) (right as? Literal)?.let { it.value += value }
            else (right as? Container)?.leftMost()?.let { it.value += value }
        } else {
            val p = (parent as? Container) ?: return

            if (p.left == this) {
                val r = p.right

                if (r is Literal) r.value += value
                else if (r is Container) r.leftMost()?.let { it.value += value }
            } else if (p.right == this) {
                p.addRight(this, value)
            }
        }
    }

}

fun main() {
    val input = File("input.txt").readLines()
    var max = 0

    input.forEach { a ->
        input.forEach { b -> 
            if (a != b) {
                val an = parse(a, null)
                val bn = parse(b, null)
                val r = reduce(sum(an, bn))

                max = maxOf(max, r.magnitude())
            }
        }
    }

    println(max)
}

fun explode(n: Sfnumber, depth: Int = 0): Boolean {
    if (n is Container) {
        if (depth == 4) {
            (n.left as? Literal)?.let { l -> (n.parent as? Container)?.addLeft(n, l.value) } 
            (n.right as? Literal)?.let { r -> (n.parent as? Container)?.addRight(n, r.value) }
            (n.parent as? Container)?.setAsZero(n)

            return true
        }

        return explode(n.left, depth + 1) || explode(n.right, depth + 1)
    }

    return false
}

fun split(n: Sfnumber): Boolean {
    if (n is Literal && n.value >= 10) {
        val p = n.parent as? Container ?: return false
        val ll = Literal(n.value / 2, null)
        val lr = Literal(n.value - ll.value, null)

        if (p.left == n) {
            val l = Container(
                left = ll,
                right = lr,
                parent = p
            )

            l.left.parent = l
            l.right.parent = l
            p.left = l
        } else if (p.right == n) {
            val r = Container(
                left = ll,
                right = lr,
                parent = p
            )

            r.left.parent = r
            r.right.parent = r
            p.right = r
        }

        return true
    } else if (n is Container) {
        return split(n.left) || split(n.right)
    }

    return false
}

fun reduce(n: Sfnumber): Sfnumber {
    var rn = n

     while (true) {
        var explode = false
        var split = false

        if (explode(rn, 0)) {
            explode = true
            println("Explode: $rn")
        }

        if (explode) continue

        if (split(rn)) {
            split = true
            println("Split: $rn")
        }

        if (!explode && !split) break
    }

    return rn
}

fun sum(a: Sfnumber, b: Sfnumber): Sfnumber {
    return Container(a, b, null).also {
        a.parent = it
        b.parent = it

        println("Sum: $it")
    }
}

fun parse(string: String, parent: Sfnumber?): Sfnumber {
    var depth = 0
    var index = 0
    val sfnumber = Container(Literal(0, null), Literal(0, null), parent)
    var child = string.drop(1).dropLast(1)
    var leftString = ""
    var rightString = ""

    do {
        if (child[index] == '[') depth++
        else if (child[index] == ']') depth--
        leftString += child[index]
        index++
    } while (depth > 0)

    index++

    do {
        if (child[index] == '[') depth++
        else if (child[index] == ']') depth--
        rightString += child[index]
        index++
    } while (depth > 0)

    sfnumber.left = when {
        leftString.startsWith("[") -> parse(leftString, sfnumber)
        else -> Literal(leftString.toInt(), sfnumber)
    }

    sfnumber.right = when {
        rightString.startsWith("[") -> parse(rightString, sfnumber)
        else -> Literal(rightString.toInt(), sfnumber)
    }

    return sfnumber
}
