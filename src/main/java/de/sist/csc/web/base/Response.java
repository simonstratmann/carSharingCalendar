// (C) 2022 PPI AG
package de.sist.csc.web.base;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Response {

    private boolean success;
    private String message;


}
