/*
 * SkinsRestorer
 *
 * Copyright (C) 2022 SkinsRestorer
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 */
package net.skinsrestorer.shared.subjects.messages;

import co.aikar.locales.LocaleManager;
import co.aikar.locales.MessageKey;
import lombok.RequiredArgsConstructor;
import net.skinsrestorer.builddata.BuildData;
import net.skinsrestorer.shared.plugin.SRPlatformAdapter;
import net.skinsrestorer.shared.plugin.SRPlugin;
import net.skinsrestorer.shared.subjects.SRForeign;
import net.skinsrestorer.shared.utils.C;
import net.skinsrestorer.shared.utils.LocaleParser;
import net.skinsrestorer.shared.utils.PropertyReader;

import javax.inject.Inject;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Locale;

@RequiredArgsConstructor(onConstructor_ = @Inject)
public class MessageLoader {
    private final SRPlugin plugin;
    private final LocaleManager<SRForeign> manager;
    private final SRPlatformAdapter<?> adapter;

    public void loadMessages() throws IOException {
        Path localesFolder = plugin.getDataFolder().resolve("locales");
        Files.createDirectories(localesFolder);

        for (String localeFile : BuildData.LOCALES) {
            String filePath = "locales/" + localeFile;
            Locale locale = localeFile.startsWith("locale_") ? LocaleParser.parseLocaleStrict(localeFile.replace("locale_", "").replace(".properties", "")) : Locale.ENGLISH;

            try (InputStream is = adapter.getResource(filePath)) {
                PropertyReader.readProperties(is).forEach((k, v) -> manager.addMessage(locale, MessageKey.of(k.toString()), C.c(v.toString())));
            }

            Path localePath = localesFolder.resolve(localeFile);
            if (!Files.exists(localePath)) {
                try (InputStream is = adapter.getResource(filePath)) {
                    Files.copy(is, localePath);
                }
            }
        }

        try (DirectoryStream<Path> stream = Files.newDirectoryStream(localesFolder)) {
            for (Path path : stream) {
                Path localeFile = path.getFileName();
                if (localeFile == null) {
                    continue;
                }

                String fileName = localeFile.toString();
                if (!fileName.endsWith(".properties")) {
                    continue;
                }

                Locale locale = fileName.startsWith("locale_") ?
                        LocaleParser.parseLocaleStrict(fileName.replace("locale_", "").replace(".properties", ""))
                        : Locale.ENGLISH;

                try (InputStream is = Files.newInputStream(path)) {
                    PropertyReader.readProperties(is).forEach((k, v) -> manager.addMessage(locale,
                            MessageKey.of(k.toString()), C.c(v.toString())));
                }
            }
        }
    }

    public void migrateOldFiles() {
        Path archive = plugin.getDataFolder().resolve("Archive");

        Path oldMessagesFile = plugin.getDataFolder().resolve("messages.yml");
        if (Files.exists(oldMessagesFile)) {
            try {
                Files.createDirectories(archive);
                String newName = "old-messages-" + System.currentTimeMillis() / 1000 + ".yml";
                Files.move(oldMessagesFile, archive.resolve(newName));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        Path oldAcf = plugin.getDataFolder().resolve("command-messages.properties");
        if (Files.exists(oldAcf)) {
            try {
                Files.createDirectories(archive);
                Files.move(oldAcf, archive.resolve("command-messages.properties"));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
