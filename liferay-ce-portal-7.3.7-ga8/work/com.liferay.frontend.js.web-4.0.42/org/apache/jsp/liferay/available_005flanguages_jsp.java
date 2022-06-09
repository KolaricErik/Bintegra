package org.apache.jsp.liferay;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;
import com.liferay.portal.kernel.language.LanguageUtil;
import com.liferay.portal.kernel.util.HtmlUtil;
import com.liferay.portal.kernel.util.LocaleUtil;
import java.util.Locale;

public final class available_005flanguages_jsp extends org.apache.jasper.runtime.HttpJspBase
    implements org.apache.jasper.runtime.JspSourceDependent {

  private static final JspFactory _jspxFactory = JspFactory.getDefaultFactory();

  private static java.util.List<String> _jspx_dependants;

  static {
    _jspx_dependants = new java.util.ArrayList<String>(1);
    _jspx_dependants.add("/liferay/init.jsp");
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
      response.setContentType("text/javascript; charset=UTF-8");
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
      out.write('\n');
      out.write('\n');

Locale locale = LocaleUtil.fromLanguageId(LanguageUtil.getLanguageId(request));

      out.write("\n");
      out.write("\n");
      out.write("AUI.add(\n");
      out.write("\t'portal-available-languages',\n");
      out.write("\tfunction(A) {\n");
      out.write("\t\tvar available = {};\n");
      out.write("\n");
      out.write("\t\tvar direction = {};\n");
      out.write("\n");
      out.write("\t\t");

		for (Locale curLocale : LanguageUtil.getAvailableLocales()) {
			String selLanguageId = LocaleUtil.toLanguageId(curLocale);
		
      out.write("\n");
      out.write("\n");
      out.write("\t\t\tavailable['");
      out.print( selLanguageId );
      out.write("'] = '");
      out.print( HtmlUtil.escapeJS(curLocale.getDisplayName(locale)) );
      out.write("';\n");
      out.write("\t\t\tdirection['");
      out.print( selLanguageId );
      out.write("'] = '");
      out.print( LanguageUtil.get(curLocale, "lang.dir") );
      out.write("';\n");
      out.write("\n");
      out.write("\t\t");

		}
		
      out.write("\n");
      out.write("\n");
      out.write("\t\tLiferay.Language.available = available;\n");
      out.write("\t\tLiferay.Language.direction = direction;\n");
      out.write("\t},\n");
      out.write("\t'',\n");
      out.write("\t{\n");
      out.write("\t\trequires: []\n");
      out.write("\t}\n");
      out.write(");");
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
