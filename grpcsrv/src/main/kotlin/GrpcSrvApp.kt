package ltd.evilcorp.rbtsrv.grpcsrv

fun main() {
    val server = GrpcServer()
    server.awaitDeath()
}
