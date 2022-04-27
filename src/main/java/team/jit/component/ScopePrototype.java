package team.jit.component;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ScopePrototype {

    private String name;
    private String scope;

    public ScopePrototype(String scope) {
        this.scope = scope;
    }
}
