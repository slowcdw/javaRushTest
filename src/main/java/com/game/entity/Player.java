package com.game.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.Objects;

@JsonIgnoreProperties(ignoreUnknown = true)
@Entity
@Table(name = "player")
public class Player {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @Column(name = "name")
    public String name;

    @Column(name = "title")
    public String title;

    @Column(name = "race")
    public String race;

    @Column(name = "profession")
    public String profession;

    @Column(name = "birthday")
    public Date birthday;

    @Column(name = "banned")
    public Boolean banned;

    @Column(name = "experience")
    public Integer experience;

    @Column(name = "level")
    public Integer level;

    @Column(name = "untilNextLevel")
    public Integer untilNextLevel;

    public Player() {
    }

    public Player(Long id, String name, String title, Race race, Profession profession, Date birthday,
                          Boolean banned, Integer experience, Integer level, Integer untilNextLevel) {
        this.id = id;
        this.name = name;
        this.title = title;
        this.race = race.toString();
        this.profession = profession.toString();
        this.birthday = birthday;
        this.banned = banned;
        this.experience = experience;
        this.level = level;
        this.untilNextLevel = untilNextLevel;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Player that = (Player) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(name, that.name) &&
                Objects.equals(title, that.title) &&
                race == that.race &&
                profession == that.profession &&
                Objects.equals(birthday, that.birthday) &&
                Objects.equals(banned, that.banned) &&
                Objects.equals(experience, that.experience) &&
                Objects.equals(level, that.level) &&
                Objects.equals(untilNextLevel, that.untilNextLevel);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, title, race, profession, birthday, banned, experience, level, untilNextLevel);
    }

    @Override
    public String toString() {
        LocalDate birthday = Instant.ofEpochMilli(this.birthday.getTime()).atZone(ZoneId.systemDefault()).toLocalDate();
        return "Player{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", title='" + title + '\'' +
                ", race='" + race + '\'' +
                ", profession='" + profession + '\'' +
                ", birthday=" + birthday +
                ", banned=" + banned +
                ", experience=" + experience +
                ", level=" + level +
                ", untilNextLevel=" + untilNextLevel +
                '}';
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getRace() {
        return race;
    }

    public void setRace(String race) {
        this.race = race;
    }

    public String getProfession() {
        return profession;
    }

    public void setBirthday(Long birthday) {
        this.birthday = new Date(birthday);
    }

    public void setProfession(String profession) {
        this.profession = profession;
    }

    public Long getBirthday() {
//        DateFormat df = new SimpleDateFormat("dd.MM.yyyy");
//        return df.format(birthday.getTime());
        if (birthday == null) return null;
        return birthday.getTime();
    }


    public Boolean getBanned() {
        return banned;
    }

    public void setBanned(Boolean banned) {
        this.banned = banned;
    }

    public Integer getExperience() {
        return experience;
    }

    public void setExperience(Integer experience) {
        this.experience = experience;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public Integer getUntilNextLevel() {
        return untilNextLevel;
    }

    public void setUntilNextLevel(Integer untilNextLevel) {
        this.untilNextLevel = untilNextLevel;
    }
}
