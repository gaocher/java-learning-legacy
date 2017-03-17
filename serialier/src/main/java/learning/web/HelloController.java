package learning.web;

import learning.enums.PersonType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by isaac on 24/02/2017.
 */
@RestController
@RequestMapping("/hello")
public class HelloController {
    @RequestMapping("/world")
    public ResponseResult say(){
        ResponseResult responseResult = new ResponseResult();
        responseResult.setPersonType(PersonType.Man);
        responseResult.setResult("xxxx");

        return responseResult;
    }

    @RequestMapping(value = "/world",method = RequestMethod.POST)
    public void pp(@RequestBody  ResponseResult responseResult){
        System.err.println(responseResult);
    }
}
