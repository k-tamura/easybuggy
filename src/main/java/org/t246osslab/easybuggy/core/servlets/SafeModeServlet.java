package org.t246osslab.easybuggy.core.servlets;

import java.io.IOException;
import java.util.Locale;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.t246osslab.easybuggy.core.utils.EmailUtils;
import org.t246osslab.easybuggy.core.utils.HTTPResponseCreator;
import org.t246osslab.easybuggy.core.utils.MessageUtils;

@SuppressWarnings("serial")
@WebServlet(urlPatterns = { "/safemode" })
public class SafeModeServlet extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {

		Locale locale = req.getLocale();
		if (!EmailUtils.isReadyToSendEmail()) {
			HTTPResponseCreator.createSimpleResponse(req, res, MessageUtils.getMsg("title.safe.mode", locale),
					MessageUtils.getInfoMsg("msg.note.safe.mode", locale));
			return;
		}
	}
}
