package org.example.forum.dao.Interfaces;

import org.example.forum.entities.Action_types;

import java.util.Optional;

public interface IActionTypesDao {

    Action_types get(int id);

    Optional<Integer> add(Action_types actionTypes);

    Boolean update(Action_types actionTypes);

    Boolean delete(int id);

}
