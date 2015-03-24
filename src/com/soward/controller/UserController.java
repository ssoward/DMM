//package com.soward.controller;
//
//import com.soward.util.SupplierUtil;
//import org.codehaus.jackson.map.ObjectMapper;
//
//import javax.servlet.ServletException;
//import javax.servlet.http.HttpServlet;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.io.IOException;
//import java.util.List;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.MediaType;
//import org.springframework.http.ResponseEntity;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestMethod;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.ResponseBody;
//import org.springframework.web.servlet.ModelAndView;
//import org.springframework.ui.Model;
//
///**
// * Created by ssoward on 9/6/14.
// */
//
//
//@Controller
////@RequestMapping("/members")
//public class UserController extends HttpServlet {
//
//
////    @RequestMapping(method = RequestMethod.GET, value = "", produces = {MediaType.APPLICATION_JSON_VALUE})
////    @ResponseBody
////    public String searchMembers(@RequestParam String searchStr, @RequestParam Long unitNumber) {
////        return "hello world";
////    }
//    @RequestMapping("hello-world")
//    protected ModelAndView handleRequest(HttpServletRequest request,
//                                         HttpServletResponse response) throws Exception {
//
//        System.out.println(">> HelloWorldController.handleRequest()");
//
//        return new ModelAndView("hello-world");
//    }
//    @RequestMapping(value="/hello", method=RequestMethod.GET)
//    public String hello(@RequestParam(value="name", required=false, defaultValue="World") String name, Model model) {
//        model.addAttribute("name", name);
//        return "helloworld";
//    }
//
//
//}
