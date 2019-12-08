import io.nats.client.*
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant
import java.util.concurrent.CompletableFuture

fun main() {
    val opcServer = OpcServer()
    val nameNode = opcServer.createStringNode("Name")

    val natsConnection: Connection = Nats.connect(
        Options.Builder().errorListener(object : ErrorListener {
            override fun errorOccurred(conn: Connection?, error: String?) = println("error: $error")
            override fun slowConsumerDetected(conn: Connection?, consumer: Consumer?) {}
            override fun exceptionOccurred(conn: Connection?, exp: Exception?) =
                println("exception: ${exp?.message}")
        }).build()
    )

    val dispatcher = natsConnection.createDispatcher {}
    dispatcher.subscribe("greeting") {
        nameNode.value = DataValue(Variant(String(it.data)))
    }

    val future = CompletableFuture<Void?>()
    Runtime.getRuntime().addShutdownHook(Thread(Runnable { future.complete(null) }))
    future.get()

    natsConnection.closeDispatcher(dispatcher)
    natsConnection.close()
    opcServer.shutdown()
}
