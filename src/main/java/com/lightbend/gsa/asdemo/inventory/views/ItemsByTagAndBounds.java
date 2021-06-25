package com.lightbend.gsa.asdemo.inventory.views;

import java.util.Optional;

import com.akkaserverless.javasdk.view.UpdateHandler;
import com.akkaserverless.javasdk.view.View;
import com.google.protobuf.Empty;
import com.lightbend.gsa.asdemo.inventory.domain.InventoryDomain.*;
import com.lightbend.gsa.asdemo.inventory.view.ItemsByTagBounds.ItemForTagBounds;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@View
public class ItemsByTagAndBounds {

    private static final Logger LOG = LoggerFactory.getLogger(ItemsByTagAndBounds.class);
    
    @UpdateHandler
    public ItemForTagBounds processItemAdded(ItemAdded event, Optional<ItemForTagBounds> state) {
        LOG.info("[{}] processItemAdded: {}", event.getItemId(), event);
        return state.orElse(
            ItemForTagBounds.newBuilder()
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
    public ItemForTagBounds processMarkedTradable(MarkedTradable event, ItemForTagBounds state) {
        LOG.info("[{}] processMarkedTradable", event.getItemId());
        return state.toBuilder()
                .setTradable(true)
                .build();
    }

    @UpdateHandler
    public ItemForTagBounds processItemOwnerChanged(ItemOwnerChanged event, ItemForTagBounds state) {
        LOG.info("[{}] processItemOwnerChanged: {}", event.getItemId(), event);
        return state.toBuilder()
                .setUserId(event.getUserId())
                .build();
    }

    @UpdateHandler
    public ItemForTagBounds processTradeStarted(TradeStarted event, ItemForTagBounds state) {
        LOG.info("[{}] processTradeStarted: {}", event.getItemId(), event);
        return state.toBuilder()
                .setTradable(false)
                .setTradeId(event.getTradeId())
                .build();
    }

    @UpdateHandler
    public ItemForTagBounds processTradeCancelled(TradeCancelled event, ItemForTagBounds state) {
        LOG.info("[{}] processTradeCancelled: {}", event.getItemId(), event);
        return state.toBuilder()
                .setTradable(event.getTradable())
                .setTradeId(Empty.getDefaultInstance().toString())
                .build();
    }
}
