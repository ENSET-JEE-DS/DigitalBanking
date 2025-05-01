package application.digitalbankingapplication.mapper;

import application.digitalbankingapplication.dto.CustomerDTO;
import application.digitalbankingapplication.model.Customer;
import org.springframework.stereotype.Service;
import org.apache.commons.beanutils.BeanUtils;

// MapStruct
@Service
public class BankAccountMapperImpl {
    public CustomerDTO customerToCustomerDTO(Customer customer) {
        CustomerDTO customerDTO = new CustomerDTO();
        try {    
            BeanUtils.copyProperties(customerDTO, customer);
        } catch (Exception e) {
            throw new RuntimeException("Error mapping Customer to CustomerDTO", e);
        }
        return customerDTO;
    }

    public Customer customerDTOToCustomer(CustomerDTO customerDTO) {
        Customer customer = new Customer();
        try {
            BeanUtils.copyProperties(customer, customerDTO);
        } catch (Exception e) {
            throw new RuntimeException("Error mapping CustomerDTO to Customer", e);
        }
        return customer;
    }
}
