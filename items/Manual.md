# User Manual - GPS Location Setup & Report Function

This app relies on GPS location data to determine whether a user is within a no-parking zone before allowing a report submission. Below are step-by-step instructions for using the app under both physical devices and emulator testing.

## 1. Enable GPS Permission (First Launch)

When the app launches for the first time, it will request location permission from the system.

- Please tap **"Allow"** when prompted, otherwise location-based reporting will not work.
- If you mistakenly denied it, go to:

  `Phone Settings → Apps → ParkReport → Permissions → Enable Location manually`

![GPSSetting](media/manual/gpsSetting.jpg)

## 2. GPS Setup in Emulator (for Testing)

If you're testing using an Android Emulator, follow the steps below to simulate your current GPS location:

1. Click the three dots (⋮) in the emulator toolbar to open **Extended Controls**.
2. Go to **Location > Search** tab.
3. For testing a valid parking zone, input:

   ```
   -35.285242, 149.123287
   ```

   (not an illegal parking area; will not appear red on the map;will not be allowed to report)

   Feel free to set any point that is not inside the red zone.

4. For testing an illegal parking zone, input:

   ```
   -35.280185, 149.131032
   ```

   (a designated illegal parking area; will appear red;will be allowed to report)

   Feel free to set any point inside the red zone.

5. Click **“Set Location”**, restart the app — the emulator’s GPS will be updated.

## 3. Using the Report Feature (Main Function)

Reporting is only available when you are located inside a no-parking zone.

### To test reporting inside a no-parking zone:

- Set emulator location to `-35.280185, 149.131032`
- Log in with your assigned credentials
- Go to the second tab (Report Page) via bottom navigation
- Wait 3–5 seconds for the map to center to your location
- If inside a red zone, take a photo and submit

### To test in a valid parking zone:

- Change location to `-35.285242, 149.123287`
- Tap the “Report” button
- Expected message:

  ```
  Not an illegal parking area, unable to report.
  ```

## 4. Expected Map Behavior

- Cold start: map loads in 3–5 seconds
- Warm start: instantly loads with last GPS; if changed, reloads in ~3s
- Red zones: clickable polygons with location name via toast
- Blue dot: current location
- Outside illegal zones: report disabled
- Map supports pinch zoom

# User Manual - Login & Registration Functionality

## 1. Predefined Login Accounts

| Role  | Username               | Password  |
|-------|------------------------|-----------|
| User  | comp2100@anu.edu.au    | comp2100  |
| User  | comp6442@anu.edu.au    | comp6442  |
| Admin | admin                  | admin     |

> Admin account is preconfigured and cannot be signed up manually.

## 2. Registering a New User

- Tap **“Don't have an account? Sign up”**
- Fill in:
    - A new username (e.g. email)
    - A secure password
- Tap **Register**
- After success, return to login and sign in

## 3. Login Confirmation

- Username and avatar shown after login
- View based on user role (User/Admin)
- Admins redirected to dashboard

## 4. Changing Your Password

- Go to **Profile/Settings**
- Enter a new password in **Change Password**
- Tap **Change Password**
- Confirmation message shown
- Re-login to verify

# Admin Manual – Report Search Function (Review Tab)

Admins can filter through 2,500+ reports using a search bar in the Review Tab.

## Search Syntax

| Goal                         | Syntax                          |
|------------------------------|----------------------------------|
| Search by user               | `u: user@example.com`            |
| Search by car plate          | `p: ABC123`                      |
| Search by user and plate     | `u: user@example.com + p: ABC123`|

> Use `+` to separate filters. Spacing is flexible.

## Instructions

1. Log in as admin
2. Go to **Review** tab
3. Use the **Search bar**
4. Enter your query
5. Tap yellow **Search**
6. Results refresh
7. Tap **View Details** for:

    - Image
    - Timestamp
    - Status
    - User info
    - Feedback

## Example Use Cases

| Task                            | Input                                | Outcome                                 |
|---------------------------------|---------------------------------------|------------------------------------------|
| Reports by Jane                | `u: jane.doe@gmail.com`              | All Jane's reports                       |
| Reports for plate "T9988Y"     | `p: T9988Y`                          | All matching reports                     |
| Jane’s report on "T9988Y"      | `u: jane.doe@gmail.com + p: T9988Y` | Only Jane’s report on that plate         |

# Admin Manual – Report Review and Moderation

## Tabs in Admin Interface

| Tab      | Description                             |
|----------|-----------------------------------------|
| Unreview | Reports pending review                  |
| Review   | Reports already reviewed                |

## Reviewing a Report

1. Go to **Unreview** tab
2. Tap a report
3. View:

    - Plate
    - Location
    - Time
    - Reporter
    - Status (`WAIT_FOR_REVIEW`)
    - Feedback box

4. Choose action:

    - **APPROVE** – Accept
    - **REJECT** – Deny
    - Optional: Add feedback

5. Tap **Submit**

## What Happens After?

- Report moves to **Reviewed** tab
- User sees status change to:

    - `APPROVED` or `REJECTED`
    - Shown in **My Reports**

## Example Flow

| Step                    | Result                                     |
|-------------------------|---------------------------------------------|
| Admin opens `ED666`     | Submitted by `comp2100@anu.edu.au`         |
| Admin approves          | Status becomes `APPROVED`                  |
| Admin rejects `T9988Y`  | Status becomes `DECLINED`                  |
| User refreshes app      | Sees final status in their submission card |

# Admin Manual – SMS Notification on Approval

When a report is **approved** by an administrator, the system sends an **SMS notification** to the owner of the reported vehicle.

## SMS Content

```
Dear owner of ${plate},

Your vehicle has been reported for illegal parking. Please take immediate action to resolve this issue to avoid penalties or towing.

If you did not initiate this request, please disregard this message.

Thank you for your attention.

— Parking Report System
```

## How to Test This Feature

To test the SMS notification functionality:

1. Open the `plate_phone_2500.csv` file located in:

   ```
   app/src/main/assets/plate_phone_2500.csv
   ```

   Or access it via GitLab:  
   [plate_phone_2500.csv](https://gitlab.cecs.anu.edu.au/u7937030/gp-25s1/-/blob/main/app/src/main/assets/plate_phone_2500.csv?ref_type=heads)

2. Add your **own phone number** to one of the vehicle plate entries.

3. Use the **User account** to report that specific plate number.

4. Log in as **Admin**, review the report, and tap **APPROVE**.

5. Once approved, you should receive the SMS notification on the phone number associated with the plate.

![SMSnotification](media/manual/SMSnotification.jpg)

