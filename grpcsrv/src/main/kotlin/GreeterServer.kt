package ltd.evilcorp.rbtsrv.grpcsrv

import io.grpc.ServerBuilder

class GreeterServer {
    private val server: io.grpc.Server = ServerBuilder
        .forPort(9876)
        .addService(GreeterImpl())
        .build().start()

    init {
        Runtime.getRuntime().addShutdownHook(object : Thread() {
            override fun run() {
                server.shutdown()
            }
        })
    }

    fun awaitDeath() = server.awaitTermination()
}
