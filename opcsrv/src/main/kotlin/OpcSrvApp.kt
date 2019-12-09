import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant
import java.util.concurrent.CompletableFuture

fun main() {
    val opcServer = OpcServer()
    val nameNode = opcServer.createStringNode("Name")

    val nats = NatsConnection()
    val dispatcher = nats.createDispatcher {}
    dispatcher.subscribe("greeting") {
        nameNode.value = DataValue(Variant(String(it.data)))
    }

    val future = CompletableFuture<Void?>()
    Runtime.getRuntime().addShutdownHook(Thread(Runnable { future.complete(null) }))
    future.get()

    nats.closeDispatcher(dispatcher)
    nats.close()
    opcServer.shutdown()
}
