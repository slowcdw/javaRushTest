package com.game.service;

import com.game.entity.Player;

import java.util.List;
import java.util.Map;

public interface PlayerService {
    List<Player> allPlayers();
    void add(Player player);
    void delete(Player player);
    void edit(Player player);
    Player getById(Long id);

    int playerCount();

    int playerConditionalCount(Map<String, String[]> parameters);
}
