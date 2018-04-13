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
import byobgyn.com.synapse.entities.Mission;
import byobgyn.com.synapse.entities.Node;
import byobgyn.com.synapse.entities.Player;
import byobgyn.com.synapse.entities.enums.ComponentType;
import byobgyn.com.synapse.utils.JsonDoc;

public class SynapseManager {
    private static SynapseManager sInstance;
    private static int NUMBER_OF_CORES = Runtime.getRuntime().availableProcessors();

    private static final int KEEP_ALIVE_TIME = 1;
    private static final TimeUnit KEEP_ALIVE_TIME_UNIT = TimeUnit.SECONDS;

    private final ThreadPoolExecutor mNetworkThreadPool;

    public static final int BOT_LOAD_COMPLETE = 1;
    public static final int PLAYER_LOGIN = 2;
    public static final int UPGRADE_COMPLETE = 3;
    public static final int UPGRADE_OPTIONS = 4;
    public static final int PURCHASE_SLOT = 5;
    public static final int TRANSFER_OPTIONS = 6;
    public static final int TRANSFER_COMPLETE = 7;
    public static final int MISSION_OPTIONS = 8;
    public static final int MISSION_ACCEPTED = 9;
    public static final int MISSION_RESULTS = 10;
    public static final int BOT_REPAIRED = 11;
    public static final int BOT_PURCHASE_LIST = 12;

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

                if(task.getResponse() != null && task.getResponse().has("player")) {
                    Player.initialize(task.getResponse().get("player"));
                }
            }
        };

        BlockingQueue<Runnable> mNetworkQueue = new LinkedBlockingQueue<>();
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

    static private void queueTask(SynapseTaskListener listener, String method, String url, int messageId) {
        queueTask(listener, method, url, messageId, null);
    }
    static private void queueTask(SynapseTaskListener listener, String method, String url, int messageId, String requastBody) {
        SynapseTask task = new SynapseTask(listener, method, url, messageId);
        task.setRequestBody(requastBody);

        sInstance.mNetworkThreadPool.execute(task.getSynapseRunnable());
    }

    static public void login(SynapseTaskListener listener) {
        queueTask(listener, "POST", "http://synapsejs.azurewebsites.net/api/synapse/players", PLAYER_LOGIN);
    }

    static public void loadBotList(SynapseTaskListener listener) {
        queueTask(listener, "GET", "http://synapsejs.azurewebsites.net/api/synapse/bots", BOT_LOAD_COMPLETE);
    }

    static public void getUpgradeOptions(SynapseTaskListener listener, Bot bot, ComponentType type) {
        String url = String.format("http://synapsejs.azurewebsites.net/api/synapse/bots/%s/upgrade/%s", bot.getId(), type.getValue());
        queueTask(listener, "GET", url, UPGRADE_OPTIONS);
    }

    static public void upgradeComponent(SynapseTaskListener listener, Bot bot, Component component) {
        String url = String.format("http://synapsejs.azurewebsites.net/api/synapse/bots/%s/upgrade/%s", bot.getId(), component.getComponentType().getValue());
        String purchaseRequest = new JsonDoc()
                .set("Id", component.getId())
                .set("Credits", component.getCredits())
                .set("Tags", component.getTags())
                .toString();
        queueTask(listener, "POST", url, UPGRADE_COMPLETE, purchaseRequest);
    }

    static public void getTransferLocations(SynapseTaskListener listener, Bot bot, String filter) {
        String url = String.format("http://synapsejs.azurewebsites.net/api/synapse/bots/%s/transfer", bot.getId());
        if(filter != null) url += String.format("?filter=%s", filter);
        queueTask(listener, "GET", url, TRANSFER_OPTIONS);
    }

    static public void transferBot(SynapseTaskListener listener, Bot bot, Node node) {
        String url = String.format("http://synapsejs.azurewebsites.net/api/synapse/bots/%s/transfer/%s", bot.getId(), node.getId());
        queueTask(listener, "POST", url, TRANSFER_COMPLETE);
    }

    static public void getMissions(SynapseTaskListener listener, Bot bot) {
        String url = String.format("http://synapsejs.azurewebsites.net/api/synapse/missions/%s", bot.getNode().getId());
        queueTask(listener, "GET", url, MISSION_OPTIONS);
    }

    static public void acceptMission(SynapseTaskListener listener, Bot bot, Mission mission) {
        String url = String.format("http://synapsejs.azurewebsites.net/api/synapse/bots/%s/missions/%s", bot.getId(), mission.getIdentifier());
        queueTask(listener, "POST", url, MISSION_ACCEPTED);
    }

    static public void getMissionResults(SynapseTaskListener listener, Bot bot) {
        String url = String.format("http://synapsejs.azurewebsites.net/api/synapse/bots/%s/complete", bot.getId());
        queueTask(listener, "POST", url, MISSION_RESULTS);
    }

    static public void repairBot(SynapseTaskListener listener, Bot bot) {
        String url = String.format("http://synapsejs.azurewebsites.net/api/synapse/bots/%s/repair", bot.getId());
        queueTask(listener, "POST", url, BOT_REPAIRED);
    }

    static public void getBotPurchaseList(SynapseTaskListener listener) {
        queueTask(listener, "GET", "http://synapsejs.azurewebsites.net/api/synapse/purchase/bots", BOT_PURCHASE_LIST);
    }

    static public void purchaseSlot(SynapseTaskListener listener) {
        queueTask(listener, "POST", "http://synapsejs.azurewebsites.net/api/synapse/purchase/slots", PURCHASE_SLOT);
    }
}
