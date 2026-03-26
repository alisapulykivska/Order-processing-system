package com.kafka.paymentmicroservice.service;

import com.kafka.core.entity.ReservedProduct;
import com.kafka.core.exception.payment.ShippingServiceException;
import com.kafka.paymentmicroservice.entity.Payment;
import com.kafka.paymentmicroservice.repository.PaymentRepository;
import com.kafka.paymentmicroservice.service.dto.Distance;
import com.kafka.paymentmicroservice.service.dto.GetDistanceDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.springframework.http.MediaType.APPLICATION_JSON;

@Service
public class PaymentServiceImpl implements PaymentService {
    private static final Logger log = LoggerFactory.getLogger(PaymentServiceImpl.class);
    private final PaymentRepository paymentRepository;
    private final RestClient restClient;

    public PaymentServiceImpl(PaymentRepository paymentRepository, RestClient.Builder builder) {
        this.paymentRepository = paymentRepository;
        this.restClient = builder.baseUrl("http://localhost:10000/shipping/").build();
    }
    private final BigDecimal pricePerKm = BigDecimal.valueOf(0.45);

    @Override
    public Payment process(List<ReservedProduct> products, Long orderId) {
        BigDecimal totalPrice = products.stream()
                .map(product -> product.price()
                        .multiply(BigDecimal.valueOf(product.quantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        GetDistanceDto getDistanceDto = new GetDistanceDto("SomePointA", "SomePointB");
        Distance distance = Optional.ofNullable(restClient.method(HttpMethod.GET).body(getDistanceDto)
                .accept(APPLICATION_JSON)
                .retrieve()
                .body(Distance.class)).orElseThrow(() -> new ShippingServiceException("Distance is null"));

        totalPrice = totalPrice.add(pricePerKm.multiply(BigDecimal.valueOf(distance.distance())));

        log.info("Processed payment");
        Payment paymentEntity = new Payment(orderId, totalPrice);
        paymentRepository.save(paymentEntity);

        var processedPayment = new Payment();
        BeanUtils.copyProperties(paymentEntity, processedPayment);
        processedPayment.setId(paymentEntity.getId());
        return processedPayment;
    }
}
