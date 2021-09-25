package com.game.service;

import com.game.entity.Player;
import com.game.repository.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@org.springframework.stereotype.Service
public class PlayerServiceImpl implements PlayerService {

    private PlayerRepository playerRepository;

    @Autowired
    public void setRepository(PlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
    }

    @Override
    @Transactional
    public List<Player> allPlayers() {
        return playerRepository.allPlayers();
    }

    @Override
    @Transactional
    public void add(Player player) {
        playerRepository.add(player);
    }

    @Override
    @Transactional
    public void delete(Player player) {
        playerRepository.delete(player);
    }

    @Override
    @Transactional
    public void edit(Player player) {
        playerRepository.edit(player);
    }

    @Override
    @Transactional
    public Player getById(Long id) {
        return playerRepository.getById(id);
    }

    @Override
    @Transactional
    public int playerCount() {
        return playerRepository.playerCount();
    }
}
