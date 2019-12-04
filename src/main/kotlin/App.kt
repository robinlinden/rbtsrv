import io.grpc.ServerBuilder
import io.grpc.stub.StreamObserver
import io.nats.client.*
import ltd.evilcorp.rbtsrv.GreeterGrpc
import ltd.evilcorp.rbtsrv.HelloReply
import ltd.evilcorp.rbtsrv.HelloRequest
import java.lang.Exception

class GreeterImpl : GreeterGrpc.GreeterImplBase() {
    private val natsConnection: Connection = Nats.connect(
        Options.Builder().errorListener(object : ErrorListener {
            override fun errorOccurred(conn: Connection?, error: String?) = println("error: $error")
            override fun exceptionOccurred(conn: Connection?, exp: Exception?) = println("exception: ${exp?.message}")
            override fun slowConsumerDetected(conn: Connection?, consumer: Consumer?) {}
        }).build()
    )

    override fun sayHello(r: HelloRequest, responseObserver: StreamObserver<HelloReply>) {
        val reply = HelloReply.newBuilder().setMessage("Hello ${r.name}.").build()
        responseObserver.onNext(reply)
        responseObserver.onCompleted()

        natsConnection.publish("greeting", r.name.toByteArray())
    }
}

class GreeterServer {
    private val server: io.grpc.Server = ServerBuilder
        .forPort(9876)
        .addService(GreeterImpl())
        .build()
        .start()

    init {
        Runtime.getRuntime().addShutdownHook(object : Thread() {
            override fun run() {
                server.shutdown()
            }
        })
    }

    fun awaitDeath() = server.awaitTermination()
}

fun main() {
    val server = GreeterServer()
    server.awaitDeath()
}
