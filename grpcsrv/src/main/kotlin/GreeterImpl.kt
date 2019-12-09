package ltd.evilcorp.rbtsrv.grpcsrv

import io.grpc.stub.StreamObserver
import ltd.evilcorp.rbtsrv.common.NatsConnection

class GreeterImpl : GreeterGrpc.GreeterImplBase() {
    private val nats = NatsConnection()

    override fun sayHello(r: HelloRequest, responseObserver: StreamObserver<HelloReply>) {
        val reply = HelloReply.newBuilder().setMessage("Hello ${r.name}.").build()
        responseObserver.onNext(reply)
        responseObserver.onCompleted()

        nats.publish("greeting", r.name.toByteArray())
    }
}
