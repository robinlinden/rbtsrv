package ltd.evilcorp.rbtsrv.opcsrv

import ltd.evilcorp.rbtsrv.common.NatsConnection
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant
import java.util.concurrent.CompletableFuture

fun main() {
    val opcServer = OpcServer().apply {
        createFolder("Robots")
    }

    val nats = NatsConnection()
    val dispatcher = nats.createDispatcher {}

    dispatcher.subscribe("robots") {
        val robotFolder = "Robots/${String(it.data)}"
        opcServer.createFolder(robotFolder)
        val robotIdNode = opcServer.createStringNode("${robotFolder}/Id")
        robotIdNode.value = DataValue(Variant(String(it.data)))
    }

    val future = CompletableFuture<Void?>()
    Runtime.getRuntime().addShutdownHook(Thread(Runnable { future.complete(null) }))
    future.get()

    nats.closeDispatcher(dispatcher)
    nats.close()
    opcServer.shutdown()
}
