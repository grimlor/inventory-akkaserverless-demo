package com.lightbend.gsa.asdemo.inventory.domain;

import com.akkaserverless.javasdk.testkit.junit.AkkaServerlessTestkitResource;
import com.google.protobuf.Empty;
import com.lightbend.gsa.asdemo.inventory.Main;
import com.lightbend.gsa.asdemo.inventory.InventoryApi.*;
import com.lightbend.gsa.asdemo.inventory.InventoryServiceClient;

import org.junit.ClassRule;
import org.junit.Test;

import static java.util.concurrent.TimeUnit.*;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;

// Example of an integration test calling our service via the Akka Serverless proxy
// Run all test classes ending with "IntegrationTest" using `mvn verify -Pit`
public class ItemIntegrationTest {
    
    /**
     * The test kit starts both the service container and the Akka Serverless proxy.
     */
    @ClassRule
    public static final AkkaServerlessTestkitResource testkit = new AkkaServerlessTestkitResource(Main.SERVICE);
    
    /**
     * Use the generated gRPC client to call the service through the Akka Serverless proxy.
     */
    private final InventoryServiceClient client;
    
    public ItemIntegrationTest() {
        client = InventoryServiceClient.create(testkit.getGrpcClientSettings(), testkit.getActorSystem());
    }

    static Throwable exceptionOf(Callable<?> callable) {
        try {
            callable.call();
            return null;
        } catch (Throwable t) {
            return t;
        }
    }

    Item getItem(String itemId) throws Exception {
        return client.getItem(GetItemCommand.newBuilder().setItemId(itemId).build())
            .toCompletableFuture().get(30, SECONDS);
    }

    Empty addItem(String itemId, String userId, String name, String description, boolean tradable, String tradeId) throws Exception {
        return client.addItem(
            AddItemCommand.newBuilder()
                            .setItemId(itemId)
                            .setUserId(userId)
                            .setName(name)
                            .setDescription(description)
                            .setTradable(tradable)
                            .setTradeId(tradeId)
                            .build()
        ).toCompletableFuture().get(30, SECONDS);
    }

    String createTestItem(boolean tradable, String itemId) throws Exception {
        createTestItem(tradable, itemId, null);

        return itemId;
    }

    String createTestItem(boolean tradable, String itemId, String tradeId) throws Exception {
        if (tradeId == null) {
            tradeId = "";
        }
        var userId = "user1";
        var name = "test";
        var description = "test item";
        addItem(itemId, userId, name, description, tradable, tradeId);

        return itemId;
    }

    Empty deleteItem(String itemId) throws Exception {
        return client.deleteItem(DeleteItemCommand.newBuilder().setItemId(itemId).build())
            .toCompletableFuture().get(30, SECONDS);
    }

    Empty markTradable(String itemId) throws Exception {
        return client.markTradable(MarkTradableCommand.newBuilder().setItemId(itemId).build())
            .toCompletableFuture().get(30, SECONDS);
    }

    Empty markNotTradable(String itemId) throws Exception {
        return client.markNotTradable(MarkNotTradableCommand.newBuilder().setItemId(itemId).build())
            .toCompletableFuture().get(30, SECONDS);
    }

    Empty changeOwner(String itemId, String userId) throws Exception {
        return client.changeOwner(ChangeOwnerCommand.newBuilder().setItemId(itemId).setUserId(userId).build())
            .toCompletableFuture().get(30, SECONDS);
    }

    Empty startTrade(String itemId, String tradeId) throws Exception {
        return client.startTrade(StartTradeCommand.newBuilder().setItemId(itemId).setTradeId(tradeId).build())
            .toCompletableFuture().get(30, SECONDS);
    }

    Empty cancelTrade(String itemId, String tradeId, boolean tradable) throws Exception {
        return client.cancelTrade(CancelTradeCommand.newBuilder().setItemId(itemId).setTradeId(tradeId).setTradable(tradable).build())
            .toCompletableFuture().get(30, SECONDS);
    }
    
    @Test
    public void getItemOnNonExistingEntity() throws Exception {
        assertThat(exceptionOf(() -> getItem("nonexistent")), instanceOf(ExecutionException.class));
    }
    
