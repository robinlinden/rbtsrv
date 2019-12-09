package ltd.evilcorp.rbtsrv.common

import io.nats.client.*

class NatsConnection(
    private val delegate: Connection = Nats.connect(
        Options.Builder().errorListener(object : ErrorListener {
            override fun errorOccurred(conn: Connection?, error: String?) = println("error: $error")
            override fun slowConsumerDetected(conn: Connection?, consumer: Consumer?) = println("slow: $consumer")
            override fun exceptionOccurred(conn: Connection?, e: Exception?) = println("exception: $e")
        }).build()
    )
) : Connection by delegate
