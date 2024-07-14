package jpa.spring.service;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jpa.spring.model.dto.PaymentStatus;
import jpa.spring.model.entities.Payment;
import jpa.spring.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PaymentService {

    @Autowired
    private final PaymentRepository paymentRepository;

    @Autowired final MoMoService moMoService;

    @Transactional
    public Payment createPayment(Payment payment) {
        // Call MoMoService to create payment
        Map<String, String> response = moMoService.createPayment(payment.getAmount());
    
        payment.setTransactionId(response.get("transactionId"));
        payment.setPaymentMethod("MOMO");
    
        // Save Payment entity
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
