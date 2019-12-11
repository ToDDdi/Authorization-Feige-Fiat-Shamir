import javafx.css.Size
import java.lang.Math.sqrt
import java.lang.System.err
import java.math.BigInteger
import java.security.MessageDigest
import java.security.PrivateKey
import java.util.*
import javax.swing.text.html.HTML.Tag.P
import java.lang.Math.random as random1

val MAXVALUE : Long = 1000000000 - 1

fun String.md5(): ByteArray {
    val digested = MessageDigest.getInstance("MD5").digest(toByteArray())
    return digested //.joinToString("") { String.format("%02x", it) }
}

fun String.SHA256(): ByteArray {
    val digested = MessageDigest.getInstance("SHA-256").digest(toByteArray())
    return digested //.joinToString("") { String.format("%02x", it) }
}

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

fun babyGiantStep(a: Long, y: Long, p: Long): Long {
    val n: Long = (sqrt(p.toDouble()) + 1).toLong()
    val values = mutableMapOf<Long, Long>()
    for(i in n downTo 1) {
        values[modularPow(a, i * n, p)] = i
    }
    for(i in 0..n) {
        val cur: Long = (modularPow(a, i, p) * y) % p
        if (values.containsKey(cur)) {
            val value: Long = values.getValue(cur)
            val answer: Long = value * n - i
            if (answer < p) {
                return answer
            }
        }
    }
    println("The equation has no solution")
    return -1
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

fun diffieHellman(): Boolean {
    var generate = generatePQg(MAXVALUE)

    var p = generate.first
    var g = generate.third

    val alice = object {
        var privateKey: Long = generatePrivateKey(p)
        var publicKey: Long = generatePublicKey(p, g, privateKey)
        var Key: Long = 0
    }
    val bob = object {
        var privateKey: Long = generatePrivateKey(p)
        var publicKey: Long = generatePublicKey(p, g, privateKey)
        var Key: Long = 0
    }

    println("Bob private key = ${bob.privateKey} and Bob public Key = ${bob.publicKey}")
    println("Alice private key = ${alice.privateKey} and Alice public Key = ${alice.publicKey}\n")
    bob.Key = alice.publicKey
    alice.Key = bob.publicKey
    println("Swap public key Bob's and Alice's...............................\n")
    bob.Key = modularPow(bob.Key, bob.privateKey, p)
    alice.Key = modularPow(alice.Key, alice.privateKey, p)

    if(bob.Key == alice.Key) {
        println("bob's key = ${bob.Key} and Alice's key = ${alice.Key}")
        return true
    } else {
        return false
    }

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