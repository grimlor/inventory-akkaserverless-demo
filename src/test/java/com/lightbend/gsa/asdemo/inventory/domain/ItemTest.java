package com.lightbend.gsa.asdemo.inventory.domain;

import com.akkaserverless.javasdk.eventsourcedentity.CommandContext;
import com.google.protobuf.Empty;
import com.lightbend.gsa.asdemo.inventory.InventoryApi.*;
import com.lightbend.gsa.asdemo.inventory.domain.InventoryDomain.*;

import org.junit.Test;
import org.mockito.*;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThrows;

public class ItemTest {
    private String entityId = "entityId1";
    private String itemId = "item1";
    private ItemImpl entity;
    private CommandContext context = Mockito.mock(CommandContext.class);
    
    private class MockedContextFailure extends RuntimeException {};
    
    @Test
    public void nonexistentEntityIdReturnsNull() {
        entity = new ItemImpl(entityId);

        var item = entity.getItem(GetItemCommand.newBuilder().setItemId(itemId).build(), context);
        assertThat("Item should not exist yet.", item, is(nullValue()));
    }
    
    @Test
    public void addItemReturnsEmptyAndEmitsItemAdded() {
        entity = new ItemImpl(entityId);

        var command = AddItemCommand.newBuilder()
                        .setItemId(itemId)
                        .setUserId("user1")
                        .setName("test")
                        .setDescription("Test item")
                        .setTradable(false)
                        .addTags("test1")
                        .addTags("test2")
                        .build();
        var result = entity.addItem(command, context);
        assertThat("Should be Empty", result, instanceOf(Empty.class));
        
        var expected = ItemAdded.newBuilder()
                        .setItemId(command.getItemId())
                        .setUserId(command.getUserId())
                        .setName(command.getName())
                        .setDescription(command.getDescription())
                        .setTradable(command.getTradable())
                        .addAllTags(command.getTagsList())
                        .build();
        Mockito.verify(context).emit(expected);

        // Simulate event callback to drive state change
        entity.itemAdded(expected);
        var getItemCommand = GetItemCommand.newBuilder()
                                .setItemId(expected.getItemId())
                                .build();
        
        var item = entity.getItem(getItemCommand, context);
        assertThat("Item should exist", item, is(notNullValue()));
        assertThat(item.getItemId(), is(expected.getItemId()));
        assertThat(item.getUserId(), is(expected.getUserId()));
        assertThat(item.getName(), is(expected.getName()));
        assertThat(item.getDescription(), is(expected.getDescription()));
        assertThat(item.getTradable(), is(expected.getTradable()));
        assertThat(item.getTagsList(), is(expected.getTagsList()));
    }

    @Test
    public void getItemReturnsRequestedItem() {
        entity = new ItemImpl(entityId);

        // Simulate event callback to drive state change
        var itemAddedEvent = ItemAdded.newBuilder()
                        .setItemId(itemId)
                        .setUserId("user1")
                        .setName("test")
                        .setDescription("Test item")
                        .setTradable(false)
                        .addTags("test1")
                        .addTags("test2")
                        .build();
        entity.itemAdded(itemAddedEvent);

        var expected = Item.newBuilder()
                        .setItemId(itemAddedEvent.getItemId())
                        .setUserId(itemAddedEvent.getUserId())
                        .setName(itemAddedEvent.getName())
                        .setDescription(itemAddedEvent.getDescription())
                        .setTradable(itemAddedEvent.getTradable())
                        .addAllTags(itemAddedEvent.getTagsList())
                        .build();

        var getItemCommand = GetItemCommand.newBuilder()
                            .setItemId(itemAddedEvent.getItemId())
                            .build();
        var result = entity.getItem(getItemCommand, context);
        assertThat("Item should exist.", result, is(expected));
        assertThat(result.getItemId(), is(expected.getItemId()));
        assertThat(result.getUserId(), is(expected.getUserId()));
        assertThat(result.getName(), is(expected.getName()));
        assertThat(result.getDescription(), is(expected.getDescription()));
        assertThat(result.getTradable(), is(expected.getTradable()));
        assertThat(result.getTagsList(), is(expected.getTagsList()));
    }
    
    @Test
    public void deleteItemReturnsEmptyAndEmitsItemDeleted() {
        entity = new ItemImpl(entityId);
        
        // Simulate event callback to drive state change
        var itemAddedEvent = ItemAdded.newBuilder()
                        .setItemId(itemId)
                        .setUserId("user1")
                        .setName("test")
                        .setDescription("Test item")
                        .setTradable(false)
                        .addTags("test1")
                        .addTags("test2")
                        .build();
        entity.itemAdded(itemAddedEvent);

        var deleteItemCommand = DeleteItemCommand.newBuilder()
                                .setItemId(itemAddedEvent.getItemId())
                                .build();
        var result = entity.deleteItem(deleteItemCommand, context);
        assertThat("Should be Empty", result, instanceOf(Empty.class));
        
        var expected = ItemDeleted.newBuilder()
                        .setItemId(deleteItemCommand.getItemId())
                        .build();
        Mockito.verify(context).emit(expected);

        // Simulate event callback to drive state change
        entity.itemDeleted(expected);
        var getItemCommand = GetItemCommand.newBuilder()
                                .setItemId(expected.getItemId())
                                .build();
        
        var item = entity.getItem(getItemCommand, context);
        assertThat("Item should no longer exist", item, is(nullValue()));
    }
    
