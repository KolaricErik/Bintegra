package org.apache.jsp.render_005flayout_005fstructure;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;
import com.liferay.portal.kernel.json.JSONArray;
import com.liferay.portal.kernel.model.LayoutConstants;
import com.liferay.portal.kernel.model.LayoutSetBranch;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.HtmlUtil;
import com.liferay.portal.kernel.util.SessionClicks;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.util.PropsValues;
import com.liferay.portlet.layoutsadmin.util.LayoutsTreeUtil;
import com.liferay.taglib.aui.AUIUtil;
import com.liferay.taglib.ui.util.SessionTreeJSClicks;
import com.liferay.fragment.constants.FragmentActionKeys;
import com.liferay.fragment.model.FragmentEntryLink;
import com.liferay.fragment.renderer.DefaultFragmentRendererContext;
import com.liferay.fragment.renderer.FragmentRendererController;
import com.liferay.fragment.service.FragmentEntryLinkLocalServiceUtil;
import com.liferay.info.constants.InfoDisplayWebKeys;
import com.liferay.info.list.renderer.InfoListRenderer;
import com.liferay.layout.display.page.LayoutDisplayPageProvider;
import com.liferay.layout.display.page.constants.LayoutDisplayPageWebKeys;
import com.liferay.layout.responsive.ResponsiveLayoutStructureUtil;
import com.liferay.layout.taglib.internal.display.context.RenderLayoutStructureDisplayContext;
import com.liferay.layout.util.structure.CollectionStyledLayoutStructureItem;
import com.liferay.layout.util.structure.ColumnLayoutStructureItem;
import com.liferay.layout.util.structure.ContainerStyledLayoutStructureItem;
import com.liferay.layout.util.structure.DropZoneLayoutStructureItem;
import com.liferay.layout.util.structure.FragmentStyledLayoutStructureItem;
import com.liferay.layout.util.structure.LayoutStructure;
import com.liferay.layout.util.structure.LayoutStructureItem;
import com.liferay.layout.util.structure.RootLayoutStructureItem;
import com.liferay.layout.util.structure.RowStyledLayoutStructureItem;
import com.liferay.portal.kernel.layoutconfiguration.util.RuntimePageUtil;
import com.liferay.portal.kernel.model.LayoutTemplate;
import com.liferay.portal.kernel.model.LayoutTemplateConstants;
import com.liferay.portal.kernel.service.LayoutTemplateLocalServiceUtil;
import com.liferay.portal.kernel.template.StringTemplateResource;
import java.util.List;
import java.util.Objects;

