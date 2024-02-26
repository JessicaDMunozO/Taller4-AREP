package edu.escuelaing.arem.ASE.app;

@Component

public class HelloController {

    @GetMapping("/")
    public static String helloController() {
        return "Hello Controller";
    }

    @GetMapping("/helloname")
    public static String helloName(String name) {
        return "Hello " + name;
    }

    @GetMapping("/square")
    public static double square(String val) {
        return Double.valueOf(val) * Double.valueOf(val);
    }

}
