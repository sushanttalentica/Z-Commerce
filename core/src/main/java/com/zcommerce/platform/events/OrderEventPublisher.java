package com.zcommerce.platform.events;

import com.ecommerce.productorder.domain.entity.Order;

public interface OrderEventPublisher {

  void publishOrderCreatedEvent(Order order);

  void publishOrderStatusUpdatedEvent(Order order);

  void publishOrderCancelledEvent(Order order);

  void publishOrderCompletedEvent(Order order);
}
