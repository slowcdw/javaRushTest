package com.game.repository;

import com.game.controller.PlayerOrder;
import com.game.entity.Player;
import com.game.entity.Profession;
import com.game.entity.Race;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.server.ResponseStatusException;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

@Repository
public class PlayerRepositoryImpl implements PlayerRepository {

    private SessionFactory sessionFactory;

    @Autowired
    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }




    @Override
    @SuppressWarnings("unchecked")
    public List<Player> allPlayers(Map<String, String[]> parameters) {
        GregorianCalendar calendar = new GregorianCalendar(3000, Calendar.JANUARY , 01);
        DateFormat df = new SimpleDateFormat("yyyy-MM-DD");
        String name = "";
        String title = "";
        String race = null;
        String profession = null;
        Long after = 0L;
        Long before = calendar.getTimeInMillis();
        Boolean banned = null;
        Integer minExperience = null;
        Integer maxExperience = null;
        Integer minLevel = null;
        Integer maxLevel = null;

        PlayerOrder order = PlayerOrder.ID;
        Integer pageNumber = 0;
        Integer pageSize = 3;

        String[] vals;
        for(String key : parameters.keySet()) {
//            System.out.println("key:"+key);
            switch (key){
                case ("name"):
                    vals = parameters.get(key);
                    for(String val : vals)
                        name = val;
                    break;
                case ("title"):
                    vals = parameters.get(key);
                    for(String val : vals)
                        title = val;
                    break;
                case ("race"):
                    vals = parameters.get(key);
                    for(String val : vals)
                        race = "'"+Race.valueOf(val)+"'";
                    break;
                case ("profession"):
                    vals = parameters.get(key);
                    for(String val : vals)
                        profession = "'"+Profession.valueOf(val)+"'";
                    break;
                case ("after"):
                    vals = parameters.get(key);
                    for(String val : vals)
                        after = Long.parseLong(val);
                    break;
                case ("before"):
                    vals = parameters.get(key);
                    for(String val : vals)
                        before = Long.parseLong(val);
                    break;
                case ("banned"):
                    vals = parameters.get(key);
                    for(String val : vals)
                        banned = Boolean.valueOf(val);
                    break;
                case ("minExperience"):
                    vals = parameters.get(key);
                    for(String val : vals)
                        minExperience = Integer.parseInt(val);
                    break;
                case ("maxExperience"):
                    vals = parameters.get(key);
                    for(String val : vals)
                        maxExperience = Integer.parseInt(val);
                    break;
                case ("minLevel"):
                    vals = parameters.get(key);
                    for(String val : vals)
                        minLevel = Integer.parseInt(val);
                    break;
                case ("maxLevel"):
                    vals = parameters.get(key);
                    for(String val : vals)
                        maxLevel = Integer.parseInt(val);
                    break;
                case ("order"):
                    vals = parameters.get(key);
                    for(String val : vals)
                        order = PlayerOrder.valueOf(val);
                    break;
                case ("pageNumber"):
                    vals = parameters.get(key);
                    for(String val : vals)
                        pageNumber = Integer.parseInt(val);
                    break;
                case ("pageSize"):
                    vals = parameters.get(key);
                    for(String val : vals)
                        pageSize = Integer.parseInt(val);
                    break;
            }
        }
//        System.out.println("name:"+name);
//        System.out.println("title:"+title);
//        System.out.println("race:"+race);
//        System.out.println("profession:"+profession);
//        System.out.println("after:"+after);
//        System.out.println("before:"+before);
//        System.out.println("banned:"+banned);
//        System.out.println("minExperience:"+minExperience);
//        System.out.println("maxExperience:"+maxExperience);
//        System.out.println("minLevel:"+minLevel);
//        System.out.println("maxLevel:"+maxLevel);

        Session session = sessionFactory.getCurrentSession();
        return session.createQuery("from Player where " +
                        "level >= coalesce("+minLevel+", level) " +
                        "and level <= coalesce("+maxLevel+", level) " +
                        "and experience >= coalesce("+minExperience+",experience)" +
                        "and experience <= coalesce("+maxExperience+",experience)" +
                        "and name like '%"+name+"%'" +
                        "and title like '%"+title+"%'" +
                        "and race = coalesce("+race+",race)" +
                        "and profession = coalesce("+profession+",profession)" +
                        "and banned = coalesce("+banned+",banned)" +
                        "and birthday <= '"+ df.format(before) +"'" +
                        "and birthday >= '"+ df.format(after) +"'" +
                        "and banned = coalesce("+banned+",banned)" +
                        "order by "+order
                ).setFirstResult(pageSize * (pageNumber)).setMaxResults(pageSize).list();
    }




