# 📚 Smart Library Management System

A Java-based desktop application designed to simplify library operations, manage book inventories, and maintain member records using Java AWT, JDBC, and MySQL.

---

## 📌 Project Overview

The **Smart Library Management System** is an academic project developed to automate basic library management activities. It provides a graphical user interface through Java AWT and uses MySQL as its database to store and manage library information.

The system helps librarians manage books, register members, search for books, issue and return books, and monitor library records efficiently.

## 🚀 Key Features

- **Book Management:** Add, view, and remove books.
- **Member Management:** Register and manage library members.
- **Book Search:** Search for books in the library inventory.
- **Issue Books:** Issue available books to registered members.
- **Return Books:** Manage book return operations.
- **Library Statistics:** View library-related statistics.
- **Database Connectivity:** Store and retrieve records using JDBC and MySQL.
- **Graphical User Interface:** User-friendly desktop interface developed using Java AWT.

## 🛠️ Technologies Used

| Technology | Purpose |
|---|---|
| Java | Core programming language |
| Java AWT | Graphical User Interface |
| JDBC | Database connectivity |
| MySQL | Database management |
| IntelliJ IDEA | Development environment |
| MySQL Connector/J | JDBC driver |

## 🗄️ Database Structure

**Database Name:** `smart_library`

The project uses the following database tables:

### 1. Books Table

Stores information about library books.

- `book_id`
- `title`
- `author`
- `category`
- `issued`

### 2. Members Table

Stores information about registered library members.

- `member_id`
- `name`
- `department`

## ⚙️ Installation and Setup

Follow these steps to run the project on your system:

**Step 1: Clone the Repository**

```bash
git clone https://github.com/aabhasghiya2007-cmyk/Smart-Library-Management-System.git
```

**Step 2: Open the Project**

- Open IntelliJ IDEA.
- Select Open Project.
- Choose the downloaded project folder.

**Step 3: Configure MySQL**

- Open MySQL Workbench.
- Create a database named `smart_library`.
- Create the required `books` and `members` tables.
- Configure your MySQL username and password in the database connection file.

**Step 4: Configure JDBC**

- Add MySQL Connector/J to the project dependencies.
- Verify the JDBC connection settings in `DBConnection.java`.

**Step 5: Run the Application**

Run `LibraryManagement.java` from IntelliJ IDEA.

## 📂 Project Structure

```text
Smart-Library-Management-System/
│
├── src/
│   ├── DBConnection.java
│   └── LibraryManagement.java
│
├── .gitignore
└── README.md
```

## 👨‍💻 Developed By

**Aabhas Ghiya**

B.Tech – Computer Science and Engineering  
Poornima Institute of Engineering and Technology (PIET)

## 🎓 Project Information

- **Project Title:** Smart Library Management System
- **Domain:** Java Programming and Database Management
- **Project Type:** Academic Project
- **Development Environment:** IntelliJ IDEA

---

⭐ If you find this project useful, feel free to explore the repository!

**Thank you for visiting my project!**
