package ltd.evilcorp.rbtsrv.grpcsrv

fun main() {
    val server = GreeterServer()
    server.awaitDeath()
}
