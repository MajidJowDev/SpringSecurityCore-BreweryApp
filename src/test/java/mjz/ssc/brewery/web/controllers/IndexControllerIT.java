package mjz.ssc.brewery.web.controllers;

import mjz.ssc.brewery.repositories.BeerInventoryRepository;
import mjz.ssc.brewery.repositories.BeerRepository;
import mjz.ssc.brewery.repositories.CustomerRepository;
import mjz.ssc.brewery.services.BeerOrderService;
import mjz.ssc.brewery.services.BeerService;
import mjz.ssc.brewery.services.BreweryService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest
public class IndexControllerIT extends BaseIT {

    @MockBean
    BeerRepository beerRepository;

    @MockBean
    BeerInventoryRepository beerInventoryRepository;

    @MockBean
    BreweryService breweryService;

    @MockBean
    CustomerRepository customerRepository;

    @MockBean
    BeerService beerService;

    @MockBean
    BeerOrderService beerOrderService;

    @Test
    void testGetIndexSlash() throws Exception{
        mockMvc.perform(get("/"))
                .andExpect(status().isOk());
    }
}
