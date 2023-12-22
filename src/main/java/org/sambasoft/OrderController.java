package org.sambasoft;

import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.sambasoft.model.Order;
import org.sambasoft.service.OrderProducer;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Log
@RestController
@RequiredArgsConstructor
public class OrderController {

  private final OrderProducer orderProducer;

  @PostMapping("/order")
  public ResponseEntity<Void> placeOrder(@RequestBody Order order) {

    log.info("order received");

    orderProducer.sendOrder(order);

    return ResponseEntity.noContent().build();
  }
}
