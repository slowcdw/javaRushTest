package com.game.repository;

import com.game.entity.Player;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Repository
public class PlayerRepositoryImpl implements PlayerRepository {

    private SessionFactory sessionFactory;

    @Autowired
    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public List<Player> allPlayers() {
        Session session = sessionFactory.getCurrentSession();
        return session.createQuery("from Player").setFirstResult(10).list();
    }

    @Override
    public void add(Player player) {
        Session session = sessionFactory.getCurrentSession();
        session.persist(player);
    }

    @Override
    public void delete(Player player) {
        Session session = sessionFactory.getCurrentSession();
        try{
            session.delete(player);
        }catch (Exception e) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "entity not found"
            );
        }
    }

    @Override
    public void edit(Player player) {
        Session session = sessionFactory.getCurrentSession();
        session.update(player);
    }

    @Override
    public Player getById(Long id) {
        Session session = sessionFactory.getCurrentSession();
            return session.get(Player.class, id);
    }

    @Override
    public int playerCount() {
        Session session = sessionFactory.getCurrentSession();
        return session.createQuery("select count(*) from Player ", Number.class).getSingleResult().intValue();
    }
}
