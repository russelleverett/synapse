package byobgyn.com.synapse.managers;

import byobgyn.com.synapse.utils.JsonDoc;

public interface SynapseTaskListener {
    void TaskComplete(int messageId, JsonDoc response);
}
