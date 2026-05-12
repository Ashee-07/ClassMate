# ClassMate - Comprehensive Academic Management System

ClassMate is a modern Android application designed to streamline communication and resource management between students and academic administrators. Built with a focus on usability and real-time updates, it serves as a digital hub for all classroom-related activities.

## 🚀 Overview

In the fast-paced academic environment, keeping track of assignments, exams, and announcements can be overwhelming. ClassMate simplifies this by providing a centralized platform where administrators can post updates and students can access them instantly.

## 📥 Download

You can download the latest version of the application from the link below:

**[Download ClassMate APK](https://github.com/Ashee-07/ClassMate/releases)**

*(Note: If no releases are available yet, you can build the APK from source using Android Studio.)*

## ✨ Key Features

### For Students:
*   **Real-time Dashboard:** View all upcoming assignments, exams, and labs at a glance.
*   **Resource Access:** Download study materials and drive updates shared by faculty.
*   **Stay Informed:** Receive instant announcements about seminars, events, and schedule changes.
*   **Academic Tracking:** Keep track of exam marks and important dates via the integrated calendar.
*   **Modern UI:** Enjoy a clean, responsive interface with smooth animations and dark mode support.

### For Administrators:
*   **Management Console:** A dedicated dashboard to add, edit, and manage all academic content.
*   **Content Control:** Easily upload assignments, schedule exams, and post events.
*   **Analytics:** View quick stats on active notices and pending assignments.
*   **Secure Access:** Admin-only login ensures data integrity and controlled management.

## 🛠 Working Mechanism

1.  **Role Selection:** Upon launching, users choose between the **Admin** or **Student** role.
2.  **Authentication:** Admins log in using secure credentials (powered by Firebase Auth).
3.  **Data Synchronization:** All data (assignments, exams, etc.) is stored in **Firebase Realtime Database**, ensuring that any change made by an admin is instantly visible to all students.
4.  **Resource Storage:** Files and documents are managed through **Firebase Storage**, allowing for easy upload and retrieval of PDF/Image materials.
5.  **Notifications:** Critical updates are pushed to devices using **Firebase Cloud Messaging (FCM)**.

## 💻 Technical Stack

*   **Language:** Java / Kotlin
*   **Backend:** Firebase (Auth, Realtime DB, Firestore, Storage)
*   **Architecture:** Activity-based with ViewBinding for clean UI logic.
*   **Libraries Used:**
    *   **Glide:** For efficient image loading and caching.
    *   **Lottie:** For high-quality vector animations.
    *   **Shimmer:** For elegant loading states.
    *   **Material Design Components:** For a modern, standardized Android look.

## 📦 Installation & Setup

1.  Clone the repository:
    ```bash
    git clone https://github.com/Ashee-07/ClassMate.git
    ```
2.  Open the project in **Android Studio**.
3.  Add your `google-services.json` file from Firebase to the `app/` directory.
4.  Sync Project with Gradle Files.
5.  Build and Run on your emulator or physical device.

---
Developed with ❤️ to make academic life easier.
