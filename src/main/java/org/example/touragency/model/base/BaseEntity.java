package org.example.touragency.model.base;

import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
public abstract class BaseEntity {

    private final UUID id;
    private final LocalDateTime createdAt;


    public BaseEntity() {
        this.id = UUID.randomUUID();
        this.createdAt = LocalDateTime.now();
    }
}
