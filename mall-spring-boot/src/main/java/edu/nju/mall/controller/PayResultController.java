package edu.nju.mall.controller;

import edu.nju.mall.dto.PayResultResponseDTO;
import edu.nju.mall.service.OrderService;
import edu.nju.mall.util.XmlUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/pay")
public class PayResultController {

    @Autowired
    private OrderService orderService;

    @PostMapping(value = "/result", consumes = MediaType.APPLICATION_XML_VALUE, produces = MediaType.TEXT_XML_VALUE)
    public String getResult(@RequestBody Map<String, Object> realResultMap) {
        boolean isSuccess = orderService.finishPay(realResultMap);
        Map<String, Object> result = new HashMap<>();
        if (isSuccess) {
            result.put("return_code", "SUCCESS");
        } else {
            result.put("return_code", "FAIL");
        }
        try {
            return XmlUtils.mapToXml(result);
        } catch (ParserConfigurationException | TransformerException | IOException ignored) {
            return null;
        }
    }

}
