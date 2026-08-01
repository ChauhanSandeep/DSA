import json, sys
from datetime import date, timedelta
from pathlib import Path

sys.path.insert(0, str(Path(__file__).resolve().parent))
from _queue import pick_queue, coming_saturday, count_due

state = json.loads((Path(__file__).resolve().parents[2] / "Tracking/data/state.json").read_text())
today = date.today()

graded = ["CombinationSum", "InsertInterval", "KokoEatingBananas",
          "ReverseNodesInKGroup", "TrappingRainWater"]

print("=== grades made today, their next eligibility ===")
for t in graded:
    p = state["problems"].get(t)
    if p:
        s = p["sm2"]
        print(f"  {t:22s} lastGrade={str(s.get('lastGrade')):8s} "
              f"interval={s.get('intervalDays'):>3}d  nextDue={s.get('nextDue')}")

sat = coming_saturday(today)
for wk in range(6):
    target = sat + timedelta(days=7 * wk)
    q = pick_queue(state, target, 6)
    total_due = count_due(state, target)
    tasks = [(e["task"], e["sm2"]["nextDue"]) for e in q]
    hits = [t for t, _ in tasks if t in graded]
    print(f"\nSaturday {target} — backlog due={total_due}; top-6:")
    for t, d in tasks:
        print(f"    {t:26s} nextDue={d}")
    print(f"    -> today's graded problems present? {hits or 'NO'}")

