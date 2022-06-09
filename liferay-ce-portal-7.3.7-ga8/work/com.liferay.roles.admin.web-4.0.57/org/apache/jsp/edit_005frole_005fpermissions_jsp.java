package org.apache.jsp;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;
import com.liferay.application.list.PanelApp;
import com.liferay.application.list.PanelAppRegistry;
import com.liferay.application.list.PanelCategory;
import com.liferay.application.list.PanelCategoryRegistry;
import com.liferay.application.list.constants.ApplicationListWebKeys;
import com.liferay.application.list.constants.PanelCategoryKeys;
import com.liferay.application.list.display.context.logic.PanelCategoryHelper;
import com.liferay.application.list.display.context.logic.PersonalMenuEntryHelper;
import com.liferay.expando.kernel.model.ExpandoBridge;
import com.liferay.expando.kernel.util.ExpandoBridgeFactoryUtil;
import com.liferay.item.selector.ItemSelector;
import com.liferay.item.selector.criteria.URLItemSelectorReturnType;
import com.liferay.item.selector.criteria.group.criterion.GroupItemSelectorCriterion;
import com.liferay.petra.string.StringBundler;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.bean.BeanParamUtil;
import com.liferay.portal.kernel.dao.orm.QueryUtil;
import com.liferay.portal.kernel.dao.search.SearchContainer;
import com.liferay.portal.kernel.exception.DuplicateRoleException;
import com.liferay.portal.kernel.exception.NoSuchRoleException;
import com.liferay.portal.kernel.exception.RequiredRoleException;
import com.liferay.portal.kernel.exception.RoleAssignmentException;
import com.liferay.portal.kernel.exception.RoleNameException;
import com.liferay.portal.kernel.exception.RolePermissionsException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.language.LanguageUtil;
import com.liferay.portal.kernel.language.UnicodeLanguageUtil;
import com.liferay.portal.kernel.model.Group;
import com.liferay.portal.kernel.model.GroupConstants;
import com.liferay.portal.kernel.model.Organization;
import com.liferay.portal.kernel.model.Permission;
import com.liferay.portal.kernel.model.PermissionDisplay;
import com.liferay.portal.kernel.model.Portlet;
import com.liferay.portal.kernel.model.PortletCategory;
import com.liferay.portal.kernel.model.PortletCategoryConstants;
import com.liferay.portal.kernel.model.Resource;
import com.liferay.portal.kernel.model.ResourceConstants;
import com.liferay.portal.kernel.model.Role;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.model.UserGroupRole;
import com.liferay.portal.kernel.model.role.RoleConstants;
import com.liferay.portal.kernel.portlet.AdministratorControlPanelEntry;
import com.liferay.portal.kernel.portlet.ControlPanelEntry;
import com.liferay.portal.kernel.portlet.LiferayWindowState;
import com.liferay.portal.kernel.portlet.OmniadminControlPanelEntry;
import com.liferay.portal.kernel.portlet.PortalPreferences;
import com.liferay.portal.kernel.portlet.PortletPreferencesFactoryUtil;
import com.liferay.portal.kernel.portlet.PortletProvider;
import com.liferay.portal.kernel.portlet.PortletProviderUtil;
import com.liferay.portal.kernel.portlet.RequestBackedPortletURLFactoryUtil;
import com.liferay.portal.kernel.security.membershippolicy.OrganizationMembershipPolicyUtil;
import com.liferay.portal.kernel.security.membershippolicy.RoleMembershipPolicyUtil;
import com.liferay.portal.kernel.security.membershippolicy.SiteMembershipPolicyUtil;
import com.liferay.portal.kernel.security.permission.ActionKeys;
import com.liferay.portal.kernel.security.permission.PermissionConverterUtil;
import com.liferay.portal.kernel.security.permission.ResourceActionsUtil;
import com.liferay.portal.kernel.security.permission.RolePermissions;
import com.liferay.portal.kernel.security.permission.comparator.ActionComparator;
import com.liferay.portal.kernel.security.permission.comparator.ModelResourceWeightComparator;
import com.liferay.portal.kernel.service.GroupLocalServiceUtil;
import com.liferay.portal.kernel.service.GroupServiceUtil;
import com.liferay.portal.kernel.service.OrganizationLocalServiceUtil;
import com.liferay.portal.kernel.service.OrganizationServiceUtil;
import com.liferay.portal.kernel.service.PortletLocalServiceUtil;
import com.liferay.portal.kernel.service.ResourcePermissionLocalServiceUtil;
import com.liferay.portal.kernel.service.RoleLocalServiceUtil;
import com.liferay.portal.kernel.service.RoleServiceUtil;
import com.liferay.portal.kernel.service.UserGroupRoleLocalServiceUtil;
import com.liferay.portal.kernel.service.permission.RolePermissionUtil;
import com.liferay.portal.kernel.template.TemplateHandler;
import com.liferay.portal.kernel.template.comparator.TemplateHandlerComparator;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.ArrayUtil;
import com.liferay.portal.kernel.util.Constants;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.HashMapBuilder;
import com.liferay.portal.kernel.util.HtmlUtil;
import com.liferay.portal.kernel.util.LinkedHashMapBuilder;
import com.liferay.portal.kernel.util.ListUtil;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.PortalUtil;
import com.liferay.portal.kernel.util.PortletKeys;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.UnicodeProperties;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.portal.kernel.util.comparator.PortletTitleComparator;
import com.liferay.portal.model.impl.ResourceImpl;
import com.liferay.portal.util.PropsValues;
import com.liferay.portal.util.WebAppPool;
import com.liferay.portlet.rolesadmin.search.ResourceActionRowChecker;
import com.liferay.portlet.usersadmin.search.GroupSearch;
import com.liferay.portlet.usersadmin.search.OrganizationSearch;
import com.liferay.product.navigation.personal.menu.BasePersonalMenuEntry;
import com.liferay.roles.admin.constants.RolesAdminPortletKeys;
import com.liferay.roles.admin.constants.RolesAdminWebKeys;
import com.liferay.roles.admin.kernel.util.RolesAdminUtil;
import com.liferay.roles.admin.role.type.contributor.RoleTypeContributor;
import com.liferay.roles.admin.web.internal.display.context.EditRoleAssignmentsManagementToolbarDisplayContext;
import com.liferay.roles.admin.web.internal.display.context.RoleDisplayContext;
import com.liferay.roles.admin.web.internal.display.context.SegmentsEntryDisplayContext;
import com.liferay.roles.admin.web.internal.display.context.SelectRoleManagementToolbarDisplayContext;
import com.liferay.roles.admin.web.internal.display.context.ViewRolesManagementToolbarDisplayContext;
import com.liferay.roles.admin.web.internal.group.type.contributor.util.GroupTypeContributorUtil;
import com.liferay.roles.admin.web.internal.role.type.contributor.util.RoleTypeContributorRetrieverUtil;
import com.liferay.roles.admin.web.internal.util.PortletDisplayTemplateUtil;
import com.liferay.segments.service.SegmentsEntryRoleLocalServiceUtil;
import com.liferay.taglib.search.ResultRow;
import com.liferay.users.admin.kernel.util.UsersAdmin;
import com.liferay.users.admin.kernel.util.UsersAdminUtil;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import javax.portlet.PortletURL;
import javax.portlet.ResourceURL;
import javax.portlet.WindowState;

