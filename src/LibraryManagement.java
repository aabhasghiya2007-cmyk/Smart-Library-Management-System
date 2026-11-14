import java.awt.*;
import java.awt.event.*;
import java.sql.*;


// ============================================================
// MAIN CLASS  (AWT GUI + MySQL database through DBConnection)
// ============================================================

public class LibraryManagement extends Frame
        implements ActionListener {

    CardLayout cardLayout;
    Panel mainPanel;

    // Colors
    Color navy = new Color(28, 43, 70);
    Color blue = new Color(48, 93, 150);
    Color lightBlue = new Color(235, 242, 250);
    Color white = Color.WHITE;
    Color gray = new Color(90, 100, 110);

    // Fonts
    Font headingFont = new Font("Arial", Font.BOLD, 25);
    Font normalFont = new Font("Arial", Font.PLAIN, 14);
    Font buttonFont = new Font("Arial", Font.BOLD, 14);
    Font tableFont = new Font("Monospaced", Font.PLAIN, 14);

    // Book components
    TextField bookIdField, bookTitleField, bookAuthorField, bookCategoryField;
    TextArea bookTable;

    // Member components
    TextField memberIdField, memberNameField, memberDepartmentField;
    TextArea memberTable;

    // Search components
    TextField searchField;
    TextArea searchTable;

    // Issue components
    TextField issueBookIdField, issueMemberIdField;
    TextArea issueInfo;

    // Return components
    TextField returnBookIdField;
    TextArea returnInfo;

    // Statistics
    Label totalBooks, availableBooks, issuedBooks, totalMembers;


    // ========================================================
    // CONSTRUCTOR
    // ========================================================

    LibraryManagement() {

        setTitle("Smart Library Management System");
        setSize(1150, 750);
        setLayout(new BorderLayout());
        setBackground(lightBlue);

        cardLayout = new CardLayout();
        mainPanel = new Panel(cardLayout);

        createDashboard();
        createBookPage();
        createMemberPage();
        createSearchPage();
        createIssuePage();
        createReturnPage();
        createStatisticsPage();

        add(mainPanel, BorderLayout.CENTER);

        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });

        setLocationRelativeTo(null);
        setVisible(true);

        // Check database connection on startup
        try (Connection con = DBConnection.getConnection()) {
            // connected
        }
        catch (SQLException e) {
            showMessage("Database connection failed: " + e.getMessage());
        }
    }


    // ========================================================
    // HEADER / BUTTON HELPERS
    // ========================================================

    Panel createHeader(String title, String subtitle) {

        Panel header = new Panel(new BorderLayout());
        header.setBackground(navy);

        Label titleLabel = new Label(title, Label.CENTER);
        titleLabel.setFont(headingFont);
        titleLabel.setForeground(white);

        Label subtitleLabel = new Label(subtitle, Label.CENTER);
        subtitleLabel.setFont(normalFont);
        subtitleLabel.setForeground(new Color(210, 220, 235));

        header.add(titleLabel, BorderLayout.CENTER);
        header.add(subtitleLabel, BorderLayout.SOUTH);

        return header;
    }

    // Header on top, any panel below it (BorderLayout allows only one
    // component per region, so they are combined here)
    Panel withHeader(Panel header, Panel below) {

        Panel north = new Panel(new BorderLayout());
        north.add(header, BorderLayout.NORTH);
        north.add(below, BorderLayout.CENTER);

        return north;
    }

    Button createButton(String text) {

        Button button = new Button(text);
        button.setFont(buttonFont);
        button.setBackground(blue);
        button.setForeground(white);
        button.addActionListener(this);

        return button;
    }

    Button createButton(String text, String command) {

        Button button = createButton(text);
        button.setActionCommand(command);

        return button;
    }


    // ========================================================
    // DASHBOARD
    // ========================================================

    void createDashboard() {

        Panel page = new Panel(new BorderLayout(15, 15));

        page.add(createHeader("SMART LIBRARY MANAGEMENT SYSTEM",
                "Welcome to your digital library"), BorderLayout.NORTH);

        Panel center = new Panel(new BorderLayout(10, 10));
        center.setBackground(lightBlue);

        Label welcome = new Label("Library Dashboard", Label.CENTER);
        welcome.setFont(new Font("Arial", Font.BOLD, 22));
        welcome.setForeground(navy);
        center.add(welcome, BorderLayout.NORTH);

        Panel menu = new Panel(new GridLayout(3, 2, 25, 25));
        menu.add(createButton("BOOK MANAGEMENT", "BOOK_PAGE"));
        menu.add(createButton("MEMBER MANAGEMENT", "MEMBER_PAGE"));
        menu.add(createButton("SEARCH BOOK", "SEARCH_PAGE"));
        menu.add(createButton("ISSUE BOOK", "ISSUE_PAGE"));
        menu.add(createButton("RETURN BOOK", "RETURN_PAGE"));
        menu.add(createButton("LIBRARY STATISTICS", "STATISTICS_PAGE"));
        center.add(menu, BorderLayout.CENTER);

        Label description = new Label(
                "Manage books \u2022 Register members \u2022 Search \u2022 Issue \u2022 Return \u2022 Track inventory",
                Label.CENTER);
        description.setFont(normalFont);
        description.setForeground(gray);
        center.add(description, BorderLayout.SOUTH);

        page.add(center, BorderLayout.CENTER);

        Panel bottom = new Panel();
        bottom.add(createButton("EXIT APPLICATION", "EXIT"));
        page.add(bottom, BorderLayout.SOUTH);

        mainPanel.add(page, "DASHBOARD");
    }


    // ========================================================
    // BOOK MANAGEMENT PAGE
    // ========================================================

    void createBookPage() {

        Panel page = new Panel(new BorderLayout(10, 10));

        Panel form = new Panel(new GridLayout(2, 4, 10, 10));
        form.setBackground(lightBlue);

        form.add(new Label("Book ID:", Label.RIGHT));
        bookIdField = new TextField();
        form.add(bookIdField);

        form.add(new Label("Book Title:", Label.RIGHT));
        bookTitleField = new TextField();
        form.add(bookTitleField);

        form.add(new Label("Author:", Label.RIGHT));
        bookAuthorField = new TextField();
        form.add(bookAuthorField);

        form.add(new Label("Category:", Label.RIGHT));
        bookCategoryField = new TextField();
        form.add(bookCategoryField);

        page.add(withHeader(
                createHeader("BOOK MANAGEMENT",
                        "Add, view and remove books from the library"),
                form), BorderLayout.NORTH);

        bookTable = new TextArea();
        bookTable.setFont(tableFont);
        bookTable.setEditable(false);
        page.add(bookTable, BorderLayout.CENTER);

        Panel buttons = new Panel();
        buttons.add(createButton("ADD BOOK", "ADD_BOOK"));
        buttons.add(createButton("REFRESH TABLE", "VIEW_BOOKS"));
        buttons.add(createButton("REMOVE BOOK", "REMOVE_BOOK"));
        buttons.add(createButton("BACK TO DASHBOARD", "DASHBOARD"));
        page.add(buttons, BorderLayout.SOUTH);

        mainPanel.add(page, "BOOK_PAGE");
    }


    // ========================================================
    // MEMBER MANAGEMENT PAGE
    // ========================================================

    void createMemberPage() {

        Panel page = new Panel(new BorderLayout(10, 10));

        Panel form = new Panel(new GridLayout(2, 3, 10, 10));
        form.setBackground(lightBlue);

        form.add(new Label("Member ID:", Label.RIGHT));
        memberIdField = new TextField();
        form.add(memberIdField);

        form.add(new Label("Member Name:", Label.RIGHT));
        memberNameField = new TextField();
        form.add(memberNameField);

        form.add(new Label("Department:", Label.RIGHT));
        memberDepartmentField = new TextField();
        form.add(memberDepartmentField);

        page.add(withHeader(
                createHeader("MEMBER MANAGEMENT",
                        "Register and manage library members"),
                form), BorderLayout.NORTH);

        memberTable = new TextArea();
        memberTable.setFont(tableFont);
        memberTable.setEditable(false);
        page.add(memberTable, BorderLayout.CENTER);

        Panel buttons = new Panel();
        buttons.add(createButton("ADD MEMBER", "ADD_MEMBER"));
        buttons.add(createButton("REFRESH TABLE", "VIEW_MEMBERS"));
        buttons.add(createButton("BACK TO DASHBOARD", "DASHBOARD"));
        page.add(buttons, BorderLayout.SOUTH);

        mainPanel.add(page, "MEMBER_PAGE");
    }


    // ========================================================
    // SEARCH PAGE
    // ========================================================

    void createSearchPage() {

        Panel page = new Panel(new BorderLayout(10, 10));

        Panel searchPanel = new Panel();
        searchPanel.add(new Label("Search:"));
        searchField = new TextField(35);
        searchPanel.add(searchField);
        searchPanel.add(createButton("SEARCH", "SEARCH_BOOK"));

        page.add(withHeader(
                createHeader("SEARCH BOOK",
                        "Find books using title or author name"),
                searchPanel), BorderLayout.NORTH);

        searchTable = new TextArea();
        searchTable.setFont(tableFont);
        searchTable.setEditable(false);
        page.add(searchTable, BorderLayout.CENTER);

        Panel bottom = new Panel();
        bottom.add(createButton("BACK TO DASHBOARD", "DASHBOARD"));
        page.add(bottom, BorderLayout.SOUTH);

        mainPanel.add(page, "SEARCH_PAGE");
    }


    // ========================================================
    // ISSUE PAGE
    // ========================================================

    void createIssuePage() {

        Panel page = new Panel(new BorderLayout(10, 10));

        Panel form = new Panel(new GridLayout(2, 2, 15, 15));
        form.setBackground(lightBlue);

        form.add(new Label("Book ID:", Label.RIGHT));
        issueBookIdField = new TextField();
        form.add(issueBookIdField);

        form.add(new Label("Member ID:", Label.RIGHT));
        issueMemberIdField = new TextField();
        form.add(issueMemberIdField);

        page.add(withHeader(
                createHeader("ISSUE BOOK",
                        "Issue an available book to a registered member"),
                form), BorderLayout.NORTH);

        issueInfo = new TextArea();
        issueInfo.setFont(normalFont);
        issueInfo.setEditable(false);
        page.add(issueInfo, BorderLayout.CENTER);

        Panel bottom = new Panel();
        bottom.add(createButton("ISSUE BOOK", "ISSUE_BOOK"));
        bottom.add(createButton("BACK TO DASHBOARD", "DASHBOARD"));
        page.add(bottom, BorderLayout.SOUTH);

        mainPanel.add(page, "ISSUE_PAGE");
    }


    // ========================================================
    // RETURN PAGE
    // ========================================================

    void createReturnPage() {

        Panel page = new Panel(new BorderLayout(10, 10));

        Panel form = new Panel(new GridLayout(1, 2, 15, 15));
        form.setBackground(lightBlue);

        form.add(new Label("Book ID:", Label.RIGHT));
        returnBookIdField = new TextField();
        form.add(returnBookIdField);

        page.add(withHeader(
                createHeader("RETURN BOOK",
                        "Return an issued book to the library"),
                form), BorderLayout.NORTH);

        returnInfo = new TextArea();
        returnInfo.setFont(normalFont);
        returnInfo.setEditable(false);
        page.add(returnInfo, BorderLayout.CENTER);

        Panel bottom = new Panel();
        bottom.add(createButton("RETURN BOOK", "RETURN_BOOK"));
        bottom.add(createButton("BACK TO DASHBOARD", "DASHBOARD"));
        page.add(bottom, BorderLayout.SOUTH);

        mainPanel.add(page, "RETURN_PAGE");
    }


    // ========================================================
    // STATISTICS PAGE
    // ========================================================

    void createStatisticsPage() {

        Panel page = new Panel(new BorderLayout(15, 15));

        page.add(createHeader("LIBRARY STATISTICS",
                        "Overview of your current library inventory"),
                BorderLayout.NORTH);

        Panel cards = new Panel(new GridLayout(2, 2, 30, 30));
        cards.setBackground(lightBlue);

        totalBooks = createStatisticsLabel("TOTAL BOOKS\n0");
        availableBooks = createStatisticsLabel("AVAILABLE BOOKS\n0");
        issuedBooks = createStatisticsLabel("ISSUED BOOKS\n0");
        totalMembers = createStatisticsLabel("TOTAL MEMBERS\n0");

        cards.add(totalBooks);
        cards.add(availableBooks);
        cards.add(issuedBooks);
        cards.add(totalMembers);

        page.add(cards, BorderLayout.CENTER);

        Panel bottom = new Panel();
        bottom.add(createButton("REFRESH STATISTICS", "REFRESH_STATS"));
        bottom.add(createButton("BACK TO DASHBOARD", "DASHBOARD"));
        page.add(bottom, BorderLayout.SOUTH);

        mainPanel.add(page, "STATISTICS_PAGE");
    }

    Label createStatisticsLabel(String text) {

        Label label = new Label(text, Label.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 22));
        label.setBackground(white);
        label.setForeground(navy);

        return label;
    }


    // ========================================================
    // BOOK TABLE FORMATTING (shared by view + search)
    // ========================================================

    static final String LINE =
            "================================================================================\n";

    StringBuilder bookTableHeader() {

        StringBuilder t = new StringBuilder();
        t.append(LINE);
        t.append(String.format("%-8s %-25s %-22s %-18s %-12s%n",
                "ID", "BOOK TITLE", "AUTHOR", "CATEGORY", "STATUS"));
        t.append(LINE);

        return t;
    }

    void appendBookRow(StringBuilder t, ResultSet rs) throws SQLException {

        String status = rs.getBoolean("issued") ? "ISSUED" : "AVAILABLE";

        t.append(String.format("%-8d %-25s %-22s %-18s %-12s%n",
                rs.getInt("book_id"),
                limit(rs.getString("title"), 24),
                limit(rs.getString("author"), 21),
                limit(rs.getString("category"), 17),
                status));
    }


    // ========================================================
    // ADD BOOK
    // ========================================================

    void addBook() {

        try {
            int id = Integer.parseInt(bookIdField.getText().trim());
            String title = bookTitleField.getText().trim();
            String author = bookAuthorField.getText().trim();
            String category = bookCategoryField.getText().trim();

            if (title.isEmpty() || author.isEmpty() || category.isEmpty()) {
                showMessage("Please enter all book details.");
                return;
            }

            String sql = "INSERT INTO books (book_id, title, author, category) "
                    + "VALUES (?, ?, ?, ?)";

            try (Connection con = DBConnection.getConnection();
                 PreparedStatement ps = con.prepareStatement(sql)) {

                ps.setInt(1, id);
                ps.setString(2, title);
                ps.setString(3, author);
                ps.setString(4, category);
                ps.executeUpdate();
            }

            showMessage("Book added successfully!");

            bookIdField.setText("");
            bookTitleField.setText("");
            bookAuthorField.setText("");
            bookCategoryField.setText("");

            displayBooks();
        }
        catch (NumberFormatException e) {
            showMessage("Please enter a valid numeric Book ID.");
        }
        catch (SQLIntegrityConstraintViolationException e) {
            showMessage("Book ID already exists!");
        }
        catch (SQLException e) {
            showMessage("Database error: " + e.getMessage());
        }
    }


    // ========================================================
    // DISPLAY BOOKS
    // ========================================================

    void displayBooks() {

        StringBuilder table = bookTableHeader();
        int count = 0;

        try (Connection con = DBConnection.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(
                     "SELECT * FROM books ORDER BY book_id")) {

            while (rs.next()) {
                appendBookRow(table, rs);
                count++;
            }
        }
        catch (SQLException e) {
            bookTable.setText("Database error: " + e.getMessage());
            return;
        }

        table.append(LINE);
        table.append("Total Books: " + count);

        bookTable.setText(table.toString());
    }


    // ========================================================
    // REMOVE BOOK
    // ========================================================

    void removeBook() {

        try {
            int id = Integer.parseInt(bookIdField.getText().trim());

            int rows;

            try (Connection con = DBConnection.getConnection();
                 PreparedStatement ps = con.prepareStatement(
                         "DELETE FROM books WHERE book_id = ?")) {

                ps.setInt(1, id);
                rows = ps.executeUpdate();
            }

            if (rows > 0) {
                showMessage("Book removed successfully!");
                displayBooks();
            }
            else {
                showMessage("Book ID not found.");
            }
        }
        catch (NumberFormatException e) {
            showMessage("Enter a valid Book ID.");
        }
        catch (SQLException e) {
            showMessage("Database error: " + e.getMessage());
        }
    }


    // ========================================================
    // ADD MEMBER
    // ========================================================

    void addMember() {

        try {
            int id = Integer.parseInt(memberIdField.getText().trim());
            String name = memberNameField.getText().trim();
            String department = memberDepartmentField.getText().trim();

            if (name.isEmpty() || department.isEmpty()) {
                showMessage("Please enter all member details.");
                return;
            }

            String sql = "INSERT INTO members (member_id, name, department) "
                    + "VALUES (?, ?, ?)";

            try (Connection con = DBConnection.getConnection();
                 PreparedStatement ps = con.prepareStatement(sql)) {

                ps.setInt(1, id);
                ps.setString(2, name);
                ps.setString(3, department);
                ps.executeUpdate();
            }

            showMessage("Member registered successfully!");

            memberIdField.setText("");
            memberNameField.setText("");
            memberDepartmentField.setText("");

            displayMembers();
        }
        catch (NumberFormatException e) {
            showMessage("Enter a valid numeric Member ID.");
        }
        catch (SQLIntegrityConstraintViolationException e) {
            showMessage("Member ID already exists!");
        }
        catch (SQLException e) {
            showMessage("Database error: " + e.getMessage());
        }
    }


    // ========================================================
    // DISPLAY MEMBERS
    // ========================================================

    void displayMembers() {

        String line =
                "===============================================================\n";

        StringBuilder table = new StringBuilder();
        table.append(line);
        table.append(String.format("%-12s %-30s %-20s%n",
                "MEMBER ID", "MEMBER NAME", "DEPARTMENT"));
        table.append(line);

        int count = 0;

        try (Connection con = DBConnection.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(
                     "SELECT * FROM members ORDER BY member_id")) {

            while (rs.next()) {
                table.append(String.format("%-12d %-30s %-20s%n",
                        rs.getInt("member_id"),
                        limit(rs.getString("name"), 29),
                        limit(rs.getString("department"), 19)));
                count++;
            }
        }
        catch (SQLException e) {
            memberTable.setText("Database error: " + e.getMessage());
            return;
        }

        table.append(line);
        table.append("Total Members: " + count);

        memberTable.setText(table.toString());
    }


    // ========================================================
    // SEARCH BOOK
    // ========================================================

    void searchBook() {

        String search = searchField.getText().trim();

        if (search.isEmpty()) {
            searchTable.setText("Please enter a book title or author name.");
            return;
        }

        StringBuilder table = bookTableHeader();
        boolean found = false;

        String sql = "SELECT * FROM books "
                + "WHERE LOWER(title) LIKE ? OR LOWER(author) LIKE ? "
                + "ORDER BY book_id";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            String pattern = "%" + search.toLowerCase() + "%";
            ps.setString(1, pattern);
            ps.setString(2, pattern);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    appendBookRow(table, rs);
                    found = true;
                }
            }
        }
        catch (SQLException e) {
            searchTable.setText("Database error: " + e.getMessage());
            return;
        }

        table.append(LINE);

        if (found) {
            searchTable.setText(table.toString());
        }
        else {
            searchTable.setText("No book found matching: " + search);
        }
    }


    // ========================================================
    // ISSUE BOOK
    // ========================================================

    void issueBook() {

        try {
            int bookId = Integer.parseInt(issueBookIdField.getText().trim());
            int memberId = Integer.parseInt(issueMemberIdField.getText().trim());

            try (Connection con = DBConnection.getConnection()) {

                // Check member
                String memberName = null;

                try (PreparedStatement ps = con.prepareStatement(
                        "SELECT name FROM members WHERE member_id = ?")) {

                    ps.setInt(1, memberId);

                    try (ResultSet rs = ps.executeQuery()) {
                        if (rs.next()) {
                            memberName = rs.getString("name");
                        }
                    }
                }

                if (memberName == null) {
                    issueInfo.setText("ERROR\n\nMember ID " + memberId
                            + " is not registered.");
                    return;
                }

                // Check book
                String title = null;
                boolean issued = false;

                try (PreparedStatement ps = con.prepareStatement(
                        "SELECT title, issued FROM books WHERE book_id = ?")) {

                    ps.setInt(1, bookId);

                    try (ResultSet rs = ps.executeQuery()) {
                        if (rs.next()) {
                            title = rs.getString("title");
                            issued = rs.getBoolean("issued");
                        }
                    }
                }

                if (title == null) {
                    issueInfo.setText("Book ID not found.");
                    return;
                }

                if (issued) {
                    issueInfo.setText("BOOK NOT AVAILABLE\n\nThe book \""
                            + title + "\" is already issued.");
                    return;
                }

                // Issue (both updates together in one transaction)
                con.setAutoCommit(false);

                try {
                    try (PreparedStatement ps = con.prepareStatement(
                            "UPDATE books SET issued = TRUE WHERE book_id = ?")) {
                        ps.setInt(1, bookId);
                        ps.executeUpdate();
                    }

                    try (PreparedStatement ps = con.prepareStatement(
                            "INSERT INTO issue_records "
                                    + "(book_id, member_id, issue_date) "
                                    + "VALUES (?, ?, CURDATE())")) {
                        ps.setInt(1, bookId);
                        ps.setInt(2, memberId);
                        ps.executeUpdate();
                    }

                    con.commit();
                }
                catch (SQLException e) {
                    con.rollback();
                    throw e;
                }

                issueInfo.setText("BOOK ISSUED SUCCESSFULLY\n\n"
                        + "Book ID    : " + bookId
                        + "\nBook Title : " + title
                        + "\nMember ID  : " + memberId
                        + "\nMember     : " + memberName
                        + "\n\nStatus: ISSUED");
            }
        }
        catch (NumberFormatException e) {
            issueInfo.setText("Please enter valid numeric IDs.");
        }
        catch (SQLException e) {
            issueInfo.setText("Database error: " + e.getMessage());
        }
    }


    // ========================================================
    // RETURN BOOK
    // ========================================================

    void returnBook() {

        try {
            int bookId = Integer.parseInt(returnBookIdField.getText().trim());

            try (Connection con = DBConnection.getConnection()) {

                String title = null;
                boolean issued = false;

                try (PreparedStatement ps = con.prepareStatement(
                        "SELECT title, issued FROM books WHERE book_id = ?")) {

                    ps.setInt(1, bookId);

                    try (ResultSet rs = ps.executeQuery()) {
                        if (rs.next()) {
                            title = rs.getString("title");
                            issued = rs.getBoolean("issued");
                        }
                    }
                }

                if (title == null) {
                    returnInfo.setText("Book ID not found.");
                    return;
                }

                if (!issued) {
                    returnInfo.setText("BOOK IS ALREADY AVAILABLE\n\nBook: "
                            + title);
                    return;
                }

                con.setAutoCommit(false);

                try {
                    try (PreparedStatement ps = con.prepareStatement(
                            "UPDATE books SET issued = FALSE WHERE book_id = ?")) {
                        ps.setInt(1, bookId);
                        ps.executeUpdate();
                    }

                    try (PreparedStatement ps = con.prepareStatement(
                            "UPDATE issue_records SET return_date = CURDATE() "
                                    + "WHERE book_id = ? AND return_date IS NULL")) {
                        ps.setInt(1, bookId);
                        ps.executeUpdate();
                    }

                    con.commit();
                }
                catch (SQLException e) {
                    con.rollback();
                    throw e;
                }

                returnInfo.setText("BOOK RETURNED SUCCESSFULLY\n\n"
                        + "Book ID    : " + bookId
                        + "\nBook Title : " + title
                        + "\nStatus     : AVAILABLE");
            }
        }
        catch (NumberFormatException e) {
            returnInfo.setText("Enter a valid numeric Book ID.");
        }
        catch (SQLException e) {
            returnInfo.setText("Database error: " + e.getMessage());
        }
    }


    // ========================================================
    // STATISTICS
    // ========================================================

    void updateStatistics() {

        int total = 0, issued = 0, members = 0;

        try (Connection con = DBConnection.getConnection();
             Statement st = con.createStatement()) {

            try (ResultSet rs = st.executeQuery(
                    "SELECT COUNT(*) AS total, "
                            + "COALESCE(SUM(issued), 0) AS issued FROM books")) {
                if (rs.next()) {
                    total = rs.getInt("total");
                    issued = rs.getInt("issued");
                }
            }

            try (ResultSet rs = st.executeQuery(
                    "SELECT COUNT(*) AS c FROM members")) {
                if (rs.next()) {
                    members = rs.getInt("c");
                }
            }
        }
        catch (SQLException e) {
            showMessage("Database error: " + e.getMessage());
            return;
        }

        totalBooks.setText("TOTAL BOOKS\n" + total);
        availableBooks.setText("AVAILABLE BOOKS\n" + (total - issued));
        issuedBooks.setText("ISSUED BOOKS\n" + issued);
        totalMembers.setText("TOTAL MEMBERS\n" + members);
    }


    // ========================================================
    // HELPERS
    // ========================================================

    String limit(String text, int length) {

        if (text.length() <= length) {
            return text;
        }

        return text.substring(0, length - 3) + "...";
    }

    void showMessage(String message) {

        Dialog dialog = new Dialog(this, "Library Management", true);
        dialog.setSize(450, 250);
        dialog.setLayout(new BorderLayout(10, 10));

        Label messageLabel = new Label(message, Label.CENTER);
        messageLabel.setFont(new Font("Arial", Font.BOLD, 15));
        dialog.add(messageLabel, BorderLayout.CENTER);

        Button ok = new Button("OK");
        ok.setFont(buttonFont);
        ok.setBackground(blue);
        ok.setForeground(white);
        ok.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dialog.dispose();
            }
        });
        dialog.add(ok, BorderLayout.SOUTH);

        dialog.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                dialog.dispose();
            }
        });

        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }


    // ========================================================
    // ACTION LISTENER
    // ========================================================

    public void actionPerformed(ActionEvent e) {

        String command = e.getActionCommand();

        switch (command) {

            case "DASHBOARD":
                cardLayout.show(mainPanel, "DASHBOARD");
                break;

            case "BOOK_PAGE":
                displayBooks();
                cardLayout.show(mainPanel, "BOOK_PAGE");
                break;

            case "MEMBER_PAGE":
                displayMembers();
                cardLayout.show(mainPanel, "MEMBER_PAGE");
                break;

            case "SEARCH_PAGE":
                cardLayout.show(mainPanel, "SEARCH_PAGE");
                break;

            case "ISSUE_PAGE":
                cardLayout.show(mainPanel, "ISSUE_PAGE");
                break;

            case "RETURN_PAGE":
                cardLayout.show(mainPanel, "RETURN_PAGE");
                break;

            case "STATISTICS_PAGE":
                updateStatistics();
                cardLayout.show(mainPanel, "STATISTICS_PAGE");
                break;

            case "ADD_BOOK":
                addBook();
                break;

            case "VIEW_BOOKS":
                displayBooks();
                break;

            case "REMOVE_BOOK":
                removeBook();
                break;

            case "ADD_MEMBER":
                addMember();
                break;

            case "VIEW_MEMBERS":
                displayMembers();
                break;

            case "SEARCH_BOOK":
                searchBook();
                break;

            case "ISSUE_BOOK":
                issueBook();
                break;

            case "RETURN_BOOK":
                returnBook();
                break;

            case "REFRESH_STATS":
                updateStatistics();
                break;

            case "EXIT":
                System.exit(0);
                break;
        }
    }


    // ========================================================
    // MAIN
    // ========================================================

    public static void main(String[] args) {

        new LibraryManagement();
    }
}