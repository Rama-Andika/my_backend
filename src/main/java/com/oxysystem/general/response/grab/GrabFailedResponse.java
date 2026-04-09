package com.oxysystem.general.response.grab;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GrabFailedResponse {
    private String target;

    private String reason;

    private String message;


}
