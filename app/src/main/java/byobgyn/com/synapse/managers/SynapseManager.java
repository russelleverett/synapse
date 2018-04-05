package byobgyn.com.synapse.managers;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import byobgyn.com.synapse.entities.Bot;
import byobgyn.com.synapse.entities.Component;
import byobgyn.com.synapse.entities.Node;
import byobgyn.com.synapse.entities.Player;
import byobgyn.com.synapse.entities.enums.ComponentType;
import byobgyn.com.synapse.utils.JsonDoc;

public class SynapseManager {
    private static SynapseManager sInstance;
    private static int NUMBER_OF_CORES = Runtime.getRuntime().availableProcessors();

    private static final int KEEP_ALIVE_TIME = 1;
    private static final TimeUnit KEEP_ALIVE_TIME_UNIT = TimeUnit.SECONDS;

    private final BlockingQueue<Runnable> mNetworkQueue;
    private final ThreadPoolExecutor mNetworkThreadPool;

    public static final int ERROR = -1;
    public static final int BOT_LOAD_COMPLETE = 1;
    public static final int PLAYER_LOGIN = 2;
    public static final int UPGRADE_COMPLETE = 3;
    public static final int UPGRADE_OPTIONS = 4;
    public static final int PURCHASE_SLOT = 5;
    public static final int TRANSFER_OPTIONS = 6;
    public static final int TRANSFER_COMPLETE = 7;

    private String mUserId;
    private Handler mHandler;

    static SynapseManager getInstance() {
        return sInstance;
    }
    String getUserId() { return mUserId; }

    public static void init(String userId) {
        sInstance = new SynapseManager(userId);
    }

    private SynapseManager(String userId) {
        mUserId = userId;

        mHandler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {
                SynapseTask task = (SynapseTask)msg.obj;
                task.getTaskListener().TaskComplete(msg.what, task.getResponse());

                if(task.getResponse().has("player")) {
                    Player.initialize(task.getResponse().get("player"));
                }
            }
        };

        mNetworkQueue = new LinkedBlockingQueue<>();
        mNetworkThreadPool = new ThreadPoolExecutor(
            NUMBER_OF_CORES,
            NUMBER_OF_CORES,
            KEEP_ALIVE_TIME,
            KEEP_ALIVE_TIME_UNIT,
            mNetworkQueue
        );
    }

    void handleState(SynapseTask task, int state) {
        Message completeMsg = mHandler.obtainMessage(state, task);
        completeMsg.sendToTarget();
    }

    static public SynapseTask login(SynapseTaskListener listener) {
        SynapseTask task = new SynapseTask(listener, "POST", "http://synapsejs.azurewebsites.net/api/synapse/players", PLAYER_LOGIN);

        sInstance.mNetworkThreadPool.execute(task.getSynapseRunnable());
        return task;
    }

    static public SynapseTask loadBotList(SynapseTaskListener listener) {
        SynapseTask task = new SynapseTask(listener, "GET", "http://synapsejs.azurewebsites.net/api/synapse/bots", BOT_LOAD_COMPLETE);

        sInstance.mNetworkThreadPool.execute(task.getSynapseRunnable());
        return task;
    }

    static public SynapseTask getUpgradeOptions(SynapseTaskListener listener, Bot bot, ComponentType type) {
        String url = String.format("http://synapsejs.azurewebsites.net/api/synapse/bots/%s/upgrade/%s", bot.getId(), type.getValue());

        SynapseTask task = new SynapseTask(listener, "GET", url, UPGRADE_OPTIONS);
        sInstance.mNetworkThreadPool.execute(task.getSynapseRunnable());
        return task;
    }

    static public SynapseTask upgradeComponent(SynapseTaskListener listener, Bot bot, Component component) {
        String url = String.format("http://synapsejs.azurewebsites.net/api/synapse/bots/%s/upgrade/%s", bot.getId(), component.getComponentType().getValue());
        SynapseTask task = new SynapseTask(listener, "POST", url, UPGRADE_COMPLETE);
        JsonDoc purchaseRequest = new JsonDoc()
                .set("Id", component.getId())
                .set("Credits", component.getCredits())
                .set("Tags", component.getTags());
        task.setRequestBody(purchaseRequest.toString());

        sInstance.mNetworkThreadPool.execute(task.getSynapseRunnable());
        return task;
    }

    static public SynapseTask getTransferLocations(SynapseTaskListener listener, Bot bot) {
        String url = String.format("http://synapsejs.azurewebsites.net/api/synapse/bots/%s/transfer", bot.getId());
        SynapseTask task = new SynapseTask(listener, "GET", url, TRANSFER_OPTIONS);
        sInstance.mNetworkThreadPool.execute(task.getSynapseRunnable());
        return task;
    }

    static public SynapseTask transferBot(SynapseTaskListener listener, Bot bot, Node node) {
        String url = String.format("http://synapsejs.azurewebsites.net/api/synapse/bots/%s/transfer/%s", bot.getId(), node.getId());
        SynapseTask task = new SynapseTask(listener, "POST", url, TRANSFER_OPTIONS);
        sInstance.mNetworkThreadPool.execute(task.getSynapseRunnable());
        return task;
    }

    static public SynapseTask purchaseSlot(SynapseTaskListener listener) {
        SynapseTask task = new SynapseTask(listener, "POST", "http://synapsejs.azurewebsites.net/api/synapse/players/slots", PURCHASE_SLOT);
        sInstance.mNetworkThreadPool.execute(task.getSynapseRunnable());
        return task;
    }
}
