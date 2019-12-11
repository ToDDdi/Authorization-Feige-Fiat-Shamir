
fun pow(a: Long, b: Long) = Math.pow(a.toDouble(), b.toDouble()).toLong()

fun generateS(N: Long): Long {
    var result: Long = 0
    while (true) {
        result = generatePrivateKey(N)
        if(modularPow(result, 1, N) == 1.toLong()) {
            break
        }
    }
    return result
}

fun calculateV(S: Long, N: Long) = modularPow(S, 2, N)

fun generateE() = (0 until 2).random().toLong()

fun calculateY(R: Long, S: Long, N: Long, E: Long): Long {
    var result: Long = 0
    if(E == 0.toLong()) {
        result = R
    } else result = modularPow(R * S, 1, N)
    return result
}

fun checkY(Y: Long, N: Long, X: Long, V: Long, E: Long): Boolean {
    var result: Long = modularPow(Y, 2, N)
    if (E == 0.toLong()) {
        if (result == X) {
            return true
        }
    } else if (E == 1.toLong()) {
        if (result == modularPow(X * V, 1, N)) {
            return true
        }
    } else return false
    return false
}

fun main() {
/*
    val p: Long = 683
    val q: Long = 811
    val n = p * q
    val s: Long = 43215
    val v = calculateV(s, n)
    val r: Long = 38177

    val x = modularPow(r, 2, n)

   val y = calculateY(r, s, n, 1)

    println(checkY(y, n, x, v, 1))
*/
    /*
    val r = generatePrivateKey(N - 1)

    val x = modularPow(r, 2, N) */


    val PQg = generatePQg(10000)
    val N = PQg.first * PQg.second
    val S = generateS(N)

    val V = calculateV(S, N)

    for (i in 0..3) {
        val r = generatePrivateKey(N - 1)
        val x = modularPow(r, 2, N)

        val e = generateE()

        val y = calculateY(r, S, N, e)

        println(checkY(y, N, x, V, e))

    }
 }