package com.propertyservice.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PropertyDto {
	
	private Long id;
	private String name;
	private Integer numberOfBeds;
	private Integer numberOfRooms;
	private Integer numberOfBathrooms;
	private Integer numberOfGuestAllowed;
	private String city;
	private String area;
	private String state;
	
	private List<RoomsDto> rooms;
	private List<String> imageUrls;
}
