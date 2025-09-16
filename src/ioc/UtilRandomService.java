package ioc;

import com.google.inject.Singleton;

import java.util.Random;
@Singleton
public class UtilRandomService implements RandomService {
    private final static Random random = new Random();

    @Override
    public double value() {
        return random.nextDouble();
    }
}