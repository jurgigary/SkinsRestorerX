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
package net.skinsrestorer.shared.utils;

import net.skinsrestorer.shared.storage.Config;

import java.util.EnumMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class MetricsCounter {
    public String usesMySQL() {
        try {
            return String.valueOf(Config.MYSQL_ENABLED);
        } catch (Exception e) {
            return null;
        }
    }


    private final Map<Service, AtomicInteger> map = new EnumMap<>(Service.class);

    public void increment(Service service) {
        map.putIfAbsent(service, new AtomicInteger());

        map.get(service).incrementAndGet();
    }

    public int collect(Service service) {
        map.putIfAbsent(service, new AtomicInteger());

        return map.get(service).getAndSet(0);
    }

    public int collectMineskinCalls() {
        return collect(Service.MINE_SKIN);
    }

    public int collectMinetoolsCalls() {
        return collect(Service.MINE_TOOLS);
    }

    public int collectMojangCalls() {
        return collect(Service.MOJANG);
    }

    public int collectAshconCalls() {
        return collect(Service.ASHCON);
    }

    public enum Service {
        MINE_SKIN,
        MINE_TOOLS,
        MOJANG,
        ASHCON
    }
}
