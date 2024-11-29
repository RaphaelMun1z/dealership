package com.merco.dealership.entities;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "tb_appointments")
public class Appointment implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private String id;

	@NotNull(message = "Required field")
	private String clientID;

	@NotNull(message = "Required field")
	private String vehicleID;

	@NotNull(message = "Required field")
	private LocalDate date;

	@NotNull(message = "Required field")
	private String responsibleSellerID;

	@NotNull(message = "Required field")
	private String appointmentType;

	private String appointmentStatus;

	public Appointment() {
	}

	public Appointment(String id, @NotNull(message = "Required field") String clientID,
			@NotNull(message = "Required field") String vehicleID, @NotNull(message = "Required field") LocalDate date,
			@NotNull(message = "Required field") String responsibleSellerID,
			@NotNull(message = "Required field") String appointmentType, String appointmentStatus) {
		super();
		this.id = id;
		this.clientID = clientID;
		this.vehicleID = vehicleID;
		this.date = date;
		this.responsibleSellerID = responsibleSellerID;
		this.appointmentType = appointmentType;
		this.appointmentStatus = appointmentStatus;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getClientID() {
		return clientID;
	}

	public void setClientID(String clientID) {
		this.clientID = clientID;
	}

	public String getVehicleID() {
		return vehicleID;
	}

	public void setVehicleID(String vehicleID) {
		this.vehicleID = vehicleID;
	}

	public LocalDate getDate() {
		return date;
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}

	public String getResponsibleSellerID() {
		return responsibleSellerID;
	}

	public void setResponsibleSellerID(String responsibleSellerID) {
		this.responsibleSellerID = responsibleSellerID;
	}

	public String getAppointmentType() {
		return appointmentType;
	}

	public void setAppointmentType(String appointmentType) {
		this.appointmentType = appointmentType;
	}

	public String getAppointmentStatus() {
		return appointmentStatus;
	}

	public void setAppointmentStatus(String appointmentStatus) {
		this.appointmentStatus = appointmentStatus;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Appointment other = (Appointment) obj;
		return Objects.equals(id, other.id);
	}

}
