package com.lightbend.gsa.asdemo.inventory.views;

import java.util.Optional;

import com.akkaserverless.javasdk.view.UpdateHandler;
import com.akkaserverless.javasdk.view.View;
import com.google.protobuf.Empty;
import com.lightbend.gsa.asdemo.inventory.domain.InventoryDomain.*;
import com.lightbend.gsa.asdemo.inventory.view.ItemsByUserOuterClass.ItemForUser;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@View
public class ItemsByUser {

    private static final Logger LOG = LoggerFactory.getLogger(ItemsByUser.class);
    
    @UpdateHandler
    public ItemForUser processItemAdded(ItemAdded event, Optional<ItemForUser> state) {
        LOG.info("[{}] processItemAdded: {}", event.getItemId(), event);
        return state.orElse(
            ItemForUser.newBuilder()
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
                .build()
        );
    }

    @UpdateHandler
    public ItemForUser processMarkedTradable(MarkedTradable event, ItemForUser state) {
        LOG.info("[{}] processMarkedTradable", event.getItemId());
        return state.toBuilder()
                .setTradable(true)
                .build();
    }

    @UpdateHandler
    public ItemForUser processItemOwnerChanged(ItemOwnerChanged event, ItemForUser state) {
        LOG.info("[{}] processItemOwnerChanged: {}", event.getItemId(), event);
        return state.toBuilder()
                .setUserId(event.getUserId())
                .build();
    }

    @UpdateHandler
    public ItemForUser processTradeStarted(TradeStarted event, ItemForUser state) {
        LOG.info("[{}] processTradeStarted: {}", event.getItemId(), event);
        return state.toBuilder()
                .setTradable(false)
                .setTradeId(event.getTradeId())
                .build();
    }

    @UpdateHandler
    public ItemForUser processTradeCancelled(TradeCancelled event, ItemForUser state) {
        LOG.info("[{}] processTradeCancelled: {}", event.getItemId(), event);
        return state.toBuilder()
                .setTradable(event.getTradable())
                .setTradeId(Empty.getDefaultInstance().toString())
                .build();
    }
}
