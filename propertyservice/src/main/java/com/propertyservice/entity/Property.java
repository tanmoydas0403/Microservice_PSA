package com.propertyservice.entity;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name="property")
public class Property {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String name;
	
	@Column(name="number_of_beds")
	private Integer numberOfBeds;
	
	@Column(name="number_of_rooms")
	private Integer numberOfRooms;
	
	@Column(name="number_of_bathrooms")
	private Integer numberOfBathrooms;
	
	@Column(name="number_of_guests_allowed")
	private Integer numberOfGuestAllowed;
	
	@ManyToOne
	@JoinColumn(name="area_id")
	private Area area;
	
	@ManyToOne
	@JoinColumn(name="city_id")
	private City city;
	
	@ManyToOne
	@JoinColumn(name="state_id")
	private State state;
	
	@OneToMany(mappedBy = "property", cascade = CascadeType.ALL, orphanRemoval = true)
	@JsonManagedReference
	private List<Rooms> rooms = new ArrayList<>();
}
