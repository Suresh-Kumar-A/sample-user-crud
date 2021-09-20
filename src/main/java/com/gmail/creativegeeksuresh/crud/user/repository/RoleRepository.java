package com.gmail.creativegeeksuresh.crud.user.repository;

import com.gmail.creativegeeksuresh.crud.user.model.Role;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends CrudRepository<Role, Integer> {
  public Role findByRoleName(String roleName);
}
