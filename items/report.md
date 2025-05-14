# [G05 - Team 55FiftyFive] Report

## Table of Contents

1. [Team Members and Roles](#team-members-and-roles)
2. [Summary of Individual Contributions](#summary-of-individual-contributions)
3. [Application Description](#application-description)
4. [Application UML](#application-uml)
5. [Application Design and Decisions](#application-design-and-decisions)
6. [Summary of Known Errors and Bugs](#summary-of-known-errors-and-bugs)
7. [Testing Summary](#testing-summary)
8. [Implemented Features](#implemented-features)
9. [Team Meetings](#team-meetings)
10. [Conflict Resolution Protocol](#conflict-resolution-protocol)

## Administrative

- Firebase Repository Link:I do not use Firebase
   - Confirm: [ ] I have already added comp21006442@gmail.com as a Editor to the Firebase project prior to due date.
- Two user accounts for markers' access are usable on the app's APK (do not change the username and password unless there are exceptional circumstances. Note that they are not real e-mail addresses in use):
   - Username: comp2100@anu.edu.au	Password: comp2100 [x] 
   - Username: comp6442@anu.edu.au	Password: comp6442 [x] 

## Team Members and Roles

| UID        | Name         | Role                | Description                                                               |
|------------|--------------|---------------------|---------------------------------------------------------------------------|
| u7807744   | Larry Wang   | Map Module Lead     | Responsible for spatial location features and map interactions            |
| u7807670   | Eden Tian    | UI/UX Designer      | Focused on visual layout design, user interaction and team administration |
| u7864325   | Weimiao Sun  | Backend Developer   | In charge of data management, storage design, and system logic            |
| u7937030   | Yudong Qiu   | Frontend Developer  | Implements and connects UI elements for both user/admin views             |
| u8016457   | Nanxuan Xie  | Technical Lead      | Oversees codebase quality, structure, and integration strategy            |



## Summary of Individual Contributions
1. **u7807670,EdenTian**  I have 20% contribution, as follows: <br>
- **Code Contribution in the final App**
    - Basic Feature [UIUX] – [layout.xml](https://gitlab.cecs.anu.edu.au/u7937030/gp-25s1/-/tree/main/app/src/main/res/layout)
    - Custom Feature [DataStream]- TODO:Link to be added

- **Code and App Design**
    - Designed all UI components with Canva- [Canva Design Link](https://www.canva.com/design/DAGkT1Vehbg/Flnu_jIR4qnpe40JJucXzg/edit?utm_content=DAGkT1Vehbg&utm_campaign=designshare&utm_medium=link2&utm_source=sharebutton)
    - Designed App Logo and colour scheme

- **Others**
    - Execute tests for [Search] feature - [Test/Search.java](https://gitlab.cecs.anu.edu.au/u7937030/gp-25s1/-/tree/main/app/src/androidTest/java/com/example/parkingreport/search)
    - Report writing and checkpoint admin files preparation- [Report.md](https://gitlab.cecs.anu.edu.au/u7937030/gp-25s1/-/blob/main/items/report.md)
    - Draft Manual - [Manual.pdf](https://gitlab.cecs.anu.edu.au/u7937030/gp-25s1/-/blob/main/items/Manual.pdf)
    - Slides preparation and MinuteMadness presentation - [MinuteMadness.pptx](https://anu365-my.sharepoint.com/:p:/r/personal/u3223587_anu_edu_au/Documents/Teaching/2025/COMP2100/Semester%201%202025/MM/MinuteMadness.S1.2025.pptx?d=w7bf8296bc94b4da6b311f118b9379b15&csf=1&web=1&e=LLSC2V)
  
2. **u7807744,LarryWang**  I have 20% contribution, as follows: 
- **Code Contribution in the final App**
    - Basic Feature [Search] – [search.java](https://gitlab.cecs.anu.edu.au/u7937030/gp-25s1/-/tree/main/app/src/main/java/com/example/parkingreport/search)
    - Custom Feature [Data-GPS]- [GPS.java](https://gitlab.cecs.anu.edu.au/u7937030/gp-25s1/-/blob/main/app/src/main/java/com/example/parkingreport/utils/GPS.java?ref_type=heads) [MapFragment.java](https://gitlab.cecs.anu.edu.au/u7937030/gp-25s1/-/blob/main/app/src/main/java/com/example/parkingreport/ui/user/fragment/MapFragment.java?ref_type=heads)

- **Code and App Design**
    - Proposed Google API GPS integration strategy and zone validation logic 
    - Designed visual structure of map overlays and feedback interactions 
    - Designed grammer of tokenizer and parser

- **Others**
    - Execute tests for data and map function - TODO


3. **u7864325,Weimiao Sun**  I have 20% contribution, as follows: <br>
- **Code Contribution in the final App**
    - Basic Feature [DataFiles] – [data/local.java](https://gitlab.cecs.anu.edu.au/u7937030/gp-25s1/-/tree/main/app/src/main/java/com/example/parkingreport/data?ref_type=heads)
    - Basic Feature [LoadShowData] - [data/local.java](https://gitlab.cecs.anu.edu.au/u7937030/gp-25s1/-/tree/main/app/src/main/java/com/example/parkingreport/data?ref_type=heads)
    - Custom Feature [Doc-History-Log]- [ViewModel.java](https://gitlab.cecs.anu.edu.au/u7937030/gp-25s1/-/tree/main/app/src/main/java/com/example/parkingreport/data/local/viewModel)

- **Code and App Design**
    - Designed [DAO] for all user and admin data - [dao.java](https://gitlab.cecs.anu.edu.au/u7937030/gp-25s1/-/tree/main/app/src/main/java/com/example/parkingreport/data/local/dao?ref_type=heads)
    - Proposed performance upgrade: switch from `ArrayList` to AVL Tree + `HashMap`

- **Others**
    - Added test cases and refined UML model structure– [UML.pdf](https://gitlab.cecs.anu.edu.au/u7937030/gp-25s1/-/blob/main/ParkingReportUML-1.0.pdf?ref_type=heads)

4. **u8016457,Nanxuan Xie**  I have 20% contribution, as follows: <br>
- **Code Contribution in the final App**
    - Basic Feature [LogIn] – [login.java](https://gitlab.cecs.anu.edu.au/u7937030/gp-25s1/-/tree/main/app/src/main/java/com/example/parkingreport/ui/login?ref_type=heads)
    - Basic Feature [Process-Permission] - [admin.java](https://gitlab.cecs.anu.edu.au/u7937030/gp-25s1/-/tree/main/app/src/main/java/com/example/parkingreport/ui/admin?ref_type=heads)
  
- **Code and App Design**
    - Designed AVL Tree-based search structure and tokenizer grammar - [AVLTree.java](https://gitlab.cecs.anu.edu.au/u7937030/gp-25s1/-/tree/main/app/src/main/java/com/example/parkingreport/utils/structures?ref_type=heads)
    - Applied [Factory Method] for notifications function - [service.java](https://gitlab.cecs.anu.edu.au/u7937030/gp-25s1/-/tree/main/app/src/main/java/com/example/parkingreport/service?ref_type=heads)
    - Applied [Singleton] for login function - [LogIn] – [login.java](https://gitlab.cecs.anu.edu.au/u7937030/gp-25s1/-/tree/main/app/src/main/java/com/example/parkingreport/ui/login?ref_type=heads)

- **Others**
    - Coordinated all member's merge requests and resolved backend conflicts.
    - Impletmented 2500+ local report data.
    - Record feature demo video.

5. **u7937030,Yudong Qiu**  I have 20% contribution, as follows: <br>
- **Code Contribution in the final App**
    - Basic Feature [UIFeedback] – [ui.java](https://gitlab.cecs.anu.edu.au/u7937030/gp-25s1/-/tree/main/app/src/main/java/com/example/parkingreport/ui?ref_type=heads)
    - Basic Feature [UI-Layout] - [layout.xml](https://gitlab.cecs.anu.edu.au/u7937030/gp-25s1/-/tree/main/app/src/main/res/layout?ref_type=heads)
    - Custom Feature [User Profile] - [UserProfileFragment.java](../app/src/main/java/com/example/parkingreport/ui/user/UserProfileFragment.java), [JsonUserDao.java](../app/src/main/java/com/example/parkingreport/data/local/dao/JsonUserDao.java)
    - Surprise Feature [LLM Model] - [LLM.java](https://gitlab.cecs.anu.edu.au/u7937030/gp-25s1/-/tree/main/app/src/main/java/com/example/parkingreport/LLM?ref_type=heads)


- **Code and App Design**
    - Designed fragment layout switching and adaptive UI logic - [MyFragment.java](https://gitlab.cecs.anu.edu.au/u7937030/gp-25s1/-/tree/main/app/src/main/java/com/example/parkingreport/ui/admin/fragment?ref_type=heads)
    - Applied responsive design rules for various screen sizes

- **Others**
    - Frontend and Backend integration

    
## Application Description

ParkReport is a dual-role mobile application designed to promote civic engagement in maintaining urban traffic order by reporting illegal parking incidents. It leverages GPS, real-time data capture, and a searchable reporting system to connect users and local council administrators in managing public parking compliance.

### Problem Statement

Illegal parking remains a persistent issue in urban environments, significantly disrupting traffic flow, compromising pedestrian safety, and straining municipal enforcement resources. While councils actively issue fines to deter violations, enforcement coverage is limited by manpower and surveillance reach.

According to official data and media reports, Sydney councils issued 822,310 paperless parking fines in the 2023/24 fiscal year — a 49% increase from the previous year — generating over AUD $226 million in revenue, or approximately $25,798 per hour (9News, 2024; Yahoo News, 2024). Despite these figures, many illegal parking incidents remain unnoticed or unreported, particularly in residential or less-patrolled zones.

To bridge this enforcement gap, ParkReport empowers everyday citizens to participate in urban traffic governance. By allowing users to report illegal parking through location-verified, photo-based submissions in designated no-parking zones, the app enhances enforcement efficiency while promoting civic engagement in maintaining public order.

### Application Use Cases and/or Examples

**User Role**:

- A citizen notices a car parked illegally in a designated no-parking zone.

- The app checks their location via GPS. If valid, the user is allowed to submit a report.

- The user takes a photo or uploads an image and enters the vehicle’s plate number.

- The user can view past submissions and monitor their review status (approved/denied).

**Admin Role**:

- Admin logs in to review all incoming reports.

- Reports are searchable by username and car plate.

- Admin checks the attached photo and validates the car plate format.

- Admin can approve or deny the report based on evidence.

- Once actioned, the user receives a status update.

**Target Users**:

- Concerned residents and pedestrians who witness parking violations.

- Local government or council administrators responsible for enforcing parking regulations.

**Value Proposition**

- Encourages public participation in maintaining traffic and pedestrian safety.

- Supports councils in extending surveillance and evidence collection.

- Leverages mobile systems and real-time location data for efficient reporting.



### Application UML

![UML Diagram](media/uml/ParkingReportUML-1.0.png)

<hr>

## Code Design and Decisions

This section outlines the design principles and implementation decisions that underpin the ParkReport application, including the structure of the backend, search mechanism, file I/O design, and the rationale behind key architectural components.

<hr>

### Project Architecture Overview
The system follows an MVVM-inspired layered architecture with modular decomposition. Core layers include:

- **data layer**: Manages entity definition (`entities`), file-based persistence (`dao`), and access logic (`repository`).
- **viewModel layer**: Supplies data to the UI while managing lifecycle consistency.
- **ui layer**: Contains screens, fragment structures, and adapters for both admin and user roles.
- **utils and services**: Provide shared structures (e.g., AVL tree), background services (e.g., notification delivery), and search engine components.

### Data Structures

1. *AVLTree*
    * *Objective: used to store and retrieve all reports (`reportTree`), and users (`userTree`) to support fast insertion, deletion, and sorted traversal.*
    * *Code Locations: defined in [AVLTree.java](https://gitlab.cecs.anu.edu.au/u7937030/gp-25s1/-/tree/main/app/src/main/java/com/example/parkingreport/utils/structures?ref_type=heads); integrated in [JsonReportDao.java](https://gitlab.cecs.anu.edu.au/u7937030/gp-25s1/-/blob/main/app/src/main/java/com/example/parkingreport/data/local/dao/JsonReportDao.java?ref_type=heads), [JsonUserDao.java](https://gitlab.cecs.anu.edu.au/u7937030/gp-25s1/-/blob/main/app/src/main/java/com/example/parkingreport/data/local/dao/JsonUserDao.java?ref_type=heads); referenced in [ReportRepository.java](https://gitlab.cecs.anu.edu.au/u7937030/gp-25s1/-/tree/main/app/src/main/java/com/example/parkingreport/data/local/repository?ref_type=heads).*
    * *Reasons:*
        * *Balanced binary search tree ensures O(log n) time for insert, delete, and lookup.*
        * *Used to keep both reports and users sorted by ID and support real-time query within large in-memory datasets.*
        * *Preferred over TreeMap or sorted lists due to custom comparator control and strict balancing.*
        * *User and report trees are reused in multiple modules (reporting, login, admin management), improving consistency across DAO and repository layers.*

2. *HashMap*
    * *Objective: used for fast reverse lookups of reports by user ID (`userIdMap`) and car plate number (`plateMap`).*
    * *Code Locations: defined in [JsonReportDao.java](https://gitlab.cecs.anu.edu.au/u7937030/gp-25s1/-/blob/main/app/src/main/java/com/example/parkingreport/data/local/dao/JsonReportDao.java?ref_type=heads), [JsonUserDao.java](https://gitlab.cecs.anu.edu.au/u7937030/gp-25s1/-/blob/main/app/src/main/java/com/example/parkingreport/data/local/dao/JsonUserDao.java?ref_type=heads); integrated in search filter logic of [Search.java](https://gitlab.cecs.anu.edu.au/u7937030/gp-25s1/-/tree/main/app/src/main/java/com/example/parkingreport/search?ref_type=heads).*
    * *Reasons:*
        * *Provides O(1) access to report or user data by unique key (email, plate).*
        * *Essential for efficient user search, duplication checks, and filtering reports in admin view.*
        * *Allows direct mapping from input tokens to IDs, minimizing lookup time and avoiding list scans.*

3. *ArrayList*
    * *Objective: used to store full JSON-loaded objects (users, reports, logs) and search result sets for UI rendering.*
    * *Code Locations: [JsonReportDao.java](https://gitlab.cecs.anu.edu.au/u7937030/gp-25s1/-/blob/main/app/src/main/java/com/example/parkingreport/data/local/dao/JsonReportDao.java?ref_type=heads), [JsonUserDao.java](https://gitlab.cecs.anu.edu.au/u7937030/gp-25s1/-/blob/main/app/src/main/java/com/example/parkingreport/data/local/dao/JsonUserDao.java?ref_type=heads)
    * *Reasons:*
        * *Chosen over LinkedList or TreeSet due to O(1) append performance and minimal memory overhead.*
        * *Used as intermediate buffers for reading from and writing to disk in JSON DAOs.*
        * *Supports sequential traversal and index-based access, allowing UI components like RecyclerView to bind cleanly.*
        * *Search results and recent reports are reversed (using O(n)) to show newest entries first, benefiting user experience.*


<hr>

### Design Patterns
1. *Factory Method Pattern*
    * *Objective: used for instantiating `Notification` objects (e.g., Email, SMS) based on notification type without hard-coding their concrete classes.*
    * *Code Locations: defined in [NotificationFactory.java](https://gitlab.cecs.anu.edu.au/u7937030/gp-25s1/-/blob/main/app/src/main/java/com/example/parkingreport/service/NotificationFactory.java?ref_type=heads); used in [SignUpActivity.java](https://gitlab.cecs.anu.edu.au/u7937030/gp-25s1/-/blob/main/app/src/main/java/com/example/parkingreport/ui/login/SignUpActivity.java?ref_type=heads) and service logic like [EmailService.java](https://gitlab.cecs.anu.edu.au/u7937030/gp-25s1/-/tree/main/app/src/main/java/com/example/parkingreport/service?ref_type=heads).*
    * *Reasons:*
        * *Provides flexible and decoupled creation of notification handlers (e.g., `EMAIL`, `SMS`).*
        * *Supports future extension with new notification types without modifying existing logic.*
        * *Follows the Open/Closed Principle in SOLID.*

2. *Singleton Pattern*
    * *Objective: ensures a single instance of repository classes (e.g., `UserRepository`, `ReportRepository`) and DAOs for consistent access to in-memory and file-based storage.*
    * *Code Locations: `getInstance()` methods in [UserRepository.java](https://gitlab.cecs.anu.edu.au/u7937030/gp-25s1/-/blob/main/app/src/main/java/com/example/parkingreport/data/local/repository/UserRepository.java?ref_type=heads), [ReportLogRepository.java](https://gitlab.cecs.anu.edu.au/u7937030/gp-25s1/-/blob/main/app/src/main/java/com/example/parkingreport/data/local/repository/ReportLogRepository.java?ref_type=heads).*
    * *Reasons:*
        * *Avoids repeated object creation and ensures shared state across ViewModels and fragments.*
        * *Improves resource management and testing stability.*
        * *Enables safe access to shared file I/O (e.g., JSON) across multiple threads.*

3. *Data Access Object (DAO) Pattern*
    * *Objective: abstracts and encapsulates access to persistent storage (JSON file).*
    * *Code Locations: [data/local/dao,Entire File](https://gitlab.cecs.anu.edu.au/u7937030/gp-25s1/-/tree/main/app/src/main/java/com/example/parkingreport/service?ref_type=heads)
    * *Reasons:*
        * *Encapsulates all file I/O operations, ensuring clear separation of concerns.*
        * *Facilitates mockable and testable data layers.*
        * *Allows flexible swapping of JSON/Firebase without UI change.*

<hr>

### Parser

### <u>Grammar(s)</u>

**Purpose:**  
The grammar allows admin users to search reports based on `username` and `car plate`, using an explicit prefix-based syntax (`U:` or `P:`). This facilitates intuitive and structured queries such as `U:Alice+P:ABC123`.

**Production Rules:**
```
<query> ::= <term> | <term> + <term>
<term> ::= <token>
<token> ::= U:<string> | P:<string>
```
**Notes:**
- `<string>` is not formally constrained. Both `plate` and `name` are treated as raw strings.
- It is the **admin's responsibility** to input valid queries (e.g., `U:John` or `P:ABC123`).
- Admins are permitted to query by username; normal users are not.
- `+` represents a logical **OR** between token types.

**Advantages:**
- Prefixes (`U:` / `P:`) make the query system clear and extensible.
- Grammar remains simple: avoids regex or external parser dependencies.
- Future-friendly: can support `AND`, parentheses, or fuzzy search if needed.


**Tokenizer Design:**

- **Used in:**  
  The `Tokenizer` is invoked in the report search interface for admin users.

- **Logic:**
    - Input string is split by `+` into components.
    - Each component is processed via `prefixHandle()`:
        - Validates prefix: must be `U:` or `P:`
        - Constructs a `Token` with `type` (`USERNAME` or `CARPLATE`) and raw `value` (as-is string)
        - Uses `equals()` to deduplicate tokens

- **Source file:**  
  [`Tokenizer.java`](https://gitlab.cecs.anu.edu.au/u7937030/gp-25s1/-/blob/main/app/src/main/java/com/example/parkingreport/search/Tokenizer.java?ref_type=heads)  
  Core methods:
    - `tokenize(String input, String role)`
    - `prefixHandle(String value, String role)`
    - `classify(String value, String role)`


**Parser Design:**

- **Used in:**  
  The `Parser.evaluateTokens()` method processes tokens and retrieves matching reports from the database.

- **Logic:**
    - If the token type is `USERNAME`:
        - Retrieves `userId` from `UserRepository`
        - Looks up associated reports via `getIdsByUser()`
    - If the token type is `CARPLATE`:
        - Uses `getIdsByPlate()` to fetch report IDs
    - Combines results:
        - Same-type tokens = OR logic
        - Different-type tokens = AND logic (via `retainAll`)

- **Special Cases:**
    - If the role is `USER`, only that user’s reports are shown.
    - If the admin sends no query, all reports are returned.

- **Source file:**  
  [`Parser.java`](https://gitlab.cecs.anu.edu.au/u7937030/gp-25s1/-/blob/main/app/src/main/java/com/example/parkingreport/search/Parser.java?ref_type=heads)  
  Main method:
    - `evaluateTokens(List<Token>, boolean, String, int, ReportRepository, UserRepository)`

---

**Design Advantages:**

- Keeps parsing and evaluation layers modular and testable.
- Query logic is fully decoupled from UI and backend storage.
- Easy to extend with new token types or query syntax.
- Compatible with both unit tests and Android instrumented tests.



<br>
<hr>

## Implemented Features

### Basic Features

1. [LogIn]. The app must support user login functionality using existing credentials. User registration (sign-up) is not required.(easy)
    * Code: [Login, Entire File](https://gitlab.cecs.anu.edu.au/u7937030/gp-25s1/-/tree/main/app/src/main/java/com/example/parkingreport/ui/login?ref_type=heads)
    * Description of feature: Our app supports both user and admin login functionality using existing credentials (comp2100@anu.edu.au / comp2100). Allows users and admins to securely log in and access different interfaces based on their role.
    * Description of your implementation: Used Singleton pattern for login session management; login credentials validated from `JsonUserDao`. Account registration supports role assignment, and verification logic (via email/username checks) ensures valid user creation.

2. [DataFiles].The application must utilize a dataset — either provided or created by the development team — where each entry represents a meaningful piece of information relevant to the app’s functionality. The dataset must be stored in a structured format, consistent with the representations taught in the course (e.g., JSON, XML). It must include a minimum of 2,500 valid entries. (easy)
    * Code: [report.json](https://gitlab.cecs.anu.edu.au/u7937030/gp-25s1/-/blob/main/app/src/main/assets/reports.json?ref_type=heads), [report_log.json](https://gitlab.cecs.anu.edu.au/u7937030/gp-25s1/-/blob/main/app/src/main/assets/report_logs.json?ref_type=heads)
    * Description of feature: Our app dynamically reads from these files to display user and report data in real-time without requiring remote servers.Maintains dataset of user profiles, reports, logs, and metadata.JSON-formatted dataset simulates persistent storage and provides at least 2,500+ entries.
    * Description of your implementation: Used GSON for serialization/deserialization; files managed in append-only format with timestamped logs. LiveData guarantees broadcast to UI when file updates occur.

3. [LoadShowData]. The app must load and display data instances from the data set. Data must be retrieved from either a local file (e.g., JSON, XML) or Firebase. (easy)
    * Code: [JsonUserDao.java](https://gitlab.cecs.anu.edu.au/u7937030/gp-25s1/-/blob/main/app/src/main/java/com/example/parkingreport/data/local/dao/JsonUserDao.java?ref_type=heads), [ReportAdapter.java](https://gitlab.cecs.anu.edu.au/u7937030/gp-25s1/-/tree/main/app/src/main/java/com/example/parkingreport/ui/reportManager?ref_type=heads)
    * Description of feature: Our app displays the latest reports immediately on screen load and preserves scroll position even when the orientation changes. Dynamically loads user and report data into UI for both user and admin.
    * Description of your implementation: ViewModel combined with LiveData ensures that screen rotations (landscape/portrait) do not disrupt or reload data. Reports are reversed (latest first) and mapped into the adapter with reactive UI update support.

4. [DataStream]. The app must simulate user interactions through data streams. These data streams must be used to feed the app so that when a user is logged in (or enters a specific activity), the data is loaded at regular time intervals and the app is updated automatically. (medium)
    * Code: [dataStream, Entire File](/Users/larrywang/data streaming/app/src/main/java/com/example/parkingreport/dataStream),[AndroidTest/dataStream, Entire File](/Users/larrywang/data streaming/app/src/androidTest/java/com/example/parkingreport/dataStream)
    * Description of feature: Our app allows admins and users to observe updated report logs or counts in real-time without needing to manually refresh the view.Emulates a backend “push” of live data changes—e.g. when an admin reviews or a user submits a report, those changes are reflected automatically in the UI at regular intervals without manual refresh.
    * Description of your implementation: In ReportStreamManager.start() we call executor.scheduleAtFixedRate(…) to invoke UserReportStatusTask.run() and AdminReportCountTask.run() on separate schedules. Inside UserReportStatusTask.run() we use ReportRepository.fetchStatusSync(reportId) and LiveData.postValue() on ReportStatusLiveData, while AdminReportCountTask.run() calls ReportRepository.fetchAllReportsCountSync() and postValue() on ReportCountLiveData. ReportLogViewModel (and other ViewModels) observe these LiveData streams so their UI observers automatically refresh whenever new data arrives.


5. [Search]. The app must allow users to search for information. Based on the user's input, adhering to pre-defined grammar(s), a query processor must interpret the input and retrieve relevant information matching the user's query. The implementation of this functionality should align with the app’s theme. The application must incorporate a tokenizer and parser utilizing a formal grammar created specifically for this purpose. (medium)
    * Code: [Search, Entire File](https://gitlab.cecs.anu.edu.au/u7937030/gp-25s1/-/tree/main/app/src/main/java/com/example/parkingreport/search?ref_type=heads)
    * Description of feature: Our app allows admin users to locate specific report records efficiently from thousands of entries using flexible keyword rules.Enables token-based search and multi-field query support as admin role (e.g. "u:john@example.com + p:XYZ789").
    * Description of your implementation:Implemented a custom `Tokenizer` and `Parser` pipeline that processes queries according to a defined grammar. Tokens are classified with prefixes (`U:` for username, `P:` for plate), deduplicated, and evaluated using a two-phase logic:Same-type tokens (e.g. `U:Alice + U:Bob`) → 'OR' logic; different-type tokens** (e.g. `U:Alice + P:ABC123`) → 'AND logic' The `Parser` matches the token sets to stored reports using `UserRepository` and `ReportRepository`. All results are dynamically fetched and displayed within the admin's UI view. The search functionality is fully tested with Android instrumented tests to verify correctness and integration with the local database.

6. [UXUI]. The app must maintain a consistent design language throughout, including colors, fonts, and UI element styles, to provide a cohesive user experience. The app must also handle orientation changes (portrait to landscape and vice versa) gracefully, ensuring that the layout adjusts appropriately. (easy)
    * Code: [canva file](https://www.canva.com/design/DAGkT1Vehbg/Flnu_jIR4qnpe40JJucXzg/edit?utm_content=DAGkT1Vehbg&utm_campaign=designshare&utm_medium=link2&utm_source=sharebutton),[res, Entire File](https://gitlab.cecs.anu.edu.au/u7937030/gp-25s1/-/tree/main/app/src/main/res?ref_type=heads)
    * Description of feature: Our app ensures all fragments and screens align with the same UI style guide, making transitions between user and admin views smooth and visually intuitive.We applied light orange, clean modern style for our app. Res file including file [color],[drawable],[layout] which contains elements fullfilling the initial canva design.Maintains visual consistency across screens, including layout, padding, and color scheme.
    * Description of your implementation: Designed in Canva with clear wireframe flow; colors, margins, font size and button radius kept consistent; enhanced spacing and hierarchy using CardView and ConstraintLayout.

7. [UIFeedback]. The UI must provide clear and informative feedback for user actions, including error messages to guide users effectively. (easy)
    * Code: [ReportAdapter.java](../app/src/main/java/com/example/parkingreport/ui/adapter/ReportAdapter.java), [fragment,Entire File](https://gitlab.cecs.anu.edu.au/u7937030/gp-25s1/-/tree/main/app/src/main/java/com/example/parkingreport/ui/user/fragment?ref_type=heads)
    * Description of feature: Our app displays appropriate success/failure prompts to guide user decisions across multiple flows. Guides users/admins with toasts or visual cues.
    * Description of your implementation: Toasts are triggered when invalid inputs or illegal actions occur (e.g., report outside no-parking zone). Button colors, toggle states, and submission results provide additional confirmation.

---

### Custom Features

**Feature Category: Location Awareness**

1. [Data-GPS]. The app must utilize GPS information based on location data. (easy)
    * Code: [MapFragment.java](../app/src/main/java/com/example/parkingreport/ui/map/MapFragment.java),[GPS.java](https://gitlab.cecs.anu.edu.au/u7937030/gp-25s1/-/blob/main/app/src/main/java/com/example/parkingreport/utils/GPS.java?ref_type=heads)
    * Description of our feature: Our app disables report submission unless the user is currently standing inside a no-parking zone according to users' real location (real virtual machine location).
    * Description of your implementation: The `GPS` utility wraps Google’s `FusedLocationProviderClient`. It first calls `getLastLocation()` and validates the result; if it’s missing or stale, it issues a one-time high-accuracy `LocationRequest` (via `requestLocationUpdates`) to fetch a fresh fix. All calls honor runtime permission checks and deliver latitude/longitude through a simple `GpsCallback.onLocationReady(lat, lng)` interface.

2. [Data-Profile]. Allows users to view/update avatar image in their profile. (easy)
    * Code: [UserProfileFragment.java](../app/src/main/java/com/example/parkingreport/ui/user/UserProfileFragment.java), [JsonUserDao.java](../app/src/main/java/com/example/parkingreport/data/local/dao/JsonUserDao.java)
    * Description of our feature:Our app enhances user personalization by displaying chosen avatars immediately after signup.
    * Description of your implementation: On registration, user can pick an image using built-in ImagePicker; avatar is stored in profile and displayed after login; Glide handles rendering and path caching.

---

**Feature Category: UI Design and Testing**

3. [UI-Layout]. Supports both portrait and landscape screen orientations. (easy)
    * Code: [admin/fragment.java](https://gitlab.cecs.anu.edu.au/u7937030/gp-25s1/-/tree/main/app/src/main/java/com/example/parkingreport/ui/admin/fragment?ref_type=heads),  [user/fragment.java](https://gitlab.cecs.anu.edu.au/u7937030/gp-25s1/-/tree/main/app/src/main/java/com/example/parkingreport/ui/user/fragment?ref_type=heads)
    * Description of our feature: Our app handles orientation change seamlessly by maintaining state through ViewModel and fragment communication.
    * Description of your implementation: Smooth transition between orientations is achieved using layout resource qualifiers; fragment transactions and ViewModel sharing allow seamless communication (e.g. profile edits triggering UI updates).

---

**Feature Category: Document Version Control**

4. [Doc-History-Log]. Maintains append-only log of user/report changes. (medium)
    * Code: [JsonReportLogDao.java](https://gitlab.cecs.anu.edu.au/u7937030/gp-25s1/-/blob/main/app/src/main/java/com/example/parkingreport/data/local/dao/JsonReportLogDao.java?ref_type=heads), [ReportLog.java](https://gitlab.cecs.anu.edu.au/u7937030/gp-25s1/-/blob/main/app/src/main/java/com/example/parkingreport/data/local/dao/ReportLogDao.java?ref_type=heads)
    * Description of our feature: Our app allows both user- and report-related operations to be tracked through append-only log files, enabling traceable histories for each account and submission.
    * Description of your implementation: Logs are maintained using an append-only strategy. When users create an account or update their password, an entry is recorded in `UserLog.json`. When reports are submitted, approved, or declined by admins, a new entry is appended to `ReportLog.json`. These logs are timestamped, immutable, and exposed to the UI via LiveData for administrative auditing without rollback mechanisms.

---

**Feature Category: Creating Processes**

5. [Process-Permission]. Enforces role-based visibility between user and admin. (easy)
    * Code: [User.java](https://gitlab.cecs.anu.edu.au/u7937030/gp-25s1/-/blob/main/app/src/main/java/com/example/parkingreport/data/local/entities/User.java?ref_type=heads), [MainActivity.java](https://gitlab.cecs.anu.edu.au/u7937030/gp-25s1/-/blob/main/app/src/main/java/com/example/parkingreport/ui/login/MainActivity.java?ref_type=heads)
    * Description of our feature: Our app ensures strict separation between user-submitted data and admin dashboard, reflecting real-world access control policies.
    * Description of your implementation: Admin has access to all user reports for moderation; users can only view their own history. Role is evaluated at login and drives logic for fragment visibility, data scope, and permission paths.

---

<hr>

### Surprise Feature

Due to the Hugging Face API frequently returning 404 errors during testing and being almost unusable, I adopted the Gemini API from Google, following the instructor's suggested alternative, to ensure the proper implementation and demonstration of the Surprise Feature. Additionally, I integrated OpenAI's API to facilitate testing with different LLM services.

Our project implements a modular and replaceable LLM (Large Language Model) module using a combination of **Strategy Pattern** and **Factory Method Pattern**.

**Design Pattern Justification**  
To ensure LLM service flexibility and maintainability, we define a unified interface `LLMClient`, which abstracts interaction methods like `askQuestion()`. Each LLM provider (e.g., Gemini, OpenAI) implements this interface and encapsulates its API logic (request construction, error handling, response parsing). This decouples business logic from specific implementations and adheres to the Open-Closed Principle. A `LLMClientFactory` is also introduced to dynamically create the correct implementation at runtime based on configuration. This design eliminates hard-coded dependencies and simplifies future extensions to support more LLMs such as HuggingFace. As a result, the LLM module becomes highly extensible, pluggable, and testable.

**License Choice**  
We adopt the **Apache License 2.0** due to its permissiveness, legal clarity, and suitability for integration-heavy projects. Unlike copyleft licenses (e.g., GPL), Apache 2.0 permits commercial use and redistribution without imposing reciprocal obligations. This makes it ideal for applications like ours, which rely on third-party LLM APIs. Apache 2.0 explicitly covers patent rights and contributor responsibilities, helping prevent legal disputes when deploying AI modules that aggregate external APIs. It also mandates attribution and disclaimer clauses, aligning with our project's use of public service information (e.g., government FAQs). These legal safeguards ensure that downstream users and collaborators understand their rights and limitations, promoting safe and transparent reuse.

**Ethical Concern**  
A key ethical concern is the **risk of misinformation** from LLM-generated answers regarding government services. Inaccurate responses may mislead users and violate the IEEE Code of Ethics’ principles of “avoiding harm to others” and “improving public understanding of technology.” To mitigate this, we include clear UI-level disclaimers like “AI-generated content is for reference only” and recommend users consult official sources. Although automatic source verification is not implemented, this practice reduces the risk of user misunderstanding and sets transparent expectations about the nature of LLM output.


<br> <hr>


## Testing Summary

### Tests for [Search]

- **Code:**  
  [TokenizerTest Class, entire file](https://gitlab.cecs.anu.edu.au/u7937030/gp-25s1/-/blob/main/app/src/androidTest/java/com/example/parkingreport/search/TokenizerTest.java)  
  for the [Tokenizer Class, entire file](https://gitlab.cecs.anu.edu.au/u7937030/gp-25s1/-/blob/main/app/src/main/java/com/example/parkingreport/search/Tokenizer.java)

  [ParserTest Class, entire file](https://gitlab.cecs.anu.edu.au/u7937030/gp-25s1/-/blob/main/app/src/androidTest/java/com/example/parkingreport/search/ParserTest.java)  
  for the [Parser Class, entire file](https://gitlab.cecs.anu.edu.au/u7937030/gp-25s1/-/blob/main/app/src/main/java/com/example/parkingreport/search/Parser.java)

- **Number of test cases:** 8
    - TokenizerTest: **4** unit test cases
    - ParserTest: **4** functional test cases — covering username, plate number, and their intersection logic

- **Code coverage:**  
  Covers core logic in both Tokenizer and Parser: input splitting, token classification, deduplication, type filtering, and token evaluation with report repository.  
  Coverage of logic paths: **>90%** of search-related branches.

- **Types of tests created and descriptions:**
    - **Functionality tests:** Validate correct parsing and classification of prefixed search strings (`U:` and `P:`).
    - **Integration tests with database:** All `ParserTest` methods evaluate tokens against a real `ReportRepository` and `UserRepository` using local JSON-based storage.
    - **Edge case testing:** Includes scenarios with no match, multiple matches (OR), and combined constraints (AND).
    - Tests use `ApplicationProvider.getApplicationContext()` and `InstrumentationRegistry.getInstrumentation().runOnMainSync()` to ensure LiveData and I/O can run safely inside `androidTest`.

### Tests for [Map]
- **Code:**
  [GPSTest Class, entire file](https://gitlab.cecs.anu.edu.au/u7937030/gp-25s1/-/blob/main/app/src/test/java/com/example/parkingreport/MapTest/GPSTest.java)
  for the [GPS Class, partial file](https://gitlab.cecs.anu.edu.au/u7937030/gp-25s1/-/blob/main/app/src/main/java/com/example/parkingreport/utils/GPS.java)

  [isPointInPolygon Class, entire file](https://gitlab.cecs.anu.edu.au/u7937030/gp-25s1/-/blob/main/app/src/test/java/com/example/parkingreport/MapTest/isPointInPolygon.java)
  for the [PolygonUtil Class, method `isPointInPolygon(...)`](https://gitlab.cecs.anu.edu.au/u7937030/gp-25s1/-/blob/main/app/src/main/java/com/example/parkingreport/utils/PolygonUtil.java)

- **Number of test cases:** 7
  - GPSTest: **4** unit test cases
  - isPointInPolygon: **3** unit test cases

- **Code coverage:**
  Covers logic that determines whether a `Location` is valid (`GPS.isValidLocation`) and whether a point falls within a no-parking polygon (`PolygonUtil.isPointInPolygon`).
  Coverage includes input filtering (null, zero-coordinates, excluded areas) and boundary-aware geometric calculation.
  **100% method-level test coverage** for both methods.

- **Types of tests created and descriptions:**

  - **Validity logic tests (GPSTest):** Verifies four scenarios of location rejection:
        * Null input
        * (0, 0) coordinate (uninitialized)
        * Known invalid "San Jose" area
        * Normal coordinate in Australia
  - **Polygon containment logic tests (isPointInPolygon):**
        * Point strictly inside the square returns true
        * Point exactly on the edge returns true (inclusive policy)
        * Point outside the square returns false
    - **Edge case testing:** Includes lower bound of coordinate space (0.0), real-world filtering for erroneous GPS defaults, and edge behavior of point-in-polygon.
    - All tests run as standard **unit tests** in the `test/` directory (no Android-specific APIs involved). Mockito is used to mock `Location` objects in `GPSTest`.

### Tests for [DataStream]
- **Code:**
  [ReportRepositoryInstrumentedTest.java](app/src/androidTest/java/com/example/parkingreport/dataStream/ReportRepositoryInstrumentedTest.java)
  for exercising the core `ReportRepository` methods (`insertReport()`, `replyReport()`, `findReport()`, `getAllReportsLive()`).

  [ReportStreamManagerTest.java](app/src/test/java/com/example/parkingreport/dataStream/ReportStreamManagerTest.java)
  for validating the `ReportStreamManager` scheduling logic (`start()`, `stop()`, internal use of `scheduleAtFixedRate()`).

- **Number of test cases:** 4

  - **ReportRepositoryInstrumentedTest:** 2 instrumented tests

        1. Verify a new report’s default status is `WAIT_FOR_REVIEW` and flips to `APPROVED` after `replyReport()`.
        2. Verify `getAllReportsLive().size()` increments correctly after multiple `insertReport()` calls.
    - **ReportStreamManagerTest:** 2 pure-JVM unit tests

        1. Assert the “user” task runs periodically at the injected `userPeriodMs`.
        2. Assert the “admin” task runs periodically at the injected `adminPeriodMs`.

- **Code coverage:**
  Achieves **>95%** coverage of all data-stream branches, including both repository interactions and scheduler setup. Exercises every path in `ReportRepository` (JSON load/save, LiveData updates) and both constructors and scheduling calls in `ReportStreamManager`.

- **Types of tests created and descriptions:**

  - **Instrumented integration tests:**
      `ReportRepositoryInstrumentedTest` runs on-device with `ApplicationProvider.getApplicationContext()` and `InstrumentationRegistry.getInstrumentation().runOnMainSync()` to safely initialize `ReportRepository` and manipulate its backing JSON store.
    * **Pure-JVM scheduling tests:**
      `ReportStreamManagerTest` injects a custom `ScheduledExecutorService` and lambda `Runnable`s to count invocations of `userTask` and `adminTask` at ultra-short intervals, verifying that `start()`/`stop()` and `scheduleAtFixedRate()` are wired correctly.
    * **Edge-case handling:**
      Both test suites begin by deleting the existing `reports.json` and resetting the singleton instance to guarantee a clean slate for each scenario.

      
<br> <hr>


## Summary of Known Errors and Bugs

1. **Bug 1: Incomplete avatar display on low-resolution screens**
    - On devices with screen height below 720dp (e.g., certain older Android phones), the avatar selection grid during registration does not fully render, causing the last row of selectable images to be cut off.
    - This affects the user experience during account creation and may confuse users who think there are no more avatar options available.

2. **Bug 2: No night mode / dark theme support**
    - The application does not include a night mode or dark theme configuration, which could cause discomfort when used in low-light environments.
    - This may lead to visual fatigue or usability issues, especially for users accustomed to system-wide dark themes.

3. **Bug 3: No accessibility mode for visually impaired users**
    - The current colour scheme (particularly the orange accent colour used for highlights and buttons) does not meet WCAG contrast recommendations for accessibility.
    - There is no high-contrast mode or screen reader optimization. This makes the app difficult or impossible to use for visually impaired users or colour-blind individuals.

> These known issues do not directly affect core functionality but highlight the need for improved adaptive design, accessibility compliance, and UX scalability across a wider range of devices.


<br> <hr>


## Team Management

### Meeting Minutes
* Link to the minutes of your meetings like above. There must be at least 4 team meetings.
  (each committed within 2 days after the meeting)
* Your meetings should also have a reasonable date spanning across Week 6 to 11.*


- [Team Meeting 1 – 14 April 2025 (Week 6)](../items/media/2025-04-14-meeting.md)
- [Team Meeting 2 – 17 April 2025 (Week 7)](../items/media/2025-04-17-meeting.md)
- [Team Meeting 3 – 20 April 2025 (Week 8)](../items/media/2025-04-20-meeting.md)
- [Team Meeting 4 – 22 April 2025 (Week 8)](../items/media/2025-04-22-meeting.md)
- [Team Meeting 5 – 30 April 2025 (Week 9)](../items/media/2025-04-30-meeting.md)
- [Team Meeting 6 – 04 May 2025 (Week 10)](../items/media/2025-05-04-meeting.md)

Each meeting focused on milestone planning, integration tasks, bug triage, and refining UI logic. Minutes include agenda, outcomes, action points, and deadlines for each team member.

<hr>


## Conflict Resolution Protocol

Our team recognises that effective collaboration and clear communication are essential for the success of this 6–8 week project. We have established the following protocol to handle potential conflicts and unforeseen events:

### Early Project Phase – Shared Responsibility & Overlap

In the early stages of the project, we encountered some overlapping contributions due to an unclear architectural direction. For example, 2–3 members occasionally worked on similar features (e.g.layout fragments), which led to Git merge conflicts and inconsistent code.

To resolve this, we appointed **one dedicated Technical Lead** (Nanxuan Xie) who took responsibility for:

- Defining and communicating module ownership
- Approving and managing all GitLab merge requests
- Coordinating codebase structure and avoiding conflict-prone commits

This helped clarify responsibilities and streamline collaboration in the later weeks of the project.

---

### General Conflict Handling Procedure

For ongoing development, we agreed to follow this structured resolution protocol:

1. **Task Incompletion Before Checkpoints**
    - The team member must notify others in advance if they anticipate delays.
    - The Technical Lead will help redistribute tasks or delegate hotfixes to unblock the milestone delivery.

2. **Absence Due to Sickness or Emergencies**
    - If a member becomes sick, the remaining team members will prioritise essential integration tasks first.
    - Whenever possible, backup tasks or documentation will be reassigned temporarily by the Technical Lead.

3. **Unresponsiveness (48+ hours)**
    - If a member is unreachable via group chat or email for more than 2 days, the team will escalate through personal message or alternative contact.
    - The Technical Lead may temporarily reassign their work if deadlines are at risk.

4. **Disagreement on Requirements or Implementation**
    - All feature or architecture misunderstandings are to be raised during weekly meetings or on GitLab issues.
    - Team decisions will be made by consensus, with the Technical Lead guiding technical feasibility discussions.
    - If consensus is not reached, the decision with the least downstream conflict (technical or timeline) will be chosen.

**Additional Scenarios** 
In addition to major conflict cases, our team proactively addressed several recurring coordination challenges:

- Overlapping contributions during early development (e.g., layout or DAO clashes) were resolved by assigning a module owner and enforcing GitLab merge reviews.
- Unforeseen technical errors (e.g., rebase mistakes, device-specific layout bugs) were mitigated using version control recovery and temporary freezes on non-critical features.
- Insufficient meeting frequency during frontend-backend integration phase was addressed by scheduling extended weekly co-working sessions (7–8 hours) in library study rooms, allowing real-time debugging, pair testing, and design clarification.

---



### Reference
1. 9News. (2024, December 1). Sydney councils rake in $226 million in parking fines in just one year. https://www.9news.com.au/national/sydney-councils-parking-fines-millions-worth-of-fines-to-drivers-in-past-year/d7c4d64e-8824-4732-9bbf-138a7396128a
2. Yahoo News. (2024, September 18). Ticketless parking fines to be banned after councils rake in $158 million. https://au.news.yahoo.com/ticketless-parking-fines-to-be-banned-after-councils-rake-in-158-million-010200116.html

