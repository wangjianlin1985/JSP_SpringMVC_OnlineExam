// 
// 
// 

package exam.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.stereotype.Controller;

@Controller
public class ValidController
{
    @RequestMapping({ "/valid" })
    public String valid() {
        return "valid";
    }
}
