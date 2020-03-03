package com.acordier.dian.kubernetes;


import io.fabric8.kubernetes.api.model.extensions.Ingress;
import io.fabric8.kubernetes.client.KubernetesClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class K8sIngressService {
    private final KubernetesClient client;

    @Autowired
    public K8sIngressService(KubernetesClient client) {
        this.client = client;
    }

    public void createIngress(Ingress ingress) {
        client.extensions().ingresses().createOrReplace(ingress);
    }
}
