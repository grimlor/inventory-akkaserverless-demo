package com.lightbend.gsa.asdemo.inventory.domain;

import com.akkaserverless.javasdk.EntityId;
import com.akkaserverless.javasdk.eventsourcedentity.*;
import com.google.protobuf.Empty;
import com.lightbend.gsa.asdemo.inventory.InventoryApi;

/** An event sourced entity. */
@EventSourcedEntity(entityType = "ItemEntity")
public class ItemImpl extends ItemInterface {
    @SuppressWarnings("unused")
    private final String entityId;
    
    public ItemImpl(@EntityId String entityId) {
        this.entityId = entityId;
    }
    
    @Override
    public InventoryDomain.ItemState snapshot() {
        // TODO: produce state snapshot here
        return InventoryDomain.ItemState.newBuilder().build();
    }
    
    @Override
    public void handleSnapshot(InventoryDomain.ItemState snapshot) {
        // TODO: restore state from snapshot here
        
    }
    
    @Override
    protected InventoryApi.Item getItem(InventoryApi.GetItemCommand command, CommandContext ctx) {
        throw ctx.fail("The command handler for `GetItem` is not implemented, yet");
    }
    
    @Override
    protected Empty addItem(InventoryApi.AddItemCommand command, CommandContext ctx) {
        throw ctx.fail("The command handler for `AddItem` is not implemented, yet");
    }
    
    @Override
    protected Empty deleteItem(InventoryApi.DeleteItemCommand command, CommandContext ctx) {
        throw ctx.fail("The command handler for `DeleteItem` is not implemented, yet");
    }
    
    @Override
    protected Empty markTradable(InventoryApi.MarkTradableCommand command, CommandContext ctx) {
        throw ctx.fail("The command handler for `MarkTradable` is not implemented, yet");
    }
    
    @Override
    protected Empty markNotTradable(InventoryApi.MarkNotTradableCommand command, CommandContext ctx) {
        throw ctx.fail("The command handler for `MarkNotTradable` is not implemented, yet");
    }
    
    @Override
    protected Empty changeOwner(InventoryApi.ChangeOwnerCommand command, CommandContext ctx) {
        throw ctx.fail("The command handler for `ChangeOwner` is not implemented, yet");
    }
    
    @Override
    protected Empty startTrade(InventoryApi.StartTradeCommand command, CommandContext ctx) {
        throw ctx.fail("The command handler for `StartTrade` is not implemented, yet");
    }
    
    @Override
    protected Empty cancelTrade(InventoryApi.CancelTradeCommand command, CommandContext ctx) {
        throw ctx.fail("The command handler for `CancelTrade` is not implemented, yet");
    }
    
    @Override
    public void itemAdded(InventoryDomain.ItemAdded event) {
        throw new RuntimeException("The event handler for `ItemAdded` is not implemented, yet");
    }
    
    @Override
    public void itemDeleted(InventoryDomain.ItemDeleted event) {
        throw new RuntimeException("The event handler for `ItemDeleted` is not implemented, yet");
    }
    
    @Override
    public void markedTradable(InventoryDomain.MarkedTradable event) {
        throw new RuntimeException("The event handler for `MarkedTradable` is not implemented, yet");
    }
    
    @Override
    public void markedNotTradable(InventoryDomain.MarkedNotTradable event) {
        throw new RuntimeException("The event handler for `MarkedNotTradable` is not implemented, yet");
    }
    
    @Override
    public void itemOwnerChanged(InventoryDomain.ItemOwnerChanged event) {
        throw new RuntimeException("The event handler for `ItemOwnerChanged` is not implemented, yet");
    }
    
    @Override
    public void tradeStarted(InventoryDomain.TradeStarted event) {
        throw new RuntimeException("The event handler for `TradeStarted` is not implemented, yet");
    }
    
    @Override
    public void tradeCancelled(InventoryDomain.TradeCancelled event) {
        throw new RuntimeException("The event handler for `TradeCancelled` is not implemented, yet");
    }
}