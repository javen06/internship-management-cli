# Internship Placement Management System (IPMS)

This is a command-line interface (CLI) application for managing internship placements, built for the SC2002/CE2002/CZ2002 Object-Oriented Design & Programming course. It connects three user roles Students, Company Representatives, and Career Center Staff to manage the entire internship application lifecycle.


## Project Documents

All supporting documentation for this project can be found in the `/Documents` folder of this repository:

| Document | Description | Link |
|----------|-------------|------|
| Final Report | Assignment write-up and declarations | [Report](Report%20and%20Supporting%20Documents/SCE4-grp4.pdf) |
| UML Sequence Diagrams | Internship workflow diagram | [Folder](Report%20and%20Supporting%20Documents/) |
| UML Class Diagram | System architecture diagram | [Class Diagram](Report%20and%20Supporting%20Documents/SCE4-Grp4UML-ClassDiagram.png) |
| Javadoc | Generated documentation | [Javadoc](javadoc) |
| Test Cases | Full test plan | [Test Cases](Report%20and%20Supporting%20Documents/Group-4-Test-Cases.pdf) |



The system is built in Java and follows a 3-Tier (Model-View-Controller) architecture, separating concerns into:
* `boundary`: UI and console I/O
* `controller`: Business logic
* `datastore`/`placement`/`user`: Data models and in-memory storage

## Prerequisites

* Java Development Kit (JDK) 8 or higher
* A Java IDE (e.g., IntelliJ, Eclipse) or the command line

## How to Run

### 1. Data File Setup

This application loads initial data from CSV files. Before running the application, you **must** place the following files in the project's root directory:

* `sample_student_list.csv`
* `sample_company_representative_list.csv`
* `sample_staff_list.csv`

The application will fail to start if these files are missing.

### 2. Compilation and Execution

1.  **Compile all `.java` files.** If you are not using an IDE, you can navigate to the `src` directory and run:
    ```bash
    javac *.java */*.java */*/*.java
    ```
2.  **Navigate to the root `src` directory** (or the directory containing the `Main.class` file).
3.  **Run the application:**
    ```bash
    java Main
    ```
    The program will start from the `Main.java` file.

## How to Use the Application

The application starts with a main authentication menu. The default password for all pre-loaded users is **`password`**.

### User Roles & Features

#### 1. Student

* **Login:** Use your Student ID (e.g., `U2345123F`) from the `sample_student_list.csv` file and the default password.
* **Features:**
    * **View Available Internships:** See a list of internships filtered for your major and year of study (Year 1/2 students only see "Basic" level).
    * **Apply for Internship:** Apply for an internship from the available list. You can have a maximum of 3 pending or successful applications.
    * **View My Applications:** See the status of all your applications ("Pending", "Successful", "Unsuccessful").
    * **Accept Internship Offer:** If an application is "Successful," you can accept it. This will automatically withdraw all your other applications.
    * **Withdraw Application:** Request to withdraw an application. This request must be approved by a Career Staff member.
    * **Edit Filters:** Change your personal filter preferences for viewing internships.
    * **Change Password**.

#### 2. Company Representative

* **Registration:** A new representative must first select "Register as Company Representative" from the main menu. Your account will be **"PENDING"** until a Career Staff member approves it.
* **Login:** Once approved, log in using your **Email Address** (not your ID) and your chosen password.
* **Features:**
    * **Create New Internship:** Post a new internship. It will be "PENDING" until approved by staff. You can have up to 5 active (Pending or Approved) internships.
    * **View/Edit/Delete Pending Internships:** You can modify or delete any internship that has not yet been reviewed by staff.
    * **Toggle Internship Visibility:** For your *approved* internships, you can toggle them "on" or "off" to make them visible or invisible to students.
    * **View & Process Applications:** View all applications for your internships and "Approve" (Successful) or "Reject" (Unsuccessful) them.
    * **Edit Filters:** Change your personal filter preferences for viewing your internships.
    * **Change Password**.

#### 3. Career Center Staff

* **Login:** Use your Staff ID (e.g., `CCSTAFF1`) from the `sample_staff_list.csv` file and the default password.
* **Features:**
    * **Process Company Rep Accounts:** View a list of "PENDING" company representatives and "Approve" (activates them) or "Reject" (deletes them).
    * **Process Internship Submissions:** View a list of "PENDING" internships and "Approve" or "Reject" them.
    * **Process Withdrawal Requests:** View and "Approve" or "Reject" withdrawal requests submitted by students.
    * **View Comprehensive Report:** Generate a full report of all internships in the system, filterable by status, major, company, etc..
    * **Edit Report Filters:** Modify the filters used to generate the report.
    * **Change Password**.
