package com.lightbend.gsa.asdemo.inventory;

import com.akkaserverless.javasdk.AkkaServerless;
import com.lightbend.gsa.asdemo.inventory.domain.InventoryDomain;
import com.lightbend.gsa.asdemo.inventory.domain.ItemImpl;
import com.lightbend.gsa.asdemo.inventory.view.ItemsByTagBounds;
import com.lightbend.gsa.asdemo.inventory.view.ItemsByUserOuterClass;
import com.lightbend.gsa.asdemo.inventory.views.ItemsByTagAndBounds;
import com.lightbend.gsa.asdemo.inventory.views.ItemsByUser;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class Main {
    
    private static final Logger LOG = LoggerFactory.getLogger(Main.class);

    public static final AkkaServerless SERVICE = new AkkaServerless()
        .registerEventSourcedEntity(
            ItemImpl.class, 
            InventoryApi.getDescriptor().findServiceByName("InventoryService"), 
            InventoryDomain.getDescriptor())
        .registerView(
            ItemsByTagAndBounds.class, 
            ItemsByTagBounds.getDescriptor().findServiceByName("ItemsByTagAndBounds"), 
            "items_by_tag_bounds", 
            InventoryDomain.getDescriptor(),
            ItemsByTagBounds.getDescriptor());
        // .registerView(
        //     ItemsByUser.class, 
        //     ItemsByUserOuterClass.getDescriptor().findServiceByName("ItemsByUser"), 
        //     "items_by_user", 
        //     InventoryDomain.getDescriptor(),
        //     ItemsByUserOuterClass.getDescriptor());
    
    public static void main(String[] args) throws Exception {
        LOG.info("starting the Akka Serverless service");
        SERVICE.start().toCompletableFuture().get();
    }
}