//        Session session = sessionFactory.getCurrentSession();
//        return session.createQuery("from Film").setFirstResult(10 * (page - 1)).setMaxResults(10).list();







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

    @Override
    public int playerConditionalCount(Map<String, String[]> parameters) {
        GregorianCalendar calendar = new GregorianCalendar(3000, Calendar.JANUARY , 01);
        DateFormat df = new SimpleDateFormat("yyyy-MM-DD");
        String name = "";
        String title = "";
        String race = null;
        String profession = null;
        Long after = 0L;
        Long before = calendar.getTimeInMillis();
        Boolean banned = null;
        Integer minExperience = null;
        Integer maxExperience = null;
        Integer minLevel = null;
        Integer maxLevel = null;
        String[] vals;
        Session session = sessionFactory.getCurrentSession();
        for(String key : parameters.keySet()) {
//            System.out.println("key:"+key);
            switch (key){
                case ("name"):
                    vals = parameters.get(key);
                    for(String val : vals)
                        name = val;
                    break;
                case ("title"):
                    vals = parameters.get(key);
                    for(String val : vals)
                        title = val;
                    break;
                case ("race"):
                    vals = parameters.get(key);
                    for(String val : vals)
                        race = "'"+Race.valueOf(val)+"'";
                    break;
                case ("profession"):
                    vals = parameters.get(key);
                    for(String val : vals)
                        profession = "'"+Profession.valueOf(val)+"'";
                    break;
                case ("after"):
                    vals = parameters.get(key);
                    for(String val : vals)
                        after = Long.parseLong(val);
                    break;
                case ("before"):
                    vals = parameters.get(key);
                    for(String val : vals)
                        before = Long.parseLong(val);
                    break;
                case ("banned"):
                    vals = parameters.get(key);
                    for(String val : vals)
                        banned = Boolean.valueOf(val);
                    break;
                case ("minExperience"):
                    vals = parameters.get(key);
                    for(String val : vals)
                        minExperience = Integer.parseInt(val);
                    break;
                case ("maxExperience"):
                    vals = parameters.get(key);
                    for(String val : vals)
                        maxExperience = Integer.parseInt(val);
                    break;
                case ("minLevel"):
                    vals = parameters.get(key);
                    for(String val : vals)
                        minLevel = Integer.parseInt(val);
                    break;
                case ("maxLevel"):
                    vals = parameters.get(key);
                    for(String val : vals)
                        maxLevel = Integer.parseInt(val);
                    break;
            }
        }
//        System.out.println("name:"+name);
//        System.out.println("title:"+title);
//        System.out.println("race:"+race);
//        System.out.println("profession:"+profession);
//        System.out.println("after:"+after);
//        System.out.println("before:"+before);
//        System.out.println("banned:"+banned);
//        System.out.println("minExperience:"+minExperience);
//        System.out.println("maxExperience:"+maxExperience);
//        System.out.println("minLevel:"+minLevel);
//        System.out.println("maxLevel:"+maxLevel);

        return session.createQuery("select count(*) from Player where " +
                "level >= coalesce("+minLevel+", level) " +
                        "and level <= coalesce("+maxLevel+", level) " +
                        "and experience >= coalesce("+minExperience+",experience)" +
                        "and experience <= coalesce("+maxExperience+",experience)" +
                        "and name like '%"+name+"%'" +
                        "and title like '%"+title+"%'" +
                        "and race = coalesce("+race+",race)" +
                        "and profession = coalesce("+profession+",profession)" +
                        "and banned = coalesce("+banned+",banned)" +
                        "and birthday <= '"+ df.format(before) +"'" +
                        "and birthday >= '"+ df.format(after) +"'" +
                        "and banned = coalesce("+banned+",banned)"
                , Number.class).getSingleResult().intValue();
    }

}
