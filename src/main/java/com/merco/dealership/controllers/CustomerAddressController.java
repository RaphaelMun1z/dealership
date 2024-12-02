package com.merco.dealership.controllers;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.merco.dealership.dto.CustomerAddressResponseDTO;
import com.merco.dealership.entities.CustomerAddress;
import com.merco.dealership.services.CustomerAddressService;

import jakarta.validation.Valid;

@RestController
@RequestMapping(value = "/customers-address")
public class CustomerAddressController {
	@Autowired
	private CustomerAddressService service;

	@GetMapping
	public ResponseEntity<List<CustomerAddressResponseDTO>> findAll() {
		List<CustomerAddress> list = service.findAllCached();
		List<CustomerAddressResponseDTO> CustomerAddresss = new ArrayList<>();

		for (CustomerAddress CustomerAddress : list) {
			CustomerAddresss.add(new CustomerAddressResponseDTO(CustomerAddress));
		}
		return ResponseEntity.ok().body(CustomerAddresss);
	}

	@GetMapping(value = "/{id}")
	public ResponseEntity<CustomerAddress> findById(@PathVariable String id) {
		CustomerAddress obj = service.findById(id);
		return ResponseEntity.ok().body(obj);
	}

	@PostMapping
	public ResponseEntity<CustomerAddressResponseDTO> insert(@RequestBody @Valid CustomerAddress obj) {
		obj = service.create(obj);
		CustomerAddressResponseDTO CustomerAddress = new CustomerAddressResponseDTO(obj);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(CustomerAddress.getId())
				.toUri();
		return ResponseEntity.created(uri).body(CustomerAddress);
	}

	@DeleteMapping(value = "/{id}")
	public ResponseEntity<Void> delete(@PathVariable String id) {
		service.delete(id);
		return ResponseEntity.noContent().build();
	}

	@PatchMapping(value = "/{id}")
	public ResponseEntity<CustomerAddress> patch(@PathVariable String id, @RequestBody CustomerAddress obj) {
		obj = service.patch(id, obj);
		return ResponseEntity.ok().body(obj);
	}

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public Map<String, String> validationExceptionHandler(MethodArgumentNotValidException ex) {
		Map<String, String> errors = new HashMap<>();
		ex.getBindingResult().getAllErrors().forEach((err) -> {
			String fieldName = ((FieldError) err).getField();
			String errorMessage = err.getDefaultMessage();
			errors.put(fieldName, errorMessage);
		});
		return errors;
	}
}