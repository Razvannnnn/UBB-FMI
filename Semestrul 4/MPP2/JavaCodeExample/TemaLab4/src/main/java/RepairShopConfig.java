import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import repository.ComputerRepairRequestRepository;
import repository.ComputerRepairedFormRepository;
import repository.jdbc.ComputerRepairRequestJdbcRepository;
import repository.jdbc.ComputerRepairedFormJdbcRepository;
import services.ComputerRepairServices;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

@Configuration
public class RepairShopConfig {
    @Bean
    Properties getProps() {

     
    }

    @Bean
    ComputerRepairRequestRepository requestsRepo(){
       

    }

    @Bean
    ComputerRepairedFormRepository formsRepo(){
       

    }

    @Bean
    ComputerRepairServices services(){
       

    }

}
