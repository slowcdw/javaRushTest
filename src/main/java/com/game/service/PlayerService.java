package com.game.service;

import com.game.entity.Player;

import java.util.List;

public interface PlayerService {
    List<Player> allPlayers();
    void add(Player player);
    void delete(Player player);
    void edit(Player player);
    Player getById(Long id);

    int playerCount();
}