public final class edit_005frole_005fpermissions_jsp extends org.apache.jasper.runtime.HttpJspBase
    implements org.apache.jasper.runtime.JspSourceDependent {


private String _getActionLabel(HttpServletRequest request, ThemeDisplay themeDisplay, String resourceName, String actionId) throws SystemException {
	String actionLabel = null;

	if (actionId.equals(ActionKeys.ACCESS_IN_CONTROL_PANEL)) {
		PanelCategoryHelper panelCategoryHelper = (PanelCategoryHelper)request.getAttribute(ApplicationListWebKeys.PANEL_CATEGORY_HELPER);
		PersonalMenuEntryHelper personalMenuEntryHelper = (PersonalMenuEntryHelper)request.getAttribute(ApplicationListWebKeys.PERSONAL_MENU_ENTRY_HELPER);

		Portlet portlet = PortletLocalServiceUtil.getPortletById(themeDisplay.getCompanyId(), resourceName);

		if (panelCategoryHelper.containsPortlet(portlet.getPortletId(), PanelCategoryKeys.SITE_ADMINISTRATION)) {
			actionLabel = LanguageUtil.get(request, "access-in-site-administration");
		}
		else if (panelCategoryHelper.containsPortlet(portlet.getPortletId(), PanelCategoryKeys.USER)) {
			actionLabel = LanguageUtil.get(request, "access-in-my-account");
		}
		else if (personalMenuEntryHelper.hasPersonalMenuEntry(portlet.getPortletId())) {
			actionLabel = LanguageUtil.get(request, "access-in-personal-menu");
		}
	}

	if (actionLabel == null) {
		actionLabel = ResourceActionsUtil.getAction(request, actionId);
	}

	return actionLabel;
}

private String _getAssigneesMessage(HttpServletRequest request, Role role, RoleDisplayContext roleDisplayContext) throws Exception {
	if (roleDisplayContext.isAutomaticallyAssigned(role)) {
		return LanguageUtil.get(request, "this-role-is-automatically-assigned");
	}

	int count = _getAssigneesTotal(role.getRoleId());

	if (count == 1) {
		return LanguageUtil.get(request, "one-assignee");
	}

	return LanguageUtil.format(request, "x-assignees", count);
}

private int _getAssigneesTotal(long roleId) throws Exception {
	return RoleLocalServiceUtil.getAssigneesTotal(roleId) + SegmentsEntryRoleLocalServiceUtil.getSegmentsEntryRolesCountByRoleId(roleId);
}

private StringBundler _getResourceHtmlId(String resource) {
	StringBundler sb = new StringBundler(2);

	sb.append("resource_");
	sb.append(StringUtil.replace(resource, '.', '_'));

	return sb;
}

private boolean _isShowScope(HttpServletRequest request, Role role, String curModelResource, String curPortletResource) throws SystemException {
	boolean showScope = true;

	if (curPortletResource.equals(PortletKeys.PORTAL)) {
		showScope = false;
	}
	else if (role.getType() != RoleConstants.TYPE_REGULAR) {
		showScope = false;
	}
	else if (Validator.isNotNull(curPortletResource)) {
		Portlet curPortlet = PortletLocalServiceUtil.getPortletById(role.getCompanyId(), curPortletResource);

		if (curPortlet != null) {
			PanelCategoryHelper panelCategoryHelper = (PanelCategoryHelper)request.getAttribute(ApplicationListWebKeys.PANEL_CATEGORY_HELPER);

			if (panelCategoryHelper.hasPanelApp(curPortlet.getPortletId()) && !panelCategoryHelper.containsPortlet(curPortlet.getPortletId(), PanelCategoryKeys.SITE_ADMINISTRATION)) {
				showScope = false;
			}
		}
	}

	if (Validator.isNotNull(curModelResource) && curModelResource.equals(Group.class.getName())) {
		showScope = true;
	}

	return showScope;
}

  private static final JspFactory _jspxFactory = JspFactory.getDefaultFactory();

  private static java.util.List<String> _jspx_dependants;

  static {
    _jspx_dependants = new java.util.ArrayList<String>(3);
    _jspx_dependants.add("/init.jsp");
    _jspx_dependants.add("/init-ext.jsp");
    _jspx_dependants.add("/edit_role_permissions_navigation.jspf");
  }

  private org.glassfish.jsp.api.ResourceInjector _jspx_resourceInjector;

  public java.util.List<String> getDependants() {
    return _jspx_dependants;
  }

  public void _jspService(HttpServletRequest request, HttpServletResponse response)
        throws java.io.IOException, ServletException {

    PageContext pageContext = null;
    HttpSession session = null;
    ServletContext application = null;
    ServletConfig config = null;
    JspWriter out = null;
    Object page = this;
    JspWriter _jspx_out = null;
    PageContext _jspx_page_context = null;

    try {
      response.setContentType("text/html");
      pageContext = _jspxFactory.getPageContext(this, request, response,
      			null, true, 8192, true);
      _jspx_page_context = pageContext;
      application = pageContext.getServletContext();
      config = pageContext.getServletConfig();
      session = pageContext.getSession();
      out = pageContext.getOut();
      _jspx_out = out;
      _jspx_resourceInjector = (org.glassfish.jsp.api.ResourceInjector) application.getAttribute("com.sun.appserv.jsp.resource.injector");

      out.write('\n');
      out.write('\n');
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      //  liferay-frontend:defineObjects
      com.liferay.frontend.taglib.servlet.taglib.DefineObjectsTag _jspx_th_liferay$1frontend_defineObjects_0 = (_jspx_resourceInjector != null) ? _jspx_resourceInjector.createTagHandlerInstance(com.liferay.frontend.taglib.servlet.taglib.DefineObjectsTag.class) : new com.liferay.frontend.taglib.servlet.taglib.DefineObjectsTag();
      _jspx_th_liferay$1frontend_defineObjects_0.setPageContext(_jspx_page_context);
      _jspx_th_liferay$1frontend_defineObjects_0.setParent(null);
      int _jspx_eval_liferay$1frontend_defineObjects_0 = _jspx_th_liferay$1frontend_defineObjects_0.doStartTag();
      if (_jspx_th_liferay$1frontend_defineObjects_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
        if (_jspx_resourceInjector != null) _jspx_resourceInjector.preDestroy(_jspx_th_liferay$1frontend_defineObjects_0);
        _jspx_th_liferay$1frontend_defineObjects_0.release();
        return;
      }
      if (_jspx_resourceInjector != null) _jspx_resourceInjector.preDestroy(_jspx_th_liferay$1frontend_defineObjects_0);
      _jspx_th_liferay$1frontend_defineObjects_0.release();
      java.lang.String currentURL = null;
      javax.portlet.PortletURL currentURLObj = null;
      java.lang.String npmResolvedPackageName = null;
      java.util.ResourceBundle resourceBundle = null;
      javax.portlet.WindowState windowState = null;
      currentURL = (java.lang.String) _jspx_page_context.findAttribute("currentURL");
      currentURLObj = (javax.portlet.PortletURL) _jspx_page_context.findAttribute("currentURLObj");
      npmResolvedPackageName = (java.lang.String) _jspx_page_context.findAttribute("npmResolvedPackageName");
      resourceBundle = (java.util.ResourceBundle) _jspx_page_context.findAttribute("resourceBundle");
      windowState = (javax.portlet.WindowState) _jspx_page_context.findAttribute("windowState");
      out.write('\n');
      out.write('\n');
      //  liferay-theme:defineObjects
      com.liferay.taglib.theme.DefineObjectsTag _jspx_th_liferay$1theme_defineObjects_0 = (_jspx_resourceInjector != null) ? _jspx_resourceInjector.createTagHandlerInstance(com.liferay.taglib.theme.DefineObjectsTag.class) : new com.liferay.taglib.theme.DefineObjectsTag();
      _jspx_th_liferay$1theme_defineObjects_0.setPageContext(_jspx_page_context);
      _jspx_th_liferay$1theme_defineObjects_0.setParent(null);
      int _jspx_eval_liferay$1theme_defineObjects_0 = _jspx_th_liferay$1theme_defineObjects_0.doStartTag();
      if (_jspx_th_liferay$1theme_defineObjects_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
        if (_jspx_resourceInjector != null) _jspx_resourceInjector.preDestroy(_jspx_th_liferay$1theme_defineObjects_0);
        _jspx_th_liferay$1theme_defineObjects_0.release();
        return;
      }
      if (_jspx_resourceInjector != null) _jspx_resourceInjector.preDestroy(_jspx_th_liferay$1theme_defineObjects_0);
      _jspx_th_liferay$1theme_defineObjects_0.release();
      com.liferay.portal.kernel.theme.ThemeDisplay themeDisplay = null;
      com.liferay.portal.kernel.model.Company company = null;
      com.liferay.portal.kernel.model.Account account = null;
      com.liferay.portal.kernel.model.User user = null;
      com.liferay.portal.kernel.model.User realUser = null;
      com.liferay.portal.kernel.model.Contact contact = null;
      com.liferay.portal.kernel.model.Layout layout = null;
      java.util.List layouts = null;
      java.lang.Long plid = null;
      com.liferay.portal.kernel.model.LayoutTypePortlet layoutTypePortlet = null;
      java.lang.Long scopeGroupId = null;
      com.liferay.portal.kernel.security.permission.PermissionChecker permissionChecker = null;
      java.util.Locale locale = null;
      java.util.TimeZone timeZone = null;
      com.liferay.portal.kernel.model.Theme theme = null;
      com.liferay.portal.kernel.model.ColorScheme colorScheme = null;
      com.liferay.portal.kernel.theme.PortletDisplay portletDisplay = null;
      java.lang.Long portletGroupId = null;
      themeDisplay = (com.liferay.portal.kernel.theme.ThemeDisplay) _jspx_page_context.findAttribute("themeDisplay");
      company = (com.liferay.portal.kernel.model.Company) _jspx_page_context.findAttribute("company");
      account = (com.liferay.portal.kernel.model.Account) _jspx_page_context.findAttribute("account");
      user = (com.liferay.portal.kernel.model.User) _jspx_page_context.findAttribute("user");
      realUser = (com.liferay.portal.kernel.model.User) _jspx_page_context.findAttribute("realUser");
      contact = (com.liferay.portal.kernel.model.Contact) _jspx_page_context.findAttribute("contact");
      layout = (com.liferay.portal.kernel.model.Layout) _jspx_page_context.findAttribute("layout");
      layouts = (java.util.List) _jspx_page_context.findAttribute("layouts");
      plid = (java.lang.Long) _jspx_page_context.findAttribute("plid");
      layoutTypePortlet = (com.liferay.portal.kernel.model.LayoutTypePortlet) _jspx_page_context.findAttribute("layoutTypePortlet");
      scopeGroupId = (java.lang.Long) _jspx_page_context.findAttribute("scopeGroupId");
      permissionChecker = (com.liferay.portal.kernel.security.permission.PermissionChecker) _jspx_page_context.findAttribute("permissionChecker");
      locale = (java.util.Locale) _jspx_page_context.findAttribute("locale");
      timeZone = (java.util.TimeZone) _jspx_page_context.findAttribute("timeZone");
      theme = (com.liferay.portal.kernel.model.Theme) _jspx_page_context.findAttribute("theme");
      colorScheme = (com.liferay.portal.kernel.model.ColorScheme) _jspx_page_context.findAttribute("colorScheme");
      portletDisplay = (com.liferay.portal.kernel.theme.PortletDisplay) _jspx_page_context.findAttribute("portletDisplay");
      portletGroupId = (java.lang.Long) _jspx_page_context.findAttribute("portletGroupId");
      out.write('\n');
      out.write('\n');
      //  portlet:defineObjects
      com.liferay.taglib.portlet.DefineObjectsTag _jspx_th_portlet_defineObjects_0 = (_jspx_resourceInjector != null) ? _jspx_resourceInjector.createTagHandlerInstance(com.liferay.taglib.portlet.DefineObjectsTag.class) : new com.liferay.taglib.portlet.DefineObjectsTag();
      _jspx_th_portlet_defineObjects_0.setPageContext(_jspx_page_context);
      _jspx_th_portlet_defineObjects_0.setParent(null);
      int _jspx_eval_portlet_defineObjects_0 = _jspx_th_portlet_defineObjects_0.doStartTag();
      if (_jspx_th_portlet_defineObjects_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
        if (_jspx_resourceInjector != null) _jspx_resourceInjector.preDestroy(_jspx_th_portlet_defineObjects_0);
        _jspx_th_portlet_defineObjects_0.release();
        return;
      }
      if (_jspx_resourceInjector != null) _jspx_resourceInjector.preDestroy(_jspx_th_portlet_defineObjects_0);
      _jspx_th_portlet_defineObjects_0.release();
      javax.portlet.ActionRequest actionRequest = null;
      javax.portlet.ActionResponse actionResponse = null;
      javax.portlet.EventRequest eventRequest = null;
      javax.portlet.EventResponse eventResponse = null;
      com.liferay.portal.kernel.portlet.LiferayPortletRequest liferayPortletRequest = null;
      com.liferay.portal.kernel.portlet.LiferayPortletResponse liferayPortletResponse = null;
      javax.portlet.PortletConfig portletConfig = null;
      java.lang.String portletName = null;
      javax.portlet.PortletPreferences portletPreferences = null;
      java.util.Map portletPreferencesValues = null;
      javax.portlet.PortletSession portletSession = null;
      java.util.Map portletSessionScope = null;
      javax.portlet.RenderRequest renderRequest = null;
      javax.portlet.RenderResponse renderResponse = null;
      javax.portlet.ResourceRequest resourceRequest = null;
      javax.portlet.ResourceResponse resourceResponse = null;
      actionRequest = (javax.portlet.ActionRequest) _jspx_page_context.findAttribute("actionRequest");
      actionResponse = (javax.portlet.ActionResponse) _jspx_page_context.findAttribute("actionResponse");
      eventRequest = (javax.portlet.EventRequest) _jspx_page_context.findAttribute("eventRequest");
      eventResponse = (javax.portlet.EventResponse) _jspx_page_context.findAttribute("eventResponse");
      liferayPortletRequest = (com.liferay.portal.kernel.portlet.LiferayPortletRequest) _jspx_page_context.findAttribute("liferayPortletRequest");
      liferayPortletResponse = (com.liferay.portal.kernel.portlet.LiferayPortletResponse) _jspx_page_context.findAttribute("liferayPortletResponse");
      portletConfig = (javax.portlet.PortletConfig) _jspx_page_context.findAttribute("portletConfig");
      portletName = (java.lang.String) _jspx_page_context.findAttribute("portletName");
      portletPreferences = (javax.portlet.PortletPreferences) _jspx_page_context.findAttribute("portletPreferences");
      portletPreferencesValues = (java.util.Map) _jspx_page_context.findAttribute("portletPreferencesValues");
      portletSession = (javax.portlet.PortletSession) _jspx_page_context.findAttribute("portletSession");
      portletSessionScope = (java.util.Map) _jspx_page_context.findAttribute("portletSessionScope");
      renderRequest = (javax.portlet.RenderRequest) _jspx_page_context.findAttribute("renderRequest");
      renderResponse = (javax.portlet.RenderResponse) _jspx_page_context.findAttribute("renderResponse");
      resourceRequest = (javax.portlet.ResourceRequest) _jspx_page_context.findAttribute("resourceRequest");
      resourceResponse = (javax.portlet.ResourceResponse) _jspx_page_context.findAttribute("resourceResponse");
      out.write('\n');
      out.write('\n');

PortalPreferences portalPreferences = PortletPreferencesFactoryUtil.getPortalPreferences(liferayPortletRequest);

boolean filterManageableGroups = true;
boolean filterManageableOrganizations = true;
boolean filterManageableRoles = true;

if (permissionChecker.isCompanyAdmin()) {
	filterManageableGroups = false;
	filterManageableOrganizations = false;
}

RoleDisplayContext roleDisplayContext = new RoleDisplayContext(request, renderResponse);

      out.write('\n');
      out.write('\n');
      out.write('\n');
      out.write('\n');
      out.write('\n');
      out.write('\n');

String cmd = ParamUtil.getString(request, Constants.CMD);

String tabs2 = "roles";
String tabs3 = ParamUtil.getString(request, "tabs3", "current");

String redirect = ParamUtil.getString(request, "redirect");

String backURL = ParamUtil.getString(request, "backURL", redirect);

long roleId = ParamUtil.getLong(request, "roleId");

Role role = RoleServiceUtil.fetchRole(roleId);

String portletResource = ParamUtil.getString(request, "portletResource");

if (Validator.isNull(redirect)) {
	PortletURL portletURL = renderResponse.createRenderURL();

	portletURL.setParameter("mvcPath", "/edit_role_permissions.jsp");
	portletURL.setParameter(Constants.CMD, Constants.VIEW);
	portletURL.setParameter("tabs1", "define-permissions");
	portletURL.setParameter("tabs2", tabs2);
	portletURL.setParameter("tabs3", tabs3);
	portletURL.setParameter("backURL", backURL);
	portletURL.setParameter("roleId", String.valueOf(role.getRoleId()));

	redirect = portletURL.toString();
}

request.setAttribute("edit_role_permissions.jsp-role", role);

request.setAttribute("edit_role_permissions.jsp-portletResource", portletResource);

if (!portletName.equals(PortletKeys.SERVER_ADMIN)) {
	portletDisplay.setShowBackIcon(true);
	portletDisplay.setURLBack(backURL);

	renderResponse.setTitle(role.getTitle(locale));
}

      out.write('\n');
      out.write('\n');
      if (_jspx_meth_liferay$1ui_success_0(_jspx_page_context))
        return;
      out.write('\n');
      if (_jspx_meth_liferay$1ui_success_1(_jspx_page_context))
        return;
      out.write('\n');
      out.write('\n');
if (
 GetterUtil.getBoolean(request.getAttribute(RolesAdminWebKeys.SHOW_NAV_TABS), true) ) {
      out.write('\n');
      out.write('	');
      //  liferay-util:include
      com.liferay.taglib.util.IncludeTag _jspx_th_liferay$1util_include_0 = (_jspx_resourceInjector != null) ? _jspx_resourceInjector.createTagHandlerInstance(com.liferay.taglib.util.IncludeTag.class) : new com.liferay.taglib.util.IncludeTag();
      _jspx_th_liferay$1util_include_0.setPageContext(_jspx_page_context);
      _jspx_th_liferay$1util_include_0.setParent(null);
      _jspx_th_liferay$1util_include_0.setPage("/edit_role_tabs.jsp");
      _jspx_th_liferay$1util_include_0.setServletContext( application );
      int _jspx_eval_liferay$1util_include_0 = _jspx_th_liferay$1util_include_0.doStartTag();
      if (_jspx_th_liferay$1util_include_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
        if (_jspx_resourceInjector != null) _jspx_resourceInjector.preDestroy(_jspx_th_liferay$1util_include_0);
        _jspx_th_liferay$1util_include_0.release();
        return;
      }
      if (_jspx_resourceInjector != null) _jspx_resourceInjector.preDestroy(_jspx_th_liferay$1util_include_0);
      _jspx_th_liferay$1util_include_0.release();
      out.write('\n');
}
      out.write('\n');
      out.write('\n');
      //  clay:container-fluid
      com.liferay.frontend.taglib.clay.servlet.taglib.ContainerFluidTag _jspx_th_clay_container$1fluid_0 = (_jspx_resourceInjector != null) ? _jspx_resourceInjector.createTagHandlerInstance(com.liferay.frontend.taglib.clay.servlet.taglib.ContainerFluidTag.class) : new com.liferay.frontend.taglib.clay.servlet.taglib.ContainerFluidTag();
      _jspx_th_clay_container$1fluid_0.setPageContext(_jspx_page_context);
      _jspx_th_clay_container$1fluid_0.setParent(null);
      _jspx_th_clay_container$1fluid_0.setCssClass("container-form-lg");
      _jspx_th_clay_container$1fluid_0.setDynamicAttribute(null, "id",  liferayPortletResponse.getNamespace() + "permissionContainer" );
      int _jspx_eval_clay_container$1fluid_0 = _jspx_th_clay_container$1fluid_0.doStartTag();
      if (_jspx_eval_clay_container$1fluid_0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
        out.write('\n');
        out.write('	');
        //  clay:row
        com.liferay.frontend.taglib.clay.servlet.taglib.RowTag _jspx_th_clay_row_0 = (_jspx_resourceInjector != null) ? _jspx_resourceInjector.createTagHandlerInstance(com.liferay.frontend.taglib.clay.servlet.taglib.RowTag.class) : new com.liferay.frontend.taglib.clay.servlet.taglib.RowTag();
        _jspx_th_clay_row_0.setPageContext(_jspx_page_context);
        _jspx_th_clay_row_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_clay_container$1fluid_0);
        int _jspx_eval_clay_row_0 = _jspx_th_clay_row_0.doStartTag();
        if (_jspx_eval_clay_row_0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
          out.write("\n");
          out.write("\t\t");
if (
 !portletName.equals(PortletKeys.SERVER_ADMIN) ) {
          out.write("\n");
          out.write("\t\t\t");
          //  clay:col
          com.liferay.frontend.taglib.clay.servlet.taglib.ColTag _jspx_th_clay_col_0 = (_jspx_resourceInjector != null) ? _jspx_resourceInjector.createTagHandlerInstance(com.liferay.frontend.taglib.clay.servlet.taglib.ColTag.class) : new com.liferay.frontend.taglib.clay.servlet.taglib.ColTag();
          _jspx_th_clay_col_0.setPageContext(_jspx_page_context);
          _jspx_th_clay_col_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_clay_row_0);
          _jspx_th_clay_col_0.setMd("3");
          int _jspx_eval_clay_col_0 = _jspx_th_clay_col_0.doStartTag();
          if (_jspx_eval_clay_col_0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
            out.write("\n");
            out.write("\t\t\t\t");
            out.write('\n');
            out.write('\n');

PanelAppRegistry panelAppRegistry = (PanelAppRegistry)request.getAttribute(ApplicationListWebKeys.PANEL_APP_REGISTRY);
PanelCategoryRegistry panelCategoryRegistry = (PanelCategoryRegistry)request.getAttribute(ApplicationListWebKeys.PANEL_CATEGORY_REGISTRY);
PersonalMenuEntryHelper personalMenuEntryHelper = (PersonalMenuEntryHelper)request.getAttribute(ApplicationListWebKeys.PERSONAL_MENU_ENTRY_HELPER);

            out.write('\n');
            out.write('\n');
            //  liferay-portlet:resourceURL
            com.liferay.taglib.portlet.ResourceURLTag _jspx_th_liferay$1portlet_resourceURL_0 = (_jspx_resourceInjector != null) ? _jspx_resourceInjector.createTagHandlerInstance(com.liferay.taglib.portlet.ResourceURLTag.class) : new com.liferay.taglib.portlet.ResourceURLTag();
            _jspx_th_liferay$1portlet_resourceURL_0.setPageContext(_jspx_page_context);
            _jspx_th_liferay$1portlet_resourceURL_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_clay_col_0);
            _jspx_th_liferay$1portlet_resourceURL_0.setCopyCurrentRenderParameters( false );
            _jspx_th_liferay$1portlet_resourceURL_0.setVarImpl("editPermissionsResourceURL");
            int _jspx_eval_liferay$1portlet_resourceURL_0 = _jspx_th_liferay$1portlet_resourceURL_0.doStartTag();
            if (_jspx_eval_liferay$1portlet_resourceURL_0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
              out.write('\n');
              out.write('	');
              if (_jspx_meth_portlet_param_0((javax.servlet.jsp.tagext.JspTag) _jspx_th_liferay$1portlet_resourceURL_0, _jspx_page_context))
                return;
              out.write('\n');
              out.write('	');
              //  portlet:param
              com.liferay.taglib.util.ParamTag _jspx_th_portlet_param_1 = (_jspx_resourceInjector != null) ? _jspx_resourceInjector.createTagHandlerInstance(com.liferay.taglib.util.ParamTag.class) : new com.liferay.taglib.util.ParamTag();
              _jspx_th_portlet_param_1.setPageContext(_jspx_page_context);
              _jspx_th_portlet_param_1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_liferay$1portlet_resourceURL_0);
              _jspx_th_portlet_param_1.setName( Constants.CMD );
              _jspx_th_portlet_param_1.setValue( Constants.EDIT );
              int _jspx_eval_portlet_param_1 = _jspx_th_portlet_param_1.doStartTag();
              if (_jspx_th_portlet_param_1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                if (_jspx_resourceInjector != null) _jspx_resourceInjector.preDestroy(_jspx_th_portlet_param_1);
                _jspx_th_portlet_param_1.release();
                return;
              }
              if (_jspx_resourceInjector != null) _jspx_resourceInjector.preDestroy(_jspx_th_portlet_param_1);
              _jspx_th_portlet_param_1.release();
              out.write('\n');
              out.write('	');
              if (_jspx_meth_portlet_param_2((javax.servlet.jsp.tagext.JspTag) _jspx_th_liferay$1portlet_resourceURL_0, _jspx_page_context))
                return;
              out.write('\n');
              out.write('	');
              //  portlet:param
              com.liferay.taglib.util.ParamTag _jspx_th_portlet_param_3 = (_jspx_resourceInjector != null) ? _jspx_resourceInjector.createTagHandlerInstance(com.liferay.taglib.util.ParamTag.class) : new com.liferay.taglib.util.ParamTag();
              _jspx_th_portlet_param_3.setPageContext(_jspx_page_context);
              _jspx_th_portlet_param_3.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_liferay$1portlet_resourceURL_0);
              _jspx_th_portlet_param_3.setName("redirect");
              _jspx_th_portlet_param_3.setValue( backURL );
              int _jspx_eval_portlet_param_3 = _jspx_th_portlet_param_3.doStartTag();
              if (_jspx_th_portlet_param_3.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                if (_jspx_resourceInjector != null) _jspx_resourceInjector.preDestroy(_jspx_th_portlet_param_3);
                _jspx_th_portlet_param_3.release();
                return;
              }
              if (_jspx_resourceInjector != null) _jspx_resourceInjector.preDestroy(_jspx_th_portlet_param_3);
              _jspx_th_portlet_param_3.release();
              out.write('\n');
              out.write('	');
              //  portlet:param
              com.liferay.taglib.util.ParamTag _jspx_th_portlet_param_4 = (_jspx_resourceInjector != null) ? _jspx_resourceInjector.createTagHandlerInstance(com.liferay.taglib.util.ParamTag.class) : new com.liferay.taglib.util.ParamTag();
              _jspx_th_portlet_param_4.setPageContext(_jspx_page_context);
              _jspx_th_portlet_param_4.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_liferay$1portlet_resourceURL_0);
              _jspx_th_portlet_param_4.setName("roleId");
              _jspx_th_portlet_param_4.setValue( String.valueOf(role.getRoleId()) );
              int _jspx_eval_portlet_param_4 = _jspx_th_portlet_param_4.doStartTag();
              if (_jspx_th_portlet_param_4.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                if (_jspx_resourceInjector != null) _jspx_resourceInjector.preDestroy(_jspx_th_portlet_param_4);
                _jspx_th_portlet_param_4.release();
                return;
              }
              if (_jspx_resourceInjector != null) _jspx_resourceInjector.preDestroy(_jspx_th_portlet_param_4);
              _jspx_th_portlet_param_4.release();
              out.write('\n');
            }
            if (_jspx_th_liferay$1portlet_resourceURL_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
              if (_jspx_resourceInjector != null) _jspx_resourceInjector.preDestroy(_jspx_th_liferay$1portlet_resourceURL_0);
              _jspx_th_liferay$1portlet_resourceURL_0.release();
              return;
            }
            if (_jspx_resourceInjector != null) _jspx_resourceInjector.preDestroy(_jspx_th_liferay$1portlet_resourceURL_0);
            _jspx_th_liferay$1portlet_resourceURL_0.release();
            com.liferay.portal.kernel.portlet.LiferayPortletURL editPermissionsResourceURL = null;
            editPermissionsResourceURL = (com.liferay.portal.kernel.portlet.LiferayPortletURL) _jspx_page_context.findAttribute("editPermissionsResourceURL");
            out.write('\n');
            out.write('\n');
            //  liferay-portlet:renderURL
            com.liferay.taglib.portlet.RenderURLTag _jspx_th_liferay$1portlet_renderURL_0 = (_jspx_resourceInjector != null) ? _jspx_resourceInjector.createTagHandlerInstance(com.liferay.taglib.portlet.RenderURLTag.class) : new com.liferay.taglib.portlet.RenderURLTag();
            _jspx_th_liferay$1portlet_renderURL_0.setPageContext(_jspx_page_context);
            _jspx_th_liferay$1portlet_renderURL_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_clay_col_0);
            _jspx_th_liferay$1portlet_renderURL_0.setCopyCurrentRenderParameters( false );
            _jspx_th_liferay$1portlet_renderURL_0.setVarImpl("editPermissionsURL");
            int _jspx_eval_liferay$1portlet_renderURL_0 = _jspx_th_liferay$1portlet_renderURL_0.doStartTag();
            if (_jspx_eval_liferay$1portlet_renderURL_0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
              out.write('\n');
              out.write('	');
              if (_jspx_meth_portlet_param_5((javax.servlet.jsp.tagext.JspTag) _jspx_th_liferay$1portlet_renderURL_0, _jspx_page_context))
                return;
              out.write('\n');
              out.write('	');
              //  portlet:param
              com.liferay.taglib.util.ParamTag _jspx_th_portlet_param_6 = (_jspx_resourceInjector != null) ? _jspx_resourceInjector.createTagHandlerInstance(com.liferay.taglib.util.ParamTag.class) : new com.liferay.taglib.util.ParamTag();
              _jspx_th_portlet_param_6.setPageContext(_jspx_page_context);
              _jspx_th_portlet_param_6.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_liferay$1portlet_renderURL_0);
              _jspx_th_portlet_param_6.setName( Constants.CMD );
              _jspx_th_portlet_param_6.setValue( Constants.EDIT );
              int _jspx_eval_portlet_param_6 = _jspx_th_portlet_param_6.doStartTag();
              if (_jspx_th_portlet_param_6.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                if (_jspx_resourceInjector != null) _jspx_resourceInjector.preDestroy(_jspx_th_portlet_param_6);
                _jspx_th_portlet_param_6.release();
                return;
              }
              if (_jspx_resourceInjector != null) _jspx_resourceInjector.preDestroy(_jspx_th_portlet_param_6);
              _jspx_th_portlet_param_6.release();
              out.write('\n');
              out.write('	');
              if (_jspx_meth_portlet_param_7((javax.servlet.jsp.tagext.JspTag) _jspx_th_liferay$1portlet_renderURL_0, _jspx_page_context))
                return;
              out.write('\n');
              out.write('	');
              //  portlet:param
              com.liferay.taglib.util.ParamTag _jspx_th_portlet_param_8 = (_jspx_resourceInjector != null) ? _jspx_resourceInjector.createTagHandlerInstance(com.liferay.taglib.util.ParamTag.class) : new com.liferay.taglib.util.ParamTag();
              _jspx_th_portlet_param_8.setPageContext(_jspx_page_context);
              _jspx_th_portlet_param_8.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_liferay$1portlet_renderURL_0);
              _jspx_th_portlet_param_8.setName("redirect");
              _jspx_th_portlet_param_8.setValue( backURL );
              int _jspx_eval_portlet_param_8 = _jspx_th_portlet_param_8.doStartTag();
              if (_jspx_th_portlet_param_8.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                if (_jspx_resourceInjector != null) _jspx_resourceInjector.preDestroy(_jspx_th_portlet_param_8);
                _jspx_th_portlet_param_8.release();
                return;
              }
              if (_jspx_resourceInjector != null) _jspx_resourceInjector.preDestroy(_jspx_th_portlet_param_8);
              _jspx_th_portlet_param_8.release();
              out.write('\n');
              out.write('	');
              //  portlet:param
              com.liferay.taglib.util.ParamTag _jspx_th_portlet_param_9 = (_jspx_resourceInjector != null) ? _jspx_resourceInjector.createTagHandlerInstance(com.liferay.taglib.util.ParamTag.class) : new com.liferay.taglib.util.ParamTag();
              _jspx_th_portlet_param_9.setPageContext(_jspx_page_context);
              _jspx_th_portlet_param_9.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_liferay$1portlet_renderURL_0);
              _jspx_th_portlet_param_9.setName("backURL");
              _jspx_th_portlet_param_9.setValue( backURL );
              int _jspx_eval_portlet_param_9 = _jspx_th_portlet_param_9.doStartTag();
              if (_jspx_th_portlet_param_9.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                if (_jspx_resourceInjector != null) _jspx_resourceInjector.preDestroy(_jspx_th_portlet_param_9);
                _jspx_th_portlet_param_9.release();
                return;
              }
              if (_jspx_resourceInjector != null) _jspx_resourceInjector.preDestroy(_jspx_th_portlet_param_9);
              _jspx_th_portlet_param_9.release();
              out.write('\n');
              out.write('	');
              //  portlet:param
              com.liferay.taglib.util.ParamTag _jspx_th_portlet_param_10 = (_jspx_resourceInjector != null) ? _jspx_resourceInjector.createTagHandlerInstance(com.liferay.taglib.util.ParamTag.class) : new com.liferay.taglib.util.ParamTag();
              _jspx_th_portlet_param_10.setPageContext(_jspx_page_context);
              _jspx_th_portlet_param_10.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_liferay$1portlet_renderURL_0);
              _jspx_th_portlet_param_10.setName("roleId");
              _jspx_th_portlet_param_10.setValue( String.valueOf(role.getRoleId()) );
              int _jspx_eval_portlet_param_10 = _jspx_th_portlet_param_10.doStartTag();
              if (_jspx_th_portlet_param_10.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                if (_jspx_resourceInjector != null) _jspx_resourceInjector.preDestroy(_jspx_th_portlet_param_10);
                _jspx_th_portlet_param_10.release();
                return;
              }
              if (_jspx_resourceInjector != null) _jspx_resourceInjector.preDestroy(_jspx_th_portlet_param_10);
              _jspx_th_portlet_param_10.release();
              out.write('\n');
            }
            if (_jspx_th_liferay$1portlet_renderURL_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
              if (_jspx_resourceInjector != null) _jspx_resourceInjector.preDestroy(_jspx_th_liferay$1portlet_renderURL_0);
              _jspx_th_liferay$1portlet_renderURL_0.release();
              return;
            }
            if (_jspx_resourceInjector != null) _jspx_resourceInjector.preDestroy(_jspx_th_liferay$1portlet_renderURL_0);
            _jspx_th_liferay$1portlet_renderURL_0.release();
            com.liferay.portal.kernel.portlet.LiferayPortletURL editPermissionsURL = null;
            editPermissionsURL = (com.liferay.portal.kernel.portlet.LiferayPortletURL) _jspx_page_context.findAttribute("editPermissionsURL");
            out.write("\n");
            out.write("\n");
            out.write("<div class=\"lfr-permission-navigation-container menubar menubar-transparent menubar-vertical-expand-lg\" id=\"");
            if (_jspx_meth_portlet_namespace_0((javax.servlet.jsp.tagext.JspTag) _jspx_th_clay_col_0, _jspx_page_context))
              return;
            out.write("permissionNavigationContainer\">\n");
            out.write("\t<div class=\"search\" id=\"");
            if (_jspx_meth_portlet_namespace_1((javax.servlet.jsp.tagext.JspTag) _jspx_th_clay_col_0, _jspx_page_context))
              return;
            out.write("permissionNavigationSearchContainer\">\n");
            out.write("\t\t<input class=\"field form-control search-query\" id=\"");
            if (_jspx_meth_portlet_namespace_2((javax.servlet.jsp.tagext.JspTag) _jspx_th_clay_col_0, _jspx_page_context))
              return;
            out.write("permissionNavigationSearch\" placeholder=\"");
            out.print( LanguageUtil.get(request, "search") );
            out.write("\" type=\"text\" />\n");
            out.write("\t</div>\n");
            out.write("\n");
            out.write("\t<div class=\"lfr-permission-navigation\" id=\"");
            if (_jspx_meth_portlet_namespace_3((javax.servlet.jsp.tagext.JspTag) _jspx_th_clay_col_0, _jspx_page_context))
              return;
            out.write("permissionNavigationDataContainer\">\n");
            out.write("\t\t<ul class=\"nav nav-nested\" id=\"");
            if (_jspx_meth_portlet_namespace_4((javax.servlet.jsp.tagext.JspTag) _jspx_th_clay_col_0, _jspx_page_context))
              return;
            out.write("permissionNavigationData\">\n");
            out.write("\t\t\t");
            //  liferay-portlet:resourceURL
            com.liferay.taglib.portlet.ResourceURLTag _jspx_th_liferay$1portlet_resourceURL_1 = (_jspx_resourceInjector != null) ? _jspx_resourceInjector.createTagHandlerInstance(com.liferay.taglib.portlet.ResourceURLTag.class) : new com.liferay.taglib.portlet.ResourceURLTag();
            _jspx_th_liferay$1portlet_resourceURL_1.setPageContext(_jspx_page_context);
            _jspx_th_liferay$1portlet_resourceURL_1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_clay_col_0);
            _jspx_th_liferay$1portlet_resourceURL_1.setVarImpl("viewPermissionsResourceURL");
            int _jspx_eval_liferay$1portlet_resourceURL_1 = _jspx_th_liferay$1portlet_resourceURL_1.doStartTag();
            if (_jspx_eval_liferay$1portlet_resourceURL_1 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
              out.write("\n");
              out.write("\t\t\t\t");
              if (_jspx_meth_portlet_param_11((javax.servlet.jsp.tagext.JspTag) _jspx_th_liferay$1portlet_resourceURL_1, _jspx_page_context))
                return;
              out.write("\n");
              out.write("\t\t\t\t");
              //  portlet:param
              com.liferay.taglib.util.ParamTag _jspx_th_portlet_param_12 = (_jspx_resourceInjector != null) ? _jspx_resourceInjector.createTagHandlerInstance(com.liferay.taglib.util.ParamTag.class) : new com.liferay.taglib.util.ParamTag();
              _jspx_th_portlet_param_12.setPageContext(_jspx_page_context);
              _jspx_th_portlet_param_12.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_liferay$1portlet_resourceURL_1);
              _jspx_th_portlet_param_12.setName( Constants.CMD );
              _jspx_th_portlet_param_12.setValue( Constants.VIEW );
              int _jspx_eval_portlet_param_12 = _jspx_th_portlet_param_12.doStartTag();
              if (_jspx_th_portlet_param_12.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                if (_jspx_resourceInjector != null) _jspx_resourceInjector.preDestroy(_jspx_th_portlet_param_12);
                _jspx_th_portlet_param_12.release();
                return;
              }
              if (_jspx_resourceInjector != null) _jspx_resourceInjector.preDestroy(_jspx_th_portlet_param_12);
              _jspx_th_portlet_param_12.release();
              out.write("\n");
              out.write("\t\t\t\t");
              if (_jspx_meth_portlet_param_13((javax.servlet.jsp.tagext.JspTag) _jspx_th_liferay$1portlet_resourceURL_1, _jspx_page_context))
                return;
              out.write("\n");
              out.write("\t\t\t\t");
              //  portlet:param
              com.liferay.taglib.util.ParamTag _jspx_th_portlet_param_14 = (_jspx_resourceInjector != null) ? _jspx_resourceInjector.createTagHandlerInstance(com.liferay.taglib.util.ParamTag.class) : new com.liferay.taglib.util.ParamTag();
              _jspx_th_portlet_param_14.setPageContext(_jspx_page_context);
              _jspx_th_portlet_param_14.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_liferay$1portlet_resourceURL_1);
              _jspx_th_portlet_param_14.setName("backURL");
              _jspx_th_portlet_param_14.setValue( backURL );
              int _jspx_eval_portlet_param_14 = _jspx_th_portlet_param_14.doStartTag();
              if (_jspx_th_portlet_param_14.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                if (_jspx_resourceInjector != null) _jspx_resourceInjector.preDestroy(_jspx_th_portlet_param_14);
                _jspx_th_portlet_param_14.release();
                return;
              }
              if (_jspx_resourceInjector != null) _jspx_resourceInjector.preDestroy(_jspx_th_portlet_param_14);
              _jspx_th_portlet_param_14.release();
              out.write("\n");
              out.write("\t\t\t\t");
              //  portlet:param
              com.liferay.taglib.util.ParamTag _jspx_th_portlet_param_15 = (_jspx_resourceInjector != null) ? _jspx_resourceInjector.createTagHandlerInstance(com.liferay.taglib.util.ParamTag.class) : new com.liferay.taglib.util.ParamTag();
              _jspx_th_portlet_param_15.setPageContext(_jspx_page_context);
              _jspx_th_portlet_param_15.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_liferay$1portlet_resourceURL_1);
              _jspx_th_portlet_param_15.setName("roleId");
              _jspx_th_portlet_param_15.setValue( String.valueOf(role.getRoleId()) );
              int _jspx_eval_portlet_param_15 = _jspx_th_portlet_param_15.doStartTag();
              if (_jspx_th_portlet_param_15.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                if (_jspx_resourceInjector != null) _jspx_resourceInjector.preDestroy(_jspx_th_portlet_param_15);
                _jspx_th_portlet_param_15.release();
                return;
              }
              if (_jspx_resourceInjector != null) _jspx_resourceInjector.preDestroy(_jspx_th_portlet_param_15);
              _jspx_th_portlet_param_15.release();
              out.write("\n");
              out.write("\t\t\t");
            }
            if (_jspx_th_liferay$1portlet_resourceURL_1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
              if (_jspx_resourceInjector != null) _jspx_resourceInjector.preDestroy(_jspx_th_liferay$1portlet_resourceURL_1);
              _jspx_th_liferay$1portlet_resourceURL_1.release();
              return;
            }
            if (_jspx_resourceInjector != null) _jspx_resourceInjector.preDestroy(_jspx_th_liferay$1portlet_resourceURL_1);
            _jspx_th_liferay$1portlet_resourceURL_1.release();
            com.liferay.portal.kernel.portlet.LiferayPortletURL viewPermissionsResourceURL = null;
            viewPermissionsResourceURL = (com.liferay.portal.kernel.portlet.LiferayPortletURL) _jspx_page_context.findAttribute("viewPermissionsResourceURL");
            out.write("\n");
            out.write("\n");
            out.write("\t\t\t");

			Map<String, Object> data = HashMapBuilder.<String, Object>put(
				"resource-href", viewPermissionsResourceURL.toString()
			).build();
			
            out.write("\n");
            out.write("\n");
            out.write("\t\t\t");
            //  liferay-portlet:renderURL
            com.liferay.taglib.portlet.RenderURLTag _jspx_th_liferay$1portlet_renderURL_1 = (_jspx_resourceInjector != null) ? _jspx_resourceInjector.createTagHandlerInstance(com.liferay.taglib.portlet.RenderURLTag.class) : new com.liferay.taglib.portlet.RenderURLTag();
            _jspx_th_liferay$1portlet_renderURL_1.setPageContext(_jspx_page_context);
            _jspx_th_liferay$1portlet_renderURL_1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_clay_col_0);
            _jspx_th_liferay$1portlet_renderURL_1.setVarImpl("viewPermissionsURL");
            int _jspx_eval_liferay$1portlet_renderURL_1 = _jspx_th_liferay$1portlet_renderURL_1.doStartTag();
            if (_jspx_eval_liferay$1portlet_renderURL_1 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
              out.write("\n");
              out.write("\t\t\t\t");
              if (_jspx_meth_portlet_param_16((javax.servlet.jsp.tagext.JspTag) _jspx_th_liferay$1portlet_renderURL_1, _jspx_page_context))
                return;
              out.write("\n");
              out.write("\t\t\t\t");
              //  portlet:param
              com.liferay.taglib.util.ParamTag _jspx_th_portlet_param_17 = (_jspx_resourceInjector != null) ? _jspx_resourceInjector.createTagHandlerInstance(com.liferay.taglib.util.ParamTag.class) : new com.liferay.taglib.util.ParamTag();
              _jspx_th_portlet_param_17.setPageContext(_jspx_page_context);
              _jspx_th_portlet_param_17.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_liferay$1portlet_renderURL_1);
              _jspx_th_portlet_param_17.setName( Constants.CMD );
              _jspx_th_portlet_param_17.setValue( Constants.VIEW );
              int _jspx_eval_portlet_param_17 = _jspx_th_portlet_param_17.doStartTag();
              if (_jspx_th_portlet_param_17.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                if (_jspx_resourceInjector != null) _jspx_resourceInjector.preDestroy(_jspx_th_portlet_param_17);
                _jspx_th_portlet_param_17.release();
                return;
              }
              if (_jspx_resourceInjector != null) _jspx_resourceInjector.preDestroy(_jspx_th_portlet_param_17);
              _jspx_th_portlet_param_17.release();
              out.write("\n");
              out.write("\t\t\t\t");
              if (_jspx_meth_portlet_param_18((javax.servlet.jsp.tagext.JspTag) _jspx_th_liferay$1portlet_renderURL_1, _jspx_page_context))
                return;
              out.write("\n");
              out.write("\t\t\t\t");
              //  portlet:param
              com.liferay.taglib.util.ParamTag _jspx_th_portlet_param_19 = (_jspx_resourceInjector != null) ? _jspx_resourceInjector.createTagHandlerInstance(com.liferay.taglib.util.ParamTag.class) : new com.liferay.taglib.util.ParamTag();
              _jspx_th_portlet_param_19.setPageContext(_jspx_page_context);
              _jspx_th_portlet_param_19.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_liferay$1portlet_renderURL_1);
              _jspx_th_portlet_param_19.setName("backURL");
              _jspx_th_portlet_param_19.setValue( backURL );
              int _jspx_eval_portlet_param_19 = _jspx_th_portlet_param_19.doStartTag();
              if (_jspx_th_portlet_param_19.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                if (_jspx_resourceInjector != null) _jspx_resourceInjector.preDestroy(_jspx_th_portlet_param_19);
                _jspx_th_portlet_param_19.release();
                return;
              }
              if (_jspx_resourceInjector != null) _jspx_resourceInjector.preDestroy(_jspx_th_portlet_param_19);
              _jspx_th_portlet_param_19.release();
              out.write("\n");
              out.write("\t\t\t\t");
              //  portlet:param
              com.liferay.taglib.util.ParamTag _jspx_th_portlet_param_20 = (_jspx_resourceInjector != null) ? _jspx_resourceInjector.createTagHandlerInstance(com.liferay.taglib.util.ParamTag.class) : new com.liferay.taglib.util.ParamTag();
              _jspx_th_portlet_param_20.setPageContext(_jspx_page_context);
              _jspx_th_portlet_param_20.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_liferay$1portlet_renderURL_1);
              _jspx_th_portlet_param_20.setName("roleId");
              _jspx_th_portlet_param_20.setValue( String.valueOf(role.getRoleId()) );
              int _jspx_eval_portlet_param_20 = _jspx_th_portlet_param_20.doStartTag();
              if (_jspx_th_portlet_param_20.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                if (_jspx_resourceInjector != null) _jspx_resourceInjector.preDestroy(_jspx_th_portlet_param_20);
                _jspx_th_portlet_param_20.release();
                return;
              }
              if (_jspx_resourceInjector != null) _jspx_resourceInjector.preDestroy(_jspx_th_portlet_param_20);
              _jspx_th_portlet_param_20.release();
              out.write("\n");
              out.write("\t\t\t");
            }
            if (_jspx_th_liferay$1portlet_renderURL_1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
              if (_jspx_resourceInjector != null) _jspx_resourceInjector.preDestroy(_jspx_th_liferay$1portlet_renderURL_1);
              _jspx_th_liferay$1portlet_renderURL_1.release();
              return;
            }
            if (_jspx_resourceInjector != null) _jspx_resourceInjector.preDestroy(_jspx_th_liferay$1portlet_renderURL_1);
            _jspx_th_liferay$1portlet_renderURL_1.release();
            com.liferay.portal.kernel.portlet.LiferayPortletURL viewPermissionsURL = null;
            viewPermissionsURL = (com.liferay.portal.kernel.portlet.LiferayPortletURL) _jspx_page_context.findAttribute("viewPermissionsURL");
            out.write("\n");
            out.write("\n");
            out.write("\t\t\t<li class=\"nav-item permission-navigation-item-summary ");
            out.print( Validator.isNull(portletResource)? "selected" : "" );
            out.write("\">\n");
            out.write("\t\t\t\t");
            //  aui:a
            com.liferay.taglib.aui.ATag _jspx_th_aui_a_0 = (_jspx_resourceInjector != null) ? _jspx_resourceInjector.createTagHandlerInstance(com.liferay.taglib.aui.ATag.class) : new com.liferay.taglib.aui.ATag();
            _jspx_th_aui_a_0.setPageContext(_jspx_page_context);
            _jspx_th_aui_a_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_clay_col_0);
            _jspx_th_aui_a_0.setCssClass("nav-link permission-navigation-link");
            _jspx_th_aui_a_0.setData( data );
            _jspx_th_aui_a_0.setHref( viewPermissionsURL.toString() );
            int _jspx_eval_aui_a_0 = _jspx_th_aui_a_0.doStartTag();
            if (_jspx_eval_aui_a_0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
              out.write("\n");
              out.write("\t\t\t\t\t");
              if (_jspx_meth_liferay$1ui_message_0((javax.servlet.jsp.tagext.JspTag) _jspx_th_aui_a_0, _jspx_page_context))
                return;
              out.write("\n");
              out.write("\t\t\t\t");
            }
            if (_jspx_th_aui_a_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
              if (_jspx_resourceInjector != null) _jspx_resourceInjector.preDestroy(_jspx_th_aui_a_0);
              _jspx_th_aui_a_0.release();
              return;
            }
            if (_jspx_resourceInjector != null) _jspx_resourceInjector.preDestroy(_jspx_th_aui_a_0);
            _jspx_th_aui_a_0.release();
            out.write("\n");
            out.write("\t\t\t</li>\n");
            out.write("\n");
            out.write("\t\t\t");
            out.write("\n");
            out.write("\t\t\t\t");
