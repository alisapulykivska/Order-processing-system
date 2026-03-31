package com.kafka.usermicroservice.service;

import jakarta.ws.rs.core.Response;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UserService {
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

    public void deleteUserById(String userId){
        getUsersResource().delete(userId);
    }

}
