package com.nexusforge.grpcplayground.sec13;

import io.grpc.netty.shaded.io.grpc.netty.GrpcSslContexts;
import io.grpc.netty.shaded.io.netty.handler.ssl.SslContext;
import io.grpc.netty.shaded.io.netty.handler.ssl.SslContextBuilder;
import org.junit.jupiter.api.TestInstance;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.TrustManagerFactory;
import java.nio.file.Path;
import java.security.KeyStore;
import java.util.concurrent.Callable;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public abstract class AbstractTest {

    public static final Path KEY_STORE = Path.of("src/test/resources/certs/grpc.keystore.jks");
    public static final Path TRUST_STORE = Path.of("src/test/resources/certs/grpc.truststore.jks");
    private static final char[] PASSWORD = "changeit".toCharArray();


    protected SslContext serverSslContext(){
        return handleException(()->GrpcSslContexts.configure(SslContextBuilder
                .forServer(getKeyManagerFactory())).build());
    }
    protected SslContext clientSslContext(){
        return handleException(()->GrpcSslContexts
                .configure(SslContextBuilder
                .forClient().trustManager(getTrustManagerFactory())).build());
    }

    protected KeyManagerFactory getKeyManagerFactory() {
        return handleException(() -> {
            var knf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
            var keyStore = KeyStore.getInstance(KEY_STORE.toFile(), PASSWORD);
            //trust and keystore password are different
            knf.init(keyStore, PASSWORD);
            return knf;

        });
    }

    protected TrustManagerFactory getTrustManagerFactory() {
        return handleException(() -> {

            var tnf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            var trustStore = KeyStore.getInstance(TRUST_STORE.toFile(), PASSWORD);
            //trust and keystore password are different
            tnf.init(trustStore);
            return tnf;

        });
    }

    private <T> T handleException(Callable<T> callable) {
        try {
            return callable.call();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
