package uk.gov.hmcts.reform.adoption;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.time.Duration;
import java.time.Instant;

public final class MockServerReadiness {
    private static final Duration DEFAULT_TIMEOUT = Duration.ofSeconds(10);
    private static final Duration DEFAULT_POLL_INTERVAL = Duration.ofMillis(200);
    private static final Duration READY_STABILITY = Duration.ofSeconds(2);

    private MockServerReadiness() {
    }

    public static void awaitReady(String host, int port) {
        awaitReady(host, port, DEFAULT_TIMEOUT, DEFAULT_POLL_INTERVAL);
    }

    static void awaitReady(String host, int port, Duration timeout, Duration pollInterval) {
        Instant deadline = Instant.now().plus(timeout);
        IOException lastException = null;
        Instant firstStableConnect = null;

        while (Instant.now().isBefore(deadline)) {
            boolean connected = false;
            try (Socket socket = new Socket()) {
                socket.connect(new InetSocketAddress(host, port), (int) pollInterval.toMillis());
                connected = true;
            } catch (IOException ex) {
                lastException = ex;
            }

            if (connected) {
                if (firstStableConnect == null) {
                    firstStableConnect = Instant.now();
                }
                if (Duration.between(firstStableConnect, Instant.now()).compareTo(READY_STABILITY) >= 0) {
                    return;
                }
            } else {
                firstStableConnect = null;
            }

            pause(pollInterval);
        }

        String message = String.format(
            "Pact mock server on %s:%d did not become ready within %d ms",
            host,
            port,
            timeout.toMillis()
        );
        throw new AssertionError(message, lastException);
    }

    private static void pause(Duration pollInterval) {
        try {
            Thread.sleep(pollInterval.toMillis());
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
            throw new AssertionError("Mock server readiness check was interrupted", ex);
        }
    }
}
