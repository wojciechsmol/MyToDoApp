package pl.wojtek.smol.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;

        import javax.servlet.http.HttpServletRequest;
        import org.springframework.web.servlet.ModelAndView;

//Handles Exceptions
@Controller("error")
public class ErrorController {

    // This method handles the exception
    @ExceptionHandler(Exception.class)
    public ModelAndView handleException
            (HttpServletRequest request, Exception ex){
        ModelAndView mv = new ModelAndView();

        mv.addObject("exception", ex.getLocalizedMessage());
        mv.addObject("url", request.getRequestURL());

        mv.setViewName("error");
        return mv;
    }

}
