package ltd.evilcorp.rbtsrv.grpcsrv

import io.grpc.stub.StreamObserver
import io.nats.client.Connection

class GreeterImpl(private val nats: Connection) : GreeterGrpc.GreeterImplBase() {
    override fun sayHello(r: HelloRequest, responseObserver: StreamObserver<HelloReply>) {
        val reply = HelloReply.newBuilder().setMessage("Hello ${r.name}.").build()
        responseObserver.onNext(reply)
        responseObserver.onCompleted()

        nats.publish("greeting", r.name.toByteArray())
    }
}
