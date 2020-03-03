package com.acordier.dian.apimanagement;

import com.acordier.dian.kubernetes.K8sIngressService;
import io.fabric8.kubernetes.api.model.IntOrString;
import io.fabric8.kubernetes.api.model.extensions.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ApiService {
    private final K8sIngressService ingressService;

    @Autowired
    public ApiService(K8sIngressService ingressService) {
        this.ingressService = ingressService;
    }

    void createApi(CreateApiDTO api) {
        ingressService.createIngress(toKubernetesIngress(api));
    }

    private Ingress toKubernetesIngress(CreateApiDTO api) {
        return new IngressBuilder()
                .withNewMetadata()
                .withAnnotations(getDefaultAnnotations())
                .withName(api.getName())
                .withNamespace("kong")
                .and()
                .withSpec(toIngressSpec(api.getRoutes()))
                .build();
    }

    private IngressSpec toIngressSpec(List<CreateRouteDTO> routes) {
        return new IngressSpecBuilder()
                .withRules(toIngressRules(routes))
                .build();
    }

    private List<IngressRule> toIngressRules(List<CreateRouteDTO> routes) {
        return Collections.singletonList(new IngressRule(null, toIngressRuleValue(routes)));
    }

    private HTTPIngressRuleValue toIngressRuleValue(List<CreateRouteDTO> routes) {
        return new HTTPIngressRuleValue(toHttpIngressPaths(routes));
    }

    private List<HTTPIngressPath> toHttpIngressPaths(List<CreateRouteDTO> routes) {
        return routes.stream()
                .map(this::toHttpIngressPath)
                .collect(Collectors.toList());
    }

    private HTTPIngressPath toHttpIngressPath(CreateRouteDTO route) {
        return new HTTPIngressPath(toIngressBackend(route), route.getPath());
    }

    private IngressBackend toIngressBackend(CreateRouteDTO route) {
        String serviceName = route.getServiceName();
        IntOrString servicePort = new IntOrString(route.getServicePort());
        return new IngressBackend(serviceName, servicePort);
    }

    private Map<String, String> getDefaultAnnotations() {
        HashMap<String, String> annotations = new HashMap<>();
        annotations.put("plugins.konghq.com", "api-key-auth");
        return annotations;
    }
}