public final class render_005flayout_005fstructure_jsp extends org.apache.jasper.runtime.HttpJspBase
    implements org.apache.jasper.runtime.JspSourceDependent {

  private static final JspFactory _jspxFactory = JspFactory.getDefaultFactory();

  private static java.util.List<String> _jspx_dependants;

  static {
    _jspx_dependants = new java.util.ArrayList<String>(2);
    _jspx_dependants.add("/render_layout_structure/init.jsp");
    _jspx_dependants.add("/init.jsp");
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
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write('\n');
      out.write('\n');

RenderLayoutStructureDisplayContext renderLayoutStructureDisplayContext = (RenderLayoutStructureDisplayContext)request.getAttribute(RenderLayoutStructureDisplayContext.class.getName());

LayoutStructure layoutStructure = renderLayoutStructureDisplayContext.getLayoutStructure();

List<String> childrenItemIds = (List<String>)request.getAttribute("render_layout_structure.jsp-childrenItemIds");

for (String childrenItemId : childrenItemIds) {
	LayoutStructureItem layoutStructureItem = layoutStructure.getLayoutStructureItem(childrenItemId);

      out.write("\n");
      out.write("\n");
      out.write("\t");
      out.write("\n");
      out.write("\t\t");
if (
 layoutStructureItem instanceof CollectionStyledLayoutStructureItem ) {
      out.write("\n");
      out.write("\n");
      out.write("\t\t\t");

			CollectionStyledLayoutStructureItem collectionStyledLayoutStructureItem = (CollectionStyledLayoutStructureItem)layoutStructureItem;

			InfoListRenderer<Object> infoListRenderer = (InfoListRenderer<Object>)renderLayoutStructureDisplayContext.getInfoListRenderer(collectionStyledLayoutStructureItem);
			
      out.write("\n");
      out.write("\n");
      out.write("\t\t\t<div class=\"");
      out.print( renderLayoutStructureDisplayContext.getCssClass(collectionStyledLayoutStructureItem) );
      out.write("\" style=\"");
      out.print( renderLayoutStructureDisplayContext.getStyle(collectionStyledLayoutStructureItem) );
      out.write("\">\n");
      out.write("\t\t\t\t");
      out.write("\n");
      out.write("\t\t\t\t\t");
if (
 infoListRenderer != null ) {
      out.write("\n");
      out.write("\n");
      out.write("\t\t\t\t\t\t");

						infoListRenderer.render(renderLayoutStructureDisplayContext.getCollection(collectionStyledLayoutStructureItem), renderLayoutStructureDisplayContext.getInfoListRendererContext(collectionStyledLayoutStructureItem.getListItemStyle(), collectionStyledLayoutStructureItem.getTemplateKey()));
						
      out.write("\n");
      out.write("\n");
      out.write("\t\t\t\t\t");
      out.write("\n");
      out.write("\t\t\t\t\t");
}
else {
      out.write("\n");
      out.write("\n");
      out.write("\t\t\t\t\t\t");

						LayoutDisplayPageProvider<?> currentLayoutDisplayPageProvider = (LayoutDisplayPageProvider<?>)request.getAttribute(LayoutDisplayPageWebKeys.LAYOUT_DISPLAY_PAGE_PROVIDER);

						try {
							request.setAttribute(LayoutDisplayPageWebKeys.LAYOUT_DISPLAY_PAGE_PROVIDER, renderLayoutStructureDisplayContext.getCollectionLayoutDisplayPageProvider(collectionStyledLayoutStructureItem));

							List<Object> collection = renderLayoutStructureDisplayContext.getCollection(collectionStyledLayoutStructureItem);

							int maxNumberOfItems = Math.min(collection.size(), collectionStyledLayoutStructureItem.getNumberOfItems());

							int numberOfRows = (int)Math.ceil((double)maxNumberOfItems / collectionStyledLayoutStructureItem.getNumberOfColumns());

							for (int i = 0; i < numberOfRows; i++) {
						
      out.write("\n");
      out.write("\n");
      out.write("\t\t\t\t\t\t\t");
      //  clay:row
      com.liferay.frontend.taglib.clay.servlet.taglib.RowTag _jspx_th_clay_row_0 = (_jspx_resourceInjector != null) ? _jspx_resourceInjector.createTagHandlerInstance(com.liferay.frontend.taglib.clay.servlet.taglib.RowTag.class) : new com.liferay.frontend.taglib.clay.servlet.taglib.RowTag();
      _jspx_th_clay_row_0.setPageContext(_jspx_page_context);
      _jspx_th_clay_row_0.setParent(null);
      int _jspx_eval_clay_row_0 = _jspx_th_clay_row_0.doStartTag();
      if (_jspx_eval_clay_row_0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
        out.write("\n");
        out.write("\n");
        out.write("\t\t\t\t\t\t\t\t");

								for (int j = 0; j < collectionStyledLayoutStructureItem.getNumberOfColumns(); j++) {
									int index = (i * collectionStyledLayoutStructureItem.getNumberOfColumns()) + j;

									if (index >= maxNumberOfItems) {
										break;
									}

									request.setAttribute(InfoDisplayWebKeys.INFO_LIST_DISPLAY_OBJECT, collection.get(index));
									request.setAttribute("render_layout_structure.jsp-childrenItemIds", layoutStructureItem.getChildrenItemIds());
								
        out.write("\n");
        out.write("\n");
        out.write("\t\t\t\t\t\t\t\t\t");
        //  clay:col
        com.liferay.frontend.taglib.clay.servlet.taglib.ColTag _jspx_th_clay_col_0 = (_jspx_resourceInjector != null) ? _jspx_resourceInjector.createTagHandlerInstance(com.liferay.frontend.taglib.clay.servlet.taglib.ColTag.class) : new com.liferay.frontend.taglib.clay.servlet.taglib.ColTag();
        _jspx_th_clay_col_0.setPageContext(_jspx_page_context);
        _jspx_th_clay_col_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_clay_row_0);
        _jspx_th_clay_col_0.setMd( String.valueOf(layoutStructure.getColumnSize(collectionStyledLayoutStructureItem.getNumberOfColumns() - 1, j)) );
        int _jspx_eval_clay_col_0 = _jspx_th_clay_col_0.doStartTag();
        if (_jspx_eval_clay_col_0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
          out.write("\n");
          out.write("\t\t\t\t\t\t\t\t\t\t");
          //  liferay-util:include
          com.liferay.taglib.util.IncludeTag _jspx_th_liferay$1util_include_0 = (_jspx_resourceInjector != null) ? _jspx_resourceInjector.createTagHandlerInstance(com.liferay.taglib.util.IncludeTag.class) : new com.liferay.taglib.util.IncludeTag();
          _jspx_th_liferay$1util_include_0.setPageContext(_jspx_page_context);
          _jspx_th_liferay$1util_include_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_clay_col_0);
          _jspx_th_liferay$1util_include_0.setPage("/render_layout_structure/render_layout_structure.jsp");
          _jspx_th_liferay$1util_include_0.setServletContext( application );
          int _jspx_eval_liferay$1util_include_0 = _jspx_th_liferay$1util_include_0.doStartTag();
          if (_jspx_th_liferay$1util_include_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
            if (_jspx_resourceInjector != null) _jspx_resourceInjector.preDestroy(_jspx_th_liferay$1util_include_0);
            _jspx_th_liferay$1util_include_0.release();
            return;
          }
          if (_jspx_resourceInjector != null) _jspx_resourceInjector.preDestroy(_jspx_th_liferay$1util_include_0);
          _jspx_th_liferay$1util_include_0.release();
          out.write("\n");
          out.write("\t\t\t\t\t\t\t\t\t");
        }
        if (_jspx_th_clay_col_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
          if (_jspx_resourceInjector != null) _jspx_resourceInjector.preDestroy(_jspx_th_clay_col_0);
          _jspx_th_clay_col_0.release();
          return;
        }
        if (_jspx_resourceInjector != null) _jspx_resourceInjector.preDestroy(_jspx_th_clay_col_0);
        _jspx_th_clay_col_0.release();
        out.write("\n");
        out.write("\n");
        out.write("\t\t\t\t\t\t\t\t");

								}
								
        out.write("\n");
        out.write("\n");
        out.write("\t\t\t\t\t\t\t");
      }
      if (_jspx_th_clay_row_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
        if (_jspx_resourceInjector != null) _jspx_resourceInjector.preDestroy(_jspx_th_clay_row_0);
        _jspx_th_clay_row_0.release();
        return;
      }
      if (_jspx_resourceInjector != null) _jspx_resourceInjector.preDestroy(_jspx_th_clay_row_0);
      _jspx_th_clay_row_0.release();
      out.write("\n");
      out.write("\n");
      out.write("\t\t\t\t\t\t");

							}
						}
						finally {
							request.removeAttribute(InfoDisplayWebKeys.INFO_LIST_DISPLAY_OBJECT);

							request.setAttribute(LayoutDisplayPageWebKeys.LAYOUT_DISPLAY_PAGE_PROVIDER, currentLayoutDisplayPageProvider);
						}
						
      out.write("\n");
      out.write("\n");
      out.write("\t\t\t\t\t");
      out.write("\n");
      out.write("\t\t\t\t");
}
      out.write("\n");
      out.write("\t\t\t</div>\n");
      out.write("\t\t");
      out.write("\n");
      out.write("\t\t");
}
else if (
 layoutStructureItem instanceof ColumnLayoutStructureItem ) {
      out.write("\n");
      out.write("\n");
      out.write("\t\t\t");

			ColumnLayoutStructureItem columnLayoutStructureItem = (ColumnLayoutStructureItem)layoutStructureItem;
			
      out.write("\n");
      out.write("\n");
      out.write("\t\t\t");
      //  clay:col
      com.liferay.frontend.taglib.clay.servlet.taglib.ColTag _jspx_th_clay_col_1 = (_jspx_resourceInjector != null) ? _jspx_resourceInjector.createTagHandlerInstance(com.liferay.frontend.taglib.clay.servlet.taglib.ColTag.class) : new com.liferay.frontend.taglib.clay.servlet.taglib.ColTag();
      _jspx_th_clay_col_1.setPageContext(_jspx_page_context);
      _jspx_th_clay_col_1.setParent(null);
      _jspx_th_clay_col_1.setCssClass( ResponsiveLayoutStructureUtil.getColumnCssClass(columnLayoutStructureItem) );
      int _jspx_eval_clay_col_1 = _jspx_th_clay_col_1.doStartTag();
      if (_jspx_eval_clay_col_1 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
        out.write("\n");
        out.write("\n");
        out.write("\t\t\t\t");

				request.setAttribute("render_layout_structure.jsp-childrenItemIds", layoutStructureItem.getChildrenItemIds());
				
        out.write("\n");
        out.write("\n");
        out.write("\t\t\t\t");
        //  liferay-util:include
        com.liferay.taglib.util.IncludeTag _jspx_th_liferay$1util_include_1 = (_jspx_resourceInjector != null) ? _jspx_resourceInjector.createTagHandlerInstance(com.liferay.taglib.util.IncludeTag.class) : new com.liferay.taglib.util.IncludeTag();
        _jspx_th_liferay$1util_include_1.setPageContext(_jspx_page_context);
        _jspx_th_liferay$1util_include_1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_clay_col_1);
        _jspx_th_liferay$1util_include_1.setPage("/render_layout_structure/render_layout_structure.jsp");
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
        out.write("\t\t\t");
      }
      if (_jspx_th_clay_col_1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
        if (_jspx_resourceInjector != null) _jspx_resourceInjector.preDestroy(_jspx_th_clay_col_1);
        _jspx_th_clay_col_1.release();
        return;
      }
      if (_jspx_resourceInjector != null) _jspx_resourceInjector.preDestroy(_jspx_th_clay_col_1);
      _jspx_th_clay_col_1.release();
      out.write("\n");
      out.write("\t\t");
      out.write("\n");
      out.write("\t\t");
}
else if (
 layoutStructureItem instanceof ContainerStyledLayoutStructureItem ) {
      out.write("\n");
      out.write("\n");
      out.write("\t\t\t");

			ContainerStyledLayoutStructureItem containerStyledLayoutStructureItem = (ContainerStyledLayoutStructureItem)layoutStructureItem;

			String containerLinkHref = renderLayoutStructureDisplayContext.getContainerLinkHref(containerStyledLayoutStructureItem, request.getAttribute(InfoDisplayWebKeys.INFO_LIST_DISPLAY_OBJECT));
			
      out.write("\n");
      out.write("\n");
      out.write("\t\t\t");
if (
 Validator.isNotNull(containerLinkHref) ) {
      out.write("\n");
      out.write("\t\t\t\t<a href=\"");
      out.print( containerLinkHref );
      out.write("\" style=\"color: inherit; text-decoration: none;\" target=\"");
      out.print( renderLayoutStructureDisplayContext.getContainerLinkTarget(containerStyledLayoutStructureItem) );
      out.write("\">\n");
      out.write("\t\t\t");
}
      out.write("\n");
      out.write("\n");
      out.write("\t\t\t<div class=\"");
      out.print( renderLayoutStructureDisplayContext.getCssClass(containerStyledLayoutStructureItem) );
      out.write("\" style=\"");
      out.print( renderLayoutStructureDisplayContext.getStyle(containerStyledLayoutStructureItem) );
      out.write("\">\n");
      out.write("\n");
      out.write("\t\t\t\t");

				request.setAttribute("render_layout_structure.jsp-childrenItemIds", layoutStructureItem.getChildrenItemIds());
				
      out.write("\n");
      out.write("\n");
      out.write("\t\t\t\t");
      //  liferay-util:include
      com.liferay.taglib.util.IncludeTag _jspx_th_liferay$1util_include_2 = (_jspx_resourceInjector != null) ? _jspx_resourceInjector.createTagHandlerInstance(com.liferay.taglib.util.IncludeTag.class) : new com.liferay.taglib.util.IncludeTag();
      _jspx_th_liferay$1util_include_2.setPageContext(_jspx_page_context);
      _jspx_th_liferay$1util_include_2.setParent(null);
      _jspx_th_liferay$1util_include_2.setPage("/render_layout_structure/render_layout_structure.jsp");
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
      out.write("\t\t\t</div>\n");
      out.write("\n");
      out.write("\t\t\t");
if (
 Validator.isNotNull(containerLinkHref) ) {
      out.write("\n");
      out.write("\t\t\t\t</a>\n");
      out.write("\t\t\t");
}
      out.write("\n");
      out.write("\t\t");
      out.write("\n");
      out.write("\t\t");
}
else if (
 layoutStructureItem instanceof DropZoneLayoutStructureItem ) {
      out.write("\n");
      out.write("\t\t\t");
      out.write("\n");
      out.write("\t\t\t\t");
if (
 Objects.equals(layout.getType(), LayoutConstants.TYPE_PORTLET) ) {
      out.write("\n");
      out.write("\n");
      out.write("\t\t\t\t\t");

					String themeId = theme.getThemeId();

					String layoutTemplateId = layoutTypePortlet.getLayoutTemplateId();

					if (Validator.isNull(layoutTemplateId)) {
						layoutTemplateId = PropsValues.DEFAULT_LAYOUT_TEMPLATE_ID;
					}

					LayoutTemplate layoutTemplate = LayoutTemplateLocalServiceUtil.getLayoutTemplate(layoutTemplateId, false, theme.getThemeId());

					if (layoutTemplate != null) {
						themeId = layoutTemplate.getThemeId();
					}

					String templateId = themeId + LayoutTemplateConstants.CUSTOM_SEPARATOR + layoutTypePortlet.getLayoutTemplateId();
					String templateContent = LayoutTemplateLocalServiceUtil.getContent(layoutTypePortlet.getLayoutTemplateId(), false, theme.getThemeId());
					String langType = LayoutTemplateLocalServiceUtil.getLangType(layoutTypePortlet.getLayoutTemplateId(), false, theme.getThemeId());

					if (Validator.isNotNull(templateContent)) {
						HttpServletRequest originalServletRequest = (HttpServletRequest)request.getAttribute("ORIGINAL_HTTP_SERVLET_REQUEST");

						RuntimePageUtil.processTemplate(originalServletRequest, response, new StringTemplateResource(templateId, templateContent), langType);
					}
					
      out.write("\n");
      out.write("\n");
      out.write("\t\t\t\t");
      out.write("\n");
      out.write("\t\t\t\t");
}
else {
      out.write("\n");
      out.write("\n");
      out.write("\t\t\t\t\t");

					request.setAttribute("render_layout_structure.jsp-childrenItemIds", layoutStructureItem.getChildrenItemIds());
					
      out.write("\n");
      out.write("\n");
      out.write("\t\t\t\t\t");
      //  liferay-util:include
      com.liferay.taglib.util.IncludeTag _jspx_th_liferay$1util_include_3 = (_jspx_resourceInjector != null) ? _jspx_resourceInjector.createTagHandlerInstance(com.liferay.taglib.util.IncludeTag.class) : new com.liferay.taglib.util.IncludeTag();
      _jspx_th_liferay$1util_include_3.setPageContext(_jspx_page_context);
      _jspx_th_liferay$1util_include_3.setParent(null);
      _jspx_th_liferay$1util_include_3.setPage("/render_layout_structure/render_layout_structure.jsp");
      _jspx_th_liferay$1util_include_3.setServletContext( application );
      int _jspx_eval_liferay$1util_include_3 = _jspx_th_liferay$1util_include_3.doStartTag();
      if (_jspx_th_liferay$1util_include_3.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
        if (_jspx_resourceInjector != null) _jspx_resourceInjector.preDestroy(_jspx_th_liferay$1util_include_3);
        _jspx_th_liferay$1util_include_3.release();
        return;
      }
      if (_jspx_resourceInjector != null) _jspx_resourceInjector.preDestroy(_jspx_th_liferay$1util_include_3);
      _jspx_th_liferay$1util_include_3.release();
      out.write("\n");
      out.write("\t\t\t\t");
      out.write("\n");
      out.write("\t\t\t");
}
      out.write("\n");
      out.write("\t\t");
      out.write("\n");
      out.write("\t\t");
}
else if (
 layoutStructureItem instanceof FragmentStyledLayoutStructureItem ) {
      out.write("\n");
      out.write("\t\t\t<div class=\"");
      out.print( Objects.equals(layout.getType(), LayoutConstants.TYPE_PORTLET) ? "master-layout-fragment" : "" );
      out.write("\">\n");
      out.write("\n");
      out.write("\t\t\t\t");

				FragmentStyledLayoutStructureItem fragmentStyledLayoutStructureItem = (FragmentStyledLayoutStructureItem)layoutStructureItem;
				
      out.write("\n");
      out.write("\n");
      out.write("\t\t\t\t");
if (
 fragmentStyledLayoutStructureItem.getFragmentEntryLinkId() > 0 ) {
      out.write("\n");
      out.write("\n");
      out.write("\t\t\t\t\t");

					FragmentEntryLink fragmentEntryLink = FragmentEntryLinkLocalServiceUtil.fetchFragmentEntryLink(fragmentStyledLayoutStructureItem.getFragmentEntryLinkId());
					
      out.write("\n");
      out.write("\n");
      out.write("\t\t\t\t\t");
if (
 fragmentEntryLink != null ) {
      out.write("\n");
      out.write("\n");
      out.write("\t\t\t\t\t\t");

						FragmentRendererController fragmentRendererController = (FragmentRendererController)request.getAttribute(FragmentActionKeys.FRAGMENT_RENDERER_CONTROLLER);

						DefaultFragmentRendererContext defaultFragmentRendererContext = renderLayoutStructureDisplayContext.getDefaultFragmentRendererContext(fragmentEntryLink, fragmentStyledLayoutStructureItem.getItemId());
						
      out.write("\n");
      out.write("\n");
      out.write("\t\t\t\t\t\t<div class=\"");
      out.print( renderLayoutStructureDisplayContext.getCssClass(fragmentStyledLayoutStructureItem) );
      out.write("\" style=\"");
      out.print( renderLayoutStructureDisplayContext.getStyle(fragmentStyledLayoutStructureItem) );
      out.write("\">\n");
      out.write("\t\t\t\t\t\t\t");
      out.print( fragmentRendererController.render(defaultFragmentRendererContext, request, response) );
      out.write("\n");
      out.write("\t\t\t\t\t\t</div>\n");
      out.write("\t\t\t\t\t");
}
      out.write("\n");
      out.write("\t\t\t\t");
}
      out.write("\n");
      out.write("\t\t\t</div>\n");
      out.write("\t\t");
      out.write("\n");
      out.write("\t\t");
}
else if (
 layoutStructureItem instanceof RowStyledLayoutStructureItem ) {
      out.write("\n");
      out.write("\n");
      out.write("\t\t\t");

			RowStyledLayoutStructureItem rowStyledLayoutStructureItem = (RowStyledLayoutStructureItem)layoutStructureItem;

			LayoutStructureItem parentLayoutStructureItem = layoutStructure.getLayoutStructureItem(rowStyledLayoutStructureItem.getParentItemId());

			boolean includeContainer = false;

			if (parentLayoutStructureItem instanceof RootLayoutStructureItem) {
				if (Objects.equals(layout.getType(), LayoutConstants.TYPE_PORTLET)) {
					includeContainer = true;
				}
				else {
					LayoutStructureItem rootParentLayoutStructureItem = layoutStructure.getLayoutStructureItem(parentLayoutStructureItem.getParentItemId());

					if (rootParentLayoutStructureItem == null) {
						includeContainer = true;
					}
					else if (rootParentLayoutStructureItem instanceof DropZoneLayoutStructureItem) {
						LayoutStructureItem dropZoneParentLayoutStructureItem = layoutStructure.getLayoutStructureItem(rootParentLayoutStructureItem.getParentItemId());

						if (dropZoneParentLayoutStructureItem instanceof RootLayoutStructureItem) {
							includeContainer = true;
						}
					}
				}
			}
			
      out.write("\n");
      out.write("\n");
      out.write("\t\t\t<div class=\"");
      out.print( renderLayoutStructureDisplayContext.getCssClass(rowStyledLayoutStructureItem) );
      out.write("\" style=\"");
      out.print( renderLayoutStructureDisplayContext.getStyle(rowStyledLayoutStructureItem) );
      out.write("\">\n");
      out.write("\t\t\t\t");
      out.write("\n");
      out.write("\t\t\t\t\t");
if (
 includeContainer ) {
      out.write("\n");
      out.write("\t\t\t\t\t\t");
      //  clay:container
      com.liferay.frontend.taglib.clay.servlet.taglib.ContainerTag _jspx_th_clay_container_0 = (_jspx_resourceInjector != null) ? _jspx_resourceInjector.createTagHandlerInstance(com.liferay.frontend.taglib.clay.servlet.taglib.ContainerTag.class) : new com.liferay.frontend.taglib.clay.servlet.taglib.ContainerTag();
      _jspx_th_clay_container_0.setPageContext(_jspx_page_context);
      _jspx_th_clay_container_0.setParent(null);
      _jspx_th_clay_container_0.setCssClass("p-0");
      _jspx_th_clay_container_0.setFluid( true );
      int _jspx_eval_clay_container_0 = _jspx_th_clay_container_0.doStartTag();
      if (_jspx_eval_clay_container_0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
        out.write("\n");
        out.write("\t\t\t\t\t\t\t");
        //  clay:row
        com.liferay.frontend.taglib.clay.servlet.taglib.RowTag _jspx_th_clay_row_1 = (_jspx_resourceInjector != null) ? _jspx_resourceInjector.createTagHandlerInstance(com.liferay.frontend.taglib.clay.servlet.taglib.RowTag.class) : new com.liferay.frontend.taglib.clay.servlet.taglib.RowTag();
        _jspx_th_clay_row_1.setPageContext(_jspx_page_context);
        _jspx_th_clay_row_1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_clay_container_0);
        _jspx_th_clay_row_1.setCssClass( ResponsiveLayoutStructureUtil.getRowCssClass(rowStyledLayoutStructureItem) );
        int _jspx_eval_clay_row_1 = _jspx_th_clay_row_1.doStartTag();
        if (_jspx_eval_clay_row_1 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
          out.write("\n");
          out.write("\n");
          out.write("\t\t\t\t\t\t\t\t");

								request.setAttribute("render_layout_structure.jsp-childrenItemIds", layoutStructureItem.getChildrenItemIds());
								
          out.write("\n");
          out.write("\n");
          out.write("\t\t\t\t\t\t\t\t");
          //  liferay-util:include
          com.liferay.taglib.util.IncludeTag _jspx_th_liferay$1util_include_4 = (_jspx_resourceInjector != null) ? _jspx_resourceInjector.createTagHandlerInstance(com.liferay.taglib.util.IncludeTag.class) : new com.liferay.taglib.util.IncludeTag();
          _jspx_th_liferay$1util_include_4.setPageContext(_jspx_page_context);
          _jspx_th_liferay$1util_include_4.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_clay_row_1);
          _jspx_th_liferay$1util_include_4.setPage("/render_layout_structure/render_layout_structure.jsp");
          _jspx_th_liferay$1util_include_4.setServletContext( application );
          int _jspx_eval_liferay$1util_include_4 = _jspx_th_liferay$1util_include_4.doStartTag();
          if (_jspx_th_liferay$1util_include_4.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
            if (_jspx_resourceInjector != null) _jspx_resourceInjector.preDestroy(_jspx_th_liferay$1util_include_4);
            _jspx_th_liferay$1util_include_4.release();
            return;
          }
          if (_jspx_resourceInjector != null) _jspx_resourceInjector.preDestroy(_jspx_th_liferay$1util_include_4);
          _jspx_th_liferay$1util_include_4.release();
          out.write("\n");
          out.write("\t\t\t\t\t\t\t");
        }
        if (_jspx_th_clay_row_1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
          if (_jspx_resourceInjector != null) _jspx_resourceInjector.preDestroy(_jspx_th_clay_row_1);
          _jspx_th_clay_row_1.release();
          return;
        }
        if (_jspx_resourceInjector != null) _jspx_resourceInjector.preDestroy(_jspx_th_clay_row_1);
        _jspx_th_clay_row_1.release();
        out.write("\n");
        out.write("\t\t\t\t\t\t");
      }
      if (_jspx_th_clay_container_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
        if (_jspx_resourceInjector != null) _jspx_resourceInjector.preDestroy(_jspx_th_clay_container_0);
        _jspx_th_clay_container_0.release();
        return;
      }
      if (_jspx_resourceInjector != null) _jspx_resourceInjector.preDestroy(_jspx_th_clay_container_0);
      _jspx_th_clay_container_0.release();
      out.write("\n");
      out.write("\t\t\t\t\t");
      out.write("\n");
      out.write("\t\t\t\t\t");
}
else {
      out.write("\n");
      out.write("\t\t\t\t\t\t");
      //  clay:row
      com.liferay.frontend.taglib.clay.servlet.taglib.RowTag _jspx_th_clay_row_2 = (_jspx_resourceInjector != null) ? _jspx_resourceInjector.createTagHandlerInstance(com.liferay.frontend.taglib.clay.servlet.taglib.RowTag.class) : new com.liferay.frontend.taglib.clay.servlet.taglib.RowTag();
      _jspx_th_clay_row_2.setPageContext(_jspx_page_context);
      _jspx_th_clay_row_2.setParent(null);
      _jspx_th_clay_row_2.setCssClass( ResponsiveLayoutStructureUtil.getRowCssClass(rowStyledLayoutStructureItem) );
      int _jspx_eval_clay_row_2 = _jspx_th_clay_row_2.doStartTag();
      if (_jspx_eval_clay_row_2 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
        out.write("\n");
        out.write("\n");
        out.write("\t\t\t\t\t\t\t");

							request.setAttribute("render_layout_structure.jsp-childrenItemIds", layoutStructureItem.getChildrenItemIds());
							
        out.write("\n");
        out.write("\n");
        out.write("\t\t\t\t\t\t\t");
        //  liferay-util:include
        com.liferay.taglib.util.IncludeTag _jspx_th_liferay$1util_include_5 = (_jspx_resourceInjector != null) ? _jspx_resourceInjector.createTagHandlerInstance(com.liferay.taglib.util.IncludeTag.class) : new com.liferay.taglib.util.IncludeTag();
        _jspx_th_liferay$1util_include_5.setPageContext(_jspx_page_context);
        _jspx_th_liferay$1util_include_5.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_clay_row_2);
        _jspx_th_liferay$1util_include_5.setPage("/render_layout_structure/render_layout_structure.jsp");
        _jspx_th_liferay$1util_include_5.setServletContext( application );
        int _jspx_eval_liferay$1util_include_5 = _jspx_th_liferay$1util_include_5.doStartTag();
        if (_jspx_th_liferay$1util_include_5.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
          if (_jspx_resourceInjector != null) _jspx_resourceInjector.preDestroy(_jspx_th_liferay$1util_include_5);
          _jspx_th_liferay$1util_include_5.release();
          return;
        }
        if (_jspx_resourceInjector != null) _jspx_resourceInjector.preDestroy(_jspx_th_liferay$1util_include_5);
        _jspx_th_liferay$1util_include_5.release();
        out.write("\n");
        out.write("\t\t\t\t\t\t");
      }
      if (_jspx_th_clay_row_2.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
        if (_jspx_resourceInjector != null) _jspx_resourceInjector.preDestroy(_jspx_th_clay_row_2);
        _jspx_th_clay_row_2.release();
        return;
      }
      if (_jspx_resourceInjector != null) _jspx_resourceInjector.preDestroy(_jspx_th_clay_row_2);
      _jspx_th_clay_row_2.release();
      out.write("\n");
      out.write("\t\t\t\t\t");
      out.write("\n");
      out.write("\t\t\t\t");
}
      out.write("\n");
      out.write("\t\t\t</div>\n");
      out.write("\t\t");
      out.write("\n");
      out.write("\t\t");
}
else {
      out.write("\n");
      out.write("\n");
      out.write("\t\t\t");

			request.setAttribute("render_layout_structure.jsp-childrenItemIds", layoutStructureItem.getChildrenItemIds());
			
      out.write("\n");
      out.write("\n");
      out.write("\t\t\t");
      //  liferay-util:include
      com.liferay.taglib.util.IncludeTag _jspx_th_liferay$1util_include_6 = (_jspx_resourceInjector != null) ? _jspx_resourceInjector.createTagHandlerInstance(com.liferay.taglib.util.IncludeTag.class) : new com.liferay.taglib.util.IncludeTag();
      _jspx_th_liferay$1util_include_6.setPageContext(_jspx_page_context);
      _jspx_th_liferay$1util_include_6.setParent(null);
      _jspx_th_liferay$1util_include_6.setPage("/render_layout_structure/render_layout_structure.jsp");
      _jspx_th_liferay$1util_include_6.setServletContext( application );
      int _jspx_eval_liferay$1util_include_6 = _jspx_th_liferay$1util_include_6.doStartTag();
      if (_jspx_th_liferay$1util_include_6.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
        if (_jspx_resourceInjector != null) _jspx_resourceInjector.preDestroy(_jspx_th_liferay$1util_include_6);
        _jspx_th_liferay$1util_include_6.release();
        return;
      }
      if (_jspx_resourceInjector != null) _jspx_resourceInjector.preDestroy(_jspx_th_liferay$1util_include_6);
      _jspx_th_liferay$1util_include_6.release();
      out.write("\n");
      out.write("\t\t");
      out.write('\n');
      out.write('	');
}
      out.write('\n');
      out.write('\n');

}

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
}
