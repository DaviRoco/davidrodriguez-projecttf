package com.davidrodriguez.projecttf.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ItemDto {
  private Long id;

  private String name;

  public String getName() {
    return name;
  }

  public Long getId() {
    return id;
  }

}
