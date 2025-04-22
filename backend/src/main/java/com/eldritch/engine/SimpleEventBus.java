package com.eldritch.engine;

import com.eldritch.client.ClientConnection;
import com.eldritch.common.EventBus;
import com.eldritch.common.EventListener;
import com.eldritch.common.GameEvent;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.locks.*;

public class SimpleEventBus implements EventBus {
    private static final Logger logger = LogManager.getLogger(SimpleEventBus.class);

    private final Map<String, Set<com.eldritch.common.EventListener>> listeners = new ConcurrentHashMap<>();
    private final Lock listenerLock = new ReentrantLock();
    private final ExecutorService executor;
    private final boolean async;

    // Remote listener holder
    private final Map<ClientConnection, com.eldritch.common.EventListener> remoteListeners = new ConcurrentHashMap<>();

    public SimpleEventBus() {
        this(true); // Default to async processing
    }

    public SimpleEventBus(boolean async) {
        this.async = async;
        if (async) {
            this.executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        } else {
            this.executor = null;
        }
    }

    @Override
    public void subscribe(String eventType, com.eldritch.common.EventListener listener) {
        listenerLock.lock();
        try {
            listeners.computeIfAbsent(eventType, k -> ConcurrentHashMap.newKeySet())
                    .add(listener);
            logger.debug("Subscribed listener to event type: {}", eventType);
        } finally {
            listenerLock.unlock();
        }
    }

    @Override
    public void unsubscribe(String eventType, com.eldritch.common.EventListener listener) {
        listenerLock.lock();
        try {
            Set<com.eldritch.common.EventListener> eventListeners = listeners.get(eventType);
            if (eventListeners != null) {
                eventListeners.remove(listener);
                if (eventListeners.isEmpty()) {
                    listeners.remove(eventType);
                }
                logger.debug("Unsubscribed listener from event type: {}", eventType);
            }
        } finally {
            listenerLock.unlock();
        }
    }

    @Override
    public void publish(GameEvent event) {
        if (logger.isTraceEnabled()) {
            logger.trace("Publishing event: {} from {}", event.getEventType(), event.getOriginId());
        }

        Set<com.eldritch.common.EventListener> eventListeners = listeners.get(event.getEventType());
        if (eventListeners != null && !eventListeners.isEmpty()) {
            // Create a copy to avoid concurrent modification issues
            List<com.eldritch.common.EventListener> listenersCopy = new ArrayList<>(eventListeners);
            //ToDo probably needs to sort them corresponding to the logic

            for (com.eldritch.common.EventListener listener : listenersCopy) {
                if (async) {
                    executor.submit(() -> safeHandleEvent(listener, event));
                } else {
                    safeHandleEvent(listener, event);
                }
            }
        } else {
            logger.debug("No subscribers for event type: {}", event.getEventType());
        }
    }

    private void safeHandleEvent(com.eldritch.common.EventListener listener, GameEvent event) {
        try {
            long startTime = System.nanoTime();
            listener.handleEvent(event);
            long duration = System.nanoTime() - startTime;

            if (duration > TimeUnit.MILLISECONDS.toNanos(100)) {
                logger.warn("Event handling took {} ms for type: {}",
                        TimeUnit.NANOSECONDS.toMillis(duration), event.getEventType());
            } else if (logger.isDebugEnabled()) {
                logger.debug("Event handled in {} ns for type: {}",
                        duration, event.getEventType());
            }
        } catch (Exception e) {
            logger.error("Error handling event {}: {}", event.getEventType(), e.getMessage(), e);
        }
    }

    @Override
    public void registerRemoteListener(ClientConnection connection) {
        EventListener listener = event -> {
            try {
                //connection.sendEvent(event);
            } catch (Exception e) {
                logger.error("Failed to send event to remote client", e);
                unsubscribe("NETWORK_EVENT", remoteListeners.get(connection));
                remoteListeners.remove(connection);
            }
        };

        remoteListeners.put(connection, listener);
        subscribe("NETWORK_EVENT", listener);
        logger.info("Registered remote listener for client: {}", connection.getClientId());
    }

    public void shutdown() {
        if (executor != null) {
            logger.info("Shutting down EventBus executor");
            executor.shutdown();
            try {
                if (!executor.awaitTermination(5, TimeUnit.SECONDS)) {
                    logger.warn("Forcing shutdown of remaining EventBus tasks");
                    executor.shutdownNow();
                }
            } catch (InterruptedException e) {
                executor.shutdownNow();
                Thread.currentThread().interrupt();
                logger.warn("EventBus shutdown interrupted", e);
            }
        }
    }

    public boolean hasSubscribersFor(String eventType) {
        boolean hasSubscribers = listeners.containsKey(eventType) &&
                !listeners.get(eventType).isEmpty();
        logger.debug("Check for subscribers of {}: {}", eventType, hasSubscribers);
        return hasSubscribers;
    }

    public int getSubscriberCount(String eventType) {
        int count = listeners.containsKey(eventType) ? listeners.get(eventType).size() : 0;
        logger.debug("Subscriber count for {}: {}", eventType, count);
        return count;
    }
}