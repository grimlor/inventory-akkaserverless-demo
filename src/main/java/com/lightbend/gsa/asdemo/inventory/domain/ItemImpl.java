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
        LOG.info("[{}] deleteItem", entityId);
        if (state != null) {
            var event = ItemDeleted.newBuilder()
                        .setItemId(command.getItemId())
                        .build();
            ctx.emit(event);
        }

        return Empty.getDefaultInstance();
    }
    
    @Override
    protected Empty markTradable(MarkTradableCommand command, CommandContext ctx) {
        LOG.info("[{}] markTradable", entityId);
        if (state != null) {
            var event = MarkedTradable.newBuilder()
                        .setItemId(command.getItemId())
                        .build();
            ctx.emit(event);
        }

        return Empty.getDefaultInstance();
    }
    
    @Override
    protected Empty markNotTradable(MarkNotTradableCommand command, CommandContext ctx) {
        LOG.info("[{}] markNotTradable", entityId);
        if (state != null) {
            var event = MarkedNotTradable.newBuilder()
                        .setItemId(command.getItemId())
                        .build();
            ctx.emit(event);
        }

        return Empty.getDefaultInstance();
    }
    
    @Override
    protected Empty changeOwner(ChangeOwnerCommand command, CommandContext ctx) {
        LOG.info("[{}] changeOwner: {}", entityId, command);
        if (state != null) {
            var event = ItemOwnerChanged.newBuilder()
                        .setItemId(command.getItemId())
                        .setUserId(command.getUserId())
                        .build();
            ctx.emit(event);
        }

        return Empty.getDefaultInstance();
    }
    
    @Override
    protected Empty startTrade(StartTradeCommand command, CommandContext ctx) {
        LOG.info("[{}] startTrade: {}", entityId, command);
        if (state != null && state.getTradable()) {
            var event = TradeStarted.newBuilder()
                        .setItemId(command.getItemId())
                        .setTradeId(command.getTradeId())
                        .build();
            ctx.emit(event);
        }

        return Empty.getDefaultInstance();
    }
    
    @Override
    protected Empty cancelTrade(CancelTradeCommand command, CommandContext ctx) {
        LOG.info("[{}] cancelTrade: {}", entityId, command);
        if (state != null && state.getTradeId() == command.getTradeId()) {
            var event = TradeCancelled.newBuilder()
                        .setItemId(command.getItemId())
                        .setTradeId(command.getTradeId())
                        .setTradable(command.getTradable())
                        .build();
            ctx.emit(event);
        }

        return Empty.getDefaultInstance();
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
        LOG.info("[{}] itemDeleted: {}", entityId, event);
        state = null;
    }
    
    @Override
    public void markedTradable(MarkedTradable event) {
        LOG.info("[{}] markedTradable", entityId);
        state = state.toBuilder()
                .setTradable(true)
                .build();
    }
    
    @Override
    public void markedNotTradable(MarkedNotTradable event) {
        LOG.info("[{}] markedNotTradable", entityId);
        state = state.toBuilder()
                .setTradable(false)
                .build();
    }
    
    @Override
    public void itemOwnerChanged(ItemOwnerChanged event) {
        LOG.info("[{}] itemOwnerChanged", entityId);
        state = state.toBuilder()
                .setUserId(event.getUserId())
                .build();
    }
    
    @Override
    public void tradeStarted(TradeStarted event) {
        LOG.info("[{}] tradeStarted", entityId);
        state = state.toBuilder()
                .setTradable(false)
                .setTradeId(event.getTradeId())
                .build();
    }
    
    @Override
    public void tradeCancelled(TradeCancelled event) {
        LOG.info("[{}] tradeCancelled", entityId);
        state = state.toBuilder()
                .setTradable(event.getTradable())
                .setTradeId(Empty.getDefaultInstance().toString())
                .build();
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