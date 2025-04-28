package com.github.paulovieirajr.meetime.rest.controller.contact;

import com.github.paulovieirajr.meetime.rest.client.HubSpotContactClient;
import com.github.paulovieirajr.meetime.rest.dto.HubSpotContactRequest;
import com.github.paulovieirajr.meetime.rest.dto.HubSpotContactResponse;
import com.github.paulovieirajr.meetime.rest.exception.common.BearerTokenInvalidException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

import static com.github.paulovieirajr.meetime.rest.constants.HubSpotParams.PROPERTIES;
import static com.github.paulovieirajr.meetime.rest.constants.HubSpotPathController.CREATE_CONTACT_PATH;
import static com.github.paulovieirajr.meetime.rest.constants.MessageError.INVALID_BEARER_TOKEN;

@RestController
@Validated
public class HubSpotContactController implements HubSpotContactControllerSwagger{

    public static final String BEARER_TOKEN_TYPE = "Bearer ";

    private final HubSpotContactClient hubSpotContactClient;

    public HubSpotContactController(HubSpotContactClient hubSpotContactClient) {
        this.hubSpotContactClient = hubSpotContactClient;
    }

    @PostMapping(CREATE_CONTACT_PATH)
    public ResponseEntity<?> createContact(String bearerToken, HubSpotContactRequest contactData) {
        if (!bearerToken.startsWith(BEARER_TOKEN_TYPE)) {
            throw new BearerTokenInvalidException(INVALID_BEARER_TOKEN.getMessage());
        }

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put(PROPERTIES.getValue(), contactData);

        HubSpotContactResponse contactCreated = hubSpotContactClient.createContact(bearerToken, requestBody);
        return ResponseEntity.status(HttpStatus.CREATED).body(contactCreated);
    }
}
