🎉 Sangam Event Management

Sangam is a full-stack Event Management Web Application built with Spring Boot, Thymeleaf, and PostgreSQL. It allows users to browse events, book venues, and manage event details efficiently.

📌 Features

🛡️ User Authentication

Secure login & registration

Role-based access (Admin & User)

🎈 Event Management

Admin can create, update, delete events

Event categories (Wedding, Birthday, Corporate, etc.)

Upload event images

Manage capacity, pricing, and decoration options

🏛️ Venue Management

Admin can add and manage venues

Track bookings and availability

👥 User Features

Browse events by category

View event details with images, price, and location

Book events online

📱 Responsive Design

Mobile-friendly interface using HTML, CSS, and Bootstrap

💻 Technology Stack

Backend: Spring Boot, Java

Frontend: Thymeleaf, HTML, CSS, Bootstrap

Database: PostgreSQL

Security: Spring Security

Build Tool: Maven

Server: Apache Tomcat

🗂 Project Structure
Sangam-Event-Management/
│
├── src/main/java/com/example/eventManagement
│   ├── controller        # Spring MVC Controllers
│   ├── entity            # JPA Entities
│   ├── repository        # Repositories
│   ├── service           # Business Logic Services
│   └── config            # Security & App Config
│
├── src/main/resources
│   ├── templates         # Thymeleaf HTML pages
│   ├── static
│   │   ├── css
│   │   ├── images
│   │   └── js
│   └── application.properties
│
├── pom.xml
└── README.md

⚙️ Installation
Prerequisites

Java 17+

Maven 3.8+

PostgreSQL 14+

IDE (IntelliJ, Eclipse, etc.)

Steps

Clone the repository

git clone https://github.com/your-username/sangam-event-management.git
cd sangam-event-management


Configure Database

Create a PostgreSQL database, e.g., sangam_db

Update application.properties with your credentials:

spring.datasource.url=jdbc:postgresql://localhost:5432/sangam_db
spring.datasource.username=your_username
spring.datasource.password=your_password
spring.jpa.hibernate.ddl-auto=update


Build the project

mvn clean install


Run the application

mvn spring-boot:run


Visit in browser: http://localhost:8080/

📝 Usage

Admin

Access dashboard at /admin/dashboard

Manage events & venues

View bookings

User

Browse events

View event details

Book events online

📸 Screenshots

Homepage – Displays all events

Event Details – Shows event info, images, and booking option

Admin Dashboard – Manage events and venues

(Add your screenshots in ./screenshots folder for GitHub display)

🤝 Contributing

Fork the repository

Create a new branch: git checkout -b feature/your-feature

Commit your changes: git commit -m "Add feature"

Push to branch: git push origin feature/your-feature

Open a Pull Request

📜 License

This project is licensed under the MIT License.

📧 Contact

Aryan Dalal

Email: your-email@example.com

GitHub: https://github.com/your-username