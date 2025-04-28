package com.github.paulovieirajr.meetime.rest.client;

import com.github.paulovieirajr.meetime.rest.dto.HubSpotTokenResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "hubSpotOAuthClient", url = "${hubspot.url.token}")
public interface HubSpotOAuthClient {

    @PostMapping(consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    HubSpotTokenResponse exchangeToken(@RequestBody MultiValueMap<String, String> body);

}
