package com.game.repository;

import com.game.entity.Player;

import java.util.List;

public interface PlayerRepository {
    List<Player> allPlayers();
    void add(Player player);
    void delete(Player player);
    void edit(Player player);
    Player getById(Long id);

    int playerCount();
}