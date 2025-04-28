package com.github.paulovieirajr.meetime.rest.client;

import com.github.paulovieirajr.meetime.rest.dto.HubSpotContactResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.Map;

import static com.github.paulovieirajr.meetime.rest.constants.HubSpotHeader.AUTHORIZATION_HEADER;

@FeignClient(name = "hubSpotContactClient", url = "${hubspot.url.create-contact}")
public interface HubSpotContactClient {

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    HubSpotContactResponse createContact(
            @RequestHeader(AUTHORIZATION_HEADER) String bearerToken,
            @RequestBody Map<String, Object> body);

}
