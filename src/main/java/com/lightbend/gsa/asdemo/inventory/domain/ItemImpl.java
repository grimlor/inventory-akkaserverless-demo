package com.lightbend.gsa.asdemo.inventory.domain;

import com.akkaserverless.javasdk.EntityId;
import com.akkaserverless.javasdk.eventsourcedentity.*;
import com.google.protobuf.Empty;
import com.lightbend.gsa.asdemo.inventory.InventoryApi.*;
import com.lightbend.gsa.asdemo.inventory.domain.InventoryDomain.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** An event sourced entity. */
@EventSourcedEntity(entityType = "ItemEntity")
public class ItemImpl extends ItemInterface {

    private static Logger LOG = LoggerFactory.getLogger(ItemImpl.class);

    @SuppressWarnings("unused")
    private final String entityId;

    private Item state;
    
    public ItemImpl(@EntityId String entityId) {
        this.entityId = entityId;
    }
    
    @Override
    public ItemState snapshot() {
        LOG.info("[{}] snapshot", entityId);
        return convert(state);
    }
    
    @Override
    public void handleSnapshot(ItemState snapshot) {
        LOG.info("[{}] handleSnapshot: {}", entityId, snapshot);
        this.state = convert(snapshot);
    }
    
    @Override
    protected Item getItem(GetItemCommand command, CommandContext ctx) {
        LOG.info("[{}] getItem", entityId);
        return state;
    }
    
    @Override
    protected Empty addItem(AddItemCommand command, CommandContext ctx) {
        LOG.info("[{}] addItem: {}", entityId, command);
        if (state == null) {
            var event = ItemAdded.newBuilder()
                        .setItemId(command.getItemId())
                        .setUserId(command.getUserId())
                        .setName(command.getName())
                        .setDescription(command.getDescription())
                        .setImageUrl(command.getImageUrl())
                        .setTradable(command.getTradable())
                        .setGpsLat(command.getGpsLat())
                        .setGpsLon(command.getGpsLon())
                        .setTradeId(command.getTradeId())
                        .addAllTags(command.getTagsList())
                        .build();
            ctx.emit(event);
        }

        return Empty.getDefaultInstance();
    }
    
    @Override
    protected Empty deleteItem(DeleteItemCommand command, CommandContext ctx) {
        throw ctx.fail("The command handler for `DeleteItem` is not implemented, yet");
    }
    
    @Override
    protected Empty markTradable(MarkTradableCommand command, CommandContext ctx) {
        throw ctx.fail("The command handler for `MarkTradable` is not implemented, yet");
    }
    
    @Override
    protected Empty markNotTradable(MarkNotTradableCommand command, CommandContext ctx) {
        throw ctx.fail("The command handler for `MarkNotTradable` is not implemented, yet");
    }
    
    @Override
    protected Empty changeOwner(ChangeOwnerCommand command, CommandContext ctx) {
        throw ctx.fail("The command handler for `ChangeOwner` is not implemented, yet");
    }
    
    @Override
    protected Empty startTrade(StartTradeCommand command, CommandContext ctx) {
        throw ctx.fail("The command handler for `StartTrade` is not implemented, yet");
    }
    
    @Override
    protected Empty cancelTrade(CancelTradeCommand command, CommandContext ctx) {
        throw ctx.fail("The command handler for `CancelTrade` is not implemented, yet");
    }
    
    @Override
    public void itemAdded(ItemAdded event) {
        LOG.info("[{}] itemAdded: {}", entityId, event);
        state = Item.newBuilder()
                .setItemId(event.getItemId())
                .setUserId(event.getUserId())
                .setName(event.getName())
                .setDescription(event.getDescription())
                .setImageUrl(event.getImageUrl())
                .setTradable(event.getTradable())
                .setGpsLat(event.getGpsLat())
                .setGpsLon(event.getGpsLon())
                .setTradeId(event.getTradeId())
                .addAllTags(event.getTagsList())
        .build();
    }
    
    @Override
    public void itemDeleted(ItemDeleted event) {
        throw new RuntimeException("The event handler for `ItemDeleted` is not implemented, yet");
    }
    
    @Override
    public void markedTradable(MarkedTradable event) {
        throw new RuntimeException("The event handler for `MarkedTradable` is not implemented, yet");
    }
    
    @Override
    public void markedNotTradable(MarkedNotTradable event) {
        throw new RuntimeException("The event handler for `MarkedNotTradable` is not implemented, yet");
    }
    
    @Override
    public void itemOwnerChanged(ItemOwnerChanged event) {
        throw new RuntimeException("The event handler for `ItemOwnerChanged` is not implemented, yet");
    }
    
    @Override
    public void tradeStarted(TradeStarted event) {
        throw new RuntimeException("The event handler for `TradeStarted` is not implemented, yet");
    }
    
    @Override
    public void tradeCancelled(TradeCancelled event) {
        throw new RuntimeException("The event handler for `TradeCancelled` is not implemented, yet");
    }

    private Item convert(ItemState state) {
        return Item.newBuilder()
                .setItemId(state.getItemId())
                .setUserId(state.getUserId())
                .setName(state.getName())
                .setDescription(state.getDescription())
                .setImageUrl(state.getImageUrl())
                .setTradable(state.getTradable())
                .setGpsLat(state.getGpsLat())
                .setGpsLon(state.getGpsLon())
                .setTradeId(state.getTradeId())
                .addAllTags(state.getTagsList())
                .build();
    }
    
    private ItemState convert(Item item) {
        return ItemState.newBuilder()
                .setItemId(item.getItemId())
                .setUserId(item.getUserId())
                .setName(item.getName())
                .setDescription(item.getDescription())
                .setImageUrl(item.getImageUrl())
                .setTradable(item.getTradable())
                .setGpsLat(item.getGpsLat())
                .setGpsLon(item.getGpsLon())
                .setTradeId(item.getTradeId())
                .addAllTags(item.getTagsList())
                .build();
    }
}