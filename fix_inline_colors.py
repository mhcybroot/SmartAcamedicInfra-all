#!/usr/bin/env python3
"""
Script to replace all hardcoded light/white color values in inline <style> blocks
across all HTML templates with the Smart Academic green/gold theme colors.
"""

import os
import re

# Map of old color values → new theme colors
COLOR_REPLACEMENTS = [
    # White / near-white backgrounds
    (r'background:\s*rgba\(255,\s*255,\s*255,\s*0\.9\)', 'background: rgba(13, 31, 20, 0.9)'),
    (r'background:\s*rgba\(255,\s*255,\s*255,\s*0\.8\)', 'background: rgba(13, 31, 20, 0.8)'),
    (r'background:\s*rgba\(255,\s*255,\s*255,\s*0\.7\)', 'background: rgba(13, 31, 20, 0.7)'),
    (r'background:\s*rgba\(255,\s*255,\s*255,\s*0\.5\)', 'background: rgba(16, 185, 129, 0.06)'),
    (r'background:\s*rgba\(255,\s*255,\s*255,\s*0\.1\)', 'background: rgba(16, 185, 129, 0.08)'),
    (r'background:\s*rgba\(255,\s*255,\s*255,\s*0\.05\)', 'background: rgba(16, 185, 129, 0.04)'),
    (r'background-color:\s*rgba\(255,\s*255,\s*255,\s*[\d.]+\)', 'background-color: rgba(16, 185, 129, 0.06)'),
    (r"background:\s*#fff\b", 'background: #0f1e14'),
    (r"background:\s*#ffffff\b", 'background: #0f1e14'),
    (r"background-color:\s*#fff\b", 'background-color: #0f1e14'),
    (r"background-color:\s*#ffffff\b", 'background-color: #0f1e14'),
    (r"background:\s*white\b", 'background: #0f1e14'),
    (r"background-color:\s*white\b", 'background-color: #0f1e14'),
    (r"background:\s*#f8f9fa\b", 'background: rgba(16, 185, 129, 0.06)'),
    (r"background-color:\s*#f8f9fa\b", 'background-color: rgba(16, 185, 129, 0.06)'),
    (r"background:\s*#f5f7fa\b", 'background: #0b1120'),
    (r"background:\s*#e9ecef\b", 'background: rgba(16, 185, 129, 0.1)'),
    (r"background-color:\s*#e9ecef\b", 'background-color: rgba(16, 185, 129, 0.1)'),
    (r"background:\s*#dee2e6\b", 'background: rgba(16, 185, 129, 0.1)'),

    # Light text colors
    (r"color:\s*#6c757d\b", 'color: #6ee7b7'),
    (r"color:\s*#495057\b", 'color: #e2f5ed'),
    (r"color:\s*#343a40\b", 'color: #e2f5ed'),
    (r"color:\s*#212529\b", 'color: #e2f5ed'),
    (r"color:\s*#dee2e6\b", 'color: rgba(110, 231, 183, 0.5)'),
    (r"color:\s*#adb5bd\b", 'color: #6ee7b7'),

    # Blue primary to green
    (r"background:\s*linear-gradient\(135deg,\s*#0d6efd[^)]+\)", 'background: linear-gradient(135deg, #10b981 0%, #059669 100%)'),
    (r"rgba\(13,\s*110,\s*253,\s*([\d.]+)\)", r'rgba(16, 185, 129, \1)'),

    # Border colors
    (r"border:\s*1px solid\s*rgba\(255,\s*255,\s*255,\s*0\.5\)", 'border: 1px solid rgba(16, 185, 129, 0.25)'),
    (r"border:\s*1px solid\s*#dee2e6\b", 'border: 1px solid rgba(16, 185, 129, 0.2)'),
    (r"border-color:\s*#dee2e6\b", 'border-color: rgba(16, 185, 129, 0.2)'),
    (r"border:\s*1px solid\s*#ced4da\b", 'border: 1px solid rgba(16, 185, 129, 0.2)'),

    # Box shadows (light ones)
    (r"box-shadow:\s*0\s+8px\s+32px\s+rgba\(0,\s*0,\s*0,\s*0\.1\)", 'box-shadow: 0 8px 32px rgba(16, 185, 129, 0.15)'),
    (r"box-shadow:\s*0\s+12px\s+40px\s+rgba\(0,\s*0,\s*0,\s*0\.15\)", 'box-shadow: 0 12px 40px rgba(16, 185, 129, 0.25)'),
]

TEMPLATE_DIRS = [
    "Skylink-custom-backend/src/main/resources/templates",
    "Result-Management-System/src/main/resources/templates",
    "watch-employee/src/main/resources/templates",
]

BASE_DIR = "/home/mhcybroot/Projects/SmartAcamedicInfra"

def process_file(filepath):
    with open(filepath, 'r', encoding='utf-8', errors='ignore') as f:
        content = f.read()

    # Only process content INSIDE <style> tags
    style_pattern = re.compile(r'(<style[^>]*>)(.*?)(</style>)', re.DOTALL | re.IGNORECASE)
    original = content

    def replace_in_style(match):
        open_tag = match.group(1)
        style_content = match.group(2)
        close_tag = match.group(3)
        for pattern, replacement in COLOR_REPLACEMENTS:
            style_content = re.sub(pattern, replacement, style_content, flags=re.IGNORECASE)
        return open_tag + style_content + close_tag

    content = style_pattern.sub(replace_in_style, content)

    if content != original:
        with open(filepath, 'w', encoding='utf-8') as f:
            f.write(content)
        return True
    return False

total_modified = 0
for template_dir in TEMPLATE_DIRS:
    full_dir = os.path.join(BASE_DIR, template_dir)
    for root, dirs, files in os.walk(full_dir):
        for filename in files:
            if filename.endswith('.html'):
                filepath = os.path.join(root, filename)
                if process_file(filepath):
                    rel = os.path.relpath(filepath, BASE_DIR)
                    print(f"  ✅ Fixed: {rel}")
                    total_modified += 1

print(f"\n✨ Done! Modified {total_modified} template files.")
