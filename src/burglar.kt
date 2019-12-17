fun main(args: Array<String>) {
    val address = "localhost"
    val port = 9999

    val client = Client(address, port, true)
    client.run()
}