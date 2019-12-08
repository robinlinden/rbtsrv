import org.eclipse.milo.opcua.sdk.core.Reference
import org.eclipse.milo.opcua.sdk.server.OpcUaServer
import org.eclipse.milo.opcua.sdk.server.api.DataItem
import org.eclipse.milo.opcua.sdk.server.api.ManagedNamespace
import org.eclipse.milo.opcua.sdk.server.api.MonitoredItem
import org.eclipse.milo.opcua.sdk.server.nodes.UaFolderNode
import org.eclipse.milo.opcua.sdk.server.nodes.UaNode
import org.eclipse.milo.opcua.sdk.server.nodes.UaVariableNode
import org.eclipse.milo.opcua.sdk.server.util.SubscriptionModel
import org.eclipse.milo.opcua.stack.core.Identifiers
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId

class SomeNamespace(server: OpcUaServer) : ManagedNamespace(server, "urn:evilcorp:rbtsrv") {
    private val subscriptionModel = SubscriptionModel(server, this)

    fun createFolder(nodePath: String) {
        val segments = nodePath.split('/')
        val nodeName = segments.last()
        val folderNodeId: NodeId = newNodeId(nodePath)
        val folderNode = UaFolderNode(
            nodeContext,
            folderNodeId,
            newQualifiedName(nodeName),
            LocalizedText.english(nodeName)
        )

        addNode(folderNode)
    }

    fun createStringNode(nodeId: String): UaVariableNode {
        val segments = nodeId.split('/')
        val nodeName = segments.last()

        val node = UaVariableNode.UaVariableNodeBuilder(nodeContext)
            .setNodeId(newNodeId(nodeId))
            .setBrowseName(newQualifiedName(nodeName))
            .setDisplayName(LocalizedText.english(nodeName))
            .setDataType(Identifiers.String)
            .setTypeDefinition(Identifiers.BaseDataVariableType)
            .build()

        addNode(node)
        return node
    }

    private fun addNode(node: UaNode) {
        nodeManager.addNode(node)
        val segments = node.nodeId.identifier.toString().split('/')

        val parentNode = if (segments.size > 1) {
            newNodeId(segments.dropLast(1).reduce { acc, s -> "$acc/$s" }).expanded()
        } else {
            Identifiers.ObjectsFolder.expanded()
        }

        node.addReference(Reference(node.nodeId, Identifiers.Organizes, parentNode, false))
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
