package mjz.ssc.brewery.web.controllers;

import mjz.ssc.brewery.repositories.BeerInventoryRepository;
import mjz.ssc.brewery.repositories.BeerRepository;
import mjz.ssc.brewery.repositories.CustomerRepository;
import mjz.ssc.brewery.services.BeerService;
import mjz.ssc.brewery.services.BreweryService;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;


public abstract class BaseIT {
    @Autowired
    WebApplicationContext wac;

    public MockMvc mockMvc;
//commented out because when we want to test form methods, the data does not loaded in database
//    @MockBean
//    BeerRepository beerRepository;
//
//    @MockBean
//    BeerInventoryRepository beerInventoryRepository;
//
//    @MockBean
//    BreweryService breweryService;
//
//    @MockBean
//    CustomerRepository customerRepository;
//
//    @MockBean
//    BeerService beerService;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(wac)
                .apply(springSecurity())
                .build();
    }
}