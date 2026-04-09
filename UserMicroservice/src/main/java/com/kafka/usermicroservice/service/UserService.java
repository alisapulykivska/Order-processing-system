package com.kafka.usermicroservice.service;

import com.kafka.core.dto.PagedResponse;
import com.kafka.usermicroservice.service.dto.UpdateUserDto;
import com.kafka.usermicroservice.service.dto.UserResponse;
import jakarta.ws.rs.core.Response;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UserService {
    private static final Logger log = LoggerFactory.getLogger(UserService.class);
    private final Keycloak keycloak;
    private final String realm = "orderflow";

    public UserService(Keycloak keycloak) {
        this.keycloak = keycloak;
    }

    public void registerUser(String username, String email, String password) {
        UserRepresentation user = new UserRepresentation();
        user.setEnabled(true);
        user.setUsername(username);
        user.setEmail(email);
        user.setFirstName("FirstName");
        user.setLastName("LastName");
        user.setEmailVerified(true);

        CredentialRepresentation credential = new CredentialRepresentation();
        credential.setType(CredentialRepresentation.PASSWORD);
        credential.setValue(password);
        credential.setTemporary(false);

        List<CredentialRepresentation> list = new ArrayList<>();
        list.add(credential);
        user.setCredentials(list);

        RealmResource realmResource = keycloak.realm(realm);
        UsersResource usersResource = realmResource.users();

        Response response = usersResource.create(user);

        if (response.getStatus() != 201) {
            throw new RuntimeException("Failed : HTTP error code : " + response.getStatus());
        }
    }

    private UsersResource getUsersResource() {
        RealmResource realmResource = keycloak.realm(realm);
        return realmResource.users();
    }

    public UserRepresentation getUserById(String userId){
        return getUsersResource().get(userId).toRepresentation();
    }

    public UserRepresentation updateUser(String userId, UpdateUserDto newUser) {
        UserResource userResource = getUsersResource().get(userId);

        UserRepresentation userRepresentation = userResource.toRepresentation();
        userRepresentation.setEmail(newUser.email());
        userRepresentation.setEnabled(true);

        userResource.update(userRepresentation);

        CredentialRepresentation credential = new CredentialRepresentation();
        credential.setType(CredentialRepresentation.PASSWORD);
        credential.setValue(newUser.password());
        credential.setTemporary(false);

        userResource.resetPassword(credential);

        return userRepresentation;
    }

    public void deleteUserById(String userId){
        getUsersResource().delete(userId);
    }

    public PagedResponse<UserResponse> getAllUsers(int page, int size) {
        UsersResource usersResource = getUsersResource();

        int first = page * size;
        List<UserRepresentation> users = usersResource.list(first, size);

        int total = usersResource.count();

        List<UserResponse> mapped = users.stream()
                .map(u -> new UserResponse(u.getUsername(), u.getEmail()))
                .toList();

        return new PagedResponse<>(mapped, page, size, total);
    }

}
