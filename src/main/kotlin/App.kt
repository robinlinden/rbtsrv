import io.grpc.ServerBuilder
import io.grpc.stub.StreamObserver
import ltd.evilcorp.rbtsrv.GreeterGrpc
import ltd.evilcorp.rbtsrv.HelloReply
import ltd.evilcorp.rbtsrv.HelloRequest

class GreeterImpl : GreeterGrpc.GreeterImplBase() {
    override fun sayHello(r: HelloRequest, responseObserver: StreamObserver<HelloReply>) {
        val reply = HelloReply.newBuilder().setMessage("Hello ${r.name}.").build()
        responseObserver.onNext(reply)
        responseObserver.onCompleted()
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
