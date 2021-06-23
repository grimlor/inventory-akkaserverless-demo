package com.lightbend.gsa.asdemo.inventory.domain;

import com.akkaserverless.javasdk.eventsourcedentity.CommandContext;
import com.google.protobuf.Empty;
import com.lightbend.gsa.asdemo.inventory.InventoryApi;
import org.junit.Test;
import org.mockito.*;

import static org.junit.Assert.assertThrows;

public class ItemTest {
    private String entityId = "entityId1";
    private ItemImpl entity;
    private CommandContext context = Mockito.mock(CommandContext.class);
    
    private class MockedContextFailure extends RuntimeException {};
    
    @Test
    public void getItemTest() {
        entity = new ItemImpl(entityId);
        
        Mockito.when(context.fail("The command handler for `GetItem` is not implemented, yet"))
            .thenReturn(new MockedContextFailure());
        
        // TODO: set fields in command, and update assertions to match implementation
        assertThrows(MockedContextFailure.class, () -> {
            entity.getItemWithReply(InventoryApi.GetItemCommand.newBuilder().build(), context);
        });
        
        // TODO: if you wish to verify events:
        //    Mockito.verify(context).emit(event);
    }
    
    @Test
    public void addItemTest() {
        entity = new ItemImpl(entityId);
        
        Mockito.when(context.fail("The command handler for `AddItem` is not implemented, yet"))
            .thenReturn(new MockedContextFailure());
        
        // TODO: set fields in command, and update assertions to match implementation
        assertThrows(MockedContextFailure.class, () -> {
            entity.addItemWithReply(InventoryApi.AddItemCommand.newBuilder().build(), context);
        });
        
        // TODO: if you wish to verify events:
        //    Mockito.verify(context).emit(event);
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