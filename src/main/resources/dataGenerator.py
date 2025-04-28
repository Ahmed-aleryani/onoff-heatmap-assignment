import random
import uuid
from datetime import datetime, timedelta

# Configuration for generating test data
days_to_generate = 3
# End date is April 28, 2025 (Europe/Tallinn timezone)
end_date = datetime(2025, 4, 28)

# Prepare arrays for dates, hours, and test user data
dates = [(end_date - timedelta(days=i)).strftime('%Y-%m-%d') for i in reversed(range(days_to_generate))]
hours = list(range(24))
usernames = ['alice', 'bob', 'carol', 'dave', 'eve']
onoff_numbers = ['100-000-0000', '200-000-0000', '300-000-0000', '400-000-0000', '500-000-0000']
contact_numbers = ['600-000-0000', '700-000-0000', '800-000-0000', '900-000-0000', '000-000-0000']
statuses = ['ANSWER', 'MISSED', 'ERROR']
incoming_flags = [0, 1]

# Generate data.sql
with open('data.sql', 'w') as f:
    # Optionally clear existing data
    f.write("DELETE FROM CALL_LOG;\n\n")
    for date in dates:
        for hour in hours:
            # Random number of calls for this hour
            num_calls = random.randint(1, 5)
            for _ in range(num_calls):
                call_id = str(uuid.uuid4())
                user_id = str(uuid.uuid4())
                username = random.choice(usernames)
                onoff = random.choice(onoff_numbers)
                contact = random.choice(contact_numbers)
                status = random.choice(statuses)
                incoming = random.choice(incoming_flags)
                duration = random.randint(30, 600)
                minute = random.randint(0, 59)
                second = random.randint(0, 59)
                start_ts = datetime.strptime(f"{date} {hour:02d}:{minute:02d}:{second:02d}", "%Y-%m-%d %H:%M:%S")
                end_ts = start_ts + timedelta(seconds=duration)
                start_str = start_ts.strftime("%Y-%m-%d %H:%M:%S.%f")[:-3]
                end_str = end_ts.strftime("%Y-%m-%d %H:%M:%S.%f")[:-3]

                f.write(
                    f"INSERT INTO CALL_LOG "
                    f"(ID, USER_ID, USERNAME, ONOFF_NUMBER, CONTACT_NUMBER, STATUS, INCOMING, DURATION, STARTED_AT, ENDED_AT) "
                    f"VALUES ('{call_id}', '{user_id}', '{username}', '{onoff}', '{contact}', '{status}', {incoming}, {duration}, "
                    f"'{start_str}', '{end_str}');\n"
                )

print("Generated data.sql with test data for:", ", ".join(dates))

# Also output the generated SQL file to stdout
with open('data.sql', 'r') as sql_file:
    print(sql_file.read())