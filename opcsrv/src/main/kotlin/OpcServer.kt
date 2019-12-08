import org.eclipse.milo.opcua.sdk.server.OpcUaServer
import org.eclipse.milo.opcua.sdk.server.api.config.OpcUaServerConfig
import org.eclipse.milo.opcua.stack.server.EndpointConfiguration

private const val OPC_DEFAULT_PORT = 4840

class OpcServer {
    private val endpointConfiguration = EndpointConfiguration.newBuilder()
        .setBindPort(OPC_DEFAULT_PORT)
        .addTokenPolicy(OpcUaServerConfig.USER_TOKEN_POLICY_ANONYMOUS)
        .build()

    private val config = OpcUaServerConfig.builder().setEndpoints(setOf(endpointConfiguration)).build()
    private val server = OpcUaServer(config)
    private val namespace = SomeNamespace(server)

    fun start() {
        server.startup().get()
        namespace.startup()
    }
}