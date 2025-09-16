import com.google.inject.Guice;
import com.google.inject.Injector;
import ioc.ConfigModule;
import ioc.ioC;

public class Main {
    public static void main(String[] args) {


//        chaining c = new chaining();
//        c.demo_method();
//
//        chaining2 c2 = new chaining2();
//        c2.demo_method();
//
//        point p = new point();
//        p.demo_method();
//
//        threading th = new threading();
//        th.demo_threads();

//        fileio ff = new fileio();
//        ff.demo();
//        Db db = new Db();
//        db.demo();

        Injector injector = Guice.createInjector(new ConfigModule());
        ioC ioc = injector.getInstance(ioC.class);
        ioc.demo();

    }
}
