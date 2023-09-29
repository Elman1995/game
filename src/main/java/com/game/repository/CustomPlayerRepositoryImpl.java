package com.game.repository;

import com.game.entity.Player;
import com.game.entity.Profession;
import com.game.entity.Race;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class CustomPlayerRepositoryImpl implements CustomPlayerRepository{
    @PersistenceContext
    private EntityManager em;

    @Override
    public Page<Player> getPlayersCustom(Pageable pageable, String name, String title, Race race, Profession profession,
                                         Long after, Long before, Boolean banned, Integer minExperience,
                                         Integer maxExperience, Integer minLevel, Integer maxLevel) {

        Query query = em.createQuery(collectQuery(false, name, title, race, profession,
                after, before, banned, minExperience,
                maxExperience, minLevel, maxLevel));
        int pageNumber = pageable.getPageNumber();
        int pageSize = pageable.getPageSize();
        query.setFirstResult((pageNumber) * pageSize);
        query.setMaxResults(pageSize);
        List<Player> animals = query.getResultList();
        Query queryCount = em.createQuery(collectQuery(true, name, title, race, profession,
                after, before, banned, minExperience,
                maxExperience, minLevel, maxLevel));
        long count = (long) queryCount.getSingleResult();
        return new PageImpl<Player>(animals, pageable, count);
    }

    @Override
    public Long getPlayersCountCustom(String name, String title, Race race, Profession profession, Long after, Long before, Boolean banned, Integer minExperience, Integer maxExperience, Integer minLevel, Integer maxLevel) {
        Query queryCount = em.createQuery(collectQuery(true, name, title, race, profession,
                after, before, banned, minExperience, maxExperience, minLevel, maxLevel));
        long count = (long) queryCount.getSingleResult();
        return count;
    }

    private String collectQuery(boolean creatingRequestQuantity, String name, String title, Race race, Profession profession,
                                Long after, Long before, Boolean banned, Integer minExperience,
                                Integer maxExperience, Integer minLevel, Integer maxLevel) {

        SimpleDateFormat formater = new SimpleDateFormat("yyyy-MM-dd");
        StringBuilder sqlQuery = null;

        if (creatingRequestQuantity) {
            sqlQuery = new StringBuilder("Select count(p.id) From Player p");
        } else {
            sqlQuery = new StringBuilder("select p from Player p");
        }

        if (name != null)
            addingQueryCondition(sqlQuery, "p.name LIKE '%" + name + "%'");
        if (title != null)
            addingQueryCondition(sqlQuery, "p.title LIKE '%" + title + "%'");
        if (race != null)
            addingQueryCondition(sqlQuery, "p.race = '" + race.name() + "'");
        if (profession != null)
            addingQueryCondition(sqlQuery, "p.profession = '" + profession.name() + "'");
        if (after != null)
            addingQueryCondition(sqlQuery, "p.birthday >= '" + formater.format(new Date(after)) + "'");
        if (before != null)
            addingQueryCondition(sqlQuery, "p.birthday <= '" + formater.format(new Date(before)) + "'");
        if (banned != null)
            addingQueryCondition(sqlQuery, "p.banned = " + banned + "");
        if (minExperience != null)
            addingQueryCondition(sqlQuery, "p.experience >= " + minExperience.intValue() + "");
        if (maxExperience != null)
            addingQueryCondition(sqlQuery, "p.experience <= " + maxExperience.intValue() + "");
        if (minLevel != null)
            addingQueryCondition(sqlQuery, "p.level >= " + minLevel.intValue() + "");
        if (maxLevel != null)
            addingQueryCondition(sqlQuery, "p.level <= " + maxLevel.intValue() + "");
        return sqlQuery.toString();
    }

    private void addingQueryCondition(StringBuilder sqlQueryAdd, String s) {
        if (sqlQueryAdd.indexOf("WHERE") != -1) {
            sqlQueryAdd.append(" AND " + s);
        } else {
            sqlQueryAdd.append(" WHERE " + s);
        }
    }
}
