package com.game.repository;

import com.game.entity.Player;
import com.game.entity.Profession;
import com.game.entity.Race;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;

public interface CustomPlayerRepository {
    Page<Player> getPlayersCustom(Pageable pageable, String name, String title, Race race, Profession profession,
                                  Long after, Long before, Boolean banned, Integer minExperience, Integer maxExperience,
                                  Integer minLevel, Integer maxLevel);

    Long getPlayersCountCustom(String name, String title, Race race, Profession profession,
                                  Long after, Long before, Boolean banned, Integer minExperience, Integer maxExperience,
                                  Integer minLevel, Integer maxLevel);
}
