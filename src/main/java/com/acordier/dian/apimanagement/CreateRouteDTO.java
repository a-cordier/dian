package com.acordier.dian.apimanagement;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
class CreateRouteDTO {
    private String path;
    private String serviceName;
    private Integer servicePort;
}
