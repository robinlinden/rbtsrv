package ltd.evilcorp.rbtsrv.grpcsrv

import io.grpc.stub.StreamObserver
import io.nats.client.Connection

class RobotServiceImpl(private val nats: Connection) : RobotServiceGrpc.RobotServiceImplBase() {
    override fun add(r: RobotInfo, responseObserver: StreamObserver<Nothing>) {
        responseObserver.onNext(Nothing.newBuilder().build())
        responseObserver.onCompleted()
        nats.publish("robots", r.id.toByteArray())
    }
}
