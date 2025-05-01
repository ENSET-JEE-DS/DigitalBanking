package application.digitalbankingapplication.mapper;

import org.mapstruct.Mapper;

import application.digitalbankingapplication.dto.CustomerDTO;
import application.digitalbankingapplication.model.Customer;

@Mapper(componentModel = "spring")
public interface CustomerMapper {
    CustomerDTO customerToCustomerDTO(Customer customer);

    Customer customerDTOToCustomer(CustomerDTO customerDTO);
}
