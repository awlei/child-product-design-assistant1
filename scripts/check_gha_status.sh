#!/bin/bash

RUN_ID=21576380732

echo "=== GitHub Actions Build Status ==="
echo ""

# 获取工作流运行信息
echo "Workflow Run Info:"
curl -s -H "Accept: application/vnd.github.v3+json" \
  "https://api.github.com/repos/awlei/new-child-product-design-assistant/actions/runs/$RUN_ID" | \
  python3 -m json.tool | grep -E '"id"|"name"|"status"|"conclusion"|"head_branch"'

echo ""
echo "=== Build Steps ==="

# 获取 Job 信息
JOB_ID=$(curl -s -H "Accept: application/vnd.github.v3+json" \
  "https://api.github.com/repos/awlei/new-child-product-design-assistant/actions/runs/$RUN_ID/jobs" | \
  python3 -c "import sys, json; print(json.load(sys.stdin)['jobs'][0]['id'])")

echo "Job ID: $JOB_ID"

# 获取步骤信息
curl -s -H "Accept: application/vnd.github.v3+json" \
  "https://api.github.com/repos/awlei/new-child-product-design-assistant/actions/jobs/$JOB_ID" | \
  python3 -c "import sys, json; data=json.load(sys.stdin); steps=data['steps']; [print(f\"{s['name']}: {s.get('status')} -> {s.get('conclusion', 'N/A')}\") for s in steps]"
