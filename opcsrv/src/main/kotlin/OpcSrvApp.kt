import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant
import java.util.concurrent.CompletableFuture

fun main() {
    val opcServer = OpcServer().apply {
        createFolder("Test")
        createFolder("Test/SomeNode")
        createFolder("Test/Test")
        createFolder("Test/Test/MoreTest")
        createStringNode("TopLevelNode").value = DataValue(Variant("Root node"))
        createStringNode("Test/Name").value = DataValue(Variant("Robotto"))
        createStringNode("Test/Test/MoreTest/Magic").value = DataValue(Variant("beep beep boop"))
    }

    val future = CompletableFuture<Void?>()
    Runtime.getRuntime().addShutdownHook(Thread(Runnable { future.complete(null) }))
    future.get()

    opcServer.shutdown()
}
