
import java.io.OutputStream
import java.net.Socket
import java.nio.charset.Charset
import java.util.*
import kotlin.concurrent.thread

fun main(args: Array<String>) {
    val address = "localhost"
    val port = 9999

    val client = Client(address, port, false)
    client.run()
}

class Client(address: String, port: Int, flag: Boolean) {
    private val connection: Socket = Socket(address, port)
    private var connected: Boolean = true
    private val flag = flag
    private var N: Long = 0
    private var S: Long = 0
    private var V: Long = 0
    private var R: Long = 0
    private var X: Long = 0
    private var E: Long = 0

    init {
        println("Connected to server at $address on port $port")
    }

    private val reader: Scanner = Scanner(connection.getInputStream())
    private val writer: OutputStream = connection.getOutputStream()

    fun run() {
        thread { read() }
    }

    private fun write(message: String) {
        writer.write((message + "\n").toByteArray(Charset.defaultCharset()))
    }

    private fun read() {
        while (connected)
            check(reader.nextLine())
    }

    private fun check(message: String) {
        var check = message.split(' ')
        when (check[0]) {
            "N" -> {
                this.N = check[1].toLong()
                authorization(check[1].toLong())

            }
            "R" -> {
                this.R = generatePrivateKey(this.N - 1)
                this.X = modularPow(this.R, 2, this.N)
                write("X ${this.X}")
                println("Отправка X")
            }
            "E" -> {
                this.E = check[1].toLong()

                write("Y ${calculateY(this.R, this.S, this.N, this.E)} ${this.E}")
            }
            "exit" -> {
                connected = false
                reader.close()
                connection.close()
            }
            else -> println(message)
        }
    }

    private fun authorization(N: Long) {
        this.S = generateS(N)
        this.V = calculateV(S, N)
        write("V ${this.V}")
    }

    private fun generateS(N: Long): Long {
        var result: Long = 0
        while (true) {
            result = generatePrivateKey(N - 1)
            if(gcd(result, N).first == 1.toLong()) {
                break
            }
        }
        return result
    }

    private fun calculateV(S: Long, N: Long) = modularPow(S, 2, N)

    private fun calculateY(R: Long, S: Long, N: Long, E: Long): Long {
        var result: Long = 0
        when (this.flag) {
            false -> when (E) {
                0.toLong() -> result = R
                1.toLong() -> result = modularPow(R * S, 1, N)
            }
            true -> when (E) {
                0.toLong() -> result = R
                1.toLong() -> result = R * 2
            }
        }

        /*if(E == 0.toLong()) {
            result = R
        } else result = modularPow(R * S, 1, N) */
        return result
    }
}
