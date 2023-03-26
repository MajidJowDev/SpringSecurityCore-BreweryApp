package mjz.ssc.brewery.web.controllers.api;

import mjz.ssc.brewery.domain.Beer;
import mjz.ssc.brewery.repositories.BeerRepository;
import mjz.ssc.brewery.web.controllers.BaseIT;
import mjz.ssc.brewery.web.model.BeerStyleEnum;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Random;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;

//@WebMvcTest
@SpringBootTest
public class BeerRestControllerIT extends BaseIT {
    @Autowired
    BeerRepository beerRepository;

    @DisplayName("Delete Tests")
    @Nested
    class DeleteTests {

        public Beer beerToDelete() {
            Random rand = new Random();

            return beerRepository.saveAndFlush(Beer.builder()
                    .beerName("Delete Me Beer")
                    .beerStyle(BeerStyleEnum.IPA)
                    .minOnHand(12)
                    .quantityToBrew(200)
                    .upc(String.valueOf(rand.nextInt(99999999)))
                    .build());
        }


        @Test
        void deleteBeerUrl() throws Exception {
            mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/beer/" + beerToDelete().getId())
                            .param("apiKey", "springuser").param("apiSecret", "springpass"))
                    .andExpect(MockMvcResultMatchers.status().isOk());
        }

        @Test
        void deleteBeerBadCredsUrl() throws Exception {
            mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/beer/" + beerToDelete().getId())
                            .param("apiKey", "spring").header("apiSecret", "guruXXXX"))
                    .andExpect(MockMvcResultMatchers.status().isUnauthorized());
        }

        @Test
        void deleteBeerBadCreds() throws Exception {
            mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/beer/" + beerToDelete().getId())
                            .header("Api-Key", "springuser").header("Api-Secret", "asdasdasd"))
                    .andExpect(MockMvcResultMatchers.status().isUnauthorized());
        }

        //testing the filter (not the memory store)
        @Test
        void deleteBeerTest() throws Exception {

            mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/beer/" + beerToDelete().getId())
                            .header("Api-Key", "springuser").header("Api-Secret", "springpass"))
                    .andExpect(MockMvcResultMatchers.status().isOk());
        }

        @Test
        void deleteBeerHttpBasicTest() throws Exception {

            mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/beer/" + beerToDelete().getId())
                            .with(httpBasic("springuser", "springpass")))
                    .andExpect(MockMvcResultMatchers.status().is2xxSuccessful());
        }

        @Test
        void deleteBeerHttpBasicUserRoleTest() throws Exception {

            mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/beer/" + beerToDelete().getId())
                            .with(httpBasic("user", "pass")))
                    .andExpect(MockMvcResultMatchers.status().isForbidden());
        }

        @Test
        void deleteBeerHttpBasicCustomerRoleTest() throws Exception {

            mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/beer/" + beerToDelete().getId())
                            .with(httpBasic("ali", "test")))
                    .andExpect(MockMvcResultMatchers.status().isForbidden());
        }

        @Test
        void deleteBeerNoAuth() throws Exception {
            mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/beer/" + beerToDelete().getId()))
                    .andExpect(MockMvcResultMatchers.status().isUnauthorized());
        }
    }

    @Test
    void findBeers() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/beer/"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }


    @Test
    void findBeerById() throws Exception {
        Beer beer = beerRepository.findAll().get(0);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/beer/" + beer.getId()))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void findBeerByUpc() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/beerUpc/0631234200036"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void findBeerFormADMIN() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/beers").param("beerName", "")
                .with(httpBasic("springuser", "springpass")))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
}
