package com.nexusforge.grpcplayground.sec12.Interceptors;

import com.nexusforge.grpcplayground.sec12.Constants;
import com.nexusforge.grpcplayground.sec12.UserRole;
import io.grpc.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;
import java.util.Set;

/*
 * user-token-1, user-token-2 -> prime users, return balance as it is
 * user-token-3, user-token-4 -> standard users, deduct $1 and return balance.
 * any other token -> not valid
 * */

public class UserRoleInterceptor implements ServerInterceptor {
    public static final Set<String> PRIME_SET = Set.of("user-token-1", "user-token-2");
    public static final Set<String> STANDARD_SET = Set.of("user-token-3", "user-token-4", "user-token-5");
    private static final Logger log = LoggerFactory.getLogger(UserRoleInterceptor.class);

    @Override
    public <ReqT, RespT> ServerCall.Listener<ReqT> interceptCall(ServerCall<ReqT, RespT> serverCall, Metadata metadata, ServerCallHandler<ReqT, RespT> serverCallHandler) {


        var token = extractToken(metadata.get(Constants.USER_TOKEN_KEY));
        log.info("{}", token);
        var ctx = toContext(token);

        //if present add, if not, reject
        if (Objects.nonNull(ctx)) {
            // special cases where you've changed context,
            // it rebuilds. Server Call with modified context
           return Contexts.interceptCall(ctx, serverCall, metadata, serverCallHandler);
        }
        return close(serverCall, metadata, Status.PERMISSION_DENIED.withDescription("user is not allowed to do this operation."));
    }

    private String extractToken(String value) {
        //extract bearer.
        return Objects.nonNull(value) && value.startsWith(Constants.BEARER) ?
                value.substring(Constants.BEARER.length()).trim() : null;
    }

    private Context toContext(String token) {
        if (Objects.nonNull(token) && (PRIME_SET.contains(token) || STANDARD_SET.contains(token))) {
            var role = PRIME_SET.contains(token) ? UserRole.PRIME : UserRole.STANDARD;
            return Context.current().withValue(Constants.USER_ROLE_KEY, role);
            //we've allowed the values into the Service layer
            // but we still don't know what the values is, in the service layer
        }
        return null;
    }

    private <ReqT, RespT> ServerCall.Listener<ReqT> close(ServerCall<ReqT, RespT> serverCall,
                                                          Metadata metadata, Status status) {
        serverCall.close(status, metadata);
        // return empty listener incase of error
        return new ServerCall.Listener<>() {
        };
    }
}
