package mvc;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import team.jit.dto.EmployeeForm;
import team.jit.dto.EmployeeUpdateForm;
import team.jit.entity.Employee;
import team.jit.entity.Position;
import team.jit.service.EmployeeService;

import java.math.BigDecimal;

@AllArgsConstructor
@Controller
@RequestMapping("/mvc/employees")
public class EmployeeMvcController {

    private final String mvcList   = "employees2/list";
    private final String mvcDetail = "employees2/detail";
    private final String mvcForm   = "employees2/form";
    private final String mvcEdit   = "employees2/edit";
    private final String mvcPaged  = "employees2/paged";

    private final EmployeeService employeeService;

    @GetMapping
    public String listEmployees(Model model) {
        model.addAttribute("employees", employeeService.findAllEmployees());
        return mvcList;
    }

    /**
     * Paged + filtered employee list.
     *
     * All filter params are optional — omitting them returns all employees for the current page.
     * Empty strings from the form are normalised to null so the JPQL IS NULL check works correctly.
     *
     * filterParams is pre-built and put in the model so the JSP can append it to
     * pagination links without duplicating the query-string-building logic in the view.
     */
    @GetMapping("/paged?page=1&size=5&name=bob&salaryMin=1200")
    public String pagedEmployees(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) BigDecimal salaryMin,
            @RequestParam(required = false) BigDecimal salaryMax,
            @PageableDefault(size = 5, sort = "id") Pageable pageable,
            Model model) {

        // Normalise empty strings → null so JPQL ":param IS NULL" short-circuit works
        if (name != null && name.isBlank()) name = null;

        Page<Employee> page = employeeService.findPagedEmployees(name, salaryMin, salaryMax, pageable);

        // Pre-build the filter query string fragment so the JSP can attach it to page links
        // e.g.  "&name=john&salaryMin=3000"
        StringBuilder filterParams = new StringBuilder();
        if (name      != null) filterParams.append("&name=").append(name);
        if (salaryMin != null) filterParams.append("&salaryMin=").append(salaryMin);
        if (salaryMax != null) filterParams.append("&salaryMax=").append(salaryMax);

        model.addAttribute("page",         page);
        model.addAttribute("name",         name      != null ? name      : "");
        model.addAttribute("salaryMin",    salaryMin != null ? salaryMin : "");
        model.addAttribute("salaryMax",    salaryMax != null ? salaryMax : "");
        model.addAttribute("filterParams", filterParams.toString());

        return mvcPaged;
    }

    // ...existing code...

    @GetMapping("/{id}")
    public String viewEmployee(@PathVariable Long id, Model model) {
        model.addAttribute("employee", employeeService.findEmployee(id));
        return mvcDetail;
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("employeeForm", new EmployeeForm());
        return mvcForm;
    }

    @PostMapping
    public String saveEmployee(@Valid @ModelAttribute("employeeForm") EmployeeForm form,
                               BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return mvcForm;
        }
        employeeService.saveEmployee(form);
        return "redirect:/mvc/employees";
    }

    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable Long id, Model model) {
        model.addAttribute("employee", employeeService.findEmployee(id));
        model.addAttribute("employeeUpdateForm", new EmployeeUpdateForm());
        model.addAttribute("positions", Position.values());
        return mvcEdit;
    }

    @PostMapping("/{id}")
    public String updateEmployee(@PathVariable Long id,
                                 @Valid @ModelAttribute("employeeUpdateForm") EmployeeUpdateForm form,
                                 BindingResult bindingResult,
                                 Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("employee", employeeService.findEmployee(id));
            model.addAttribute("positions", Position.values());
            return mvcEdit;
        }
        employeeService.updateEmployee(id, form);
        return "redirect:/mvc/employees";
    }

    @PostMapping("/{id}/delete")
    public String deleteEmployee(@PathVariable Long id) {
        employeeService.deleteEmployee(id);
        return "redirect:/mvc/employees";
    }
}

