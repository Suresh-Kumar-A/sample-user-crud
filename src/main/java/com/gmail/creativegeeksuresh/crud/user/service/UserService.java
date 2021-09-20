package com.gmail.creativegeeksuresh.crud.user.service;

import com.gmail.creativegeeksuresh.crud.user.dto.UserDto;
import com.gmail.creativegeeksuresh.crud.user.exception.InvalidCredentialsException;
import com.gmail.creativegeeksuresh.crud.user.exception.InvalidUserException;
import com.gmail.creativegeeksuresh.crud.user.exception.UserAlreadyExistsException;
import com.gmail.creativegeeksuresh.crud.user.model.Role;
import com.gmail.creativegeeksuresh.crud.user.model.User;
import com.gmail.creativegeeksuresh.crud.user.repository.UserRepository;
import com.gmail.creativegeeksuresh.crud.user.service.util.AppConstants;
import com.gmail.creativegeeksuresh.crud.user.service.util.CustomUtils;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
  @Autowired
  private UserRepository userRepository;

  @Autowired
  private CustomUtils customUtils;

  @Autowired
  private RoleService roleService;

  public User createUser(UserDto request) throws UserAlreadyExistsException, Exception {
    if (userRepository.findByUsernameOrEmail(request.getUsername(), request.getEmail()) != null)
      throw new UserAlreadyExistsException("User with similar data exists");
    User newUser = new User();
    newUser.setUsername(request.getUsername());
    newUser.setEmail(request.getEmail());
    newUser.setPassword(customUtils.encodeUsingBcryptPasswordEncoder(request.getPassword()));
    newUser.setUid(customUtils.generateToken());
    newUser.setCreatedAt(new Date());
    newUser.setRole(roleService.findByRoleName(AppConstants.USER_ROLE_STRING));
    return userRepository.save(newUser);
  }

  public User findByUsername(String username) throws InvalidUserException, Exception {
    User user = userRepository.findByUsername(username);
    if (user == null)
      throw new InvalidUserException("User does not exists");
    else
      return user;
  }

  public User findByUsernameOrEmail(String username, String email) throws InvalidUserException, Exception {
    User user = userRepository.findByUsernameOrEmail(username, email);
    if (user == null)
      throw new InvalidUserException("User does not exists");
    else
      return user;
  }

  public User validateUserCredentials(UserDto user)
      throws InvalidCredentialsException, InvalidUserException, Exception {
    User dbUser = findByUsernameOrEmail(user.getUsername(), user.getEmail());
    if (dbUser != null) {
      if (customUtils.verifyUserPassword(user.getPassword(), dbUser.getPassword())) {
        dbUser.setPassword("");
        return dbUser;
      } else
        throw new InvalidCredentialsException("User Credentials are incorrect or invalid");
    } else
      throw new InvalidUserException("User does not exists");
  }

  public void createAdminUser(String adminUsername, String adminPassword, String adminEmail) throws Exception {
    if (userRepository.findByUsername(adminUsername) == null) {
      User adminUser = new User();
      Role role = roleService.findByRoleName(AppConstants.ADMIN_ROLE_STRING);
      adminUser.setUsername(adminUsername);
      adminUser.setEmail(adminEmail);
      adminUser.setPassword(customUtils.encodeUsingBcryptPasswordEncoder(adminPassword));
      adminUser.setUid(customUtils.generateToken());
      adminUser.setCreatedAt(new Date());
      adminUser.setRole(role);
      userRepository.save(adminUser);
    }
  }

  public List<User> getAllUsers() throws Exception {
    return (List<User>) userRepository.findAll();
  }

  public List<User> getAllUsersWithoutPassword() throws Exception {
    List<User> userList = getAllUsers();
    userList.stream().map(user -> {
      user.setPassword("");
      return user;
    }).collect(Collectors.toList());
    return userList;
  }

  public User findByUid(String uid) throws Exception {
    return userRepository.findByUid(uid);
  }

  public User findByUidWithoutPassword(String uid) throws Exception {
    User user = findByUid(uid);
    user.setPassword("");
    return user;
  }

  private void deleteUser(User user) throws Exception {
    userRepository.delete(user);
  }

  public void deleteByUid(String uid) throws InvalidUserException, Exception {
    User user = findByUid(uid);
    if (user != null)
      deleteUser(user);
    else
      throw new InvalidUserException("User Does not Exists");
  }

  public User updateUser(User user) throws InvalidUserException, Exception {
    User temp = findByUid(user.getUid());
    if (temp != null) {
      if (user.getUsername() != null && user.getUsername().trim() != "") {
        temp.setUsername(user.getUsername());
      }
      if (user.getEmail() != null && user.getEmail().trim() != "") {
        temp.setEmail(user.getEmail());
      }
      if (user.getPassword() != null && !user.getPassword().isBlank()) {
        temp.setPassword(customUtils.encodeUsingBcryptPasswordEncoder(user.getPassword()));
      }
      // if (user.getPhonenumber() != null && user.getPhonenumber().trim() != "") {
      // temp.setPhonenumber(user.getPhonenumber());
      // }
      // if (user.getAddress() != null && user.getAddress().trim() != "") {
      // temp.setAddress(user.getAddress());
      // }
      return userRepository.save(temp);
    } else
      throw new InvalidUserException("User Does not Exists");
  }
}
