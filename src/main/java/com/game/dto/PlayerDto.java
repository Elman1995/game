package com.game.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.game.entity.Player;
import com.game.entity.Profession;
import com.game.entity.Race;

import javax.persistence.Column;
import java.util.Arrays;
import java.util.Date;

public class PlayerDto {

    @JsonProperty(Fields.ID)
    private Long id;

    @JsonProperty(Fields.NAME)
    private String name;

    @JsonProperty(Fields.TITLE)
    private String title;

    @JsonProperty(Fields.RACE)
    private Race race;

    @JsonProperty(Fields.PROFESSION)
    private Profession profession;

    @JsonProperty(Fields.BIRTHDAY)
    private Date birthday;

    @JsonProperty(Fields.BANNED)
    private Boolean banned;

    @JsonProperty(Fields.EXPERIENCE)
    private Integer experience;

    @JsonProperty(Fields.LEVEL)
    private int level;

    @JsonProperty(Fields.UNTILNEXTLEVEL)
    private int untilNextLevel;

    public PlayerDto(Long id, String name, String title, Race race, Profession profession, Date birthday, boolean banned, int experience, int level, int untilNextLevel) {
        this.id = id;
        this.name = name;
        this.title = title;
        this.race = race;
        this.profession = profession;
        this.birthday = birthday;
        this.banned = banned;
        this.experience = experience;
        this.level = level;
        this.untilNextLevel = untilNextLevel;
    }

    public PlayerDto(Player player) {
        this.id = player.getId();
        this.name = player.getName();
        this.title = player.getTitle();
        this.race = Arrays.stream(Race.values()).filter(x -> x.name().equals(player.getRace())).findAny().get();
        this.profession = Arrays.stream(Profession.values()).filter(x -> x.name().equals(player.getProfession())).findAny().get();
        this.birthday = player.getBirthday();
        this.banned = player.isBanned();
        this.experience = player.getExperience();
        this.level = player.getLevel();
        this.untilNextLevel = player.getUntilNextLevel();
    }

    public PlayerDto() {
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

    public Race getRace() {
        return race;
    }

    public void setRace(Race race) {
        this.race = race;
    }

    public Profession getProfession() {
        return profession;
    }

    public void setProfession(Profession profession) {
        this.profession = profession;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public Boolean isBanned() {
        return banned;
    }

    public void setBanned(boolean banned) {
        this.banned = banned;
    }

    public Integer getExperience() {
        return experience;
    }

    public void setExperience(int experience) {
        this.experience = experience;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getUntilNextLevel() {
        return untilNextLevel;
    }

    public void setUntilNextLevel(int untilNextLevel) {
        this.untilNextLevel = untilNextLevel;
    }

    public static class Fields {

        public static final String ID = "id";
        public static final String NAME = "name";
        public static final String TITLE = "title";
        public static final String RACE = "race";
        public static final String PROFESSION = "profession";
        public static final String BIRTHDAY = "birthday";
        public static final String BANNED = "banned";
        public static final String EXPERIENCE = "experience";
        public static final String LEVEL = "level";
        public static final String UNTILNEXTLEVEL = "untilNextLevel";


    }

}
