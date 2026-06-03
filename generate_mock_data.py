import psycopg2
import random

# Database connection details for Skylink
DB_HOST = "localhost"
DB_PORT = "5432"
DB_NAME = "skylink_database"
DB_USER = "mhcybroot"
DB_PASS = "MhR@2025"

def generate_mock_employees():
    try:
        conn = psycopg2.connect(
            host=DB_HOST,
            port=DB_PORT,
            dbname=DB_NAME,
            user=DB_USER,
            password=DB_PASS
        )
        cur = conn.cursor()
        
        # Check if we already have employees
        cur.execute("SELECT count(*) FROM employee")
        count = cur.fetchone()[0]
        
        if count > 0:
            print(f"Database already has {count} academic members. Skipping generation.")
            return

        print("Generating mock Academic Members...")
        
        names = ["Dr. Alan Turing", "Prof. Marie Curie", "Dr. Albert Einstein", "Dr. Richard Feynman", "Prof. Ada Lovelace"]
        roles = ["Professor", "Associate Professor", "Lecturer", "Department Head", "Researcher"]
        
        for i in range(len(names)):
            emp_id = f"ACAD-{1000 + i}"
            name = names[i]
            role = roles[i]
            email = f"{name.split()[-1].lower()}@smartacademic.edu"
            salary = random.randint(50000, 120000)
            
            cur.execute("""
                INSERT INTO employee (id, name, role, email, monthly_salary, is_guest) 
                VALUES (%s, %s, %s, %s, %s, false)
                ON CONFLICT (id) DO NOTHING;
            """, (emp_id, name, role, email, salary))
        
        conn.commit()
        print("Mock data generated successfully!")
        
        cur.close()
        conn.close()
    except Exception as e:
        print(f"Error connecting or inserting into database: {e}")

if __name__ == "__main__":
    generate_mock_employees()
