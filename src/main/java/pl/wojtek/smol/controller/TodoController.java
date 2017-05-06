package pl.wojtek.smol.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import pl.wojtek.smol.model.Todo;
import pl.wojtek.smol.service.TodoService;

import javax.validation.Valid;
import java.text.SimpleDateFormat;
import java.util.Date;

@Controller
public class TodoController {

    // Autowiring service used for displaying todos
    @Autowired
    TodoService service;

    @InitBinder
    protected void initBinder(WebDataBinder binder) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        binder.registerCustomEditor(Date.class, new CustomDateEditor(
                dateFormat, false));
    }

    //Gets current's user name
    private String getLoggedInUsername(ModelMap model) {
        Object principal = SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();

        if (principal instanceof UserDetails) {
            return ((UserDetails) principal).getUsername();
        }

        return principal.toString();

    }


    // Show Todo's page
    @RequestMapping(value = "/list-todos", method = RequestMethod.GET)
    public String showTodos(ModelMap model){
        String name = getLoggedInUsername(model);
        model.put("todos", service.retrieveTodos(name));
        return "list-todos";
    }


    // Show AddToDoPage
    @RequestMapping(value = "/add-todo", method = RequestMethod.GET)
    public String showAddTodoPage(ModelMap model){
        model.addAttribute("todo", new Todo(0, getLoggedInUsername(model), "", new Date(), false));
        return "todo";
    }

    // Delete Todo
    @RequestMapping(value = "/delete-todo", method = RequestMethod.GET)
    public String deleteTodo(@RequestParam int id){
        service.deleteTodo(id);
        return "redirect:/list-todos";
    }

    // Update Todo
    @RequestMapping(value = "/update-todo", method = RequestMethod.POST)
    public String updateTodo(ModelMap model, @Valid Todo todo, BindingResult result){
        // If there are errors
        if(result.hasErrors()){
            return "todo";
        }
        todo.setUser((String) model.get("name"));
        service.updateTodo(todo);
        return "redirect:/list-todos";

    }

    // Shows Update Todo Page
    @RequestMapping(value = "/update-todo", method = RequestMethod.GET)
    public String showUpdateTodoPage(ModelMap model, @RequestParam int id){
        Todo todo = service.retrieveTodo(id);
        model.put("todo", todo);
        return "todo";
    }

    // Add a todo to the list
    @RequestMapping(value = "/add-todo", method = RequestMethod.POST)
    public String addToDo(ModelMap model, @Valid Todo todo, BindingResult result){
        // If there are errors
        if(result.hasErrors()){
            return "todo";
        }
        service.addTodo(getLoggedInUsername(model), todo.getDesc(), new Date(), false);
        return "redirect:/list-todos";
    }


}
