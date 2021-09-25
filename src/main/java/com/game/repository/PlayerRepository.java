package com.game.repository;

import com.game.entity.Player;

import java.util.List;
import java.util.Map;

public interface PlayerRepository {
    List<Player> allPlayers();
    void add(Player player);
    void delete(Player player);
    void edit(Player player);
    Player getById(Long id);
    public int playerConditionalCount(Map<String, String[]> parameters);

    int playerCount();
}