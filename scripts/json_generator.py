import csv
import json
from datetime import datetime

def generate_reports_and_logs(
    csv_path: str,
    report_json_path: str,
    log_json_path: str,
    # The default user as reporter
    user_names=('comp2100@anu.edu.au', 'comp6442@anu.edu.au'),
    description='User submits a new report.'
):
    """
    Read the csv files and generate the fake data persistant files in these path:
      1. report_json_path
      2. log_json_path
    """
    plates = []
    with open(csv_path, newline='', encoding='utf-8') as f:
        reader = csv.reader(f)
        for row in reader:
            if not row or row[0].startswith('#'):
                continue
            plates.append(row[0].strip())

    timestamp = datetime.now().strftime("%Y-%m-%dT%H:%M:%S")
    reports = []
    logs = []

    # Generate the fake lists for reports and logs
    for idx, plate in enumerate(plates, start=1):
        user_id = (idx - 1) % len(user_names)

        report = {
            "ID": idx,
            "carPlate": plate,
            "feedback": "",
            "location": "CHINA",
            "reportPicUrl": "/data/user/0/com.example.parkingreport/files/images/default_parking.png",
            "status": "WAIT_FOR_REVIEW",
            "timestamp": timestamp,
            "userId": user_id+1,
            "userName": user_names[user_id]
        }
        reports.append(report)

        log = {
            "description": description,
            "logId": idx,
            "reportId": idx,
            "timestamp": timestamp,
            "userId": user_id+1
        }
        logs.append(log)

    # Write the lists to files
    with open(report_json_path, 'w', encoding='utf-8') as f:
        json.dump(reports, f, indent=2, ensure_ascii=False)
    with open(log_json_path, 'w', encoding='utf-8') as f:
        json.dump(logs, f, indent=2, ensure_ascii=False)

if __name__ == '__main__':
    # Change the following path if needed.
    generate_reports_and_logs(
        csv_path=r'plate_phone_2500.csv',
        report_json_path=r'reports.json',
        log_json_path=r'report_logs.json'
    )
    print("Generated reports.json and report_logs.json")
