package ning.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class IndexController {

    @RequestMapping(value = "/index")
    @PreAuthorize("hasAuthority('index')")
    public String index() {
        return "index";
    }

    @RequestMapping(value = "/hasAuthority")
    @PreAuthorize("hasAuthority('hasAuthority')")
    public String hasAuthority() {
        return "hasAuthority";
    }

    @RequestMapping(value = "/hasRole")
    @PreAuthorize("hasRole('hasRole')")
    public String hasRole() {
        return "hasRole";
    }

}
