/**
 * Copyright 2010 Västra Götalandsregionen
 *
 *   This library is free software; you can redistribute it and/or modify
 *   it under the terms of version 2.1 of the GNU Lesser General Public
 *   License as published by the Free Software Foundation.
 *
 *   This library is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU Lesser General Public License for more details.
 *
 *   You should have received a copy of the GNU Lesser General Public
 *   License along with this library; if not, write to the
 *   Free Software Foundation, Inc., 59 Temple Place, Suite 330,
 *   Boston, MA 02111-1307  USA
 */

package se.vgregion.hook;

import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.notifications.BaseUserNotificationHandler;
import com.liferay.portal.model.UserNotificationEvent;
import com.liferay.portal.service.ServiceContext;
import com.liferay.portal.util.PortletKeys;

/*
* Created by Erik Andersson on 09/08/15.
*/

public class MBMessageNotificationHandler extends BaseUserNotificationHandler {

    public MBMessageNotificationHandler() {
        setPortletId(PortletKeys.MESSAGE_BOARDS);
    }

    @Override
    protected String getBody(UserNotificationEvent userNotificationEvent, ServiceContext serviceContext) throws Exception {

        JSONObject payload = JSONFactoryUtil.createJSONObject(userNotificationEvent.getPayload());

        return payload.getString("title");
    }

    @Override
    protected String getLink(UserNotificationEvent userNotificationEvent, ServiceContext serviceContext) throws Exception {
        JSONObject payload = JSONFactoryUtil.createJSONObject(userNotificationEvent.getPayload());

        return payload.getString("url");
    }
}
