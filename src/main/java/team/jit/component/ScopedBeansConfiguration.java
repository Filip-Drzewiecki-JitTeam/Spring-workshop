package team.jit.component;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.web.context.annotation.RequestScope;

import static org.springframework.beans.factory.config.BeanDefinition.SCOPE_PROTOTYPE;
import static org.springframework.beans.factory.config.BeanDefinition.SCOPE_SINGLETON;

@Configuration
public class ScopedBeansConfiguration {

    @Bean
    @Scope(SCOPE_SINGLETON)
    public ScopeSingleton scopeSingleton() {
        return new ScopeSingleton("singleton");
    }

    @Bean
    @Scope(SCOPE_PROTOTYPE)
    public ScopePrototype scopePrototype() {
        return new ScopePrototype("prototype");
    }

    @Bean
    @RequestScope
    public ScopeRequest scopeRequest() {
        return new ScopeRequest("request");
    }
}
