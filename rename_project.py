import os
import shutil

project_root = r"e:\Emo Games\EMOVibes"

exclude_dirs = {'.git', '.gradle', 'build', '.idea', 'gradle'}
exclude_extensions = {'.png', '.jpg', '.jpeg', '.gif', '.ico', '.webp', '.keystore', '.jar', '.aar', '.so'}

replacements = [
    ("com.mohit.emovibes", "com.mohit.emovibes"),
    ("com.mohit.emovibes", "com.mohit.emovibes"),
    ("EMOVibes", "EMOVibes"),
    ("emovibes", "emovibes"),
    ("Mohit", "Mohit"),
    ("Mohit", "Mohit"),
    ("Mohit", "Mohit")
]

# 1. Replace text in files
for root, dirs, files in os.walk(project_root):
    dirs[:] = [d for d in dirs if d not in exclude_dirs and not d.endswith('build')]
    
    for file in files:
        if any(file.endswith(ext) for ext in exclude_extensions):
            continue
            
        file_path = os.path.join(root, file)
        
        try:
            with open(file_path, 'r', encoding='utf-8') as f:
                content = f.read()
                
            new_content = content
            for old_str, new_str in replacements:
                new_content = new_content.replace(old_str, new_str)
                
            if new_content != content:
                with open(file_path, 'w', encoding='utf-8') as f:
                    f.write(new_content)
                print(f"Modified: {file_path}")
        except UnicodeDecodeError:
            pass
        except Exception as e:
            print(f"Error processing {file_path}: {e}")

# 2. Rename directories bottom-up
for root, dirs, files in os.walk(project_root, topdown=False):
    for d in dirs:
        if d in exclude_dirs or d.endswith('build'):
            continue
            
        dir_path = os.path.join(root, d)
        
        # Rename 'emovibes' to 'emovibes'
        if d == 'emovibes':
            new_dir_path = os.path.join(root, 'emovibes')
            os.rename(dir_path, new_dir_path)
            print(f"Renamed dir: {dir_path} -> {new_dir_path}")
            dir_path = new_dir_path
            d = 'emovibes'
            
        # Rename 'arturo254' to 'mohit'
        if d == 'arturo254':
            new_dir_path = os.path.join(root, 'mohit')
            os.rename(dir_path, new_dir_path)
            print(f"Renamed dir: {dir_path} -> {new_dir_path}")