if (
 role.getType() == RoleConstants.TYPE_ORGANIZATION ) {
            out.write("\n");
            out.write("\n");
            out.write("\t\t\t\t\t");

					Portlet usersAdminPortlet = PortletLocalServiceUtil.getPortletById(company.getCompanyId(), PortletProviderUtil.getPortletId(User.class.getName(), PortletProvider.Action.VIEW));

					editPermissionsResourceURL.setParameter("portletResource", usersAdminPortlet.getPortletId());

					editPermissionsURL.setParameter("portletResource", usersAdminPortlet.getPortletId());

					data.put("resource-href", editPermissionsResourceURL.toString());
					
            out.write("\n");
            out.write("\n");
            out.write("\t\t\t\t\t<div class=\"");
            out.print( portletResource.equals(usersAdminPortlet.getPortletId())? "selected" : "" );
            out.write("\">\n");
            out.write("\t\t\t\t\t\t");
            //  aui:a
            com.liferay.taglib.aui.ATag _jspx_th_aui_a_1 = (_jspx_resourceInjector != null) ? _jspx_resourceInjector.createTagHandlerInstance(com.liferay.taglib.aui.ATag.class) : new com.liferay.taglib.aui.ATag();
            _jspx_th_aui_a_1.setPageContext(_jspx_page_context);
            _jspx_th_aui_a_1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_clay_col_0);
            _jspx_th_aui_a_1.setCssClass("nav-link permission-navigation-link");
            _jspx_th_aui_a_1.setData( data );
            _jspx_th_aui_a_1.setHref( editPermissionsURL.toString() );
            int _jspx_eval_aui_a_1 = _jspx_th_aui_a_1.doStartTag();
            if (_jspx_eval_aui_a_1 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
              out.write("\n");
              out.write("\t\t\t\t\t\t\t");
              out.print( PortalUtil.getPortletLongTitle(usersAdminPortlet, application, locale) );
              out.write("\n");
              out.write("\t\t\t\t\t\t");
            }
            if (_jspx_th_aui_a_1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
              if (_jspx_resourceInjector != null) _jspx_resourceInjector.preDestroy(_jspx_th_aui_a_1);
              _jspx_th_aui_a_1.release();
              return;
            }
            if (_jspx_resourceInjector != null) _jspx_resourceInjector.preDestroy(_jspx_th_aui_a_1);
            _jspx_th_aui_a_1.release();
            out.write("\n");
            out.write("\t\t\t\t\t</div>\n");
            out.write("\t\t\t\t");
            out.write("\n");
            out.write("\t\t\t\t");
}
else if (
 role.getType() == RoleConstants.TYPE_REGULAR ) {
            out.write("\n");
            out.write("\n");
            out.write("\t\t\t\t\t");

					editPermissionsResourceURL.setParameter("portletResource", PortletKeys.PORTAL);

					editPermissionsURL.setParameter("portletResource", PortletKeys.PORTAL);

					data.put("resource-href", editPermissionsResourceURL.toString());
					
            out.write("\n");
            out.write("\n");
            out.write("\t\t\t\t\t<li class=\"nav-item permission-navigation-section\">\n");
            out.write("\t\t\t\t\t\t<a class=\"collapse-icon nav-link permission-navigation-item-header toggler-header toggler-header-expanded\" href=\"javascript:;\">\n");
            out.write("\t\t\t\t\t\t\t");
            if (_jspx_meth_liferay$1ui_message_1((javax.servlet.jsp.tagext.JspTag) _jspx_th_clay_col_0, _jspx_page_context))
              return;
            out.write("\n");
            out.write("\n");
            out.write("\t\t\t\t\t\t\t<span class=\"collapse-icon-closed\">\n");
            out.write("\t\t\t\t\t\t\t\t");
            if (_jspx_meth_aui_icon_0((javax.servlet.jsp.tagext.JspTag) _jspx_th_clay_col_0, _jspx_page_context))
              return;
            out.write("\n");
            out.write("\t\t\t\t\t\t\t</span>\n");
            out.write("\t\t\t\t\t\t\t<span class=\"collapse-icon-open\">\n");
            out.write("\t\t\t\t\t\t\t\t");
            if (_jspx_meth_aui_icon_1((javax.servlet.jsp.tagext.JspTag) _jspx_th_clay_col_0, _jspx_page_context))
              return;
            out.write("\n");
            out.write("\t\t\t\t\t\t\t</span>\n");
            out.write("\t\t\t\t\t\t</a>\n");
            out.write("\n");
            out.write("\t\t\t\t\t\t<div class=\"permission-navigation-item-content toggler-content toggler-content-expanded\">\n");
            out.write("\t\t\t\t\t\t\t<ul class=\"nav nav-stacked permission-navigation-section\">\n");
            out.write("\t\t\t\t\t\t\t\t<div class=\"permission-navigation-item-content\">\n");
            out.write("\t\t\t\t\t\t\t\t\t<li class=\"nav-item permission-navigation-item-container\">\n");
            out.write("\t\t\t\t\t\t\t\t\t\t");
            //  aui:a
            com.liferay.taglib.aui.ATag _jspx_th_aui_a_2 = (_jspx_resourceInjector != null) ? _jspx_resourceInjector.createTagHandlerInstance(com.liferay.taglib.aui.ATag.class) : new com.liferay.taglib.aui.ATag();
            _jspx_th_aui_a_2.setPageContext(_jspx_page_context);
            _jspx_th_aui_a_2.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_clay_col_0);
            _jspx_th_aui_a_2.setCssClass("nav-link permission-navigation-link");
            _jspx_th_aui_a_2.setData( data );
            _jspx_th_aui_a_2.setHref( editPermissionsURL.toString() );
            int _jspx_eval_aui_a_2 = _jspx_th_aui_a_2.doStartTag();
            if (_jspx_eval_aui_a_2 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
              out.write("\n");
              out.write("\t\t\t\t\t\t\t\t\t\t\t");
              if (_jspx_meth_liferay$1ui_message_2((javax.servlet.jsp.tagext.JspTag) _jspx_th_aui_a_2, _jspx_page_context))
                return;
              out.write("\n");
              out.write("\t\t\t\t\t\t\t\t\t\t");
            }
            if (_jspx_th_aui_a_2.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
              if (_jspx_resourceInjector != null) _jspx_resourceInjector.preDestroy(_jspx_th_aui_a_2);
              _jspx_th_aui_a_2.release();
              return;
            }
            if (_jspx_resourceInjector != null) _jspx_resourceInjector.preDestroy(_jspx_th_aui_a_2);
            _jspx_th_aui_a_2.release();
            out.write("\n");
            out.write("\t\t\t\t\t\t\t\t\t</li>\n");
            out.write("\t\t\t\t\t\t\t\t</div>\n");
            out.write("\t\t\t\t\t\t\t</ul>\n");
            out.write("\n");
            out.write("\t\t\t\t\t\t\t");

							for (PanelCategory panelCategory : panelCategoryRegistry.getChildPanelCategories(PanelCategoryKeys.CONTROL_PANEL)) {
								List<PanelApp> panelApps = panelAppRegistry.getPanelApps(panelCategory);
							
            out.write("\n");
            out.write("\n");
            out.write("\t\t\t\t\t\t\t\t");
if (
 !panelApps.isEmpty() ) {
            out.write("\n");
            out.write("\t\t\t\t\t\t\t\t\t<div class=\"permission-navigation-section\">\n");
            out.write("\t\t\t\t\t\t\t\t\t\t<a class=\"collapse-icon nav-link permission-navigation-item-header toggler-header toggler-header-collapsed\" href=\"javascript:;\">\n");
            out.write("\t\t\t\t\t\t\t\t\t\t\t");
            out.print( panelCategory.getLabel(locale) );
            out.write("\n");
            out.write("\t\t\t\t\t\t\t\t\t\t\t<span class=\"collapse-icon-closed\">\n");
            out.write("\t\t\t\t\t\t\t\t\t\t\t\t");
            if (_jspx_meth_aui_icon_2((javax.servlet.jsp.tagext.JspTag) _jspx_th_clay_col_0, _jspx_page_context))
              return;
            out.write("\n");
            out.write("\t\t\t\t\t\t\t\t\t\t\t</span>\n");
            out.write("\t\t\t\t\t\t\t\t\t\t\t<span class=\"collapse-icon-open\">\n");
            out.write("\t\t\t\t\t\t\t\t\t\t\t\t");
            if (_jspx_meth_aui_icon_3((javax.servlet.jsp.tagext.JspTag) _jspx_th_clay_col_0, _jspx_page_context))
              return;
            out.write("\n");
            out.write("\t\t\t\t\t\t\t\t\t\t\t</span>\n");
            out.write("\t\t\t\t\t\t\t\t\t\t</a>\n");
            out.write("\n");
            out.write("\t\t\t\t\t\t\t\t\t\t<ul class=\"nav nav-stacked permission-navigation-item-content toggler-content toggler-content-collapsed\">\n");
            out.write("\n");
            out.write("\t\t\t\t\t\t\t\t\t\t\t");

											for (PanelApp panelApp : panelApps) {
												Portlet panelAppPortlet = PortletLocalServiceUtil.getPortletById(company.getCompanyId(), panelApp.getPortletId());

												String controlPanelEntryClassName = panelAppPortlet.getControlPanelEntryClass();
												ControlPanelEntry controlPanelEntry = panelAppPortlet.getControlPanelEntryInstance();

												if (Objects.equals(controlPanelEntryClassName, AdministratorControlPanelEntry.class.getName()) || Objects.equals(controlPanelEntryClassName, OmniadminControlPanelEntry.class.getName()) || AdministratorControlPanelEntry.class.isAssignableFrom(controlPanelEntry.getClass()) || OmniadminControlPanelEntry.class.isAssignableFrom(controlPanelEntry.getClass())) {
													continue;
												}

												editPermissionsResourceURL.setParameter("portletResource", panelAppPortlet.getPortletId());

												editPermissionsURL.setParameter("portletResource", panelAppPortlet.getPortletId());

												data.put("resource-href", editPermissionsResourceURL.toString());
											
            out.write("\n");
            out.write("\n");
            out.write("\t\t\t\t\t\t\t\t\t\t\t\t<li class=\"nav-item permission-navigation-item-container ");
            out.print( portletResource.equals(panelAppPortlet.getPortletId())? "selected" : "" );
            out.write("\">\n");
            out.write("\t\t\t\t\t\t\t\t\t\t\t\t\t");
            //  aui:a
            com.liferay.taglib.aui.ATag _jspx_th_aui_a_3 = (_jspx_resourceInjector != null) ? _jspx_resourceInjector.createTagHandlerInstance(com.liferay.taglib.aui.ATag.class) : new com.liferay.taglib.aui.ATag();
            _jspx_th_aui_a_3.setPageContext(_jspx_page_context);
            _jspx_th_aui_a_3.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_clay_col_0);
            _jspx_th_aui_a_3.setCssClass("nav-link permission-navigation-link");
            _jspx_th_aui_a_3.setData( data );
            _jspx_th_aui_a_3.setHref( editPermissionsURL.toString() );
            int _jspx_eval_aui_a_3 = _jspx_th_aui_a_3.doStartTag();
            if (_jspx_eval_aui_a_3 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
              out.write("\n");
              out.write("\t\t\t\t\t\t\t\t\t\t\t\t\t\t");
              out.print( PortalUtil.getPortletLongTitle(panelAppPortlet, application, locale) );
              out.write("\n");
              out.write("\t\t\t\t\t\t\t\t\t\t\t\t\t");
            }
            if (_jspx_th_aui_a_3.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
              if (_jspx_resourceInjector != null) _jspx_resourceInjector.preDestroy(_jspx_th_aui_a_3);
              _jspx_th_aui_a_3.release();
              return;
            }
            if (_jspx_resourceInjector != null) _jspx_resourceInjector.preDestroy(_jspx_th_aui_a_3);
            _jspx_th_aui_a_3.release();
            out.write("\n");
            out.write("\t\t\t\t\t\t\t\t\t\t\t\t</li>\n");
            out.write("\n");
            out.write("\t\t\t\t\t\t\t\t\t\t\t");

											}
											
            out.write("\n");
            out.write("\n");
            out.write("\t\t\t\t\t\t\t\t\t\t</ul>\n");
            out.write("\t\t\t\t\t\t\t\t\t</div>\n");
            out.write("\t\t\t\t\t\t\t\t");
}
            out.write("\n");
            out.write("\n");
            out.write("\t\t\t\t\t\t\t");

							}
							
            out.write("\n");
            out.write("\n");
            out.write("\t\t\t\t\t\t</div>\n");
            out.write("\t\t\t\t\t</li>\n");
            out.write("\t\t\t\t\t<li class=\"nav-item permission-navigation-section\">\n");
            out.write("\t\t\t\t\t\t<a class=\"collapse-icon nav-link permission-navigation-item-header toggler-header toggler-header-expanded\" href=\"javascript:;\">\n");
            out.write("\t\t\t\t\t\t\t");
            if (_jspx_meth_liferay$1ui_message_3((javax.servlet.jsp.tagext.JspTag) _jspx_th_clay_col_0, _jspx_page_context))
              return;
            out.write("\n");
            out.write("\n");
            out.write("\t\t\t\t\t\t\t<span class=\"collapse-icon-closed\">\n");
            out.write("\t\t\t\t\t\t\t\t");
            if (_jspx_meth_aui_icon_4((javax.servlet.jsp.tagext.JspTag) _jspx_th_clay_col_0, _jspx_page_context))
              return;
            out.write("\n");
            out.write("\t\t\t\t\t\t\t</span>\n");
            out.write("\t\t\t\t\t\t\t<span class=\"collapse-icon-open\">\n");
            out.write("\t\t\t\t\t\t\t\t");
            if (_jspx_meth_aui_icon_5((javax.servlet.jsp.tagext.JspTag) _jspx_th_clay_col_0, _jspx_page_context))
              return;
            out.write("\n");
            out.write("\t\t\t\t\t\t\t</span>\n");
            out.write("\t\t\t\t\t\t</a>\n");
            out.write("\n");
            out.write("\t\t\t\t\t\t<div class=\"permission-navigation-item-content toggler-content toggler-content-expanded\">\n");
            out.write("\n");
            out.write("\t\t\t\t\t\t\t");

							for (PanelCategory panelCategory : panelCategoryRegistry.getChildPanelCategories(PanelCategoryKeys.COMMERCE)) {
								List<PanelApp> panelApps = panelAppRegistry.getPanelApps(panelCategory);
							
            out.write("\n");
            out.write("\n");
            out.write("\t\t\t\t\t\t\t\t");
if (
 !panelApps.isEmpty() ) {
            out.write("\n");
            out.write("\t\t\t\t\t\t\t\t\t<div class=\"permission-navigation-section\">\n");
            out.write("\t\t\t\t\t\t\t\t\t\t<a class=\"collapse-icon nav-link permission-navigation-item-header toggler-header toggler-header-collapsed\" href=\"javascript:;\">\n");
            out.write("\t\t\t\t\t\t\t\t\t\t\t");
            out.print( panelCategory.getLabel(locale) );
            out.write("\n");
            out.write("\t\t\t\t\t\t\t\t\t\t\t<span class=\"collapse-icon-closed\">\n");
            out.write("\t\t\t\t\t\t\t\t\t\t\t\t");
            if (_jspx_meth_aui_icon_6((javax.servlet.jsp.tagext.JspTag) _jspx_th_clay_col_0, _jspx_page_context))
              return;
            out.write("\n");
            out.write("\t\t\t\t\t\t\t\t\t\t\t</span>\n");
            out.write("\t\t\t\t\t\t\t\t\t\t\t<span class=\"collapse-icon-open\">\n");
            out.write("\t\t\t\t\t\t\t\t\t\t\t\t");
            if (_jspx_meth_aui_icon_7((javax.servlet.jsp.tagext.JspTag) _jspx_th_clay_col_0, _jspx_page_context))
              return;
            out.write("\n");
            out.write("\t\t\t\t\t\t\t\t\t\t\t</span>\n");
            out.write("\t\t\t\t\t\t\t\t\t\t</a>\n");
            out.write("\n");
            out.write("\t\t\t\t\t\t\t\t\t\t<ul class=\"nav nav-stacked permission-navigation-item-content toggler-content toggler-content-collapsed\">\n");
            out.write("\n");
            out.write("\t\t\t\t\t\t\t\t\t\t\t");

											for (PanelApp panelApp : panelApps) {
												Portlet panelAppPortlet = PortletLocalServiceUtil.getPortletById(company.getCompanyId(), panelApp.getPortletId());

												String controlPanelEntryClassName = panelAppPortlet.getControlPanelEntryClass();
												ControlPanelEntry controlPanelEntry = panelAppPortlet.getControlPanelEntryInstance();

												if (Objects.equals(controlPanelEntryClassName, AdministratorControlPanelEntry.class.getName()) || Objects.equals(controlPanelEntryClassName, OmniadminControlPanelEntry.class.getName()) || AdministratorControlPanelEntry.class.isAssignableFrom(controlPanelEntry.getClass()) || OmniadminControlPanelEntry.class.isAssignableFrom(controlPanelEntry.getClass())) {
													continue;
												}

												editPermissionsResourceURL.setParameter("portletResource", panelAppPortlet.getPortletId());

												editPermissionsURL.setParameter("portletResource", panelAppPortlet.getPortletId());

												data.put("resource-href", editPermissionsResourceURL.toString());
											
            out.write("\n");
            out.write("\n");
            out.write("\t\t\t\t\t\t\t\t\t\t\t\t<li class=\"nav-item permission-navigation-item-container ");
            out.print( portletResource.equals(panelAppPortlet.getPortletId())? "selected" : "" );
            out.write("\">\n");
            out.write("\t\t\t\t\t\t\t\t\t\t\t\t\t");
            //  aui:a
            com.liferay.taglib.aui.ATag _jspx_th_aui_a_4 = (_jspx_resourceInjector != null) ? _jspx_resourceInjector.createTagHandlerInstance(com.liferay.taglib.aui.ATag.class) : new com.liferay.taglib.aui.ATag();
            _jspx_th_aui_a_4.setPageContext(_jspx_page_context);
            _jspx_th_aui_a_4.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_clay_col_0);
            _jspx_th_aui_a_4.setCssClass("nav-link permission-navigation-link");
            _jspx_th_aui_a_4.setData( data );
            _jspx_th_aui_a_4.setHref( editPermissionsURL.toString() );
            int _jspx_eval_aui_a_4 = _jspx_th_aui_a_4.doStartTag();
            if (_jspx_eval_aui_a_4 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
              out.write("\n");
              out.write("\t\t\t\t\t\t\t\t\t\t\t\t\t\t");
              out.print( PortalUtil.getPortletLongTitle(panelAppPortlet, application, locale) );
              out.write("\n");
              out.write("\t\t\t\t\t\t\t\t\t\t\t\t\t");
            }
            if (_jspx_th_aui_a_4.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
              if (_jspx_resourceInjector != null) _jspx_resourceInjector.preDestroy(_jspx_th_aui_a_4);
              _jspx_th_aui_a_4.release();
              return;
            }
            if (_jspx_resourceInjector != null) _jspx_resourceInjector.preDestroy(_jspx_th_aui_a_4);
            _jspx_th_aui_a_4.release();
            out.write("\n");
            out.write("\t\t\t\t\t\t\t\t\t\t\t\t</li>\n");
            out.write("\n");
            out.write("\t\t\t\t\t\t\t\t\t\t\t");

											}
											
            out.write("\n");
            out.write("\n");
            out.write("\t\t\t\t\t\t\t\t\t\t</ul>\n");
            out.write("\t\t\t\t\t\t\t\t\t</div>\n");
            out.write("\t\t\t\t\t\t\t\t");
}
            out.write("\n");
            out.write("\n");
            out.write("\t\t\t\t\t\t\t");

							}
							
            out.write("\n");
            out.write("\n");
            out.write("\t\t\t\t\t\t</div>\n");
            out.write("\t\t\t\t\t</li>\n");
            out.write("\t\t\t\t\t<li class=\"nav-item permission-navigation-section\">\n");
            out.write("\t\t\t\t\t\t<a class=\"collapse-icon nav-link permission-navigation-item-header toggler-header toggler-header-expanded\" href=\"javascript:;\">\n");
            out.write("\t\t\t\t\t\t\t");
            if (_jspx_meth_liferay$1ui_message_4((javax.servlet.jsp.tagext.JspTag) _jspx_th_clay_col_0, _jspx_page_context))
              return;
            out.write("\n");
            out.write("\n");
            out.write("\t\t\t\t\t\t\t<span class=\"collapse-icon-closed\">\n");
            out.write("\t\t\t\t\t\t\t\t");
            if (_jspx_meth_aui_icon_8((javax.servlet.jsp.tagext.JspTag) _jspx_th_clay_col_0, _jspx_page_context))
              return;
            out.write("\n");
            out.write("\t\t\t\t\t\t\t</span>\n");
            out.write("\t\t\t\t\t\t\t<span class=\"collapse-icon-open\">\n");
            out.write("\t\t\t\t\t\t\t\t");
            if (_jspx_meth_aui_icon_9((javax.servlet.jsp.tagext.JspTag) _jspx_th_clay_col_0, _jspx_page_context))
              return;
            out.write("\n");
            out.write("\t\t\t\t\t\t\t</span>\n");
            out.write("\t\t\t\t\t\t</a>\n");
            out.write("\n");
            out.write("\t\t\t\t\t\t<div class=\"permission-navigation-item-content toggler-content toggler-content-expanded\">\n");
            out.write("\n");
            out.write("\t\t\t\t\t\t\t");

							for (PanelCategory panelCategory : panelCategoryRegistry.getChildPanelCategories(PanelCategoryKeys.APPLICATIONS_MENU_APPLICATIONS)) {
								List<PanelApp> panelApps = panelAppRegistry.getPanelApps(panelCategory);
							
            out.write("\n");
            out.write("\n");
            out.write("\t\t\t\t\t\t\t\t");
if (
 !panelApps.isEmpty() ) {
            out.write("\n");
            out.write("\t\t\t\t\t\t\t\t\t<div class=\"permission-navigation-section\">\n");
            out.write("\t\t\t\t\t\t\t\t\t\t<a class=\"collapse-icon nav-link permission-navigation-item-header toggler-header toggler-header-collapsed\" href=\"javascript:;\">\n");
            out.write("\t\t\t\t\t\t\t\t\t\t\t");
            out.print( panelCategory.getLabel(locale) );
            out.write("\n");
            out.write("\t\t\t\t\t\t\t\t\t\t\t<span class=\"collapse-icon-closed\">\n");
            out.write("\t\t\t\t\t\t\t\t\t\t\t\t");
            if (_jspx_meth_aui_icon_10((javax.servlet.jsp.tagext.JspTag) _jspx_th_clay_col_0, _jspx_page_context))
              return;
            out.write("\n");
            out.write("\t\t\t\t\t\t\t\t\t\t\t</span>\n");
            out.write("\t\t\t\t\t\t\t\t\t\t\t<span class=\"collapse-icon-open\">\n");
            out.write("\t\t\t\t\t\t\t\t\t\t\t\t");
            if (_jspx_meth_aui_icon_11((javax.servlet.jsp.tagext.JspTag) _jspx_th_clay_col_0, _jspx_page_context))
              return;
            out.write("\n");
            out.write("\t\t\t\t\t\t\t\t\t\t\t</span>\n");
            out.write("\t\t\t\t\t\t\t\t\t\t</a>\n");
            out.write("\n");
            out.write("\t\t\t\t\t\t\t\t\t\t<ul class=\"nav nav-stacked permission-navigation-item-content toggler-content toggler-content-collapsed\">\n");
            out.write("\n");
            out.write("\t\t\t\t\t\t\t\t\t\t\t");

											for (PanelApp panelApp : panelApps) {
												Portlet panelAppPortlet = PortletLocalServiceUtil.getPortletById(company.getCompanyId(), panelApp.getPortletId());

												String controlPanelEntryClassName = panelAppPortlet.getControlPanelEntryClass();
												ControlPanelEntry controlPanelEntry = panelAppPortlet.getControlPanelEntryInstance();

												if (Objects.equals(controlPanelEntryClassName, AdministratorControlPanelEntry.class.getName()) || Objects.equals(controlPanelEntryClassName, OmniadminControlPanelEntry.class.getName()) || AdministratorControlPanelEntry.class.isAssignableFrom(controlPanelEntry.getClass()) || OmniadminControlPanelEntry.class.isAssignableFrom(controlPanelEntry.getClass())) {
													continue;
												}

												editPermissionsResourceURL.setParameter("portletResource", panelAppPortlet.getPortletId());

												editPermissionsURL.setParameter("portletResource", panelAppPortlet.getPortletId());

												data.put("resource-href", editPermissionsResourceURL.toString());
											
            out.write("\n");
            out.write("\n");
            out.write("\t\t\t\t\t\t\t\t\t\t\t\t<li class=\"nav-item permission-navigation-item-container ");
            out.print( portletResource.equals(panelAppPortlet.getPortletId())? "selected" : "" );
            out.write("\">\n");
            out.write("\t\t\t\t\t\t\t\t\t\t\t\t\t");
            //  aui:a
            com.liferay.taglib.aui.ATag _jspx_th_aui_a_5 = (_jspx_resourceInjector != null) ? _jspx_resourceInjector.createTagHandlerInstance(com.liferay.taglib.aui.ATag.class) : new com.liferay.taglib.aui.ATag();
            _jspx_th_aui_a_5.setPageContext(_jspx_page_context);
            _jspx_th_aui_a_5.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_clay_col_0);
            _jspx_th_aui_a_5.setCssClass("nav-link permission-navigation-link");
            _jspx_th_aui_a_5.setData( data );
            _jspx_th_aui_a_5.setHref( editPermissionsURL.toString() );
            int _jspx_eval_aui_a_5 = _jspx_th_aui_a_5.doStartTag();
            if (_jspx_eval_aui_a_5 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
              out.write("\n");
              out.write("\t\t\t\t\t\t\t\t\t\t\t\t\t\t");
              out.print( PortalUtil.getPortletLongTitle(panelAppPortlet, application, locale) );
              out.write("\n");
              out.write("\t\t\t\t\t\t\t\t\t\t\t\t\t");
            }
            if (_jspx_th_aui_a_5.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
              if (_jspx_resourceInjector != null) _jspx_resourceInjector.preDestroy(_jspx_th_aui_a_5);
              _jspx_th_aui_a_5.release();
              return;
            }
            if (_jspx_resourceInjector != null) _jspx_resourceInjector.preDestroy(_jspx_th_aui_a_5);
            _jspx_th_aui_a_5.release();
            out.write("\n");
            out.write("\t\t\t\t\t\t\t\t\t\t\t\t</li>\n");
            out.write("\n");
            out.write("\t\t\t\t\t\t\t\t\t\t\t");

											}
											
            out.write("\n");
            out.write("\n");
            out.write("\t\t\t\t\t\t\t\t\t\t</ul>\n");
            out.write("\t\t\t\t\t\t\t\t\t</div>\n");
            out.write("\t\t\t\t\t\t\t\t");
}
            out.write("\n");
            out.write("\n");
            out.write("\t\t\t\t\t\t\t");

							}
							
            out.write("\n");
            out.write("\n");
            out.write("\t\t\t\t\t\t</div>\n");
            out.write("\t\t\t\t\t</li>\n");
            out.write("\t\t\t\t");
            out.write("\n");
            out.write("\t\t\t");
}
            out.write("\n");
            out.write("\n");
            out.write("\t\t\t");

			for (String panelCategoryKey : (String[])request.getAttribute(RolesAdminWebKeys.PANEL_CATEGORY_KEYS)) {
				PanelCategory panelCategory = panelCategoryRegistry.getPanelCategory(panelCategoryKey);

				List<PanelApp> panelApps = panelAppRegistry.getPanelApps(panelCategory);
			
            out.write("\n");
            out.write("\n");
            out.write("\t\t\t\t");
if (
 !panelApps.isEmpty() ) {
            out.write("\n");
            out.write("\t\t\t\t\t<div class=\"permission-navigation-section\">\n");
            out.write("\t\t\t\t\t\t<a class=\"collapse-icon nav-link permission-navigation-item-header toggler-header toggler-header-collapsed\" href=\"javascript:;\">\n");
            out.write("\t\t\t\t\t\t\t");
            out.print( panelCategory.getLabel(locale) );
            out.write("\n");
            out.write("\t\t\t\t\t\t\t<span class=\"collapse-icon-closed\">\n");
            out.write("\t\t\t\t\t\t\t\t");
            if (_jspx_meth_aui_icon_12((javax.servlet.jsp.tagext.JspTag) _jspx_th_clay_col_0, _jspx_page_context))
              return;
            out.write("\n");
            out.write("\t\t\t\t\t\t\t</span>\n");
            out.write("\t\t\t\t\t\t\t<span class=\"collapse-icon-open\">\n");
            out.write("\t\t\t\t\t\t\t\t");
            if (_jspx_meth_aui_icon_13((javax.servlet.jsp.tagext.JspTag) _jspx_th_clay_col_0, _jspx_page_context))
              return;
            out.write("\n");
            out.write("\t\t\t\t\t\t\t</span>\n");
            out.write("\t\t\t\t\t\t</a>\n");
            out.write("\n");
            out.write("\t\t\t\t\t\t<ul class=\"nav nav-stacked permission-navigation-item-content toggler-content toggler-content-collapsed\">\n");
            out.write("\n");
            out.write("\t\t\t\t\t\t\t");

							for (PanelApp panelApp : panelApps) {
								Portlet panelAppPortlet = PortletLocalServiceUtil.getPortletById(company.getCompanyId(), panelApp.getPortletId());

								String controlPanelEntryClassName = panelAppPortlet.getControlPanelEntryClass();
								ControlPanelEntry controlPanelEntry = panelAppPortlet.getControlPanelEntryInstance();

								if (Objects.equals(controlPanelEntryClassName, AdministratorControlPanelEntry.class.getName()) || Objects.equals(controlPanelEntryClassName, OmniadminControlPanelEntry.class.getName()) || AdministratorControlPanelEntry.class.isAssignableFrom(controlPanelEntry.getClass()) || OmniadminControlPanelEntry.class.isAssignableFrom(controlPanelEntry.getClass())) {
									continue;
								}

								editPermissionsResourceURL.setParameter("portletResource", panelAppPortlet.getPortletId());

								editPermissionsURL.setParameter("portletResource", panelAppPortlet.getPortletId());

								data.put("resource-href", editPermissionsResourceURL.toString());
							
            out.write("\n");
            out.write("\n");
            out.write("\t\t\t\t\t\t\t\t<li class=\"nav-item permission-navigation-item-container ");
            out.print( portletResource.equals(panelAppPortlet.getPortletId())? "selected" : "" );
            out.write("\">\n");
            out.write("\t\t\t\t\t\t\t\t\t");
            //  aui:a
            com.liferay.taglib.aui.ATag _jspx_th_aui_a_6 = (_jspx_resourceInjector != null) ? _jspx_resourceInjector.createTagHandlerInstance(com.liferay.taglib.aui.ATag.class) : new com.liferay.taglib.aui.ATag();
            _jspx_th_aui_a_6.setPageContext(_jspx_page_context);
            _jspx_th_aui_a_6.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_clay_col_0);
            _jspx_th_aui_a_6.setCssClass("nav-link permission-navigation-link");
            _jspx_th_aui_a_6.setData( data );
            _jspx_th_aui_a_6.setHref( editPermissionsURL.toString() );
            int _jspx_eval_aui_a_6 = _jspx_th_aui_a_6.doStartTag();
            if (_jspx_eval_aui_a_6 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
              out.write("\n");
              out.write("\t\t\t\t\t\t\t\t\t\t");
              out.print( PortalUtil.getPortletLongTitle(panelAppPortlet, application, locale) );
              out.write("\n");
              out.write("\t\t\t\t\t\t\t\t\t");
            }
            if (_jspx_th_aui_a_6.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
              if (_jspx_resourceInjector != null) _jspx_resourceInjector.preDestroy(_jspx_th_aui_a_6);
              _jspx_th_aui_a_6.release();
              return;
            }
            if (_jspx_resourceInjector != null) _jspx_resourceInjector.preDestroy(_jspx_th_aui_a_6);
            _jspx_th_aui_a_6.release();
            out.write("\n");
            out.write("\t\t\t\t\t\t\t\t</li>\n");
            out.write("\n");
            out.write("\t\t\t\t\t\t\t");

							}
							
            out.write("\n");
            out.write("\n");
            out.write("\t\t\t\t\t\t</ul>\n");
            out.write("\t\t\t\t\t</div>\n");
            out.write("\t\t\t\t");
}
            out.write("\n");
            out.write("\n");
            out.write("\t\t\t");

			}
			
            out.write("\n");
            out.write("\n");
            out.write("\t\t\t<li class=\"nav-item permission-navigation-section\">\n");
            out.write("\t\t\t\t<a class=\"collapse-icon nav-link permission-navigation-item-header toggler-header toggler-header-collapsed\" href=\"javascript:;\">\n");
            out.write("\t\t\t\t\t");
            if (_jspx_meth_liferay$1ui_message_5((javax.servlet.jsp.tagext.JspTag) _jspx_th_clay_col_0, _jspx_page_context))
              return;
            out.write("\n");
            out.write("\n");
            out.write("\t\t\t\t\t<span class=\"collapse-icon-closed\">\n");
            out.write("\t\t\t\t\t\t");
            if (_jspx_meth_aui_icon_14((javax.servlet.jsp.tagext.JspTag) _jspx_th_clay_col_0, _jspx_page_context))
              return;
            out.write("\n");
            out.write("\t\t\t\t\t</span>\n");
            out.write("\t\t\t\t\t<span class=\"collapse-icon-open\">\n");
            out.write("\t\t\t\t\t\t");
            if (_jspx_meth_aui_icon_15((javax.servlet.jsp.tagext.JspTag) _jspx_th_clay_col_0, _jspx_page_context))
              return;
            out.write("\n");
            out.write("\t\t\t\t\t</span>\n");
            out.write("\t\t\t\t</a>\n");
            out.write("\n");
            out.write("\t\t\t\t<div class=\"permission-navigation-item-content toggler-content toggler-content-collapsed\">\n");
            out.write("\n");
            out.write("\t\t\t\t\t");

					for (PanelCategory panelCategory : panelCategoryRegistry.getChildPanelCategories(PanelCategoryKeys.SITE_ADMINISTRATION)) {
						List<PanelApp> panelApps = panelAppRegistry.getPanelApps(panelCategory);
					
            out.write("\n");
            out.write("\n");
            out.write("\t\t\t\t\t\t");
if (
 !panelApps.isEmpty() ) {
            out.write("\n");
            out.write("\t\t\t\t\t\t\t<div class=\"permission-navigation-section\">\n");
            out.write("\t\t\t\t\t\t\t\t<a class=\"collapse-icon nav-link permission-navigation-item-header toggler-header toggler-header-collapsed\" href=\"javascript:;\">\n");
            out.write("\t\t\t\t\t\t\t\t\t");
            out.print( panelCategory.getLabel(locale) );
            out.write("\n");
            out.write("\t\t\t\t\t\t\t\t\t<span class=\"collapse-icon-closed\">\n");
            out.write("\t\t\t\t\t\t\t\t\t\t");
            if (_jspx_meth_aui_icon_16((javax.servlet.jsp.tagext.JspTag) _jspx_th_clay_col_0, _jspx_page_context))
              return;
            out.write("\n");
            out.write("\t\t\t\t\t\t\t\t\t</span>\n");
            out.write("\t\t\t\t\t\t\t\t\t<span class=\"collapse-icon-open\">\n");
            out.write("\t\t\t\t\t\t\t\t\t\t");
            if (_jspx_meth_aui_icon_17((javax.servlet.jsp.tagext.JspTag) _jspx_th_clay_col_0, _jspx_page_context))
              return;
            out.write("\n");
            out.write("\t\t\t\t\t\t\t\t\t</span>\n");
            out.write("\t\t\t\t\t\t\t\t</a>\n");
            out.write("\n");
            out.write("\t\t\t\t\t\t\t\t<ul class=\"nav nav-stacked permission-navigation-item-content toggler-content toggler-content-collapsed\">\n");
            out.write("\n");
            out.write("\t\t\t\t\t\t\t\t\t");

									for (PanelApp panelApp : panelApps) {
										Portlet panelAppPortlet = PortletLocalServiceUtil.getPortletById(company.getCompanyId(), panelApp.getPortletId());

										editPermissionsResourceURL.setParameter("portletResource", panelAppPortlet.getPortletId());

										editPermissionsURL.setParameter("portletResource", panelAppPortlet.getPortletId());

										data.put("resource-href", editPermissionsResourceURL.toString());
									
            out.write("\n");
            out.write("\n");
            out.write("\t\t\t\t\t\t\t\t\t\t<li class=\"nav-item permission-navigation-item-container ");
            out.print( portletResource.equals(panelAppPortlet.getPortletId())? "selected" : "" );
            out.write("\">\n");
            out.write("\t\t\t\t\t\t\t\t\t\t\t");
            //  aui:a
            com.liferay.taglib.aui.ATag _jspx_th_aui_a_7 = (_jspx_resourceInjector != null) ? _jspx_resourceInjector.createTagHandlerInstance(com.liferay.taglib.aui.ATag.class) : new com.liferay.taglib.aui.ATag();
            _jspx_th_aui_a_7.setPageContext(_jspx_page_context);
            _jspx_th_aui_a_7.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_clay_col_0);
            _jspx_th_aui_a_7.setCssClass("nav-link permission-navigation-link");
            _jspx_th_aui_a_7.setData( data );
            _jspx_th_aui_a_7.setHref( editPermissionsURL.toString() );
            int _jspx_eval_aui_a_7 = _jspx_th_aui_a_7.doStartTag();
            if (_jspx_eval_aui_a_7 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
              out.write("\n");
              out.write("\t\t\t\t\t\t\t\t\t\t\t\t");
              out.print( PortalUtil.getPortletLongTitle(panelAppPortlet, application, locale) );
              out.write("\n");
              out.write("\t\t\t\t\t\t\t\t\t\t\t");
            }
            if (_jspx_th_aui_a_7.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
              if (_jspx_resourceInjector != null) _jspx_resourceInjector.preDestroy(_jspx_th_aui_a_7);
              _jspx_th_aui_a_7.release();
              return;
            }
            if (_jspx_resourceInjector != null) _jspx_resourceInjector.preDestroy(_jspx_th_aui_a_7);
            _jspx_th_aui_a_7.release();
            out.write("\n");
            out.write("\t\t\t\t\t\t\t\t\t\t</li>\n");
            out.write("\n");
            out.write("\t\t\t\t\t\t\t\t\t");

									}
									
            out.write("\n");
            out.write("\n");
            out.write("\t\t\t\t\t\t\t\t</ul>\n");
            out.write("\t\t\t\t\t\t\t</div>\n");
            out.write("\t\t\t\t\t\t");
}
            out.write("\n");
            out.write("\n");
            out.write("\t\t\t\t\t");

					}

					Set<String> hiddenPortletIds = Collections.emptySet();

					PortletCategory portletCategory = (PortletCategory)WebAppPool.get(company.getCompanyId(), WebKeys.PORTLET_CATEGORY);

					PortletCategory hiddentPortletCategory = portletCategory.getCategory(PortletCategoryConstants.NAME_HIDDEN);

					if (hiddentPortletCategory != null) {
						hiddenPortletIds = hiddentPortletCategory.getPortletIds();
					}
					
            out.write("\n");
            out.write("\n");
            out.write("\t\t\t\t\t<div class=\"permission-navigation-section\">\n");
            out.write("\t\t\t\t\t\t<a class=\"collapse-icon nav-link permission-navigation-item-header toggler-header toggler-header-collapsed\" href=\"javascript:;\">\n");
            out.write("\t\t\t\t\t\t\t");
            if (_jspx_meth_liferay$1ui_message_6((javax.servlet.jsp.tagext.JspTag) _jspx_th_clay_col_0, _jspx_page_context))
              return;
            out.write("\n");
            out.write("\n");
            out.write("\t\t\t\t\t\t\t<span class=\"collapse-icon-closed\">\n");
            out.write("\t\t\t\t\t\t\t\t");
            if (_jspx_meth_aui_icon_18((javax.servlet.jsp.tagext.JspTag) _jspx_th_clay_col_0, _jspx_page_context))
              return;
            out.write("\n");
            out.write("\t\t\t\t\t\t\t</span>\n");
            out.write("\t\t\t\t\t\t\t<span class=\"collapse-icon-open\">\n");
            out.write("\t\t\t\t\t\t\t\t");
            if (_jspx_meth_aui_icon_19((javax.servlet.jsp.tagext.JspTag) _jspx_th_clay_col_0, _jspx_page_context))
              return;
            out.write("\n");
            out.write("\t\t\t\t\t\t\t</span>\n");
            out.write("\t\t\t\t\t\t</a>\n");
            out.write("\n");
            out.write("\t\t\t\t\t\t<ul class=\"nav nav-stacked permission-navigation-item-content toggler-content toggler-content-collapsed\">\n");
            out.write("\n");
            out.write("\t\t\t\t\t\t\t");

							boolean includeSystemPortlets = false;

							List<Portlet> portlets = PortletLocalServiceUtil.getPortlets(company.getCompanyId(), includeSystemPortlets, false);

							portlets = ListUtil.sort(portlets, new PortletTitleComparator(application, locale));

							for (Portlet curPortlet : portlets) {
								if (Validator.isNull(curPortlet.getPortletId()) || hiddenPortletIds.contains(curPortlet.getPortletId())) {
									continue;
								}

								editPermissionsResourceURL.setParameter("portletResource", curPortlet.getPortletId());

								editPermissionsURL.setParameter("portletResource", curPortlet.getPortletId());

								data.put("resource-href", editPermissionsResourceURL.toString());
							
            out.write("\n");
            out.write("\n");
            out.write("\t\t\t\t\t\t\t\t<li class=\"nav-item permission-navigation-item-container ");
            out.print( portletResource.equals(curPortlet.getPortletId())? "selected" : "" );
            out.write("\">\n");
            out.write("\t\t\t\t\t\t\t\t\t");
            //  aui:a
            com.liferay.taglib.aui.ATag _jspx_th_aui_a_8 = (_jspx_resourceInjector != null) ? _jspx_resourceInjector.createTagHandlerInstance(com.liferay.taglib.aui.ATag.class) : new com.liferay.taglib.aui.ATag();
            _jspx_th_aui_a_8.setPageContext(_jspx_page_context);
            _jspx_th_aui_a_8.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_clay_col_0);
            _jspx_th_aui_a_8.setCssClass("nav-link permission-navigation-link");
            _jspx_th_aui_a_8.setData( data );
            _jspx_th_aui_a_8.setHref( editPermissionsURL.toString() );
            int _jspx_eval_aui_a_8 = _jspx_th_aui_a_8.doStartTag();
            if (_jspx_eval_aui_a_8 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
              out.write("\n");
              out.write("\t\t\t\t\t\t\t\t\t\t");
              out.print( PortalUtil.getPortletLongTitle(curPortlet, application, locale) );
              out.write("\n");
              out.write("\t\t\t\t\t\t\t\t\t");
            }
            if (_jspx_th_aui_a_8.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
              if (_jspx_resourceInjector != null) _jspx_resourceInjector.preDestroy(_jspx_th_aui_a_8);
              _jspx_th_aui_a_8.release();
              return;
            }
            if (_jspx_resourceInjector != null) _jspx_resourceInjector.preDestroy(_jspx_th_aui_a_8);
            _jspx_th_aui_a_8.release();
            out.write("\n");
            out.write("\t\t\t\t\t\t\t\t</li>\n");
            out.write("\n");
            out.write("\t\t\t\t\t\t\t");

							}
							
            out.write("\n");
            out.write("\n");
            out.write("\t\t\t\t\t\t</ul>\n");
            out.write("\t\t\t\t\t</div>\n");
            out.write("\t\t\t\t</div>\n");
            out.write("\t\t\t</li>\n");
            out.write("\n");
            out.write("\t\t\t");
