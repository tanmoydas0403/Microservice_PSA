package com.propertyservice.service;


import com.propertyservice.dto.APIResponse;
import com.propertyservice.dto.EmailRequest;
import com.propertyservice.dto.RoomsDto;
import com.propertyservice.entity.*;
import com.propertyservice.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.propertyservice.dto.PropertyDto;

import java.time.LocalDate;
import java.util.List;

@Service
public class PropertyService {
	@Autowired
	private PropertyRepository propertyRepository;

	@Autowired
	private AreaRepository areaRepository;

	@Autowired
	private CityRepository cityRepository;

	@Autowired
	private StateRepository stateRepository;

	@Autowired
	private RoomsRepository roomsRepository;

	@Autowired
	private RoomAvailabilityRepository availabilityRepository;

	@Autowired
	private PropertyPhotosRepository propertyPhotosRepository;

	@Autowired
	private S3Service s3Service;

	@Autowired
	private EmailProducer emailProducer;
	
//	public Property addProperty(PropertyDto dto, MultipartFile[] files) {
//		Area area = areaRepository.findByName(dto.getArea());
//		City city = cityRepository.findByName(dto.getCity());
//		State state = stateRepository.findByName(dto.getState());
//
//		Property property = new Property();
//		property.setName(dto.getName());
//		property.setNumberOfBathrooms(dto.getNumberOfBathrooms());
//		property.setNumberOfBeds(dto.getNumberOfBeds());
//		property.setNumberOfRooms(dto.getNumberOfRooms());
//		property.setNumberOfGuestAllowed(dto.getNumberOfGuestAllowed());
//		property.setArea(area);
//		property.setCity(city);
//		property.setState(state);
//
//		Property savedProperty = propertyRepository.save(property);
//
//		// Save rooms
//		for (RoomsDto roomsDto : dto.getRooms()) {
//			Rooms rooms = new Rooms();
//			rooms.setProperty(savedProperty);
//			rooms.setRoomType(roomsDto.getRoomType());
//			rooms.setBasePrice(roomsDto.getBasePrice());
//			roomsRepository.save(rooms);
//		}
//
//		List<String> fileUrls = s3Service.uploadFiles(files);
//		for (String url: fileUrls){
//			PropertyPhotos propertyPhotos = new PropertyPhotos();
//			propertyPhotos.setUrl(url);
//			propertyPhotos.setProperty(savedProperty);
//			propertyPhotosRepository.save(propertyPhotos);
//		}
//		return savedProperty;
//	}
//
public PropertyDto addProperty(PropertyDto dto, MultipartFile[] files) {
	Area area = areaRepository.findByName(dto.getArea());
	City city = cityRepository.findByName(dto.getCity());
	State state = stateRepository.findByName(dto.getState());

	Property property = new Property();
	property.setName(dto.getName());
	property.setNumberOfBathrooms(dto.getNumberOfBathrooms());
	property.setNumberOfBeds(dto.getNumberOfBeds());
	property.setNumberOfRooms(dto.getNumberOfRooms());
	property.setNumberOfGuestAllowed(dto.getNumberOfGuestAllowed());
	property.setArea(area);
	property.setCity(city);
	property.setState(state);

	Property savedProperty = propertyRepository.save(property);

	// Save rooms
	for (RoomsDto roomsDto : dto.getRooms()) {
		Rooms rooms = new Rooms();
		rooms.setProperty(savedProperty);
		rooms.setRoomType(roomsDto.getRoomType());
		rooms.setBasePrice(roomsDto.getBasePrice());
		roomsRepository.save(rooms);
	}

	// Save photos
	List<String> fileUrls = s3Service.uploadFiles(files);
	for (String url: fileUrls){
		PropertyPhotos propertyPhotos = new PropertyPhotos();
		propertyPhotos.setUrl(url);
		propertyPhotos.setProperty(savedProperty);
		propertyPhotosRepository.save(propertyPhotos);
	}

	// Build DTO for response
	PropertyDto responseDto = new PropertyDto();
	responseDto.setId(savedProperty.getId());
	responseDto.setName(savedProperty.getName());
	responseDto.setNumberOfBeds(savedProperty.getNumberOfBeds());
	responseDto.setNumberOfRooms(savedProperty.getNumberOfRooms());
	responseDto.setNumberOfBathrooms(savedProperty.getNumberOfBathrooms());
	responseDto.setNumberOfGuestAllowed(savedProperty.getNumberOfGuestAllowed());
	responseDto.setCity(savedProperty.getCity().getName());
	responseDto.setArea(savedProperty.getArea().getName());
	responseDto.setState(savedProperty.getState().getName());

	// Fetch and set rooms
	List<RoomsDto> roomsDtoList = roomsRepository.findAll().stream()
			.filter(r -> r.getProperty().getId().equals(savedProperty.getId()))
			.map(r -> new RoomsDto(r.getId(), r.getRoomType(), r.getBasePrice()))
			.toList();
	responseDto.setRooms(roomsDtoList);

	// Set image URLs
	responseDto.setImageUrls(fileUrls);

	emailProducer.sendEmail(new EmailRequest(
			"dtanmoy0403@gmail.com",//email receiver
			"Property Added",
			"Your property has been added successfully. "
	));

	return responseDto;
}

public APIResponse searchProperty(String city, LocalDate date){
	List<Property> properties =propertyRepository.searchProperty(city,date);
	APIResponse<List<Property>> response = new APIResponse<>();

	response.setMessage("Search Result");
	response.setStatus(200);
	response.setData(properties);
	return response;
}
}
