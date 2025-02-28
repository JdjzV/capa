package group.rxcloud.capa.rpc;


import group.rxcloud.capa.component.http.CapaHttpBuilder;
import group.rxcloud.capa.infrastructure.config.CapaProperties;

import java.util.function.Supplier;

/**
 * A builder for the {@link CapaRpcClient},
 * Currently only HTTP Client will be supported.
 */
public class CapaRpcClientBuilder {

    /**
     * Determine if this builder will create Rpc client with HTTP/... clients.
     */
    private final CapaApiProtocol apiProtocol;

    /**
     * Builder for Capa's HTTP Client.
     */
    private final CapaHttpBuilder httpBuilder;

    /**
     * Creates a constructor for {@link CapaRpcClient}.
     */
    public CapaRpcClientBuilder() {
        this(new CapaHttpBuilder());
    }

    /**
     * Creates a constructor for {@link CapaRpcClient} with custom {@link CapaHttpBuilder}.
     */
    public CapaRpcClientBuilder(Supplier<CapaHttpBuilder> capaHttpBuilderSupplier) {
        this(capaHttpBuilderSupplier.get());
    }

    /**
     * Creates a constructor for {@link CapaRpcClient}.
     */
    public CapaRpcClientBuilder(CapaHttpBuilder httpBuilder) {
        this.apiProtocol = CapaApiProtocol.parseProtocol(CapaProperties.API_PROTOCOL.get());
        this.httpBuilder = httpBuilder;
    }

    /**
     * Build an instance of the Client based on the provided setup.
     *
     * @return an instance of the setup Client
     * @throws IllegalStateException if any required field is missing
     */
    public CapaRpcClient build() {
        return buildCapaClient(this.apiProtocol);
    }

    /**
     * Creates an instance of a Capa Client based on the chosen protocol.
     *
     * @param protocol Capa API's protocol.
     * @return the GRPC Client.
     * @throws IllegalStateException if either host is missing or if port is missing or a negative number.
     */
    private CapaRpcClient buildCapaClient(CapaApiProtocol protocol) {
        if (protocol == null) {
            throw new IllegalStateException("Protocol is required.");
        }

        switch (protocol) {
            case HTTP:
                return buildCapaClientHttp();
            default:
                throw new IllegalStateException("Unsupported protocol: " + protocol.name());
        }
    }

    /**
     * Creates and instance of CapaClient over HTTP.
     *
     * @return CapaClient over HTTP.
     */
    private CapaRpcClient buildCapaClientHttp() {
        return new CapaRpcClientHttp(this.httpBuilder.build());
    }
}
