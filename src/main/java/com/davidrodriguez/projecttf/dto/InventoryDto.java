package com.davidrodriguez.projecttf.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InventoryDto {
  private Long id;
  private int total;
  private Long itemId;
}
