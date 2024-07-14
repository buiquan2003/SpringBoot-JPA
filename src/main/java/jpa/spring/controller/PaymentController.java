package jpa.spring.controller;

import org.springframework.web.bind.annotation.RestController;

import jpa.spring.core.ResponseObject;
import jpa.spring.model.entities.Payment;
import jpa.spring.service.PaymentService;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping("/payments")
@RequiredArgsConstructor
public class PaymentController {

    @Autowired
    private final PaymentService paymentService;

    @PostMapping("/create")
    public ResponseEntity<ResponseObject<Payment>> createPayment(@RequestBody Payment payment) {
        ResponseObject<Payment> result = new ResponseObject<>();
        Payment newPayment = paymentService.createPayment(payment);
        result.setMessage("payment succsee");
        result.setData(newPayment);
        return new ResponseEntity<>(result, HttpStatus.OK);

    }

    @PostMapping("/{paymentId}/complete")
    public ResponseEntity<ResponseObject<Payment>> completePayment(@PathVariable Long paymentId) {
        ResponseObject<Payment> result = new ResponseObject<>();
        Payment payment = paymentService.completePayment(paymentId);
        result.setMessage("payment complete succsee");
        result.setData(payment);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
 
    @PostMapping("/{paymentId}/fail")
    public ResponseEntity<ResponseObject<Payment>> failPayment(@PathVariable Long paymentId) {
        ResponseObject<Payment> result = new ResponseObject<>();
        Payment payment = paymentService.failPayment(paymentId);
        result.setMessage("payment fail succsee");
        result.setData(payment);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
    


}
