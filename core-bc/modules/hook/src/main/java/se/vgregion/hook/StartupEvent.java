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

import com.liferay.portal.kernel.events.ActionException;
import com.liferay.portal.kernel.events.SimpleAction;
import com.liferay.portal.kernel.notifications.UserNotificationManagerUtil;

/**
 * Created by Erik Andersson on 09/08/15.
 */
public class StartupEvent extends SimpleAction {

    public static final MBMessageNotificationHandler HANDLER = new MBMessageNotificationHandler();

    @Override
    public void run(String[] ids) throws ActionException {

        UserNotificationManagerUtil.addUserNotificationHandler(HANDLER);
        System.out.println("Add custom User Notification Handler");
    }
}
