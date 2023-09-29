package com.game.service.impl;

import com.game.controller.PlayerOrder;
import com.game.dto.PlayerDto;
import com.game.entity.Player;
import com.game.entity.Profession;
import com.game.entity.Race;
import com.game.repository.PlayerRepository;
import com.game.service.PlayerService;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class PlayerServiceImpl implements PlayerService {

    private final PlayerRepository playerRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    public PlayerServiceImpl(PlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
    }

    private int characterLevelCalculation(int experience) {
        return (int) ((Math.sqrt(2500 + (200 * experience)) - 50) / 100);
    }

    private int characterExperienceCalculation(int experience, int level) {
        return Math.abs(50 * (level + 1) * (level + 2) - experience);
    }

    @Override
    public PlayerDto getPlayer(Long id) {
        Optional<Player> player = playerRepository.findById(id);
        return ((player.isPresent()) ? new PlayerDto(player.get()) : null);
    }

    @Override
    public List<PlayerDto> getPlayer(String name, String title, Race race, Profession profession, Long after,
                                     Long before, Boolean banned, Integer minExperience, Integer maxExperience,
                                     Integer minLevel, Integer maxLevel, PlayerOrder order, Integer pageNumber,
                                     Integer pageSize) {

        Pageable firstPageWithTwoElements = PageRequest.of((pageNumber == null ? 0 : (int) pageNumber),
                (pageSize == null ? 3 : (int) pageSize),
                Sort.by((order == null ? PlayerOrder.ID.getFieldName() : order.getFieldName())));

        return playerRepository.getPlayersCustom(firstPageWithTwoElements, name, title, race, profession, after, before,
                        banned, minExperience, maxExperience, minLevel, maxLevel).
                stream().map(x -> new PlayerDto(x)).collect(Collectors.toList());
    }

    @Override
    public int getPlayersCount(String name, String title, Race race, Profession profession, Long after,
                               Long before, Boolean banned, Integer minExperience, Integer maxExperience,
                               Integer minLevel, Integer maxLevel) {
        return Math.toIntExact(playerRepository.getPlayersCountCustom(name, title, race, profession, after, before,
                banned, minExperience, maxExperience, minLevel, maxLevel));
    }

    @Override
    @Transactional
    public PlayerDto save(PlayerDto playerDto) {
        Player newPlayer = new Player();
        newPlayer.setName(playerDto.getName());
        newPlayer.setTitle(playerDto.getTitle());
        newPlayer.setRace(playerDto.getRace().name());
        newPlayer.setProfession(playerDto.getProfession().name());
        newPlayer.setBirthday(playerDto.getBirthday());
        newPlayer.setBanned(playerDto.isBanned());
        newPlayer.setExperience(playerDto.getExperience());
        newPlayer.setLevel(characterLevelCalculation(playerDto.getExperience()));
        newPlayer.setUntilNextLevel(characterExperienceCalculation(playerDto.getExperience(), newPlayer.getLevel()));

        return new PlayerDto(playerRepository.save(newPlayer));
    }

    @Override
    @Transactional
    public PlayerDto update(Long id, PlayerDto playerDto) throws NotFoundException {
        Optional<Player> player = playerRepository.findById(id);
        if (!player.isPresent())
            throw new NotFoundException("Player not found !");

        Player oldPlayer = player.get();
        if (playerDto.getName() != null)
            oldPlayer.setName(playerDto.getName());
        if (playerDto.getTitle() != null)
            oldPlayer.setTitle(playerDto.getTitle());
        if (playerDto.getRace() != null)
            oldPlayer.setRace(playerDto.getRace().name());
        if (playerDto.getProfession() != null)
            oldPlayer.setProfession(playerDto.getProfession().name());
        if (playerDto.getBirthday() != null)
            oldPlayer.setBirthday(playerDto.getBirthday());
        if (playerDto.isBanned() != null)
            oldPlayer.setBanned(playerDto.isBanned());
        if (playerDto.getExperience() != null) {
            oldPlayer.setExperience(playerDto.getExperience());
            oldPlayer.setLevel(characterLevelCalculation(playerDto.getExperience()));
            oldPlayer.setUntilNextLevel(characterExperienceCalculation(playerDto.getExperience(), oldPlayer.getLevel()));
        }
        return new PlayerDto(playerRepository.save(oldPlayer));
    }

    @Override
    @Transactional
    public void deletePlayers(Long id) throws NotFoundException {
        Optional<Player> player = playerRepository.findById(id);
        if (!player.isPresent())
            throw new NotFoundException("Player not found !");
        Player oldPlayer = player.get();
        playerRepository.delete(oldPlayer);
    }


}
