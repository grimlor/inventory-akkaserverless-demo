package com.lightbend.gsa.asdemo.inventory.domain;

import com.akkaserverless.javasdk.eventsourcedentity.CommandContext;
import com.google.protobuf.Empty;
import com.lightbend.gsa.asdemo.inventory.InventoryApi;
import com.lightbend.gsa.asdemo.inventory.InventoryApi.AddItemCommand;
import com.lightbend.gsa.asdemo.inventory.InventoryApi.GetItemCommand;
import com.lightbend.gsa.asdemo.inventory.InventoryApi.Item;
import com.lightbend.gsa.asdemo.inventory.domain.InventoryDomain.ItemAdded;

import org.junit.Test;
import org.mockito.*;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThrows;

public class ItemTest {
    private String entityId = "entityId1";
    private ItemImpl entity;
    private CommandContext context = Mockito.mock(CommandContext.class);
    
    private class MockedContextFailure extends RuntimeException {};
    
    @Test
    public void nonexistentEntityIdReturnsNull() {
        entity = new ItemImpl(entityId);

        var item = entity.getItem(GetItemCommand.newBuilder().setItemId(entityId).build(), context);
        assertThat("Item should not exist yet.", item, is(nullValue()));
    }
    
    @Test
    public void addItemReturnsEmptyAndEmitsItemAdded() {
        entity = new ItemImpl(entityId);

        var command = AddItemCommand.newBuilder()
                        .setItemId(entityId)
                        .setUserId("user1")
                        .setName("test")
                        .setDescription("Test item")
                        .setTradable(false)
                        .addTags("test1")
                        .addTags("test2")
                        .build();
        var item = entity.addItem(command, context);

        assertThat("Should be Empty", item, instanceOf(Empty.class));
        
        var expected = ItemAdded.newBuilder()
                        .setItemId(entityId)
                        .setUserId("user1")
                        .setName("test")
                        .setDescription("Test item")
                        .setTradable(false)
                        .addTags("test1")
                        .addTags("test2")
                        .build();
        Mockito.verify(context).emit(expected);
    }

    @Test
    public void getItemReturnsRequestedItem() {
        entity = new ItemImpl(entityId);

        // Simulate event callback to drive state change
        var itemAddedEvent = ItemAdded.newBuilder()
                        .setItemId(entityId)
                        .setUserId("user1")
                        .setName("test")
                        .setDescription("Test item")
                        .setTradable(false)
                        .addTags("test1")
                        .addTags("test2")
                        .build();
        entity.itemAdded(itemAddedEvent);

        var expected = Item.newBuilder()
                        .setItemId(entityId)
                        .setUserId("user1")
                        .setName("test")
                        .setDescription("Test item")
                        .setTradable(false)
                        .addTags("test1")
                        .addTags("test2")
                        .build();

        var getCommand = GetItemCommand.newBuilder()
                            .setItemId(entityId)
                            .build();
        var result = entity.getItem(getCommand, context);
        assertThat("Item should exist.", result, is(expected));
    }
    
    @Test
    public void deleteItemTest() {
        entity = new ItemImpl(entityId);
        
        Mockito.when(context.fail("The command handler for `DeleteItem` is not implemented, yet"))
            .thenReturn(new MockedContextFailure());
        
        // TODO: set fields in command, and update assertions to match implementation
        assertThrows(MockedContextFailure.class, () -> {
            entity.deleteItemWithReply(InventoryApi.DeleteItemCommand.newBuilder().build(), context);
        });
        
        // TODO: if you wish to verify events:
        //    Mockito.verify(context).emit(event);
    }
    
    @Test
    public void markTradableTest() {
        entity = new ItemImpl(entityId);
        
        Mockito.when(context.fail("The command handler for `MarkTradable` is not implemented, yet"))
            .thenReturn(new MockedContextFailure());
        
        // TODO: set fields in command, and update assertions to match implementation
        assertThrows(MockedContextFailure.class, () -> {
            entity.markTradableWithReply(InventoryApi.MarkTradableCommand.newBuilder().build(), context);
        });
        
        // TODO: if you wish to verify events:
        //    Mockito.verify(context).emit(event);
    }
    
    @Test
    public void markNotTradableTest() {
        entity = new ItemImpl(entityId);
        
        Mockito.when(context.fail("The command handler for `MarkNotTradable` is not implemented, yet"))
            .thenReturn(new MockedContextFailure());
        
        // TODO: set fields in command, and update assertions to match implementation
        assertThrows(MockedContextFailure.class, () -> {
            entity.markNotTradableWithReply(InventoryApi.MarkNotTradableCommand.newBuilder().build(), context);
        });
        
        // TODO: if you wish to verify events:
        //    Mockito.verify(context).emit(event);
    }
    
    @Test
    public void changeOwnerTest() {
        entity = new ItemImpl(entityId);
        
        Mockito.when(context.fail("The command handler for `ChangeOwner` is not implemented, yet"))
            .thenReturn(new MockedContextFailure());
        
        // TODO: set fields in command, and update assertions to match implementation
        assertThrows(MockedContextFailure.class, () -> {
            entity.changeOwnerWithReply(InventoryApi.ChangeOwnerCommand.newBuilder().build(), context);
        });
        
        // TODO: if you wish to verify events:
        //    Mockito.verify(context).emit(event);
    }
    
    @Test
    public void startTradeTest() {
        entity = new ItemImpl(entityId);
        
        Mockito.when(context.fail("The command handler for `StartTrade` is not implemented, yet"))
            .thenReturn(new MockedContextFailure());
        
        // TODO: set fields in command, and update assertions to match implementation
        assertThrows(MockedContextFailure.class, () -> {
            entity.startTradeWithReply(InventoryApi.StartTradeCommand.newBuilder().build(), context);
        });
        
        // TODO: if you wish to verify events:
        //    Mockito.verify(context).emit(event);
    }
    
    @Test
    public void cancelTradeTest() {
        entity = new ItemImpl(entityId);
        
        Mockito.when(context.fail("The command handler for `CancelTrade` is not implemented, yet"))
            .thenReturn(new MockedContextFailure());
        
        // TODO: set fields in command, and update assertions to match implementation
        assertThrows(MockedContextFailure.class, () -> {
            entity.cancelTradeWithReply(InventoryApi.CancelTradeCommand.newBuilder().build(), context);
        });
        
        // TODO: if you wish to verify events:
        //    Mockito.verify(context).emit(event);
    }
}