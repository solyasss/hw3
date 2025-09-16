package ioc;

import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class DigitOtpGenerator implements OtpGenerator
{
    private final RandomService randomService;

    @Inject
    public DigitOtpGenerator(RandomService randomService)
    {
        this.randomService = randomService;
        System.out.println("DigitGenerator: "
                + randomService.getClass().getName() + " "
                + randomService.hashCode()
        );
    }

    @Override
    public String otp(int length) {
        char[] chars = new char[length];
        for (int i = 0; i < length; i++) {
            chars[i] = (char) (48 + (int)(randomService.value() * 10));
        }
        return new String(chars);
    }

}