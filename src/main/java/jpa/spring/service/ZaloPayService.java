package jpa.spring.service;

import org.apache.http.NameValuePair;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import jpa.spring.core.HMACUtil;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class ZaloPayService {

    private static final Map<String, String> config = new HashMap<String, String>() {{
        put("appid", "553");
        put("key1", "9phuAOYhan4urywHTh0ndEXiV3pKHr5Q");
        put("key2", "Iyz2habzyr7AG8SgvoBCbKwKi3UzlLi3");
        put("endpoint", "https://sbgateway.zalopay.vn/api/getlistmerchantbanks");
    }};

    public String getBankList() throws Exception {
        String appid = config.get("appid");
        String reqtime = Long.toString(System.currentTimeMillis());
        String data = appid + "|" + reqtime;
        String mac = HMACUtil.HMacHexStringEncode(HMACUtil.HMACSHA256, config.get("key1"), data);

        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("appid", appid));
        params.add(new BasicNameValuePair("reqtime", reqtime));
        params.add(new BasicNameValuePair("mac", mac));

        URIBuilder uri = new URIBuilder(config.get("endpoint"));
        uri.addParameters(params);

        try (CloseableHttpClient client = HttpClients.createDefault()) {
            HttpGet get = new HttpGet(uri.build());
            try (CloseableHttpResponse res = client.execute(get);
                 BufferedReader rd = new BufferedReader(new InputStreamReader(res.getEntity().getContent()))) {

                StringBuilder resultJsonStr = new StringBuilder();
                String line;
                while ((line = rd.readLine()) != null) {
                    resultJsonStr.append(line);
                }
                return resultJsonStr.toString();
            }
        }
    }

    public Map<String, List<String>> parseBankList(String json) {
        JSONObject result = new JSONObject(json);
        JSONObject banksObject = result.getJSONObject("banks");

        Map<String, List<String>> bankList = new HashMap<>();
        for (String pmcid : banksObject.keySet()) {
            JSONArray banks = banksObject.getJSONArray(pmcid);
            List<String> bankNames = new ArrayList<>();
            banks.forEach(bank -> bankNames.add(bank.toString()));
            bankList.put(pmcid, bankNames);
        }
        return bankList;
    }

    public Map<String, String> createPayment(double amount) {
        Map<String, String> response = new HashMap<>();
        response.put("transactionId", UUID.randomUUID().toString());
        response.put("status", "SUCCESS");
        return response;
    }
}