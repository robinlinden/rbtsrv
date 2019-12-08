import io.grpc.stub.StreamObserver
import io.nats.client.*
import ltd.evilcorp.rbtsrv.grpcsrv.GreeterGrpc
import ltd.evilcorp.rbtsrv.grpcsrv.HelloReply
import ltd.evilcorp.rbtsrv.grpcsrv.HelloRequest

class GreeterImpl : GreeterGrpc.GreeterImplBase() {
    private val natsConnection: Connection =
        Nats.connect(
            Options.Builder().errorListener(object : ErrorListener {
                override fun errorOccurred(conn: Connection?, error: String?) = println("error: $error")
                override fun slowConsumerDetected(conn: Connection?, consumer: Consumer?) {}
                override fun exceptionOccurred(conn: Connection?, exp: Exception?) =
                    println("exception: ${exp?.message}")
            }).build()
        )

    override fun sayHello(r: HelloRequest, responseObserver: StreamObserver<HelloReply>) {
        val reply = HelloReply.newBuilder().setMessage("Hello ${r.name}.").build()
        responseObserver.onNext(reply)
        responseObserver.onCompleted()

        natsConnection.publish("greeting", r.name.toByteArray())
    }
}
