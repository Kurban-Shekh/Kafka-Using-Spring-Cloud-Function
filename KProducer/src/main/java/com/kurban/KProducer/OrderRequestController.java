package com.kurban.KProducer;

import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/orders")
public class OrderRequestController {
    private final StreamBridge streamBridge;

    public OrderRequestController(StreamBridge streamBridge) {
        this.streamBridge = streamBridge;
    }

    @PostMapping
    public ResponseEntity<String> createNewOrder(@RequestBody OrderRequestDTO orderRequest){
        boolean sent = streamBridge.send("orderLocation-out-0",orderRequest);
        if(!sent){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to publish order request");
        }else{
            return ResponseEntity.accepted().body("The order has been succesfully published ");
        }
    }
}
