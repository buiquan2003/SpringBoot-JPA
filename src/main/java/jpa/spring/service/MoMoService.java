package jpa.spring.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class MoMoService {

    @Value("${momo.partnerCode}")
    private String partnerCode;

    @Value("${momo.accessKey}")
    private String accessKey;

    @Value("${momo.secretKey}")
    private String secretKey;

    @Value("${momo.endpoint}")
    private String endpoint;

    public Map<String, String> createPayment(Double amount) {
        String requestId = UUID.randomUUID().toString();
        String orderId = UUID.randomUUID().toString();
        String orderInfo = "Payment with MoMo";
        String returnUrl = "http://yourwebsite.com/return";
        String notifyUrl = "http://yourwebsite.com/notify";
        String extraData = "";

        // Build request parameters
        Map<String, String> requestParams = new HashMap<>();
        requestParams.put("partnerCode", partnerCode);
        requestParams.put("accessKey", accessKey);
        requestParams.put("requestId", requestId);
        requestParams.put("amount", String.valueOf(amount));
        requestParams.put("orderId", orderId);
        requestParams.put("orderInfo", orderInfo);
        requestParams.put("returnUrl", returnUrl);
        requestParams.put("notifyUrl", notifyUrl);
        requestParams.put("extraData", extraData);

        // Generate signature
        String signature = createSignature(requestParams);
        requestParams.put("signature", signature);

        // Send request to MoMo
        RestTemplate restTemplate = new RestTemplate();
        @SuppressWarnings("unchecked")
        Map<String, String> response = restTemplate.postForObject(endpoint, requestParams, Map.class);

        // Return response
        return response;
    }

    public boolean verifyPayment(Map<String, String> momoParams) {
        // Validate the signature
        String signature = momoParams.remove("signature");
        String hashData = buildQueryUrl(momoParams);
        String generatedSignature = hmacSHA256(secretKey, hashData);

        return signature.equals(generatedSignature);
    }

    private String createSignature(Map<String, String> params) {
        // Generate the signature using MoMo's requirements
        StringBuilder hashData = new StringBuilder();
        params.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .forEach(entry -> hashData.append(entry.getKey()).append('=').append(entry.getValue()).append('&'));
        hashData.deleteCharAt(hashData.length() - 1);
        return hmacSHA256(secretKey, hashData.toString());
    }

    private String hmacSHA256(String key, String data) {
        try {
            Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
            SecretKeySpec secret_key = new SecretKeySpec(key.getBytes(), "HmacSHA256");
            sha256_HMAC.init(secret_key);

            byte[] hash = sha256_HMAC.doFinal(data.getBytes());
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (Exception e) {
            throw new RuntimeException("Failed to generate HMAC SHA256 signature", e);
        }
    }

    private String buildQueryUrl(Map<String, String> params) {
        StringBuilder queryUrl = new StringBuilder();
        params.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .forEach(entry -> {
                    queryUrl.append(entry.getKey()).append('=').append(entry.getValue()).append('&');
                });
        queryUrl.deleteCharAt(queryUrl.length() - 1);
        return queryUrl.toString();
    }
}
