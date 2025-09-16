package ioc;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;


@Singleton
public class SafeFilenameGenerator implements FilenameGenerator
{

    private final RandomService random;

    private static final char[] ALLOWED = (
            "ABCDEFGHIJKLMNOPQRSTUVWXYZ" +
                    "abcdefghijklmnopqrstuvwxyz" +
                    "0123456789" +
                    "_-."
    ).toCharArray();

    @Inject
    public SafeFilenameGenerator(@Named("util") RandomService random)
    {
        this.random = random;
        System.out.println("SafeFilenameGenerator: " +
                random.getClass().getName() + " " + random.hashCode());
    }

    @Override
    public String filename(int length) {
        if (length < 8) {
            throw new IllegalArgumentException("Filename length must be >= 8");
        }

        char[] buf = new char[length];
        for (int i = 0; i < length; i++) {
            int idx = (int) (random.value() * ALLOWED.length);
            if (idx == ALLOWED.length) idx = ALLOWED.length - 1;
            buf[i] = ALLOWED[idx];
        }

        if (buf[0] == '.') buf[0] = '_';

        return new String(buf);
    }
}
