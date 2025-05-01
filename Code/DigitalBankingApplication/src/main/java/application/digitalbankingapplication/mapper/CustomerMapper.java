package application.digitalbankingapplication.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import application.digitalbankingapplication.dto.CustomerDTO;
import application.digitalbankingapplication.model.Customer;

@Mapper(componentModel = "spring")
public interface CustomerMapper {
    // CustomerMapper INSTANCE = Mappers.getMapper(CustomerMapper.class);

    CustomerDTO customerToCustomerDTO(Customer customer);

    Customer customerDTOToCustomer(CustomerDTO customerDTO);
}
