package org.example.freshdeliveryserver.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Order implements Serializable {
    private Integer orderId;
    private Integer deliveryPersonId;
    private Status status;
    private String content;
    private String notes;
    private Integer routeId;
    private String senderName;
    private String senderAddress;
    private Integer senderPhone;
    private String receiverName;
    private String receiverAddress;
    private Integer receiverPhone;
    private Float totalDistance;
    private Float totalWeight;
    private BigDecimal totalPrice;
    private LocalDateTime createdAt;
    private LocalDateTime completedAt;
    private LocalDateTime deliveryDate;
}
