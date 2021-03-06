package ltd.evilcorp.rbtsrv.grpcsrv

import io.grpc.ServerBuilder
import ltd.evilcorp.rbtsrv.common.NatsConnection

class GrpcServer {
    private val nats = NatsConnection()
    private val server: io.grpc.Server = ServerBuilder
        .forPort(9876)
        .addService(RobotServiceImpl(nats))
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
