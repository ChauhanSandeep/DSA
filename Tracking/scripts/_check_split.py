 bimport json
import sys
from datetime import date
from pathlib import Path

sys.path.insert(0, "Tracking/scripts")
from _queue import pick_queue, coming_saturday, effective_difficulty

state = json.loads(Path("Tracking/data/state.json").read_text())
sat = coming_saturday(date.today())
q = pick_queue(state, sat, 10)


def lane(e):
    return "REVISION" if e["sm2"].get("lastReviewed") else "NEW"


counts = {"REVISION": 0, "NEW": 0}
print("Queue for %s (size=%d):" % (sat, len(q)))
for e in q:
    counts[lane(e)] += 1
    print("  [%-8s] %-26s %s" % (lane(e), e["task"], effective_difficulty(e)))
print("\nTotals -> NEW: %d, REVISION: %d" % (counts["NEW"], counts["REVISION"]))

