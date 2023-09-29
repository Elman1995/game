package com.game.service;

import com.game.controller.PlayerOrder;
import com.game.dto.PlayerDto;
import com.game.entity.Profession;
import com.game.entity.Race;
import javassist.NotFoundException;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

public interface PlayerService {
    PlayerDto getPlayer(Long id);

    List<PlayerDto> getPlayer(String name, String title, Race race, Profession profession, Long after, Long before,
                              Boolean banned, Integer minExperience, Integer maxExperience, Integer minLevel,
                              Integer maxLevel, PlayerOrder order, Integer pageNumber, Integer pageSize);

    int getPlayersCount(String name, String title, Race race, Profession profession, Long after, Long before,
                        Boolean banned, Integer minExperience, Integer maxExperience, Integer minLevel,
                        Integer maxLevel);

    PlayerDto save(PlayerDto playerDto);

    PlayerDto update(Long id, PlayerDto playerDto) throws NotFoundException;

    void deletePlayers(Long id) throws NotFoundException;


}
