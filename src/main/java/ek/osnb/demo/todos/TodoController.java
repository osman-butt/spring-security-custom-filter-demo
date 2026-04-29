package ek.osnb.demo.todos;


import ek.osnb.demo.security.apikey.ApiKeyPrincipal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicLong;

@RestController
@RequestMapping("/api/todos")
class TodoController {

    private static final Logger log = LoggerFactory.getLogger(TodoController.class);
    private final AtomicLong idCounter = new AtomicLong(1L);
    private final List<TodoDetails> todos = new ArrayList<>();

    public TodoController() {
        todos.add(new TodoDetails(idCounter.getAndIncrement(), "Buy groceries", false));
        todos.add(new TodoDetails(idCounter.getAndIncrement(), "Walk the dog", true));
        todos.add(new TodoDetails(idCounter.getAndIncrement(), "Read a book", false));
    }

    @GetMapping
    public List<TodoDetails> getTodos(Authentication authentication) {
        log.info("User '{}' is fetching todos", ((ApiKeyPrincipal) Objects.requireNonNull(authentication.getPrincipal())).name());
        return todos;
    }

    @PostMapping
    public TodoDetails createTodo(@RequestBody CreateTodoRequest newTodo) {
        TodoDetails newTodoDetails = new TodoDetails(idCounter.getAndIncrement(), newTodo.title(), false);

        todos.add(newTodoDetails);

        return newTodoDetails;
    }
}
