package com.lightbend.gsa.asdemo.inventory.actions;

import com.akkaserverless.javasdk.Reply;
import com.akkaserverless.javasdk.ServiceCallRef;
import com.akkaserverless.javasdk.action.Action;
import com.akkaserverless.javasdk.action.ActionContext;
import com.akkaserverless.javasdk.action.Handler;
import com.google.protobuf.Empty;
import com.lightbend.gsa.asdemo.inventory.InventoryApi.CancelTradeCommand;
import com.lightbend.gsa.asdemo.inventory.InventoryApi.ChangeOwnerCommand;
import com.lightbend.gsa.asdemo.inventory.InventoryApi.StartTradeCommand;
import com.lightbend.gsa.trade.domain.TradeDomain.TradeAccepted;
import com.lightbend.gsa.trade.domain.TradeDomain.TradeOffered;
import com.lightbend.gsa.trade.domain.TradeDomain.TradeRejected;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Action
public class TradeServiceSubscriberAction {
    
    private static final Logger LOG = LoggerFactory.getLogger(TradeServiceSubscriberAction.class);
    private static final String INVENTORY_SERVICE = "com.lightbend.gsa.asdemo.inventory.InventoryService";

    @Handler
    public Reply<Empty> comsumeTradeOffered(TradeOffered tradeOffered, ActionContext context) {
        LOG.info("[{}] consumeTradeOffered: {}", tradeOffered.getTradeId(), tradeOffered);

        ServiceCallRef<StartTradeCommand> ref = context.serviceCallFactory()
            .lookup(INVENTORY_SERVICE, "StartTrade", StartTradeCommand.class);
        var cmd = StartTradeCommand.newBuilder()
                                    .setItemId(tradeOffered.getSellerUserId())
                                    .setTradeId(tradeOffered.getTradeId())
                                    .build();
        return Reply.forward(ref.createCall(cmd));
    }

    @Handler
    public Reply<Empty> consumeTradeAccepted(TradeAccepted tradeAccepted, ActionContext context) {
        LOG.info("[{}] consumeTradeAccepted: {}", tradeAccepted.getTradeId(), tradeAccepted);

        ServiceCallRef<ChangeOwnerCommand> ref = context.serviceCallFactory()
            .lookup(INVENTORY_SERVICE, "ChangeOwner", ChangeOwnerCommand.class);
        // var cmd = ChangeOwnerCommand.newBuilder()
        //                             .setItemId(tradeAccepted.getItemId())
        //                             .setUserId(tradeAccepted.setSellerUserId())
        //                             .setTradable(false)
        //                             .build();
        // return Reply.forward(ref.createCall(cmd));
        return Reply.noReply();
    }

    @Handler
    public Reply<Empty> consumeTradeRejected(TradeRejected tradeRejected, ActionContext context) {
        LOG.info("[{}] consumeTradeRejected: {}", tradeRejected.getTradeId(), tradeRejected);

        ServiceCallRef<CancelTradeCommand> ref = context.serviceCallFactory()
            .lookup(INVENTORY_SERVICE, "CancelTrade", CancelTradeCommand.class);
        // var cmd = CancelTradeCommand.newBuilder()
        //                             .setItemId(tradeRejected.getItemId())
        //                             .setTradeId(tradeRejected.getTradeId())
        //                             .setTradable(tradeRejected.getTradable())
        //                             .build();
        // return Reply.forward(ref.createCall(cmd));
        return Reply.noReply();
    }
}
