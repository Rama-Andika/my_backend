package com.oxysystem.general.controller.auth;
import com.oxysystem.general.dto.auth.data.auth.LoginDto;
import com.oxysystem.general.dto.auth.data.auth.RefreshTokenDTO;
import com.oxysystem.general.model.tenant.admin.User;
import com.oxysystem.general.response.FailedResponse;
import com.oxysystem.general.response.LoginResponse;
import com.oxysystem.general.response.SuccessResponse;
import com.oxysystem.general.service.admin.RefreshTokenService;
import com.oxysystem.general.service.admin.UserService;
import com.oxysystem.general.util.JwtUtil;
import com.oxysystem.general.config.tenant.TenantContext;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;


@RestController
@CrossOrigin
@RequestMapping("/rest/auth")
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final RefreshTokenService refreshTokenService;
    private final JwtUtil jwtUtil;

    public AuthController(AuthenticationManager authenticationManager, UserService userService, RefreshTokenService refreshTokenService, JwtUtil jwtUtil) {
        this.authenticationManager = authenticationManager;
        this.userService = userService;
        this.refreshTokenService = refreshTokenService;
        this.jwtUtil = jwtUtil;
    }

    @Operation(
            summary = "User Authentication (Login)",
            description = "This endpoint is used to authentication user"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "401", description = "Invalid username or password", content = @Content(
                    schema = @Schema(implementation = FailedResponse.class)
            )),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(
                    schema = @Schema(implementation = FailedResponse.class)
            )),
            @ApiResponse(responseCode = "200", description = "Success", content = @Content(
                    schema = @Schema(implementation = SuccessResponse.class)
            ))
    })
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDto dto){
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(dto.getUsername(), dto.getPassword())
            );

            User user = userService.findByUsername(dto.getUsername())
                    .orElseThrow(() -> new RuntimeException("User not found"));

            String accessToken = jwtUtil.generateAccessToken(user.getUsername(), TenantContext.getCurrentTenant());

            refreshTokenService.delete(user.getUserId());

            String refreshToken = refreshTokenService.create(user);

            String groupName = "";
            if(user.getUserGroup() != null && user.getUserGroup().getGroupName() != null){
                groupName = user.getUserGroup().getGroupName();
            }
            LoginResponse loginResponse = new LoginResponse(String.valueOf(user.getUserId()), user.getFullName(), groupName, user.getUserLevel(), accessToken, refreshToken);
            SuccessResponse<LoginResponse> response = new SuccessResponse<>("success", loginResponse);
            return ResponseEntity.ok(response);

        }catch (Exception e){
            FailedResponse<String> response = new FailedResponse<>("Login failed","Invalid username or password");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    @PostMapping("/refresh")
    public ResponseEntity<?> refreshToken(@RequestBody RefreshTokenDTO req) {
        User user = refreshTokenService.validateAndRotate(req.getRefreshToken());

        String accessToken = jwtUtil.generateAccessToken(user.getUsername(), TenantContext.getCurrentTenant());
        String newRefreshToken = refreshTokenService.create(user);

        LoginResponse loginResponse = new LoginResponse(String.valueOf(user.getUserId()), user.getFullName(), user.getUserGroup().getGroupName(), user.getUserLevel(), accessToken, newRefreshToken);
        SuccessResponse<LoginResponse> response = new SuccessResponse<>("success", loginResponse);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/")
    public String index(){
        return "Hello World!";
    }
}
