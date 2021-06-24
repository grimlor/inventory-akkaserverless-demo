package com.lightbend.gsa.asdemo.inventory;

import com.akkaserverless.javasdk.AkkaServerless;
import com.lightbend.gsa.asdemo.inventory.domain.InventoryDomain;
import com.lightbend.gsa.asdemo.inventory.domain.ItemImpl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.lightbend.gsa.asdemo.inventory.MainComponentRegistrations.withGeneratedComponentsAdded;

public final class Main {
    
    private static final Logger LOG = LoggerFactory.getLogger(Main.class);


    
    public static void main(String[] args) throws Exception {
        LOG.info("starting the Akka Serverless service");
        withGeneratedComponentsAdded(
            new AkkaServerless()
                .registerEventSourcedEntity(
                    ItemImpl.class, 
                    InventoryApi.getDescriptor().findServiceByName("InventoryService"), 
                    InventoryDomain.getDescriptor())
        ).start().toCompletableFuture().get();
    }
}