import java.io.OutputStream
import java.net.ServerSocket
import java.net.Socket
import java.nio.charset.Charset
import java.util.*
import kotlin.concurrent.thread


fun main(args: Array<String>) {
    val server = ServerSocket(9999)
    println("Server is running on port ${server.localPort}")

    while (true) {
        val client = server.accept()
        println("Client connected: ${client.inetAddress.hostAddress}")

        // Run client in it's own thread.
        thread { ClientHandler(client).run() }
    }

}

class ClientHandler(client: Socket) {
    private val client: Socket = client
    private val reader: Scanner = Scanner(client.getInputStream())
    private val writer: OutputStream = client.getOutputStream()
    private val calculator: Calculator = Calculator()
    private var running: Boolean = false
    private val PQg: Triple<Long, Long, Long> = generatePQg(10000)
    private val N: Long = PQg.first * PQg.second
    private var dataKey: Long = 0
    private var clientX: Long = 0
    private var E: Long = 0

    fun run() {
        running = true
        // Welcome message
        /*write("Welcome to the server!\n" +
                "To Exit, write: 'EXIT'.\n" +
                "To use the calculator, input two numbers separated with a space and an operation in the ending\n" +
                "Example: 5 33 multi\n" +
                "Available operations: 'add', 'sub', 'div', 'multi'")
        write("N ${N}")*/
        write("N ${N}")

        while (running) {
            try {
                val text = reader.nextLine()
                if (text == "EXIT"){
                    shutdown()
                    continue
                }

                val values = text.split(' ')
                checkMessage(text)

            } catch (ex: Exception) {
                // TODO: Implement exception handling
                shutdown()
            } finally {

            }

        }
    }

    private fun write(message: String) {
        writer.write((message + "\n").toByteArray(Charset.defaultCharset()))
    }

    private fun shutdown() {
        running = false
        client.close()
        println("${client.inetAddress.hostAddress} closed the connection")
    }

    private fun checkMessage(message: String) {
        val check = message.split(' ')
        when (check[0]) {
            "V" -> {
                this.dataKey = check[1].toLong()
                println("Регистрация ключа клиента: ${this.dataKey}")
                write("R")
            }
            "X" -> {
                this.clientX = check[1].toLong()
                this.E = generateE()
                write("E ${this.E}")
            }
            "Y" -> {
                if(checkY(check[1].toLong(), this.N, this.clientX, this.dataKey, this.E)) {
                    write("Авторизация прошла успешно")
                } else write("Авторизация не прошла")

            }
        }
    }

    private fun generateE() = (0 until 2).random().toLong()

    private fun checkY(Y: Long, N: Long, X: Long, V: Long, E: Long): Boolean {
        var result: Long = modularPow(Y, 2, N)
        when(E) {
            0.toLong() -> return result == X
            1.toLong() -> return result == modularPow(X * V, 1, N)
        }
        return false
    }

}