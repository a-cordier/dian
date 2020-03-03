package com.acordier.dian.apimanagement;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
class CreateApiDTO {
    String name;
    Integer version;
    private List<CreateRouteDTO> routes;
}
