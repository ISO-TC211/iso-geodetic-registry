package org.iso.registry.client;

import com.amazonaws.serverless.exceptions.ContainerInitializationException;
import com.amazonaws.serverless.proxy.internal.testutils.Timer;
import com.amazonaws.serverless.proxy.model.AwsProxyRequest;
import com.amazonaws.serverless.proxy.model.AwsProxyResponse;
import com.amazonaws.serverless.proxy.spring.SpringLambdaContainerHandler;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestStreamHandler;
import lombok.extern.slf4j.Slf4j;
import org.iso.registry.client.configuration.web.IsoClientConfiguration;
import org.iso.registry.client.configuration.web.WebApplicationInitializer;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;

import javax.servlet.DispatcherType;
import javax.servlet.FilterRegistration;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.EnumSet;

@Slf4j
public class StreamLambdaHandler implements RequestStreamHandler {

    private static SpringLambdaContainerHandler<AwsProxyRequest, AwsProxyResponse> handler;

    static {
        try {
            AnnotationConfigWebApplicationContext applicationContext = new AnnotationConfigWebApplicationContext();
            applicationContext.register(AwsLambdaConfig.class);
            handler = SpringLambdaContainerHandler.getAwsProxyHandler(applicationContext, "aws");

            // we use the onStartup method of the handler to register our custom filter
//            handler.onStartup(servletContext -> {
//                FilterRegistration.Dynamic registration = servletContext.addFilter("CognitoIdentityFilter", CognitoIdentityFilter.class);
//                registration.addMappingForUrlPatterns(EnumSet.of(DispatcherType.REQUEST), false, "/*");
//            });
        } catch (ContainerInitializationException e) {
            // if we fail here. We re-throw the exception to force another cold start
            e.printStackTrace();
            throw new RuntimeException("Could not initialize Spring framework", e);
        }
    }

    public StreamLambdaHandler() {
        // we enable the timer for debugging. This SHOULD NOT be enabled in production.
        Timer.enable();
    }


    @Override
    public void handleRequest(InputStream inputStream, OutputStream outputStream, Context context)
        throws IOException {
        try {
            handler.proxyStream(inputStream, outputStream, context);
        }
        catch (Exception e) {
            log.error("123 error in processing", e);
            e.printStackTrace();
        }

    }
}
