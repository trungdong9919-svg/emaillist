# Email List Web App

A Java Servlet/JSP web application from Murach's Java Servlets and JSP textbook.  
Built with Maven and deployable on **Apache Tomcat 10.1** (Jakarta EE 10).

## Project Structure

```
emaillist/
├── src/
│   └── main/
│       ├── java/murach/
│       │   ├── business/User.java          # User model class
│       │   ├── data/UserDB.java            # In-memory data layer
│       │   └── email/EmailListServlet.java # Main servlet
│       └── webapp/
│           ├── includes/
│           │   ├── header.html             # Page header include
│           │   └── footer.jsp              # Page footer include
│           ├── styles/
│           │   └── main.css               # Stylesheet
│           ├── WEB-INF/
│           │   └── web.xml                # Deployment descriptor
│           ├── index.jsp                  # Join form page
│           └── thanks.jsp                 # Thank-you confirmation page
├── Dockerfile                             # Multi-stage Docker build
├── docker-compose.yml                     # Docker Compose config
└── pom.xml                               # Maven build config
```

## Running Locally (with Tomcat in IntelliJ)

1. Open the project in IntelliJ IDEA
2. Configure a **Tomcat 10.1** local server
3. Deploy `emaillist:war exploded`
4. Access at: `http://localhost:8080/`

## Build with Maven

```bash
mvn clean package
```
WAR file: `target/emaillist.war`

## Docker Deployment

### Build and run with Docker Compose (recommended)

```bash
docker-compose up --build
```
App available at: **http://localhost:8080**

### Manual Docker commands

```bash
# Build the image
docker build -t emaillist .

# Run the container
docker run -d -p 8080:8080 --name emaillist-tomcat emaillist

# Stop and remove
docker stop emaillist-tomcat && docker rm emaillist-tomcat
```

## Features

- **Join Form**: Users can register with Email, First Name, and Last Name
- **Server-side validation**: All fields are required
- **Confirmation page**: Shows submitted information after joining
- **Return navigation**: Users can go back to submit another entry
- **Dynamic footer year**: Copyright year updates automatically
