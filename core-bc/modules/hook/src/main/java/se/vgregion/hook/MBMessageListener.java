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

import com.liferay.portal.ModelListenerException;

import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.notifications.NotificationEvent;
import com.liferay.portal.kernel.notifications.NotificationEventFactoryUtil;

import com.liferay.portal.model.BaseModelListener;
import com.liferay.portal.model.Company;
import com.liferay.portal.model.Group;
import com.liferay.portal.model.LayoutSet;

import com.liferay.portal.service.CompanyLocalServiceUtil;
import com.liferay.portal.service.GroupLocalServiceUtil;
import com.liferay.portal.service.LayoutSetLocalServiceUtil;
import com.liferay.portal.service.UserNotificationEventLocalServiceUtil;

import com.liferay.portal.util.PortalUtil;
import com.liferay.portal.util.PortletKeys;

import com.liferay.portlet.asset.model.AssetEntry;
import com.liferay.portlet.asset.model.AssetRenderer;
import com.liferay.portlet.asset.service.AssetEntryLocalServiceUtil;
import com.liferay.portlet.expando.model.ExpandoTableConstants;
import com.liferay.portlet.expando.model.ExpandoValue;
import com.liferay.portlet.expando.service.ExpandoValueLocalServiceUtil;
import com.liferay.portlet.journal.model.JournalArticle;
import com.liferay.portlet.journal.model.JournalArticleConstants;
import com.liferay.portlet.journal.service.JournalArticleLocalServiceUtil;

import com.liferay.portlet.messageboards.model.MBMessage;


/*
* Created by Erik Andersson on 09/08/15.
*/

public class MBMessageListener extends BaseModelListener<MBMessage> {

  private final String EXP_COL_NAME_CREATE_DISCUSSION_NOTIFICATION = "create-discussion-notification";
  private final String DEFAULT_LOCALE_STRING = "sv_SE";

  @Override
  public void onAfterUpdate(MBMessage message) throws ModelListenerException {

        try {

            // Only for new messages that are discussions
            if (!message.isDiscussion() || !message.isReply()) {
              return;
            }

            long companyId = message.getCompanyId();
            long groupId = message.getGroupId();
            long classPK = message.getClassPK();
            String className = message.getClassName();

            boolean createDiscussionNotification = ExpandoValueLocalServiceUtil.getData(companyId, Group.class.getName(),
              ExpandoTableConstants.DEFAULT_TABLE_NAME, EXP_COL_NAME_CREATE_DISCUSSION_NOTIFICATION,
              groupId, false);

            // Only continue if group (site) has been setup to create notifications for discussions
            if(!createDiscussionNotification) {
              return;
            }

            if(className.equals(JournalArticle.class.getName())) {
              System.out.println("Article comment");

              JournalArticle article = JournalArticleLocalServiceUtil.fetchLatestIndexableArticle(classPK);

              String articleLayoutUuid = article.getLayoutUuid();

              // Only continue if there is a display page
              if(articleLayoutUuid.equals("")) {
                return;
              }

              Group group = GroupLocalServiceUtil.getGroup(groupId);

              Company company = CompanyLocalServiceUtil.getCompany(companyId);

              JSONObject json = JSONFactoryUtil.createJSONObject();

              String notificationTitle = message.getUserName() + " postade en kommentar på artikeln " + article.getTitle(DEFAULT_LOCALE_STRING);

              String siteURL = "";

              LayoutSet layoutSet = LayoutSetLocalServiceUtil.getLayoutSet(groupId, false);
              String virtualHostName = layoutSet.getVirtualHostname();

              if(virtualHostName.equals("")) {
                siteURL = "/web" + group.getFriendlyURL();
              }

              String articleURL = siteURL + JournalArticleConstants.CANONICAL_URL_SEPARATOR + article.getUrlTitle();

              json.put("title", notificationTitle);
              json.put("userId", article.getUserId());
              json.put("url", articleURL);

              NotificationEvent event = NotificationEventFactoryUtil.createNotificationEvent(System.currentTimeMillis(), PortletKeys.MESSAGE_BOARDS, json);

              UserNotificationEventLocalServiceUtil.addUserNotificationEvent(article.getUserId(), event);
            }

        } catch (Exception e) {
            throw new ModelListenerException(e);
        }

      }

}
