package com.pm.billingservice.grpc;


import billing.BillingRequest;
import billing.BillingResponse;
import billing.BillingServiceGrpc;
import io.grpc.stub.StreamObserver;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.server.service.GrpcService;

@Slf4j
@GrpcService
public class BillingGrpcService extends BillingServiceGrpc.BillingServiceImplBase {

    @Override
    public void createBillingAccount(BillingRequest request,
                                     StreamObserver<BillingResponse> responseObserver) {
        log.info("Create billing account request: {}", request.toString());

        //Business logic - e.g save to database, perform calculates etc

        BillingResponse response = BillingResponse.newBuilder()
                .setAccountId("1234")
                .setStatus("ACTIVE")
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}
