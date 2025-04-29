package com.github.paulovieirajr.meetime.rest.controller.contact;

import com.github.paulovieirajr.meetime.rest.dto.HubSpotContactRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import static com.github.paulovieirajr.meetime.rest.constants.HubSpotHeader.AUTHORIZATION_HEADER;

@Tag(name = "HubSpot Contact", description = "HubSpot Contact API")
public interface HubSpotContactControllerSwagger {

    @Operation(
            summary = "Create a new contact",
            description = "Create a new contact in HubSpot",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponse(responseCode = "201", description = "Contact created successfully")
    @ApiResponse(responseCode = "400", description = "Bad request")
    @ApiResponse(responseCode = "401", description = "Unauthorized")
    @ApiResponse(responseCode = "403", description = "Forbidden")
    @ApiResponse(responseCode = "409", description = "Conflict")
    @ApiResponse(responseCode = "422", description = "Unprocessable entity")
    @ApiResponse(responseCode = "500", description = "Internal server error")
    @ApiResponse(responseCode = "503", description = "Service unavailable")
    ResponseEntity<?> createContact(@RequestHeader(AUTHORIZATION_HEADER) String bearerToken,
                                    @Valid @RequestBody HubSpotContactRequest contactData
    );
}
