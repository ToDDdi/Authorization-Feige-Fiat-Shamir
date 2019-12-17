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
    private var running: Boolean = false
    private val PQg: Triple<Long, Long, Long> = generatePQg(10000)
    private val N: Long = PQg.first * PQg.second
    private var dataKey: Long = 0
    private var clientX: Long = 0
    private var E: Long = 0
    private var level: Long = 0
    private val levelAuthorization: Long = 4

    fun run() {
        running = true

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
                for(i in 0..levelAuthorization) {
                    this.E = generateE()
                    write("E ${this.E} ${i + 1}")
                }
            }
            "Y" -> {
                    this.E = check[2].toLong()
                    if (checkY(check[1].toLong(), this.N, this.clientX, this.dataKey, this.E)) {
                        println("Тест №${this.level + 1} Прошёл успешно из ${this.levelAuthorization + 1}")
                        level++
                    } else {
                        write("Авторизация не прошла, соединение закрыто")
                        println("Пользователь не прошёл проверку, соединение закрыто")
                        write("exit")
                    }
                    if (level == levelAuthorization + 1) {
                        write("Вы авторизировались")
                        println("Пользователь авторизировался")
                    }
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