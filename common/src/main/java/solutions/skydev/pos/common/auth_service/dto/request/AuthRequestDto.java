package solutions.skydev.pos.common.auth_service.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

import java.io.Serializable;

@Builder
@Jacksonized
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class AuthRequestDto implements Serializable {

    private String username;
    private String password;
    private Boolean admin;
    private Long userId;
}
