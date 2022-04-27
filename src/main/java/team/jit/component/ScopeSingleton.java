package team.jit.component;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ScopeSingleton {

    private String name;
    private String scope;

    public ScopeSingleton(String scope) {
        this.scope = scope;
    }
}
