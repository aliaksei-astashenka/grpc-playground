/*
 * Copyright (c) 2016-2020 Michael Zhang <yidongnan@gmail.com>
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated
 * documentation files (the "Software"), to deal in the Software without restriction, including without limitation the
 * rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to
 * permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the
 * Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE
 * WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 * COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR
 * OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package org.examples.config;

import io.grpc.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import static io.grpc.Metadata.ASCII_STRING_MARSHALLER;

public class LogGrpcInterceptor implements ClientInterceptor {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    public static final Metadata.Key<String> TRACE_ID_KEY = Metadata.Key.of("traceId", ASCII_STRING_MARSHALLER);

    @Override
    public <M, R> ClientCall<M, R> interceptCall(final MethodDescriptor<M, R> method, CallOptions callOptions, Channel next) {
        return new BackendForwardingClientCall<M, R>(method, next.newCall(method, callOptions.withDeadlineAfter(10000, TimeUnit.MILLISECONDS))) {

            @Override
            public void sendMessage(M message) {
                logger.info("Method: {}, Message: {}", methodName, message);
                super.sendMessage(message);
            }

            @Override
            public void start(Listener<R> responseListener, Metadata headers) {
                // TODO: Use the sleuth traceId
                headers.put(TRACE_ID_KEY, "traceId-" + new Date().getTime());

                BackendListener<R> backendListener = new BackendListener<>(methodName, responseListener);
                super.start(backendListener, headers);
            }
        };
    }

    private class BackendListener<R> extends ClientCall.Listener<R> {

        String methodName;
        ClientCall.Listener<R> responseListener;

        protected BackendListener(String methodName, ClientCall.Listener<R> responseListener) {
            super();
            this.methodName = methodName;
            this.responseListener = responseListener;
        }

        @Override
        public void onMessage(R message) {
            logger.info("Method: {}, Response: {}", methodName, message);
            responseListener.onMessage(message);
        }

        @Override
        public void onHeaders(Metadata headers) {
            responseListener.onHeaders(headers);
        }

        @Override
        public void onClose(Status status, Metadata trailers) {
            responseListener.onClose(status, trailers);
        }

        @Override
        public void onReady() {
            responseListener.onReady();
        }
    }

    private class BackendForwardingClientCall<M, R> extends io.grpc.ForwardingClientCall.SimpleForwardingClientCall<M, R> {

        String methodName;

        protected BackendForwardingClientCall(MethodDescriptor<M, R> method, ClientCall delegate) {
            super(delegate);
            methodName = method.getFullMethodName();
        }
    }
}
