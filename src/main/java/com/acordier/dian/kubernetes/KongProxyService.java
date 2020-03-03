package com.acordier.dian.kubernetes;

import io.fabric8.kubernetes.api.model.HasMetadata;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClientException;
import io.fabric8.kubernetes.client.dsl.ParameterNamespaceListVisitFromServerGetDeleteRecreateWaitApplicable;
import io.fabric8.kubernetes.client.dsl.base.CustomResourceDefinitionContext;
import io.fabric8.kubernetes.client.dsl.internal.RawCustomResourceOperationsImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class KongProxyService {
    private final ResourceLoader resourceLoader;
    private final KubernetesClient client;

    @Autowired
    public KongProxyService(ResourceLoader resourceLoader, KubernetesClient client) {
        this.resourceLoader = resourceLoader;
        this.client = client;
    }

    public void initialize() {
        ParameterNamespaceListVisitFromServerGetDeleteRecreateWaitApplicable<HasMetadata, Boolean> ingressConfig = loadIngressConfig();
        List<HasMetadata> metadata = ingressConfig.fromServer().get();
        if (metadata.isEmpty()) {
            ingressConfig.createOrReplace();
        }
        initializePlugins();
    }

    private void initializePlugins() {
        initializePluginFromYAML("classpath:kong/plugins/api-key.yml");
    }

    private void initializePluginFromYAML(String resource) {
        try {
            RawCustomResourceOperationsImpl customResourcesOperations = client.customResource(getPluginContext());
            Map<String, Object> customResource = customResourcesOperations.load(getPluginDefinition(resource));
            customResourcesOperations.createOrReplace("kong", customResource);
        } catch (IOException e) {
            throw new KubernetesClientException("unable to load plugin definition from " + resource, e);
        }
    }

    private ParameterNamespaceListVisitFromServerGetDeleteRecreateWaitApplicable<HasMetadata, Boolean> loadIngressConfig() {
        try {
            return client.load(getProxyDefinitions());
        } catch (IOException e) {
            throw new KubernetesClientException("unable to load proxy definitions", e);
        }
    }

    private InputStream getProxyDefinitions() throws IOException {
        return resourceLoader.getResource("classpath:kong/definitions.yml").getInputStream();
    }

    private InputStream getPluginDefinition(String resource) throws IOException {
        return resourceLoader.getResource(resource).getInputStream();
    }

    private CustomResourceDefinitionContext getPluginContext() {
        return new CustomResourceDefinitionContext.Builder()
                .withGroup("configuration.konghq.com")
                .withPlural("kongplugins")
                .withScope("Namespaced")
                .withVersion("v1")
                .build();
    }

}
