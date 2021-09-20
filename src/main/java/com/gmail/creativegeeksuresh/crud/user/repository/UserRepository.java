package com.gmail.creativegeeksuresh.crud.user.repository;

import com.gmail.creativegeeksuresh.crud.user.model.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<User, Integer> {
  public User findByUid(String uid);

  public User findByUsername(String username);

  public User findByEmail(String email);

  public User findByUsernameOrEmail(String username, String email);
}
