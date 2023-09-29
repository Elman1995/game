package com.game.controller;

import com.game.dto.PlayerDto;
import com.game.entity.Profession;
import com.game.entity.Race;
import com.game.service.PlayerService;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.criteria.CriteriaBuilder;
import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/rest/players")
public class PlayerController {

    private final Date date_start_of_restriction = new java.text.SimpleDateFormat ("dd/MM/yyyy HH:mm:ss").parse("01/01/2000 01:00:00");
    private final Date date_end_of_restriction = new java.text.SimpleDateFormat ("dd/MM/yyyy HH:mm:ss").parse("01/01/3000 01:00:00");
    private final PlayerService playerService;

    @Autowired
    public PlayerController(PlayerService playerService) throws ParseException {
        this.playerService = playerService;
    }

    //@GetMapping("{name, title}")
    @GetMapping
    public ResponseEntity<List<PlayerDto>> getPlayers(@RequestParam(required = false) String name,
                                                      @RequestParam(required = false) String title,
                                                      @RequestParam(required = false) Race race,
                                                      @RequestParam(required = false) Profession profession,
                                                      @RequestParam(required = false) Long after,
                                                      @RequestParam(required = false) Long before,
                                                      @RequestParam(required = false) Boolean banned,
                                                      @RequestParam(required = false) Integer minExperience,
                                                      @RequestParam(required = false) Integer maxExperience,
                                                      @RequestParam(required = false) Integer minLevel,
                                                      @RequestParam(required = false) Integer maxLevel,
                                                      @RequestParam(required = false) PlayerOrder order,
                                                      @RequestParam(required = false) Integer pageNumber,
                                                      @RequestParam(required = false) Integer pageSize
                                                      ) {
        return new ResponseEntity<>(playerService.getPlayer(name, title, race, profession, after, before, banned,
                minExperience, maxExperience, minLevel, maxLevel, order, pageNumber, pageSize), HttpStatus.OK);
    }

    @GetMapping("/count")
    public ResponseEntity<Integer> getPlayersCount(@RequestParam(required = false) String name,
                                                   @RequestParam(required = false) String title,
                                                   @RequestParam(required = false) Race race,
                                                   @RequestParam(required = false) Profession profession,
                                                   @RequestParam(required = false) Long after,
                                                   @RequestParam(required = false) Long before,
                                                   @RequestParam(required = false) Boolean banned,
                                                   @RequestParam(required = false) Integer minExperience,
                                                   @RequestParam(required = false) Integer maxExperience,
                                                   @RequestParam(required = false) Integer minLevel,
                                                   @RequestParam(required = false) Integer maxLevel
                                                   ) {
        return new ResponseEntity<>(playerService.getPlayersCount(name, title, race, profession, after, before, banned,
                minExperience, maxExperience, minLevel, maxLevel), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<PlayerDto> createPlayers(@RequestBody PlayerDto playerDto) {
        if(playerDto == null
                || playerDto.getName() == null
                || playerDto.getTitle() == null
                || playerDto.getBirthday() == null
                || playerDto.getName().length() > 12
                || playerDto.getTitle().length() > 30
                || playerDto.getName().isEmpty()
                || playerDto.getExperience() == null
                || playerDto.getExperience() < 0 || playerDto.getExperience() > 10000000
                || playerDto.getBirthday().getTime() < 0
                || playerDto.getBirthday().getTime() < date_start_of_restriction.getTime()
                || playerDto.getBirthday().getTime() > date_end_of_restriction.getTime()) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(playerService.save(playerDto), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PlayerDto> getPlayers(@PathVariable(name = "id") Long id) {
        if(id < 1)
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        PlayerDto playerDto = playerService.getPlayer(id);
        return ((playerDto != null) ? new ResponseEntity<>(playerDto, HttpStatus.OK) : new ResponseEntity<>(null, HttpStatus.NOT_FOUND));
    }

    @PostMapping("/{id}")
    public ResponseEntity<PlayerDto> updatePlayers(@PathVariable(name = "id") Long id, @RequestBody PlayerDto playerDto) {
        if(id < 1 || playerDto == null
                || (playerDto.getName() != null && (playerDto.getName().length() > 12 || playerDto.getName().isEmpty()))
                || (playerDto.getTitle() != null && playerDto.getTitle().length() > 30)
                || (playerDto.getBirthday() != null && (playerDto.getBirthday().getTime() < 0
                                            || playerDto.getBirthday().getTime() < date_start_of_restriction.getTime()
                                            || playerDto.getBirthday().getTime() > date_end_of_restriction.getTime()))
                || (playerDto.getExperience() != null && (playerDto.getExperience() < 0 || playerDto.getExperience() > 10000000)))
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        try {
            return new ResponseEntity<>(playerService.update(id, playerDto), HttpStatus.OK);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<PlayerDto> deletePlayers(@PathVariable(name = "id") Long id) {
        if(id < 1)
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        try {
            playerService.deletePlayers(id);
            return new ResponseEntity<>(null, HttpStatus.OK);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

}
