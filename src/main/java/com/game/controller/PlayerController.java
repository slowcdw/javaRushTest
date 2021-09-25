package com.game.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.game.entity.Player;
import com.game.service.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.StringWriter;
import java.util.List;
import java.util.Map;

@Controller
public class PlayerController {
    private int page;
    private PlayerService playerService;
    @Autowired
    public void setPlayerService(PlayerService playerService) {
        this.playerService = playerService;
    }

//    @RequestMapping(value = "/", method = RequestMethod.GET)
//    public ModelAndView getPlayersList() {
//        List<Player> players = playerService.allPlayers();
//        ModelAndView modelAndView = new ModelAndView();
//        modelAndView.setViewName("index");
//        modelAndView.addObject("playerList", players);
//        return modelAndView;
//    }



    @RequestMapping(value="/rest/players/{id}", method = RequestMethod.DELETE)
    public ModelAndView deletePlayer(@PathVariable("id") String id) {
        System.out.println("delete");
        Long lid = validId(id);
        ModelAndView modelAndView = new ModelAndView();
        int playerCount = playerService.playerCount();
        int page = ((playerCount - 1) % 10 == 0 && playerCount > 10 && this.page == (playerCount + 9)/10) ?
                this.page - 1 : this.page;
//        modelAndView.setViewName("redirect:/");
        modelAndView.addObject("page", page);
        Player player = playerService.getById(lid);
        playerService.delete(player);
        return modelAndView;
    }

    @RequestMapping (value = "/rest/players/count", method = RequestMethod.GET)
    public ResponseEntity<Integer> getCount (HttpServletRequest request) {
        Map<String, String[]> parameters = request.getParameterMap();
        int playerCount = playerService.playerConditionalCount(parameters);
        ModelAndView mv = new ModelAndView();
        mv.setViewName("index");
        return new ResponseEntity<>(
                playerCount,
                HttpStatus.OK);
    }

    @RequestMapping (value = "/rest/players", method = RequestMethod.GET)
    public ResponseEntity<String> getPlayersList (HttpServletRequest request) throws IOException {
        Map<String, String[]> parameters = request.getParameterMap();
        List<Player> playerList = playerService.allPlayers(parameters);

        StringWriter writer = new StringWriter();
        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(writer, playerList);

        String result = writer.toString();
//        System.out.println(result);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "text/html; charset=utf-8");
        return new ResponseEntity<>(
                result,
                headers,
                HttpStatus.OK);
    }


