package team.jit.component;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
public class ScopeRequest implements Serializable {

    private String name;
    private String scope;

    public ScopeRequest(String scope) {
        this.scope = scope;
    }
}
