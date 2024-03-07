package com.davidrodriguez.projecttf.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InventoryLogDto {

  private Long id;

  private String transaction;

  private int amount;

  private Long itemId;
}