if (
 role.getType() == RoleConstants.TYPE_REGULAR ) {
            out.write("\n");
            out.write("\t\t\t\t<li class=\"nav-item permission-navigation-section\">\n");
            out.write("\t\t\t\t\t<a class=\"collapse-icon collapse-icon nav-link nav-link permission-navigation-item-header toggler-header toggler-header-collapsed\" href=\"javascript:;\">\n");
            out.write("\t\t\t\t\t\t");
            if (_jspx_meth_liferay$1ui_message_7((javax.servlet.jsp.tagext.JspTag) _jspx_th_clay_col_0, _jspx_page_context))
              return;
            out.write("\n");
            out.write("\n");
            out.write("\t\t\t\t\t\t<span class=\"collapse-icon-closed\">\n");
            out.write("\t\t\t\t\t\t\t");
            if (_jspx_meth_aui_icon_20((javax.servlet.jsp.tagext.JspTag) _jspx_th_clay_col_0, _jspx_page_context))
              return;
            out.write("\n");
            out.write("\t\t\t\t\t\t</span>\n");
            out.write("\t\t\t\t\t\t<span class=\"collapse-icon-open\">\n");
            out.write("\t\t\t\t\t\t\t");
            if (_jspx_meth_aui_icon_21((javax.servlet.jsp.tagext.JspTag) _jspx_th_clay_col_0, _jspx_page_context))
              return;
            out.write("\n");
            out.write("\t\t\t\t\t\t</span>\n");
            out.write("\t\t\t\t\t</a>\n");
            out.write("\n");
            out.write("\t\t\t\t\t<ul class=\"nav nav-stacked permission-navigation-item-content toggler-content toggler-content-collapsed\">\n");
            out.write("\n");
            out.write("\t\t\t\t\t\t");

						for (BasePersonalMenuEntry basePersonalMenuEntry : personalMenuEntryHelper.getBasePersonalMenuEntries()) {
							Portlet personalPortlet = PortletLocalServiceUtil.getPortletById(company.getCompanyId(), basePersonalMenuEntry.getPortletId());

							editPermissionsResourceURL.setParameter("portletResource", personalPortlet.getPortletId());

							editPermissionsURL.setParameter("portletResource", personalPortlet.getPortletId());

							data.put("resource-href", editPermissionsResourceURL.toString());
						
            out.write("\n");
            out.write("\n");
            out.write("\t\t\t\t\t\t\t<li class=\"nav-item permission-navigation-item-container ");
            out.print( portletResource.equals(personalPortlet.getPortletId())? "selected" : "" );
            out.write("\">\n");
            out.write("\t\t\t\t\t\t\t\t");
            //  aui:a
            com.liferay.taglib.aui.ATag _jspx_th_aui_a_9 = (_jspx_resourceInjector != null) ? _jspx_resourceInjector.createTagHandlerInstance(com.liferay.taglib.aui.ATag.class) : new com.liferay.taglib.aui.ATag();
            _jspx_th_aui_a_9.setPageContext(_jspx_page_context);
            _jspx_th_aui_a_9.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_clay_col_0);
            _jspx_th_aui_a_9.setCssClass("nav-link permission-navigation-link");
            _jspx_th_aui_a_9.setData( data );
            _jspx_th_aui_a_9.setHref( editPermissionsURL.toString() );
            int _jspx_eval_aui_a_9 = _jspx_th_aui_a_9.doStartTag();
            if (_jspx_eval_aui_a_9 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
              out.write("\n");
              out.write("\t\t\t\t\t\t\t\t\t");
              out.print( PortalUtil.getPortletLongTitle(personalPortlet, application, locale) );
              out.write("\n");
              out.write("\t\t\t\t\t\t\t\t");
            }
            if (_jspx_th_aui_a_9.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
              if (_jspx_resourceInjector != null) _jspx_resourceInjector.preDestroy(_jspx_th_aui_a_9);
              _jspx_th_aui_a_9.release();
              return;
            }
            if (_jspx_resourceInjector != null) _jspx_resourceInjector.preDestroy(_jspx_th_aui_a_9);
            _jspx_th_aui_a_9.release();
            out.write("\n");
            out.write("\t\t\t\t\t\t\t</li>\n");
            out.write("\n");
            out.write("\t\t\t\t\t\t");

						}
						
            out.write("\n");
            out.write("\n");
            out.write("\t\t\t\t\t</ul>\n");
            out.write("\t\t\t\t</li>\n");
            out.write("\t\t\t");
}
            out.write("\n");
            out.write("\t\t</ul>\n");
            out.write("\t</div>\n");
            out.write("</div>");
            out.write("\n");
            out.write("\t\t\t");
          }
          if (_jspx_th_clay_col_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
            if (_jspx_resourceInjector != null) _jspx_resourceInjector.preDestroy(_jspx_th_clay_col_0);
            _jspx_th_clay_col_0.release();
            return;
          }
          if (_jspx_resourceInjector != null) _jspx_resourceInjector.preDestroy(_jspx_th_clay_col_0);
          _jspx_th_clay_col_0.release();
          out.write("\n");
          out.write("\t\t");
}
          out.write("\n");
          out.write("\n");
          out.write("\t\t");
          //  clay:col
          com.liferay.frontend.taglib.clay.servlet.taglib.ColTag _jspx_th_clay_col_1 = (_jspx_resourceInjector != null) ? _jspx_resourceInjector.createTagHandlerInstance(com.liferay.frontend.taglib.clay.servlet.taglib.ColTag.class) : new com.liferay.frontend.taglib.clay.servlet.taglib.ColTag();
          _jspx_th_clay_col_1.setPageContext(_jspx_page_context);
          _jspx_th_clay_col_1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_clay_row_0);
          _jspx_th_clay_col_1.setCssClass("lfr-permission-content-container");
          _jspx_th_clay_col_1.setDynamicAttribute(null, "id",  liferayPortletResponse.getNamespace() + "permissionContentContainer" );
          _jspx_th_clay_col_1.setMd( portletName.equals(PortletKeys.SERVER_ADMIN) ? String.valueOf(12) : String.valueOf(9) );
          int _jspx_eval_clay_col_1 = _jspx_th_clay_col_1.doStartTag();
          if (_jspx_eval_clay_col_1 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
            out.write("\n");
            out.write("\t\t\t");
            out.write("\n");
            out.write("\t\t\t\t");
if (
 cmd.equals(Constants.VIEW) ) {
            out.write("\n");
            out.write("\t\t\t\t\t");
            //  liferay-util:include
            com.liferay.taglib.util.IncludeTag _jspx_th_liferay$1util_include_1 = (_jspx_resourceInjector != null) ? _jspx_resourceInjector.createTagHandlerInstance(com.liferay.taglib.util.IncludeTag.class) : new com.liferay.taglib.util.IncludeTag();
            _jspx_th_liferay$1util_include_1.setPageContext(_jspx_page_context);
            _jspx_th_liferay$1util_include_1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_clay_col_1);
            _jspx_th_liferay$1util_include_1.setPage("/edit_role_permissions_summary.jsp");
            _jspx_th_liferay$1util_include_1.setServletContext( application );
            int _jspx_eval_liferay$1util_include_1 = _jspx_th_liferay$1util_include_1.doStartTag();
            if (_jspx_th_liferay$1util_include_1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
              if (_jspx_resourceInjector != null) _jspx_resourceInjector.preDestroy(_jspx_th_liferay$1util_include_1);
              _jspx_th_liferay$1util_include_1.release();
              return;
            }
            if (_jspx_resourceInjector != null) _jspx_resourceInjector.preDestroy(_jspx_th_liferay$1util_include_1);
            _jspx_th_liferay$1util_include_1.release();
            out.write("\n");
            out.write("\n");
            out.write("\t\t\t\t\t");
if (
 portletName.equals(PortletKeys.SERVER_ADMIN) ) {
            out.write("\n");
            out.write("\t\t\t\t\t\t<br />\n");
            out.write("\n");
            out.write("\t\t\t\t\t\t");
            //  aui:button
            com.liferay.taglib.aui.ButtonTag _jspx_th_aui_button_0 = (_jspx_resourceInjector != null) ? _jspx_resourceInjector.createTagHandlerInstance(com.liferay.taglib.aui.ButtonTag.class) : new com.liferay.taglib.aui.ButtonTag();
            _jspx_th_aui_button_0.setPageContext(_jspx_page_context);
            _jspx_th_aui_button_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_clay_col_1);
            _jspx_th_aui_button_0.setHref( redirect );
            _jspx_th_aui_button_0.setType("cancel");
            int _jspx_eval_aui_button_0 = _jspx_th_aui_button_0.doStartTag();
            if (_jspx_th_aui_button_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
              if (_jspx_resourceInjector != null) _jspx_resourceInjector.preDestroy(_jspx_th_aui_button_0);
              _jspx_th_aui_button_0.release();
              return;
            }
            if (_jspx_resourceInjector != null) _jspx_resourceInjector.preDestroy(_jspx_th_aui_button_0);
            _jspx_th_aui_button_0.release();
            out.write("\n");
            out.write("\t\t\t\t\t");
}
            out.write("\n");
            out.write("\t\t\t\t");
            out.write("\n");
            out.write("\t\t\t\t");
}
else {
            out.write("\n");
            out.write("\t\t\t\t\t");
            //  liferay-util:include
            com.liferay.taglib.util.IncludeTag _jspx_th_liferay$1util_include_2 = (_jspx_resourceInjector != null) ? _jspx_resourceInjector.createTagHandlerInstance(com.liferay.taglib.util.IncludeTag.class) : new com.liferay.taglib.util.IncludeTag();
            _jspx_th_liferay$1util_include_2.setPageContext(_jspx_page_context);
            _jspx_th_liferay$1util_include_2.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_clay_col_1);
            _jspx_th_liferay$1util_include_2.setPage("/edit_role_permissions_form.jsp");
            _jspx_th_liferay$1util_include_2.setServletContext( application );
            int _jspx_eval_liferay$1util_include_2 = _jspx_th_liferay$1util_include_2.doStartTag();
            if (_jspx_th_liferay$1util_include_2.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
              if (_jspx_resourceInjector != null) _jspx_resourceInjector.preDestroy(_jspx_th_liferay$1util_include_2);
              _jspx_th_liferay$1util_include_2.release();
              return;
            }
            if (_jspx_resourceInjector != null) _jspx_resourceInjector.preDestroy(_jspx_th_liferay$1util_include_2);
            _jspx_th_liferay$1util_include_2.release();
            out.write("\n");
            out.write("\t\t\t\t");
            out.write("\n");
            out.write("\t\t\t");
}
            out.write("\n");
            out.write("\t\t");
          }
          if (_jspx_th_clay_col_1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
            if (_jspx_resourceInjector != null) _jspx_resourceInjector.preDestroy(_jspx_th_clay_col_1);
            _jspx_th_clay_col_1.release();
            return;
          }
          if (_jspx_resourceInjector != null) _jspx_resourceInjector.preDestroy(_jspx_th_clay_col_1);
          _jspx_th_clay_col_1.release();
          out.write('\n');
          out.write('	');
        }
        if (_jspx_th_clay_row_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
          if (_jspx_resourceInjector != null) _jspx_resourceInjector.preDestroy(_jspx_th_clay_row_0);
          _jspx_th_clay_row_0.release();
          return;
        }
        if (_jspx_resourceInjector != null) _jspx_resourceInjector.preDestroy(_jspx_th_clay_row_0);
        _jspx_th_clay_row_0.release();
        out.write('\n');
      }
      if (_jspx_th_clay_container$1fluid_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
        if (_jspx_resourceInjector != null) _jspx_resourceInjector.preDestroy(_jspx_th_clay_container$1fluid_0);
        _jspx_th_clay_container$1fluid_0.release();
        return;
      }
      if (_jspx_resourceInjector != null) _jspx_resourceInjector.preDestroy(_jspx_th_clay_container$1fluid_0);
      _jspx_th_clay_container$1fluid_0.release();
      out.write('\n');
      out.write('\n');
      if (_jspx_meth_aui_script_0(_jspx_page_context))
        return;
      out.write('\n');
      out.write('\n');
      if (_jspx_meth_aui_script_1(_jspx_page_context))
        return;
      out.write('\n');
      out.write('\n');
      //  aui:script
      com.liferay.taglib.aui.ScriptTag _jspx_th_aui_script_2 = (_jspx_resourceInjector != null) ? _jspx_resourceInjector.createTagHandlerInstance(com.liferay.taglib.aui.ScriptTag.class) : new com.liferay.taglib.aui.ScriptTag();
      _jspx_th_aui_script_2.setPageContext(_jspx_page_context);
      _jspx_th_aui_script_2.setParent(null);
      int _jspx_eval_aui_script_2 = _jspx_th_aui_script_2.doStartTag();
      if (_jspx_eval_aui_script_2 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
        if (_jspx_eval_aui_script_2 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
          out = _jspx_page_context.pushBody();
          _jspx_th_aui_script_2.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
          _jspx_th_aui_script_2.doInitBody();
        }
        do {
          out.write("\n");
          out.write("\tfunction ");
          if (_jspx_meth_portlet_namespace_15((javax.servlet.jsp.tagext.JspTag) _jspx_th_aui_script_2, _jspx_page_context))
            return;
          out.write("updateActions() {\n");
          out.write("\t\tvar form = document.");
          if (_jspx_meth_portlet_namespace_16((javax.servlet.jsp.tagext.JspTag) _jspx_th_aui_script_2, _jspx_page_context))
            return;
          out.write("fm;\n");
          out.write("\n");
          out.write("\t\tLiferay.Util.postForm(form, {\n");
          out.write("\t\t\tdata: {\n");
          out.write("\t\t\t\tredirect: '");
          out.print( HtmlUtil.escapeJS(redirect) );
          out.write("',\n");
          out.write("\t\t\t\tselectedTargets: Liferay.Util.listCheckedExcept(\n");
          out.write("\t\t\t\t\tform,\n");
          out.write("\t\t\t\t\t'");
          if (_jspx_meth_portlet_namespace_17((javax.servlet.jsp.tagext.JspTag) _jspx_th_aui_script_2, _jspx_page_context))
            return;
          out.write("allRowIds'\n");
          out.write("\t\t\t\t),\n");
          out.write("\t\t\t\tunselectedTargets: Liferay.Util.listUncheckedExcept(\n");
          out.write("\t\t\t\t\tform,\n");
          out.write("\t\t\t\t\t'");
          if (_jspx_meth_portlet_namespace_18((javax.servlet.jsp.tagext.JspTag) _jspx_th_aui_script_2, _jspx_page_context))
            return;
          out.write("allRowIds'\n");
          out.write("\t\t\t\t),\n");
          out.write("\t\t\t},\n");
          out.write("\t\t});\n");
          out.write("\t}\n");
          int evalDoAfterBody = _jspx_th_aui_script_2.doAfterBody();
          if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
            break;
        } while (true);
        if (_jspx_eval_aui_script_2 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
          out = _jspx_page_context.popBody();
      }
      if (_jspx_th_aui_script_2.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
        if (_jspx_resourceInjector != null) _jspx_resourceInjector.preDestroy(_jspx_th_aui_script_2);
        _jspx_th_aui_script_2.release();
        return;
      }
      if (_jspx_resourceInjector != null) _jspx_resourceInjector.preDestroy(_jspx_th_aui_script_2);
      _jspx_th_aui_script_2.release();
    } catch (Throwable t) {
      if (!(t instanceof SkipPageException)){
        out = _jspx_out;
        if (out != null && out.getBufferSize() != 0)
          out.clearBuffer();
        if (_jspx_page_context != null) _jspx_page_context.handlePageException(t);
        else throw new ServletException(t);
      }
    } finally {
      _jspxFactory.releasePageContext(_jspx_page_context);
    }
  }

  private boolean _jspx_meth_liferay$1ui_success_0(PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  liferay-ui:success
    com.liferay.taglib.ui.SuccessTag _jspx_th_liferay$1ui_success_0 = (_jspx_resourceInjector != null) ? _jspx_resourceInjector.createTagHandlerInstance(com.liferay.taglib.ui.SuccessTag.class) : new com.liferay.taglib.ui.SuccessTag();
    _jspx_th_liferay$1ui_success_0.setPageContext(_jspx_page_context);
    _jspx_th_liferay$1ui_success_0.setParent(null);
    _jspx_th_liferay$1ui_success_0.setKey("permissionDeleted");
    _jspx_th_liferay$1ui_success_0.setMessage("the-permission-was-deleted");
    int _jspx_eval_liferay$1ui_success_0 = _jspx_th_liferay$1ui_success_0.doStartTag();
    if (_jspx_th_liferay$1ui_success_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      if (_jspx_resourceInjector != null) _jspx_resourceInjector.preDestroy(_jspx_th_liferay$1ui_success_0);
      _jspx_th_liferay$1ui_success_0.release();
      return true;
    }
    if (_jspx_resourceInjector != null) _jspx_resourceInjector.preDestroy(_jspx_th_liferay$1ui_success_0);
    _jspx_th_liferay$1ui_success_0.release();
    return false;
  }

  private boolean _jspx_meth_liferay$1ui_success_1(PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  liferay-ui:success
    com.liferay.taglib.ui.SuccessTag _jspx_th_liferay$1ui_success_1 = (_jspx_resourceInjector != null) ? _jspx_resourceInjector.createTagHandlerInstance(com.liferay.taglib.ui.SuccessTag.class) : new com.liferay.taglib.ui.SuccessTag();
    _jspx_th_liferay$1ui_success_1.setPageContext(_jspx_page_context);
    _jspx_th_liferay$1ui_success_1.setParent(null);
    _jspx_th_liferay$1ui_success_1.setKey("permissionsUpdated");
    _jspx_th_liferay$1ui_success_1.setMessage("the-role-permissions-were-updated");
    int _jspx_eval_liferay$1ui_success_1 = _jspx_th_liferay$1ui_success_1.doStartTag();
    if (_jspx_th_liferay$1ui_success_1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      if (_jspx_resourceInjector != null) _jspx_resourceInjector.preDestroy(_jspx_th_liferay$1ui_success_1);
      _jspx_th_liferay$1ui_success_1.release();
      return true;
    }
    if (_jspx_resourceInjector != null) _jspx_resourceInjector.preDestroy(_jspx_th_liferay$1ui_success_1);
    _jspx_th_liferay$1ui_success_1.release();
    return false;
  }

  private boolean _jspx_meth_portlet_param_0(javax.servlet.jsp.tagext.JspTag _jspx_th_liferay$1portlet_resourceURL_0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  portlet:param
    com.liferay.taglib.util.ParamTag _jspx_th_portlet_param_0 = (_jspx_resourceInjector != null) ? _jspx_resourceInjector.createTagHandlerInstance(com.liferay.taglib.util.ParamTag.class) : new com.liferay.taglib.util.ParamTag();
    _jspx_th_portlet_param_0.setPageContext(_jspx_page_context);
    _jspx_th_portlet_param_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_liferay$1portlet_resourceURL_0);
    _jspx_th_portlet_param_0.setName("mvcPath");
    _jspx_th_portlet_param_0.setValue("/view_resources.jsp");
    int _jspx_eval_portlet_param_0 = _jspx_th_portlet_param_0.doStartTag();
    if (_jspx_th_portlet_param_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      if (_jspx_resourceInjector != null) _jspx_resourceInjector.preDestroy(_jspx_th_portlet_param_0);
      _jspx_th_portlet_param_0.release();
      return true;
    }
    if (_jspx_resourceInjector != null) _jspx_resourceInjector.preDestroy(_jspx_th_portlet_param_0);
    _jspx_th_portlet_param_0.release();
    return false;
  }

  private boolean _jspx_meth_portlet_param_2(javax.servlet.jsp.tagext.JspTag _jspx_th_liferay$1portlet_resourceURL_0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  portlet:param
    com.liferay.taglib.util.ParamTag _jspx_th_portlet_param_2 = (_jspx_resourceInjector != null) ? _jspx_resourceInjector.createTagHandlerInstance(com.liferay.taglib.util.ParamTag.class) : new com.liferay.taglib.util.ParamTag();
    _jspx_th_portlet_param_2.setPageContext(_jspx_page_context);
    _jspx_th_portlet_param_2.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_liferay$1portlet_resourceURL_0);
    _jspx_th_portlet_param_2.setName("tabs2");
    _jspx_th_portlet_param_2.setValue("roles");
    int _jspx_eval_portlet_param_2 = _jspx_th_portlet_param_2.doStartTag();
    if (_jspx_th_portlet_param_2.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      if (_jspx_resourceInjector != null) _jspx_resourceInjector.preDestroy(_jspx_th_portlet_param_2);
      _jspx_th_portlet_param_2.release();
      return true;
    }
    if (_jspx_resourceInjector != null) _jspx_resourceInjector.preDestroy(_jspx_th_portlet_param_2);
    _jspx_th_portlet_param_2.release();
    return false;
  }

  private boolean _jspx_meth_portlet_param_5(javax.servlet.jsp.tagext.JspTag _jspx_th_liferay$1portlet_renderURL_0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  portlet:param
    com.liferay.taglib.util.ParamTag _jspx_th_portlet_param_5 = (_jspx_resourceInjector != null) ? _jspx_resourceInjector.createTagHandlerInstance(com.liferay.taglib.util.ParamTag.class) : new com.liferay.taglib.util.ParamTag();
    _jspx_th_portlet_param_5.setPageContext(_jspx_page_context);
    _jspx_th_portlet_param_5.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_liferay$1portlet_renderURL_0);
    _jspx_th_portlet_param_5.setName("mvcPath");
    _jspx_th_portlet_param_5.setValue("/view_resources.jsp");
    int _jspx_eval_portlet_param_5 = _jspx_th_portlet_param_5.doStartTag();
    if (_jspx_th_portlet_param_5.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      if (_jspx_resourceInjector != null) _jspx_resourceInjector.preDestroy(_jspx_th_portlet_param_5);
      _jspx_th_portlet_param_5.release();
      return true;
    }
    if (_jspx_resourceInjector != null) _jspx_resourceInjector.preDestroy(_jspx_th_portlet_param_5);
    _jspx_th_portlet_param_5.release();
    return false;
  }

  private boolean _jspx_meth_portlet_param_7(javax.servlet.jsp.tagext.JspTag _jspx_th_liferay$1portlet_renderURL_0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  portlet:param
    com.liferay.taglib.util.ParamTag _jspx_th_portlet_param_7 = (_jspx_resourceInjector != null) ? _jspx_resourceInjector.createTagHandlerInstance(com.liferay.taglib.util.ParamTag.class) : new com.liferay.taglib.util.ParamTag();
    _jspx_th_portlet_param_7.setPageContext(_jspx_page_context);
    _jspx_th_portlet_param_7.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_liferay$1portlet_renderURL_0);
    _jspx_th_portlet_param_7.setName("tabs2");
    _jspx_th_portlet_param_7.setValue("roles");
    int _jspx_eval_portlet_param_7 = _jspx_th_portlet_param_7.doStartTag();
    if (_jspx_th_portlet_param_7.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      if (_jspx_resourceInjector != null) _jspx_resourceInjector.preDestroy(_jspx_th_portlet_param_7);
      _jspx_th_portlet_param_7.release();
      return true;
    }
    if (_jspx_resourceInjector != null) _jspx_resourceInjector.preDestroy(_jspx_th_portlet_param_7);
    _jspx_th_portlet_param_7.release();
    return false;
  }

  private boolean _jspx_meth_portlet_namespace_0(javax.servlet.jsp.tagext.JspTag _jspx_th_clay_col_0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  portlet:namespace
    com.liferay.taglib.portlet.NamespaceTag _jspx_th_portlet_namespace_0 = (_jspx_resourceInjector != null) ? _jspx_resourceInjector.createTagHandlerInstance(com.liferay.taglib.portlet.NamespaceTag.class) : new com.liferay.taglib.portlet.NamespaceTag();
    _jspx_th_portlet_namespace_0.setPageContext(_jspx_page_context);
    _jspx_th_portlet_namespace_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_clay_col_0);
    int _jspx_eval_portlet_namespace_0 = _jspx_th_portlet_namespace_0.doStartTag();
    if (_jspx_th_portlet_namespace_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      if (_jspx_resourceInjector != null) _jspx_resourceInjector.preDestroy(_jspx_th_portlet_namespace_0);
      _jspx_th_portlet_namespace_0.release();
      return true;
    }
    if (_jspx_resourceInjector != null) _jspx_resourceInjector.preDestroy(_jspx_th_portlet_namespace_0);
    _jspx_th_portlet_namespace_0.release();
    return false;
  }

  private boolean _jspx_meth_portlet_namespace_1(javax.servlet.jsp.tagext.JspTag _jspx_th_clay_col_0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  portlet:namespace
    com.liferay.taglib.portlet.NamespaceTag _jspx_th_portlet_namespace_1 = (_jspx_resourceInjector != null) ? _jspx_resourceInjector.createTagHandlerInstance(com.liferay.taglib.portlet.NamespaceTag.class) : new com.liferay.taglib.portlet.NamespaceTag();
    _jspx_th_portlet_namespace_1.setPageContext(_jspx_page_context);
    _jspx_th_portlet_namespace_1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_clay_col_0);
    int _jspx_eval_portlet_namespace_1 = _jspx_th_portlet_namespace_1.doStartTag();
    if (_jspx_th_portlet_namespace_1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      if (_jspx_resourceInjector != null) _jspx_resourceInjector.preDestroy(_jspx_th_portlet_namespace_1);
      _jspx_th_portlet_namespace_1.release();
      return true;
    }
    if (_jspx_resourceInjector != null) _jspx_resourceInjector.preDestroy(_jspx_th_portlet_namespace_1);
    _jspx_th_portlet_namespace_1.release();
    return false;
  }

  private boolean _jspx_meth_portlet_namespace_2(javax.servlet.jsp.tagext.JspTag _jspx_th_clay_col_0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  portlet:namespace
    com.liferay.taglib.portlet.NamespaceTag _jspx_th_portlet_namespace_2 = (_jspx_resourceInjector != null) ? _jspx_resourceInjector.createTagHandlerInstance(com.liferay.taglib.portlet.NamespaceTag.class) : new com.liferay.taglib.portlet.NamespaceTag();
    _jspx_th_portlet_namespace_2.setPageContext(_jspx_page_context);
    _jspx_th_portlet_namespace_2.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_clay_col_0);
    int _jspx_eval_portlet_namespace_2 = _jspx_th_portlet_namespace_2.doStartTag();
    if (_jspx_th_portlet_namespace_2.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      if (_jspx_resourceInjector != null) _jspx_resourceInjector.preDestroy(_jspx_th_portlet_namespace_2);
      _jspx_th_portlet_namespace_2.release();
      return true;
    }
    if (_jspx_resourceInjector != null) _jspx_resourceInjector.preDestroy(_jspx_th_portlet_namespace_2);
    _jspx_th_portlet_namespace_2.release();
    return false;
  }

  private boolean _jspx_meth_portlet_namespace_3(javax.servlet.jsp.tagext.JspTag _jspx_th_clay_col_0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  portlet:namespace
    com.liferay.taglib.portlet.NamespaceTag _jspx_th_portlet_namespace_3 = (_jspx_resourceInjector != null) ? _jspx_resourceInjector.createTagHandlerInstance(com.liferay.taglib.portlet.NamespaceTag.class) : new com.liferay.taglib.portlet.NamespaceTag();
    _jspx_th_portlet_namespace_3.setPageContext(_jspx_page_context);
    _jspx_th_portlet_namespace_3.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_clay_col_0);
    int _jspx_eval_portlet_namespace_3 = _jspx_th_portlet_namespace_3.doStartTag();
    if (_jspx_th_portlet_namespace_3.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      if (_jspx_resourceInjector != null) _jspx_resourceInjector.preDestroy(_jspx_th_portlet_namespace_3);
      _jspx_th_portlet_namespace_3.release();
      return true;
    }
    if (_jspx_resourceInjector != null) _jspx_resourceInjector.preDestroy(_jspx_th_portlet_namespace_3);
    _jspx_th_portlet_namespace_3.release();
    return false;
  }

  private boolean _jspx_meth_portlet_namespace_4(javax.servlet.jsp.tagext.JspTag _jspx_th_clay_col_0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  portlet:namespace
    com.liferay.taglib.portlet.NamespaceTag _jspx_th_portlet_namespace_4 = (_jspx_resourceInjector != null) ? _jspx_resourceInjector.createTagHandlerInstance(com.liferay.taglib.portlet.NamespaceTag.class) : new com.liferay.taglib.portlet.NamespaceTag();
    _jspx_th_portlet_namespace_4.setPageContext(_jspx_page_context);
    _jspx_th_portlet_namespace_4.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_clay_col_0);
    int _jspx_eval_portlet_namespace_4 = _jspx_th_portlet_namespace_4.doStartTag();
    if (_jspx_th_portlet_namespace_4.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      if (_jspx_resourceInjector != null) _jspx_resourceInjector.preDestroy(_jspx_th_portlet_namespace_4);
      _jspx_th_portlet_namespace_4.release();
      return true;
    }
    if (_jspx_resourceInjector != null) _jspx_resourceInjector.preDestroy(_jspx_th_portlet_namespace_4);
    _jspx_th_portlet_namespace_4.release();
    return false;
  }

  private boolean _jspx_meth_portlet_param_11(javax.servlet.jsp.tagext.JspTag _jspx_th_liferay$1portlet_resourceURL_1, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  portlet:param
    com.liferay.taglib.util.ParamTag _jspx_th_portlet_param_11 = (_jspx_resourceInjector != null) ? _jspx_resourceInjector.createTagHandlerInstance(com.liferay.taglib.util.ParamTag.class) : new com.liferay.taglib.util.ParamTag();
    _jspx_th_portlet_param_11.setPageContext(_jspx_page_context);
    _jspx_th_portlet_param_11.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_liferay$1portlet_resourceURL_1);
    _jspx_th_portlet_param_11.setName("mvcPath");
    _jspx_th_portlet_param_11.setValue("/view_resources.jsp");
    int _jspx_eval_portlet_param_11 = _jspx_th_portlet_param_11.doStartTag();
    if (_jspx_th_portlet_param_11.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      if (_jspx_resourceInjector != null) _jspx_resourceInjector.preDestroy(_jspx_th_portlet_param_11);
      _jspx_th_portlet_param_11.release();
      return true;
    }
    if (_jspx_resourceInjector != null) _jspx_resourceInjector.preDestroy(_jspx_th_portlet_param_11);
    _jspx_th_portlet_param_11.release();
    return false;
  }

  private boolean _jspx_meth_portlet_param_13(javax.servlet.jsp.tagext.JspTag _jspx_th_liferay$1portlet_resourceURL_1, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  portlet:param
    com.liferay.taglib.util.ParamTag _jspx_th_portlet_param_13 = (_jspx_resourceInjector != null) ? _jspx_resourceInjector.createTagHandlerInstance(com.liferay.taglib.util.ParamTag.class) : new com.liferay.taglib.util.ParamTag();
    _jspx_th_portlet_param_13.setPageContext(_jspx_page_context);
    _jspx_th_portlet_param_13.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_liferay$1portlet_resourceURL_1);
    _jspx_th_portlet_param_13.setName("tabs1");
    _jspx_th_portlet_param_13.setValue("roles");
    int _jspx_eval_portlet_param_13 = _jspx_th_portlet_param_13.doStartTag();
    if (_jspx_th_portlet_param_13.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      if (_jspx_resourceInjector != null) _jspx_resourceInjector.preDestroy(_jspx_th_portlet_param_13);
      _jspx_th_portlet_param_13.release();
      return true;
    }
    if (_jspx_resourceInjector != null) _jspx_resourceInjector.preDestroy(_jspx_th_portlet_param_13);
    _jspx_th_portlet_param_13.release();
    return false;
  }

  private boolean _jspx_meth_portlet_param_16(javax.servlet.jsp.tagext.JspTag _jspx_th_liferay$1portlet_renderURL_1, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  portlet:param
    com.liferay.taglib.util.ParamTag _jspx_th_portlet_param_16 = (_jspx_resourceInjector != null) ? _jspx_resourceInjector.createTagHandlerInstance(com.liferay.taglib.util.ParamTag.class) : new com.liferay.taglib.util.ParamTag();
    _jspx_th_portlet_param_16.setPageContext(_jspx_page_context);
    _jspx_th_portlet_param_16.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_liferay$1portlet_renderURL_1);
    _jspx_th_portlet_param_16.setName("mvcPath");
    _jspx_th_portlet_param_16.setValue("/view_resources.jsp");
    int _jspx_eval_portlet_param_16 = _jspx_th_portlet_param_16.doStartTag();
    if (_jspx_th_portlet_param_16.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      if (_jspx_resourceInjector != null) _jspx_resourceInjector.preDestroy(_jspx_th_portlet_param_16);
      _jspx_th_portlet_param_16.release();
      return true;
    }
    if (_jspx_resourceInjector != null) _jspx_resourceInjector.preDestroy(_jspx_th_portlet_param_16);
    _jspx_th_portlet_param_16.release();
    return false;
  }

  private boolean _jspx_meth_portlet_param_18(javax.servlet.jsp.tagext.JspTag _jspx_th_liferay$1portlet_renderURL_1, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  portlet:param
    com.liferay.taglib.util.ParamTag _jspx_th_portlet_param_18 = (_jspx_resourceInjector != null) ? _jspx_resourceInjector.createTagHandlerInstance(com.liferay.taglib.util.ParamTag.class) : new com.liferay.taglib.util.ParamTag();
    _jspx_th_portlet_param_18.setPageContext(_jspx_page_context);
    _jspx_th_portlet_param_18.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_liferay$1portlet_renderURL_1);
    _jspx_th_portlet_param_18.setName("tabs1");
    _jspx_th_portlet_param_18.setValue("roles");
    int _jspx_eval_portlet_param_18 = _jspx_th_portlet_param_18.doStartTag();
    if (_jspx_th_portlet_param_18.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      if (_jspx_resourceInjector != null) _jspx_resourceInjector.preDestroy(_jspx_th_portlet_param_18);
      _jspx_th_portlet_param_18.release();
      return true;
    }
    if (_jspx_resourceInjector != null) _jspx_resourceInjector.preDestroy(_jspx_th_portlet_param_18);
    _jspx_th_portlet_param_18.release();
    return false;
  }

  private boolean _jspx_meth_liferay$1ui_message_0(javax.servlet.jsp.tagext.JspTag _jspx_th_aui_a_0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  liferay-ui:message
    com.liferay.taglib.ui.MessageTag _jspx_th_liferay$1ui_message_0 = (_jspx_resourceInjector != null) ? _jspx_resourceInjector.createTagHandlerInstance(com.liferay.taglib.ui.MessageTag.class) : new com.liferay.taglib.ui.MessageTag();
    _jspx_th_liferay$1ui_message_0.setPageContext(_jspx_page_context);
    _jspx_th_liferay$1ui_message_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_a_0);
    _jspx_th_liferay$1ui_message_0.setKey("summary");
    int _jspx_eval_liferay$1ui_message_0 = _jspx_th_liferay$1ui_message_0.doStartTag();
    if (_jspx_th_liferay$1ui_message_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      if (_jspx_resourceInjector != null) _jspx_resourceInjector.preDestroy(_jspx_th_liferay$1ui_message_0);
      _jspx_th_liferay$1ui_message_0.release();
      return true;
    }
    if (_jspx_resourceInjector != null) _jspx_resourceInjector.preDestroy(_jspx_th_liferay$1ui_message_0);
    _jspx_th_liferay$1ui_message_0.release();
    return false;
  }

  private boolean _jspx_meth_liferay$1ui_message_1(javax.servlet.jsp.tagext.JspTag _jspx_th_clay_col_0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  liferay-ui:message
    com.liferay.taglib.ui.MessageTag _jspx_th_liferay$1ui_message_1 = (_jspx_resourceInjector != null) ? _jspx_resourceInjector.createTagHandlerInstance(com.liferay.taglib.ui.MessageTag.class) : new com.liferay.taglib.ui.MessageTag();
    _jspx_th_liferay$1ui_message_1.setPageContext(_jspx_page_context);
    _jspx_th_liferay$1ui_message_1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_clay_col_0);
    _jspx_th_liferay$1ui_message_1.setKey("control-panel");
    int _jspx_eval_liferay$1ui_message_1 = _jspx_th_liferay$1ui_message_1.doStartTag();
    if (_jspx_th_liferay$1ui_message_1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      if (_jspx_resourceInjector != null) _jspx_resourceInjector.preDestroy(_jspx_th_liferay$1ui_message_1);
      _jspx_th_liferay$1ui_message_1.release();
      return true;
    }
    if (_jspx_resourceInjector != null) _jspx_resourceInjector.preDestroy(_jspx_th_liferay$1ui_message_1);
    _jspx_th_liferay$1ui_message_1.release();
    return false;
  }

  private boolean _jspx_meth_aui_icon_0(javax.servlet.jsp.tagext.JspTag _jspx_th_clay_col_0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  aui:icon
    com.liferay.taglib.aui.IconTag _jspx_th_aui_icon_0 = (_jspx_resourceInjector != null) ? _jspx_resourceInjector.createTagHandlerInstance(com.liferay.taglib.aui.IconTag.class) : new com.liferay.taglib.aui.IconTag();
    _jspx_th_aui_icon_0.setPageContext(_jspx_page_context);
    _jspx_th_aui_icon_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_clay_col_0);
    _jspx_th_aui_icon_0.setImage("caret-right");
    _jspx_th_aui_icon_0.setMarkupView("lexicon");
    int _jspx_eval_aui_icon_0 = _jspx_th_aui_icon_0.doStartTag();
    if (_jspx_th_aui_icon_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      if (_jspx_resourceInjector != null) _jspx_resourceInjector.preDestroy(_jspx_th_aui_icon_0);
      _jspx_th_aui_icon_0.release();
      return true;
    }
    if (_jspx_resourceInjector != null) _jspx_resourceInjector.preDestroy(_jspx_th_aui_icon_0);
    _jspx_th_aui_icon_0.release();
    return false;
  }

  private boolean _jspx_meth_aui_icon_1(javax.servlet.jsp.tagext.JspTag _jspx_th_clay_col_0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  aui:icon
    com.liferay.taglib.aui.IconTag _jspx_th_aui_icon_1 = (_jspx_resourceInjector != null) ? _jspx_resourceInjector.createTagHandlerInstance(com.liferay.taglib.aui.IconTag.class) : new com.liferay.taglib.aui.IconTag();
    _jspx_th_aui_icon_1.setPageContext(_jspx_page_context);
    _jspx_th_aui_icon_1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_clay_col_0);
    _jspx_th_aui_icon_1.setImage("caret-bottom");
    _jspx_th_aui_icon_1.setMarkupView("lexicon");
    int _jspx_eval_aui_icon_1 = _jspx_th_aui_icon_1.doStartTag();
    if (_jspx_th_aui_icon_1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      if (_jspx_resourceInjector != null) _jspx_resourceInjector.preDestroy(_jspx_th_aui_icon_1);
      _jspx_th_aui_icon_1.release();
      return true;
    }
    if (_jspx_resourceInjector != null) _jspx_resourceInjector.preDestroy(_jspx_th_aui_icon_1);
    _jspx_th_aui_icon_1.release();
    return false;
  }

  private boolean _jspx_meth_liferay$1ui_message_2(javax.servlet.jsp.tagext.JspTag _jspx_th_aui_a_2, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  liferay-ui:message
    com.liferay.taglib.ui.MessageTag _jspx_th_liferay$1ui_message_2 = (_jspx_resourceInjector != null) ? _jspx_resourceInjector.createTagHandlerInstance(com.liferay.taglib.ui.MessageTag.class) : new com.liferay.taglib.ui.MessageTag();
    _jspx_th_liferay$1ui_message_2.setPageContext(_jspx_page_context);
    _jspx_th_liferay$1ui_message_2.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_a_2);
    _jspx_th_liferay$1ui_message_2.setKey("general-permissions");
    int _jspx_eval_liferay$1ui_message_2 = _jspx_th_liferay$1ui_message_2.doStartTag();
    if (_jspx_th_liferay$1ui_message_2.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      if (_jspx_resourceInjector != null) _jspx_resourceInjector.preDestroy(_jspx_th_liferay$1ui_message_2);
      _jspx_th_liferay$1ui_message_2.release();
      return true;
    }
    if (_jspx_resourceInjector != null) _jspx_resourceInjector.preDestroy(_jspx_th_liferay$1ui_message_2);
    _jspx_th_liferay$1ui_message_2.release();
    return false;
  }

  private boolean _jspx_meth_aui_icon_2(javax.servlet.jsp.tagext.JspTag _jspx_th_clay_col_0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  aui:icon
    com.liferay.taglib.aui.IconTag _jspx_th_aui_icon_2 = (_jspx_resourceInjector != null) ? _jspx_resourceInjector.createTagHandlerInstance(com.liferay.taglib.aui.IconTag.class) : new com.liferay.taglib.aui.IconTag();
    _jspx_th_aui_icon_2.setPageContext(_jspx_page_context);
    _jspx_th_aui_icon_2.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_clay_col_0);
    _jspx_th_aui_icon_2.setImage("caret-right");
    _jspx_th_aui_icon_2.setMarkupView("lexicon");
    int _jspx_eval_aui_icon_2 = _jspx_th_aui_icon_2.doStartTag();
    if (_jspx_th_aui_icon_2.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      if (_jspx_resourceInjector != null) _jspx_resourceInjector.preDestroy(_jspx_th_aui_icon_2);
      _jspx_th_aui_icon_2.release();
      return true;
    }
    if (_jspx_resourceInjector != null) _jspx_resourceInjector.preDestroy(_jspx_th_aui_icon_2);
    _jspx_th_aui_icon_2.release();
    return false;
  }

  private boolean _jspx_meth_aui_icon_3(javax.servlet.jsp.tagext.JspTag _jspx_th_clay_col_0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  aui:icon
    com.liferay.taglib.aui.IconTag _jspx_th_aui_icon_3 = (_jspx_resourceInjector != null) ? _jspx_resourceInjector.createTagHandlerInstance(com.liferay.taglib.aui.IconTag.class) : new com.liferay.taglib.aui.IconTag();
    _jspx_th_aui_icon_3.setPageContext(_jspx_page_context);
    _jspx_th_aui_icon_3.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_clay_col_0);
    _jspx_th_aui_icon_3.setImage("caret-bottom");
    _jspx_th_aui_icon_3.setMarkupView("lexicon");
    int _jspx_eval_aui_icon_3 = _jspx_th_aui_icon_3.doStartTag();
    if (_jspx_th_aui_icon_3.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      if (_jspx_resourceInjector != null) _jspx_resourceInjector.preDestroy(_jspx_th_aui_icon_3);
      _jspx_th_aui_icon_3.release();
      return true;
    }
    if (_jspx_resourceInjector != null) _jspx_resourceInjector.preDestroy(_jspx_th_aui_icon_3);
    _jspx_th_aui_icon_3.release();
    return false;
  }

  private boolean _jspx_meth_liferay$1ui_message_3(javax.servlet.jsp.tagext.JspTag _jspx_th_clay_col_0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  liferay-ui:message
    com.liferay.taglib.ui.MessageTag _jspx_th_liferay$1ui_message_3 = (_jspx_resourceInjector != null) ? _jspx_resourceInjector.createTagHandlerInstance(com.liferay.taglib.ui.MessageTag.class) : new com.liferay.taglib.ui.MessageTag();
    _jspx_th_liferay$1ui_message_3.setPageContext(_jspx_page_context);
    _jspx_th_liferay$1ui_message_3.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_clay_col_0);
    _jspx_th_liferay$1ui_message_3.setKey("commerce");
    int _jspx_eval_liferay$1ui_message_3 = _jspx_th_liferay$1ui_message_3.doStartTag();
    if (_jspx_th_liferay$1ui_message_3.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      if (_jspx_resourceInjector != null) _jspx_resourceInjector.preDestroy(_jspx_th_liferay$1ui_message_3);
      _jspx_th_liferay$1ui_message_3.release();
      return true;
    }
    if (_jspx_resourceInjector != null) _jspx_resourceInjector.preDestroy(_jspx_th_liferay$1ui_message_3);
    _jspx_th_liferay$1ui_message_3.release();
    return false;
  }

  private boolean _jspx_meth_aui_icon_4(javax.servlet.jsp.tagext.JspTag _jspx_th_clay_col_0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  aui:icon
    com.liferay.taglib.aui.IconTag _jspx_th_aui_icon_4 = (_jspx_resourceInjector != null) ? _jspx_resourceInjector.createTagHandlerInstance(com.liferay.taglib.aui.IconTag.class) : new com.liferay.taglib.aui.IconTag();
    _jspx_th_aui_icon_4.setPageContext(_jspx_page_context);
    _jspx_th_aui_icon_4.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_clay_col_0);
    _jspx_th_aui_icon_4.setImage("caret-right");
    _jspx_th_aui_icon_4.setMarkupView("lexicon");
    int _jspx_eval_aui_icon_4 = _jspx_th_aui_icon_4.doStartTag();
    if (_jspx_th_aui_icon_4.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      if (_jspx_resourceInjector != null) _jspx_resourceInjector.preDestroy(_jspx_th_aui_icon_4);
      _jspx_th_aui_icon_4.release();
      return true;
    }
    if (_jspx_resourceInjector != null) _jspx_resourceInjector.preDestroy(_jspx_th_aui_icon_4);
    _jspx_th_aui_icon_4.release();
    return false;
  }

  private boolean _jspx_meth_aui_icon_5(javax.servlet.jsp.tagext.JspTag _jspx_th_clay_col_0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  aui:icon
    com.liferay.taglib.aui.IconTag _jspx_th_aui_icon_5 = (_jspx_resourceInjector != null) ? _jspx_resourceInjector.createTagHandlerInstance(com.liferay.taglib.aui.IconTag.class) : new com.liferay.taglib.aui.IconTag();
    _jspx_th_aui_icon_5.setPageContext(_jspx_page_context);
    _jspx_th_aui_icon_5.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_clay_col_0);
    _jspx_th_aui_icon_5.setImage("caret-bottom");
    _jspx_th_aui_icon_5.setMarkupView("lexicon");
    int _jspx_eval_aui_icon_5 = _jspx_th_aui_icon_5.doStartTag();
    if (_jspx_th_aui_icon_5.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      if (_jspx_resourceInjector != null) _jspx_resourceInjector.preDestroy(_jspx_th_aui_icon_5);
      _jspx_th_aui_icon_5.release();
      return true;
    }
    if (_jspx_resourceInjector != null) _jspx_resourceInjector.preDestroy(_jspx_th_aui_icon_5);
    _jspx_th_aui_icon_5.release();
    return false;
  }

  private boolean _jspx_meth_aui_icon_6(javax.servlet.jsp.tagext.JspTag _jspx_th_clay_col_0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  aui:icon
    com.liferay.taglib.aui.IconTag _jspx_th_aui_icon_6 = (_jspx_resourceInjector != null) ? _jspx_resourceInjector.createTagHandlerInstance(com.liferay.taglib.aui.IconTag.class) : new com.liferay.taglib.aui.IconTag();
    _jspx_th_aui_icon_6.setPageContext(_jspx_page_context);
    _jspx_th_aui_icon_6.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_clay_col_0);
    _jspx_th_aui_icon_6.setImage("caret-right");
    _jspx_th_aui_icon_6.setMarkupView("lexicon");
    int _jspx_eval_aui_icon_6 = _jspx_th_aui_icon_6.doStartTag();
    if (_jspx_th_aui_icon_6.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      if (_jspx_resourceInjector != null) _jspx_resourceInjector.preDestroy(_jspx_th_aui_icon_6);
      _jspx_th_aui_icon_6.release();
      return true;
    }
    if (_jspx_resourceInjector != null) _jspx_resourceInjector.preDestroy(_jspx_th_aui_icon_6);
    _jspx_th_aui_icon_6.release();
    return false;
  }

  private boolean _jspx_meth_aui_icon_7(javax.servlet.jsp.tagext.JspTag _jspx_th_clay_col_0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  aui:icon
    com.liferay.taglib.aui.IconTag _jspx_th_aui_icon_7 = (_jspx_resourceInjector != null) ? _jspx_resourceInjector.createTagHandlerInstance(com.liferay.taglib.aui.IconTag.class) : new com.liferay.taglib.aui.IconTag();
    _jspx_th_aui_icon_7.setPageContext(_jspx_page_context);
    _jspx_th_aui_icon_7.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_clay_col_0);
    _jspx_th_aui_icon_7.setImage("caret-bottom");
    _jspx_th_aui_icon_7.setMarkupView("lexicon");
    int _jspx_eval_aui_icon_7 = _jspx_th_aui_icon_7.doStartTag();
    if (_jspx_th_aui_icon_7.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      if (_jspx_resourceInjector != null) _jspx_resourceInjector.preDestroy(_jspx_th_aui_icon_7);
      _jspx_th_aui_icon_7.release();
      return true;
    }
    if (_jspx_resourceInjector != null) _jspx_resourceInjector.preDestroy(_jspx_th_aui_icon_7);
    _jspx_th_aui_icon_7.release();
    return false;
  }

  private boolean _jspx_meth_liferay$1ui_message_4(javax.servlet.jsp.tagext.JspTag _jspx_th_clay_col_0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  liferay-ui:message
    com.liferay.taglib.ui.MessageTag _jspx_th_liferay$1ui_message_4 = (_jspx_resourceInjector != null) ? _jspx_resourceInjector.createTagHandlerInstance(com.liferay.taglib.ui.MessageTag.class) : new com.liferay.taglib.ui.MessageTag();
    _jspx_th_liferay$1ui_message_4.setPageContext(_jspx_page_context);
    _jspx_th_liferay$1ui_message_4.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_clay_col_0);
    _jspx_th_liferay$1ui_message_4.setKey("applications-menu");
    int _jspx_eval_liferay$1ui_message_4 = _jspx_th_liferay$1ui_message_4.doStartTag();
    if (_jspx_th_liferay$1ui_message_4.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      if (_jspx_resourceInjector != null) _jspx_resourceInjector.preDestroy(_jspx_th_liferay$1ui_message_4);
      _jspx_th_liferay$1ui_message_4.release();
      return true;
    }
    if (_jspx_resourceInjector != null) _jspx_resourceInjector.preDestroy(_jspx_th_liferay$1ui_message_4);
    _jspx_th_liferay$1ui_message_4.release();
    return false;
  }

  private boolean _jspx_meth_aui_icon_8(javax.servlet.jsp.tagext.JspTag _jspx_th_clay_col_0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  aui:icon
    com.liferay.taglib.aui.IconTag _jspx_th_aui_icon_8 = (_jspx_resourceInjector != null) ? _jspx_resourceInjector.createTagHandlerInstance(com.liferay.taglib.aui.IconTag.class) : new com.liferay.taglib.aui.IconTag();
    _jspx_th_aui_icon_8.setPageContext(_jspx_page_context);
    _jspx_th_aui_icon_8.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_clay_col_0);
    _jspx_th_aui_icon_8.setImage("caret-right");
    _jspx_th_aui_icon_8.setMarkupView("lexicon");
    int _jspx_eval_aui_icon_8 = _jspx_th_aui_icon_8.doStartTag();
    if (_jspx_th_aui_icon_8.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      if (_jspx_resourceInjector != null) _jspx_resourceInjector.preDestroy(_jspx_th_aui_icon_8);
      _jspx_th_aui_icon_8.release();
      return true;
    }
    if (_jspx_resourceInjector != null) _jspx_resourceInjector.preDestroy(_jspx_th_aui_icon_8);
    _jspx_th_aui_icon_8.release();
    return false;
  }

  private boolean _jspx_meth_aui_icon_9(javax.servlet.jsp.tagext.JspTag _jspx_th_clay_col_0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  aui:icon
    com.liferay.taglib.aui.IconTag _jspx_th_aui_icon_9 = (_jspx_resourceInjector != null) ? _jspx_resourceInjector.createTagHandlerInstance(com.liferay.taglib.aui.IconTag.class) : new com.liferay.taglib.aui.IconTag();
    _jspx_th_aui_icon_9.setPageContext(_jspx_page_context);
    _jspx_th_aui_icon_9.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_clay_col_0);
    _jspx_th_aui_icon_9.setImage("caret-bottom");
    _jspx_th_aui_icon_9.setMarkupView("lexicon");
    int _jspx_eval_aui_icon_9 = _jspx_th_aui_icon_9.doStartTag();
    if (_jspx_th_aui_icon_9.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      if (_jspx_resourceInjector != null) _jspx_resourceInjector.preDestroy(_jspx_th_aui_icon_9);
      _jspx_th_aui_icon_9.release();
      return true;
    }
    if (_jspx_resourceInjector != null) _jspx_resourceInjector.preDestroy(_jspx_th_aui_icon_9);
    _jspx_th_aui_icon_9.release();
    return false;
  }

  private boolean _jspx_meth_aui_icon_10(javax.servlet.jsp.tagext.JspTag _jspx_th_clay_col_0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  aui:icon
    com.liferay.taglib.aui.IconTag _jspx_th_aui_icon_10 = (_jspx_resourceInjector != null) ? _jspx_resourceInjector.createTagHandlerInstance(com.liferay.taglib.aui.IconTag.class) : new com.liferay.taglib.aui.IconTag();
    _jspx_th_aui_icon_10.setPageContext(_jspx_page_context);
    _jspx_th_aui_icon_10.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_clay_col_0);
    _jspx_th_aui_icon_10.setImage("caret-right");
    _jspx_th_aui_icon_10.setMarkupView("lexicon");
    int _jspx_eval_aui_icon_10 = _jspx_th_aui_icon_10.doStartTag();
    if (_jspx_th_aui_icon_10.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      if (_jspx_resourceInjector != null) _jspx_resourceInjector.preDestroy(_jspx_th_aui_icon_10);
      _jspx_th_aui_icon_10.release();
      return true;
    }
    if (_jspx_resourceInjector != null) _jspx_resourceInjector.preDestroy(_jspx_th_aui_icon_10);
    _jspx_th_aui_icon_10.release();
    return false;
  }

  private boolean _jspx_meth_aui_icon_11(javax.servlet.jsp.tagext.JspTag _jspx_th_clay_col_0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  aui:icon
    com.liferay.taglib.aui.IconTag _jspx_th_aui_icon_11 = (_jspx_resourceInjector != null) ? _jspx_resourceInjector.createTagHandlerInstance(com.liferay.taglib.aui.IconTag.class) : new com.liferay.taglib.aui.IconTag();
    _jspx_th_aui_icon_11.setPageContext(_jspx_page_context);
    _jspx_th_aui_icon_11.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_clay_col_0);
    _jspx_th_aui_icon_11.setImage("caret-bottom");
    _jspx_th_aui_icon_11.setMarkupView("lexicon");
    int _jspx_eval_aui_icon_11 = _jspx_th_aui_icon_11.doStartTag();
    if (_jspx_th_aui_icon_11.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      if (_jspx_resourceInjector != null) _jspx_resourceInjector.preDestroy(_jspx_th_aui_icon_11);
      _jspx_th_aui_icon_11.release();
      return true;
    }
    if (_jspx_resourceInjector != null) _jspx_resourceInjector.preDestroy(_jspx_th_aui_icon_11);
    _jspx_th_aui_icon_11.release();
    return false;
  }

  private boolean _jspx_meth_aui_icon_12(javax.servlet.jsp.tagext.JspTag _jspx_th_clay_col_0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  aui:icon
    com.liferay.taglib.aui.IconTag _jspx_th_aui_icon_12 = (_jspx_resourceInjector != null) ? _jspx_resourceInjector.createTagHandlerInstance(com.liferay.taglib.aui.IconTag.class) : new com.liferay.taglib.aui.IconTag();
    _jspx_th_aui_icon_12.setPageContext(_jspx_page_context);
    _jspx_th_aui_icon_12.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_clay_col_0);
    _jspx_th_aui_icon_12.setImage("caret-right");
    _jspx_th_aui_icon_12.setMarkupView("lexicon");
    int _jspx_eval_aui_icon_12 = _jspx_th_aui_icon_12.doStartTag();
    if (_jspx_th_aui_icon_12.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      if (_jspx_resourceInjector != null) _jspx_resourceInjector.preDestroy(_jspx_th_aui_icon_12);
      _jspx_th_aui_icon_12.release();
      return true;
    }
    if (_jspx_resourceInjector != null) _jspx_resourceInjector.preDestroy(_jspx_th_aui_icon_12);
    _jspx_th_aui_icon_12.release();
    return false;
  }

  private boolean _jspx_meth_aui_icon_13(javax.servlet.jsp.tagext.JspTag _jspx_th_clay_col_0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  aui:icon
    com.liferay.taglib.aui.IconTag _jspx_th_aui_icon_13 = (_jspx_resourceInjector != null) ? _jspx_resourceInjector.createTagHandlerInstance(com.liferay.taglib.aui.IconTag.class) : new com.liferay.taglib.aui.IconTag();
    _jspx_th_aui_icon_13.setPageContext(_jspx_page_context);
    _jspx_th_aui_icon_13.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_clay_col_0);
    _jspx_th_aui_icon_13.setImage("caret-bottom");
    _jspx_th_aui_icon_13.setMarkupView("lexicon");
    int _jspx_eval_aui_icon_13 = _jspx_th_aui_icon_13.doStartTag();
    if (_jspx_th_aui_icon_13.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      if (_jspx_resourceInjector != null) _jspx_resourceInjector.preDestroy(_jspx_th_aui_icon_13);
      _jspx_th_aui_icon_13.release();
      return true;
    }
    if (_jspx_resourceInjector != null) _jspx_resourceInjector.preDestroy(_jspx_th_aui_icon_13);
    _jspx_th_aui_icon_13.release();
    return false;
  }

  private boolean _jspx_meth_liferay$1ui_message_5(javax.servlet.jsp.tagext.JspTag _jspx_th_clay_col_0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  liferay-ui:message
    com.liferay.taglib.ui.MessageTag _jspx_th_liferay$1ui_message_5 = (_jspx_resourceInjector != null) ? _jspx_resourceInjector.createTagHandlerInstance(com.liferay.taglib.ui.MessageTag.class) : new com.liferay.taglib.ui.MessageTag();
    _jspx_th_liferay$1ui_message_5.setPageContext(_jspx_page_context);
    _jspx_th_liferay$1ui_message_5.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_clay_col_0);
    _jspx_th_liferay$1ui_message_5.setKey("site-and-asset-library-administration");
    int _jspx_eval_liferay$1ui_message_5 = _jspx_th_liferay$1ui_message_5.doStartTag();
    if (_jspx_th_liferay$1ui_message_5.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      if (_jspx_resourceInjector != null) _jspx_resourceInjector.preDestroy(_jspx_th_liferay$1ui_message_5);
      _jspx_th_liferay$1ui_message_5.release();
      return true;
    }
    if (_jspx_resourceInjector != null) _jspx_resourceInjector.preDestroy(_jspx_th_liferay$1ui_message_5);
    _jspx_th_liferay$1ui_message_5.release();
    return false;
  }

  private boolean _jspx_meth_aui_icon_14(javax.servlet.jsp.tagext.JspTag _jspx_th_clay_col_0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  aui:icon
    com.liferay.taglib.aui.IconTag _jspx_th_aui_icon_14 = (_jspx_resourceInjector != null) ? _jspx_resourceInjector.createTagHandlerInstance(com.liferay.taglib.aui.IconTag.class) : new com.liferay.taglib.aui.IconTag();
    _jspx_th_aui_icon_14.setPageContext(_jspx_page_context);
    _jspx_th_aui_icon_14.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_clay_col_0);
    _jspx_th_aui_icon_14.setImage("caret-right");
    _jspx_th_aui_icon_14.setMarkupView("lexicon");
    int _jspx_eval_aui_icon_14 = _jspx_th_aui_icon_14.doStartTag();
    if (_jspx_th_aui_icon_14.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      if (_jspx_resourceInjector != null) _jspx_resourceInjector.preDestroy(_jspx_th_aui_icon_14);
      _jspx_th_aui_icon_14.release();
      return true;
    }
    if (_jspx_resourceInjector != null) _jspx_resourceInjector.preDestroy(_jspx_th_aui_icon_14);
    _jspx_th_aui_icon_14.release();
    return false;
  }

  private boolean _jspx_meth_aui_icon_15(javax.servlet.jsp.tagext.JspTag _jspx_th_clay_col_0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  aui:icon
    com.liferay.taglib.aui.IconTag _jspx_th_aui_icon_15 = (_jspx_resourceInjector != null) ? _jspx_resourceInjector.createTagHandlerInstance(com.liferay.taglib.aui.IconTag.class) : new com.liferay.taglib.aui.IconTag();
    _jspx_th_aui_icon_15.setPageContext(_jspx_page_context);
    _jspx_th_aui_icon_15.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_clay_col_0);
    _jspx_th_aui_icon_15.setImage("caret-bottom");
    _jspx_th_aui_icon_15.setMarkupView("lexicon");
    int _jspx_eval_aui_icon_15 = _jspx_th_aui_icon_15.doStartTag();
    if (_jspx_th_aui_icon_15.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      if (_jspx_resourceInjector != null) _jspx_resourceInjector.preDestroy(_jspx_th_aui_icon_15);
      _jspx_th_aui_icon_15.release();
      return true;
    }
    if (_jspx_resourceInjector != null) _jspx_resourceInjector.preDestroy(_jspx_th_aui_icon_15);
    _jspx_th_aui_icon_15.release();
    return false;
  }

  private boolean _jspx_meth_aui_icon_16(javax.servlet.jsp.tagext.JspTag _jspx_th_clay_col_0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  aui:icon
    com.liferay.taglib.aui.IconTag _jspx_th_aui_icon_16 = (_jspx_resourceInjector != null) ? _jspx_resourceInjector.createTagHandlerInstance(com.liferay.taglib.aui.IconTag.class) : new com.liferay.taglib.aui.IconTag();
    _jspx_th_aui_icon_16.setPageContext(_jspx_page_context);
    _jspx_th_aui_icon_16.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_clay_col_0);
    _jspx_th_aui_icon_16.setImage("caret-right");
    _jspx_th_aui_icon_16.setMarkupView("lexicon");
    int _jspx_eval_aui_icon_16 = _jspx_th_aui_icon_16.doStartTag();
    if (_jspx_th_aui_icon_16.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      if (_jspx_resourceInjector != null) _jspx_resourceInjector.preDestroy(_jspx_th_aui_icon_16);
      _jspx_th_aui_icon_16.release();
      return true;
    }
    if (_jspx_resourceInjector != null) _jspx_resourceInjector.preDestroy(_jspx_th_aui_icon_16);
    _jspx_th_aui_icon_16.release();
    return false;
  }

  private boolean _jspx_meth_aui_icon_17(javax.servlet.jsp.tagext.JspTag _jspx_th_clay_col_0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  aui:icon
    com.liferay.taglib.aui.IconTag _jspx_th_aui_icon_17 = (_jspx_resourceInjector != null) ? _jspx_resourceInjector.createTagHandlerInstance(com.liferay.taglib.aui.IconTag.class) : new com.liferay.taglib.aui.IconTag();
    _jspx_th_aui_icon_17.setPageContext(_jspx_page_context);
    _jspx_th_aui_icon_17.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_clay_col_0);
    _jspx_th_aui_icon_17.setImage("caret-bottom");
    _jspx_th_aui_icon_17.setMarkupView("lexicon");
    int _jspx_eval_aui_icon_17 = _jspx_th_aui_icon_17.doStartTag();
    if (_jspx_th_aui_icon_17.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      if (_jspx_resourceInjector != null) _jspx_resourceInjector.preDestroy(_jspx_th_aui_icon_17);
      _jspx_th_aui_icon_17.release();
      return true;
    }
    if (_jspx_resourceInjector != null) _jspx_resourceInjector.preDestroy(_jspx_th_aui_icon_17);
    _jspx_th_aui_icon_17.release();
    return false;
  }

  private boolean _jspx_meth_liferay$1ui_message_6(javax.servlet.jsp.tagext.JspTag _jspx_th_clay_col_0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  liferay-ui:message
    com.liferay.taglib.ui.MessageTag _jspx_th_liferay$1ui_message_6 = (_jspx_resourceInjector != null) ? _jspx_resourceInjector.createTagHandlerInstance(com.liferay.taglib.ui.MessageTag.class) : new com.liferay.taglib.ui.MessageTag();
    _jspx_th_liferay$1ui_message_6.setPageContext(_jspx_page_context);
    _jspx_th_liferay$1ui_message_6.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_clay_col_0);
    _jspx_th_liferay$1ui_message_6.setKey("applications");
    int _jspx_eval_liferay$1ui_message_6 = _jspx_th_liferay$1ui_message_6.doStartTag();
    if (_jspx_th_liferay$1ui_message_6.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      if (_jspx_resourceInjector != null) _jspx_resourceInjector.preDestroy(_jspx_th_liferay$1ui_message_6);
      _jspx_th_liferay$1ui_message_6.release();
      return true;
    }
    if (_jspx_resourceInjector != null) _jspx_resourceInjector.preDestroy(_jspx_th_liferay$1ui_message_6);
    _jspx_th_liferay$1ui_message_6.release();
    return false;
  }

  private boolean _jspx_meth_aui_icon_18(javax.servlet.jsp.tagext.JspTag _jspx_th_clay_col_0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  aui:icon
    com.liferay.taglib.aui.IconTag _jspx_th_aui_icon_18 = (_jspx_resourceInjector != null) ? _jspx_resourceInjector.createTagHandlerInstance(com.liferay.taglib.aui.IconTag.class) : new com.liferay.taglib.aui.IconTag();
    _jspx_th_aui_icon_18.setPageContext(_jspx_page_context);
    _jspx_th_aui_icon_18.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_clay_col_0);
    _jspx_th_aui_icon_18.setImage("caret-right");
    _jspx_th_aui_icon_18.setMarkupView("lexicon");
    int _jspx_eval_aui_icon_18 = _jspx_th_aui_icon_18.doStartTag();
    if (_jspx_th_aui_icon_18.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      if (_jspx_resourceInjector != null) _jspx_resourceInjector.preDestroy(_jspx_th_aui_icon_18);
      _jspx_th_aui_icon_18.release();
      return true;
    }
    if (_jspx_resourceInjector != null) _jspx_resourceInjector.preDestroy(_jspx_th_aui_icon_18);
    _jspx_th_aui_icon_18.release();
    return false;
  }

  private boolean _jspx_meth_aui_icon_19(javax.servlet.jsp.tagext.JspTag _jspx_th_clay_col_0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  aui:icon
    com.liferay.taglib.aui.IconTag _jspx_th_aui_icon_19 = (_jspx_resourceInjector != null) ? _jspx_resourceInjector.createTagHandlerInstance(com.liferay.taglib.aui.IconTag.class) : new com.liferay.taglib.aui.IconTag();
    _jspx_th_aui_icon_19.setPageContext(_jspx_page_context);
    _jspx_th_aui_icon_19.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_clay_col_0);
    _jspx_th_aui_icon_19.setImage("caret-bottom");
    _jspx_th_aui_icon_19.setMarkupView("lexicon");
    int _jspx_eval_aui_icon_19 = _jspx_th_aui_icon_19.doStartTag();
    if (_jspx_th_aui_icon_19.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      if (_jspx_resourceInjector != null) _jspx_resourceInjector.preDestroy(_jspx_th_aui_icon_19);
      _jspx_th_aui_icon_19.release();
      return true;
    }
    if (_jspx_resourceInjector != null) _jspx_resourceInjector.preDestroy(_jspx_th_aui_icon_19);
    _jspx_th_aui_icon_19.release();
    return false;
  }

  private boolean _jspx_meth_liferay$1ui_message_7(javax.servlet.jsp.tagext.JspTag _jspx_th_clay_col_0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  liferay-ui:message
    com.liferay.taglib.ui.MessageTag _jspx_th_liferay$1ui_message_7 = (_jspx_resourceInjector != null) ? _jspx_resourceInjector.createTagHandlerInstance(com.liferay.taglib.ui.MessageTag.class) : new com.liferay.taglib.ui.MessageTag();
    _jspx_th_liferay$1ui_message_7.setPageContext(_jspx_page_context);
    _jspx_th_liferay$1ui_message_7.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_clay_col_0);
    _jspx_th_liferay$1ui_message_7.setKey("user");
    int _jspx_eval_liferay$1ui_message_7 = _jspx_th_liferay$1ui_message_7.doStartTag();
    if (_jspx_th_liferay$1ui_message_7.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      if (_jspx_resourceInjector != null) _jspx_resourceInjector.preDestroy(_jspx_th_liferay$1ui_message_7);
      _jspx_th_liferay$1ui_message_7.release();
      return true;
    }
    if (_jspx_resourceInjector != null) _jspx_resourceInjector.preDestroy(_jspx_th_liferay$1ui_message_7);
    _jspx_th_liferay$1ui_message_7.release();
    return false;
  }

  private boolean _jspx_meth_aui_icon_20(javax.servlet.jsp.tagext.JspTag _jspx_th_clay_col_0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  aui:icon
    com.liferay.taglib.aui.IconTag _jspx_th_aui_icon_20 = (_jspx_resourceInjector != null) ? _jspx_resourceInjector.createTagHandlerInstance(com.liferay.taglib.aui.IconTag.class) : new com.liferay.taglib.aui.IconTag();
    _jspx_th_aui_icon_20.setPageContext(_jspx_page_context);
    _jspx_th_aui_icon_20.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_clay_col_0);
    _jspx_th_aui_icon_20.setImage("caret-right");
    _jspx_th_aui_icon_20.setMarkupView("lexicon");
    int _jspx_eval_aui_icon_20 = _jspx_th_aui_icon_20.doStartTag();
    if (_jspx_th_aui_icon_20.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      if (_jspx_resourceInjector != null) _jspx_resourceInjector.preDestroy(_jspx_th_aui_icon_20);
      _jspx_th_aui_icon_20.release();
      return true;
    }
    if (_jspx_resourceInjector != null) _jspx_resourceInjector.preDestroy(_jspx_th_aui_icon_20);
    _jspx_th_aui_icon_20.release();
    return false;
  }

  private boolean _jspx_meth_aui_icon_21(javax.servlet.jsp.tagext.JspTag _jspx_th_clay_col_0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  aui:icon
    com.liferay.taglib.aui.IconTag _jspx_th_aui_icon_21 = (_jspx_resourceInjector != null) ? _jspx_resourceInjector.createTagHandlerInstance(com.liferay.taglib.aui.IconTag.class) : new com.liferay.taglib.aui.IconTag();
    _jspx_th_aui_icon_21.setPageContext(_jspx_page_context);
    _jspx_th_aui_icon_21.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_clay_col_0);
    _jspx_th_aui_icon_21.setImage("caret-bottom");
    _jspx_th_aui_icon_21.setMarkupView("lexicon");
    int _jspx_eval_aui_icon_21 = _jspx_th_aui_icon_21.doStartTag();
    if (_jspx_th_aui_icon_21.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      if (_jspx_resourceInjector != null) _jspx_resourceInjector.preDestroy(_jspx_th_aui_icon_21);
      _jspx_th_aui_icon_21.release();
      return true;
    }
    if (_jspx_resourceInjector != null) _jspx_resourceInjector.preDestroy(_jspx_th_aui_icon_21);
    _jspx_th_aui_icon_21.release();
    return false;
  }

  private boolean _jspx_meth_aui_script_0(PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  aui:script
    com.liferay.taglib.aui.ScriptTag _jspx_th_aui_script_0 = (_jspx_resourceInjector != null) ? _jspx_resourceInjector.createTagHandlerInstance(com.liferay.taglib.aui.ScriptTag.class) : new com.liferay.taglib.aui.ScriptTag();
    _jspx_th_aui_script_0.setPageContext(_jspx_page_context);
    _jspx_th_aui_script_0.setParent(null);
    int _jspx_eval_aui_script_0 = _jspx_th_aui_script_0.doStartTag();
    if (_jspx_eval_aui_script_0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
      if (_jspx_eval_aui_script_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
        out = _jspx_page_context.pushBody();
        _jspx_th_aui_script_0.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
        _jspx_th_aui_script_0.doInitBody();
      }
      do {
        out.write("\n");
        out.write("\tfunction ");
        if (_jspx_meth_portlet_namespace_5((javax.servlet.jsp.tagext.JspTag) _jspx_th_aui_script_0, _jspx_page_context))
          return true;
        out.write("selectOrganization(\n");
        out.write("\t\torganizationId,\n");
        out.write("\t\tgroupId,\n");
        out.write("\t\tname,\n");
        out.write("\t\ttype,\n");
        out.write("\t\ttarget\n");
        out.write("\t) {\n");
        out.write("\t\t");
        if (_jspx_meth_portlet_namespace_6((javax.servlet.jsp.tagext.JspTag) _jspx_th_aui_script_0, _jspx_page_context))
          return true;
        out.write("selectGroup(groupId, name, target);\n");
        out.write("\t}\n");
        int evalDoAfterBody = _jspx_th_aui_script_0.doAfterBody();
        if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
          break;
      } while (true);
      if (_jspx_eval_aui_script_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
        out = _jspx_page_context.popBody();
    }
    if (_jspx_th_aui_script_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      if (_jspx_resourceInjector != null) _jspx_resourceInjector.preDestroy(_jspx_th_aui_script_0);
      _jspx_th_aui_script_0.release();
      return true;
    }
    if (_jspx_resourceInjector != null) _jspx_resourceInjector.preDestroy(_jspx_th_aui_script_0);
    _jspx_th_aui_script_0.release();
    return false;
  }

  private boolean _jspx_meth_portlet_namespace_5(javax.servlet.jsp.tagext.JspTag _jspx_th_aui_script_0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  portlet:namespace
    com.liferay.taglib.portlet.NamespaceTag _jspx_th_portlet_namespace_5 = (_jspx_resourceInjector != null) ? _jspx_resourceInjector.createTagHandlerInstance(com.liferay.taglib.portlet.NamespaceTag.class) : new com.liferay.taglib.portlet.NamespaceTag();
    _jspx_th_portlet_namespace_5.setPageContext(_jspx_page_context);
    _jspx_th_portlet_namespace_5.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_script_0);
    int _jspx_eval_portlet_namespace_5 = _jspx_th_portlet_namespace_5.doStartTag();
    if (_jspx_th_portlet_namespace_5.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      if (_jspx_resourceInjector != null) _jspx_resourceInjector.preDestroy(_jspx_th_portlet_namespace_5);
      _jspx_th_portlet_namespace_5.release();
      return true;
    }
    if (_jspx_resourceInjector != null) _jspx_resourceInjector.preDestroy(_jspx_th_portlet_namespace_5);
    _jspx_th_portlet_namespace_5.release();
    return false;
  }

  private boolean _jspx_meth_portlet_namespace_6(javax.servlet.jsp.tagext.JspTag _jspx_th_aui_script_0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  portlet:namespace
    com.liferay.taglib.portlet.NamespaceTag _jspx_th_portlet_namespace_6 = (_jspx_resourceInjector != null) ? _jspx_resourceInjector.createTagHandlerInstance(com.liferay.taglib.portlet.NamespaceTag.class) : new com.liferay.taglib.portlet.NamespaceTag();
    _jspx_th_portlet_namespace_6.setPageContext(_jspx_page_context);
    _jspx_th_portlet_namespace_6.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_script_0);
    int _jspx_eval_portlet_namespace_6 = _jspx_th_portlet_namespace_6.doStartTag();
    if (_jspx_th_portlet_namespace_6.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      if (_jspx_resourceInjector != null) _jspx_resourceInjector.preDestroy(_jspx_th_portlet_namespace_6);
      _jspx_th_portlet_namespace_6.release();
      return true;
    }
    if (_jspx_resourceInjector != null) _jspx_resourceInjector.preDestroy(_jspx_th_portlet_namespace_6);
    _jspx_th_portlet_namespace_6.release();
    return false;
  }

  private boolean _jspx_meth_aui_script_1(PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  aui:script
    com.liferay.taglib.aui.ScriptTag _jspx_th_aui_script_1 = (_jspx_resourceInjector != null) ? _jspx_resourceInjector.createTagHandlerInstance(com.liferay.taglib.aui.ScriptTag.class) : new com.liferay.taglib.aui.ScriptTag();
    _jspx_th_aui_script_1.setPageContext(_jspx_page_context);
    _jspx_th_aui_script_1.setParent(null);
    _jspx_th_aui_script_1.setUse("aui-loading-mask-deprecated,aui-parse-content,aui-toggler,autocomplete-base,autocomplete-filters");
    int _jspx_eval_aui_script_1 = _jspx_th_aui_script_1.doStartTag();
    if (_jspx_eval_aui_script_1 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
      if (_jspx_eval_aui_script_1 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
        out = _jspx_page_context.pushBody();
        _jspx_th_aui_script_1.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
        _jspx_th_aui_script_1.doInitBody();
      }
      do {
        out.write("\n");
        out.write("\tvar AParseContent = A.Plugin.ParseContent;\n");
        out.write("\n");
        out.write("\tvar permissionNavigationDataContainer = A.one(\n");
        out.write("\t\t'#");
        if (_jspx_meth_portlet_namespace_7((javax.servlet.jsp.tagext.JspTag) _jspx_th_aui_script_1, _jspx_page_context))
          return true;
        out.write("permissionNavigationDataContainer'\n");
        out.write("\t);\n");
        out.write("\n");
        out.write("\tvar togglerDelegate;\n");
        out.write("\n");
        out.write("\tfunction createLiveSearch() {\n");
        out.write("\t\tvar instance = this;\n");
        out.write("\n");
        out.write("\t\tvar PermissionNavigationSearch = A.Component.create({\n");
        out.write("\t\t\tAUGMENTS: [A.AutoCompleteBase],\n");
        out.write("\n");
        out.write("\t\t\tEXTENDS: A.Base,\n");
        out.write("\n");
        out.write("\t\t\tNAME: 'searchpermissioNnavigation',\n");
        out.write("\n");
        out.write("\t\t\tprototype: {\n");
        out.write("\t\t\t\tinitializer: function () {\n");
        out.write("\t\t\t\t\tvar instance = this;\n");
        out.write("\n");
        out.write("\t\t\t\t\tinstance._bindUIACBase();\n");
        out.write("\t\t\t\t\tinstance._syncUIACBase();\n");
        out.write("\t\t\t\t},\n");
        out.write("\t\t\t},\n");
        out.write("\t\t});\n");
        out.write("\n");
        out.write("\t\tvar getItems = function () {\n");
        out.write("\t\t\tvar results = [];\n");
        out.write("\n");
        out.write("\t\t\tpermissionNavigationItems.each(function (item, index, collection) {\n");
        out.write("\t\t\t\tresults.push({\n");
        out.write("\t\t\t\t\tdata: item.text().trim(),\n");
        out.write("\t\t\t\t\tnode: item,\n");
        out.write("\t\t\t\t});\n");
        out.write("\t\t\t});\n");
        out.write("\n");
        out.write("\t\t\treturn results;\n");
        out.write("\t\t};\n");
        out.write("\n");
        out.write("\t\tvar getNoResultsNode = function () {\n");
        out.write("\t\t\tif (!noResultsNode) {\n");
        out.write("\t\t\t\tnoResultsNode = A.Node.create(\n");
        out.write("\t\t\t\t\t'<div class=\"alert\">");
        if (_jspx_meth_liferay$1ui_message_8((javax.servlet.jsp.tagext.JspTag) _jspx_th_aui_script_1, _jspx_page_context))
          return true;
        out.write("</div>'\n");
        out.write("\t\t\t\t);\n");
        out.write("\t\t\t}\n");
        out.write("\n");
        out.write("\t\t\treturn noResultsNode;\n");
        out.write("\t\t};\n");
        out.write("\n");
        out.write("\t\tvar permissionNavigationItems = permissionNavigationDataContainer.all(\n");
        out.write("\t\t\t'.permission-navigation-item-container'\n");
        out.write("\t\t);\n");
        out.write("\n");
        out.write("\t\tvar permissionNavigationSectionsNode = permissionNavigationDataContainer.all(\n");
        out.write("\t\t\t'.permission-navigation-section'\n");
        out.write("\t\t);\n");
        out.write("\n");
        out.write("\t\tvar noResultsNode;\n");
        out.write("\n");
        out.write("\t\tvar permissionNavigationSearch = new PermissionNavigationSearch({\n");
        out.write("\t\t\tinputNode: '#");
        if (_jspx_meth_portlet_namespace_8((javax.servlet.jsp.tagext.JspTag) _jspx_th_aui_script_1, _jspx_page_context))
          return true;
        out.write("permissionNavigationSearch',\n");
        out.write("\t\t\tminQueryLength: 0,\n");
        out.write("\t\t\tnodes: '.permission-navigation-item-container',\n");
        out.write("\t\t\tresultFilters: 'subWordMatch',\n");
        out.write("\t\t\tresultTextLocator: 'data',\n");
        out.write("\t\t\tsource: getItems(),\n");
        out.write("\t\t});\n");
        out.write("\n");
        out.write("\t\tpermissionNavigationSearch.on('query', function (event) {\n");
        out.write("\t\t\tif (event.query) {\n");
        out.write("\t\t\t\ttogglerDelegate.expandAll();\n");
        out.write("\t\t\t}\n");
        out.write("\t\t\telse {\n");
        out.write("\t\t\t\ttogglerDelegate.collapseAll();\n");
        out.write("\t\t\t}\n");
        out.write("\t\t});\n");
        out.write("\n");
        out.write("\t\tpermissionNavigationSearch.on('results', function (event) {\n");
        out.write("\t\t\tpermissionNavigationItems.each(function (item, index, collection) {\n");
        out.write("\t\t\t\titem.addClass('hide');\n");
        out.write("\t\t\t});\n");
        out.write("\n");
        out.write("\t\t\tevent.results.forEach(function (item, index) {\n");
        out.write("\t\t\t\titem.raw.node.removeClass('hide');\n");
        out.write("\t\t\t});\n");
        out.write("\n");
        out.write("\t\t\tvar foundVisibleSection;\n");
        out.write("\n");
        out.write("\t\t\tpermissionNavigationSectionsNode.each(function (\n");
        out.write("\t\t\t\titem,\n");
        out.write("\t\t\t\tindex,\n");
        out.write("\t\t\t\tcollection\n");
        out.write("\t\t\t) {\n");
        out.write("\t\t\t\tvar action = 'addClass';\n");
        out.write("\n");
        out.write("\t\t\t\tvar visibleItem = item.one(\n");
        out.write("\t\t\t\t\t'.permission-navigation-item-container:not(.hide)'\n");
        out.write("\t\t\t\t);\n");
        out.write("\n");
        out.write("\t\t\t\tif (visibleItem) {\n");
        out.write("\t\t\t\t\taction = 'removeClass';\n");
        out.write("\n");
        out.write("\t\t\t\t\tfoundVisibleSection = true;\n");
        out.write("\t\t\t\t}\n");
        out.write("\n");
        out.write("\t\t\t\titem[action]('hide');\n");
        out.write("\t\t\t});\n");
        out.write("\n");
        out.write("\t\t\tvar noResultsNode = getNoResultsNode();\n");
        out.write("\n");
        out.write("\t\t\tif (foundVisibleSection) {\n");
        out.write("\t\t\t\tnoResultsNode.remove();\n");
        out.write("\t\t\t}\n");
        out.write("\t\t\telse {\n");
        out.write("\t\t\t\tpermissionNavigationDataContainer.appendChild(noResultsNode);\n");
        out.write("\t\t\t}\n");
        out.write("\t\t});\n");
        out.write("\t}\n");
        out.write("\n");
        out.write("\tvar originalSelectedValues = [];\n");
        out.write("\n");
        out.write("\tfunction processNavigationLinks() {\n");
        out.write("\t\tvar permissionContainerNode = A.one(\n");
        out.write("\t\t\t'#");
        if (_jspx_meth_portlet_namespace_9((javax.servlet.jsp.tagext.JspTag) _jspx_th_aui_script_1, _jspx_page_context))
          return true;
        out.write("permissionContainer'\n");
        out.write("\t\t);\n");
        out.write("\n");
        out.write("\t\tvar permissionContentContainerNode = permissionContainerNode.one(\n");
        out.write("\t\t\t'#");
        if (_jspx_meth_portlet_namespace_10((javax.servlet.jsp.tagext.JspTag) _jspx_th_aui_script_1, _jspx_page_context))
          return true;
        out.write("permissionContentContainer'\n");
        out.write("\t\t);\n");
        out.write("\n");
        out.write("\t\tpermissionContainerNode.delegate(\n");
        out.write("\t\t\t'click',\n");
        out.write("\t\t\tfunction (event) {\n");
        out.write("\t\t\t\tevent.preventDefault();\n");
        out.write("\n");
        out.write("\t\t\t\tvar href = event.currentTarget.attr('data-resource-href');\n");
        out.write("\n");
        out.write("\t\t\t\thref = Liferay.Util.addParams('p_p_isolated=true', href);\n");
        out.write("\n");
        out.write("\t\t\t\tpermissionContentContainerNode.plug(A.LoadingMask);\n");
        out.write("\n");
        out.write("\t\t\t\tpermissionContentContainerNode.loadingmask.show();\n");
        out.write("\n");
        out.write("\t\t\t\tpermissionContentContainerNode.unplug(AParseContent);\n");
        out.write("\n");
        out.write("\t\t\t\tLiferay.Util.fetch(href)\n");
        out.write("\t\t\t\t\t.then(function (response) {\n");
        out.write("\t\t\t\t\t\tif (response.status === 401) {\n");
        out.write("\t\t\t\t\t\t\twindow.location.reload();\n");
        out.write("\t\t\t\t\t\t}\n");
        out.write("\t\t\t\t\t\telse if (response.ok) {\n");
        out.write("\t\t\t\t\t\t\treturn response.text();\n");
        out.write("\t\t\t\t\t\t}\n");
        out.write("\t\t\t\t\t\telse {\n");
        out.write("\t\t\t\t\t\t\tthrow new Error(\n");
        out.write("\t\t\t\t\t\t\t\t'");
        if (_jspx_meth_liferay$1ui_message_9((javax.servlet.jsp.tagext.JspTag) _jspx_th_aui_script_1, _jspx_page_context))
          return true;
        out.write("'\n");
        out.write("\t\t\t\t\t\t\t);\n");
        out.write("\t\t\t\t\t\t}\n");
        out.write("\t\t\t\t\t})\n");
        out.write("\t\t\t\t\t.then(function (response) {\n");
        out.write("\t\t\t\t\t\tpermissionContentContainerNode.loadingmask.hide();\n");
        out.write("\n");
        out.write("\t\t\t\t\t\tpermissionContentContainerNode.unplug(A.LoadingMask);\n");
        out.write("\n");
        out.write("\t\t\t\t\t\tpermissionContentContainerNode.plug(AParseContent);\n");
        out.write("\n");
        out.write("\t\t\t\t\t\tpermissionContentContainerNode.empty();\n");
        out.write("\n");
        out.write("\t\t\t\t\t\tpermissionContentContainerNode.setContent(response);\n");
        out.write("\n");
        out.write("\t\t\t\t\t\tvar checkedNodes = permissionContentContainerNode.all(\n");
        out.write("\t\t\t\t\t\t\t':checked'\n");
        out.write("\t\t\t\t\t\t);\n");
        out.write("\n");
        out.write("\t\t\t\t\t\toriginalSelectedValues = checkedNodes.val();\n");
        out.write("\n");
        out.write("\t\t\t\t\t\tA.all('.permission-navigation-link').removeClass('active');\n");
        out.write("\n");
        out.write("\t\t\t\t\t\tevent.currentTarget.addClass('active');\n");
        out.write("\t\t\t\t\t})\n");
        out.write("\t\t\t\t\t.catch(function (error) {\n");
        out.write("\t\t\t\t\t\tpermissionContentContainerNode.loadingmask.hide();\n");
        out.write("\n");
        out.write("\t\t\t\t\t\tpermissionContentContainerNode.unplug(A.LoadingMask);\n");
        out.write("\n");
        out.write("\t\t\t\t\t\tLiferay.Util.openToast({\n");
        out.write("\t\t\t\t\t\t\tmessage: error.message,\n");
        out.write("\t\t\t\t\t\t\ttype: 'warning',\n");
        out.write("\t\t\t\t\t\t});\n");
        out.write("\t\t\t\t\t});\n");
        out.write("\t\t\t},\n");
        out.write("\t\t\t'.permission-navigation-link'\n");
        out.write("\t\t);\n");
        out.write("\t}\n");
        out.write("\n");
        out.write("\tfunction processTargetCheckboxes() {\n");
        out.write("\t\tvar permissionContainerNode = A.one(\n");
        out.write("\t\t\t'#");
        if (_jspx_meth_portlet_namespace_11((javax.servlet.jsp.tagext.JspTag) _jspx_th_aui_script_1, _jspx_page_context))
          return true;
        out.write("permissionContainer'\n");
        out.write("\t\t);\n");
        out.write("\n");
        out.write("\t\tpermissionContainerNode.delegate(\n");
        out.write("\t\t\t'change',\n");
        out.write("\t\t\tfunction (event) {\n");
        out.write("\t\t\t\tvar unselectedTargetsNode = permissionContainerNode.one(\n");
        out.write("\t\t\t\t\t'#");
        if (_jspx_meth_portlet_namespace_12((javax.servlet.jsp.tagext.JspTag) _jspx_th_aui_script_1, _jspx_page_context))
          return true;
        out.write("unselectedTargets'\n");
        out.write("\t\t\t\t);\n");
        out.write("\n");
        out.write("\t\t\t\tvar unselectedTargets = unselectedTargetsNode.val().split(',');\n");
        out.write("\n");
        out.write("\t\t\t\tvar form = A.one(document.");
        if (_jspx_meth_portlet_namespace_13((javax.servlet.jsp.tagext.JspTag) _jspx_th_aui_script_1, _jspx_page_context))
          return true;
        out.write("fm);\n");
        out.write("\n");
        out.write("\t\t\t\tform.all('input[type=checkbox]').each(function (item, index) {\n");
        out.write("\t\t\t\t\tvar checkbox = A.one(item);\n");
        out.write("\n");
        out.write("\t\t\t\t\tvar value = checkbox.val();\n");
        out.write("\n");
        out.write("\t\t\t\t\tif (checkbox.get('checked')) {\n");
        out.write("\t\t\t\t\t\tvar unselectedTargetIndex = unselectedTargets.indexOf(\n");
        out.write("\t\t\t\t\t\t\tvalue\n");
        out.write("\t\t\t\t\t\t);\n");
        out.write("\n");
        out.write("\t\t\t\t\t\tif (unselectedTargetIndex != -1) {\n");
        out.write("\t\t\t\t\t\t\tunselectedTargets.splice(unselectedTargetIndex, 1);\n");
        out.write("\t\t\t\t\t\t}\n");
        out.write("\t\t\t\t\t}\n");
        out.write("\t\t\t\t\telse if (originalSelectedValues.indexOf(value) != -1) {\n");
        out.write("\t\t\t\t\t\tunselectedTargets.push(value);\n");
        out.write("\t\t\t\t\t}\n");
        out.write("\t\t\t\t});\n");
        out.write("\n");
        out.write("\t\t\t\tunselectedTargetsNode.val(unselectedTargets.join(','));\n");
        out.write("\t\t\t},\n");
        out.write("\t\t\t':checkbox'\n");
        out.write("\t\t);\n");
        out.write("\t}\n");
        out.write("\n");
        out.write("\tA.on('domready', function (event) {\n");
        out.write("\t\ttogglerDelegate = new A.TogglerDelegate({\n");
        out.write("\t\t\tcontainer: ");
        if (_jspx_meth_portlet_namespace_14((javax.servlet.jsp.tagext.JspTag) _jspx_th_aui_script_1, _jspx_page_context))
          return true;
        out.write("permissionNavigationDataContainer,\n");
        out.write("\t\t\tcontent: '.permission-navigation-item-content',\n");
        out.write("\t\t\theader: '.permission-navigation-item-header',\n");
        out.write("\t\t});\n");
        out.write("\n");
        out.write("\t\tcreateLiveSearch();\n");
        out.write("\t\tprocessNavigationLinks();\n");
        out.write("\t\tprocessTargetCheckboxes();\n");
        out.write("\t});\n");
        int evalDoAfterBody = _jspx_th_aui_script_1.doAfterBody();
        if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
          break;
      } while (true);
      if (_jspx_eval_aui_script_1 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
        out = _jspx_page_context.popBody();
    }
    if (_jspx_th_aui_script_1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      if (_jspx_resourceInjector != null) _jspx_resourceInjector.preDestroy(_jspx_th_aui_script_1);
      _jspx_th_aui_script_1.release();
      return true;
    }
    if (_jspx_resourceInjector != null) _jspx_resourceInjector.preDestroy(_jspx_th_aui_script_1);
    _jspx_th_aui_script_1.release();
    return false;
  }

  private boolean _jspx_meth_portlet_namespace_7(javax.servlet.jsp.tagext.JspTag _jspx_th_aui_script_1, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  portlet:namespace
    com.liferay.taglib.portlet.NamespaceTag _jspx_th_portlet_namespace_7 = (_jspx_resourceInjector != null) ? _jspx_resourceInjector.createTagHandlerInstance(com.liferay.taglib.portlet.NamespaceTag.class) : new com.liferay.taglib.portlet.NamespaceTag();
    _jspx_th_portlet_namespace_7.setPageContext(_jspx_page_context);
    _jspx_th_portlet_namespace_7.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_script_1);
    int _jspx_eval_portlet_namespace_7 = _jspx_th_portlet_namespace_7.doStartTag();
    if (_jspx_th_portlet_namespace_7.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      if (_jspx_resourceInjector != null) _jspx_resourceInjector.preDestroy(_jspx_th_portlet_namespace_7);
      _jspx_th_portlet_namespace_7.release();
      return true;
    }
    if (_jspx_resourceInjector != null) _jspx_resourceInjector.preDestroy(_jspx_th_portlet_namespace_7);
    _jspx_th_portlet_namespace_7.release();
    return false;
  }

  private boolean _jspx_meth_liferay$1ui_message_8(javax.servlet.jsp.tagext.JspTag _jspx_th_aui_script_1, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  liferay-ui:message
    com.liferay.taglib.ui.MessageTag _jspx_th_liferay$1ui_message_8 = (_jspx_resourceInjector != null) ? _jspx_resourceInjector.createTagHandlerInstance(com.liferay.taglib.ui.MessageTag.class) : new com.liferay.taglib.ui.MessageTag();
    _jspx_th_liferay$1ui_message_8.setPageContext(_jspx_page_context);
    _jspx_th_liferay$1ui_message_8.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_script_1);
    _jspx_th_liferay$1ui_message_8.setKey("there-are-no-results");
    int _jspx_eval_liferay$1ui_message_8 = _jspx_th_liferay$1ui_message_8.doStartTag();
    if (_jspx_th_liferay$1ui_message_8.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      if (_jspx_resourceInjector != null) _jspx_resourceInjector.preDestroy(_jspx_th_liferay$1ui_message_8);
      _jspx_th_liferay$1ui_message_8.release();
      return true;
    }
    if (_jspx_resourceInjector != null) _jspx_resourceInjector.preDestroy(_jspx_th_liferay$1ui_message_8);
    _jspx_th_liferay$1ui_message_8.release();
    return false;
  }

  private boolean _jspx_meth_portlet_namespace_8(javax.servlet.jsp.tagext.JspTag _jspx_th_aui_script_1, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  portlet:namespace
    com.liferay.taglib.portlet.NamespaceTag _jspx_th_portlet_namespace_8 = (_jspx_resourceInjector != null) ? _jspx_resourceInjector.createTagHandlerInstance(com.liferay.taglib.portlet.NamespaceTag.class) : new com.liferay.taglib.portlet.NamespaceTag();
    _jspx_th_portlet_namespace_8.setPageContext(_jspx_page_context);
    _jspx_th_portlet_namespace_8.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_script_1);
    int _jspx_eval_portlet_namespace_8 = _jspx_th_portlet_namespace_8.doStartTag();
    if (_jspx_th_portlet_namespace_8.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      if (_jspx_resourceInjector != null) _jspx_resourceInjector.preDestroy(_jspx_th_portlet_namespace_8);
      _jspx_th_portlet_namespace_8.release();
      return true;
    }
    if (_jspx_resourceInjector != null) _jspx_resourceInjector.preDestroy(_jspx_th_portlet_namespace_8);
    _jspx_th_portlet_namespace_8.release();
    return false;
  }

  private boolean _jspx_meth_portlet_namespace_9(javax.servlet.jsp.tagext.JspTag _jspx_th_aui_script_1, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  portlet:namespace
    com.liferay.taglib.portlet.NamespaceTag _jspx_th_portlet_namespace_9 = (_jspx_resourceInjector != null) ? _jspx_resourceInjector.createTagHandlerInstance(com.liferay.taglib.portlet.NamespaceTag.class) : new com.liferay.taglib.portlet.NamespaceTag();
    _jspx_th_portlet_namespace_9.setPageContext(_jspx_page_context);
    _jspx_th_portlet_namespace_9.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_script_1);
    int _jspx_eval_portlet_namespace_9 = _jspx_th_portlet_namespace_9.doStartTag();
    if (_jspx_th_portlet_namespace_9.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      if (_jspx_resourceInjector != null) _jspx_resourceInjector.preDestroy(_jspx_th_portlet_namespace_9);
      _jspx_th_portlet_namespace_9.release();
      return true;
    }
    if (_jspx_resourceInjector != null) _jspx_resourceInjector.preDestroy(_jspx_th_portlet_namespace_9);
    _jspx_th_portlet_namespace_9.release();
    return false;
  }

  private boolean _jspx_meth_portlet_namespace_10(javax.servlet.jsp.tagext.JspTag _jspx_th_aui_script_1, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  portlet:namespace
    com.liferay.taglib.portlet.NamespaceTag _jspx_th_portlet_namespace_10 = (_jspx_resourceInjector != null) ? _jspx_resourceInjector.createTagHandlerInstance(com.liferay.taglib.portlet.NamespaceTag.class) : new com.liferay.taglib.portlet.NamespaceTag();
    _jspx_th_portlet_namespace_10.setPageContext(_jspx_page_context);
    _jspx_th_portlet_namespace_10.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_script_1);
    int _jspx_eval_portlet_namespace_10 = _jspx_th_portlet_namespace_10.doStartTag();
    if (_jspx_th_portlet_namespace_10.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      if (_jspx_resourceInjector != null) _jspx_resourceInjector.preDestroy(_jspx_th_portlet_namespace_10);
      _jspx_th_portlet_namespace_10.release();
      return true;
    }
    if (_jspx_resourceInjector != null) _jspx_resourceInjector.preDestroy(_jspx_th_portlet_namespace_10);
    _jspx_th_portlet_namespace_10.release();
    return false;
  }

  private boolean _jspx_meth_liferay$1ui_message_9(javax.servlet.jsp.tagext.JspTag _jspx_th_aui_script_1, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  liferay-ui:message
    com.liferay.taglib.ui.MessageTag _jspx_th_liferay$1ui_message_9 = (_jspx_resourceInjector != null) ? _jspx_resourceInjector.createTagHandlerInstance(com.liferay.taglib.ui.MessageTag.class) : new com.liferay.taglib.ui.MessageTag();
    _jspx_th_liferay$1ui_message_9.setPageContext(_jspx_page_context);
    _jspx_th_liferay$1ui_message_9.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_script_1);
    _jspx_th_liferay$1ui_message_9.setKey("sorry,-we-were-not-able-to-access-the-server");
    int _jspx_eval_liferay$1ui_message_9 = _jspx_th_liferay$1ui_message_9.doStartTag();
    if (_jspx_th_liferay$1ui_message_9.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      if (_jspx_resourceInjector != null) _jspx_resourceInjector.preDestroy(_jspx_th_liferay$1ui_message_9);
      _jspx_th_liferay$1ui_message_9.release();
      return true;
    }
    if (_jspx_resourceInjector != null) _jspx_resourceInjector.preDestroy(_jspx_th_liferay$1ui_message_9);
    _jspx_th_liferay$1ui_message_9.release();
    return false;
  }

  private boolean _jspx_meth_portlet_namespace_11(javax.servlet.jsp.tagext.JspTag _jspx_th_aui_script_1, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  portlet:namespace
    com.liferay.taglib.portlet.NamespaceTag _jspx_th_portlet_namespace_11 = (_jspx_resourceInjector != null) ? _jspx_resourceInjector.createTagHandlerInstance(com.liferay.taglib.portlet.NamespaceTag.class) : new com.liferay.taglib.portlet.NamespaceTag();
    _jspx_th_portlet_namespace_11.setPageContext(_jspx_page_context);
    _jspx_th_portlet_namespace_11.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_script_1);
    int _jspx_eval_portlet_namespace_11 = _jspx_th_portlet_namespace_11.doStartTag();
    if (_jspx_th_portlet_namespace_11.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      if (_jspx_resourceInjector != null) _jspx_resourceInjector.preDestroy(_jspx_th_portlet_namespace_11);
      _jspx_th_portlet_namespace_11.release();
      return true;
    }
    if (_jspx_resourceInjector != null) _jspx_resourceInjector.preDestroy(_jspx_th_portlet_namespace_11);
    _jspx_th_portlet_namespace_11.release();
    return false;
  }

  private boolean _jspx_meth_portlet_namespace_12(javax.servlet.jsp.tagext.JspTag _jspx_th_aui_script_1, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  portlet:namespace
    com.liferay.taglib.portlet.NamespaceTag _jspx_th_portlet_namespace_12 = (_jspx_resourceInjector != null) ? _jspx_resourceInjector.createTagHandlerInstance(com.liferay.taglib.portlet.NamespaceTag.class) : new com.liferay.taglib.portlet.NamespaceTag();
    _jspx_th_portlet_namespace_12.setPageContext(_jspx_page_context);
    _jspx_th_portlet_namespace_12.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_script_1);
    int _jspx_eval_portlet_namespace_12 = _jspx_th_portlet_namespace_12.doStartTag();
    if (_jspx_th_portlet_namespace_12.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      if (_jspx_resourceInjector != null) _jspx_resourceInjector.preDestroy(_jspx_th_portlet_namespace_12);
      _jspx_th_portlet_namespace_12.release();
      return true;
    }
    if (_jspx_resourceInjector != null) _jspx_resourceInjector.preDestroy(_jspx_th_portlet_namespace_12);
    _jspx_th_portlet_namespace_12.release();
    return false;
  }

  private boolean _jspx_meth_portlet_namespace_13(javax.servlet.jsp.tagext.JspTag _jspx_th_aui_script_1, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  portlet:namespace
    com.liferay.taglib.portlet.NamespaceTag _jspx_th_portlet_namespace_13 = (_jspx_resourceInjector != null) ? _jspx_resourceInjector.createTagHandlerInstance(com.liferay.taglib.portlet.NamespaceTag.class) : new com.liferay.taglib.portlet.NamespaceTag();
    _jspx_th_portlet_namespace_13.setPageContext(_jspx_page_context);
    _jspx_th_portlet_namespace_13.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_script_1);
    int _jspx_eval_portlet_namespace_13 = _jspx_th_portlet_namespace_13.doStartTag();
    if (_jspx_th_portlet_namespace_13.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      if (_jspx_resourceInjector != null) _jspx_resourceInjector.preDestroy(_jspx_th_portlet_namespace_13);
      _jspx_th_portlet_namespace_13.release();
      return true;
    }
    if (_jspx_resourceInjector != null) _jspx_resourceInjector.preDestroy(_jspx_th_portlet_namespace_13);
    _jspx_th_portlet_namespace_13.release();
    return false;
  }

  private boolean _jspx_meth_portlet_namespace_14(javax.servlet.jsp.tagext.JspTag _jspx_th_aui_script_1, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  portlet:namespace
    com.liferay.taglib.portlet.NamespaceTag _jspx_th_portlet_namespace_14 = (_jspx_resourceInjector != null) ? _jspx_resourceInjector.createTagHandlerInstance(com.liferay.taglib.portlet.NamespaceTag.class) : new com.liferay.taglib.portlet.NamespaceTag();
    _jspx_th_portlet_namespace_14.setPageContext(_jspx_page_context);
    _jspx_th_portlet_namespace_14.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_script_1);
    int _jspx_eval_portlet_namespace_14 = _jspx_th_portlet_namespace_14.doStartTag();
    if (_jspx_th_portlet_namespace_14.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      if (_jspx_resourceInjector != null) _jspx_resourceInjector.preDestroy(_jspx_th_portlet_namespace_14);
      _jspx_th_portlet_namespace_14.release();
      return true;
    }
    if (_jspx_resourceInjector != null) _jspx_resourceInjector.preDestroy(_jspx_th_portlet_namespace_14);
    _jspx_th_portlet_namespace_14.release();
    return false;
  }

  private boolean _jspx_meth_portlet_namespace_15(javax.servlet.jsp.tagext.JspTag _jspx_th_aui_script_2, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  portlet:namespace
    com.liferay.taglib.portlet.NamespaceTag _jspx_th_portlet_namespace_15 = (_jspx_resourceInjector != null) ? _jspx_resourceInjector.createTagHandlerInstance(com.liferay.taglib.portlet.NamespaceTag.class) : new com.liferay.taglib.portlet.NamespaceTag();
    _jspx_th_portlet_namespace_15.setPageContext(_jspx_page_context);
    _jspx_th_portlet_namespace_15.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_script_2);
    int _jspx_eval_portlet_namespace_15 = _jspx_th_portlet_namespace_15.doStartTag();
    if (_jspx_th_portlet_namespace_15.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      if (_jspx_resourceInjector != null) _jspx_resourceInjector.preDestroy(_jspx_th_portlet_namespace_15);
      _jspx_th_portlet_namespace_15.release();
      return true;
    }
    if (_jspx_resourceInjector != null) _jspx_resourceInjector.preDestroy(_jspx_th_portlet_namespace_15);
    _jspx_th_portlet_namespace_15.release();
    return false;
  }

  private boolean _jspx_meth_portlet_namespace_16(javax.servlet.jsp.tagext.JspTag _jspx_th_aui_script_2, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  portlet:namespace
    com.liferay.taglib.portlet.NamespaceTag _jspx_th_portlet_namespace_16 = (_jspx_resourceInjector != null) ? _jspx_resourceInjector.createTagHandlerInstance(com.liferay.taglib.portlet.NamespaceTag.class) : new com.liferay.taglib.portlet.NamespaceTag();
    _jspx_th_portlet_namespace_16.setPageContext(_jspx_page_context);
    _jspx_th_portlet_namespace_16.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_script_2);
    int _jspx_eval_portlet_namespace_16 = _jspx_th_portlet_namespace_16.doStartTag();
    if (_jspx_th_portlet_namespace_16.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      if (_jspx_resourceInjector != null) _jspx_resourceInjector.preDestroy(_jspx_th_portlet_namespace_16);
      _jspx_th_portlet_namespace_16.release();
      return true;
    }
    if (_jspx_resourceInjector != null) _jspx_resourceInjector.preDestroy(_jspx_th_portlet_namespace_16);
    _jspx_th_portlet_namespace_16.release();
    return false;
  }

  private boolean _jspx_meth_portlet_namespace_17(javax.servlet.jsp.tagext.JspTag _jspx_th_aui_script_2, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  portlet:namespace
    com.liferay.taglib.portlet.NamespaceTag _jspx_th_portlet_namespace_17 = (_jspx_resourceInjector != null) ? _jspx_resourceInjector.createTagHandlerInstance(com.liferay.taglib.portlet.NamespaceTag.class) : new com.liferay.taglib.portlet.NamespaceTag();
    _jspx_th_portlet_namespace_17.setPageContext(_jspx_page_context);
    _jspx_th_portlet_namespace_17.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_script_2);
    int _jspx_eval_portlet_namespace_17 = _jspx_th_portlet_namespace_17.doStartTag();
    if (_jspx_th_portlet_namespace_17.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      if (_jspx_resourceInjector != null) _jspx_resourceInjector.preDestroy(_jspx_th_portlet_namespace_17);
      _jspx_th_portlet_namespace_17.release();
      return true;
    }
    if (_jspx_resourceInjector != null) _jspx_resourceInjector.preDestroy(_jspx_th_portlet_namespace_17);
    _jspx_th_portlet_namespace_17.release();
    return false;
  }

  private boolean _jspx_meth_portlet_namespace_18(javax.servlet.jsp.tagext.JspTag _jspx_th_aui_script_2, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  portlet:namespace
    com.liferay.taglib.portlet.NamespaceTag _jspx_th_portlet_namespace_18 = (_jspx_resourceInjector != null) ? _jspx_resourceInjector.createTagHandlerInstance(com.liferay.taglib.portlet.NamespaceTag.class) : new com.liferay.taglib.portlet.NamespaceTag();
    _jspx_th_portlet_namespace_18.setPageContext(_jspx_page_context);
    _jspx_th_portlet_namespace_18.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_aui_script_2);
    int _jspx_eval_portlet_namespace_18 = _jspx_th_portlet_namespace_18.doStartTag();
    if (_jspx_th_portlet_namespace_18.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      if (_jspx_resourceInjector != null) _jspx_resourceInjector.preDestroy(_jspx_th_portlet_namespace_18);
      _jspx_th_portlet_namespace_18.release();
      return true;
    }
    if (_jspx_resourceInjector != null) _jspx_resourceInjector.preDestroy(_jspx_th_portlet_namespace_18);
    _jspx_th_portlet_namespace_18.release();
    return false;
  }
}
