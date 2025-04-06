//
//package com.example.demo;
//
//import com.example.demo.dto.TestDto;
//import io.swagger.v3.oas.annotations.Operation;
//import io.swagger.v3.oas.annotations.Parameter;
//import io.swagger.v3.oas.annotations.responses.ApiResponse;
//import io.swagger.v3.oas.annotations.responses.ApiResponses;
//import io.swagger.v3.oas.annotations.tags.Tag;
//import org.springframework.web.bind.annotation.*;
//
//@RestController
//@RequestMapping("/api")
//@Tag(name = "示例控制器", description = "示例API")
//public class TestController {
//
//    @GetMapping("/hello")
//    @Operation(summary = "返回问候语", description = "返回一个简单的问候语")
//    public String sayHello() {
//        return "Hello, Swagger!";
//    }
//
//
//    @PostMapping("/echo")
//    @Operation(summary = "Echo a message", description = "Returns the same message that was sent")
//    @ApiResponses(value = {
//            @ApiResponse(responseCode = "200", description = "Successfully echoed"),
//            @ApiResponse(responseCode = "400", description = "Invalid input")
//    })
//    public String echo(
//            @Parameter(description = "Message to echo", required = true)
//            @RequestBody TestDto message,
//            @Parameter(description = "Message name", required = false)
//            @RequestParam(required = false) String name) {
//        return "Echo: " + message;
//    }
//}