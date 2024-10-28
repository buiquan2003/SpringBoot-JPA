package jpa.spring.service;

import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import jpa.spring.model.dto.*;
import jpa.spring.model.entities.*;
import jpa.spring.repository.*;
import lombok.*;

@Service
@RequiredArgsConstructor
public class PaymentService {

    @Autowired
    private final PaymentRepository paymentRepository;

    @Autowired
    private final ZaloPayService zaloPayService;

    @Transactional
    public Payment createPayment(Payment payment) {
        Map<String, String> response = zaloPayService.createPayment(payment.getAmount());
    
        payment.setTransactionId(response.get("transactionId"));
        payment.setPaymentMethod("ZALOPAY");
    
        return paymentRepository.save(payment);
    }

    @Transactional
    public Payment completePayment(Long paymentId) {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new RuntimeException("Payment not found"));

        payment.setStatus(PaymentStatus.COMPLETED);
        return paymentRepository.save(payment);
    }

    @Transactional
    public Payment failPayment(Long paymentId) {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new RuntimeException("Payment not found"));
        payment.setStatus(PaymentStatus.FAILED);
        return paymentRepository.save(payment);
    }
}