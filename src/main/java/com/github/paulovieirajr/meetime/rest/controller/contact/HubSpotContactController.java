package com.github.paulovieirajr.meetime.rest.controller.contact;

import com.github.paulovieirajr.meetime.rest.client.HubSpotContactClient;
import com.github.paulovieirajr.meetime.rest.dto.HubSpotContactRequest;
import com.github.paulovieirajr.meetime.rest.dto.HubSpotContactResponse;
import com.github.paulovieirajr.meetime.rest.exception.common.BearerTokenInvalidException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import static com.github.paulovieirajr.meetime.rest.controller.contact.log.ContactLogger.*;

@RestController
@Validated
public class HubSpotContactController implements HubSpotContactControllerSwagger {

    private static final Logger LOGGER = LoggerFactory.getLogger(HubSpotContactController.class);

    public static final String BEARER_TOKEN_TYPE = "Bearer ";

    private final HubSpotContactClient hubSpotContactClient;

    public HubSpotContactController(HubSpotContactClient hubSpotContactClient) {
        this.hubSpotContactClient = hubSpotContactClient;
    }

    @PostMapping(CREATE_CONTACT_PATH)
    public ResponseEntity<?> createContact(String bearerToken, HubSpotContactRequest contactData) {
        LOGGER.info(LOG_CREATE_CONTACT_ENDPOINT.getMessage());
        if (!bearerToken.startsWith(BEARER_TOKEN_TYPE)) {
            LOGGER.error(LOG_BEARER_TOKEN_INVALID.getMessage());
            throw new BearerTokenInvalidException(INVALID_BEARER_TOKEN.getMessage());
        }

        LOGGER.info(LOG_BUILD_REQUEST_BODY.getMessage());
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put(PROPERTIES.getValue(), contactData);

        LOGGER.info(LOG_SEND_REQUEST.getMessage());
        HubSpotContactResponse contactCreated = hubSpotContactClient.createContact(bearerToken, requestBody);
        LOGGER.info(LOG_CONTACT_CREATED_SUCCESS.getMessage(), contactCreated);
        return ResponseEntity.status(HttpStatus.CREATED).body(contactCreated);
    }
}
