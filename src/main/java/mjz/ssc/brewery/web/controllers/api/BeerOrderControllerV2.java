package mjz.ssc.brewery.web.controllers.api;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mjz.ssc.brewery.domain.security.User;
import mjz.ssc.brewery.security.perms.BeerOrderReadPermissionV2;
import mjz.ssc.brewery.services.BeerOrderService;
import mjz.ssc.brewery.web.model.BeerOrderDto;
import mjz.ssc.brewery.web.model.BeerOrderPagedList;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v2/orders/")
public class BeerOrderControllerV2 {

    private static final Integer DEFAULT_PAGE_NUMBER = 0;
    private static final Integer DEFAULT_PAGE_SIZE = 25;

    private final BeerOrderService beerOrderService;

    @BeerOrderReadPermissionV2
    @GetMapping
    public BeerOrderPagedList listOrders(@AuthenticationPrincipal User user, //injecting Authentication principal (instead of passing the user Id as path parameter)
                                         @RequestParam(value = "pageNumber", required = false) Integer pageNumber,
                                         @RequestParam(value = "pageSize", required = false) Integer pageSize){

        if (pageNumber == null || pageNumber < 0){
            pageNumber = DEFAULT_PAGE_NUMBER;
        }

        if (pageSize == null || pageSize < 1) {
            pageSize = DEFAULT_PAGE_SIZE;
        }

        if (user.getCustomer() != null) {
            return beerOrderService.listOrders(user.getCustomer().getId(), PageRequest.of(pageNumber, pageSize));
        } else {
            return beerOrderService.listOrders(PageRequest.of(pageNumber, pageSize));
        }
    }

    @BeerOrderReadPermissionV2
    @GetMapping("{orderId}")
    public BeerOrderDto getOrder(@PathVariable("orderId") UUID orderId){
       BeerOrderDto beerOrderDto = beerOrderService.getOrderById(orderId);

       if(beerOrderDto == null) {
           throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Order Not Found");
       }

       log.debug("Found Order: " + beerOrderDto);

       return beerOrderDto;
    }
}
