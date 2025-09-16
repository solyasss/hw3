package ioc;

import com.google.inject.AbstractModule;
import com.google.inject.name.Names;

public class ConfigModule extends AbstractModule

{
    @Override
    protected void configure() {
        bind(RandomService.class)
                .annotatedWith(Names.named("util"))
                .to(UtilRandomService.class);

        bind(RandomService.class).to(LcRandomService.class);
        bind(OtpGenerator.class).to(DigitOtpGenerator.class);
        bind(FilenameGenerator.class).to(SafeFilenameGenerator.class);

    }
}
