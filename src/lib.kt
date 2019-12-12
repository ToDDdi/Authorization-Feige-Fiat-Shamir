import javafx.css.Size
import java.lang.Math.sqrt
import java.lang.System.err
import java.math.BigInteger
import java.security.MessageDigest
import java.security.PrivateKey
import java.util.*
import javax.swing.text.html.HTML.Tag.P
import java.lang.Math.random as random1

fun generateInput(p: Long): Long {
    //val maxValue: Long = Math.pow(2.toDouble(), (63).toDouble()).toLong() - 1.toLong()
    return (1 until p).random()
}

fun gcd(a: Long, b: Long): Triple<Long, Long, Long> {
        if (a == 0.toLong()) {
            return Triple(b, 0, 1)
        }
        val result = gcd(b % a, a)
        //println(Triple(result.first, result.third - (b / a) * result.second, result.second))
        return Triple(result.first, result.third - (b / a) * result.second, result.second)
}

fun generatePQg(maxValue: Long): Triple<Long, Long, Long> {
    var p: Long
    var q: Long
    var g: Long
    while(true) {
        q = (0 until maxValue).random()
        p = 2.toLong() * q + 1.toLong()
        if ((p.toBigInteger()).isProbablePrime(20) && (q.toBigInteger()).isProbablePrime(20)) {
            break
        }
    }
    while(true) {
        g = (1 until p-1).random()
        if(!(g.toBigInteger()).isProbablePrime(20)) {
            continue
        }
        if(modularPow(g, q, p) != 1.toLong()) { //g является первообразным корнем по модулю
            break
        }
    }
    return Triple(p, (p - 1) / 2, g)
}

fun generatePrivateKey (p: Long) : Long {
    return (1 until p).random()
}

fun generatePublicKey (p: Long, g: Long, privateKey: Long) : Long {
    return modularPow(g, privateKey, p)
}

fun modularPow(base: Long, indexN: Long, modulus: Long): Long {
    var c: Long = 1
    for(item in 1..indexN)
        c = (c * base) % modulus
    return c
}

fun modularPow2(base: Long, indexN: Long, modulus: Long): Long {
    var c: Long = 1
    for(item in 1..indexN)
        c = (c * base) % modulus
    if(c < 0) c = (c + modulus) % modulus
    return c
}