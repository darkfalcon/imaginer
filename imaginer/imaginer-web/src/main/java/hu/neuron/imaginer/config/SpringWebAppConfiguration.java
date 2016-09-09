package hu.neuron.imaginer.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@EnableWebMvc
@ComponentScan(basePackages={"hu.neuron.imaginer"})
public class SpringWebAppConfiguration {

}
