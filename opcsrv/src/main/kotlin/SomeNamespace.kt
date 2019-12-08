import org.eclipse.milo.opcua.sdk.core.AccessLevel
import org.eclipse.milo.opcua.sdk.core.Reference
import org.eclipse.milo.opcua.sdk.server.OpcUaServer
import org.eclipse.milo.opcua.sdk.server.api.DataItem
import org.eclipse.milo.opcua.sdk.server.api.ManagedNamespace
import org.eclipse.milo.opcua.sdk.server.api.MonitoredItem
import org.eclipse.milo.opcua.sdk.server.nodes.UaFolderNode
import org.eclipse.milo.opcua.sdk.server.nodes.UaVariableNode
import org.eclipse.milo.opcua.sdk.server.util.SubscriptionModel
import org.eclipse.milo.opcua.stack.core.Identifiers
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned

class SomeNamespace(server: OpcUaServer) : ManagedNamespace(server, "evilcorp:rbtsrv") {
    private val subscriptionModel = SubscriptionModel(server, this)

    override fun onStartup() {
        super.onStartup()

        val folderNodeId: NodeId = newNodeId("Test")

        val folderNode = UaFolderNode(
            nodeContext,
            folderNodeId,
            newQualifiedName("Test"),
            LocalizedText.english("Test")
        )

        nodeManager.addNode(folderNode)

        folderNode.addReference(
            Reference(
                folderNode.nodeId,
                Identifiers.Organizes,
                Identifiers.ObjectsFolder.expanded(),
                false
            )
        )

        val name = "Name"
        val variant = Variant("Robotto")
        val node = UaVariableNode.UaVariableNodeBuilder(nodeContext)
            .setNodeId(newNodeId("Test/$name"))
            .setAccessLevel(Unsigned.ubyte(AccessLevel.getMask(AccessLevel.READ_WRITE)))
            .setUserAccessLevel(Unsigned.ubyte(AccessLevel.getMask(AccessLevel.READ_WRITE)))
            .setBrowseName(newQualifiedName(name))
            .setDisplayName(LocalizedText.english(name))
            .setDataType(Identifiers.String)
            .setTypeDefinition(Identifiers.BaseDataVariableType)
            .build().apply {
                value = DataValue(variant)
            }
        nodeManager.addNode(node)
        folderNode.addOrganizes(node)
    }

    override fun onDataItemsDeleted(dataItems: MutableList<DataItem>?) =
        subscriptionModel.onDataItemsDeleted(dataItems)

    override fun onMonitoringModeChanged(monitoredItems: MutableList<MonitoredItem>?) =
        subscriptionModel.onMonitoringModeChanged(monitoredItems)

    override fun onDataItemsCreated(dataItems: MutableList<DataItem>?) =
        subscriptionModel.onDataItemsCreated(dataItems)

    override fun onDataItemsModified(dataItems: MutableList<DataItem>?) =
        subscriptionModel.onDataItemsModified(dataItems)
}
