# Schedule of Development Activities (5-Week Plan)

| Week       | Focus Area                             | Tasks                                                                                                                                                                                                                                                      | Assigned Members                            |
|------------|-----------------------------------------|------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|---------------------------------------------|
| **Week 1** *(Completed)* | UI Design, Login & Notification Setup | - Completed initial front-end UI (user & admin pages)<br> - Implemented basic map display<br> - Implemented the app's notification system (Factory Method pattern) and login logic for both users and admins<br> - Completed the UML design for the system | Eden, Yudong, Larry, Nanxuan, Weimiao       |
| **Week 2** *(Current)*   | JSON Integration & Logic Planning      | - Migrated user data from database to JSON<br> - Planned data storage structure and read/write logic<br> - Initial exploration of restricted parking zone logic on the map                                                                                 | Weimiao, Nanxuan, Larry                     |
| **Week 3**               | Backend & API Implementation           | - Implement full backend logic (Room DB, field/file structure)<br> - Develop APIs for registration, login, and user actions<br> - Add map overlay for restricted parking zones and implement logic<br> - Build out the Admin UI features                   | Weimiao, Nanxuan, Larry, Eden, Yudong       |
| **Week 4**               | Frontend ↔ Backend Integration & Fixes | - Connect frontend with backend APIs<br> - Complete user report and admin review workflows<br> - Fix auto-login bug after registration<br> - Finalise logic for detecting illegal parking based on map overlays                                            | All members                                 |
| **Week 5**               | Testing & Optimisation                 | - Write individual module test cases<br> - Optimise data read/write (e.g., tree structure)<br> - Conduct performance tests and UI refinement<br> - Final system validation and presentation prep                                                           | All members (tests by individual modules)   |

# Task Assignment Overview

| Member         | Main Responsibilities |
|----------------|------------------------|
| **Weimiao Sun** | Responsible for JSON data structure, Room database integration, backend API development, bug fixing, and performance optimisation (e.g., tree-based storage). |
| **Nanxuan Xie** | Assists with backend architecture, user behaviour logging, and integration of login/notification logic. |
| **Larry Wang**  | In charge of map module development, including GPS positioning, restricted parking zone overlay, photo capture, and illegal parking detection logic. |
| **Eden Tian**   | Focuses on UI design and refinement (user and admin views), frontend adjustments, and interface integration. |
| **Yudong Qiu**  | Works on UI component development, adapter logic, and assists with frontend testing and optimisation. |

