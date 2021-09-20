package com.gmail.creativegeeksuresh.crud.user.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.gmail.creativegeeksuresh.crud.user.dto.CustomErrorResponse;
import com.gmail.creativegeeksuresh.crud.user.dto.UserDto;
import com.gmail.creativegeeksuresh.crud.user.exception.InvalidCredentialsException;
import com.gmail.creativegeeksuresh.crud.user.exception.InvalidUserException;
import com.gmail.creativegeeksuresh.crud.user.exception.UserAlreadyExistsException;
import com.gmail.creativegeeksuresh.crud.user.model.User;
import com.gmail.creativegeeksuresh.crud.user.service.UserService;
import com.gmail.creativegeeksuresh.crud.user.service.util.CustomJwtService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/user")
public class ApiController {

    @Autowired
    UserService userService;

    @Autowired
    CustomJwtService customJwtService;

    @PostMapping(value = "/create-account")
    public ResponseEntity<?> createUserAccount(@RequestBody UserDto request) {
        try {
            return new ResponseEntity<>(userService.createUser(request), HttpStatus.CREATED);
        } catch (UserAlreadyExistsException uaex) {
            System.err.println(uaex.getLocalizedMessage());
            return new ResponseEntity<>(
                    new CustomErrorResponse(HttpStatus.CONFLICT.value(), uaex.getLocalizedMessage(), ""),
                    HttpStatus.CONFLICT);
        } catch (Exception ex) {
            System.err.println(ex.getLocalizedMessage());
            return new ResponseEntity<>(
                    new CustomErrorResponse(HttpStatus.BAD_REQUEST.value(), ex.getLocalizedMessage(), ""),
                    HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping(value = "/login")
    public ResponseEntity<?> loginUserAccount(@RequestBody UserDto request) {
        try {
            return new ResponseEntity<>(userService.validateUserCredentials(request), HttpStatus.OK);
        } catch (UserAlreadyExistsException uaex) {
            System.err.println(uaex.getLocalizedMessage());
            return new ResponseEntity<>(
                    new CustomErrorResponse(HttpStatus.CONFLICT.value(), uaex.getLocalizedMessage(), ""),
                    HttpStatus.CONFLICT);
        } catch (InvalidUserException iuex) {
            System.err.println(iuex.getLocalizedMessage());
            return new ResponseEntity<>(
                    new CustomErrorResponse(HttpStatus.UNAUTHORIZED.value(), iuex.getLocalizedMessage(), ""),
                    HttpStatus.UNAUTHORIZED);
        }catch (InvalidCredentialsException icex) {
            System.err.println(icex.getLocalizedMessage());
            return new ResponseEntity<>(
                    new CustomErrorResponse(HttpStatus.UNAUTHORIZED.value(), icex.getLocalizedMessage(), ""),
                    HttpStatus.UNAUTHORIZED);
        } 
        catch (Exception ex) {
            System.err.println(ex.getLocalizedMessage());
            return new ResponseEntity<>(
                    new CustomErrorResponse(HttpStatus.BAD_REQUEST.value(), ex.getLocalizedMessage(), ""),
                    HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(value = "/view-all")
    public ResponseEntity<?> getAllUsers() {
        try {
            // To get all users with encrypted password use getAllUsers()
            // To get all users with empty password field use getAllUsersWithoutPassword()
            return new ResponseEntity<>(userService.getAllUsersWithoutPassword(), HttpStatus.OK);
        } catch (InvalidUserException iuex) {
            System.err.println(iuex.getLocalizedMessage());
            return new ResponseEntity<>(
                    new CustomErrorResponse(HttpStatus.CONFLICT.value(), iuex.getLocalizedMessage(), ""),
                    HttpStatus.CONFLICT);
        } catch (Exception ex) {
            System.err.println(ex.getLocalizedMessage());
            return new ResponseEntity<>(
                    new CustomErrorResponse(HttpStatus.BAD_REQUEST.value(), ex.getLocalizedMessage(), ""),
                    HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(value = "/view/{uid}")
    public ResponseEntity<?> getUserByUid(@PathVariable(value = "uid") String uid) {
        try {
            // To get user with encrypted password use findByUid()
            // To get user with empty password field use findByUidWithoutPassword()
            User user = userService.findByUidWithoutPassword(uid);
            if (user != null)
                return new ResponseEntity<>(user, HttpStatus.OK);
            else
                throw new InvalidUserException("User does not exists");
        } catch (InvalidUserException iuex) {
            System.err.println(iuex.getLocalizedMessage());
            return new ResponseEntity<>(
                    new CustomErrorResponse(HttpStatus.CONFLICT.value(), iuex.getLocalizedMessage(), ""),
                    HttpStatus.CONFLICT);
        } catch (Exception ex) {
            System.err.println(ex.getLocalizedMessage());
            return new ResponseEntity<>(
                    new CustomErrorResponse(HttpStatus.BAD_REQUEST.value(), ex.getLocalizedMessage(), ""),
                    HttpStatus.BAD_REQUEST);
        }
    }

    @PatchMapping(value = "/update")
    public ResponseEntity<?> updateUser(@RequestBody User user) {
        try {
            return new ResponseEntity<>(userService.updateUser(user), HttpStatus.OK);
        } catch (InvalidUserException iuex) {
            System.err.println(iuex.getLocalizedMessage());
            return new ResponseEntity<>(
                    new CustomErrorResponse(HttpStatus.CONFLICT.value(), iuex.getLocalizedMessage(), ""),
                    HttpStatus.CONFLICT);
        } catch (Exception ex) {
            System.err.println(ex.getLocalizedMessage());
            return new ResponseEntity<>(
                    new CustomErrorResponse(HttpStatus.BAD_REQUEST.value(), ex.getLocalizedMessage(), ""),
                    HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping(value = "/delete/{uid}")
    public ResponseEntity<?> deleteUserByUid(@PathVariable(value = "uid") String uid) {
        try {
            userService.deleteByUid(uid);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (InvalidUserException iuex) {
            System.err.println(iuex.getLocalizedMessage());
            return new ResponseEntity<>(
                    new CustomErrorResponse(HttpStatus.CONFLICT.value(), iuex.getLocalizedMessage(), ""),
                    HttpStatus.CONFLICT);
        } catch (Exception ex) {
            System.err.println(ex.getLocalizedMessage());
            return new ResponseEntity<>(
                    new CustomErrorResponse(HttpStatus.BAD_REQUEST.value(), ex.getLocalizedMessage(), ""),
                    HttpStatus.BAD_REQUEST);
        }
    }


    @PostMapping(value = "/create-jwt-token")
    public ResponseEntity<Map<String, Object>> createSignedJwtTokenRequest(@RequestBody Map<String, Object> request) {
        Map<String, Object> response = new HashMap<>();
        try {
            response.put("token", customJwtService.generateSignedJwtToken(request));
            response.put("createdAt", new Date().toString());
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (Exception e) {
            e.printStackTrace();
            response.put("errorInfo", e.getLocalizedMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping(value = "/verify-jwt-token")
    public ResponseEntity<Map<String, Object>> verifyAndParseJwtToken(@RequestBody Map<String, Object> request) {
        Map<String, Object> response = new HashMap<>();
        try {
            response = customJwtService.verifyJwtTokenAndGetValue((String) request.get("token"));
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            response.put("errorInfo", e.getLocalizedMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

}

// @PostMapping(value = "/admin/delete-user")
// public ResponseEntity<?> deleteUserAccount(@RequestParam String username) {
// try {
// userService.deleteUserByUsername(username);
// return new ResponseEntity<>(new HashMap<>(), HttpStatus.OK);
// } catch (UserAlreadyExistsException uaex) {
// System.err.println(uaex.getLocalizedMessage());
// return new ResponseEntity<>(uaex.getLocalizedMessage(), HttpStatus.CONFLICT);
// } catch (Exception ex) {
// System.err.println(ex.getLocalizedMessage());
// return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
// }
// }