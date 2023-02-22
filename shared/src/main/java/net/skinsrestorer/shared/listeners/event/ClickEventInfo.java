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
package net.skinsrestorer.shared.listeners.event;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.skinsrestorer.shared.interfaces.SRServerPlayer;

@Getter
@RequiredArgsConstructor
public class ClickEventInfo {
    private final MaterialType material;
    private final String displayName;
    private final SRServerPlayer player;
    private final Object commandIssuer;
    private final int currentPage;

    public enum MaterialType {
        HEAD,
        YELLOW_PANE,
        GREEN_PANE,
        RED_PANE,
        UNKNOWN;
    }
}
