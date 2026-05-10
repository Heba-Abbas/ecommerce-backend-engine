package com.ecommerce.backend_engine.controller;

import com.ecommerce.backend_engine.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/test")
public class ProductController {

    @Autowired private ProductService productService;
    @Autowired private BatchService batchService;

    // الطلب 1
    @PostMapping("/v1/unsafe") public String v1u(@RequestParam Long id) { productService.purchaseV1Unsafe(id, 1); return "Done Unsafe"; }
    @PostMapping("/v1/safe") public String v1s(@RequestParam Long id) { productService.purchaseV1Safe(id, 1); return "Done Safe"; }

    // الطلب 2
    @PostMapping("/v2/limitless") public String v2l() { productService.taskV2Limitless(); return "Started Limitless"; }
    @PostMapping("/v2/controlled") public String v2c() { productService.taskV2Controlled(); return "Started Controlled"; }

    // الطلب 3
    @PostMapping("/v3/sync") public String v3s() { productService.notifyV3Sync(); return "Sync Done"; }
    @PostMapping("/v3/async") public String v3a() { productService.notifyV3Async(); return "Async Started"; }

    // الطلب 4
    @GetMapping("/v4/heavy") public String v4h() { return "Processed: " + batchService.processV4Heavy(); }
    @GetMapping("/v4/chunks") public String v4c() { return "Processed: " + batchService.processV4Chunks(); }
}