# SetswSage – Nail Business Analytics Platform 💅📊

SetswSage is a Java-based desktop analytics application designed to help independent nail technicians track appointments, purchases, and financial performance in one place.

The platform combines automated scheduling data with manually entered appointments and expenses, producing real-time business insights through an interactive GUI.

---

## ✨ Features
- Java Swing desktop interface with branded home screen
- Manual appointment & purchase entry via pop-up dialogs
- CSV ingestion from scheduling platforms (e.g. Acuity)
- Real-time analytics:
  - Service revenue
  - Deposits collected
  - Outstanding balances
  - Tips & estimated take-home
  - Supply spend & reinvestment rate
- Clean separation of concerns (UI, analytics, data, models)
- Privacy-safe sample datasets included

---

## 🛠 Tech Stack
- **Language:** Java (Java SE 21)
- **UI:** Swing (JFrame, JPanel, Dialogs)
- **Data:** CSV-based persistence
- **Architecture:** MVC-style separation
- **IDE:** Eclipse

---

## 📂 Project Structure
src/
├─ app/ # Application entry point
├─ ui/ # GUI panels & dialogs
├─ analytics/ # Business logic & metrics
├─ io/ # CSV readers/writers
└─ model/ # Appointment & Purchase models
data/
├─ *_SAMPLE.csv # Sample datasets (real data ignored)
---

## 🚀 How to Run
1. Open the project in Eclipse
2. Run `Main.java`
3. Use the UI to add appointments, purchases, and view analytics

---

## 🔒 Data Privacy
Real business CSV files are excluded via `.gitignore`.  
Only sample data is included in the repository.

---

## 📌 Future Improvements
- Charts & visual dashboards
- Monthly/weekly filtering
- Exportable PDF reports
- Database-backed storage

---

Built by **Sajal Sandhu**
