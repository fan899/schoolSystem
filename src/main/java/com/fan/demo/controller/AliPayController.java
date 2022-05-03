package com.fan.demo.controller;


import cn.hutool.core.date.DateUtil;
import cn.hutool.json.JSONObject;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.alipay.api.request.AlipayTradePayRequest;
import com.alipay.api.response.AlipayTradeRefundResponse;
import com.fan.demo.common.Constants;
import com.fan.demo.common.Result;
import com.fan.demo.config.AlipayConfig;
import com.fan.demo.controller.dto.Alipay;
import com.fan.demo.exception.ServiceException;
import com.fan.demo.mapper.OrderMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.crypto.Data;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName: AlipayController
 * @Description: 调用阿里沙箱支付
 * @Author fancy
 * @Date 2022/5/3
 * @Version 1.0
 */
@RestController
@RequestMapping("/alipay")
public class AliPayController {

    private static final String GATEWAY_URL = "https://openapi.alipaydev.com/gateway.do";
    private static final String FORMAT = "JSON";
    private static final String CHARSET = "UTF-8";
    // 签名方式
    private static final String SIGN_TYPE = "RSA2";

    @Autowired
    private AlipayConfig alipayConfig;

    @Resource // ?为什么用autowired不行?
    private OrderMapper orderMapper;

    /**
     * 向支付宝发送支付请求
     * @param alipay
     * @param httpResponse
     */
    @GetMapping("/pay")
    public void pay(Alipay alipay, HttpServletResponse httpResponse) {  // http://localhost:9090/alipay/pay?subject=${row.name}&traceNo=${row.no}&totalAmount=${row.total}

        // 1.创建Client,通用sdk提供的Client, 负责调用支付宝的API
        AlipayClient alipayClient = new DefaultAlipayClient(
                GATEWAY_URL,
                alipayConfig.getAppId(),
                alipayConfig.getAppPrivateKey(),
                FORMAT,
                CHARSET,
                alipayConfig.getAlipayPublicKey(),
                SIGN_TYPE);

        // 2. 创建Request并设置Request参数
        // 发送请求的request类
        AlipayTradePagePayRequest request = new AlipayTradePagePayRequest();
        request.setNotifyUrl(alipayConfig.getNotifyUrl());
        JSONObject bizContent = new JSONObject();
        // 订单编号(本系统生成的)
        bizContent.set("out_trade_no", alipay.getTraceNo());
        // 订单的总金额
        bizContent.set("total_amount", alipay.getTotalAmount());
        // 支付名称
        bizContent.set("subject", alipay.getSubject());
        // 固定配置
        bizContent.set("product_code", "FAST_INSTANT_TRADE_PAY");
        request.setBizContent(bizContent.toString());

        // 执行请求，拿到响应的结果，返回给浏览器
        String form = "";
        try {
            // 调用SDK生成表单
            form = alipayClient.pageExecute(request).getBody();
        } catch (AlipayApiException e) {
            throw new ServiceException(Constants.CODE_500, "生成表单失败");
        }
        httpResponse.setContentType("text/html;charset=" + CHARSET);
        try {
            httpResponse.getWriter().write(form);
            httpResponse.getWriter().flush();
            httpResponse.getWriter().close();
        } catch (IOException e) {
            throw new ServiceException(Constants.CODE_500, "输出表单失败");
        }
    }

    /**
     * 支付回调，处理支付宝返回的数据
     * @param httpRequest
     * @return
     */

    /*@PostMapping("/notify") // 必须使用POST接口
    public Result payNotify(HttpServletRequest httpRequest) {
        if (httpRequest.getParameter("trade_status").equals("TRADE_SUCCESS")) {
            System.out.println("===========支付宝异步回调==========");

            HashMap<String, String> params = new HashMap<>();
            Map<String, String[]> requestParams = httpRequest.getParameterMap();
            for (String name : requestParams.keySet()) {
                params.put(name, httpRequest.getParameter(name));
            }

            String tradeNo = params.get("out_trade_no");
            String gmtPayment = params.get("gmt_payment");
            String alipayTradeNo = params.get("trade_no");

            String sign = params.get("sign");
            String content = AlipaySignature.getSignCheckContentV1(params);
            try {
                // 验证签名
                boolean checkSignature = AlipaySignature.rsa256CheckContent(content, sign, alipayConfig.getAlipayPublicKey(), CHARSET);
                // 支付宝验签
                if (checkSignature) {
                    // 验签通过
                    System.out.println("交易名称" + params.get("subject"));
                    System.out.println("交易状态" + params.get("trade_status"));
                    System.out.println("支付宝交易凭证号" + params.get("trade_no"));
                    System.out.println("商户订单号" + params.get("out_trade_no"));
                    System.out.println("交易金额" + params.get("total_amount"));
                    System.out.println("买家在支付宝唯一id" + params.get("buyer_id"));
                    System.out.println("买家付款时间" + params.get("gmt_payment"));
                    System.out.println("买家付款金额" + params.get("buyer_pay_amount"));

                    // 更新订单支付状态
                    String orderStatus = "已支付";
                    orderMapper.updateState(tradeNo, orderStatus, gmtPayment, alipayTradeNo);
                }
            } catch (AlipayApiException e) {
                e.printStackTrace();
            }
        }
        return Result.success();
    }*/

    /**
     * 退款接口
     * @param alipay
     * @return
     */

/*
    @GetMapping("/return")
    public Result returnPay(Alipay alipay) {
        // 1.创建Client，通用SDK提供的client，负责调用支付宝api
        AlipayClient alipayClient = new DefaultAlipayClient(GATEWAY_URL, alipayConfig.getAppId(),
                alipayConfig.getAppPrivateKey(), FORMAT, CHARSET, alipayConfig.getAlipayPublicKey(), SIGN_TYPE);
        // 2.创建Request，设置参数
        AlipayTradePayRequest request = new AlipayTradePayRequest();
        JSONObject bizContent = new JSONObject();
        // 支付宝回调的订单流水号
        bizContent.set("trade_no", alipay.getAlipayTraceNo());
        // 订单总金额
        bizContent.set("refund_amount", alipay.getTotalAmount());
        // 本系统生成的订单编号
        bizContent.set("out_request_no", alipay.getTraceNo());

        request.setBizContent(bizContent.toString());

        // 3.执行请求
        AlipayTradeRefundResponse refundResponse = null;
        try {
            refundResponse = alipayClient.execute(request);
            if (refundResponse.isSuccess()) {
                System.out.println("调用成功");

                // 4.更新数据库状态
                String orderStatus = "已退款";
                String now = DateUtil.now();
                orderMapper.updatePayState(alipay.getTraceNo(), orderStatus, now);
                return Result.success();
            }else {
                System.out.println(refundResponse.getBody());
                return Result.error(refundResponse.getCode(), refundResponse.getBody());
            }
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }
        return Result.success();
    }

*/




}
