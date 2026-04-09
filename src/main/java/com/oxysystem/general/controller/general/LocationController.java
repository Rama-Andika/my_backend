package com.oxysystem.general.controller.general;

import com.oxysystem.general.response.SuccessResponse;
import com.oxysystem.general.service.general.LocationService;
import com.oxysystem.general.validation.annotation.PageParam;
import com.oxysystem.general.validation.annotation.SizeParam;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/api")
public class LocationController {
    private final LocationService locationService;

    public LocationController(LocationService locationService) {
        this.locationService = locationService;
    }

    @GetMapping("/locations/grab/select")
    public ResponseEntity<?> getLocationsGrabMartForSelect(String product){
        return locationService.getLocationsGrabForSelectForApi(product);
    }


    @Operation(
            summary = "Location List",
            description = "Retrieve list of locations"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Success", content = @Content(
                    schema = @Schema(implementation = SuccessResponse.class)
            ))
    })
    @GetMapping("/locations")
    public ResponseEntity<?> getLocations(@Parameter(description = "Filter by name (partial match allowed)") @RequestParam(required = false, defaultValue = "") String name,
                                          @Parameter(description = "Filter by grab merchant", example = "6-C7LKRXJEDCACKA") @RequestParam(required = false, defaultValue = "")String grabMerchantId,
                                          @Parameter(description = "Filter by grab food merchant", example = "6-C7LKRXJEDCACKA") @RequestParam(required = false, defaultValue = "")String grabFoodMerchantId,
                                          @PageParam @RequestParam(required = false, defaultValue = "0") int page,
                                          @SizeParam @RequestParam(required = false, defaultValue = "20") int size){
        return locationService.getLocations(name, grabMerchantId, grabFoodMerchantId, page, size);
    }

    @GetMapping("/grab-packaging-item-locations")
    public ResponseEntity<?> getPackagingItemLocations(@Parameter(description = "Filter by name (partial match allowed)") @RequestParam(required = false, defaultValue = "") String name,
                                                       @Parameter(description = "Filter by product like Grab Mart or Grab Food")  @RequestParam(required = false, defaultValue = "") String product,
                                                       @PageParam @RequestParam(required = false, defaultValue = "0") int page,
                                                       @SizeParam @RequestParam(required = false, defaultValue = "20") int size){
        return locationService.getPackagingItemLocations(name, product, page, size);
    }
}
