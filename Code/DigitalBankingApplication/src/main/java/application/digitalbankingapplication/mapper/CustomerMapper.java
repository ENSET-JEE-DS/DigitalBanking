package application.digitalbankingapplication.mapper;


import application.digitalbankingapplication.dto.CustomerDTO;
import application.digitalbankingapplication.model.Customer;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CustomerMapper {
    CustomerDTO customerToCustomerDTO(Customer customer);
    Customer customerDTOToCustomer(CustomerDTO customerDTO);
}