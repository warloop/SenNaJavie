package org.example.forum.dao.Interfaces;

import org.example.forum.entities.AccountType;

import java.util.Optional;

public interface IAccountTypeDao {

    AccountType get(int id);

    Optional<Integer> add(AccountType accountType);

    Boolean update(AccountType accountType);

    Boolean delete(int id);

}
