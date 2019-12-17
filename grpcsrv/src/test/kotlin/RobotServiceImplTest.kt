package ltd.evilcorp.rbtsrv.grpcsrv

import io.grpc.inprocess.InProcessChannelBuilder
import io.grpc.inprocess.InProcessServerBuilder
import io.mockk.mockk
import io.mockk.verifySequence
import io.nats.client.Connection
import org.junit.jupiter.api.Test

class RobotServiceImplTest {
    @Test
    fun `publishes its input`() {
        val mock = mockk<Connection>(relaxUnitFun = true)

        val serverName = InProcessServerBuilder.generateName()
        val server = InProcessServerBuilder
            .forName(serverName)
            .directExecutor()
            .addService(RobotServiceImpl(mock))
            .build().start()

        val stub = RobotServiceGrpc.newBlockingStub(
            InProcessChannelBuilder.forName(serverName).directExecutor().build()
        )

        stub.add(RobotInfo.newBuilder().setId("Robot 0").build())
        stub.add(RobotInfo.newBuilder().setId("not a real robot").build())
        stub.add(RobotInfo.newBuilder().setId("Robot 42").build())
        server.shutdownNow()

        verifySequence {
            mock.publish("robots", "Robot 0".toByteArray())
            mock.publish("robots", "not a real robot".toByteArray())
            mock.publish("robots", "Robot 42".toByteArray())
        }
    }
}
