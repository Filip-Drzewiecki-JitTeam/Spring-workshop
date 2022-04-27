package team.jit.controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import team.jit.component.ScopeRequest;
import team.jit.component.ScopeSingleton;
import team.jit.component.ScopePrototype;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/scopes")
@AllArgsConstructor
public class ScopeController {

    private final ScopeSingleton scopeSingletonOne;
    private final ScopeSingleton scopeSingletonTwo;
    private final ScopePrototype scopePrototypeOne;
    private final ScopePrototype scopePrototypeTwo;
    private final ScopeRequest scopeRequest;

    @GetMapping
    public List<Object> findSingletonBuilding() {
        return formData();
    }

    @PutMapping
    public List<Object> editSingletonBuilding() {
        scopeSingletonOne.setName("newName");
        scopePrototypeOne.setName("newName");
        scopeRequest.setName("newName");
        return formData();
    }

    private List<Object> formData() {
        Map<String, Object> requestScope = new HashMap<>();
        requestScope.put("scope", scopeRequest.getScope());
        requestScope.put("name", scopeRequest.getName());

        List<Object> list = new ArrayList<>();
        list.add(scopeSingletonOne);
        list.add(scopeSingletonTwo);
        list.add(scopePrototypeOne);
        list.add(scopePrototypeTwo);
        list.add(requestScope);
        return list;
    }
}
