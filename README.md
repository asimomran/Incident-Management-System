# Incident Management System (IMS)

Engineering Challenge: Mission-Critical Incident Management System

## Overview

This project is a lightweight event-driven Incident Management System designed to simulate how modern production systems ingest, process, track, and resolve high-volume infrastructure failures.

The system receives failure signals from different components such as APIs, caches, and databases, processes them asynchronously, groups related failures into incidents, stores raw telemetry separately, and exposes a workflow-driven dashboard for monitoring and RCA management.

---

# Architecture

```text
Signal Generator / React UI
        ↓
Cloudflare Worker (Edge Layer)
   ├── Lightweight Validation
   ├── Edge Rate Limiting
        ↓
Spring Boot Ingestion API
        ↓
Rate Limiter
        ↓
Async Queue (LinkedBlockingQueue)
        ↓
Worker Thread
        ↓
Processing Layer
   ├── Debounce
   ├── Alert Strategy
        ↓
Data Lake (signals.json)
        ↓
Incident Service
   ├── Incident Lifecycle
   ├── RCA Validation
   ├── MTTR Calculation
        ↓
In-Memory Incident Store
        ↓
Incident APIs
        ↓
React Dashboard
```

---

# Features

* Async signal ingestion using `LinkedBlockingQueue`
* Thread-safe rate limiting
* Event-driven processing pipeline
* Raw signal persistence using lightweight Data Lake (`signals.json`)
* Incident lifecycle management:

  * OPEN
  * INVESTIGATING
  * RESOLVED
  * CLOSED
* Mandatory RCA validation before incident closure
* MTTR (Mean Time To Repair) calculation
* React dashboard for incident monitoring
* Raw signal viewer page
* Cloudflare Worker edge ingestion layer
* Signal simulation using Python script

---

# Tech Stack

| Layer             | Technology                  |
| ----------------- | --------------------------- |
| Backend           | Spring Boot                 |
| Frontend          | React + Vite                |
| Queue             | LinkedBlockingQueue         |
| Data Lake         | JSON File Storage           |
| Edge Layer        | Cloudflare Workers          |
| Signal Simulation | Python                      |
| Rate Limiting     | Java Concurrency Primitives |
| Storage           | In-Memory Incident Store    |

---

# Why This Architecture

This project focuses on core distributed system concepts rather than CRUD operations.

Implemented concepts include:

| Feature           | Purpose                                  |
| ----------------- | ---------------------------------------- |
| Async Queue       | Handles burst traffic asynchronously     |
| Rate Limiter      | Prevents overload and cascading failures |
| Data Lake         | Separates raw telemetry from incidents   |
| MTTR              | Tracks operational repair metrics        |
| Cloudflare Worker | Adds edge-layer request handling         |
| Debouncing        | Prevents duplicate incident creation     |
| RCA Validation    | Enforces workflow integrity              |

---

# Project Structure

```text
ims-project/
├── backend/
├── frontend/
├── signal_generator.py
├── signals.json
└── README.md
```

---

# Running the Project

## 1. Start Backend

```bash
cd backend
./mvnw spring-boot:run
```

Windows:

```bash
mvnw.cmd spring-boot:run
```

Backend runs on:

```text
http://localhost:8080
```

---

## 2. Start Frontend

```bash
cd frontend
npm install
npm run dev
```

Frontend runs on:

```text
http://localhost:5173
```

---

## 3. Run Signal Generator

```bash
python signal_generator.py
```

This simulates failures from:

* RDBMS
* CACHE
* API

---

# API Endpoints

| Endpoint                 | Method | Description            |
| ------------------------ | ------ | ---------------------- |
| `/signals`               | POST   | Ingest signal          |
| `/incidents`             | GET    | Get all incidents      |
| `/incidents/{id}/status` | PUT    | Update incident status |
| `/incidents/{id}/rca`    | POST   | Submit RCA             |
| `/signals/raw`           | GET    | View raw signals       |

---

# Incident Workflow

```text
OPEN
   ↓
INVESTIGATING
   ↓
RESOLVED
   ↓
CLOSED
```

The system rejects closure if RCA is missing.

---

# MTTR

MTTR (Mean Time To Repair) is automatically calculated using:

```text
MTTR = end_time - start_time
```

* `start_time` → first signal received
* `end_time` → incident closure time

---

# Cloudflare Worker

The Cloudflare Worker acts as an edge ingestion layer before requests reach the backend.

Responsibilities:

* lightweight validation
* request forwarding
* edge rate limiting

Flow:

```text
Signal Generator
      ↓
Cloudflare Worker
      ↓
Spring Boot API
```

---

# Raw Signal Data Lake

All incoming signals are stored separately in:

```text
signals.json
```

This acts as a lightweight audit log for telemetry events.

---

# Future Improvements

* MySQL/PostgreSQL persistence
* Redis caching
* Kafka event streaming
* Distributed deployment
* Alert notification integrations
* Observability dashboards

---

# Author

GitHub:asimomran
