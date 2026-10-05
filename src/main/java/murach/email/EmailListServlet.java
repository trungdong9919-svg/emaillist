package murach.email;

import java.io.IOException;
import java.util.Calendar;
import java.util.logging.Level;
import java.util.logging.Logger;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import murach.business.User;
import murach.data.UserDB;

@WebServlet("/emailList")
public class EmailListServlet extends HttpServlet {

    private static final Logger logger =
            Logger.getLogger(EmailListServlet.class.getName());

    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {

        // Set current year for footer
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        request.setAttribute("currentYear", currentYear);

        String url = "/index.jsp";

        // Get action parameter
        String action = request.getParameter("action");
        if (action == null) {
            action = "join";
        }

        switch (action) {
            case "join":
                url = "/index.jsp";
                break;

            case "add":
                String firstName = request.getParameter("firstName");
                String lastName  = request.getParameter("lastName");
                String email     = request.getParameter("email");

                // Basic server-side validation
                if (firstName == null || firstName.isBlank() ||
                    lastName  == null || lastName.isBlank()  ||
                    email     == null || email.isBlank()) {
                    request.setAttribute("errorMessage", "All fields are required.");
                    url = "/index.jsp";
                } else {
                    User user = new User(firstName.trim(), lastName.trim(), email.trim());
                    UserDB.insert(user);

                    // Send welcome/confirmation email to the registered address
                    try {
                        EmailService.sendWelcomeEmail(
                                user.getEmail(),
                                user.getFirstName(),
                                user.getLastName());
                        request.setAttribute("emailSent", true);
                    } catch (Exception ex) {
                        // Log but don't break the user flow if email fails
                        logger.log(Level.WARNING,
                                "Could not send welcome email to " + user.getEmail(), ex);
                        request.setAttribute("emailSent", false);
                    }

                    request.setAttribute("user", user);
                    url = "/thanks.jsp";
                }
                break;

            default:
                url = "/index.jsp";
        }

        getServletContext()
                .getRequestDispatcher(url)
                .forward(request, response);
    }

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {

        // Set current year for footer
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        request.setAttribute("currentYear", currentYear);

        getServletContext()
                .getRequestDispatcher("/index.jsp")
                .forward(request, response);
    }
}
