package com.acordier.dian.kubernetes;

import io.fabric8.kubernetes.client.DefaultKubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClientException;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class K8sClientConfig {

    @Bean
    public KubernetesClient kubernetesClient() {
        try {
            return new DefaultKubernetesClient();
        } catch (KubernetesClientException e) {
            throw new BeanCreationException("unable to initialize kubernetes client. is minikube up and running ?");
        }
    }
}
