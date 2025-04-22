package com.eldritch.engine;

import java.io.Serializable;
import java.util.UUID;

public abstract class GameObject implements Serializable {
    protected final UUID id;
    protected final String name;

    public GameObject(String name) {
        this.id = UUID.randomUUID();
        this.name = name;
    }

    public UUID getId() { return id; }
    public String getName() { return name; }
}
