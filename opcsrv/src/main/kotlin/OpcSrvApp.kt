import java.util.concurrent.CompletableFuture

fun main() {
    val opcServer = OpcServer()
    opcServer.start()

    val future = CompletableFuture<Void?>()
    Runtime.getRuntime().addShutdownHook(Thread(Runnable { future.complete(null) }))
    future.get()
}
