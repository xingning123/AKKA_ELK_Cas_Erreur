package fr.rades.template.infrastructure.SuperviseurFile;

import akka.actor.*;
import scala.concurrent.duration.Duration;
import scala.concurrent.duration.FiniteDuration;

import static java.util.concurrent.TimeUnit.MILLISECONDS;

public class Settings extends AbstractExtensionId<SettingsImpl>
        implements ExtensionIdProvider {
    public final static Settings Extension = new Settings();
    private Settings() {}
    public Settings lookup() {
        return Settings.Extension;
    }

    public SettingsImpl createExtension(ExtendedActorSystem system) {
        return new SettingsImpl(system);
    }
}

class SettingsImpl implements Extension {

    final FiniteDuration lotDuration;
    final int nombreRequetesLimite;
    final String endPoint;

    final String url;
    String username;
    String password;


    public SettingsImpl(ExtendedActorSystem system) {
        nombreRequetesLimite = system.settings().config().getInt("app.createur-lot-requetes.nombre-requetes-limite");
        lotDuration =
                Duration.create(
                        system.settings().config().getDuration(
                                "app.createur-lot-requetes.lot-duration", MILLISECONDS), MILLISECONDS);

        endPoint =
                system.settings().config().getString("app.createur-lot-requetes.action");

//        url = system.settings().config().getString( "elasticsearch.url");
        String rawUrl = system.settings().config().getString("elasticsearch.url");
        url = normalizeUrl(rawUrl);
        username = system.settings().config().getString("elasticsearch.username");
        password = system.settings().config().getString("elasticsearch.password");
    }

    private String normalizeUrl(String rawUrl) {
        if (rawUrl.startsWith("//")) {
            // Remove the double slashes if present
            return rawUrl.substring(2);
        } else {
            return rawUrl;
        }
    }
}

abstract class SettingsActor extends AbstractLoggingActor {
    final SettingsImpl settings = Settings.Extension.get(context().system());
}
