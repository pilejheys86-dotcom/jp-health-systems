JHEYS TRISTAN KEITH A. PILE
BSIT-MWA ITE242
JP HEALTH SYSTEMS

The JP Health Systems - Medical Record Keeper is a comprehensive Java-based desktop application designed to efficiently manage and organize medical records 
for students (Senior High School and College) and personnel within an educational institution. Featuring a user-friendly Graphical User Interface (GUI), this 
system provides robust Create, Read, Update, and Delete (CRUD) functionalities, ensuring data integrity and accessibility. Leveraging Object-Oriented Programming (OOP) 
principles, the application offers tailored data entry forms and viewing options for each client type, streamlining record management and enhancing user experience.

Functionality and Features:

**Secure Login:**
    * Provides a secure login interface with username and password fields, a "show password" option, and clear "OK" and "Cancel" actions.
    * Implements basic credential validation.
**Centralized Dashboard:**
    * Offers a main dashboard with an activity log, displaying real-time updates on record additions, modifications, and deletions.
    * Activity log includes: Date, Time, Name, Client Type (Senior High School, College, Personnel), and Status.
**Comprehensive Record Management (CRUD):**
    **Create:**
        * Allows users to add new records with specific forms for Senior High School, College, and Personnel.
        * Forms include detailed personal information, medical history, and physical examination sections.
        * Dynamic form elements (combo boxes, check boxes, date pickers) enhance data entry accuracy.
        * Automated age calculation from birthdate.
    **Read/View:**
        * Presents client profiles in an easily navigable format, organized alphabetically and by client type.
        * Displays all stored information, including personal details, medical history, and physical examination results.
    **Update/Edit:**
        * Enables users to modify existing records, ensuring data accuracy and up-to-dateness.
    **Delete:**
        * Provides the ability to remove records when necessary, with appropriate confirmation prompts.
**Client-Specific Data Entry:**
    * Tailored forms for Senior High School, College, and Personnel, capturing relevant information for each client type.
    **Personnel Form Example:**
       * Includes radio buttons to specify whether the personnel is "Faculty" or "Non-teaching."
       * Enforces a specific format for personnel ID numbers through validation.
**Medical History and Physical Examination:**
   * Includes detailed sections for medical history (allergies, asthma, etc.) and physical examination (BP, PR, weight, etc.).
   * Gyne/Obstetrical option is disabled for male clients.
   * Physical examination date and time are automatically generated.
**Data Validation and Security:**
   * Implements data validation (RegEx, error handling) to ensure data integrity.
   * Provides error message boxes for user feedback.
   * Exit confirmation prompt.
**Object-Oriented Design:**
   * Utilizes OOP principles to create modular and maintainable code.
   * Uses separate classes for each client type.
   * Uses abstract parent classes, and child classes that override parent functionality.
   * Encapsulates data and methods within classes.