    @Test
    public void markTradableReturnsEmptyAndEmitsMarkedTradable() {
        entity = new ItemImpl(entityId);
        
        // Simulate event callback to drive state change
        var itemAddedEvent = ItemAdded.newBuilder()
                        .setItemId(itemId)
                        .setUserId("user1")
                        .setName("test")
                        .setDescription("Test item")
                        .setTradable(false)
                        .addTags("test1")
                        .addTags("test2")
                        .build();
        entity.itemAdded(itemAddedEvent);

        var getItemCommand = GetItemCommand.newBuilder()
                            .setItemId(itemAddedEvent.getItemId())
                            .build();
        var item = entity.getItem(getItemCommand, context);
        assertThat("Should be marked not tradable", item.getTradable(), is(false));

        var markTradableCommand = MarkTradableCommand.newBuilder()
                                    .setItemId(item.getItemId())
                                    .build();
        var result = entity.markTradable(markTradableCommand, context);
        assertThat("Should be Empty", result, instanceOf(Empty.class));
        
        var expected = MarkedTradable.newBuilder()
                        .setItemId(markTradableCommand.getItemId())
                        .build();
        Mockito.verify(context).emit(expected);

        // Simulate event callback to drive state change
        entity.markedTradable(expected);

        item = entity.getItem(getItemCommand, context);
        assertThat("Should be marked tradable", item.getTradable(), is(true));
    }
    
    @Test
    public void markNotTradableReturnsEmptyAndEmitsMarkedNotTradable() {
        entity = new ItemImpl(entityId);
        
        // Simulate event callback to drive state change
        var itemAddedEvent = ItemAdded.newBuilder()
                        .setItemId(itemId)
                        .setUserId("user1")
                        .setName("test")
                        .setDescription("Test item")
                        .setTradable(true)
                        .addTags("test1")
                        .addTags("test2")
                        .build();
        entity.itemAdded(itemAddedEvent);

        var getItemCommand = GetItemCommand.newBuilder()
                            .setItemId(itemAddedEvent.getItemId())
                            .build();
        var item = entity.getItem(getItemCommand, context);
        assertThat("Should be marked tradable", item.getTradable(), is(true));

        var markNotTradableCommand = MarkNotTradableCommand.newBuilder()
                                    .setItemId(item.getItemId())
                                    .build();
        var result = entity.markNotTradable(markNotTradableCommand, context);
        assertThat("Should be Empty", result, instanceOf(Empty.class));
        
        var expected = MarkedNotTradable.newBuilder()
                        .setItemId(markNotTradableCommand.getItemId())
                        .build();
        Mockito.verify(context).emit(expected);

        // Simulate event callback to drive state change
        entity.markedNotTradable(expected);

        item = entity.getItem(getItemCommand, context);
        assertThat("Should be marked not tradable", item.getTradable(), is(false));
    }
    
    @Test
    public void changeOwnerTest() {
        entity = new ItemImpl(entityId);
        
        // Simulate event callback to drive state change
        var itemAddedEvent = ItemAdded.newBuilder()
                                .setItemId(itemId)
                                .setUserId("user1")
                                .setName("test")
                                .setDescription("Test item")
                                .setTradable(true)
                                .addTags("test1")
                                .addTags("test2")
                                .build();
        entity.itemAdded(itemAddedEvent);

        var getItemCommand = GetItemCommand.newBuilder()
                            .setItemId(itemAddedEvent.getItemId())
                            .build();
        var item = entity.getItem(getItemCommand, context);
        assertThat(item.getUserId(), is(itemAddedEvent.getUserId()));

        var changeOwnerCommand = ChangeOwnerCommand.newBuilder()
                                    .setItemId(item.getItemId())
                                    .setUserId("user2")
                                    .build();
        var result = entity.changeOwner(changeOwnerCommand, context);
        assertThat("Should be Empty", result, instanceOf(Empty.class));

        var expected = ItemOwnerChanged.newBuilder()
                        .setItemId(changeOwnerCommand.getItemId())
                        .setUserId(changeOwnerCommand.getUserId())
                        .build();
        Mockito.verify(context).emit(expected);

        // Simulate event callback to drive state change
        entity.itemOwnerChanged(expected);

        item = entity.getItem(getItemCommand, context);
        assertThat(item.getUserId(), is(expected.getUserId()));
    }
    
    @Test
    public void startTradeTest() {
        entity = new ItemImpl(entityId);
        
        Mockito.when(context.fail("The command handler for `StartTrade` is not implemented, yet"))
            .thenReturn(new MockedContextFailure());
        
        // TODO: set fields in command, and update assertions to match implementation
        assertThrows(MockedContextFailure.class, () -> {
            entity.startTradeWithReply(StartTradeCommand.newBuilder().build(), context);
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
            entity.cancelTradeWithReply(CancelTradeCommand.newBuilder().build(), context);
        });
        
        // TODO: if you wish to verify events:
        //    Mockito.verify(context).emit(event);
    }
}