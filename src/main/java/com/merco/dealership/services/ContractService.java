package com.merco.dealership.services;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedModel;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.merco.dealership.controllers.ContractController;
import com.merco.dealership.dto.ContractResponseDTO;
import com.merco.dealership.entities.Contract;
import com.merco.dealership.mapper.Mapper;
import com.merco.dealership.repositories.ContractRepository;
import com.merco.dealership.services.exceptions.DataViolationException;
import com.merco.dealership.services.exceptions.DatabaseException;
import com.merco.dealership.services.exceptions.ResourceNotFoundException;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ConstraintViolationException;

@Service
public class ContractService {
	@Autowired
	private ContractRepository repository;

	@Autowired
	PagedResourcesAssembler<ContractResponseDTO> assembler;

	public PagedModel<EntityModel<ContractResponseDTO>> findAll(Pageable pageable) {
		Page<Contract> contractPage = repository.findAll(pageable);
		Page<ContractResponseDTO> contractPageDTO = contractPage
				.map(p -> Mapper.modelMapper(p, ContractResponseDTO.class));
		contractPageDTO.map(i -> i.add(linkTo(methodOn(ContractController.class).findById(i.getId())).withSelfRel()));
		Link link = linkTo(
				methodOn(ContractController.class).findAll(pageable.getPageNumber(), pageable.getPageSize(), "asc"))
				.withSelfRel();
		return assembler.toModel(contractPageDTO, link);
	}

	public ContractResponseDTO findById(String id) {
		Contract contract = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException(id));
		ContractResponseDTO contractDTO = Mapper.modelMapper(contract, ContractResponseDTO.class);
		contractDTO.add(linkTo(methodOn(ContractController.class).findById(id)).withSelfRel());
		return contractDTO;
	}

	@Transactional
	public ContractResponseDTO create(Contract obj) {
		try {
			Contract contract = repository.save(obj);
			ContractResponseDTO contractDTO = Mapper.modelMapper(contract, ContractResponseDTO.class);
			contractDTO.add(linkTo(methodOn(ContractController.class).findById(contract.getId())).withSelfRel());
			return contractDTO;
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
	public Contract patch(String id, Contract obj) {
		try {
			Contract entity = repository.getReferenceById(id);
			updateData(entity, obj);
			Contract Contract = repository.save(entity);
			return Contract;
		} catch (EntityNotFoundException e) {
			throw new ResourceNotFoundException(id);
		} catch (ConstraintViolationException e) {
			throw new DatabaseException("Some invalid field.");
		} catch (DataIntegrityViolationException e) {
			throw new DataViolationException();
		}
	}

	private void updateData(Contract entity, Contract obj) {
//		if (obj.getName() != null)
//			entity.setName(obj.getName());
//		if (obj.getEmail() != null)
//			entity.setEmail(obj.getEmail());
//		if (obj.getPhone() != null)
//			entity.setPhone(obj.getPhone());
	}
}
