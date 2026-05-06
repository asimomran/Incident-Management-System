import requests
import time
import random
from concurrent.futures import ThreadPoolExecutor

URL = "https://crimson-mouse-e8ee.pc2accasim.workers.dev/signals"

components = ["RDBMS", "CACHE", "API"]

errors = {
    "RDBMS": ["connection timeout", "db down"],
    "CACHE": ["cache miss spike", "redis not responding"],
    "API": ["500 error", "latency spike"]
}

def send_signal():
    comp = random.choice(components)
    payload = {
        "component": comp,
        "error": random.choice(errors[comp]),
        "timestamp": time.strftime("%Y-%m-%dT%H:%M:%S")
    }

    response = requests.post(URL, json=payload)
    print(response.status_code)

# 🔥 burst 100 requests in parallel
with ThreadPoolExecutor(max_workers=50) as executor:
    for _ in range(100):
        executor.submit(send_signal)