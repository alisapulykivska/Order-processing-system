package com.kafka.paymentmicroservice.service;

import com.kafka.core.entity.ReservedProduct;
import com.kafka.paymentmicroservice.entity.Payment;
import com.kafka.paymentmicroservice.repository.PaymentRepository;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class PaymentServiceImpl implements PaymentService {
    //public static final String SAMPLE_CREDIT_CARD_NUMBER = "374245455400126";
    private static final Logger log = LoggerFactory.getLogger(PaymentServiceImpl.class);
    private final PaymentRepository paymentRepository;

    @Override
    public Payment process(List<ReservedProduct> products, Long orderId) {
        BigDecimal totalPrice = BigDecimal.ZERO;
        for (ReservedProduct product : products) {
            totalPrice = totalPrice.add(product.getPrice()
                    .multiply(new BigDecimal(product.getQuantity())));
        }

        log.info("Processed payment");
        Payment paymentEntity = new Payment(orderId, totalPrice);
        paymentRepository.save(paymentEntity);

        var processedPayment = new Payment();
        BeanUtils.copyProperties(paymentEntity, processedPayment);
        processedPayment.setId(paymentEntity.getId());
        return processedPayment;
    }

    /*@Override
    public List<Payment> findAll() {
        return paymentRepository.findAll().stream().map(entity -> new Payment(entity.getId(), entity.getOrderId(), entity.getProductId(), entity.getProductPrice(), entity.getProductQuantity())
        ).collect(Collectors.toList());
    }*/
}
