package ioc;

import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class ioC
{
    private final RandomService randomService;
    private final SaltGenerator saltGenerator;
    private final OtpGenerator otpGenerator;
    private final FilenameGenerator filenameGenerator;

    @Inject
    public ioC(RandomService randomService,
               SaltGenerator saltGenerator,
               OtpGenerator otpGenerator,
               FilenameGenerator filenameGenerator)
    {
        this.randomService = randomService;
        this.saltGenerator = saltGenerator;
        this.otpGenerator = otpGenerator;
        this.filenameGenerator = filenameGenerator;

        System.out.println("ioC: "
                + randomService.getClass().getName() + " "
                + randomService.hashCode());
    }

    public void demo()
    {
        System.out.println(randomService.value());
        System.out.println(saltGenerator.getSalt(10));
        System.out.println(otpGenerator.otp(8));

        System.out.println(filenameGenerator.filename(12));
    }
}
