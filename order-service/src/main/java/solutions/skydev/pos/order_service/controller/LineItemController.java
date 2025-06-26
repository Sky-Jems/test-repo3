package solutions.skydev.pos.order_service.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import solutions.skydev.pos.common.order_service.dto.response.LineItemResponseDto;
import solutions.skydev.pos.order_service.model.mapper.LineItemMapper;
import solutions.skydev.pos.order_service.service.LineItemService;

import java.util.List;

@RestController
@RequestMapping("/line-items")
public class LineItemController {
    
    private final LineItemService lineItemService;
    private final LineItemMapper lineItemMapper;
    
    @Autowired
    public LineItemController(LineItemService lineItemService, LineItemMapper lineItemMapper) {
        this.lineItemService = lineItemService;
        this.lineItemMapper = lineItemMapper;
    }
    
    @GetMapping
    public ResponseEntity<List<LineItemResponseDto>> getAllLineItems() {
        List<LineItemResponseDto> lineItems = this.lineItemMapper.toResponseDtoList(this.lineItemService.getAllLineItems());
        return ResponseEntity.ok(lineItems);
    }
}
