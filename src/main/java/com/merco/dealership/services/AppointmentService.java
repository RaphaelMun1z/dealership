package com.merco.dealership.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.merco.dealership.entities.Appointment;
import com.merco.dealership.repositories.AppointmentRepository;
import com.merco.dealership.services.exceptions.DataViolationException;
import com.merco.dealership.services.exceptions.DatabaseException;
import com.merco.dealership.services.exceptions.ResourceNotFoundException;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ConstraintViolationException;

@Service
public class AppointmentService {
	@Autowired
	private AppointmentRepository repository;

	public List<Appointment> findAllCached() {
		return findAll();
	}

	public List<Appointment> findAll() {
		return repository.findAll();
	}

	public Appointment findById(String id) {
		Optional<Appointment> obj = repository.findById(id);
		return obj.orElseThrow(() -> new ResourceNotFoundException(id));
	}

	@Transactional
	public Appointment create(Appointment obj) {
		try {
			Appointment Appointment = repository.save(obj);
			return Appointment;
		} catch (DataIntegrityViolationException e) {
			throw new DataViolationException();
		}
	}

	@Transactional
	public void delete(String id) {
		try {
			if (repository.existsById(id)) {
				repository.deleteById(id);
			} else {
				throw new ResourceNotFoundException(id);
			}
		} catch (EmptyResultDataAccessException e) {
			throw new ResourceNotFoundException(id);
		} catch (DataIntegrityViolationException e) {
			throw new DatabaseException(e.getMessage());
		}
	}

	@Transactional
	public Appointment patch(String id, Appointment obj) {
		try {
			Appointment entity = repository.getReferenceById(id);
			updateData(entity, obj);
			Appointment Appointment = repository.save(entity);
			return Appointment;
		} catch (EntityNotFoundException e) {
			throw new ResourceNotFoundException(id);
		} catch (ConstraintViolationException e) {
			throw new DatabaseException("Some invalid field.");
		} catch (DataIntegrityViolationException e) {
			throw new DataViolationException();
		}
	}

	private void updateData(Appointment entity, Appointment obj) {
//		if (obj.getName() != null)
//			entity.setName(obj.getName());
//		if (obj.getEmail() != null)
//			entity.setEmail(obj.getEmail());
//		if (obj.getPhone() != null)
//			entity.setPhone(obj.getPhone());
	}
}