    @Test
    public void addItemOnNonExistingEntity() throws Exception {
        var tradable = false;
        var itemId = createTestItem(tradable, "addItem");
        var userId = "user1";
        var name = "test";
        var description = "test item";

        var expected = Item.newBuilder()
                            .setItemId(itemId)
                            .setUserId(userId)
                            .setName(name)
                            .setDescription(description)
                            .setTradable(tradable)
                            .build();
        assertThat(getItem(itemId), is(expected));
    }
    
    @Test
    public void deleteItemOnNonExistingEntity() throws Exception {
        // completes without throwing
        assertThat(exceptionOf(() -> deleteItem("deleteItem")), is(nullValue()));
    }

    @Test
    public void deleteItemOnExistingItem() throws Exception {
        var itemId = createTestItem(false, "deleteItem");

        deleteItem(itemId);
        assertThat(exceptionOf(() -> getItem(itemId)), instanceOf(ExecutionException.class));
    }

    @Test
    public void markTradableOnNonExistingEntity() throws Exception {
        // completes without throwing
        assertThat(exceptionOf(() -> markTradable("tradable")), is(nullValue()));
    }
    
    @Test
    public void markTradableOnExitingItem() throws Exception {
        var itemId = createTestItem(false, "markTradable");

        markTradable(itemId);
        var item = getItem(itemId);
        assertThat(item.getItemId(), is(itemId));
        assertThat(item.getTradable(), is(true));
    }

    @Test
    public void markNotTradableOnNonExistingEntity() throws Exception {
        // completes without throwing
        assertThat(exceptionOf(() -> markTradable("nottradable")), is(nullValue()));
    }
    
    @Test
    public void markNotTradableOnExistingEntity() throws Exception {
        var itemId = createTestItem(true, "markNotTradable");

        markNotTradable(itemId);
        var item = getItem(itemId);
        assertThat(item.getItemId(), is(itemId));
        assertThat(item.getTradable(), is(false));
    }

    @Test
    public void changeOwnerOnNonExistingEntity() throws Exception {
        // completes without throwing
        assertThat(exceptionOf(() -> changeOwner("changeOwner", "newOwner")), is(nullValue()));
    }

    @Test
    public void changeOwnerOnExistingEntity() throws Exception {
        var itemId = createTestItem(false, "changeOwner");
        var item = getItem(itemId);
        assertThat(item.getUserId(), is("user1"));

        var newOwner = "newOwner";
        changeOwner(itemId, newOwner);

        item = getItem(itemId);
        assertThat(item.getItemId(), is(itemId));
        assertThat(item.getUserId(), is(newOwner));
    }
    
    @Test
    public void startTradeOnNonExistingEntity() throws Exception {
        // completes without throwing
        assertThat(exceptionOf(() -> startTrade("startTrade", "tradeId")), is(nullValue()));
    }

    @Test
    public void startTradeOnExistingEntity() throws Exception {
        var itemId = createTestItem(true, "startTrade");
        var item = getItem(itemId);
        assertThat(item.getTradeId(), is(Empty.getDefaultInstance().toString()));
        assertThat(item.getTradable(), is(true));

        var tradeId = "tradeId";
        startTrade(itemId, tradeId);

        item = getItem(itemId);
        assertThat(item.getItemId(), is(itemId));
        assertThat(item.getTradable(), is(false));
        assertThat(item.getTradeId(), is(tradeId));
    }
    
    @Test
    public void cancelTradeOnNonExistingEntity() throws Exception {
        // completes without throwing
        assertThat(exceptionOf(() -> cancelTrade("cancelTrade", "tradeId", false)), is(nullValue()));
    }

    @Test
    public void cancelTradeOnExistingEntity() throws Exception {
        var tradeId = "tradeId";
        var itemId = createTestItem(false, "cancelTrade", tradeId);
        var item = getItem(itemId);
        assertThat(item.getTradeId(), is(tradeId));
        assertThat(item.getTradable(), is(false));

        cancelTrade(itemId, tradeId, true);
        
        item = getItem(itemId);
        assertThat(item.getItemId(), is(itemId));
        assertThat(item.getTradable(), is(true));
        assertThat(item.getTradeId(), is(Empty.getDefaultInstance().toString()));
    }
}