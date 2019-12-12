import java.io.File

class Client(N: Long) {
    val S = generateS(N)
    val V = calculateV(S, N)
    val R = generatePrivateKey(N - 1)
    val X = modularPow(R, 2, N)
}

fun generateClients(length: Long, N: Long) = Array(length.toInt(), {i -> Client(N)})

fun pow(a: Long, b: Long) = Math.pow(a.toDouble(), b.toDouble()).toLong()

fun generateS(N: Long): Long {
    var result: Long = 0
    while (true) {
        result = generatePrivateKey(N - 1)
        if(gcd(result, N).first == 1.toLong()) {
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
    when(E) {
        0.toLong() -> return result == X
        1.toLong() -> return result == modularPow(X * V, 1, N)
    }
    return false
}

fun protocolStart(iter: Int, client: Array<Client>, N: Long) {
    var checkTest = LongArray(iter) {i -> -1}
    for(i in client.indices) {
        val e = generateE()
        val y = calculateY(client[i].R, client[i].S, N, e)
        println("---------------------------------------")
        println("Производится вход клиента №${i + 1}")
        for(j in 0..iter) {
            if(checkY(y, N, client[i].X, client[i].V, e)) {
                println("Тест №${j + 1} клиента №${i + 1} прошел успешно")
            } else {
                println("Тест №${j + 1} клиента №${i + 1} не прошёл успешно")
                println("Клиенту №${i + 1} не удалось войти")
                break
            }
        }
        println("Авторизация прошла успешно!")
        println("----------------------------------------")
    }
}

fun main() {
    println("SERVER")
    val PQg = generatePQg(10000)
    val N = PQg.first * PQg.second
    println("---------------------")
    println("Client")
    var client = generateClients(2, N)
    protocolStart(3, client, N)
 }