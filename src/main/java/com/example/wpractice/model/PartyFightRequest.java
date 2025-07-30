package com.example.wpractice.model;

import com.example.wpractice.util.Constants;
import ga.strikepractice.arena.Arena;
import ga.strikepractice.battlekit.BattleKit;
import ga.strikepractice.party.Party;

import java.util.Objects;
import java.util.UUID;

/**
 * Модель запроса на бой между пати.
 */
public class PartyFightRequest {
    private final UUID requesterId;
    private final Party requesterParty;
    private final Party targetParty;
    private final BattleKit kit;
    private final Arena arena;
    private final long timestamp;

    public PartyFightRequest(UUID requesterId, Party requesterParty, Party targetParty, BattleKit kit, Arena arena) {
        this.requesterId = Objects.requireNonNull(requesterId, "Requester ID cannot be null");
        this.requesterParty = Objects.requireNonNull(requesterParty, "Requester party cannot be null");
        this.targetParty = Objects.requireNonNull(targetParty, "Target party cannot be null");
        this.kit = Objects.requireNonNull(kit, "Kit cannot be null");
        this.arena = Objects.requireNonNull(arena, "Arena cannot be null");
        this.timestamp = System.currentTimeMillis();
    }

    /**
     * Проверяет, истёк ли запрос.
     *
     * @return true, если запрос истёк.
     */
    public boolean isExpired() {
        return System.currentTimeMillis() - timestamp > Constants.REQUEST_TIMEOUT;
    }

    public UUID getRequesterId() {
        return requesterId;
    }

    public Party getRequesterParty() {
        return requesterParty;
    }

    public Party getTargetParty() {
        return targetParty;
    }

    public BattleKit getKit() {
        return kit;
    }

    public Arena getArena() {
        return arena;
    }
}