    @RequestMapping(value="/rest/players/{id}", method = RequestMethod.GET)
    public ResponseEntity<String> getPlayer(@PathVariable("id") String id) throws IOException {
//        System.out.println("get");
        Long lid = validId(id);
        ModelAndView modelAndView = new ModelAndView();
        int playerCount = playerService.playerCount();
        int page = ((playerCount - 1) % 10 == 0 && playerCount > 10 && this.page == (playerCount + 9)/10) ?
                this.page - 1 : this.page;
//        modelAndView.setViewName("redirect:/");
        modelAndView.setViewName("index");
        Player player = playerService.getById(lid);
        if (player == null) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "entity not found"
            );
        }
        modelAndView.addObject("player", player);

        StringWriter writer = new StringWriter();
        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(writer, player);
        String result = writer.toString();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "text/html; charset=utf-8");

        return new ResponseEntity<>(
                result,
                headers,
                HttpStatus.OK);
    }

    @RequestMapping(value = "/rest/players", method = RequestMethod.POST)
    public ResponseEntity<String> addPlayer(@RequestBody String jsonString) throws IOException {
        ObjectMapper mapper0 = new ObjectMapper();
        Player player = mapper0.readValue(jsonString, Player.class);
        if(player.name == null || player.title == null || player.race == null
                || player.profession == null
                || player.birthday == null || player.experience == null || player.name.length() > 12
                || player.title.length() > 30 || player.name.equals("") || player.experience < 0
                || player.experience > 10_000_000 || player.getBirthday() < 0
                || player.birthday.getYear() + 1900 < 2000 || player.birthday.getYear() + 1900 > 3000)
        {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "BAD_REQUEST"
            );
        }
        if(player.banned == null) player.banned = false;
        updateLevelPlayer(player);
        ModelAndView modelAndView = new ModelAndView();
            modelAndView.setViewName("index");
            modelAndView.addObject("page", page);
            playerService.add(player);
        modelAndView.addObject("player", player);
        StringWriter writer = new StringWriter();
        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(writer, player);
        String result = writer.toString();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "text/html; charset=utf-8");
        return new ResponseEntity<>(
                result,
                headers,
                HttpStatus.OK);
    }

    @RequestMapping(value = "/rest/players/{id}", method = RequestMethod.POST)
    public ResponseEntity<String> editPlayer(@PathVariable("id") String id, @RequestBody String jsonString) throws IOException {
        Long lid = validId(id);
        Player player = playerService.getById(lid);
        if (player == null) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "entity not found"
            );
        }

        if (jsonString.equals("{}")){
            StringWriter writer = new StringWriter();
            ObjectMapper mapper = new ObjectMapper();
            mapper.writeValue(writer, player);
            String result = writer.toString();
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Type", "text/html; charset=utf-8");
            return new ResponseEntity<>(
                    result,
                    headers,
                    HttpStatus.OK);
        }

        ObjectMapper mapper0 = new ObjectMapper();
        Player playerNew;
        try{
            playerNew = mapper0.readValue(jsonString, Player.class);
            final ObjectNode node = new ObjectMapper().readValue(jsonString, ObjectNode.class);
            if (node.has("name")) {
                if (node.get("name").toString().replace("\"","").isEmpty()){
                    throw new Exception();
                }
            }
        }catch (Exception e){
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "BAD_REQUEST"
            );
        }
        if((playerNew.name != null && playerNew.name.length() > 12)
                || (playerNew.title != null && playerNew.title.length() > 30) || (playerNew.experience != null && playerNew.experience < 0)
                || (playerNew.experience != null && playerNew.experience > 10_000_000) || (playerNew.getBirthday() != null && playerNew.getBirthday() < 0)
                || (playerNew.getBirthday() != null && playerNew.birthday.getYear() + 1900 < 2000) || (playerNew.getBirthday() != null && playerNew.birthday.getYear() + 1900 > 3000))
        {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "BAD_REQUEST"
            );
        }
        if (playerNew.getName() != null) player.setName(playerNew.getName());
        if (playerNew.getTitle() != null) player.setTitle(playerNew.getTitle());
        if (playerNew.getRace() != null) player.setRace(playerNew.getRace());
        if (playerNew.getProfession() != null) player.setProfession(playerNew.getProfession());
        if (playerNew.getBirthday() != null) player.setBirthday(playerNew.getBirthday());
        if (playerNew.getBanned() != null) player.setBanned(playerNew.getBanned());
        if (playerNew.getExperience() != null) player.setExperience(playerNew.getExperience());
        updateLevelPlayer(player);

        playerService.edit(player);
        StringWriter writer = new StringWriter();
        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(writer, player);
        String result = writer.toString();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "text/html; charset=utf-8");
        return new ResponseEntity<>(
                result,
                headers,
                HttpStatus.OK);
    }

    private void updateLevelPlayer(Player player) {
        player.setLevel((int)(Math.sqrt(200 * player.getExperience() + 2500) - 50)/100);
        player.setUntilNextLevel(50 * (player.level + 1) * (player.level + 2) - player.experience);
    }

    private Long validId(String id){
        Long lid = 0L;
        try{
            lid = Long.parseLong(id);
            if(lid <= 0) {
                throw new NumberFormatException();
            }
        }catch (NumberFormatException e){
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "BAD REQUEST"
            );
        }
        return lid;
    }

//    public static void main(String[] args) {
//        System.out.println(Long.parseLong("123.23"));
//    }
}
