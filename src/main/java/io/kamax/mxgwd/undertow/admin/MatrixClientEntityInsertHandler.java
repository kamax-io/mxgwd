/*
 * Matrix Gateway Daemon
 * Copyright (C) 2018 Kamax Sarl
 *
 * https://www.kamax.io/
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package io.kamax.mxgwd.undertow.admin;

import io.kamax.matrix.json.GsonUtil;
import io.kamax.mxgwd.config.matrix.EntityIO;
import io.kamax.mxgwd.model.Entity;
import io.kamax.mxgwd.model.Gateway;
import io.kamax.mxgwd.model.Request;
import io.kamax.mxgwd.undertow.HttpServerExchangeHandler;
import io.undertow.server.HttpServerExchange;

import java.io.IOException;
import java.util.Objects;

public class MatrixClientEntityInsertHandler extends HttpServerExchangeHandler {

    private Gateway gw;

    public MatrixClientEntityInsertHandler(Gateway gw) {
        this.gw = gw;
    }

    @Override
    public void handleRequest(HttpServerExchange exchange) throws IOException {
        Request req = extract(exchange);
        EntityIO io = GsonUtil.get().fromJson(new String(req.getBody()), EntityIO.class);
        if (Objects.isNull(io.getId())) {
            Entity e = gw.createEntity(io);
            sendJsonResponse(exchange, GsonUtil.makeObj("id", e.getId()));
        } else {
            Entity e = gw.getEntity(io.getId());
            e.setAclType(io.getAclType());
            sendJsonResponse(exchange, "{}");
        }

    }

}
