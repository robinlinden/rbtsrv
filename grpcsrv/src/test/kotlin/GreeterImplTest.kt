package ltd.evilcorp.rbtsrv.grpcsrv

import io.grpc.inprocess.InProcessChannelBuilder
import io.grpc.inprocess.InProcessServerBuilder
import io.mockk.mockk
import io.mockk.verify
import io.nats.client.Connection
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class GreeterImplTest {
    @Test
    fun `sends the correct response`() {
        val serverName = InProcessServerBuilder.generateName()
        val server = InProcessServerBuilder
            .forName(serverName)
            .directExecutor()
            .addService(GreeterImpl(mockk<Connection>(relaxUnitFun = true)))
            .build().start()

        val stub = GreeterGrpc.newBlockingStub(
            InProcessChannelBuilder.forName(serverName).directExecutor().build()
        )

        val reply = stub.sayHello(HelloRequest.newBuilder().setName("Robot").build())
        assertEquals("Hello Robot.", reply.message)

        server.shutdownNow()
    }

    @Test
    fun `publishes its input`() {
        val mock = mockk<Connection>(relaxUnitFun = true)

        val serverName = InProcessServerBuilder.generateName()
        val server = InProcessServerBuilder
            .forName(serverName)
            .directExecutor()
            .addService(GreeterImpl(mock))
            .build().start()

        val stub = GreeterGrpc.newBlockingStub(
            InProcessChannelBuilder.forName(serverName).directExecutor().build()
        )

        stub.sayHello(HelloRequest.newBuilder().setName("Robot").build())
        server.shutdownNow()

        verify { mock.publish("greeting", "Robot".toByteArray()) }
    }
}
